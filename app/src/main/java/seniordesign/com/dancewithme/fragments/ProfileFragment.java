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

import seniordesign.com.dancewithme.R;
import seniordesign.com.dancewithme.activities.DanceStyleActivity;
import seniordesign.com.dancewithme.activities.HomeActivity;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.activity = (HomeActivity)this.getActivity();

        getUser();
    }

    private void getUser() {
        User = ParseUser.getCurrentUser();

        if(User != null) {
            ParseFile profilePic = (ParseFile) User.get("ProfilePicture");
            if(profilePic!=null) {
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
        System.out.println(User.getList("DanceStyles"));
        //initFragment();
    }

    private void initFragment() {
        // Instaniating an array list (you don't need to do this, you already have yours).
        ArrayList<Object> userStyles = (ArrayList<Object>) User.get("DanceStyles");
        if(userStyles == null || userStyles.isEmpty()){
            userStyles = new ArrayList<Object>();
        }

        styleListAdapter = new DanceStyleListAdapter(activity.getApplicationContext(),
                userStyles, (MyApplication)activity.getApplication());
        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and your
        // array as a third parameter.
//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
//                this.activity,
//                android.R.layout.simple_list_item_1,
//                userStyles );
//
        stylesList.setAdapter(styleListAdapter);
        stylesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object item = styleListAdapter.getItem(position);
                if (item instanceof DanceStyle) {
                    // If clicked on style, go to dance activity
                    Log.d(TAG, "Clicked on dancestyle:" + item);

                    // Get dance obj
                    DanceStyle style = (DanceStyle) item;

                    Intent i = new Intent(getActivity(), DanceStyleActivity.class);

                    // Send game id to the bet activity
                    i.putExtra("style", style.getStyle());

                    // Start the activity
                    startActivity(i);
                }
            }
        });
    }

    public void loadImagefromGallery(View view) {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }

    public void addDanceStyle(){

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

                ImageView imgView = (ImageView) this.activity.findViewById(R.id.profPic);
                if(bm!=null){ bm.recycle(); }
                bm = BitmapFactory.decodeFile(imgDecodableString);
                ((BitmapDrawable)imgView.getDrawable()).getBitmap().recycle();
                imgView.setImageBitmap(bm);


                Bitmap out = Bitmap.createScaledBitmap(bm,(int)(bm.getWidth()*0.25),(int)(bm.getHeight()*0.25),true);
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
        }

    }

    public void displayMatchingInterface(View view)
    {
        Toast.makeText(activity.getApplicationContext(), "Please wait. We are searching for your dance partner",
                Toast.LENGTH_SHORT).show();
        //Intent intent = new Intent(this, InvalidLoginActivity.class);
        //startActivity(intent);
        intent = new Intent(activity.getApplicationContext(), HomeActivity.class);
        serviceIntent = new Intent(activity.getApplicationContext(), MessageService.class);
        startActivity(intent);
        //startService(serviceIntent);
    }
}
