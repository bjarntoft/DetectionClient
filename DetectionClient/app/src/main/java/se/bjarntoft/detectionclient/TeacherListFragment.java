package se.bjarntoft.detectionclient;

import android.app.ListFragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;


/**
 * Created by Andreas on 2016-04-04.
 */
public class TeacherListFragment extends ListFragment {
    private ViewGroup rootView;
    private MainActivity parentActivity;
    String[] values;


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

        // Extraherar formulärsdata till en sträng för http-request.
        String request = "id=" + id + "&password=" + password;

        // Loggar in användaren.
        GetTeachersTask getTeachersTask = new GetTeachersTask();
        getTeachersTask.execute(parentActivity, this, request);

        //String[] values = {"aaa", "bbb", "ccc", "ddd"};

        while(values==null) {
            System.out.println("Väntar...");
        }

        ArrayAdapter adapter = new ArrayAdapter(parentActivity, android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup)inflater.inflate(R.layout.fragment_teacherlist, container, false);

        return rootView;
    }

    public void setValues(String[] values) {
        this.values = values;
    }

}
