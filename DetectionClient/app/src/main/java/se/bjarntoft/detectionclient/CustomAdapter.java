package se.bjarntoft.detectionclient;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.sql.Array;
import java.util.List;

/**
 * Created by Andreas on 2016-04-05.
 */
public class CustomAdapter extends BaseAdapter {
    Context context;
    List<ListItem> rowItem;

    String[] btUnits = {"00:06:66:64:33:30", "00:06:66:64:43:75", "00:06:66:45:B7:09", "00:06:66:64:45:39", "00:06:66:60:2C:C3"};


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
        LinearLayout llRow = (LinearLayout)convertView.findViewById(R.id.list_row);
        TextView tvTitel = (TextView)convertView.findViewById(R.id.list_title);
        TextView tvStatus = (TextView)convertView.findViewById(R.id.list_status);
        TextView tvVisit = (TextView)convertView.findViewById(R.id.list_visit);

        ListItem row = rowItem.get(position);
        String connected = row.getConnected();

        tvTitel.setText(row.getName() + " (" + row.getId() + ")");

        // Kontrollerar tillgänglighet.
        if(row.getStatus().equals("Upptagen")) {
            tvVisit.setText("");
            convertView.setEnabled(false);
            convertView.setOnClickListener(null);

            tvStatus.setText(row.getStatus());
        } else {
            tvStatus.setText("");
        }

        // Kontrollerar zon-närvaro.
        Boolean visitingFlag = false;
        for(String item: btUnits) {
            if(item.equals(connected)) {
                visitingFlag = true;
            }
        }

        // Uppdaterar gui baserat på zon-närvaro.
        if(!visitingFlag) {
            llRow.setVisibility(View.GONE);
        } else {
            llRow.setVisibility(View.VISIBLE);
        }

        return convertView;
    }
}
