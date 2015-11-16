package seniordesign.com.dancewithme.activities;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SendCallback;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;

import seniordesign.com.dancewithme.R;
import seniordesign.com.dancewithme.pojos.DanceStyle;
import seniordesign.com.dancewithme.pojos.Dancehall;
import seniordesign.com.dancewithme.pojos.Dislikes;
import seniordesign.com.dancewithme.pojos.Likes;
import seniordesign.com.dancewithme.pojos.Matches;
import seniordesign.com.dancewithme.utils.Logger;


public class MatchActivity extends Activity {
    private static final String TAG = MatchActivity.class.getSimpleName();
    private String[] BEGINNER_MAPPING = {"Beginner", "Intermediate", "Expert"};
    private String[] INTERMEDIATE_MAPPING = {"Intermediate", "Expert", "Beginner"};
    private String[] EXPERT_MAPPING = {"Expert", "Intermediate", "Beginner"};

    private Dancehall venue;
    private ParseUser matchUser;
    private LinkedList<ParseUser> namesQueue;

    private ImageButton profPic;
    private TextView mNameText;
    private TextView mSkillText;


    public MatchActivity() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout for this fragment
        setContentView(R.layout.activity_match);

        String venueId;
        if((venueId = getIntent().getExtras().getString("venueId")) != null){
            ParseQuery<Dancehall> query = ParseQuery.getQuery("Dancehall");
            query.whereEqualTo("objectId", venueId);
            try {
                venue = query.getFirst();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else{
            Logger.d(TAG, "ERROR: Could not retrieve venueId from extras");
        }

        setConversationsList();

        mNameText = (TextView) findViewById(R.id.nameText);
        mSkillText = (TextView) findViewById(R.id.skillText);
        profPic = (ImageButton) findViewById(R.id.ib_profPic);

        findViewById(R.id.acceptButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logger.d(TAG, "Accept Button Clicked");
                acceptButton();
                getNextUser();
                if(fillPage() == false){
                    Toast.makeText(getApplicationContext(), "It looks like you've seen everyone attending, come back later!", Toast.LENGTH_LONG).show();

                    try {
                        // Wait half a second before exiting
                        Thread.sleep(500);
                    } catch(InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }

                    finish();
                }
            }
        });

        findViewById(R.id.denyButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logger.d(TAG, "Deny Button Clicked");
                denyButton();
                getNextUser();
                if(fillPage() == false){
                    Toast.makeText(getApplicationContext(), "It looks like you've seen everyone attending, come back later!", Toast.LENGTH_LONG).show();

                    try {
                        // Wait half a second before exiting
                        Thread.sleep(500);
                    } catch(InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }

                    finish();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        setConversationsList();
        getNextUser();

        if(fillPage() == false){
            Toast.makeText(this, "It looks like you've seen everyone attending, come back later!", Toast.LENGTH_LONG).show();

            try {
                // Wait half a second before exiting
                Thread.sleep(500);
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }

            finish();
        }
    }

    private void setConversationsList() {
        // Remove this User, Dislikes, and Likes from this group
        ArrayList<ParseUser> attendees = new ArrayList<>(venue.getAttendees());
        attendees.remove(ParseUser.getCurrentUser());

        Dislikes d = (Dislikes) ParseUser.getCurrentUser().get("Dislikes");
        attendees.removeAll(d.getDislikesArray());
        Likes l = (Likes) ParseUser.getCurrentUser().get("Likes");
        attendees.removeAll(l.getLikesArray());

        namesQueue = sortUsers(attendees);
    }

    private LinkedList<ParseUser> sortUsers(ArrayList<ParseUser> attendees) {
        LinkedList<ParseUser> temp = new LinkedList<>();

        if(attendees.size() > 0) {
            DanceStyle style = (DanceStyle) ParseUser.getCurrentUser().get(venue.getStyle());
            String myGender = ParseUser.getCurrentUser().getString("gender");

            if (style.getPreferences().isEmpty()) {
                // Current User has no preferences, sort according to predefined requirements
                if (style.getSkill().equals("Beginner")) {
                    // Current user is a Beginner, sort based on following order: Beg, Int, Exp
                    for (String skill : BEGINNER_MAPPING) {
                        for (ParseUser user : attendees) {
                            try{
                                user.fetchIfNeeded();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            // Only add this User if their skill matches and their of the opposite gender
                            try {
                                if (((DanceStyle) user.fetchIfNeeded().get(venue.getStyle())).getSkill().equals(skill) && !user.fetchIfNeeded().getString("gender").equals(myGender)) {
                                    temp.add(user);
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } else if (style.getSkill().equals("Intermediate")) {
                    // Current user is a Intermediate, sort based on following order: Int, Exp, Beg
                    for (String skill : INTERMEDIATE_MAPPING) {
                        for (ParseUser user : attendees) {
                            try{
                                user.fetchIfNeeded();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            // Only add this User if their skill matches and their of the opposite gender
                            if (((DanceStyle) user.get(venue.getStyle())).getSkill().equals(skill) && !user.getString("gender").equals(myGender)) {
                                temp.add(user);
                            }
                        }
                    }
                } else {
                    // Current user is an Expert, sort based on following order: Exp, Int, Beg
                    for (String skill : EXPERT_MAPPING) {
                        for (ParseUser user : attendees) {
                            try{
                                user.fetchIfNeeded();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            // Only add this User if their skill matches and their of the opposite gender
                            if (((DanceStyle) user.get(venue.getStyle())).getSkill().equals(skill) && !user.getString("gender").equals(myGender)) {
                                temp.add(user);
                            }
                        }
                    }
                }
            } else {
                // Current User has preferences, sort according to their preferences
                ArrayList<String> prefs = style.getPreferences();

                for (String skill : prefs) {
                    for (ParseUser user : attendees) {
                        // Only add this User if their skill matches and their of the opposite gender
                        try{
                            user.fetchIfNeeded();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        try {
                            if (((DanceStyle) user.fetchIfNeeded().get(venue.getStyle())).getSkill().equals(skill) && !user.fetchIfNeeded().getString("gender").equals(myGender)) {
                                temp.add(user);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        return temp;
    }

    private void getNextUser(){
        if(namesQueue.size() > 0){
            matchUser = namesQueue.remove();
        } else{
            matchUser = null;
        }
    }

    private boolean fillPage() {
        if(matchUser != null){
            try {
                ParseFile profilePic = (ParseFile) matchUser.get("ProfilePicture");

                if (profilePic != null) {
                    Bitmap bm = BitmapFactory.decodeByteArray(profilePic.getData(), 0, profilePic.getData().length);
                    profPic.setImageBitmap(bm);
                }
                else{
                    if(matchUser.getString("gender").equals("Male")){
                        profPic.setImageDrawable(getResources().getDrawable(R.drawable.blank_avatar_male));
                    } else{
                        profPic.setImageDrawable(getResources().getDrawable(R.drawable.blank_avatar_female));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            mNameText.setText(matchUser.getString("first_name"));
            mSkillText.setText(((DanceStyle) matchUser.get(venue.getStyle())).getSkill());

            return true;
        }
        else {
            profPic.setImageDrawable(getResources().getDrawable(R.drawable.blank_avatar_male));
            mNameText.setText(null);
            mSkillText.setText(null);

            return false;
        }
    }

    private void acceptButton() {
        // Get this User and add it to my "Likes" array
        Likes l = (Likes) ParseUser.getCurrentUser().get("Likes");
        l.addLike(matchUser);

        if(((Likes) matchUser.get("Likes")).getLikesArray().contains(ParseUser.getCurrentUser())){
            // There is a like situation (FYI both of these lines saveInBackground for Matches class; no need to call it explicitly)
            ((Matches) ParseUser.getCurrentUser().get("Matches")).addMatch(matchUser);
            ((Matches) matchUser.get("Matches")).addMatch(ParseUser.getCurrentUser());

            //send a match push notification to other user
            JSONObject otherData = new JSONObject();
            String matchMessage = "You just got matched with " + ParseUser.getCurrentUser().get("first_name");
            try {
                otherData.put("alert", matchMessage);
                otherData.put("title", "DanceWithMe");
                otherData.put("from", ParseUser.getCurrentUser().getObjectId());
            }catch (Exception e){
                e.printStackTrace();
                return;
            }

            ParseQuery otherQuery = ParseInstallation.getQuery();
            otherQuery.whereEqualTo("username", matchUser.getUsername());

            ParsePush otherPush = new ParsePush();
            otherPush.setQuery(otherQuery);
            otherPush.setData(otherData);
            otherPush.sendInBackground(new SendCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        Log.d("push", "The push campaign to matched user has been created.");
                    } else {
                        Log.d("push", "Error sending push:" + e.getMessage());
                    }
                }
            });

            //send a match push notification to myself
            JSONObject meData = new JSONObject();
            String meMessage = "You just got matched with " + matchUser.get("first_name");
            try {
                meData.put("alert", meMessage);
                meData.put("title", "DanceWithMe");
                meData.put("from", matchUser.getObjectId());
            } catch (Exception e){
                e.printStackTrace();
                return;
            }

            ParseQuery meQuery = ParseInstallation.getQuery();
            meQuery.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
            ParsePush mePush = new ParsePush();
            mePush.setQuery(meQuery);
            mePush.setData(meData);
            mePush.sendInBackground(new SendCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        Log.d("push", "The push campaign to self has been created.");
                    } else {
                        Log.d("push", "Error sending push:" + e.getMessage());
                    }
                }
            });
        }
        ParseUser.getCurrentUser().saveInBackground();
    }

    private void denyButton(){
        // Grab the user you just denied and add it to Dislikes
        Dislikes d = (Dislikes) ParseUser.getCurrentUser().get("Dislikes");
        d.addDislike(matchUser);
    }
}