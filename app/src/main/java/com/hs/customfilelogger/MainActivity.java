package com.hs.customfilelogger;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.security.Permissions;

public class MainActivity extends AppCompatActivity {

    private static final int WRITE_EXTERNAL_STORAGE_PERMISSION = 1001;
    MyLogger logger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * Put the below piece of setup in launcher activity.
         */
        checkForWritePermission();


        logger = MyLogger.getInstance(MainActivity.class.getSimpleName());
        logger.d("Activity onCreate");
    }

    @Override
    protected void onResume() {
        super.onResume();
        logger.d("Activity onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        logger.d("Activity onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        logger.d("Activity onDestroy");
    }

    private void checkForWritePermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "External storage permission is required to log the messages", Toast.LENGTH_LONG).show();
                checkForWritePermission();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        WRITE_EXTERNAL_STORAGE_PERMISSION);
            }
        } else {
            //Setup the log folder and file
            MyLogger.setup(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case WRITE_EXTERNAL_STORAGE_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // Once the permission is granted
                    // Setup the log folder and file
                    MyLogger.setup(this);

                } else {
                    Toast.makeText(this,"App will be unable to write logs",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
