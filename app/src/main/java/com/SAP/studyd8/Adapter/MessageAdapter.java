package com.SAP.studyd8.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.SAP.studyd8.Chat;
import com.SAP.studyd8.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder>{
    private Context mContext;
    private List<Chat> mChats;
    public static final int MSG_LEFT = 0, MSG_RIGHT = 1;
    FirebaseUser firebaseUser;

    public MessageAdapter(Context mContext, List<Chat> mChats)
    {
        this.mChats = mChats;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         if (viewType == MSG_RIGHT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, final int position) {
        holder.show_message.setText(mChats.get(position).getMessage());
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(mChats.get(position).getSender().equals(firebaseUser.getUid()))
            return MSG_RIGHT;
        else
            return MSG_LEFT;
    }

    @Override
    public int getItemCount() {
       return mChats.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView show_message;

        public ViewHolder(View itemView)
        {
            super(itemView);
            show_message = itemView.findViewById(R.id.show_message);
        }


    }
}
