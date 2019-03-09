#import "PdfViewerPlugin.h"
#import <pdf_viewer/pdf_viewer-Swift.h>

@implementation PdfViewerPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftPdfViewerPlugin registerWithRegistrar:registrar];
}
@end
