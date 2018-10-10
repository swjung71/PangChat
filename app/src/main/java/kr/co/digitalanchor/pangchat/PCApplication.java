package kr.co.digitalanchor.pangchat;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import kr.co.digitalanchor.pangchat.act.IntroActivity;
import kr.co.digitalanchor.pangchat.act.MainActivity;
import kr.co.digitalanchor.pangchat.utils.StaticValues;
import kr.co.digitalanchor.pangchat.utils.Utils;

import static kr.co.digitalanchor.pangchat.utils.StaticValues.PREF;
/**
 * Created by Peter Jung on 2016-10-26.
 */
public class PCApplication extends Application {

    public static volatile Context applicationContext;
    public static volatile Handler applicationHandler;
    private static final List<BaseActivity> activities = new ArrayList<>();
    private static Location location;
    public static GoogleAnalytics analytics;
    public static Tracker tracker;
    private static boolean isCall;
    private static boolean isVideo;
    private static boolean isWaiting;

    private static final Hashtable<String, Typeface> typefaceCache = new Hashtable<>();

    static Pattern URL_PATTERN;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = getApplicationContext();

        analytics = GoogleAnalytics.getInstance(this);
        analytics.setDryRun(false);
        analytics.setLocalDispatchPeriod(1800);

        tracker = analytics.newTracker("UA-89368149-1");
        tracker.setAppName("pangchat");
        tracker.setAppVersion(getAppVersionName());
        tracker.enableExceptionReporting(true);
        tracker.enableAdvertisingIdCollection(true);
        tracker.enableAutoActivityTracking(true);

        /*
        * Log Setting
        */

        Logger.init("PangChat").setLogLevel(LogLevel.NONE).hideThreadInfo();
        URL_PATTERN = Pattern.compile("^(https?):\\/\\/([^:\\/\\s]+)(:([^\\/]*))?((\\/[^\\s/\\/]+)*)?\\/([^#\\s\\?]*)(\\?([^#\\s]*))?(#(\\w*))?$");
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);

        // TODO 언어 설정이 변경되면!!!
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public static String getPhoneNumber() {

        String number = null;

        try {

            TelephonyManager manager = (TelephonyManager) applicationContext.getSystemService(Context.TELEPHONY_SERVICE);

            number = manager.getLine1Number();

            number = "0" + number.substring(number.length() - 10, number.length());

        } catch (Exception e) {

            number = null;
        }

        return TextUtils.isEmpty(number) ? "" : number;
    }

    /* Device의 고유 번호 */
    public static String getDeviceNumber() {

        TelephonyManager tm = (TelephonyManager) applicationContext.getSystemService(Context.TELEPHONY_SERVICE);

        String id = tm.getDeviceId();

        if (TextUtils.isEmpty(id)) {

            id = getDeviceID();
        }

        return id;
    }

    /* 직접 사용하지 않음 */
    public static String getDeviceID() {

        UUID uuid = null;

        final String androidId = Settings.Secure.getString(applicationContext.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        try {

            if (!"9774d56d682e549c".equals(androidId)) {

                uuid = UUID.nameUUIDFromBytes(androidId.getBytes("utf8"));

            } else {

                TelephonyManager manager = (TelephonyManager) applicationContext.getSystemService(Context.TELEPHONY_SERVICE);

                final String deviceId = manager.getDeviceId();

                uuid = deviceId != null ?
                        UUID.nameUUIDFromBytes(deviceId.getBytes("utf8")) : UUID.randomUUID();
            }

        } catch (UnsupportedEncodingException e) {

            throw new RuntimeException(e);
        }

        return uuid.toString().replaceAll("-", "");

    }

    public static SharedPreferences getPreference() {

        return applicationContext.getSharedPreferences(PREF, MODE_PRIVATE);

    }

    /**
     * 프리퍼런스에 저장
     *
     * @param key
     * @param value
     * @return
     */
    public static boolean putString(String key, String value) {

        SharedPreferences pref = applicationContext.getSharedPreferences(PREF, MODE_PRIVATE);

        SharedPreferences.Editor editor = pref.edit();

        editor.putString(key, value);

        return editor.commit();
    }

    public static String getString(String key) {

        return getString(key, null);
    }

    public static String getString(String key, String defaultValue) {

        SharedPreferences pref = applicationContext.getSharedPreferences(PREF, MODE_PRIVATE);

        return pref.getString(key, defaultValue);
    }

    public static int getInt(String key, int defaultValue) {

        SharedPreferences pref = applicationContext.getSharedPreferences(PREF, MODE_PRIVATE);

        return pref.getInt(key, defaultValue);
    }

    public static boolean putInt(String key, int value) {

        SharedPreferences pref = applicationContext.getSharedPreferences(PREF, MODE_PRIVATE);

        SharedPreferences.Editor editor = pref.edit();

        editor.putInt(key, value);

        return editor.commit();
    }


    public static boolean getBoolean(String key, boolean defaultValue) {

        try {

            SharedPreferences pref = applicationContext.getSharedPreferences(PREF, MODE_PRIVATE);

            return pref.getBoolean(key, defaultValue);

        } catch (Exception e) {

            return defaultValue;
        }
    }

    public static boolean putBoolean(String key, boolean value) {

        SharedPreferences pref = applicationContext.getSharedPreferences(PREF, MODE_PRIVATE);

        SharedPreferences.Editor editor = pref.edit();

        editor.putBoolean(key, value);

        return editor.commit();
    }

    public static boolean putRegistrationId(String version) {

        return putString(StaticValues.GCM_REG_ID, version);
    }

    public static String getRegistrationId() {

        return getString(StaticValues.GCM_REG_ID, "");
    }

    public static boolean putCity(String city) {

        return putString(StaticValues.CITY, city);
    }

    public static String getCity() {

        return getString(StaticValues.CITY, "");
    }

    public static void clear() {

        SharedPreferences pref = applicationContext.getSharedPreferences(PREF, MODE_PRIVATE);

        SharedPreferences.Editor editor = pref.edit();

        editor.clear();

        editor.commit();
    }


    private boolean saveSharedPreferenceToFile(File dest) {

        boolean res = false;

        ObjectOutputStream output = null;

        try {

            output = new ObjectOutputStream(new FileOutputStream(dest));

            SharedPreferences pref = getSharedPreferences(PREF, MODE_PRIVATE);

            output.writeObject(pref.getAll());

            res = true;

        } catch (Exception e) {

            res = false;

        } finally {

            try {

                if (output != null) {

                    output.flush();
                    output.close();
                }

            } catch (IOException e) {

                output = null;
            }
        }

        return res;
    }

    private boolean loadSharedPreferencesFromFile(File src) {

        boolean res = false;

        ObjectInputStream input = null;

        try {

            input = new ObjectInputStream(new FileInputStream(src));

            SharedPreferences.Editor editor = getSharedPreferences(PREF, MODE_PRIVATE).edit();

            editor.clear();

            Map<String, ?> entries = (Map<String, ?>) input.readObject();

            for (Map.Entry<String, ?> entry : entries.entrySet()) {

                Object value = entry.getValue();

                String key = entry.getKey();

                if (value instanceof Boolean) {

                    editor.putBoolean(key, ((Boolean) value).booleanValue());

                } else if (value instanceof Float) {

                    editor.putFloat(key, ((Float) value).floatValue());

                } else if (value instanceof Integer) {

                    editor.putInt(key, ((Integer) value).intValue());

                } else if (value instanceof Long) {

                    editor.putLong(key, ((Long) value).longValue());

                } else if (value instanceof String) {

                    editor.putString(key, (String) value);
                }
            }

            editor.commit();

            res = true;

        } catch (Exception e) {

            res = false;

        } finally {

            try {

                if (input != null) {

                    input.close();
                }

            } catch (IOException e) {

                input = null;
            }
        }

        return res;
    }

    public static boolean putAppdVersion(int version) {

        return putInt("property_app_version", version);
    }

    public static int getRegisteredVersion() {

        return getInt("property_app_version", Integer.MIN_VALUE);
    }

    /**
     * @return version code in manifest.xml
     * 1 2 이런 식으로 정수가 하나씩 증가하는 값
     */
    public static int getAppVersionCode() {

        try {

            PackageInfo info = applicationContext.getPackageManager()
                    .getPackageInfo(applicationContext.getPackageName(), 0);

            return info.versionCode;

        } catch (PackageManager.NameNotFoundException e) {

            throw new RuntimeException(e);
        }
    }

    public static String getNationalCode() {

        String countryCode = null;

        try {

            TelephonyManager manager = (TelephonyManager) applicationContext.getSystemService(Context.TELEPHONY_SERVICE);

            countryCode = manager.getSimCountryIso();

        } catch (Exception e) {

            countryCode = null;
        }

        return TextUtils.isEmpty(countryCode) ? "" : countryCode;


        //return "kr";
    }
    /**
     * @return version number in manifest.xml
     * 1.0.1 이런식의 version name
     */
    public static String getAppVersionName() {

        try {

            PackageInfo info = applicationContext.getPackageManager()
                    .getPackageInfo(applicationContext.getPackageName(), 0);

            return info.versionName;

        } catch (PackageManager.NameNotFoundException e) {

            throw new RuntimeException(e);
        }
    }

    /**
     * 서버의 version과 현재 앱의 version을 비교하여 서버의 version이 높으면 true(업데이트 필요)를 return
     * @param currentVersionName 서버에서 받아온 version number 이며
     * @return update가 필요하면 true를 return
     */
    public static boolean isUpdate(String currentVersionName) {

        boolean isUpdate = false;

        try {

            PackageInfo info = applicationContext.getPackageManager()
                    .getPackageInfo(applicationContext.getPackageName(), 0);

            String[] appVersionTokens = info.versionName.split("\\.");

            String[] curVersionTokens = currentVersionName.split("\\.");


            if(appVersionTokens.length < curVersionTokens.length){
                isUpdate = true;
                return isUpdate;
            }

            for (int i = 0; appVersionTokens.length > i; i++) {

                try {

                    if (Integer.parseInt(appVersionTokens[i]) < Integer.parseInt(curVersionTokens[i])) {

                        isUpdate = true;

                        break;

                    } else if (Integer.parseInt(appVersionTokens[i]) > Integer.parseInt(curVersionTokens[i])) {

                        isUpdate = false;

                        break;
                    }

                } catch (NumberFormatException e) {

                    isUpdate = false;

                    break;
                }
            }

        } catch (Exception e) {

            isUpdate = false;
        }

        return isUpdate;
    }

    public static void addActivity(BaseActivity activity) {

        activities.add(activity);
    }

    public static BaseActivity getLastActivity(){
        return activities.get(activities.size()-1);
    }

    public static void removeActivity(BaseActivity activity) {

        activities.remove(activity);

    }

    public static void stopAllActivity() {

        for (BaseActivity activity : activities) {
            activity.finish();
        }
    }

    public static MainActivity getMainActivity(){
        ArrayList<BaseActivity> activitiesCopy = new ArrayList<BaseActivity>(activities);
        for(BaseActivity activity : activitiesCopy){
                Logger.i("activity : " + activity.getLocalClassName());
            if(activity.getLocalClassName().equalsIgnoreCase("act.MainActivity")){
                Logger.i("MainAcitivty is Contained");
                return (MainActivity) activity;
            }
        }
        return null;
    }

    public static IntroActivity getIntroActivity(){
        ArrayList<BaseActivity> activitiesCopy = new ArrayList<BaseActivity>(activities);
        for(BaseActivity activity : activitiesCopy){
            Logger.i("activity : " + activity.getLocalClassName());
            if(activity.getLocalClassName().equalsIgnoreCase("act.IntroActivity")){
                return (IntroActivity) activity;
            }
        }
        return null;
    }

    public static void stopWaitingActivity(String peerAlias1){

        boolean isFirst = true;

        ArrayList<BaseActivity> activitiesCopy = new ArrayList<BaseActivity>(activities);
        for(BaseActivity activity : activitiesCopy){
            if(activity.getLocalClassName().equalsIgnoreCase("act.WaitingActivity")){
                removeActivity(activity);
                Logger.i("WaitingActivity is Contained");
                if(isFirst) {
                    Utils.showToast(activity, peerAlias1 + "님이 영상채팅 요청을 취소하였습니다.");
                    isFirst = false;
                }
                activity.finish();
            }
            if(activity.getLocalClassName().equalsIgnoreCase("act.VideoChatActivity")){
                removeActivity(activity);
                Logger.i("VideoChatActivity is Contained");
                if(isFirst) {
                    Utils.showToast(activity, peerAlias1 + "님이 영상채팅 요청을 취소하였습니다.");
                    isFirst = false;
                }
                activity.finish();
            }
        }
    }

    public static void stopVideoChatActivity(String peerAlias1){

        boolean isFirst = true;

        ArrayList<BaseActivity> activitiesCopy = new ArrayList<BaseActivity>(activities);
        for(BaseActivity activity : activitiesCopy){
            Logger.i("activity name : " + activity.getLocalClassName());

            if(activity.getLocalClassName().equalsIgnoreCase("act.VideoChatActivity")){
                removeActivity(activity);
                Logger.i("VideoChatActivity is Contained");
                if(isFirst) {
                    Utils.showToast(activity, peerAlias1 + "님이 영상채팅 요청을 거절하였습니다.");
                    isFirst = false;
                }
                activity.finish();
            }
        }
    }

    public static void setIsCall(boolean value){
        isCall = value;

        Logger.i("setIsCall : " + isCall);
    }

    public static boolean getIsCall(){
        return isCall;
    }

    public static void setIsVideo(boolean value){
        isVideo = value;

        Logger.i("Main : setIsVideo : " + isVideo);
    }

    public static boolean getIsVideo(){
        return isVideo;
    }

    public static void setIsWaiting(boolean value){
        isWaiting = value;

        Logger.i("setIsWaiting : " + isWaiting);
    }

    public static boolean getIsWaiting(){
        return isWaiting;
    }
}
