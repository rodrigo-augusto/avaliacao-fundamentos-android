package com.example.administrador.myapplication.models.persistence;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.example.administrador.myapplication.models.entities.User;
import com.example.administrador.myapplication.util.AppUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rodrigo on 01/06/2015.
 */
public final class UsersRepository {

    private static class Singleton {
        public static final UsersRepository INSTANCE = new UsersRepository();
    }

    private UsersRepository() {
        super();
    }

    public static UsersRepository getInstance() {
        return Singleton.INSTANCE;
    }

    public void save(User user) {
        DatabaseHelper helper = new DatabaseHelper(AppUtil.CONTEXT);
        SQLiteDatabase db = helper.getWritableDatabase();
        //if (user.getUserId() == null) {
        db.insert(UserContract.TABLE, null, UserContract.getContentValues(user));
        //} else {
        //    String where = UserContract.ID + " = ?";
        //    String[] args = {user.getUserId().toString()};
        //    db.update(UserContract.TABLE, UserContract.getContentValues(user), where, args);
        //}
        db.close();
        helper.close();
    }

    public void delete(User user) {
        DatabaseHelper helper = new DatabaseHelper(AppUtil.CONTEXT);
        SQLiteDatabase db = helper.getWritableDatabase();
        String where = UserContract.ID + " = ?";
        String[] args = {user.getUserId().toString()};
        db.delete(UserContract.TABLE, where, args);
        db.close();
        helper.close();
    }

    public List<User> getAll() {
        DatabaseHelper helper = new DatabaseHelper(AppUtil.CONTEXT);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(UserContract.TABLE, UserContract.COLUNS, null, null, null, null, null);
        List<User> users = UserContract.bindList(cursor);
        db.close();
        helper.close();
        return users;
    }

    public List<User> findAll(User user) {
        DatabaseHelper helper = new DatabaseHelper(AppUtil.CONTEXT);
        SQLiteDatabase db = helper.getReadableDatabase();

        StringBuilder where = new StringBuilder("1 = 1");
        List<String> args = new ArrayList<String>();

        if (!TextUtils.isEmpty(user.getUserId())) {
            where.append(" AND ").append(UserContract.ID).append(" = ? ");
            args.add(user.getUserId().toString());
        }

        if (!TextUtils.isEmpty(user.getPassword())) {
            where.append(" AND ").append(UserContract.PASSWORD).append(" = ? ");
            args.add(user.getPassword().toString());
        }

        Cursor cursor = db.query(UserContract.TABLE,
                UserContract.COLUNS,
                where.toString(),
                args.toArray(new String[args.size()]),
                null, null, null);
        List<User> users = UserContract.bindList(cursor);
        db.close();
        helper.close();
        return users;
    }

}
