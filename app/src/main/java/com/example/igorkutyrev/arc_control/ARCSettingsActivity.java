package com.example.igorkutyrev.arc_control;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import com.redmadrobot.inputmask.MaskedTextChangedListener;

public class ARCSettingsActivity extends AppCompatActivity
{
    public static final String APP_PREFS = "ARC_APP_PREFS";

    public static final int OR_HORIZONTAL = 0;
    public static final int OR_VERTICAL = 1;

    public static String getIpString () { return IPString; }
    public static int getTankControlOrientation () { return tankControlOrientation; }
    public static int getSendDelay () { return sendDelay; }

    public static void loadSettings (Context ctx)
    {
        preferences = ctx.getSharedPreferences (APP_PREFS, Context.MODE_PRIVATE);
        IPString = preferences.getString (KEY_IP, "192.168.1.185");
        tankControlOrientation = preferences.getInt (KEY_TANK_OR, OR_HORIZONTAL);
        sendDelay = preferences.getInt (KEY_SEND_DELAY, 100);
        Log.d ("ARCSETTINGS", "Settings loaded");
    }

    public static void saveSettings ()
    {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString (KEY_IP, IPString);
        editor.putInt (KEY_TANK_OR, tankControlOrientation);
        editor.putInt (KEY_SEND_DELAY, sendDelay);
        editor.apply ();
        Log.d ("ARCSETTINGS", "Settings saved");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.arc_settings);

        loadSettings (this);

        final EditText editTextIp = (EditText) findViewById (R.id.settingsIpEditText);

        final MaskedTextChangedListener listenerIp = new MaskedTextChangedListener (
                "[099].[099].[099].[099]",
                true,
                editTextIp,
                null,
                new MaskedTextChangedListener.ValueListener ()
                {
                    @Override
                    public void onTextChanged (boolean maskFilled, @NonNull final String extractedValue)
                    {
                        if (maskFilled)
                        {
                            IPString = editTextIp.getText ().toString ();
                            saveSettings ();
                        }
                    }
                }
        );

        editTextIp.addTextChangedListener (listenerIp);
        editTextIp.setOnFocusChangeListener (listenerIp);
        editTextIp.setHint ("192.168.1.185");

        final EditText sendDelayEditText = (EditText) findViewById (R.id.settings_parameter_send_delay);
        sendDelayEditText.setText (Integer.toString (sendDelay));
        sendDelayEditText.addTextChangedListener (new TextWatcher ()
        {
            @Override
            public void beforeTextChanged (CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged (CharSequence s, int start, int before, int count) {
                try { Integer.parseInt (s.toString ()); }
                catch (NumberFormatException e) {}
            }

            @Override
            public void afterTextChanged (Editable s)
            {
            }
        });

        rerfeshLayout ();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id)
        {
            case R.id.action_about:
            {
                Intent intent = new Intent (this, AboutActivity.class);
                intent.addFlags (Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity (intent);
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public void onConnectButton (View view)
    {
        CommandSender sender = new CommandSender (IPString, ARCSettingsActivity.this)
        {
            @Override
            protected void onSuccess ()
            {
                Toast.makeText(getApplicationContext(), R.string.connection_test_success, Toast.LENGTH_SHORT)
                     .show ();
                Intent intent = new Intent (ARCSettingsActivity.this, TankControlActivity.class);
                startActivity (intent);
            }

            @Override
            protected void onFailure ()
            {
                Toast.makeText(getApplicationContext(), R.string.connection_test_failure, Toast.LENGTH_SHORT)
                     .show ();
            }
        };

        Toast.makeText (getApplicationContext(), R.string.connection_test_start, Toast.LENGTH_SHORT)
             .show();
        sender.send (0, 0);
    }

    public void onTankOrientation (View view)
    {
        if (tankControlOrientation == OR_HORIZONTAL)
            tankControlOrientation = OR_VERTICAL;
        else
            tankControlOrientation = OR_HORIZONTAL;
        rerfeshLayout ();
    }

    private void rerfeshLayout ()
    {
        final EditText editTextIp = (EditText) findViewById(R.id.settingsIpEditText);
        editTextIp.setText(IPString, TextView.BufferType.EDITABLE);

        final TextView tankControlOrientationText = (TextView) findViewById (R.id.settings_tank_control_orientation);
        if (tankControlOrientation == OR_HORIZONTAL)
            tankControlOrientationText.setText (R.string.settings_orientation_horizontal);
        else if (tankControlOrientation == OR_VERTICAL)
            tankControlOrientationText.setText (R.string.settings_orientation_vertical);
    }

    private static SharedPreferences preferences = null;

    private static String IPString = "192.168.1.185";
    private static int tankControlOrientation = OR_HORIZONTAL;
    private static int sendDelay = 100;

    private static final String KEY_IP = "IP";
    private static final String KEY_TANK_OR = "TankOrientation";
    private static final String KEY_SEND_DELAY = "SendDelay";
}