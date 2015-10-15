package seniordesign.com.dancewithme.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import seniordesign.com.dancewithme.activities.HomeActivity;
import seniordesign.com.dancewithme.activities.MyApplication;

public class HomeTabFragment extends Fragment {
    HomeActivity activity;
    MyApplication application;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (HomeActivity) getActivity();
        application = (MyApplication) activity.getApplication();
    }

}
