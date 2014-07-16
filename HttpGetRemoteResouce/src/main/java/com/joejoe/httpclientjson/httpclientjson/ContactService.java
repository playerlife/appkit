package com.joejoe.httpclientjson.httpclientjson;

import android.net.Uri;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ContactService {

    private static final String path = "http://fqjj.net/images/imgdata.js";
    private static ContactService singleton;

    private ContactService() {
    }

    public static ContactService getInstance() {

        if (singleton == null) {
            singleton = new ContactService();
        }
        return singleton;
    }

    public List<ContactBean> getContactList() {

        List<ContactBean> contacts = null;
        HttpGet httpGet = new HttpGet(path);
        BasicHttpParams httpParams = new BasicHttpParams();
        httpGet.addHeader("Content-Type", "text/html;charset=UTF-8");
        HttpConnectionParams.setConnectionTimeout(httpParams, 3000);
        HttpConnectionParams.setSoTimeout(httpParams, 5000);
        DefaultHttpClient httpClient = new DefaultHttpClient(httpParams);

        try {
            HttpResponse response = httpClient.execute(httpGet);

            if (response.getStatusLine().getStatusCode() == 200) {
                String jsonStr = EntityUtils.toString(response.getEntity(), "gbk");
                jsonStr = jsonStr.replace("\r", "");
                jsonStr = jsonStr.replace("\n", "");
                jsonStr = jsonStr.replace("\t", "");
                jsonStr = jsonStr.replace("\\", "");
                contacts = getJsonData(jsonStr);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return contacts;
    }

    private List<ContactBean> getJsonData(String jsonStr) {

        List<ContactBean> contacts = new ArrayList<ContactBean>();
        ContactBean bean = null;
        JSONObject object = null;

        try {
            JSONArray jsonArray = new JSONArray(jsonStr);

            for (int i = 0; i < jsonArray.length(); i++) {
                object = jsonArray.getJSONObject(i);
                bean = new ContactBean();
                bean.setId(object.getInt("id"));
                bean.setName(object.getString("name"));
                bean.setImage(object.getString("image"));
                contacts.add(bean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return contacts;
    }

    public Uri getImageURI(String path, File cacheFile) {

        String fileName = path.substring(path.lastIndexOf("/") + 1);
        File file = new File(cacheFile, fileName);

        if (file.exists()) {
            return Uri.fromFile(file);
        }
        HttpGet httpGet = new HttpGet(path);
        BasicHttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 3000);
        HttpConnectionParams.setSoTimeout(httpParams, 5000);
        DefaultHttpClient httpClient = new DefaultHttpClient(httpParams);

        try {
            HttpResponse response = httpClient.execute(httpGet);

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                InputStream inputStream = entity.getContent();
                FileOutputStream outputStream = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int len = 0;

                while ((len = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, len);
                }
                inputStream.close();
                outputStream.close();
                return Uri.fromFile(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}



