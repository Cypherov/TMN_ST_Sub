package com.example.monthly_apha;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SpinnerHelper implements AdapterView.OnItemSelectedListener{
    private Context context;
    public Spinner spinner;
    private String selectedItem;
    private String[] items;
    // to check if this is the first selection of an item
    private boolean isFirstSelection;
    private ArrayAdapter<String> arrayAdapter;
    private String spinnerKey;
    private List<String> defaultItems;
    private List<String> defaultItemsCat =  Arrays.asList("Food", "Grocery", "Entertainment");
    private List<String> defaultItemsAcc =  Arrays.asList("Cash", "Bank", "Bank Muscat");

    public SpinnerHelper(Context context, Spinner spinner,  int SpinnerKey,int listnum) {
        this.context = context;
        this.spinner = spinner;
        //this.spinnerKey = SpinnerKey;
        //this.items = items;
        if (listnum ==1){
            this.defaultItems = defaultItemsCat;
            spinnerKey = "cat";
        } else if (listnum ==2) {
            this.defaultItems = defaultItemsAcc;
            spinnerKey= "acc";
        }
        isFirstSelection = true;
        setupAdapter();
        checkAndInitializeSpinner();
        /*
        arrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, items);

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(this);

         */
    }


    public SpinnerHelper(Context context, Spinner spinner,  String[]items) {
        this.context = context;
        this.spinner = spinner;
        //this.spinnerKey = SpinnerKey;
        this.items = items;
        isFirstSelection = true;

        arrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, items);

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(this);

    }
    public SpinnerHelper(Context context, int listnum){
        this.context = context;
        if (listnum ==1){
            spinnerKey = "cat";
        } else if (listnum ==2) {
            spinnerKey= "acc";
        }
    }
    public void setupAdapter() {
        List<String> itemss = getSpinnerItems();
        //Collections.sort(itemss);
        // sorting was causing a problem where the edit spinner was not showing the correct item from the data base when opened
        // it should select the acc and cat from the database and show it in the spinner but it was showing wrong selection
        // as the sort is changing the arrangment of the items
        arrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, itemss);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(this);
    }
    private void checkAndInitializeSpinner() {
        List<String> spinnerItems = getSpinnerItems();
        if (spinnerItems.isEmpty()) {
            // SharedPreferences does not contain items, initialize with default items
            saveSpinnerItems(defaultItems);
            arrayAdapter.addAll(defaultItems);
            arrayAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedItem = parent.getItemAtPosition(position).toString();
        // prevent the toast to auto appear whenever the activity is opened before the user makes any selections
        if (isFirstSelection){
            isFirstSelection = false;
        }else {
            Toast.makeText(context, "Selected: " + selectedItem, Toast.LENGTH_SHORT).show();
        }

    }



    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Handle nothing selected (if needed)
    }
    public String getSelectedItem(){
        return selectedItem;
    }

    /*
    public int getPosition(String value) {
        if (value != null) {
            for (int i = 0; i < items.length; i++) {
                if (value.equals(items[i])) {
                    return i;
                }
            }
        }
        return -1; // Value not found
    }

     */
    public int getPosition(String value) {
        List<String> items = getSpinnerItems();
        if (value != null && items != null) {
            for (int i = 0; i < items.size(); i++) {
                if (value.equals(items.get(i))) {
                    return i;
                }
            }
        }
        return -1; // Value not found
    }

    public void addItemToSpinner(String newItem) {
        List<String> items = getSpinnerItems();
        items.add(newItem);
        saveSpinnerItems(items);
        if(arrayAdapter !=null){
            arrayAdapter.notifyDataSetChanged();

        }
    }
    public void editItemInSpinner(int position, String editedItem) {
        List<String> items = getSpinnerItems();
        items.set(position, editedItem);
        saveSpinnerItems(items);
        if(arrayAdapter !=null){
            arrayAdapter.notifyDataSetChanged();
        }
        //arrayAdapter.notifyDataSetChanged();
    }

    public void deleteItemFromSpinner(int position) {
        List<String> items = getSpinnerItems();
        items.remove(position);
        saveSpinnerItems(items);
        if(arrayAdapter !=null){
            arrayAdapter.notifyDataSetChanged();
        }
        //arrayAdapter.notifyDataSetChanged();
    }
    public List<String> getSpinnerItems() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(spinnerKey, Context.MODE_PRIVATE);
        Set<String> set = sharedPreferences.getStringSet("items", new HashSet<>());
        return new ArrayList<>(set);
    }
    public void saveSpinnerItems(List<String> items) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(spinnerKey, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> set = new HashSet<>(items);
        editor.putStringSet("items", set);
        editor.apply();
    }


}
