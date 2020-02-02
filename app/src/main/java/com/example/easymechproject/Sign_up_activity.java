package com.example.easymechproject;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.HashMap;

public class Sign_up_activity extends AppCompatActivity {
    SQLiteOpenHelper openHelper;
    SQLiteDatabase db;
    Button _sign_button;
    EditText editID, textname,textpass2, textphone, textemail, textpass;
    String id, name, phone, email,  pass1, pass2,code;
    private Spinner spinner;
    private FirebaseAuth easyMechAuth;
    private DatabaseReference easyMechRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_activity);
        openHelper = new DatabaseHelper(this);
        easyMechAuth = FirebaseAuth.getInstance();

        _sign_button = (Button) findViewById(R.id.SignUp);
        textname = (EditText) findViewById(R.id.username);
        spinner = findViewById(R.id.Mobile_spinner);
        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,CountryData.countryISO));
        textphone = (EditText) findViewById(R.id.phone);
        textemail = (EditText) findViewById(R.id.my_email);
        textpass = (EditText) findViewById(R.id.pass_word1);
        textpass2 = (EditText) findViewById(R.id.pass_word2);
        code = CountryData.countryCodes[spinner.getSelectedItemPosition()];
        _sign_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                db=openHelper.getWritableDatabase();
                String name = textname.getText().toString();
                if(name.isEmpty() || (name.length()>25 && name.length()<5)){
                    textname.setError("Username is Required!");
                    textname.requestFocus();
                    return;
                }
                String phone = textphone.getText().toString();
                if(phone.isEmpty() || phone.length()<10){
                    textphone.setError("Valid Number is Required!");
                    textphone.requestFocus();
                    return;
                }
                String mobile = "+"+ code + phone;
                String email = textemail.getText().toString();
                if(email.isEmpty()){
                    textemail.setError("Email Address is Required!");
                    textemail.requestFocus();
                    return;
                }
                else if(email.matches("[0-9a-zA-Z._-]+@[a-z]+\\.+[a-z]+")==false){
                    textemail.setError("Valid Email Address is Required!");
                    textemail.requestFocus();
                    return;
                }
                String pass1 = textpass.getText().toString();
                if(pass1.isEmpty()){
                    textpass.setError("Password is Required!");
                    textpass.requestFocus();
                    return;
                }
                else if(pass1.length()<8){
                    Toast.makeText(getApplicationContext(),"Please Enter a Valid Password containing at least 8 characters",Toast.LENGTH_LONG).show();
                }
                String pass2 = textpass2.getText().toString();
                if(pass2.isEmpty()){
                    textpass2.setError("Password is Required!");
                    textpass2.requestFocus();
                    return;
                }

                else if(pass2.length()<8){
                    Toast.makeText(getApplicationContext(),"Please Enter a Valid Password containing at least 8 characters",Toast.LENGTH_LONG).show();
                    return;
                }
                else if(pass1.equals(pass2)) {
                    //insertData(name, mobile, email, pass2);
                    registerUser(name, email, mobile, pass2);
                    Toast.makeText(getApplicationContext(), "Your account is in progress! Please confirm your phone number", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Sign_up_activity.this, Mobile_OTP.class);
                    intent.putExtra("phonenumber", mobile);
                    startActivity(intent);
                }
                else {
                    textpass2.setError("Password Doesn't Match!");
                    textpass2.requestFocus();
                    return;
                }


            }
        });

    }
    private void registerUser(final String name,final String email, final String phone, final String password) {

        easyMechAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    final String uid = currentUser.getUid();

                    FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                        @Override
                        public void onSuccess(InstanceIdResult instanceIdResult) {
                            String token = instanceIdResult.getToken();

                            easyMechRef = FirebaseDatabase.getInstance().getReference().child("Drivers").child(uid);

                            HashMap<String, String> userMap = new HashMap<>();
                            userMap.put("name", name);
                            userMap.put("email", email);
                            userMap.put("mobile", phone);
                            userMap.put("password", password);
                            userMap.put("device_token",token);

                            easyMechRef.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {
                                        //mProgressBar.setVisibility(View.GONE);
                                      //  Intent mIntent = new Intent(Sign_up_activity.this, MainActivity.class);
                                       // mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        Toast.makeText(Sign_up_activity.this, "User Created!", Toast.LENGTH_LONG).show();
                                       // startActivity(mIntent);
                                        finish();
                                    }
                                    else{
                                        Toast.makeText(Sign_up_activity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }

                                }
                            });

                        }
                    });


                } else {
                    //mProgressBar.setVisibility(View.GONE);
                    Toast.makeText(Sign_up_activity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }



}
