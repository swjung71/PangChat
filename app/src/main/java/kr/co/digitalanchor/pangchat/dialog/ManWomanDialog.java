package kr.co.digitalanchor.pangchat.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;

import kr.co.digitalanchor.pangchat.BaseActivity;
import kr.co.digitalanchor.pangchat.R;

/**
 * Created by user on 2016-12-20.
 */

public class ManWomanDialog extends BaseActivity implements View.OnClickListener {

    private Button btnManWoman;
    private Button btnMan;
    private Button btnWoman;

    private ImageButton btnClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_man_woman);


        btnManWoman = (Button)findViewById(R.id.btn_man_woman);
        btnManWoman.setOnClickListener(this);

        btnMan = (Button)findViewById(R.id.btn_man);
        btnMan.setOnClickListener(this);

        btnWoman = (Button)findViewById(R.id.btn_woman);
        btnWoman.setOnClickListener(this);

        btnClose = (ImageButton)findViewById(R.id.btn_close);
        btnClose.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.btn_man_woman:

                intent.putExtra("sex", -1);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.btn_man:
                intent.putExtra("sex", 0);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.btn_woman:
                intent.putExtra("sex", 1);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.btn_close:
                setResult(RESULT_CANCELED);
                finish();
            default:
                break;
        }
    }
}
