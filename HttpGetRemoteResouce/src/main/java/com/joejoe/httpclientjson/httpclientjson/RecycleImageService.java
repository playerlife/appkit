package com.joejoe.httpclientjson.httpclientjson;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.io.File;


public class RecycleImageService extends IntentService {

    public RecycleImageService() {
        super("RecycleImageService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        try {
            // 延迟1分钟回收图片
            Thread.sleep(1000 * 60);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Log.d("IntentService", "启动回收服务");

        Bundle bundle = intent.getExtras();
        File tmpFile = (File) bundle.getSerializable("tmpFile");
        // 删除临时文件
        File[] files = tmpFile.listFiles();
        for (File file : files) {
            file.delete();
        }
        tmpFile.delete();
        System.exit(0);
    }
}
