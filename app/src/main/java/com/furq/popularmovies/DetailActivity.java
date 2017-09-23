package com.furq.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.furq.popularmovies.models.Movie;


/**
 * Created by furqan.khan on 9/21/17.
 */

public class DetailActivity extends AppCompatActivity {

    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_screen);

        // Enable back button on action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // get the intent values which was passed from previous activity
        movie = getIntent().getExtras().getParcelable("movies");

        // set actionbar title to movie name
        getSupportActionBar().setTitle(movie.getTitle());

        TextView movieYear = (TextView) findViewById(R.id.txtYear);
        TextView movieRating = (TextView) findViewById(R.id.txtRating);
        TextView movieDetails = (TextView) findViewById(R.id.txtDetails);

        ImageView imageThumbnail = (ImageView) findViewById(R.id.imageView);

        String voteAvg = String.valueOf(movie.getVoteAverage());

        movieYear.setText(getString(R.string.detail_release_date, movie.getReleaseDate()));
        movieRating.setText(getString(R.string.detail_ratings, voteAvg));
        movieDetails.setText(movie.getOverview());
        // load image with Glide
        Glide.with(this)
                .load(Constant.IMAGE_BASE_URL + movie.getPosterPath()).into(imageThumbnail);
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
}
