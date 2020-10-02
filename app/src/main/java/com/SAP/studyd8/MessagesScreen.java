package com.SAP.studyd8;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.TextView;

import com.SAP.studyd8.Fragments.ChatsFragment;
import com.SAP.studyd8.Fragments.UserFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;


public class MessagesScreen extends AppCompatActivity {

    TextView username;

    FirebaseUser firebaseUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference ref;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages_screen);


        Toolbar toolbar = findViewById(R.id.toolbar);
      //  setSupportActionBar(toolbar);
     //   getSupportActionBar().setTitle("");
    //    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        username = findViewById(R.id.MessagesTopText);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        uid = firebaseUser.getUid();

        ref = db.collection("users").document(uid);

        ref.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                username.setText(value.getString("username"));
            }
        });


        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager viewPager = findViewById(R.id.view_pager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPagerAdapter.addFragment(new ChatsFragment(), "Chats");
        viewPagerAdapter.addFragment(new UserFragment(), "Users");

        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);


}

    class ViewPagerAdapter extends FragmentPagerAdapter{

        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        ViewPagerAdapter(FragmentManager fm){
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            fragments = new ArrayList<>();
            titles = new ArrayList<>();
        }


        @Override
        public Fragment getItem(int position)
        {
            return fragments.get(position);
        }

        @Override
        public int getCount()
        {
            return fragments.size();
        }

        public void addFragment(Fragment fragment, String title)
        {
            fragments.add(fragment);
            titles.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }
}
