package com.furq.popularmovies;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.furq.popularmovies.Fragments.MovieDetailsFragment;
import com.furq.popularmovies.models.Movie;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;


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

    private Movie movie;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_screen);
        ButterKnife.bind(this);

        // Realm db reference
        realm = Realm.getDefaultInstance();

        // get the intent values which was passed from previous activity
        movie = getIntent().getExtras().getParcelable("movies");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(isFavourite()) {
            favourite.setImageResource(R.drawable.heart);
        }
        ImageView image = (ImageView) findViewById(R.id.backdrop_image);

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

                if (realm.isInTransaction())
                    realm.cancelTransaction();

                if (!isFavourite()) {
                    realm.beginTransaction();
                    realm.copyToRealm(movie);
                    realm.commitTransaction();
                    favourite.setImageResource(R.drawable.heart);
                } else {
                    realm.beginTransaction();
                    realm.where(Movie.class).contains("id", movie.getId()).findFirst().deleteFromRealm();
                    realm.commitTransaction();
                    favourite.setImageResource(R.drawable.heart_outline);
                }
            }
        });
    }

    boolean isFavourite() {
        return realm.where(Movie.class).contains("id", movie.getId()).findAll().size() != 0;
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
        realm.close();
    }
}
