package com.android.sql.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class UserDao {
    private SQLiteDatabase sd;

    public UserDao(SQLiteDatabase sd) {
        this.sd = sd;
    }

    /**
     * 如果登录成功，获取用户id
     * @param username
     * @param password
     * @return
     */
    public int getUser(String username, String password) {
        String sql="select * from User where username=? and password=?";
        Cursor cursor = sd.rawQuery(sql, new String[]{username, password});
        int count = cursor.getCount();
        if(count==0)
            return -1;
        else{
            cursor.moveToFirst();
            return cursor.getInt(cursor.getColumnIndex("id"));
        }

    }

    /**
     * 查看用户名是否被注册
     * @param username
     * @return
     */
    public boolean isValid(String username) {
        String sql="select * from User where username=?";
        Cursor cursor = sd.rawQuery(sql, new String[]{username});
        int count = cursor.getCount();
        if(count==0)
            return true;
        else
            return false;
    }

    /**
     * 注册用户
     * @param username
     * @param password
     * @return
     */
    public boolean addUser(String username, String password) {
        if(isValid(username)){
            ContentValues values = new ContentValues();
            values.put("username", username);
            values.put("password", password);
            long i = sd.insert("User", null, values);
            values.clear();
            if(i!=-1)
                return true;
            else
                return false;
        }
        else
            return false;

    }
}
