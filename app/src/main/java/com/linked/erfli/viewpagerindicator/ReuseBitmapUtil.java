package com.linked.erfli.viewpagerindicator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by erfli on 12/27/16.
 */

public class ReuseBitmapUtil {

    public static Set<SoftReference<Bitmap>> mReusableBitmaps;

    public static void init() {
        if (mReusableBitmaps == null)
            mReusableBitmaps =
                    Collections.synchronizedSet(new HashSet<SoftReference<Bitmap>>());
    }

    public static void clear() {
        if (mReusableBitmaps != null)
            mReusableBitmaps.clear();
    }

    public static Bitmap getReuseBitmap(Context context, GifActivity.FileEntity fileEntity) {
        Bitmap bitmap = null;
        if (ReuseBitmapUtil.mReusableBitmaps != null && ReuseBitmapUtil.mReusableBitmaps.size() > 0) {
            Log.i("bitmap", "before:" + ReuseBitmapUtil.mReusableBitmaps.size());
            final BitmapFactory.Options options = new BitmapFactory.Options();
            SoftReference softReference = ReuseBitmapUtil.mReusableBitmaps.iterator().next();
            ReuseBitmapUtil.mReusableBitmaps.remove(softReference);
            Bitmap reuseBitmap = (Bitmap) softReference.get();
            if (reuseBitmap != null && !reuseBitmap.isRecycled()) {
                options.inMutable = true;
                options.inBitmap = reuseBitmap;
                Log.i("bitmap", "after:" + ReuseBitmapUtil.mReusableBitmaps.size());
            }
            options.inJustDecodeBounds = false;
            try {
                bitmap = BitmapFactory.decodeStream(context.getAssets().open("gif/" + fileEntity.path), null, options);
            } catch (IOException e) {
                e.printStackTrace();
                Log.i("bitmap", e.toString());
            }
        }
        return bitmap;
    }
}
