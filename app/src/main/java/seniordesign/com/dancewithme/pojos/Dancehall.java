package seniordesign.com.dancewithme.pojos;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;

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

    public String getStyle(){
        return getString("style");
    }

    public ParseGeoPoint getGeoPoint() {
        return getParseGeoPoint("latlong");
    }

    public ArrayList<ParseUser> getAttendees(){
        return (ArrayList<ParseUser>) get("attendees");
    }

    public void setAttendees(ArrayList<ParseUser> list){
        put("attendees", list);
        saveInBackground();
    }

    public void addAttendee(ParseUser p){
        addUnique("attendees", p);
        saveInBackground();
    }

    public String getAddress(){
        return getString("Address");
    }

    public String getPhone(){
        return getString("PhoneNumber");
    }
}
