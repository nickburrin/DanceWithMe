package seniordesign.com.dancewithme.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import seniordesign.com.dancewithme.R;
import seniordesign.com.dancewithme.activities.HomeActivity;
import seniordesign.com.dancewithme.activities.MyApplication;

public class HomeTabFragment extends Fragment {
    HomeActivity activity;
    MyApplication application;
    //User user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (HomeActivity) getActivity();
        application = (MyApplication) activity.getApplication();
        //user = DataStore.getInstance().getUser();
    }

}
