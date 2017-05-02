package com.madhu_bookmarks_manager.bookmarksmanager;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mbhar on 4/29/2017 at 9:37 PM.
 */

public class BasicLinkInfo {
    private String mUrl;
    private boolean favourite;
    private String category;
    private String mTitle;
    private String mImage;
    private String mLogo;
    private int mColor;
    private boolean mDone;
    private String pushKey;

    BasicLinkInfo(){
        mUrl="";
        favourite = false;
        category = "";
        mTitle = "";
        mImage = "";
    }

    BasicLinkInfo(String url, String title, String logo){
        mUrl = url;
        favourite = false;
        mTitle = title;
        category = "Other";
        mLogo = logo;
        mDone = false;
        boolean foundImage = false;
        if(url.contains("youtu")){
            String pattern = "(?<=watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";

            Pattern compiledPattern = Pattern.compile(pattern);
            Matcher matcher = compiledPattern.matcher(url);

            if(matcher.find()){
                mImage = "https://img.youtube.com/vi/" + matcher.group() + "/0.jpg";
                foundImage = true;
            }
            else{
                pattern = "youtu\\.be\\/([a-zA-Z0-9_-]+)$";

                compiledPattern = Pattern.compile(pattern);
                matcher = compiledPattern.matcher(url);
                if(matcher.find()){
                    mImage = "https://img.youtube.com/vi/" + matcher.group(1) + "/0.jpg";
                    foundImage = true;
                }
            }
            category = "Youtube";
            mLogo = Constants.YOUTUBE_LOGO;
        }
        else if(url.contains("wiki")){
            mLogo = Constants.WIKI_LOGO;
            category = "Wiki";
        }
        else if(url.contains("stackoverflow") || url.contains("stackexchange"))
        {
            mLogo = Constants.STACK_LOGO;
            category = "Stack";
        }

        if(!foundImage){
            Random rnd = new Random();
            mColor = rnd.nextInt(5);
        }
    }

    @Override
    public boolean equals(Object obj) {
        return this.getmUrl() == ((BasicLinkInfo) obj).getmUrl();
    }

    public String getmLogo() {
        return mLogo;
    }

    public void setmLogo(String mLogo) {
        this.mLogo = mLogo;
    }

    public String getmUrl() {
        return mUrl;
    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmImage() {
        return mImage;
    }

    public void setmImage(String mImage) {
        this.mImage = mImage;
    }

    public int getmColor() {
        return mColor;
    }

    public void setmColor(int mColor) {
        this.mColor = mColor;
    }

    public boolean ismDone() {
        return mDone;
    }

    public void setmDone(boolean mDone) {
        this.mDone = mDone;
    }

    public String getPushKey() {
        return pushKey;
    }

    public void setPushKey(String pushKey) {
        this.pushKey = pushKey;
    }
}
