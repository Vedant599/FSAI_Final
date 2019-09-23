package com.example.helpdroid1223;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class EmergencyContactsActivity extends AppCompatActivity {
    public ImageButton panic;
    private FusedLocationProviderClient client;
    String loc;
    int permission_check1,permission_check3,permission_check2;
    final private int request_code=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_contacts);
        client = LocationServices.getFusedLocationProviderClient(this);

        panic = (ImageButton) findViewById(R.id.panicbtn);
        panic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 permission_check1 = ContextCompat.checkSelfPermission(EmergencyContactsActivity.this,Manifest.permission.CALL_PHONE);
                 permission_check2=ContextCompat.checkSelfPermission(EmergencyContactsActivity.this,Manifest.permission.SEND_SMS);
                 permission_check3 = ContextCompat.checkSelfPermission(EmergencyContactsActivity.this,Manifest.permission.ACCESS_FINE_LOCATION);


                mycall();
                mymessage();
            }
        });


    }

    private void mycall() {
        final String number ="+919324810612";
        permission_check1 = ContextCompat.checkSelfPermission(this,Manifest.permission.CALL_PHONE);
        if(permission_check1==PackageManager.PERMISSION_GRANTED) {

            startActivity(new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+number)));
        }
        else
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE},1);
        }
    }

    private void mymessage() {

         permission_check2=ContextCompat.checkSelfPermission(this,Manifest.permission.SEND_SMS);

        if(permission_check2==PackageManager.PERMISSION_GRANTED)
        {

            sendmessage();
        }
        else
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},0);
        }
    }

    private void sendmessage() {
         String number ="+919324810612";

         String message =myLocation();
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
         if(number!="")
         {
         SmsManager smsManager =SmsManager.getDefault();
         smsManager.sendTextMessage(number,null,message,null,null);
        Toast.makeText(this, "Message Sent", Toast.LENGTH_SHORT).show();
    }}

    private String myLocation() {

        int permission_check3 = ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION);
        if(permission_check3==PackageManager.PERMISSION_GRANTED) {
            client.getLastLocation().addOnSuccessListener(EmergencyContactsActivity.this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                     loc=location.toString();

                }
            });
        }
        else
        {
            ActivityCompat.requestPermissions(EmergencyContactsActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},2);
             loc ="Hello";
        }
        return loc;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Toast.makeText(this, "Message Sent", Toast.LENGTH_SHORT).show();
        switch(requestCode)
        {

            case 0:
                if(grantResults.length>=0 && grantResults [0]==PackageManager.PERMISSION_GRANTED)
                {
                    sendmessage();
                }
                else
                {
                    Toast.makeText(this, "You Dont Have Permission to Send Message", Toast.LENGTH_SHORT).show();
                }
                break;
            case 1:
                if(grantResults.length>=0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    mycall();
                }
                else
                {
                    Toast.makeText(this, "You Dont Have Permission to call", Toast.LENGTH_SHORT).show();
                }
            case 2:
                if(grantResults.length>=0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    myLocation();
                }
                else
                {
                    Toast.makeText(this, "You Dont Have Permission to access location", Toast.LENGTH_SHORT).show();
                }
        }
    }
}