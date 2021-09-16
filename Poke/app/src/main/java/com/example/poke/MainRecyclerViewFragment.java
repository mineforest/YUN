package com.example.poke;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class MainRecyclerViewFragment extends Fragment{
    ArrayList<Recipe_get> rcps = new ArrayList<>();
    ArrayList<Recipe_get> rcps_siyeonyong = new ArrayList<>();
    ArrayList<Recipe_get> sorted_rcps = new ArrayList<>();
    ArrayList<String> myIngreList;
    CustomAdapter adapter;
    CustomAdapter adapter3;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String uid;
    ArrayList<UserHistory> historyList;
    FirebaseUser user;
    private ViewPager2 viewPager;
    private MainViewpageAdapter adapter2;
    ProgressDialog progressDialog;
    Intent intent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        View view = inflater.inflate(R.layout.main_page, container, false);
        setHasOptionsMenu(true);
        ((MainActivity)getActivity()).getSupportActionBar().setElevation(0);
        myIngreList = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        uid = user.getUid();

        progressDialog = new ProgressDialog(getActivity());

        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("history").child(uid).addValueEventListener(historyListener);

        mDatabase.child("ingredient").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myIngreList.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot ridSnapshot : snapshot.getChildren()) {
                        UserIngredient ingres = ridSnapshot.getValue(UserIngredient.class);
                        myIngreList.add(ingres.getIngredientTitle());
                    }
                }
                for (Recipe_get rcp : rcps) {
                    rcp.setRate(myIngreList);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mDatabase.onDisconnect();

        RecyclerView recyclerView = view.findViewById(R.id.main_recylerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, RecyclerView.VERTICAL, false));
        adapter = new CustomAdapter(rcps);
        recyclerView.setAdapter(adapter);
        int largePadding = getResources().getDimensionPixelSize(R.dimen.shr_product_grid_spacing);
        int smallPadding = getResources().getDimensionPixelSize(R.dimen.shr_product_grid_spacing_small);
        recyclerView.addItemDecoration(new MainGridItemDecoration(largePadding, smallPadding));

        RecyclerView recyclerView2 = view.findViewById(R.id.main_recylerView2);
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setLayoutManager(new GridLayoutManager(getContext(), 2, RecyclerView.VERTICAL, false));
        adapter3 = new CustomAdapter(sorted_rcps);
        recyclerView2.setAdapter(adapter3);
        recyclerView2.addItemDecoration(new MainGridItemDecoration(largePadding, smallPadding));


        viewPager = view.findViewById(R.id.main_pager);
        adapter2 = new MainViewpageAdapter(rcps_siyeonyong);
        viewPager.setAdapter(adapter2);

        ImageView more_view1 = view.findViewById(R.id.more_rcp_thumbnail);
        more_view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(v.getContext(), MainMoreViewActivity.class);
                intent.putExtra("rcp",rcps);
                v.getContext().startActivity(intent);
            }
        });

        ImageView more_view2 = view.findViewById(R.id.more_rcp_thumbnail2);
        more_view2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(v.getContext(), MainMoreViewActivity.class);
                intent.putExtra("rcp",sorted_rcps);
                v.getContext().startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout_menu:
                FirebaseAuth.getInstance().signOut();
                myStartActivity(LoginActivity.class);
                break;

            case R.id.passwrod_reset_menu:
                myStartActivity(PasswordResetActivity.class);
                break;

            case R.id.revoke_menu:
                revokeAccess();
                myStartActivity(LoginActivity.class);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    ValueEventListener historyListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            StringBuilder rids = new StringBuilder();
            rids.append("6905019"); // 히스토리없을때 default값
            W2vHttpConn w2v = new W2vHttpConn();

            if (snapshot.exists()) {
                rcps.clear();
                rids.setLength(0);
                historyList = new ArrayList<>();
                for (DataSnapshot ridSnapshot : snapshot.getChildren()) {
                    UserHistory history = ridSnapshot.getValue(UserHistory.class);
                    historyList.add(new UserHistory(history.getRcp_id(), history.getRecipeTitle(), history.getRecipeImage(), history.getDate(), history.getRate()));
                    rids.append(history.getRcp_id()).append("+");
                }
                rids.deleteCharAt(rids.lastIndexOf("+"));
            }

            new Thread(){
                @Override
                public void run() {
                    String[] r_ids= w2v.getData(rids.toString());
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    for (int i = 0 ; i < 50 ; i++) {
                        String r_id = r_ids[i];
                        DocumentReference docRef = db.collection("recipe").document(r_id);
                        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                String rcp_id = documentSnapshot.getData().get("id").toString();
                                String title = documentSnapshot.getData().get("name").toString();
                                String thumbnail = documentSnapshot.getData().get("thumbnail").toString();
                                String cook_time = documentSnapshot.getData().get("time").toString();
                                List<String> tags = (List<String>) documentSnapshot.get("tag");
                                List<Map<String, String>> ingre_list = (List<Map<String, String>>) documentSnapshot.get("ingre_list");

                                Recipe_get rr = new Recipe_get(rcp_id, title, thumbnail, cook_time, ingre_list, tags);
                                rr.setRate(myIngreList);
                                rcps.add(rr);

                                adapter.notifyDataSetChanged();
                            }
                        });
                    }

                    for (String r_id : r_ids) {
                        DocumentReference docRef = db.collection("recipe").document(r_id);
                        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                String rcp_id = documentSnapshot.getData().get("id").toString();
                                String title = documentSnapshot.getData().get("name").toString();
                                String thumbnail = documentSnapshot.getData().get("thumbnail").toString();
                                String cook_time = documentSnapshot.getData().get("time").toString();
                                List<String> tags = (List<String>) documentSnapshot.get("tag");
                                List<Map<String, String>> ingre_list = (List<Map<String, String>>) documentSnapshot.get("ingre_list");
                                Recipe_get rr = new Recipe_get(rcp_id, title, thumbnail, cook_time, ingre_list, tags);
                                rr.setRate(myIngreList);
                                sorted_rcps.add(rr);
                                sorted_rcps.sort(new Comparator<Recipe_get>(){
                                    @Override
                                    public int compare(Recipe_get o1, Recipe_get o2) {
                                        if (o1.getRate() < o2.getRate()) return 1;
                                        if (o1.getRate() > o2.getRate()) return -1;
                                        return 0;
                                    }
                                });

                                adapter3.notifyDataSetChanged();
                            }
                        });
                    }
                }
            }.start();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    private void myStartActivity(Class c){
        Intent intent = new Intent(getActivity(),c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void revokeAccess() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child(uid).removeValue();

        mAuth.getCurrentUser().delete();
    }

    class ExceptionHandler implements Thread.UncaughtExceptionHandler {
        @Override
        public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {
            Intent intent = new Intent(getContext(), CrashedActivity.class);
            startActivity(intent);
//            new Thread() {
//                @Override
//                public void run() {
//                    Looper.prepare();
//                    Toast.makeText(getContext(), "Application crashed", Toast.LENGTH_LONG).show();
//                    Looper.loop();
//                }
//            }.start();
//            try {
//                Thread.sleep(4000); // Let the Toast display before app will get shutdown
//            } catch (InterruptedException ed) {// Ignored.
//            }
        }
    }

}