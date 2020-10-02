package com.SAP.studyd8.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


// idea this users tab can contain the users with common classes

public class UserFragment extends Fragment {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> mUsers;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        mUsers = new ArrayList<>();

        userAdapter = new UserAdapter(getContext(), mUsers);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(userAdapter);

        readUsers();

        return view;
    }



    private void readUsers(){
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    mUsers.clear();

                    for(QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult()))
                    {
                        if(!document.getString("userId").equals(FirebaseAuth.getInstance().getUid())) {
                            User user = new User();

                            user.setUsername(document.getString("username"));
                            user.setMajor(document.getString("major"));
                            user.setUserId(document.getString("userId"));
                            user.setFirstName(document.getString("FirstName"));
                            user.setLastName(document.getString("lastName"));
                            user.setStudyHabits(document.getString("studyHabits"));

                            mUsers.add(user);
                        }
                    }

                    userAdapter.notifyDataSetChanged();
                }
            }
        });


            }



    }




