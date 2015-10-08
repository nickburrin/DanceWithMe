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

    public String getStyle() {
        return getString("Name");
    }

    public void setStyle(String style) {
        put("Name", style);
    }

    public String getSkill() {
        return getString("Skill");
    }

    public void setSkill(String skill) {
        put("Skill", skill);
    }

    public ArrayList<String> getPreferences() {
        ArrayList<String> preferences = (ArrayList<String>) get("Preferences");

//        JSONArray prefs = getJSONArray("Preferences");
//        try {
//            for (int i = 0; i < prefs.length(); i++) {
//                preferences.add(prefs.getString(i));
//            }
//        } catch(JSONException e){
//            e.printStackTrace();
//        }
        return preferences;
    }

    public void setPreferences(ArrayList<String> preferences) {
        put("Preferences", preferences);
    }
}
