package com.bxj.manager;

import com.bxj.APIService;
import com.bxj.App;
import com.bxj.utils.FileStorageUtil;
import com.bxj.utils.LogUtil;
import com.bxj.utils.NetUitl;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

public class DownLoadMgr {
    private static DownLoadMgr mInstance;

    @Inject
    APIService apiservice;

    private DownLoadMgr() {
        App.getInstance().getAppComponent().inject(this);
    }

    public static DownLoadMgr getInstance() {
        if (mInstance == null) {
            mInstance = new DownLoadMgr();
        }
        return mInstance;
    }

    public void getHtmlAndSave(String url, File saveHtmlFile) throws IOException {
        if (!NetUitl.isConnected()) {
            throw new IOException();
        }
        LogUtil.s("url-----" + url);
        FileStorageUtil.writeInputStream(saveHtmlFile, apiservice.getContent(url).execute().body().byteStream());
    }
}
