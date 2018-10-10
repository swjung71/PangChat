package kr.co.digitalanchor.pangchat.act;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import kr.co.digitalanchor.pangchat.BaseActivity;
import kr.co.digitalanchor.pangchat.R;
import kr.co.digitalanchor.pangchat.handler.DBHelper;
import kr.co.digitalanchor.pangchat.handler.HttpHelper;
import kr.co.digitalanchor.pangchat.model.SearchIDResult;
import kr.co.digitalanchor.pangchat.model.SearchPW;
import kr.co.digitalanchor.pangchat.model.SearchPWResult;
import kr.co.digitalanchor.pangchat.model.User;

public class IDPWActivity extends BaseActivity implements View.OnClickListener {

    private Button btnSearchID;
    private Button btnSearchPW;
    private Button btnBack;
    private EditText editPhoneForID;
    private EditText editPhoneForPW;
    private EditText editID;
    private ImageButton btnClose;

    private DBHelper mDBHelper;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idpw);
        mDBHelper = DBHelper.getInstance(this);
        mUser = mDBHelper.getUserInfo();

        btnSearchID = (Button) findViewById(R.id.btn_searchID);
        btnSearchID.setOnClickListener(this);

        btnSearchPW = (Button) findViewById(R.id.btn_searchPW);
        btnSearchPW.setOnClickListener(this);

        btnClose = (ImageButton) findViewById(R.id.btnMenu);
        btnClose.setOnClickListener(this);

        btnBack = (Button) findViewById(R.id.btn_back);
        btnBack.setOnClickListener(this);

        editPhoneForID = (EditText) findViewById(R.id.editPhoneForID);
        editPhoneForPW = (EditText) findViewById(R.id.editPhoneForPW);
        editID = (EditText) findViewById(R.id.editID);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;

            case R.id.btn_searchID:
                if (isValidateInfo()) {
                    searchID();
                }
                break;

            case R.id.btn_searchPW:

                Logger.i("searchIDPW");
                if (isValidateInfoForPW()) {
                    searchPW();
                }
                break;

            default:
                break;
        }
    }

    private boolean isValidateInfo() {

        String temp = null;

        String msg = null;

        do {


            temp = editPhoneForID.getText().toString();

            if (TextUtils.isEmpty(temp)) {

                msg = getResources().getString(R.string.error_no_phone);

                break;
            }


        } while (false);

        if (TextUtils.isEmpty(msg)) {

            return true;

        } else {

            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

            return false;
        }
    }

    private boolean isValidateInfoForPW() {

        String temp = null;

        String msg = null;

        do {


            temp = editID.getText().toString();

            if (TextUtils.isEmpty(temp)) {

                msg = getResources().getString(R.string.error_no_ID);

                break;
            }

            temp = editPhoneForPW.getText().toString();

            if (TextUtils.isEmpty(temp)) {

                msg = getResources().getString(R.string.error_no_phone);

                break;
            }


        } while (false);

        if (TextUtils.isEmpty(msg)) {

            return true;

        } else {

            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

            return false;
        }
    }

    private void searchID() {
        if (mUser == null || mUser.getUserName() == null || TextUtils.isEmpty(mUser.getUserName())) {
            Logger.i("remote searchID");
            remoteSearchID();
        } else {

            MaterialDialog.Builder builder = new MaterialDialog.Builder(this);
            builder.title(getResources().getString(R.string.identification)).content(
                    getResources().getString(R.string.search_ID_result) + mUser.getUserName() + "입니다.")
                    .positiveText(getResources().getString(R.string.confirm))
                    .callback(new MaterialDialog.SimpleCallback() {
                        @Override
                        public void onPositive(MaterialDialog materialDialog) {
                            materialDialog.dismiss();
                        }
                    }).build().show();
        }
    }

    private void searchPW() {
        if (mUser == null || mUser.getPasswd() == null || TextUtils.isEmpty(mUser.getPasswd())) {
            Logger.i("remote searchPW");
            remoteSearchPW();
        } else {
            Logger.i("search PW locally");
            Logger.i("userName : " + mUser.getUserName() + ":");
            Logger.i("ID : " + editID.getText().toString() + ":");
            if (mUser.getUserName().equalsIgnoreCase(editID.getText().toString())) {
                MaterialDialog.Builder builder = new MaterialDialog.Builder(this);
                builder.title(getResources().getString(R.string.password)).content(
                        getResources().getString(R.string.search_PW_result) + mUser.getPasswd() + "입니다.")
                        .positiveText(getResources().getString(R.string.confirm))
                        .callback(new MaterialDialog.SimpleCallback() {
                            @Override
                            public void onPositive(MaterialDialog materialDialog) {
                                materialDialog.dismiss();
                            }
                        }).build().show();
            } else {
                MaterialDialog.Builder builder = new MaterialDialog.Builder(this);
                builder.title(getResources().getString(R.string.password)).content(getResources().getString(R.string.error_search_PW_no_ID))
                        .positiveText(getResources().getString(R.string.confirm))
                        .callback(new MaterialDialog.SimpleCallback() {
                            @Override
                            public void onPositive(MaterialDialog materialDialog) {
                                materialDialog.dismiss();
                            }
                        }).build().show();
            }
        }
    }

    public void  remoteSearchID(){

        SearchPW pw = new SearchPW();
        pw.setUserName("");
        pw.setPhone_number(editPhoneForID.getText().toString());

        final Gson gson = new Gson();

        Logger.i(gson.toJson(pw));

        JsonObjectRequest request = HttpHelper.searchID(pw, "searchID",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        SearchIDResult result = gson.fromJson(response.toString(), SearchIDResult.class);
                        switch (result.getResultCode()) {

                            case HttpHelper.SUCCESS:
                                Logger.i("ID search : ID : " + result.getUserName());

                                MaterialDialog.Builder builder = new MaterialDialog.Builder(IDPWActivity.this);
                                builder.title(getResources().getString(R.string.identification)).content(
                                        getResources().getString(R.string.search_ID_result) + result.getUserName() + getResources().getString(R.string.search_ID_result_end))
                                        .positiveText(getResources().getString(R.string.confirm))
                                        .callback(new MaterialDialog.SimpleCallback() {
                                            @Override
                                            public void onPositive(MaterialDialog materialDialog) {
                                                materialDialog.dismiss();
                                            }
                                        }).build().show();

                                break;

                            case HttpHelper.NO_USER:
                                Logger.i("search ID fail");

                                MaterialDialog.Builder builder1 = new MaterialDialog.Builder(IDPWActivity.this);
                                builder1.title(getResources().getString(R.string.identification)).content(
                                        getResources().getString(R.string.error_search_ID_not_match))
                                        .positiveText(getResources().getString(R.string.confirm))
                                        .callback(new MaterialDialog.SimpleCallback() {
                                            @Override
                                            public void onPositive(MaterialDialog materialDialog) {
                                                materialDialog.dismiss();
                                            }
                                        }).build().show();
                                break;

                            default:
                                Logger.i("Fail to search ID: " + result.getResultMessage());
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

    public void remoteSearchPW(){
        SearchPW user = new SearchPW();
        user.setUserName(editID.getText().toString());
        user.setPhone_number(editPhoneForPW.getText().toString());

        final Gson gson = new Gson();

        Logger.i(gson.toJson(user));

        JsonObjectRequest request = HttpHelper.searchPW(user, "searchPW",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        SearchPWResult result = gson.fromJson(response.toString(), SearchPWResult.class);
                        switch (result.getResultCode()) {

                            case HttpHelper.SUCCESS:
                                Logger.i("search passwd : " + result.getPasswd());

                                MaterialDialog.Builder builder = new MaterialDialog.Builder(IDPWActivity.this);
                                builder.title(getResources().getString(R.string.password)).content(
                                        getResources().getString(R.string.search_PW_result) + result.getPasswd() + getResources().getString(R.string.search_PW_result_end))
                                        .positiveText(getResources().getString(R.string.confirm))
                                        .callback(new MaterialDialog.SimpleCallback() {
                                            @Override
                                            public void onPositive(MaterialDialog materialDialog) {
                                                materialDialog.dismiss();
                                            }
                                        }).build().show();
                                break;

                            case HttpHelper.NO_USER:

                                MaterialDialog.Builder builder1 = new MaterialDialog.Builder(IDPWActivity.this);
                                builder1.title(getResources().getString(R.string.password)).content(
                                        getResources().getString(R.string.error_search_PW_not_match))
                                        .positiveText(getResources().getString(R.string.confirm))
                                        .callback(new MaterialDialog.SimpleCallback() {
                                            @Override
                                            public void onPositive(MaterialDialog materialDialog) {
                                                materialDialog.dismiss();
                                            }
                                        }).build().show();
                                break;

                            default:
                                Logger.i("Fail to search PW : " + result.getResultMessage());
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


}
