package com.madhu_bookmarks_manager.bookmarksmanager;

import android.graphics.drawable.Drawable;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by mbhar on 4/28/2017 at 3:16 AM.
 */

public class LinkClass {
    private String URL;
    private String ImageURL;
    private Drawable image;
    private String title;
    private Date lastViewed;
    private boolean favourite;
    private DateFormat dateFormat;

    LinkClass(String link){
        URL = link;
        ImageURL = "";
        image = Drawable.createFromPath("@drawable/ic_not_fav");
        title = "Article Title";
        dateFormat = DateFormat.getDateInstance();
        lastViewed = new Date();
        favourite = false;
    }

    public String getURL() {
        return URL;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public Drawable getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getLastViewed() {
        return dateFormat.format(lastViewed);
    }

    public boolean isFavourite() {
        return favourite;
    }
}
