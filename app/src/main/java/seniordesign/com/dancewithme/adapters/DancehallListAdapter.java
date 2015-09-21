package seniordesign.com.dancewithme.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by nickburrin on 9/21/15.
 */
public class DancehallListAdapter extends ArrayAdapter<Object> {
    private static final String TAG = DancehallListAdapter.class.getSimpleName();

    private ArrayList<Object> dancehalls;
    Activity context;

    public DancehallListAdapter(Activity context, ArrayList items) {
        super(context, 0,  items);
        this.context = context;
        this.dancehalls = items;
    }

    @Override
    public long GetItemId(int position)
    {
        return position;
    }

    @Override
    public string this[int position] {
        get { return items[position]; }
    }

    @Override
    public int getCount() {
        return dancehalls.size();
    }

    @Override
    public View GetView(int position, View convertView, ViewGroup parent)
    {
        View view = convertView; // re-use an existing view, if one is available
        if (view == null) // otherwise create a new one
            view = context.LayoutInflater.Inflate(Android.Resource.Layout.SimpleListItem1, null);
        view.findViewById<TextView>(Android.Resource.Id.Text1).Text = dancehalls.get(position);
        return view;
    }

}
