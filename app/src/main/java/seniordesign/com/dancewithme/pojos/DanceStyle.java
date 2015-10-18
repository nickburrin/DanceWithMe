package seniordesign.com.dancewithme.pojos;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by nickburrin on 10/5/15.
 */
@ParseClassName("DanceStyle")
public class DanceStyle extends ParseObject{
    public DanceStyle() {}

    public String getId()
    {
        return getObjectId();
    }

    public String getStyle() {
        return getString("style");
    }

    public void setStyle(String style) {
        put("style", style);
        saveInBackground();
    }

    public String getSkill() {
        return getString("Skill");
    }

    public void setSkill(String skill) {
        put("Skill", skill);
        saveInBackground();
    }

    public ArrayList<String> getPreferences() {
        return (ArrayList<String>) get("Preferences");
    }

    public void setPreferences(ArrayList<String> preferences) {
        put("Preferences", preferences);
        saveInBackground();
    }
}
