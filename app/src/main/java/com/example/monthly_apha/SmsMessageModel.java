package com.example.monthly_apha;

public class SmsMessageModel {
    private String msg_id;
    private String message;
    private String date;

    public SmsMessageModel(String message) {
        this.message = message;
    }
    public SmsMessageModel(String id, String message) {
        this.msg_id = id;
        this.message = message;
    }

    // Getters and Setters
    public String getMsg_id() { return msg_id; }
    public void setMsg_id(String msg_id) { this.msg_id = msg_id; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
}