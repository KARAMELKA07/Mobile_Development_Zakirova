package ru.mirea.zakirovakr.mireaproject;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private static final String TAG = PostAdapter.class.getSimpleName();
    private List<NetworkFragment.Post> posts;

    public PostAdapter(List<NetworkFragment.Post> posts) {
        this.posts = posts;
        Log.d(TAG, "PostAdapter initialized with posts: " + posts.size());
    }

    public void setPosts(List<NetworkFragment.Post> posts) {
        this.posts.clear();
        this.posts.addAll(posts);
        Log.d(TAG, "Setting posts, count: " + posts.size());
        for (int i = 0; i < Math.min(posts.size(), 5); i++) {
            Log.d(TAG, "Post " + i + ": Title=" + posts.get(i).getTitle() + ", Body=" + posts.get(i).getBody());
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);
        Log.d(TAG, "Creating ViewHolder for viewType: " + viewType);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        NetworkFragment.Post post = posts.get(position);
        holder.title.setText(post.getTitle() != null ? post.getTitle() : "No Title");
        holder.body.setText(post.getBody() != null ? post.getBody() : "No Body");
        Log.d(TAG, "Binding post at position " + position + ": Title=" + post.getTitle());
    }

    @Override
    public int getItemCount() {
        int count = posts.size();
        Log.d(TAG, "getItemCount: " + count);
        return count;
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView body;

        PostViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.postTitle);
            body = itemView.findViewById(R.id.postBody);
            Log.d(TAG, "PostViewHolder created, title view: " + (title != null) + ", body view: " + (body != null));
        }
    }
}