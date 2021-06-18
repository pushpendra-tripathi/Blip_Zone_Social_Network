package com.starlord.blipzone.views.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.starlord.blipzone.R;
import com.starlord.blipzone.adapters.NotificationAdapter;
import com.starlord.blipzone.callbacks.ApiResultCallback;
import com.starlord.blipzone.configurations.UrlConstants;
import com.starlord.blipzone.models.NotificationModel;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.starlord.blipzone.api.CommonClassForAPI.callAuthGetRequest;

public class AlertFragment extends Fragment {
    ImageView backBtn;
    TextView title;
    RecyclerView alertRecyclerView;
    LinearLayoutManager linearLayoutManager;
    NotificationAdapter notificationAdapter;
    ArrayList<NotificationModel> notificationModelArrayList;
    String TAG = "AlertFragmentLog";

    public AlertFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View alertView = inflater.inflate(R.layout.fragment_alert, container, false);
        initializeViews(alertView);
        return alertView;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadNotificationsRequest();

        backBtn.setOnClickListener(v -> {
            requireActivity().onBackPressed();
        });
    }

    private void loadNotificationsRequest() {
        callAuthGetRequest(getActivity(),
                UrlConstants.NOTIFICATIONS,
                new ApiResultCallback() {
                    @Override
                    public void onAPIResultSuccess(JSONObject jsonObject) {
                        Log.d(TAG, "onResponse: Success");
                        processNotificationsRequest(jsonObject);
                    }

                    @Override
                    public void onAPIResultError(VolleyError volleyError) {
                        Log.d(TAG, "onAPIResultErrorCode: " + volleyError.networkResponse.statusCode);
                        Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void processNotificationsRequest(JSONObject jsonObject) {
        try {
            boolean status = jsonObject.getBoolean("status");

            if (status) {
                JSONArray data = jsonObject.getJSONArray("data");
                for (int i = 0; i < data.length(); i++) {
                    JSONObject notification = data.getJSONObject(i);
                    NotificationModel notificationModel = new NotificationModel();

                    notificationModel.setType(notification.getInt("type"));
                    notificationModel.setContent(notification.getString("comment"));

                    if (notification.getInt("type") == 1) {
                        JSONObject notifier = notification.getJSONObject("notifier");
                        notificationModel.setNotifierUserName(notifier.getString("username"));
                        notificationModel.setNotifierUserImage(notifier.getString("pic"));
                        notificationModel.setNotifierUserId(notifier.getInt("id"));
                    } else {
                        notificationModel.setNotifierUserName(notification.getString("notifier"));
                    }

                    try {
                        notificationModel.setPostId(notification.getInt("post"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        notificationModel.setPostImageUrl(notification.getString("post_data"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    notificationModelArrayList.add(notificationModel);
                    notificationAdapter.notifyDataSetChanged();
                }
            } else {
                Toast.makeText(getActivity(),
                        "No notifications available for now...",
                        Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initializeViews(View alertView) {
        backBtn = alertView.findViewById(R.id.backBtn_alert);
        title = alertView.findViewById(R.id.title_alert);
        notificationModelArrayList = new ArrayList<>();
        title.setText(R.string.notifications);
        alertRecyclerView = alertView.findViewById(R.id.alert_rv);
        linearLayoutManager = new LinearLayoutManager(getContext());
        notificationAdapter = new NotificationAdapter(getActivity(), notificationModelArrayList);
        alertRecyclerView.setLayoutManager(linearLayoutManager);
        alertRecyclerView.setAdapter(notificationAdapter);
    }
}