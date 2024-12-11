package com.android.sql.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;

public class IntroductionActivity extends AppCompatActivity {
    LottieAnimationView lottie1,lottie2;
    ImageView kp,logo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);
        kp=findViewById(R.id.kp);
        logo=findViewById(R.id.logo);
        lottie1 = findViewById(R.id.lottie1);

        logo.animate()
                .translationY(-2000)
                .setDuration(1000)
                .setStartDelay(6500);
        lottie1.animate()
                .translationY(2000)
                .setDuration(1000)
                .setStartDelay(6500)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        // 动画结束后启动 MainActivity
                        Intent intent = new Intent(IntroductionActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                });
    }

}