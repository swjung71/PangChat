package kr.co.digitalanchor.pangchat.frag;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import java.util.ArrayList;

import kr.co.digitalanchor.pangchat.R;
import kr.co.digitalanchor.pangchat.handler.DBHelper;
import kr.co.digitalanchor.pangchat.handler.HttpHelper;
import kr.co.digitalanchor.pangchat.handler.VolleySingleton;
import kr.co.digitalanchor.pangchat.model.MatchingUser;
import kr.co.digitalanchor.pangchat.model.RequestMatching;
import kr.co.digitalanchor.pangchat.model.ResultMatching;
import kr.co.digitalanchor.pangchat.model.User;


public class MatchingFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    private ListView mListView;
    private MatchingAdaptor mAdaptor;
    private ArrayList<MatchingUser> mUser;
    private RequestQueue mQueue;
    private int from = 0;
    private DBHelper mDBHelper;
    private User me;
    private Button btnNoRecord;

    View rootView;
    public MatchingFragment () {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static MatchingFragment newInstance(int sectionNumber) {
        MatchingFragment fragment = new MatchingFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_matching, container, false);
        mUser = new ArrayList<MatchingUser>();
        mDBHelper = DBHelper.getInstance(getContext());
        me = mDBHelper.getUserInfo();
        mQueue = VolleySingleton.getmInstance(getContext()).getmRequestQueue();
        initView();
        requestMatching();
        return rootView;
    }

    private void initView(){
        mListView = (ListView)rootView.findViewById(R.id.listMatching);
        mAdaptor = new MatchingAdaptor(getContext(), mUser);
        mListView.setAdapter(mAdaptor);

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                        && (mListView.getLastVisiblePosition() - mListView.getHeaderViewsCount() -
                        mListView.getFooterViewsCount()) >= (mAdaptor.getCount() - 1)) {
                        requestMatching();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });

        btnNoRecord = (Button) rootView.findViewById(R.id.no_recode);
    }

    private void requestMatching(){
        RequestMatching model = new RequestMatching();
        model.setUserID(me.getUserPK());
        model.setFrom(from);
        model.setEnd(from);

        final Gson gson = new Gson();

        Logger.i(gson.toJson(model));

        JsonObjectRequest request = HttpHelper.requestMatching(model, "matching",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        ResultMatching result = gson.fromJson(response.toString(), ResultMatching.class);
                        switch (result.getResultCode()) {

                            case HttpHelper.SUCCESS:

                                if(from == 0){
                                    if(result.getUsers() !=  null && result.getUsers().size() > 0){
                                        btnNoRecord.setVisibility(View.GONE);
                                        mListView.setVisibility(View.VISIBLE);
                                        from = from + 10;
                                        mUser.addAll(result.getUsers());
                                        mAdaptor.notifyDataSetChanged();
                                        mListView.setSelection(0);
                                    }
                                }else {
                                    if(result.getUsers() !=  null && result.getUsers().size() > 0) {
                                        mUser.addAll(result.getUsers());
                                        mAdaptor.notifyDataSetChanged();
                                    }
                                    from = from + 5;
                                }

                                break;

                            case HttpHelper.NO_USER:
                                break;
                            default:
                                Logger.i("error: " + result.getResultMessage());
                                Toast.makeText(getContext(), getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
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
