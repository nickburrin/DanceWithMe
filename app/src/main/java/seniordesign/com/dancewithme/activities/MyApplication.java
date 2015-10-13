package seniordesign.com.dancewithme.activities;
import android.app.Application;
import android.content.Intent;

import com.parse.Parse;
import com.parse.ParseInstallation;

import com.parse.ParseUser;
import com.parse.PushService;
import com.parse.ParseObject;

import seniordesign.com.dancewithme.pojos.DanceStyle;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        ParseObject.registerSubclass(DanceStyle.class);
        Parse.initialize(this, "Q7azOG47hd1mk0cPmwhUTm3DKcKapSHQepzNdSrd", "NA1kTUjHQqc2vbLDp5ywGvCBMGtLp6EKsAc91nxT");
//        Parse.enableLocalDatastore(this);
//        Parse.initialize(this, "Q7azOG47hd1mk0cPmwhUTm3DKcKapSHQepzNdSrd", "NA1kTUjHQqc2vbLDp5ywGvCBMGtLp6EKsAc91nxT");
        ParseInstallation.getCurrentInstallation().saveInBackground();
        // Specify an Activity to handle all pushes by default.
        PushService.setDefaultPushCallback(this, PushActivity.class);
    }
}
