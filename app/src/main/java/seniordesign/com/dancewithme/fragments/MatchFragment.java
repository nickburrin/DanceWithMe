package seniordesign.com.dancewithme.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SendCallback;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import seniordesign.com.dancewithme.R;
import seniordesign.com.dancewithme.utils.Logger;


public class MatchFragment extends HomeTabFragment {
    private static final String TAG = MatchFragment.class.getSimpleName();;

    private String currentUserId;
    private ArrayList<ParseUser> names;

    private ImageButton acceptButton;
    private ImageButton denyButton;
    private ImageButton profPic;
    private View view;
    private Bitmap bm = null;
    private ArrayList<String> dislikedUserLikes;
    private ArrayList<String> dislikedUserDislikes;
    private ArrayList<String> myDislikes;
    private ParseUser dislikedUser;
    private TextView mNameText;

    private int nextMatch;

    public MatchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void setConversationsList() {
        currentUserId = ParseUser.getCurrentUser().getObjectId();
        names = new ArrayList<ParseUser>();

        /*
            This is good, but I think we can optimize it with a few things:
                1) Instead of storing email Strings in the Likes, Dislikes, and Matches arrays,
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
                4) Move all the onClickListener code into methods called
                    acceptButton(), denyButton(), and reloadPicture() --
                    or something to that effect. This will make code
                    more readable and modular.
         */
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereNotEqualTo("objectId", currentUserId);
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> userList, com.parse.ParseException e) {
                if (e == null) {
                    // Pretty sure the line below will work and save time
                    //names = (ArrayList<ParseUser>) userList;
                    for (int i = 0; i < userList.size(); i++) {
                        names.add(userList.get(i));
                    }
                } else {
                    Toast.makeText(activity.getApplicationContext(),
                            "Error loading user list",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
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
//                Logger.d(TAG, "Accept Button Clicked");
//<<<<<<< HEAD
//                ArrayList<String> myLikes = (ArrayList<String>) ParseUser.getCurrentUser().get("Likes");
//                ArrayList<String> myMatches = (ArrayList<String>) ParseUser.getCurrentUser().get("Matches");
//                //myLikes.add(names.get(0));
//                ParseUser likedUser = names.get(0);
//                ArrayList<ParseObject> likedUserLikes = (ArrayList<ParseObject>)likedUser.get("Likes");
//
//                ArrayList<ParseObject> likedUserDislikes = (ArrayList<ParseObject>)likedUser.get("Dislikes");                // set up our query for a User object
//                ParseQuery<ParseUser> userQuery = likedUser.getQuery();
//
//                userQuery.include("Likes");
//                userQuery.include("Dislikes");
//                if(!likedUserDislikes.contains(ParseUser.getCurrentUser().getUsername())){
//                    if(likedUserLikes.contains(ParseUser.getCurrentUser().getUsername())){//there is a like situation
//                        myMatches.add(likedUser.getUsername());
//                        ParseUser.getCurrentUser().saveInBackground();
//                        //send a match push notification
//                        //JSONObject json = new JSONObject();
//                        JSONObject data = new JSONObject();
//                        String matchMessage = "You just got matched with " + ParseUser.getCurrentUser().get("first_name");
//                        try {
//                            data.put("alert", matchMessage);
//                            data.put("title", "DanceWithMe");
//                            data.put("from", ParseUser.getCurrentUser().getUsername());
//                            //json.put("data", data);
//                        }catch (Exception e){
//                            e.printStackTrace();
//                            return;
//                        }
//
//
////                        ParsePush parsePush = new ParsePush();
////                        parsePush.setData(data);
////
////                        ParseQuery<ParseInstallation> parseQuery = ParseInstallation.getQuery();
////
////                        parseQuery.whereEqualTo("username", likedUser.getUsername());
////                        parsePush.setQuery(parseQuery);
////                        parsePush.sendInBackground(new SendCallback() {
////                            public void done(ParseException e) {
////                                if (e == null) {
////                                    Log.d("push", "The push campaign has been created.");
////                                } else {
////                                    Log.d("push", "Error sending push:" + e.getMessage());
////                                }
////                            }
////                        });
//
//                        ParsePush parsePush = new ParsePush();
//                        //parsePush.setData(data);
//
//                        ParseQuery parseQuery = ParseInstallation.getQuery();
//                        parseQuery.whereEqualTo("username", likedUser.getUsername());
//                        parsePush.setQuery(parseQuery);
//                        parsePush.setData(data);
//                        (ArrayList<String>) UserMatches.get("username");
//                        //parsePush.setMessage("Lets turn up tone");
//                        parsePush.sendInBackground(new SendCallback() {
//                            public void done(ParseException e) {
//                                if (e == null) {
//                                    Log.d("push", "The push campaign has been created.");
//                                } else {
//                                    Log.d("push", "Error sending push:" + e.getMessage());
//                                }
//                            }
//                        });
//
//
//                    }else{
//                        myLikes.add(likedUser.getUsername());
//                        ParseUser.getCurrentUser().saveInBackground();
//                    }
//                }
//                //add user matches
//                ParseQuery<ParseObject> query = ParseQuery.getQuery("Matches");
//                query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
//                try {
//                    existingStyle = (UserMatches) query.getFirst();
//                    skillLevel = existingStyle.getSkill();
//                    prefs = existingStyle.getPreferences();
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//                names.remove(0);
//                ParseUser matchUser = names.get(0);
//                ParseFile profilePic = (ParseFile) matchUser.get("ProfilePicture");
//                if(profilePic != null) {
//                    try {
//                        bm = BitmapFactory.decodeByteArray(profilePic.getData(), 0, profilePic.getData().length);
//                        profPic.setImageBitmap(bm);
//                    } catch (com.parse.ParseException e) {
//                        //Toast.makeText(this.activity.getApplicationContext(), "No profile pic", Toast.LENGTH_LONG).show();
//                        e.printStackTrace();
//                    }
//                }
//                mNameText.setText((String) matchUser.get("first_name"));
//
//
//=======
                acceptButton();
                getNextUser();
            }
        });

        denyButton = (ImageButton) view.findViewById(R.id.denyButton);
        denyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logger.d(TAG, "Deny Button Clicked");
                denyButton();
                getNextUser();
            }
        });

        return view;
    }

    private void acceptButton() {
        ArrayList<ParseUser> myLikes = (ArrayList<ParseUser>) ParseUser.getCurrentUser().get("Likes");
        ArrayList<ParseUser> myMatches = (ArrayList<ParseUser>) ParseUser.getCurrentUser().get("Matches");

        ParseUser likedUser = names.get(0);
        ArrayList<ParseObject> likedUserLikes = (ArrayList<ParseObject>)likedUser.get("Likes");
        ArrayList<ParseObject> likedUserDislikes = (ArrayList<ParseObject>)likedUser.get("Dislikes");                // set up our query for a User object

        ParseQuery<ParseUser> userQuery = likedUser.getQuery();
        userQuery.include("Likes");
        userQuery.include("Dislikes");

        if(likedUserDislikes.contains(ParseUser.getCurrentUser()) == false){
            if(likedUserLikes.contains(ParseUser.getCurrentUser())){//there is a like situation
                myMatches.add(likedUser);
                ParseUser.getCurrentUser().saveInBackground();

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

            }else{
                myLikes.add(likedUser);
                ParseUser.getCurrentUser().saveInBackground();
            }
        }
    }

    private void denyButton(){
        ParseUser.getCurrentUser().getList("Dislikes").add(dislikedUser);
        ParseUser.getCurrentUser().saveInBackground();
    }

    private void getNextUser(){
        names.remove(0);
        ParseUser matchUser = names.get(0);
        ParseFile profilePic = (ParseFile) matchUser.get("ProfilePicture");
        if(profilePic != null) {
            try {
                bm = BitmapFactory.decodeByteArray(profilePic.getData(), 0, profilePic.getData().length);
                profPic.setImageBitmap(bm);
            } catch (ParseException e) {
                //Toast.makeText(this.activity.getApplicationContext(), "No profile pic", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
        mNameText.setText((String) matchUser.get("first_name"));
    }

    @Override
    public void onResume() {
        setConversationsList();
        super.onResume();
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