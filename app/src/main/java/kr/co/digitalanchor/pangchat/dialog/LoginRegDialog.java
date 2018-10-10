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
import kr.co.digitalanchor.pangchat.act.SignupActivity;

/**
 * Created by Peter Jung on 2016-11-22.
 */

public class LoginRegDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private Button btn_login;
    private Button btn_reg;
    private ImageButton btn_close;

    protected LoginRegDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public LoginRegDialog(Context context) {
        super(context);
        mContext = context;
        //다이얼로그에 title bar를 없애는 것
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }

    public LoginRegDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_login_reg);

        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);

        btn_close = (ImageButton)findViewById(R.id.btn_close);
        btn_close.setOnClickListener(this);

        btn_reg = (Button)findViewById(R.id.btn_reg);
        btn_reg.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_login:
                LoginDialog dialog = new LoginDialog(mContext);
                dialog.show();
                dismiss();
                break;
            case R.id.btn_reg:

                Intent intent = new Intent(mContext, SignupActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mContext.startActivity(intent);
                dismiss();
                break;

            case R.id.btn_close:
                //dismiss();
                break;

            default:
                break;
        }
    }
}
