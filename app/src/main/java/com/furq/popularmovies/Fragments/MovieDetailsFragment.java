package com.furq.popularmovies.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.furq.popularmovies.Adapters.ReviewAdapter;
import com.furq.popularmovies.Adapters.TrailerAdapter;
import com.furq.popularmovies.Constant;
import com.furq.popularmovies.R;
import com.furq.popularmovies.models.Movie;
import com.furq.popularmovies.models.Review;
import com.furq.popularmovies.models.ReviewResponse;
import com.furq.popularmovies.models.Trailer;
import com.furq.popularmovies.models.TrailerResponse;
import com.furq.popularmovies.utils.ApiClient;
import com.furq.popularmovies.utils.ApiInterface;
import com.furq.popularmovies.utils.Network;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

/**
 * Created by furqan.khan on 06/10/2017.
 */

public class MovieDetailsFragment extends Fragment {

    @Bind(R.id.imageView)
    ImageView imageView;
    @Bind(R.id.txtTitle)
    TextView txtTitle;
    @Bind(R.id.txtRating)
    TextView txtRating;
    @Bind(R.id.ratingBar)
    RatingBar ratingBar;
    @Bind(R.id.txtOverview)
    TextView txtOverview;
    @Bind(R.id.txtRelease)
    TextView txtRelease;
    @Bind(R.id.reviewsRecyclerView)
    RecyclerView reviewsRecyclerView;
    @Bind(R.id.reviewLabel)
    TextView reviewLabel;
    @Bind(R.id.trailerLabel)
    TextView trailerLabel;
    @Bind(R.id.extras)
    LinearLayout extraLayout;
    @Bind(R.id.trailersRecyclerView)
    RecyclerView trailerHorizontalList;
    @Bind(R.id.scrollView)
    ScrollView scrollView;

    Movie movie;
    List<Trailer> trailerList;
    List<Review> reviewList;

    public MovieDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (getArguments().containsKey("movie")) {
            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.collapsing_toolbar);
            if (appBarLayout != null) {
                appBarLayout.setTitle("");
            }
            movie = getArguments().getParcelable("movie");
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String voteAvg = String.valueOf(movie.getVoteAverage());

        txtTitle.setText(movie.getTitle());
        txtRating.setText(voteAvg);
        txtOverview.setText(movie.getOverview());
        txtRelease.setText("Release date: " + movie.getReleaseDate());
        ratingBar.setMax(5);
        ratingBar.setRating(movie.getVoteAverage().floatValue() / 2f);
        // load image with Glide
        Glide.with(this)
                .load(Constant.BACKDROP_IMAGE_BASE_URL + movie.getPosterPath()).into(imageView);

        getTrailers();
        getReview();

        if(savedInstanceState !=null) {

            final int[] position = savedInstanceState.getIntArray("ARTICLE_SCROLL_POSITION");
            if(position != null)
                scrollView.post(new Runnable() {
                    public void run() {
                        scrollView.scrollTo(position[0], position[1]);
                    }
                });
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putIntArray("ARTICLE_SCROLL_POSITION",
                new int[]{ scrollView.getScrollX(), scrollView.getScrollY()});
    }

    private void getTrailers() {
        if (!Network.isConnected(getActivity())) {
            Toast.makeText(getContext(), "Please check your internet connectivity", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiInterface apiService =
                ApiClient.getClient(Constant.BASE_URL).create(ApiInterface.class);
        Call<TrailerResponse> call;
        call = apiService.loadTrailers(movie.getId(), Constant.API_KEY);
        call.enqueue(new Callback<TrailerResponse>() {
            @Override
            public void onResponse(Call<TrailerResponse> call, Response<TrailerResponse> response) {
                int statusCode = response.code();

                if (statusCode == 200) {
                    trailerList = response.body().getResults();

                    if (!trailerList.isEmpty()) {
                        TrailerAdapter recyclerAdapter = new TrailerAdapter(trailerList, R.layout.list_trailer_item, getActivity());
                        trailerHorizontalList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                        trailerHorizontalList.setAdapter(recyclerAdapter);
                    } else {
                        trailerLabel.setVisibility(View.GONE);
                    }

                } else {
                    Toast.makeText(getActivity(), "Something went wrong, Please try again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TrailerResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });
    }

    private void getReview() {
        if (!Network.isConnected(getActivity())) {
            Toast.makeText(getContext(), "Please check your internet connectivity", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiInterface apiService =
                ApiClient.getClient(Constant.BASE_URL).create(ApiInterface.class);
        Call<ReviewResponse> call;
        call = apiService.loadReviews(movie.getId(), Constant.API_KEY);
        call.enqueue(new Callback<ReviewResponse>() {

            @Override
            public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {

                int statusCode = response.code();

                if (statusCode == 200) {

                    reviewList = response.body().getResults();
                    if (!reviewList.isEmpty()) {
                        ReviewAdapter recyclerAdapter = new ReviewAdapter(reviewList, R.layout.list_review_item);
                        reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                        reviewsRecyclerView.setAdapter(recyclerAdapter);
                    } else {
                        reviewLabel.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(getActivity(), "Something went wrong, Please try again", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ReviewResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });
    }

}
