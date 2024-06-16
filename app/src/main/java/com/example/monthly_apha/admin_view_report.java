package com.example.monthly_apha;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class admin_view_report extends AppCompatActivity {

    private static final String TAG = "admin_view_report_activity";
    private RecyclerView reportsRecyclerView;
    private ReportAdapter reportsAdapter;
    Button btnUserInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_view_report);

        btnUserInterface= findViewById(R.id.btnUserInterface);

        btnUserInterface.setOnClickListener(v -> {
            startActivity(new Intent(admin_view_report.this, MainActivity.class));
        });
        DatabaseReference usersRef = fb.getCustomReferenace("users"); // replace "users" with the path to your users in the database

        TextView userCountTextView = findViewById(R.id.userCountTextView); // replace with your TextView's ID

        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get the number of users
                long numUsers = dataSnapshot.getChildrenCount();

                // Update the TextView
                String msg = "Number of users: " + String.valueOf(numUsers);
                userCountTextView.setText(msg);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors.
                Log.w(TAG, "loadUsers:onCancelled", databaseError.toException());
            }
        });


        reportsRecyclerView = findViewById(R.id.reportsRecyclerView);
        reportsRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        reportsAdapter = new ReportAdapter(new ArrayList<>());
        reportsRecyclerView.setAdapter(reportsAdapter);

        fetchReports();
    }

    private List<reports> fetchReports() {
        List<reports> reportsList = new ArrayList<>();
        DatabaseReference reportsRef = fb.getCustomReferenace("Reports");

        reportsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<reports> reportsList = new ArrayList<>();
                for (DataSnapshot reportSnapshot : dataSnapshot.getChildren()) {
                    reports report = reportSnapshot.getValue(reports.class);
                    String key = reportSnapshot.getKey(); // Get the key of the report
                    report.setKey(key); // Save the key in the report object
                    reportsList.add(report);
                }
                reportsAdapter.updateData(reportsList); // Notify the adapter that the data has changed
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadReports:onCancelled", databaseError.toException());
            }
        });

        return reportsList;
    }
}