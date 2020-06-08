package com.android.sql.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.android.sql.MyApplication;

@SuppressLint("AppCompatCustomView")
public class ClockTextView extends TextView {

    public ClockTextView(Context context) {
        super(context);
        setTypeface(MyApplication.getInstace().getTypeface());
    }

    public ClockTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setTypeface(MyApplication.getInstace().getTypeface());
    }

    public ClockTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTypeface(MyApplication.getInstace().getTypeface());
    }

    public ClockTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setTypeface(MyApplication.getInstace().getTypeface());
    }
}
