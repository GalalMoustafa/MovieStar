package com.galal.moviestar;


public class Movies {

    private String _id;
    private String _title;
    private String _poster;
    private String _overview;
    private String _release_date;
    private String _rate;

    public Movies(){

    }

    public Movies(String id , String title , String poster , String overview , String release_date , String rate){
        this._id = id;
        this._title = title;
        this._poster = poster;
        this._overview = overview;
        this._release_date = release_date;
        this._rate = rate;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void set_title(String _title) {
        this._title = _title;
    }

    public void set_poster(String _poster) {
        this._poster = _poster;
    }

    public void set_overview(String _overview) {
        this._overview = _overview;
    }

    public void set_release_date(String _release_date) {
        this._release_date = _release_date;
    }

    public void set_rate(String _rate) {
        this._rate = _rate;
    }


    public String get_id() {
        return _id;
    }

    public String get_title() {
        return _title;
    }

    public String get_poster() {
        return _poster;
    }

    public String get_overview() {
        return _overview;
    }

    public String get_release_date() {
        return _release_date;
    }

    public String get_rate() {
        return _rate;
    }
}
