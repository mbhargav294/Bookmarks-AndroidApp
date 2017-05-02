package com.madhu_bookmarks_manager.bookmarksmanager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;

public class LinkView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_view);

        SharedPreferences linkData = getSharedPreferences(Constants.PREFS_NAME, 0);
        boolean isFav = linkData.getBoolean("silentMode", false);
        String Title = linkData.getString("title", "Invalid");
        String Text = linkData.getString("text", "404 Not Found");
        //int pos = linkData.getInt("pos", 0);

        //ImageView favicon = (ImageView) this.findViewById(R.id.fab);

        this.setTitle(Title);
        WebView webText = (WebView) this.findViewById(R.id.web_text);
        webText.loadData(Text, "text/html; charset=utf-8", "UTF-8");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
}