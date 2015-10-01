package seniordesign.com.dancewithme.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseQuery;
import com.parse.ParseUser;


import java.text.ParseException;
import java.util.List;

import seniordesign.com.dancewithme.R;
import seniordesign.com.dancewithme.adapters.CustomOnItemSelectedListener;


public class RegisterActivity extends ActionBarActivity {

    // UI fields
    private EditText mFirstName;
    private EditText mLastName;
    private EditText mEmailView;
    private EditText mPasswordView;
    private EditText mConfirmPasswordView;
    private Spinner mGenderSpinner;
    private Button mRegisterButton;
    private Button mExistingUserButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ParseAnalytics.trackAppOpenedInBackground(getIntent());

        String username = getIntent().getExtras().getString("username");
        if(username != null){
            // TODO: fill in username box
        }

        mEmailView = ((EditText) findViewById(R.id.et_email));
        mPasswordView = ((EditText) findViewById(R.id.et_password));
        mConfirmPasswordView = ((EditText) findViewById(R.id.et_confirm_password));
        mFirstName = ((EditText) findViewById(R.id.et_first_name));
        mLastName = ((EditText) findViewById(R.id.et_last_name));

        mGenderSpinner = ((Spinner) findViewById(R.id.spinner_gender));
        mGenderSpinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());

        mExistingUserButton = (Button) findViewById(R.id.button_existing_user);
        mExistingUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mRegisterButton = (Button) findViewById(R.id.button_submit_new_account);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_an_account, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void createAccount(){
        // Only create an account if all fields check out
        if(verifyUIFields() == true) {
            // extract all UI fields, create ParseUser
            ParseUser user = extractParseUser();

            // push to Parse

/*
            ParseUser user = new ParseUser();
            user.setUsername(username);
            user.setPassword(password);

            user.signUpInBackground(new SignUpCallback() {
                public void done(com.parse.ParseException e) {
                    if (e == null) {
                        Intent i = new Intent(LoginActivity.this, SelectAVenue.class);
                        startActivity(i);
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "There was an error signing up."
                                , Toast.LENGTH_LONG).show();
                    }
                }
            });
*/
        }
    }

    private boolean verifyUIFields() {
        boolean fieldsOK = false;
        View focusView = null;

        if(mFirstName.getText().toString().isEmpty()){
            // Check for first name
            mFirstName.setError(getString(R.string.error_field_required));
            focusView = mFirstName;
            fieldsOK = true;
        }
        if(mLastName.getText().toString().isEmpty()){
            // Check for last name
            mLastName.setError(getString(R.string.error_field_required));
            focusView = mLastName;
            fieldsOK = true;
        }
        if (TextUtils.isEmpty(mPasswordView.getText().toString())) {
            // Check if the user entered password
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            fieldsOK = true;
        }
        if (TextUtils.isEmpty(mConfirmPasswordView.getText().toString())) {
            // Check if the user entered confirm password
            mConfirmPasswordView.setError(getString(R.string.error_field_required));
            focusView = mConfirmPasswordView;
            fieldsOK = true;
        }
        if (TextUtils.isEmpty(mEmailView.getText().toString())) {
            // Check if user entered email
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            fieldsOK = true;
        }
        if (!isEmailValid(mEmailView.getText().toString())) {
            // Check for a valid email address.
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            fieldsOK = true;
        }
        if(!doPasswordsMatch(mPasswordView.getText().toString(), mConfirmPasswordView.getText().toString())){
            // Check if confirm password matches the original, if the user entered one.
            mConfirmPasswordView.setError(getString(R.string.error_password_no_match));
            focusView = mConfirmPasswordView;
            fieldsOK = true;
        }

        if (fieldsOK) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        }

        return fieldsOK;
    }

    private boolean isEmailValid(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean doPasswordsMatch(String pass, String confirm) {
        if(pass.isEmpty() || confirm.isEmpty()) {
            return false;
        } else if(pass.equals(confirm)){
            return true;
        } else{
            return false;
        }
    }

    private ParseUser extractParseUser() {
        String email = ((EditText) findViewById(R.id.et_email)).getText().toString();
        String password = ((EditText) findViewById(R.id.et_password)).getText().toString();
        String confirmPassword = ((EditText) findViewById(R.id.et_confirm_password)).getText().toString();
        String firstName = ((EditText) findViewById(R.id.et_first_name)).getText().toString();
        String lastName = ((EditText) findViewById(R.id.et_last_name)).getText().toString();
        String gender = ((Spinner) findViewById(R.id.spinner_gender)).getSelectedItem().toString();

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("gender", "female");
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    // The query was successful.
                } else {
                    // Something went wrong.
                }
            }
        });

        if(query.)
        ParseUser newUser = new ParseUser();

    }


    public void displaySelectAVenue(View view)
    {
        Toast.makeText(getApplicationContext(), "Your account has been created successfully. We will direct you to the Select a Venue page.",
                Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, SelectAVenue.class);
        startActivity(intent);
    }
}