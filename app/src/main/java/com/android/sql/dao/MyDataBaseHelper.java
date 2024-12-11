package com.android.sql.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyDataBaseHelper extends SQLiteOpenHelper {

    public static final String CREATE_USER="create table User(id integer primary key autoincrement,username text unique,password text)";
    public static final String CREATE_STUDY="create table study_task(user_id integer,task_name text,task_time integer,total_time integer,constraint ky_task primary key (user_id,task_name))";
    private Context context;
    private static MyDataBaseHelper sHelper;
    private static final int VERSION = 1;

    public MyDataBaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context=context;
    }

    public synchronized static MyDataBaseHelper getInstance(Context context) {
        if (null == sHelper) {
            sHelper = new MyDataBaseHelper(context, CREATE_USER, null, VERSION);
        }
        return sHelper;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER);
        db.execSQL(CREATE_STUDY);
        Toast.makeText(context,"succeed",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists User");
        db.execSQL("drop table if exists study_task");
        onCreate(db);
    }

    /**
     * 修改密码
     * @param username
     * @param password
     * @return
     */
    public int updatePwd(String username,String password){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("password",password);
        int update=db.update("user_table",values,"username=?",new String[]{username+""});
        db.close();
        return update;
}
}
