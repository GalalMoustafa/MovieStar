package com.galal.moviestar;


import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

public class trailers extends AsyncTask<Void, Void, String> {

    static String id;
    String TrailersJsonStr;
    HttpURLConnection urlConnection = null;
    BufferedReader reader = null;
    static ArrayList<String> Trailers = new ArrayList<>();

    public ArrayList<String> getTrailersFromJson(String TrailersJsonStr)
            throws JSONException {


        final String OWM_KEY = "key";
        Trailers.clear();
        JSONObject TrailersJson = new JSONObject(TrailersJsonStr);
        JSONArray TrailersArray = TrailersJson.getJSONArray("results");

        for (int i = 0; i < TrailersArray.length(); i++) {
            JSONObject Trailer = TrailersArray.getJSONObject(i);
            Trailers.add(Trailer.getString(OWM_KEY));
        }
        return null;

    }


    @Override
    protected String doInBackground(Void... params) {
        URL url = null;
        try {
            url = new URL("http://api.themoviedb.org/3/movie/"+id+"/videos?api_key="+BuildConfig.THE_MOVIE_DB_API_KEY);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }
            TrailersJsonStr = buffer.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("IOexception", "Error closing stream", e);
                }
            }
        }
        return TrailersJsonStr;
    }

    @Override
    protected void onPostExecute(String TrailersJsonStr) {
        try {
            getTrailersFromJson(TrailersJsonStr);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
