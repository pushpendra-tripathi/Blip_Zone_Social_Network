package com.starlord.blipzone.adapters;

import android.annotation.SuppressLint;
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
import com.starlord.blipzone.models.UserModel;
import com.starlord.blipzone.views.activities.OtherProfileActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonsAdapter extends RecyclerView.Adapter<PersonsAdapter.PersonViewHolder> {
    Context context;
    ArrayList<UserModel> followersList;

    public PersonsAdapter(Context context, ArrayList<UserModel> followersList){
        this.context = context;
        this.followersList = followersList;
    }

    @NonNull
    @NotNull
    @Override
    public PersonViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(context).inflate(R.layout.layout_persons, null);
        return new PersonViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull @NotNull PersonsAdapter.PersonViewHolder holder, int position) {
        UserModel userModel = followersList.get(position);
        Picasso.get().load(userModel.getProfileImage()).placeholder(R.drawable.profile_avatar).into(holder.userImage);
        holder.username.setText(userModel.getUserName());
        holder.fullName.setText(userModel.getFirstName() + " " + userModel.getLastName());
        holder.personLayout.setOnClickListener(v ->{
            Intent intent = new Intent(context, OtherProfileActivity.class);
            intent.putExtra("userId", String.valueOf(userModel.getId()));
            intent.putExtra("username", userModel.getUserName());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return followersList.size();
    }

    public class PersonViewHolder extends RecyclerView.ViewHolder {
        CircleImageView userImage;
        TextView username, fullName;
        RelativeLayout personLayout;
        public PersonViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.iv_user_image);
            username = itemView.findViewById(R.id.usernameTv);
            fullName = itemView.findViewById(R.id.fullName);
            personLayout = itemView.findViewById(R.id.person_layout);
        }
    }
}
