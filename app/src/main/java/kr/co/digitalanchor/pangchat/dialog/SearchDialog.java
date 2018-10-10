package kr.co.digitalanchor.pangchat.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.orhanobut.logger.Logger;

import kr.co.digitalanchor.pangchat.BaseActivity;
import kr.co.digitalanchor.pangchat.R;

/**
 * Created by user on 2016-12-20.
 */

public class SearchDialog extends BaseActivity implements View.OnClickListener {

    private Button btnSearch;
    private EditText alias;
    private ImageButton btnClose;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_search);

        btnSearch = (Button)findViewById(R.id.btn_searchMember);
        btnSearch.setOnClickListener(this);
        btnClose = (ImageButton)findViewById(R.id.btn_close);
        btnClose.setOnClickListener(this);

        alias = (EditText)findViewById(R.id.text_alias);
    }

    @Override
    public void onClick(View v) {
        Logger.i("SearchDialog clicked");
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.btn_searchMember:
                intent.putExtra("alias", alias.getText().toString());
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
