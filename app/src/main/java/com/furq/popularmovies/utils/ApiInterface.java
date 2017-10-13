package com.furq.popularmovies.utils;

import com.furq.popularmovies.models.MovieResponse;
import com.furq.popularmovies.models.Review;
import com.furq.popularmovies.models.ReviewResponse;
import com.furq.popularmovies.models.TrailerResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import static android.R.attr.id;

/**
 * Created by furqan.khan on 9/20/17.
 */

public interface ApiInterface {

    @GET("movie/top_rated")
    Call<MovieResponse> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("movie/popular")
    Call<MovieResponse> getPopularMovies(@Query("api_key") String apiKey);


    @GET("movie/{id}/videos")
    Call<TrailerResponse> loadTrailers(@Path("id")  String id, @Query("api_key") String api_key);

    @GET("movie/{id}/reviews")
    Call<ReviewResponse> loadReviews(@Path("id") String id, @Query("api_key") String api_key);

}
