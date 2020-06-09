package com.android.sql.activity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.sql.dao.MyDataBaseHelper;
import com.android.sql.dao.UserDao;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private MyDataBaseHelper db;
    private TextView password_text;
    private TextView username_text;
    private Button register;
    private UserDao userDao;
    private QMUITopBarLayout bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        bar = findViewById(R.id.register_bar);
        bar.setTitle("注册");
        db = new MyDataBaseHelper(this, "study.db", null, 2);
        SQLiteDatabase sd = db.getWritableDatabase();
        userDao=new UserDao(sd);
        password_text = findViewById(R.id.edittext_password);
        username_text=findViewById(R.id.edittext_username);
        register = findViewById(R.id.register);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register:
                String password = password_text.getText().toString();
                String username= username_text.getText().toString();
                boolean isRegister = userDao.addUser(username,password);
                if (isRegister) {
                    Toast.makeText(this,"注册成功",Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    Toast.makeText(this, "注册失败", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
}
