package com.android.chatapp.discovery.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.chatapp.R;
import com.android.chatapp.discovery.repository.Callback;
import com.android.chatapp.discovery.service.UserService;
import com.android.chatapp.model.Chat;
import com.android.chatapp.model.User;
import com.android.chatapp.util.ImageLoader;
import com.android.chatapp.util.TimeFormatter;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private List<Chat> chats;
    private String currentUid;
    private Context context;
    private final UserService userService;

    public ChatAdapter(Context context, List<Chat> chats, String currentUid, UserService userService) {
        this.context = context;
        this.chats = chats;
        this.currentUid = currentUid;
        this.userService = userService;
    }

    public void updateChats(List<Chat> newChats) {
        this.chats = newChats;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Chat chat = chats.get(position);
        String otherUid = chat.getParticipants().stream()
                .filter(uid -> !uid.equals(currentUid))
                .findFirst()
                .orElse(null);

        if (otherUid != null) {
            userService.getUserById(otherUid, new Callback<User>() {
                @Override
                public void onSuccess(User user) {
                    if (user != null) {
                        holder.tvUsername.setText(user.getUsername());
                        ImageLoader.loadImage(context, user.getPhotoUrl(), holder.ivUserAvatar, R.drawable.ic_avatar);
                        holder.onlineIndicator.setVisibility(
                                user.isOnline() ? View.VISIBLE : View.GONE
                        );
                    }
                }

                @Override
                public void onError(String error) {
                    Log.e("ChatAdapter", "Failed to load user: " + error);
                }
            });
        }

        holder.tvLastMessage.setText(chat.getLastMessage());
        holder.tvTime.setText(TimeFormatter.formatTimestamp(chat.getLastTimestamp()));
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    static class ChatViewHolder extends RecyclerView.ViewHolder {
        ImageView ivUserAvatar;
        TextView tvUsername, tvLastMessage, tvTime;
        View onlineIndicator;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            ivUserAvatar = itemView.findViewById(R.id.ivUserAvatar);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvLastMessage = itemView.findViewById(R.id.tvLastMessage);
            tvTime = itemView.findViewById(R.id.tvTime);
            onlineIndicator = itemView.findViewById(R.id.onlineIndicator);
        }
    }
}
