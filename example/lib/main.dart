import 'package:flutter/material.dart';
import 'package:simple_pdf_viewer/simple_pdf_viewer.dart';

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
        body: SimplePdfViewerWidget(
          completeCallback: (bool result){
            print("completeCallback,result:${result}");
          },
          initialUrl: "https://www.orimi.com/pdf-test.pdf",
        ),
      ),
    );
  }
}
