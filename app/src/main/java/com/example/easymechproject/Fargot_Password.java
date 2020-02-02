package com.example.easymechproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Fargot_Password extends AppCompatActivity {
    Button btn_es;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("message");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fargot__password);

        btn_es = (Button) findViewById(R.id.yes_confirm);

        btn_es.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "That's too bad! Go home.",Toast.LENGTH_LONG).show();
                myRef.setValue("Hello, World!");
                startActivity(new Intent(Fargot_Password.this, Base_Home.class));
            }
        });


    }
}
