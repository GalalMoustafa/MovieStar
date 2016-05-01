package com.galal.moviestar;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public class Detail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setTitle("Details");
        FragmentManager manager = this.getSupportFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
        trans.replace(R.id.detailactivity, new DetailFragment() , "details")
                .commit();

    }


    @Override
    public void onBackPressed() {
        FragmentManager manager = this.getSupportFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
        trans.remove(getSupportFragmentManager().findFragmentByTag("details"));
        trans.commit();
        manager.popBackStack();
        finish();

    }
}
