package com.example.administrador.myapplication.models.persistence;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.administrador.myapplication.models.entities.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Rodrigo on 01/06/2015.
 */
public class UserContract {

    public static final String TABLE = "user_my_app";
    public static final String ID = "user_id";
    public static final String PASSWORD = "password";

    public static final String[] COLUNS = {ID, PASSWORD};

    public static String createTable() {
        final StringBuilder sql = new StringBuilder();
        sql.append(" CREATE TABLE IF NOT EXISTS ");
        sql.append(TABLE);
        sql.append(" ( ");
        sql.append(ID + " TEXT PRIMARY KEY , ");
        sql.append(PASSWORD + " TEXT");
        sql.append(" ) WITHOUT ROWID; ");
        return sql.toString();
    }

    public static ContentValues getContentValues(User user) {
        ContentValues content = new ContentValues();
        content.put(ID, user.getUserId());
        content.put(PASSWORD, user.getPassword());
        return content;
    }

    public static User bind(Cursor cursor) {
        if (!cursor.isBeforeFirst() || cursor.moveToNext()) {
            User user = new User();
            user.setUserId(cursor.getString(cursor.getColumnIndex(ID)));
            user.setPassword(cursor.getString(cursor.getColumnIndex(PASSWORD)));
            return user;
        }
        return null;
    }

    public static List<User> bindList(Cursor cursor) {
        final List<User> users = new ArrayList<User>();
        while (cursor.moveToNext()) {
            users.add(bind(cursor));
        }
        return users;
    }
    
}
