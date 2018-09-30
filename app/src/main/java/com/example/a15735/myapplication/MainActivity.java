package com.example.a15735.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button button;
    private lovelayout lovelayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        mhearviewlayout = (hearviewlayout) findViewById(R.id.heartview);
//        if (mhearviewlayout==null){
//            throw new RuntimeException("heart is null");
//        }
           lovelayout =findViewById(R.id.he);
        button = findViewById(R.id.but);
        button.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.but:
            lovelayout.addlove();
                break;
        }
    }
}
