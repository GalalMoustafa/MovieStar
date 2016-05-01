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

public class FetchMoviesData extends AsyncTask<Void, Void, String> {
    HttpURLConnection urlConnection = null;
    BufferedReader reader = null;
    String MoviesJsonStr = null;


    public ArrayList<String> getMoviesDataFromJson(String MoviesJsonStr)
            throws JSONException {


        final String OWM_TITLE = "original_title";
        final String OWM_POSTER = "poster_path";
        final String OWM_RELEASE_DATE = "release_date";
        final String OWM_SYNOPSIS = "overview";
        final String OWM_RATING = "vote_average";
        final String OWM_ID = "id";

        JSONObject MoviesJson = new JSONObject(MoviesJsonStr);
        JSONArray MoviesArray = MoviesJson.getJSONArray("results");

        fragmentMain.title.clear();
        fragmentMain.poster.clear();
        fragmentMain.overview.clear();
        fragmentMain.releaseDate.clear();
        fragmentMain.rate.clear();
        fragmentMain.id.clear();

        for (int i = 0; i < MoviesArray.length(); i++) {
            JSONObject Movies = MoviesArray.getJSONObject(i);
            fragmentMain.title.add(Movies.getString(OWM_TITLE));
            fragmentMain.poster.add(Movies.getString(OWM_POSTER));
            fragmentMain.releaseDate.add(Movies.getString(OWM_RELEASE_DATE));
            fragmentMain.overview.add(Movies.getString(OWM_SYNOPSIS));
            fragmentMain.rate.add(Movies.getString(OWM_RATING));
            fragmentMain.id.add(Movies.getString(OWM_ID));
        }
        return fragmentMain.poster;

    }

    @Override
    protected String doInBackground(Void... params) {
        URL url = null;
        try {
            if (MainActivity.input == 0) {
                url = new URL("http://api.themoviedb.org/3/movie/top_rated?api_key="+BuildConfig.THE_MOVIE_DB_API_KEY);
            } else {
                url = new URL("http://api.themoviedb.org/3/movie/popular?api_key="+BuildConfig.THE_MOVIE_DB_API_KEY);
            }

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
            MoviesJsonStr = buffer.toString();
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
        return MoviesJsonStr;

    }

    @Override
    protected void onPostExecute(String MoviesJsonStr) {

        if (MoviesJsonStr != null) {
            try {
                ArrayList<String> strings = getMoviesDataFromJson(MoviesJsonStr);
                fragmentMain.myAdapter.clear();
                for (int i = 0; i < strings.size(); i++)
                    fragmentMain.myAdapter.add(strings.get(i));
            } catch (JSONException e) {
                Log.e("-----", e.toString());
            }
        }


    }
}