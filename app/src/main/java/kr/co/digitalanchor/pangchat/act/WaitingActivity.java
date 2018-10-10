package kr.co.digitalanchor.pangchat.act;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import kr.co.digitalanchor.pangchat.BaseActivity;
import kr.co.digitalanchor.pangchat.PCApplication;
import kr.co.digitalanchor.pangchat.R;
import kr.co.digitalanchor.pangchat.handler.DBHelper;
import kr.co.digitalanchor.pangchat.handler.HttpHelper;
import kr.co.digitalanchor.pangchat.handler.VolleySingleton;
import kr.co.digitalanchor.pangchat.model.PangChatByUser;
import kr.co.digitalanchor.pangchat.model.Result;
import kr.co.digitalanchor.pangchat.model.User;
import kr.co.digitalanchor.pangchat.view.RoundedNetworkImageView;

/**
 * Created by user on 2016-12-22.
 */

public class WaitingActivity extends BaseActivity implements View.OnClickListener {

    public Button btnBack;
    private Button btnCancel;
    private Button btnAccept;
    private Button btnReject;
    private TextView nameTextView;
    private String partnerID;
    private String partnerAlias;
    private String partnerCity;
    private String partnerImage;
    private JSONObject channel;
    private String mChannelID;
    private int isRandom;
    private DBHelper mDBHelper;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);
        mDBHelper = DBHelper.getInstance(this);
        mUser = mDBHelper.getUserInfo();
        Intent intent = getIntent();
        //partner = (PangChatUser)intent.getSerializableExtra("partner");
        String param = intent.getStringExtra("param");

        Gson gson = new Gson();

        try {
            channel = new JSONObject(param);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Logger.i("WaitingActivity channel.toString() : " + channel.toString());

        partnerID = intent.getStringExtra("partnerID");
        partnerAlias = intent.getStringExtra("partnerAlias");
        partnerCity = intent.getStringExtra("partnerCity");
        partnerImage = intent.getStringExtra("partnerImage");
        mChannelID = intent.getStringExtra("channelID");
        Logger.i("WaitingActivity mChannelID : " + mChannelID);

        isRandom = intent.getIntExtra("isRandom", 0);

        initView();

        PCApplication.setIsWaiting(true);
    }

    @Override
    public void onClick(View v) {

        Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        switch (v.getId()){
            case R.id.btn_back:
                vibe.cancel();
                reject();
                break;
            case R.id.btn_cancel:
                vibe.cancel();
                reject();
                break;

            case R.id.btn_accept:
                vibe.cancel();
                accept();
                break;
            case R.id.btn_reject:
                vibe.cancel();
                reject();
                break;

            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibe.cancel();
        reject();
        super.onBackPressed();
    }

    public void initView(){
        btnBack = (Button) findViewById(R.id.btn_back);
        btnBack.setOnClickListener(this);
        btnCancel = (Button)findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(this);
        btnAccept = (Button)findViewById(R.id.btn_accept);
        btnAccept.setOnClickListener(this);
        btnReject = (Button)findViewById(R.id.btn_reject);
        btnReject.setOnClickListener(this);
        nameTextView = (TextView)findViewById(R.id.name_text);

        nameTextView.setText(partnerCity  + " : " + partnerAlias + getResources().getString(R.string.request_chat));

        RoundedNetworkImageView networkImageView = (RoundedNetworkImageView) findViewById(R.id.rounded_image);
        ImageLoader imageLoader = VolleySingleton.getmInstance(this).getmImageLoader();
        networkImageView.setImageUrl(HttpHelper.getImageURL(partnerImage), imageLoader);
    }

    private void accept(){

        Intent intent = new Intent();
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setClass(this, VideoChatActivity.class);
        // PlayRTC Sample 유형 전달
        intent.putExtra("isRequester", 0);//value 1 requestor, 0 receiver
        intent.putExtra("param", channel.toString());

        intent.putExtra("partnerID", partnerID);
        intent.putExtra("channelID", mChannelID);
        intent.putExtra("isRandom", 0);//0 이면 수도 1이면 random;

        intent.putExtra("partnerAlias", partnerAlias);
        intent.putExtra("partnerImage", partnerImage);
        intent.putExtra("partnerCity", partnerCity);

        Logger.i("WaitingActivity accept channel : " + channel.toString());
        startActivity(intent);
        finish();
    }

    private void reject() {
        //등록되지 않은 사용자
        if (mUser == null || mUser.getUserAlias() == null || TextUtils.isEmpty(mUser.getUserAlias())) {
            Logger.i("GCM userAlias is empty");
            return;
        }

        PangChatByUser model = new PangChatByUser();

        model.setSex(mUser.getSex());
        model.setAge(mUser.getAge());
        model.setUserID("" + mUser.getUserPK());
        model.setUserAlias(mUser.getUserAlias());
        model.setImageURL(mUser.getImagePath());
        model.setCity(mUser.getCity());
        model.setReceiveAlias(partnerAlias);
        model.setReceiveID(partnerID);
        model.setChannelID(mChannelID);

        final Gson gson = new Gson();

        Logger.i("WaitingActivity requestRejectPangChat PangChatByUser gson : " + gson.toJson(model));
        JsonObjectRequest request = HttpHelper.requestPangChat(model, "requestRejectPangChat",

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Result result = gson.fromJson(response.toString(), Result.class);
                        switch (result.getResultCode()) {

                            case HttpHelper.SUCCESS:
                                Logger.i("Successfully request reject pang chat");
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.success_reject), Toast.LENGTH_LONG);
                                finish();
                                break;

                            case HttpHelper.GCM_NO_REG:
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_unavailable), Toast.LENGTH_SHORT);
                                finish();
                                break;

                            default:
                                Logger.i("Fail to request cancel YoungChat : " + result.getResultMessage());
                                finish();
                                break;
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Logger.e(error.toString());
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_reject), Toast.LENGTH_LONG).show();
                    }
                });

        if (request != null) {
            addRequest(request);
        }
    }
}
