package com.example.monthly_apha;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ReportIssue extends AppCompatActivity {
    //declaring views
    EditText etTitle, etDetails;
    Spinner spinnerTopic;
    Button btnSub,btnHome;
    String [] topics = {"Report Issue", "Improvement Suggestion","Others"};
    String msgTitle, msgDetails,msgTopic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_report_issue);

            etTitle= findViewById(R.id.reporttitle);
            etDetails = findViewById(R.id.details);
            btnSub = findViewById(R.id.btnReportIssue);
            btnHome = findViewById(R.id.btnhome);
            spinnerTopic = findViewById(R.id.spinner3);

            SpinnerHelper topicSpinnerHelper = new SpinnerHelper(this,spinnerTopic,topics);


            btnSub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    msgTitle = etTitle.getText().toString();
                    msgDetails = etDetails.getText().toString();
                    msgTopic = topicSpinnerHelper.getSelectedItem();
                    reports report = new reports(msgTitle, msgDetails, msgTopic,fb.instance.getUid(),fb.getInstance().getEmail());
                    fb.instance.fbAddReport(report);
                    Toast.makeText(getApplicationContext(),"Report Sent",Toast.LENGTH_LONG).show();
                }
            });
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReportIssue.this, MainActivity.class);
                startActivity(intent);
            }
        });





    }
}