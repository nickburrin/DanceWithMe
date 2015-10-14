package seniordesign.com.dancewithme.pojos;

import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;

/**
 * Created by nickburrin on 10/14/15.
 */
@ParseClassName("Matches")
public class Matches extends ParseObject{
    public Matches(){}

    public String getId()
    {
        return getObjectId();
    }


    public String getUserId() {
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


    public ArrayList<String> getMatches() {
        return (ArrayList<String>) get("matchArray");
    }
}
