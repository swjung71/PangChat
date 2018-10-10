package kr.co.digitalanchor.pangchat.handler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.orhanobut.logger.Logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import kr.co.digitalanchor.pangchat.model.GuestInfo;
import kr.co.digitalanchor.pangchat.model.UpdateUser;
import kr.co.digitalanchor.pangchat.model.User;

/**
 * Created by Xian on 2016-08-12.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static DBHelper sInstance;

    private static final String DATABASE_NAME = "YoungChat";
    private static final int DATABASE_VERSION = 1;

    private static final String USER_TABLE = "user";
    private static final String TABLE_MSG = "msg";
    private static final String TABLE_MSG_GUEST = "guest_info";//chatting 할 때 정보
    private static final String TRIGGER_NEW_MESSAGE = "new_message_trigger";
    private static final String TABLE_TALK_USER = "talk_user";
    private static final String TABLE_MESSAGE_USER = "message_user";

    //14개
    private static final String USER_ALIAS = "alias";
    private static final String USER_NAME = "name";
    private static final String USER_SEX = "sex";
    private static final String USER_SUBJECT = "subject";
    private static final String USER_SUBJECT_ID = "subjectID";
    private static final String USER_JOB = "job";
    private static final String USER_JOB_ID = "jobID";
    private static final String USER_CITY = "city";
    private static final String USER_CITY_ID = "cityID";
    private static final String USER_AGE = "age";
    private static final String USER_IMAGE_PATH = "image";
    private static final String USER_PK = "userPK";
    private static final String USER_PW = "passwd";
    private static final String USER_LIKE = "like";
    private static final String USER_END_DAY = "end_day";
    private static final String USER_LOG_IN = "login";
    private static final String USER_NATION = "nation";
    private static final String USER_NATION_ID = "nation_id";
    private static final String USER_INTEREST_SEX = "interest_sex";
    private static final String USER_INTEREST_SEX_ID = "interest_sex_id";
    private static final String USER_INTEREST_AGE = "interest_age";
    private static final String USER_INTEREST_AGE_ID = "interest_age_id";

    private static final String CHAT_KEY = "messagePK";
    private static final String MSG_ID = "messageID"; // 서버에 저장되는 message primary key, 불필요
    private static final String GUEST_ID = "guest_id";
    private static final String GUEST_ALIAS = "guest_alias";
    private static final String SENDER_ID = "sender_id";
    private static final String MSG = "msg";
    private static final String TIMESTAMP = "time";
    private static final String UNREAD_COUNT = "unreadcount";

    private static final String GUEST_AGE = "age";
    private static final String GUEST_LOCATION_LONG = "long";
    private static final String GUEST_LOCATION_LAT = "lat";
    private static final String GUEST_IMAGE_PATH = "image_path";
    private static final String GUEST_SEX = "sex";
    private static final String NEW_MESSAGE_COUNT = "newMessageCount";
    private static final String GUEST_TIME_LAST_LOCATION = "time";

    private static final String TALK_USER_AGE = "age";
    private static final String TALK_USER_ID = "id";
    private static final String TALK_USER_ALIAS = "alias";
    private static final String TALK_USER_LOCATION_LONG = "long";
    private static final String TALK_USER_LOCATION_LAT = "lat";
    private static final String TALK_USER_IMAGE_PATH = "image_path";
    private static final String TALK_USER_SEX = "sex";
    private static final String TALK_USER_LAST_TIME = "time";
    private static final String TALK_USER_CITY = "city";
    private static final String TALK_USER_DONG = "dong";
    private static final String TALK_USER_TALK = "talk";
    private static final String TALK_USER_ON_AIR = "isAir";

    public static synchronized DBHelper getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new DBHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String CREATE_USER_INFO_TABLE = "CREATE TABLE " + USER_TABLE
                + "("
                + USER_PK + " INTEGER, "
                + USER_NAME  + " TEXT, "
                + USER_ALIAS+ " TEXT, "
                + USER_SEX+ " TEXT, "
                + USER_AGE+ " TEXT, "
                + USER_IMAGE_PATH+ " TEXT, "
                + USER_PW+ " TEXT, "
                + USER_CITY+ " TEXT, "
                + USER_CITY_ID+ " TEXT, "
                + USER_NATION+ " TEXT, "
                + USER_NATION_ID+ " TEXT, "
                + USER_INTEREST_AGE+ " TEXT, "
                + USER_INTEREST_AGE_ID+ " TEXT, "
                + USER_INTEREST_SEX + " TEXT, "
                + USER_INTEREST_SEX_ID + " TEXT, "
                + USER_JOB+ " TEXT, "
                + USER_JOB_ID+ " TEXT, "
                + USER_SUBJECT_ID+ " TEXT, "
                + USER_LIKE+ " TEXT, "
                + USER_END_DAY + " TEXT, "
                + USER_LOG_IN + " TEXT, "
                + USER_SUBJECT+ " TEXT)"; //14개

        String CREATE_TABLE_GUEST = "CREATE TABLE " + TABLE_MSG_GUEST + " ( "
                + GUEST_ID + " INTEGER PRIMARY KEY, "
                + GUEST_ALIAS + " TEXT, "
                + GUEST_AGE + " INTEGER, "
                + GUEST_SEX + " INTEGER, "
                + GUEST_IMAGE_PATH + " TEXT, "
                + GUEST_LOCATION_LAT + " REAL, "
                + GUEST_LOCATION_LONG + " REAL, "
                + GUEST_TIME_LAST_LOCATION + " INTEGER, "
                + NEW_MESSAGE_COUNT + " INTEGER )";


        sqLiteDatabase.execSQL(CREATE_USER_INFO_TABLE);
        sqLiteDatabase.execSQL(CREATE_TABLE_GUEST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }

    public void clearAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MSG_GUEST);
        onCreate(db);
    }

    public User getUserInfo(){

        SQLiteDatabase db = this.getReadableDatabase();

        String[] result_columns = new String[]{USER_PK, USER_NAME, USER_ALIAS, USER_AGE,
                USER_SEX, USER_SUBJECT, USER_IMAGE_PATH, USER_SUBJECT_ID,
                USER_JOB, USER_JOB_ID, USER_CITY, USER_CITY_ID, USER_PW, USER_LIKE, USER_END_DAY, USER_LOG_IN,
                USER_NATION, USER_NATION_ID, USER_INTEREST_SEX, USER_INTEREST_AGE, USER_INTEREST_AGE_ID, USER_INTEREST_SEX_ID
        };

        Cursor cursor = db.query(true, USER_TABLE, result_columns, null, null, null, null, null, "1");
        User user = new User();
        if(cursor.moveToFirst()){

            user.setUserPK(cursor.getInt(0));
            user.setUserName(cursor.getString(1));
            user.setUserAlias(cursor.getString(2));
            user.setAge(cursor.getInt(3));
            user.setSex(cursor.getInt(4));
            user.setSubject(cursor.getString(5));
            user.setImagePath(cursor.getString(6));
            user.setSubjectID(cursor.getInt(7));
            user.setJob(cursor.getString(8));
            user.setJobID(cursor.getInt(9));
            user.setCity(cursor.getString(10));
            user.setCityID(cursor.getInt(11));
            user.setPasswd(cursor.getString(12));
            user.setLike(cursor.getInt(13));
            Logger.i("end day date : " + cursor.getString(14));
            user.setEndDay(cursor.getString(14));
            user.setLogin(cursor.getInt(15));
            user.setDeviceNumber(cursor.getString(16));
            user.setNationID(cursor.getInt(17));
            user.setInterestSex(cursor.getString(18));
            user.setInterestAge(cursor.getString(19));
            user.setInterestAgeID(cursor.getInt(20));
            user.setInterestSexID(cursor.getInt(21));

            if(cursor != null){
                cursor.close();
            }
            return user;
        }else {
            if(cursor != null){
                cursor.close();
            }
            return null;
        }
    }

    public void insertUserInfo(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(USER_PK, user.getUserPK());
        values.put(USER_NAME, user.getUserName());
        values.put(USER_ALIAS, user.getUserAlias());
        values.put(USER_AGE, user.getAge());
        values.put(USER_SEX, user.getSex());
        values.put(USER_SUBJECT, user.getSubject());
        values.put(USER_IMAGE_PATH, user.getImagePath());
        values.put(USER_SUBJECT_ID, user.getSubjectID());
        values.put(USER_JOB, user.getJob());
        values.put(USER_JOB_ID, user.getJobID());
        values.put(USER_CITY, user.getCity());
        values.put(USER_CITY_ID, user.getCityID());
        values.put(USER_PW, user.getPasswd());
        values.put(USER_LIKE, user.getLike());
        values.put(USER_NATION, user.getNation());
        values.put(USER_NATION_ID, user.getNationID());
        values.put(USER_INTEREST_AGE, user.getInterestAge());
        values.put(USER_INTEREST_AGE_ID, user.getInterestAgeID());
        values.put(USER_INTEREST_SEX, user.getInterestSex());
        values.put(USER_INTEREST_SEX_ID, user.getInterestSexID());
        //1 is login 0 is logout
        values.put(USER_LOG_IN, 1);

        Logger.i("end day : " + user.getEndDay());
        values.put(USER_END_DAY, user.getEndDay());
        db.insert(USER_TABLE, null, values);

        if(db != null){
            db.close();
        }
    }


    public void updateLike(String userPK, int like){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(USER_LIKE, like);
        String[] where = {String.valueOf(userPK)};
        db.update(USER_TABLE, values, USER_PK + "=" + userPK, null);

        if(db != null){
            db.close();
        }
    }

    public void updateUser(UpdateUser user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        //sex, name, userPK는 바꾸지 않음
        values.put(USER_IMAGE_PATH, user.getImagePath());
        values.put(USER_AGE, user.getAge());
        values.put(USER_ALIAS, user.getUserAlias());
        values.put(USER_SUBJECT, user.getSubject());
        values.put(USER_SUBJECT_ID, user.getSubjectID());
        values.put(USER_JOB, user.getJob());
        values.put(USER_JOB_ID, user.getJobID());
        values.put(USER_CITY, user.getCity());
        values.put(USER_CITY_ID, user.getCityID());
        values.put(USER_PW, user.getPasswd());
        values.put(USER_INTEREST_SEX, user.getInterestSex());
        values.put(USER_INTEREST_SEX_ID, user.getInterestSexID());
        values.put(USER_INTEREST_AGE, user.getInterestAge());
        values.put(USER_INTEREST_AGE_ID, user.getInterestAgeID());
        values.put(USER_NATION, user.getNation());
        values.put(USER_NATION_ID, user.getNationID());
        values.put(USER_LOG_IN, 1);
        db.update(USER_TABLE, values, null, null);

        if(db != null){
            db.close();
        }
    }


    public void insertGuest(String guestID, String guestAlias, int guestSex, int guestAge, String guestImage,
                            double longitude, double latitude, long lastTime){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(GUEST_ID, guestID);
        values.put(GUEST_AGE, guestAge);
        values.put(GUEST_ALIAS, guestAlias);
        values.put(GUEST_SEX, guestSex);
        values.put(GUEST_IMAGE_PATH, guestImage);
        values.put(GUEST_LOCATION_LAT, latitude);
        values.put(GUEST_LOCATION_LONG, longitude);
        values.put(GUEST_TIME_LAST_LOCATION, lastTime);

        Logger.i("insert Guest message Time stamp " + lastTime);
        Date date = new Date(lastTime);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateformatted = format.format(date);
        Logger.i("insert Guest message Time is " + dateformatted);

        values.put(NEW_MESSAGE_COUNT, 0);

        db.insert(TABLE_MSG_GUEST, null, values);

        if(db != null){
            db.close();
        }
    }

    public void updateGuest(String guestID, String guestAlias, int guestAge, String guestImage,
                            double longitude, double latitude, long lastTime){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(GUEST_AGE, guestAge);
        values.put(GUEST_ALIAS, guestAlias);
        values.put(GUEST_LOCATION_LAT, latitude);
        values.put(GUEST_LOCATION_LONG, longitude);
        values.put(GUEST_TIME_LAST_LOCATION, lastTime);
        values.put(GUEST_IMAGE_PATH, guestImage);

        Logger.i("updateGuest message Time stamp " + lastTime);
        Date date = new Date(lastTime);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateformatted = format.format(date);
        Logger.i("updateGuest message Time is " + dateformatted);

        db.update(TABLE_MSG_GUEST, values, GUEST_ID + "=?", new String[]{guestID});

        if (db != null) {

            db.close();

            db = null;
        }
    }

    public GuestInfo getGuest(String guestID){

        SQLiteDatabase db = this.getReadableDatabase();

        String[] result_columns = new String[]{GUEST_ID, GUEST_ALIAS, GUEST_IMAGE_PATH, GUEST_LOCATION_LAT,
                GUEST_LOCATION_LONG, GUEST_AGE, GUEST_SEX, GUEST_TIME_LAST_LOCATION, NEW_MESSAGE_COUNT
        };

        Logger.i("guestID " + guestID);
        Cursor cursor = db.query(true, TABLE_MSG_GUEST, result_columns, GUEST_ID + "=?", new String[]{guestID},
                null, null, null, "1");

        GuestInfo info = new GuestInfo();
        if(cursor.moveToFirst()){

            info.setGuestID(cursor.getInt(0));
            info.setGuestAlias(cursor.getString(1));
            info.setImagePath(cursor.getString(2));
            info.setLatitude(cursor.getDouble(3));
            info.setLongitude(cursor.getDouble(4));
            info.setAge(cursor.getInt(5));
            info.setSex(cursor.getInt(6));
            info.setLastTime(cursor.getLong(7));


            Logger.i("guestInfo message Time stamp " + info.getLastTime());
            Date date = new Date(info.getLastTime());
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateformatted = format.format(date);
            Logger.i("guestInfo message Time is " + dateformatted);


            info.setUnReadCount(cursor.getInt(8));
            if(cursor != null){
                cursor.close();
            }
            return info;
        }
        if(cursor != null){
            cursor.close();
        }
        return null;
    }

    public ArrayList<GuestInfo> getGuests(){
        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<GuestInfo> guestUsers = new ArrayList<GuestInfo>();

        String[] result_columns = new String[]{GUEST_ID, GUEST_ALIAS, GUEST_IMAGE_PATH, GUEST_LOCATION_LAT,
                GUEST_LOCATION_LONG, GUEST_AGE, GUEST_SEX, GUEST_TIME_LAST_LOCATION, NEW_MESSAGE_COUNT
        };

        Cursor cursor = db.query(true, TABLE_MSG_GUEST, result_columns, null, null,
                null, null, null, null);

        if(cursor.moveToFirst()){
            do {
                GuestInfo info = new GuestInfo();
                info.setGuestID(cursor.getInt(0));
                info.setGuestAlias(cursor.getString(1));
                info.setImagePath(cursor.getString(2));
                info.setLatitude(cursor.getDouble(3));
                info.setLongitude(cursor.getDouble(4));
                info.setAge(cursor.getInt(5));
                info.setSex(cursor.getInt(6));
                info.setLastTime(cursor.getLong(7));

                Logger.i("guestInfos message Time stamp " + info.getLastTime());
                Date date = new Date(info.getLastTime());
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dateformatted = format.format(date);
                Logger.i("guestInfos message Time is " + dateformatted);
                Logger.i("guest Alias " + info.getGuestAlias());

                info.setUnReadCount(cursor.getInt(8));
                guestUsers.add(info);
            } while (cursor.moveToNext());
            if(cursor != null){
                cursor.close();
            }
            return guestUsers;
        }
        if(cursor != null){
            cursor.close();
        }
        return null;
    }

    public void updateEndDay(String date){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        Logger.i("end day : " + date);
        values.put(USER_END_DAY, date);

        db.update(USER_TABLE, values, null, null);

        if (db != null) {

            db.close();

            db = null;
        }
    }

    public void login(){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(USER_LOG_IN, 1);

        db.update(USER_TABLE, values, null, null);

        if (db != null) {

            db.close();

            db = null;
        }
    }

    public void logout(){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(USER_LOG_IN, 0);

        db.update(USER_TABLE, values, null, null);

        if (db != null) {
            db.close();
            db = null;
        }
    }
}
