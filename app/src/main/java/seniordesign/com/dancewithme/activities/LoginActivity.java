package seniordesign.com.dancewithme.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import com.parse.LogInCallback;
import com.parse.ParseInstallation;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import seniordesign.com.dancewithme.R;
import seniordesign.com.dancewithme.pojos.Matches;
import seniordesign.com.dancewithme.utils.Logger;


public class LoginActivity extends Activity {
    private static final String TAG = LoginActivity.class.getSimpleName();

    private Button signUpButton;
    private Button loginButton;
    private LoginButton facebookLoginButton;
    private Button forgotPasswordButton;
    private EditText emailField;
    private EditText passwordField;
    private Intent intent;
    private Intent serviceIntent;
    private boolean newUser;
    private String email;
    private String id;
    private String firstname;
    private String lastname;
    private String gender;
    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = new Intent(getApplicationContext(), HomeActivity.class);
        serviceIntent = new Intent(getApplicationContext(), MessageService.class);
        Intent i;

//        if(ParseUser.getCurrentUser() != null){
//            startService(serviceIntent);
//            startActivity(new Intent(this, HomeActivity.class));
//            //startActivity(new Intent(this, VenueFragment.class));   // temp for Nick
//        }



//        Parse.enableLocalDatastore(this);
        // Initialize SDK before setContentView(Layout ID)
        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_login);

        try {
            PackageInfo info = getPackageManager().getPackageInfo("seniordesign.com.dancewithme", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

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
        //View view = inflater.inflate(R.layout.splash, container, false);

        facebookLoginButton = (LoginButton) findViewById(R.id.login_button);
        //facebookLoginButton.setReadPermissions("user_friends");
        // If using in a fragment
        //facebookLoginButton.setFragment(this);
        // Other app specific specialization

        // Callback registration



        facebookLoginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Call private method
                onFblogin();
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

    // Private method to handle Facebook login and callback
    private void onFblogin()
    {
        callbackManager = CallbackManager.Factory.create();

        // Set permissions
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends", "email"));

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        Log.e("onSuccess", "--------" + loginResult.getAccessToken());
                        Log.e("Token", "--------" + loginResult.getAccessToken().getToken());
                        Log.e("Permision", "--------" + loginResult.getRecentlyGrantedPermissions());
                        Profile profile = Profile.getCurrentProfile();
                        firstname = profile.getFirstName();
                        lastname = profile.getLastName();

                       // Log.e("Image URI", "--" + profile.getLinkUri());

                        Log.e("OnGraph", "------------------------");
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {
                                        // Application code
                                        Log.e("GraphResponse", "-------------" + response.toString());
                                        try {

                                            email = object.getString("email");
                                            id = object.getString("id");
                                            //firstname = object.getString("first_name");
                                            //lastname = object.getString("last_name");
                                            gender = object.getString("gender");
                                            attemptLogin();
                                            if(newUser){
                                                createAccount();
                                            }


                                        } catch (JSONException e) {
                                                e.printStackTrace();
                                        }
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,gender,email");
                        request.setParameters(parameters);
                        request.executeAsync();

                    }

                    @Override
                    public void onCancel() {
                        Log.d(TAG,"On cancel");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d(TAG,error.toString());
                    }
                });
    }

    public void createAccount(){
        // Only create an account if all fields check out
        if(UIFieldsOK() == true) {
            // extract all UI fields, create ParseUser
            ParseUser user = extractParseUser();

            if(user != null){
                // push to Parse
                user.signUpInBackground(new SignUpCallback() {
                    public void done(com.parse.ParseException e) {
                        if (e == null) {
                            // In addition to sign-up, add a Matches object for this user
                            Matches newMatcher = new Matches(ParseUser.getCurrentUser().getObjectId(), Arrays.asList());
                            ParseUser.getCurrentUser().put("Matches", newMatcher);
                            ParseUser.getCurrentUser().saveInBackground();

                            // Redirect to ProfileFragment
                            intent.putExtra("first_time", true);
                            startActivity(intent);
                            startService(serviceIntent);

                        } else {
                            Toast.makeText(getApplicationContext(), "There was an error signing up.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        }else{
            Toast.makeText(this, "An non facebook account with this email already exists", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean UIFieldsOK() {
        boolean incompleteFields = false;
        View focusView = null;

        if(firstname.isEmpty()){
            // Check for first name
            incompleteFields = true;
        }

        if(lastname.isEmpty()){

            incompleteFields = true;
        }

        if (id.isEmpty()) {
            // Check if the user entered password
            incompleteFields = true;
        }

        if (email.isEmpty()) {
            incompleteFields = true;
        }

        if (gender.isEmpty()) {
            // Check if user entered email
            ((TextView)findViewById(R.id.tv_invisible_error)).setError(getString(R.string.error_select_gender));
            focusView = findViewById(R.id.tv_invisible_error);
            incompleteFields = true;
        }

        return !incompleteFields;
    }

    private ParseUser extractParseUser() {
        ParseUser newUser = null;

        // See if there exists an existing user with the same email
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("email", email);

        try {
            if(query.count() > 0){
                //There is already an account with this email
                Toast.makeText(this, "An account with this email already exists", Toast.LENGTH_SHORT).show();
            } else {
                //Create the new account
                newUser = new ParseUser();
                newUser.setEmail(email);
                newUser.setUsername(email);
                newUser.setPassword(id);
                newUser.put("first_name", firstname);
                newUser.put("last_name", lastname);
                newUser.put("gender", gender);
                newUser.put("Likes", Arrays.asList());
                newUser.put("Dislikes", Arrays.asList());
            }
        } catch (com.parse.ParseException e) {
            e.printStackTrace();
        }

        return newUser;
    }

    private void attemptLogin(){
        //boolean newUser;
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("username", email);
        installation.saveInBackground();
        Logger.d(TAG, "User Logging in");
        ParseUser.logInInBackground(email, id, new LogInCallback() {
            public void done(ParseUser user, com.parse.ParseException e) {
                if (user != null) {
                    startActivity(intent);
                    startService(serviceIntent);
                } else {
                    newUser =  true;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        stopService(new Intent(this, MessageService.class));
        super.onDestroy();
    }
}