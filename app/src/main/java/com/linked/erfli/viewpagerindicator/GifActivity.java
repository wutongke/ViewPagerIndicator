package com.linked.erfli.viewpagerindicator;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class GifActivity extends AppCompatActivity {
    LinkedBlockingQueue<Bitmap> bitmaps;
    ImageView imageView;
    AtomicInteger atomicInteger = new AtomicInteger();
    private List<FileEntity> fileEntityList;
    private Subscription subscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif);
        imageView = (ImageView) findViewById(R.id.play_image);
        ReuseBitmapUtil.init();
        bitmaps = new LinkedBlockingQueue<>(1);
        loadBitmap();
        imageView.post(setImageDrawableRunnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ReuseBitmapUtil.clear();
        bitmaps = null;
        subscription.unsubscribe();
        imageView.removeCallbacks(setImageDrawableRunnable);
        imageView.removeCallbacks(createBitmapRunnable);
    }

    private void loadBitmap() {
        try {
            String fileNames[] = this.getAssets().list("gif");
            if (fileNames == null || fileNames.length == 0) {
                Log.i("fileSize", "null");
                return;
            }
            Log.i("fileSize", "1:" + fileNames.length);

            List<FileEntity> fileEntityList = new ArrayList<>();
            for (int i = 0, count = fileNames.length; i < count; i++) {
                FileEntity fileEntity = new FileEntity();
                fileEntity.id = i;
                fileEntity.path = fileNames[i];
                fileEntityList.add(fileEntity);
            }
            this.fileEntityList = fileEntityList;
            imageView.post(createBitmapRunnable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Runnable createBitmapRunnable = new Runnable() {
        @Override
        public void run() {
            if (fileEntityList != null) {
                atomicInteger.set(0);
                productBitmap(fileEntityList);
            }
        }
    };
    private Runnable setImageDrawableRunnable = new Runnable() {
        @Override
        public void run() {
            if (bitmaps == null)
                return;
            Bitmap bitmap = bitmaps.poll();
            if (bitmap != null) {
                if (imageView.getDrawable() != null && ((BitmapDrawable) imageView.getDrawable()).getBitmap() != null) {
                    ReuseBitmapUtil.mReusableBitmaps.add(new SoftReference<>(((BitmapDrawable) imageView.getDrawable()).getBitmap()));
                }

                imageView.setImageBitmap(bitmap);
            }
            imageView.postDelayed(this, 100);
        }
    };

    private Bitmap createBitmap(FileEntity fileEntity) {
        Bitmap bitmap = ReuseBitmapUtil.getReuseBitmap(GifActivity.this, fileEntity);
        if (bitmap == null) {
            try {
                bitmap = BitmapFactory.decodeStream(getAssets().open("gif/" + fileEntity.path));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.i("bitmap", "new");
        } else {
            Log.i("bitmap", "reuse");
        }
        bitmap = Bitmap.createScaledBitmap(bitmap, imageView.getWidth(), imageView.getHeight(), true);
        return bitmap;
    }

    private void putBitmapToQueue(Bitmap bitmap, FileEntity fileEntity) {
        //保证顺序提交bitmap
        while (atomicInteger.get() != fileEntity.id) ;
        try {
            if (bitmaps != null) {
                bitmaps.put(bitmap);
                atomicInteger.getAndIncrement();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void productBitmap(final List<FileEntity> fileEntityList) {
        subscription = Observable.from(fileEntityList)
                .observeOn(Schedulers.io())
                .subscribe(new Action1<FileEntity>() {
                    @Override
                    public void call(FileEntity fileEntity) {
                        Bitmap bitmap = createBitmap(fileEntity);
                        putBitmapToQueue(bitmap, fileEntity);
                        //循环执行动画
                        if (atomicInteger.get() == fileEntityList.size()) {
                            imageView.post(createBitmapRunnable);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.i("bitmap", throwable.toString());
                    }
                });
    }

    class FileEntity {
        String path;
        int id;
    }
}
