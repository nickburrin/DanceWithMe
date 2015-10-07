package seniordesign.com.dancewithme.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import seniordesign.com.dancewithme.R;

/**
 * Created by nickburrin on 10/6/15.
 */
public class DanceStyleActivity extends Activity {
    private static final String TAG = DanceStyleActivity.class.getSimpleName();    // Used for debugging

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dancestyle);

        // Get bundle content
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            // TODO: need to check if this is an existing DanceStyle and grab the related data
        } else {
            finish();
        }
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_beginner:
                if (checked)
                    // Pirates are the best
                    break;
            case R.id.radio_intermediate:
                if (checked)
                    // Ninjas rule
                    break;
            case R.id.radio_expert:
                if (checked)
                    // Ninjas rule
                    break;
        }
    }

}
