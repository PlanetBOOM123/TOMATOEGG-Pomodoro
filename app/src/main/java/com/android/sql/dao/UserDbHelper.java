package com.android.sql.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDbHelper extends SQLiteOpenHelper {
    private static final String DBname="user.db";

    public UserDbHelper(Context context) {
        super(context, DBname, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql="create table users (id integer primary key autoincrement, " +
                "touxiang text," +
                "account text," +
                "password text," +
                "gxmsg text," +
                "history_gold integer," +
                "gold integer)";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
