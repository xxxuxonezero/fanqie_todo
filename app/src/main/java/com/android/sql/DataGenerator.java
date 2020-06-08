package com.android.sql;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.sql.activity.R;

public class DataGenerator {
    public static final int []mTabRes = new int[]{R.drawable.todo,R.drawable.data};
    public static final int []mTabResPressed = new int[]{R.drawable.todo1,R.drawable.data1};
    public static final String []mTabTitle = new String[]{"待办","数据统计"};

    public static Fragment[] getFragments(String from){
        Fragment fragments[] = new Fragment[2];
        fragments[0] = FragmentToDo.newInstance(from,"");
        fragments[1] = FragmentData.newInstance(from,"");
        return fragments;
    }

    /**
     * 获取Tab 显示的内容
     * @param context
     * @param position
     * @return
     */
    public static View getTabView(Context context, int position){
        View view = LayoutInflater.from(context).inflate(R.layout.tab,null);
        ImageView tabIcon = (ImageView) view.findViewById(R.id.tab_content_image);
        tabIcon.setImageResource(DataGenerator.mTabRes[position]);
        TextView tabText = (TextView) view.findViewById(R.id.tab_content_text);
        tabText.setText(mTabTitle[position]);
        return view;
    }
}
