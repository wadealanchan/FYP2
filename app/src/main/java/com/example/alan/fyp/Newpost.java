package com.example.alan.fyp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.alan.fyp.activity.AuthClass;
import com.example.alan.fyp.databinding.ActivityNewpostBinding;
import com.example.alan.fyp.model.Post;
import com.example.alan.fyp.model.User;
import com.example.alan.fyp.viewModel.PostViewModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.sql.Timestamp;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Intent.ACTION_GET_CONTENT;

public class Newpost extends AppCompatActivity {


    StorageReference storageReference;
    DatabaseReference postReference;
    DatabaseReference userReference;
    FirebaseAuth mAuth;
    Post post = new Post();
    ProgressDialog progressDialog;

    ActivityNewpostBinding newpostBinding;
    EditText editTitle;
    EditText editDescription;

    @BindView(R.id.image_media)
    ImageButton imageMedia;
    private static final int GALLERY_REQUEST = 1;
    private Uri imageMediaURI;
    public final String TAG="New Post: ";
    PostViewModel postViewModel = new PostViewModel();
    FirebaseUser firebaseuser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        newpostBinding = DataBindingUtil.setContentView(Newpost.this, R.layout.activity_newpost);
        //setContentView(R.layout.activity_newpost);
        mAuth = FirebaseAuth.getInstance();

        storageReference = FirebaseStorage.getInstance().getReference();

        progressDialog = new ProgressDialog(this);
        
        newpostBinding.setPost(postViewModel);
        ButterKnife.bind(this);
    }

    public void imageclick(View view)
    {
        Intent intent = new Intent(ACTION_GET_CONTENT);
        intent.setType("image/*");

        startActivityForResult(intent, GALLERY_REQUEST);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_post_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_share) {
            createPost(item.getActionView());
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {

            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(Newpost.this);

        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                imageMediaURI = result.getUri();

                Picasso.with(Newpost.this).load(imageMediaURI).fit().centerCrop().into(imageMedia);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Log.e("IMAGE", "Error: " + error);
            }
        }
    }


    private void createPost(View v){


        final String title = postViewModel.title.get();
        final String description = postViewModel.description.get();

            //&& imageMedia != null
        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(description) ) {

            progressDialog.setMessage("Posting....");
            progressDialog.show();

            StorageReference filePath = storageReference.child("blog_images").child(imageMediaURI.getLastPathSegment());


            filePath.putFile(imageMediaURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {

                    String userId = firebaseuser.getUid();
                    Log.d(TAG, userId);

                    Post post = new Post();
                    post.setTitle(title);
                    post.setDescription(description);
                    post.setImage(taskSnapshot.getDownloadUrl().toString());
                    post.setUserId(firebaseuser.getUid());

                    Timestamp tsTemp = new Timestamp(System.currentTimeMillis());
                    post.setDate(tsTemp);

                    FirebaseFirestore.getInstance().collection("posts")
                            .add(post)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                    Toast.makeText(Newpost.this, "post successfully", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error adding document", e);
                                    progressDialog.dismiss();
                                }
                            });

//                    userReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            Log.i("Messages", dataSnapshot.toString());
//                            if (dataSnapshot.exists()) {
//                                Log.d(TAG, "heyheyhey get it here?");
//                                Uri downloadUri = taskSnapshot.getDownloadUrl();
//                                DatabaseReference newPost = postReference.push();
//
//
//
//                                post.setTitle(title);
//                                post.setDescription(description);
//                                post.setImage(downloadUri.toString());
//
//
//                                //User user = dataSnapshot.getValue(User.class);
//
//
//                                post.getUser().setId(firebaseuser.getUid());
//                                post.getUser().setName(firebaseuser.getDisplayName());
//                                if(firebaseuser.getPhotoUrl()!=null){
//                                    post.getUser().setImage(firebaseuser.getPhotoUrl().toString());
//                                }else
//                                {
//
//                                }
//
//                                newPost.setValue(post).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//
//                                        if (task.isSuccessful()) {
//                                            //startActivity(new Intent(NewPost.this, MainActivity.class));
//                                            Toast.makeText(Newpost.this, "post successfully", Toast.LENGTH_SHORT).show();
//                                        }
//
//                                        progressDialog.dismiss();
//                                    }
//                                });
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//
//                        }
//                    });


                }
            });
        } else {
            Snackbar.make(v, "Fill all fields", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }









    }










}
