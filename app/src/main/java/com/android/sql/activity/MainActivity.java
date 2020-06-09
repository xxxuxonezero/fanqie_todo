package com.android.sql.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.sql.dao.MyDataBaseHelper;
import com.android.sql.dao.UserDao;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private MyDataBaseHelper db;
    private TextView password_text;
    private TextView username_text;
    private Button login;
    private Button register;
    private UserDao userDao;
    private QMUITopBarLayout bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bar=findViewById(R.id.login_bar);
        bar.setTitle("登录");
        db = new MyDataBaseHelper(this, "study.db", null, 2);
        SQLiteDatabase sd = db.getWritableDatabase();
        userDao=new UserDao(sd);
        password_text = findViewById(R.id.edittext_password);
        username_text=findViewById(R.id.edittext_username);
        login=findViewById(R.id.login);
        register = findViewById(R.id.register);
        login.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                String password = password_text.getText().toString();
                String username= username_text.getText().toString();
                int id = userDao.getUser(username, password);
                if (id!=-1) {
                    Toast.makeText(this,"登录成功",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.putExtra("user_id",id);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(this, "密码错误，登录失败", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.register:
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

}
