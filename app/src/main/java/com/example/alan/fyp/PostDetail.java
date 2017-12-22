package com.example.alan.fyp;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.alan.fyp.databinding.ActivityPostdetailBinding;
import com.example.alan.fyp.model.User;
import com.example.alan.fyp.viewModel.PostViewModel;
import com.example.alan.fyp.viewModel.UserViewModel;
import com.google.firebase.auth.FirebaseAuth;

public class PostDetail extends AppCompatActivity {

    private FirebaseAuth mAuth;
    ActivityPostdetailBinding postdetailBinding ;
    PostViewModel postViewModel = new PostViewModel();
    UserViewModel userViewModel ;
    String username;
    String userimage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postdetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_postdetail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.postdetailfab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mAuth = FirebaseAuth.getInstance();

        postViewModel.title.set(getIntent().getExtras().getString("title"));
        postViewModel.description.set(getIntent().getExtras().getString("description"));
        postViewModel.image.set(getIntent().getExtras().getString("image"));

        User user = new User();
        user.setName(getIntent().getExtras().getString("user_name"));
        user.setImage(getIntent().getExtras().getString("user_image"));

        Log.d("Postdetail: ", getIntent().getExtras().getString("user_name")+ "    "+getIntent().getExtras().getString("user_image"));
//        postViewModel.user.get().setName(getIntent().getExtras().getString("user_name"));
//        postViewModel.user.get().setImage(getIntent().getExtras().getString("user_image"));

        postViewModel.user.set(user);



        postdetailBinding.setPostvmodel(postViewModel);

    }





}
