package com.example.poke;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
public class SearchAdapter extends RecyclerView.Adapter<SearchViewHolder> implements Filterable {

    ArrayList<Recipe_get> unFilteredList;
    ArrayList<Recipe_get> filteredList;
    private final ArrayList<Recipe_get> searchList;

    public SearchAdapter(ArrayList<Recipe_get> search_list) {

        this.searchList = search_list;
        this.unFilteredList = search_list;
        this.filteredList = search_list;
    }

    Context context;

    @Override
    public void onAttachedToRecyclerView(@NonNull @NotNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        context = recyclerView.getContext();
    }

    @NonNull
    @NotNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_list,parent,false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SearchViewHolder holder, int position) {
        if (searchList != null && position < searchList.size()) {
            Recipe_get rcp = searchList.get(position);
            holder.search_title.setText(rcp.getName());
            Glide.with(holder.itemView)
                    .load(rcp.getThumbnail())
                    .into(holder.search_thumbnail);
        }
    }


    @Override
    public int getItemCount() {
        return (searchList != null ? searchList.size() : 0);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if(charString.isEmpty()) {
                    filteredList = unFilteredList;
                } else {
                    ArrayList<Recipe_get> filteringList = new ArrayList<>();
                    for(Recipe_get name : unFilteredList) {
                       // if(name.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteringList.add(name);
                       // }
                    }
                    filteredList = filteringList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredList = (ArrayList<Recipe_get>)results.values;
                notifyDataSetChanged();
            }
        };
    }
}