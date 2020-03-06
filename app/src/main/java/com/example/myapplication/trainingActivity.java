package com.example.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
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

public class trainingActivity extends AppCompatActivity {


    private static int RESULT_LOAD_IMAGE = 1;
    private ArrayList<String> images;

    private static final String TAG = trainingActivity.class.getName();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

//        Intent i = new Intent(
//                Intent.ACTION_PICK,
//                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//
//        startActivityForResult(i, RESULT_LOAD_IMAGE);

        //try to get all images from album
        Uri uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        ArrayList<String> listOfAllImages = new ArrayList<String>();
        String absolutePathOfImage = null;
        ArrayList<String> ten_uri_string = new ArrayList<>();


        Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);


        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA));

            listOfAllImages.add(absolutePathOfImage);
        }
        Log.d(TAG, "im here");
        Log.d(TAG, "Arraylistsize: "+ listOfAllImages.size());

        //generate 10 random number
        Set<Integer> number_set = generateRandomNumer(listOfAllImages.size());
        Log.d(TAG, "number_set size is " + number_set.size());

        for(Integer i: number_set){
            Log.d(TAG, "random number is :" + i);
            String new_path = listOfAllImages.get(i);
            ten_uri_string.add(new_path);
            Log.d(TAG, "path: "+ new_path);
        }


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

            }
        });




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
//
//        /**
//         * Getting All Images Path.
//         *
//         * @param activity
//         *            the activity
//         * @return ArrayList with images Path
//         */
//        private ArrayList<String> getAllShownImagesPath(Activity activity, ArrayList<>) {
//            Uri uri;
//            Cursor cursor;
//            int column_index_data, column_index_folder_name;
//            ArrayList<String> listOfAllImages = new ArrayList<String>();
//            String absolutePathOfImage = null;
//            uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//
//            String[] projection = { MediaStore.MediaColumns.DATA,
//                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME };
//
//            cursor = activity.getContentResolver().query(uri, projection, null,
//                    null, null);
//
//            column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
//            column_index_folder_name = cursor
//                    .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
//            while (cursor.moveToNext()) {
//                absolutePathOfImage = cursor.getString(column_index_data);
//
//                listOfAllImages.add(absolutePathOfImage);
//            }
//            return listOfAllImages;
//        }
    }
}
