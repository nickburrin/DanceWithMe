package seniordesign.com.dancewithme.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

import com.parse.ParseException;

import seniordesign.com.dancewithme.R;


public class ForgotYourPassword extends Activity {
    private EditText mEdit = null;
    private final int emailLength = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_your_password);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_forgot_your_password, menu);
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

    private void validateInputs() throws Exception
    {
        String email = mEdit.getText().toString();
        if(email.length() > emailLength)
        {
            throw new Exception("Your email exceeds " +emailLength + "characters.");
        }
        if(!isEmailValid(email))
        {
            throw new Exception("The email is invalid. Please re-enter a valid email.");
        }

    }

    private boolean isEmailValid(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void resetPassword(String email)
    {
        ParseUser.requestPasswordResetInBackground(email, new RequestPasswordResetCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    // An email was successfully sent with reset instructions.
                } else {
                    // Something went wrong. Look at the ParseException to see what's up.
                }
            }
        });

    }

    public void emailPassword(View view)
    {
        mEdit = (EditText) findViewById(R.id.email_reset_password);

        try
        {
            this.validateInputs();

            this.resetPassword(mEdit.getText().toString());

            Toast.makeText(ForgotYourPassword.this, "Your password has been emailed to you.", Toast.LENGTH_SHORT).show();
            this.displayLoginPage(view);
        }
        catch(Exception e)
        {
            Toast.makeText(ForgotYourPassword.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    public void displayLoginPage(View view)
    {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}