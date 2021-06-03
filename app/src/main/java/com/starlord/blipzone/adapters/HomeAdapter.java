package com.starlord.blipzone.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.starlord.blipzone.R;
import com.starlord.blipzone.models.BlogModel;
import com.starlord.blipzone.utils.SquareImageView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewAdapter> {
    Context context;
    ArrayList<BlogModel> blogModelArrayList;

    public HomeAdapter(Context context, ArrayList<BlogModel> blogModelArrayList){
        this.context = context;
        this.blogModelArrayList = blogModelArrayList;
    }

    @NonNull
    @NotNull
    @Override
    public HomeViewAdapter onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_home_feed, null);
        return new HomeViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull HomeAdapter.HomeViewAdapter holder, int position) {
        BlogModel blogModel = blogModelArrayList.get(position);
        Picasso.get().load(blogModel.getUserModel().getProfileImage()).into(holder.circleImageView);
        holder.username.setText(blogModel.getUserModel().getUserName());
        Picasso.get().load(blogModel.getImageUrl()).into(holder.squareImageView);
        holder.content.setText(blogModel.getContent());
        holder.postTime.setText(blogModel.getCreatedAt());

        int likeCount = 0;
        String lastLikeBy = "";
        if (blogModel.getLikeList() != null)
            likeCount = blogModel.getLikeList().size();
        if (likeCount > 0){
            lastLikeBy = blogModel.getLikeList().get(0).getUserName();
            holder.likesText.setText("Liked by "+ lastLikeBy );
        } else if (likeCount > 1){
            lastLikeBy = blogModel.getLikeList().get(0).getUserName();
            holder.likesText.setText("Liked by "+ lastLikeBy +", and " + likeCount + " others");
        }



    }

    @Override
    public int getItemCount() {
        return blogModelArrayList.size();
    }

    public class HomeViewAdapter extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView username, likesText, content, allComment, postTime;
        ImageView ellipses, redHeart, whiteHeart, comment;
        SquareImageView squareImageView;
        public HomeViewAdapter(@NonNull @NotNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.profile_photo);
            username = itemView.findViewById(R.id.username);
            ellipses = itemView.findViewById(R.id.ivEllipses);
            squareImageView = itemView.findViewById(R.id.post_image);
            redHeart = itemView.findViewById(R.id.image_heart_red);
            whiteHeart = itemView.findViewById(R.id.image_heart);
            comment = itemView.findViewById(R.id.speech_bubble);
            likesText = itemView.findViewById(R.id.image_likes);
            content = itemView.findViewById(R.id.image_caption);
            allComment = itemView.findViewById(R.id.image_comments_link);
            postTime = itemView.findViewById(R.id.image_time_posted);
        }
    }
}
