package com.example.myproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String databaseName = "MyProjectDatabase";
    private static final int databaseVersion = 4;

    public DatabaseHelper(@Nullable Context context) {
        super(context, databaseName, null, databaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase MyDatabase) {
        Log.d("Debug", "Create Database");
        MyDatabase.execSQL("CREATE TABLE User (user_id INTEGER PRIMARY KEY AUTOINCREMENT, user_username VARCHAR(30), user_password VARCHAR(30), user_email VARCHAR(50), user_firstname VARCHAR(50), user_lastname VARCHAR(50), user_phone CHAR(10), user_picture BINARY, user_lat VARCHAR(12), user_lng VARCHAR(12))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDatabase, int i, int i1) {
        MyDatabase.execSQL("Drop Table if exists allusers");
    }

    public Boolean insertData(String username, String email, String phone, String password) {
        Log.d("Debug", "Insert Data");
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("user_username", username);
        contentValues.put("user_email", email);
        contentValues.put("user_phone", phone);
        contentValues.put("user_password", password);
        long result = MyDatabase.insert("User", null, contentValues);

        if (result == -1) return false;
        else return true;
    }

    public Integer checkUsername(String username, String email, String phone){
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor checkUsername = MyDatabase.rawQuery("SELECT 1 FROM User WHERE user_username = ?", new String[]{username});

        if(checkUsername.getCount() > 0) {
            return 1; // Return 1 หมายถึงว่ามี Username นี้อยู่ใน Database แล้ว
        } else {
            Cursor checkEmail = MyDatabase.rawQuery("SELECT 1 FROM User WHERE user_email = ?", new String[]{email});
            if (checkEmail.getCount() > 0) {
                return 2; // Return 2 หมายถึงว่ามี Email นี้อยู่ใน Database แล้ว
            } else {
                Cursor checkPhone = MyDatabase.rawQuery("SELECT 1 FROM User WHERE user_phone = ?", new String[]{phone});
                if (checkPhone.getCount() > 0) {
                    return 3; // Return 3 หมายถึงว่ามี Phone นี้อยู่ใน Database แล้ว
                } else {
                    return 0; // ผ่านเงื่อนไขไม่มีข้อมูลซ้ำ
                }
            }
        }
    }

    public Boolean checkHasUsernameLogin (String email, String password) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery("SELECT * FROM User WHERE ( user_email = ? OR user_email = ? OR user_phone = ? ) AND user_password = ?", new String[]{email, email, email, password});

        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }

//        public Boolean checkPhoneAndPassword(String password, String phone){
//        SQLiteDatabase MyDatabase = this.getWritableDatabase();
//        Cursor cursor = MyDatabase.rawQuery("Select * from allusers where password =? and phone = ?", new String[]{phone, password});
//            if (cursor.getCount() > 0)
//                return true;
//            else
//                return false;
//        }
//
//    public boolean checkEmailPassword(EditText email, EditText password) {
//        SQLiteDatabase MyDatabase = this.getWritableDatabase();
//        Cursor cursor = MyDatabase.rawQuery("Select * from allusers where email =? and password = ?", new String[]{String.valueOf(email), String.valueOf(password)});
//        if (cursor.getCount() > 0)
//            return true;
//        else
//            return false;
//    }

}


