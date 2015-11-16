package seniordesign.com.dancewithme.pojos;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Arrays;
import java.util.List;

/**
 * Created by nickburrin on 11/16/15.
 */
@ParseClassName("Likes")
public class Likes extends ParseObject{
    public Likes(){}

    public Likes(String id){
        this();
        setUserId(id);
        setLikes(Arrays.asList());
    }

    public String getId()
    {
        try {
            this.fetchIfNeeded();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return getObjectId();
    }

    public void setUserId(String id) {
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

    public List<ParseUser> getLikesArray() {
        try {
            this.fetchIfNeeded();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return getList("likesArray");
    }

    public void setLikes(List objects) {
        put("likesArray", objects);
        saveInBackground();
    }

    public void addLike(ParseUser matchedUser) {
        try {
            this.fetchIfNeeded();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        this.addUnique("likesArray", matchedUser);
        this.saveInBackground();
    }
}
