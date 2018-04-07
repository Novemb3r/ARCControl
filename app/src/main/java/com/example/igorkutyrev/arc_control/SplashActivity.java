package com.example.igorkutyrev.arc_control;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class SplashActivity extends AppCompatActivity
{
    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_splash);

        View heliImage = findViewById(R.id.heliImage);
        heliImage.setBackgroundResource(R.drawable.heli_flight);
        heliAnimation = (AnimationDrawable) heliImage.getBackground();

        heliAnimation.start();

        new Handler ().postDelayed (new Runnable ()
        {
            @Override
            public void run ()
            {
                Intent intent = new Intent (SplashActivity.this, nextScreen);
                startActivity (intent);
            }
        }, 2000);
    };

    public void onSplashClick (View view)
    {
        Intent intent = new Intent (this, nextScreen);
        startActivity (intent);
    }

    private AnimationDrawable heliAnimation;
    private final Class<ARCSettingsActivity> nextScreen = ARCSettingsActivity.class;
}
