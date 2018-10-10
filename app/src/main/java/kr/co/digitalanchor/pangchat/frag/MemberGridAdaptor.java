package kr.co.digitalanchor.pangchat.frag;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;

import kr.co.digitalanchor.pangchat.R;
import kr.co.digitalanchor.pangchat.dialog.ProfileDialog;
import kr.co.digitalanchor.pangchat.handler.HttpHelper;
import kr.co.digitalanchor.pangchat.handler.VolleySingleton;
import kr.co.digitalanchor.pangchat.model.PangChatUser;
import kr.co.digitalanchor.pangchat.model.User;
import kr.co.digitalanchor.pangchat.utils.FadeInNetworkImageView;

/**
 * Created by Peter Jung on 2016-11-28.
 */

public class MemberGridAdaptor extends BaseAdapter {

    private Context mContext;
    private User mUser;
    private ArrayList<PangChatUser> mUsers;

    public Context getContext(){return  mContext;}
    public void setContext(Context context){mContext = context;}

    public ArrayList<PangChatUser> getUsers(){return mUsers;}
    public void setUsers(ArrayList<PangChatUser> users){mUsers = users;}

    @Override
    public int getCount() {
        if(mUsers != null && mUsers.size() >0 ){
            return mUsers.size();
        }else {
            return 0;
        }
    }

    @Override
    public Object getItem(int index) {
        return mUsers.get(index);
    }

    @Override
    public long getItemId(int index) {
        return index;
    }

    @Override
    public View getView(final int index, View gridView, ViewGroup viewGroup) {

        if(gridView == null){
            LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            gridView = inflater.inflate(R.layout.grid_member, null);
        }

        FadeInNetworkImageView networkImageView = (FadeInNetworkImageView)gridView.findViewById(R.id.network_image);
        TextView text = (TextView) gridView.findViewById(R.id.pangchat_text);
        ImageView onAir = (ImageView)gridView.findViewById(R.id.is_onAir);
        TextView city = (TextView)gridView.findViewById(R.id.pangchat_text_city) ;

        text.setText(mUsers.get(index).getAlias() + "(" + (mUsers.get(index).getSex() == 0 ? "남" : "여 ") + mUsers.get(index).getAge() + "세) :" );
        city.setText(mUsers.get(index).getCity());
        //0 on 1 off 2 onAir
        // 옛날 것 1이 onAir
        if(mUsers.get(index).getIsOnAir() !=  2){
            onAir.setVisibility(View.GONE);
        }

        ImageLoader imageLoader = VolleySingleton.getmInstance(mContext).getmImageLoader();
        networkImageView.setImageUrl(HttpHelper.getImageURL(mUsers.get(index).getImageURL()), imageLoader);

        gridView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ProfileDialog dialogPangChatProfile= new ProfileDialog(mContext);
                dialogPangChatProfile.setPangChatUser(mUsers.get(index));
                dialogPangChatProfile.setUser(mUser);
                dialogPangChatProfile.show();

                /*DialogSendMsg dialogSendMsg = new DialogSendMsg(mContext);
                //String value = mUser.get(index).getUserID() + " : " + (mUser.get(index).getSex() == 0 ? "남" : "여") + mUser.get(index).getIsOnAir();
                dialogSendMsg.setUserInfo(mUser.get(index));
                dialogSendMsg.show();*/
                //Toast.makeText(mContext, mUser.get(index).getUserID(), Toast.LENGTH_LONG).show();

            }
        });
        return gridView;
    }

    public void setMe(User me){
        this.mUser = me;
    }
}
