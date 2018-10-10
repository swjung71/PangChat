package kr.co.digitalanchor.pangchat.handler;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import kr.co.digitalanchor.pangchat.utils.LruBitmapCache;

/**
 * Created by Xian on 2016-08-16.
 */
public class VolleySingleton {

    private static VolleySingleton mInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static Context mCtx;

    private VolleySingleton(Context context){
        mCtx = context;

        mRequestQueue = getmRequestQueue();

        mImageLoader = new ImageLoader(mRequestQueue, new LruBitmapCache(LruBitmapCache.getCacheSize(mCtx)));
    }

    public static synchronized VolleySingleton getmInstance(Context context){
        if(mInstance == null){
            mInstance = new VolleySingleton(context);
        }

        return mInstance;
    }

    public RequestQueue getmRequestQueue(){
        if(mRequestQueue == null){
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext(), new OkHttp3Stack());
        }
        return mRequestQueue;
    }

    public ImageLoader getmImageLoader(){
        return mImageLoader;
    }
}
