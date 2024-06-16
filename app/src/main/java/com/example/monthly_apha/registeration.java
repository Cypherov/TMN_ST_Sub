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
import com.google.common.hash.Hashing;
import com.google.firebase.auth.AuthResult;

import java.nio.charset.StandardCharsets;

public class registeration extends AppCompatActivity {
    Button btnbtLogin, btnRegister;
    EditText etName, etEmail, etPassword;
    String tName, tEmail, tpassword;
    TextView tvLogin;
   // Users newUser;
   // tmnDB tdb;
    //firebase
   //private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);
        //btnbtLogin = findViewById(R.id.btnbtLogin);
        //firebase auth
        // Initialize Firebase Auth
        //mAuth = FirebaseAuth.getInstance();
        fb FB = fb.getInstance();


            // Check if user is signed in (non-null) and update UI accordingly.
            //disabled for testing, could be cancelled from the project
            //FirebaseUser currentUser = mAuth.getCurrentUser();
            if(FB.isSignedIn()){
                FB.updateCurrentUser();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }



        // Register Button
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tName = etName.getText().toString().trim();
                tEmail = etEmail.getText().toString().trim();
                tpassword = etPassword.getText().toString().trim();

                if (tName.isEmpty() || tEmail.isEmpty() || tpassword.isEmpty()){
                    Toast.makeText(registeration.this, "Please input all the required details above", Toast.LENGTH_LONG).show();
                }
                else{
                   // newUser = new Users(tName,tEmail,tpassword);
                 //   tdb = new tmnDB(registeration.this);
                 //   tdb.addUser(newUser);
                    fb.instance.fbAuth().createUserWithEmailAndPassword(tEmail, tpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Toast.makeText(registeration.this, "Registered Successfully", Toast.LENGTH_LONG).show();
                                        //FirebaseUser user = mAuth.getCurrentUser();
                                        FB.updateCurrentUser();
                                        //hash the password
                                        String sha256passHash= Hashing.sha256().hashString(tpassword, StandardCharsets.UTF_8).toString();
                                        UserDetails user = new UserDetails(FB.getUid(),tName,tEmail,sha256passHash);
                                        FB.fbAddUserDetails(user);
                                        Intent forward = new Intent(registeration.this, Login.class);
                                        startActivity(forward);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(registeration.this, "Error! "+task.getException().getMessage(),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        //Back to Login Button
        /*
        btnbtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forward = new Intent(registeration.this, Login.class);
                startActivity(forward);
            }
        });

         */
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forward = new Intent(registeration.this, Login.class);
                startActivity(forward);
            }
        });





    }
}