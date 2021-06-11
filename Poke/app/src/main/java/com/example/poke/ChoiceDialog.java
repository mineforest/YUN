//package com.example.poke;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.DialogFragment;
//
//public class ChoiceDialog extends DialogFragment {
//    private Button add;
//    private Button barcode;
//
//    public ChoiceDialog(){
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.ingre_add_popup, container, false);
//        Bundle args = getArguments();
//        add = view.findViewById(R.id.addPop);
//        barcode = view.findViewById(R.id.barcode_scan_Button);
//
//        add.setOnClickListener(onClickListener);
//
//        return view;
//    }
//
//    View.OnClickListener onClickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            if(v.getId() == R.id.addPop){
//                Bundle args = new Bundle();
//                IngredientDialog dialog = new IngredientDialog();
//                dialog.setArguments(args); // 데이터 전달
//                dialog.show(getActivity().getSupportFragmentManager(),"tag");
//            }
//
//        }
//    };
//}
