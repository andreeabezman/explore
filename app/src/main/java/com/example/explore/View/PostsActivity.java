package com.example.explore.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.explore.APIClient;
import com.example.explore.Adapter.PostsAdapter;
import com.example.explore.ApiInterface;
import com.example.explore.Posts;
import com.example.explore.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PostsActivity extends AppCompatActivity {

    private List<Posts> postsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private PostsAdapter mAdapter;
    private FloatingActionButton btAdd;
    private TextView textv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);

        textv = (TextView)findViewById(R.id.Posts);
        Typeface face = Typeface.createFromAsset(getAssets(),
                "fonts/YARDSALE.TTF");
        textv.setTypeface(face);

        recyclerView = findViewById(R.id.posts_recycleView);
        btAdd = findViewById(R.id.btAdd);

        callPosts();

        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             /*   DialogFragment dialog = FullScreenDialog.newInstance();
                dialog.show(getSupportFragmentManager(), "tag");
                */
                    Intent moveToPosts = new Intent(PostsActivity.this, AddPost.class);
                   startActivity(moveToPosts);
            }
        });
    }

    private void callPosts() {
        APIClient apiClient = new APIClient();
        Retrofit retrofit = apiClient.getApiClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<List<Posts>> call = apiInterface.getAllPosts();
        call.enqueue(new Callback<List<Posts>>() {
            @Override
            public void onResponse(Call<List<Posts>> call, Response<List<Posts>> response) {
                if (response.body() != null && response.isSuccessful()) {
                    postsList = response.body();
                    setupRecyclerView();
                }
            }

            @Override
            public void onFailure(Call<List<Posts>> call, Throwable t) {
                //  Toast.makeText(ViewPlace.this, "Failed to load place", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        mAdapter = new PostsAdapter(postsList, this);
        recyclerView.setAdapter(mAdapter);
    }

}