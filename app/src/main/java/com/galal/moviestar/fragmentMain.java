package com.galal.moviestar;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class fragmentMain extends Fragment {


    static ImageAdapter myAdapter;
    static int clickedMovie = -1;
    static ArrayList<String> title = new ArrayList<>();
    static ArrayList<String> releaseDate = new ArrayList<>();
    static ArrayList<String> overview = new ArrayList<>();
    static ArrayList<String> rate = new ArrayList<>();
    static ArrayList<String> poster = new ArrayList<>();
    static ArrayList<String> id = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Context context = getActivity();


        if (MainActivity.input == 2){
            dbHelper db = new dbHelper(getContext() , null , null , 1);
            myAdapter = new ImageAdapter(context);
            db.getData();
            try {
                fragmentMain.myAdapter.clear();
                for (int i = 0; i < fragmentMain.poster.size(); i++)
                    fragmentMain.myAdapter.add(fragmentMain.poster.get(i));

            }
            catch (Exception e){
                Log.e("-----", e.toString());
            }
        }

        else {
            FetchMoviesData weatherTask = new FetchMoviesData();
            weatherTask.execute();
            myAdapter = new ImageAdapter(context);
        }

        View rootview = inflater.inflate(R.layout.grid_view_fragment, container, false);
        GridView gridview = (GridView) rootview.findViewById(R.id.grid_view);
        gridview.setAdapter(myAdapter);


        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                clickedMovie = position;
                trailers.id = fragmentMain.id.get(position);
                reviews.id = fragmentMain.id.get(position);
                trailers t = new trailers();

                dbHelper.checked = 0;
                final dbHelper db = new dbHelper(getContext() , null , null , 1);
                db.isChecked(fragmentMain.id.get(fragmentMain.clickedMovie));

                try {
                    t.execute().get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                reviews r = new reviews();
                try {
                    r.execute().get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                if (MainActivity.mTwoPane == false){
                    Intent intent = new Intent(getActivity(), Detail.class);
                    startActivity(intent);
                }
                else {
                    getFragmentManager().beginTransaction()
                            .replace(R.id.Detail_tablet, new DetailFragment())
                            .commit();
                }

            }
        });



        return rootview;
    }


    class ImageAdapter extends ArrayAdapter<String> {

        Context context;

        public ImageAdapter(Context context) {
            super(context, 0);
            this.context = context;
        }



        public View getView(int position, View convertView, ViewGroup parent) {

            ImageView myImage ;
            if (convertView == null){
                myImage = new ImageView(context);
                myImage.setLayoutParams(new GridView.LayoutParams(360, ViewGroup.LayoutParams.MATCH_PARENT));

            }

            else{
                myImage = (ImageView) convertView;
            }

            Picasso.with(context)
                    .load("http://image.tmdb.org/t/p/" + "w342/" + getItem(position).substring(1))
                    .placeholder(R.drawable.preload)
                    .error(R.drawable.preload_error)
                    .into(myImage);
            return myImage;
        }


    }


}


