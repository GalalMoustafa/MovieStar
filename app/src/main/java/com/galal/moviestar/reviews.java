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


public class reviews extends AsyncTask<Void, Void, String> {

    static String id;
    String ReviewsJsonStr;
    HttpURLConnection urlConnection = null;
    BufferedReader reader = null;
    static ArrayList<String> Reviews = new ArrayList<>();

    public ArrayList<String> getReviewsFromJson(String ReviewsJsonStr)
            throws JSONException {


        final String OWM_Content = "content";
        Reviews.clear();
        JSONObject ReviewsJson = new JSONObject(ReviewsJsonStr);
        JSONArray reviewsArray = ReviewsJson.getJSONArray("results");

        for (int i = 0; i < reviewsArray.length(); i++) {
            JSONObject review = reviewsArray.getJSONObject(i);
            Reviews.add(review.getString(OWM_Content));
        }

        return null;

    }


    @Override
    protected String doInBackground(Void... params) {
        URL url = null;
        try {

            url = new URL("http://api.themoviedb.org/3/movie/"+reviews.id+"/reviews?api_key="+BuildConfig.THE_MOVIE_DB_API_KEY);
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
            ReviewsJsonStr = buffer.toString();
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

        return ReviewsJsonStr;
    }

    @Override
    protected void onPostExecute(String ReviewsJsonStr) {


        if (ReviewsJsonStr != null) {
            try {
                getReviewsFromJson(ReviewsJsonStr);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
