package com.example.poke;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MemberInitActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;

    private static final String Tag = "UserInitActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_init);

        findViewById(R.id.checkButton).setOnClickListener(onClickListener);
    }

    //뒤로가기 시 종료
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
        finish();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    View.OnClickListener onClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.checkButton:
                    profileUpdate();
                    break;
            }
        }
    };

    private void profileUpdate(){
        String name=((EditText)findViewById(R.id.nameEditText)).getText().toString();
        String age=((EditText)findViewById(R.id.ageEditText)).getText().toString();
        String birthDay=((EditText)findViewById(R.id.birthDayEditText)).getText().toString();
        String gender=((EditText)findViewById(R.id.genderEditText)).getText().toString();

        if(name.length() > 0 && age.length() > 0 && birthDay.length() > 5 && gender.length() > 0) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            mDatabase = FirebaseDatabase.getInstance().getReference();
            String uid = user.getUid();
            UserInfo userInfo = new UserInfo(name, age, birthDay, gender);
            if(user != null){
                //mDatabase.child("users").child(uid).push();
                mDatabase.child("users").child(uid).setValue(userInfo)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                startToast("회원정보 등록을 성공하였습니다.");
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                startToast("회원정보 등록에 실패하였습니다.");
                            }
                        });
            }



        } else{
            startToast("회원정보를 입력해주세요.");
        }
    }

    private void startToast(String msg){
        Toast.makeText(this, msg,Toast.LENGTH_SHORT).show();
    }



}
