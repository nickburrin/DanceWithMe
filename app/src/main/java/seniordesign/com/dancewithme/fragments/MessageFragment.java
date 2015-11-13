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
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import seniordesign.com.dancewithme.R;
import seniordesign.com.dancewithme.activities.MessagingActivity;
import seniordesign.com.dancewithme.pojos.Matches;


public class MessageFragment extends Fragment {
    private static final String TAG = MessageFragment.class.getSimpleName();

    private ArrayAdapter<String> namesArrayAdapter;
    private ArrayList<String> names;
    private ListView usersListView;
    private ProgressDialog progressDialog;
    private BroadcastReceiver receiver = null;
    private View view;


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

        for(ParseUser i: userMatches) {
            try {
                i.fetchIfNeeded();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            names.add(i.getString("first_name"));
        }

        // TODO: fill the adapter with Users instead of Strings
        namesArrayAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(),
                R.layout.user_list_item, names);
        usersListView = (ListView) view.findViewById(R.id.usersListView);
        usersListView.setAdapter(namesArrayAdapter);

        usersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int i, long l) {
                openConversation(i);
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
                   Log.d(TAG, "Error finding that user");
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