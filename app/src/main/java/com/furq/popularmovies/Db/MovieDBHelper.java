package com.furq.popularmovies.Db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.furq.popularmovies.Db.MovieContract.MovieEntry;


/**
 * Created by furqan.khan on 16/10/2017.
 */

public class MovieDBHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 2;

    static final String DATABASE_NAME = "movies.db";
    private static final String TAG = "MovieDBHelper ";


    public MovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.i(TAG, "Helper constructed");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_LOCATION_TABLE = "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
                MovieEntry.MOVIE_ID + " INTEGER PRIMARY KEY," +
                MovieEntry.MOVIE_TITLE + " TEXT NOT NULL, " +
//                Favorites.COLUMN_POSTER + " BLOB NOT NULL, " +
                MovieEntry.MOVIE_RELEASE_DATE + " TEXT  NULL, " +
                MovieEntry.MOVIE_CONTENT + " TEXT  NULL, " +
                MovieEntry.MOVIE_RATING + " TEXT  NULL, " +
                MovieEntry.MOVIE_BACKDROP_PATH + " TEXT NULL," +
                MovieEntry.MOVIE_POSTER_PATH + " TEXT  NULL" +
                " );" ;
        Log.i(TAG, SQL_CREATE_LOCATION_TABLE);
        db.execSQL(SQL_CREATE_LOCATION_TABLE);
        Log.i(TAG, "Table has been created");
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

}