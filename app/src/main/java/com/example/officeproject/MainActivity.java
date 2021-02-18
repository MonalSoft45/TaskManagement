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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
   private Button btnLogin;
   private TextView tvLogin;
   private EditText etEmail;
   private EditText etPass;
   private FirebaseAuth firebaseAuth;
   private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin = findViewById(R.id.btn_LogIn);
        tvLogin=findViewById(R.id.tv_Signup);
        etEmail = findViewById(R.id.et_email);
        etPass = findViewById(R.id.et_password);
        firebaseAuth = FirebaseAuth.getInstance();
        mDialog = new ProgressDialog(this);

        if (firebaseAuth.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
        }


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mEmail = etEmail.getText().toString().trim();
                String mPass = etPass.getText().toString().trim();

                if (TextUtils.isEmpty(mEmail)){
                    etEmail.setError("Required Email");
                    return;
                }
                if (TextUtils.isEmpty(mPass)){
                    etPass.setError("Required Password");
                    return;
                }
                mDialog.setMessage("Authenticate...");
                mDialog.show();
                firebaseAuth.signInWithEmailAndPassword(mEmail,mPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(MainActivity.this, "Login SuccessFull", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                            mDialog.dismiss();
                        }else {
                            Toast.makeText(MainActivity.this, "Problem", Toast.LENGTH_SHORT).show();
                            mDialog.dismiss();
                        }

                    }
                });
        //
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),RegistrationActivity.class));
            }
        });
    }
}