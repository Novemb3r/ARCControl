package com.example.igorkutyrev.arc_control;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainMenuActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }

    @Override
    protected void onNewIntent (Intent intent)
    {
        super.onNewIntent (intent);
    }

    public void onAboutClick(View view)
    {
        Intent intent = new Intent(MainMenuActivity.this, AboutActivity.class);
        startActivity(intent);
    }

    public void onTankHorClick(View view)
    {
        Intent intent = new Intent(MainMenuActivity.this, TankControlActivity.class);
        startActivity(intent);
    }

    public void onSettingsClick(View view)
    {
        Intent intent = new Intent(MainMenuActivity.this, ARCSettingsActivity.class);
        startActivity(intent);
    }

    public void onJoystickClick(View view)
    {
        Intent intent = new Intent(MainMenuActivity.this, JoystickControlActivity.class);
        startActivity(intent);
    }
}
