package com.example.igorkutyrev.arc_control;

import android.app.ActionBar;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


public class TankControlActivity extends AppCompatActivity
{
    void sendData(SeekBar s1, SeekBar s2)
    {
        int s1value = 1000 - s1.getProgress();
        int s2value = 1000 - s2.getProgress();

        sender.send (s1value, s2value);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.tank_control_horizontal);

        ARCSettingsActivity.loadSettings (this);

        sb1 = (SeekBar) findViewById (R.id.seekBar1);
        sb2 = (SeekBar) findViewById (R.id.seekBar2);

        sb1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onStopTrackingTouch(SeekBar sb1)
            {
                sb1.setProgress(1000);
            }

            @Override
            public void onStartTrackingTouch(SeekBar sb1) {}

            @Override
            public void onProgressChanged(SeekBar sb1, int progress,
                                          boolean fromUser) {
                int seekValue = 1000-sb1.getProgress();
                String x = Integer.toString(seekValue);
                ((TextView) findViewById(R.id.textView1)).setText(x);
//                sendData(sb1, sb2);
            }
        });

        sb2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onStopTrackingTouch(SeekBar sb2)
            {
                sb2.setProgress(1000);
            }

            @Override
            public void onStartTrackingTouch(SeekBar sb2) {}

            @Override
            public void onProgressChanged(SeekBar sb2, int progress,
                                          boolean fromUser) {
                int seekValue = 1000-sb2.getProgress();
                String x = Integer.toString(seekValue);
                ((TextView) findViewById(R.id.textView2)).setText(x);
//                sendData(sb1, sb2);
            }
        });

        sender = new CommandSender (ARCSettingsActivity.getIpString (), this)
        {
            @Override
            protected void onSuccess () {}

            @Override
            protected void onFailure ()
            {
//                Toast.makeText (TankControlActivity.this, R.string.cmd_send_failure, Toast.LENGTH_SHORT)
//                     .show ();
            }
        };
    }

    @Override
    protected void onResume ()
    {
        super.onResume ();

        if (ARCSettingsActivity.getTankControlOrientation () != ARCSettingsActivity.OR_VERTICAL)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        else
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        connectionThread = new Thread(new Runnable()
        {
            public void run()
            {
                while (true)
                {
                    try
                    {
                        sendData(sb1, sb2);
                        Thread.sleep(100);
                    } catch (InterruptedException e)
                    {
                        return;
                    }
                }
            }
        });
        connectionThread.start ();
    }

    @Override
    protected void onPause ()
    {
        super.onPause ();
        connectionThread.interrupt ();
        connectionThread = null;
    }

    private CommandSender sender;
    private Thread connectionThread = null;
    private SeekBar sb1 = null;
    private SeekBar sb2 = null;
}