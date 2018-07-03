package com.example.q.example;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.q.example.ContactsFragment;
import com.example.q.example.GalleryFragment;
import com.example.q.example.LightFragment;

public class MyPagerAdapter extends FragmentStatePagerAdapter {

    int mNoOfTabs;

    public MyPagerAdapter(FragmentManager fm, int NumberOfTabs)
    {
        super(fm);
        this.mNoOfTabs = NumberOfTabs;
    }


    @Override
    public Fragment getItem(int position) {
        switch(position)
        {

            case 0:
                ContactsFragment tab1 = new ContactsFragment();
                return tab1;
            case 1:
                GalleryFragment tab2 = new GalleryFragment();
                return  tab2;
            case 2:
                LightFragment tab3 = new LightFragment();

                return  tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNoOfTabs;
    }
}