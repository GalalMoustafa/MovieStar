package com.galal.moviestar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class review extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        int x = DetailFragment.c + 1;
        setTitle("Review "+ x );
    }

    protected void onStart()
    {
        super.onStart();
        TextView t = (TextView)findViewById(R.id.rev);

        t.setText(reviews.Reviews.get(DetailFragment.c));
    }
}
