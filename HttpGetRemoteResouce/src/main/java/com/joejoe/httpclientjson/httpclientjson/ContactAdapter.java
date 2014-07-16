package com.joejoe.httpclientjson.httpclientjson;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

public class ContactAdapter extends BaseAdapter {

    private Context context;
    private List<ContactBean> beans;
    private File fileCache;
    private LayoutInflater inflater;

    public ContactAdapter(Context context, List<ContactBean> beans, File fileCache) {

        this.context = context;
        this.beans = beans;
        this.fileCache = fileCache;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return beans.size();
    }

    @Override
    public Object getItem(int position) {
        return beans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = inflater.inflate(R.layout.adapter_item, null);
        ImageView image = (ImageView) view.findViewById(R.id.img);
        TextView txt = (TextView) view.findViewById(R.id.title);
        ContactBean bean = beans.get(position);
        txt.setText(bean.getName());
        httpClientGetImage(image, bean.getImage());
        return view;
    }

    private void httpClientGetImage(ImageView image, String imgPath) {

        ContactService servce = ContactService.getInstance();
        AsyncImageTask task = new AsyncImageTask(servce, image);
        task.execute(imgPath);
    }


    /**
     * 使用AsyncTask来防止加载线程启的太多系统崩溃
     * @String execute() 的参数类型
     * @Integer onProgressUpdate() 参数类型
     * @Uri onPostExecute 返回的参数型
     */
    private class AsyncImageTask extends AsyncTask<String, Integer, Uri> {

        private ContactService service;
        private ImageView view;

        public AsyncImageTask(ContactService service, ImageView view) {
            this.service = service;
            this.view = view;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Uri uri) {

            super.onPostExecute(uri);
            // UI线程设置图片
            if (view != null && uri != null) {
                view.setImageURI(uri);
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Uri doInBackground(String... params) {
            // 新线程获取图片
            Uri uri = service.getImageURI(params[0], fileCache);
            return uri;
        }
    }
}
