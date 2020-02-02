package com.example.easymechproject;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class Base_Home extends AppCompatActivity{

    SQLiteDatabase db;
    SQLiteOpenHelper openHelper;
    Cursor cursor;

    private EditText email;
    private EditText pass_word;
    private Button _login;
    public int counter = 5;
    public static String emails, pass;
    private FirebaseAuth easyMechAuth;

    private DatabaseReference easyMechDriverRef;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base__home);

        openHelper = new DatabaseHelper(this);
        db=openHelper.getReadableDatabase();
        email = (EditText)findViewById(R.id._username);
        pass_word = (EditText)findViewById(R.id._password);
        _login = (Button)findViewById(R.id.login_btn);

        easyMechDriverRef = FirebaseDatabase.getInstance().getReference().child("Users");
        easyMechAuth = FirebaseAuth.getInstance();


        _login.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                emails = email.getText().toString();
                pass = pass_word.getText().toString();
/*                cursor = db.rawQuery("SELECT * FROM "+DatabaseHelper.TABLE_NAME+ " WHERE "
                                +DatabaseHelper.KEY_NAME+ "=? AND "+DatabaseHelper.KEY_PASS+"=?",
                        new String[]{emails,pass});*/

                if(emails.equals("")){
                    email.setError("Enter the Username!");
                    email.requestFocus();
                    return;
                }
                else if(pass.equals("")){
                    pass_word.setError("Enter the password!");
                    pass_word.requestFocus();
                    return;
                }
                //loginUser(emails, pass);
                Intent intent = new Intent(Base_Home.this,Services_LIsts.class);
                startActivity(intent);
                /*else if(cursor!=null){
                    if(cursor.getCount()>0){
                        cursor.moveToNext();
                        Toast.makeText(getApplicationContext(),"Login Successfull!.",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(Base_Home.this,Services_LIsts.class));
                    }

                    else{
                        Toast.makeText(getApplicationContext(),"Incorrect Password or Username! Please Verify.",Toast.LENGTH_LONG).show();
                    }
                }*/

            }
        });
    }

    public void sign_up_here(View v){
        TextView tv = (TextView) findViewById(R.id.sign_up_link);
        Intent int1 = new Intent(Base_Home.this, Sign_up_activity.class);
        startActivity(int1);
    }

    public void forgot_password_here(View v){
        TextView fp = (TextView) findViewById(R.id.fogrgotten_pass);
        startActivity(new Intent(Base_Home.this, Fargot_Password.class));
    }

    private void loginUser(String email, String password) {
        easyMechAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //PB.setVisibility(View.GONE);


                if(task.isSuccessful()){

                    final String current_user = easyMechAuth.getCurrentUser().getUid();


                    FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                        @Override
                        public void onSuccess(InstanceIdResult instanceIdResult) {
                            String token = instanceIdResult.getToken();
                            easyMechDriverRef.child(current_user).child("device_token").setValue(token)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getApplicationContext(),"Login Successfull!.",Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(Base_Home.this,Services_LIsts.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });

                        }
                    });
                }
                else{
                    Toast.makeText(Base_Home.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }


}
