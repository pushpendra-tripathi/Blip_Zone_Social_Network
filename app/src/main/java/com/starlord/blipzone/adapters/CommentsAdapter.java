package com.starlord.blipzone.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.starlord.blipzone.models.CommentModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder> {
    Context context;
    ArrayList<CommentModel> commentModelArrayList;

    public CommentsAdapter(Context context, ArrayList<CommentModel> commentModelArrayList){
        this.context = context;
        this.commentModelArrayList = commentModelArrayList;
    }

    @NonNull
    @NotNull
    @Override
    public CommentsViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CommentsAdapter.CommentsViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class CommentsViewHolder extends RecyclerView.ViewHolder {
        public CommentsViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
        }
    }
}