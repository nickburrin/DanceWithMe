package seniordesign.com.dancewithme.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import seniordesign.com.dancewithme.R;
import seniordesign.com.dancewithme.activities.HomeActivity;
import seniordesign.com.dancewithme.activities.LoginActivity;
import seniordesign.com.dancewithme.activities.MessageService;


public class ProfileFragment extends HomeTabFragment {


    private EditText mUsernameView;
    private ImageButton mProfPic;
    private ListView danceStyles;

    private Intent intent;
    private Intent serviceIntent;
    private View view;
    private Button logoutButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        // Inflate the layout for this fragment
        //activity.setContentView(R.layout.activity_profile_management);
        ((TextView) view.findViewById(R.id.username)).setText(ParseUser.getCurrentUser().get("first_name")
                + " " + ParseUser.getCurrentUser().get("last_name"));

        mProfPic = (ImageButton) view.findViewById(R.id.profPic);
        mProfPic.setImageDrawable(null); //fill with an image

        danceStyles = (ListView) view.findViewById(R.id.lv_dance_styles);

        // Instanciating an array list (you don't need to do this, you already have yours).
        List<String> temp_array_list = new ArrayList<String>();
        temp_array_list.add("one");
        temp_array_list.add("two");
        temp_array_list.add("three");

        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and your
        // array as a third parameter.
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this.activity,
                android.R.layout.simple_list_item_1,
                temp_array_list );

        danceStyles.setAdapter(arrayAdapter);

        logoutButton = (Button) view.findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.stopService(new Intent(activity.getApplicationContext(), MessageService.class));
                ParseUser.logOut();
                Intent intent = new Intent(activity.getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    public void changeProfPic(View v) {
        System.out.println("Clicked on picture");
    }

    //@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        activity.getMenuInflater().inflate(R.menu.menu_profile_management, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void displayMatchingInterface(View view)
    {
        Toast.makeText(activity.getApplicationContext(), "Please wait. We are searching for your dance partner",
                Toast.LENGTH_SHORT).show();
        //Intent intent = new Intent(this, InvalidLoginActivity.class);
        //startActivity(intent);
        intent = new Intent(activity.getApplicationContext(), HomeActivity.class);
        serviceIntent = new Intent(activity.getApplicationContext(), MessageService.class);
        startActivity(intent);
        //startService(serviceIntent);
    }

//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;

//    public ProfileFragment() {
//        // Required empty public constructor
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_profile, container, false);
//    }
//

}
