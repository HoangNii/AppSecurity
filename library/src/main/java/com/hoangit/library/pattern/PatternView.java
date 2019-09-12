package com.hoangit.library.pattern;

import android.content.Context;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.hoangit.library.AppSecurityConfig;
import com.hoangit.library.AppSecurityView;
import com.hoangit.library.R;

public class PatternView extends FrameLayout {

    private TextView tvTitle,tvBodyTitle;

    private Lock9View lock9View;

    private String passCheck;

    private AppSecurityView.OnListen onListen;

    private boolean isUnlock;

    public void setUnlock(boolean unlock) {
        isUnlock = unlock;
    }

    public void setOnListen(AppSecurityView.OnListen onListen) {
        this.onListen = onListen;
    }


    public PatternView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }



    public void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.pattern_view,null);
        addView(view);
        initView();
        setup();
    }

    private void setup() {

        if(isUnlock){
            tvTitle.setText("Draw unlock pattern");
        }else {
            String title;
            if(AppSecurityConfig.getTypeSecurity(getContext())==AppSecurityConfig.PATTERN){
                title = "Disable unlock pattern";
            }else {
                title = "Draw your unlock pattern";
            }
            tvTitle.setText(title);
        }

        tvBodyTitle.setText("Connect at least 4 dots");

        lock9View.setGestureCallback(new Lock9View.GestureCallback() {
            @Override
            public void onNodeConnected(@NonNull int[] numbers) {

            }

            @Override
            public void onGestureFinished(@NonNull int[] numbers) {
                if(numbers.length<4)
                    return;

                StringBuilder builder = new StringBuilder();
                for (int number : numbers) {
                    builder.append(number);
                }

                if(isUnlock){
                    if(builder.toString().equals(AppSecurityConfig.getPattern(getContext()))){
                        onListen.onSuccess();
                    }else {
                        vibrator(getContext());
                        tvBodyTitle.setText("Wrong pattern");
                        startAnimationError();
                    }
                    return;
                }


                if(AppSecurityConfig.getTypeSecurity(getContext())==AppSecurityConfig.PATTERN){
                    if(AppSecurityConfig.getPattern(getContext()).equals(builder.toString())){
                        AppSecurityConfig.putTypeSecurity(getContext(),AppSecurityConfig.NO);
                        onListen.onSuccess();
                        vibrator(getContext());
                    }else {
                        vibrator(getContext());
                        tvBodyTitle.setText("Wrong pattern");
                        startAnimationError();
                    }
                }else {
                    if(passCheck==null){
                        passCheck = builder.toString();
                        startAnimationInputAgain();
                    }else {
                        if(passCheck.equals(builder.toString())){
                            AppSecurityConfig.putTypeSecurity(getContext(),AppSecurityConfig.PATTERN);
                            AppSecurityConfig.putPattern(getContext(),passCheck);
                            onListen.onSuccess();
                            vibrator(getContext());
                        }else {
                            vibrator(getContext());
                            passCheck = null;
                            tvBodyTitle.setText("Wrong pattern");
                            tvTitle.setText("Draw your unlock pattern");
                            startAnimationError();
                        }
                    }
                }
            }
        });
    }

    public void clear(){
        startAnimationError();
        vibrator(getContext());
        passCheck = null;
        tvTitle.setText("Draw your unlock pattern");
        tvBodyTitle.setText("Connect at least 4 dots");
    }

    private void startAnimationError() {
        tvBodyTitle.animate().translationX(100).setDuration(50).withEndAction(() ->
                tvBodyTitle.animate().translationX(-100).setDuration(100).withEndAction(() ->
                        tvBodyTitle.animate().translationX(70).setDuration(50).withEndAction(() ->
                                tvBodyTitle.animate().translationX(-70).setDuration(50).withEndAction(() ->
                                        tvBodyTitle.animate().translationX(50).setDuration(50).withEndAction(() ->
                                                tvBodyTitle.animate().translationX(-25).setDuration(50).withEndAction(() ->
                                                        tvBodyTitle.animate().translationX(0).setDuration(50).start()).start()).start()).start()).start()).start()).start();
        tvTitle.animate().translationX(100).setDuration(50).withEndAction(() ->
                tvTitle.animate().translationX(-100).setDuration(100).withEndAction(() ->
                        tvTitle.animate().translationX(70).setDuration(50).withEndAction(() ->
                                tvTitle.animate().translationX(-70).setDuration(50).withEndAction(() ->
                                        tvTitle.animate().translationX(50).setDuration(50).withEndAction(() ->
                                                tvTitle.animate().translationX(-25).setDuration(50).withEndAction(() ->
                                                        tvTitle.animate().translationX(0).setDuration(50).start()).start()).start()).start()).start()).start()).start();
    }


    private void startAnimationInputAgain(){
        tvBodyTitle.animate().translationX(-getContext().getResources().getDisplayMetrics().widthPixels)
                .setDuration(200)
                .withEndAction(() ->
                        tvBodyTitle.animate().translationX(getContext().getResources().getDisplayMetrics().widthPixels)
                                .setDuration(0)
                                .withEndAction(() ->
                                        tvBodyTitle.animate().translationX(0)
                                                .setDuration(200).start()).start()).start();
        tvTitle.animate().translationX(-getContext().getResources().getDisplayMetrics().widthPixels)
                .setDuration(200)
                .withEndAction(() ->
                        tvTitle.animate().translationX(getContext().getResources().getDisplayMetrics().widthPixels)
                                .setDuration(0)
                                .withEndAction(() -> {
                                    tvBodyTitle.setText("Draw the pattern again");
                                    tvTitle.setText("Confirm your pattern");
                                    tvTitle.animate().translationX(0)
                                            .setDuration(200).start();
                                }).start()).start();
    }

    private void initView() {
        tvTitle = findViewById(R.id.tv_title);
        lock9View = findViewById(R.id.lock_9_view);
        tvBodyTitle = findViewById(R.id.tv_body);
    }

    private void vibrator(Context context){
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null && vibrator.hasVibrator()) {
            vibrator.vibrate(50); // for 500 ms
        }
    }


}
