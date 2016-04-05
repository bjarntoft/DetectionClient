package se.bjarntoft.detectionclient;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by Andreas Bjärntoft on 2016-03-29.
 */
public class GetTeachersTask extends AsyncTask<Object, Void, Boolean> {
    private MainActivity parentActivity;
    private TeacherListFragment parentFragment;


    @Override
    protected Boolean doInBackground(Object... params) {
        // Extraherar indata.
        parentActivity = (MainActivity)params[0];
        parentFragment = (TeacherListFragment)params[1];
        String request = (String)params[2];

        try {
            URL url = new URL(AppPreferences.HOST + "getTeachers.php");

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
                    JSONArray jsonArray = jsonObject.getJSONArray("teachers");

                    // Kontrollerar innehållet i mottaget json-objekt.
                    if(jsonArray.length() > 0) {
                        // Loopar genom json-objekt.
                        for(int i=0; i < jsonArray.length(); i++) {
                            // Extraherar data.
                            String name = jsonArray.getJSONObject(i).getString("name");
                            String status = jsonArray.getJSONObject(i).getString("status");

                            // Adderar rad i lista.
                            parentFragment.addListItem(name, status);
                        }

                        return true;
                    }
                } catch (JSONException e) {
                    System.out.println("JSONException: " + e);
                }
            }
        } catch(MalformedURLException e) {
            System.out.println("MalformedURLException: " + e);
        } catch(IOException e) {
            System.out.println("IOException: " + e);
        }

        return false;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        // Uppdaterar gui.
        if(aBoolean) {
            parentFragment.updateList();
        } else {
            System.out.println("Lyckades inte");
        }
    }
}
