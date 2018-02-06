package com.example.alan.fyp.viewModel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.alan.fyp.Henson;
import com.example.alan.fyp.activity.PostDetail;
import com.example.alan.fyp.R;
import com.example.alan.fyp.model.Post;
import com.example.alan.fyp.model.User;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

/**
 * Created by wadealanchan on 11/11/2017.
 */

public class PostViewModel extends BaseObservable {
    public String PostId;
    public Post post;
    public final ObservableField<String> title = new ObservableField<String>();
    public final ObservableField<String> description = new ObservableField<String>();
    public final ObservableField<String> image = new ObservableField<String>();
    public final ObservableField<User> user = new ObservableField<User>();
    public final ObservableField<String> timestamp = new ObservableField<String>();
    public final ObservableBoolean isReadmore = new ObservableBoolean(false);
    private ArrayAdapter<CharSequence> subjectAdapter;


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
        else {
            Picasso.with(view.getContext()).load(R.drawable.ic_avatar_default).into(view);
        }
    }


    public void onSaveClick(View view){
        Intent intent = Henson.with(view.getContext()).gotoPostDetail()
                .PostId(this.PostId)
                .postDescription(this.description.get())
//                .postImage(this.image!=null ? this.image.get():"")
                .postTtile(this.title.get())
                .postUserId(this.post.getUserId())
                .questionerId(this.post.getUserId())
                .timestamp(this.timestamp.get())
                .build();
        if (this.user.get() != null) {
            intent.putExtra("user_id", this.user.get().id);
            intent.putExtra("user_name", this.user.get().getName());
            intent.putExtra("user_image", this.user.get().getImage());
        }

        if(this.image.get()!=null)
        {

            intent.putExtra("post_image", this.image.get());
        }
        else
        {

            intent.putExtra("post_image", "1");
        }
        view.getContext().startActivity(intent);
    }

    public void toggleReadmore() {
        if (this.isReadmore.get()) {
            this.isReadmore.set(false);
        } else {
            this.isReadmore.set(true);
        }
    }

    @Bindable
    public ArrayAdapter<CharSequence> getsubjectAdapter(){
        return this.subjectAdapter;
    }

    public void setSubjectAdapter(ArrayAdapter<CharSequence> subjectAdapter)
    {
        this.subjectAdapter=subjectAdapter;
    }


    @BindingAdapter("spinnerAdapter")
    public static void setSpinnerAdapter(Spinner sp, ArrayAdapter<CharSequence> adapter) {
        sp.setAdapter(adapter);

    }


    public void onImageClick(View view) {
        Intent intent = Henson.with(view.getContext()).gotoImageDetailActivity()
                .imageuri(this.image.get())
                .build();
        view.getContext().startActivity(intent);
    }

    @BindingAdapter({"bind:imagesrc2"})
    public static void loadImage(PhotoView photoView , String image) {
        if(image!=null)
            Picasso.with(photoView.getContext()).load(image).into(photoView);
        else
            Picasso.with(photoView.getContext()).load(R.drawable.ic_avatar_default).into(photoView);
    }





}
