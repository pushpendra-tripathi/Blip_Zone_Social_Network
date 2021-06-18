package com.starlord.blipzone.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.starlord.blipzone.R;
import com.starlord.blipzone.models.ChatListModel;
import com.starlord.blipzone.views.activities.ChatActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatListViewHolder> {
    Context context;
    ArrayList<ChatListModel> chatListModelArrayList;

    public ChatListAdapter(Context context, ArrayList<ChatListModel> chatListModelArrayList){
        this.context = context;
        this.chatListModelArrayList = chatListModelArrayList;
    }

    @NonNull
    @NotNull
    @Override
    public ChatListViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item_chatlist, null);
        return new ChatListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ChatListAdapter.ChatListViewHolder holder, int position) {
        ChatListModel chatListModel = chatListModelArrayList.get(position);

        Picasso.get().load(chatListModel.getUserModel().getProfileImage())
                .placeholder(R.drawable.profile_avatar)
                .into(holder.profileImage);

        holder.userName.setText(chatListModel.getUserModel().getUserName());
        holder.lastMessage.setText(chatListModel.getText());

        holder.chatListLayout.setOnClickListener(v-> {
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("username", chatListModel.getUserModel().getUserName());
            intent.putExtra("profileImage", chatListModel.getUserModel().getProfileImage());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return chatListModelArrayList.size();
    }

    public class ChatListViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout chatListLayout;
        CircleImageView profileImage;
        TextView userName, lastMessage, timeStamp;
        public ChatListViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            chatListLayout = itemView.findViewById(R.id.chatListLayout);
            profileImage = itemView.findViewById(R.id.user_image_chat);
            userName = itemView.findViewById(R.id.tv_user_name);
            lastMessage = itemView.findViewById(R.id.tv_user_status);
            timeStamp = itemView.findViewById(R.id.tv_last_message_time);
        }
    }
}
