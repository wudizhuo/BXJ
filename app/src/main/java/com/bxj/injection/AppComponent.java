package com.bxj.injection;

import com.bxj.App;
import com.bxj.activity.MainActivity;
import com.bxj.manager.DownLoadMgr;

import javax.inject.Singleton;

import dagger.Component;
import okhttp3.OkHttpClient;

@Singleton
@Component(modules = {AppModule.class, NetworkModule.class, PresenterModule.class})
public interface AppComponent {
    void inject(App app);

    void inject(MainActivity activity);

    void inject(DownLoadMgr downLoadMgr);

    OkHttpClient okHttpClient();
}