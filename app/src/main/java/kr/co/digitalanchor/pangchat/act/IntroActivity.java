package kr.co.digitalanchor.pangchat.act;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import kr.co.digitalanchor.pangchat.BaseActivity;
import kr.co.digitalanchor.pangchat.PCApplication;
import kr.co.digitalanchor.pangchat.R;
import kr.co.digitalanchor.pangchat.dialog.LoginRegDialog;
import kr.co.digitalanchor.pangchat.handler.DBHelper;
import kr.co.digitalanchor.pangchat.handler.HttpHelper;
import kr.co.digitalanchor.pangchat.model.CheckUser;
import kr.co.digitalanchor.pangchat.model.GCMUpdate;
import kr.co.digitalanchor.pangchat.model.GetVersion;
import kr.co.digitalanchor.pangchat.model.Result;
import kr.co.digitalanchor.pangchat.model.ResultGCMUpdate;
import kr.co.digitalanchor.pangchat.model.User;
import kr.co.digitalanchor.pangchat.model.UserCheckResult;
import kr.co.digitalanchor.pangchat.model.VersionCheckResult;
import kr.co.digitalanchor.pangchat.utils.StaticValues;
import kr.co.digitalanchor.pangchat.utils.Utils;

/**
 * registerGCM--> getAvailableUpdate --> gcmUpdate -->
 * 1. checkExistUser 만약 있다면--> gcmUpdate --> gcm을 서버로 전송하고 showNext
 * 만약 없다면 --> 로그인/회원가입 Dialog
 * 2. gcm을 서버로 전송하고 showNext
 */
public class IntroActivity extends BaseActivity {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private final int MY_PERMISSION_REQUEST_STORAGE = 100;

    private final static int REQUEST_CHECK_USER = 1000;
    /**
     * GCM ID를 가져오는 것이 성공했을 때 생성하는 message
     */
    private final static int GET_GCM_ID_COMPLETE = 1001;
    /**
     * app version확인을 마쳤을 때 생성되는 message
     */
    private final static int APP_VERSION_CHECK_COMPLETE = 1002;

    /**
     * 서버로부터 user 정보를 가져온 경우
     */
    private final static int GET_USER_INFO_COMPLETE = 1003;
    private DBHelper mDBHelper;
    private User mUser = null;

    /**
     * 더 이상 진행을 하지 않고 (stop)하고 update화면으로 감
     */
    private boolean isStopped = false;

    private boolean isFirst = true;

    /**
     * Application permission 목록, android build target 23
     */
    public static final String[] MANDATORY_PERMISSIONS = {
            "android.permission.READ_PHONE_STATE",
            "android.permission.INTERNET",
            "android.permission.CAMERA",
            "android.permission.RECORD_AUDIO",
            "android.permission.MODIFY_AUDIO_SETTINGS",
            "android.permission.ACCESS_NETWORK_STATE",
            "android.permission.CHANGE_WIFI_STATE",
            "android.permission.ACCESS_WIFI_STATE",
            "android.permission.GET_ACCOUNTS",
            "android.permission.BLUETOOTH",
            "android.permission.BLUETOOTH_ADMIN",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.ACCESS_FINE_LOCATION",
            "android.permission.ACCESS_COARSE_LOCATION",
            "com.android.vending.BILLING",
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.VIBRATE",
            "android.permission.WAKE_LOCK"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        // Application permission 23
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            checkPermission(MANDATORY_PERMISSIONS);
        } else {
            mDBHelper = DBHelper.getInstance(getApplicationContext());
            mUser = mDBHelper.getUserInfo();
            registerGCM();
            makeShortCut();
        }

        /*mDBHelper = DBHelper.getInstance(getApplicationContext());
        mUser = mDBHelper.getUserInfo();*/
       /* registerGCM();
        makeShortCut();*/
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @TargetApi(23)
    private void checkPermission(String[] permissions) {

        requestPermissions(permissions, MY_PERMISSION_REQUEST_STORAGE);
    }

    /*
     * google play를 check하고 등록하는 것
     */
    private void registerGCM() {

        if (checkPlayServices()) {
            registerInBackground();
        }
    }

    @Override
    protected void onHandleMessage(Message msg) {

        switch (msg.what) {

            case GET_GCM_ID_COMPLETE:
                getAvailableUpdate();
                break;
            case APP_VERSION_CHECK_COMPLETE:
                gcmUpdate();
                break;
            case GET_USER_INFO_COMPLETE:
                gcmUpdate();
            default:

                break;
        }
    }

    private boolean checkPlayServices() {

        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        if (resultCode != ConnectionResult.SUCCESS) {

            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {

                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();

            } else {
                Logger.d("This device does not support play service.");
                finish();
            }

            return false;
        }

        return true;
    }

    /**
     * GCM 등록 번호 얻어 오기
     */
    private void registerInBackground() {

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {

                try {
                    /// Validation
                    GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(getApplicationContext());

                    /// Get Registration ID
                    String id = gcm.register(StaticValues.GCM_SENDER_ID);
                    storeRegistrationId(id);

                    Logger.d("registration id " + id);
                    Logger.d("ApplicationContext  " + getApplicationContext().getPackageName());

                    return "Succeed";

                } catch (IOException ex) {

                    Utils.showToast(IntroActivity.this, "WiFi 네트워크가 꺼져있습니다. WiFi 상태를 확인하시고 앱을 다시 시작해주세요");

                    Logger.e(ex.toString());

                    PCApplication.applicationHandler.post(new Runnable() {
                        @Override
                        public void run() {

                            MaterialDialog.Builder builder = new MaterialDialog.Builder(IntroActivity.this);

                            builder.title("알림").content("네트워크가 꺼져있습니다. 네트워크 상태를 확인하시고 앱을 다시 시작해주세요.")
                                    .positiveText("확인")
                                    .callback(new MaterialDialog.SimpleCallback() {
                                        @Override
                                        public void onPositive(MaterialDialog materialDialog) {

                                            PCApplication.stopAllActivity();

                                            materialDialog.dismiss();
                                        }
                                    }).build().show();
                        }
                    });

                    return "Failed";
                }
            }

            @Override
            protected void onPostExecute(String result) {

                super.onPostExecute(result);

                if (result.compareTo("Succeed") == 0) {
                    sendEmptyMessage(GET_GCM_ID_COMPLETE);
                }
            }
        }.execute(null, null, null);
    }

    /**
     * 서버에 GCM을 등록하는 것
     */
    private void gcmUpdate() {
        //등록되지 않은 사용자
        if (mUser == null || mUser.getUserAlias() == null || TextUtils.isEmpty(mUser.getUserAlias())) {
            checkExistingUser();
            Logger.i("GCM userAlias is empty");
            return;
        }

        if (mUser.getLogin() == 0) {
            LoginRegDialog dialog = new LoginRegDialog(IntroActivity.this);
            dialog.show();
            Logger.i("he logout");
            return;
        }

        GCMUpdate model = new GCMUpdate();

        model.setGcm(PCApplication.getRegistrationId());
        model.setAppVersion(PCApplication.getAppVersionName());
        model.setUserPK("" + mUser.getUserPK());
        final Gson gson = new Gson();

        Logger.i("gcmUpdate gson : " + gson.toJson(model));

        JsonObjectRequest request = HttpHelper.gcmUpdate(model, "gcmUpdate",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        ResultGCMUpdate result = gson.fromJson(response.toString(), ResultGCMUpdate.class);
                        switch (result.getResultCode()) {

                            case HttpHelper.SUCCESS:
                                Logger.i("Successfully update GCM");
                                mDBHelper.updateEndDay(result.getEndDay());
                                Logger.i("update end day " + result.getEndDay());
                                showNextScreen(300);
                                //Toast.makeText(getApplicationContext(), "Successfully  date", Toast.LENGTH_LONG).show();
                                break;

                            default:
                                Logger.i("Fail to update GCM : " + result.getResultMessage());
                                showNextScreen(300);
                                //Toast.makeText(getApplicationContext(), result.getResultMessage(), Toast.LENGTH_LONG).show();
                                break;
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        handleError(error);
                    }
                });

        if (request != null) {
            addRequest(request);
        }
    }

    /**
     * update가 있는 지 확인하는 함수
     */
    private void getAvailableUpdate() {

        GetVersion model = new GetVersion();
        final Gson gson = new Gson();

        JsonObjectRequest request = HttpHelper.getVersion(model, "getVersion",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        VersionCheckResult result = gson.fromJson(response.toString(), VersionCheckResult.class);
                        switch (result.getResultCode()) {

                            case HttpHelper.SUCCESS:

                                if (PCApplication.isUpdate(result.getVersion())) {
                                    isStopped = true;

                                    Logger.i("Update exist");
                                    MaterialDialog.Builder builder = new MaterialDialog.Builder(IntroActivity.this);

                                    builder.title("업데이트")
                                            .content("새로운 버전이 출시되었습니다.\n업데이트 해주세요.")
                                            .positiveText("확인").cancelable(false).callback(new MaterialDialog.SimpleCallback() {
                                        @Override
                                        public void onPositive(MaterialDialog materialDialog) {
                                            materialDialog.dismiss();
                                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                                            PCApplication.stopAllActivity();
                                        }
                                    }).build().show();
                                } else {
                                    Logger.i("No update available");
                                    sendEmptyMessage(APP_VERSION_CHECK_COMPLETE);
                                }
                                break;

                            default:
                                handleResultCode(result.getResultCode(), result.getResultMessage());
                                break;
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        handleError(error);
                    }
                });

        if (request != null) {
            addRequest(request);
        }
    }

    private void storeRegistrationId(String id) {

        int version = PCApplication.getAppVersionCode();

        PCApplication.putRegistrationId(id);
        PCApplication.putAppdVersion(version);
    }

    /*
     * existing true;
    */
    private void checkExistingUser() {

        //User user = mDBHelper.getUserInfo();

        //등록되지 않은 사용자이면 서버에 사용자가 있는 지 확인
        if (mUser == null || mUser.getUserAlias() == null || TextUtils.isEmpty(mUser.getUserAlias())) {

            final Gson gson = new Gson();
            CheckUser model = new CheckUser();
            model.setDeviceNumber(PCApplication.getDeviceNumber());

            Logger.i("CheckUser is : " + gson.toJson(model));

            JsonObjectRequest request = HttpHelper.checkUser(model, "checkUser",
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            UserCheckResult result = gson.fromJson(response.toString(), UserCheckResult.class);

                            Logger.i(" result message " + result.getResultMessage() + " result code : " + result.getResultCode());

                            switch (result.getResultCode()) {

                                case HttpHelper.SUCCESS:

                                    Logger.i("Successfully checkUser");
                                    Logger.i("Success : " + result.getResultCode());

                                    if (result.getUser() != null) {

                                        Logger.i("checkUser has user");

                                        //password를 가져옴 (MD5 hash 상태로)
                                        mDBHelper.insertUserInfo(result.getUser());

                                        //null이였던 mUser 모델을 만듦
                                        mUser = mDBHelper.getUserInfo();
                                        getImage(result);
                                        sendEmptyMessage(GET_USER_INFO_COMPLETE);
                                    }

                                    break;

                                case HttpHelper.NO_USER:

                                    Logger.i("checkUser has no user");
                                    Logger.i("Success : " + result.getResultCode());

                                    LoginRegDialog dialog = new LoginRegDialog(IntroActivity.this);
                                    //back button 안 먹게 하는 것
                                    //dialog.setCancelable(false);
                                    //dialog 밖을 터치했을 때 안 사라지게 하는 것
                                    //dialog.setCanceledOnTouchOutside(false);
                                    dialog.show();
                                    break;

                                default:
                                    Logger.i("Fail checkUser : " + result.getResultMessage());
                                    Toast.makeText(getApplicationContext(), result.getResultMessage(), Toast.LENGTH_LONG).show();
                                    break;
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            handleError(error);
                        }
                    });

            if (request != null) {
                addRequest(request);
            }
        } else {
            showNextScreen(300);
        }
    }

    /**
     * @param delayed 밀리세컨드 후에 다음 activity를 뛰움
     */
    public void showNextScreen(final int delayed) {

        // update가 있어서 중지한 경우
        if (isStopped) {
            return;
        }


        Logger.i("getAppVersionName:" + PCApplication.getAppVersionName());
        Logger.i("getDeviceNumber():" + PCApplication.getDeviceNumber());
        Logger.i("getPhoneNumber:" + PCApplication.getPhoneNumber());
        Logger.i("getAppVersionCode():" + PCApplication.getAppVersionCode());
        Logger.i("getRegisteredVersion():" + PCApplication.getRegisteredVersion());
        Logger.i("device model: " + new User().getDevModel());
        Logger.i("android version : " + new User().getOsVersion());
        Logger.i("nationCode : " + PCApplication.getNationalCode());

        //등록이 되어 있지 않다면
        if (mUser == null || mUser.getUserAlias() == null || TextUtils.isEmpty(mUser.getUserAlias())) {

            /*Logger.i("show SignUpActivity");
            final Intent intent = new Intent();
            intent.setClass(getApplicationContext(), SignupActivity.class);*/
        } else {

            /*try{
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

                String str1 = mUser.getEndDay();
                Date date1 = formatter.parse(str1);

                Logger.i("end day : " + date1);

                String str2 = formatter.format(new Date());

                Date date2 = formatter.parse(str2);
                Logger.i("end day current: " + date2);

                if (date1.compareTo(date2)<0)
                {
                    System.out.println("date2 is Greater than my date1");

                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.expired), Toast.LENGTH_SHORT).show();
                    final Intent intent = new Intent();
                    intent.setClass(getApplicationContext(), PurchaseActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    return;
                }

            }catch (ParseException e1){
                e1.printStackTrace();
            }*/

            final Intent intent = new Intent();
            intent.setClass(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            getHandler().postDelayed(new Runnable() {

                @Override
                public void run() {

                    startActivity(intent);
                    finish();

                }
            }, delayed);
        }
    }

    /**
     * 사용자 이미지를 저장하는 함수
     *
     * @param user : 서버로 부터 받은 사용자 정보
     */
    private void getImage(UserCheckResult user) {
        final String path = user.getUser().getImagePath();
        String url = HttpHelper.getImageURL(path);
        ImageRequest request = new ImageRequest(url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {

                        Logger.i("response get Image");

                        String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/temp/" + path;
                        FileOutputStream out = null;

                        try {
                            out = new FileOutputStream(filePath);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
                        } catch (Exception e) {
                            Logger.e(e.getMessage());
                        } finally {
                            try {
                                if (out != null) {
                                    out.close();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        handleError(error);
                    }
                });
        addRequest(request);
    }

    /**
     * shortcut을 만드는 함수
     */
    private void makeShortCut() {

        if (PCApplication.getString("check", "").isEmpty()) {

            Intent shortcutIntent = new Intent(Intent.ACTION_MAIN);
            shortcutIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            shortcutIntent.setClassName(this, getClass().getName());
            shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);

            Intent intent = new Intent();
            intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
            intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, getResources().getString(R.string.app_name));
            intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                    Intent.ShortcutIconResource.fromContext(this, R.drawable.ic_launcher));
            intent.putExtra("duplicate", false);
            intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");

            sendBroadcast(intent);

            //check가 shortcut이 있는지 나타냄
            PCApplication.putString("check", "exist");
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSION_REQUEST_STORAGE: {
                mDBHelper = DBHelper.getInstance(getApplicationContext());
                mUser = mDBHelper.getUserInfo();
                registerGCM();
                makeShortCut();
            }

        }
    }
}
