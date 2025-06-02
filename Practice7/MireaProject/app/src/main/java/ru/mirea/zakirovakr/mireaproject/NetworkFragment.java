package ru.mirea.zakirovakr.mireaproject;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import ru.mirea.zakirovakr.mireaproject.databinding.FragmentNetworkBinding;

public class NetworkFragment extends Fragment {

    private static final String TAG = NetworkFragment.class.getSimpleName();
    private FragmentNetworkBinding binding;
    private PostAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNetworkBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Initialize RecyclerView
        adapter = new PostAdapter(new ArrayList<>());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setHasFixedSize(true); // Optimize for fixed-size items
        binding.recyclerView.setVisibility(View.VISIBLE);
        Log.d(TAG, "RecyclerView initialized, visibility: " + binding.recyclerView.getVisibility());

        // Show loading state
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.textView.setText("Loading posts...");
        binding.textView.setVisibility(View.VISIBLE);

        // Fetch data
        fetchPosts();

        return view;
    }

    private void fetchPosts() {
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        JsonPlaceholderApi api = retrofit.create(JsonPlaceholderApi.class);
        Call<List<Post>> call = api.getPosts();
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "API call successful, posts received: " + response.body().size());
                    adapter.setPosts(response.body());
                    binding.textView.setVisibility(View.GONE);
                    binding.recyclerView.post(() -> {
                        binding.recyclerView.invalidate();
                        binding.recyclerView.requestLayout();
                        binding.recyclerView.scrollToPosition(0); // Scroll to top
                        Log.d(TAG, "RecyclerView updated, item count: " + adapter.getItemCount());
                    });
                } else {
                    Log.e(TAG, "API call failed with code: " + response.code());
                    binding.textView.setText("Failed to load posts: HTTP " + response.code());
                    binding.textView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Log.e(TAG, "API call error: " + t.getMessage(), t);
                binding.textView.setText("Error: " + t.getMessage());
                binding.textView.setVisibility(View.VISIBLE);
            }
        });
    }

    public interface JsonPlaceholderApi {
        @GET("posts")
        Call<List<Post>> getPosts();
    }

    public static class Post {
        private int id;
        private String title;
        private String body;

        public int getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getBody() {
            return body;
        }
    }
}