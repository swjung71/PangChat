package kr.co.digitalanchor.pangchat.view;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;

import com.orhanobut.logger.Logger;
import com.sktelecom.playrtc.util.ui.PlayRTCVideoView;

import kr.co.digitalanchor.pangchat.R;

public class PlayRTCVideoViewGroup extends RelativeLayout {

    private static final String LOG_TAG = "VIDEO-VIEW";

     /*
     * 로컬 영상 출력 뷰
     */
    private LocalVideoView localView = null;

    /*
     * 상대방 영상 출력 뷰
     */
    private RemoteVideoView remoteView = null;

    /*
     * Layouy XML에 기술한 영상 출력을 위한 관련 뷰를 초기화 설정을 했는지 여부
     * Activity의 onWindowFocusChanged에서 화면에 보여 질 때(사이즈 확인 가능 시점) 호출(최초 1번) 한다.
     */
    public boolean initVideoView = false;

    private Point screenDimensions = null;

    private RTCViewSizeType localViewSize = RTCViewSizeType.Full;
    public enum RTCViewSizeType {
        Full,
        Small
    };

    public PlayRTCVideoViewGroup(Context context) {
        super(context);
    }

    public PlayRTCVideoViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PlayRTCVideoViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    /*
     * 영상 출력뷰의 내부 렌더링 객체를 해제한다.
     * new v2.2.5
     */
    public void releaseView() {
        if(localView != null) {
            localView.release();
            Logger.i("PlayViewGroup release for local");
        }
        if(remoteView != null) {
            remoteView.release();
            Logger.i("PlayViewGroup release for remote");
        }
        System.gc();
    }

    /*
     * Layouy XML에 기술한 영상 출력을 위한 관련 뷰를 설정한다.
     * PlayRTCVideoViewGroup 크기를 높이 기준으로 4(폭):3(높이)으로 재 조정하고,
     * Remote(RemoteVideoView) 뷰는 PlayRTCVideoViewGroup 크기에 맞게 생성하고
     * Local(LocalVideoView) 뷰는 Remote 뷰 크기의 30%로 좌상단에 생성한다.
     * initVideoView는 Activity의 onWindowFocusChanged에서 화면에 보여 질 때(사이즈 확인 가능 시점) 호출(최초 1번) 한다.
     * new v2.2.5
     * @see com.playrtc.sample.view.LocalVideoView
     * @see com.playrtc.sample.view.RemoteVideoView
     */
    public void initVideoView() {

        Logger.i("initVideoView");
        // 이미 뷰를 초기화 했는지 체크
        if(initVideoView == true) {
            return;
        }
        initVideoView = true;

        // PlayRTCVideoView의 부모 ViewGroup의 사이즈 확인
        screenDimensions = new Point();
        int height = this.getHeight();

        // ViewGroup의 사이즈 재조정, 높이 기준으로 4(폭):3(높이)으로 재 조정
        // 4:3 = width:height ,  width = ( 4 * height) / 3
        float width = (4.0f * height) / 3.0f;

        LayoutParams param = (LayoutParams)this.getLayoutParams();
        param.width = (int)width;
        param.height = (int)height;
        this.setLayoutParams(param);

        screenDimensions.x = param.width;
        screenDimensions.y = param.height;

        initLocalVideo();
        initRemoteVideo();
    }

    /*
     * Layouy XML에 기술한 영상 출력을 위한 관련 뷰를 초기화 설정을 했는지 여부를 반환한다.
     * Activity의 onWindowFocusChanged에서 화면에 보여 질 때 최초 1번 initVideoView()를 호출하기 위해서 임.
     * @return boolean
     *
     */
    public boolean isInitVideoView () {
        return initVideoView;
    }

    /*
     * 영상 뷰를 생성했는지 여부를 반환한다.
     * 영상 뷰 샹성 여부를 체크하는 이유는 Sample App에서 영상뷰 생성 시 사이즈 지정을 위해
     * Activity의 onWindowFocusChanged에서 화면에 보여 질 때 최초 1번 createVideoView()를 호출하기 위해서 임.
     * @return boolean
     *
     */
    public boolean isCreatedVideoView() {
        if(localView == null && remoteView == null)
        {
            return false;
        }
        return true;
    }

    /*
     * Local 영상 뷰를 반환한다.
     * @return LocalVideoView
     */
    public LocalVideoView getLocalView() {
        return localView;
    }

    /*
     * Remote 영상 뷰를 반환한다.
     * @return RemoteVideoView
     */
    public RemoteVideoView getRemoteView() {
        return remoteView;
    }


    public void resizeLocalVideoView(RTCViewSizeType size) {

        Logger.i("start resize");
        if(size == localViewSize) {
            return;
        }
        localViewSize = size;

        LayoutParams lp;
        if(size == RTCViewSizeType.Full) {
            lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            lp.setMargins(0,0,0,0);
        }
        else {
            // 세로 모드임, 해상도는 가로 기준
            int smallWidth = (int)(screenDimensions.y*0.2);
            int samllHeight = (int)(screenDimensions.x*0.2);

            Logger.i("small width : " + smallWidth);
            Logger.i("small height : " + samllHeight);

            lp = new LayoutParams(smallWidth, samllHeight);
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);

            lp.setMargins(30,30, 30, 30);
            Logger.i("resized : " + lp.toString());
            /*localView.setLayoutParams( lp );
            localView.requestLayout();*/
        }

        localView.setLayoutParams( lp );
        //localView.requestLayout();
    }

    /*
     * Layouy XML에 기술한 Local 영상 뷰를 설정한다.
     * v2.2.6
     */
    private void initLocalVideo() {


        // 부모 View의 30% 비율로 크기를 지정한다
        int width = (int)(screenDimensions.x*0.3);
        int height = (int)(screenDimensions.y*0.3);

        localView = (LocalVideoView)findViewById(R.id.local_video);

        /*
         * 거울 모드를 지정한다.
         * @param mirror boolean, 영상 출력을 거울 모드로 지정한다.
         *        true로 지정하면 거울로 보는것처럼 오른쪽이 화면의 오른쪽으로 출력된다.
         *        주로 로컬 영상(전방 카메라 사용)의 경우 거울 모드로 지정한다.
         */

        localView.setMirror(true);
        /*RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)localView.getLayoutParams();
        params.width = width;
        params.height = height;
        localView.setLayoutParams(params);*/
        localView.setBgClearColor(200, 200, 200, 255);
        localView.setVideoFrameObserver(new PlayRTCVideoView.VideoRendererObserver(){
            @Override
            public	void onFrameResolutionChanged(PlayRTCVideoView view, int videoWidth, int videoHeight, int rotationDegree) {
                Log.i(LOG_TAG, "Local FrameResolution videoWidth[" + videoWidth + "] videoHeight["+videoHeight+"] rotationDegree["+rotationDegree+"]");
            }

            @Override
            public void onFirstFrameRendered() {
                Log.i(LOG_TAG, "Local onFirstFrameRendered....");
            }
        });

        // v2.2.6
        localView.initRenderer();
        Logger.i("init local view");
        //localView.requestLayout();

    }

    /*
     * Layouy XML에 기술한 Remote 영상 뷰를 설정한다.
     * v2.2.6
     */
    private void initRemoteVideo() {

        remoteView = (RemoteVideoView)findViewById(R.id.remote_video);
        /*
         * 거울 모드를 지정한다.
         * @param mirror boolean, 영상 출력을 거울 모드로 지정한다.<br>
         *        true로 지정하면 거울로 보는것처럼 오른쪽이 화면의 오른쪽으로 출력된다.<br>
         *        주로 로컬 영상(전방 카메라 사용)의 경우 거울 모드로 지정한다.
         */
        remoteView.setMirror(false);
        remoteView.setBgClearColor(127, 127, 127, 255);
        remoteView.setVideoFrameObserver(new PlayRTCVideoView.VideoRendererObserver(){
            @Override
            public	void onFrameResolutionChanged(PlayRTCVideoView view, int videoWidth, int videoHeight, int rotationDegree) {
                Log.i(LOG_TAG, "Remote FrameResolution videoWidth[" + videoWidth + "] videoHeight["+videoHeight+"] rotationDegree["+rotationDegree+"]");
            }

            @Override
            public void onFirstFrameRendered() {
                Log.i(LOG_TAG, "Remote onFirstFrameRendered....");
                localView.post(new Runnable(){
                    @Override
                    public void run() {
                        resizeLocalVideoView(RTCViewSizeType.Small);
                    }
                });
            }
        });

        // v2.2.6
        remoteView.initRenderer();
        Logger.i("init remote view");
        //remoteView.requestLayout();
    }
}
