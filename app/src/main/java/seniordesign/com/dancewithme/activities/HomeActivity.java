package seniordesign.com.dancewithme.activities;

import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import com.parse.ParseUser;

import seniordesign.com.dancewithme.R;
import seniordesign.com.dancewithme.fragments.MessageFragment;
import seniordesign.com.dancewithme.fragments.ProfileFragment;
import seniordesign.com.dancewithme.fragments.VenueFragment;


public class HomeActivity extends FragmentActivity {
    private static final String TAG = HomeActivity.class.getSimpleName();

    // Private class params
    private ActionBar.Tab profileTab, venueTab, messageTab;

    ParseUser user;
    HomeActivity activity;
    MyApplication application;
    BroadcastReceiver receiver;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        activity = this;
        application = (MyApplication) activity.getApplication();
        //user = DataStore.getInstance().getUser();
        user = ParseUser.getCurrentUser();

        // Get layout params
        ViewPager mViewPager = (ViewPager) findViewById(R.id.view_pager);
        ActionBar actionBar = getActionBar();
        // Init the action bar
        if(actionBar!=null){
            actionBar.setDisplayShowHomeEnabled(false);  // hides action bar icon
            actionBar.setDisplayShowTitleEnabled(false); // hides action bar title
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

            // initialize tabs
            profileTab = actionBar.newTab();
            venueTab = actionBar.newTab();
            messageTab = actionBar.newTab();

            //add the icons to the tabs
            profileTab.setIcon(R.drawable.tab_account_sel);
            venueTab.setIcon(R.drawable.tab_match);
            messageTab.setIcon(R.drawable.tab_message);

            //listener for change in tabs
            MyTabListener mListener = new MyTabListener(mViewPager);

            //every tab gets a tab listener
            profileTab.setTabListener(mListener);
            venueTab.setTabListener(mListener);
            messageTab.setTabListener(mListener);

            //add tabs to the bar
            actionBar.addTab(profileTab, 0, false);
            actionBar.addTab(venueTab, 1, true);
            actionBar.addTab(messageTab, 2, false);

            actionBar.setSelectedNavigationItem(1);

        } else {
            finish();
        }

        // Initialize the view pager & pager adapter
        DemoCollectionPagerAdapter mDemoCollectionPagerAdapter;
        mDemoCollectionPagerAdapter = new DemoCollectionPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mDemoCollectionPagerAdapter);
        mViewPager.setOnPageChangeListener(

                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        // When swiping between pages, select the
                        // corresponding tab.
                        if (position == 0){
                            profileTab.setIcon(R.drawable.tab_account_sel);
                            venueTab.setIcon(R.drawable.tab_match);
                            messageTab.setIcon(R.drawable.tab_message);
                        }
                        else if (position == 1){
                            profileTab.setIcon(R.drawable.tab_account_sel);
                            venueTab.setIcon(R.drawable.tab_match);
                            messageTab.setIcon(R.drawable.tab_message);
                        }
                        else if (position == 2){
                            profileTab.setIcon(R.drawable.tab_account_sel);
                            venueTab.setIcon(R.drawable.tab_match);
                            messageTab.setIcon(R.drawable.tab_message);
                        }
                        getActionBar().setSelectedNavigationItem(position);
                    }
                });
        new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When swiping between pages, select the corresponding tab.
                getActionBar().setSelectedNavigationItem(position);
            }
        };

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            if(extras.getBoolean("first_time")) {
                mViewPager.setCurrentItem(0); // Set view page to Profile on first time login
            }
        }
    }



    // Pager adapter that contains the different fragments necessary for navigating through the tabs
    public class DemoCollectionPagerAdapter extends FragmentStatePagerAdapter {

        public DemoCollectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment;
            switch (i){
                case 0: fragment = new ProfileFragment(); break;    // Set tab 0 --> Profile
                case 1: fragment = new VenueFragment(); break;      // Set tab 1 --> VenueFragment
                case 2: fragment = new MessageFragment(); break;    // Set tab 2 --> MessageFragment
                default: fragment = new ProfileFragment(); break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }

    }
}




