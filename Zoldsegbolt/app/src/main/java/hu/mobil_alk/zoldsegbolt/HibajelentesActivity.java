package hu.mobil_alk.zoldsegbolt;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class HibajelentesActivity extends AppCompatActivity {
    private boolean keszultKep = false;

    private final int REQUEST_CODE_ASK_PERMISSIONS = 123;
    int tag = 1;
    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hibajelentes);

        imageView = (ImageView) findViewById(R.id.kep);

    }

    public void kuldes(View view) {
        if (keszultKep){
            Toast.makeText(this, "Hibajelentés elküldve", Toast.LENGTH_SHORT).show();
            finish();
        }else {
            Toast.makeText(this, "Nem készült kép", Toast.LENGTH_SHORT).show();
        }
    }

    public void cancel(View view) {
        finish();
    }


    public void openCamera(View view) {
        checkPermission();
    }

    void checkPermission(){
        if (Build.VERSION.SDK_INT >= 23){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_PERMISSIONS);
                return;
            }
        }

        takePicture();

    }

    private void takePicture() {
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, tag);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePicture();
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == tag && resultCode == RESULT_OK){
            Bundle b = data.getExtras();
            Bitmap img = (Bitmap) b.get("data");
            imageView.setImageBitmap(img);
            keszultKep = true;
        }
    }
}