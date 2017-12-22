package com.example.alan.fyp;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.alan.fyp.activity.AuthClass;
import com.google.firebase.iid.FirebaseInstanceId;

public class MainActivity extends AppCompatActivity implements View.OnClickListener  {
    Button login_bt;
    Button google_login;
    public final String TAG ="MainClass:Database";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //String token = FirebaseInstanceId.getInstance().getToken();
               // Log.e("tokenFromAct", token);
            }
        }, 7000);

        findViewById(R.id.btn_login).setOnClickListener(this);
        findViewById(R.id.btn_chat).setOnClickListener(this);
        findViewById(R.id.btn_profile).setOnClickListener(this);
        findViewById(R.id.btn_mainpage).setOnClickListener(this);
        findViewById(R.id.btn_newpost).setOnClickListener(this);

    }

    private static Handler mHandler = new Handler();


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_login) {
            Intent newAct = new Intent(MainActivity.this,AuthClass.class);
            //startActivity(newAct);
            startActivityForResult(newAct, 1);
        }
        else if(i == R.id.btn_chat)
        {
            Intent newAct = new Intent(MainActivity.this,Chat.class);
            startActivity(newAct);
        }
        else if(i == R.id.btn_profile)
        {
            Intent newAct = new Intent(MainActivity.this,Profile.class);
            startActivity(newAct);
        }
        else if(i == R.id.btn_mainpage)
        {
            Intent newAct = new Intent(MainActivity.this,Mainpage.class);
            startActivity(newAct);
        }
        else if(i == R.id.btn_newpost)
        {
            Intent newAct = new Intent(MainActivity.this,Newpost.class);
            startActivity(newAct);
        }

    }


    }

