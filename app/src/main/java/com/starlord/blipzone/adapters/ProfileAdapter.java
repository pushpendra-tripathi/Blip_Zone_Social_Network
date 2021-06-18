package com.starlord.blipzone.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.starlord.blipzone.R;
import com.starlord.blipzone.models.BlogModel;
import com.starlord.blipzone.utils.SquareImageView;
import com.starlord.blipzone.views.activities.ViewPostActivity;

import java.util.ArrayList;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder> {
    ArrayList<BlogModel> blogModelList;
    Context context;
    String userName, profileImageUrl;

    public ProfileAdapter(Context context, ArrayList<BlogModel> blogModelList, String userName, String profileImageUrl) {
        this.context = context;
        this.blogModelList = blogModelList;
        this.userName = userName;
        this.profileImageUrl = profileImageUrl;
    }

    @NonNull
    @org.jetbrains.annotations.NotNull
    @Override
    public ProfileViewHolder onCreateViewHolder(@NonNull @org.jetbrains.annotations.NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_card_profile, null);
        return new ProfileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @org.jetbrains.annotations.NotNull ProfileAdapter.ProfileViewHolder holder, int position) {
        BlogModel blogModel = blogModelList.get(position);
        if (blogModel.getImageUrl().length() > 4) {
            Picasso.get().load(blogModel.getImageUrl()).into(holder.imageView);
        }

        holder.imageView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ViewPostActivity.class);
            intent.putExtra("blogModelArray", blogModelList);
            intent.putExtra("userName", userName);
            intent.putExtra("profileImage", profileImageUrl);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return blogModelList.size();
    }

    public class ProfileViewHolder extends RecyclerView.ViewHolder {
        SquareImageView imageView;

        public ProfileViewHolder(@NonNull @org.jetbrains.annotations.NotNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.profileImageView);
        }
    }
}
