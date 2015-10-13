package seniordesign.com.dancewithme.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;


import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import seniordesign.com.dancewithme.R;
import seniordesign.com.dancewithme.activities.DanceStyleActivity;
import seniordesign.com.dancewithme.activities.HomeActivity;
import seniordesign.com.dancewithme.activities.LoginActivity;
import seniordesign.com.dancewithme.activities.MessageService;
import seniordesign.com.dancewithme.activities.MyApplication;
import seniordesign.com.dancewithme.adapters.DanceStyleListAdapter;
import seniordesign.com.dancewithme.pojos.DanceStyle;


public class ProfileFragment extends HomeTabFragment {
    private static final String TAG = ProfileFragment.class.getSimpleName();

    private static int RESULT_LOAD_IMG = 1;
    String imgDecodableString;

    private ImageButton profPic;
    private TextView usernameView;
    private Button editProf;
    private Button addDanceStyles;
    private ListView stylesList;
    private DanceStyleListAdapter styleListAdapter;

    ParseUser User;
    private Bitmap bm = null;
    private HomeActivity activity;

    private Intent intent;
    private Intent serviceIntent;
    private View view;
    private Button logoutButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.activity = (HomeActivity)this.getActivity();
    }

    private void getUser() {
        User = ParseUser.getCurrentUser();

        if(User != null) {
            ParseFile profilePic = (ParseFile) User.get("ProfilePicture");
            if(profilePic != null) {
                try {
                    bm = BitmapFactory.decodeByteArray(profilePic.getData(), 0, profilePic.getData().length);
                    profPic.setImageBitmap(bm);
                } catch (com.parse.ParseException e) {
                    Toast.makeText(this.activity.getApplicationContext(), "No profile pic", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }else{
            // TODO: should exit to the login screen, this shouldnt really happen
            Toast.makeText(this.activity.getApplicationContext(), "No user", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        usernameView = ((TextView) view.findViewById(R.id.tv_username));
        usernameView.setText(ParseUser.getCurrentUser().get("first_name") + " " + ParseUser.getCurrentUser().get("last_name"));

        profPic = (ImageButton) view.findViewById(R.id.ib_profPic);
        profPic.setOnTouchListener(new ImageView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    loadImagefromGallery(v);
                    return true;
                }
                return false;
            }
        });

        editProf = (Button) view.findViewById(R.id.button_edit_profile);
        editProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(ParseUser.getCurrentUser().get("danceStyles"));
                //enable the user to modify the name, gender, etc.
            }
        });

        addDanceStyles = (Button) view.findViewById(R.id.button_add_dancestyles);
        addDanceStyles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //enable the user to add DanceStyles
                Intent i = new Intent(activity, DanceStyleActivity.class);
                startActivity(i);
            }
        });
        stylesList = (ListView) view.findViewById(R.id.lv_dance_styles);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getUser();
        initFragment();
    }

    @Override
    public void onResume(){
        super.onResume();
        initFragment();
    }

    private void initFragment() {
        ArrayList<Object> userStyles = (ArrayList<Object>) ParseUser.getCurrentUser().getList("danceStyles");

        if(userStyles.isEmpty()){
            Toast.makeText(activity.getApplicationContext(), "Welcome! Specify your dance styles and start matching!",
                    Toast.LENGTH_SHORT).show();
        } else{
            styleListAdapter = new DanceStyleListAdapter(activity.getApplicationContext(),
                    userStyles, (MyApplication)activity.getApplication());

            stylesList.setAdapter(styleListAdapter);
            stylesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Object item = styleListAdapter.getItem(position);
                    if (item instanceof DanceStyle) {
                        // If clicked on style, go to dance activity
                        // Get dance obj
                        DanceStyle style = (DanceStyle) item;
                        Log.d(TAG, "Clicked on dancestyle:" + style.getId());

                        Intent i = new Intent(getActivity(), DanceStyleActivity.class);
                        i.putExtra("style_id", style.getObjectId());  // Send objectId to the DanceStyleActivity
                        startActivity(i);
                    }
                }
            });
        }
    }

    public void loadImagefromGallery(View view) {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }

    //@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        activity.getMenuInflater().inflate(R.menu.menu_profile_management, menu);

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        User = ParseUser.getCurrentUser();
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == this.activity.RESULT_OK && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = this.activity.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = null;
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();

                ImageView imgView = (ImageView) this.activity.findViewById(R.id.ib_profPic);
                if(bm!=null){
                    bm.recycle();
                }
                bm = BitmapFactory.decodeFile(imgDecodableString);
                ((BitmapDrawable)imgView.getDrawable()).getBitmap().recycle();
                imgView.setImageBitmap(bm);


                Bitmap out = Bitmap.createScaledBitmap(bm, (int)(bm.getWidth()*0.25), (int)(bm.getHeight()*0.25), true);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                out.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                stream.close();
                stream = null;
                ParseFile pf = new ParseFile(byteArray);
                User.put("ProfilePicture", pf);
                User.saveInBackground();
            } else {
                Toast.makeText(this.activity, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this.activity, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
            e.printStackTrace();
        }

    }
}