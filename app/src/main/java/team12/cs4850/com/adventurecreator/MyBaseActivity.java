package team12.cs4850.com.adventurecreator;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

//based on https://stackoverflow.com/questions/32367041/calling-toolbar-on-each-activity

public abstract class MyBaseActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener  {

    protected Toolbar toolbar;
    protected boolean hasBackNavigationArrow = true;

    protected DatabaseReference mDatabase;
    protected FirebaseAuth auth;


    //***************global variables*********************
    static ZAdventure currAdventure;
    static ZEvent currEvent;
    static boolean isNewAdventure = false;

    static boolean isNewChildEvent = false;
    static ZEvent currChildEvent;

    //eventTypes is loaded for each user.
    //there is the built-in set, with the possibility of users adding their own custom sets.
    //eventTypes set to null when you logout
    //eventTypes reloaded on login (in PostLoginActivity)
    //static ArrayList<String> eventTypes;

/*    static ArrayList<String> loadEventTypes () {
        ArrayList<String> myEventTypes = new ArrayList<>();
        //load the built-in types
        myEventTypes.add(Constants.BASIC_EVENT_STRING);
        myEventTypes.add(Constants.MONSTER_EVENT_STRING);
        myEventTypes.add(Constants.FORKED_PATH);
        myEventTypes.add(Constants.DARK_ROOM);
        return myEventTypes;
    }*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //if (getResources().getBoolean(R.bool.portrait_only)) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //}

        setContentView(getLayoutResource());
        configureToolbar();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
    }

    protected abstract int getLayoutResource();
    /*
        //in each activity, override getLayoutResource():
        @Override
        protected int getLayoutResource() {
            return R.layout.activity_toolbar;
        }
    */

    protected boolean isSignedIn() {
        return auth.getCurrentUser() != null;
    }

    private void configureToolbar() {
        toolbar = findViewById(R.id.my_toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);

            if (hasBackNavigationArrow) {
                // Get a support ActionBar corresponding to this toolbar
                ActionBar ab = getSupportActionBar();
                // Enable the Up button
                ab.setDisplayHomeAsUpEnabled(true);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(this);
        //invalidateOptionsMenu();    //so onPrepareOptionsMenu is called
        //updateStatus();
    }

    @Override
    protected void onStop() {
        super.onStop();
        auth.removeAuthStateListener(this);
    }


    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth auth) {
        if (isSignedIn()) {
            //attachRecyclerViewAdapter();
        }
        else {
            //eventTypes = null;      //need to reload for each user
            startActivity(new Intent(getBaseContext(), StartActivity.class));
            finish();
            //Toast.makeText(this, R.string.signing_in, Toast.LENGTH_SHORT).show();
            //auth.signInAnonymously().addOnCompleteListener(new SignInResultNotifier(this));
        }
    }

    protected void signOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
    }


}

