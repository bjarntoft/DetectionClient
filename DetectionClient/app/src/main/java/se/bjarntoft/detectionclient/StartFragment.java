package se.bjarntoft.detectionclient;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


/**
 * Created by Andreas on 2016-03-31.
 */
public class StartFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private ViewGroup rootView;
    private MainActivity parentActivity;

    // Gui-komponenter.
    private TextView tvLoginStatus;
    private TextView tvSystemStatus;
    private Spinner spAccessStatus;
    private EditText etUserId;
    private EditText etUserPassword;
    private Button btLogin;
    private Button btLogout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        parentActivity = (MainActivity)getActivity();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup)inflater.inflate(R.layout.fragment_start, container, false);

        // Identifierar gui-komponenter.
        tvLoginStatus = (TextView)rootView.findViewById(R.id.start_loginStatus);
        tvSystemStatus = (TextView)rootView.findViewById(R.id.start_systemStatus);
        spAccessStatus = (Spinner)rootView.findViewById(R.id.start_statusSpinner);
        etUserId = (EditText)rootView.findViewById(R.id.start_userId);
        etUserPassword = (EditText)rootView.findViewById(R.id.start_userPassword);
        btLogin = (Button)rootView.findViewById(R.id.start_loginButton);
        btLogout = (Button)rootView.findViewById(R.id.start_logoutButton);

        // Ansluter lyssnare.
        btLogin.setOnClickListener(this);
        spAccessStatus.setOnItemSelectedListener(this);

        return rootView;
    }


    @Override
    public void onStart() {
        super.onStart();

        // Hämtar sparade variabler.
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(parentActivity.getApplication());
        boolean login = sharedPreferences.getBoolean(AppPreferences.USER_LOGGED_IN, false);
        boolean visitSystem = sharedPreferences.getBoolean(AppPreferences.SENT_TOKEN_TO_SERVER, false);

        // Anslutet spinner.
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(parentActivity, R.array.status_array, android.R.layout.simple_spinner_dropdown_item);
        spAccessStatus.setAdapter(arrayAdapter);

        // Uppdaterar gui.
        if(login) {
            tvLoginStatus.setText(sharedPreferences.getString(AppPreferences.USER_ID, "-"));
        } else {
            tvLoginStatus.setText("Ej inloggad");
        }

        // Uppdaterar gui.
        if(visitSystem) {
            tvSystemStatus.setText("Aktivt");
        } else {
            tvSystemStatus.setText("Ej aktivt");
        }
    }


    @Override
    public void onClick(View v) {
        if (v == btLogin) {
            // Extraherar formulärsdata till en sträng för http-request.
            String request = "id=" + etUserId.getText().toString().trim() + "&password=" + etUserPassword.getText().toString().trim();

            // Loggar in användaren.
            LoginTask loginTask = new LoginTask();
            loginTask.execute(parentActivity, this, request);
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public void setLoginStatus(String status) {
        tvLoginStatus.setText(status);
    }
}
