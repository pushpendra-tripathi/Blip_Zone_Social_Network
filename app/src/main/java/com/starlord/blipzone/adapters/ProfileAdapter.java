package com.starlord.blipzone.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.starlord.blipzone.R;
import com.starlord.blipzone.configurations.UrlConstants;
import com.starlord.blipzone.models.BlogModel;

import java.util.ArrayList;
import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder> {
    ArrayList<BlogModel> blogModelList;
    Context context;

    public ProfileAdapter(Context context, ArrayList<BlogModel> blogModelList){
        this.context = context;
        this.blogModelList = blogModelList;
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
        Glide.with(context)
                .load(UrlConstants.BASE_URL + blogModel.getImageUrl())
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return blogModelList.size();
    }

    public class ProfileViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public ProfileViewHolder(@NonNull @org.jetbrains.annotations.NotNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.profileImageView);
        }
    }
}