package com.madhu_bookmarks_manager.bookmarksmanager;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by mbhar on 4/28/2017 at 1:07 PM.
 */

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private ArrayList<LinkClass> links;
    public MyAdapter(ArrayList<LinkClass> Data) {
        links = Data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.link_card_small, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.titleTextView.setText(links.get(position).getTitle());
        holder.dateTextView.setText(links.get(position).getLastViewed());
    }

    @Override
    public int getItemCount() {
        return links.size();
    }


}
