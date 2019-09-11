package com.hoangit.library.passcode;

import android.content.Context;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.hoangit.library.AppSecurityConfig;
import com.hoangit.library.AppSecurityView;
import com.hoangit.library.R;
import java.util.ArrayList;

public class PassCodeView extends FrameLayout {

    private StringBuffer passCode = new StringBuffer();

    private String passCheck;

    private RecyclerView rcvKeyInput;

    private ImageView imgIn1,imgIn2,imgIn3,imgIn4;

    private TextView tvTitle,tvBodyTitle;

    private View viewIn;

    private AppSecurityView.OnListen onListen;

    private boolean isUnlock;

    public void setUnlock(boolean unlock) {
        isUnlock = unlock;
    }

    public void setOnListen(AppSecurityView.OnListen onListen) {
        this.onListen = onListen;
    }

    public PassCodeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    public void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.passcode_view,null);
        addView(view);
        initView();
        setupKey();
    }

    private void setupKey() {

        if(isUnlock){
            tvTitle.setText("Nhập mã PIN");
        }else {
            String title;
            if(AppSecurityConfig.getTypeSecurity(getContext())==1){
                title = "Tắt khóa ứng dụng";
            }else {
                title = "Đặt mã PIN của bạn";
            }
            tvTitle.setText(title);
        }

        tvBodyTitle.setText("Nhập 4 chữ số");

        ArrayList<Key> keys = new ArrayList<>();
        keys.add(new Key("1",""));
        keys.add(new Key("2","ABC"));
        keys.add(new Key("3","DEF"));
        keys.add(new Key("4","GHI"));
        keys.add(new Key("5","JKL"));
        keys.add(new Key("6","MNO"));
        keys.add(new Key("7","PQRS"));
        keys.add(new Key("8","TUV"));
        keys.add(new Key("9","WXYZ"));
        keys.add(new Key("10",""));
        keys.add(new Key("0",""));
        keys.add(new Key("11","Delete"));
        rcvKeyInput.setLayoutManager(new GridLayoutManager(getContext(),3));
        rcvKeyInput.setAdapter(new KeyPassCodeAdapter(getContext(),keys) {
            @Override
            public void OnItemClick(String wl) {
                if(wl.equals("11")){
                    deleteKey();
                }else {
                    inputKey(Integer.parseInt(wl));
                }
            }
        });
    }

    private void inputKey(int i) {

        if(passCode.length()<4){
            passCode.append(i);
        } else {
            return;
        }

        if(passCode.length()==1){
            imgIn1.setSelected(true);
        }else if(passCode.length()==2){
            imgIn2.setSelected(true);
        } else if(passCode.length()==3){
            imgIn3.setSelected(true);
        }else {
            imgIn4.setSelected(true);
            imgIn4.requestLayout();
            check();
        }

        viewIn.requestLayout();
    }

    private void deleteKey() {
        if(imgIn4.isSelected()){
            imgIn4.setSelected(false);
        }else if(imgIn3.isSelected()){
            imgIn3.setSelected(false);
        }else if(imgIn2.isSelected()){
            imgIn2.setSelected(false);
        } else {
            imgIn1.setSelected(false);
        }

        if(passCode.length()>0){
            passCode.replace(passCode.length()-1,passCode.length(),"");
        }

        viewIn.requestLayout();
    }


    private void initView() {
        rcvKeyInput = findViewById(R.id.rcvKeyInput);
        imgIn1 = findViewById(R.id.imgIn1);
        imgIn2 = findViewById(R.id.imgIn2);
        imgIn3 = findViewById(R.id.imgIn3);
        imgIn4 = findViewById(R.id.imgIn4);
        viewIn = findViewById(R.id.viewIn);
        tvTitle = findViewById(R.id.tv_title);
        tvBodyTitle = findViewById(R.id.tv_body);
    }


    private void check() {
        if(isUnlock){
            if(passCode.toString().equals(AppSecurityConfig.getPin(getContext()))){
                onListen.onSuccess();
            }else {
                startAnimationError();
                vibrator(getContext());
                tvBodyTitle.setText("Sai mã");
            }
            return;
        }

        if(AppSecurityConfig.getTypeSecurity(getContext())==AppSecurityConfig.PASS_CODE){
            if(passCode.toString().equals(AppSecurityConfig.getPin(getContext()))){
                AppSecurityConfig.putTypeSecurity(getContext(),AppSecurityConfig.NO);
                onListen.onSuccess();
                vibrator(getContext());
            }else {
                startAnimationError();
                vibrator(getContext());
            }
        }else {
            if(passCheck==null){
                passCheck = passCode.toString();
                startAnimationInputAgain();
            }else {
                if(passCheck.equals(passCode.toString())){
                    AppSecurityConfig.putTypeSecurity(getContext(),AppSecurityConfig.PASS_CODE);
                    AppSecurityConfig.putPin(getContext(),passCode.toString());
                    onListen.onSuccess();
                    vibrator(getContext());
                }else {
                   clear();
                }
            }
        }
    }

    public void clear(){
        startAnimationError();
        vibrator(getContext());
        passCheck = null;
        tvTitle.setText("Đặt mã Pin của bạn");
    }

    private void startAnimationInputAgain(){
        viewIn.animate().translationX(-getContext().getResources().getDisplayMetrics().widthPixels)
                .setDuration(200)
                .withEndAction(() ->
                        viewIn.animate().translationX(getContext().getResources().getDisplayMetrics().widthPixels)
                                .setDuration(0)
                                .withEndAction(() ->
                                        viewIn.animate().translationX(0)
                                                .setDuration(200).withEndAction(this::clearIndicator).start()).start()).start();
        tvTitle.animate().translationX(-getContext().getResources().getDisplayMetrics().widthPixels)
                .setDuration(200)
                .withEndAction(() ->
                        tvTitle.animate().translationX(getContext().getResources().getDisplayMetrics().widthPixels)
                                .setDuration(0)
                                .withEndAction(() -> {
                                    tvTitle.setText("Xác nhận hình của bạn");
                                    tvTitle.animate().translationX(0)
                                            .setDuration(200).withEndAction(this::clearIndicator).start();
                                }).start()).start();
    }

    private void clearIndicator(){
        passCode = new StringBuffer();
        imgIn1.setSelected(false);
        imgIn2.setSelected(false);
        imgIn3.setSelected(false);
        imgIn4.setSelected(false);
        viewIn.requestLayout();
    }


    private void startAnimationError() {
        viewIn.animate().translationX(100).setDuration(50).withEndAction(() ->
                viewIn.animate().translationX(-100).setDuration(100).withEndAction(() ->
                        viewIn.animate().translationX(70).setDuration(50).withEndAction(() ->
                                viewIn.animate().translationX(-70).setDuration(50).withEndAction(() ->
                                        viewIn.animate().translationX(50).setDuration(50).withEndAction(() ->
                                                viewIn.animate().translationX(-25).setDuration(50).withEndAction(() ->
                                                        viewIn.animate().translationX(0).setDuration(50).withEndAction(this::clearIndicator).start()).start()).start()).start()).start()).start()).start();
        tvTitle.animate().translationX(100).setDuration(50).withEndAction(() ->
                tvTitle.animate().translationX(-100).setDuration(100).withEndAction(() ->
                        tvTitle.animate().translationX(70).setDuration(50).withEndAction(() ->
                                tvTitle.animate().translationX(-70).setDuration(50).withEndAction(() ->
                                        tvTitle.animate().translationX(50).setDuration(50).withEndAction(() ->
                                                tvTitle.animate().translationX(-25).setDuration(50).withEndAction(() ->
                                                        tvTitle.animate().translationX(0).setDuration(50).start()).start()).start()).start()).start()).start()).start();

        if(!isUnlock)return;
        tvBodyTitle.animate().translationX(100).setDuration(50).withEndAction(() ->
                tvBodyTitle.animate().translationX(-100).setDuration(100).withEndAction(() ->
                        tvBodyTitle.animate().translationX(70).setDuration(50).withEndAction(() ->
                                tvBodyTitle.animate().translationX(-70).setDuration(50).withEndAction(() ->
                                        tvBodyTitle.animate().translationX(50).setDuration(50).withEndAction(() ->
                                                tvBodyTitle.animate().translationX(-25).setDuration(50).withEndAction(() ->
                                                        tvBodyTitle.animate().translationX(0).setDuration(50).start()).start()).start()).start()).start()).start()).start();

    }


    private void vibrator(Context context){
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null && vibrator.hasVibrator()) {
            vibrator.vibrate(50); // for 500 ms
        }
    }

}
