package kr.co.digitalanchor.pangchat.frag;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Peter Jung on 2016-10-26.
 */
public class SectionsPageAdapter extends FragmentPagerAdapter {

        public SectionsPageAdapter (FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if(position == 1) {
                return MemberFragment.newInstance(position + 1);
            }else if (position == 2){
                return MatchingFragment.newInstance(position + 1);
            }else if (position == 3 ){
                return FriendFragment.newInstance(position + 1);
            }else{
                return PangChatFragment.newInstance(position +1);
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 1:
                    return "멤버";
                case 2:
                    return "매칭기록";
                case 3:
                    return "친구";
            }
            return null;
        }
}
