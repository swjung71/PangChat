package kr.co.digitalanchor.pangchat.utils;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by Xian on 2016-08-12.
 */
public class Utils {

    private static Toast logToast = null;


    /**
     * 화면에 Toast를 짧게 출력
     * @param activity Activity
     * @param msg String, 출력 메세지
     */
    public static void showToast(final Activity activity, final String msg) {
        activity.runOnUiThread(new Runnable(){
            public void run()
            {
                if (logToast != null) {
                    logToast.cancel();
                    logToast = null;
                }
                logToast = Toast.makeText(activity.getApplicationContext(), msg, Toast.LENGTH_SHORT);
                logToast.show();
            }

        });
    }

}
