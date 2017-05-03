package com.madhu_bookmarks_manager.bookmarksmanager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;


public class HomePage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    //All view objects
    private TextView mUserNameView;
    private TextView mEmailView;
    private CircleImageView mUserImageView;
    private ListView listView;
    private EditText mEditText;
    private ImageView mAddLinkButton;
    private Vibrator myVib;
    private InputMethodManager imm;
    private ProgressBar mProgressBar;

    //All custom variables
    private static final String ANONYMOUS = "Anonymous";
    private String mUserName;
    private String mEmail;
    private Uri mImageUri;
    private String newLink;
    private BasicLinkAdapter mBasicLinkAdapter;

    private Singleton singleton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        singleton = Singleton.getInstance();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        mProgressBar = (ProgressBar) findViewById(R.id.main_progress);
        mProgressBar.setVisibility(ProgressBar.GONE);

        listView = (ListView) findViewById(R.id.list_item);
        mBasicLinkAdapter = new BasicLinkAdapter(this, R.layout.link_card_large, singleton.getmAllLinks());
        listView.setAdapter(mBasicLinkAdapter);


        singleton.setmFirebaseAuth(FirebaseAuth.getInstance());
        singleton.setmFirebaseDatabase(FirebaseDatabase.getInstance());

        /************************
         * Block for User Login *
         ************************/
        singleton.setmAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                singleton.setmUser(firebaseAuth.getCurrentUser());
                if(singleton.getmUser() != null) {
                    Intent intent = getIntent();
                    String action = intent.getAction();
                    String type = intent.getType();

                    if (Intent.ACTION_SEND.equals(action) && type != null) {
                        if ("text/plain".equals(type)) {
                            try {
                                parseLinks(intent.toString()); // Handle text being sent
                            } catch (IOException e) {
                                Toast.makeText(HomePage.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                    mProgressBar.setVisibility(ProgressBar.VISIBLE);
                    singleton.setmDatabaseReference(singleton.getmFirebaseDatabase()
                            .getReference()
                            .child("users")
                            .child(singleton.getmUser().getUid()));
                    OnSignedInInitialize(singleton.getmUser());
                    mProgressBar.setVisibility(ProgressBar.GONE);
                }
                else{
                    //User is signed out
                    OnSignedOutCleanUp();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setProviders(Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                            new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                                    .setTheme(R.style.AppTheme)
                                    .build(),
                            Singleton.getRcSignIn());
                }
            }
        });


        /*************************************
         * DEFAULT CODE START                *
         ************************************/
        this.setTitle("Homepage");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        /************************************
         * INITIALIZATION OF VIEWS          *
         ************************************/
        View headerLayout = navigationView.getHeaderView(0);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        myVib = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
        mUserNameView = (TextView) headerLayout.findViewById(R.id.user_name_view);
        mEmailView = (TextView) headerLayout.findViewById(R.id.user_email_view);
        mUserImageView = (CircleImageView) headerLayout.findViewById(R.id.user_image_view);
        mEditText = (EditText) findViewById(R.id.edit_text_link);
        mAddLinkButton = (ImageView) findViewById(R.id.add_link_btn);


        /************************************
         * Adding a listener to the EditText*
         ************************************/
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().length() > 0){
                    mAddLinkButton.setBackground(getResources().getDrawable(R.drawable.ic_add_enabled));
                    mAddLinkButton.setEnabled(true);
                }
                else{
                    mAddLinkButton.setBackground(getResources().getDrawable(R.drawable.ic_add_enabled));
                    mAddLinkButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        /************************************
         *Adding a new link to database     *
         ************************************/
        mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(400)});
        mAddLinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAddLinkButton.isEnabled()) {
                    newLink = mEditText.getText().toString().trim();

                    if(!newLink.startsWith("http"))
                        newLink = "https://" + newLink;

                    if(singleton.getmLinksMap().containsKey(newLink)){
                        AlertDialog.Builder adb=new AlertDialog.Builder(HomePage.this);
                        adb.setTitle("Duplicate found!");
                        adb.setMessage("Link already exists in you list");
                        adb.setNegativeButton("OK", null);
                        mEditText.setText("");
                        adb.show();
                    } else {
                        try {
                            mEditText.clearFocus();
                            imm.toggleSoftInput(InputMethodManager.RESULT_HIDDEN, 0);
                            mEditText.setText("");
                            parseLinks(newLink);
                        } catch (Exception e) {
                            Toast.makeText(HomePage.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            mEditText.requestFocus();
                            myVib.vibrate(50);
                        }
                    }
                }
            }
        });

    }

    private void parseLinks(String newLink) throws IOException {
        Log.v("HELLO FROM OTHER SIDE", newLink);
        Document d = Jsoup.connect(newLink).get();

        Element e1 = d.head().select("link[href~=.*\\.(ico|png)]").first();
        String logoURL = e1.attr("href");
        if (logoURL == null || logoURL.length() == 0) {
            Element e2 = d.head().select("meta[itemprop=image]").first();
            logoURL = e2.attr("itemprop");
        }

        BasicLinkInfo bLink = new BasicLinkInfo(newLink, d.title(), logoURL);
        String key = singleton.getmDatabaseReference().push().getKey();
        bLink.setPushKey(key);
        singleton.getmDatabaseReference().child(key).setValue(bLink);
    }

    private void attachDatabaseReadListener(){
        if(singleton.getmLinksEvenListener() == null) {
            singleton.setmLinksEvenListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    BasicLinkInfo linkInfo = dataSnapshot.getValue(BasicLinkInfo.class);
                    singleton.addToAllLinks(linkInfo);
                    mBasicLinkAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    mBasicLinkAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    BasicLinkInfo removedLink = dataSnapshot.getValue(BasicLinkInfo.class);
                    mBasicLinkAdapter.remove(removedLink);
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
            singleton.getmDatabaseReference().addChildEventListener(singleton.getmLinksEvenListener());
        }
    }

    private void  detachDatabaseReadListener(){
        if(singleton.getmLinksEvenListener() != null) {
            singleton.getmDatabaseReference().removeEventListener(singleton.getmLinksEvenListener());
            singleton.setmLinksEvenListener(null);
        }
    }

    private void OnSignedInInitialize(FirebaseUser user){
        mUserName = user.getDisplayName();
        mEmail = user.getEmail();
        mImageUri = user.getPhotoUrl();
        attachDatabaseReadListener();
        if(mUserNameView!=null && mUserImageView != null && mEmailView != null) {
            mUserNameView.setText(mUserName);
            mEmailView.setText(mEmail);
            Glide.with(HomePage.this).load(mImageUri).asBitmap().into(mUserImageView);
        }
    }

    private void OnSignedOutCleanUp(){
        mUserName = ANONYMOUS;
        mEmail = ANONYMOUS;
        mImageUri = null;
        mBasicLinkAdapter.clear();
        detachDatabaseReadListener();
    }

    @Override
    protected void onPause() {
        super.onPause();
        singleton.getmFirebaseAuth().removeAuthStateListener(singleton.getmAuthStateListener());
        detachDatabaseReadListener();
        mBasicLinkAdapter.clear();
    }


    @Override
    protected void onResume() {
        super.onResume();
        singleton.getmFirebaseAuth().addAuthStateListener(singleton.getmAuthStateListener());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Singleton.getRcSignIn()){
            if(resultCode == RESULT_OK){
                //Came out of sign in screen
            }
            else if (resultCode == RESULT_CANCELED){
                Toast.makeText(HomePage.this, "Signin Cancled", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    /************************************
     *  UI Functions starts from here   *
     ************************************/

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent = null;
        if (id == R.id.nav_home) {

        } else if (id == R.id.nav_youtube) {
            intent = new Intent(HomePage.this, FavouritesView.class);
            intent.putExtra("Name", "YouTube");
        } else if (id == R.id.nav_wiki) {
            intent = new Intent(HomePage.this, FavouritesView.class);
            intent.putExtra("Name", "Wikipedia");
        } else if (id == R.id.nav_stack) {
            intent = new Intent(HomePage.this, FavouritesView.class);
            intent.putExtra("Name", "StackExchange");
        } else if(id == R.id.nav_favourites){
            intent = new Intent(HomePage.this, FavouritesView.class);
            intent.putExtra("Name", "Favourites");
        } else if (id == R.id.settings){

        }
        else if(id == R.id.sign_out){
            AuthUI.getInstance().signOut(this);
        }

        if(intent != null)
            startActivity(intent);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
