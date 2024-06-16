package com.example.monthly_apha;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    Button btnLogin, btnRegister;
    EditText etEmail, etPassword;
    TextView tvregister, tvResetpass;
    String tEmail, tpassword;
    //private FirebaseAuth mAuth;
    //private FirebaseUser currentUser;
    fb FB= fb.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btnLogin);
        //btnRegister = findViewById(R.id.btnRegister);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        tvregister = findViewById(R.id.signupText);
        tvResetpass = findViewById(R.id.tvResetpass);
        // Initialize Firebase Auth
        //mAuth = FirebaseAuth.getInstance();
            super.onStart();
            // Check if user is signed in (non-null) and update UI accordingly.
            //currentUser = mAuth.getCurrentUser();
            if(fb.getInstance().isSignedIn()){
                Intent go = new Intent(Login.this, MainActivity.class);
                startActivity(go);
            }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tEmail = etEmail.getText().toString();
                tpassword = etPassword.getText().toString();
                if (tEmail.isEmpty() || tpassword.isEmpty()){
                    Toast.makeText(Login.this, "Please input all the required details above", Toast.LENGTH_LONG).show();
                }
                else {
                    FB.fbAuth().signInWithEmailAndPassword(tEmail, tpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_LONG).show();
                                FB.getCurretUser();
                                FB.updateCurrentUser();
                                Intent go = new Intent(Login.this, MainActivity.class);
                                startActivity(go);
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(Login.this, "Signin failed.. " + task.getException().getMessage(), Toast.LENGTH_LONG).show();

                            }
                        }

                    });
                }
            }
        });

            /*
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, registeration.class);
                startActivity(intent);
            }
        });

             */
        tvregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, registeration.class);
                startActivity(intent);
            }
        });
        tvResetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, forgotPassword.class);
                startActivity(intent);
            }
        });


    }
}