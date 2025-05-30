package com.sameer.arora;

import static top.niunaijun.blackbox.core.env.BEnvironment.getDataFilesDir;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import java.io.File;
import java.io.IOException;

import top.niunaijun.blackbox.BlackBoxCore;
import top.niunaijun.blackbox.entity.pm.InstallResult;
import top.niunaijun.blackbox.utils.FileUtils;

public class MainActivity extends AppCompatActivity {

    Context ctx;
    InstallResult installResult;
    BlackBoxCore blackBoxCore;

    int Storage_Permission = 142;

    String Launch = "Launch";
    String[] appPackage = {"com.tencent.ig","com.pubg.krmobile","com.ea.gp.apexlegendsmobilefps","com.twitter.android","com.katana.facebook","com.APK.BYPASS"};

    static {
        System.loadLibrary("SameerArora");
    }

    MaterialButton installtwitter,installfacebook;

    private native String Telegram();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        blackBoxCore = BlackBoxCore.get();
        blackBoxCore.doCreate();

        ctx = this;

        MaterialToolbar materialToolbar = findViewById(R.id.toolbar);
        materialToolbar.inflateMenu(R.menu.options_home);

        if (!Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        }

        MaterialButton InstallGlobal = findViewById(R.id.InstallGlobal);
        MaterialButton installkorea = findViewById(R.id.installkorea);
        MaterialButton installbgmi = findViewById(R.id.InstallBgmi);
        installtwitter = findViewById(R.id.installtwitter);
        installfacebook = findViewById(R.id.installfacebook);
        MaterialButton installgg = findViewById(R.id.installgg);

        InstallGlobal.setOnClickListener(view -> {
            InstallGame(InstallGlobal,appPackage[0]);
        });

        installkorea.setOnClickListener(view -> {
            InstallGame(installkorea,appPackage[1]);
        });

        installbgmi.setOnClickListener(view -> {
            InstallGame(installbgmi,appPackage[2]);
        });

        installfacebook.setOnClickListener(view -> {
            if(isPackageInstalled(appPackage[4])){
                blackBoxCore.launchApk(appPackage[4],0);
            } else {
                installResult = blackBoxCore.installPackageAsUser(appPackage[4],0);
                if (installResult.success) {
                    installfacebook.setText(Launch);
                } else {
                    Toast.makeText(ctx, installResult.msg, Toast.LENGTH_SHORT).show();
                }
            }
        });

        installtwitter.setOnClickListener(view -> {
            if(isPackageInstalled(appPackage[3])){
                blackBoxCore.launchApk(appPackage[3],0);
            }  else {
                installResult = blackBoxCore.installPackageAsUser(appPackage[3],0);
                if (installResult.success) {
                    installtwitter.setText(Launch);
                } else {
                    Toast.makeText(ctx, installResult.msg, Toast.LENGTH_SHORT).show();
                }
            }
        });

        installgg.setOnClickListener(view -> {
            if(isPackageInstalled(appPackage[5])){
                blackBoxCore.launchApk(appPackage[5],0);
            }  else {
                installResult = blackBoxCore.installPackageAsUser(appPackage[5],0);
                if (installResult.success) {
                    installgg.setText(Launch);
                    blackBoxCore.launchApk(appPackage[5],0);
                } else {
                    Toast.makeText(ctx, installResult.msg, Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (isPackageInstalled(appPackage[0])) {
            InstallGlobal.setText(Launch);
        }

        if (isPackageInstalled(appPackage[1])) {
            installkorea.setText(Launch);
        }

        if (isPackageInstalled(appPackage[2])) {
            installbgmi.setText(Launch);
        }

        if (isPackageInstalled(appPackage[3])) {
            installtwitter.setText(Launch);
        }

        if (isPackageInstalled(appPackage[4])) {
            installfacebook.setText(Launch);
        }

        if (isPackageInstalled(appPackage[5])){
            installgg.setText(Launch);
            blackBoxCore.launchApk(appPackage[5],0);
        } else {
            installResult = blackBoxCore.installPackageAsUser(appPackage[5],0);
            if (installResult.success) {
                installgg.setText(Launch);
                blackBoxCore.launchApk(appPackage[5],0);
            } else {
                Toast.makeText(ctx, installResult.msg, Toast.LENGTH_SHORT).show();
            }
        }

        version(findViewById(R.id.vrglobal));
        version(findViewById(R.id.vrkorea));
        version(findViewById(R.id.vrindia));

        LinearLayout imageView = findViewById(R.id.telegram);
        imageView.setOnClickListener(view -> {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(Telegram()));
            startActivity(i);
        });

    }

    void version(TextView textView){
        textView.setText(textView.getText().toString().replaceAll("1.9.0","2.0.0"));
    }

    void InstallGame(MaterialButton materialButton,String GameName) {
        if (getPackageManager().canRequestPackageInstalls()){
            if (new File("/storage/emulated/0/vSdcard/Android/obb/"+GameName+"/main.16285."+GameName+".obb").exists()) {
                if(isPackageInstalled(GameName)){
                    blackBoxCore.launchApk(GameName,0);
                } else {
                    installResult = blackBoxCore.installPackageAsUser(GameName,0);
                    if (installResult.success) {
                        materialButton.setText(Launch);
                    } else {
                        Toast.makeText(this, installResult.msg, Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                new MyCopyTask().execute("/storage/emulated/0/Android/obb/"+GameName+"/main.16285."+GameName+".obb",GameName);
            }
        } else {
            startActivityForResult(new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES),112);
        }
    }

    boolean isPackageInstalled(String appPackage){
        return blackBoxCore.isInstalled(appPackage,0);
    }

    private class MyCopyTask extends AsyncTask<String, Integer, File> {
        AlertDialog dialog;
        String message;

        @Override
        protected void onPreExecute() {
            AlertDialog.Builder builder = new AlertDialog.Builder(ctx,R.style.WrapContentDialog);
            builder.setCancelable(false);
            View customLayout = getLayoutInflater().inflate(R.layout.dialog_game_blaster_progress,null);
            builder.setView(customLayout);
            dialog = builder.create();
            dialog.show();
        }

        @Override
        protected File doInBackground(String... params) {
            String sourcePath = params[0];
            File source = new File(sourcePath);
            String filename = sourcePath.substring(sourcePath.lastIndexOf("/")+1);
            File destination = new File("/storage/emulated/0/vSdcard/Android/obb/"+params[1]+"/"+filename);
            try {
                FileUtils.copyFile(source,destination);
            } catch (IOException e) {
                message = e.getMessage();
            }
            return destination;
        }

        @Override
        protected void onProgressUpdate(Integer... values){
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(File result) {
            if(result.exists()) {
                Toast.makeText(getApplicationContext(),"OBB Copied", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(),message, Toast.LENGTH_LONG).show();
            }
            dialog.dismiss();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Storage_Permission) {
            if (!hasAllPermissionsGranted(grantResults)) {
                Toast.makeText(this, "Unable To Get Storage Permission", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public boolean hasAllPermissionsGranted(@NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    void RunShell(String cmd){
        try {
            Runtime.getRuntime().exec(new String[]{"sh","-c",cmd});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
