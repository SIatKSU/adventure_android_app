package team12.cs4850.com.adventurecreator;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;


//based on https://stackoverflow.com/questions/32367041/calling-toolbar-on-each-activity

public abstract class MyBaseActivity extends AppCompatActivity {

    protected Toolbar toolbar;
    protected MenuItem actionBluetooth;

    //protected static boolean startingUpSoConnectSilently = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //if (getResources().getBoolean(R.bool.portrait_only)) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //}

        setContentView(getLayoutResource());
        configureToolbar();
    }

    @Override
    protected void onStart() {
        super.onStart();

        //invalidateOptionsMenu();    //so onPrepareOptionsMenu is called
        //updateStatus();

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

}

