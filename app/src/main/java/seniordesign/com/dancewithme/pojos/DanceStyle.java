package seniordesign.com.dancewithme.pojos;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.ArrayList;

/**
 * Created by nickburrin on 10/5/15.
 */
@ParseClassName("DanceStyle")
public class DanceStyle extends ParseObject{
    public DanceStyle() {}

    public DanceStyle(String id, String style, String skill, ArrayList<String> prefs){
        this();
        setUserId(id);
        setStyle(style);
        setSkill(skill);
        setPreferences(prefs);
    }

    public String getId()
    {
        return getObjectId();
    }

    public String setUserId(){
        return getString("userId");
    }

    public void setUserId(String s){
        put("userId", s);
        saveInBackground();
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
