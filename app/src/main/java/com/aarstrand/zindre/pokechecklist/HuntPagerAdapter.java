package com.aarstrand.zindre.pokechecklist;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;



public class HuntPagerAdapter extends FragmentPagerAdapter {

    private static final int HUNTINGTAB = 0;
    private static final int REGISTERTAB = 1;
    public static final String ARG_NUMBER = "number" ;
    int i;

    public HuntPagerAdapter(FragmentManager fm, int i) {
        super(fm);
        this.i = i;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){
            case HUNTINGTAB:
                fragment = new HuntingTab();
                break;
            case REGISTERTAB:
                fragment = new RegisterTab();
                break;
        }
        Bundle args = new Bundle();
        args.putInt(ARG_NUMBER,i);
        if(fragment!=null)
            fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return HuntActivity.NUMBER_OF_TABS;
    }
}
