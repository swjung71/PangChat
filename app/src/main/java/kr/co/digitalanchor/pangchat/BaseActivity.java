package kr.co.digitalanchor.pangchat;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.orhanobut.logger.Logger;

import kr.co.digitalanchor.pangchat.dialog.CustomProgressDialog;
import kr.co.digitalanchor.pangchat.handler.MultipartRequest;
import kr.co.digitalanchor.pangchat.handler.VolleySingleton;

/**
 * Created by Peter Jung on 2015-06-10.
 */
public class BaseActivity extends FragmentActivity {

    private final long INTERVAL = 1000; //1초를 나타냄, 1초 이내의 클릭은 방지하는 코드

    private long draft = 0;//바로 앞의 클릭 시간을 저장하는 변수

    protected RequestQueue mQueue;

    protected CustomProgressDialog mLoading;

    /**
     * Handler는 메인(UI) Thread와 backThread(통신 같은 것을 처림하는 thread)간의 통신을 하기 위한 수단
     * 한 Thread가 다른 Thread의 member 변수를 건들이면 앱이 죽음 (왜냐하면 동시성 문제가 발생하기
     * 때문에 Android에서 이를 막기 위해서 thread 간에 member를 건들이지 못하게 함)
     */
    private final Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            onHandleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //activity 생성시 자동으로 PCApplication에 등록함
        PCApplication.addActivity(this);
        //mQueue = Volley.newRequestQueue(getApplicationContext(), new OkHttp3Stack());
        //volley singleton으로 하나의 volley만 사용
        mQueue = VolleySingleton.getmInstance(getApplicationContext()).getmRequestQueue();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {

        PCApplication.removeActivity(this);
        if (mLoading != null) {

            if (mLoading.isShowing()) {

                mLoading.dismiss("");
            }
            mLoading = null;
        }
        super.onDestroy();
    }

    protected void onHandleMessage(Message msg) {
    }

    public Handler getHandler() {
        return mHandler;
    }

    public void sendEmptyMessage(int what) {

        if (mHandler != null) {

            mHandler.sendEmptyMessage(what);
        }
    }

    public void sendEmptyMessage(int what, long delay) {

        if (mHandler != null) {

            mHandler.sendEmptyMessageDelayed(what, delay);
        }
    }

    public void sendMessage(int what, Bundle data) {

        if (mHandler != null && data != null) {

            Message msg = new Message();

            msg.what = what;

            msg.setData(data);

            mHandler.sendMessage(msg);
        }
    }


    /**
     * 짧은 더블 클릭 방지
     */
    public boolean isDuplicateRuns() {

        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - draft;

        if (0 <= intervalTime && INTERVAL >= intervalTime) {

            return true;

        } else {

            draft = tempTime;

            return false;
        }
    }

    protected void showLoading() {

        if (mLoading == null) {
            mLoading = new CustomProgressDialog(this);
            mLoading.setCancelable(false);
        }
        mLoading.show("");
    }

    protected void showLoading(String title) {

        if (mLoading == null) {

            mLoading = new CustomProgressDialog(this);
            mLoading.setCancelable(false);
            mLoading.setTitle(title);
        }

        mLoading.show("");
    }

    protected void dismissLoading() {

        if (mLoading != null) {
            mLoading.dismiss("");
        }
    }

    protected void addRequest(JsonObjectRequest request) {

        try {

            mQueue.add(request);

        } catch (Exception e) {

            e.printStackTrace();
            Logger.e(e.getMessage());

            dismissLoading();
        }
    }

    protected void addRequest(MultipartRequest request) {

        try {

            mQueue.add(request);

        } catch (Exception e) {

            e.printStackTrace();
            Logger.e(e.getMessage());

            dismissLoading();
        }
    }

    protected void addRequest(ImageRequest request) {

        try {

            mQueue.add(request);

        } catch (Exception e) {

            e.printStackTrace();
            Logger.e(e.getMessage());

            dismissLoading();
        }
    }

    protected void handleResultCode(int code, String msg) {

        dismissLoading();

        switch (code) {

            case 0001:

                msg = "서버 오류입니다. (SQL Error)";

                break;


            default:

                msg = "알수없는 오류입니다.";

                break;
        }

        Logger.e(msg);

        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    protected void handleError(VolleyError error) {

        dismissLoading();

        Logger.e(error.toString());

        if (error instanceof ServerError) {

            Toast.makeText(getApplicationContext(), getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            //Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();

        } else if (error instanceof TimeoutError) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            //Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();

        } else if (error instanceof ParseError) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            //Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();

        } else if (error instanceof NetworkError) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            //Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();

        } else if (error instanceof NoConnectionError) {

            MaterialDialog.Builder builder = new MaterialDialog.Builder(this);

            builder.title(getResources().getString(R.string.alarm)).content(getResources().getString(R.string.network_turn_off))
                    .positiveText(getResources().getString(R.string.confirm))
                    .callback(new MaterialDialog.SimpleCallback() {
                        @Override
                        public void onPositive(MaterialDialog materialDialog) {

                            PCApplication.stopAllActivity();

                            materialDialog.dismiss();
                        }
                    }).build().show();

        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            //Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
