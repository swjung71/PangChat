package kr.co.digitalanchor.pangchat.view;

import android.content.Context;
import android.util.AttributeSet;

import com.sktelecom.playrtc.util.ui.PlayRTCVideoView;

/**
 * 로컬 영상 출력 뷰 <br>
 * PlayRTCVideoView(SurfaceView)를 상속
 */
public class LocalVideoView extends PlayRTCVideoView {

    public LocalVideoView(Context context) {
        super(context);
        // 레이어 중첩 시 리모트 영상 뷰 위에 출력 되도록 렌더링 우성순위를 높게 지정
        super.setZOrderMediaOverlay(true);
    }

    public LocalVideoView(Context context, AttributeSet attrs) {
        super(context, attrs );
        // 레이어 중첩 시 리모트 영상 뷰 위에 출력 되도록 렌더링 우성순위를 높게 지정
        super.setZOrderMediaOverlay(true);
    }
}
