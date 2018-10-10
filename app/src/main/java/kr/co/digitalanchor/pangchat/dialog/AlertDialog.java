package kr.co.digitalanchor.pangchat.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import kr.co.digitalanchor.pangchat.R;

/**
 * Created by Peter Jung on 2016-11-22.
 */

public class AlertDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private Button btnOK;
    private TextView mText;
    private String mMessage;
    private ImageButton btnClose;

    public AlertDialog(Context context) {
        super(context);
        mContext = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    public void setMessage(String msg){
        mMessage = msg;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_alert);

        btnOK = (Button)findViewById(R.id.btn_ok);
        btnOK.setOnClickListener(this);

        btnClose = (ImageButton)findViewById(R.id.btn_close);
        btnClose.setOnClickListener(this);

        mText = (TextView)findViewById(R.id.alert_msg);
        mText.setText(mMessage);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_ok:
                dismiss();
                break;

            case R.id.btn_close:
                dismiss();
                break;

            default:
                dismiss();
                break;
        }
    }
}
