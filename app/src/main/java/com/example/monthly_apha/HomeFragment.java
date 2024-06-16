package com.example.monthly_apha;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    Button btnLogin, btnRegister, btnAddTxn, btnSignout,btnViewTxn,btnReport, btnViewSms, btnManageBudget;
    TextView textView, tvDailyLimit, tvDailySpending;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment home.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textView = view.findViewById(R.id.is);
        tvDailyLimit = view.findViewById(R.id.textviewdailylimit);
        tvDailySpending = view.findViewById(R.id.textviewdailyspending);

        if (fb.getInstance().isSignedIn()){
            fb.instance.setDailySpending(getContext(),tvDailySpending,"Daily Spending: ");
            //textView.setText("Signed In: "+fb.instance.getUid() + " Monthly Spending: "+sumA);
        }

        if (fb.getInstance().isSignedIn()){
            fb.instance.setSumOfTxnsMonth(getContext(),textView,"Monthly Spending: ");
            //textView.setText("Signed In: "+fb.instance.getUid() + " Monthly Spending: "+sumA);
        }

        if (fb.getInstance().isSignedIn()){
            fb.instance.setDailyLimit(getContext(),tvDailyLimit,"Daily Spending Limit:");
            //textView.setText("Signed In: "+fb.instance.getUid() + " Monthly Spending: "+sumA);
        }





        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        BudgetManager budgetManager = new BudgetManager(getContext());

        preferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if (key.equals(BudgetManager.BUDGET_KEY)) {
                    updateDailySpending();
                }
            }
        };

        // Register the listener
        sharedPreferences.registerOnSharedPreferenceChangeListener(preferenceChangeListener);

        // Initial update
        updateDailySpending();
    }
    @Override
    public void onResume() {
        super.onResume();
        // Update the TextView when the activity resumes
        updateDailySpending();
    }

    @Override
    public void onPause() {
        super.onPause();
        // Unregister the listener
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(preferenceChangeListener);
    }

    private void updateDailySpending() {
        if (fb.getInstance().isSignedIn()){
            fb.instance.setDailyLimit(getContext(),tvDailyLimit,"Daily Spending Limit: ");
        }
    }


}