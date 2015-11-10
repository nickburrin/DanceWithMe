package seniordesign.com.dancewithme.fragments;

import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

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
    private String currentUserId;
    private ArrayAdapter<String> namesArrayAdapter;
    private ArrayAdapter<String> favoritesArrayAdapter;
    private ArrayList<String> names;
    private ArrayList<String> favorites;
    private ListView usersListView;
    private ListView favoritesListView;
    private ProgressDialog progressDialog;
    private BroadcastReceiver receiver = null;
    private View view;
    private View convertFavoriteView;
    private View convertUserView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_message, container, false);
        convertFavoriteView = inflater.inflate(R.layout.favorite_list_item, container, false);
        //showSpinner();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setConversationsList();
    }

    private void setConversationsList() {
        List<ParseUser> userMatches = ((Matches) ParseUser.getCurrentUser().get("Matches")).getMatches();
        names = new ArrayList<String>();
        List<ParseUser> userFavorites = (List) ParseUser.getCurrentUser().get("Favorites");
        favorites = new ArrayList<String>();



        //for favorites
        for(ParseUser i: userMatches) {
            try {
                i.fetchIfNeeded();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            favorites.add(i.getString("first_name"));

//            ParseQuery<ParseUser> query = ParseUser.getQuery();
//            query.whereEqualTo("username", myMatchesNames.get(i));
//            query.findInBackground(new FindCallback<ParseUser>() {
//                public void done(List<ParseUser> userList, com.parse.ParseException e) {
//                    if (e == null) {
//                        for (int i = 0; i < userList.size(); i++) {
//                            names.add(userList.get(i).getUsername().toString());
//                        }

        }

        favoritesArrayAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(),
                R.layout.favorite_list_item, favorites);
        favoritesListView = (ListView) view.findViewById(R.id.usersListView);
        favoritesListView.setAdapter(favoritesArrayAdapter);

        favoritesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int i, long l) {
                openConversation(i);
            }
        });


        //User Matches
        for(ParseUser i: userMatches) {
            try {
                i.fetchIfNeeded();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            names.add(i.getString("first_name"));

//            ParseQuery<ParseUser> query = ParseUser.getQuery();
//            query.whereEqualTo("username", myMatchesNames.get(i));
//            query.findInBackground(new FindCallback<ParseUser>() {
//                public void done(List<ParseUser> userList, com.parse.ParseException e) {
//                    if (e == null) {
//                        for (int i = 0; i < userList.size(); i++) {
//                            names.add(userList.get(i).getUsername().toString());
//                        }

        }

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
                    Toast.makeText(getActivity().getApplicationContext(), "Error finding that user", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //show a loading spinner while the sinch client starts
    private void showSpinner() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Boolean success = intent.getBooleanExtra("success", false);
                progressDialog.dismiss();
                if (!success) {
                    Toast.makeText(getActivity().getApplicationContext(), "Messaging service failed to start", Toast.LENGTH_LONG).show();
                }
            }
        };

        LocalBroadcastManager.getInstance(this.getActivity()).registerReceiver(receiver, new IntentFilter("seniordesign.com.dancewithme.activities.HomeActivity"));
    }

    @Override
    public void onResume() {
        super.onResume();
        setConversationsList();
    }

    @Override
    public void onPause(){
        super.onPause();
    }

}


//    private String recipientId;
//    private EditText messageBodyField;
//    private String messageBody;
//    private MessageService.MessageServiceInterface messageService;
//    private MessageAdapter messageAdapter;
//    private ListView messagesList;
//    private String currentUserId;
//    private ServiceConnection serviceConnection = new MyServiceConnection();
//    private MessageClientListener messageClientListener = new MyMessageClientListener();
//
//
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_message, container, false);
//
//        getActivity().bindService(new Intent(this.getActivity(), MessageService.class), serviceConnection, 0);
//
//        Intent intent = getActivity().getIntent();
//        recipientId = intent.getStringExtra("RECIPIENT_ID");
//        currentUserId = ParseUser.getCurrentUser().getObjectId();
//
//        messagesList = (ListView) view.findViewById(R.id.listMessages);
//        messageAdapter = new MessageAdapter(this.getActivity());
//        messagesList.setAdapter(messageAdapter);
//        populateMessageHistory();
//
//        messageBodyField = (EditText) view.findViewById(R.id.messageBodyField);
//
//        view.findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                sendMessage();
//            }
//        });
//        return view;
//    }
//
//
//    //get previous messages from parse & display
//    private void populateMessageHistory() {
//        String[] userIds = {currentUserId, recipientId};
//        ParseQuery<ParseObject> query = ParseQuery.getQuery("ParseMessage");
//        query.whereContainedIn("senderId", Arrays.asList(userIds));
//        query.whereContainedIn("recipientId", Arrays.asList(userIds));
//        query.orderByAscending("createdAt");
//        query.findInBackground(new FindCallback<ParseObject>() {
//            @Override
//            public void done(List<ParseObject> messageList, com.parse.ParseException e) {
//                if (e == null) {
//                    for (int i = 0; i < messageList.size(); i++) {
//                        WritableMessage message = new WritableMessage(messageList.get(i).get("recipientId").toString(), messageList.get(i).get("messageText").toString());
//                        if (messageList.get(i).get("senderId").toString().equals(currentUserId)) {
//                            messageAdapter.addMessage(message, MessageAdapter.DIRECTION_OUTGOING);
//                        } else {
//                            messageAdapter.addMessage(message, MessageAdapter.DIRECTION_INCOMING);
//                        }
//                    }
//                }
//            }
//        });
//    }
//
//    private void sendMessage() {
//        messageBody = messageBodyField.getText().toString();
//        if (messageBody.isEmpty()) {
//            Toast.makeText(this.getActivity(), "Please enter a message", Toast.LENGTH_LONG).show();
//            return;
//        }
//
//        messageService.sendMessage(recipientId, messageBody);
//        messageBodyField.setText("");
//    }
//
//    @Override
//    public void onDestroy() {
//        messageService.removeMessageClientListener(messageClientListener);
//        getActivity().unbindService(serviceConnection);
//        super.onDestroy();
//    }
//
//    private class MyServiceConnection implements ServiceConnection {
//        @Override
//        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
//            messageService = (MessageService.MessageServiceInterface) iBinder;
//            messageService.addMessageClientListener(messageClientListener);
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName componentName) {
//            messageService = null;
//        }
//    }
//
//    private class MyMessageClientListener implements MessageClientListener {
//        @Override
//        public void onMessageFailed(MessageClient client, Message message,
//                                    MessageFailureInfo failureInfo) {
//           // Toast.makeText(MessagingActivity.this, "Message failed to send.", Toast.LENGTH_LONG).show();
//        }
//
//        @Override
//        public void onIncomingMessage(MessageClient client, Message message) {
//            if (message.getSenderId().equals(recipientId)) {
//                WritableMessage writableMessage = new WritableMessage(message.getRecipientIds().get(0), message.getTextBody());
//                messageAdapter.addMessage(writableMessage, MessageAdapter.DIRECTION_INCOMING);
//            }
//        }
//
//        @Override
//        public void onMessageSent(MessageClient client, Message message, String recipientId) {
//
//            final WritableMessage writableMessage = new WritableMessage(message.getRecipientIds().get(0), message.getTextBody());
//
//            //only add message to parse database if it doesn't already exist there
//            ParseQuery<ParseObject> query = ParseQuery.getQuery("ParseMessage");
//            query.whereEqualTo("sinchId", message.getMessageId());
//            query.findInBackground(new FindCallback<ParseObject>() {
//                @Override
//                public void done(List<ParseObject> messageList, com.parse.ParseException e) {
//                    if (e == null) {
//                        if (messageList.size() == 0) {
//                            ParseObject parseMessage = new ParseObject("ParseMessage");
//                            parseMessage.put("senderId", currentUserId);
//                            parseMessage.put("recipientId", writableMessage.getRecipientIds().get(0));
//                            parseMessage.put("messageText", writableMessage.getTextBody());
//                            parseMessage.put("sinchId", writableMessage.getMessageId());
//                            parseMessage.saveInBackground();
//
//                            messageAdapter.addMessage(writableMessage, MessageAdapter.DIRECTION_OUTGOING);
//                        }
//                    }
//                }
//            });
//        }
//
//        @Override
//        public void onMessageDelivered(MessageClient client, MessageDeliveryInfo deliveryInfo) {}
//
//        @Override
//        public void onShouldSendPushData(MessageClient client, Message message, List<PushPair> pushPairs) {}
//    }
//
//    public MessageFragment() {
//        // Required empty public constructor
//    }
//
//
//
//
//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//    }
//
//
//
//
