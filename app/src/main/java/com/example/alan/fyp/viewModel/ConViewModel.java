package com.example.alan.fyp.viewModel;

import android.content.Intent;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alan.fyp.Henson;
import com.example.alan.fyp.R;
import com.example.alan.fyp.databinding.DialogBottomSheetRequestStudentsBinding;
import com.example.alan.fyp.model.Post;
import com.example.alan.fyp.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

/**
 * Created by wadealanchan on 4/1/2018.
 */

public class ConViewModel {


    public final ObservableField<User> user = new ObservableField<User>();
    public final ObservableField<Post> post = new ObservableField<Post>();
    public final ObservableBoolean isExpanded = new ObservableBoolean(false);
    public String conId;
    BottomSheetDialog mBottomSheetDialogdialog;


    @BindingAdapter({"bind:imagesrc"})
    public static void loadImage(ImageView view, String image) {
        if (image != null)
            Picasso.with(view.getContext()).load(image).into(view);
        else
            Picasso.with(view.getContext()).load(R.drawable.ic_avatar_default).into(view);
    }


    public void onSaveClick(View view) {
        Log.d("ConViewModel", this.user.get().getName());
        Intent intent1 = Henson.with(view.getContext()).gotoChat2()
                .conversationId(conId)
                .postDescription(this.post.get().getDescription())
                .postTtile(this.post.get().getTitle())
                .targetUserName(this.user.get().getName())
                .build();
        view.getContext().startActivity(intent1);
    }


    public void onSaveClick2(View view) {

        ConViewModel conViewModel = new ConViewModel();
        conViewModel.user.set(this.user.get());
        conViewModel.post.set(this.post.get());
        mBottomSheetDialogdialog = new BottomSheetDialog(view.getContext());
        DialogBottomSheetRequestStudentsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(view.getContext()), R.layout.dialog_bottom_sheet_request_students, null, false);
        View v = binding.getRoot();
        //View v = View.inflate(view.getContext(), R.layout.dialog_bottom_sheet_request_students, null);
        mBottomSheetDialogdialog.setContentView(v);
        mBottomSheetDialogdialog.show();
        binding.setConversation(conViewModel);

        TextView txt_startchatting = v.findViewById(R.id.txt_startchatting);
        txt_startchatting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startChatting(v);
                mBottomSheetDialogdialog.dismiss();
            }
        });

    }

    public void startChatting(View view) {
        Log.d("ConViewModel", conId);
        FirebaseFirestore.getInstance().collection("conversation").document(conId).update("status", "chatting").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                 Log.d("conViewModel", "DocumentSnapshot successfully updated!");
                onSaveClick(view);


            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("conViewModel", "Error updating document", e);
                    }
                });

    }

    @BindingAdapter("drawablesrc")
    public static void setDrawableSrc(ImageView imageView, Drawable drawable) {
        imageView.setImageDrawable(drawable);

    }

    public void toggleExpanded() {
        if (this.isExpanded.get()) {
            this.isExpanded.set(false);
        } else {
            this.isExpanded.set(true);
        }
    }


}
