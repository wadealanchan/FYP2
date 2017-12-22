package com.example.alan.fyp.viewModel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.alan.fyp.PostDetail;
import com.example.alan.fyp.R;
import com.example.alan.fyp.model.Post;
import com.example.alan.fyp.model.User;
import com.google.firebase.database.Exclude;
import com.squareup.picasso.Picasso;

/**
 * Created by wadealanchan on 11/11/2017.
 */

public class PostViewModel  {

    public String firePostId;
    public final ObservableField<String> title = new ObservableField<String>();
    public final ObservableField<String> description = new ObservableField<String>();
    public final ObservableField<String> image = new ObservableField<String>();
    public final ObservableField<User> user = new ObservableField<User>();
    public final ObservableBoolean isReadmore = new ObservableBoolean(false);


    public PostViewModel() {
    }


    public PostViewModel(String Title,String Description,String image, User user)
    {
        this.title.set(Title);
        this.description.set(Description);
        this.image.set(image);
        this.user.set(user);
    }

    @BindingAdapter({"bind:imagesrc"})
    public static void loadImage(ImageView view, String image) {
        if(image!=null)
            Picasso.with(view.getContext()).load(image).into(view);
        else
            Picasso.with(view.getContext()).load(R.drawable.ic_avatar_default).into(view);
    }


    public void onSaveClick(View view){
        Intent intent = new Intent();
        intent.setClass(view.getContext(), PostDetail.class);
        intent.putExtra("title", this.title.get());
        intent.putExtra("description", this.description.get());
        intent.putExtra("image", this.image.get());

        Log.e("OnSaveClick",this.user.get().getName());
        Log.e("OnSaveClick",this.user.get().getImage());
        intent.putExtra("user_name", this.user.get().getName());
        intent.putExtra("user_image", this.user.get().getImage());

        view.getContext().startActivity(intent);
    }

    public void toggleReadmore() {
        if (this.isReadmore.get()) {
            this.isReadmore.set(false);
        } else {
            this.isReadmore.set(true);
        }
    }


}
