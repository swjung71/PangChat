package kr.co.digitalanchor.pangchat.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;

import kr.co.digitalanchor.pangchat.R;
import kr.co.digitalanchor.pangchat.act.AppInfoActivity;
import kr.co.digitalanchor.pangchat.act.MyProfileActivity;
import kr.co.digitalanchor.pangchat.act.NoticeActivity;

/**
 * Created by Peter Jung on 2016-11-16.
 */
public class MainDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private ImageButton btnClose;
    private Button btnProfile;
    private Button btnNotice;
    private Button btnAppInfo;
    private Button btnLogout;
    private Button btnWithdraw;

    public MainDialog(Context context) {
        super(context);
        mContext = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    public MainDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected MainDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_dialog);

        btnProfile = (Button)findViewById(R.id.btn_profile);
        btnProfile.setOnClickListener(this);
        btnNotice = (Button)findViewById(R.id.btn_notices);
        btnNotice.setOnClickListener(this);
        btnAppInfo = (Button)findViewById(R.id.btn_app_info);
        btnAppInfo.setOnClickListener(this);
        btnLogout = (Button)findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(this);
        btnWithdraw = (Button)findViewById(R.id.btn_withdraw);
        btnWithdraw.setOnClickListener(this);
        btnClose = (ImageButton)findViewById(R.id.btn_close);
        btnClose.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btn_profile:

                Intent profileIntent = new Intent(mContext, MyProfileActivity.class);
                mContext.startActivity(profileIntent);
                this.dismiss();

                break;
            case R.id.btn_notices:

                Intent noticeIntent = new Intent(mContext, NoticeActivity.class);
                mContext.startActivity(noticeIntent);
                this.dismiss();
                break;

            case R.id.btn_app_info:

                Intent appInfoIntent = new Intent(mContext, AppInfoActivity.class);
                mContext.startActivity(appInfoIntent);
                dismiss();
                break;

            case R.id.btn_logout:
                LogoutDialog logoutDialog = new LogoutDialog(mContext);
                logoutDialog.show();
                dismiss();
                break;
            case R.id.btn_withdraw:
                WithdrawDialog withdrawDialog = new WithdrawDialog(mContext);
                withdrawDialog.show();
                dismiss();
                break;
            case R.id.btn_close:
                dismiss();
                break;
            default:
                break;
        }
    }
}
