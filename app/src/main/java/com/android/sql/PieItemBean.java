package com.android.sql;

public class PieItemBean {
    public String name;
    public float value;
    public int color;
    public int time;

    public PieItemBean(String name, float value, int color,int time) {
        this.name = name;
        this.value = value;
        this.color = color;
        this.time=time;
    }
}
