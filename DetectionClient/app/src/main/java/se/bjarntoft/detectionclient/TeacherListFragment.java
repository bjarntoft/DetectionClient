package se.bjarntoft.detectionclient;

import android.app.ListFragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Andreas on 2016-04-04.
 */
public class TeacherListFragment extends ListFragment {
    private ViewGroup rootView;
    private MainActivity parentActivity;

    // Lista.
    private CustomAdapter customAdapter;
    private List<ListItem> listItem = new ArrayList<ListItem>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        parentActivity = (MainActivity)getActivity();
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(parentActivity);
        String id = sharedPreferences.getString(AppPreferences.USER_ID, "");
        String password = sharedPreferences.getString(AppPreferences.USER_PASSWORD, "");

        // Extraherar användaruppgifter till en sträng för http-request.
        String request = "id=" + id + "&password=" + password;

        // Begär lista över lärare.
        GetTeachersTask getTeachersTask = new GetTeachersTask();
        getTeachersTask.execute(parentActivity, this, request);

        // Skapar skal till lista.
        customAdapter = new CustomAdapter(parentActivity, listItem);
        setListAdapter(customAdapter);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup)inflater.inflate(R.layout.fragment_teacherlist, container, false);

        return rootView;
    }


    public void addListItem(String id, String name, String status, String connected) {
        ListItem item = new ListItem(id, name, status, connected);
        listItem.add(item);
    }


    public void updateList() {
        customAdapter.notifyDataSetChanged();
    }

    public void clearList() {
        listItem.clear();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        // Extraherar formulärsdata till en sträng för http-request.
        String request = "id=" + listItem.get(position).getId();

        // Loggar in användaren.
        NotificationTask notificationTask = new NotificationTask();
        notificationTask.execute(parentActivity, this, request);

        System.out.println("Önskar besök: " + request);

    }
}
