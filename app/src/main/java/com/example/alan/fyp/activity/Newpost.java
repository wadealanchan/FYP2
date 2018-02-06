package com.example.alan.fyp.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.alan.fyp.PreviewDialog;
import com.example.alan.fyp.R;
import com.example.alan.fyp.databinding.ActivityNewpostBinding;
import com.example.alan.fyp.model.Post;
import com.example.alan.fyp.photoeditor.MediaActivity;
import com.example.alan.fyp.photoeditor.PhotoEditorActivity;
import com.example.alan.fyp.viewModel.PostViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.sql.Timestamp;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Newpost extends MediaActivity {


    StorageReference storageReference;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    ActivityNewpostBinding newpostBinding;
    //    @BindView(R.id.image_media)
//    ImageButton imageMedia;
    public static final int MY_CHILD_ACTIVITY = 2034;
    private static final int GALLERY_REQUEST = 1;
    private Uri imageMediaURI;
    public final String TAG = "New Post: ";

    PostViewModel postViewModel = new PostViewModel();
    FirebaseUser firebaseuser = FirebaseAuth.getInstance().getCurrentUser();
    @BindView(R.id.spinner)
    Spinner spinner;
    String Category;
    String imagefile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        newpostBinding = DataBindingUtil.setContentView(Newpost.this, R.layout.activity_newpost);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mAuth = FirebaseAuth.getInstance();

        storageReference = FirebaseStorage.getInstance().getReference();

        progressDialog = new ProgressDialog(this);

        newpostBinding.setPost(postViewModel);
        ButterKnife.bind(this);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.subject,
                R.layout.list_item_spinner);

        postViewModel.setSubjectAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Category = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    public void imageclick() {

        final String OPTION_CAMERA = "Camera";
        final String OPTION_GALLERY = "Gallery";
        final CharSequence cameraTypes[] = new CharSequence[]{OPTION_CAMERA, OPTION_GALLERY};
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
        builder.setTitle("Pick a camera");
        builder.setItems(cameraTypes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (cameraTypes[which].equals(OPTION_CAMERA)) {
                    startCameraActivity();
                } else if (cameraTypes[which].equals(OPTION_GALLERY)) {
                    openGallery();
                }
            }
        });
        builder.show();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_post_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_share) {
            if (Category.equals("Category")) {
                Toast.makeText(Newpost.this, "Please choose a category", Toast.LENGTH_SHORT).show();

            } else
                createPost(item.getActionView());
        }
        if (item.getItemId() == R.id.action_image) {
            imageclick();
        }
        if (item.getItemId() == R.id.action_image_preview) {
            if (imagefile != null) {
                FragmentManager fm = getFragmentManager();
                PreviewDialog dialogFragment = new PreviewDialog();
                Bundle args = new Bundle();
                args.putString("imageuri", imagefile);
                dialogFragment.setArguments(args);
                dialogFragment.show(fm, "Sample Fragment");
            } else {
                Toast.makeText(Newpost.this, "You have not uploaded a photo yet!", Toast.LENGTH_SHORT).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
//
//            Uri imageUri = data.getData();
//
//            CropImage.activity(imageUri)
//                    .setGuidelines(CropImageView.Guidelines.ON)
//                    .setAspectRatio(1, 1)
//                    .start(Newpost.this);
//
//        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//
//            CropImage.ActivityResult result = CropImage.getActivityResult(data);
//            if (resultCode == RESULT_OK) {
//
//                imageMediaURI = result.getUri();
//
////                Picasso.with(Newpost.this).load(imageMediaURI).fit()
////                        .centerCrop()
////                        .into(imageMedia);
//
//            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//                Exception error = result.getError();
//                Log.e("IMAGE", "Error: " + error);
//            }
//        }
//    }


    private void createPost(View v) {


        final String title = postViewModel.title.get();
        final String description = postViewModel.description.get();

        //&& imageMedia != null
        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(description)) {

            progressDialog.setMessage("Posting....");
            progressDialog.show();

            Post post = new Post();
            post.setTitle(title);
            post.setDescription(description);
            post.setUserId(firebaseuser.getUid());
            Timestamp tsTemp = new Timestamp(System.currentTimeMillis());
            post.setDate(tsTemp);
            post.setCategory(Category);


            Log.d(TAG, "imageMediaURI: " + imageMediaURI);

            if (imageMediaURI != null) {
                StorageReference filePath = storageReference.child("blog_images").child(imageMediaURI.getLastPathSegment());

                filePath.putFile(imageMediaURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d(TAG, " image here?");
                        post.setImage(taskSnapshot.getDownloadUrl().toString());
                        FirebaseFirestore.getInstance().collection("posts")
                                .add(post)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                        progressDialog.dismiss();
                                        Toast.makeText(Newpost.this, "post successfully", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error adding document", e);
                                    }
                                });
                    }
                });
            } else {
                FirebaseFirestore.getInstance().collection("posts")
                        .add(post)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                Toast.makeText(Newpost.this, "post successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                            }
                        });
            }


        } else {
            Snackbar.make(v, "Fill all fields", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }

    }


    @Override
    protected void onPhotoTaken() {
        Intent intent = new Intent(this, PhotoEditorActivity.class);
        intent.putExtra("selectedImagePath", selectedImagePath);
        startActivityForResult(intent, MY_CHILD_ACTIVITY);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (MY_CHILD_ACTIVITY): {
                if (resultCode == Activity.RESULT_OK) {
                    // TODO Extract the data returned from the child Activity.
                    imagefile = data.getStringExtra("imageUri");
                    imageMediaURI = Uri.parse(imagefile);
                }
                break;
            }
        }
    }


}
