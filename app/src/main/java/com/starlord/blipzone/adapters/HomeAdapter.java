package com.starlord.blipzone.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.starlord.blipzone.R;
import com.starlord.blipzone.models.BlogModel;
import com.starlord.blipzone.utils.SquareImageView;
import com.starlord.blipzone.views.activities.CommentsActivity;
import com.starlord.blipzone.views.activities.LikesActivity;
import com.starlord.blipzone.views.activities.OtherProfileActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.WebSocket;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewAdapter> {
    private final Context context;
    private final ArrayList<BlogModel> blogModelArrayList;
    private WebSocket webSocket;
    String TAG = "HomeAdapterLog";
    private final OnHeartClickListener onHeartClickListener;

    public HomeAdapter(Context context, ArrayList<BlogModel> blogModelArrayList, OnHeartClickListener onHeartClickListener) {
        this.onHeartClickListener = onHeartClickListener;
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

    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    @Override
    public void onBindViewHolder(@NonNull @NotNull HomeAdapter.HomeViewAdapter holder, int position) {
        BlogModel blogModel = blogModelArrayList.get(position);
        Picasso.get().load(blogModel.getUserModel().getProfileImage())
                .placeholder(R.drawable.profile_avatar)
                .into(holder.circleImageView);
        holder.username.setText(blogModel.getUserModel().getUserName());
        Picasso.get().load(blogModel.getImageUrl()).into(holder.squareImageView);
        holder.content.setText(blogModel.getContent());
        holder.postTime.setText(blogModel.getCreatedAt());

        int likeCount = 0;
        String lastLikeBy = "";
        if (blogModel.getLikeModel() != null) {
            holder.likesText.setVisibility(View.VISIBLE);
            likeCount = blogModel.getLikeModel().getLikeCount();
        }
        if (blogModel.getLikeModel() != null && likeCount > 0) {
            lastLikeBy = blogModel.getLikeModel().getUserModel().getUserName();
            holder.likesText.setText("Liked by " + lastLikeBy + ", and " + likeCount + " others");
        } else if (blogModel.getLikeModel() != null) {
            lastLikeBy = blogModel.getLikeModel().getUserModel().getUserName();
            holder.likesText.setText("Liked by " + lastLikeBy);
        }

        holder.circleImageView.setOnClickListener(v -> {
            Intent intent = new Intent(context, OtherProfileActivity.class);
            intent.putExtra("userId", String.valueOf(blogModel.getUserModel().getId()));
            intent.putExtra("username", blogModel.getUserModel().getUserName());
            context.startActivity(intent);
        });

        if (blogModel.isLiked()) {
            holder.whiteHeart.setVisibility(View.GONE);
            holder.redHeart.setVisibility(View.VISIBLE);
        }

        holder.comment.setOnClickListener(v -> {
            Intent intent = new Intent(context, CommentsActivity.class);
            intent.putExtra("blogId", String.valueOf(blogModel.getId()));
            intent.putExtra("userId", blogModel.getUserModel().getId());
            context.startActivity(intent);
        });

        holder.allComment.setOnClickListener(v -> {
            Intent intent = new Intent(context, CommentsActivity.class);
            intent.putExtra("blogId", String.valueOf(blogModel.getId()));
            context.startActivity(intent);
        });

        holder.likesText.setOnClickListener(v -> {
            Intent intent = new Intent(context, LikesActivity.class);
            intent.putExtra("blogId", String.valueOf(blogModel.getId()));
            context.startActivity(intent);
        });

        holder.whiteHeart.setOnClickListener(v -> {
            // for liking the post
            onHeartClickListener.onHeartClick(String.valueOf(blogModel.getUserModel().getId()),
                    String.valueOf(blogModel.getId()),
                    false);
            holder.whiteHeart.setVisibility(View.GONE);
            holder.redHeart.setVisibility(View.VISIBLE);
        });

        holder.redHeart.setOnClickListener(v -> {
            // for unliking the post
            onHeartClickListener.onHeartClick(String.valueOf(blogModel.getUserModel().getId()),
                    String.valueOf(blogModel.getId()),
                    true);
            holder.whiteHeart.setVisibility(View.VISIBLE);
            holder.redHeart.setVisibility(View.GONE);
        });

        // Double tap to like feature
        holder.squareImageView.setOnTouchListener(new View.OnTouchListener() {
            private final GestureDetector gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    Log.d(TAG, "onDoubleTap");
                    // for liking the post
                    onHeartClickListener.onHeartClick(String.valueOf(blogModel.getUserModel().getId()),
                            String.valueOf(blogModel.getId()),
                            false);
                    holder.whiteHeart.setVisibility(View.GONE);
                    holder.redHeart.setVisibility(View.VISIBLE);
                    holder.animatedHrat.setVisibility(View.VISIBLE);
                    animateHeart(holder.animatedHrat);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            holder.animatedHrat.setVisibility(View.GONE);
                        }
                    }, 600);

                    return super.onDoubleTap(e);
                }
            });

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG, "Raw event: " + event.getAction() + ", (" + event.getRawX() + ", " + event.getRawY() + ")");
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });

    }

    // Heart animation
    public void animateHeart(final ImageView view) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        prepareAnimation(scaleAnimation);

        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        prepareAnimation(alphaAnimation);

        AnimationSet animation = new AnimationSet(true);
        animation.addAnimation(alphaAnimation);
        animation.addAnimation(scaleAnimation);
        animation.setDuration(500);
        animation.setFillAfter(true);

        view.startAnimation(animation);

    }

    private void prepareAnimation(Animation animation) {
        animation.setRepeatCount(1);
        animation.setRepeatMode(Animation.REVERSE);
    }

    @Override
    public int getItemCount() {
        return blogModelArrayList.size();
    }

    public static class HomeViewAdapter extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView username, likesText, content, allComment, postTime;
        ImageView ellipses, redHeart, whiteHeart, comment, animatedHrat;
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
            animatedHrat = itemView.findViewById(R.id.animatedHeart);
        }
    }

    public interface OnHeartClickListener {
        void onHeartClick(String userId, String blogId, boolean liked);
    }
}
