package com.furq.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.furq.popularmovies.Db.MovieContract;
import com.furq.popularmovies.Fragments.MovieDetailsFragment;
import com.furq.popularmovies.models.Movie;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by furqan.khan on 9/21/17.
 */

public class DetailActivity extends AppCompatActivity {

    @Bind(R.id.detail_toolbar)
    Toolbar toolbar;
    @Bind(R.id.fab)
    FloatingActionButton favourite;
    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @Bind(R.id.app_bar)
    AppBarLayout appBarLayout;
    @Bind(R.id.scroll_view)
    NestedScrollView scrollView;

    private Movie movie;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_screen);
        ButterKnife.bind(this);

        // get the intent values which was passed from previous activity
        movie = getIntent().getExtras().getParcelable("movies");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (isFavourite()) {
            favourite.setImageResource(R.drawable.heart);
        }
        final ImageView image = (ImageView) findViewById(R.id.backdrop_image);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }
            }
        });


        // load image with Glide
        Glide.with(this)
                .load(Constant.BACKDROP_IMAGE_BASE_URL + movie.getBackdropPath()).into(image);

        Bundle arguments = new Bundle();
        arguments.putParcelable("movie", movie);
        MovieDetailsFragment fragment = new MovieDetailsFragment();
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.movie_detail_container, fragment)
                .commit();

        favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isFavourite()) {
                    Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    addMovie(byteArray);
                    favourite.setImageResource(R.drawable.heart);
                } else {
                    removeMovie();
                    favourite.setImageResource(R.drawable.heart_outline);
                }
            }
        });
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        final int[] position = savedInstanceState.getIntArray("SCROLL_POSITION");
        if (position != null)
            scrollView.post(new Runnable() {
                public void run() {
                    scrollView.scrollTo(position[0], position[1]);
                }
            });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void removeMovie() {
        String currentMovieId = String.valueOf(movie.getId());
        String whereClause = MovieContract.MovieEntry.MOVIE_ID + " = ?";
        String[] whereArgs = new String[]{currentMovieId};
        int rowsDeleted = getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI, whereClause, whereArgs);
        File photofile = new File(getFilesDir(), currentMovieId);
        if (photofile.exists()) {
            photofile.delete();
        }
    }


    /**
     * Add Movie to favorite db and write image to file
     */
    private void addMovie(byte[] byteArray) {
        /* Add Movie to ContentProvider */
        ContentValues values = new ContentValues();
        values.put(MovieContract.MovieEntry.MOVIE_ID, movie.getId());
        values.put(MovieContract.MovieEntry.MOVIE_TITLE, movie.getTitle());
        values.put(MovieContract.MovieEntry.MOVIE_CONTENT, movie.getOverview());
        values.put(MovieContract.MovieEntry.MOVIE_RELEASE_DATE, movie.getReleaseDate());
        values.put(MovieContract.MovieEntry.MOVIE_RATING, movie.getVoteAverage());
        values.put(MovieContract.MovieEntry.MOVIE_POSTER_PATH, movie.getPosterPath());
        values.put(MovieContract.MovieEntry.MOVIE_BACKDROP_PATH, movie.getBackdropPath());
        Uri insertedMovieUri = getContentResolver().
                insert(MovieContract.MovieEntry.CONTENT_URI, values);

        /* Write the file to disk */
        String filename = String.valueOf(movie.getId());
        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(byteArray);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    boolean isFavourite() {
        Boolean flag = false;
        Cursor cursor = getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                new String[]{MovieContract.MovieEntry.MOVIE_ID},
                MovieContract.MovieEntry.MOVIE_ID + " = ? ",
                new String[]{String.valueOf(movie.getId())},
                null);
        if (cursor != null) {
            int cursorCount = cursor.getCount();
            flag = cursorCount > 0 ? true : false;
            cursor.close();
        }
        return flag;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // on pressing back button back to previous screen
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
