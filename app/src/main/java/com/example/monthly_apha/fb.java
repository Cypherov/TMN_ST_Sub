package com.example.monthly_apha;

import static androidx.core.content.ContextCompat.startActivity;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class fb {
    static fb instance= new fb();
    private FirebaseAuth tAuth;
    private FirebaseDatabase tDatabase;

    private DatabaseReference tDatabaseRef;

    private FirebaseUser currentUser;
    private String uid;
    private String email, name;
    public Double totalSum;
    public String transactionIdLocal;

    private fb(){
        tAuth = FirebaseAuth.getInstance();
        currentUser = tAuth.getCurrentUser();
        tDatabase = FirebaseDatabase.getInstance("https://expenditure-tracker-mobile-app-default-rtdb.asia-southeast1.firebasedatabase.app");
        tDatabaseRef = tDatabase.getReference();
        if (isSignedIn()){
            uid = currentUser.getUid();
            tDatabaseRef.child("users").child(uid).child("Txns").keepSynced(true);

        }

        //keeping an offline record of the following ref
        //tDatabaseRef.keepSynced(true);

    }
    public static DatabaseReference getCustomReferenace(String x){
        return getInstance().tDatabase.getReference(x);
    }


    public static fb getInstance(){
        if (instance == null){
            instance = new fb();
        }
        return instance;
    }

    public FirebaseAuth fbAuth(){return tAuth;}
    public void fbAddTxn(txns txn){
        DatabaseReference txnRef = tDatabaseRef.child("users").child(uid).child("Txns").push();
        txnRef.setValue(txn);
        transactionIdLocal = txnRef.getKey();
    }
    public String getTransactionIdLocal(){
        return transactionIdLocal;
    }
    public void fbAddReport(reports report){
        tDatabaseRef.child("Reports").push().setValue(report);
    }
    public void fbAddUserDetails(UserDetails Details){
        tDatabaseRef.child("users").child(uid).child("Details").setValue(Details);
    }
    /* replaced by update user
    public void authenticateUser(){
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            uid = currentUser.getUid();
            //keeping an offline record of the following ref
            //tDatabaseRef.child("users").child(uid).child("Txns").keepSynced(true);
            // The user is signed in.
        } else {
            // No user is signed in.
        }
    }
     */

    public Boolean isSignedIn(){
        if(currentUser != null){
            return true;
        }else {
            return false;
        }
    }
    public void updateCurrentUser() {
        currentUser = tAuth.getCurrentUser();
        if (currentUser != null) {
            uid = currentUser.getUid();
            // Update the DatabaseReference with the new user ID
            //tDatabaseRef = tDatabase.getReference().child("users").child(uid);
        }
    }

    public DatabaseReference getDbRef(){
        return tDatabaseRef;
    }
    public  DatabaseReference getTxnRef(){
        return tDatabaseRef.child("users").child(uid).child("Txns");
    }
    public DatabaseReference getReportRef(){
        return tDatabaseRef.child("Reports");
    }
    public String getUid(){
        return uid;
    }
    public String  getEmail(){
        FirebaseUser user = currentUser;

        if (user != null) {
            String email = user.getEmail();
            return email;
        }
        return null;
    }
    public FirebaseUser getCurretUser(){
        return currentUser;
    }

    public void setSumOfTxnsMonth(Context context, TextView tv, String msg){
        YearMonth currentYearMonth = YearMonth.now();
        LocalDate firstDayOfMonth = currentYearMonth.atDay(1);
        LocalDate lastDayOfMonth = currentYearMonth.atEndOfMonth();

        // Format the dates as strings (MM/dd/yyyy)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        long startDate = firstDayOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long endDate = lastDayOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();

        // Create the query to retrieve transactions within the date range
        Query query = getTxnRef().orderByChild("date").startAt(startDate).endAt(endDate);
        // Attach a listener to retrieve data
        totalSum = 0.0;

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                totalSum = 0.0;
                for (DataSnapshot txnSnapshot : dataSnapshot.getChildren()) {
                    txns txn = txnSnapshot.getValue(txns.class);
                    // Check if txn is not null and add its amount to the total sum
                    if (txn != null) {
                        totalSum += txn.getAmount();
                    }
                }
                tv.setText(msg + String.format("%.2f", totalSum));
                //tv.setText(msg +totalSum);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context, "Cant fetch amount", Toast.LENGTH_LONG).show();
            }
        });

    }


    public void setDailyLimit(Context context, TextView tv, String msg){
        YearMonth currentYearMonth = YearMonth.now();
        LocalDate firstDayOfMonth = currentYearMonth.atDay(1);
        LocalDate lastDayOfMonth = currentYearMonth.atEndOfMonth();
        // Format the dates as strings (MM/dd/yyyy)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        long startDate = firstDayOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long endDate = lastDayOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();

        // Create the query to retrieve transactions within the date range
        Query query = getTxnRef().orderByChild("date").startAt(startDate).endAt(endDate);
        totalSum = 0.0;

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                totalSum = 0.0; // Initialize total sum
                for (DataSnapshot txnSnapshot : dataSnapshot.getChildren()) {
                    txns txn = txnSnapshot.getValue(txns.class);
                    // Check if txn is not null and add its amount to the total sum
                    if (txn != null) {
                        totalSum += txn.getAmount();
                    }
                }

                BudgetManager limit = new BudgetManager(context);

                double dailyLimit = limit.calculateDailySpendingLimitForCurrentMonth(totalSum);


                // displaying total monthly spending
                //tv.setText(msg +dailyLimit);
                tv.setText(String.format(msg + "%.2f", dailyLimit));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
                Toast.makeText(context, "Cant fetch amount", Toast.LENGTH_LONG).show();

            }
        });

    }

    public void setDailySpending(Context context, TextView tv, String msg){
        YearMonth currentYearMonth = YearMonth.now();

        LocalDate today = LocalDate.now();

        // Format the dates as strings (MM/dd/yyyy)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String currentDateString = today.format(formatter);

// Parse the date string back to a LocalDate
        LocalDate currentDate = LocalDate.parse(currentDateString, formatter);

        long currentdateLong = currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();

        // Create the query to retrieve transactions within the date range
        Query query = getTxnRef().orderByChild("date").equalTo(currentdateLong);
        // Attach a listener to retrieve data
        totalSum = 0.0;

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                totalSum = 0.0; // Initialize total sum
                for (DataSnapshot txnSnapshot : dataSnapshot.getChildren()) {
                    txns txn = txnSnapshot.getValue(txns.class);
                    // Check if txn is not null and add its amount to the total sum
                    if (txn != null) {
                        totalSum += txn.getAmount();
                    }
                }

                // displaying total monthly spending
                tv.setText(msg + String.format("%.2f", totalSum));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
                Toast.makeText(context, "Cant fetch amount", Toast.LENGTH_LONG).show();

            }
        });

    }


}
