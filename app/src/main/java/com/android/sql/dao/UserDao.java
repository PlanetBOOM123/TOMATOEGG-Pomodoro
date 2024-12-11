package com.android.sql.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.android.sql.bean.UserBean;

import java.util.ArrayList;
import java.util.List;


public class UserDao {
    private UserDbHelper mHelper;

    public UserDao(Context context) {
        mHelper = new UserDbHelper(context);
    }
    //添加一个用户
    public void insert(UserBean userBean) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        String insert_sql = "INSERT INTO users (touxiang,account,password,gxmsg,history_gold,gold) VALUES (?,?,?,?,0,0)";
        Object[] obj = {userBean.getTouxiang(), userBean.getAccount(),userBean.getPassword(),userBean.getGxmsg()};
        db.execSQL(insert_sql, obj);
        db.close();
    }

    //全表查询用户
    public List<UserBean> query() {

        SQLiteDatabase db = mHelper.getReadableDatabase();
        List<UserBean> userBeans = new ArrayList<UserBean>();
        String query_sql = "select * from users";
        Cursor cursor = db.rawQuery(query_sql, null);

        if (0 == cursor.getCount()) {
            return null;
        }
        cursor.moveToFirst();
        do {
            int id = cursor.getInt(0);
            String a = cursor.getString(1);
            String b = cursor.getString(2);
            String c = cursor.getString(3);
            String d = cursor.getString(4);
            int e =  cursor.getInt(5);
            int f =  cursor.getInt(6);
            UserBean taskbean = new UserBean(id, a, b,c,d,e,f);
            userBeans.add(taskbean);
        }while (cursor.moveToNext());

        return userBeans;
    }
    // 根据账号查找，找到返回找到的那条数据，没找到返回null
    public UserBean query(String account) {

        SQLiteDatabase db = mHelper.getReadableDatabase();
        String query_sql = "select * from users";
        Cursor cursor = db.rawQuery(query_sql, null);

        if (0 == cursor.getCount()) {
            return null;
        }
        cursor.moveToFirst();
        do {
            int id = cursor.getInt(0);
            String a = cursor.getString(1);
            String b = cursor.getString(2);
            String c = cursor.getString(3);
            String d = cursor.getString(4);
            int e =  cursor.getInt(5);
            int f =  cursor.getInt(6);
            if (account.equals(b)){
                UserBean userBean1 = new UserBean(id, a, b,c,d,e,f);
                return userBean1;
            }
        }while (cursor.moveToNext());
        return null;
    }
    //根据自增id删除用户
    public boolean delete(int id) {
        try {
            SQLiteDatabase db = mHelper.getReadableDatabase();
            db.delete("users", "id = ?", new String[] { ""+id });
            return true;
        }catch (Exception e) {
            return false;
        }

    }
    //根据自增id更新用户信息[密码，个性，]
    public boolean update(UserBean userBean){
        try {
            SQLiteDatabase db = mHelper.getReadableDatabase();
            String[] whereArgs = { String.valueOf(userBean.getId()) };
            ContentValues cv = new ContentValues();
            cv.put("password", userBean.getPassword());
            cv.put("gxmsg", userBean.getGxmsg());
            db.update("users",cv,"id=?",whereArgs);
            return true;
        }catch (Exception e) {
            return false;
        }
    }
    //根据自增id更新用户信息[金币相关]
    public boolean update_gold(UserBean userBean){
        try {
            SQLiteDatabase db = mHelper.getReadableDatabase();
            String[] whereArgs = { String.valueOf(userBean.getId()) };
            ContentValues cv = new ContentValues();
            cv.put("history_gold", userBean.getHistory_gold());
            cv.put("gold", userBean.getGold());
            db.update("users",cv,"id=?",whereArgs);
            return true;
        }catch (Exception e) {
            return false;
        }
    }
}
