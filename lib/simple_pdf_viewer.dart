import 'dart:async';

import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';
import 'package:flutter/widgets.dart';
import 'package:webview_flutter/webview_flutter.dart';

typedef LoadCompleteCallback = void Function(bool res);

class SimplePdfViewerWidget extends StatelessWidget {
  /// The initial URL to load.
  final String initialUrl;
  final LoadCompleteCallback completeCallback;

  const SimplePdfViewerWidget({
    Key key,
    this.initialUrl,
    this.completeCallback
  }) :super(key: key);

  @override
  Widget build(BuildContext context) {
    if (defaultTargetPlatform == TargetPlatform.iOS) {
      return WebView(
          initialUrl: initialUrl,
          javascriptMode:JavascriptMode.unrestricted);
    } else if (defaultTargetPlatform == TargetPlatform.android) {
      return _AndroidPdfWidget(
          initialUrl: initialUrl,
          completeCallback: completeCallback
      );
    }
    return Text(
        '$defaultTargetPlatform is not yet supported by the pdf_viewer plugin');
  }
}

class _AndroidPdfWidget extends StatefulWidget{
  /// The initial URL to load.
  final String initialUrl;
  final LoadCompleteCallback completeCallback;

  const _AndroidPdfWidget({
    Key key,
    this.initialUrl,
    this.completeCallback
  }) :super(key: key);

  @override
  State<StatefulWidget> createState() => _AndroidPdfWidgetState();
}

class _AndroidPdfWidgetState extends State<_AndroidPdfWidget>{

  @override
  Widget build(BuildContext context) {
    return AndroidView(
        viewType: 'com.liyuanhust.flutter/pdfviewer',
        creationParams: _CreationParams.fromWidget(widget).toMap(),
        creationParamsCodec: const StandardMessageCodec(),
        onPlatformViewCreated: this._onPlatformViewCreated);
  }

  void _onPlatformViewCreated(int id) {
    final _channel = MethodChannel('com.liyuanhust.flutter/pdfviewer_$id');
    _channel.setMethodCallHandler(this._onMethodCall);
  }

  Future<bool> _onMethodCall(MethodCall call) async {
    switch (call.method) {
      case 'loadComplete':
        final bool res = call.arguments['result'];
        this.widget.completeCallback?.call(res);
        return true;
    }
    throw MissingPluginException(
        '${call.method} was invoked but has no handler');
  }
}

class _CreationParams {
  _CreationParams({this.initialUrl});

  static _CreationParams fromWidget(_AndroidPdfWidget widget) {
    return _CreationParams(
      initialUrl: widget.initialUrl,
    );
  }

  final String initialUrl;

  Map<String, dynamic> toMap() {
    return <String, dynamic>{
      'initialUrl': initialUrl
    };
  }
}