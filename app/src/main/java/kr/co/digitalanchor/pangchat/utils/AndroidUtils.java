package kr.co.digitalanchor.pangchat.utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.PowerManager;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.orhanobut.logger.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import kr.co.digitalanchor.pangchat.R;

public class AndroidUtils {
    // convert InputStream to String
    public static String inputStreamToString(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {

            return null;

        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();

    }

    // convert from UTF-8 -> internal Java String format
    public static String convertFromUTF8(String s) {
        String out = null;
        try {
            out = new String(s.getBytes("ISO-8859-1"), "UTF-8");
        } catch (Exception e) {

            return null;
        }
        return out;
    }

    // convert from internal Java String format -> UTF-8
    public static String convertToUTF8(String s) {

        String out = null;

        try {

            out = new String(s.getBytes("UTF-8"), "ISO-8859-1");

        } catch (java.io.UnsupportedEncodingException e) {

            return null;
        }

        return out;
    }

    public static boolean containsIgnoreCase(String src, String what) {
        final int length = what.length();
        if (length == 0)
            return true; // Empty string is contained

        final char firstLo = Character.toLowerCase(what.charAt(0));
        final char firstUp = Character.toUpperCase(what.charAt(0));

        for (int i = src.length() - length; i >= 0; i--) {
            // Quick check before calling the more expensive regionMatches() method:
            final char ch = src.charAt(i);

            if (ch != firstLo && ch != firstUp)
                continue;

            if (src.regionMatches(true, i, what, 0, length))
                return true;
        }

        return false;
    }

    public static String getCurrentTimeIncludeMs() {

        Calendar cal = Calendar.getInstance();

        cal.getTime();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        return format.format(cal.getTime());
    }

    public static String getCurrentTime4Chat() {

        Calendar calendar = Calendar.getInstance();

        calendar.getTime();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return format.format(calendar.getTime());
    }

    public static String convertTimeStamp4Chat(long time) {

        Date date = new Date(time);

        SimpleDateFormat format = new SimpleDateFormat("HH:mm");

        return format.format(date);
    }

    public static String convertDateStamp4Chat(long time) {

        Date date = new Date(time);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        return format.format(date);
    }

    public static String convertCurrentTime4Chat(long time) {

        Date date = new Date(time);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return format.format(date);
    }

    public static void initializeApp(Context context) {

        //DBHelper helper = new DBHelper(context);
    }

    public static void showKeyboard(View view) {

        if (view == null) {

            return;
        }

        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    public static boolean isKeyboardShowed(View view) {

        if (view == null) {

            return false;
        }

        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        return imm.isActive();
    }

    public static void hideKeyboard(View view) {

        if (view == null) {

            return;
        }

        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        if (!imm.isActive()) {

            return;
        }

        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showNotification(Context context, String title, String text, PendingIntent intent, boolean sound, boolean vib, boolean reject) {


        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        builder.setSmallIcon(R.drawable.ic_notification);

        builder.setAutoCancel(true);

        if (!TextUtils.isEmpty(title)) {

            builder.setContentTitle(title);

        } else {

            builder.setContentTitle(context.getString(R.string.app_name));
        }

        if (!TextUtils.isEmpty(text)) {

            builder.setContentText(text);
        }

        if (intent != null) {

            Logger.i("intent is not null");
            builder.setContentIntent(intent);
        }


        if (sound) {
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            builder.setSound(alarmSound);
        }

        if (vib) {
            builder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
        }
        nm.notify(2, builder.build());

    }

    public static void showNotificationNotice(Context context, String title, String text,PendingIntent intent) {


        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        builder.setSmallIcon(R.drawable.ic_notification);

        builder.setAutoCancel(true);

        if (!TextUtils.isEmpty(title)) {

            builder.setContentTitle(title);

        } else {

            builder.setContentTitle(context.getString(R.string.app_name));
        }

        if (!TextUtils.isEmpty(text)) {

            builder.setContentText(text);
        }

        if (intent != null) {

            Logger.i("intent is not null");
            builder.setContentIntent(intent);
        }

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(alarmSound);

        builder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
        nm.notify(2, builder.build());
        Logger.i("notified notice");
    }

    /**
     * 잠자는 스크린을 깨운다.
     *
     * @param context
     */
    public static void acquireCpuWakeLock(Context context) {

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);

        PowerManager.WakeLock cpuWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK |
                        PowerManager.ACQUIRE_CAUSES_WAKEUP,
                context.getPackageName());

        if (cpuWakeLock == null) {

            return;
        }

        cpuWakeLock.acquire(10000);
    }

    public static boolean isNetworkAvailable(Context context) {

        try {

            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo.State wifi = manager.getNetworkInfo(1).getState();

            if (wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING) {

                return true;
            }

            NetworkInfo.State mobile = manager.getNetworkInfo(0).getState();

            if (mobile == NetworkInfo.State.CONNECTED || mobile == NetworkInfo.State.CONNECTING) {

                return true;
            }

        } catch (Exception e) {

            return false;

        }
        return false;
    }

}
