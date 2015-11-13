package seniordesign.com.dancewithme.fragments;

import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.parse.ParseException;
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
    private MessageUserListAdapter namesArrayAdapter;

    private ArrayList<String> names;
    private ArrayList<String> favorites;
    private ListView usersListView;
    private ListView favoritesListView;
    private ProgressDialog progressDialog;
    private BroadcastReceiver receiver = null;
    private View view;
    private MyApplication application;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_message, container, false);
        setConversationsList();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setConversationsList();
    }

    private void setConversationsList() {
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
                Object item = namesArrayAdapter.getItem(i);
                if(item instanceof ParseUser){
                    Intent intent = new Intent(getActivity().getApplicationContext(), MessagingActivity.class);
                    intent.putExtra("RECIPIENT_ID", ((ParseUser) item).getObjectId());
                    startActivity(intent);
                }

                Log.d(TAG, "you clicked the message");
            }
        });
        usersListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> a, View v, int i, long l){
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