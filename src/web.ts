import { WebPlugin } from '@capacitor/core';
import { FileChooserPluginPlugin } from './definitions';

export class FileChooserPluginWeb extends WebPlugin implements FileChooserPluginPlugin {
  constructor() {
    super({
      name: 'FileChooserPlugin',
      platforms: ['web'],
    });
  }

  async openDocuments(): Promise<{ uri: string }> {
    console.log('open Documents reached');
    return { uri: '' };
  }
}

const FileChooserPlugin = new FileChooserPluginWeb();

export { FileChooserPlugin };

import { registerWebPlugin } from '@capacitor/core';
registerWebPlugin(FileChooserPlugin);
