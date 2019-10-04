package com.example.explore.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.explore.Model.PlaceDetail;
import com.example.explore.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {
    private List<PlaceDetail> mDataset;
    private Context mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        View itemView;
        TextView address, placename;
        ImageView thumb;
        RatingBar ratingBar;
        public ViewHolder(View v) {
            super(v);
            itemView = v;
            address = v.findViewById(R.id.address_fav);
            placename = v.findViewById(R.id.place_name_fav);
            thumb = v.findViewById(R.id.ivFav);
            ratingBar = v.findViewById(R.id.ratingBarfav);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public FavoriteAdapter(List<PlaceDetail> myDataset, Context context) {
        mDataset = new ArrayList<>();
        for (PlaceDetail p: myDataset){
            if (p.getResult() != null){
                mDataset.add(p);

            }
        }
        mContext = context;
        notifyDataSetChanged();
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                         int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.favorite_listitem, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.address.setText(mDataset.get(position).getResult().getFormatted_address());
        holder.placename.setText(mDataset.get(position).getResult().getName());
        //holder.ratingBar.setRating(mDataset.get(position).getResult().getRating());
        if (mDataset.get(position).getResult().getRating() != null){
            holder.ratingBar.setRating(Float.valueOf(mDataset.get(position).getResult().getRating()));
            holder.ratingBar.setVisibility(View.VISIBLE);
        }else{
            holder.ratingBar.setVisibility(View.GONE);
        }

        if (mDataset.get(position).getResult().getPhotos() != null && mDataset.get(position).getResult().getPhotos().length > 0){
            Log.wtf("picture","value "+mDataset.get(position).getResult().getName());
            Picasso.with(mContext)
                    .load(getPhotoOfPlace(mDataset.get(position).getResult().getPhotos()[0].getPhoto_reference(),1000))
                    .placeholder(R.drawable.ic_photo_black_24dp)
                    .error(R.drawable.ic_photo_black_24dp)
                    .into(holder.thumb);
        }else{
            Picasso.with(mContext)
                    .load(R.drawable.ic_photo_black_24dp)
                    .placeholder(R.drawable.ic_photo_black_24dp)
                    .error(R.drawable.ic_photo_black_24dp)
                    .into(holder.thumb);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset == null ? 0 : mDataset.size();
    }

    private String getPhotoOfPlace(String photo_reference, int maxWidth) {
        return "https://maps.googleapis.com/maps/api/place/photo?maxwidth="+maxWidth+"&photoreference="+ photo_reference +
                "&key=AIzaSyB0Y8jpKrw__OxhGqNHgsKU3Sb7hXxgLDA";
    }

}
