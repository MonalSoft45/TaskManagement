package com.example.officeproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {

    private EditText regEmail,regPassword;
    private Button btnRegister;
    private TextView tvSignIn;
    private ProgressDialog mDialog;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        regEmail =findViewById(R.id.et_email_reg);
        regPassword = findViewById(R.id.et_password_reg);
        btnRegister=findViewById(R.id.btn_register);
        tvSignIn=findViewById(R.id.tv_login);

        firebaseAuth=FirebaseAuth.getInstance();
        mDialog = new ProgressDialog(this);

        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mEmail = regEmail.getText().toString().trim();
                String mPassword = regPassword.getText().toString().trim();

                if(TextUtils.isEmpty(mEmail)){
                    regEmail.setError("Required Email");
                    return;
                }
                if (TextUtils.isEmpty(mPassword)){
                    regPassword.setError("Required Password");
                    return;
                }
                mDialog.setMessage("Processing...");
                mDialog.show();

                firebaseAuth.createUserWithEmailAndPassword(mEmail,mPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isComplete()){
                            Toast.makeText(RegistrationActivity.this, "SuccessFull", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            mDialog.dismiss();
                        }else{
                            Toast.makeText(RegistrationActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                            mDialog.dismiss();
                        }


                    }
                });

            }
        });
    }
}