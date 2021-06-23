package com.christina1701.plugins.filechooser;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.getcapacitor.JSObject;
import com.getcapacitor.NativePlugin;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;

@NativePlugin(
    requestCodes = {FileChooserPlugin.REQUEST_DOCUMENTS}
)
public class FileChooserPlugin extends Plugin {
    protected static final int REQUEST_DOCUMENTS = 666;

    @PluginMethod
    public void openDocuments(PluginCall call) {
        saveCall(call);

        pluginRequestPermissions(new String[] {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, REQUEST_DOCUMENTS);
    }

    @Override
    protected void handleRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.handleRequestPermissionsResult(requestCode, permissions, grantResults);

        PluginCall savedCall = getSavedCall();

        if (savedCall == null) {
            Log.d("FileChooserPlugin: ","No stored plugin call for permissions request result");
            return;
        }

        for(int result : grantResults) {
            if (result == PackageManager.PERMISSION_DENIED) {
                savedCall.error("User denied permission");
                return;
            }
        }

        if (requestCode == REQUEST_DOCUMENTS) {
            // We got the permission
            openGallery(savedCall);
        }
    }

    protected void openGallery(PluginCall call) {
        Intent intent;

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        } else {
            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.INTERNAL_CONTENT_URI);
        }

        // want to specify type? e.g. intent.setType("video/*"); or "*/*" to allow every type
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra("return-data", true);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(call, intent, REQUEST_DOCUMENTS);
    }

    // in order to handle the intents result, we have to @Override handleOnActivityResult
    @Override
    protected void handleOnActivityResult(int requestCode, int resultCode, Intent data) {
        super.handleOnActivityResult(requestCode, resultCode, data);

        // Get the previously saved call
        PluginCall savedCall = getSavedCall();

        if (savedCall == null) {
            return;
        }

        if (requestCode == REQUEST_DOCUMENTS) {
            JSObject ret = new JSObject();

            // user did not select anything
            if (data == null) {
                ret.put("uri", "null");
                savedCall.success(ret);
                return;
            }

            Uri uri = data.getData();
            String returnUri = "";
            if (uri != null) {
                returnUri = uri.toString();
                ret.put("uri", returnUri);
                savedCall.success(ret);
            } else {
                savedCall.reject("An error occurred - Could not get URI");
            }
        }
    }
}
