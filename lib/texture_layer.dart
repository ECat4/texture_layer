import 'dart:async';

import 'package:flutter/services.dart';

class TextureLayer {
  static const MethodChannel _channel = MethodChannel('texture_layer');

  static Future<String?> get createTexture async {
    final String? code = await _channel.invokeMethod('createTexture');
    return code;
  }
}
