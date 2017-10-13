package com.furq.popularmovies.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.furq.popularmovies.R;
import com.furq.popularmovies.models.Review;
import com.furq.popularmovies.models.Trailer;

import org.w3c.dom.Text;

import java.text.BreakIterator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by furqan.khan on 9/20/17.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private static final String TAG = ReviewAdapter.class.getSimpleName();

    private List<Review> reviewsList;
    private int itemLayout;

    public ReviewAdapter(List<Review> reviewsList, int itemLayout) {
        this.reviewsList = reviewsList;
        this.itemLayout = itemLayout;

    }

    @Override
    public ReviewAdapter.ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewAdapter.ReviewViewHolder holder, int position) {

        holder.author.setText(reviewsList.get(position).getAuthor().toString());
        holder.details.setText(reviewsList.get(position).getContent().toString());
    }

    @Override
    public int getItemCount() {
        return reviewsList.size();
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.context)
        TextView details;
        @Bind(R.id.author)
        TextView author;

        public ReviewViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);


        }


    }
}
