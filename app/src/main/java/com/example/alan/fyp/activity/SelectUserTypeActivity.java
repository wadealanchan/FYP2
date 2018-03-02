package com.example.alan.fyp.activity;

import android.content.Intent;
import android.graphics.Color;

import com.example.alan.fyp.Henson;
import com.example.alan.fyp.R;
import com.stephentuso.welcome.BackgroundColor;
import com.stephentuso.welcome.BasicPage;
import com.stephentuso.welcome.TitlePage;
import com.stephentuso.welcome.WelcomeActivity;
import com.stephentuso.welcome.WelcomeConfiguration;

/**
 * Created by wadealanchan on 19/2/2018.
 */

public class SelectUserTypeActivity  extends WelcomeActivity {



    @Override
    protected WelcomeConfiguration configuration() {
        return new WelcomeConfiguration.Builder(this)
                .defaultBackgroundColor(R.color.blue_background)
                .defaultTitleTypefacePath("Roboto-Bold.ttf")
                .defaultHeaderTypefacePath("Roboto-Bold.ttf")
                .bottomLayout(WelcomeConfiguration.BottomLayout.BUTTON_BAR)
                .page(new BasicPage(R.drawable.ic_front_desk_white, "Hello! ","Are you going to be a tutor or a student ?")
                        .headerColorResource(this, R.color.colorAccent))
                .swipeToDismiss(false)
                .canSkip(false)
                .exitAnimation(android.R.anim.fade_out)
                .build();
    }



    @Override
    protected void onButtonBarFirstPressed() {
        goToAuthClass("tutor");
    }

    @Override
    protected void onButtonBarSecondPressed() {
        goToAuthClass("student");
    }

    public void goToAuthClass(String userType) {
        Intent intent = Henson.with(this).gotoSignUpFormActivity().userType(
                userType)
                .build();

        startActivity(intent);
    }
}
