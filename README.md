# simple_pdf_viewer

A very simple flutter plugin project. Just show pdf from remote url on android/ios, support embedded widget.


## Features

On Android, first it use okhttp to download file to cache folder, then use  built on [AndroidPdfViewer](https://github.com/barteksc/AndroidPdfViewer) to load file on local.

On IOS, it just use embedded [webview](https://github.com/flutter/plugins/blob/master/packages/webview_flutter/lib/webview_flutter.dart) to load url.

## Note
Add this to ios/Runner/info.plist to support embedded widget
```
<key>io.flutter.embedded_views_preview</key>
<true/>
```

If your pdf url is http not https, add this to
```
<key>NSAppTransportSecurity</key>
    <dict>
        <key>NSAllowsArbitraryLoads</key>
    <true/>
</dict>
```

Add follow to android/app/build.gradle
```
 release {
            ndk {
                abiFilters "armeabi-v7a", "x86"
            }
        }
```



If you have some android compile error about  android support-v4 or AndroidX. It may caused by android webview plugin, we could remove the plugin to fix.

1. add this config  to android/app/build.gradle top level
```
configurations{
    all*.exclude module: "webview_flutter"
}
```
2. create dir io.flutter.plugins.webviewflutter, create file WebViewFlutterPlugin.java with follow content
```
package io.flutter.plugins.webviewflutter;

import io.flutter.plugin.common.PluginRegistry;

public class WebViewFlutterPlugin {
    public static void registerWith(PluginRegistry.Registrar registrar) {
        //Do nothing
    }
}

```



## Install

simple_pdf_viewer: ^0.1.5

License: Apache 2.0



## Thanks

-   [AndroidPdfViewer](https://github.com/barteksc/AndroidPdfViewer)
-   [flutter_pdf_viewer](https://github.com/pycampers/flutter_pdf_viewer).


## Screenshot
![image](https://github.com/liyuanhust/flutter_simple_pdf_viewer/blob/master/document/2.png)


