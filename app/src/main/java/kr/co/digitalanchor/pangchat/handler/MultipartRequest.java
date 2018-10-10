package kr.co.digitalanchor.pangchat.handler;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.orhanobut.logger.Logger;

import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by albert on 14-3-21.
 */
public class MultipartRequest extends Request<String> {
    public static final String KEY_PICTURE = "TransferFile";
    public static final String KEY_PICTURE_NAME = "filename";
    public static final String KEY_USERPK = "deviceNum";

    private HttpEntity mHttpEntity;

    private String mDeviceNum;
    private Response.Listener mListener;

    public MultipartRequest(String url, String filePath, String deviceNum,
                            Response.Listener<String> listener,
                            Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);

        Logger.i("photo URL : " + url);

        mDeviceNum = deviceNum;
        mListener = listener;
        mHttpEntity = buildMultipartEntity(filePath);
    }

    public MultipartRequest(String url, File file, String deviceNum,
                            Response.Listener<String> listener,
                            Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);

        mDeviceNum = deviceNum;
        mListener = listener;



        mHttpEntity = buildMultipartEntity(file);
    }

    private HttpEntity buildMultipartEntity(String filePath) {
        File file = new File(filePath);
        return buildMultipartEntity(file);
    }

    private HttpEntity buildMultipartEntity(File file) {
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        /*builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        try {
            builder.setCharset(CharsetUtils.get("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/
        String fileName = file.getName();
        FileBody fileBody = new FileBody(file);
        /*FileBody fileBody = new FileBody(file, ContentType.create("image/png"),
                file.getName());*/
        builder.addPart(KEY_PICTURE, fileBody);
        //builder.addTextBody(KEY_PICTURE_NAME, fileName);
        //builder.addTextBody(KEY_USERPK, mDeviceNum);
        return builder.build();
    }

    @Override
    public String getBodyContentType() {
        return mHttpEntity.getContentType().getValue();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            mHttpEntity.writeTo(bos);
        } catch (IOException e) {
            VolleyLog.e("IOException writing to ByteArrayOutputStream");
        }
        return bos.toByteArray();
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        try {
            String data = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(data, getCacheEntry());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }
}