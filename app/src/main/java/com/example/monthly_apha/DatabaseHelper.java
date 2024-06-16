package com.example.monthly_apha;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "SmsDatabase.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "sms_messages";
    private static final String COLUMN_ID = "id"; // txn_id column name
    private static final String COLUMN_MESSAGE = "message";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_MESSAGE + " TEXT" + ")";
        db.execSQL(CREATE_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the existing table
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        // Create the table again with the new structure
        onCreate(db);
    }

    public void addMessage(SmsMessageModel message) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MESSAGE, message.getMessage());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }
    public boolean deleteMessage(String messageId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete("sms_messages", "id = ?", new String[]{messageId});
        db.close();
        return rowsAffected > 0;
    }
    public List<SmsMessageModel> getAllMessages() {
        List<SmsMessageModel> messages = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[] { COLUMN_ID, COLUMN_MESSAGE}, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                SmsMessageModel message = new SmsMessageModel(cursor.getString(0), cursor.getString(1));
                messages.add(message);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return messages;
    }
}