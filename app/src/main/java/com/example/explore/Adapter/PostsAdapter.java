package com.example.explore.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.explore.Posts;
import com.example.explore.R;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {
    private List<Posts> mDataset;
    private Context mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        View itemView;
        TextView title, content, date;
        ImageView thumb;
        public ViewHolder(View v) {
            super(v);
            itemView = v;
            title = v.findViewById(R.id.title_post);
            content = v.findViewById(R.id.content_post);
            date = v.findViewById(R.id.date_post);
            thumb = v.findViewById(R.id.ivPost);
                    //ma mai auzi? eu da eu nu te a Nu te aud
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public PostsAdapter(List<Posts> myDataset, Context context) {
        mDataset = myDataset;
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
                .inflate(R.layout.posts_listitem, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.title.setText(mDataset.get(position).getTitle());
        holder.content.setText(mDataset.get(position).getContent());
        Date date = new Date(mDataset.get(position).getDate());
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-YYYY hh:mm", Locale.ENGLISH);

        holder.date.setText(dateFormat.format(date));

        Picasso.with(mContext)
                .load("http://10.0.2.2:8080/explore/files/"+mDataset.get(position).getTitle()+".jpg")
                .placeholder(R.drawable.bg_login)
                .into(holder.thumb);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset == null ? 0 : mDataset.size();
    }
}