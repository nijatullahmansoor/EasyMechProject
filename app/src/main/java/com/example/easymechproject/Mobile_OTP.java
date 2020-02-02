package com.example.easymechproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.executor.TaskExecutor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;



import java.util.concurrent.TimeUnit;


public class Mobile_OTP extends AppCompatActivity {
    private Button _confirm;
    private String verificationID;
    private FirebaseAuth Verify;
    private EditText code_text;
    private String code;
    private ProgressBar PB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile__otp);
        code_text =(EditText) findViewById(R.id.OTP_Con);
        PB = findViewById(R.id.progressBar);
        Verify = FirebaseAuth.getInstance();
        _confirm = (Button) findViewById(R.id.confirm_1);
        String phonenumber = getIntent().getStringExtra("phonenumber");
        send_OTP(phonenumber);


        _confirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String code = code_text.getText().toString();
                if(code.isEmpty() || code.length()<6 ){
                    code_text.setError("Enter code...");;
                    code_text.requestFocus();
                    PB.setVisibility(View.VISIBLE);

                    return;
                }
                PB.setVisibility(View.VISIBLE);
                verifyCode(code);

            }
        });
    }

    private void verifyCode(String code){
        PhoneAuthCredential credit = PhoneAuthProvider.getCredential(verificationID, code);
        SignIn(credit);
    }

    private void SignIn(PhoneAuthCredential credit) {
        Verify.signInWithCredential(credit).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent int2 = new Intent(Mobile_OTP.this, Base_Home.class);
                    int2.setFlags(int2.FLAG_ACTIVITY_NEW_TASK | int2.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(int2);

                }
                else{
                    Toast.makeText(Mobile_OTP.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void send_OTP(String number) {
        PB.setVisibility(View.VISIBLE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(number, 60, TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD, mCallBack);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationID = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if(code!=null){
                code_text.setText(code);
                verifyCode(code);

            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(Mobile_OTP.this, e.getMessage(),Toast.LENGTH_LONG).show();
            PB.setVisibility(View.GONE);
        }
    };

}