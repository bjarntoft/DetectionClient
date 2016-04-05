package se.bjarntoft.detectionclient;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Andreas on 2016-04-05.
 */
public class CustomAdapter extends BaseAdapter {
    Context context;
    List<ListItem> rowItem;


    CustomAdapter(Context context, List<ListItem> rowItem) {
        this.context = context;
        this.rowItem = rowItem;
    }


    @Override
    public int getCount() {
        return rowItem.size();
    }


    @Override
    public Object getItem(int position) {
        return rowItem.get(position);
    }


    @Override
    public long getItemId(int position) {
        return rowItem.indexOf(getItem(position));
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.list_item, null);
        }

        // Identifierar gui-komponenter.
        TextView tvTitel = (TextView)convertView.findViewById(R.id.list_title);
        TextView tvStatus = (TextView)convertView.findViewById(R.id.list_status);

        ListItem row = rowItem.get(position);

        tvTitel.setText(row.getName());
        tvStatus.setText(row.getStatus());

        return convertView;
    }
}
