package kr.co.digitalanchor.pangchat.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import kr.co.digitalanchor.pangchat.PCApplication;
import kr.co.digitalanchor.pangchat.R;
import kr.co.digitalanchor.pangchat.act.VideoChatActivity;
import kr.co.digitalanchor.pangchat.handler.DBHelper;
import kr.co.digitalanchor.pangchat.handler.HttpHelper;
import kr.co.digitalanchor.pangchat.handler.OkHttp3Stack;
import kr.co.digitalanchor.pangchat.handler.VolleySingleton;
import kr.co.digitalanchor.pangchat.model.FriendAdd;
import kr.co.digitalanchor.pangchat.model.PangChatUser;
import kr.co.digitalanchor.pangchat.model.Result;
import kr.co.digitalanchor.pangchat.model.User;
import kr.co.digitalanchor.pangchat.utils.FadeInNetworkImageView;
import kr.co.digitalanchor.pangchat.utils.LruBitmapCache;

/**
 * Created by Peter Jung on 2016-10-05.
 */
public class ProfileDialog extends Dialog  implements View.OnClickListener {

    private ImageButton btnClose;
    private Button btnChat;
    private Button btnAddFriend;
    private TextView textNickName;
    private TextView textID;
    private TextView textSexAge;
    private TextView textLike;
    private TextView textCityJob;
    private TextView textNickName2;
    private TextView textInterestAge;
    private TextView textInterestSex;
    private TextView textInterestSubject;

    private User mUser;
    private Context mContext;
    private PangChatUser mPartner;
    private ImageLoader mImageLoader;
    private RequestQueue mRequestQueue;
    private FadeInNetworkImageView networkImageView;
    private RequestQueue mQueue;
    private DBHelper mDBHelper;

    private int isMe; //0 이면 다른 사람 1이면 자신

    public ProfileDialog(Context context) {
        super(context);
        mContext = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDBHelper = DBHelper.getInstance(mContext);
        mUser = mDBHelper.getUserInfo();
    }

    public void setUser(User user){
        mUser = user;
        Logger.i("userPK " + mUser.getUserPK());
    }

    public void setPangChatUser(PangChatUser partner){
        mPartner = partner;
    }

    public void setIsMe(int isMe) {this.isMe = isMe; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_member_profile);

        initView();
    }

    public void initView(){

        btnClose = (ImageButton)findViewById(R.id.btn_close);
        btnClose.setOnClickListener(this);

        textNickName = (TextView)findViewById(R.id.profile_nick_name);
        //textID = (TextView)findViewById(R.id.profile_id);
        textSexAge = (TextView)findViewById(R.id.profile_sex_age);
        textLike = (TextView)findViewById(R.id.profile_like);
        textCityJob = (TextView)findViewById(R.id.profile_city_job);
        textNickName2 = (TextView)findViewById(R.id.text_nick_name2);
        textInterestAge = (TextView)findViewById(R.id.text_interest_age);
        textInterestSex = (TextView)findViewById(R.id.text_Interest_sex);
        textInterestSubject = (TextView)findViewById(R.id.text_interest_subject);

        btnChat = (Button)findViewById(R.id.btn_one_to_one);
        btnChat.setOnClickListener(this);

        btnAddFriend = (Button)findViewById(R.id.btn_add_friend);
        btnAddFriend.setOnClickListener(this);

        mQueue = VolleySingleton.getmInstance(PCApplication.applicationContext).getmRequestQueue();
        networkImageView = (FadeInNetworkImageView)findViewById(R.id.image_profile);
        mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext(), new OkHttp3Stack());
        mImageLoader = new ImageLoader(mRequestQueue, new LruBitmapCache(LruBitmapCache.getCacheSize(mContext)));
        networkImageView.setImageUrl(HttpHelper.getImageURL(mPartner.getImageURL()), mImageLoader);

        textNickName.setText(mPartner.getAlias());
        //textID = (TextView)findViewById(R.id.profile_id);
        textSexAge.setText((mPartner.getSex() == 0 ? "남" : "여") + "/" +mPartner.getAge() + getContext().getResources().getString(R.string.age_end));
        textLike.setText(mPartner.getNumOfLike() + getContext().getResources().getString(R.string.num_end) + "/" +
                getContext().getResources().getString(R.string.friend) + mPartner.getNumOfFriend() + getContext().getResources().getString(R.string.human_num_end));
        String job[] = getContext().getResources().getStringArray(R.array.jobs);
        textCityJob.setText(mPartner.getCity()+"/" + job[mPartner.getJobID()]);
        textNickName2.setText(mPartner.getAlias());
        String intAge[] = getContext().getResources().getStringArray(R.array.interest_age);
        textInterestAge.setText(intAge[mPartner.getInterestAgeID()]);
        textInterestSex.setText(mPartner.getInterestSexID() == 0 ? "남" : "여");
        String intSubject[] = getContext().getResources().getStringArray(R.array.subject);
        textInterestSubject.setText(intSubject[mPartner.getInterestSubjectID()]);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_close:
                Logger.i("Member profile close");
                dismiss();
                break;
            case R.id.btn_one_to_one:
                alert(mContext.getResources().getString(R.string.alertWaiting));
                //startChat();
                break;
            case R.id.btn_add_friend:
                Logger.i("add friend");
                addFriend();
                break;
            default:
                break;
        }
    }


    private void addFriend(){

        FriendAdd model = new FriendAdd();
        model.setUserID(String.valueOf(mPartner.getUserID()));
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
                                Toast.makeText(mContext, "친구로 등록하였습니다.", Toast.LENGTH_LONG).show();
                                dismiss();
                                break;

                            case HttpHelper.EXIST_FRIEND:
                                Logger.i("Successfully addFriend");
                                Toast.makeText(mContext, "이미 등록된 친구입니다", Toast.LENGTH_LONG).show();
                                dismiss();
                                break;

                            default:
                                Logger.i("Fail to addFriend : " + result.getResultMessage());
                                Toast.makeText(mContext, "친구 등록과정에서 에러가 발생하였습니다.", Toast.LENGTH_LONG).show();
                                dismiss();
                                break;
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Logger.e(error.toString());
                        Toast.makeText(mContext, "친구 등록과정에서 에러가 발생하였습니다.", Toast.LENGTH_LONG).show();
                        dismiss();
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

    //to send YoungChat here
    private JSONObject param = null;
    public void startChat(){

        //delete for free
        /*
        try{
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

            String str1 = mUser.getEndDay();
            Date date1 = formatter.parse(str1);

            Logger.i("end day : " + date1);

            String str2 = formatter.format(new Date());

            Date date2 = formatter.parse(str2);
            Logger.i("end day current: " + date2);

            if (date1.compareTo(date2)<0)
            {
                System.out.println("date2 is Greater than my date1");

                Toast.makeText(mContext, mContext.getResources().getString(R.string.expired), Toast.LENGTH_SHORT).show();
                Intent intentBucket = new Intent();
                intentBucket.setClass(PCApplication.applicationContext, PurchaseActivity.class);
                intentBucket.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mContext.startActivity(intentBucket);
                return;
            }

        }catch (ParseException e1){
            e1.printStackTrace();
        }
        */

        prepareChannel("PC-" + mUser.getUserPK() + "-" + mPartner.getUserID(), ""+mUser.getUserPK(), mUser.getUserAlias());
    }

    public void prepareChannel(String channelName, String userId, String userName) {
        Logger.i("onClickCreateChannel channelName[" + channelName + "] userId[" + userId + "] userName[" + userName + "]");
        // 채널방 정보 생성
        JSONObject parameters = new JSONObject();

        if (TextUtils.isEmpty(channelName) == false) {
            JSONObject channel = new JSONObject();
            try {
                // 채널에 대한 이름을 지정한다.
                channel.put("channelName", channelName);
                parameters.put("channel", channel);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (TextUtils.isEmpty(userId) == false || TextUtils.isEmpty(userName) == false) {
            // 채널 사용자에 대한 정보를 정의한다.
            JSONObject peer = new JSONObject();
            try {
                if (TextUtils.isEmpty(userId) == false) {
                    // application에서 사용하는 사용자 아이디를 지정
                    peer.put("uid", userId);
                }
                if (TextUtils.isEmpty(userName) == false) {
                    // 사용자에 대한 별칭을 지정한다.
                    peer.put("userName", userName);
                }
                parameters.put("peer", peer);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Logger.i("playRTC.onClickCreateChannel " + parameters.toString());
        param = parameters;
        //requestChat();
        startRTCActivity();
        //activity.getPlayRTCHandler().createChannel(parameters);
    }

    private void startRTCActivity(){
        Intent intent = new Intent(mContext, VideoChatActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("partner", mPartner);
        // PlayRTC Sample 유형 전달
        intent.putExtra("isRequester", 1); // create Connection
        intent.putExtra("param", param.toString());
        intent.putExtra("partnerID", ""+mPartner.getUserID());
        intent.putExtra("partnerAlias", mPartner.getAlias());
        intent.putExtra("partnerCity", mPartner.getCity());
        intent.putExtra("partnerImage", mPartner.getImageURL());
        intent.putExtra("channelID", "PC-" +  mUser.getUserPK() + "-" + mPartner.getUserID() );
        Logger.i("putExtra" + param.toString());
        mContext.startActivity(intent);
        Logger.i("star activity" + param.toString());
        dismiss();
    }

    void alert(String message) {

        AlertDialog dialog = new AlertDialog(mContext);
        dialog.setMessage(message);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                startChat();
            }
        });
        dialog.show();
    }
}
