package project.afinal.anuchar.mobilemarket;

import android.os.AsyncTask;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Dew on 2/18/2018.
 */

public class AsyncHttpTask extends AsyncTask<String, Void, String> {

    protected String DoInBackground(String... urls){
        String result = "";
        URL url;
        HttpURLConnection urlConnection = null;

        try {
            url = new URL (urls[0]);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected String doInBackground(String... strings) {
        return null;
    }
}
