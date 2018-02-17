package com.example.alan.fyp;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alan.fyp.ListViewModel.ConListViewModel;
import com.example.alan.fyp.ListViewModel.PendingListViewModel;
import com.example.alan.fyp.databinding.FragmentConversationBinding;
import com.example.alan.fyp.model.Post;
import com.example.alan.fyp.model.User;
import com.example.alan.fyp.model.model_conversation;
import com.example.alan.fyp.viewModel.ConViewModel;
import com.example.alan.fyp.viewModel.PendingViewModel;
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;


public class ViewPagerConversationFragment extends BaseFragment {

    public static final String ARG_INITIAL_POSITION = "ARG_INITIAL_POSITION";
    FragmentConversationBinding binding;
    public final String TAG = "Conversation: ";
    FirebaseUser firebaseuser = FirebaseAuth.getInstance().getCurrentUser();
    ConListViewModel conListViewModel = new ConListViewModel();
    PendingListViewModel pendingListViewModel = new PendingListViewModel();
    PendingViewModel pendingViewModel = new PendingViewModel();
    ConViewModel conViewModel = new ConViewModel();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_conversation, container, false);

        View view = binding.getRoot();



        Activity parentActivity = getActivity();
        final ObservableRecyclerView recyclerView = view.findViewById(R.id.scroll);
        recyclerView.setLayoutManager(new LinearLayoutManager(parentActivity));
        recyclerView.setHasFixedSize(false);

//        final ObservableRecyclerView recyclerView2 = view.findViewById(R.id.pendingList);
//        recyclerView2.setLayoutManager(new LinearLayoutManager(parentActivity));
//        recyclerView2.setHasFixedSize(false);



        if (parentActivity instanceof ObservableScrollViewCallbacks) {
            // Scroll to the specified offset after layout
            Bundle args = getArguments();
            if (args != null && args.containsKey(ARG_INITIAL_POSITION)) {
                final int initialPosition = args.getInt(ARG_INITIAL_POSITION, 0);
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




        FirebaseFirestore.getInstance().collection("conversation")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "listen:error", e);
                            return;
                        }

                        pendingListViewModel.items.clear();
                        conListViewModel.items.clear();
                        for (DocumentSnapshot dc : value) {
                            model_conversation model_conversation = dc.toObject(com.example.alan.fyp.model.model_conversation.class);

                            if(firebaseuser!=null && !model_conversation.isChatIsOver()) {

                                if (model_conversation.getAid().equals(firebaseuser.getUid())) {

                                    if(!model_conversation.getStatus().equals("pending")) {
                                    ConViewModel conViewModel = new ConViewModel();
                                    conViewModel.conId = dc.getId();
                                    getUserInfo(conViewModel, model_conversation.getPostUserId());
                                    getPostInfo(conViewModel, model_conversation.getPostId());
                                    conListViewModel.items.add(conViewModel);


                                    }else {
                                        ConViewModel conViewModel = new ConViewModel();
                                        conViewModel.conId = dc.getId();
                                        getUserInfo(conViewModel, model_conversation.getAid());
                                        getPostInfo(conViewModel, model_conversation.getPostId());
                                        pendingListViewModel.items.add(conViewModel);

                                    }
                                }

                                if (model_conversation.getPostUserId().equals(firebaseuser.getUid())) {
                                    if(!model_conversation.getStatus().equals("pending")) {
                                        ConViewModel conViewModel = new ConViewModel();
                                        conViewModel.conId = dc.getId();
                                        getUserInfo(conViewModel, model_conversation.getAid());
                                        getPostInfo(conViewModel, model_conversation.getPostId());
                                        conListViewModel.items.add(conViewModel);


                                } else {

                                        ConViewModel conViewModel = new ConViewModel();
                                        conViewModel.conId = dc.getId();
                                        getUserInfo(conViewModel, model_conversation.getAid());
                                        getPostInfo(conViewModel, model_conversation.getPostId());
                                        pendingListViewModel.items.add(conViewModel);

                                    }

                                }
                            }
                        }
                        binding.executePendingBindings();
                    }

                });



        binding.setConList(conListViewModel);
        binding.setPendingList(pendingListViewModel);
        binding.setConversation(conViewModel);


        return view;
    }


    public void getUserInfo(ConViewModel viewModel, String aid) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Users").document(aid).get().addOnCompleteListener(userInfotask -> {

            if (userInfotask.isSuccessful()) {

                viewModel.user.set(userInfotask.getResult().toObject(User.class));
            } else {
                viewModel.user.set(null);
            }

        });
    }

    public void getPostInfo(ConViewModel viewModel, String postid) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("posts").document(postid)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()) {
                    viewModel.post.set(documentSnapshot.toObject(Post.class));
                }
            }
        });
    }







}


