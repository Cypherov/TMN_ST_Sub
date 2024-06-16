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

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewsmsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewsmsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ViewsmsFragment() {
        // Required empty public constructor
    }

    public static ViewsmsFragment newInstance(String param1, String param2) {
        ViewsmsFragment fragment = new ViewsmsFragment();
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
        return inflater.inflate(R.layout.fragment_sms, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecyclerView(view);
        fetchMessagesAndDisplay(view);
        // checking any change in the database
        DatabaseReference transactionsRef = fb.getInstance().getTxnRef();
        Query query = transactionsRef.orderByChild("date");

        // addValueEventListener updates when there are changes, while the old addListenerForSingleValueEvent changes only when I press
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                fetchMessagesAndDisplay(view);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("MyApp", "Database error: " + databaseError.getMessage());
                // Handle error
            }
        });
    }

    private SmsAdapter adapter;

    private void fetchMessagesAndDisplay(View view) {
        if (isAdded() && getActivity() != null) {
            DatabaseHelper dbHelper = new DatabaseHelper(getActivity());
            List<SmsMessageModel> messages = dbHelper.getAllMessages();
            adapter = new SmsAdapter(getActivity(), messages);
            RecyclerView recyclerView = view.findViewById(R.id.smsRecyclerView);
            recyclerView.setAdapter(adapter);
        }
    }
    private void setupRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.smsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }





}