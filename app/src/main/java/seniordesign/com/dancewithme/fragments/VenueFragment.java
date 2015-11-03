package seniordesign.com.dancewithme.fragments;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;

import seniordesign.com.dancewithme.R;
import seniordesign.com.dancewithme.activities.MatchActivity;
import seniordesign.com.dancewithme.adapters.DancehallListAdapter;
import seniordesign.com.dancewithme.pojos.Dancehall;
import seniordesign.com.dancewithme.utils.Logger;

public class VenueFragment extends HomeTabFragment implements LocationListener{
    private static final String TAG = VenueFragment.class.getSimpleName();
    private String[] DANCE_STYLES = new String[]{"Country", "Salsa", "Tango"};

    private ArrayList<ParseObject> venues;
    private ListView venue_list;
    private LocationManager mLocationManager;
    private Location currentLocation;

    private View view;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLocationManager = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_venue, container, false);
        venue_list = (ListView) view.findViewById(R.id.lv_dancehalls);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        currentLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        initListView();
    }

    private void initListView() {
        // What dance styles does the User have?
        ArrayList<String> userStyles = new ArrayList<>();
        for(int i = 0; i < DANCE_STYLES.length; i++){
            if(ParseUser.getCurrentUser().get(DANCE_STYLES[i]) != null){
                userStyles.add(DANCE_STYLES[i]);
            }
        }

        // Find all Dancehalls of the User's dance styles
        // TODO: need to refine this for Dancehalls with multiple styles
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Dancehall");
        query.whereContainedIn("style", userStyles);
        try {
            venues = (ArrayList) query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        venue_list.setAdapter(new DancehallListAdapter(getActivity(), venues, currentLocation));
        venue_list.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object item = venue_list.getAdapter().getItem(position);
                if (item instanceof Dancehall) {
                    // If clicked on dancehall, go to Matching
                    // Get dancehall obj
                    Dancehall venue = (Dancehall) item;
                    venue.addAttendee(ParseUser.getCurrentUser());
                    Log.d(TAG, "Added User to dancehall: " + venue.getId());

                    Intent i = new Intent(getActivity().getApplicationContext(), MatchActivity.class);
                    i.putExtra("venueId", venue.getObjectId());  // Send objectId to the DanceStyleActivity
                    startActivity(i);
                }
            }
        });
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

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
    }

    @Override
    public void onProviderDisabled(String provider) {
        Logger.d("Latitude", "disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Logger.d("Latitude", "enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Logger.d("Latitude","status");
    }

}
