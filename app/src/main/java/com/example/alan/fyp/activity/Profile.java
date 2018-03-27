package com.example.alan.fyp.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.alan.fyp.R;
import com.example.alan.fyp.databinding.ActivityProfileBinding;
import com.example.alan.fyp.viewModel.UserViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wadealanchan on 25/10/2017.
 */

public class Profile extends BaseActivity {

    StorageReference storageReference;
    public static final int GALLERY_REQUEST = 1;
    private final int CAMERA_REQUEST = 1034;
    private Uri imageUserAvatarURI = null;
    private ProgressDialog mProgressDialog1;
    private DatabaseReference mDatabaseRefUsers;
    private FirebaseAuth mAuth;
    ActivityProfileBinding binding;
    @BindView(R.id.avatar)
    de.hdodenhof.circleimageview.CircleImageView circleImageView;
    UserViewModel userViewModel;
    public final String TAG = "Profile Class";
    FirebaseUser firebaseuser = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    protected String selectedOutputPath;
    private static final String PHOTO_PATH = "com.example.alan.fyp.activity";
    File mCurrentPhoto;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);
        storageReference = FirebaseStorage.getInstance().getReference();
        if (firebaseuser != null) {
            if (firebaseuser.getPhotoUrl() != null) {
                FirebaseUser(firebaseuser.getPhotoUrl().toString());
            } else {
                FirebaseUser(null);
            }
        } else {
            FirebaseUser(null);
        }
        ButterKnife.bind(this);


    }

    public void FirebaseUser(String imageurl) {


        if (firebaseuser != null) {
            userViewModel = new UserViewModel(firebaseuser.getDisplayName(), "", firebaseuser.getEmail(), imageurl);
            binding.setUser(userViewModel);

        } else {
            binding.setUser(null);

        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQUEST) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callCameraIntent();
            } else {
                Toast.makeText(Profile.this, "You need set permission Camera to use this App", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @OnClick(R.id.avatar)
    public void changeUserAvata(View view) {
        final String OPTION_CAMERA = "Camera";
        final String OPTION_GALLERY = "Gallery";

        final CharSequence cameraTypes[] = new CharSequence[]{OPTION_CAMERA, OPTION_GALLERY};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick a camera");
        builder.setItems(cameraTypes, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (cameraTypes[which].equals(OPTION_CAMERA)) {
                    if (ActivityCompat.checkSelfPermission(Profile.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(Profile.this, new String[]{android.Manifest.permission.CAMERA}, CAMERA_REQUEST);
                    } else {
                        callCameraIntent();
                    }
                } else if (cameraTypes[which].equals(OPTION_GALLERY)) {
                    callGalleryIntent();
                }
            }
        });
        builder.show();

    }

    private void callGalleryIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_REQUEST);
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhoto = image;
        return image;
    }

    private void callCameraIntent() {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (intent.resolveActivity(getPackageManager()) != null) {
//            startActivityForResult(intent, CAMERA_REQUEST);
//
//        }

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST);
            }
        }
    }


    public void saveUserInfo() {

        if (imageUserAvatarURI != null) {

            StorageReference filePath = storageReference.child("User_avatar").child(imageUserAvatarURI.getLastPathSegment());
            showProgressDialog();
            filePath.putFile(imageUserAvatarURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setPhotoUri(taskSnapshot.getDownloadUrl())
                            .build();

                    firebaseuser.updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {


                                        FirebaseFirestore.getInstance().collection("Users").document(firebaseuser.getUid())
                                                .update("image", taskSnapshot.getDownloadUrl().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                hideProgressDialog();
                                                Toast.makeText(Profile.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                                                Log.d(TAG, "DocumentSnapshot successfully updated!");
                                                finish();
                                            }
                                        })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w(TAG, "Error updating document", e);
                                                    }
                                                });

                                    }
                                }
                            });


                }
            });


        } else {
            Toast.makeText(Profile.this, "Fill all fields", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "result" + resultCode);
        if (resultCode == RESULT_OK && (requestCode == GALLERY_REQUEST || requestCode == CAMERA_REQUEST)) {
            Log.d(TAG, "request" + requestCode);

            Uri imageUri = requestCode == CAMERA_REQUEST ? Uri.fromFile(mCurrentPhoto) : data.getData();
            Log.d(TAG, "" + imageUri);
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);


            Log.d("Profile", "if1");

        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Log.d("Profile", "if2");
                imageUserAvatarURI = result.getUri();
                FirebaseUser(imageUserAvatarURI.toString());

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Log.d(TAG, error.getMessage());
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save) {

            saveUserInfo();
        }
        return super.onOptionsItemSelected(item);
    }


}
