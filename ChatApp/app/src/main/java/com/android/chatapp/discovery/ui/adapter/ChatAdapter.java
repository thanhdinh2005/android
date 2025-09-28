package com.android.chatapp.discovery.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.chatapp.R;
import com.android.chatapp.model.Chat;
import com.android.chatapp.model.User;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private List<Chat> chats;
    private String currentUid;
    private Context context;

    public ChatAdapter(Context context, List<Chat> chats, String currentUid) {
        this.context = context;
        this.chats = chats;
        this.currentUid = currentUid;
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

        String currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String otherUid = chat.getParticipants().stream()
                .filter(uid -> !uid.equals(currentUid))
                .findFirst()
                .orElse(null);

        if (otherUid != null) {
            FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(otherUid)
                    .get()
                    .addOnSuccessListener(snapshot -> {
                        User user = snapshot.toObject(User.class);
                        if (user != null) {
                            holder.tvUsername.setText(user.getUsername());

                            Glide.with(holder.itemView.getContext())
                                    .load(user.getPhotoUrl())
                                    .placeholder(R.drawable.ic_avatar)
                                    .circleCrop()
                                    .into(holder.ivUserAvatar);

                            holder.onlineIndicator.setVisibility(
                                    user.isOnline() ? View.VISIBLE : View.GONE
                            );
                        }
                    });
        }

        holder.tvLastMessage.setText(chat.getLastMessage());

        String timeText = formatTimestamp(chat.getLastTimestamp());
        holder.tvTime.setText(timeText);
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

    private String formatTimestamp(long timestamp) {
        Date date = new Date(timestamp);
        SimpleDateFormat todayFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM", Locale.getDefault());

        Calendar now = Calendar.getInstance();
        Calendar msgTime = Calendar.getInstance();
        msgTime.setTime(date);

        if (now.get(Calendar.YEAR) == msgTime.get(Calendar.YEAR) &&
                now.get(Calendar.DAY_OF_YEAR) == msgTime.get(Calendar.DAY_OF_YEAR)) {
            return todayFormat.format(date);
        }

        now.add(Calendar.DAY_OF_YEAR, -1);
        if (now.get(Calendar.YEAR) == msgTime.get(Calendar.YEAR) &&
                now.get(Calendar.DAY_OF_YEAR) == msgTime.get(Calendar.DAY_OF_YEAR)) {
            return "Yesterday";
        }

        return dateFormat.format(date);
    }

}

