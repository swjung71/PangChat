package kr.co.digitalanchor.pangchat.act;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

import kr.co.digitalanchor.pangchat.BaseActivity;
import kr.co.digitalanchor.pangchat.R;

/**
 * Created by Xian on 2015-06-10.
 */
public class PolicyAgreeActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.term_webview);
        WebView webView = (WebView)findViewById(R.id.webView);

        Intent intent = getIntent();
        int type = intent.getIntExtra("Type", 0);
        switch (type){
            case 0:
                webView.loadUrl("file:///android_asset/html/term.txt");
                break;
            case 1:
                webView.loadUrl("file:///android_asset/html/privacy.txt");
                break;
            case 2:
                webView.loadUrl("file:///android_asset/html/location.txt");
                break;
            default:
                break;
        }
        //webView.loadUrl("file:///android_asset/html/location.txt");
    }
}
