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
import com.parse.SignUpCallback;


import java.text.ParseException;
import java.util.List;

import seniordesign.com.dancewithme.R;
import seniordesign.com.dancewithme.adapters.CustomOnItemSelectedListener;
import seniordesign.com.dancewithme.fragments.ProfileFragment;


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

        // Autofill the email field if it was filled out in the LoginActivity
        String email = getIntent().getExtras().getString("email");
        if(email != null){
            ((EditText) findViewById(R.id.et_email)).setText(email);
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

            if(user != null){
                // push to Parse
                user.signUpInBackground(new SignUpCallback() {
                    public void done(com.parse.ParseException e) {
                        if (e == null) {
                            Intent i = new Intent(RegisterActivity.this, HomeActivity.class);
                            i.putExtra("first_time", true);
                            startActivity(i);
                        } else {
                            Toast.makeText(getApplicationContext(), "There was an error signing up.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
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
        ParseUser newUser = null;

        String email = ((EditText) findViewById(R.id.et_email)).getText().toString();
        String password = ((EditText) findViewById(R.id.et_password)).getText().toString();
        String firstName = ((EditText) findViewById(R.id.et_first_name)).getText().toString();
        String lastName = ((EditText) findViewById(R.id.et_last_name)).getText().toString();
        String gender = ((Spinner) findViewById(R.id.spinner_gender)).getSelectedItem().toString();

        // See if there exists an existing user with the same email
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("email", email);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> list, com.parse.ParseException e) {
                if (e == null) {
                    // The query was successful.
                    System.out.println("No existing users with email.");
                } else {
                    // Something went wrong.
                    e.printStackTrace();
                }
            }
        });

        if(query.hasCachedResult()){
            //There is already an account with this email
            Toast.makeText(getApplicationContext(), "An account with this email already exists", Toast.LENGTH_SHORT).show();
        } else {
            //Create the new account
            newUser = new ParseUser();
            newUser.setEmail(email);
            newUser.setUsername(email);
            newUser.setPassword(password);
            newUser.put("first_name", firstName);
            newUser.put("last_name", lastName);
            newUser.put("gender", gender);
        }

        return newUser;
    }


    public void displaySelectAVenue(View view)
    {
        Toast.makeText(getApplicationContext(), "Your account has been created successfully. We will direct you to the Select a Venue page.",
                Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, SelectAVenue.class);
        startActivity(intent);
    }
}