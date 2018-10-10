package kr.co.digitalanchor.pangchat.act;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.sktelecom.playrtc.exception.RequiredConfigMissingException;
import com.sktelecom.playrtc.exception.RequiredParameterMissingException;
import com.sktelecom.playrtc.exception.UnsupportedPlatformVersionException;
import com.sktelecom.playrtc.util.ui.PlayRTCVideoView;

import org.json.JSONException;
import org.json.JSONObject;

import kr.co.digitalanchor.pangchat.BaseActivity;
import kr.co.digitalanchor.pangchat.PCApplication;
import kr.co.digitalanchor.pangchat.R;
import kr.co.digitalanchor.pangchat.handler.DBHelper;
import kr.co.digitalanchor.pangchat.handler.HttpHelper;
import kr.co.digitalanchor.pangchat.handler.PlayRTCHandler;
import kr.co.digitalanchor.pangchat.model.FriendAdd;
import kr.co.digitalanchor.pangchat.model.OnAirClass;
import kr.co.digitalanchor.pangchat.model.PangChatByUser;
import kr.co.digitalanchor.pangchat.model.PangChatUser;
import kr.co.digitalanchor.pangchat.model.Result;
import kr.co.digitalanchor.pangchat.model.User;
import kr.co.digitalanchor.pangchat.view.PlayRTCVideoViewGroup;

public class VideoChatActivity extends BaseActivity implements View.OnClickListener{

    /*
   * PlayRTC-Handler Class
   * PlayRTC 메소드 , PlayRTC객체의 이벤트 처리
   */
    private PlayRTCHandler playrtcHandler = null;

    /*
     * PlayRTCVideoView를 위한 부모 뷰 그룹
     */
    public PlayRTCVideoViewGroup videoArea = null;

    /*
     * isCloesActivity가 false이면 Dialog를 통해 사용자의 종료 의사를 확인하고<br>
     * Activity를 종료 처리. 만약 채널에 입장한 상태이면 먼저 채널을 종료한다.
     */
    private boolean isCloesActivity = false;

    /*
     * PlayRTC Sample Type
     * - 1 : 영상 + 음성 + Data Sample<br>
     * - 2 : 영상 + 음성 Sample<br>
     * - 3 : 음성 Sample<br>
     * - 4 : Data Sample<br>
     */
    private int playrtcType = 2;
    /*
     * isRandom
     * 1 이면 random chatting
     * 0 이면 수동 연결
     */
    private int isRandom = 0;

    private boolean isAvailable = true;


    //connect 된 상태인지 확인
    private boolean connected = false;

    private JSONObject channel = null;

    String mChannelID;
    private PangChatUser mGuest;
    private String guestID;
    private String myID;
    private User mUser;

    private DBHelper mDBHelper;

    private Button btnLike;
    private Button btnAddFriend;
    private Button btnEnd;

    /*
    * 1이면 내가 거는 것
    * 0이면 상대방으로부터 걸려 온것
    */
    private int isRequester;

    public boolean isOnSave = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);

        setContentView(R.layout.activity_video_chat);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

        mDBHelper = DBHelper.getInstance(this);

        MainActivity mainActivity = PCApplication.getMainActivity();
        Logger.i("it is start releaseCamera");
        if(mainActivity != null){
            Logger.i("it is start releaseCamera");
            mainActivity.getmPreview().releaseCamera();
            mainActivity.isRelease = true;
        }
        Intent intent = getIntent();

        playrtcHandler = new PlayRTCHandler(this);
        //  PlayRTC 인스턴스를 생성.
        try {
            playrtcHandler.createPlayRTC(playrtcType);
        } catch (UnsupportedPlatformVersionException e) {
            e.printStackTrace();
        } catch (RequiredParameterMissingException e) {
            e.printStackTrace();
        }

        String param = intent.getStringExtra("param");
        Gson gson = new Gson();
        Logger.i("param : " + gson.toJson(param));

        try {

            channel = new JSONObject(param);
            mUser = mDBHelper.getUserInfo();
            myID = String.valueOf(mDBHelper.getUserInfo().getUserPK());

            //mGuest = mDBHelper.getGuest(guestID);

            //1이 requestor임
            isRequester = intent.getIntExtra("isRequester", 1);
            //receiver 면

           /* mGuest = new PangChatUser();
            mGuest.setAlias(intent.getStringExtra("partnerAlias"));
            mGuest.setCity(intent.getStringExtra("partnerCity"));
            guestID = intent.getStringExtra("partnerID");
            mGuest.setUserID(Integer.parseInt(guestID));
            mGuest.setImageURL(intent.getStringExtra("partnerImage"));*/

            if(isRequester == 0){
                mGuest = new PangChatUser();
                mGuest.setAlias(intent.getStringExtra("partnerAlias"));
                mGuest.setCity(intent.getStringExtra("partnerCity"));
                guestID = intent.getStringExtra("partnerID");
                mGuest.setUserID(Integer.parseInt(guestID));
                mGuest.setImageURL(intent.getStringExtra("partnerImage"));
                Logger.i("partnerID : " + guestID);
                Logger.i("imagePath : " + mGuest.getImageURL());
                Logger.i("partnerAlias : " + mGuest.getAlias());
            }else {
                mGuest = (PangChatUser)intent.getSerializableExtra("partner");
                guestID = ""+mGuest.getUserID();
            }

            btnLike = (Button)findViewById(R.id.btn_like);
            btnLike.setOnClickListener(this);
            btnAddFriend = (Button)findViewById(R.id.btn_friend);
            btnAddFriend.setOnClickListener(this);
            btnEnd = (Button)findViewById(R.id.btn_end);
            btnEnd.setOnClickListener(this);
            /*video 스트림 출력을 위한 PlayRTCVideoView의 부모 ViewGroup */
            videoArea = (PlayRTCVideoViewGroup) findViewById(R.id.videoarea);
            mChannelID = intent.getStringExtra("channelID").trim();

            Logger.i("channelID : " + mChannelID);

            //내가 거는 것
            if (isRequester == 1) {
                this.getPlayRTCHandler().createChannel(channel);
                getPlayRTCHandler().connectChannel(mChannelID, channel);
                //createDialog();
                Logger.i("createChannel :" + channel.toString() + ":");
                Logger.i("createChannel :" + mChannelID + ":");
                //내가 받는 것
            } else {
                Logger.i("ConnectChannel : channelID : " + mChannelID);
                Logger.i("connect Channel : " + channel.toString());
                getPlayRTCHandler().connectChannel(mChannelID, channel);
            }
        } catch (RequiredConfigMissingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //createVideo();
        updateOnAir(2);

    }

/*    @Override
    protected void onUserLeaveHint() {.
        super.onUserLeaveHint();
        updateOnAir(1);
    }*/

    @Override
    public void onPause() {
        super.onPause();
        // 미디어 스트리밍 처리 pause
        if (playrtcHandler != null) playrtcHandler.onActivityPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        // 미디어 스트리밍 처리 resume
        if (playrtcHandler != null) playrtcHandler.onActivityResume();

        Logger.i("Video onResume");
        PCApplication.setIsVideo(true);

    }

    @Override
    protected void onDestroy() {
        if(getPlayRTCHandler().getPlayRTC() != null) {
            // If you does not call playrtc.close(), playrtc instence is remaining every new call.
            // playrtc instence can not used again

            Logger.i("getPlayRTC remove");

            getPlayRTCHandler().getPlayRTC().close();
            getPlayRTCHandler().setPlayRTC(null);
        }

        //getPlayRTCHandler().getPlayRTC().getLocalMedia().removeVideoRenderer();
        //playrtcObserver = null;
        //android.os.Process.killProcess(android.os.Process.myPid());
        //super.onDestroy();

        // PlayRTC 인스턴스 해제
        if (playrtcHandler != null) {
            playrtcHandler.close();
            playrtcHandler = null;
        }
        // v2.2.6
        if (videoArea != null) {
            Logger.i("videoArea is release");
            videoArea.releaseView();
        }

        this.finish();
        if(PCApplication.getMainActivity() != null){
            if(PCApplication.getMainActivity().videoArea.getVisibility() == View.VISIBLE) {
                Logger.i("create video preview");
                //PCApplication.getMainActivity().btnBack.callOnClick();
                PCApplication.getMainActivity().createVideoPreview();
            }
        }

        updateOnAir(0);//0 on, 1 off, 2 onAir
        //MainActivity.isVideo = false;
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //super.onSaveInstanceState(outState);
        isOnSave = true;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        // Layout XML에 VideoView를 기술한 경우. v2.2.6
        Logger.i("onWindowFocusChanged");
        if (hasFocus && videoArea.isInitVideoView() == false) {
            // 4. 영상 스트림 출력을 위한 PlayRTCVideoView 초기화
            Logger.i("onWindowFocusChanged false");
            videoArea.initVideoView();
        }
    }

    @Override
    public void onBackPressed() {

        // Activity를 종료하도록 isCloesActivity가 true로 지정되어 있다면 종료 처리
        if (isCloesActivity) {
            // BackPress 처리 -> onDestroy 호출
            setResult(RESULT_OK, new Intent());
            sendCancel(mChannelID);
            super.onBackPressed();
        } else {
            if (playrtcHandler.isChannelConnected() == true) {
                isCloesActivity = false;
                // PlayRTC 플랫폼 채널을 종료한다.
                playrtcHandler.disconnectChannel();
            } else {
                isCloesActivity = true;
                onBackPressed();
            }
        }
    }
    /*
     * PlayRTCHandler 인스턴스를 반환한다.
     * @return PlayRTCHandler
     */
    public PlayRTCHandler getPlayRTCHandler() {
        return playrtcHandler;
    }

    public PlayRTCVideoViewGroup getVideoArea() {
        return videoArea;
    }

    /*
     * 로컬 영상 PlayRTCVideoView 인스턴스를 반환한다.
     * @return PlayRTCVideoView
     */
    public PlayRTCVideoView getLocalVideoView() {
        return videoArea.getLocalView();
    }

    /*
     * 상대방 영상 PlayRTCVideoView 인스턴스를 반환한다.
     * @return PlayRTCVideoView
     */
    public PlayRTCVideoView getRemoteVideoView() {
        return videoArea.getRemoteView();
    }

    /*
     * PlayRTCActivity를 종료한다.
     * PlayRTCHandler에서 채널이 종료 할 때 호출한다.
     * @param isClose boolean, 종료 처리 시 사용자의 종료 으의사를 묻는 여부
     */
    public void setOnBackPressed(boolean isClose) {
        isCloesActivity = isClose;
        //CameraPreview.setBlocked(false);
        this.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_end:
                if(getPlayRTCHandler().isPeerSuccess == true) {
                    Logger.i("isPeerSuccess : true");
                    onBackPressed();
                }else{
                    Logger.i("isPeerSuccess : false");

                    onBackPressed();
                }
                break;
            case R.id.btn_like:
                addLike();
                break;
            case R.id.btn_friend:
                addFriend();
                break;
            default:
                break;
        }
    }

    private void addFriend(){

        FriendAdd model = new FriendAdd();
        model.setUserID(String.valueOf(mGuest.getUserID()));
        model.setMyID(String.valueOf(mUser.getUserPK()));

        final Gson gson = new Gson();
        Logger.i(gson.toJson(model));

        JsonObjectRequest request = HttpHelper.addFriend(model, "addFriend",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Result result = gson.fromJson(response.toString(), Result.class);
                        switch (result.getResultCode()) {

                            case HttpHelper.SUCCESS:
                                Logger.i("Successfully addFriend");
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.success_add_friend), Toast.LENGTH_LONG).show();

                                break;

                            case HttpHelper.EXIST_FRIEND:
                                Logger.i("Successfully addFriend");
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.already_friend), Toast.LENGTH_LONG).show();
                                break;

                            default:
                                Logger.i("Fail to addFriend : " + result.getResultMessage());
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_add_friend), Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Logger.e(error.toString());
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_add_friend), Toast.LENGTH_LONG).show();
                    }
                });

        if (request != null) {
            addRequest(request);
        }
    }

    private void addLike(){

        FriendAdd model = new FriendAdd();
        model.setUserID(String.valueOf(mGuest.getUserID()));
        model.setMyID(String.valueOf(mUser.getUserPK()));

        final Gson gson = new Gson();
        Logger.i(gson.toJson(model));

        JsonObjectRequest request = HttpHelper.addFriend(model, "addLike",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Result result = gson.fromJson(response.toString(), Result.class);
                        switch (result.getResultCode()) {

                            case HttpHelper.SUCCESS:
                                Logger.i("Successfully addLike");
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.success_add_like), Toast.LENGTH_LONG).show();

                                break;

                            case HttpHelper.EXIST_FRIEND:
                                Logger.i("Successfully addLike");
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.alread_add_like), Toast.LENGTH_LONG).show();
                                break;

                            default:
                                Logger.i("Fail to addLike : " + result.getResultMessage());
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_add_like), Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Logger.e(error.toString());
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_add_like), Toast.LENGTH_LONG).show();
                    }
                });

        if (request != null) {
            addRequest(request);
        }
    }
    public void requestChat(String channelID) {

        mChannelID = channelID;

        //등록되지 않은 사용자
        if(mUser == null || mUser.getUserAlias() == null || TextUtils.isEmpty(mUser.getUserAlias())){
            Logger.i("GCM userAlias is empty");
            return;
        }

        /*if(mGuest.getIsOnAir() != 0 ){
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_unavailable), Toast.LENGTH_SHORT).show();
            return;
        }*/

        PangChatByUser model = new PangChatByUser();
        model.setAge(mUser.getAge());
        model.setSex(mUser.getSex());
        model.setUserAlias(mUser.getUserAlias());
        model.setUserID(""+mUser.getUserPK());
        model.setReceiveAlias(mGuest.getAlias());
        model.setImageURL(mGuest.getImageURL());
        model.setCity(mGuest.getCity());
        model.setReceiveID(""+mGuest.getUserID());
        //model.setChannelID("PCABC");
        model.setChannelID(channelID);
        Logger.i("getChannelID : " + model.getChannelID());
        //model.setChannelID("PC-" + mUser.getUserPK() + "-" + mGuest.getUserID());

        //model.setChannelID("YC-");
        final Gson gson = new Gson();

        Logger.i("video chat activity requestPangChat PangChatByUser gson : " + gson.toJson(model));
        JsonObjectRequest request = HttpHelper.requestChat(model, "requestPangChat",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Result result = gson.fromJson(response.toString(), Result.class);
                        switch (result.getResultCode()) {

                            case HttpHelper.SUCCESS:
                                Logger.i("Successfully request PangChat");
                                //isSuccessRequestFaceChat = true;
                                //startRTCActivity();
                                //Toast.makeText(getApplicationContext(), "Successfully  date", Toast.LENGTH_LONG).show();
                                break;

                            case HttpHelper.NO_AVIL:
                                Logger.i("Successfully NO available");
                                isAvailable = false;
                                //isSuccessRequestFaceChat = true;
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_unavailable), Toast.LENGTH_SHORT).show();
                                break;

                            case HttpHelper.GCM_NO_REG:
                                Logger.i("Successfully NO available");
                                isAvailable = false;
                                //isSuccessRequestFaceChat = true;
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_other), Toast.LENGTH_SHORT).show();
                                finish();
                                break;

                            default:
                                Logger.i("Fail to request PangChat : " + result.getResultMessage());
                                Toast.makeText(getApplicationContext(), result.getResultMessage(), Toast.LENGTH_SHORT).show();
                                break;
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Logger.e(error.toString());
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                    }
                });

        if (request != null) {
            addRequest(request);
        }
    }

    public void sendCancel(String channelID) {

        //등록되지 않은 사용자
        if(mUser == null || mUser.getUserAlias() == null || TextUtils.isEmpty(mUser.getUserAlias())){
            Logger.i("GCM userAlias is empty");
            return;
        }



        /*if(mGuest.getIsOnAir() != 0 ){
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_unavailable), Toast.LENGTH_SHORT).show();
            return;
        }*/

        PangChatByUser model = new PangChatByUser();
        model.setAge(mUser.getAge());
        model.setSex(mUser.getSex());
        model.setUserAlias(mUser.getUserAlias());
        model.setUserID(""+mUser.getUserPK());
        model.setReceiveAlias(mGuest.getAlias());
        model.setImageURL(mGuest.getImageURL());
        model.setCity(mGuest.getCity());
        model.setReceiveID(""+mGuest.getUserID());
        //model.setChannelID("PCABC");
        model.setChannelID(channelID);
        Logger.i("getChannelID : " + model.getChannelID());
        //model.setChannelID("PC-" + mUser.getUserPK() + "-" + mGuest.getUserID());

        //model.setChannelID("YC-");
        final Gson gson = new Gson();

        Logger.i("videoChatActivity requestCancelPangChat PangChatByUser gson : " + gson.toJson(model));
        JsonObjectRequest request = HttpHelper.requestChat(model, "requestCancelPangChat",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Result result = gson.fromJson(response.toString(), Result.class);
                        switch (result.getResultCode()) {

                            case HttpHelper.SUCCESS:
                                Logger.i("Successfully request requestCancelPangChat");
                                //isSuccessRequestFaceChat = true;
                                //startRTCActivity();
                                //Toast.makeText(getApplicationContext(), "Successfully  date", Toast.LENGTH_LONG).show();
                                break;

                            case HttpHelper.NO_AVIL:
                                Logger.i(" requestCancelPangChat Successfully NO available");
                                //isSuccessRequestFaceChat = true;
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_unavailable), Toast.LENGTH_SHORT).show();
                                break;

                            case HttpHelper.GCM_NO_REG:
                                //Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_unavailable), Toast.LENGTH_SHORT).show();
                                break;

                            default:
                                Logger.i("Fail to requestCancelPangChat : " + result.getResultMessage());
                                Toast.makeText(getApplicationContext(), result.getResultMessage(), Toast.LENGTH_SHORT).show();
                                break;
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Logger.e(error.toString());
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                    }
                });

        if (request != null) {
            addRequest(request);
        }
    }

    public void updateOnAir(int onAir) {

        //등록되지 않은 사용자
        if(mUser == null || mUser.getUserAlias() == null || TextUtils.isEmpty(mUser.getUserAlias())){
            Logger.i("GCM userAlias is empty");
            return;
        }

        /*if(mGuest.getIsOnAir() != 0 ){
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_unavailable), Toast.LENGTH_SHORT).show();
            return;
        }*/

        OnAirClass model = new OnAirClass();
        model.setIsOnAir(onAir);
        model.setUserID(mUser.getUserPK());
        final Gson gson = new Gson();

        Logger.i("VideoChatActivity updateOnAir PangChatByUser gson : " + gson.toJson(model));
        JsonObjectRequest request = HttpHelper.updateOnAir(model, "updateOnAir",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Result result = gson.fromJson(response.toString(), Result.class);
                        switch (result.getResultCode()) {

                            case HttpHelper.SUCCESS:
                                Logger.i("Successfully request updateOnAir");
                                //isSuccessRequestFaceChat = true;
                                //startRTCActivity();
                                //Toast.makeText(getApplicationContext(), "Successfully  date", Toast.LENGTH_LONG).show();
                                break;

                            default:
                                Logger.i("Fail to updateOnAir: " + result.getResultMessage());
                                //Toast.makeText(getApplicationContext(), result.getResultMessage(), Toast.LENGTH_SHORT).show();
                                break;
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Logger.e(error.toString());
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                    }
                });

        if (request != null) {
            addRequest(request);
        }
    }

    public String getGuestID(){
        return this.guestID;
    }
}
