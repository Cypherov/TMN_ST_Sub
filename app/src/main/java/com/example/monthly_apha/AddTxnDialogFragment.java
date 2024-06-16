package com.example.monthly_apha;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddTxnDialogFragment extends DialogFragment {

    // Declare UI
    EditText note, amount, date;
    CheckBox paid, toBePaid, autoDateBox;
    Button addTxn, addNewCat, editCat, deleteCat, addNewAcc, editAcc, deleteAcc;
    String snote, samount, sdate, scategory, saccount;
    String spaid = "false";
    String stoBePaid = "false";
    boolean isPaid,isToBePaid;
    Spinner spinner, spinner2;
    Context context = getContext();

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_addtxn);

        // Assign UI elements to variable
        note = dialog.findViewById(R.id.note);
        amount = dialog.findViewById(R.id.amount);
        date = dialog.findViewById(R.id.date);
        autoDateBox = dialog.findViewById(R.id.autoDate);
        paid = dialog.findViewById(R.id.paid);
        toBePaid = dialog.findViewById(R.id.toBePaid);
        addTxn = dialog.findViewById(R.id.addTxn);
        addNewCat = dialog.findViewById(R.id.btnAddCat);
        editCat = dialog.findViewById(R.id.btnEditCat);
        deleteCat = dialog.findViewById(R.id.btnDeleteCat);
        addNewAcc = dialog.findViewById(R.id.btnAddAcc);
        editAcc = dialog.findViewById(R.id.btnEditAcc);
        deleteAcc = dialog.findViewById(R.id.btnDeleteAcc);
        spinner = dialog.findViewById(R.id.spinner);
        spinner2 = dialog.findViewById(R.id.spinner2);

        // auto checking autodate
        autoDateBox.setChecked(true);


        // Initialize SpinnerHelper instances
        // this to be exhanged with 0x9s commented code as its the same but with encaps
        SpinnerHelper categorySpinnerHelper = new SpinnerHelper(getActivity(), spinner, 1, 1);
        SpinnerHelper accountSpinnerHelper = new SpinnerHelper(getActivity(), spinner2, 2, 2);
        categorySpinnerHelper.addItemToSpinner("test");

        spinner.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                note.setText("yes");
                return true;
            }
        });
//Spinner adjustmentss
 //add category item to category spinner
        addNewCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.add_spinner_item_dialog);
                // setting title
                TextView editCatTitle = dialog.findViewById(R.id.title);
                editCatTitle.setText("Add New Category");
                // Save Button in dialog
                Button saveButton = dialog.findViewById(R.id.saveButton);
                saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText addCat= dialog.findViewById(R.id.etnewitemname);
                        if (addCat == null || addCat.getText().toString()==""){
                            Toast.makeText(getContext(),"Insert Category Name",Toast.LENGTH_SHORT).show();
                            return;
                        }else {
                            categorySpinnerHelper.addItemToSpinner(addCat.getText().toString());
                            categorySpinnerHelper.setupAdapter();
                            dialog.dismiss();
                        }
                    }
                });

                // Cancel Button in dialog
                Button cancelButton = dialog.findViewById(R.id.cancelButton);
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                dialog.getWindow().setGravity(Gravity.CENTER);
            }
        });
 //edit category item in category spinner
        editCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.edit_spinner_item_dialog);
                // Assigning Views
                Spinner editSpinner = dialog.findViewById(R.id.spinnerdialog);
                EditText editCat = dialog.findViewById(R.id.etedititemname);
                Button saveButton = dialog.findViewById(R.id.saveButton);
                Button cancelButton = dialog.findViewById(R.id.cancelButton);
                // setting title
                TextView editCatTitle = dialog.findViewById(R.id.title);
                editCatTitle.setText("Edit Existing Category");

                // Creating SpinnerHelper
                SpinnerHelper editSpinnerHelper = new SpinnerHelper(dialog.getContext(), editSpinner, 1, 1);

                // Set initial value for EditText from selected item in spinner
                editCat.setText(editSpinnerHelper.getSelectedItem());

                // Save Button in dialog
                saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String newItem = editCat.getText().toString();
                        String oldItem = editSpinnerHelper.getSelectedItem();
                        int pos = editSpinnerHelper.getPosition(oldItem);
                        editSpinnerHelper.editItemInSpinner(pos, newItem);
                        categorySpinnerHelper.setupAdapter();
                        dialog.dismiss();
                    }
                });

                // Cancel Button in dialog
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                dialog.getWindow().setGravity(Gravity.CENTER);
            }
        });
 //delete category item from category spinner
        deleteCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.delete_spinner_item_dialog);

                // Assigning Views
                Spinner deleteSpinner = dialog.findViewById(R.id.spinnerdialog);
                TextView tvDeleteMSG = dialog.findViewById(R.id.tvdeleteMsg);
                Button deleteButton = dialog.findViewById(R.id.saveButton);
                Button cancelButton = dialog.findViewById(R.id.cancelButton);

                // setting title
                TextView deleteCatTitle = dialog.findViewById(R.id.title);
                deleteCatTitle.setText("Delete Existing Category");

                // Creating SpinnerHelper
                SpinnerHelper deleteSpinnerHelper = new SpinnerHelper(dialog.getContext(), deleteSpinner, 1, 1);

                // Set initial value for TextView from selected item in spinner
                tvDeleteMSG.setText("The Selected Category will be Deleted");

                // Delete Button in dialog
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String oldItem = deleteSpinnerHelper.getSelectedItem();
                        int pos = deleteSpinnerHelper.getPosition(oldItem);
                        deleteSpinnerHelper.deleteItemFromSpinner(pos);
                        categorySpinnerHelper.setupAdapter();
                        dialog.dismiss();
                    }
                });

                // Cancel Button in dialog
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                dialog.getWindow().setGravity(Gravity.CENTER);
            }
        });
// Adjust Acc Spinner
// add acc spinner
        addNewAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.add_spinner_item_dialog);

                // Assigning Views
                EditText addAcc = dialog.findViewById(R.id.etnewitemname);
                Button saveButton = dialog.findViewById(R.id.saveButton);
                Button cancelButton = dialog.findViewById(R.id.cancelButton);

                // setting title
                TextView addAccTitle = dialog.findViewById(R.id.title);
                addAccTitle.setText("Add New Account");

                // Save Button in dialog
                saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (addAcc == null || addAcc.getText().toString()==""){
                            Toast.makeText(getContext(),"Insert Account Name",Toast.LENGTH_SHORT).show();
                            return;
                        }else{
                            accountSpinnerHelper.addItemToSpinner(addAcc.getText().toString());
                            accountSpinnerHelper.setupAdapter();
                            dialog.dismiss();
                        }

                    }
                });

                // Cancel Button in dialog
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                dialog.getWindow().setGravity(Gravity.CENTER);
            }
        });
// edit acc spinner
        editAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.edit_spinner_item_dialog);

                // Assigning Views
                Spinner editSpinner = dialog.findViewById(R.id.spinnerdialog);
                EditText editAcc = dialog.findViewById(R.id.etedititemname);
                Button saveButton = dialog.findViewById(R.id.saveButton);
                Button cancelButton = dialog.findViewById(R.id.cancelButton);

                // setting title
                TextView editAccTitle = dialog.findViewById(R.id.title);
                editAccTitle.setText("Edit Existing Accounts");

                // Creating SpinnerHelper
                SpinnerHelper editSpinnerHelper = new SpinnerHelper(dialog.getContext(), editSpinner, 2, 2);

                // Set initial value for EditText from selected item in spinner
                editAcc.setText(editSpinnerHelper.getSelectedItem());

                // Save Button in dialog
                saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String newItem = editAcc.getText().toString();
                        String oldItem = editSpinnerHelper.getSelectedItem();
                        int pos = editSpinnerHelper.getPosition(oldItem);
                        editSpinnerHelper.editItemInSpinner(pos, newItem);
                        accountSpinnerHelper.setupAdapter();
                        dialog.dismiss();
                    }
                });

                // Cancel Button in dialog
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                dialog.getWindow().setGravity(Gravity.CENTER);
            }
        });
// delete acc spinner
        deleteAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.delete_spinner_item_dialog);

                // Assigning Views
                Spinner deleteSpinner = dialog.findViewById(R.id.spinnerdialog);
                TextView tvDeleteMSG = dialog.findViewById(R.id.tvdeleteMsg);
                Button deleteButton = dialog.findViewById(R.id.saveButton);
                Button cancelButton = dialog.findViewById(R.id.cancelButton);

                // setting title
                TextView deleteAccTitle = dialog.findViewById(R.id.title);
                deleteAccTitle.setText("Delete Existing Account");

                // Creating SpinnerHelper
                SpinnerHelper deleteSpinnerHelper = new SpinnerHelper(dialog.getContext(), deleteSpinner, 2, 2);

                // Set initial value for TextView from selected item in spinner
                tvDeleteMSG.setText("The Selected Account will be Deleted");

                // Delete Button in dialog
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String oldItem = deleteSpinnerHelper.getSelectedItem();
                        int pos = deleteSpinnerHelper.getPosition(oldItem);
                        deleteSpinnerHelper.deleteItemFromSpinner(pos);
                        accountSpinnerHelper.setupAdapter();
                        dialog.dismiss();
                    }
                });

                // Cancel Button in dialog
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                dialog.getWindow().setGravity(Gravity.CENTER);
            }
        });

        // disable autodate automatically when date et is touched
        date.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (autoDateBox.isChecked()){
                        autoDateBox.setChecked(false);
                    }
                }
                return false; // return false to keep the EditText editable
            }
        });
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (autoDateBox.isChecked()){
                    autoDateBox.setChecked(false);
                }
            }
        });
        addTxn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String snote = note.getText().toString();
                String samount = amount.getText().toString();
                scategory = categorySpinnerHelper.getSelectedItem();
                saccount = accountSpinnerHelper.getSelectedItem();

                // date format
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY");
                // instance of dateAdjust class
                dateAdjust dformat = new dateAdjust();

                if (autoDateBox.isChecked()){
                    sdate = dateFormat.format(new Date());
                }else{
                    sdate = dformat.adjustFormat(date.getText().toString());
                    //sdate = dateFormat.format(date.getText().toString());
                    if (sdate == "incorrect"){
                        // in case the date was not well formated and couldnt be adjusted
                        // a an error message will be given + auto date will be enabled
                        Toast.makeText(getContext(),"Error Incorrect Date, Auto Date has been enabled. Try Again",Toast.LENGTH_LONG).show();
                        autoDateBox.setChecked(true);
                        return;
                    }
                }


                if(paid.isChecked()){
                    spaid = "true";
                    isPaid =true;
                }else {
                    spaid = "false";
                    isPaid =false;
                }
                if(toBePaid.isChecked()){
                    stoBePaid= "true";
                    isToBePaid=true;
                }else {
                    stoBePaid= "false";
                    isToBePaid=false;
                }

                if (!samount.isEmpty() && samount !=null){
                    double dsamount = Double.parseDouble(samount);
                    if (fb.getInstance().isSignedIn()) {
                        //uid = currentUser.getUid();
                        Date ddate = stringToDate(sdate);
                        long timestamp = ddate.getTime();
                        txns txn = new txns(snote,dsamount,timestamp,scategory,saccount, isPaid,isToBePaid);
                        fb.getInstance().fbAddTxn(txn);
                        Toast.makeText(getContext(),"Txn Added",Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                        //DatabaseReference txnfirebase = database.getReference();
                        //txnfirebase.child("users").child(uid).child("Txns").push().setValue(txn);
                    }

                    /* uploading to database without user credentials
                    txns txn = new txns(snote,dsamount,sdate,scategory,saccount, isPaid,isToBePaid);
                    DatabaseReference txnfirebase = database.getReference();
                    txnfirebase.child("Txns").push().setValue(txn);
                     */

                }else {
                    Toast.makeText(getContext(),"Insert Amount",Toast.LENGTH_LONG).show();
                }



            }
            private Date stringToDate(String sdate) {

                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                Date submittedDate = null;
                try {
                    submittedDate = formatter.parse(sdate);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                return submittedDate;
            }



        });
        // Add logic for other UI elements as needed

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);

        return dialog;
    }
}