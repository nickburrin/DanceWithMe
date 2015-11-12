package seniordesign.com.dancewithme.fragments;

import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import seniordesign.com.dancewithme.R;
import seniordesign.com.dancewithme.activities.MessagingActivity;
import seniordesign.com.dancewithme.activities.MyApplication;
import seniordesign.com.dancewithme.adapters.MessageUserListAdapter;
import seniordesign.com.dancewithme.pojos.Matches;


public class MessageFragment extends Fragment {
    private static final String TAG = MessageFragment.class.getSimpleName();
//<<<<<<< HEAD
//    private String currentUserId;
//    private MessageUserListAdapter namesArrayAdapter;
//    private MessageUserListAdapter favoritesArrayAdapter;
//=======
    //List<ParseUser> userMatches;
    private MessageUserListAdapter namesArrayAdapter;
//>>>>>>> master
    private ArrayList<String> names;
    private ArrayList<String> favorites;
    private ListView usersListView;
    private ListView favoritesListView;
    private ProgressDialog progressDialog;
    private BroadcastReceiver receiver = null;
    private View view;
//<<<<<<< HEAD
//    private View convertFavoriteView;
//    private View convertUserView;
    private MyApplication application;
//=======

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_message, container, false);
//<<<<<<< HEAD
//        //convertFavoriteView = inflater.inflate(R.layout.favorite_list_item, container, false);
//        //showSpinner();
//=======
        setConversationsList();
//>>>>>>> master
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setConversationsList();
    }

    private void setConversationsList() {
//<<<<<<< HEAD
//        final ArrayList<ParseUser> userMatches = (ArrayList<ParseUser>) ((Matches) ParseUser.getCurrentUser().get("Matches")).getMatches();
//        names = new ArrayList<String>();
//        final ArrayList<ParseUser> userFavorites = (ArrayList<ParseUser>) ParseUser.getCurrentUser().get("Favorites");
//        favorites = new ArrayList<String>();
//=========
        ParseQuery<Matches> matchQuery = ParseQuery.getQuery("Matches");
        matchQuery.whereEqualTo("userId", ParseUser.getCurrentUser().getObjectId());

        List<ParseUser> userMatches = null;
        try {
            userMatches = matchQuery.getFirst().getMatches();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        names = new ArrayList<>();




        //User Matches
        for(ParseUser i: userMatches) {
            try {
                i.fetchIfNeeded();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            names.add(i.getString("first_name"));
        }

        namesArrayAdapter = new MessageUserListAdapter(getActivity().getApplicationContext(),
                (ArrayList<ParseUser>) userMatches, application, false);
        usersListView = (ListView) view.findViewById(R.id.lv_user_list);

        usersListView.setAdapter(namesArrayAdapter);

        usersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int i, long l) {
                // Object item = gamesListAdapter.getItem(position);
                openConversation(i);
                Log.d(TAG, "you clicked the message");
            }
        });
        usersListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> a, View v, int i, long l) {
                ParseQuery<Matches> matchQuery = ParseQuery.getQuery("Matches");
                matchQuery.whereEqualTo("userId", ParseUser.getCurrentUser().getObjectId());
                Matches userMatches = null;
                try {
                    userMatches = matchQuery.getFirst();
                    userMatches.getMatches().remove(namesArrayAdapter.getItem(i));
                    userMatches.saveInBackground();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                    //THe following part is currently not working
//                ParseQuery<Matches> matchQuery2 = ParseQuery.getQuery("Matches");
//                matchQuery.whereEqualTo("userId", namesArrayAdapter.getItem(i).getObjectId());
//                Matches userMatches2 = null;
//                try {
//                    userMatches2 = matchQuery2.getFirst();
//                    userMatches2.getMatches().remove(ParseUser.getCurrentUser());
//                    userMatches2.saveInBackground();
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//                //userMatches.remove(i);
//
//                ParseUser.getCurrentUser().saveInBackground();
//                setConversationsList();
                return false;
            }
        });
    }

    // Open a conversation with one person
    //  public void openConversation(ArrayList<String> names, int pos) {
    public void openConversation(int pos) {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("first_name", names.get(pos));
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> user, com.parse.ParseException e) {
                if (e == null) {
                    Intent intent = new Intent(getActivity().getApplicationContext(), MessagingActivity.class);
                    intent.putExtra("RECIPIENT_ID", user.get(0).getObjectId());
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Error finding that user", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        setConversationsList();
    }

    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);

        if (getActivity() != null) {
            if (visible) {
                setConversationsList();
            }
        }
    }

    @Override
    public void onPause(){
        super.onPause();
    }
}