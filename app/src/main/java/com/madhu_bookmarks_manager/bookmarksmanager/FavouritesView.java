package com.madhu_bookmarks_manager.bookmarksmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

public class FavouritesView extends AppCompatActivity {

    ArrayList<BasicLinkInfo> fav;
    ArrayList<BasicLinkInfo> yt;
    ArrayList<BasicLinkInfo> wiki;
    ArrayList<BasicLinkInfo> stack;
    SubtaskAdapter mBasicLinkAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites_view);

        Intent intent = getIntent();
        String title = intent.getStringExtra("Name");
        this.setTitle(title);

        fav = Singleton.getInstance().getmFavouritesList();
        yt = Singleton.getInstance().getmYoutubeList();
        wiki = Singleton.getInstance().getmWikiList();
        stack = Singleton.getInstance().getmStackList();
        Toast.makeText(this, ""+stack.size(), Toast.LENGTH_LONG).show();


        GridView gridView = (GridView) findViewById(R.id.list_item_favs);
        if(title.equals("Favourites")) {
            mBasicLinkAdapter = new SubtaskAdapter(this, R.layout.link_card_small, fav);
        } else if(title.equals("YouTube")) {
            mBasicLinkAdapter = new SubtaskAdapter(this, R.layout.link_card_small, yt);
        } else if(title.equals("Wikipedia")) {
            mBasicLinkAdapter = new SubtaskAdapter(this, R.layout.link_card_small, wiki);
        } else if(title.equals("StackExchange")) {
            mBasicLinkAdapter = new SubtaskAdapter(this, R.layout.link_card_small, stack);
        }
        mBasicLinkAdapter.notifyDataSetChanged();
        gridView.setAdapter(mBasicLinkAdapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        fav.clear();
        yt.clear();
        wiki.clear();
        stack.clear();
        mBasicLinkAdapter.notifyDataSetChanged();
        finish();
    }
}
