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

import com.parse.GetCallback;
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
import seniordesign.com.dancewithme.pojos.Matches;
import seniordesign.com.dancewithme.utils.Logger;


public class MatchActivity extends Activity {
    private static final String TAG = MatchActivity.class.getSimpleName();
    private String[] BEGINNER_MAPPING = {"Beginner", "Intermediate", "Expert"};
    private String[] INTERMEDIATE_MAPPING = {"Intermediate", "Expert", "Beginner"};
    private String[] EXPERT_MAPPING = {"Expert", "Intermediate", "Beginner"};

    private Dancehall venue;
    private ParseUser matchUser;
    private ArrayList<ParseUser> attendees;
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
                fillPage();
            }
        });

        findViewById(R.id.denyButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logger.d(TAG, "Deny Button Clicked");
                denyButton();
                getNextUser();
                fillPage();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        //
        if(setConversationsList() == true){
            getNextUser();
            fillPage();
        } else{
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

    private boolean setConversationsList() {
        /*
            This is good, but I think we can optimize it with a few things:
                2) ****** DONE ******
                    Once you have a list of all the users, you can immediately
                    remove those users who you already matched to or dislike.
                    This then would be the array you sort based on preferences.
                     i.e.
                        attendees = (ArrayList<ParseUser>) resultFromQuery;
                        attendees.removeAll(user.get("Dislikes"));
                        attendees.removeAll(user.get("Matches"));
                3) Alaap was right, we can use a Queue, which allows us to
                    pull and remove the next user (the head of queue) in the
                    same method call Queue.poll(); The only thing we would
                    have to do is sort the Users and add them to the Queue
                    in correct order (which we would have to do with an
                    ArrayList anyways
         */

        // Get all Users of opposite sex that are not me

        // Remove this User, Dislikes, and Likes from this group
        ArrayList<ParseUser> attendees = new ArrayList<>(venue.getAttendees());
        attendees.remove(ParseUser.getCurrentUser());
        attendees.removeAll(ParseUser.getCurrentUser().<ParseUser>getList("Dislikes"));
        attendees.removeAll(ParseUser.getCurrentUser().<ParseUser>getList("Likes"));

        if(attendees.size() == 0){
            return false;
        }

        namesQueue = sortUsers(attendees);
        return true;
    }

    private LinkedList<ParseUser> sortUsers(ArrayList<ParseUser> attendees) {
        LinkedList<ParseUser> temp = new LinkedList<>();

        DanceStyle style = (DanceStyle) ParseUser.getCurrentUser().get(venue.getStyle());
        String myGender = ParseUser.getCurrentUser().getString("gender");

        if(style.getPreferences().isEmpty()){
            // Current User has no preferences, sort according to predefined requirements
            if(style.getSkill().equals("Beginner")){
                // Current user is a Beginner, sort based on following order: Beg, Int, Exp
                for(String skill: BEGINNER_MAPPING) {
                    for (ParseUser user : attendees) {
                        // Only add this User if their skill matches and their of the opposite gender
                        if(((DanceStyle)user.get(venue.getStyle())).getSkill().equals(skill) && !user.getString("gender").equals(myGender)){
                            temp.add(user);
                        }
                    }
                }
            } else if(style.getSkill().equals("Intermediate")){
                // Current user is a Intermediate, sort based on following order: Int, Exp, Beg
                for(String skill: INTERMEDIATE_MAPPING) {
                    for (ParseUser user : attendees) {
                        // Only add this User if their skill matches and their of the opposite gender
                        if(((DanceStyle)user.get(venue.getStyle())).getSkill().equals(skill) && !user.getString("gender").equals(myGender)){
                            temp.add(user);
                        }
                    }
                }
            } else {
                // Current user is an Expert, sort based on following order: Exp, Int, Beg
                for(String skill: EXPERT_MAPPING) {
                    for (ParseUser user : attendees) {
                        // Only add this User if their skill matches and their of the opposite gender
                        if(((DanceStyle)user.get(venue.getStyle())).getSkill().equals(skill) && !user.getString("gender").equals(myGender)){
                            temp.add(user);
                        }
                    }
                }
            }
        } else {
            // Current User has preferences, sort according to their preferences
            ArrayList<String> prefs = style.getPreferences();

            for(String skill: prefs){
                for(ParseUser user: attendees){
                    // Only add this User if their skill matches and their of the opposite gender

                    try {
                        if (((DanceStyle) user.fetchIfNeeded().get(venue.getStyle())).getSkill().equals(skill) && !user.fetchIfNeeded().getString("gender").equals(myGender)) {
                            temp.add(user);
                        }
                    }catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return temp;
    }

    private void acceptButton() {
        // Get this User and add it to my "Likes" array
        ParseUser.getCurrentUser().addUnique("Likes", matchUser);

        if(matchUser.getList("Likes").contains(ParseUser.getCurrentUser())){
            // There is a like situation (FYI both of these lines saveInBackground; no need to call it explicitly)
            ((Matches) ParseUser.getCurrentUser().get("Matches")).addMatch(matchUser);
            ((Matches) matchUser.get("Matches")).addMatch(ParseUser.getCurrentUser());

            //send a match push notification to other user
            JSONObject data = new JSONObject();
            String matchMessage = "You just got matched with " + ParseUser.getCurrentUser().get("first_name");
            try {
                data.put("alert", matchMessage);
                data.put("title", "DanceWithMe");
                data.put("from", ParseUser.getCurrentUser().getObjectId());
                //json.put("data", data);
            }catch (Exception e){
                e.printStackTrace();
                return;
            }

            ParseQuery parseQuery = ParseInstallation.getQuery();
            parseQuery.whereEqualTo("username", matchUser.getUsername());

            ParsePush parsePush = new ParsePush();
            parsePush.setQuery(parseQuery);
            parsePush.setData(data);
            parsePush.sendInBackground(new SendCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        Log.d("push", "The push campaign has been created.");
                    } else {
                        Log.d("push", "Error sending push:" + e.getMessage());
                    }
                }
            });

            //send a match push notification to myself
            JSONObject data1 = new JSONObject();
            String matchMessage1 = "You just got matched with " + matchUser.get("first_name");
            try {
                data1.put("alert", matchMessage1);
                data1.put("title", "DanceWithMe");
                data1.put("from", matchUser.getObjectId());
                //json.put("data", data);
            }catch (Exception e){
                e.printStackTrace();
                return;
            }

            ParseQuery parseQuery1 = ParseInstallation.getQuery();
            parseQuery.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());

            ParsePush parsePush1 = new ParsePush();
            parsePush1.setQuery(parseQuery1);
            parsePush1.setData(data1);
            //parsePush.setMessage("Lets turn up tone");
            parsePush1.sendInBackground(new SendCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        Log.d("push", "The push campaign has been created.");
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
        ParseUser.getCurrentUser().addUnique("Dislikes", matchUser);
        ParseUser.getCurrentUser().saveInBackground();
    }

    private void getNextUser(){
        if(namesQueue.size() > 0){
            matchUser = namesQueue.remove();
        }
//        if(attendees.size() > 0){
//            matchUser = attendees.get(0);
//            attendees.remove(0);
//        }
    }

    private void fillPage() {
        try {
            ParseFile profilePic = (ParseFile) matchUser.get("ProfilePicture");

            if (profilePic != null) {
                Bitmap bm = BitmapFactory.decodeByteArray(profilePic.getData(), 0, profilePic.getData().length);
                profPic.setImageBitmap(bm);
            }
            else{
                profPic.setImageDrawable(getResources().getDrawable(R.drawable.android_robot));
            }
        } catch (Exception e) {
            //Toast.makeText(this.activity.getApplicationContext(), "No profile pic", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        mNameText.setText(matchUser.getString("first_name"));
        // DanceStyle matchUserDanceStyle = new DanceStyle();
        mSkillText.setText(((DanceStyle) matchUser.get(venue.getStyle())).getSkill());
    }
}