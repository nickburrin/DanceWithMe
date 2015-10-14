package seniordesign.com.dancewithme.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Arrays;

import seniordesign.com.dancewithme.R;
import seniordesign.com.dancewithme.adapters.CustomOnItemSelectedListener;

/**
 * Created by nickburrin on 10/14/15.
 */
public class EditUserActivity extends Activity{
    private static final String TAG = EditUserActivity.class.getSimpleName();

    private EditText mEmailView;
    private EditText mFirstName;
    private EditText mLastName;
    private Spinner mGenderSpinner;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);
        ParseAnalytics.trackAppOpenedInBackground(getIntent());

        ParseUser user = ParseUser.getCurrentUser();
        mEmailView = ((EditText) findViewById(R.id.et_email));
        mEmailView.setText(user.getEmail());

        mFirstName = ((EditText) findViewById(R.id.et_first_name));
        mFirstName.setText(user.getString("first_name"));

        mLastName = ((EditText) findViewById(R.id.et_last_name));
        mLastName.setText(user.getString("last_name"));

//          Not sure if we should allow them to change gender lol
//        mGenderSpinner = ((Spinner) findViewById(R.id.spinner_gender));
//        mGenderSpinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());
//        mGenderSpinner.setPrompt("Select your gender");

        findViewById(R.id.button_cancel_changes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        findViewById(R.id.button_submit_changes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUser();
            }
        });
    }

    private void updateUser() {
        String email = ((EditText) findViewById(R.id.et_email)).getText().toString();
        String firstName = ((EditText) findViewById(R.id.et_first_name)).getText().toString();
        String lastName = ((EditText) findViewById(R.id.et_last_name)).getText().toString();

        if(email.isEmpty() || firstName.isEmpty() || lastName.isEmpty()){
            Toast.makeText(this.getApplicationContext(), "Please complete all fields", Toast.LENGTH_SHORT).show();
        } else if(isEmailValid(email)){
            Toast.makeText(this.getApplicationContext(), "Enter a valid email", Toast.LENGTH_SHORT).show();
        } else {
            //Create the new account
            ParseUser.getCurrentUser().setEmail(email);
            ParseUser.getCurrentUser().put("first_name", firstName);
            ParseUser.getCurrentUser().put("last_name", lastName);
            ParseUser.getCurrentUser().saveInBackground();
        }
    }

    private boolean isEmailValid(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
