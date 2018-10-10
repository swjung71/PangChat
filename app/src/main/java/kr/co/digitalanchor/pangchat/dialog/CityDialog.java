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

public class CityDialog extends BaseActivity implements View.OnClickListener {

    private Button btnNationWide;
    private Button btnSeoul; //0
    private Button btnIncheon; //4
    private Button btnDaejun; //6
    private Button btnDaeGu; ///10
    private Button btnGangju; //2
    private Button btnUlsan; // 11
    private Button btnPusan; //1
    private Button btnKyunggi; //3
    private Button btnKangwan; //7
    private Button btnChongchung; //5
    private Button btnJeolla; //8
    private Button btnKyungsang; //9
    private Button btnJeju; //12

    private ImageButton btnClose;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_city);
        btnNationWide = (Button)findViewById(R.id.btn_nation_wide);
        btnSeoul = (Button)findViewById(R.id.btn_seoul);
        btnIncheon = (Button)findViewById(R.id.btn_incheon);
        btnDaejun = (Button)findViewById(R.id.btn_daejun);
        btnDaeGu = (Button)findViewById(R.id.btn_deagu);
        btnGangju = (Button)findViewById(R.id.btn_kangju);
        btnUlsan = (Button)findViewById(R.id.btn_ulsan);
        btnPusan = (Button)findViewById(R.id.btn_pusan);
        btnKyunggi = (Button)findViewById(R.id.btn_kyunggi);
        btnKangwan = (Button)findViewById(R.id.btn_gangwon);
        btnChongchung = (Button)findViewById(R.id.btn_chongchung);
        btnJeolla = (Button)findViewById(R.id.btn_jeolla);
        btnKyungsang = (Button)findViewById(R.id.btn_kyungsang);
        btnJeju = (Button)findViewById(R.id.btn_jeju);
        btnClose = (ImageButton)findViewById(R.id.btn_close);

        btnNationWide.setOnClickListener(this);
        btnSeoul.setOnClickListener(this); //0
        btnIncheon.setOnClickListener(this); //4
        btnDaejun.setOnClickListener(this); //6
        btnDaeGu.setOnClickListener(this); ///10
        btnGangju.setOnClickListener(this); //2
        btnUlsan.setOnClickListener(this); // 11
        btnPusan.setOnClickListener(this); //1
        btnKyunggi.setOnClickListener(this); //3
        btnKangwan.setOnClickListener(this); //7
        btnChongchung.setOnClickListener(this); //5
        btnJeolla.setOnClickListener(this); //8
        btnKyungsang.setOnClickListener(this); //9
        btnJeju.setOnClickListener(this); //12
        btnClose.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.btn_nation_wide:
                intent.putExtra("city", -1);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.btn_seoul:
                intent.putExtra("city", 0);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.btn_incheon:
                intent.putExtra("city", 4);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.btn_daejun:
                intent.putExtra("city", 6);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.btn_deagu:
                intent.putExtra("city", 10);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.btn_kangju:
                intent.putExtra("city", 2);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.btn_ulsan:
                intent.putExtra("city", 11);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.btn_pusan:
                intent.putExtra("city", 1);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.btn_kyunggi:
                intent.putExtra("city", 3);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.btn_gangwon:
                intent.putExtra("city", 7);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.btn_chongchung:
                intent.putExtra("city", 5);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.btn_jeolla:
                intent.putExtra("city", 8);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.btn_jeju:
                intent.putExtra("city", 12);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.btn_kyungsang:
                intent.putExtra("city", 9);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.btn_close:
                setResult(RESULT_CANCELED);
                finish();
                break;
            default:
                break;
        }
    }
}
