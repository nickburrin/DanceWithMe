package seniordesign.com.dancewithme.activities;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.parse.ParseException;
import com.parse.ParsePushBroadcastReceiver;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import seniordesign.com.dancewithme.helper.NotificationUtils;
import seniordesign.com.dancewithme.pojos.Matches;
import seniordesign.com.dancewithme.utils.Logger;

import seniordesign.com.dancewithme.R;

public class MyPushReceiver extends ParsePushBroadcastReceiver {
    private final String TAG = MyPushReceiver.class.getSimpleName();

    private NotificationUtils notificationUtils;

    private Intent parseIntent;

    public MyPushReceiver() {
        super();
    }

    @Override
    public void onPushReceive(Context context, Intent intent) {

        super.onPushReceive(context, intent);

        if (intent == null)
            return;

        try {
            JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));

            Log.e(TAG, "Push received: " + json);

            parseIntent = intent;

            parsePushJson(context, json);

        } catch (JSONException e) {
            Log.e(TAG, "Push message json exception: " + e.getMessage());
        }

    }

    @Override
    protected void onPushDismiss(Context context, Intent intent) {
        super.onPushDismiss(context, intent);
    }

    @Override
    protected void onPushOpen(Context context, Intent intent) {
        super.onPushOpen(context, intent);
    }

    /**
     * Parses the push notification json
     *
     * @param context
     * @param json
     */
    private void parsePushJson(Context context, JSONObject json) {
        try {
            //boolean isBackground = json.getBoolean("is_background");
            //JSONObject data = json.getJSONObject("data");
            String title = json.getString("title");
            String message = json.getString("message");
            //String message = "";
            String from = json.getString("from");

            //ParseUser myUser = ParseUser.getCurrentUser();
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereEqualTo("objectId", from);
            ParseUser matchedUser = (ParseUser) query.getFirst();

            ParseQuery<Matches> matchQuery = ParseQuery.getQuery("Matches");
            matchQuery.whereEqualTo("userId", ParseUser.getCurrentUser().getObjectId());
            Matches userMatches = matchQuery.getFirst();
            userMatches.addMatch(matchedUser);


            Intent resultIntent = new Intent(context, HomeActivity.class);
            showNotificationMessage(context, title, message, resultIntent);

        } catch (JSONException e) {
            Log.e(TAG, "Push message json exception: " + e.getMessage());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    /**
     * Shows the notification message in the notification bar
     * If the app is in background, launches the app
     *
     * @param context
     * @param title
     * @param message
     * @param intent
     */
    private void showNotificationMessage(Context context, String title, String message, Intent intent) {

        notificationUtils = new NotificationUtils(context);

        intent.putExtras(parseIntent.getExtras());

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        notificationUtils.showNotificationMessage(title, message, intent);
    }
}
