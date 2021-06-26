package com.starlord.blipzone.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.starlord.blipzone.R;
import com.starlord.blipzone.adapters.HomeAdapter;
import com.starlord.blipzone.callbacks.ApiResultCallback;
import com.starlord.blipzone.configurations.GlobalVariables;
import com.starlord.blipzone.configurations.UrlConstants;
import com.starlord.blipzone.models.BlogModel;
import com.starlord.blipzone.models.CommentModel;
import com.starlord.blipzone.models.LikeModel;
import com.starlord.blipzone.models.UserModel;
import com.starlord.blipzone.views.activities.UserChatListActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

import static com.starlord.blipzone.api.CommonClassForAPI.callAuthGetRequest;
import static com.starlord.blipzone.api.CommonClassForAPI.callAuthPostRequest;
import static com.starlord.blipzone.configurations.UrlConstants.LIKE_ACTION_WS;
import static com.starlord.blipzone.configurations.UrlConstants.NOTIFICATION_WS;
import static com.starlord.blipzone.configurations.UrlConstants.POST_ID_WS;
import static com.starlord.blipzone.configurations.UrlConstants.TYPE_WS;
import static com.starlord.blipzone.configurations.UrlConstants.UNLIKE;


public class HomeFragment extends Fragment {
    ArrayList<BlogModel> blogModelArrayList;
    RecyclerView homeRecyclerView;
    LinearLayoutManager linearLayoutManager;
    HomeAdapter homeAdapter;
    TextView title;
    private ProgressBar loadingPB;
    private NestedScrollView nestedSV;
    String TAG = "HomeFragmentLog";
    private WebSocket webSocket;
    ImageView chatListBtn;
    boolean firstCall = true;
    private int page = 1;

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
        loadBlogPostsRequest();

        chatListBtn.setOnClickListener(v->{
            startActivity(new Intent(getActivity(), UserChatListActivity.class));
        });

        // Pagination for loading more data from server
        nestedSV.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (!firstCall) {
                    if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                        // in this method we are incrementing page number,
                        // making progress bar visible and calling get data method.
                        page++;
                        loadingPB.setVisibility(View.VISIBLE);
                        loadMoreBlogPostsRequest(page);
                    }
                }
            }
        });
    }

    private void loadBlogPostsRequest() {
        callAuthGetRequest(getActivity(), UrlConstants.BLOG_LIST, new ApiResultCallback() {
            @Override
            public void onAPIResultSuccess(JSONObject jsonObject) {
                Log.d(TAG, "loadBlogPostsRequest: Success-> " + jsonObject);
                processBlogRequest(jsonObject);
            }

            @Override
            public void onAPIResultError(VolleyError volleyError) {
                Log.d(TAG, "loadBlogPostsRequest: Error-> " + volleyError.networkResponse.statusCode);
                Toast.makeText(getActivity(), "Please check your internet connectivity...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadMoreBlogPostsRequest(int page) {
        // For loading more blogs from server
        callAuthGetRequest(getActivity(), UrlConstants.MORE_BLOG_LIST + page, new ApiResultCallback() {
            @Override
            public void onAPIResultSuccess(JSONObject jsonObject) {
                Log.d(TAG, "loadMoreBlogPostsRequest: Success-> " + jsonObject);
                processBlogRequest(jsonObject);
            }

            @Override
            public void onAPIResultError(VolleyError volleyError) {
                Log.d(TAG, "loadMoreBlogPostsRequest:  Error-> " + volleyError.networkResponse.statusCode);
                Toast.makeText(getActivity(), "That's all the post..", Toast.LENGTH_SHORT).show();
                // hiding our progress bar.
                loadingPB.setVisibility(View.GONE);
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
                        } catch (Exception e) {

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
                firstCall = false;
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
        chatListBtn = homeView.findViewById(R.id.iv_menu_home);
        loadingPB = homeView.findViewById(R.id.idPBLoading);
        nestedSV = homeView.findViewById(R.id.idNestedSV);
        homeRecyclerView = homeView.findViewById(R.id.home_feed_rv);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        homeAdapter = new HomeAdapter(getActivity(), blogModelArrayList, (userId, blogId, liked) -> {
            if (!liked) {
                initiateSocketConnection(userId);
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put(TYPE_WS, LIKE_ACTION_WS);
                    jsonObject.put(POST_ID_WS, blogId);

                    webSocket.send(jsonObject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                //implement api call for unlike the post
                callUnlikeRequest(blogId);
            }
        });
        homeRecyclerView.setLayoutManager(linearLayoutManager);
        homeRecyclerView.setAdapter(homeAdapter);
        title = homeView.findViewById(R.id.username_feed);
        title.setText(R.string.app_name);
    }

    private void callUnlikeRequest(String blogId) {
        callAuthPostRequest(getActivity(), UNLIKE + blogId, new ApiResultCallback() {
            @Override
            public void onAPIResultSuccess(JSONObject jsonObject) {

            }

            @Override
            public void onAPIResultError(VolleyError volleyError) {
                Toast.makeText(getActivity(),
                        "Something went wrong, please check your internet connectivity",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void initiateSocketConnection(String userID) {
        String SERVER_PATH = NOTIFICATION_WS + userID + "/?user_token="
                + GlobalVariables.getInstance(getActivity()).getUserToken();

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(SERVER_PATH).build();
        webSocket = client.newWebSocket(request, new CommentWebSocketListener());
    }

    private static class CommentWebSocketListener extends WebSocketListener {

        @Override
        public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
            super.onOpen(webSocket, response);

        }

        @Override
        public void onMessage(@NotNull WebSocket webSocket, @NotNull String textResponse) {
            super.onMessage(webSocket, textResponse);

        }

        @Override
        public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, @org.jetbrains.annotations.Nullable Response response) {
            super.onFailure(webSocket, t, response);

        }
    }
}

