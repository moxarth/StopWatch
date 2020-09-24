package com.example.stopwatchadvanced;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;

public class StopWatchActivity extends AppCompatActivity {

    Button startBtn, btnStop;
    Animation icAnchorAnimation;
    ImageView icAnchorImage;
    Chronometer timerHere;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_watch);

        icAnchorImage = findViewById(R.id.bgAnchorImage);
        startBtn = findViewById(R.id.btnStart);
        btnStop = findViewById(R.id.btnStop);
        timerHere = findViewById(R.id.timerHere);

        //create optional Animation
        btnStop.setAlpha(0);

        // load Animation
        icAnchorAnimation = AnimationUtils.loadAnimation(this,R.anim.round_image_alone_anim);

        Typeface MLight = Typeface.createFromAsset(getAssets(), "fonts/MLight.ttf");
        Typeface MMedium = Typeface.createFromAsset(getAssets(), "fonts/MMedium.ttf");
        Typeface MRegular = Typeface.createFromAsset(getAssets(), "fonts/MRegular.ttf");

        startBtn.setTypeface(MMedium);
        btnStop.setTypeface(MMedium);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start Animation
                icAnchorImage.startAnimation(icAnchorAnimation);
                btnStop.animate().alpha(1).translationY(-80).setDuration(400).start();
                startBtn.animate().alpha(0).setDuration(400).start();

                //start timer
                timerHere.setBase(SystemClock.elapsedRealtime());
                timerHere.start();
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timerHere.stop();
                icAnchorImage.clearAnimation();

                startBtn.animate().alpha(1).translationY(-80).setDuration(400).start();
                btnStop.animate().alpha(0).setDuration(400).start();
            }
        });
    }
}