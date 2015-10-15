package seniordesign.com.dancewithme.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;

import seniordesign.com.dancewithme.R;
import seniordesign.com.dancewithme.adapters.DancehallListAdapter;
import seniordesign.com.dancewithme.pojos.Dancehall;

public class VenueActivity extends Activity implements LocationListener{
    private static final String TAG = VenueActivity.class.getSimpleName();
    private final double METERS_TO_MILES = 0.00062137;

    private ArrayList<ParseObject> venues;
    private ListView venue_list;
    private LocationManager mLocationManager;
    private Location currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue);

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        venue_list = (ListView) findViewById(R.id.lv_dancehalls);
    }

    @Override
    public void onResume() {
        super.onResume();
        initListView();
    }

    private void initListView() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Dancehall");
        try {
            venues = (ArrayList) query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        venue_list.setAdapter(new DancehallListAdapter(this, venues));
        venue_list.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object item = venue_list.getAdapter().getItem(position);
                if (item instanceof Dancehall) {
                    // If clicked on dancehall, go to Matching
                    // Get dancehall obj
                    Dancehall venue = (Dancehall) item;
                    Log.d(TAG, "Clicked on dancehall:" + venue.getId());

                    Location venueLoc = new Location(LocationManager.GPS_PROVIDER);
                    venueLoc.setLatitude(venue.getLocation().getLatitude());
                    venueLoc.setLongitude(venue.getLocation().getLongitude());
                    Log.d(TAG, "Distance is:" + venueLoc.distanceTo(currentLocation)*METERS_TO_MILES);

                    Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                    i.putExtra("event_id", venue.getObjectId());  // Send objectId to the DanceStyleActivity
                    startActivity(i);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_select_avenue, menu);
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

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude", "disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude", "enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }

}
