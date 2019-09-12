package com.hoangit.library;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.hoangit.library.passcode.PassCodeView;
import com.hoangit.library.pattern.PatternView;

public class AppSecurityView extends FrameLayout {

    private OnListen onListen;

    private boolean isUnlock;

    public void setUnlock(boolean unlock) {
        isUnlock = unlock;
    }

    public void setOnListen(OnListen onListen) {
        this.onListen = onListen;
    }

    public AppSecurityView(@NonNull Context context) {
        super(context);
    }

    public AppSecurityView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AppSecurityView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.security_view,null);
        addView(view);
        PassCodeView passCodeView =  view.findViewById(R.id.pass_code_view);
        PatternView patternView =  view.findViewById(R.id.pattern_view);
        passCodeView.setUnlock(isUnlock);
        patternView.setUnlock(isUnlock);
        passCodeView.init();
        patternView.init();
        passCodeView.setOnListen(() -> onListen.onSuccess());
        patternView.setOnListen(() -> onListen.onSuccess());

        if(AppSecurityConfig.getTypeSecurity(getContext())==AppSecurityConfig.PATTERN){
            patternView.setVisibility(VISIBLE);
            passCodeView.setVisibility(GONE);
        }else {
            patternView.setVisibility(GONE);
            passCodeView.setVisibility(VISIBLE);
        }

        TextView tvChangeType = view.findViewById(R.id.tv_change_type);
        ImageView imgChangeType = view.findViewById(R.id.img_type);
        if(AppSecurityConfig.getTypeSecurity(getContext())==AppSecurityConfig.NO){
            tvChangeType.setText("  Change To Pattern  ");
            imgChangeType.setImageResource(R.drawable.ic_pattern);
            tvChangeType.setSelected(true);
            tvChangeType.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(tvChangeType.isSelected()){
                        passCodeView.setVisibility(GONE);
                        patternView.setVisibility(VISIBLE);
                        passCodeView.clear();
                        tvChangeType.setText("  Change to PIN Code  ");
                        imgChangeType.setImageResource(R.drawable.ic_pin);
                        tvChangeType.setSelected(false);
                    }else {
                        passCodeView.setVisibility(VISIBLE);
                        patternView.setVisibility(GONE);
                        patternView.clear();
                        tvChangeType.setText("  Change To Pattern  ");
                        imgChangeType.setImageResource(R.drawable.ic_pattern);
                        tvChangeType.setSelected(true);
                    }
                }
            });
        }else{
            tvChangeType.setVisibility(INVISIBLE);
            imgChangeType.setVisibility(INVISIBLE);
        }
    }

    public interface OnListen{
        void onSuccess();
    }

}
