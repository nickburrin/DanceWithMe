package seniordesign.com.dancewithme.activities;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.parse.ParseUser;
import android.widget.TextView;
import android.widget.Toast;

import seniordesign.com.dancewithme.R;

public class ProfileManagement extends Activity {

    private EditText mUsernameView;
    private ImageButton mProfPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_management);
        ((TextView) findViewById(R.id.username)).setText(ParseUser.getCurrentUser().get("first_name")
                + " " + ParseUser.getCurrentUser().get("last_name"));

        mProfPic = (ImageButton) findViewById(R.id.profPic);
        mProfPic.setImageDrawable(null); //fill with an image
    }

    public void changeProfPic(View v) {
        //do stuff
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile_management, menu);
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


    public void displayMatchingInterface(View view)
    {
        Toast.makeText(getApplicationContext(), "Please wait. We are searching for your dance partner",
                Toast.LENGTH_SHORT).show();
        //Intent intent = new Intent(this, InvalidLoginActivity.class);
        //startActivity(intent);
    }

}
