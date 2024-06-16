package com.example.monthly_apha;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Application;
import android.content.Intent;

import com.google.firebase.database.FirebaseDatabase;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        // creating a singleton
        fb.getInstance();
        //fb.instance.authenticateUser();
        if(!fb.getInstance().isSignedIn()){
                Intent forward = new Intent(this, Login.class);
                forward.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(forward);
        }

    }
}
