package com.liyuanhust.pdfviewer

import android.content.Context
import android.view.View
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.platform.PlatformView
import com.github.barteksc.pdfviewer.PDFView as LocalPdfView


class PdfView: PlatformView, MethodChannel.MethodCallHandler {
    companion object {
        const val CHANNEL = "com.liyuanhust.flutter/pdfviewer"
    }

    private val localPdfView:LocalPdfView
    private val methodChannel: MethodChannel
    private var fileLoader: FileLoader? = null
    private var disposed = false

    constructor(context: Context, messenger: BinaryMessenger, id: Int, params: Map<String, Any>?){
        localPdfView = LocalPdfView(context, null)
        methodChannel = MethodChannel(messenger, "${CHANNEL}_$id")
        methodChannel.setMethodCallHandler(this)

        if (params?.containsKey("initialUrl") == true) {
            val url = params["initialUrl"] as String
            fileLoader = FileLoader(context.applicationContext)
            fileLoader?.startDownload(url) {file->
                val res =
                if (file != null && !disposed) {
                    localPdfView.fromFile(file)
                            .load()
                    true
                } else false
                methodChannel.invokeMethod("loadComplete", hashMapOf("result" to  res))
            }
        }

    }


    override fun getView(): View {
        return localPdfView
    }

    override fun dispose() {
        methodChannel.setMethodCallHandler(null)
        disposed = true
        fileLoader?.cancel()
        localPdfView.recycle()
    }

    override fun onMethodCall(p0: MethodCall?, p1: MethodChannel.Result?) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}