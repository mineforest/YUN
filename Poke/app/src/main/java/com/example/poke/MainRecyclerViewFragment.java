package com.example.poke;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainRecyclerViewFragment extends Fragment {
    ArrayList<Recipe_get> rcps = new ArrayList<>();
    CustomAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        View view = inflater.inflate(R.layout.home_recyler_test, container, false);

        //테스트용 레시피 id들
        String[] test_ids = {"1762278", "1762498","1894779", "1899131", "1978049", "2001746",
                "2017354", "2442087", "2528933", "2442087", "2803587", "3568149",};
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        for(int i =0;i<test_ids.length; i++){
            DocumentReference docRef = db.collection("recipe").document(test_ids[i]);
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    String rcp_id = documentSnapshot.getData().get("id").toString();
                    String title = documentSnapshot.getData().get("name").toString();
                    String thumbnail = documentSnapshot.getData().get("thumbnail").toString();
                    String cook_time = documentSnapshot.getData().get("time").toString();
                    Recipe_get r = new Recipe_get(rcp_id, title, thumbnail, cook_time);
                    rcps.add(r);
                    adapter.notifyDataSetChanged();
                }
            });
        }
        RecyclerView recyclerView = view.findViewById(R.id.main_recylerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, RecyclerView.VERTICAL, false));
        adapter = new CustomAdapter(rcps);
        recyclerView.setAdapter(adapter);
        int largePadding = getResources().getDimensionPixelSize(R.dimen.shr_product_grid_spacing);
        int smallPadding = getResources().getDimensionPixelSize(R.dimen.shr_product_grid_spacing_small);
        recyclerView.addItemDecoration(new MainGridItemDecoration(largePadding, smallPadding));

        return view;
    }
}
