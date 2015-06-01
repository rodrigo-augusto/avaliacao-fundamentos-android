package com.example.administrador.myapplication.models.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String BANCO_DADOS = "SERVICE_ORDER_DB";
    private static int VERSION = 3;

    public DatabaseHelper(Context context) {
        super(context, DatabaseHelper.BANCO_DADOS, null, DatabaseHelper.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ServiceOrderContract.createTable());
        db.execSQL(UserContract.createTable());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE service_order;");
        db.execSQL(ServiceOrderContract.createTable());
        db.execSQL(UserContract.createTable());
    }

}
