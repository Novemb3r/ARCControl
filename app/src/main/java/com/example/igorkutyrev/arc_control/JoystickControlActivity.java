package com.example.igorkutyrev.arc_control;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;


public class JoystickControlActivity extends Activity
{

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.joystick_control);

        ImageView mImageView;
        mImageView = (ImageView) findViewById(R.id.joystick);
        mImageView.setImageResource(R.drawable.compass);

        WindowManager w = getWindowManager();
        final Point size = new Point();
        w.getDefaultDisplay().getSize(size);

        ImageView iw = (ImageView) findViewById(R.id.joystick);
        iw.setOnTouchListener(new ImageView.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                TextView tw = (TextView) findViewById(R.id.touchPoint);
                double dest = Math.sqrt((event.getX()-size.x/2)*(event.getX()-size.x/2) + (event.getY()-size.y/2)*(event.getY()-size.y/2));
                String str = "Point: (" + Float.toString(event.getX()) + ", " + Float.toString(event.getY()) + ") " + "\nDest: " + Double.toString(dest);
                tw.setText(str);
                return true;
            }
        });


    }
}
