package com.sameer.arora.app;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;

import java.io.File;

import top.niunaijun.blackbox.BlackBoxCore;
import top.niunaijun.blackbox.app.configuration.AppLifecycleCallback;
import top.niunaijun.blackbox.app.configuration.ClientConfiguration;

public class App extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        try {
            BlackBoxCore.get().doAttachBaseContext(base, new ClientConfiguration() {

                @Override
                public String getHostPackageName() {
                    return base.getPackageName();
                }

                @Override
                public boolean isHideRoot() {
                    return true;
                }

                @Override
                public boolean isHideXposed() {
                    return true;
                }

                @Override
                public boolean isEnableDaemonService(){
                    return false;
                }

                public boolean requestInstallPackage(File file){
                    PackageInfo packageInfo = base.getPackageManager().getPackageArchiveInfo(file.getAbsolutePath(),0);
                    return false;
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        BlackBoxCore.get().doCreate();
        BlackBoxCore.get().addAppLifecycleCallback(new AppLifecycleCallback(){

            public void beforeCreateApplication(){
                String packageName;
                String processName;
                Context context;
                int userId;
            }

            public void beforeApplicationOnCreate(){
                String packageName;
                String processName;
                Application application;
                int userId;
            }

            public void afterApplicationOnCreate(){
                String packageName;
                String processName;
                Application application;
                int userId;
            }

        });
    }

}
