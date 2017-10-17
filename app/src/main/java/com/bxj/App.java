package com.bxj;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;

import com.bxj.injection.AppComponent;
import com.bxj.injection.AppModule;
import com.bxj.injection.DaggerAppComponent;
import com.umeng.analytics.MobclickAgent;

import java.io.File;

public class App extends Application {
    private static Context applicationContext;
    private static App app;

    protected AppComponent appComponent;

    public static App getInstance() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
        appComponent.inject(this);
        applicationContext = this.getApplicationContext();
        app = this;
        checkConfig();
    }


    public AppComponent getAppComponent() {
        return appComponent;
    }

    public static App getApp() {
        return app;
    }

    public void appinit() {
    }

    private void checkConfig() {
        initFileDir();
        // 友盟的配置
        // 调试模式
        MobclickAgent.setDebugMode(BuildConfig.DEBUG);
    }

    private void initFileDir() {
        try {
            File appDir = new File(getContext().getExternalFilesDir(null),
                    AppConstants.OFFLINE_DIR);
            if (!appDir.exists()) {
                appDir.mkdirs();
            }
            File createFile = new File(appDir, AppConstants.TYPE_BXJ + "/");// 创建步行街目录
            if (!createFile.exists()) {
                createFile.mkdirs();
            }
            createFile = new File(createFile, AppConstants.HTML_DIR);
            if (!createFile.exists()) {
                createFile.mkdirs();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Context getContext() {
        return applicationContext;
    }

    /**
     * 获取app版本号。
     */
    public static String getVersionName() {
        try {
            return getContext().getPackageManager().getPackageInfo(
                    getContext().getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
