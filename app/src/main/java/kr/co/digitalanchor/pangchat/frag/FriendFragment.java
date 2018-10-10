package kr.co.digitalanchor.pangchat.frag;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import java.util.ArrayList;

import kr.co.digitalanchor.pangchat.PCApplication;
import kr.co.digitalanchor.pangchat.R;
import kr.co.digitalanchor.pangchat.handler.DBHelper;
import kr.co.digitalanchor.pangchat.handler.HttpHelper;
import kr.co.digitalanchor.pangchat.handler.VolleySingleton;
import kr.co.digitalanchor.pangchat.model.PangChatUser;
import kr.co.digitalanchor.pangchat.model.RequestFriend;
import kr.co.digitalanchor.pangchat.model.ResponseMember;
import kr.co.digitalanchor.pangchat.model.User;


public class FriendFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    private GridView mGridView;
    private FriendGridAdaptor mAdaptor;
    private ArrayList<PangChatUser> mUsers;
    private View rootView;
    private RequestQueue mQueue;
    private int from = 0;
    private User mMe;
    private DBHelper mDHelper;
    private Button btnNoRecord;

    public FriendFragment () {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static FriendFragment newInstance(int sectionNumber) {
        FriendFragment fragment = new FriendFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_friend, container, false);
        mQueue = VolleySingleton.getmInstance(PCApplication.applicationContext).getmRequestQueue();
        mDHelper = DBHelper.getInstance(getContext());
        mMe = mDHelper.getUserInfo();
        initView();
        requestFriend();
        return rootView;
    }

    private void initView(){
        btnNoRecord = (Button)rootView.findViewById(R.id.no_recode) ;
        mGridView = (GridView)rootView.findViewById(R.id.member_grid);
        mAdaptor = new FriendGridAdaptor();
        mAdaptor.setContext(getContext());
        mUsers = new ArrayList<PangChatUser>();
        mAdaptor.setUsers(mUsers);
        mAdaptor.setMe(mMe);
        mGridView.setAdapter(mAdaptor);

        mGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                Logger.i("onScrollStateChanged Event");
                switch (view.getId()){

                    case R.id.member_grid:
                        Logger.i("mGridView.getLastVisiblePosition() : " + mGridView.getLastVisiblePosition() );
                        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                                && (mGridView.getLastVisiblePosition() >= (from-1))) {
                            Logger.i("mGridView.getLastVisiblePosition() : " + mGridView.getLastVisiblePosition() );
                            Logger.i("mAdapter.getCount() " + mAdaptor.getCount());
                            Logger.i("end of list gridView");
                            requestFriend();
                            //requestMember(sex, city);
                            // Now your listview has hit the bottom
                        }
                        Logger.i("mAdapter.getCount() " + mAdaptor.getCount());
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
    }

    // man : 0 남녀, 1남, 2 여, city 0 전국
    private void requestFriend(){
        RequestFriend model = new RequestFriend();
        model.setUserID(mMe.getUserPK());
        model.setFrom(from);
        model.setEnd(from);

        final Gson gson = new Gson();
        Logger.i(gson.toJson(model));

        JsonObjectRequest request = HttpHelper.requestFriend(model, "friend",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        ResponseMember result = gson.fromJson(response.toString(), ResponseMember.class);
                        switch (result.getResultCode()) {

                            case HttpHelper.SUCCESS:
                                Logger.i("receive user size: " + result.getUsers().size());
                                Logger.i("from : " + from);
                                if(from == 0) {
                                    if(mUsers != null) {
                                        mUsers.clear();
                                    }
                                }

                                if(result.getUsers() !=null && result.getUsers().size() >= 1){
                                    mGridView.setVisibility(View.VISIBLE);
                                    btnNoRecord.setVisibility(View.GONE);
                                }
                                mUsers.addAll(result.getUsers());

                                //mAdaptor.setUsers(mUsers);
                                mAdaptor.notifyDataSetChanged();

                                if(from == 0 ) {
                                   //mGridView.setSelection(mUsers.size());
                                    from = from + 15;
                                }else {
                                    from = from + 6;
                                }
                                break;
                            case HttpHelper.NO_USER:
                                break;

                            default:
                                Logger.i("error: " + result.getResultMessage());
                                Toast.makeText(getContext(), getResources().getString(R.string.network_error), Toast.LENGTH_LONG).show();
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
