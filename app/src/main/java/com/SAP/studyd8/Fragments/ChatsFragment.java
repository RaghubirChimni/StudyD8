package com.SAP.studyd8.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.SAP.studyd8.Chat;
import com.SAP.studyd8.Adapter.UserAdapter;
import com.SAP.studyd8.R;
import com.SAP.studyd8.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.v1.StructuredQuery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executor;

public class ChatsFragment extends Fragment {


    private List<User> mUsers;
    private List<Chatlist> usersList;
    private List<String> userDocs;
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    FirebaseUser firebaseUser;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DatabaseReference reference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chats, container, false);

        usersList = new ArrayList<>();
        mUsers = new ArrayList<>();
        userDocs = new ArrayList<>();
        userAdapter = new UserAdapter(getContext(), mUsers);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(userAdapter);


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        reference = FirebaseDatabase.getInstance().getReference("chatList").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                    Chatlist chatlist = snapshot.getValue(Chatlist.class);

                    usersList.add(chatlist);
                }

                Log.e("TAG", "about to readChatList");
                readChatList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }



    private void readChatList() {

        DocumentReference ref;
        mUsers.clear();

      //  Collections.reverse(usersList);

        for(int i = 0; i < usersList.size(); i++){

            ref = db.collection("users").document(usersList.get(i).getId());

            ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if(task.isSuccessful()){

                        DocumentSnapshot value = task.getResult();

                        User user = new User();

                        user.setUserId(value.getString("userId"));
                        user.setUsername(value.getString("username"));
                        user.setStudyHabits(value.getString("studyHabits"));
                        user.setFirstName(value.getString("firstName"));
                        user.setLastName(value.getString("lastName"));
                        user.setMajor(value.getString("major"));
                        user.setUniversity(value.getString("university"));

                        if(!mUsers.contains(user)){
                            mUsers.add(user);
                        }

                    }

                    userAdapter.notifyDataSetChanged();
                }
            });
        }




    }



}




