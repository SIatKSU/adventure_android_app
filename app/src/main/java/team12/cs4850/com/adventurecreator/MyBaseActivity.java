package team12.cs4850.com.adventurecreator;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//based on https://stackoverflow.com/questions/32367041/calling-toolbar-on-each-activity

public abstract class MyBaseActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener  {

    protected Toolbar toolbar;

    protected DatabaseReference mDatabase;
    protected FirebaseAuth auth;

    static ZAdventure currAdventure;

    //protected static boolean startingUpSoConnectSilently = false;

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

    //protected abstract int getMenuResource();
/*
    //in each activity, override getMenuResource():
    @Override
    protected int getMenuResource() {
        return R.layout.THE_MENU_RESOURCE_FOR_THIS_ACTIVITY;
        //return -1 if no menu.
    }
*/

/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

*//*        if (getMenuResource() != -1) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(getMenuResource(), menu);
            actionBluetooth = menu.findItem(R.id.action_bluetooth);
        }*//*
        return true;
    }*/

/*    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //case R.id.action_bluetooth:
            //    return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/

    //override update status in derived class if more specific actions required.
    //protected void updateStatus(String statusString) {
    //}

    protected boolean isSignedIn() {
        return auth.getCurrentUser() != null;
    }

    private void configureToolbar() {
        toolbar = findViewById(R.id.my_toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            // Get a support ActionBar corresponding to this toolbar
            //ActionBar ab = getSupportActionBar();
            // Enable the Up button
            //ab.setDisplayHomeAsUpEnabled(true);
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
        //mSendButton.setEnabled(isSignedIn());
        //mMessageEdit.setEnabled(isSignedIn());

        if (isSignedIn()) {
            //attachRecyclerViewAdapter();
        }
        else {
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

