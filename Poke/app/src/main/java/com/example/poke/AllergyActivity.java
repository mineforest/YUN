package com.example.poke;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AllergyActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allergy);

        findViewById(R.id.menuButton1).setOnClickListener(onClickListener);
        findViewById(R.id.menuButton2).setOnClickListener(onClickListener);
        findViewById(R.id.menuButton3).setOnClickListener(onClickListener);
    }
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.menuButton1:
                    addAllergy(R.id.menuButton1);
                    break;
                case R.id.menuButton2:
                    addAllergy(R.id.menuButton2);
                    break;
                case R.id.menuButton3:
                    addAllergy(R.id.menuButton3);
                    break;
            }
        }
    };

        public void addAllergy(int btn){
            String allergy = ((Button)findViewById(btn)).getText().toString();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            mDatabase = FirebaseDatabase.getInstance().getReference();
            String uid = user.getUid();
            UserAllergy userAllergy = new UserAllergy(allergy);

            mDatabase.child("allergy").child(uid).push().setValue(userAllergy)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            startToast("등록을 성공하였습니다.");
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            startToast("등록에 실패하였습니다.");
                        }
                    });
        }

    private void startToast(String msg){
        Toast.makeText(this, msg,Toast.LENGTH_SHORT).show();
    }
}
