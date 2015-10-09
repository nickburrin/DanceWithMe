package seniordesign.com.dancewithme.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by nickburrin on 9/21/15.
 */
public class DancehallListAdapter extends ArrayAdapter<Object> {
    private static final String TAG = DancehallListAdapter.class.getSimpleName();

    private ArrayList<Object> dancehalls;
    Activity context;

    static class ViewHolder {
        TextView dancehall;
        TextView address;
    }


    public DancehallListAdapter(Activity context, ArrayList items) {
        super(context, 0,  items);
        this.context = context;
        this.dancehalls = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder = null;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            holder = new ViewHolder();

            //Get whatever is @ position
            Object item = getItem(position);
            int itemPosition = getItemViewType(position);
        }


        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        // we have one view types, namely the dancehalls
        return 1;
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public int getCount() {
        return dancehalls.size();
    }

    @Override
    public Object getItem(int position) {
        Object item = null;

        if (position < dancehalls.size()) {
            item = dancehalls.get(position);
        }
        return item;
    }
}
