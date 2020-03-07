package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.trainingfaces.MESSAGE";
    private static final String TAG = MainActivity.class.getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private static int RESULT_LOAD_IMAGE = 1;
    public void startTraining(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, trainingActivity.class);
        startActivity(intent);

    }


}


