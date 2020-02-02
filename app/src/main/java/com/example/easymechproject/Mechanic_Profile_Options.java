package com.example.easymechproject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class Mechanic_Profile_Options extends AppCompatActivity {
    private TextView call_up;
    private TextView search_loc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanic__profile__options);
    }
    public void map_from_here(View v){
        search_loc = (TextView)findViewById(R.id.user_addresses);
        String LOCATING = search_loc.getText().toString();

        Uri gmmIntentUri = Uri.parse("geo:0,0?q="+LOCATING);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    public void appoint_from_here(View v){
        startActivity(new Intent(Mechanic_Profile_Options.this, Appointments.class));
    }

    public void call_from_here(View v){
        call_up = (TextView) findViewById(R.id.mechanic_phone1);

        String TEL_NUM = call_up.getText().toString();
        Intent intentCall = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", TEL_NUM, null));

        if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED)
        {
            Toast.makeText(getApplicationContext(), "Please grant permission to call", Toast.LENGTH_SHORT).show();
            requestPermission();
        }
        else
        {
            startActivity(intentCall);
        }

    }
    private void requestPermission(){
        ActivityCompat.requestPermissions(Mechanic_Profile_Options.this, new String[]{Manifest.permission.CALL_PHONE},1);
    }
}
