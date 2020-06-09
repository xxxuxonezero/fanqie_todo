package com.android.sql.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.android.sql.DataGenerator;
import com.android.sql.dao.MyDataBaseHelper;
import com.android.sql.domin.Task;
import com.android.sql.TaskAdapter;
import com.android.sql.dao.TaskDao;
import com.google.android.material.tabs.TabLayout;
import com.qmuiteam.qmui.widget.QMUIAnimationListView;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogBuilder;

import java.util.List;
import java.util.logging.Logger;

import static android.widget.Toast.*;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private QMUITopBarLayout bar;
    private Button addTask;
    private QMUIAnimationListView listView;
    private List<Task> taskList;
    private int user_id;
    private QMUIDialog.CustomDialogBuilder builder;
    private SeekBar seekBar;
    private EditText task;
    private String task_name;
    private TextView show_time;
    private int time;
    private TaskDao taskDao;
    private TaskAdapter taskAdapter;
    private MyDataBaseHelper db;
    private String edit_task;
    private int edit_time;
    private TabLayout mTabLayout;
    private Fragment[]mFragmensts;

    //    private CustomPopWindow.PopupWindowBuilder popWindow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_success_activity);
        listView = findViewById(R.id.list_view);
        setBar();
        mFragmensts = DataGenerator.getFragments("TabLayout Tab");
        initView();
        final Intent intent=getIntent();
        user_id=intent.getIntExtra("user_id",-1);
        db = new MyDataBaseHelper(this, "study.db", null, 2);
        SQLiteDatabase sd = db.getWritableDatabase();
        taskDao=new TaskDao(sd);
        addTask.setOnClickListener(this);
        initListView();
        //点击事件，编辑
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Task curtask=taskList.get(position);
                new QMUIDialog.CustomDialogBuilder(LoginActivity.this).setLayout(R.layout.edit_task)
                        .setTitle("编辑")
                        .addAction("取消", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                dialog.dismiss();
                            }
                        })
                        .addAction("确定", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                TextView t = dialog.findViewById(R.id.edit_task);
                                edit_task=t.getText().toString();
                                TextView t2 = dialog.findViewById(R.id.edit_time);
                                edit_time = Integer.parseInt(t2.getText().toString());
                                boolean b = taskDao.updateTask(user_id, curtask.getName(), edit_task, edit_time);
                                if (b) {
                                    makeText(LoginActivity.this,"修改成功", LENGTH_SHORT).show();
                                    initListView();
                                    dialog.dismiss();
                                }
                                else{
                                    makeText(LoginActivity.this,"修改失败", LENGTH_SHORT).show();
                                }

                            }
                        }).show();
            }
        });
        //长按点击事件，删除
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final Task curtask=taskList.get(position);
                new QMUIDialog.MessageDialogBuilder(LoginActivity.this)
                        .setMessage("确定要删除吗")
                        .addAction("取消", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                dialog.dismiss();
                            }
                        })
                        .addAction("确定", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                taskDao.deleteTask(user_id,curtask.getName());
                                taskList.remove(position);
                                taskAdapter.notifyDataSetChanged();
                                Toast.makeText(LoginActivity.this,"删除成功", LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }).show();
                return true;
            }
        });
        setCustomDialog();
    }

    @SuppressLint("ResourceAsColor")
    protected void setBar() {
        bar = findViewById(R.id.topbar);
    }

    protected void initTask() {
        taskList=taskDao.getAllTast(user_id);
    }

    protected void initListView(){
        initTask();
        taskAdapter = new TaskAdapter(LoginActivity.this,taskList);
        listView.setAdapter(taskAdapter);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_task:
                makeText(this,"add", LENGTH_SHORT).show();
                builder.show();
                break;
            case R.id.begin:
                int position= (int) v.getTag();
                Task task=taskList.get(position);
                if(task!=null){
                    Intent intent = new Intent(LoginActivity.this, ClockActivity.class);
                    intent.putExtra("user_id",user_id);
                    intent.putExtra("task_name",task.getName());
                    intent.putExtra("task_time",task.getTime());
                    Log.i("time",task.getTime()+"");
                    startActivity(intent);
                }
                break;
            default:
                break;
        }
    }

    @SuppressLint("ResourceType")
    protected void setCustomDialog() {
        setTheme(R.style.AppTheme2);
        builder=new QMUIDialog.CustomDialogBuilder(this);
        final QMUIDialogBuilder qmuiDialogBuilder = builder.setLayout(R.layout.add_task)
                .setTitle("待办事项")
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction(R.drawable.clock, " ", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        seekBar = dialog.findViewById(R.id.seekBar);
                        show_time = dialog.findViewById(R.id.show_time);
                        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                String s = progress + "分钟";
                                show_time.setText(s.toCharArray(), 0, s.length()
                                );
                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {

                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {

                            }
                        });
                    }
                })
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        task = dialog.findViewById(R.id.task);
                        task_name = task.getText().toString();
                        seekBar = dialog.findViewById(R.id.seekBar);
                        show_time = dialog.findViewById(R.id.show_time);
                        time = seekBar.getProgress();
                        boolean b = taskDao.addTask(user_id, task_name, time);
                        if (b) {
                            makeText(LoginActivity.this,"添加成功", LENGTH_SHORT).show();
                            taskList.add(new Task(task_name,time));
                            taskAdapter.notifyDataSetChanged();
//                            initListView();
                            dialog.dismiss();
                        }else
                        {
                            makeText(LoginActivity.this,"该任务已存在", LENGTH_SHORT).show();
                        }
//                        Toast.makeText(LoginActivity.this,taskname, LENGTH_SHORT);
                    }
                });
    }

    private void initView() {
        mTabLayout = (TabLayout) findViewById(R.id.tab_bottom);
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                onTabItemSelected(tab.getPosition());

                //改变Tab 状态
                for(int i=0;i< mTabLayout.getTabCount();i++){
                    if(i == tab.getPosition()){
                        mTabLayout.getTabAt(i).setIcon(DataGenerator.mTabResPressed[i]);
                    }else{
                        mTabLayout.getTabAt(i).setIcon(DataGenerator.mTabRes[i]);
                    }
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container_self,mFragmensts[0]).commit();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mTabLayout.addTab(mTabLayout.newTab().setIcon(R.drawable.todo1).setText(DataGenerator.mTabTitle[0]));
        mTabLayout.addTab(mTabLayout.newTab().setIcon(R.drawable.data).setText(DataGenerator.mTabTitle[1]));

    }

    private void onTabItemSelected(int position){
        Fragment fragment = null;
        switch (position){
            case 0:
                listView.setVisibility(View.VISIBLE);
                addTask = bar.addRightTextButton("+", R.id.add_task);
                addTask.setOnClickListener(this);
                fragment = mFragmensts[0];
                break;
            case 1:
                listView.setVisibility(View.GONE);
                bar.removeAllRightViews();
                fragment = mFragmensts[1];
                break;
        }
        if(fragment!=null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container_self,fragment).commit();
        }
    }
}
