package com.joejoe.pictureviewer.mypictureviewer;

import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ViewFlipper;

import java.util.ArrayList;

public class IMGList extends ActionBarActivity implements View.OnTouchListener,
        GestureDetector.OnGestureListener {

    private RelativeLayout root;
    private Button leftButton, rightButton;
    private ArrayList<String> fileNamesPath = new ArrayList<String>();
    private ViewFlipper viewFlipper;
    private BitmapFactory.Options options;
    private ImageView preView, targetView, nextView;
    private int currentImgIdx = 0;
    private DisplayMetrics dm;
    private GestureDetector mGestureDetector;
    private static final int FLING_MIN_DISTANCE = 50;
    private static final int FLING_MIN_VELOCITY = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imglist);
        dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        findView();
        Cursor cursor = this.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        options = new BitmapFactory.Options();
        options.inSampleSize = 8;

        while (cursor.moveToNext()) {
            // 获取图片的保存位置的数据
            byte[] data = cursor.getBlob(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            fileNamesPath.add(String.valueOf(new String(data, 0, data.length - 1)));

        }
        setImageIndex();
        root.setOnTouchListener(this);
        mGestureDetector = new GestureDetector(this);
        root.setLongClickable(true);
    }

    private void findView() {
        root = (RelativeLayout) findViewById(R.id.root);
        leftButton = (Button) findViewById(R.id.left_btn);
        rightButton = (Button) findViewById(R.id.right_btn);
        viewFlipper = (ViewFlipper) findViewById(R.id.filpper);
        preView = (ImageView) findViewById(R.id.pre);
        targetView = (ImageView) findViewById(R.id.target);
        nextView = (ImageView) findViewById(R.id.next);
    }

    private void setImageIndex() {
        preView.setImageBitmap(BitmapFactory.decodeFile(fileNamesPath.get(currentImgIdx), options));
        targetView.setImageBitmap(BitmapFactory.decodeFile(fileNamesPath.get(currentImgIdx + 1), options));
        nextView.setImageBitmap(BitmapFactory.decodeFile(fileNamesPath.get(currentImgIdx + 2), options));
    }

    public void prev(View view) {
        if (currentImgIdx <= 0) return;
        currentImgIdx--;
        setImageIndex();
        viewFlipper.setInAnimation(this, R.anim.slide_in_right);
        viewFlipper.setOutAnimation(this, R.anim.slide_out_left);
        viewFlipper.showPrevious();
        viewFlipper.stopFlipping();
    }

    public void next(View view) {

        if (currentImgIdx >= fileNamesPath.size() - 1) return;
        currentImgIdx++;
        setImageIndex();
        viewFlipper.setInAnimation(this, android.R.anim.slide_in_left);
        viewFlipper.setOutAnimation(this, android.R.anim.slide_out_right);
        viewFlipper.showNext();
        viewFlipper.stopFlipping();
    }

    public void auto(View view) {

        if (currentImgIdx >= fileNamesPath.size() - 1) return;
        currentImgIdx++;
        setImageIndex();
        viewFlipper.setInAnimation(this, android.R.anim.slide_in_left);
        viewFlipper.setOutAnimation(this, android.R.anim.slide_out_right);
        viewFlipper.startFlipping();
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    private void changeButtonVisibility(MotionEvent e) {

        int buttonWidth = leftButton.getWidth();

        if (e.getX() < buttonWidth) {
            leftButton.setVisibility(View.VISIBLE);
            rightButton.setVisibility(View.INVISIBLE);
        }

        if (e.getX() > dm.widthPixels - buttonWidth) {
            rightButton.setVisibility(View.VISIBLE);
            leftButton.setVisibility(View.INVISIBLE);
        }

        if (e.getX() > buttonWidth && e.getX() < dm.widthPixels - buttonWidth) {
            rightButton.setVisibility(View.INVISIBLE);
            leftButton.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE
                && Math.abs(velocityX) > FLING_MIN_VELOCITY) {

            if (!(currentImgIdx <= 0)) {
                currentImgIdx--;
                setImageIndex();
                viewFlipper.setInAnimation(this, R.anim.slide_in_right);
                viewFlipper.setOutAnimation(this, R.anim.slide_out_left);
                viewFlipper.showPrevious();
                viewFlipper.stopFlipping();
            }

        } else if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE
                && Math.abs(velocityX) > FLING_MIN_VELOCITY) {

            if (!(currentImgIdx >= fileNamesPath.size() - 1)) {
                currentImgIdx++;
                setImageIndex();
                viewFlipper.setInAnimation(this, android.R.anim.slide_in_left);
                viewFlipper.setOutAnimation(this, android.R.anim.slide_out_right);
                viewFlipper.showNext();
                viewFlipper.stopFlipping();
            }
        }
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            Log.d("MotionEvent================>", String.valueOf(event.getX()) + "<===>" + String.valueOf(event.getX()));
            changeButtonVisibility(event);
        }
        return mGestureDetector.onTouchEvent(event);

    }
}
