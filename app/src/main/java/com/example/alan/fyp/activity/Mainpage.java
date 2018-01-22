package com.example.alan.fyp.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.alan.fyp.R;
import com.example.alan.fyp.databinding.ActivityMainpageBinding;
import com.example.alan.fyp.model.Post;
import com.example.alan.fyp.model.User;
import com.example.alan.fyp.ListViewModel.PostListViewModel;
import com.example.alan.fyp.viewModel.PostViewModel;
import com.example.alan.fyp.viewModel.UserViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import butterknife.ButterKnife;


public class Mainpage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ActivityMainpageBinding binding;
    public final String TAG = "Mainpage: ";
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    Post post = new Post();
    PostListViewModel postList = new PostListViewModel();
    UserViewModel userViewModel = new UserViewModel();
    TextView textview;
    FirebaseUser firebaseuser = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference postsRef = db.collection("posts");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_mainpage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newAct = new Intent(Mainpage.this,Newpost.class);
                startActivityForResult(newAct, 1);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        FirebaseAuth.getInstance().addAuthStateListener((firebaseAuth) -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null ) {
                if(user.getPhotoUrl()!=null){
                    userViewModel = new UserViewModel(user.getDisplayName(), "", user.getEmail(), user.getPhotoUrl().toString());
                    //post.getUser().setImage(user.getPhotoUrl().toString());
                    binding.setUser(userViewModel);
                }else
                {
                    userViewModel = new UserViewModel(user.getDisplayName(), "", user.getEmail(), null);
                    binding.setUser(userViewModel);
                }
            } else {
                binding.setUser(null);

            }
        });



        showcontent();


        binding.setPostList(postList);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mainpage, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_history) {
            // Handle the camera action
        } else if (id == R.id.nav_biology) {


        } else if (id == R.id.nav_math) {

        } else if (id == R.id.nav_english) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
                    Post post = document.toObject(Post.class);

                    PostViewModel postViewModel = post.toViewModel();
                    postViewModel.PostId = document.getId();


                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("Users").document(post.getUserId()).get().addOnCompleteListener(userInfotask -> {

                        if (userInfotask.isSuccessful()) {

                            postViewModel.user.set(userInfotask.getResult().toObject(User.class));
                        } else {
                            postViewModel.user.set(null);
                        }

                    });
                    postViewModel.post = post;
                    postList.items.add(postViewModel);
                }
                binding.executePendingBindings();
            }
        });

    }


}
