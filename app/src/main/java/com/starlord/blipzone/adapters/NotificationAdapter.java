package com.starlord.blipzone.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.starlord.blipzone.R;
import com.starlord.blipzone.configurations.UrlConstants;
import com.starlord.blipzone.models.NotificationModel;
import com.starlord.blipzone.views.activities.OtherProfileActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {
    Context context;
    ArrayList<NotificationModel> notificationModelArrayList;

    public NotificationAdapter(Context context, ArrayList<NotificationModel> notificationModelArrayList){
        this.context = context;
        this.notificationModelArrayList = notificationModelArrayList;
    }

    @NonNull
    @NotNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_notification_item, null);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull NotificationAdapter.NotificationViewHolder holder, int position) {
        NotificationModel notificationModel = notificationModelArrayList.get(position);

        holder.notificationTitle.setText(notificationModel.getContent());
        switch (notificationModel.getType()){
            case 1:
                holder.notificationTypeImage.setImageResource(R.drawable.notification_follow);
                break;
            case 2:
                holder.notificationTypeImage.setImageResource(R.drawable.notification_like);
                break;
            case 3:
                holder.notificationTypeImage.setImageResource(R.drawable.notificaiton_comment);
                break;
        }
        if (notificationModel.getType() == 1){
            holder.userImage.setVisibility(View.VISIBLE);
            Picasso.get().load(UrlConstants.BASE_URL + notificationModel.getNotifierUserImage())
                    .placeholder(R.drawable.profile_avatar)
                    .into(holder.userImage);
        }

        holder.userImage.setOnClickListener(v -> {
            Intent intent = new Intent(context, OtherProfileActivity.class);
            intent.putExtra("userId", String.valueOf(notificationModel.getNotifierUserId()));
            intent.putExtra("username", notificationModel.getNotifierUserName());
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return notificationModelArrayList.size();
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        CircleImageView notificationTypeImage;
        TextView notificationTitle;
        CircleImageView userImage;
        public NotificationViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            notificationTypeImage = itemView.findViewById(R.id.iv_left_icon);
            notificationTitle = itemView.findViewById(R.id.tv_title);
            userImage = itemView.findViewById(R.id.iv_right_icon);
        }
    }
}
