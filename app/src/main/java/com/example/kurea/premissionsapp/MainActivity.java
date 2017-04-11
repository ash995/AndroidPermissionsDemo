package com.example.kurea.premissionsapp;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button callButton = (Button) findViewById(R.id.callButton);
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //CHECK WHETHER THE ANDROID IS ABOVE MARSHMELLOW OR NOT
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //IF THESE TWO ARE NOT EQUAL THEN WE DONT HAVE THE PERMISSION
                    if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        //THIS WILL RETURN TRUE IF THE USER HAS ALREADY DENIED PERMISSION BEFORE
                        if(shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)){
                            //WE CREATE AN ALERT DIALOG EXPLAINING THE USER WHY WE NEED THE PERMISSION
                            new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("Call Permission")
                                    .setMessage("Hi there! We can't call anyone without the call permission, could you please grant it?")
                                    .setPositiveButton("Yep", new DialogInterface.OnClickListener() {
                                        @RequiresApi(api = Build.VERSION_CODES.M)
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, PERMISSION_REQUEST_CODE);
                                        }
                                    })
                                    .setNegativeButton("No thanks", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Toast.makeText(MainActivity.this, "Denied", Toast.LENGTH_LONG).show();
                                        }
                                    }).show();
                        }
                        else
                            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, PERMISSION_REQUEST_CODE);
                    }
                }
                else {
                    //if device is running on less than marshmellow than do it directly, and we wont have to do anything explicitly
                    //to access a dangerous permission
                    makeCall();
                }

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //check if request code equal to the code we passed
        if (requestCode == PERMISSION_REQUEST_CODE) {
            //first if we check the length of grantResults and if it is greater than 0, then we have some granted permission
            //and then we check whether the permission we requested for is in the PERMISSION_GRANTED variable
            //we use 0 index because we granted only one permission
            if (grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makeCall();
            }
        }
    }

    private void makeCall() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + "12345678"));
        startActivity(intent);
    }
}
