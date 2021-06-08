package com.starlord.blipzone.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.starlord.blipzone.R;
import com.starlord.blipzone.models.CommentModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

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
        View view = LayoutInflater.from(context).inflate(R.layout.layout_comments, null);
        return new CommentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CommentsAdapter.CommentsViewHolder holder, int position) {
        CommentModel commentModel = commentModelArrayList.get(position);
        Picasso.get().load(commentModel.getUserModel().getProfileImage())
                .placeholder(R.drawable.profile_avatar)
                .into(holder.profileImage);

        holder.commentUserName.setText(commentModel.getUserModel().getUserName());
        holder.comment.setText(commentModel.getContent());
        holder.commentTime.setText(commentModel.getCreatedAt());

    }

    @Override
    public int getItemCount() {
        return commentModelArrayList.size();
    }

    public class CommentsViewHolder extends RecyclerView.ViewHolder {
        CircleImageView profileImage;
        TextView commentUserName, comment, commentTime;
        public CommentsViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.comment_profile_image);
            commentUserName = itemView.findViewById(R.id.comment_username);
            comment = itemView.findViewById(R.id.comment);
            commentTime = itemView.findViewById(R.id.comment_time_posted);
        }
    }
}