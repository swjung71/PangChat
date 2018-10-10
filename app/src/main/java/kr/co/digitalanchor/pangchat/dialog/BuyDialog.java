package kr.co.digitalanchor.pangchat.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;

import kr.co.digitalanchor.pangchat.R;
import kr.co.digitalanchor.pangchat.handler.DBHelper;
import kr.co.digitalanchor.pangchat.model.User;

/**
 * Created by Peter Jung on 2016-11-22.
 */

public class BuyDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private User mUser;
    private DBHelper mDBHelper;

    private Button btn1Month;
    private Button btn3Month;
    private Button btn5Month;
    private Button btnTop;
    private Button btnSame;
    private ImageButton btnClose;

    public BuyDialog(Context context){
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mContext = context;
        mDBHelper = DBHelper.getInstance(mContext);
        mUser = mDBHelper.getUserInfo();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_buy);

        btn1Month = (Button)findViewById(R.id.btn_buy_1);
        btn1Month.setOnClickListener(this);

        btn3Month = (Button)findViewById(R.id.btn_buy_3);
        btn3Month.setOnClickListener(this);

        btn5Month = (Button)findViewById(R.id.btn_buy_5);
        btn1Month.setOnClickListener(this);

        btnTop = (Button)findViewById(R.id.btn_buy_top);
        btnTop.setOnClickListener(this);

        btnSame = (Button)findViewById(R.id.btn_same_inter);
        btnSame.setOnClickListener(this);

        btnClose= (ImageButton)findViewById(R.id.btn_close);
        btnClose.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btn_buy_1:
                break;

            case R.id.btn_buy_3:
                break;

            case R.id.btn_buy_5:
                break;

            case R.id.btn_buy_top:
                break;

            case  R.id.btn_same_inter:
                break;

            case R.id.btn_close:
                dismiss();
                break;

            default:
                break;
        }
    }
}
