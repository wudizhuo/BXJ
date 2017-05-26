package com.bxj.manager;

import com.bxj.APIService;
import com.bxj.utils.FileStorageUtil;
import com.bxj.utils.NetUitl;

import java.io.File;
import java.io.IOException;

import retrofit2.Retrofit;

public class DownLoadMgr {
    private static DownLoadMgr mInstance;
    private APIService apiservice;

    private DownLoadMgr() {
        apiservice = new Retrofit.Builder().baseUrl("http://bbs.hupu.com/").build().create(APIService.class);
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
        FileStorageUtil.writeInputStream(saveHtmlFile, apiservice.getContent(url).execute().body().byteStream());
    }
}
