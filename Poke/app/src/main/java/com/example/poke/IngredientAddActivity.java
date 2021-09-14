package com.example.poke;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.poke.R;
import com.example.poke.UserIngredient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class IngredientAddActivity extends AppCompatActivity {
    private Button btn;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_ingredient);

        btn = findViewById(R.id.ingreBtn);
        btn.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ingredientAdd();
            onBackPressed();
        }
    };

    public void ingredientAdd(){
        String title =((TextView)findViewById(R.id.ingreTitle)).getText().toString();
        String cate =((TextView)findViewById(R.id.ingreCate)).getText().toString();
        String day =((TextView)findViewById(R.id.ingreD)).getText().toString();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        String uid = user.getUid();

        mDatabase.child("ingredient").child(uid).push().setValue(new UserIngredient(title, day, cate));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
