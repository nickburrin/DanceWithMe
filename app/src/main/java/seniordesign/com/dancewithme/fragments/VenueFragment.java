package seniordesign.com.dancewithme.fragments;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;

import seniordesign.com.dancewithme.R;
import seniordesign.com.dancewithme.activities.MatchActivity;
import seniordesign.com.dancewithme.adapters.DancehallListAdapter;
import seniordesign.com.dancewithme.pojos.Dancehall;
import seniordesign.com.dancewithme.utils.Logger;


public class VenueFragment extends Fragment implements LocationListener{
    private static final String TAG = VenueFragment.class.getSimpleName();
    private String[] DANCE_STYLES = new String[]{"Country", "Salsa", "Tango", "Swing"};

    private ArrayList<Dancehall> venues;
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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        currentLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        initListView();
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        currentLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        initListView();
    }
    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        //if (activityReady) {
        if (getActivity() != null) {
            if (visible) {
                initListView();
            }
        }
    }
    private void initListView() {
        // Find all Dancehalls of the User's dance styles
        ArrayList<Dancehall> temp = null;

        ParseQuery<Dancehall> query = ParseQuery.getQuery("Dancehall");

        if(currentLocation != null) {
            query.whereWithinMiles("latlong", new ParseGeoPoint(currentLocation.getLatitude(), currentLocation.getLongitude()), 50.0);
        } else {
            query.whereNotEqualTo("objectId", null);
        }

        try {
            temp = (ArrayList) query.find();
        } catch (ParseException e) {
            Toast.makeText(getActivity().getApplicationContext(), "There were no venues found with 50 miles", Toast.LENGTH_LONG).show();
            return;
        }

        venues = new ArrayList<>();
        for(Dancehall v: temp){
            if(ParseUser.getCurrentUser().get(v.getStyle()) != null){
                venues.add(v);
            }
        }

        if(venues.size() == 0){
            Toast.makeText(getActivity().getApplicationContext(), "Specify your dance styles to start matching!", Toast.LENGTH_LONG).show();
            return;
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
