package se.bjarntoft.detectionclient;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


/**
 * Created by Andreas Bjärntoft on 2016-03-29.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {
    private ViewGroup rootView;
    private MainActivity parentActivity;

    // Gui-kompontneter.
    private TextView tfLoginInfo;
    private TextView tfMeetingInfo;
    private EditText tfLoginId;
    private EditText tfLoginPassword;
    private Button btnLogin;
    private TextView tvLoginStatus;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        parentActivity = (MainActivity)getActivity();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup)inflater.inflate(R.layout.fragment_login, container, false);

        // Identifierar gui-komponenter.
        tfLoginInfo = (TextView)rootView.findViewById(R.id.loginInfo);
        tfMeetingInfo = (TextView)rootView.findViewById(R.id.meetingInfo);
        tfLoginId = (EditText)rootView.findViewById(R.id.loginId);
        tfLoginPassword = (EditText)rootView.findViewById(R.id.loginPassword);
        btnLogin = (Button)rootView.findViewById(R.id.loginButton);
        tvLoginStatus = (TextView)rootView.findViewById(R.id.loginStatus);

        // Ansluter lyssnare.
        btnLogin.setOnClickListener(this);



        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(parentActivity.getApplication());

        if(sharedPreferences.getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false)) {
            tfMeetingInfo.setText("Aktiverat");
        } else {
            tfMeetingInfo.setText("Ej aktiverat");
        }

        if(sharedPreferences.getBoolean(QuickstartPreferences.USER_LOGGED_IN, false)) {
            tfMeetingInfo.setText(sharedPreferences.getString(QuickstartPreferences.USER_ID, ""));
        } else {
            tfMeetingInfo.setText("Ej inloggad");
        }


        return rootView;
    }


    @Override
    public void onClick(View v) {
        if (v == btnLogin) {
            // Extraherar formulärsdata till en sträng för http-request.
            String request = "id=" + tfLoginId.getText().toString().trim() + "&password=" + tfLoginPassword.getText().toString().trim();

            // Loggar in användaren.
            LoginTask loginTask = new LoginTask();
            loginTask.execute(parentActivity, this, request);
        }
    }


    public void setLoginStatus(String status) {
        tvLoginStatus.setText(status);
    }
}
