package seniordesign.com.dancewithme.pojos;

import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nickburrin on 10/14/15.
 */
@ParseClassName("Matches")
public class Matches extends ParseObject{
    public Matches(){}

    public String getId()
    {
        try {
            this.fetchIfNeeded();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return getObjectId();
    }

    public void setId(String id) {
        put("userId", id);
        saveInBackground();
    }

    public String getUserId() {
        try {
            this.fetchIfNeeded();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return getString("userId");
    }

    public ParseUser getUser() {
        ParseUser result = null;
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("objectId", getUserId());

        try {
            result = query.getFirst();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return result;
    }

    public List<ParseUser> getMatches() {
        try {
            this.fetchIfNeeded();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return getList("matchArray");
    }

    public void setMatches(List<Object> objects) {
        put("matchArray", objects);
        saveInBackground();
    }

    public void addMatch(ParseUser matchedUser) {
        try {
            this.fetchIfNeeded();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        this.addUnique("matchArray", matchedUser);
        this.saveInBackground();
    }


}
