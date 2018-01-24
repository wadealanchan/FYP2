package com.example.alan.fyp.photoeditor;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.alan.fyp.R;
import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

public class ImageDetailActivity extends AppCompatActivity {


    @InjectExtra String imageuri;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Dart.inject(this);

        Log.d("ImageDetailActivity",imageuri);
        PhotoView photoView = findViewById(R.id.photo_view);
        loadImage(photoView,imageuri);
    }



    public static void loadImage(PhotoView photoView , String image) {
        if(image!=null)
            Picasso.with(photoView.getContext()).load(image).into(photoView);
        else
            Picasso.with(photoView.getContext()).load(R.drawable.ic_avatar_default).into(photoView);
    }






}
