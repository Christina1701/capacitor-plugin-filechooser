import { Plugins } from '@capacitor/core';

const { FileChooserPlugin } = Plugins;

export class FileChooser {
  openDocuments() {
    return FileChooserPlugin.openDocuments();
  }
}
