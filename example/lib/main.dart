import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:pdf_viewer/pdf_viewer.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: PdfViewerWidget(
          completeCallback: (bool result){
            print("completeCallback,result:${result}");
          },
          initialUrl: "https://ifp-test.oss-cn-shenzhen.aliyuncs.com/file/1e22e8b4-9f52-4992-8cf9-22d1da0ea3cc.pdf",
//          child: Text('Running on: $_platformVersion\n'),
        ),
      ),
    );
  }
}
