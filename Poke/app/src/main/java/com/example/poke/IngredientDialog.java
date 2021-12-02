package com.example.poke;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class IngredientDialog extends DialogFragment implements AdapterView.OnItemSelectedListener {
    private Fragment fragment;
    private DatabaseReference mDatabase;
    String uid;

    private Button okBtn;
    private EditText nameText;
    //private EditText cateText;
    private EditText dateText;
    private String title;
    private String cate;
    private String date;
    private String key;
    private Button cancelBtn;
    private ImageView barcode_btn;
    Spinner spinner;
    String[] ingre_item;
    private ImageView calendar_btn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ingre_popup,container,false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        view.setClipToOutline(true);
        okBtn = view.findViewById(R.id.dialogOkBtn);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        if(user != null)
        uid = user.getUid();
        okBtn.setOnClickListener(onClickListener);
        spinner = view.findViewById(R.id.ingre_spinner);
        nameText = view.findViewById(R.id.prod_name_txt);
        dateText = view.findViewById(R.id.daycnt_txt);
        barcode_btn = view.findViewById(R.id.barcodeButton);
        barcode_btn.setOnClickListener(scanClickListener);
        cancelBtn = view.findViewById(R.id.dialogCancelBtn);
        cancelBtn.setOnClickListener(onClickListener);
        registerForContextMenu(nameText);
        calendar_btn = view.findViewById(R.id.ingreCalendarButton);
        calendar_btn.setOnClickListener(calendarListener);
        dateText.setOnClickListener(calendarListener);
        dateText.setFocusableInTouchMode(false);
        nameText.setImeOptions(EditorInfo.IME_ACTION_DONE);

        spinner.setOnItemSelectedListener(this);
        ingre_item = new String[]{"두류","견과류","채소류","과일류","육류","알류"
        ,"어패류","해조류","유제품","음료/주류","기타"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, ingre_item);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(adapter);

        fragment = getActivity().getSupportFragmentManager().findFragmentByTag("tag");
        setHasOptionsMenu(true);
        return view;
    }

    //@Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long L){
        cate=ingre_item[i];
    }

    //@Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        cate="";
    }

    View.OnClickListener calendarListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    dateText.setText(String.format("%d-%02d-%02d",year,month+1,dayOfMonth));
                }
            },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DATE));

            Calendar min = Calendar.getInstance();
            min.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DATE));
            datePickerDialog.getDatePicker().setMinDate(min.getTime().getTime());

            datePickerDialog.show();
        }
    };

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.dialogOkBtn){
                title = nameText.getText().toString();
                //cate = cateText.getText().toString();
                date = dateText.getText().toString();
                UserIngredient userIngredient;

                if(title.length() >=1 && cate.length() >=2 && date.length()>=9){
                    if(key != null){
                        userIngredient = new UserIngredient(title, date, cate);
                        mDatabase.child("ingredient").child(uid).child(key).setValue(userIngredient);
                    }
                    else{
                        userIngredient = new UserIngredient(title, date, cate);
                        mDatabase.child("ingredient").child(uid).push().setValue(userIngredient);
                    }
                    if (fragment != null) {
                        DialogFragment dialogFragment = (DialogFragment) fragment;
                        dialogFragment.dismiss();
                    }
                }
                else{
                    startToast("다시 입력해주세요");
                }
            }
            else if(v.getId() == R.id.dialogCancelBtn){
                DialogFragment dialogFragment = (DialogFragment) fragment;
                dialogFragment.dismiss();
            }

        }
    };

    View.OnClickListener scanClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            IntentIntegrator.forSupportFragment(IngredientDialog.this).initiateScan();
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            String barcode_num = result.getContents();
            if(barcode_num == null) {
                Toast.makeText(getContext(), "취소됨",Toast.LENGTH_LONG).show();
            } else {

                BarcodeApiCaller barcodeApiCaller = new BarcodeApiCaller();
                barcodeApiCaller.getXmlData(barcode_num);
                String p_name = barcodeApiCaller.getP_name();
                String p_cate = barcodeApiCaller.getP_cate();
                String p_date = barcodeApiCaller.getP_date();

                if(p_name == null || p_cate == null || p_date == null) {
                    Toast.makeText(getContext(), "읽을 수 없습니다.\n다시 시도하거나 수동 입력해주세요.",Toast.LENGTH_LONG).show();
                }
                nameText.setText(p_cate);
                //cateText.setText(cate);
                dateText.setText(p_date);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void startToast(String msg){
        Toast.makeText(getActivity(), msg,Toast.LENGTH_SHORT).show();
    }
}
