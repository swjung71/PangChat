package kr.co.digitalanchor.pangchat.act;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import kr.co.digitalanchor.pangchat.BaseActivity;
import kr.co.digitalanchor.pangchat.R;
import kr.co.digitalanchor.pangchat.dialog.MainDialog;

public class NoticeActivity extends BaseActivity implements View.OnClickListener{

    private Button btnBack;
    private ImageButton btnMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        btnBack = (Button)findViewById(R.id.btn_back);
        btnBack.setOnClickListener(this);

        btnMenu = (ImageButton)findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnMenu:
                MainDialog dialog = new MainDialog(this);
                dialog.show();
                break;
            case R.id.btn_back:
                finish();
                break;
            default:
                break;
        }
    }
}
