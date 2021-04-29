//package com.example.poke;
//
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.HashMap;
//
//public class db extends AppCompatActivity {
//
//    EditText et_user_name, et_user_email;
//    Button btn;
//    private DatabaseReference mDatabase;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.db);
//
//        et_user_name = findViewById(R.id.et_user_name);
//        et_user_email = findViewById(R.id.et_user_email);
//        btn = findViewById(R.id.btn);
//
//        mDatabase = FirebaseDatabase.getInstance().getReference();
//
//        readUser();
//
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String getUserName = et_user_name.getText().toString();
//                String getUserEmail = et_user_email.getText().toString();
//
//                HashMap result = new HashMap<>();
//                result.put("name", getUserName);
//                result.put("email", getUserEmail);
//
//                writeNewUser("1", getUserName, getUserEmail);
//            }
//        });
//
//
//    private void writeNewUser(String userId, String name, String email) {
//        UserInfo user = new UserInfo(name, email);
//
//        mDatabase.child("users").child(userId).setValue(user)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Toast.makeText(com.example.poke.db.this,
//                                "저장 완료", Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(com.example.poke.db.this,
//                                "저장 실패", Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//    }
//
//    private void readUser() {
//        mDatabase.child("users").child("1").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.getValue(UserInfo.class) != null) {
//                    UserInfo post = snapshot.getValue(UserInfo.class);
//                    Log.w("FireBaseData", "getData" + post.toString());
//                } else {
//                    Toast.makeText(com.example.poke.db.this,
//                            "데이터없음", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.w("FireBaseData", "loadPost:OnCancelled", error.toException());
//            }
//        });
//    }
//}
//
