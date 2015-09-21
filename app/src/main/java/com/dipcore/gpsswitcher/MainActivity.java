package com.dipcore.gpsswitcher;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends Activity implements SwitchGPSDialog.NoticeListener{

    private TextView logTextView;
    private boolean isSuAvailable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init(){

        isSuAvailable = ShellInterface.isSuAvailable();

        // Views
        logTextView = (TextView)findViewById(R.id.log);


        // Version
        ((TextView)findViewById(R.id.caption)).setText("GPS Switcher v" + BuildConfig.VERSION_NAME);


        // Scripts and libraries
        Tools.copyAssetFolder(getAssets(), "scripts", "/data/data/" + getPackageName() + "/scripts/");

        if(isSuAvailable) {
            ShellInterface.runCommand("sh /data/data/" + getPackageName() + "/scripts/install.sh");
            String log = ShellInterface.getProcessOutput("sh /data/data/" + getPackageName() + "/scripts/start.sh");
            logTextView.append(log);
        } else {
            logTextView.append("su required");
        }
    }

    public void switchToInternalGPS(View v){
        if(isSuAvailable) {
            String log = ShellInterface.getProcessOutput("sh /data/data/" + getPackageName() + "/scripts/internal.sh");
            logTextView.append(log);
        } else {
            logTextView.append("su required");
        }
    }

    public void switchToExternalGPS(View v){
        if(isSuAvailable) {
            SwitchGPSDialog dialog = new SwitchGPSDialog();
            dialog.show(getFragmentManager(), "SwitchGPSDialog");
        } else {
            logTextView.append("su required");
        }

    }

    public void reboot(View v) {
        if(isSuAvailable) {
            ShellInterface.runCommand("reboot");
        } else {
            logTextView.append("su required");
        }
    }

    public void close(View v) {
        finish();
    }


    public void onSwitchGPSDialogDialogSaveClick(DialogFragment dialog, String lib, String dev, String speed){
        if (lib == null)
            lib="universal";
        if (dev == null)
            dev="/dev/ttyUSB0";
        if (speed == null)
            speed = "9600";
         String log = ShellInterface.getProcessOutput("sh /data/data/" + getPackageName() + "/scripts/external.sh " + lib + " " + dev + " " + speed);
         logTextView.append(log);
    }

    public void onSwitchGPSDialogCancelClick(DialogFragment dialog){

    }



}
