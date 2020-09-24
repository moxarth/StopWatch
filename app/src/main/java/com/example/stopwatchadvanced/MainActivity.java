package com.example.stopwatchadvanced;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView tvSpalsh, tvSubSplash;
    Button btnGetStarted;
    ImageView splashImage;
    Animation alpha_to_go, text_anim, btn_anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        splashImage = findViewById(R.id.ivSplash);
        tvSpalsh = findViewById(R.id.tvSplash);
        tvSubSplash = findViewById(R.id.tvSubSplash);

        btnGetStarted = findViewById(R.id.btnget);

        // load animation
        alpha_to_go = AnimationUtils.loadAnimation(this, R.anim.alpha_to_go);
        text_anim = AnimationUtils.loadAnimation(this, R.anim.text_anim);
        btn_anim = AnimationUtils.loadAnimation(this, R.anim.btn_anim);

        // set Animation
        splashImage.startAnimation(alpha_to_go);
        tvSpalsh.startAnimation(text_anim);
        tvSubSplash.startAnimation(text_anim);
        btnGetStarted.startAnimation(btn_anim);

        Typeface MLight = Typeface.createFromAsset(getAssets(), "fonts/MLight.ttf");
        Typeface MMedium = Typeface.createFromAsset(getAssets(), "fonts/MMedium.ttf");
        Typeface MRegular = Typeface.createFromAsset(getAssets(), "fonts/MRegular.ttf");

        tvSpalsh.setTypeface(MRegular);
        tvSubSplash.setTypeface(MLight);
        btnGetStarted.setTypeface(MMedium);

        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent stopWatchActivity = new Intent(MainActivity.this, StopWatchActivity.class);
                startActivity(stopWatchActivity);
            }
        });

    }
}