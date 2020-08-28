declare module '@capacitor/core' {
  interface PluginRegistry {
    FileChooserPlugin: FileChooserPluginPlugin;
  }
}

export interface FileChooserPluginPlugin {
  openDocuments(): Promise<{ uri: string }>;
}
