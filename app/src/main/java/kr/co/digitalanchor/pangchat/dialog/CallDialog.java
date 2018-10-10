package kr.co.digitalanchor.pangchat.dialog;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import kr.co.digitalanchor.pangchat.PCApplication;
import kr.co.digitalanchor.pangchat.R;

/**
 * Created by Peter Jung on 2016-11-22.
 */

public class CallDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private Button btnOK;
    private TextView mText;
    private TextView mText2;
    private String mMessage;
    private ImageButton btnClose;

    public CallDialog(Context context) {
        super(context);
        mContext = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    public void setMessage(String msg) {
        mMessage = msg;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_call);

        btnOK = (Button) findViewById(R.id.btn_ok);
        btnOK.setOnClickListener(this);

        btnClose = (ImageButton) findViewById(R.id.btn_close);
        btnClose.setOnClickListener(this);

        mText = (TextView)findViewById(R.id.textPartial);
        mText.setText(Html.fromHtml("<font color=red>" + mContext.getResources().getString(R.string.call) + "</font>&nbsp;" + mContext.getResources().getString(R.string.call_guide)));

        mText2 = (TextView)findViewById(R.id.textPartial2);


        final SpannableStringBuilder sb = new SpannableStringBuilder(mContext.getString(R.string.call_guide_detail3));

        // Span to set text color to some RGB value
        final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.RED);

        sb.setSpan(fcs, 60, 65, Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        mText2.setText(sb);/*
        mText2.setText(Html.fromHtml(mContext.getResources().getString(R.string.call_guide_detail2) +
                "<br><br>" + mContext.getResources().getString(R.string.call_guide_detail2_1) + "&nbsp;" +
                        "<font color=red>" + mContext.getResources().getString(R.string.call_guide_detail2_2) + "</font>" +
                        mContext.getResources().getString(R.string.call_guide_detail2_3)
                )
        );*/
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_ok:
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:1877-0859"));
                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mContext.startActivity(intent);

                PCApplication.setIsCall(true);
                Logger.i("PCApplication.isCall " + PCApplication.getIsCall());
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
