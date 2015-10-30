package seniordesign.com.dancewithme.pojos;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

import java.util.ArrayList;

/**
 * Created by nickburrin on 10/13/15.
 */
@ParseClassName("Dancehall")
public class Dancehall extends ParseObject {
    public Dancehall() {}

    public String getId()
    {
        return getObjectId();
    }

    public String getName() {
        return getString("Venue");
    }

    public ParseGeoPoint getGeoPoint() {
        return getParseGeoPoint("latlong");
    }

    public ArrayList<Event> getEvents() {
        return (ArrayList<Event>) get("events");
    }

    public String getAddress(){
        return getString("Address");
    }

    public String getPhone(){
        return getString("PhoneNumber");
    }
}
