package kr.co.digitalanchor.pangchat.dialog;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import kr.co.digitalanchor.pangchat.BaseActivity;
import kr.co.digitalanchor.pangchat.R;
import kr.co.digitalanchor.pangchat.act.MainActivity;
import kr.co.digitalanchor.pangchat.handler.DBHelper;
import kr.co.digitalanchor.pangchat.handler.HttpHelper;
import kr.co.digitalanchor.pangchat.model.CheckID;
import kr.co.digitalanchor.pangchat.model.ReviewResult;
import kr.co.digitalanchor.pangchat.model.User;

/**
 * Created by Peter Jung on 2016-11-22.
 */

public class ReviewDialog extends BaseActivity implements View.OnClickListener {

    private Button btnOK;
    private Button btnCancel;
    private ImageButton btnClose;
    private User mUser;
    private DBHelper mDBHelper;

    public void setUser(User user) {
        mUser = user;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_review);

        mDBHelper = DBHelper.getInstance(this);
        mUser= mDBHelper.getUserInfo();

        btnOK = (Button) findViewById(R.id.btn_ok);
        btnOK.setOnClickListener(this);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(this);
        btnClose = (ImageButton) findViewById(R.id.btn_close);
        btnClose.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_ok:

                reviewRequst();
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details?id=" + getPackageName()));
                //intent.setData(Uri.parse("market://details?id=com.misell.goodguygoodgirl"));
                startActivity(intent);

                break;
            case R.id.btn_close:
            case R.id.btn_cancel:
                Intent intentMain = new Intent(this, MainActivity.class);
                intentMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentMain);
                finish();
                break;
            default:
                break;
        }
    }


    private void reviewRequst() {
        //deviceModel과 OSVersion, isAndroid 는 user에 정의되어 있음
        CheckID checkID = new CheckID();
        checkID.setUserName(mUser.getUserName());
        final Gson gson = new Gson();

        Logger.i(gson.toJson(checkID));

        JsonObjectRequest request = HttpHelper.review(checkID, "review",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        ReviewResult result = gson.fromJson(response.toString(), ReviewResult.class);
                        switch (result.getResultCode()) {

                            case HttpHelper.SUCCESS:
                                Logger.i("success review");

                                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                                Calendar cal = Calendar.getInstance();
                                try {
                                    cal.setTime(df.parse(mUser.getEndDay()));
                                    cal.add(Calendar.DAY_OF_YEAR, 7);
                                    mDBHelper.updateEndDay(df.format(cal.getTime()));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                finish();
                                break;

                            default:
                                Logger.i("Fail to review: " + result.getResultMessage());
                                finish();
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
