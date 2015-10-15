package seniordesign.com.dancewithme.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
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
import com.parse.SaveCallback;
import com.parse.SendCallback;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import seniordesign.com.dancewithme.R;
import seniordesign.com.dancewithme.activities.HomeActivity;
import seniordesign.com.dancewithme.activities.LoginActivity;
import seniordesign.com.dancewithme.activities.MessageService;
import seniordesign.com.dancewithme.pojos.UserMatches;
import seniordesign.com.dancewithme.utils.Logger;


public class MatchFragment extends HomeTabFragment {
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
    //private ParseUser myDislikedUser;
    private static final String TAG = HomeActivity.class.getSimpleName();;

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


        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereNotEqualTo("objectId", currentUserId);
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> userList, com.parse.ParseException e) {
                if (e == null) {
                    for (int i = 0; i < userList.size(); i++) {
                        names.add(userList.get(i));
                    }

                } else {
                    Toast.makeText(activity.getApplicationContext(),
                            "Error loading user list",
                            Toast.LENGTH_LONG).show();
                }
                //names = (ArrayList<ParseUser>) userList;
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
//        mNameText.setText(names.get(0).get("first_name") + " ");
        profPic = (ImageButton) view.findViewById(R.id.ib_profPic);
        acceptButton = (ImageButton) view.findViewById(R.id.acceptButton);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logger.d(TAG, "Accept Button Clicked");
                ArrayList<String> myLikes = (ArrayList<String>) ParseUser.getCurrentUser().get("Likes");
                ArrayList<String> myMatches = (ArrayList<String>) ParseUser.getCurrentUser().get("Matches");
                //myLikes.add(names.get(0));
                ParseUser likedUser = names.get(0);
                ArrayList<ParseObject> likedUserLikes = (ArrayList<ParseObject>)likedUser.get("Likes");

                ArrayList<ParseObject> likedUserDislikes = (ArrayList<ParseObject>)likedUser.get("Dislikes");                // set up our query for a User object
                ParseQuery<ParseUser> userQuery = likedUser.getQuery();

                userQuery.include("Likes");
                userQuery.include("Dislikes");
                if(!likedUserDislikes.contains(ParseUser.getCurrentUser().getUsername())){
                    if(likedUserLikes.contains(ParseUser.getCurrentUser().getUsername())){//there is a like situation
                        myMatches.add(likedUser.getUsername());
                        ParseUser.getCurrentUser().saveInBackground();
                        //send a match push notification
                        //JSONObject json = new JSONObject();
                        JSONObject data = new JSONObject();
                        String matchMessage = "You just got matched with " + ParseUser.getCurrentUser().get("first_name");
                        try {
                            data.put("alert", matchMessage);
                            data.put("title", "DanceWithMe");
                            data.put("from", ParseUser.getCurrentUser().getUsername());
                            //json.put("data", data);
                        }catch (Exception e){
                            e.printStackTrace();
                            return;
                        }


//                        ParsePush parsePush = new ParsePush();
//                        parsePush.setData(data);
//
//                        ParseQuery<ParseInstallation> parseQuery = ParseInstallation.getQuery();
//
//                        parseQuery.whereEqualTo("username", likedUser.getUsername());
//                        parsePush.setQuery(parseQuery);
//                        parsePush.sendInBackground(new SendCallback() {
//                            public void done(ParseException e) {
//                                if (e == null) {
//                                    Log.d("push", "The push campaign has been created.");
//                                } else {
//                                    Log.d("push", "Error sending push:" + e.getMessage());
//                                }
//                            }
//                        });

                        ParsePush parsePush = new ParsePush();
                        //parsePush.setData(data);

                        ParseQuery parseQuery = ParseInstallation.getQuery();
                        parseQuery.whereEqualTo("username", likedUser.getUsername());
                        parsePush.setQuery(parseQuery);
                        parsePush.setData(data);
                        (ArrayList<String>) UserMatches.get("username");
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
                        myLikes.add(likedUser.getUsername());
                        ParseUser.getCurrentUser().saveInBackground();
                    }
                }
                //add user matches
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Matches");
                query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
                try {
                    existingStyle = (UserMatches) query.getFirst();
                    skillLevel = existingStyle.getSkill();
                    prefs = existingStyle.getPreferences();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                names.remove(0);
                ParseUser matchUser = names.get(0);
                ParseFile profilePic = (ParseFile) matchUser.get("ProfilePicture");
                if(profilePic != null) {
                    try {
                        bm = BitmapFactory.decodeByteArray(profilePic.getData(), 0, profilePic.getData().length);
                        profPic.setImageBitmap(bm);
                    } catch (com.parse.ParseException e) {
                        //Toast.makeText(this.activity.getApplicationContext(), "No profile pic", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
                mNameText.setText((String) matchUser.get("first_name"));


            }
        });

        denyButton = (ImageButton) view.findViewById(R.id.denyButton);
        denyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDislikes = (ArrayList<String>) ParseUser.getCurrentUser().get("Dislikes");///get the current User's dislikes
                // myDislikes.put(names.get(0));
                dislikedUser = names.get(0);//grab the user you just denied


                //get denied users Likes and Dislikes
                dislikedUserLikes = (ArrayList<String>) dislikedUser.get("Likes");
                dislikedUserDislikes = (ArrayList<String>) dislikedUser.get("Dislikes");

                //Probably not going to use this part of the code -- doesn't buy us anything (can't access users dislikes
                // set up our query for a User object
                ParseQuery<ParseUser> userQueryLikes = dislikedUser.getQuery();
                userQueryLikes.include("Likes");
                userQueryLikes.whereEqualTo("Likes", ParseUser.getCurrentUser().getUsername());
                userQueryLikes.findInBackground(new FindCallback<ParseUser>() {
                    public void done(List<ParseUser> likeList, ParseException e) {
                        if (!likeList.isEmpty()) {
                            //dislikedUser.remove("Dislikes");
                            dislikedUserLikes.remove(ParseUser.getCurrentUser().getUsername());
                            //dislikedUser.put("Dislikes", disli)
                            dislikedUser.saveInBackground();
                            //dislikedUser.
                        }
                    }
                });
                if (dislikedUserDislikes.contains(ParseUser.getCurrentUser().getUsername())) {
                    // dislikedUser.remove("Likes");
                    dislikedUserDislikes.remove(ParseUser.getCurrentUser().getUsername());
                    dislikedUser.put("Likes", dislikedUserDislikes);
                    dislikedUser.saveInBackground();
                } else {
                    myDislikes.add(dislikedUser.getUsername());
                    ParseUser.getCurrentUser().saveInBackground();
                }
                names.remove(0);
                ParseUser matchUser = names.get(0);
                ParseFile profilePic = (ParseFile) matchUser.get("ProfilePicture");
                if(profilePic != null) {
                    try {
                        bm = BitmapFactory.decodeByteArray(profilePic.getData(), 0, profilePic.getData().length);
                        profPic.setImageBitmap(bm);
                    } catch (com.parse.ParseException e) {
                        //Toast.makeText(this.activity.getApplicationContext(), "No profile pic", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
                mNameText.setText(names.get(0).get("first_name") + " ");

            }
        });

        return view;

    }



    @Override
    public void onResume() {
        setConversationsList();
        super.onResume();
    }

}
