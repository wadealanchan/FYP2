package com.example.alan.fyp.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.alan.fyp.R;
import com.example.alan.fyp.ViewPagerMainpage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.stephentuso.welcome.BackgroundColor;
import com.stephentuso.welcome.BasicPage;
import com.stephentuso.welcome.TitlePage;
import com.stephentuso.welcome.WelcomeActivity;
import com.stephentuso.welcome.WelcomeConfiguration;

public class WelcomePageActivity extends WelcomeActivity {


    private FirebaseAuth mAuth= FirebaseAuth.getInstance();;
    @Override
    protected WelcomeConfiguration configuration() {
        return new WelcomeConfiguration.Builder(this)
                .defaultBackgroundColor(new BackgroundColor(Color.WHITE))
                .defaultTitleTypefacePath("Roboto-Bold.ttf")
                .defaultHeaderTypefacePath("Roboto-Bold.ttf")
                .bottomLayout(WelcomeConfiguration.BottomLayout.BUTTON_BAR)
                .page(new TitlePage(R.drawable.bcd1427900754, "Welcome SnapQ&A")
                        .titleColorResource(this, R.color.colorAccent))
                .swipeToDismiss(false)
                .canSkip(false)
                .exitAnimation(android.R.anim.fade_out)
                .build();
    }

    @Override
    protected void onButtonBarFirstPressed() {
        Intent intent = new Intent(this, AuthClass.class);
        startActivity(intent);
    }

    @Override
    protected void onButtonBarSecondPressed() {
        Intent intent = new Intent(this, SelectUserTypeActivity.class);
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null) {
            Intent intent = new Intent(this, ViewPagerMainpage.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }



}
