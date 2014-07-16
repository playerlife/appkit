package com.joejoe.httpclientjson.httpclientjson;

import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.io.File;
import java.util.List;

public class Main extends ActionBarActivity {

    private ListView listView;
    private File tmpfile;
    private ContactAdapter contactAdapter;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == 200) {
                // 取得数据
                List<ContactBean> contactBeans = (List<ContactBean>) msg.obj;
                // 请求图片
                contactAdapter = new ContactAdapter(getApplicationContext(), contactBeans, tmpfile);
                // 设置
                listView.setAdapter(contactAdapter);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_client_jsonmain);
        listView = (ListView) findViewById(R.id.listview);
        tmpfile = new File(Environment.getExternalStorageDirectory(), "tmpfile");

        if (!tmpfile.exists()) {
            tmpfile.mkdirs();
        }
        new Thread() {
            @Override
            public void run() {
                super.run();
                ContactService service = ContactService.getInstance();
                List<ContactBean> contacts = null;
                contacts = service.getContactList();
                Message msg = new Message();
                msg.what = 200;
                msg.obj = contacts;
                handler.sendMessage(msg);
            }
        }.start();
    }

    private void startRecycleService() {
        Intent intent = new Intent(this, RecycleImageService.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("tmpFile", tmpfile);
        intent.putExtras(bundle);
        startService(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 如果目录存在 则启动回收服务
        if (tmpfile.exists()) {
            startRecycleService();
            finish();
        }
    }

}
