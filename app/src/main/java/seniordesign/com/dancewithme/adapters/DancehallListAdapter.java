package seniordesign.com.dancewithme.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.parse.ParseException;

import java.util.ArrayList;

import seniordesign.com.dancewithme.R;
import seniordesign.com.dancewithme.pojos.Dancehall;

/**
 * Created by nickburrin on 9/21/15.
 */
public class DancehallListAdapter extends ArrayAdapter<java.lang.Object> {
    private static final String TAG = DancehallListAdapter.class.getSimpleName();

    private ArrayList<java.lang.Object> dancehalls;
    Activity activity;

    static class ViewHolder {
        TextView dancehall;
        TextView address;
    }


    public DancehallListAdapter(Activity context, ArrayList items) {
        super(context, 0,  items);
        this.activity = context;
        this.dancehalls = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder = null;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            holder = new ViewHolder();

            if (getItemViewType(position) == 0) {
                convertView = inflater.inflate(R.layout.list_item_generic, parent, false);

                // list item views
                holder.dancehall = (TextView) convertView.findViewById(R.id.tv_title);
                holder.address = (TextView) convertView.findViewById(R.id.tv_description);
            } else {
                // Some other type of view
            }
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        java.lang.Object item = getItem(position);

        if (getItemViewType(position) == 0) {
            Dancehall venue = null;
            try {
                venue = ((Dancehall) item).fetchIfNeeded();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            holder.dancehall.setText(venue.getName());
            holder.address.setText(venue.getAddress());
        }

        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        // we have one view types, namely the dancehalls
        return 1;
    }

    @Override
    public int getCount() {
        return dancehalls.size();
    }

    @Override
    public int getItemViewType(int position) {
        java.lang.Object item = null;
        if (position < dancehalls.size())
            item = dancehalls.get(position);

        if (item != null && item instanceof Dancehall)
            return 0; // DanceStyle object
        else
            return 1; // Some other item
    }

    @Override
    public java.lang.Object getItem(int position) {
        java.lang.Object item = null;

        if (position < dancehalls.size()) {
            item = dancehalls.get(position);
        }
        return item;
    }
}
