package com.galal.moviestar;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    public static int input = 0;
    public static boolean mTwoPane = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences settings = getSharedPreferences("input", 0);
        input = settings.getInt("value", 0);

        if(findViewById(R.id.Detail_tablet) != null) {
            mTwoPane = true;
            FetchMoviesData weatherTask = new FetchMoviesData();
            weatherTask.execute();
            if(savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.Detail_tablet, new DetailFragment())
                        .commit();
            }
        }

        else {
            mTwoPane = false;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.MAIN, new fragmentMain())
                        .commit();
            }
            else if (savedInstanceState.containsKey("input")){
                input = savedInstanceState.getInt("input");
            }

        }

    }


    @Override
    public void onResume() {
        super.onResume();
        if ( input == 2){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.MAIN, new fragmentMain())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("input",input);
        SharedPreferences settings = getSharedPreferences("input", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("value",input);
        editor.commit();
        super.onSaveInstanceState(outState);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        String[] values = getResources().getStringArray(R.array.sort_options);
        if (id == R.id.action_sort){
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            Window window = this.getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);

            dialog.setSingleChoiceItems(values, input,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                            switch (item) {


                                case 0:
                                    input = 0;
                                    getSupportFragmentManager().beginTransaction()
                                            .replace(R.id.MAIN, new fragmentMain())
                                            .commit();
                                    break;
                                case 1:
                                    input = 1;
                                    getSupportFragmentManager().beginTransaction()
                                            .replace(R.id.MAIN, new fragmentMain())
                                            .commit();
                                    break;
                                case 2:
                                    input = 2;
                                    getSupportFragmentManager().beginTransaction()
                                            .replace(R.id.MAIN, new fragmentMain())
                                            .commit();

                            }
                            dialog.dismiss();
                        }
                    });

            dialog.setTitle("Sort by");
            dialog.show();
            return true;
        }

        return true;
    }

}


