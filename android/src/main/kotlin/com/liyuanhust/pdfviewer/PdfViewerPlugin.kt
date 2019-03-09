package com.liyuanhust.pdfviewer

import io.flutter.plugin.common.PluginRegistry.Registrar

class PdfViewerPlugin {
  companion object {
    @JvmStatic
    fun registerWith(registrar: Registrar) {
      registrar.platformViewRegistry()
              .registerViewFactory(PdfView.CHANNEL, PdfViewerFactory(registrar.messenger()))
    }
  }
}
