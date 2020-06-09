package com.android.sql.activity;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.sql.dao.MyDataBaseHelper;
import com.android.sql.dao.TaskDao;
import com.android.sql.service.MusicService;
import com.android.sql.view.ClockTextView;

import static com.android.sql.activity.R.drawable.start;


public class ClockActivity extends AppCompatActivity implements View.OnClickListener {
    private ClockTextView minute;
    private ClockTextView second;
    private ClockTextView task_ing;
    private ImageView stop;
    private ImageView end;
    private ImageView music;
    boolean isStart=true;
    private int task_time;
    private TaskDao taskDao;
    private String task_name;
    private int user_id;
    private Intent intent2;

    private static final int UPDATE_CLOCK=0x111;
    private static final int STOP_CLOCK=0x222;
    @SuppressLint("HandlerLeak")
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case UPDATE_CLOCK:
                    int second_num=Integer.parseInt(second.getText().toString());
                    int minute_num=Integer.parseInt(minute.getText().toString());
                    if(second_num<59&&second_num>=0){
                        if(second_num<9){
                            second.setText("0"+(second_num+1));
                        }else{
                            second.setText((second_num+1)+"");
                        }
                    }
                    else if (second_num==59){
                        if(minute_num+1==task_time){
                            taskDao.updateTime(user_id,task_name,task_time);
                            handler.removeCallbacks(update_thread);
                            ClockActivity.this.finish();
                        }
                        else{
                            second.setText("00");
                            if(minute_num<9){
                                minute.setText("0"+(minute_num+1));
                            }
                            else{
                                minute.setText((minute_num+1)+"");
                            }
                        }
                    }
//                    handler.sendEmptyMessageDelayed(msg.what,1000);
                    break;
                case STOP_CLOCK:
                    int m=Integer.parseInt(minute.getText().toString());
                    if(m!=0){
                        taskDao.updateTime(user_id,task_name,m);
                    }
                    ClockActivity.this.finish();
                    break;
                default:
                    break;

            }
        }
    };
    Thread update_thread=new Thread(new Runnable() {
        @Override
        public void run() {
            Message msg = Message.obtain();
            msg.what = UPDATE_CLOCK;
            handler.sendMessage(msg);
            handler.postDelayed(update_thread,1000);
        }
    });
    //结束专注
    Thread stop_thread=new Thread(new Runnable() {
        @Override
        public void run() {
            Message msg=Message.obtain();
            msg.what=STOP_CLOCK;
            handler.sendMessage(msg);
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clock);
        Intent intent=getIntent();
        //设置底部导航栏不会遮挡布局
        getWindow()
                .addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        MyDataBaseHelper db=new MyDataBaseHelper(ClockActivity.this,"study.db",null,2);
        SQLiteDatabase sd = db.getWritableDatabase();
        taskDao=new TaskDao(sd);
        task_name = intent.getStringExtra("task_name");
        task_time = intent.getIntExtra("task_time",0);
        user_id = intent.getIntExtra("user_id", -1);
        intent2=new Intent(ClockActivity.this, MusicService.class);
        //获取控件，并添加监听器
        stop=findViewById(R.id.stop);
        stop.setOnClickListener(this);
        minute = findViewById(R.id.minute);
        second = findViewById(R.id.second);
        end = findViewById(R.id.end_time);
        end.setOnClickListener(this);
        music = findViewById(R.id.music);
        music.setOnClickListener(this);
        task_ing = findViewById(R.id.task_ing);
        task_ing.setText(task_name);
        update();
    }

    //解决了退出该活动后，音乐仍旧播放的问题
    @Override
    protected void onDestroy() {
        stopService(intent2);
        super.onDestroy();
    }

    protected void update() {
        handler.post(update_thread);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.stop:
                if(isStart){
                    stop.setBackgroundResource(start);
                    isStart=false;
                    handler.removeCallbacks(update_thread);
                }else{
                    stop.setBackgroundResource(R.drawable.stop);
                    isStart=true;
                    handler.post(update_thread);
                }
                break;
            case R.id.music:
//                Intent intent=new Intent(ClockActivity.this, MusicService.class);
                Log.i("music","his");
                if(!MusicService.isplay){
                    Log.i("music","start");
                    startService(intent2);
                }else{
                    Log.i("music","stop");
                    stopService(intent2);
                }
                break;
            case R.id.end_time:
                handler.removeCallbacks(update_thread);
                handler.post(stop_thread);
                break;
            default:
                break;
        }
    }
}
