package com.example.alan.fyp.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.alan.fyp.R;
import com.example.alan.fyp.databinding.ActivityQuestionDetailBinding;
import com.example.alan.fyp.model.Post;
import com.example.alan.fyp.viewModel.PostViewModel;
import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;

import butterknife.BindView;
import butterknife.ButterKnife;


public class QuestionDetailActivity extends AppCompatActivity {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    ActivityQuestionDetailBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_question_detail);
        ButterKnife.bind(this);
        Intent i = getIntent();
        Post post = (Post)i.getSerializableExtra("postObject");

        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        PostViewModel postViewModel = new PostViewModel();
        postViewModel.post.set(post);









      binding.setPostViewModel(postViewModel);
    }

}
