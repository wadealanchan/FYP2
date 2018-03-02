package com.example.alan.fyp.viewModel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.databinding.ObservableField;
import android.graphics.drawable.Drawable;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.alan.fyp.R;
import com.squareup.picasso.Picasso;

/**
 * Created by wadealanchan on 30/10/2017.
 */

public class UserViewModel extends BaseObservable {
    public ObservableField<String> displayName = new ObservableField<String>();
    public ObservableField<String> password = new ObservableField<String>();
    public ObservableField<String> email = new ObservableField<String>();
    public ObservableField<String> Image = new ObservableField<String>();
    public ObservableField<String> passwordAgain = new ObservableField<String>();
    public ObservableField<String> userType = new ObservableField<String>();
    private ArrayAdapter<CharSequence> subjectAdapter;

    public UserViewModel() {
        this.displayName.set("");
        this.password.set("");
        this.email.set("");
        this.Image.set("");
    }

    public UserViewModel(String displayName, String password, String email, String image) {
        this.displayName.set(displayName);
        this.password.set(password);
        this.email.set(email);
        this.Image.set(image);
    }


    @BindingAdapter({"bind:imageUrl"})
    public static void loadImage(ImageView view, String image) {
        if(image!=null)
        Picasso.with(view.getContext()).load(image).into(view);
        else
            Picasso.with(view.getContext()).load(R.drawable.ic_avatar_default).into(view);
    }


    @Bindable
    public ArrayAdapter<CharSequence> getsubjectAdapter() {
        return this.subjectAdapter;
    }

    public void setSubjectAdapter(ArrayAdapter<CharSequence> subjectAdapter) {
        this.subjectAdapter = subjectAdapter;
    }

    @BindingAdapter("spinnerAdapter")
    public static void setSpinnerAdapter(Spinner sp, ArrayAdapter<CharSequence> adapter) {
        sp.setAdapter(adapter);

    }




}
