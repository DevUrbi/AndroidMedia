package eus.urbieta.multimedia;

import android.content.Intent;

import java.io.File;
import java.text.SimpleDateFormat;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class camera_default_example extends AppCompatActivity {
    private final int PHOTO_RESULT = 1;
    private Uri mLastPhotoURI = null;
    private ImageView mPhotoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_default_example);

        mPhotoView = (ImageView) findViewById( R.id.photoView );
    }

    private Uri createFileURI(){
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmssSS").format( System.currentTimeMillis() );
        String fileName = String.format("PHOTO_%s.jpg", timeStamp);
        return Uri.fromFile( new File( Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_PICTURES ), fileName ) );
    }

    public void takePhoto(View view) {
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if( takePhotoIntent.resolveActivity( getPackageManager() ) != null ){ // Detectar si hay una app para el intent
            mLastPhotoURI = createFileURI();
            takePhotoIntent.putExtra( MediaStore.EXTRA_OUTPUT, mLastPhotoURI );
            startActivityForResult( takePhotoIntent, PHOTO_RESULT );
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ( requestCode == PHOTO_RESULT && resultCode == RESULT_OK ){
            mPhotoView.setImageBitmap( BitmapFactory.decodeFile( mLastPhotoURI.getPath() ) );
        }
    }
}
