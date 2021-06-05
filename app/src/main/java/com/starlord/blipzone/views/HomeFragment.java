package com.starlord.blipzone.views;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.starlord.blipzone.R;
import com.starlord.blipzone.adapters.HomeAdapter;
import com.starlord.blipzone.callbacks.ApiResultCallback;
import com.starlord.blipzone.configurations.UrlConstants;
import com.starlord.blipzone.models.BlogModel;
import com.starlord.blipzone.models.CommentModel;
import com.starlord.blipzone.models.LikeModel;
import com.starlord.blipzone.models.UserModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.starlord.blipzone.api.CommonClassForAPI.callAuthGetRequest;


public class HomeFragment extends Fragment {
    ArrayList<BlogModel> blogModelArrayList;
    RecyclerView homeRecyclerView;
    LinearLayoutManager linearLayoutManager;
    HomeAdapter homeAdapter;
    TextView title;
    String TAG = "HomeFragmentLog";

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View homeView = inflater.inflate(R.layout.fragment_home, container, false);
        initializeViews(homeView);
        return homeView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadBlogPosts();
    }

    private void loadBlogPosts() {
        callAuthGetRequest(getActivity(), UrlConstants.BLOG_POST, new ApiResultCallback() {
            @Override
            public void onAPIResultSuccess(JSONObject jsonObject) {
                Log.d(TAG, "onResponse: Success");
                processBlogRequest(jsonObject);
            }

            @Override
            public void onAPIResultError(VolleyError volleyError) {
                Log.d(TAG, "onAPIResultErrorCode: " + volleyError.networkResponse.statusCode);
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void processBlogRequest(JSONObject jsonObject) {
        try {
            boolean status = jsonObject.getBoolean("status");

            if (status) {
                JSONArray data = jsonObject.getJSONArray("data");
                for (int i = 0; i < data.length(); i++) {
                    JSONObject blog = data.getJSONObject(i);
                    BlogModel blogModel = new BlogModel();
                    blogModel.setId(blog.getInt("id"));

                    try {
                        JSONArray commentArray = blog.getJSONArray("comment");
                        List<CommentModel> commentModelList = new ArrayList<>();
                        for (int j = 0; j < commentArray.length(); j++) {
                            JSONObject comment = commentArray.getJSONObject(j);
                            CommentModel commentModel = new CommentModel();
                            commentModel.setId(comment.getInt("id"));

                            JSONObject user = comment.getJSONObject("user");
                            UserModel userModel = new UserModel();
                            userModel.setId(user.getInt("id"));
                            userModel.setUserName(user.getString("username"));
                            userModel.setFirstName(user.getString("first_name"));
                            userModel.setLastName(user.getString("last_name"));
                            userModel.setProfileImage(user.getString("profile_image"));

                            commentModel.setUserModel(userModel);
                            commentModel.setCreatedAt(comment.getString("created_at"));
                            commentModel.setLastUpdatedAt(comment.getString("last_updated_on"));
                            commentModel.setContent(comment.getString("content"));
                            commentModel.setPostId(comment.getInt("post"));
                            commentModelList.add(commentModel);
                        }
                        blogModel.setCommentList(commentModelList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        JSONArray likesArray = blog.getJSONArray("like");
                        LikeModel likeModel = new LikeModel();

                            JSONObject likesUser = likesArray.getJSONObject(0);
                            JSONObject user = likesUser.getJSONObject("user");
                            UserModel userModel = new UserModel();
                            userModel.setId(user.getInt("id"));
                            userModel.setUserName(user.getString("username"));
                            userModel.setFirstName(user.getString("first_name"));
                            userModel.setLastName(user.getString("last_name"));
                            userModel.setProfileImage(user.getString("profile_image"));
                            likeModel.setUserModel(userModel);

                            //getting the like count
                            JSONObject likesCount = likesArray.getJSONObject(1);
                            try {
                                int count = likesCount.getInt("count");
                                likeModel.setLikeCount(count);
                            }catch (Exception e){

                            }
                            blogModel.setLikeModel(likeModel);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    blogModel.setLiked(blog.getBoolean("is_liked"));
                    blogModel.setOwner(blog.getBoolean("owner"));

                    JSONObject user = blog.getJSONObject("user");
                    UserModel blogUserModel = new UserModel();
                    blogUserModel.setId(user.getInt("id"));
                    blogUserModel.setUserName(user.getString("username"));
                    blogUserModel.setFirstName(user.getString("first_name"));
                    blogUserModel.setLastName(user.getString("last_name"));
                    blogUserModel.setProfileImage(user.getString("profile_image"));

                    blogModel.setUserModel(blogUserModel);
                    blogModel.setCreatedAt(blog.getString("created_at"));
                    blogModel.setLastUpdatedOn(blog.getString("last_updated_at"));
                    blogModel.setContent(blog.getString("content"));
                    blogModel.setImageUrl(blog.getString("image"));
                    blogModel.setViewType(blog.getInt("view_type"));

                    blogModelArrayList.add(blogModel);
                    homeAdapter.notifyDataSetChanged();
                }
            } else {
                Toast.makeText(getActivity(),
                        "Something went wrong",
                        Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initializeViews(View homeView) {
        blogModelArrayList = new ArrayList<>();
        homeRecyclerView = homeView.findViewById(R.id.home_feed_rv);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        homeAdapter = new HomeAdapter(getActivity(), blogModelArrayList);
        homeRecyclerView.setLayoutManager(linearLayoutManager);
        homeRecyclerView.setAdapter(homeAdapter);
        title = homeView.findViewById(R.id.username_feed);
        title.setText("Feed");
    }

}

