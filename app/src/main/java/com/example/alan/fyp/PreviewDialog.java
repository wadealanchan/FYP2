package com.example.alan.fyp;

import android.app.DialogFragment;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alan.fyp.databinding.FragmentPreviewDialogBinding;
import com.example.alan.fyp.viewModel.PostViewModel;
import com.f2prateek.dart.InjectExtra;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

public class PreviewDialog extends DialogFragment {

    PostViewModel postViewModel = new PostViewModel();
    FragmentPreviewDialogBinding binding;
    String imageuri;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_preview_dialog, container, false);
        View view = binding.getRoot();
        binding.setPost(postViewModel);
        getDialog().setTitle("Simple Dialog");
        imageuri = getArguments().getString("imageuri");

        Log.d("Fragemnt",imageuri );

        PhotoView photoView = view.findViewById(R.id.photo_view);
        loadImage(photoView,imageuri);
        return view;
    }


    public static void loadImage(PhotoView photoView , String image) {
        if(image!=null)
            Picasso.with(photoView.getContext()).load(image).into(photoView);
        else
            Picasso.with(photoView.getContext()).load(R.drawable.ic_avatar_default).into(photoView);
    }
}
