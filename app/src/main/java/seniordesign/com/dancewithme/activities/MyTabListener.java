package seniordesign.com.dancewithme.activities;

/**
 * Created by ryantemplin on 9/16/15.
 */
import android.app.ActionBar;
import android.app.Fragment;
import android.support.v4.view.ViewPager;


import seniordesign.com.dancewithme.R;

public class MyTabListener implements ActionBar.TabListener {
    // Fragment fragment;
    ViewPager mViewPager;

    public MyTabListener(ViewPager viewPager) {
        this.mViewPager = viewPager;
    }

    public void onTabSelected(android.app.ActionBar.Tab tab, android.app.FragmentTransaction ft) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    public void onTabUnselected(android.app.ActionBar.Tab tab, android.app.FragmentTransaction ft) {
        //nothing done here
    }

    public void onTabReselected(android.app.ActionBar.Tab tab, android.app.FragmentTransaction ft) {
        // nothing done here
    }
}