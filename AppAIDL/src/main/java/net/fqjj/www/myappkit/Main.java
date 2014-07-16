package net.fqjj.www.myappkit;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.joejoe.pictureviewer.mypictureviewer.IMyAidlInterface;
import com.joejoe.httpclientgo.myhttpclient.IHttpClientAidlInterface;
import com.joejoe.httpclientjson.httpclientjson.IHttpClientAidl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Main extends ActionBarActivity {

    private String[] remoteServieList = new String[]{
            "net.fqjj.www.PCITRUE_VIEW_AIDL_SERVICE",
            "net.fqjj.www.HTTP_CLIENT_AIDL_SERVICE",
            "net.fqjj.www.HTTP_CLIENT_AIDL_IMAGE"
    };

    private int remoteServieListStep = 0;

    private int[] applogos = {R.drawable.mountain, R.drawable.global, R.drawable.tag,
            R.drawable.music, R.drawable.pc, R.drawable.person,
            R.drawable.symbol, R.drawable.date, R.drawable.yy
    };

    private String[] titles = {"手机图片查看器", "远程图片查看器", "http远程JSON通信"};
    private List<Map<String, Object>> items;
    private int currentItem;
    private Toast toast;
    private IMyAidlInterface aidlService;
    private IHttpClientAidlInterface aidlService2;
    private IHttpClientAidl aidlService3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_grid);
        currentItem = R.id.grid_view;
        items = new ArrayList<Map<String, Object>>();

        for (int i = 0; i < applogos.length; i++) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("logo", applogos[i]);
            item.put("titles", titles[i]);
            items.add(item);
        }

        useGridLayout();
        toast = new Toast(this);
        TextView text = new TextView(this);
        text.setText("当前的列表项已选择");
        text.setTextSize(20);
        text.setPadding(10, 10, 10, 10);
        text.setTextColor(Color.WHITE);
        toast.setView(text);
        text.setBackgroundColor(Color.BLACK);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        bindRemoteService(remoteServieList[remoteServieListStep]);
    }


    private void bindRemoteService(final String action) {

        new Thread() {
            @Override
            public void run() {
                Intent intent = new Intent();
                intent.setAction(action);
                bindService(intent, conn, Service.BIND_AUTO_CREATE);
            }
        }.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (currentItem == id) {
            toast.show();
            return true;
        }

        if (id == R.id.list_view) {
            currentItem = R.id.list_view;
            useListLayout();
            return true;
        }

        if (id == R.id.grid_view) {
            currentItem = R.id.grid_view;
            useGridLayout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void useGridLayout() {

        this.setContentView(R.layout.activity_grid);
        GridView view = (GridView) findViewById(R.id.layout_view);
        SimpleAdapter adapter = new SimpleAdapter(this, items, R.layout.sub_item_grid,
                new String[]{"logo", "titles"}, new int[]{R.id.app_logo, R.id.app_title});
        view.setAdapter(adapter);
        view.setOnItemClickListener(listener);
        // 注册上下文菜单
        Main.this.registerForContextMenu(view);
    }

    private void useListLayout() {

        this.setContentView(R.layout.activity_list);
        ListView view = (ListView) findViewById(R.id.layout_view);
        SimpleAdapter adapter = new SimpleAdapter(this, items, R.layout.sub_item_list,
                new String[]{"logo", "titles"}, new int[]{R.id.app_logo, R.id.app_title});
        view.setAdapter(adapter);
        view.setOnItemClickListener(listener);
        // 注册上下文菜单
        Main.this.registerForContextMenu(view);
    }

    AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Intent intent = new Intent();

            switch (position) {
                case 0:
                    ComponentName component = new ComponentName(
                            "com.joejoe.pictureviewer.mypictureviewer",
                            "com.joejoe.pictureviewer.mypictureviewer.PictureViewer");
                    intent.setComponent(component);
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    startActivity(intent);
                    overridePendingTransition(R.anim.zoomin, R.anim.zooout);
                    break;

                case 1:
                    ComponentName component2 = new ComponentName(
                            "com.joejoe.httpclientgo.myhttpclient",
                            "com.joejoe.httpclientgo.myhttpclient.HttpClientMain");
                    intent.setComponent(component2);
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    startActivity(intent);
                    overridePendingTransition(R.anim.zoomin, R.anim.zooout);
                    break;

                case 2:
                    ComponentName component3 = new ComponentName(
                            "com.joejoe.httpclientjson.httpclientjson",
                            "com.joejoe.httpclientjson.httpclientjson.HttpClientJSONMain");
                    intent.setComponent(component3);
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    startActivity(intent);
                    overridePendingTransition(R.anim.zoomin, R.anim.zooout);
                    break;
            }
        }


    };


    @Override
    public void onCreateContextMenu(ContextMenu menu, View source, ContextMenu.ContextMenuInfo menuInfo) {
        // ContextMenuInfo转成 AdapterContextMenuInfo 获取当前点击的列表项
        int index = ((AdapterView.AdapterContextMenuInfo) menuInfo).position;
        showContextMenu(menu, index);
    }

    private void showContextMenu(ContextMenu menu, int index) {

        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.context_menu, menu);

        if (index == 0) {

            try {
                menu.getItem(0).setTitle("应用名称:" + aidlService.getSysName());
                menu.getItem(1).setTitle("作者:" + aidlService.getAuthor());
                menu.getItem(2).setTitle("应用创建时间:" + aidlService.getCreateTime());
                menu.getItem(3).setTitle("应用修改时间:" + aidlService.getModifyTime());
                menu.getItem(4).setTitle("版本号:" + aidlService.getVersion());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        if (index == 1) {
            try {
                menu.getItem(0).setTitle("应用名称:" + aidlService2.getSysName());
                menu.getItem(1).setTitle("作者:" + aidlService2.getAuthor());
                menu.getItem(2).setTitle("应用创建时间:" + aidlService2.getCreateTime());
                menu.getItem(3).setTitle("应用修改时间:" + aidlService2.getModifyTime());
                menu.getItem(4).setTitle("版本号:" + aidlService2.getVersion());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        if (index == 2) {
            try {
                menu.getItem(0).setTitle("应用名称:" + aidlService3.getSysName());
                menu.getItem(1).setTitle("作者:" + aidlService3.getAuthor());
                menu.getItem(2).setTitle("应用创建时间:" + aidlService3.getCreateTime());
                menu.getItem(3).setTitle("应用修改时间:" + aidlService3.getModifyTime());
                menu.getItem(4).setTitle("版本号:" + aidlService3.getVersion());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            if (remoteServieListStep == 0) {
                aidlService = IMyAidlInterface.Stub.asInterface(service);
            }

            if (remoteServieListStep == 1) {
                aidlService2 = IHttpClientAidlInterface.Stub.asInterface(service);
            }

            if (remoteServieListStep == 2) {
                aidlService3 = IHttpClientAidl.Stub.asInterface(service);
            }

            remoteServieListStep++;

            if (remoteServieListStep >= remoteServieList.length) {
                return;
            }
            bindRemoteService(remoteServieList[remoteServieListStep]);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            aidlService = null;
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.unbindService(conn);
    }
}
