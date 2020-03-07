package com.example.myapplication;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static com.example.myapplication.MainActivity.EXTRA_MESSAGE;


public class trainingActivity extends AppCompatActivity {


    private static int RESULT_LOAD_IMAGE = 1;
    private ArrayList<String> images;
    private int training_cycle;
    ArrayList<String> listOfAllImages = new ArrayList<String>();
    String training_accuracy_message;
    private float training_accuracy;
    private String this_round_correct_image;
    private String this_round_correct_image_path;
    private int correct_picks;
    private long time_started;
    private long time_elapsed;

    private static final String TAG = trainingActivity.class.getName();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);
        training_cycle = 0;
        correct_picks = 0;
        time_started = SystemClock.elapsedRealtime();

        //try to get all images from album
        Uri uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        String absolutePathOfImage = null;
        ArrayList<String> ten_uri_string = new ArrayList<>();


        Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);


        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA));

            listOfAllImages.add(absolutePathOfImage);
        }
        Log.d(TAG, "im here");
        Log.d(TAG, "Arraylistsize: "+ listOfAllImages.size());

        refreshPage();

//        //generate 10 random number
//        Set<Integer> number_set = generateRandomNumer(listOfAllImages.size());
//        Log.d(TAG, "number_set size is " + number_set.size());
//
//        for(Integer i: number_set){
//            Log.d(TAG, "random number is :" + i);
//            String new_path = listOfAllImages.get(i);
//            ten_uri_string.add(new_path);
//            Log.d(TAG, "path: "+ new_path);
//        }


        //imageShowInGrid(ten_uri_string);

    }

    private void imageShowInGrid(ArrayList<String> ten_uri_string){
        //add random pick 10 image to the grid view
        GridView gallery = (GridView) findViewById(R.id.galleryGridView);

        gallery.setAdapter(new ImageAdapter(this, ten_uri_string));
        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                if (null != images && !images.isEmpty())
                    Toast.makeText(
                            getApplicationContext(),
                            "position " + position + " " + images.get(position),
                            300).show();
                ;
                //get image name that user chose
                String selected_image = getImageName(images.get(position));
                if(this_round_correct_image.equals(selected_image)){
                    correct_picks++;
                }
                //pick wrong, show the correct image
                else{
                    showUpCorrectImg(this_round_correct_image_path);
                }

                training_cycle++;
                if(training_cycle < 15){
                    Log.d(TAG, "cycle number is " + training_cycle);
                    refreshPage();
                }
                else{
                    Log.d(TAG, "cycle up to 15 times, cycle number is " + training_cycle);
                    //show training result
                    Intent intent = new Intent(trainingActivity.this, result_analysis.class);
                    training_accuracy = (float) correct_picks*100/15;
                    time_elapsed = SystemClock.elapsedRealtime() - time_started;
                    String acc_message = "training accuracy is " + training_accuracy +"%"+ '\n'+ "Time: " + (float)time_elapsed/1000 +" seconds";
                    intent.putExtra(EXTRA_MESSAGE, acc_message);
//                    String time_message = ;
//                    intent.putExtra(EXTRA_MESSAGE, time_message);
                    startActivity(intent);
                }
            }
        });

    }

    private void showUpCorrectImg(String img_path){
        Log.d(TAG, "correct image path is "+ img_path);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        Log.v("width", width+"");

        Dialog builder = new Dialog(this);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                //
            }
        });

        ImageView imageView = new ImageView(this);
        Bitmap myBitmap = BitmapFactory.decodeFile(img_path);
        imageView.setImageBitmap(myBitmap);
        builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        builder.getWindow().setLayout((6*width)/7,(1*height)/2);
        builder.show();
    }

    private String getImageName(String picturePath){
        File f = new File(picturePath);
        String imageName = f.getName();
        return imageName;
    }

    private void refreshPage(){
        ArrayList<String> ten_uri_string = new ArrayList<>();

        Set<Integer> number_set = generateRandomNumer(listOfAllImages.size());
        Log.d(TAG, "number_set size is " + number_set.size());

        for(Integer i: number_set){
            Log.d(TAG, "random number is :" + i);
            String new_path = listOfAllImages.get(i);
            ten_uri_string.add(new_path);
            Log.d(TAG, "path: "+ new_path);
        }

        //randomly choose one in this 10 images
        Random rand = new Random();
        int rand_int = rand.nextInt(10);

        this_round_correct_image_path = ten_uri_string.get(rand_int);
        this_round_correct_image = getImageName(this_round_correct_image_path);

        TextView textView = findViewById(R.id.displayName);
        textView.setText(this_round_correct_image);

        imageShowInGrid(ten_uri_string);
    }
    protected Set<Integer> generateRandomNumer(int array_size){
        Random rand = new Random();
        Set<Integer> hash_set = new HashSet<Integer>();
        while(hash_set.size() < 10){
            int rand_int = rand.nextInt(array_size);
            hash_set.add(rand_int);
        }
        return hash_set;

    }
    //@Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        //Intent intent = getIntent();
//
//        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
//            Uri selectedImage = data.getData();
//            String[] filePathColumn = { MediaStore.Images.Media.DATA };
//
//            Cursor cursor = getContentResolver().query(selectedImage,
//                    filePathColumn, null, null, null);
//            cursor.moveToFirst();
//
//            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//            String picturePath = cursor.getString(columnIndex);
//
//
//            ImageView imageView = (ImageView) findViewById(R.id.imgView);
//            Bitmap bitmap= null;
//
//            try {
//                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage));
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//            //imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
//            imageView.setImageBitmap(bitmap);
////            Uri imageUri= ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cursor.getInt(columnIndex));
////
////            // now that you have the media URI, you can decode it to a bitmap
////            try {
////                ParcelFileDescriptor pfd = this.getContentResolver().openFileDescriptor(imageUri, "r");
////                if (pfd != null) {
////                    imageView.setImageBitmap(BitmapFactory.decodeFileDescriptor(pfd.getFileDescriptor()));
////                }
////            } catch (IOException ex) {
////                Log.d(TAG, "bitmap failed");
////            }
//
//            cursor.close();
//        }


    //}


    //private ArrayList<String> images = new ArrayList<String>();

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_training);
//
//        Intent intent = getIntent();
//
//        GridView gallery = (GridView) findViewById(R.id.galleryGridView);
//
//        gallery.setAdapter(new ImageAdapter(this));
//
//        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> arg0, View arg1,
//                                    int position, long arg3) {
//                if (null != images && !images.isEmpty())
//                    Toast.makeText(
//                            getApplicationContext(),
//                            "position " + position + " " + images.get(position),
//                            300).show();
//                ;
//
//            }
//        });
//    }

//    /**
//     * The Class ImageAdapter.
//     */
    private class ImageAdapter extends BaseAdapter {

        /** The context. */
        private Activity context;

        /**
         * Instantiates a new image adapter.
         *
         * @param localContext
         *            the local context
         */
        public ImageAdapter(Activity localContext, ArrayList<String> paths) {
            context = localContext;
            images = paths;
        }

        public int getCount() {
            return images.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            ImageView picturesView;
            if (convertView == null) {
                picturesView = new ImageView(context);
                picturesView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                picturesView
                        .setLayoutParams(new GridView.LayoutParams(270, 270));

            } else {
                picturesView = (ImageView) convertView;
            }

            Glide.with(context).load(images.get(position))
                    .placeholder(new ColorDrawable(Color.BLACK)).centerCrop()
                    .into(picturesView);

            return picturesView;
        }
    }
}
