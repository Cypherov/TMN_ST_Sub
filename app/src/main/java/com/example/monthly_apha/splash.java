package com.example.monthly_apha;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class splash extends AppCompatActivity {
    private static final long splash_TIMEOUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(fb.getInstance().isSignedIn()){
                    String currentUser = fb.getInstance().getUid();
                    if (!currentUser.equals("clu55wDCXagI7Ak6e2ogaO1czVo1")){
                        // if the user is not and admin, he will be transfered to MainActivity
                        Intent intent = new Intent(splash.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        // if the user is admin, he will be transfered to admin_view_report
                        Intent intent = new Intent(splash.this, admin_view_report.class);
                        startActivity(intent);
                        finish();
                    }

                } else {
                    // If the user is not signed in, transfer to login page
                    Intent intent = new Intent(splash.this, Login.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, splash_TIMEOUT);


    }
}