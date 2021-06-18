package com.starlord.blipzone.views.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.google.android.material.textfield.TextInputEditText;
import com.starlord.blipzone.R;
import com.starlord.blipzone.adapters.PersonsAdapter;
import com.starlord.blipzone.callbacks.ApiResponseCallback;
import com.starlord.blipzone.models.UserModel;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.starlord.blipzone.api.CommonClassForAPI.callSearchRequest;

public class SearchFragment extends Fragment {
    TextInputEditText searchBar;
    ImageView backBtn;
    TextView searchTextPlaceHolder;
    RecyclerView searchRecyclerView;
    LinearLayoutManager linearLayoutManager;
    PersonsAdapter personsAdapter;
    ArrayList<UserModel> userModelList;
    String TAG = "SearchFragmentLog";

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View searchView = inflater.inflate(R.layout.fragment_search, container, false);
        initializeViews(searchView);
        return searchView;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d(TAG, "beforeTextChanged: ");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG, "onTextChanged: ");
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals("")) {
                    Log.d(TAG, "afterTextChanged: ");
                    searchTextPlaceHolder.setVisibility(View.GONE);
                    loadSearchRequest(s.toString());
                }
            }
        });

        backBtn.setOnClickListener(v ->{
            requireActivity().onBackPressed();
        });
    }

    private void loadSearchRequest(String string) {
        callSearchRequest(getActivity(), string, new ApiResponseCallback() {
            @Override
            public void onApiSuccessResult(JSONObject jsonObject) {
                Log.d(TAG, "onResponse: Success");
                processSearchRequest(jsonObject);
            }

            @Override
            public void onApiFailureResult(Exception e) {
                Log.d(TAG, "onAPIResultErrorCode: " + e.toString());
            }

            @Override
            public void onApiErrorResult(VolleyError volleyError) {
                Log.d(TAG, "onAPIResultErrorCode: " + volleyError.networkResponse.statusCode);
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void processSearchRequest(JSONObject jsonObject) {
        if (userModelList.size() > 0)
            userModelList.clear();
        try {
            boolean status = jsonObject.getBoolean("status");

            if (status) {
                JSONArray data = jsonObject.getJSONArray("data");
                for (int i = 0; i < data.length(); i++) {

                    try {
                        JSONObject user = data.getJSONObject(i);
                        UserModel userModel = new UserModel();
                        userModel.setId(user.getInt("id"));
                        userModel.setUserName(user.getString("username"));
                        userModel.setFirstName(user.getString("first_name"));
                        userModel.setLastName(user.getString("last_name"));
                        userModel.setProfileImage(user.getString("profile_image"));
                        userModelList.add(userModel);
                        personsAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

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

    private void initializeViews(View searchView) {
        searchBar = searchView.findViewById(R.id.et_search);
        searchBar.setVisibility(View.VISIBLE);
        backBtn = searchView.findViewById(R.id.backBtn_search);
        searchTextPlaceHolder = searchView.findViewById(R.id.search_text);
        searchRecyclerView = searchView.findViewById(R.id.search_rv);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        userModelList = new ArrayList<>();
        personsAdapter = new PersonsAdapter(getActivity(), userModelList);
        searchRecyclerView.setLayoutManager(linearLayoutManager);
        searchRecyclerView.setAdapter(personsAdapter);
    }
}