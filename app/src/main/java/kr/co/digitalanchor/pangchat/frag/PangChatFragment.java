package kr.co.digitalanchor.pangchat.frag;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.orhanobut.logger.Logger;
import com.sktelecom.playrtc.exception.RequiredConfigMissingException;
import com.sktelecom.playrtc.exception.RequiredParameterMissingException;
import com.sktelecom.playrtc.exception.UnsupportedPlatformVersionException;
import com.sktelecom.playrtc.util.ui.PlayRTCVideoView;

import org.json.JSONException;
import org.json.JSONObject;

import kr.co.digitalanchor.pangchat.R;
import kr.co.digitalanchor.pangchat.handler.DBHelper;
import kr.co.digitalanchor.pangchat.handler.PlayRTCHandlerFrag;
import kr.co.digitalanchor.pangchat.model.GuestInfo;
import kr.co.digitalanchor.pangchat.model.User;
import kr.co.digitalanchor.pangchat.view.PlayRTCVideoViewGroup;


/*
 * random chatting을 하는 fragment
 */
public class PangChatFragment extends Fragment implements IOnFocusListenable {
    /*
     * PlayRTC-Handler Class
     * PlayRTC 메소드 , PlayRTC객체의 이벤트 처리
     */
    private PlayRTCHandlerFrag playrtcHandlerFrag = null;

    /*
     * PlayRTCVideoView를 위한 부모 뷰 그룹
     */
    public PlayRTCVideoViewGroup videoLayer = null;

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
     * sourceType
     * 0이면 random chatting
     * 1이면 수동 연결
     */
    private int sourceType = 0;
    /*
     * 0이면 내가 거는 것
     * 1이면 상대방으로부터 걸려 온것
     */
    private int whoStart = 0;
    private JSONObject channel = null;
    String mChannelID;
    private GuestInfo mGuest;

    private User mUser;
    private DBHelper mDBhelper;

    private static final String ARG_SECTION_NUMBER = "section_number";

    public PangChatFragment () {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PangChatFragment newInstance(int sectionNumber) {
        PangChatFragment fragment = new PangChatFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public void setSourceType(int type){
        sourceType = type;
    }

    public int getSourceType(){
        return sourceType;
    }

    public void setGuest(GuestInfo info){
        mGuest = info;
    }

    public GuestInfo getGuest(){
        return mGuest;
    }

    public void setWhoStart(int whoStart){
        this.whoStart = whoStart;
    }

    public int getWhoStart(){
        return whoStart;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        //mDBhelper = DBHelper.getInstance(PCApplication.applicationContext);

        //mUser = mDBhelper.getUserInfo();
        playrtcHandlerFrag = new PlayRTCHandlerFrag(this);
        //  PlayRTC 인스턴스를 생성.
        try {
            playrtcHandlerFrag.createPlayRTC(playrtcType);
        } catch (UnsupportedPlatformVersionException e) {
            e.printStackTrace();
        } catch (RequiredParameterMissingException e) {
            e.printStackTrace();
        }

        initUIControls(rootView);

        ViewTreeObserver viewTreeObserver = rootView.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                Logger.i("Height : " + rootView.getHeight() + " Width : " + rootView.getWidth());
                ViewTreeObserver obs = rootView.getViewTreeObserver();
                obs.removeOnGlobalLayoutListener(this);

                if(videoLayer.isInitVideoView() == false) {
                    Logger.i("onWindowFocusChanged fasle");
                    videoLayer.initVideoView();
                }
            }
        });
       /* TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
        textView.setText(getString(R.string.chatting));*/
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

       /* if(videoArea.isInitVideoView() == false){
            videoArea.initVideoView();
        }*/
        mChannelID = "PC_TEST";

        try {
            JSONObject parameter = new JSONObject();
            JSONObject channelParam = new JSONObject();
            channelParam.put("channelName", mChannelID);
            parameter.put("channel", channelParam);

            JSONObject peer = new JSONObject();
            //peer.put("uid", userID);
            //peer.put("userName", userName);
            peer.put("uid", null);
            peer.put("userName", null);
            parameter.put("peer", peer);

            channel = parameter;
        }catch (JSONException e){
            e.printStackTrace();
        }

        try {
            whoStart = 0;
            if (whoStart == 0) {
                Logger.i("createChannel : " + channel.toString());
                getPlayRTCHandler().createChannel(channel);
            } else {
                getPlayRTCHandler().connectChannel(mChannelID, channel);
                Logger.i("ConnectChannel : " + channel.toString());
            }
        }catch (RequiredConfigMissingException e){
            e.printStackTrace();
        }
    }

    private void initUIControls(View rootView) {

		/*video 스트림 출력을 위한 PlayRTCVideoView의 부모 ViewGroup */
        videoLayer = (PlayRTCVideoViewGroup) rootView.findViewById(R.id.videoarea);

		/*  채널 종료 버튼 */
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // Layout XML에 VideoView를 기술한 경우. v2.2.6
        Logger.i("onWindowFocusChanged");
       /* if (hasFocus && videoArea.isInitVideoView() == false) {
            // 4. 영상 스트림 출력을 위한 PlayRTCVideoView 초기화
            Logger.i("onWindowFocusChanged false");
            videoArea.initVideoView();
        }*/
    }

    @Override
    public void onPause() {
        super.onPause();
        if (playrtcHandlerFrag != null) playrtcHandlerFrag.onActivityPause();
        Logger.i("PangChat onPause");
        //getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        //videoArea.initVideoView = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        // 미디어 스트리밍 처리 resume
        if (playrtcHandlerFrag != null) playrtcHandlerFrag.onActivityResume();
        /*if(videoArea.isInitVideoView() == false){
            videoArea.initVideoView();
        }*/
    }

    @Override
    public void onDestroy() {
        // PlayRTC 인스턴스 해제
        if (playrtcHandlerFrag != null) {
            playrtcHandlerFrag.close();
            playrtcHandlerFrag = null;
        }
        // v2.2.6
        if (videoLayer != null) {
            videoLayer.releaseView();
        }
        Logger.i("before remove PangChatFragement");

        super.onDestroy();
    }

    /*
   * PlayRTCHandlerFrag 인스턴스를 반환한다.
   * @return PlayRTCHandlerFrag
   */
    public PlayRTCHandlerFrag getPlayRTCHandler() {
        return playrtcHandlerFrag;
    }

    public PlayRTCVideoViewGroup getVideoLayer() {
        return videoLayer;
    }

    /*
     * 로컬 영상 PlayRTCVideoView 인스턴스를 반환한다.
     * @return PlayRTCVideoView
     */
    public PlayRTCVideoView getLocalVideoView() {
        return videoLayer.getLocalView();
    }

    /*
     * 상대방 영상 PlayRTCVideoView 인스턴스를 반환한다.
     * @return PlayRTCVideoView
     */
    public PlayRTCVideoView getRemoteVideoView() {
        return videoLayer.getRemoteView();
    }

    public void setOnBackPressed(boolean isClose) {
        isCloesActivity = isClose;
        this.onBackPressed();
    }

    /*
    * isCloesActivity가 false이면 Dialog를 통해 사용자의 종료 의사를 확인하고<br>
    * Activity를 종료 처리. 만약 채널에 입장한 상태이면 먼저 채널을 종료한다.
    */
    public void onBackPressed() {

        // Activity를 종료하도록 isCloesActivity가 true로 지정되어 있다면 종료 처리
        if (isCloesActivity) {
            getActivity().finish();
        } else {

            if (playrtcHandlerFrag.isChannelConnected() == true) {
                isCloesActivity = false;
                // PlayRTC 플랫폼 채널을 종료한다.
                playrtcHandlerFrag.disconnectChannel();
            } else {
                isCloesActivity = true;
                onBackPressed();
            }
        }
    }

}
