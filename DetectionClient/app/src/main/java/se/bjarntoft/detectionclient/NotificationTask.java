package se.bjarntoft.detectionclient;

import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Andreas on 2016-05-04.
 */
public class NotificationTask extends AsyncTask<Object, Void, Boolean> {
    private MainActivity parentActivity;
    private TeacherListFragment parentFragment;


    @Override
    protected Boolean doInBackground(Object... params) {
        // Extraherar indata.
        parentActivity = (MainActivity)params[0];
        parentFragment = (TeacherListFragment)params[1];
        String request = (String)params[2];

        try {
            URL url = new URL(AppPreferences.HOST + "notification.php");

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
                return true;
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
            Toast.makeText(parentActivity, "Din besöksförfrågan är registrerad.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(parentActivity, "Din besöksförfrågan misslyckades. Försök igen.", Toast.LENGTH_SHORT).show();
        }
    }
}