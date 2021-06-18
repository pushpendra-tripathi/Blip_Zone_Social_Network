package com.starlord.blipzone.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.starlord.blipzone.R;
import com.starlord.blipzone.configurations.GlobalVariables;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter {
    private static final int TYPE_MESSAGE_SENT = 0;
    private static final int TYPE_MESSAGE_RECEIVED = 1;

    private final LayoutInflater inflater;
    private final Context context;
    private final List<JSONObject> messages = new ArrayList<>();

    public ChatAdapter(Context context, LayoutInflater inflater) {
        this.inflater = inflater;
        this.context = context;
    }

    private class SentMessageHolder extends RecyclerView.ViewHolder {

        TextView messageTxt;

        public SentMessageHolder(@NonNull View itemView) {
            super(itemView);

            messageTxt = itemView.findViewById(R.id.sentTxt);
        }
    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {

        TextView messageTxt;

        public ReceivedMessageHolder(@NonNull View itemView) {
            super(itemView);

            messageTxt = itemView.findViewById(R.id.receivedTxt);
        }
    }

    @Override
    public int getItemViewType(int position) {

        JSONObject message = messages.get(position);

        try {
            if (message.getString("username").equals(String.valueOf(GlobalVariables.getInstance(context).getUserName()))) {

                if (message.has("text"))
                    return TYPE_MESSAGE_SENT;

            } else {

                if (message.has("text"))
                    return TYPE_MESSAGE_RECEIVED;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return -1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;

        switch (viewType) {
            case TYPE_MESSAGE_SENT:
                view = inflater.inflate(R.layout.layout_item_messages_sent, parent, false);
                return new SentMessageHolder(view);

            case TYPE_MESSAGE_RECEIVED:
                view = inflater.inflate(R.layout.layout_item_messages_received, parent, false);
                return new ReceivedMessageHolder(view);

        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        JSONObject message = messages.get(position);

        try {
            if (message.getString("username").equals(String.valueOf(GlobalVariables.getInstance(context).getUserName()))) {

                if (message.has("text")) {

                    SentMessageHolder messageHolder = (SentMessageHolder) holder;
                    messageHolder.messageTxt.setText(message.getString("text"));

                }

            } else {

                if (message.has("text")) {

                    ReceivedMessageHolder messageHolder = (ReceivedMessageHolder) holder;
                    messageHolder.messageTxt.setText(message.getString("text"));

                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void addItem(JSONObject jsonObject) {
        messages.add(jsonObject);
        notifyDataSetChanged();
    }
}