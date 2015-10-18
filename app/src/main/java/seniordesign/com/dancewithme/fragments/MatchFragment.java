package seniordesign.com.dancewithme.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import seniordesign.com.dancewithme.R;
import seniordesign.com.dancewithme.pojos.DanceStyle;
import seniordesign.com.dancewithme.pojos.Matches;
import seniordesign.com.dancewithme.utils.Logger;


public class MatchFragment extends HomeTabFragment {
    private static final String TAG = MatchFragment.class.getSimpleName();;

    private ParseUser matchUser;
    private ArrayList<ParseUser> names;
    private LinkedList<ParseUser> namesQueue;

    private ImageButton acceptButton;
    private ImageButton denyButton;
    private ImageButton profPic;
    private TextView mNameText;
    private View view;

    private String eventStyle;

    public MatchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getActivity().getIntent().getExtras();
        if(extras != null){
            eventStyle = extras.getString("eventStyle");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_match, container, false);
        //watch out for this line
//        setConversationsList();
//        ParseUser matchUser = names.get(0);
//        ParseFile profilePic = (ParseFile) matchUser.get("ProfilePicture");
//        if(profilePic != null) {
//            try {
//                bm = BitmapFactory.decodeByteArray(profilePic.getData(), 0, profilePic.getData().length);
//                profPic.setImageBitmap(bm);
//            } catch (com.parse.ParseException e) {
//                Toast.makeText(this.activity.getApplicationContext(), "No profile pic", Toast.LENGTH_LONG).show();
//                e.printStackTrace();
//            }
//        }

        mNameText = (TextView) view.findViewById(R.id.nameText);
        profPic = (ImageButton) view.findViewById(R.id.ib_profPic);

        acceptButton = (ImageButton) view.findViewById(R.id.acceptButton);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logger.d(TAG, "Accept Button Clicked");
                acceptButton();
                getNextUser();
                fillPage();
            }
        });

        denyButton = (ImageButton) view.findViewById(R.id.denyButton);
        denyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logger.d(TAG, "Deny Button Clicked");
                denyButton();
                getNextUser();
                fillPage();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setConversationsList();
        getNextUser();
        fillPage();
    }

    private void setConversationsList() {
        names = new ArrayList<ParseUser>();
        //namesQueue = new LinkedList<ParseUser>();
        /*
            This is good, but I think we can optimize it with a few things:
                1)  ***** DONE ******
                    Instead of storing email Strings in the Likes, Dislikes, and Matches arrays,
                    store the actual ParseUser.
                    i.e.
                        if(ParseUserA likes ParseUserB)
                            ParseUserA.likes.add(ParseUserB);
                2) Once you have a list of all the users, you can immediately
                    remove those users who you already matched to or dislike.
                    This then would be the array you sort based on preferences.
                     i.e.
                        names = (ArrayList<ParseUser>) resultFromQuery;
                        names.removeAll(user.get("Dislikes"));
                        names.removeAll(user.get("Matches"));
                3) Alaap was right, we can use a Queue, which allows us to
                    pull and remove the next user (the head of queue) in the
                    same method call Queue.poll(); The only thing we would
                    have to do is sort the Users and add them to the Queue
                    in correct order (which we would have to do with an
                    ArrayList anyways
                4) ***** DONE ******
                    Move all the onClickListener code into methods called
                    acceptButton(), denyButton(), and reloadPicture() --
                    or something to that effect. This will make code
                    more readable and modular.
         */
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereNotEqualTo("objectId", ParseUser.getCurrentUser().getObjectId());
        try {
            // Get all the attendees and remove Dislikes
            names = (ArrayList<ParseUser>) query.find();
            names.removeAll(ParseUser.getCurrentUser().getList("Dislikes"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        sortUsers(names);
    }

    private void sortUsers(ArrayList<ParseUser> users) {
        namesQueue = new LinkedList<ParseUser>();

        ParseQuery<DanceStyle> styleQuery = ParseQuery.getQuery("DanceStyle");
        styleQuery.whereEqualTo("userId", ParseUser.getCurrentUser().getObjectId());
        styleQuery.whereEqualTo("style", eventStyle);

        DanceStyle style = null;
        try {
            style = styleQuery.getFirst();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // This is a little tricky bc we have to find the specific dancestyle for each
        //  user who is attending this event. Its a lot of work. Not sure how to efficiently
        //  grab it from the array of danceStyles
        if(style.getSkill().equals("Beginner")){
            for(ParseUser user: users){
                //if(user is a beginner, add to the list first)
            }
            for(ParseUser user: users){
                //if(user is a intermediate, add to the list here)
            }
            for(ParseUser user: users){
                //if(user is an expert, add to the list last)
            }
        } else if(style.getSkill().equals("Intermediate")){

        } else {

        }
    }

    private void acceptButton() {
        // Get this User and add it to my "Likes" array
        ParseUser.getCurrentUser().addUnique("Likes", matchUser);

        if(matchUser.getList("Likes").contains(ParseUser.getCurrentUser())){
            // There is a like situation (FYI both of these lines saveInBackground; no need to call it explicitly)
            ((Matches) ParseUser.getCurrentUser().get("Matches")).addMatch(matchUser);
            ((Matches) matchUser.get("Matches")).addMatch(ParseUser.getCurrentUser());
            /*
            Matches myMatches = ((Matches) ParseUser.getCurrentUser().get("Matches"));
            Matches theirMatches = (Matches) matchUser.get("Matches");
            try {
                myMatches.fetchIfNeeded();
                theirMatches.fetchIfNeeded();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            myMatches.getMatches().add(matchUser);
            theirMatches.getMatches().add(ParseUser.getCurrentUser());
            */
            /*
                JUST WANT TO SEE IF THIS WORKS WITHOUT NOTIFICATIONS

            //send a match push notification
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
            parseQuery.whereEqualTo("username", likedUser.getUsername());

            ParsePush parsePush = new ParsePush();
            parsePush.setQuery(parseQuery);
            parsePush.setData(data);
            //parsePush.setMessage("Lets turn up tone");
            parsePush.sendInBackground(new SendCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        Log.d("push", "The push campaign has been created.");
                    } else {
                        Log.d("push", "Error sending push:" + e.getMessage());
                    }
                }
            });
            */
        }

        ParseUser.getCurrentUser().saveInBackground();
    }

    private void denyButton(){
        // Grab the user you just denied and add it to Dislikes
        ParseUser.getCurrentUser().addUnique("Dislikes", matchUser);
        ParseUser.getCurrentUser().saveInBackground();
    }

    private void getNextUser(){
        if(names.size() > 0){
            matchUser = names.get(0);
            names.remove(0);
        }
    }

    private void fillPage() {
        ParseFile profilePic = (ParseFile) matchUser.get("ProfilePicture");
        if(profilePic != null) {
            try {
                Bitmap bm = BitmapFactory.decodeByteArray(profilePic.getData(), 0, profilePic.getData().length);
                profPic.setImageBitmap(bm);
            } catch (ParseException e) {
                //Toast.makeText(this.activity.getApplicationContext(), "No profile pic", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        } else {
            profPic.setImageDrawable(getResources().getDrawable(R.drawable.android_robot));
        }
        mNameText.setText((String) matchUser.get("first_name"));
    }
}


//    private void acceptButton() {
//        ArrayList<String> myLikes = (ArrayList<String>) ParseUser.getCurrentUser().get("Likes");
//        ArrayList<String> myMatches = (ArrayList<String>) ParseUser.getCurrentUser().get("Matches");
//
//        ParseUser likedUser = names.get(0);
//        ArrayList<ParseObject> likedUserLikes = (ArrayList<ParseObject>)likedUser.get("Likes");
//        ArrayList<ParseObject> likedUserDislikes = (ArrayList<ParseObject>)likedUser.get("Dislikes");                // set up our query for a User object
//
//        ParseQuery<ParseUser> userQuery = likedUser.getQuery();
//        userQuery.include("Likes");
//        userQuery.include("Dislikes");
//
//        if(likedUserDislikes.contains(ParseUser.getCurrentUser().getUsername()) == false){
//            if(likedUserLikes.contains(ParseUser.getCurrentUser().getUsername())){//there is a like situation
//                myMatches.add(likedUser.getUsername());
//                ParseUser.getCurrentUser().saveInBackground();
//
//                //send a match push notification
//                JSONObject data = new JSONObject();
//                String matchMessage = "You just got matched with " + ParseUser.getCurrentUser().get("first_name");
//                try {
//                    data.put("alert", matchMessage);
//                    data.put("title", "DanceWithMe");
//                    data.put("from", ParseUser.getCurrentUser().getUsername());
//                    //json.put("data", data);
//                }catch (Exception e){
//                    e.printStackTrace();
//                    return;
//                }
//
//                ParseQuery parseQuery = ParseInstallation.getQuery();
//                parseQuery.whereEqualTo("username", likedUser.getUsername());
//
//                ParsePush parsePush = new ParsePush();
//                parsePush.setQuery(parseQuery);
//                parsePush.setData(data);
//                //parsePush.setMessage("Lets turn up tone");
//                parsePush.sendInBackground(new SendCallback() {
//                    public void done(ParseException e) {
//                        if (e == null) {
//                            Log.d("push", "The push campaign has been created.");
//                        } else {
//                            Log.d("push", "Error sending push:" + e.getMessage());
//                        }
//                    }
//                });
//
//            }else{
//                myLikes.add(likedUser.getUsername());
//                ParseUser.getCurrentUser().saveInBackground();
//            }
//        }
//    }