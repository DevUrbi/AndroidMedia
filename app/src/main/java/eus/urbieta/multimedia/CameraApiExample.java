package eus.urbieta.multimedia;

import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageButton;

import java.util.Arrays;
import java.util.Comparator;

// Requiere minimo api 21 para usar la api 2 de camara de android
public class CameraApiExample extends AppCompatActivity {

    private CameraDevice mCameraDevice;
    private CaptureRequest.Builder mCaptureRequestBuilder;
    private CameraCaptureSession mCameraCaptureSession;
    private TextureView mCameraView;
    private Size mPreviewSize;
    private CameraDevice.StateCallback mStateCallBack = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            mCameraDevice = camera;
            SurfaceTexture texture = mCameraView.getSurfaceTexture();
            if( texture == null ){
                return;
            }
            texture.setDefaultBufferSize( mPreviewSize.getWidth(), mPreviewSize.getHeight() );
            Surface surface = new Surface( texture);

            try {
                mCaptureRequestBuilder = mCameraDevice.createCaptureRequest( CameraDevice.TEMPLATE_PREVIEW );
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
            mCaptureRequestBuilder.addTarget( surface );

            try {
                mCameraDevice.createCaptureSession( Arrays.asList(surface), mPreviewStateCallBack, null );
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {

        }

        @Override
        public void onError( CameraDevice camera, int error){

        }
    };
    private TextureView.SurfaceTextureListener mSurfaceListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            openCamera();
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {

        }
    };
    private CameraCaptureSession.StateCallback mPreviewStateCallBack = new CameraCaptureSession.StateCallback() {
        @Override
        public void onConfigured(@NonNull CameraCaptureSession session) {
            startPreview(session);
        }

        @Override
        public void onConfigureFailed(@NonNull CameraCaptureSession session) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_api_example);

        mCameraView = (TextureView) findViewById( R.id.camera_view );
        mCameraView.setSurfaceTextureListener( mSurfaceListener );
    }

    public void takePicture(View view) {
    }

    static class CompareSizesByArea implements Comparator<Size>{
        @Override
        public int compare(Size lhs, Size rhs) {
            return ( lhs.getWidth() * lhs.getWidth() ) - ( rhs.getWidth() * rhs.getHeight() );
        }

    }
}
