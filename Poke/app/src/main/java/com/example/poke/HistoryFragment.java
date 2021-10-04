package com.example.poke;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {
    private RecyclerView recyclerView;
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    private ArrayList<UserHistory> historyList;
    private FirebaseAuth mAuth;
    HistoryAdapter adapter = new HistoryAdapter();
    String uid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history_fragment, container, false);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null)
            uid = user.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        recyclerView = (RecyclerView) view.findViewById(R.id.history_recycler);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager
                = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        historyList = new ArrayList<>();

        database = FirebaseDatabase.getInstance();
        mDatabase.child("history").child(uid).addChildEventListener(historyChildEventListener);
        adapter = new HistoryAdapter(historyList);
        recyclerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                UserHistory deleteditem = historyList.get(viewHolder.getBindingAdapterPosition());

                int position = viewHolder.getBindingAdapterPosition();
                mDatabase.child("history").child(uid).child(deleteditem.getRcp_id()).removeValue();
                historyList.remove(viewHolder.getBindingAdapterPosition());
                adapter.notifyItemRemoved(viewHolder.getBindingAdapterPosition());
                Snackbar.make(recyclerView,"삭제 완료", Snackbar.LENGTH_LONG).setAction("실행취소", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDatabase.child("history").child(uid).child(deleteditem.getRcp_id()).setValue(deleteditem);
                        Snackbar.make(recyclerView,"취소되었습니다.",Snackbar.LENGTH_LONG).show();
                    }
                }).show();
            }
        }).attachToRecyclerView(recyclerView);

        return view;
    }

    ChildEventListener historyChildEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            UserHistory history = snapshot.getValue(UserHistory.class);
            historyList.add(history);
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

}

