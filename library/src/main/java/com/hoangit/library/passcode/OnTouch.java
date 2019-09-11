package com.hoangit.library.passcode;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;

public class OnTouch implements View.OnTouchListener {
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if(event.getAction()==MotionEvent.ACTION_DOWN){
            v.animate()
                    .scaleX(0.8f)
                    .scaleY(0.8f)
                    .setDuration(50)
                    .start();
        }else if(event.getAction()==MotionEvent.ACTION_UP||event.getAction()==MotionEvent.ACTION_CANCEL){
            v.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(50)
                    .start();
        }
        return false;
    }
}
