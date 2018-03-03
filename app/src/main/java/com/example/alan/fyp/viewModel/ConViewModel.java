package com.example.alan.fyp.viewModel;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
                .targetUserName(this.user.get().getName())
                .build();
        intent1.putExtra("postObject", this.post.get());
        view.getContext().startActivity(intent1);
    }


    public void onSaveClick2(View view) {

        ConViewModel conViewModel = new ConViewModel();
        conViewModel.user.set(this.user.get());
        conViewModel.post.set(this.post.get());
        mBottomSheetDialogdialog = new BottomSheetDialog(view.getContext());
        DialogBottomSheetRequestStudentsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(view.getContext()), R.layout.dialog_bottom_sheet_request_students, null, false);
        View v = binding.getRoot();
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


        TextView txt_reject = v.findViewById(R.id.txt_reject);
        txt_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext(), R.style.AlertDialogStyle);
                builder.setMessage(v.getContext().getString(R.string.sure_reject));
                builder.setPositiveButton(v.getContext().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseFirestore.getInstance().collection("conversation").document(conId)
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        mBottomSheetDialogdialog.dismiss();
                                        Toast.makeText(v.getContext(), "Request Rejected", Toast.LENGTH_SHORT).show();
                                        ((Activity) (v.getContext())).finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        //Log.w(TAG, "Error deleting document", e);
                                    }
                                });
                    }
                });
                builder.setNegativeButton(v.getContext().getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                    }
                });
                builder.show();


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
