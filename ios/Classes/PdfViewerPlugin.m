#import "PdfViewerPlugin.h"
#import <simple_pdf_viewer/simple_pdf_viewer-Swift.h>

@implementation PdfViewerPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftPdfViewerPlugin registerWithRegistrar:registrar];
}
@end
