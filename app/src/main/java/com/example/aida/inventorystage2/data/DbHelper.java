package com.example.aida.inventorystage2.data;

/**
 * Created by Aida on 7/29/2018.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "inventory.db";
    public static final int DATABASE_VERSION = 1;
    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_IT_TABLE = "CREATE TABLE " + Contract.CompanyEntry.TABLE_NAME + " (" +
                Contract.CompanyEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Contract.CompanyEntry.COLUMN_NAME + " TEXT NOT NULL," +
                Contract.CompanyEntry.COLUMN_SALARY + " REAL NOT NULL," +
                Contract.CompanyEntry.COLUMN_NUMBER + " INTEGER NOT NULL DEFAULT 0," +
                Contract.CompanyEntry.COLUMN_COMPANY_NAME + " TEXT NOT NULL," +
                Contract.CompanyEntry.COLUMN_COMPANY_PHONE + " TEXT NOT NULL);";
        db.execSQL(SQL_CREATE_IT_TABLE);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}

