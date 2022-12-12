#import "TextureLayerPlugin.h"
#if __has_include(<texture_layer/texture_layer-Swift.h>)
#import <texture_layer/texture_layer-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "texture_layer-Swift.h"
#endif

@implementation TextureLayerPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftTextureLayerPlugin registerWithRegistrar:registrar];
}
@end
