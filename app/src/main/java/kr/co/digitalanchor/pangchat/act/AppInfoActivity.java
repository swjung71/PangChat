package kr.co.digitalanchor.pangchat.act;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import kr.co.digitalanchor.pangchat.BaseActivity;
import kr.co.digitalanchor.pangchat.PCApplication;
import kr.co.digitalanchor.pangchat.R;
import kr.co.digitalanchor.pangchat.dialog.MainDialog;
import kr.co.digitalanchor.pangchat.handler.HttpHelper;
import kr.co.digitalanchor.pangchat.model.GetVersion;
import kr.co.digitalanchor.pangchat.model.VersionCheckResult;

public class AppInfoActivity extends BaseActivity implements View.OnClickListener {

    private Button btnUpdate;
    private Button btnBack;
    private ImageButton btnMenu;

    private TextView updateInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_info);

        btnUpdate = (Button)findViewById(R.id.btn_update);
        btnUpdate.setOnClickListener(this);

        btnBack = (Button)findViewById(R.id.btn_back);
        btnBack.setOnClickListener(this);

        btnMenu = (ImageButton)findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(this);

        updateInfo = (TextView)findViewById(R.id.versionText);
        updateInfo.setText(updateInfo.getText() + PCApplication.getAppVersionName());

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_update:
                getAvailableUpdate();
                break;

            case R.id.btn_back:
                finish();
                break;

            case R.id.btnMenu:
                MainDialog dialog = new MainDialog(this);
                dialog.show();

        }
    }

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

                                    Logger.i("Update exist");

                                    MaterialDialog.Builder builder = new MaterialDialog.Builder(AppInfoActivity.this);

                                    builder.title(getResources().getString(R.string.update))
                                            .content(getResources().getString(R.string.need_update))
                                            .positiveText(getResources().getString(R.string.confirm)).cancelable(false).callback(new MaterialDialog.SimpleCallback() {
                                        @Override
                                        public void onPositive(MaterialDialog materialDialog) {
                                            materialDialog.dismiss();
                                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                                            PCApplication.stopAllActivity();
                                        }
                                    }).build().show();
                                } else {
                                    MaterialDialog.Builder builder = new MaterialDialog.Builder(AppInfoActivity.this);

                                    builder.title(getResources().getString(R.string.update))
                                            .content(getResources().getString(R.string.last_version))
                                            .positiveText(getResources().getString(R.string.confirm)).cancelable(false).callback(new MaterialDialog.SimpleCallback() {
                                        @Override
                                        public void onPositive(MaterialDialog materialDialog) {
                                            materialDialog.dismiss();
                                        }
                                    }).build().show();

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
}
