package com.example.poke;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
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
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainRecyclerViewFragment extends Fragment{
    ArrayList<Recipe_get> first_rcps = new ArrayList<>();
    ArrayList<Recipe_get> rcps = new ArrayList<>();
    ArrayList<Recipe_get> sorted_rcps = new ArrayList<>();
    ArrayList<String> myIngreList;
    CustomAdapter adapter;
    CustomAdapter adapter3;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String uid;
    FirebaseUser user;
    ProgressDialog progressDialog;
    Intent intent;
    ShimmerFrameLayout shimmerFrameLayout;
    RecyclerView recyclerView;
    RecyclerView recyclerView2;
    SwipeRefreshLayout swipeRefreshLayout;

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

        swipeRefreshLayout = view.findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setOnRefreshListener(() -> view.postDelayed(() -> {
            reload();
            swipeRefreshLayout.setRefreshing(false);
        },500));

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
                adapter3.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mDatabase.onDisconnect();

        shimmerFrameLayout = view.findViewById(R.id.sfl);
        shimmerFrameLayout.startShimmer();

        recyclerView = view.findViewById(R.id.main_recylerView);
        recyclerView.setVisibility(View.INVISIBLE);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1, RecyclerView.VERTICAL, false));
        adapter = new CustomAdapter(rcps);
        recyclerView.setAdapter(adapter);
        int largePadding = getResources().getDimensionPixelSize(R.dimen.shr_product_grid_spacing);
        int smallPadding = getResources().getDimensionPixelSize(R.dimen.shr_product_grid_spacing_small);
        recyclerView.addItemDecoration(new MainGridItemDecoration(largePadding, smallPadding));

        recyclerView2 = view.findViewById(R.id.main_recylerView2);
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setLayoutManager(new GridLayoutManager(getContext(), 1, RecyclerView.VERTICAL, false));
        adapter3 = new CustomAdapter(sorted_rcps);
        recyclerView2.setAdapter(adapter3);
        recyclerView2.addItemDecoration(new MainGridItemDecoration(largePadding, smallPadding));

        TextView tv_foryou = view.findViewById(R.id.tv_foryou);
        TextView tv_donow = view.findViewById(R.id.tv_donow);

        tv_foryou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(v.getContext(), MainMoreViewActivity.class);
                intent.putExtra("rcp",rcps);
                intent.putExtra("more_title", tv_foryou.getText());
                v.getContext().startActivity(intent);
            }
        });

        tv_donow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(v.getContext(), MainMoreViewActivity.class);
                intent.putExtra("rcp",sorted_rcps);
                intent.putExtra("more_title", tv_donow.getText());
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
            W2vHttpConn w2v = new W2vHttpConn();

            new Thread(){
                @Override
                public void run() {
                    first_rcps = w2v.getRcp(uid, 1);
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    for (Recipe_get recipe_get : first_rcps) {
                        String r_id = recipe_get.getId();
                        DocumentReference docRef = db.collection("recipe").document(r_id);
                        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                recipe_get.setName(documentSnapshot.getData().get("name").toString());
                                recipe_get.setThumbnail(documentSnapshot.getData().get("thumbnail").toString());
                                recipe_get.setTime(documentSnapshot.getData().get("time").toString());
                                recipe_get.setIngre_list((List<Map<String, String>>) documentSnapshot.get("ingre_list"));
                                recipe_get.setRate(myIngreList);
                                rcps.add(recipe_get);
                                sorted_rcps.add(recipe_get);

                                if(rcps.size() == first_rcps.size()) {
                                    rcps.sort((o1, o2) -> {
                                        int sc1 = Integer.parseInt(o1.getScore());
                                        int sc2 = Integer.parseInt(o2.getScore());
                                        return Integer.compare(sc2, sc1);
                                    });
                                    shimmerFrameLayout.stopShimmer();
                                    shimmerFrameLayout.setVisibility(View.GONE);
                                    adapter.notifyDataSetChanged();
                                    recyclerView.setVisibility(View.VISIBLE);
                                }

                                if (sorted_rcps.size() == first_rcps.size()) {
                                    sorted_rcps.sort((o1, o2) -> o2.getRate().compareTo(o1.getRate()));
                                    adapter3.notifyDataSetChanged();
                                }
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

    private void reload() {
        Intent intent = getActivity().getIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_NO_ANIMATION);
        getActivity().overridePendingTransition(0,0);
        getActivity().finish();
        getActivity().overridePendingTransition(0,0);
        startActivity(intent);
    }

    class ExceptionHandler implements Thread.UncaughtExceptionHandler {
        @Override
        public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {
            Intent intent = new Intent(getContext(), CrashedActivity.class);
            startActivity(intent);
        }
    }

}