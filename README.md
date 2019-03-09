# simple_pdf_viewer

A very simple flutter plugin project. Just show pdf from remote url on android/ios, support embedded widget.


## Features

On Android, first it use okhttp to download file to cache folder, than use  built on [AndroidPdfViewer](https://github.com/barteksc/AndroidPdfViewer) to load file on local.

On IOS, it just use embedded [webview](https://github.com/flutter/plugins/blob/master/packages/webview_flutter/lib/webview_flutter.dart) to load url.

## Note
Add io.flutter.embedded_views_preview=true info.plist to support embedded widget





## Install

simple_pdf_viewer: ^0.1.1

License: Apache 2.0



## Thanks

-   [AndroidPdfViewer](https://github.com/barteksc/AndroidPdfViewer)
-   [flutter_pdf_viewer](https://github.com/pycampers/flutter_pdf_viewer).


