package com.example.monthly_apha;

import java.text.SimpleDateFormat;
import java.util.Date;

public class txns {
    private String note, txnId;
    private double amount;
    private long date;
    private String category, account;
    private boolean paid, toBePaid;
    public txns() {

    }
    public txns(String not,double am, long dat, String cat, String acc, boolean pa, boolean tbp){
        note = not;
        amount = am;
        date = dat;
        category = cat;
        account=acc;
        paid=pa;
        toBePaid=tbp;

    }
    public txns(String not,double am, long dat, String cat, String acc){
        note = not;
        amount = am;
        date = dat;
        category = cat;
        account=acc;
    }
    public void updateTxn(String not,double am, long dat, String cat, String acc, boolean pa, boolean tbp){
        note = not;
        amount = am;
        date = dat;
        category = cat;
        account=acc;
        paid=pa;
        toBePaid=tbp;

    }
// getters they are neccessary for firebase db connection I believe
    public String getNote() {
        return note;
    }
    public String getTxnId() {
        return txnId;
    }

    public double getAmount() {
        return amount;
    }

    public long getDate() {
        return date;
    }

    public String getCategory() {
        return category;
    }
    public String getAccount() {
        return account;
    }
    public boolean getPaid(){
        return paid;
    }
    public boolean getToBePaid() {
        return toBePaid;
    }
//---
// setters
    public void setNote(String note) {
        this.note = note;
    }
    public void setTxnId(String txnId) {
        this.txnId = txnId;
    }


    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    public void setAccount(String account) {
        this.account = account;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public void setToBePaid(boolean toBePaid) {
        this.toBePaid = toBePaid;
    }
    public String getDateString() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date(this.getDate());
        return formatter.format(date);
    }
//---
}
