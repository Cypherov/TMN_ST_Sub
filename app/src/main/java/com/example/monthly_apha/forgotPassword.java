package com.example.monthly_apha;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgotPassword extends AppCompatActivity {

    Button btnResetPassword;
    EditText etEmail;
    TextView tvSignIn;
    String tEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnResetPassword = findViewById(R.id.btnReset);
        etEmail = findViewById(R.id.etEmail);
        tvSignIn = findViewById(R.id.tvSignIn);


        tvSignIn.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), Login.class));
            finish();
        });

        btnResetPassword.setOnClickListener(v -> {
            tEmail = etEmail.getText().toString();
            if (tEmail.isEmpty()){
                Toast.makeText(forgotPassword.this, "Please input your email address", Toast.LENGTH_LONG).show();
            }
            else {
                resetPassword(tEmail);
            }
        });


    }
    public void resetPassword(String userEmail) {
        FirebaseAuth auth = fb.getInstance().fbAuth();

        auth.sendPasswordResetEmail(userEmail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Inform the user that a password reset email has been sent
                            Toast.makeText(forgotPassword.this, "Password reset email sent to " + userEmail, Toast.LENGTH_LONG).show();
                        } else {
                            // Display the error to the user
                            Toast.makeText(forgotPassword.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}