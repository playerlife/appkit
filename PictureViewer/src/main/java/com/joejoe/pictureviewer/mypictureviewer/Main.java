package com.joejoe.pictureviewer.mypictureviewer;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class Main extends ActionBarActivity implements OnClickListener {

        @Override
        protected void onCreate(Bundle savedInstanceState) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            TextView buttonClick = (TextView) findViewById(R.id.button_click);
            buttonClick.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            if (R.id.button_click == view.getId()) {
                forwardActivity();
            }
        }

        private void forwardActivity() {
            Intent intent = new Intent(Main.this, IMGList.class);
            startActivity(intent);
            overridePendingTransition(R.anim.zoomin, R.anim.zooout);
            finish();
        }
    }
