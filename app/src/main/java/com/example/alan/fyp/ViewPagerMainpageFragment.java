package com.example.alan.fyp;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alan.fyp.ListViewModel.PostListViewModel;
import com.example.alan.fyp.activity.Newpost;
import com.example.alan.fyp.databinding.FragmentMainpageBinding;
import com.example.alan.fyp.model.Post;
import com.example.alan.fyp.model.User;
import com.example.alan.fyp.util.QuestionData;
import com.example.alan.fyp.viewModel.PostViewModel;
import com.example.alan.fyp.viewModel.UserViewModel;
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * Created by wadealanchan on 5/1/2018.
 */

public class ViewPagerMainpageFragment extends BaseFragment {

   public final String TAG = "Mainpage: ";
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    Post post = new Post();
    PostListViewModel postList = new PostListViewModel();
    UserViewModel userViewModel = new UserViewModel();
    TextView textview;
    FirebaseUser firebaseuser = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference postsRef = db.collection("posts");
    FragmentMainpageBinding binding;
    public static final String ARG_SCROLL_Y = "ARG_SCROLL_Y";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mainpage, container, false);

        View view = binding.getRoot();





        Activity parentActivity = getActivity();
        final ObservableRecyclerView recyclerView = view.findViewById(R.id.scroll);
        recyclerView.setLayoutManager(new LinearLayoutManager(parentActivity));
        recyclerView.setHasFixedSize(false);
        //View headerView = LayoutInflater.from(parentActivity).inflate(R.layout.padding, null);
        //setDummyDataWithHeader(recyclerView, headerView);

        if (parentActivity instanceof ObservableScrollViewCallbacks) {
            // Scroll to the specified offset after layout
            Bundle args = getArguments();
            if (args != null && args.containsKey(ARG_SCROLL_Y)) {
                final int initialPosition = args.getInt(ARG_SCROLL_Y, 0);
                ScrollUtils.addOnGlobalLayoutListener(recyclerView, new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.scrollVerticallyToPosition(initialPosition);
                    }
                });
            }


            recyclerView.setTouchInterceptionViewGroup(parentActivity.findViewById(R.id.root));

            recyclerView.setScrollViewCallbacks((ObservableScrollViewCallbacks) parentActivity);
        }


        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newAct = new Intent(getActivity(),Newpost.class);
                startActivityForResult(newAct, 1);
            }
        });

     // QuestionData questionData = new QuestionData();
       // questionData.showcontent("1");
        showcontent();
        binding.setPostList(postList);

//        QuestionData.posts.addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<Post>>() {
//
//
//            @Override
//            public void onChanged(ObservableList<Post> posts) {
//
//                for (Post post : posts) {
//
//                    PostViewModel postViewModel = post.toViewModel();
//                    FirebaseFirestore db = FirebaseFirestore.getInstance();
//                        db.collection("Users").document(post.getUserId()).get().addOnCompleteListener(userInfotask -> {
//
//                            if (userInfotask.isSuccessful()) {
//
//                                postViewModel.user.set(userInfotask.getResult().toObject(User.class));
//                                postViewModel.user.get().id=userInfotask.getResult().getId();
//                            } else {
//                                postViewModel.user.set(null);
//                            }
//
//                        });
//
//                        postViewModel.post.set(post);
//                        postList.items.add(postViewModel);
//
//                }
//                binding.executePendingBindings();
//            }
//
//            @Override
//            public void onItemRangeChanged(ObservableList<Post> posts, int i, int i1) {
//
//            }
//
//            @Override
//            public void onItemRangeInserted(ObservableList<Post> posts, int i, int i1) {
//
//            }
//
//            @Override
//            public void onItemRangeMoved(ObservableList<Post> posts, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onItemRangeRemoved(ObservableList<Post> posts, int i, int i1) {
//
//            }
//        });

        return view;
    }

    public void showcontent(){

        FirebaseFirestore.getInstance().collection("posts").orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                postList.items.clear();
                for (DocumentSnapshot document : value) {
                    if(document.exists()) {
                        Post post = document.toObject(Post.class);
                        PostViewModel postViewModel = post.toViewModel();
                        postViewModel.PostId = document.getId();
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("Users").document(post.getUserId()).get().addOnCompleteListener(userInfotask -> {

                            if (userInfotask.isSuccessful()) {

                                postViewModel.user.set(userInfotask.getResult().toObject(User.class));
                                postViewModel.user.get().id=userInfotask.getResult().getId();
                            } else {
                                postViewModel.user.set(null);
                            }

                        });

                        postViewModel.post.set(post);
                        postList.items.add(postViewModel);
                    }
                }
                binding.executePendingBindings();
            }
        });

   }











}
