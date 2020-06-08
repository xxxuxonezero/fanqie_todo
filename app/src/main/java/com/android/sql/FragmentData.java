package com.android.sql;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.sql.activity.R;
import com.android.sql.dao.MyDataBaseHelper;
import com.android.sql.dao.TaskDao;
import com.android.sql.domin.Task;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentData#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentData extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private List<Task> tasks;
    private int user_id;
    private PieView pieView;
    public static final int[] colors=new int[]{Color.rgb(	65,105,225),Color.rgb(255,0,255),Color.rgb(255,20,147),
            Color.rgb(220,20,60),Color.rgb(255,182,193),Color.rgb(127,255,170),Color.rgb(	0,255,255),
            Color.rgb(70,130,180),
            Color.rgb(	255,140,0),Color.rgb(255,255,0),Color.rgb(	0,100,0)};
    public FragmentData() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentData.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentData newInstance(String param1, String param2) {
        FragmentData fragment = new FragmentData();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_data, container, false);
        Intent intent = getActivity().getIntent();
        user_id=intent.getIntExtra("user_id",-1);
        Log.i("user_id", user_id + "");
        SQLiteDatabase sd = new MyDataBaseHelper(getContext(), "study.db", null, 2).getWritableDatabase();
        tasks=new TaskDao(sd).getTotal(user_id);
        Log.i("num",tasks.size()+"");
        pieView=view.findViewById(R.id.pieview);
        convert(tasks);
        pieView.setPieViewOnClickListener(new PieView.OnClickListener() {
            @Override
            public void onClick(PieItemBean pieItemBean) {
                Toast.makeText(getContext(),pieItemBean.name,Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }


    public void convert(List<Task> tasks){
        List<PieItemBean> list = new ArrayList<>();
        int count=0;
        for (Task task : tasks) {
            count+=task.getTotal_time();
        }
        int i=-1;
        for (Task task : tasks) {
            list.add(new PieItemBean(task.getName(), (float) (task.getTotal_time()*1.0/count*100),colors[(++i)%colors.length],task.getTotal_time()));
        }
        pieView.setData(list);
    }
}
