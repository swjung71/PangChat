package kr.co.digitalanchor.pangchat.act;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.TransitionDrawable;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import kr.co.digitalanchor.pangchat.BaseActivity;
import kr.co.digitalanchor.pangchat.PCApplication;
import kr.co.digitalanchor.pangchat.R;
import kr.co.digitalanchor.pangchat.dialog.AlertDialog;
import kr.co.digitalanchor.pangchat.dialog.CallDialog;
import kr.co.digitalanchor.pangchat.dialog.CityDialog;
import kr.co.digitalanchor.pangchat.dialog.MainDialog;
import kr.co.digitalanchor.pangchat.dialog.ManWomanDialog;
import kr.co.digitalanchor.pangchat.dialog.SearchDialog;
import kr.co.digitalanchor.pangchat.frag.FriendFragment;
import kr.co.digitalanchor.pangchat.frag.MatchingFragment;
import kr.co.digitalanchor.pangchat.frag.MemberFragment;
import kr.co.digitalanchor.pangchat.handler.DBHelper;
import kr.co.digitalanchor.pangchat.handler.HttpHelper;
import kr.co.digitalanchor.pangchat.handler.OnSwipeTouchListener;
import kr.co.digitalanchor.pangchat.model.FriendAdd;
import kr.co.digitalanchor.pangchat.model.OnAirClass;
import kr.co.digitalanchor.pangchat.model.PangChatUser;
import kr.co.digitalanchor.pangchat.model.RequestRandom;
import kr.co.digitalanchor.pangchat.model.Result;
import kr.co.digitalanchor.pangchat.model.ResultRandomChat;
import kr.co.digitalanchor.pangchat.model.User;
import kr.co.digitalanchor.pangchat.purchase.HomeWatcher;
import kr.co.digitalanchor.pangchat.purchase.OnHomePressedListener;
import kr.co.digitalanchor.pangchat.purchase.PurchaseActivity;
import kr.co.digitalanchor.pangchat.view.CameraPreview;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private User mUser;
    private DBHelper mDBhelper;

    private Button btnMember;
    private Button btnMatching;
    private Button btnFriend;
    private Button btnBucket;
    //private Button btnLike;
    private Button btnBack;
    private ImageButton btnMenu;
    private Button btnManWoman;
    private Button btnCity;
    private Button btnManWoman2;
    private Button btnCity2;
    private Button btnSearch;
    private Button btnCall;

    private ImageView iconText;

    private LinearLayout headerLayout;
    private LinearLayout menuLayout;
    private FrameLayout mainArea;
    public FrameLayout videoArea;

    private MemberFragment memberFragment;
    private MatchingFragment matchingFragment;
    private FriendFragment friendFragment;
    private PangChatUser mPartner;
    private int state = 0;
    private int tmpState = 0;

    private int city = -1;
    private int sex = -1;

    private final static int CITY_REQUEST = 10000;
    private final static int MAN_WOMAN_REQUEST = 10001;
    private final static int SEARCH_REQUEST = 10002;
    private final static int CITY_REQUEST2 = 10003;
    private final static int MAN_WOMAN_REQUEST2 = 10004;

    private boolean isClose = false;

    //video
    private Camera mCamera;
    private CameraPreview mPreview;
    private FrameLayout preview;

    private OnSetMemberState memberStateCallBack;
    private OnSearchMember onSearchMemberCallBack;

    private boolean isUserHint = true;

    private HomeWatcher mHomeWatcher;

    private boolean isOnSaved = false;

    public boolean isRelease = false;

    /*public static boolean isPurchase = false;
    public static boolean isVideo = false;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDBhelper = DBHelper.getInstance(this);
        mUser = mDBhelper.getUserInfo();
        //mDBhelper = DBHelper.getInstance(PCApplication.applicationContext);
        //mUser = mDBhelper.getUserInfo();
        initUIControls();

        createVideoPreview();

        mHomeWatcher = new HomeWatcher(this);

        mHomeWatcher.setOnHomePressedListener(new OnHomePressedListener() {
            @Override
            public void onHomePressed() {
                mHomeWatcher.stopWatch();
                updateOnAir(1);
            }

            @Override
            public void onHomeLongPressed() {
            }
        });
    }

 /*   @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        updateOnAir(1);
    }*/

    private void initUIControls() {


        btnMember = (Button) findViewById(R.id.btn_member);
        btnMember.setOnClickListener(this);
        btnMatching = (Button) findViewById(R.id.btn_matching);
        btnMatching.setOnClickListener(this);
        btnFriend = (Button) findViewById(R.id.btn_friend);
        btnFriend.setOnClickListener(this);

        //btnLike = (Button) findViewById(R.id.btn_like);
        //btnLike.setOnClickListener(this);
        btnBucket = (Button) findViewById(R.id.btn_bucket);
        btnBucket.setOnClickListener(this);

        btnMenu = (ImageButton) findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(this);

        btnBack = (Button) findViewById(R.id.btn_back);
        btnBack.setOnClickListener(this);

        btnManWoman = (Button) findViewById(R.id.btn_man_woman);
        btnManWoman.setOnClickListener(this);

        btnCity = (Button) findViewById(R.id.btn_city);
        btnCity.setOnClickListener(this);

        btnManWoman2 = (Button) findViewById(R.id.btn_man_woman2);
        btnManWoman2.setOnClickListener(this);

        btnCity2 = (Button) findViewById(R.id.btn_city2);
        btnCity2.setOnClickListener(this);

        btnSearch = (Button) findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(this);

        btnCall = (Button) findViewById(R.id.btn_call);
        btnCall.setOnClickListener(this);

        headerLayout = (LinearLayout) findViewById(R.id.headerLayout);
        menuLayout = (LinearLayout) findViewById(R.id.menuLayout);
        mainArea = (FrameLayout) findViewById(R.id.mainArea);
        videoArea = (FrameLayout) findViewById(R.id.videoLayout);
        videoArea.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()) {

            public void onSwipeRight() {
                alert(getResources().getString(R.string.alertWaiting));
                //requestRandomChat();
            }

            @Override
            public void onSwipeTop() {

            }
        });

        iconText = (ImageView) findViewById(R.id.iconText);

        //createVideoPreview();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (android.os.Build.VERSION.SDK_INT >= 23) {

            //영상통화가 시작되었으면
            if (PCApplication.getIsVideo()) {
                if (!isRelease) {
                    Logger.i("Main : Resume : isRelease");
                    createVideoPreview();
                    PCApplication.setIsVideo(false);
                }else {
                    isRelease = false;
                }
                //Waiting으로 부터 왔다면
                if(PCApplication.getIsWaiting()){
                    Logger.i("Main : Resume : Video from Waiting");
                    PCApplication.setIsWaiting(false);
                }
                //Waiting만 했다면
            } else if (PCApplication.getIsWaiting()) {
                Logger.i("Main : Resume : only Waiting");
                createVideoPreview();
                PCApplication.setIsWaiting(false);
            }

             /*if (PCApplication.getIsCall() || PCApplication.getIsWaiting()) {
                Logger.i("Main isCall");
                if(!isRelease) {
                    createVideoPreview();
                    isRelease =false;
                }
                PCApplication.setIsCall(false);
                PCApplication.setIsWaiting(false);
            }*/

        } else {
            createVideoPreview();
        }
        isOnSaved = false;
        isClose = false;

        updateOnAir(0);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
       /* if(mCamera != null){
            mCamera.release();
        }*/

        mHomeWatcher.stopWatch();
    }

    @Override
    protected void onStart() {
        super.onStart();

        Logger.i("Main : onStart");
        Logger.i("Main : PCApplication.isCall " + PCApplication.getIsCall());
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            Logger.i("BuildVersion : " + Build.VERSION.SDK_INT);

            if (PCApplication.getIsCall()) {
                Logger.i("Main : isCall");
                createVideoPreview();
                PCApplication.setIsCall(false);
            }
            /*
            if (PCApplication.getIsCall() || PCApplication.getIsWaiting()) {
                Logger.i("Main isCall");


                if(!isRelease) {
                    createVideoPreview();
                    isRelease =false;
                }
                PCApplication.setIsCall(false);

            }*/


        }
        mHomeWatcher.startWatch();
    }

    @Override
    protected void onDestroy() {
        Logger.i("Main onDestroy ");
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        isOnSaved = true;
    }

    @Override
    public void onBackPressed() {
        Logger.i("onBackPressed");
        if (isClose) {
            Logger.i("onBackPressed isClose");
            if (!isOnSaved) {
                super.onBackPressed();
            }
        } else {
            Logger.i("onBackPressed isNotClose");
            //updateOnAir(1);
            createCloseAlertDialog();
            //closeAlertDialog.show();
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_member:
                showMain(1);
                break;
            case R.id.btn_matching:
                showMain(2);
                break;
            case R.id.btn_friend:
                showMain(3);
                break;
            case R.id.btn_back:
                showVideo();
                if (android.os.Build.VERSION.SDK_INT >= 23) {
                    //refreshVideoPreview();
                    //releaseCamera();
                    Logger.i("Main : btn_back");
                    Logger.i("Main : back : PCApplication.Video " + PCApplication.getIsVideo());
                    //createVideoPreview2();
                    if (PCApplication.getIsVideo()) {
                        Logger.i("Main : back : isVideo");
                        createVideoPreview2();
                        PCApplication.setIsVideo(false);

                        if(PCApplication.getIsWaiting()) {
                            Logger.i("Main : back : Video from Waiting");
                            PCApplication.setIsWaiting(false);
                        }
                    }else if(PCApplication.getIsWaiting()){
                        Logger.i("Main : back : isWaiting");
                        PCApplication.setIsWaiting(false);
                    }
                } else {
                    createVideoPreview();
                }
                break;
            case R.id.btn_like:
                break;
            case R.id.btnMenu:
                MainDialog mainDialog = new MainDialog(MainActivity.this);
                mainDialog.show();
                break;

            case R.id.btn_man_woman:
                Intent intentSex = new Intent();
                intentSex.setClass(this, ManWomanDialog.class);
                startActivityForResult(intentSex, MAN_WOMAN_REQUEST);
                break;
            case R.id.btn_man_woman2:
                Intent intentSex2 = new Intent();
                intentSex2.setClass(this, ManWomanDialog.class);
                startActivityForResult(intentSex2, MAN_WOMAN_REQUEST2);
                break;

            case R.id.btn_city:
                Intent intentCity = new Intent();
                intentCity.setClass(this, CityDialog.class);
                startActivityForResult(intentCity, CITY_REQUEST);
                break;
            case R.id.btn_city2:
                Intent intentCity2 = new Intent();
                intentCity2.setClass(this, CityDialog.class);
                startActivityForResult(intentCity2, CITY_REQUEST2);
                break;

            case R.id.btn_search:
                Intent intentSearch = new Intent();
                intentSearch.setClass(this, SearchDialog.class);
                startActivityForResult(intentSearch, SEARCH_REQUEST);
                break;

            case R.id.btn_bucket:
                //isPurchase = true;
                Intent intentBucket = new Intent();
                intentBucket.setClass(this, PurchaseActivity.class);
                startActivity(intentBucket);
                break;
            case R.id.btn_call:
                CallDialog dialog = new CallDialog(this);

                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialog.show();
                dialog.getWindow().setAttributes(lp);
                break;

            default:
                break;
        }
    }

    public void showVideo() {

        Logger.i("start showVideo");
        videoArea.setVisibility(View.VISIBLE);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        tmpState = state;
        state = 0;
        switch (tmpState) {
            case 0:
                break;
            case 1:
                ft.remove(memberFragment);
                ft.commit();
                mainArea.setVisibility(View.GONE);
                break;
            case 2:
                ft.remove(matchingFragment);
                ft.commit();
                mainArea.setVisibility(View.GONE);
                break;
            case 3:
                ft.remove(friendFragment);
                ft.commit();
                mainArea.setVisibility(View.GONE);
                break;
        }
        changeTopForVideo();
    }

    /**
     * @param value 1: 맴버, 2 매칭 3 친구
     */
    private void showMain(int value) {

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        mainArea.setVisibility(View.VISIBLE);
        tmpState = state;
        switch (value) {
            case 1:
                state = 1;
                switch (tmpState) {
                    case 0:
                        videoArea.setVisibility(View.GONE);
                        memberFragment = new MemberFragment();
                        memberStateCallBack = (OnSetMemberState) memberFragment;
                        onSearchMemberCallBack = (OnSearchMember) memberFragment;
                        ft.add(R.id.mainArea, memberFragment, "member");
                        ft.commit();
                        break;
                    case 1:
                        break;
                    case 2:
                        memberFragment = new MemberFragment();
                        ft.replace(R.id.mainArea, memberFragment, "member");
                        ft.commit();
                        break;
                    case 3:
                        memberFragment = new MemberFragment();
                        ft.replace(R.id.mainArea, memberFragment, "member");
                        ft.commit();
                        break;
                    default:
                        break;
                }
                break;
            case 2:
                state = 2;
                switch (tmpState) {
                    case 0:
                        videoArea.setVisibility(View.GONE);
                        matchingFragment = new MatchingFragment();
                        ft.add(R.id.mainArea, matchingFragment, "matching");
                        ft.commit();
                        break;
                    case 1:
                        matchingFragment = new MatchingFragment();
                        ft.replace(R.id.mainArea, matchingFragment, "matching");
                        ft.commit();
                        break;
                    case 2:
                        break;
                    case 3:
                        matchingFragment = new MatchingFragment();
                        ft.replace(R.id.mainArea, matchingFragment, "matching");
                        ft.commit();
                        break;
                    default:
                        break;
                }
                break;
            case 3:
                state = 3;
                switch (tmpState) {
                    case 0:
                        videoArea.setVisibility(View.GONE);
                        friendFragment = new FriendFragment();
                        ft.add(R.id.mainArea, friendFragment, "friend");
                        ft.commit();
                        break;
                    case 1:
                        friendFragment = new FriendFragment();
                        ft.replace(R.id.mainArea, friendFragment, "friend");
                        ft.commit();
                        break;
                    case 2:
                        friendFragment = new FriendFragment();
                        ft.replace(R.id.mainArea, friendFragment, "friend");
                        ft.commit();
                        break;
                    case 3:
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
        mainArea.setVisibility(View.VISIBLE);
        changeTopForOther(value);
    }

    private void changeToMember() {

    }

    private void changeTopForVideo() {
        btnBack.setVisibility(View.GONE);
        iconText.setVisibility(View.VISIBLE);
        //btnBucket.setVisibility(View.VISIBLE);
        //btnLike.setVisibility(View.VISIBLE);
        btnMember.setSelected(false);
        btnMatching.setSelected(false);
        btnFriend.setSelected(false);
        btnManWoman.setVisibility(View.GONE);
        btnCity.setVisibility(View.GONE);
        btnSearch.setVisibility(View.GONE);
        btnMember.setTextColor(getResources().getColor(R.color.white));
        btnMatching.setTextColor(getResources().getColor(R.color.white));
        btnFriend.setTextColor(getResources().getColor(R.color.white));
        btnCall.setTextColor(getResources().getColor(R.color.white));
        if (tmpState != 0) {
            TransitionDrawable transitionDrawable = (TransitionDrawable) headerLayout.getBackground();
            transitionDrawable.reverseTransition(500);
            TransitionDrawable transitionDrawable1 = (TransitionDrawable) menuLayout.getBackground();
            transitionDrawable1.reverseTransition(500);
        }
    }

    private void changeTopForOther(int state) {

        btnBack.setVisibility(View.VISIBLE);
        iconText.setVisibility(View.GONE);
        btnBucket.setVisibility(View.GONE);
        //btnLike.setVisibility(View.GONE);

        if (state == 1) {
            btnManWoman.setVisibility(View.VISIBLE);
            btnCity.setVisibility(View.VISIBLE);
            btnSearch.setVisibility(View.VISIBLE);
            btnMember.setSelected(true);
            btnMatching.setSelected(false);
            btnFriend.setSelected(false);
            btnMember.setTextColor(getResources().getColor(R.color.text_bold));
            btnMatching.setTextColor(getResources().getColor(R.color.button_text_color));
            btnFriend.setTextColor(getResources().getColor(R.color.button_text_color));
            btnCall.setTextColor(getResources().getColor(R.color.button_text_color));
        } else if (state == 2) {
            btnManWoman.setVisibility(View.GONE);
            btnCity.setVisibility(View.GONE);
            btnSearch.setVisibility(View.GONE);
            btnMember.setSelected(false);
            btnMatching.setSelected(true);
            btnFriend.setSelected(false);
            btnMatching.setTextColor(getResources().getColor(R.color.text_bold));
            btnMember.setTextColor(getResources().getColor(R.color.button_text_color));
            btnFriend.setTextColor(getResources().getColor(R.color.button_text_color));
            btnCall.setTextColor(getResources().getColor(R.color.button_text_color));
        } else {
            btnManWoman.setVisibility(View.GONE);
            btnCity.setVisibility(View.GONE);
            btnSearch.setVisibility(View.GONE);
            btnMember.setSelected(false);
            btnMatching.setSelected(false);
            btnFriend.setSelected(true);
            btnFriend.setTextColor(getResources().getColor(R.color.text_bold));
            btnMatching.setTextColor(getResources().getColor(R.color.button_text_color));
            btnMember.setTextColor(getResources().getColor(R.color.button_text_color));
            btnCall.setTextColor(getResources().getColor(R.color.button_text_color));
        }
        if (tmpState == 0) {
            TransitionDrawable transitionDrawable = (TransitionDrawable) headerLayout.getBackground();
            transitionDrawable.startTransition(500);
            TransitionDrawable transitionDrawable1 = (TransitionDrawable) menuLayout.getBackground();
            transitionDrawable1.startTransition(500);
        }
    }

    /**
     * A safe way to get an instance of the Camera object.
     */
    public Camera getCameraInstance() {

        int cameraCount = 0;
        Camera cam = null;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();
        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            Camera.getCameraInfo(camIdx, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                try {
                    cam = Camera.open(camIdx);
                    cam.setDisplayOrientation(90);
                } catch (RuntimeException e) {
                    Logger.e("Camera failed to open: " + e.getLocalizedMessage());
                    //e.printStackTrace();
                }
            }
        }
        return cam; // returns null if camera is unavailable
    }

    public void createVideoPreview() {
        Logger.i("createVideoPreview");
        // Create an instance of Camera
        mCamera = getCameraInstance();
        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        preview = (FrameLayout) findViewById(R.id.videoLayout);

        preview.addView(mPreview);
    }

    public void createVideoPreview2() {
        Logger.i("createVideoPreview2");
        // Create an instance of Camera

        mCamera = getCameraInstance();
        mPreview = new CameraPreview(this, mCamera);
        preview = (FrameLayout) findViewById(R.id.videoLayout);
        preview.removeAllViews();
        preview.addView(mPreview);
        /*
        if(mCamera == null) {
            mCamera = getCameraInstance();
            if(mPreview == null) {
                mPreview = new CameraPreview(this, mCamera);
                preview = (FrameLayout) findViewById(R.id.videoLayout);
                preview.removeAllViews();
                preview.addView(mPreview);
            }
        }*/
        // Create our Preview view and set it as the content of our activity.


    }

    public void refreshVideoPreview() {
        Logger.i("refresh Preview");
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.videoLayout);
        preview.addView(mPreview);
    }

    public void releaseCamera() {
        if (mCamera != null) {
            Logger.i("releaseCamera");
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }


    public interface OnSetMemberState {
        public void onMemberStateSelected(int city, int sex);
    }

    public interface OnSearchMember {
        public void onMemberSearch(String alias);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Logger.i("onActivityResult : requestCode : " + resultCode + " resultCode : " + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CITY_REQUEST:
                if (resultCode == RESULT_OK) {
                    city = data.getIntExtra("city", -1);
                    memberStateCallBack.onMemberStateSelected(city, sex);
                }
                break;
            case MAN_WOMAN_REQUEST:
                if (resultCode == RESULT_OK) {
                    sex = data.getIntExtra("sex", -1);
                    memberStateCallBack.onMemberStateSelected(city, sex);
                }
                break;
            case CITY_REQUEST2:
                if (resultCode == RESULT_OK) {
                    city = data.getIntExtra("city", -1);
                }
                break;
            case MAN_WOMAN_REQUEST2:
                if (resultCode == RESULT_OK) {
                    sex = data.getIntExtra("sex", -1);
                }
                break;
            case SEARCH_REQUEST:
                Logger.i("SEARCH_REQUEST");
                if (resultCode == RESULT_OK) {
                    Logger.i("RESULT_OK");
                    onSearchMemberCallBack.onMemberSearch(data.getStringExtra("alias"));
                }
                break;
            default:
                break;
        }
    }

    public void requestRandomChat() {

        //delete for free

       /* try{
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
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.expired), Toast.LENGTH_SHORT).show();

                Intent intentBucket = new Intent();
                intentBucket.setClass(this, PurchaseActivity.class);
                startActivity(intentBucket);

                *//*final Intent intent = new Intent();
                intent.setClass(this, PurchaseActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);*//*
                return;
            }

        }catch (ParseException e1){
            e1.printStackTrace();
        }*/

        //등록되지 않은 사용자
        if (mUser == null || mUser.getUserAlias() == null || TextUtils.isEmpty(mUser.getUserAlias())) {
            Logger.i("GCM userAlias is empty");
            return;
        }

        RequestRandom model = new RequestRandom();
        model.setUserID(mUser.getUserPK());
        model.setCity(city);
        model.setSex(sex);
        model.setInterest(mUser.getSubjectID());
        //model.setChannelID("YC-");
        final Gson gson = new Gson();

        Logger.i("PangChatByUser gson random: " + gson.toJson(model));
        JsonObjectRequest request = HttpHelper.requestRandomChat(model, "requestRandomChat",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        ResultRandomChat result = gson.fromJson(response.toString(), ResultRandomChat.class);
                        switch (result.getResultCode()) {

                            case HttpHelper.SUCCESS:
                                Logger.i("Successfully request requestRandomChat");

                                mPartner = result.getPangChatUser();
                                //isSuccessRequestFaceChat = true;

                                startChat();
                                //Toast.makeText(getApplicationContext(), "Successfully  date", Toast.LENGTH_LONG).show();
                                break;

                            case HttpHelper.NO_USER:
                                Logger.i(" requestRandomChat Successfully NO User");
                                //isSuccessRequestFaceChat = true;
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_no_matching), Toast.LENGTH_LONG).show();
                                break;

                            case HttpHelper.GCM_NO_REG:
                                Logger.i("RAN : GCM NO REG");
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.gcm_no_reg), Toast.LENGTH_SHORT).show();
                                break;

                            default:
                                Logger.i("RAN : Fail to requestRandomChat : " + result.getResultMessage());
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

    private void startRTCActivity() {
        //isVideo = true;
        Intent intent = new Intent(this, VideoChatActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("partner", mPartner);
        // PlayRTC Sample 유형 전달
        intent.putExtra("isRequester", 1); // create Connection
        intent.putExtra("param", param.toString());
        intent.putExtra("partnerID", "" + mPartner.getUserID());
        intent.putExtra("partnerAlias", mPartner.getAlias());
        intent.putExtra("partnerCity", mPartner.getCity());
        intent.putExtra("partnerImage", mPartner.getImageURL());
        intent.putExtra("channelID", "PC-" + mUser.getUserPK() + "-" + mPartner.getUserID());
        startActivity(intent);
        Logger.i("star activity" + param.toString());
    }

    public void updateOnAir(final int onAir) {

        //등록되지 않은 사용자
        if (mUser == null || mUser.getUserAlias() == null || TextUtils.isEmpty(mUser.getUserAlias())) {
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

        Logger.i("OnAirClass gson : " + gson.toJson(model));
        JsonObjectRequest request = HttpHelper.updateOnAir(model, "updateOnAir",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Result result = gson.fromJson(response.toString(), Result.class);
                        switch (result.getResultCode()) {

                            case HttpHelper.SUCCESS:
                                Logger.i("Successfully request updateOnAir");
                                if (onAir == 1) {
                                    isClose = true;
                                    onBackPressed();
                                }
                                //onDestroy();
                                //isSuccessRequestFaceChat = true;
                                //startRTCActivity();
                                //Toast.makeText(getApplicationContext(), "Successfully  date", Toast.LENGTH_LONG).show();
                                break;

                            default:
                                Logger.i("Fail to updateOnAir: " + result.getResultMessage());
                                //Toast.makeText(getApplicationContext(), result.getResultMessage(), Toast.LENGTH_SHORT).show();
                                if (onAir == 1) {
                                    isClose = true;
                                    onBackPressed();
                                }
                                //onDestroy();
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

    public void matching(String receiverID) {

        //등록되지 않은 사용자
        if (mUser == null || mUser.getUserAlias() == null || TextUtils.isEmpty(mUser.getUserAlias())) {
            Logger.i("GCM userAlias is empty");
            return;
        }

        /*if(mGuest.getIsOnAir() != 0 ){
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_unavailable), Toast.LENGTH_SHORT).show();
            return;
        }*/

        FriendAdd model = new FriendAdd();
        model.setUserID(receiverID);
        model.setMyID("" + mUser.getUserPK());
        final Gson gson = new Gson();

        Logger.i("matching gson : " + gson.toJson(model));
        JsonObjectRequest request = HttpHelper.matched(model, "matched",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Result result = gson.fromJson(response.toString(), Result.class);
                        switch (result.getResultCode()) {

                            case HttpHelper.SUCCESS:
                                Logger.i("Successfully matching");

                                //onDestroy();
                                //isSuccessRequestFaceChat = true;
                                //startRTCActivity();
                                //Toast.makeText(getApplicationContext(), "Successfully  date", Toast.LENGTH_LONG).show();
                                break;

                            default:
                                Logger.i("Fail to matching: " + result.getResultMessage());
                                //Toast.makeText(getApplicationContext(), result.getResultMessage(), Toast.LENGTH_SHORT).show();
                                //onDestroy();
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

    private void createCloseAlertDialog() {

        MaterialDialog.Builder builder = new MaterialDialog.Builder(this);

        builder.title(getResources().getString(R.string.close_alert_title))
                .content(getResources().getString(R.string.close_alert_message))
                .positiveText(getResources().getString(R.string.ok)).negativeText(R.string.no).cancelable(false).callback(new MaterialDialog.Callback() {

            @Override
            public void onPositive(MaterialDialog dialog) {
                dialog.dismiss();
                if (!isClose) {
                    updateOnAir(1);
                    mHomeWatcher.stopWatch();
                } else {
                    onBackPressed();
                }
            }

            @Override
            public void onNegative(MaterialDialog dialog) {
                dialog.dismiss();
                isClose = false;
            }
        }).build().show();
        // Create the Alert Builder.
       /* AlertDialog.Builder alertDialogBuilder;
        if(android.os.Build.VERSION.SDK_INT >= 23){
            alertDialogBuilder = new AlertDialog.Builder(this, R.style.PangChatTheme_StyleDialog_Over23);
        }else{
            alertDialogBuilder = new AlertDialog.Builder(this);
        }*/

        // Set a Alert.
       /* alertDialogBuilder.setTitle(R.string.close_alert_title);


        alertDialogBuilder.setMessage(R.string.close_alert_message);
        alertDialogBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int id) {
                dialogInterface.dismiss();
                if(!isClose){
                    updateOnAir(1);
                }else {
                    onBackPressed();
                }
            }
        });
        alertDialogBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int id) {
                dialogInterface.dismiss();
                isClose = false;

            }
        });

        // Create the Alert.
        closeAlertDialog = alertDialogBuilder.create();*/
    }

    //to send YoungChat here
    private JSONObject param = null;

    public void startChat() {
        prepareChannel("PC-" + mUser.getUserPK() + "-" + mPartner.getUserID(), "" + mUser.getUserPK(), mUser.getUserAlias());
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

    public CameraPreview getmPreview() {
        return this.mPreview;
    }

    void alert(String message) {

        AlertDialog dialog = new AlertDialog(this);
        dialog.setMessage(message);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                requestRandomChat();
            }
        });

        dialog.show();

    }
}
