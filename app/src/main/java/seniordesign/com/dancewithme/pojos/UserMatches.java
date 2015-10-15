package seniordesign.com.dancewithme.pojos;

import com.parse.ParseObject;

import java.util.ArrayList;

/**
 * Created by ryantemplin on 10/14/15.
 */
public class UserMatches extends ParseObject{
    public UserMatches(){}

    public String getId() {return getObjectId();}

    public String getUsername() {
        return getString("username");
    }

    public ArrayList<String> getMatches(){
        ArrayList<String> matches = (ArrayList<String>) this.get("Matches");
        return matches;
    }

    public void setUsername(String username) {
        put("username", username);
        this.saveInBackground();
    }

    public void addMatch(String username) {
        ArrayList<String> matches = getMatches();
        matches.add(username);
        this.saveInBackground();
    }

}
