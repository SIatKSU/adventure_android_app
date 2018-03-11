package team12.cs4850.com.adventurecreator;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EventListActivity extends MyBaseActivity {

    private TextView adventureLabel;

    private RecyclerView eventRecyclerView;
    private ZEventAdapter zEventAdapter;

    private static final String TAG = "EventListPage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adventureLabel = findViewById(R.id.eventsLabel);
        eventRecyclerView = findViewById(R.id.mEventsRecycler);

        eventRecyclerView.setHasFixedSize(true);
        LinearLayoutManager myLayoutMgr = new LinearLayoutManager(this);
        eventRecyclerView.setLayoutManager(myLayoutMgr);

        adventureLabel.setText(currAdventure.adventureName);

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_event_list;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_signout:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        if (isSignedIn()) {
//            FirebaseUser user = auth.getCurrentUser();
//            tvLoggedInAs.setText(getString(R.string.LoggedInAs) + user.getDisplayName());
//
            attachRecyclerViewAdapter();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void btnClick(View view) {

    }

    private void attachRecyclerViewAdapter() {
        zEventAdapter = new ZEventAdapter(currAdventure.events);
        eventRecyclerView.setAdapter(zEventAdapter);
    }



}

