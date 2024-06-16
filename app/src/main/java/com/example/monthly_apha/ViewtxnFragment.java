package com.example.monthly_apha;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewtxnFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewtxnFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ViewtxnFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Shorts.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewtxnFragment newInstance(String param1, String param2) {
        ViewtxnFragment fragment = new ViewtxnFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_txns, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get the current year and month
        YearMonth currentYearMonth = YearMonth.now();
        LocalDate firstDayOfMonth = currentYearMonth.atDay(1);
        LocalDate lastDayOfMonth = currentYearMonth.atEndOfMonth();

        // Format the dates as strings (yyyy-MM-dd)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // Convert LocalDate to long (timestamp)
        long startDate = firstDayOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long endDate = lastDayOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();


        fb FB= fb.getInstance();
        DatabaseReference transactionsRef = FB.getTxnRef();
        transactionsRef.keepSynced(true);

        //query to retrieve transactions within the date range
        Query query = transactionsRef.orderByChild("date").startAt(startDate).endAt(endDate);


        // Initialize RecyclerView and set an empty adapter  | x1z replacement part 1
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewTransactions);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        TxnAdapter adapter = new TxnAdapter(new ArrayList<>(), getContext());
        recyclerView.setAdapter(adapter);

        // Attach a listener to retrieve data
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<txns> transactionsList = new ArrayList<>();
                for (DataSnapshot txnSnapshot : dataSnapshot.getChildren()) {
                    txns txn = txnSnapshot.getValue(txns.class);
                    // getting txn id for each txn
                    txn.setTxnId(txnSnapshot.getKey());
                    transactionsList.add(txn);
                }

                // | x1z replacement part 2
                adapter.updateData(transactionsList);

                // Set up your RecyclerView adapter x1z
                /*
                RecyclerView recyclerView = view.findViewById(R.id.recyclerViewTransactions);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                TxnAdapter adapter = new TxnAdapter(transactionsList, getContext());
                recyclerView.setAdapter(adapter);

                 */
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("MyApp", "Database error: " + databaseError.getMessage());
                // Handle error
            }
        });
    }

}