package com.SAP.studyd8.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.SAP.studyd8.Chat;
import com.SAP.studyd8.MessageActivity;
import com.SAP.studyd8.R;
import com.SAP.studyd8.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.List;
import java.util.Objects;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context mContext;
    private List<User> mUsers;
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    private String lastMessage;

    public UserAdapter(Context mContext, List<User> mUsers)
    {
        this.mUsers = mUsers;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item, parent, false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.username.setText(mUsers.get(position).getUsername());

        holder.lastMessage(mUsers.get(position).getUserId(), holder.last_message);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MessageActivity.class);
                intent.putExtra("userId", (mUsers.get(position)).getUserId());
                mContext.startActivity(intent);
            }
        });

    }



    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView username;
        public TextView last_message;


        public ViewHolder(View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            last_message = itemView.findViewById(R.id.last_msg);
        }

       public void lastMessage(final String id, final TextView last_message){

            lastMessage = "";

            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("chats").orderBy("timestamp", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful())
                    {
                        for(QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult()))
                        {
                            Chat chat = new Chat();

                            chat.setSender(document.getString("sender"));
                            chat.setRecipient(document.getString("recipient"));
                            chat.setMessage(document.getString("message"));

                            if(chat.getSender().equals(firebaseUser.getUid()) && chat.getRecipient().equals(id) || chat.getSender().equals(id) && chat.getRecipient().equals(firebaseUser.getUid()))
                            {
                                last_message.setText(chat.getMessage());
                                lastMessage = chat.getMessage();
                                break;
                            }

                        }
                    }

                }

            });

        }



    }


}
