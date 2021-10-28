package com.example.poke;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MemberInitActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private RadioButton mBtn;
    private RadioButton wBtn;
    private Boolean bool;
    private static final String Tag = "UserInitActivity";
    private ImageButton calendar_btn;
    private EditText birthET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_init);

        mBtn = findViewById(R.id.manButton);
        wBtn = findViewById(R.id.womanButton);
        calendar_btn = findViewById(R.id.initCalendarButton);
        calendar_btn.setOnClickListener(calendarListener);
        birthET = findViewById(R.id.birthDayEditText);
        birthET.setFocusableInTouchMode(false);
        mBtn.setChecked(true);
        bool = false;

        mBtn.setOnClickListener(v -> bool = false);

        wBtn.setOnClickListener(v -> bool = true);

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

    View.OnClickListener onClickListener = v -> {
        if (v.getId() == R.id.checkButton) {
            profileUpdate();
        }
    };

    View.OnClickListener calendarListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(MemberInitActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    birthET.setText(String.format("%d-%02d-%02d",year,month+1,dayOfMonth));
                }
            },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DATE));
            datePickerDialog.show();
        }
    };

    private void profileUpdate(){
        String nickName=((EditText)findViewById(R.id.nickNameEditText)).getText().toString();
        String birthDay=((EditText)findViewById(R.id.birthDayEditText)).getText().toString();
        int birthage=Integer.parseInt(birthDay.substring(2,3));
        String age= Integer.toString((birthage > 21 ? (22+100-birthage):(22-birthage)));

        String gender;
        if(bool)
            gender = "man";
        else
            gender = "woman";

        if(nickName.length() > 1 && age.length() > 0 && birthDay.length() > 5 ) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            mDatabase = FirebaseDatabase.getInstance().getReference();
            String uid = user.getUid();
            UserInfo userInfo = new UserInfo(nickName, age, birthDay, gender);
            if(user != null){
                mDatabase.child("users").child(uid).setValue(userInfo)
                        .addOnSuccessListener(aVoid -> {
                            startToast("회원정보 등록을 성공하였습니다.");
                            myStartActivity(PreferenceActivity.class);
                        })
                        .addOnFailureListener(e -> startToast("회원정보 등록에 실패하였습니다."));
            }
        } else{
            startToast("회원정보를 입력해주세요.");
        }
    }

    private void myStartActivity(Class c){
        Intent intent = new Intent(this,c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void startToast(String msg){
        Toast.makeText(this, msg,Toast.LENGTH_SHORT).show();
    }
}
