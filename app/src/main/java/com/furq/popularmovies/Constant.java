package com.furq.popularmovies;

/**
 * Created by furqan.khan on 9/20/17.
 */

public final class Constant {

    /**
     The caller references the constants using Consts.EMPTY_STRING,
     and so on. Thus, the caller should be prevented from constructing objects of
     this class, by declaring this private constructor.
     */
    private Constant(){
        //this prevents even the native class from
        //calling this constructor as well :
        throw new AssertionError();
    }

    public static final String API_KEY = "70170f500d3279bb6f57e59445a614ca";
    public static final String BASE_URL = "http://api.themoviedb.org/3/";
    public static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w185/";

}
