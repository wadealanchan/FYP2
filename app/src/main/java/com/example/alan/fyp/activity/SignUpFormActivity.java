package com.example.alan.fyp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.alan.fyp.R;
import com.example.alan.fyp.ViewPagerMainpage;
import com.example.alan.fyp.databinding.ActivitySignUpFormBinding;
import com.example.alan.fyp.model.User;
import com.example.alan.fyp.viewModel.UserViewModel;
import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignUpFormActivity extends BaseActivity {


    @BindView(R.id.sign_up_toolbar)
    Toolbar toolbar;
    ActivitySignUpFormBinding binding;
    @InjectExtra
    String userType;
    private FirebaseAuth mAuth;
    public final String TAG = "SignUpFormActivity";
    ProgressDialog progressDialog;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    UserViewModel userViewModel = new UserViewModel();
    @BindView(R.id.field_name)
    com.rengwuxian.materialedittext.MaterialEditText field_name;
    @BindView(R.id.field_email)
    com.rengwuxian.materialedittext.MaterialEditText field_email;
    @BindView(R.id.field_pw)
    com.rengwuxian.materialedittext.MaterialEditText field_pw;
    @BindView(R.id.field_repw)
    com.rengwuxian.materialedittext.MaterialEditText field_repw;
    @BindView(R.id.spinner)
    Spinner spinner;

    String institutionOrGrade;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up_form);
        Dart.inject(this);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        userViewModel.userType.set(userType);
        binding.setUser(userViewModel);


        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        if(userType.equals("tutor")) {

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.institution,
                    R.layout.list_item_spinner);

            userViewModel.setSubjectAdapter(adapter);
        } else {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.grade,
                    R.layout.list_item_spinner);
            userViewModel.setSubjectAdapter(adapter);
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                institutionOrGrade = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.tb_done:
                if (validateForm()) {
                    createAccount();
                } else {
                    Toast.makeText(SignUpFormActivity.this, "fields are not completely filled ", Toast.LENGTH_SHORT).show();
                }
                return true;

        }
        return super.onOptionsItemSelected(item);
    }


    private boolean validateForm() {

        if(field_name.getText().toString().isEmpty())return false;
        if(field_email.getText().toString().isEmpty())return false;
        if(field_pw.getText().toString().isEmpty())return false;
        if(field_repw.getText().toString().isEmpty()) return false;

        if(!field_pw.getText().toString().trim().equals(field_repw.getText().toString().trim()))
        {
            Toast.makeText(this, "Passwords are not equal", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (institutionOrGrade.equals("Institution") || institutionOrGrade.equals("Grade") ) {
            Toast.makeText(SignUpFormActivity.this, "Please enter your educational background", Toast.LENGTH_SHORT).show();
            return false;
        }

       return true;
    }

    private void createAccount() {


        final String email= field_email.getText().toString().trim();
        final String name = field_name.getText().toString().trim();
        final String password= field_pw.getText().toString().trim();

        Log.d(TAG,email+" "+name+" "+password);

        progressDialog.setMessage("Signing Up...");
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser firebaseuser = mAuth.getCurrentUser();
                    setName(name);
                    User user = new User();
                    user.setName(name);
                    user.setType(userType);
                    user.setInstitutionOrGrade(institutionOrGrade);
                    Toast.makeText(SignUpFormActivity.this, email + " Successfully registered", Toast.LENGTH_SHORT).show();
                    db.collection("Users").document(firebaseuser.getUid()).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(SignUpFormActivity.this, ViewPagerMainpage.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);

                            } else {
                                Log.d(TAG, "Error ");
                            }
                        }
                    });

                } else {
                    Toast.makeText(SignUpFormActivity.this, "unsuccessful", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                }
            }
        });


    }


    private void setName(String Name) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(Name)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated.");
                        }
                    }
                });
    }

}
