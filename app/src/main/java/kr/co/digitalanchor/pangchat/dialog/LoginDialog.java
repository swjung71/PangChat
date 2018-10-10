package kr.co.digitalanchor.pangchat.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import kr.co.digitalanchor.pangchat.PCApplication;
import kr.co.digitalanchor.pangchat.R;
import kr.co.digitalanchor.pangchat.act.IDPWActivity;
import kr.co.digitalanchor.pangchat.act.IntroActivity;
import kr.co.digitalanchor.pangchat.act.MainActivity;
import kr.co.digitalanchor.pangchat.handler.DBHelper;
import kr.co.digitalanchor.pangchat.handler.HttpHelper;
import kr.co.digitalanchor.pangchat.handler.VolleySingleton;
import kr.co.digitalanchor.pangchat.model.Login;
import kr.co.digitalanchor.pangchat.model.User;
import kr.co.digitalanchor.pangchat.model.UserCheckResult;

/**
 * Created by Peter Jung on 2016-11-22.
 */

public class LoginDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private Button btnLogin;
    private Button btnSearchIDPW;
    private ImageButton btnClose;
    private EditText editID;
    private EditText editPW;

    private DBHelper mDBHelper;
    private User mUser;

    private RequestQueue mQueue;

    public LoginDialog(Context context) {
        super(context);
        mContext = context;
        mDBHelper = DBHelper.getInstance(mContext);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        mUser = mDBHelper.getUserInfo();
        mQueue = VolleySingleton.getmInstance(PCApplication.applicationContext).getmRequestQueue();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_login);

        btnLogin = (Button)findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);

        btnSearchIDPW = (Button)findViewById(R.id.btn_searchIDPW);
        btnSearchIDPW.setOnClickListener(this);

        btnClose = (ImageButton)findViewById(R.id.btn_close);
        btnClose.setOnClickListener(this);

        editID = (EditText)findViewById(R.id.editID);
        editPW = (EditText)findViewById(R.id.edit_passwd);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_login:
                if(isValidateInfo()){
                    checkLogin();
                }
                break;
            case R.id.btn_searchIDPW:

                Intent intent = new Intent(mContext, IDPWActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mContext.startActivity(intent);
                break;

            case R.id.btn_close:
                LoginRegDialog dialog = new LoginRegDialog(mContext);
                dialog.show();
                dismiss();
                break;

            default:
                break;
        }
    }

    private boolean isValidateInfo() {

        String temp = null;

        String msg = null;

        do {

            temp = editID.getText().toString();

            if (TextUtils.isEmpty(temp)) {

                msg = mContext.getResources().getString(R.string.error_no_ID);

                break;
            }

            temp = editPW.getText().toString();

            if (TextUtils.isEmpty(temp)) {

                msg = mContext.getResources().getString(R.string.error_no_passwd);

                break;
            }

        } while (false);

        if (TextUtils.isEmpty(msg)) {

            return true;

        } else {

            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();

            return false;
        }
    }

    private void checkLogin(){
        if (mUser== null || mUser.getUserName() == null || TextUtils.isEmpty(mUser.getUserName())) {
            Logger.i("remote");
            checkLoginRemote();
        }else{

            Logger.i("Login ID : " + mUser.getUserName());
            Logger.i("Login pw : " + mUser.getPasswd());
            Logger.i("editID : " + editID.getText().toString());
            Logger.i("editPW : " + editPW.getText().toString());
            if(mUser.getUserName().equalsIgnoreCase(editID.getText().toString()) && mUser.getPasswd().equalsIgnoreCase(editPW.getText().toString())){
                mDBHelper.login();
                Intent intent = new Intent(mContext, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mContext.startActivity(intent);
                dismiss();
            }else{
                Toast.makeText(mContext, mContext.getResources().getString(R.string.error_not_match_IDPW), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void checkLoginRemote(){


        //deviceModel과 OSVersion, isAndroid 는 user에 정의되어 있음
        Login user = new Login();
        user.setUserName(editID.getText().toString());
        user.setPasswd(editPW.getText().toString());
        //1.1 이런식의 version name
        user.setAppVersion(PCApplication.getAppVersionName());
        user.setDeviceNumber(PCApplication.getDeviceNumber());
        user.setGcmID(PCApplication.getRegistrationId());
        user.setPhoneNumber(PCApplication.getPhoneNumber());
        user.setNationalCode(PCApplication.getNationalCode());

        final Gson gson = new Gson();

        Logger.i(gson.toJson(user));

        JsonObjectRequest request = HttpHelper.login(user, "login",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        UserCheckResult result = gson.fromJson(response.toString(), UserCheckResult.class);
                        switch (result.getResultCode()) {

                            case HttpHelper.SUCCESS:
                                result.getUser();
                                Logger.i("User exist and Insert");
                                Logger.i("passwd check : pk : " + result.getUser().getUserPK());
                                mDBHelper.insertUserInfo(result.getUser());

                                Toast.makeText(mContext.getApplicationContext(), mContext.getResources().getString(R.string.login_success), Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(mContext, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                mContext.startActivity(intent);

                                dismiss();
                                break;

                            case HttpHelper.NO_USER:
                                Logger.i("Login fail");
                                Toast.makeText(mContext.getApplicationContext(), mContext.getResources().getString(R.string.login_fail), Toast.LENGTH_LONG).show();
                                break;

                            default:
                                Logger.i("Fail to register : " + result.getResultMessage());
                                //Toast.makeText(getApplicationContext(), result.getResultMessage(), Toast.LENGTH_LONG).show();
                                break;
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Logger.e(error.toString());
                    }
                });

        if (request != null) {
            addRequest(request);
        }
    }

    protected void addRequest(JsonObjectRequest request) {
        try {
            mQueue.add(request);
        } catch (Exception e) {
            e.printStackTrace();
            Logger.e(e.getMessage());
        }
    }
}
