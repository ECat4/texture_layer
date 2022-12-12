import 'dart:async';

import 'package:flutter/services.dart';

class TextureLayer {
  static const MethodChannel _channel = MethodChannel('texture_layer');

  static Future<int?> get createTexture async {
    final int? code = await _channel.invokeMethod('createTexture');
    return code;
  }
}
