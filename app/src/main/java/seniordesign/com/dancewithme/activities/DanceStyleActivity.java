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

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;

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

    DanceStyle style;
    String skillLevel;
    ArrayList<String> prefs = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dancestyle);

        DanceStyle styleId = null;
        // Get bundle content
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("DanceStyle");
            query.whereEqualTo("objectId", extras.getString("style_id"));
            try {
                style = (DanceStyle) query.getFirst();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        mDanceStyle = (Spinner) findViewById(R.id.spinner_dance_style);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.dancestyles_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mDanceStyle.setAdapter(new NothingSelectedSpinnerAdapter(adapter, R.layout.contact_spinner_row_nothing_selected, this));

        mSkill = (RadioGroup) findViewById(R.id.radiogroup_skill_level);

        if(style != null){
            mDanceStyle.setSelection(adapter.getPosition(style.getStyle()) + 1);
            mDanceStyle.setEnabled(false);

            if(style.getSkill().equals("Beginner")){
                mSkill.check(R.id.radio_beginner);
            } else if(style.getSkill().equals("Intermediate")){
                mSkill.check(R.id.radio_intermediate);
            } else{
                mSkill.check(R.id.radio_expert);
            }

            for(String x: style.getPreferences()){
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
                submitDanceStyle();
                finish();
            }
        });
    }

    private void submitDanceStyle() {
        if(skillLevel == null){
            Toast.makeText(this.getApplicationContext(), "Specify a skill level",
                    Toast.LENGTH_SHORT).show();
        } else {
            DanceStyle newStyle = new DanceStyle();
            newStyle.setStyle(mDanceStyle.getSelectedItem().toString());
            newStyle.setSkill(skillLevel);
            newStyle.setPreferences(prefs);

            //ParseObject danceStyles = (ParseObject) ParseUser.getCurrentUser().get("danceStyles");
            ParseUser.getCurrentUser().add("danceStyles", newStyle);
            ParseUser.getCurrentUser().saveInBackground();
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
