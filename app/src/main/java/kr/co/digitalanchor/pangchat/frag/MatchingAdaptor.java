package kr.co.digitalanchor.pangchat.frag;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import java.util.ArrayList;

import kr.co.digitalanchor.pangchat.PCApplication;
import kr.co.digitalanchor.pangchat.R;
import kr.co.digitalanchor.pangchat.handler.HttpHelper;
import kr.co.digitalanchor.pangchat.handler.VolleySingleton;
import kr.co.digitalanchor.pangchat.model.MatchingUser;
import kr.co.digitalanchor.pangchat.model.RequestDeleteMatching;
import kr.co.digitalanchor.pangchat.model.Result;
import kr.co.digitalanchor.pangchat.utils.AndroidUtils;
import kr.co.digitalanchor.pangchat.view.RoundedNetworkImageView;
import kr.co.digitalanchor.pangchat.view.ViewHolder;

/**
 * Created by user on 2016-12-20.
 */

public class MatchingAdaptor extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<MatchingUser> mMatchingUsers;
    private Context mContext;
    private RequestQueue mQueue;

    public MatchingAdaptor(Context context, ArrayList<MatchingUser> user) {
        mQueue = VolleySingleton.getmInstance(PCApplication.applicationContext).getmRequestQueue();
        mContext = context;
        mMatchingUsers = user;
        inflater = LayoutInflater.from(this.mContext);
    }
    @Override
    public int getCount() {
        return mMatchingUsers.size();
    }

    @Override
    public Object getItem(int position) {
        return mMatchingUsers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_view_matching, null);
        }
        MatchingUser user = mMatchingUsers.get(position);

        RoundedNetworkImageView networkImageView = (RoundedNetworkImageView) ViewHolder.get(convertView, R.id.rounded_image);

        ImageLoader imageLoader = VolleySingleton.getmInstance(mContext).getmImageLoader();
        networkImageView.setImageUrl(HttpHelper.getImageURL(user.getImageURL()), imageLoader);

        TextView nameTextView = (TextView) ViewHolder.get(convertView, R.id.name_text);

        TextView timeTextView = (TextView) ViewHolder.get(convertView, R.id.chat_time_text);

        Button btnDelete = (Button) ViewHolder.get(convertView, R.id.delete_button);

        Logger.i("mUserSize : " + mMatchingUsers.size());
        nameTextView.setText(user.getAlias() + ": " + user.getCity() + ": " + (user.getSex() == 0 ? "남" : "여") + user.getAge() + mContext.getResources().getString(R.string.age_end));

        timeTextView.setText(AndroidUtils.convertCurrentTime4Chat(user.getChatTime()));

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestMatchingDelete(mMatchingUsers.get(position).getMatchingID(), position);
            }
        });
        return convertView;
    }

    public void requestMatchingDelete(int matchingID, final int position){
        RequestDeleteMatching model = new RequestDeleteMatching();
        model.setMatchingID(matchingID);
        final Gson gson = new Gson();

        Logger.i(gson.toJson(model));

        JsonObjectRequest request = HttpHelper.deleteMatching(model, "deleteMatching",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Result result = gson.fromJson(response.toString(), Result.class);
                        switch (result.getResultCode()) {

                            case HttpHelper.SUCCESS:
                                Toast.makeText(mContext, mContext.getResources().getString(R.string.delete_success), Toast.LENGTH_SHORT).show();
                                mMatchingUsers.remove(position);
                                notifyDataSetChanged();
                                break;
                            default:
                                Logger.i("error: " + result.getResultMessage());
                                Toast.makeText(mContext, mContext.getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
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
