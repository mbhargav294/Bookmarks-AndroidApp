package com.madhu_bookmarks_manager.bookmarksmanager;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by mbhar on 4/28/2017 at 1:02 PM.
 */

public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView titleTextView;
    public TextView dateTextView;
    public ImageView mIconView;

    public MyViewHolder(View v){
        super(v);
        titleTextView = (TextView) v.findViewById(R.id.link_title);
        dateTextView = (TextView) v.findViewById(R.id.link_last_viewed);
        mIconView = (ImageView) v.findViewById(R.id.favicon);
    }

    @Override
    public void onClick(View v) {
        /*switch (v.getId()){
            case R.id.favicon:
                mIconView.setImageResource(R.drawable.ic_fav);
        }*/
    }
}