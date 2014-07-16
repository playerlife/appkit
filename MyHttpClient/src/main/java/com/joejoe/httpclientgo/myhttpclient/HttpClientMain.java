package com.joejoe.httpclientgo.myhttpclient;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class HttpClientMain extends ActionBarActivity {

    private static final String imgurl = "http://fqjj.net/images/big.jpg";
    private static final String imgurl2 = "http://fqjj.net/images/big2.jpg";
    private static final int REQUEST_TIMEOUT = 3000;
    private static final int SO_TIMEOUT = 5000;
    Button connectionButton, httpClientButton;
    private Bitmap imgBitmap;
    private ImageView imageView;
    private ImageView imageView2;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_client_main);
        connectionButton = (Button) this.findViewById(R.id.connection_button);
        httpClientButton = (Button) this.findViewById(R.id.http_client_button);
        imageView = (ImageView) findViewById(R.id.imageView);
        imageView2 = (ImageView) findViewById(R.id.imageView2);
        connectionButton.setOnClickListener(listener);
        httpClientButton.setOnClickListener(listener2);
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showProgress("任务正在执行中", "正在加载图片，请稍等...");
            new Thread() {
                @Override
                public void run() {
                    HttpURLConnection conn = null;
                    try {
                        URL url = new URL(imgurl);
                        conn = (HttpURLConnection) url.openConnection();
                        conn.setDoInput(true);
                        conn.setConnectTimeout(REQUEST_TIMEOUT);
                        conn.setReadTimeout(SO_TIMEOUT);
                        conn.connect();
                        InputStream stream = conn.getInputStream();
                        imgBitmap = BitmapFactory.decodeStream(stream);
                        handler.sendEmptyMessage(0x10);
                    } catch (java.io.IOException e) {
                        handler.sendEmptyMessage(0x00);
                        e.printStackTrace();
                    } finally {
                        conn.disconnect();
                    }
                }
            }.start();

        }
    };

    View.OnClickListener listener2 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showProgress("任务正在执行中", "正在加载图片，请稍等...");
            new Thread() {
                @Override
                public void run() {
                    HttpGet httpGet = new HttpGet(imgurl2);
                    BasicHttpParams httpParameters = new BasicHttpParams();
                    HttpConnectionParams.setConnectionTimeout(httpParameters,REQUEST_TIMEOUT);
                    HttpConnectionParams.setSoTimeout(httpParameters, SO_TIMEOUT);
                    DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
                    try {
                        HttpResponse response = httpClient.execute(httpGet);
                        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                            HttpEntity httpEntity = response.getEntity();
                            InputStream inputstream = httpEntity.getContent();
                            imgBitmap = BitmapFactory.decodeStream(inputstream);
                            handler.sendEmptyMessage(0x20);
                        }
                    } catch (IOException e) {
                        handler.sendEmptyMessage(0x00);
                        e.printStackTrace();
                    } finally {
                        httpClient.getConnectionManager().shutdown();
                    }
                }
            }.start();
        }
    };

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            hideProgress();
            switch (msg.what) {
                case 0x10:
                    imageView.setImageBitmap(imgBitmap);
                    break;
                case 0x20:
                    imageView2.setImageBitmap(imgBitmap);
                    break;
                case 0x00:
                    showProgress("请求超时", "请求超时 请稍后再请求");
                    break;
            }
        }

    };

    public void showProgress(String title, String content) {
        progressDialog = ProgressDialog.show(this, title, content, false, true);
    }

    public void hideProgress() {
        progressDialog.dismiss();
    }

}
