package com.android.sql.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.android.sql.domin.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskDao {
    SQLiteDatabase sd;

    public TaskDao(SQLiteDatabase sd) {
        this.sd = sd;
    }

    public boolean isValid(int user_id, String task_name) {
        String sql="select * from study_task where user_id=? and task_name=?";
        Cursor cursor = sd.rawQuery(sql, new String[]{user_id + "", task_name});
        int i = cursor.getCount();
        if (i == 0) {
            return true;
        }else{
            return false;
        }

    }

    public boolean addTask(int user_id, String task_name, int time) {
        if(isValid(user_id,task_name)){
            ContentValues cv=new ContentValues();
            cv.put("user_id",user_id);
            cv.put("task_name", task_name);
            cv.put("task_time",time);
            cv.put("total_time",0);
            long i = sd.insert("study_task", null, cv);
            if (i != -1) {
                return true;
            }else{
                return false;
            }
        }
        else{
            return false;
        }
    }

    public List<Task> getAllTast(int user_id){
        List<Task> tasks=new ArrayList<>();
        String sql="select * from study_task where user_id=?";
        Cursor cursor = sd.rawQuery(sql, new String[]{user_id + ""});
        if(cursor.moveToFirst()){
            do {
                String task_name = cursor.getString(cursor.getColumnIndex("task_name"));
                int task_time=cursor.getInt(cursor.getColumnIndex("task_time"));
                tasks.add(new Task(task_name,task_time));
            }while(cursor.moveToNext());
            cursor.close();
        }
        return tasks;

    }

    public boolean updateTask(int user_id,String task_name,String task_name_after,int task_time){
        ContentValues cv = new ContentValues();
        cv.put("task_name",task_name_after);
        cv.put("task_time",task_time);
        int i = sd.update("study_task", cv, "user_id=? and task_name=?", new String[]{user_id + "", task_name});
        if (i != -1) {
            return true;
        }
        else{
            return false;
        }
    }

    public void deleteTask(int user_id, String task_name) {
        sd.delete("study_task","user_id=? and task_name=?",new String[]{user_id+"",task_name})     ;
    }

    public void updateTime(int user_id, String task_name, int time) {
        String sql ="select total_time from study_task where user_id=? and task_name=?";
        Cursor cursor = sd.rawQuery(sql, new String[]{String.valueOf(user_id), task_name});
        cursor.moveToFirst();
        int total_time=cursor.getInt(cursor.getColumnIndex("total_time"));
        cursor.close();
        ContentValues cv = new ContentValues();
        cv.put("total_time",total_time+time);
        sd.update("study_task",cv,"user_id=? and task_name=?",new String[]{String.valueOf(user_id), task_name});
    }

    public List<Task> getTotal(int user_id) {
        String sql="select * from study_task where user_id=? and total_time>0";
        Cursor cursor = sd.rawQuery(sql, new String[]{String.valueOf(user_id)});
        List<Task> tasks = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do{
                String task_name = cursor.getString(cursor.getColumnIndex("task_name"));
                int total_time = cursor.getInt(cursor.getColumnIndex("total_time"));
                Task task = new Task();
                task.setName(task_name);
                task.setTotal_time(total_time);
                tasks.add(task);
            }while (cursor.moveToNext());
        }
        return tasks;
    }

}
