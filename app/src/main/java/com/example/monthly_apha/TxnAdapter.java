package com.example.monthly_apha;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
public class TxnAdapter extends RecyclerView.Adapter<TxnAdapter.ViewHolder> {

    private List<txns> transactionsList;
    private Context context;
    private fb FB = fb.getInstance();

    //Declaring Views
    EditText etNote,etAmount, etDate;
    CheckBox cbPaid, cbToBePaid, cbAutoDateBox;
    Spinner spinnerCat, spinnerAcc;

    //-- Declaring variables
    String snote, scategory,saccount,sdate, eCat, eAcc;
    double samount;
    Boolean isPaid,isToBePaid;

    public TxnAdapter(List<txns> transactionsList, Context context) {
        this.transactionsList = transactionsList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_transaction, parent, false);
        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        txns txn = transactionsList.get(position);

        // Bind transaction details to views
        holder.textViewNote.setText("Note: " + txn.getNote());
        holder.textViewAmount.setText("Amount: " + txn.getAmount());
        holder.textViewDate.setText("Date: " + txn.getDateString());
        holder.textViewCategory.setText("Category: " + txn.getCategory());
        holder.textViewAccount.setText("Account: " + txn.getAccount());
        holder.textViewIsPaid.setText("Paid: " + (txn.getPaid() ? "Yes" : "No"));
        holder.textViewToBePaid.setText("To Be Paid: " + (txn.getToBePaid() ? "Yes" : "No"));

        // Set click listeners for Edit and Delete buttons
        holder.buttonEdit.setOnClickListener(v -> editTransaction(txn));
        holder.buttonDelete.setOnClickListener(v -> deleteTransaction(txn));
    }

    @Override
    public int getItemCount() {
        return transactionsList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewAmount, textViewDate, textViewNote,
                textViewCategory, textViewAccount, textViewIsPaid, textViewToBePaid;
        Button buttonEdit, buttonDelete;

        ViewHolder(View itemView) {
            super(itemView);
            textViewAmount = itemView.findViewById(R.id.textViewAmount);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewNote = itemView.findViewById(R.id.textViewNote);
            textViewCategory = itemView.findViewById(R.id.textViewCategory);
            textViewAccount = itemView.findViewById(R.id.textViewAccount);
            textViewIsPaid = itemView.findViewById(R.id.textViewIsPaid);
            textViewToBePaid = itemView.findViewById(R.id.textViewToBePaid);
            buttonEdit = itemView.findViewById(R.id.buttonEdit);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }

    private void editTransaction(txns txn) {
        //
    //spinner item lists


        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_edit_transaction);
// Assigning Views
        etAmount= dialog.findViewById(R.id.amount);
        etNote = dialog.findViewById(R.id.note);
        etDate = dialog.findViewById(R.id.date);
        cbAutoDateBox = dialog.findViewById(R.id.autoDate);
        spinnerCat = dialog.findViewById(R.id.spinner);
        spinnerAcc = dialog.findViewById(R.id.spinner2);
        cbPaid = dialog.findViewById(R.id.paid);
        cbToBePaid = dialog.findViewById(R.id.toBePaid);

//Creating Spinners
        SpinnerHelper categorySpinnerHelper = new SpinnerHelper(dialog.getContext(),spinnerCat,1,1);
        SpinnerHelper accountSpinnerHelper = new SpinnerHelper(dialog.getContext(), spinnerAcc,2,2);

// Set initial values for views from transaction
        etNote.setText(txn.getNote());
        etAmount.setText(String.valueOf(txn.getAmount()));
        etDate.setText(txn.getDateString());

//setting spinners
        int accPosition = accountSpinnerHelper.getPosition(txn.getAccount());
        if (accPosition != -1) {
            spinnerAcc.setSelection(accPosition);
        } else {
            // Handle case where "Category 2" is not found
            Toast.makeText(dialog.getContext(), "recorded account is not available for selection",Toast.LENGTH_LONG).show();
        }
        int catPosition = categorySpinnerHelper.getPosition(txn.getCategory());
        if (catPosition != -1) {
            spinnerCat.setSelection(catPosition);
        } else {
            // Handle case where "Category 2" is not found
            Toast.makeText(dialog.getContext(), "recorded category is not available for selection",Toast.LENGTH_LONG).show();
        }

//setting checkboxes
        cbPaid.setChecked(txn.getPaid());
        cbToBePaid.setChecked(txn.getToBePaid());

// Save Button in dialog
        Button saveButton = dialog.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(v -> {
            // Assigning values for variables from views (Possibly Updated ones)
            snote = etNote.getText().toString();
            samount = Double.parseDouble(etAmount.getText().toString());
            sdate = etDate.getText().toString();
            // sdate
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY");
            dateAdjust dformat = new dateAdjust();
            if (cbAutoDateBox.isChecked()){
                sdate = dateFormat.format(new Date());
            }else{
                sdate = dformat.adjustFormat(etDate.getText().toString());
                if (sdate.equals("incorrect")){
                    Toast.makeText(dialog.getContext(), "Error Incorrect Date, Auto Date has been enabled. Try Again",Toast.LENGTH_LONG).show();
                    cbAutoDateBox.setChecked(true);
                    return;
                }
            }
            scategory = categorySpinnerHelper.getSelectedItem();
            saccount = accountSpinnerHelper.getSelectedItem();
                            //useless
                            eCat = categorySpinnerHelper.getSelectedItem();
                            eAcc = accountSpinnerHelper.getSelectedItem();
            isPaid = cbPaid.isChecked();
            isToBePaid = cbToBePaid.isChecked();

            //updating txn values
            Date ddate = stringToDate(sdate);
            long ldate = ddate.getTime();
            txn.updateTxn(snote,samount,ldate,scategory,saccount, isPaid,isToBePaid);

            // Update the transaction data (e.g., save to database)
            String uid = FB.getUid();
            DatabaseReference transactionsRef = FB.getTxnRef();
            String transactionId = txn.getTxnId();
            DatabaseReference transactionToUpdateRef = transactionsRef.child(transactionId);
            transactionToUpdateRef.setValue(txn);

            Toast.makeText(context, "Updated Successfully", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        Button cancelButton = dialog.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void deleteTransaction(txns txn) {

        fb.instance.getTxnRef().child(txn.getTxnId()).removeValue();
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
    public void updateData(List<txns> newTransactionsList) {
        this.transactionsList = newTransactionsList;
        notifyDataSetChanged();
    }

}


