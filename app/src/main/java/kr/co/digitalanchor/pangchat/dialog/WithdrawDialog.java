package kr.co.digitalanchor.pangchat.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
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
import kr.co.digitalanchor.pangchat.handler.DBHelper;
import kr.co.digitalanchor.pangchat.handler.HttpHelper;
import kr.co.digitalanchor.pangchat.handler.VolleySingleton;
import kr.co.digitalanchor.pangchat.model.Result;
import kr.co.digitalanchor.pangchat.model.User;
import kr.co.digitalanchor.pangchat.model.Withdraw;

/**
 * Created by Peter Jung on 2016-11-22.
 */

public class WithdrawDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private Button btnOK;
    private Button btnCancel;
    private ImageButton btnClose;

    private DBHelper mDBHelper;
    private User mUser;

    private RequestQueue mQueue;

    public WithdrawDialog(Context context) {
        super(context);
        mContext = context;
        mDBHelper = DBHelper.getInstance(mContext);
        mUser = mDBHelper.getUserInfo();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mQueue = VolleySingleton.getmInstance(PCApplication.applicationContext).getmRequestQueue();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_withdraw);
        btnOK = (Button)findViewById(R.id.btn_ok);
        btnOK.setOnClickListener(this);

        btnCancel = (Button)findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(this);
        btnClose = (ImageButton)findViewById(R.id.btn_close);
        btnClose.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.btn_ok:
                requestWithdraw();
                break;

            case R.id.btn_close:
            case R.id.btn_cancel:
                dismiss();
                break;

            default:
                break;
        }
    }

    private void requestWithdraw(){
        Withdraw user = new Withdraw();
        user.setUserPK(mUser.getUserPK());
        final Gson gson = new Gson();

        Logger.i(gson.toJson(user));

        JsonObjectRequest request = HttpHelper.withdraw(user, "withdraw",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Result result = gson.fromJson(response.toString(), Result.class);
                        switch (result.getResultCode()) {

                            case HttpHelper.SUCCESS:
                                mDBHelper.clearAll();
                                Toast.makeText(mContext.getApplicationContext(), mContext.getResources().getString(R.string.withdraw_success), Toast.LENGTH_LONG).show();
                                PCApplication.stopAllActivity();
                                dismiss();
                                break;

                            case HttpHelper.NO_USER:
                                Logger.i("Login fail");
                                Toast.makeText(mContext.getApplicationContext(), mContext.getResources().getString(R.string.withdraw_fail), Toast.LENGTH_LONG).show();
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
