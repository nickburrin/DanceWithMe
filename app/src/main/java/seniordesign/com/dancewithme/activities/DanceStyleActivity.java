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

import seniordesign.com.dancewithme.R;
import seniordesign.com.dancewithme.adapters.NothingSelectedSpinnerAdapter;
import seniordesign.com.dancewithme.pojos.DanceStyle;

/**
 * Created by nickburrin on 10/6/15.
 */
public class DanceStyleActivity extends Activity {
    private static final String TAG = DanceStyleActivity.class.getSimpleName();    // Used for debugging

    Spinner mDanceStyle;
    RadioGroup mSkill;
    Button mCancel;
    Button mSubmit;

    DanceStyle existingStyle;
    String style;
    String skillLevel;
    ArrayList<String> prefs = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dancestyle);

        // Get bundle content
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("DanceStyle");
            query.whereEqualTo("objectId", extras.getString("style_id"));
            try {
                existingStyle = (DanceStyle) query.getFirst();
                style = existingStyle.getStyle();
                skillLevel = existingStyle.getSkill();
                prefs = existingStyle.getPreferences();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        // TODO: Need to remove option of creating dancestyles that already exist!
        mDanceStyle = (Spinner) findViewById(R.id.spinner_dance_style);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.dancestyles_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mDanceStyle.setAdapter(new NothingSelectedSpinnerAdapter(adapter, R.layout.contact_spinner_row_nothing_selected, this));

        mSkill = (RadioGroup) findViewById(R.id.radiogroup_skill_level);

        if(existingStyle != null){
            mDanceStyle.setSelection(adapter.getPosition(existingStyle.getStyle()) + 1);
            mDanceStyle.setEnabled(false);

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
        style = mDanceStyle.getSelectedItem().toString();

        if(skillLevel == null || style == null){
            Toast.makeText(this.getApplicationContext(), "Complete all fields",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else {
            if(existingStyle == null){
                DanceStyle newStyle = new DanceStyle(ParseUser.getCurrentUser().getObjectId(),
                        style, skillLevel, prefs);

                ParseUser.getCurrentUser().put(style, newStyle);
                ParseUser.getCurrentUser().saveInBackground();
            }
            else{
                existingStyle.setSkill(skillLevel);
                existingStyle.setPreferences(prefs);
                existingStyle.saveInBackground();
            }

            return true;
        }
    }

    public void onRadioButtonClicked(View view) {
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
