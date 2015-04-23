package seniordesign.com.dancewithme;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //this.createUser();
        //this.putPrivateData();
        //ParseUser.getCurrentUser().;
    }

    private void putPrivateData() {
        ParseObject privateData = new ParseObject("PrivateUserData");
        privateData.setACL(new ParseACL(ParseUser.getCurrentUser()));
        privateData.put("phoneNumber", "281-650-1466");
        ParseUser.getCurrentUser().put("privateData", privateData);
    }

    private void createUser() {
        ParseUser user = new ParseUser();
        user.setUsername("nickburrin18");
        user.setPassword("Barney11");
        user.setEmail("nickburrin@utexas.edu");
        user.put("first_name", "Nick");
        user.put("last_name", "Burrin");

        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    System.out.println("Success!");
                } else {
                    System.out.println("Failure.");
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}