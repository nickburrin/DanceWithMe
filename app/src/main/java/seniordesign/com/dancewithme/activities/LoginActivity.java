package seniordesign.com.dancewithme.activities;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.parse.LogInCallback;
//import com.parse.ParseAnalytics;
import com.parse.ParseInstallation;
import com.parse.ParseUser;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import seniordesign.com.dancewithme.R;
import seniordesign.com.dancewithme.fragments.MessageFragment;
import seniordesign.com.dancewithme.utils.Logger;


public class LoginActivity extends Activity {
    private static final String TAG = "Touba";

    private Button signUpButton;
    private Button loginButton;
    private Button forgotPasswordButton;
    private EditText emailField;
    private EditText passwordField;
    private Intent intent;
    private Intent serviceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i;
        if(ParseUser.getCurrentUser() != null){
            //startActivity(new Intent(this, HomeActivity.class));
            startActivity(new Intent(this, VenueActivity.class));   // temp for Nick
        }

        intent = new Intent(getApplicationContext(), HomeActivity.class);
        serviceIntent = new Intent(getApplicationContext(), MessageService.class);
//        Parse.enableLocalDatastore(this);

        setContentView(R.layout.activity_login);

        loginButton = (Button) findViewById(R.id.loginButton);
        signUpButton = (Button) findViewById(R.id.signupButton);
        //       forgotPasswordButton = (Button) findViewById(R.id.forgotyourpasswordButton);
        emailField = (EditText) findViewById(R.id.loginUsername);
        passwordField = (EditText) findViewById(R.id.loginPassword);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailField.getText().toString();
                String password = passwordField.getText().toString();
                ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                installation.put("username", email);
                installation.saveInBackground();
                Logger.d(TAG, "User Logging in");
                ParseUser.logInInBackground(email, password, new LogInCallback() {
                    public void done(ParseUser user, com.parse.ParseException e) {
                        if (user != null) {
                            startActivity(intent);
                            startService(serviceIntent);
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "Wrong username/password combo",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailField.getText().toString();

                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                registerIntent.putExtra("email", email);
                startActivity(registerIntent);
            }

        });

//        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(LoginActivity.this, ForgotYourPassword.class);
//                startActivity(intent);
//            }
//        });
    }

    @Override
    public void onDestroy() {
        stopService(new Intent(this, MessageService.class));
        super.onDestroy();
    }
}

//import seniordesign.com.dancewithme.R;
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//
//import com.parse.ParseAnalytics;
//import com.parse.ParseObject;
//import android.view.View;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import seniordesign.com.dancewithme.R;
//
//public class LoginActivity extends Activity {
//    /**
//     * Called when the activity is first created.
//     */
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        //setContentView(R.layout.activity_invalid_login);
//        setContentView(R.layout.activity_login);
//
//
//        //ParseObject testObject = new ParseObject("TestObject");
//        //testObject.put("foo", "bar");
//        //testObject.saveInBackground();
//
//        //testObject.
//
//      //  ParseAnalytics.trackAppOpenedInBackground(getIntent());
//    }
//
//    public void validateLogin(View view) {
//        Toast.makeText(getApplicationContext(), "You have logged in successfully. We will direct you to the Select a Venue page.",
//                Toast.LENGTH_SHORT).show();
//        displaySelectAVenue(view);
//    }
//
//    public void displayForgotYourPassword(View view)
//    {
//        Intent intent = new Intent(this, ForgotYourPassword.class);
//        startActivity(intent);
//    }
//
//    public void displayCreateAnAccount(View view) {
//        Intent intent = new Intent(this, RegisterActivity.class);
//        startActivity(intent);
//        //setContentView(R.layout.activity_register);
//    }
//
//    public void displaySelectAVenue(View view)
//    {
//        Intent intent = new Intent(this, VenueActivity.class);
//        startActivity(intent);
//    }
//
//}

//import android.app.Activity;
//import android.content.Intent;
//
//import android.os.Bundle;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.AutoCompleteTextView;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import com.parse.LogInCallback;
//import com.parse.Parse;
//import com.parse.ParseInstallation;
//import com.parse.ParseUser;
//
//import seniordesign.com.dancewithme.R;


///**
// * A login screen that offers login via email/password and via Google+ sign in.
// */
//
//public class LoginActivity extends Activity{
//
//    /**
//     * Keep track of the login task to ensure we can mCancel it if requested.
//     */
//    // UI references.
//    private AutoCompleteTextView mEmailView;
//    private EditText mPasswordView;
//    private View mProgressView;
//    private View mLoginFormView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
//
//        // Enable Local Datastore, and set the application id and client key
//        Parse.enableLocalDatastore(this);
//        Parse.initialize(this, "Q7azOG47hd1mk0cPmwhUTm3DKcKapSHQepzNdSrd", "NA1kTUjHQqc2vbLDp5ywGvCBMGtLp6EKsAc91nxT");
//        ParseInstallation.getCurrentInstallation().saveInBackground();
//
//        // Set up the login form.
//        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
//        mPasswordView = (EditText) findViewById(R.id.password);
//
//        Button mSignInButton = (Button) findViewById(R.id.email_sign_in_button);
//        mSignInButton.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                attemptLogin(mEmailView.getText().toString(), mPasswordView.getText().toString());
//            }
//        });
//
//        mLoginFormView = findViewById(R.id.login_form);
//        mProgressView = findViewById(R.id.login_progress);
//    }
//
//    /**
//     * Attempts to sign in or register the account specified by the login form.
//     * If there are form errors (invalid email, missing fields, etc.), the
//     * errors are presented and no actual login attempt is made.
//     */
//    public void attemptLogin(String email, String password) {
//        // Reset errors.
//        mEmailView.setError(null);
//        mPasswordView.setError(null);
//
//        boolean mCancel = false;
//        View focusView = null;
//
//        // Check for a valid password, if the user entered one.
//        if (!password.isEmpty() && !isPasswordValid(password)) {
//            mPasswordView.setError(getString(R.string.error_invalid_password));
//            focusView = mPasswordView;
//            mCancel = true;
//        }
//
//        // Check for a valid email address.
//        if (email.isEmpty()) {
//            mEmailView.setError(getString(R.string.error_field_required));
//            focusView = mEmailView;
//            mCancel = true;
//        } else if (!isEmailValid(email)) {
//            mEmailView.setError(getString(R.string.error_invalid_email));
//            focusView = mEmailView;
//            mCancel = true;
//        }
//
//        if (mCancel) {
//            // There was an error; don't attempt login and focus the first
//            // form field with an error.
//            focusView.requestFocus();
//        } else {
//            // Show a progress spinner, and kick off a background task to
//            // perform the user login attempt.
//            ParseUser.logInInBackground(email, password, new LogInCallback() {
//                public void done(ParseUser user, com.parse.ParseException e) {
//                    if (user != null) {
//                        Intent i = new Intent(LoginActivity.this, VenueActivity.class);
//                        startActivity(i);
//                    } else {
//                        Toast.makeText(getApplicationContext(),
//                                "There was an error logging in.",
//                                Toast.LENGTH_LONG).show();
//                    }
//                }
//            });
//        }
//    }
//
//    private boolean isEmailValid(String email) {
//        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
//    }
//
//    private boolean isPasswordValid(String password) {
//        return password.length() > 6;
//    }
//}
//
//
//
