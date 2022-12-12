package com.ov.video.texture_layer;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.Surface;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;


import java.util.ArrayList;
import java.util.Arrays;

import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.view.TextureRegistry;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class CameraLayer {

    private Handler backgroudHandler;
    Context context;

    public CameraLayer(Context context) {
        backgroudHandler=new Handler();
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void createTexture(FlutterEngine engine, MethodChannel.Result result){

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

            CameraManager cameraManager=(CameraManager) context.getSystemService(context.CAMERA_SERVICE);
            if(ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
                result.success(-1);
                ActivityCompat.requestPermissions((Activity)context,new String[]{Manifest.permission.CAMERA},1);
            }

            TextureRegistry.SurfaceTextureEntry entry=engine.getRenderer().createSurfaceTexture();
            SurfaceTexture surfaceTexture=entry.surfaceTexture();
            Surface surface=new Surface(surfaceTexture);

            try {
                cameraManager.openCamera(
                        "0",
                        new CameraDevice.StateCallback() {
                            @Override
                            public void onOpened(@NonNull CameraDevice camera) {

                                result.success(entry.id());
                                CaptureRequest.Builder builder=null;

                                try {
                                    builder=camera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                                    builder.addTarget(surface);

                                    startPreview(result,camera,surface,builder,backgroudHandler);
                                } catch (CameraAccessException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onDisconnected(@NonNull CameraDevice camera) {

                            }

                            @Override
                            public void onError(@NonNull CameraDevice camera, int error) {

                            }
                        },backgroudHandler
                );
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void startPreview(MethodChannel.Result result, CameraDevice cameraDevice, Surface surface, CaptureRequest.Builder builder, Handler handler){

        try {
            cameraDevice.createCaptureSession(
                    Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
                        @Override
                        public void onConfigured(@NonNull CameraCaptureSession session) {

                            Log.i("CLxx:","Start preview");
                            try {
                                session.setRepeatingRequest(
                                        builder.build(),null,handler
                                );

                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                            Log.i("CLxx:","Start preview Failed");

                        }
                    },backgroudHandler
            );
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
}
