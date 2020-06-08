package com.android.sql;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.android.sql.activity.R;
import com.android.sql.domin.Task;

import java.util.List;

public class TaskAdapter extends BaseAdapter implements View.OnClickListener {
    private final View.OnClickListener listener;
    private final List<Task> list;

    public TaskAdapter(View.OnClickListener listener, List<Task> list) {
        this.listener = listener;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder=new ViewHolder();
            convertView=LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item,parent,false);
            holder.begin=convertView.findViewById(R.id.begin);
            holder.study_time = convertView.findViewById(R.id.study_time);
            holder.task_name=convertView.findViewById(R.id.task_name);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        holder.task_name.setText(list.get(position).getName());
        holder.study_time.setText(list.get(position).getTime()+"分钟");
        holder.begin.setOnClickListener(listener);
        holder.begin.setTag(position);
        return convertView;
    }

    @Override
    public void onClick(View v) {

    }

    class ViewHolder{
        TextView task_name;
        TextView study_time;
        Button begin;
    }
}
