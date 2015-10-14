package seniordesign.com.dancewithme.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.parse.ParseException;

import java.util.ArrayList;

import seniordesign.com.dancewithme.R;
import seniordesign.com.dancewithme.activities.MyApplication;
import seniordesign.com.dancewithme.pojos.DanceStyle;

/**
 * Created by nickburrin on 10/6/15.
 */
public class DanceStyleListAdapter extends ArrayAdapter<Object> {
    private static final String TAG = DanceStyleListAdapter.class.getSimpleName();

    private ArrayList<Object> stylesList;
    private MyApplication application;

    /**
     * A container for a specific deal in the ListView.
     */
    static class ViewHolder {
        TextView style;
        TextView skill;
    }

    public DanceStyleListAdapter(Context context, ArrayList<Object> list, MyApplication app) {
        super(context, 0, list);
        this.stylesList = list;
        this.application = app;

//        Logger.d(TAG, "games size = " + stylesList.size());
//        for(int i = 0; i < stylesList.size(); i++){
//            Logger.d(TAG, stylesList.get(i).toString());
//        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            holder = new ViewHolder();

            if (getItemViewType(position) == 0) {
                // Game object in array
                convertView = inflater.inflate(R.layout.list_item_generic, parent, false);

                // list item views
                holder.style = (TextView) convertView.findViewById(R.id.tv_title);
                holder.skill = (TextView) convertView.findViewById(R.id.tv_description);
            }
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Object item = getItem(position);

        if (getItemViewType(position) == 0) {
            DanceStyle style = null;
            try {
                style = ((DanceStyle)item).fetchIfNeeded();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            holder.style.setText(style.getStyle());
            holder.skill.setText(style.getSkill());
        }

        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        // we have two view types, games and times
        return 1;
    }

    @Override
    public int getCount() {
        return stylesList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Object item = null;
        if (position < stylesList.size())
            item = stylesList.get(position);

        if (item != null && item instanceof DanceStyle)
            return 0; // DanceStyle object
        else
            return 1; // Some other item
    }

    @Override
    public Object getItem(int position) {
        Object item = null;
        if (position < stylesList.size())
            item = stylesList.get(position);
        return item;
    }
}
