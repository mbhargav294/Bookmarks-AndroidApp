package com.madhu_bookmarks_manager.bookmarksmanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

public class FavouritesView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites_view);

        this.setTitle("Favourites");
        Singleton singleton = Singleton.getInstance();
        ListView listView = (ListView) findViewById(R.id.list_item_favs);
        BasicLinkAdapter mBasicLinkAdapter = new BasicLinkAdapter(this, R.layout.link_card_large, singleton.getmFavouritesList());
        listView.setAdapter(mBasicLinkAdapter);


    }
}
