package com.galal.moviestar;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DetailFragment extends Fragment {

    static TrailerAdapter myListAdapter ;
    static ReviewsAdapter myListAdapter2;
    static CustomListView listView;
    static CustomListView listView2;
    static int c;
    int checked;
    View rootview;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootview = inflater.inflate(R.layout.detailfragment, container , false);
        if (fragmentMain.clickedMovie == -1){
            View view = rootview.findViewById(R.id.view);
            view.setVisibility(view.INVISIBLE);
            View view2 = rootview.findViewById(R.id.view2);
            view2.setVisibility(view2.INVISIBLE);
            TextView trailers = (TextView) rootview.findViewById(R.id.trailer);
            trailers.setVisibility(trailers.INVISIBLE);
            TextView reviews = (TextView) rootview.findViewById(R.id.review);
            reviews.setVisibility(reviews.INVISIBLE);
        }
        if (fragmentMain.clickedMovie != -1) {
            setData();
        }
        return rootview;
    }





    public void setData(){
        final dbHelper db = new dbHelper(getContext() , null , null , 1);
        TextView moviedetail = (TextView) rootview.findViewById(R.id.movie);
        moviedetail.setVisibility(moviedetail.GONE);
        TextView title = (TextView) rootview.findViewById(R.id.MovieTitle);
        title.setText(fragmentMain.title.get(fragmentMain.clickedMovie));
        Picasso.with(getContext())
                .load("http://image.tmdb.org/t/p/" + "w185/" + fragmentMain.poster.get(fragmentMain.clickedMovie).substring(1))
                .into((ImageView) rootview.findViewById(R.id.posterView));
        TextView year = (TextView) rootview.findViewById(R.id.Year);
        year.setText(fragmentMain.releaseDate.get(fragmentMain.clickedMovie).substring(0, 4));

        TextView rate = (TextView) rootview.findViewById(R.id.Rate);
        rate.setText(fragmentMain.rate.get(fragmentMain.clickedMovie)+ " / 10");

        TextView overview = (TextView) rootview.findViewById(R.id.overview);
        overview.setText(fragmentMain.overview.get(fragmentMain.clickedMovie));

        ImageView fav = (ImageView) rootview.findViewById(R.id.fav);


        if (dbHelper.checked == 0){
            checked = 0;
            fav.setImageResource(R.drawable.unchecked);
        }
        else {
            checked = 1;
            fav.setImageResource(R.drawable.checked);
        }


        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView fav = (ImageView) rootview.findViewById(R.id.fav);

                if (checked == 0){
                    checked = 1;
                    fav.setImageResource(R.drawable.checked);
                    Movies m = new Movies(fragmentMain.id.get(fragmentMain.clickedMovie) , fragmentMain.title.get(fragmentMain.clickedMovie)
                            , fragmentMain.poster.get(fragmentMain.clickedMovie)
                            , fragmentMain.overview.get(fragmentMain.clickedMovie)
                            , fragmentMain.releaseDate.get(fragmentMain.clickedMovie)
                            , fragmentMain.rate.get(fragmentMain.clickedMovie));
                    db.addMovie(m);
                    Toast.makeText(getActivity(), "Added to favourites",
                            Toast.LENGTH_SHORT).show();

                }
                else {
                    checked = 0;
                    fav.setImageResource(R.drawable.unchecked);
                    db.deleteMovie(fragmentMain.id.get(fragmentMain.clickedMovie));
                    Toast.makeText(getActivity(), "Deleted from favourites",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        listView = (CustomListView) rootview.findViewById(R.id.listView);
        myListAdapter = new TrailerAdapter(getContext() , trailers.Trailers);
        listView.setAdapter(myListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + trailers.Trailers.get(position)));
                    startActivity(intent);
                } catch (ActivityNotFoundException ex) {
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://www.youtube.com/watch?v=" + trailers.Trailers.get(position)));
                    startActivity(intent);
                }
            }
        });



        listView2 = (CustomListView) rootview.findViewById(R.id.reviews);
        myListAdapter2 = new ReviewsAdapter(getContext() , reviews.Reviews);
        listView2.setAdapter(myListAdapter2);
        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                c = position;
                Intent intent = new Intent(getContext(), review.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onCreateOptionsMenu(Menu menu , MenuInflater inflater) {

        inflater.inflate(R.menu.detailfragment, menu);

        MenuItem menuItem = menu.findItem(R.id.action_share);

        ShareActionProvider mShareActionProvider =
                (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        if (mShareActionProvider != null ) {
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        } else {
            Log.d("------", "Share Action Provider is null");
        }

    }

    private Intent createShareForecastIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        if (fragmentMain.clickedMovie != -1){
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Movie Title :  " + fragmentMain.title.get(fragmentMain.clickedMovie)
                    + "\n" + "Rate :  " + fragmentMain.rate.get(fragmentMain.clickedMovie)
                    + "\n" + "Release Date :  " + fragmentMain.releaseDate.get(fragmentMain.clickedMovie)
                    + "\n" + "Poster Link :  http://image.tmdb.org/t/p/original/" + fragmentMain.poster.get(fragmentMain.clickedMovie).substring(1)
                    + "\n" + "Trailer :  http://www.youtube.com/watch?v=" + trailers.Trailers.get(0));
        }
        return shareIntent;
    }





    class TrailerAdapter extends ArrayAdapter<String> {

        public TrailerAdapter(Context context, ArrayList<String> trailers) {
            super(context, R.layout.trailer_row , trailers);
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(getContext());
            View customView = inflater.inflate(R.layout.trailer_row , parent ,false);
            TextView trailer = (TextView) customView.findViewById(R.id.trailertext);
            ImageView play = (ImageView) customView.findViewById(R.id.trailerimage);

            trailer.setText("Trailer  " + String.valueOf(position + 1));
            trailer.setTextColor(Color.parseColor("#FFFFFF"));
            play.setImageResource(R.drawable.play);
            return customView;
        }


    }




    class ReviewsAdapter extends ArrayAdapter<String> {

        public ReviewsAdapter(Context context , ArrayList<String> Reviews) {
            super(context, R.layout.review_row ,Reviews);
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(getContext());
            View customView = inflater.inflate(R.layout.review_row , parent ,false);
            TextView trailer = (TextView) customView.findViewById(R.id.reviewText);
            ImageView rev = (ImageView) customView.findViewById(R.id.reviewImage);

            trailer.setText("Review  " + String.valueOf(position + 1));
            trailer.setTextColor(Color.parseColor("#FFFFFF"));
            rev.setImageResource(R.drawable.rev);
            return customView;
        }


    }

}
