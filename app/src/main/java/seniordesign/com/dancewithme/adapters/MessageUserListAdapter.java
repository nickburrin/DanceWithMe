package seniordesign.com.dancewithme.adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.logging.Logger;

import seniordesign.com.dancewithme.R;
import seniordesign.com.dancewithme.activities.MyApplication;
import seniordesign.com.dancewithme.pojos.DanceStyle;

/**
 * Created by ryantemplin on 11/10/15.
 */
public class MessageUserListAdapter extends ArrayAdapter<ParseUser>{
    private static final String TAG = MessageUserListAdapter.class.getSimpleName();

    private ArrayList<ParseUser> usersList;
    private MyApplication application;
    private boolean favoriteStatus;

    /**
     * A container for a specific deal in the ListView.
     */
    static class ViewHolder {
        ImageButton userProfPic;
        TextView userName;
        ImageView yellowStar;
    }

    public MessageUserListAdapter(Context context, ArrayList<ParseUser> usersList, MyApplication app, boolean favorites) {
        super(context, 0, usersList);
        this.usersList = usersList;
        this.application = app;
        this.favoriteStatus = favorites;

        Log.d(TAG, "games size = " + usersList.size());
        for(int i = 0; i < usersList.size(); i++){
            Log.d(TAG, usersList.get(i).toString());
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            holder = new ViewHolder();
            if (getItemViewType(position) == 0) {
                // Game object in array
                convertView = inflater.inflate(R.layout.user_list_item, parent, false);

                // User message views
                holder.userProfPic = (ImageButton) convertView.findViewById(R.id.iv_user_prof_pic);
                holder.userName = (TextView) convertView.findViewById(R.id.tv_user_list_item);
                holder.yellowStar = (ImageView) convertView.findViewById(R.id.yellow_star);


            }
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ParseUser item = getItem(position);

        if (getItemViewType(position) == 0) {
            ParseUser user = (ParseUser)item;
//            Team homeTeam = game.getHomeTeam();
//            Team awayTeam = game.getAwayTeam();
//            String gameId = game.getData_id();

            //put the team name
            try {
                holder.userName.setText(user.fetchIfNeeded().getString("first_name"));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            //ImageUtil.displayHighResImage(holder.homeTeamLogoView, homeTeam.getLogoUrl());
            //ImageUtil.displayHighResImage(holder.awayTeamLogoView, awayTeam.getLogoUrl());
            ParseFile profilePic = (ParseFile) user.get("ProfilePicture");
            if(profilePic != null) {
                try {
                    holder.userProfPic.setImageBitmap(BitmapFactory.decodeByteArray(profilePic.getData(), 0, profilePic.getData().length));
                } catch (com.parse.ParseException e) {
                    e.printStackTrace();
                }
            } else{
                Log.d(TAG, "something is jacked up in message adapter");
            }


            if(favoriteStatus){
                holder.yellowStar.setVisibility(View.VISIBLE);
            }else{
                holder.yellowStar.setVisibility(View.GONE);
            }
        }

        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        // we have two view types, games and times
        return 2;
    }

    @Override
    public int getCount() {
        return usersList.size();
    }

    @Override
    public int getItemViewType(int position) {
        ParseUser item = null;
        if (position < usersList.size())
            item = usersList.get(position);

        if (item != null && item instanceof ParseUser)
            return 0; // Game object
        else
            return 1; // Time String
    }

    @Override
    public ParseUser getItem(int position) {
        ParseUser item = null;
        if (position < usersList.size())
            item = usersList.get(position);
        return item;
    }

//    private void populateGameHolder(final Game game, ViewHolder holder) {
//        ImageUtils.displayLowResImage(holder.imageView, game);
//
//    }

}