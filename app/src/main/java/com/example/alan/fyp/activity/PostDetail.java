package com.example.alan.fyp.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.alan.fyp.Henson;
import com.example.alan.fyp.R;
import com.example.alan.fyp.databinding.ActivityPostdetailBinding;
import com.example.alan.fyp.model.Post;
import com.example.alan.fyp.model.User;
import com.example.alan.fyp.viewModel.PostViewModel;
import com.example.alan.fyp.viewModel.UserViewModel;
import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

public class PostDetail extends AppCompatActivity {

    private FirebaseAuth mAuth;
    ActivityPostdetailBinding postdetailBinding ;
    PostViewModel postViewModel = new PostViewModel();
    UserViewModel userViewModel ;
    String username;
    String userimage;

    @InjectExtra String PostId;

    @InjectExtra String postTtile;


    @InjectExtra String postDescription;


    @InjectExtra String postImage;


    @InjectExtra String questionerId;
    String answererId;

    FirebaseUser firebaseuser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postdetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_postdetail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        answererId = firebaseuser.getUid();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.postdetailfab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mAuth = FirebaseAuth.getInstance();

        Dart.inject(this);


        postViewModel.title.set(postTtile);
        postViewModel.description.set(postDescription);
        postViewModel.image.set(postImage);

        User user = new User();
        user.setName(getIntent().getExtras().getString("user_name"));
        user.setImage(getIntent().getExtras().getString("user_image"));

        postViewModel.user.set(user);


        ButterKnife.bind(this);
        postdetailBinding.setPostvmodel(postViewModel);

    }


    @Optional
    @OnClick(R.id.detail_thumbup)
    public void Interested(ImageButton button) {


        Log.d("PostDetail", PostId + questionerId);

        Intent intent = new Intent(this,Chat.class);
//        Intent intent = Henson.with(button.getContext()).gotoChat()
//                .postId(PostId)
//                .questioner(questionerId)
//                .build();

        intent.putExtra("PostId", this.PostId);
        intent.putExtra("Questioner", this.questionerId);

        button.getContext().startActivity(intent);

    }




}
