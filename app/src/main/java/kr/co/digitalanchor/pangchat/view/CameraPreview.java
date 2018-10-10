package kr.co.digitalanchor.pangchat.view;

/**
 * Created by Peter Jung on 2016-11-17.
 */

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.orhanobut.logger.Logger;

import java.io.IOException;

/** A basic Camera preview class */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private Camera mCamera;
    //private static boolean isBlocked = false;

    public CameraPreview(Context context, Camera camera) {
        super(context);
        mCamera = camera;

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        mHolder = holder;
        try {
            if(holder != null && mCamera != null) {
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();
            }
        } catch (IOException e) {
            Logger.d("Error setting camera preview: " + e.getMessage());
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty. Take care of releasing the Camera preview in your activity.
        if(mCamera != null) {
            Logger.i("surfaceDestroyed");
            try {
                mCamera.stopPreview();
                mCamera.setPreviewCallback(null);
                mCamera.release();
                mCamera = null;
            }catch (RuntimeException e){
                Logger.i("surfaceDesotry runtime error");
                return;
            }

        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.
        Logger.i("surfaceChanged");
        if (mHolder.getSurface() == null){
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            if(mCamera != null) {
                mCamera.stopPreview();
            }
        } catch (Exception e){
            // ignore: tried to stop a non-existent preview
            e.printStackTrace();
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        try {

            if(mCamera != null ) {
                //if(!isBlocked) {
                    mCamera.setPreviewDisplay(holder);
                    //Thread.sleep(2000);
                    Logger.i("start Preview");
                    mCamera.startPreview();
                //}
            }

        } catch (Exception e){
            Logger.d("Error starting camera preview: " + e.getMessage());
        }
    }

    public void setmCamera(Camera mCamera) {
        this.mCamera = mCamera;
    }

/*    public static void setBlocked(boolean isBlockedParam){
        isBlocked = isBlockedParam;
    }*/

    public void releaseCamera(){
        Logger.i("start releaseCamera");
        if(mCamera != null) {
            Logger.i("releaseCamera");
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }
}