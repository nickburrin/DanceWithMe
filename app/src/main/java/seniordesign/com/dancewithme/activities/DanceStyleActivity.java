package seniordesign.com.dancewithme.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seniordesign.com.dancewithme.R;
import seniordesign.com.dancewithme.adapters.NothingSelectedSpinnerAdapter;
import seniordesign.com.dancewithme.pojos.DanceStyle;

/**
 * Created by nickburrin on 10/6/15.
 */
public class DanceStyleActivity extends Activity {
    private static final String TAG = DanceStyleActivity.class.getSimpleName();    // Used for debugging
    ArrayList<String> DANCE_STYLES = new ArrayList(Arrays.asList("Country", "Salsa", "Tango", "Swing"));

    Spinner mDanceStyle;
    RadioGroup mLead;
    RadioGroup mSkill;
    Button mCancel;
    Button mSubmit;

    DanceStyle existingStyle;
    Boolean isLead;
    String style;
    String skillLevel;
    ArrayList<String> prefs = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dancestyle);

        ArrayAdapter<String> adapter;

        // Get bundle content
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            // The activity is modifying an existing style
            ParseQuery<ParseObject> query = ParseQuery.getQuery("DanceStyle");
            query.whereEqualTo("objectId", extras.getString("style_id"));

            try {
                existingStyle = (DanceStyle) query.getFirst();
                style = existingStyle.getStyle();
                isLead = existingStyle.isLead();
                skillLevel = existingStyle.getSkill();
                prefs = existingStyle.getPreferences();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, DANCE_STYLES);
        } else {
            // This activity is creating a new style
            List<String> stylesUserDoesntHave = new ArrayList<>();
            for(String s: DANCE_STYLES){
                if(ParseUser.getCurrentUser().get(s) == null){
                    stylesUserDoesntHave.add(s);
                }
            }
            adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, stylesUserDoesntHave);
        }


        mDanceStyle = (Spinner) findViewById(R.id.spinner_dance_style);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mDanceStyle.setAdapter(new NothingSelectedSpinnerAdapter(adapter, R.layout.contact_spinner_row_nothing_selected, this));

        mLead = (RadioGroup) findViewById(R.id.radiogroup_lead_or_follow);
        mSkill = (RadioGroup) findViewById(R.id.radiogroup_skill_level);

        if(existingStyle != null){
            mDanceStyle.setSelection(adapter.getPosition(existingStyle.getStyle()) + 1);
            mDanceStyle.setEnabled(false);

            if(existingStyle.isLead()){
                mLead.check(R.id.radio_lead);
            } else {
                mLead.check(R.id.radio_follow);
            }

            if(existingStyle.getSkill().equals("Beginner")){
                mSkill.check(R.id.radio_beginner);
            } else if(existingStyle.getSkill().equals("Intermediate")){
                mSkill.check(R.id.radio_intermediate);
            } else{
                mSkill.check(R.id.radio_expert);
            }

            for(String x: existingStyle.getPreferences()){
                if(x.equals("Beginner")){
                    ((CheckBox)findViewById(R.id.checkbox_beginner)).setChecked(true);
                } else if(x.equals("Intermediate")){
                    ((CheckBox)findViewById(R.id.checkbox_intermediate)).setChecked(true);
                } else if(x.equals("Expert")){
                    ((CheckBox)findViewById(R.id.checkbox_expert)).setChecked(true);
                }
            }
        }

        mCancel = (Button) findViewById(R.id.button_cancel_dancestyle);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        mSubmit = (Button) findViewById(R.id.button_submit_dancestyle);
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(submitDanceStyle() == true) {
                    finish();
                }
            }
        });
    }

    private boolean submitDanceStyle() {
        try {
            style = mDanceStyle.getSelectedItem().toString();
        }catch(NullPointerException e){
            Toast.makeText(this.getApplicationContext(), "Complete all fields",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        if(skillLevel == null || isLead == null){
            Toast.makeText(this.getApplicationContext(), "Complete all fields",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else {
            if(existingStyle == null){
                DanceStyle newStyle = new DanceStyle(ParseUser.getCurrentUser().getObjectId(),
                        style, isLead, skillLevel, prefs);

                ParseUser.getCurrentUser().put(style, newStyle);
                ParseUser.getCurrentUser().saveInBackground();
            }
            else{
                existingStyle.setLead(isLead);
                existingStyle.setSkill(skillLevel);
                existingStyle.setPreferences(prefs);
                existingStyle.saveInBackground();
            }

            return true;
        }
    }

    public void onLeadFollowRadioClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_lead:
                if (checked) {
                    isLead = true;
                }
                break;
            case R.id.radio_follow:
                if (checked) {
                    isLead = false;
                }
                break;
        }
    }

    public void onSkillRadioClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_beginner:
                if (checked) {
                    skillLevel = "Beginner";
                }
                break;
            case R.id.radio_intermediate:
                if (checked) {
                    skillLevel = "Intermediate";
                }
                break;
            case R.id.radio_expert:
                if (checked) {
                    skillLevel = "Expert";
                }
                break;
        }
    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkbox_beginner:
                if (checked && !prefs.contains("Beginner")){
                    prefs.add("Beginner");
                }
                else{
                    if(prefs.contains("Beginner")){
                        prefs.remove("Beginner");
                    }
                }
                break;
            case R.id.checkbox_intermediate:
                if (checked && !prefs.contains("Intermediate")){
                    prefs.add("Intermediate");
                }
                else{
                    if(prefs.contains("Intermediate")){
                        prefs.remove("Intermediate");
                    }
                }
                break;
            case R.id.checkbox_expert:
                if (checked && !prefs.contains("Expert")){
                    prefs.add("Expert");
                }
                else{
                    if(prefs.contains("Expert")){
                        prefs.remove("Expert");
                    }
                }
        }
    }
}
