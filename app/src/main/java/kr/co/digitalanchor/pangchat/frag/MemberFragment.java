package kr.co.digitalanchor.pangchat.frag;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
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
import kr.co.digitalanchor.pangchat.act.MainActivity;
import kr.co.digitalanchor.pangchat.handler.DBHelper;
import kr.co.digitalanchor.pangchat.handler.HttpHelper;
import kr.co.digitalanchor.pangchat.handler.VolleySingleton;
import kr.co.digitalanchor.pangchat.model.PangChatUser;
import kr.co.digitalanchor.pangchat.model.RequestMember;
import kr.co.digitalanchor.pangchat.model.RequestSearch;
import kr.co.digitalanchor.pangchat.model.ResponseMember;
import kr.co.digitalanchor.pangchat.model.ResultSearch;
import kr.co.digitalanchor.pangchat.model.User;

public class MemberFragment extends Fragment implements MainActivity.OnSetMemberState, MainActivity.OnSearchMember{
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    private GridView mGridView;
    private MemberGridAdaptor mAdaptor;
    private ArrayList<PangChatUser> mUsers;
    private View rootView;
    private RequestQueue mQueue;
    private int from = 0;
    private int end;
    private int sex=-1;
    private int city=-1;

    private User user;
    private DBHelper mDBHelper;


    public MemberFragment () {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static MemberFragment newInstance(int sectionNumber) {
        MemberFragment fragment = new MemberFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_member, container, false);
        mQueue = VolleySingleton.getmInstance(PCApplication.applicationContext).getmRequestQueue();
        mDBHelper = DBHelper.getInstance(getContext());
        user = mDBHelper.getUserInfo();
        initView();
        requestMember(sex, city);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        //requestMember(sex, city);
    }

    private void initView(){
        mGridView = (GridView)rootView.findViewById(R.id.member_grid);
        mAdaptor = new MemberGridAdaptor();
        mAdaptor.setContext(getContext());
        mUsers = new ArrayList<PangChatUser>();
        mAdaptor.setUsers(mUsers);
        mAdaptor.setMe(user);
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
                            requestMember(sex, city);
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
    private void requestMember(int man, int city){
        RequestMember model = new RequestMember();
        model.setSex(man);
        model.setCity(city);
        model.setFrom(from);
        model.setEnd(from);

        final Gson gson = new Gson();

        Logger.i(gson.toJson(model));

        JsonObjectRequest request = HttpHelper.requestMember(model, "member",
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
                                       // Logger.i("it's clear");
                                    }
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

    private void searchMember(String alias){
        RequestSearch model = new RequestSearch();
        model.setAlias(alias);
        final Gson gson = new Gson();

        Logger.i(gson.toJson(model));

        JsonObjectRequest request = HttpHelper.searchUser(model, "membersearch",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        ResultSearch result = gson.fromJson(response.toString(), ResultSearch.class);
                        switch (result.getResultCode()) {

                            case HttpHelper.SUCCESS:
                                Logger.i("receive user alias: " + result.getUser().getAlias());

                                    if(mUsers != null) {
                                        mUsers.clear();
                                    }

                                mUsers.add(result.getUser());
                                mAdaptor.setUsers(mUsers);
                                mAdaptor.notifyDataSetChanged();
                                break;

                            case HttpHelper.OFF_USER:
                                Toast.makeText(getContext(), getResources().getString(R.string.offUser), Toast.LENGTH_SHORT).show();
                                break;

                            case HttpHelper.NO_USER:
                                Toast.makeText(getContext(), getResources().getString(R.string.no_user), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onMemberStateSelected(int city, int sex) {
        this.city = city;
        this.sex = sex;
        this.from = 0;
        Logger.i("onMemberStateSelected : city :" + city  + " sex : " + sex);
        requestMember(this.sex, this.city);
    }

    @Override
    public void onMemberSearch(String alias) {
        this.from = 0;
        Logger.i("onMemberSearch : alias:" + alias);
        searchMember(alias);
    }
}
