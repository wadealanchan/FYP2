package com.example.alan.fyp.viewModel;

import android.content.Intent;
import android.databinding.BindingAdapter;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.alan.fyp.Henson;
import com.example.alan.fyp.R;
import com.example.alan.fyp.model.Post;
import com.example.alan.fyp.model.User;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wadealanchan on 4/1/2018.
 */

public class ConViewModel {


    public final ObservableField<User> user = new ObservableField<User>();
    public final ObservableField<Post> post = new ObservableField<Post>();
    public String conId;



    @BindingAdapter({"bind:imagesrc"})
    public static void loadImage(ImageView view, String image) {
        if(image!=null)
            Picasso.with(view.getContext()).load(image).into(view);
        else
            Picasso.with(view.getContext()).load(R.drawable.ic_avatar_default).into(view);
    }




    public void onSaveClick(View view){
        Log.d("ConViewModel",this.user.get().getName());
        Intent intent1 =Henson.with(view.getContext()).gotoChat2()
                .conversationId(conId)
                .targetUserName(this.user.get().getName())
                .build();
        view.getContext().startActivity(intent1);
    }


    public String getDateFormatted(){
        Log.d("Conview model:", post.get().getDate().toString());
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String dateString = sdf.format(post.get().getDate());
        return dateString;
    }




}
