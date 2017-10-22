package com.furq.popularmovies.Db;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by furqan.khan on 16/10/2017.
 */

public class MovieContract {
    public static final String CONTENT_AUTHORITY = "com.furq.popularmovies";

    public static final String PATH_MOVIE = "movie";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String COLUMN_MOVIE_ID_KEY = "movie_id";

    private MovieContract() {
        throw new AssertionError("No instances");
    }

    /* Inner class that defines the contents of the movies table */
    public static final class MovieEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
                "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE +
                "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        // Table name
        public static final String TABLE_NAME = "movie";

        public static final String MOVIE_ID = "id";
        public static final String MOVIE_TITLE = "title";
        public static final String MOVIE_RELEASE_DATE = "date";
        public static final String MOVIE_CONTENT = "content";
        public static final String MOVIE_RATING = "rating";
        public static final String MOVIE_BACKDROP_PATH = "backdrop";
        public static final String MOVIE_POSTER_PATH = "poster";

        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }
}
