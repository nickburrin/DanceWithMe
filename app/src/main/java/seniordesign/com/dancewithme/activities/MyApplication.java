package seniordesign.com.dancewithme.activities;
import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;

import com.parse.PushService;
import com.parse.ParseObject;

import com.facebook.FacebookSdk;

import seniordesign.com.dancewithme.pojos.DanceStyle;
import seniordesign.com.dancewithme.pojos.Dancehall;
import seniordesign.com.dancewithme.pojos.Event;
import seniordesign.com.dancewithme.pojos.Matches;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        ParseObject.registerSubclass(DanceStyle.class);
        ParseObject.registerSubclass(Dancehall.class);
        ParseObject.registerSubclass(Matches.class);
        ParseObject.registerSubclass(Event.class);
        Parse.initialize(this, "Q7azOG47hd1mk0cPmwhUTm3DKcKapSHQepzNdSrd", "NA1kTUjHQqc2vbLDp5ywGvCBMGtLp6EKsAc91nxT");
        ParseInstallation.getCurrentInstallation().saveInBackground();
        // Specify an Activity to handle all pushes by default.
        PushService.setDefaultPushCallback(this, PushActivity.class);

        FacebookSdk.sdkInitialize(this);
    }
}
