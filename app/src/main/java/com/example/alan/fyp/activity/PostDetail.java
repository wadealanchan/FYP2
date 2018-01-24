package com.example.alan.fyp.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alan.fyp.Henson;
import com.example.alan.fyp.R;
import com.example.alan.fyp.databinding.ActivityPostdetailBinding;
import com.example.alan.fyp.model.model_conversation;
import com.example.alan.fyp.model.User;
import com.example.alan.fyp.viewModel.PostViewModel;
import com.example.alan.fyp.viewModel.UserViewModel;
import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

public class PostDetail extends BaseActivity implements ObservableScrollViewCallbacks {

    private static final float MAX_TEXT_SCALE_DELTA = 0.3f;

    private View mImageView;
    private View mOverlayView;
    private ObservableScrollView mScrollView;
    private TextView mTitleView;
    private View mFab;
    private int mActionBarSize;
    private int mFlexibleSpaceShowFabOffset;
    private int mFlexibleSpaceImageHeight;
    private int mFabMargin;
    private boolean mFabIsShown;
    private boolean mCircleImageViewIsShown;
    private TextView mUsernameView;
    private TextView mTimestampView;
    private de.hdodenhof.circleimageview.CircleImageView mCircleImageView;

    private FirebaseAuth mAuth;
    ActivityPostdetailBinding postdetailBinding ;
    PostViewModel postViewModel = new PostViewModel();
    UserViewModel userViewModel ;
    String username;
    String userimage;


    @InjectExtra String postUserId;
    @InjectExtra String PostId;
    @InjectExtra String postTtile;
    @InjectExtra String postDescription;
//    @InjectExtra String postImage;
    @InjectExtra String questionerId;
    @InjectExtra String timestamp;
    String answererId;


    FirebaseUser firebaseuser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postdetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_postdetail);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        answererId = firebaseuser.getUid();
        mAuth = FirebaseAuth.getInstance();

        User user = new User();
        user.setName(getIntent().getExtras().getString("user_name"));
        user.setImage(getIntent().getExtras().getString("user_image"));

        postViewModel.user.set(user);
        Dart.inject(this);
        postdetailBinding.setPostvmodel(postViewModel);
        postViewModel.title.set(postTtile);
        postViewModel.description.set(postDescription);
        //postViewModel.image.set(postImage);
        postViewModel.timestamp.set(timestamp);

        if(getIntent().getExtras().getString("post_image").equals("1"))
        {
            postViewModel.image.set(null);
        }
        else
        {
            postViewModel.image.set(getIntent().getExtras().getString("post_image"));
        }

        mFlexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);
        mFlexibleSpaceShowFabOffset = getResources().getDimensionPixelSize(R.dimen.flexible_space_show_fab_offset);
        mActionBarSize = getActionBarSize();
        mUsernameView = findViewById(R.id.detail_username);
        mTimestampView = findViewById(R.id.text_timestamp);
        mImageView = findViewById(R.id.image);
        mOverlayView = findViewById(R.id.overlay);
        mCircleImageView = findViewById(R.id.detail_avatar);
        mScrollView = findViewById(R.id.scroll);
        mScrollView.setScrollViewCallbacks(this);
        mTitleView = findViewById(R.id.title);
        mTitleView.setText(postTtile);
        setTitle(null);
        mFab = findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (postUserId.equals(firebaseuser.getUid())) {

                    Toast.makeText(PostDetail.this, "You are the questioner",
                            Toast.LENGTH_SHORT).show();

                } else {

                    FirebaseFirestore.getInstance().collection("conversation")
                            .whereEqualTo("postId",PostId)
                            .whereEqualTo("aid",firebaseuser.getUid())
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot documentSnapshots) {

                                    if (!documentSnapshots.isEmpty()) {
                                           passdata(v, documentSnapshots.getDocuments().get(0).getId(),1);
                                           Log.d("Postdetail conid", documentSnapshots.getDocuments().get(0).getId());

                                    } else {
                                        model_conversation c = new model_conversation();
                                        c.setAid(firebaseuser.getUid());
                                        c.setPostId(PostId);
                                        c.setPostUserId(postUserId);
                                        FirebaseFirestore.getInstance().collection("conversation")
                                                .add(c)
                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {
                                                        Log.d("", "DocumentSnapshot added with ID: " + documentReference.getId());
                                                        passdata(v, documentReference.getId(),1);
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w("", "Error adding document", e);
                                                    }
                                                });
                                    }

                                }
                            });


                }
            }
        });
        mFabMargin = getResources().getDimensionPixelSize(R.dimen.margin_standard);
        ViewHelper.setScaleX(mFab, 0);
        ViewHelper.setScaleY(mFab, 0);

        ScrollUtils.addOnGlobalLayoutListener(mScrollView, new Runnable() {
            @Override
            public void run() {
                mScrollView.scrollTo(0, 0);
                // If you'd like to start from scrollY == 0, don't write like this:
                //mScrollView.scrollTo(0, 0);
                // The initial scrollY is 0, so it won't invoke onScrollChanged().
                // To do this, use the following:
                onScrollChanged(0, false, false);

                // You can also achieve it with the following codes.
                // This causes scroll change from 1 to 0.
                //mScrollView.scrollTo(0, 1);
                //mScrollView.scrollTo(0, 0);
            }
        });

    }





    public void passdata(View v ,String conId, int number){


        switch(number) {
            case 0:
//                Intent intent =Henson.with(v.getContext()).gotoConversation()
//                        .conversationId(conId)
//                        .build();
//                v.getContext().startActivity(intent);
                break;
            case 1:
                Intent intent1 =Henson.with(v.getContext()).gotoChat2()
                        .conversationId(conId)
                        .targetUserName(postViewModel.user.get().getName())
                        .build();
                v.getContext().startActivity(intent1);
                break;

        }




    }



    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        // Translate overlay and image
        float flexibleRange = mFlexibleSpaceImageHeight - mActionBarSize;
        int minOverlayTransitionY = mActionBarSize - mOverlayView.getHeight();
        ViewHelper.setTranslationY(mOverlayView, ScrollUtils.getFloat(-scrollY, minOverlayTransitionY, 0));
        ViewHelper.setTranslationY(mImageView, ScrollUtils.getFloat(-scrollY / 2, minOverlayTransitionY, 0));

        // Change alpha of overlay
        ViewHelper.setAlpha(mOverlayView, ScrollUtils.getFloat((float) scrollY / flexibleRange, 0, 1));

        // Scale title text
        float scale = 1 + ScrollUtils.getFloat((flexibleRange - scrollY) / flexibleRange, 0, MAX_TEXT_SCALE_DELTA);
        ViewHelper.setPivotX(mTitleView, 0);
        ViewHelper.setPivotY(mTitleView, 0);
        ViewHelper.setScaleX(mTitleView, scale);
        ViewHelper.setScaleY(mTitleView, scale);



        // Translate title text
        int maxTitleTranslationY = (int) (mFlexibleSpaceImageHeight - mTitleView.getHeight() * scale);
        int titleTranslationY = maxTitleTranslationY - scrollY;
        ViewHelper.setTranslationY(mTitleView, titleTranslationY);

        //Scale username
        ViewHelper.setPivotX(mUsernameView, 0);
        ViewHelper.setPivotY(mUsernameView, 0);
        ViewHelper.setScaleX(mUsernameView, scale);
        ViewHelper.setScaleY(mUsernameView, scale);

        ViewHelper.setPivotX(mTimestampView, 0);
        ViewHelper.setPivotY(mTimestampView, 0);
        ViewHelper.setScaleX(mTimestampView, scale);
        ViewHelper.setScaleY(mTimestampView, scale);




        //Translate username text
        int maxUsernameTranslationY = 0;
        int UsernameTranslationY = maxUsernameTranslationY- scrollY;
        ViewHelper.setTranslationY(mUsernameView, UsernameTranslationY);

        Log.d("Postdetail", Integer.toString(UsernameTranslationY));

        //Translate timestamp
        int maxtimestampTranslationY =  1;
        int timestampTranslationY = maxtimestampTranslationY- scrollY ;
        ViewHelper.setTranslationY(mTimestampView, timestampTranslationY);
        Log.d("Postdetail  ", "timestampTranslationY:  " +String.valueOf( maxtimestampTranslationY));

        //Translate avatar

        int max_avatarTranslationY = 0;

        float avatarTranslationY =  max_avatarTranslationY -scrollY;
        ViewHelper.setTranslationY(mCircleImageView, avatarTranslationY);


        // Translate FAB
        int maxFabTranslationY = mFlexibleSpaceImageHeight - mFab.getHeight() / 2;
        float fabTranslationY = ScrollUtils.getFloat(
                -scrollY + mFlexibleSpaceImageHeight - mFab.getHeight() / 2,
                mActionBarSize - mFab.getHeight() / 2,
                maxFabTranslationY);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            // On pre-honeycomb, ViewHelper.setTranslationX/Y does not set margin,
            // which causes FAB's OnClickListener not working.
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mFab.getLayoutParams();
            lp.leftMargin = mOverlayView.getWidth() - mFabMargin - mFab.getWidth();
            lp.topMargin = (int) fabTranslationY;
            mFab.requestLayout();
        } else {
            ViewHelper.setTranslationX(mFab, mOverlayView.getWidth() - mFabMargin - mFab.getWidth());
            ViewHelper.setTranslationY(mFab, fabTranslationY);
        }

        // Show/hide FAB
        if (fabTranslationY < mFlexibleSpaceShowFabOffset) {
            hideFab();
        } else {
            showFab();
        }
    }

    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
    }

    private void showFab() {
        if (!mFabIsShown) {
            ViewPropertyAnimator.animate(mFab).cancel();
            ViewPropertyAnimator.animate(mFab).scaleX(1).scaleY(1).setDuration(200).start();
            mFabIsShown = true;
        }
    }

    private void hideFab() {
        if (mFabIsShown) {
            ViewPropertyAnimator.animate(mFab).cancel();
            ViewPropertyAnimator.animate(mFab).scaleX(0).scaleY(0).setDuration(200).start();
            mFabIsShown = false;
        }
    }



}
