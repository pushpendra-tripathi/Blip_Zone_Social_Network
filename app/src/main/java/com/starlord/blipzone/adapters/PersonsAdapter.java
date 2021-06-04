package com.starlord.blipzone.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.starlord.blipzone.models.UserModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

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
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PersonsAdapter.PersonViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class PersonViewHolder extends RecyclerView.ViewHolder {
        public PersonViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
        }
    }
}
