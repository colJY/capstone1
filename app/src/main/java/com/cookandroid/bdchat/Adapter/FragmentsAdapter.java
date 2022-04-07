package com.cookandroid.bdchat.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.cookandroid.bdchat.Fragments.ChatsFragment;
import com.cookandroid.bdchat.Fragments.RequestsFragment;
import com.cookandroid.bdchat.Fragments.StatusFragment;

public class FragmentsAdapter extends FragmentPagerAdapter {
    public FragmentsAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
    // 메인 탭 이동
        switch (position)
        {
            case 0: return new ChatsFragment();
            case 1: return new StatusFragment();
            case 2: return new RequestsFragment();
            default:return new ChatsFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        String title = null;
        if(position == 0)
        {
            title ="CHATS";
        }
        if(position == 1)
        {
            title ="STATUS";
        }
        if(position == 2)
        {
            title ="Requests";
        }
        return title;
    }
}
