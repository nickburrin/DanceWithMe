package seniordesign.com.dancewithme.activities;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import com.parse.LogInCallback;
//import com.parse.ParseAnalytics;
import com.parse.ParseInstallation;
import com.parse.ParseUser;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;


import seniordesign.com.dancewithme.R;
import seniordesign.com.dancewithme.fragments.MessageFragment;
import seniordesign.com.dancewithme.utils.Logger;


public class LoginActivity extends Activity {
    private static final String TAG = "Touba";

    private Button signUpButton;
    private Button loginButton;
    private LoginButton facebookLoginButton;
    private Button forgotPasswordButton;
    private EditText emailField;
    private EditText passwordField;
    private Intent intent;
    private Intent serviceIntent;
    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = new Intent(getApplicationContext(), HomeActivity.class);
        serviceIntent = new Intent(getApplicationContext(), MessageService.class);
        Intent i;
        /*
        if(ParseUser.getCurrentUser() != null){
            startService(serviceIntent);
            startActivity(new Intent(this, HomeActivity.class));
            //startActivity(new Intent(this, VenueActivity.class));   // temp for Nick
        }
        */


//        Parse.enableLocalDatastore(this);

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
        callbackManager = CallbackManager.Factory.create();
        facebookLoginButton = (LoginButton) findViewById(R.id.login_button);
        facebookLoginButton.setReadPermissions("user_friends");
        // If using in a fragment
        //facebookLoginButton.setFragment(this);
        // Other app specific specialization

        // Callback registration

        facebookLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code

            }

            @Override
            public void onCancel() {
                // App code

            }

            @Override
            public void onError(FacebookException exception) {
                // App code

            }
        });

        facebookLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LoginManager myLoginManager = LoginManager.getInstance();//.logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "user_friends"));
                AccessToken myAccessToken = AccessToken.getCurrentAccessToken();
                Profile myProfile = Profile.getCurrentProfile();
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
                // start Facebook Login

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