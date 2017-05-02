package com.madhu_bookmarks_manager.bookmarksmanager;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mbhar on 5/1/2017 at 7:02 PM.
 */

class Singleton {
    //Firebase database objects
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mLinksEvenListener;

    //Firebase Auth objects
    private static final int RC_SIGN_IN = 1;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseUser mUser;

    /**************************
     * The king, one and only *
     **************************/
    private ArrayList<BasicLinkInfo> mAllLinks;
    private HashMap<String, String> mLinksMap;

    public ArrayList<BasicLinkInfo> getmFavouritesList() {
        return mFavouritesList;
    }

    public void setmFavouritesList(ArrayList<BasicLinkInfo> mFavouritesList) {
        this.mFavouritesList = mFavouritesList;
    }

    private ArrayList<BasicLinkInfo> mFavouritesList;
    private ArrayList<BasicLinkInfo> mYoutubeList;
    private ArrayList<BasicLinkInfo> mWikiList;
    private ArrayList<BasicLinkInfo> mStackList;

    private static final Singleton ourInstance = new Singleton();
    static Singleton getInstance() {
        return ourInstance;
    }
    private Singleton() {
        mAllLinks = new ArrayList<>();
        mLinksMap = new HashMap<>();
        mFavouritesList = new ArrayList<>();
        mYoutubeList = new ArrayList<>();
        mWikiList = new ArrayList<>();
        mStackList = new ArrayList<>();
    }

    FirebaseDatabase getmFirebaseDatabase() {
        return mFirebaseDatabase;
    }

    void setmFirebaseDatabase(FirebaseDatabase mFirebaseDatabase) {
        this.mFirebaseDatabase = mFirebaseDatabase;
    }

    DatabaseReference getmDatabaseReference() {
        return mDatabaseReference;
    }

    void setmDatabaseReference(DatabaseReference mDatabaseReference) {
        this.mDatabaseReference = mDatabaseReference;
    }

    ChildEventListener getmLinksEvenListener() {
        return mLinksEvenListener;
    }

    void setmLinksEvenListener(ChildEventListener mLinksEvenListener) {
        this.mLinksEvenListener = mLinksEvenListener;
    }

    static int getRcSignIn() {
        return RC_SIGN_IN;
    }

    FirebaseAuth getmFirebaseAuth() {
        return mFirebaseAuth;
    }

    void setmFirebaseAuth(FirebaseAuth mFirebaseAuth) {
        this.mFirebaseAuth = mFirebaseAuth;
    }

    FirebaseAuth.AuthStateListener getmAuthStateListener() {
        return mAuthStateListener;
    }

    void setmAuthStateListener(FirebaseAuth.AuthStateListener mAuthStateListener) {
        this.mAuthStateListener = mAuthStateListener;
    }

    FirebaseUser getmUser() {
        return mUser;
    }

    void setmUser(FirebaseUser mUser) {
        this.mUser = mUser;
    }

    public static Singleton getOurInstance() {
        return ourInstance;
    }

    ArrayList<BasicLinkInfo> getmAllLinks() {
        return mAllLinks;
    }

    public void setmAllLinks(ArrayList<BasicLinkInfo> mAllLinks) {
        this.mAllLinks = mAllLinks;
    }

    void addToAllLinks(BasicLinkInfo linkInfo){
        this.mAllLinks.add(linkInfo);
        mLinksMap.put(linkInfo.getmUrl(), linkInfo.getPushKey());
        if(linkInfo.isFavourite()){
            mFavouritesList.add(linkInfo);
        }
    }

    void removeFromAllLinks(int position){
        String key = this.mAllLinks.get(position).getmUrl();
        String pushString = this.mLinksMap.get(key);
        mDatabaseReference.child(pushString).removeValue();
        mLinksMap.remove(key);
        this.mAllLinks.remove(position);
    }

    void modifyFavouritesToItem(int position, boolean state, Context t){
        BasicLinkInfo pLink = mAllLinks.get(position);
        String key = this.mAllLinks.get(position).getmUrl();
        String pushString = this.mLinksMap.get(key);
        pLink.setFavourite(state);
        mDatabaseReference.child(pushString).setValue(mAllLinks.get(position));
        if(state){
            if(!mFavouritesList.contains(pLink))
                mFavouritesList.add(pLink);
        }
        else {
            mFavouritesList.remove(pLink);
        }
        Toast.makeText(t, ""+mFavouritesList.size(), Toast.LENGTH_LONG).show();
    }

    HashMap<String, String> getmLinksMap() {
        return mLinksMap;
    }
}
