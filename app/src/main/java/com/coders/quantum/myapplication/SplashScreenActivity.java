package com.coders.quantum.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {

    private static final int SPLASH_DISPLAY_LENGTH = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ImageView logoImage = findViewById(R.id.logoImage);

        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        logoImage.startAnimation(fadeIn);

        new Handler().postDelayed(() -> {
            Intent mainIntent = new Intent(SplashScreenActivity.this, HomeActivity.class);
            startActivity(mainIntent);
            finish();
        }, SPLASH_DISPLAY_LENGTH);

//        Intent intent = new Intent(SplashScreenActivity.this, HomeActivity.class);
//        startActivity(intent);
//        finish();
    }
}