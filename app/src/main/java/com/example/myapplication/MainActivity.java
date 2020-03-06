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
    private static final String TAG = MainActivity.class.getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        LinearLayout linearLayout= new LinearLayout(this);
//        linearLayout.setOrientation(LinearLayout.VERTICAL);

        //ImageView firstImage = (ImageView) findViewById(R.id.imageView2);
        //String titileName =


//        for (int j = 1; j < 10; j++) {
//
//            //ImageView Setup
//            ImageView imageView = new ImageView(this);
//            //setting image resource
//            imageView.setImageResource(R.drawable.angel_gao);
//
//
//            //int imageResource = getResources().getIdentifier("@drawable/angel_gao", null, this.getPackageName());
//            int imageResource = getResources().getIdentifier("d002_p00"+j, "drawable",this.getPackageName();
//            imageView.setImageResource(imageResource);
//
//            //setting image position
//            imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT));
//
//            linearLayout.addView(imageView);
//
//       }
//        //ImageView Setup
//        ImageView imageView = new ImageView(this);
//        //setting image resource
//        imageView.setImageResource(R.drawable.angel_gao);
//
//
//        int imageResource = getResources().getIdentifier("@drawable/angel_gao", null, this.getPackageName());
//        //int imageResource = getResources().getIdentifier("d002_p00"+j, "drawable",this.getPackageName();
//        Log.d(TAG,"id is " + imageResource);
//        imageView.setImageResource(imageResource);
//
//        //setting image position
//        imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT));
//
//        linearLayout.addView(imageView);
//        setContentView(linearLayout);
    }

    private static int RESULT_LOAD_IMAGE = 1;
    public void startTraining(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, trainingActivity.class);
        startActivity(intent);

//        Intent i = new Intent(
//                Intent.ACTION_PICK,
//                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, this, trainingActivity.class);
//
//        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }


}


