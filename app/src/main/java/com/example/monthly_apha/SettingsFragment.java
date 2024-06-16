package com.example.monthly_apha;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {
    private TextView tvCurrentBudget;
    private EditText etSetBudget;
    private Button btnSetBudget, btnSignout, btnReport;
    private Button adminButton;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingsFragment() {
        // Required empty constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LibraryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
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
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);




        tvCurrentBudget = view.findViewById(R.id.tvcurrentbudget);
        etSetBudget = view.findViewById(R.id.etsetBudget);
        btnSetBudget = view.findViewById(R.id.btnSetBudget);
        btnReport= view.findViewById(R.id.btnReport);
        btnSignout = view.findViewById(R.id.btnsignout);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        displayCurrentBudget();
        btnSetBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    double newBudget = Double.parseDouble(etSetBudget.getText().toString());
                    BudgetManager.setBudget(newBudget);
                    displayCurrentBudget(); // Refresh the displayed budget
                } catch (NumberFormatException e) {
                    etSetBudget.setError("Please enter a valid number.");
                }
            }
        });
        btnSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mAuth.signOut();
                    Toast.makeText(getContext(), "User Sign out!", Toast.LENGTH_SHORT).show();
                    fb.getInstance().updateCurrentUser();
                    Intent intent = new Intent(getContext(), Login.class);
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Exception during sign out: "+e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        });
        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forward = new Intent(getContext(), ReportIssue.class);
                startActivity(forward);
            }
        });
        String currentUserId = fb.getInstance().getUid();



        adminButton = view.findViewById(R.id.btnadmin);
        if (currentUserId.equals("clu55wDCXagI7Ak6e2ogaO1czVo1")) {

            adminButton.setVisibility(View.VISIBLE);
            adminButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent forward = new Intent(getContext(), admin_view_report.class);
                    startActivity(forward);
                }
            });
        }else{
            adminButton.setVisibility(View.GONE);
        }

    }
    private void displayCurrentBudget() {
        double currentBudget = BudgetManager.getBudget();
        tvCurrentBudget.setText(""+currentBudget);
    }



}