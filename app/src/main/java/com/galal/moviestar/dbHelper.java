package com.galal.moviestar;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class dbHelper extends SQLiteOpenHelper{


    public static int checked = 0;
    private static final int Database_Version = 1;
    private static final String Database_Name = "Movies.db";
    public static final String Table_Name = "movies";
    public static final String column_id = "_id";
    public static final String column_title = "_title";
    public static final String column_poster = "_poster";
    public static final String column_overview = "_overview";
    public static final String column_release = "_release_date";
    public static final String column_rate = "_rate";


    public dbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, Database_Name, factory, Database_Version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE " + Table_Name + " ( " +
                column_id + " TEXT PRIMARY KEY, " +
                column_title + " TEXT, " +
                column_poster + " TEXT, " +
                column_overview + " TEXT, " +
                column_release + " TEXT, " +
                column_rate + " TEXT " +
                " );";

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Table_Name);
        onCreate(db);
    }

    public void addMovie(Movies movies){
        ContentValues values = new ContentValues();
        values.put(column_id , movies.get_id());
        values.put(column_title , movies.get_title());
        values.put(column_poster , movies.get_poster());
        values.put(column_overview , movies.get_overview());
        values.put(column_release , movies.get_release_date());
        values.put(column_rate , movies.get_rate());
        SQLiteDatabase database = getWritableDatabase();
        database.insert(Table_Name , null , values);
        database.close();
    }

    public void deleteMovie(String id){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + Table_Name + " WHERE " + column_id + "=\"" + id + "\";");
    }


    public void isChecked(String id){

        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT " + column_id + " FROM " + Table_Name + " WHERE " + column_id + " = " + id;
        Cursor c = db.rawQuery(query , null);
        c.moveToFirst();
        while ( !c.isAfterLast()){
            if (c.getString(c.getColumnIndex(column_id)) != null){
                checked = 1;
            }
            else {
                checked = 0;
            }
            c.moveToNext();
        }
        db.close();

    }

    public void getData(){
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + Table_Name ;

        Cursor c = db.rawQuery(query ,null);
        c.moveToFirst();
        fragmentMain.title.clear();
        fragmentMain.poster.clear();
        fragmentMain.overview.clear();
        fragmentMain.releaseDate.clear();
        fragmentMain.rate.clear();
        fragmentMain.id.clear();

        while (!c.isAfterLast()){
            if (c.getString(c.getColumnIndex(column_id)) != null){
                fragmentMain.id.add(c.getString(c.getColumnIndex(column_id)));
                fragmentMain.title.add(c.getString(c.getColumnIndex(column_title)));
                fragmentMain.poster.add(c.getString(c.getColumnIndex(column_poster)));
                fragmentMain.overview.add(c.getString(c.getColumnIndex(column_overview)));
                fragmentMain.releaseDate.add(c.getString(c.getColumnIndex(column_release)));
                fragmentMain.rate.add(c.getString(c.getColumnIndex(column_rate)));

                c.moveToNext();
            }
        }
        db.close();
    }

}
