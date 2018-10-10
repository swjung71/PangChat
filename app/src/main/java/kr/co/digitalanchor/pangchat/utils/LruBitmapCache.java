package kr.co.digitalanchor.pangchat.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by Xian on 2016-08-16.
 */
public class LruBitmapCache extends LruCache<String, Bitmap> implements ImageLoader.ImageCache {
    public LruBitmapCache(int maxSize) {
        super(maxSize);
    }
    public LruBitmapCache(Context context){
        this(getCacheSize(context));
    }
    @Override
    protected int sizeOf(String key, Bitmap value){
        return value.getRowBytes() * value.getHeight();
    }
    @Override
    public Bitmap getBitmap(String url){
        return get(url);
    }
    @Override
    public void putBitmap(String url, Bitmap bitmap){
        put(url, bitmap);
    }
    //Return a cache size equal to approximately 5 scrrens worth of images.
    public static int getCacheSize(Context ctx){
        final DisplayMetrics displayMetrics = ctx.getResources().getDisplayMetrics();
        final int screenWidth = displayMetrics.widthPixels;
        final int screenHeight = displayMetrics.heightPixels;

        //4 bytes per pixel
        final int screenBytes = screenHeight * screenWidth *4;

        return screenBytes * 5;
    }
}
