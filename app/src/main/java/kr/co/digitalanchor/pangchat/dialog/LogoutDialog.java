package kr.co.digitalanchor.pangchat.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;

import kr.co.digitalanchor.pangchat.PCApplication;
import kr.co.digitalanchor.pangchat.R;
import kr.co.digitalanchor.pangchat.handler.DBHelper;

/**
 * Created by Peter Jung on 2016-11-24.
 */

public class LogoutDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private Button btnOK;
    private Button btnCancel;
    private ImageButton btnClose;

    private DBHelper mDBHelper;

    public LogoutDialog(Context context){
        super(context);
        mContext = context;
        mDBHelper = DBHelper.getInstance(mContext);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_logout);

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
            case R.id.btn_close:
            case R.id.btn_cancel:
                dismiss();
                break;
            case R.id.btn_ok:
                mDBHelper.logout();
                PCApplication.stopAllActivity();
                dismiss();
                break;
            default:
                break;
        }
    }
}
