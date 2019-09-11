package com.hoangit.appsecurity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.hoangit.library.AppSecurityConfig;
import com.hoangit.library.AppSecurityView;
import com.hoangit.library.passcode.PassCodeView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(AppSecurityConfig.getTypeSecurity(this)!=AppSecurityConfig.NO){
            showDialogUnlock();
        }

        TextView sw = findViewById(R.id.sw);
        sw.setText(AppSecurityConfig.getTypeSecurity(this)!=AppSecurityConfig.NO?"ON":"OFF");
        sw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogCreateLock(sw);
            }
        });
    }

    private void showDialogCreateLock(TextView sw){
        Dialog dialog = new Dialog(MainActivity.this,R.style.AppTheme);
        dialog.setContentView(R.layout.dialog_lock);
        AppSecurityView securityView = dialog.findViewById(R.id.security_view);
        dialog.findViewById(R.id.bt_back).setOnClickListener(view -> dialog.dismiss());
        dialog.setOnDismissListener(dialogInterface -> sw.setText(AppSecurityConfig.getTypeSecurity(MainActivity.this)!=AppSecurityConfig.NO?"ON":"OFF"));
        securityView.setOnListen(dialog::dismiss);
        securityView.setUnlock(false);
        securityView.init();
        dialog.show();
    }


    private void showDialogUnlock(){
        Dialog dialog = new Dialog(MainActivity.this,R.style.AppTheme);
        dialog.setContentView(R.layout.dialog_lock);
        AppSecurityView securityView = dialog.findViewById(R.id.security_view);
        securityView.setOnListen(() -> {
            dialog.setOnDismissListener(dialogInterface -> { });
            dialog.dismiss();
        });
        dialog.findViewById(R.id.bt_back).setVisibility(View.GONE);
        dialog.setOnDismissListener(dialogInterface -> finish());
        securityView.setUnlock(true);
        securityView.init();
        dialog.show();
    }

}
