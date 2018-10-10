package kr.co.digitalanchor.pangchat.handler;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import kr.co.digitalanchor.pangchat.model.BuyItem;
import kr.co.digitalanchor.pangchat.model.CheckAlias;
import kr.co.digitalanchor.pangchat.model.CheckID;
import kr.co.digitalanchor.pangchat.model.CheckUser;
import kr.co.digitalanchor.pangchat.model.FriendAdd;
import kr.co.digitalanchor.pangchat.model.GCMUpdate;
import kr.co.digitalanchor.pangchat.model.GetVersion;
import kr.co.digitalanchor.pangchat.model.GuestInfoRequest;
import kr.co.digitalanchor.pangchat.model.Login;
import kr.co.digitalanchor.pangchat.model.OnAirClass;
import kr.co.digitalanchor.pangchat.model.PangChatByUser;
import kr.co.digitalanchor.pangchat.model.ReceiverUser;
import kr.co.digitalanchor.pangchat.model.RequestDeleteMatching;
import kr.co.digitalanchor.pangchat.model.RequestFriend;
import kr.co.digitalanchor.pangchat.model.RequestMatching;
import kr.co.digitalanchor.pangchat.model.RequestMember;
import kr.co.digitalanchor.pangchat.model.RequestRandom;
import kr.co.digitalanchor.pangchat.model.RequestSearch;
import kr.co.digitalanchor.pangchat.model.SearchPW;
import kr.co.digitalanchor.pangchat.model.UpdateUser;
import kr.co.digitalanchor.pangchat.model.Withdraw;

import static com.android.volley.Request.Method.POST;

/**
 * Created by Xian on 2016-08-12.
 */
public class HttpHelper {

    /**
     * true : dev ; false : real
     */
    public static boolean isDev = false;

    /**
     * Dev Server url http://14.63.225.89/studytime-server
     */

    public static final String PROTOCOL = "https://";

    public static final String DOMAIN = "www.dastudytime.kr/";

    public static final String PATH = "pangchat-server/";

    public static final String PROTOCOL_DEV = "http://";

    public static final String DOMAIN_DEV = "14.63.225.89/";

    public static final String PATH_DEV = "pangchat-server/";

    public static final int  SUCCESS = 0;
    public static final int NO_USER = 1;
    public static final int EXIST_ALIAS = 2;
    public static final int EXIST_DEVICE = 3;
    public static final int NO_AVIL = 4;
    public static final int EXIST_FRIEND = 8;
    public static final int EXIST_USER = 9;
    public static final int OFF_USER = 10;
    public static final int SUCCESS_DATE=11;
    public static final int GCM_NO_REG = 1012;

    public static String getURL() {

        if (isDev) {

            return PROTOCOL_DEV + DOMAIN_DEV + PATH_DEV;

        } else {

            return PROTOCOL + DOMAIN + PATH;
        }
    }

    public static String getImageURL(String path) {

        if (isDev) {

            return PROTOCOL_DEV + DOMAIN_DEV + "resources/photo/" + path;

        } else {

            return PROTOCOL + DOMAIN + "resources/photo/" + path;
        }
    }

    /**
     * GCMUpdate,
     * registerURL
     *
     * @param model         params
     * @param listener      response listener
     * @param errorListener error listener
     * @return request
     */
    public static JsonObjectRequest gcmUpdate(GCMUpdate model, String url,
                                                 Response.Listener<JSONObject> listener,
                                                 Response.ErrorListener errorListener) {
        Gson gson = new Gson();
        String json = gson.toJson(model);
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(json);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new JsonObjectRequest(POST, getURL() + url, jsonObj, listener, errorListener);
    }

    /**
     * GCMUpdate,
     * registerURL
     *
     * @param model         params
     * @param listener      response listener
     * @param errorListener error listener
     * @return request
     */
    public static JsonObjectRequest checkUser(CheckUser model, String url,
                                              Response.Listener<JSONObject> listener,
                                              Response.ErrorListener errorListener) {
        Gson gson = new Gson();
        String json = gson.toJson(model);
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(json);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new JsonObjectRequest(POST, getURL() +  url, jsonObj, listener, errorListener);
    }

    /**
     * GCMUpdate,
     * registerURL
     *
     * @param model         params
     * @param listener      response listener
     * @param errorListener error listener
     * @return request
     */
    public static JsonObjectRequest getVersion(GetVersion model, String url,
                                              Response.Listener<JSONObject> listener,
                                              Response.ErrorListener errorListener) {
        Gson gson = new Gson();
        String json = gson.toJson(model);
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(json);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new JsonObjectRequest(POST, getURL() +  url, jsonObj, listener, errorListener);
    }

    /**
     * register
     *
     * @param model         params
     * @param listener      response listener
     * @param errorListener error listener
     * @return request
     */
    public static JsonObjectRequest register(ReceiverUser model, String url,
                                             Response.Listener<JSONObject> listener,
                                             Response.ErrorListener errorListener) {
        Gson gson = new Gson();
        String json = gson.toJson(model);
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(json);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new JsonObjectRequest(POST, getURL() +  url, jsonObj, listener, errorListener);
    }

    /**
     * register
     *
     * @param model         params
     * @param listener      response listener
     * @param errorListener error listener
     * @return request
     */
    /*public static JsonObjectRequest requestFaceChat(FaceChateByUser model, String url,
                                             Response.Listener<JSONObject> listener,
                                             Response.ErrorListener errorListener) {
        Gson gson = new Gson();
        String json = gson.toJson(model);
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(json);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new JsonObjectRequest(POST, getURL() +  url, jsonObj, listener, errorListener);
    }*/


    public static JsonObjectRequest withdraw(Withdraw model, String url,
                                             Response.Listener<JSONObject> listener,
                                             Response.ErrorListener errorListener) {
        Gson gson = new Gson();
        String json = gson.toJson(model);
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(json);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new JsonObjectRequest(POST, getURL() +  url, jsonObj, listener, errorListener);
    }


    public static JsonObjectRequest getGuestInfo(GuestInfoRequest model, String url,
                                             Response.Listener<JSONObject> listener,
                                             Response.ErrorListener errorListener) {
        Gson gson = new Gson();
        String json = gson.toJson(model);
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(json);

            Logger.i(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JsonObjectRequest(POST, getURL() +  url, jsonObj, listener, errorListener);
    }

    public static MultipartRequest uploadPhoto(String url, String filePath, String userPK,
                                           Response.Listener<String> listener,
                                           Response.ErrorListener errorListener) {
        return new MultipartRequest(getURL() + url, filePath, userPK, listener, errorListener);
    }

    public static JsonObjectRequest addFriend(FriendAdd model, String url,
                                              Response.Listener<JSONObject> listener,
                                              Response.ErrorListener errorListener) {
        Gson gson = new Gson();
        String json = gson.toJson(model);
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(json);

            Logger.i(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JsonObjectRequest(POST, getURL() +  url, jsonObj, listener, errorListener);
    }

    public static JsonObjectRequest requestAddFriend(FriendAdd model, String url,
                                                  Response.Listener<JSONObject> listener,
                                                  Response.ErrorListener errorListener) {
        Gson gson = new Gson();
        String json = gson.toJson(model);
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(json);

            Logger.i(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JsonObjectRequest(POST, getURL() +  url, jsonObj, listener, errorListener);
    }


    public static JsonObjectRequest updateUser(UpdateUser model, String url,
                                             Response.Listener<JSONObject> listener,
                                             Response.ErrorListener errorListener) {
        Gson gson = new Gson();
        String json = gson.toJson(model);
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(json);

            Logger.i(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JsonObjectRequest(POST, getURL() +  url, jsonObj, listener, errorListener);
    }

    /**
     * @param model User로 보내나 서버에서는 다른 것으로 받음
     * @param url
     * @param listener
     * @param errorListener
     * @return
     */
    public static JsonObjectRequest login(Login model, String url,
                                          Response.Listener<JSONObject> listener,
                                          Response.ErrorListener errorListener) {
        Gson gson = new Gson();
        String json = gson.toJson(model);
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(json);
            Logger.i(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JsonObjectRequest(POST, getURL() +  url, jsonObj, listener, errorListener);
    }

    /**
     * @param model User로 보내나 서버에서는 다른 것으로 받음
     * @param url
     * @param listener
     * @param errorListener
     * @return
     */
    public static JsonObjectRequest checkID(CheckID model, String url,
                                            Response.Listener<JSONObject> listener,
                                            Response.ErrorListener errorListener) {
        Gson gson = new Gson();
        String json = gson.toJson(model);
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(json);
            Logger.i(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JsonObjectRequest(POST, getURL() +  url, jsonObj, listener, errorListener);
    }

    /**
     * @param model User로 보내나 서버에서는 다른 것으로 받음
     * @param url
     * @param listener
     * @param errorListener
     * @return
     */
    public static JsonObjectRequest checkAlias(CheckAlias model, String url,
                                          Response.Listener<JSONObject> listener,
                                          Response.ErrorListener errorListener) {
        Gson gson = new Gson();
        String json = gson.toJson(model);
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(json);
            Logger.i(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JsonObjectRequest(POST, getURL() +  url, jsonObj, listener, errorListener);
    }

    /**
     * @param model User로 보내나 서버에서는 다른 것으로 받음
     * @param url
     * @param listener
     * @param errorListener
     * @return
     */
    public static JsonObjectRequest review(CheckID model, String url,
                                               Response.Listener<JSONObject> listener,
                                               Response.ErrorListener errorListener) {
        Gson gson = new Gson();
        String json = gson.toJson(model);
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(json);
            Logger.i(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JsonObjectRequest(POST, getURL() +  url, jsonObj, listener, errorListener);
    }

    /**
     * @param model User로 보내나 서버에서는 다른 것으로 받음
     * @param url
     * @param listener
     * @param errorListener
     * @return
     */
    public static JsonObjectRequest searchID(SearchPW model, String url,
                                             Response.Listener<JSONObject> listener,
                                             Response.ErrorListener errorListener) {
        Gson gson = new Gson();
        String json = gson.toJson(model);
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(json);
            Logger.i(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JsonObjectRequest(POST, getURL() +  url, jsonObj, listener, errorListener);
    }

    /**
     * @param model User로 보내나 서버에서는 다른 것으로 받음
     * @param url
     * @param listener
     * @param errorListener
     * @return
     */
    public static JsonObjectRequest searchPW(SearchPW model, String url,
                                           Response.Listener<JSONObject> listener,
                                           Response.ErrorListener errorListener) {
        Gson gson = new Gson();
        String json = gson.toJson(model);
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(json);
            Logger.i(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JsonObjectRequest(POST, getURL() +  url, jsonObj, listener, errorListener);
    }

    /**
     * @param model User로 보내나 서버에서는 다른 것으로 받음
     * @param url
     * @param listener
     * @param errorListener
     * @return
     */
    public static JsonObjectRequest requestChat(PangChatByUser model, String url,
                                                Response.Listener<JSONObject> listener,
                                                Response.ErrorListener errorListener) {
        Gson gson = new Gson();
        String json = gson.toJson(model);
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(json);
            Logger.i(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JsonObjectRequest(POST, getURL() +  url, jsonObj, listener, errorListener);
    }

    /**
     * @param model User로 보내나 서버에서는 다른 것으로 받음
     * @param url
     * @param listener
     * @param errorListener
     * @return
     */
    public static JsonObjectRequest requestMember(RequestMember model, String url,
                                                  Response.Listener<JSONObject> listener,
                                                  Response.ErrorListener errorListener) {
        Gson gson = new Gson();
        String json = gson.toJson(model);
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(json);
            Logger.i(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JsonObjectRequest(POST, getURL() +  url, jsonObj, listener, errorListener);
    }

    /**
     * @param model User로 보내나 서버에서는 다른 것으로 받음
     * @param url
     * @param listener
     * @param errorListener
     * @return
     */
    public static JsonObjectRequest searchUser(RequestSearch model, String url,
                                               Response.Listener<JSONObject> listener,
                                               Response.ErrorListener errorListener) {
        Gson gson = new Gson();
        String json = gson.toJson(model);
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(json);
            Logger.i(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JsonObjectRequest(POST, getURL() +  url, jsonObj, listener, errorListener);
    }

    /**
     * @param model User로 보내나 서버에서는 다른 것으로 받음
     * @param url
     * @param listener
     * @param errorListener
     * @return
     */
    public static JsonObjectRequest requestFriend(RequestFriend model, String url,
                                                  Response.Listener<JSONObject> listener,
                                                  Response.ErrorListener errorListener) {
        Gson gson = new Gson();
        String json = gson.toJson(model);
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(json);
            Logger.i(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JsonObjectRequest(POST, getURL() +  url, jsonObj, listener, errorListener);
    }

    /**
     * @param model User로 보내나 서버에서는 다른 것으로 받음
     * @param url
     * @param listener
     * @param errorListener
     * @return
     */
    public static JsonObjectRequest deleteMatching(RequestDeleteMatching model, String url,
                                                   Response.Listener<JSONObject> listener,
                                                   Response.ErrorListener errorListener) {
        Gson gson = new Gson();
        String json = gson.toJson(model);
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(json);
            Logger.i(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JsonObjectRequest(POST, getURL() +  url, jsonObj, listener, errorListener);
    }

    /**
     * @param model User로 보내나 서버에서는 다른 것으로 받음
     * @param url
     * @param listener
     * @param errorListener
     * @return
     */
    public static JsonObjectRequest requestMatching(RequestMatching model, String url,
                                                    Response.Listener<JSONObject> listener,
                                                    Response.ErrorListener errorListener) {
        Gson gson = new Gson();
        String json = gson.toJson(model);
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(json);
            Logger.i(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JsonObjectRequest(POST, getURL() +  url, jsonObj, listener, errorListener);
    }

    /**
     * @param model User로 보내나 서버에서는 다른 것으로 받음
     * @param url
     * @param listener
     * @param errorListener
     * @return
     */
    public static JsonObjectRequest requestPangChat(PangChatByUser model, String url,
                                                    Response.Listener<JSONObject> listener,
                                                    Response.ErrorListener errorListener) {
        Gson gson = new Gson();
        String json = gson.toJson(model);
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(json);
            Logger.i(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JsonObjectRequest(POST, getURL() +  url, jsonObj, listener, errorListener);
    }

    /**
     * @param model User로 보내나 서버에서는 다른 것으로 받음
     * @param url
     * @param listener
     * @param errorListener
     * @return
     */
    public static JsonObjectRequest requestRandomChat(RequestRandom model, String url,
                                                      Response.Listener<JSONObject> listener,
                                                      Response.ErrorListener errorListener) {
        Gson gson = new Gson();
        String json = gson.toJson(model);
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(json);
            Logger.i(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JsonObjectRequest(POST, getURL() +  url, jsonObj, listener, errorListener);
    }

    /**
     * @param model User로 보내나 서버에서는 다른 것으로 받음
     * @param url
     * @param listener
     * @param errorListener
     * @return
     */
    public static JsonObjectRequest updateOnAir(OnAirClass model, String url,
                                                Response.Listener<JSONObject> listener,
                                                Response.ErrorListener errorListener) {
        Gson gson = new Gson();
        String json = gson.toJson(model);
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(json);
            Logger.i(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JsonObjectRequest(POST, getURL() +  url, jsonObj, listener, errorListener);
    }

    /**
     * @param model User로 보내나 서버에서는 다른 것으로 받음
     * @param url
     * @param listener
     * @param errorListener
     * @return
     */
    public static JsonObjectRequest buyItem(BuyItem model, String url,
                                            Response.Listener<JSONObject> listener,
                                            Response.ErrorListener errorListener) {
        Gson gson = new Gson();
        String json = gson.toJson(model);
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(json);
            Logger.i(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JsonObjectRequest(POST, getURL() +  url, jsonObj, listener, errorListener);
    }

    /**
     * @param model User로 보내나 서버에서는 다른 것으로 받음
     * @param url
     * @param listener
     * @param errorListener
     * @return
     */
    public static JsonObjectRequest matched(FriendAdd model, String url,
                                            Response.Listener<JSONObject> listener,
                                            Response.ErrorListener errorListener) {
        Gson gson = new Gson();
        String json = gson.toJson(model);
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(json);
            Logger.i(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JsonObjectRequest(POST, getURL() +  url, jsonObj, listener, errorListener);
    }
}
