package seniordesign.com.dancewithme.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import seniordesign.com.dancewithme.R;
import seniordesign.com.dancewithme.pojos.Dislikes;
import seniordesign.com.dancewithme.pojos.Likes;
import seniordesign.com.dancewithme.pojos.Matches;
import seniordesign.com.dancewithme.utils.Logger;


public class LoginActivity extends Activity {
    private static final String TAG = LoginActivity.class.getSimpleName();

    private LoginButton facebookLoginButton;
    private Button forgotPasswordButton;
    private EditText emailField;
    private EditText passwordField;
    private CallbackManager callbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Initialize SDK before setContentView(Layout ID)
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
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

        // This needs to go in onResume()
        if (ParseUser.getCurrentUser() != null) {
            startService(new Intent(LoginActivity.this, MessageService.class));
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
        }

        //       forgotPasswordButton = (Button) findViewById(R.id.forgotyourpasswordButton);
        emailField = (EditText) findViewById(R.id.loginUsername);
        passwordField = (EditText) findViewById(R.id.loginPassword);

        findViewById(R.id.loginButton).setOnClickListener(new View.OnClickListener() {
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
                            startService(new Intent(LoginActivity.this, MessageService.class));
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "Wrong username/password combo",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        findViewById(R.id.forgotPasswordButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String email = emailField.getText().toString();
//                String password = passwordField.getText().toString();
//                ParseInstallation installation = ParseInstallation.getCurrentInstallation();
//                installation.put("username", email);
//                installation.saveInBackground();
//                Logger.d(TAG, "User Logging in");
//                ParseUser.logInInBackground(email, password, new LogInCallback() {
//                    public void done(ParseUser user, com.parse.ParseException e) {
//                        if (user != null) {
//                            startService(new Intent(LoginActivity.this, MessageService.class));
//                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
//                        } else {
//                            Toast.makeText(getApplicationContext(),
//                                    "Wrong username/password combo",
//                                    Toast.LENGTH_LONG).show();
//                        }
//                    }
//                });
                Intent i = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(i);
            }
        });

        findViewById(R.id.signupButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailField.getText().toString();

                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                registerIntent.putExtra("email", email);
                startActivity(registerIntent);
            }

        });

        facebookLoginButton = (LoginButton) findViewById(R.id.facebook_login_button);
        facebookLoginButton.setReadPermissions("email");    // Includes default permission "public_profile" and now "email"
        facebookLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                checkIfFacebookUserIsDanceWithMeUser(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "FB Login was canceled");
            }

            @Override
            public void onError(FacebookException e) {
                Log.d(TAG, "FB Login was canceled");
                e.printStackTrace();
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

    private void checkIfFacebookUserIsDanceWithMeUser(AccessToken token) {
        GraphRequest getProfInfo = GraphRequest.newMeRequest(token,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            String facebookId = object.getString("id");
                            String firstname = object.getString("first_name");
                            String lastname = object.getString("last_name");
                            String email = object.getString("email");

                            String gender;
                            if (object.getString("gender").equalsIgnoreCase("male")) {
                                gender = "Male";
                            } else {
                                gender = "Female";
                            }

                            ParseQuery<ParseUser> doesUserExist = ParseUser.getQuery();
                            doesUserExist.whereEqualTo("email", email);
                            try {
                                if (doesUserExist.count() == 0) {
                                    createAccount(email, facebookId, firstname, lastname, gender);
                                } else {
                                    attemptLogin(email, facebookId);
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,first_name,last_name,gender,email");
        getProfInfo.setParameters(parameters);
        getProfInfo.executeAsync();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data); // This is for just straight up facebook
    }

    @Override
    public void onDestroy() {
        stopService(new Intent(this, MessageService.class));
        super.onDestroy();
    }

    private ParseUser createAccount(String email, String password, String firstname, String lastname, String gender) {
        ParseUser newUser = null;

        //Create the new account
        newUser = new ParseUser();
        newUser.setEmail(email);
        newUser.setUsername(email);
        newUser.setPassword(password);
        newUser.put("first_name", firstname);
        newUser.put("last_name", lastname);
        newUser.put("gender", gender);

        //newUser.put("ProfilePicture", profilePicture);

        newUser.signUpInBackground(new SignUpCallback() {
            public void done(com.parse.ParseException e) {
                if (e == null) {
                    // In addition to sign-up, add a Matches object for this user
                    Matches newMatcher = new Matches(ParseUser.getCurrentUser().getObjectId(), Arrays.asList());
                    ParseUser.getCurrentUser().put("Likes", new Likes(ParseUser.getCurrentUser().getObjectId()));
                    ParseUser.getCurrentUser().put("Dislikes", new Dislikes(ParseUser.getCurrentUser().getObjectId()));
                    ParseUser.getCurrentUser().put("Matches", newMatcher);
                    ParseUser.getCurrentUser().put("Likes", new Likes(ParseUser.getCurrentUser().getObjectId()));
                    ParseUser.getCurrentUser().put("Dislikes", new Dislikes(ParseUser.getCurrentUser().getObjectId()));
                    ParseUser.getCurrentUser().saveInBackground();

                    // Redirect to ProfileFragment
                    Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                    i.putExtra("first_time", true);
                    startActivity(i);
                } else {
                    Toast.makeText(getApplicationContext(), "There was an error signing up.",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        return newUser;
    }

    private void attemptLogin(String email, String password) {
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("username", email);
        installation.saveInBackground();

        Logger.d(TAG, "User Logging in");
        ParseUser.logInInBackground(email, password, new LogInCallback() {
            public void done(ParseUser user, com.parse.ParseException e) {
                if (user != null) {
                    startService(new Intent(LoginActivity.this, MessageService.class));
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                } else {
                    Toast.makeText(getApplicationContext(), "There was an error logging in", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}