package com.example.igorkutyrev.arc_control;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;

public class Utils
{
    static void setViewBackground (View v, Drawable background)
    {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
            v.setBackgroundDrawable(background);
        else v.setBackground(background);
    }

    static void editTextShortErrorHighlight (final EditText et) {
        editTextSetErrorHighlight(et);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                editTextRemoveErrorHighlight(et);
            }
        }, HIGHLIGHT_LENGTH);
    }

    static void editTextSetErrorHighlight (EditText et)
    {
        if (et != null) {
            Drawable back = et.getBackground();
            back.setColorFilter(0xFFDD2C00, PorterDuff.Mode.SRC_ATOP);
            setViewBackground(et, back);
        }
    }

    static void editTextRemoveErrorHighlight (EditText et)
    {
        if (et != null) et.setBackgroundResource(R.drawable.abc_edit_text_material);
    }

    private static int HIGHLIGHT_LENGTH = 500;
}
