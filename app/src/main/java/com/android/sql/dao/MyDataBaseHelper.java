package com.android.sql.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyDataBaseHelper extends SQLiteOpenHelper {
    public static final String CREATE_USER="create table User(id integer primary key autoincrement,username text unique,password text)";
    public static final String CREATE_STUDY="create table study_task(user_id integer,task_name text,task_time integer,total_time integer,constraint ky_task primary key (user_id,task_name))";
    private Context context;
    public MyDataBaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context=context;
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
}
