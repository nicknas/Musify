package com.musify.view.message;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.musify.R;
import com.musify.model.message.Message;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<Message> messages;
    private Context context;

    private static final int CHATBOT_MESSAGE = 0;
    private static final int USER_MESSAGE = 1;

    public MessageAdapter(Context context) {
        messages = new ArrayList<>();
        this.context = context;
    }

    public void add(Message message) {
        this.messages.add(message);
        notifyItemInserted(this.messages.size() - 1);
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == USER_MESSAGE) {
            return new MessageViewHolder(LayoutInflater.from(context).inflate(R.layout.user_message, parent, false));
        }
        else {
            return new MessageViewHolder(LayoutInflater.from(context).inflate(R.layout.chatbot_message, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        TextView messageBodyView = holder.message.findViewById(R.id.message_body);
        messageBodyView.setText(messages.get(position).getText());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {

        if (messages.get(position).isBelongsToUser()) {
            return USER_MESSAGE;
        } else {
            return CHATBOT_MESSAGE;
        }
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder{
        public View message;

        public MessageViewHolder(View message) {
            super(message);
            this.message = message;
        }
    }

}

