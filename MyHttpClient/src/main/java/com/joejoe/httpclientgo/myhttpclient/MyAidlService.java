package com.joejoe.httpclientgo.myhttpclient;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.joejoe.httpclientgo.myhttpclient.IHttpClientAidlInterface;

public class MyAidlService extends Service {

    String createTime,modifyTime, author, version,sysName;

    public MyAidlService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    @Override
    public void onCreate() {
        createTime = getResources().getString(R.string.modify_time);
        modifyTime = getString(R.string.modify_time);
        author = getString(R.string.author);
        version = getString(R.string.version);
        sysName = getString(R.string.sys_name);
    }

    // 远程调用实现此接口
    public class MyBinder extends IHttpClientAidlInterface.Stub {

        @Override
        public String getCreateTime() throws RemoteException {
            return createTime;
        }

        @Override
        public String getModifyTime() throws RemoteException {
            return modifyTime;
        }

        @Override
        public String getAuthor() throws RemoteException {
            return author;
        }

        @Override
        public String getVersion() throws RemoteException {
            return version;
        }

        @Override
        public String getSysName() throws RemoteException {
            return sysName;
        }
    }

}
