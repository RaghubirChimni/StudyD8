package com.SAP.studyd8;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.SAP.studyd8.Adapter.MessageAdapter;
import com.SAP.studyd8.Adapter.UserAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MessageActivity extends AppCompatActivity {

    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    TextView username;
    ImageButton sendMessageButton;
    EditText message;

    Intent intent;
    String userId;

    MessageAdapter messageAdapter;
    List<Chat> mChats;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Toolbar toolbar = findViewById(R.id.toolbar);

        mChats = new ArrayList<>();

        messageAdapter = new MessageAdapter(getApplicationContext(), mChats);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(messageAdapter);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        message = findViewById(R.id.message);
        sendMessageButton = findViewById(R.id.sendMessageButton);
        username = findViewById(R.id.username);

        intent = getIntent();
        userId = intent.getStringExtra("userId");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        assert userId != null;
        if(!userId.equals("")) {
            DocumentReference ref = FirebaseFirestore.getInstance().collection("users").document(userId);

            ref.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    username.setText(value.getString("username"));
                    readMessages(firebaseUser.getUid(), userId);
                }
            });
        }

        // Button to send message onClick
        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String m = message.getText().toString();

                if(!m.equals("")) {
                    sendMessage(firebaseUser.getUid(), userId, m);
                    readMessages(firebaseUser.getUid(), userId);
                }
                else
                    Toast.makeText(getApplicationContext(), "You can't send an empty message", Toast.LENGTH_SHORT).show();

                message.setText("");

            }
        });



    }


    private void sendMessage(String sender, String recipient, String message)
    {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        final long time = System.currentTimeMillis()/1000;

        final HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("sender", sender);
        hashMap.put("recipient", recipient);
        hashMap.put("message", message);

        databaseReference.child("Chats").push().setValue(hashMap);

        hashMap.put("timestamp", FieldValue.serverTimestamp());

        db.collection("chatLists").document().set(hashMap);
        db.collection("chats").document().set(hashMap);

        final DatabaseReference r = FirebaseDatabase.getInstance().getReference("chatList").child(firebaseUser.getUid()).child(userId);


        final HashMap<String, Object> hashMap2 = new HashMap<>();
        hashMap.put("id", userId);
        hashMap.put("time", time);

        r.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                r.child("id").setValue(userId);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final DatabaseReference chatRefReceiver = FirebaseDatabase.getInstance().getReference("chatList")
                .child(userId)
                .child(firebaseUser.getUid());



        chatRefReceiver.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                chatRefReceiver.child("id").setValue(firebaseUser.getUid());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



    private void readMessages(final String myId, final String uid) {

        mChats.clear();

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("chats").orderBy("timestamp", Query.Direction.ASCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                boolean isUpdated = false;
                if (task.isSuccessful()) {
                    mChats.clear();

                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                        Chat chat = new Chat();

                        chat.setSender(document.getString("sender"));
                        chat.setRecipient(document.getString("recipient"));
                        chat.setMessage(document.getString("message"));


                        if (chat.getSender().equals(myId) && chat.getRecipient().equals(uid) || chat.getSender().equals(uid) && chat.getRecipient().equals(myId)) {
                            mChats.add(chat);
                            isUpdated = true;
                        }

                    }

                    if (isUpdated) {
                        messageAdapter.notifyDataSetChanged();
                    }
                }

            }

        });

    }
}