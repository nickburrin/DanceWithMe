package seniordesign.com.dancewithme.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogOutCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import seniordesign.com.dancewithme.R;
import seniordesign.com.dancewithme.fragments.ProfileFragment;

public class ChangePasswordActivity extends Activity {
    public static final int minPasswordLength = 6;
    public static final int maxPasswordLength = 30;


    private EditText mnewPasswordEditText = null;
    private EditText mnewConfirmPasswordEditText = null;


    private String newPassword = null;
    private String newConfirmPassword = null;

    //private ParseUser currentParseUser = ParseUser.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_change_password, menu);
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

    private void getAllUIFieldObjects()
    {
        this.mnewPasswordEditText = (EditText) findViewById(R.id.new_password_change_password);
        this.mnewConfirmPasswordEditText = (EditText) findViewById(R.id.confirm_new_password_change_password);
    }

    private void extractDataFromUIFields()
    {
        this.newPassword = this.mnewPasswordEditText.getText().toString();
        this.newConfirmPassword = this.mnewConfirmPasswordEditText.getText().toString();
    }

    private boolean validatePassword(String passwordInput)
    {
        int len = passwordInput.length();
        return ((len > minPasswordLength) && (len < maxPasswordLength));
    }

    private void validateInputs() throws Exception
    {
        int i = 0;
        StringBuilder errorSB = new StringBuilder("Please correct the following fields:\n");

        //Validate the length of the new password
        if(!this.validatePassword(this.newPassword))
        {
            errorSB.append("Your new password must be between" + minPasswordLength + " and " + maxPasswordLength + ".\n");
            i++;
        }

        //Validate the length of the new confirm password
        if(!this.validatePassword(this.newConfirmPassword))
        {
            errorSB.append("Your new confirm password must be between" + minPasswordLength + " and " + maxPasswordLength + ".\n");
            i++;
        }

        //Validate that the new password matches the new confirm password
        if(!this.newPassword.equals(this.newConfirmPassword))
        {
            errorSB.append("Your new password must match your new confirm password.\n");
            i++;
        }

        if(i > 0)
        {
            throw new Exception(errorSB.toString());
        }
    }

    private void processChangeYourPassword()
    {
        ParseUser.getCurrentUser().setPassword(this.newPassword);
        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (null == e) {
                    Toast.makeText(getApplicationContext(), "Your password has been changed!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Your password could not be changed.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void changeYourPasswordDriver(View view)
    {
        try
        {
            //Obtain Object References to each of data fields
            this.getAllUIFieldObjects();

            //Extract data
            this.extractDataFromUIFields();

            //validate all fields
            this.validateInputs();

            //change your password
            this.processChangeYourPassword();

            //this.displayProfilePage(view);
        }
        catch(Exception e)
        {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

}



//package seniordesign.com.dancewithme.activities;
//
//import android.support.v7.app.ActionBarActivity;
//import android.os.Bundle;
//import android.view.Menu;
//import android.view.MenuItem;
//
//import seniordesign.com.dancewithme.R;
//
//public class ChangePassword extends ActionBarActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_change_password);
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_change_password, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//}
