package se.bjarntoft.detectionclient;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by Andreas Bjärntoft on 2016-04-12.
 */
public class BluetoothDetectionTask extends AsyncTask<Object, Void, Void> {
    @Override
    protected Void doInBackground(Object... params) {
        // Extraherar indata.
        Context context = (Context)params[0];
        String macAdress = (String)params[1];

        // Hämtar användaruppgifter.
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String id = sharedPreferences.getString(AppPreferences.USER_ID, "");
        String password = sharedPreferences.getString(AppPreferences.USER_PASSWORD, "");

        // Konstruerar http-request.
        String request = "id=" + id + "&password=" + password + "&detection=" + macAdress;

        try {
            URL url = new URL(AppPreferences.HOST + "registerDetection.php");

            // Upprättar anslutning.
            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);

            // Sänder http-request.
            OutputStream outputStream = urlConnection.getOutputStream();
            outputStream.write((request).getBytes());
            outputStream.flush();
            outputStream.close();

            // Tar emot respons.
            if(urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                // Läser in mottagen data.
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    response.append(line);
                }

                try {
                    // Extraherar mottaget json-objekt.
                    JSONObject jsonObject = new JSONObject(response.toString());
                } catch (JSONException e) {
                    System.out.println("ERROR: " + e);
                }
            }
        } catch(IOException e) {
            System.out.println("ERROR: " + e);
        }

        return null;
    }
}
