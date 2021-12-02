package com.example.poke;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.poke.R;

import java.util.ArrayList;
import java.util.List;

public class IngredientListAdapter extends BaseAdapter {
    private final Context context;
    private final List<String> list;
    private final LayoutInflater inflate;
    private ViewHolder viewHolder;
    private int pos;

    public IngredientListAdapter(List<String> list, Context context){
        this.list = list;
        this.context = context;
        this.inflate = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
         pos = position;

        if(convertView == null){
            convertView = inflate.inflate(R.layout.ingre_search_listview,null);
            viewHolder = new ViewHolder();
            viewHolder.label = convertView.findViewById(R.id.label);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

//        viewHolder.label.setOnClickListener(onClickListener);

        // 리스트에 있는 데이터를 리스트뷰 셀에 뿌린다.
        viewHolder.label.setText(list.get(position));
        viewHolder.label.bringToFront();
        return convertView;
    }


    class ViewHolder{
        public TextView label;
    }



    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            list.get(pos);
        }
    };
}
