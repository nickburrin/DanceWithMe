package seniordesign.com.dancewithme.pojos;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by nickburrin on 10/14/15.
 */
@ParseClassName("Event")
public class Event extends ParseObject {
    public Event() {}

    public Date getDate(){
        return getDate("date");
    }

    public void setDate(Date d){
        put("date", d);
        saveInBackground();
    }

    public String getTitle(){
        return getString("title");
    }

    public void setTitle(String t){
        put("title", t);
        saveInBackground();
    }

    public ArrayList<ParseUser> getAttendees(){
        return (ArrayList<ParseUser>) get("attendees");
    }

    public void setAttendees(ArrayList<ParseUser> list){
        put("attendees", list);
        saveInBackground();
    }

    public void addAttendee(ParseUser p){
        getAttendees().add(p);
        this.saveInBackground();
    }
}
