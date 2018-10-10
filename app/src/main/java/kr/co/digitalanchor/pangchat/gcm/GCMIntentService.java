package kr.co.digitalanchor.pangchat.gcm;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import kr.co.digitalanchor.pangchat.PCApplication;
import kr.co.digitalanchor.pangchat.act.IntroActivity;
import kr.co.digitalanchor.pangchat.act.WaitingActivity;
import kr.co.digitalanchor.pangchat.handler.DBHelper;
import kr.co.digitalanchor.pangchat.handler.HttpHelper;
import kr.co.digitalanchor.pangchat.handler.VolleySingleton;
import kr.co.digitalanchor.pangchat.model.GuestInfo;
import kr.co.digitalanchor.pangchat.model.GuestInfoRequest;
import kr.co.digitalanchor.pangchat.model.GuestInfoResult;
import kr.co.digitalanchor.pangchat.utils.AndroidUtils;


public class GCMIntentService extends IntentService {

    private DBHelper mDBHelper;
    private RequestQueue mQueue;
    public static int count = 0;

    public GCMIntentService() {
        super("GCMIntentService");
        mDBHelper = DBHelper.getInstance(PCApplication.applicationContext);
        mQueue = VolleySingleton.getmInstance(PCApplication.applicationContext).getmRequestQueue();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);

        Logger.i("GCM onHandelIntent");
        if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)
                || GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
            GCMBroadcastReceiver.completeWakefulIntent(intent);
            return;
        }

        Bundle bundle = intent.getExtras();

        if (bundle.isEmpty()) {
            GCMBroadcastReceiver.completeWakefulIntent(intent);
            return;
        }

        if (!GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
            // Release the wake lock provided by the WakefulBroadcastReceiver.
            GCMBroadcastReceiver.completeWakefulIntent(intent);
            return;
        }

        String code = bundle.getString("code");

        if (TextUtils.isEmpty(code)) {
            return;
        }


        AndroidUtils.acquireCpuWakeLock(this.getApplicationContext());

        int type = 0;
        Intent intent1 = null;
        switch (code) {

            case "PangChat":
                Logger.i("code is PangChat");
                String peerAlias = bundle.getString("partnerAlias");
                String peerCity = bundle.getString("partnerCity");
                String imagePath = bundle.getString("partnerImage");
                String peerID = bundle.getString("partnerID");
                String channelID = bundle.getString("channelID").trim();
                onClickConnectChannel(channelID, peerID, peerAlias, imagePath, peerCity);

                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone r = RingtoneManager.getRingtone(PCApplication.applicationContext, notification);
                r.play();

                Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibe.vibrate(5000);
               /* AndroidUtils.showNotificationNotice(PCApplication.applicationContext, PCApplication.applicationContext.getResources().getString(R.string.pangchat_request),
                        peerAlias + PCApplication.applicationContext.getResources().getString(R.string.pangchat_request_msg), getNormalStart(bundle));*/
                AndroidUtils.acquireCpuWakeLock(PCApplication.applicationContext);
                break;

            case "PangChatCancel":
                Logger.i("code is cancel FaceChat");
                String peerAlias1 = bundle.getString("partnerAlias");
                PCApplication.stopWaitingActivity(peerAlias1);
                break;

            case "PangChatReject":
                Logger.i("code is cancel FaceChat");
                String peerAlias3 = bundle.getString("partnerAlias");
                PCApplication.stopVideoChatActivity(peerAlias3);
                break;

            case "Notice":
                Logger.i("notice : " + bundle.getString("msg"));
                AndroidUtils.showNotificationNotice(PCApplication.applicationContext, "공지사항",
                        bundle.getString("msg"), getNormalStart(bundle));

                //화면을 깨우는 것
                AndroidUtils.acquireCpuWakeLock(PCApplication.applicationContext);
                break;

            default:
                break;
        }

        GCMBroadcastReceiver.completeWakefulIntent(intent);
    }

    /*
    * 채널 입장 버튼을 눌렀을 때
    *
    * @param channelId String, 입장할 채널의 아이디
    * @param userId    String, 사용자 Application 사용자 아이디
    * @param userName  String, 사용자 이름
    */
    public void onClickConnectChannel(String channelId, String userId, String userName, String imagePath, String city) {
        Logger.i("onConnectChannel channelId[" + channelId + "] userId[" + userId + "] userName[" + userName + "]" + "imagePath [" + imagePath + "]" + "city [" + city+ "]");

        // 채널방 정보 생성
        JSONObject parameters = new JSONObject();

        if (TextUtils.isEmpty(channelId) == false) {
            // 채널정보를 정의한다.
            JSONObject channel = new JSONObject();
            try {
                // 채널에 대한 이름을 지정한다.
                channel.put("channelName", channelId);
                parameters.put("channel", channel);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (TextUtils.isEmpty(userId) == false || TextUtils.isEmpty(userName) == false) {

            JSONObject peer = new JSONObject();
            try {
                if (TextUtils.isEmpty(userId) == false) {
                    peer.put("uid", userId);
                }
                if (TextUtils.isEmpty(userName) == false) {
                    peer.put("userName", userName);
                }

                parameters.put("peer", peer);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Logger.i("onConnectChannel call playRTC.createChannel(" + channelId + ", parameters)" + parameters.toString());
        Intent intent = new Intent();
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setClass(getApplicationContext(), WaitingActivity.class);
        intent.putExtra("param", parameters.toString());
        intent.putExtra("partnerID", userId);
        intent.putExtra("partnerAlias", userName);
        intent.putExtra("partnerCity", city);
        intent.putExtra("partnerImage", imagePath);
        intent.putExtra("channelID", channelId);

        Logger.i("before Activity PLayRTCActivity : ");

        //바로 화면으로 가기 위해서 생략
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        try {
            pendingIntent.send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
        Logger.i("start Activity WaitingActivity");
    }

   /* private PendingIntent getIntentNewMessage(int type, String senderId, String senderName) {

        TaskStackBuilder stackBuilder = null;
        Intent chat = null;

        //chat에 들어가기 화면
        //Intent chatIntro = new Intent(YCApplication.applicationContext, ListChildActivity.class);

        //Intent controlChild = new Intent(STApplication.applicationContext, ControlChildExActivity.class);

        //controlChild.putExtra("ChildID", senderId);

        chat = new Intent(YCApplication.applicationContext, ChatActivity.class);
        chat.putExtra(ChatActivity.GUEST_ID, senderId);
        chat.putExtra(ChatActivity.GUEST_ALIAS, senderName);

        stackBuilder = TaskStackBuilder.create(YCApplication.applicationContext);

        //stackBuilder.addNextIntent(childrenList);
        //stackBuilder.addNextIntent(controlChild);
        stackBuilder.addNextIntent(chat);

        return stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
    }*/


    //화면 실행
    private PendingIntent getNormalStart(Bundle bundle) {

        PendingIntent pIntent = null;

        Intent intent = null;

        intent = new Intent(PCApplication.applicationContext, IntroActivity.class);

        if (bundle != null) {

            intent.putExtras(bundle);
        }

        pIntent = PendingIntent.getActivity(PCApplication.applicationContext, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        return pIntent;


    }

    //화면 실행
    /*private PendingIntent getNormalStart(int type, Bundle bundle) {

        PendingIntent pIntent = null;

        Intent intent = null;

        intent = new Intent(PCApplication.applicationContext, YoungChatActivity.class);

        switch (type) {
            case 1://insert Youngchat user

                break;
            case 2: // delete Youngchat user

                if (bundle != null) {
                    intent.putExtras(bundle);
                }
                intent.putExtra("TYPE", 2);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                break;
            default:
                break;
        }

        pIntent = PendingIntent.getActivity(YCApplication.applicationContext, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        return pIntent;
    }*/



    public void requestGuestInfo(int senderID) {

        final int guestID = senderID;
        GuestInfoRequest model = new GuestInfoRequest();
        model.setGuestID(guestID);

        final Gson gson = new Gson();

        Logger.i("guestInof gson : " + gson.toJson(model));
        JsonObjectRequest request = HttpHelper.getGuestInfo(model, "getGuestInfo",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        GuestInfoResult result = gson.fromJson(response.toString(), GuestInfoResult.class);
                        switch (result.getResultCode()) {

                            case HttpHelper.SUCCESS:
                                Logger.i("receive GuestInfo ");
                                GuestInfo receivedGuest = result.getGuestInfos().get(0);
                                GuestInfo guest = mDBHelper.getGuest("" + guestID);

                                Logger.i("GuestInfo time : " + receivedGuest.getLastTime());
                                if (guest != null) {
                                    mDBHelper.updateGuest("" + receivedGuest.getGuestID(), receivedGuest.getGuestAlias(),
                                            receivedGuest.getAge(), receivedGuest.getImagePath(), receivedGuest.getLongitude(),
                                            receivedGuest.getLatitude(), receivedGuest.getLastTime());
                                } else {
                                    mDBHelper.insertGuest("" + receivedGuest.getGuestID(), receivedGuest.getGuestAlias(),
                                            receivedGuest.getSex(), receivedGuest.getAge(), receivedGuest.getImagePath(),
                                            receivedGuest.getLongitude(), receivedGuest.getLatitude(), receivedGuest.getLastTime());
                                }
                                //Toast.makeText(getApplicationContext(), "Successfully  date", Toast.LENGTH_LONG).show();
                                break;

                            default:
                                Logger.i("Fail to receive GuestInfo: " + result.getResultMessage());
                                break;
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Logger.e(error.toString());
                    }
                });

        if (request != null) {
            addRequest(request);
        }
    }

    protected void addRequest(JsonObjectRequest request) {
        try {
            mQueue.add(request);
        } catch (Exception e) {
            e.printStackTrace();
            Logger.e(e.getMessage());
        }
    }
}
