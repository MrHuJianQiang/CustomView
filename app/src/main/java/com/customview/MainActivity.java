package com.customview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.customview.widget.CustomTextView;

public class MainActivity extends AppCompatActivity {
    private CustomTextView custom_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }
}
