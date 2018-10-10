package kr.co.digitalanchor.pangchat.act;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.soundcloud.android.crop.Crop;

import org.json.JSONObject;

import java.io.File;
import java.util.List;

import kr.co.digitalanchor.pangchat.BaseActivity;
import kr.co.digitalanchor.pangchat.PCApplication;
import kr.co.digitalanchor.pangchat.R;
import kr.co.digitalanchor.pangchat.dialog.LoginRegDialog;
import kr.co.digitalanchor.pangchat.dialog.ReviewDialog;
import kr.co.digitalanchor.pangchat.handler.DBHelper;
import kr.co.digitalanchor.pangchat.handler.HttpHelper;
import kr.co.digitalanchor.pangchat.handler.MultipartRequest;
import kr.co.digitalanchor.pangchat.model.CheckAlias;
import kr.co.digitalanchor.pangchat.model.CheckID;
import kr.co.digitalanchor.pangchat.model.ReceiverUser;
import kr.co.digitalanchor.pangchat.model.Result;
import kr.co.digitalanchor.pangchat.model.User;
import kr.co.digitalanchor.pangchat.model.UserCheckResult;
import kr.co.digitalanchor.pangchat.utils.MD5;

/**
 * Created by Peter Jung on 2016-11-17.
 */
public class SignupActivity extends BaseActivity implements View.OnClickListener  {

    private final int COMPLETE_REGISTER = 50002;
    private final int REQUEST_PHOTO_UPLOAD = 5003;

    private final int PICK_FROM_GALLERY = 50004;
    private final int PICK_FROM_CAMERA = 50005;

    private final String TYPE = "Type";

    private DBHelper mDBHelper;
    private ImageButton btnClose;
    private Button btnGrallery;
    private Button btnCamera;
    private Button btnNickNameDup;
    private Button btnIDDup;
    private Button btnReg;
    private CheckBox checkAgree;
    private CheckBox checkPrivacy;
    private Button btnViewAgree;
    private Button btnViewPrivacy;
    private ImageView photoImage;
    private EditText editNickName;
    private EditText editID;
    private EditText editPass;

    private Spinner ageSpinner;
    private Spinner regionSpinner;
    private Spinner sexSpinner;
    private Spinner subjectSpinner;
    private Spinner jobSpinner;
    private Spinner nationSpinner;
    private Spinner interestAgeSpinner;
    private Spinner interestSexSpinner;

    private int mAgeInt;
    private int mNationInt;
    private int mRegionInt;
    private int mSexInt;
    private int mSubjectInt;
    private int mJobInt;
    private int mInterestSexInt;
    private int mInterestAgeInt;

    private String mAge;
    private String mNation;
    private String mRegion;
    private String mSex;
    private String mSubject;
    private String mJob;
    private String mInterestSex;
    private String mInterestAge;

    private ArrayAdapter<CharSequence> ageSpinAdapter;
    private ArrayAdapter<CharSequence> regionSpinAdapter;
    private ArrayAdapter<CharSequence> sexSpinAdapter;
    private ArrayAdapter<CharSequence> subjectSpinAdapter;
    private ArrayAdapter<CharSequence> jobSpinAdapter;
    private ArrayAdapter<CharSequence> interestAgeSpinAdapter;
    private ArrayAdapter<CharSequence> interestSexSpinAdapter;
    private ArrayAdapter<CharSequence> nationSpinAdapter;


    //최정 저장되는 이미지
    private static Uri finalFileUri;
    private static String finalFileName;
    private static Uri mImageCaptureUri;
    private static Uri outputFileUri;

    private User mUser;
    private ReceiverUser receiverUser;
    private Context mContext = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_signup);
        setFinishOnTouchOutside(false);
        mDBHelper = DBHelper.getInstance(this);
        mContext = this;
        initView();
    }

    @Override
    public void onClick(View view) {
        if (isDuplicateRuns()) {
            return;
        }

        switch (view.getId()){
            case R.id.btn_close:
                LoginRegDialog dialog = new LoginRegDialog(PCApplication.getIntroActivity());
                dialog.show();
                finish();
                break;

            case R.id.btn_id_dup:
                checkID();
                break;

            case R.id.btn_nick_name_dup:
                checkAlias();
                break;

            case R.id.btn_camera:
                getPhotoFromCamera();
                break;

            case R.id.btn_gallery:
                getPhotoFromGallery();
                break;

            case R.id.btn_reg:
                if(isValidateInfo()){
                    register();
                }
                break;
            case R.id.btn_see_agree:
                startAgree(0);
                break;
            case R.id.btn_see_privacy:
                startAgree(1);
                break;
            default:
                break;
        }

    }

    private void initView(){

        jobSpinner = (Spinner)findViewById(R.id.job_spinner);
        jobSpinAdapter = MyArrayAdapter.createFromResource(this, R.array.jobs, R.layout.my_spinner_textview);
        jobSpinAdapter.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        jobSpinner.setAdapter(jobSpinAdapter);
        jobSpinner.setPrompt(getResources().getString(R.string.job_hint));
        jobSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mJob = (String) jobSpinner.getSelectedItem();
                mJobInt = jobSpinner.getSelectedItemPosition();
                Logger.i("job  : " + mJob);
                Logger.i("job int : " + mJobInt);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        sexSpinner = (Spinner)findViewById(R.id.sexSpinner);
        sexSpinAdapter = MyArrayAdapter.createFromResource(this, R.array.sex, R.layout.my_spinner_textview);
        sexSpinAdapter.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        sexSpinner.setAdapter(sexSpinAdapter);
        sexSpinner.setPrompt(getResources().getString(R.string.sex));
        sexSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mSex = (String) sexSpinner.getSelectedItem();
                mSexInt = sexSpinner.getSelectedItemPosition();
                Logger.i("sex : " + mSex);
                Logger.i("sex int: " + mSexInt);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        nationSpinner= (Spinner)findViewById(R.id.nationSpinner);
        nationSpinAdapter = MyArrayAdapter.createFromResource(this, R.array.nation, R.layout.my_spinner_textview);
        nationSpinAdapter.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        nationSpinner.setAdapter(nationSpinAdapter );
        nationSpinner.setPrompt(getResources().getString(R.string.nation));
        nationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mNation = (String) nationSpinner.getSelectedItem();
                mNationInt = nationSpinner.getSelectedItemPosition();
                Logger.i("nation : " + mNation);
                Logger.i("nation Int : " + mNationInt);
                if(mNationInt == 0){
                    regionSpinner = (Spinner)findViewById(R.id.regionSpinner);
                    regionSpinAdapter = MyArrayAdapter.createFromResource(SignupActivity.this, R.array.region, R.layout.my_spinner_textview);
                    regionSpinAdapter .setDropDownViewResource(android.R.layout.simple_list_item_checked);
                    regionSpinner.setAdapter(regionSpinAdapter);
                    regionSpinner.setPrompt(getResources().getString(R.string.city));
                    regionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            mRegion = (String) regionSpinner.getSelectedItem();
                            mRegionInt = regionSpinner.getSelectedItemPosition();
                            Logger.i("region : " + mRegion);
                            Logger.i("region Int : " + mRegionInt);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                }else if(mNationInt ==1 ){
                    regionSpinner = (Spinner)findViewById(R.id.regionSpinner);
                    regionSpinAdapter = MyArrayAdapter.createFromResource(SignupActivity.this, R.array.region1, R.layout.my_spinner_textview);
                    regionSpinAdapter .setDropDownViewResource(android.R.layout.simple_list_item_checked);
                    regionSpinner.setAdapter(regionSpinAdapter);
                    regionSpinner.setPrompt(getResources().getString(R.string.city));
                    regionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            mRegion = (String) regionSpinner.getSelectedItem();
                            mRegionInt = regionSpinner.getSelectedItemPosition();
                            Logger.i("region : " + mRegion);
                            Logger.i("region Int : " + mRegionInt);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        ageSpinner = (Spinner)findViewById(R.id.ageSpinner);
        ageSpinAdapter = MyArrayAdapter.createFromResource(this, R.array.age, R.layout.my_spinner_textview);
        ageSpinAdapter.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        ageSpinner.setAdapter(ageSpinAdapter);
        ageSpinner.setPrompt(getResources().getString(R.string.age));
        ageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mAge = (String) ageSpinner.getSelectedItem();
                mAgeInt = ageSpinner.getSelectedItemPosition();
                Logger.i("age : " + mAge);
                Logger.i("age int : " + mAgeInt);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        subjectSpinner = (Spinner)findViewById(R.id.subjectSpinner);
        subjectSpinAdapter = MyArrayAdapter.createFromResource(this, R.array.subject, R.layout.my_spinner_textview);
        subjectSpinAdapter.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        subjectSpinner.setAdapter(subjectSpinAdapter);
        subjectSpinner.setPrompt(getResources().getString(R.string.interest_sub));
        subjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mSubject = (String) subjectSpinner.getSelectedItem();
                mSubjectInt = subjectSpinner.getSelectedItemPosition();
                Logger.i("subject : " + mSubject);
                Logger.i("subject int : " + mSubjectInt);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        interestAgeSpinner = (Spinner)findViewById(R.id.interest_age);
        interestAgeSpinAdapter = MyArrayAdapter.createFromResource(this, R.array.interest_age, R.layout.my_spinner_textview);
        interestAgeSpinAdapter.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        interestAgeSpinner.setAdapter(interestAgeSpinAdapter);
        interestAgeSpinner.setPrompt(getResources().getString(R.string.age));
        interestAgeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mInterestAge = (String) interestAgeSpinner.getSelectedItem();
                mInterestAgeInt = interestAgeSpinner.getSelectedItemPosition();
                Logger.i("InterestAge : " + mInterestAge);
                Logger.i("InterestAge int : " + mInterestAgeInt);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        interestSexSpinner = (Spinner)findViewById(R.id.interest_sex);
        interestSexSpinAdapter = MyArrayAdapter.createFromResource(this, R.array.sex, R.layout.my_spinner_textview);
        interestSexSpinAdapter.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        interestSexSpinner.setAdapter(interestSexSpinAdapter);
        interestSexSpinner.setPrompt(getResources().getString(R.string.sex));
        interestSexSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mInterestSex = (String) interestSexSpinner.getSelectedItem();
                mInterestSexInt = interestSexSpinner.getSelectedItemPosition();
                Logger.i("InterestSex : " + mInterestSex);
                Logger.i("InterestSex int : " + mInterestSexInt);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        checkAgree = (CheckBox)findViewById(R.id.check_agree);
        checkAgree.setPadding(checkAgree.getPaddingLeft(), checkAgree.getPaddingTop(),
                checkAgree.getPaddingRight(), checkAgree.getPaddingBottom());

        checkPrivacy = (CheckBox)findViewById(R.id.check_privacy);
        checkPrivacy.setPadding(checkPrivacy.getPaddingLeft(), checkPrivacy.getPaddingTop(),
                checkPrivacy.getPaddingRight(), checkPrivacy.getPaddingBottom());

        btnClose = (ImageButton) findViewById(R.id.btn_close);
        btnClose.setOnClickListener(this);

        btnCamera = (Button)findViewById(R.id.btn_camera);
        btnCamera.setOnClickListener(this);

        btnGrallery = (Button)findViewById(R.id.btn_gallery);
        btnGrallery.setOnClickListener(this);

        btnIDDup = (Button)findViewById(R.id.btn_id_dup);
        btnIDDup.setOnClickListener(this);

        btnNickNameDup = (Button)findViewById(R.id.btn_nick_name_dup);
        btnNickNameDup.setOnClickListener(this);

        btnViewAgree = (Button)findViewById(R.id.btn_see_agree);
        btnViewAgree.setOnClickListener(this);

        btnViewPrivacy = (Button)findViewById(R.id.btn_see_privacy);
        btnViewPrivacy.setOnClickListener(this);

        btnReg = (Button)findViewById(R.id.btn_reg);
        btnReg.setOnClickListener(this);

        editNickName = (EditText)findViewById(R.id.edit_nickname);
        editID = (EditText)findViewById(R.id.editID);
        editPass = (EditText)findViewById(R.id.edit_passwd);

        photoImage = (ImageView)findViewById(R.id.photo);
    }

    class MyArrayAdapter<T> extends ArrayAdapter<T> {

        public MyArrayAdapter(Context context, int resource, List<T> objects) {
            super(context, resource, objects);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View v = super.getView(position, convertView, parent);

            return v;
        }
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View v = super.getDropDownView(position, convertView, parent);
            return v;
        }
    }

    private boolean isValidateInfo() {

        String temp = null;

        String msg = null;

        do {
            // check required info

            if(finalFileUri == null || TextUtils.isEmpty(finalFileUri.getPath())) {
                msg = getResources().getString(R.string.error_no_image);

                break;
            }

            temp = mSubject;

            if (TextUtils.isEmpty(temp)) {

                msg = getResources().getString(R.string.error_no_subject);

                break;
            }

            temp = editID.getText().toString();

            if (TextUtils.isEmpty(temp)) {

                msg = getResources().getString(R.string.error_no_ID);

                break;
            }


            temp = editNickName.getText().toString();

            if (TextUtils.isEmpty(temp)) {

                msg = getResources().getString(R.string.error_no_nickName);

                break;
            }

            temp = editPass.getText().toString();

            if (TextUtils.isEmpty(temp)) {

                msg = getResources().getString(R.string.error_no_passwd);

                break;
            }

            temp = mJob;

            if (TextUtils.isEmpty(temp)) {

                msg = getResources().getString(R.string.error_no_job);

                break;
            }

            temp = mAge;

            if (TextUtils.isEmpty(temp)) {

                msg = getResources().getString(R.string.error_no_age);

                break;
            }

            temp = mSex;

            if (TextUtils.isEmpty(temp)) {

                msg = getResources().getString(R.string.error_no_sex);

                break;
            }

            temp = mNation;

            if (TextUtils.isEmpty(temp)) {

                msg = getResources().getString(R.string.error_no_nation);

                break;
            }

            temp = mRegion;

            if (TextUtils.isEmpty(temp)) {

                msg = getResources().getString(R.string.error_no_city);

                break;
            }

            temp = mInterestAge;

            if (TextUtils.isEmpty(temp)) {

                msg = getResources().getString(R.string.error_no_interest_age);

                break;
            }

            temp = mInterestSex;

            if (TextUtils.isEmpty(temp)) {

                msg = getResources().getString(R.string.error_no_interest_sex);

                break;
            }

            if (!checkAgree.isChecked()) {

                msg = getResources().getString(R.string.error_no_agree_policy);

                break;
            }

            if (!checkPrivacy.isChecked()) {

                msg = getResources().getString(R.string.error_no_agree_privacy);

                break;
            }


        } while (false);

        if (TextUtils.isEmpty(msg)) {

            return true;

        } else {

            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

            return false;
        }
    }

    private void getPhotoFromGallery(){
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType(MediaStore.Images.Media.CONTENT_TYPE);
        i.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, PICK_FROM_GALLERY);
    }

    private void getPhotoFromCamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // 임시로 사용할 파일의 경로를 생성
        String url = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
        outputFileUri = Uri.fromFile(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), url));

        Logger.i("getPhotoFromCamera : " + outputFileUri);
        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, outputFileUri);
        startActivityForResult(intent, PICK_FROM_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK)
            return;

        switch (requestCode) {

            case PICK_FROM_GALLERY: {
                mImageCaptureUri = data.getData();
                Logger.i("gallery : " + mImageCaptureUri.getPath().toString());

                finalFileName = MD5.getHash(PCApplication.getDeviceNumber() + System.currentTimeMillis())+".png";
                Logger.i("gallery : finalFileName : " + finalFileName);
                String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/tmp/" + finalFileName;
                File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/tmp/" );
                directory.mkdir();
                File file = new File(filePath);

                finalFileUri = Uri.fromFile(file);

                //source --> final destination
                Crop.of(mImageCaptureUri, finalFileUri).withMaxSize(320, 320).asSquare().asPng(true).start(this);
                Logger.i("gallery : uri" + finalFileUri);
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, finalFileUri));
                break;
            }

            case PICK_FROM_CAMERA: {

                Logger.i("PICK Camera");
                Logger.i("outputFileUri : " + outputFileUri);
                finalFileName = MD5.getHash(PCApplication.getDeviceNumber() + System.currentTimeMillis())+".png";
                String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/tmp/" + finalFileName;
                File file = new File(filePath);
                finalFileUri = Uri.fromFile(file);
                Logger.i("pick camera 2 : uri " + finalFileUri);
                Crop.of(outputFileUri, finalFileUri).withMaxSize(320, 320).asSquare().asPng(true).start(this);

                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, finalFileUri));
                break;
            }
            case Crop.REQUEST_CROP:{
                try {
                    //sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, finalFileUri));
                    Logger.i("gallery CROP result : " + finalFileUri);
                    Bitmap finalBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), finalFileUri);
                    //photoImage.setBackgroundColor(getResources().getColor(R.color.transparent));
                    //photoImage.setImageBitmap(finalBitmap);
                    photoImage.setImageURI(finalFileUri);
                    Logger.i("swj : setImageURI");
                }catch (Exception error){
                    error.printStackTrace();
                }
                break;
            }

        }
    }

    private void uploadPhoto(){

        MultipartRequest request = HttpHelper.uploadPhoto("photo", finalFileUri.getPath(), PCApplication.getDeviceNumber(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Logger.i("photo response " + response.toString());
                        Gson gson = new Gson();
                        Result result = gson.fromJson(response, Result.class);

                        switch (result.getResultCode()){
                            case HttpHelper.SUCCESS:
                                Logger.i("photo upload success");
                                sendEmptyMessage(COMPLETE_REGISTER);
                                break;
                            default:
                                Logger.i("photo upload fail");
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_upload_image), Toast.LENGTH_SHORT).show();
                                break;

                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        handleError(error);
                    }
                });

        if(request != null) {
            addRequest(request);
        }
    }

    private void checkID(){
        if(editID.getText().toString().isEmpty() || TextUtils.isEmpty(editID.getText().toString())){
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_no_ID), Toast.LENGTH_SHORT).show();
        }else{
            requestCheckID();
        }
    }

    private void requestCheckID(){
        CheckID checkID = new CheckID();
        checkID.setUserName(editID.getText().toString());
        final Gson gson = new Gson();

        Logger.i(gson.toJson(checkID));

        JsonObjectRequest request = HttpHelper.checkID(checkID, "checkID",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Result result = gson.fromJson(response.toString(), Result.class);
                        switch (result.getResultCode()) {

                            case HttpHelper.SUCCESS:
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.can_use_ID), Toast.LENGTH_SHORT).show();
                                break;

                            case HttpHelper.EXIST_USER:
                                Logger.i("exist id");
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_exist_id), Toast.LENGTH_SHORT).show();
                                break;

                            default:
                                Logger.i("fail for check ID: " + result.getResultMessage());
                                //Toast.makeText(getApplicationContext(), result.getResultMessage(), Toast.LENGTH_LONG).show();
                                break;
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        handleError(error);
                    }
                });

        if (request != null) {
            addRequest(request);
        }
    }
    private void checkAlias(){
        if(editNickName.getText().toString().isEmpty() || TextUtils.isEmpty(editNickName.getText().toString())){
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_no_nickName), Toast.LENGTH_SHORT).show();
        }else{
            requestCheckAlias();
        }
    }

    private void requestCheckAlias(){
        CheckAlias checkAlias = new CheckAlias();
        checkAlias.setUserAlias(editNickName.getText().toString());
        final Gson gson = new Gson();

        Logger.i(gson.toJson(checkAlias));

        JsonObjectRequest request = HttpHelper.checkAlias(checkAlias, "checkAlias",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Result result = gson.fromJson(response.toString(), Result.class);
                        switch (result.getResultCode()) {

                            case HttpHelper.SUCCESS:
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.can_use_alias), Toast.LENGTH_SHORT).show();
                                break;

                            case HttpHelper.EXIST_ALIAS:
                                Logger.i("exist alias");
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_exist_alias), Toast.LENGTH_SHORT).show();
                                break;

                            default:
                                Logger.i("fail for check ID: " + result.getResultMessage());
                                //Toast.makeText(getApplicationContext(), result.getResultMessage(), Toast.LENGTH_LONG).show();
                                break;
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        handleError(error);
                    }
                });

        if (request != null) {
            addRequest(request);
        }
    }

    private void register(){
        receiverUser = new ReceiverUser();
        receiverUser.setUserName(editID.getText().toString());
        receiverUser.setUserAlias(editNickName.getText().toString());
        receiverUser.setSubject(((String) subjectSpinner.getSelectedItem()));
        receiverUser.setSubjectID(subjectSpinner.getSelectedItemPosition());
        receiverUser.setSex(sexSpinner.getSelectedItemPosition());
        receiverUser.setAge(new Integer((String) ageSpinner.getSelectedItem()));

        Logger.i("age : " + receiverUser.getAge());
        receiverUser.setDeviceNumber(PCApplication.getDeviceNumber());
        receiverUser.setPhoneNumber(PCApplication.getPhoneNumber());
        receiverUser.setGcmID(PCApplication.getRegistrationId());
        receiverUser.setNationalCode(PCApplication.getNationalCode());
        receiverUser.setAppVersion(PCApplication.getAppVersionName());
        receiverUser.setImagePath(finalFileName);

        receiverUser.setJob((String)jobSpinner.getSelectedItem());
        receiverUser.setJobID(jobSpinner.getSelectedItemPosition());

        receiverUser.setCity((String)regionSpinner.getSelectedItem());
        receiverUser.setCityID(regionSpinner.getSelectedItemPosition());

        receiverUser.setPasswd(editPass.getText().toString());
        receiverUser.setNation(mNation);
        receiverUser.setNationID(mNationInt);
        receiverUser.setInterestAge(mInterestAge);
        receiverUser.setInterestAgeID(mInterestAgeInt);
        receiverUser.setInterestSex(mInterestSex);
        receiverUser.setInterestSexID(mInterestSexInt);

        Logger.i("fileName : " + finalFileName);

        final Gson gson = new Gson();
        Logger.i(gson.toJson(receiverUser));
        JsonObjectRequest request = HttpHelper.register(receiverUser, "register",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        UserCheckResult result = gson.fromJson(response.toString(), UserCheckResult.class);
                        switch (result.getResultCode()) {

                            case HttpHelper.SUCCESS:
                                Logger.i("Successfully register");
                                mUser = result.getUser();
                                Logger.i("register User : " + gson.toJson(mUser));
                                mDBHelper.insertUserInfo(mUser);

                                Logger.i("insertUser user");
                                sendEmptyMessage(REQUEST_PHOTO_UPLOAD);
                                //Toast.makeText(getApplicationContext(), "Successfully  date", Toast.LENGTH_LONG).show();
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.success_reg), Toast.LENGTH_SHORT).show();
                                break;

                            case HttpHelper.EXIST_ALIAS:
                                Logger.i("exist user");
                                Toast.makeText(getApplicationContext(), "이미 사용중인 아이디/닉네임입니다.", Toast.LENGTH_LONG).show();
                                break;

                            case HttpHelper.EXIST_DEVICE:
                                Logger.i("exist device");
                                Toast.makeText(getApplicationContext(), "이미 등록된 기기입니다.", Toast.LENGTH_LONG).show();
                                break;

                            default:
                                Logger.i("Fail to register : " + result.getResultMessage());
                                //Toast.makeText(getApplicationContext(), result.getResultMessage(), Toast.LENGTH_LONG).show();
                                break;
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        handleError(error);
                    }
                });

        if (request != null) {
            addRequest(request);
        }
    }

    @Override
    protected void onHandleMessage(Message msg) {

        switch (msg.what){
            case REQUEST_PHOTO_UPLOAD:
                uploadPhoto();
                break;
            case COMPLETE_REGISTER:
                completeRegister();
                break;
            default:
                break;
        }
    }

    private void completeRegister() {

        if(PCApplication.getString("review", "").isEmpty()){
            Intent intent = new Intent(this, ReviewDialog.class);
            startActivity(intent);
            finish();
            Logger.i("have to write review");
            PCApplication.putString("review", "reviewed");
        }else{
            Logger.i("already write review");
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void startAgree(int type){
        Intent intent = new Intent();
        intent.setClass(this, PolicyAgreeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(TYPE, type);
        startActivity(intent);
    }
}
