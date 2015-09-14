//package seniordesign.com.dancewithme.activities;
//
//import android.app.ActionBar;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentActivity;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentStatePagerAdapter;
//import android.support.v4.view.ViewPager;
//import android.os.Bundle;
//
//import seniordesign.com.dancewithme.R;
//import seniordesign.com.dancewithme.activities.activities.fragments.ProfileFragment;
//import seniordesign.com.dancewithme.activities.activities.fragments.MessageFragment;
//import seniordesign.com.dancewithme.activities.activities.fragments.MatchFragment;
//
//
//
//public class HomeActivity extends FragmentActivity {
//    private static final String TAG = HomeActivity.class.getSimpleName();
//
//    // Private class params
//    private ActionBar.Tab profileTab, gamesTab, betsTab;
//
//
//    HomeActivity activity;
//    MyApplication application;
//    //User user;
//
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        //setContentView(R.layout.activity_games);
//        activity = this;
//        application = (MyApplication) activity.getApplication();
//        user = DataStore.getInstance().getUser();
//
//
//        // Get layout params
//        ViewPager mViewPager = (ViewPager) findViewById(R.id.view_pager);
//        ActionBar actionBar = getActionBar();
//        // Init the action bar
//        if(actionBar!=null){
//            actionBar.setDisplayShowHomeEnabled(false);  // hides action bar icon
//            actionBar.setDisplayShowTitleEnabled(false); // hides action bar title
//            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
//
//            // initialize tabs
//            profileTab = actionBar.newTab();
//            gamesTab = actionBar.newTab();
//            betsTab = actionBar.newTab();
//
//            //add the icons to the tabs
//            profileTab.setIcon(R.drawable.tab_account);
//            gamesTab.setIcon(R.drawable.tab_games_sel);
//            betsTab.setIcon(R.drawable.tab_bets);
//
//            //listener for change in tabs
//            MyTabListener mListener = new MyTabListener(mViewPager);
//
//            //every tab gets a tab listener
//            profileTab.setTabListener(mListener);
//            gamesTab.setTabListener(mListener);
//            betsTab.setTabListener(mListener);
//
//            //add tabs to the bar
//            actionBar.addTab(profileTab, 0, false);
//            actionBar.addTab(gamesTab, 1, true);
//            actionBar.addTab(betsTab, 2, false);
//
//            actionBar.setSelectedNavigationItem(1);
//
//        } else {
//            finish();
//        }
//
//        // Initialize the view pager & pager adapter
//        DemoCollectionPagerAdapter mDemoCollectionPagerAdapter;
//        mDemoCollectionPagerAdapter =
//                new DemoCollectionPagerAdapter(
//                        getSupportFragmentManager());
//        mViewPager.setAdapter(mDemoCollectionPagerAdapter);
//        mViewPager.setOnPageChangeListener(
//                new ViewPager.SimpleOnPageChangeListener() {
//                    @Override
//                    public void onPageSelected(int position) {
//                        // When swiping between pages, select the
//                        // corresponding tab.
//                        if (position == 0){
//                            profileTab.setIcon(R.drawable.tab_account_sel);
//                            gamesTab.setIcon(R.drawable.tab_games);
//                            betsTab.setIcon(R.drawable.tab_bets);
//                        }
//                        else if (position == 1){
//                            profileTab.setIcon(R.drawable.tab_account);
//                            gamesTab.setIcon(R.drawable.tab_games_sel);
//                            betsTab.setIcon(R.drawable.tab_bets);
//                        }
//                        else if (position == 2){
//                            profileTab.setIcon(R.drawable.tab_account);
//                            gamesTab.setIcon(R.drawable.tab_games);
//                            betsTab.setIcon(R.drawable.tab_bets_sel);
//                        }
//                        getActionBar().setSelectedNavigationItem(position);
//                    }
//                });
//        new ViewPager.SimpleOnPageChangeListener() {
//            @Override
//            public void onPageSelected(int position) {
//                // When swiping between pages, select the                        // corresponding tab.
//                getActionBar().setSelectedNavigationItem(position);
//            }
//        };
//        mViewPager.setCurrentItem(1); // Set default view page to games
//
//
//        // Set broadcast listener for logout (
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction("com.package.ACTION_LOGOUT");
//        registerReceiver(new BroadcastReceiver() {
//
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                //At this point you should start the login activity and finish this one
//                finish();
//            }
//        }, intentFilter);
//
//    }
//
//
//
//    // Pager adapter that contains the different fragments necessary for navigating through the tabs
//    public class DemoCollectionPagerAdapter extends FragmentStatePagerAdapter {
//
//        public DemoCollectionPagerAdapter(FragmentManager fm) {
//            super(fm);
//        }
//
//        @Override
//        public Fragment getItem(int i) {
//            Fragment fragment;
//            switch (i){
//                case 0: fragment = new ProfileFragment();break;     // Set tab 0 --> Profile
//                case 1: fragment = new GamesListFragment();break;   // Set tab 1 --> Games List
//                case 2: fragment = new BetListFragment();break;  // Set tab 2 --> Bets List
//                default: fragment = new GamesListFragment();break;
//            }
//            return fragment;
//        }
//
//        @Override
//        public int getCount() {
//            return 3;
//        }
//
//    }
//
//
//
//
//}
//
//
//
//
