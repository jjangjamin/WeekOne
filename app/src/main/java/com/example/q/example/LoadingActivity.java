package com.example.q.example;

import android.app.AppComponentFactory;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

public class LoadingActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 5000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        TextView loadingtext = (TextView) findViewById(R.id.loadingtext);
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(60);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        loadingtext.startAnimation(anim);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                Intent mainIntent = new Intent(LoadingActivity.this, MainActivity.class);
                startActivity(mainIntent);
                finish();


                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        },SPLASH_TIME_OUT);


    }
}
