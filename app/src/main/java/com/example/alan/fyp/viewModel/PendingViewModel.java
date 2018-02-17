package com.example.alan.fyp.viewModel;

import android.databinding.BindingAdapter;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.graphics.drawable.Drawable;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.widget.ImageView;

import com.example.alan.fyp.R;
import com.example.alan.fyp.model.Post;
import com.example.alan.fyp.model.User;
import com.squareup.picasso.Picasso;

/**
 * Created by wadealanchan on 13/2/2018.
 */

public class PendingViewModel {


    public final ObservableField<User> user = new ObservableField<User>();
    public final ObservableField<Post> post = new ObservableField<Post>();
    public final ObservableBoolean isExpanded = new ObservableBoolean(false);
    public String conId;
    BottomSheetDialog mBottomSheetDialogdialog;



    @BindingAdapter({"bind:imagesrc"})
    public static void loadImage(ImageView view, String image) {
        if(image!=null)
            Picasso.with(view.getContext()).load(image).into(view);
        else
            Picasso.with(view.getContext()).load(R.drawable.ic_avatar_default).into(view);
    }


    public void onSaveClick2(View view){

        mBottomSheetDialogdialog = new BottomSheetDialog(view.getContext());
        // DialogBottomSheetBinding binding= DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_bottom_sheet, null, false);
//        DialogBottomSheetBinding binding = DataBindingUtil.inflate(LayoutInflater.from(view.getContext()), R.layout.dialog_bottom_sheet_request_students, null, false);
//        //View view = getLayoutInflater().inflate(R.layout.dialog_bottom_sheet, null);
//        view = binding.getRoot();
        View v = View.inflate(view.getContext(), R.layout.dialog_bottom_sheet_request_students, null);
        mBottomSheetDialogdialog.setContentView(v);
        mBottomSheetDialogdialog.show();
        //binding.setPostvmodel();
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
