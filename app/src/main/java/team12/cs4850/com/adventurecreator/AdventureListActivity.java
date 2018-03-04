package team12.cs4850.com.adventurecreator;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdventureListActivity extends MyBaseActivity {

    private TextView tvLoggedInAs;

    private RecyclerView adventureRecyclerView;
    private ZAdventureAdapter zAdventureAdapter;
    private ArrayList<ZAdventure> adventureList = new ArrayList<>();

    private Query query;
    private ValueEventListener myQueryListener;

    private static final String TAG = "AdventureListPage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvLoggedInAs = findViewById(R.id.tvUsername);
        adventureRecyclerView = findViewById(R.id.mAdventureRecycler);

        adventureRecyclerView.setHasFixedSize(true);
        LinearLayoutManager myLayoutMgr = new LinearLayoutManager(this);
        myLayoutMgr.setReverseLayout(true);
        myLayoutMgr.setStackFromEnd(true);
        adventureRecyclerView.setLayoutManager(myLayoutMgr);

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_adventure_list;
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
            FirebaseUser user = auth.getCurrentUser();
            tvLoggedInAs.setText(getString(R.string.LoggedInAs) + user.getDisplayName());

            attachRecyclerViewAdapter();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void btnClick(View view) {
        switch (view.getId()) {
            case R.id.btnNew:
                startActivity(new Intent(AdventureListActivity.this, CreateNewAdventureActivity.class));
                break;
            case R.id.btnEdit:
                break;
            case R.id.btnDelete:
                break;
        }
    }

    private void attachRecyclerViewAdapter() {

        if (myQueryListener != null) {
            query.removeEventListener(myQueryListener);
        }

        query = mDatabase.child("adventures").orderByChild("userid").equalTo(auth.getUid());

        zAdventureAdapter = new ZAdventureAdapter(adventureList);
        adventureRecyclerView.setAdapter(zAdventureAdapter);

        myQueryListener = query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Toast.makeText(getBaseContext(), "calling onDataChange", Toast.LENGTH_SHORT).show();
                adventureList = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    ZAdventure zAdventure = postSnapshot.getValue(ZAdventure.class);
                    adventureList.add(zAdventure);

                    zAdventureAdapter.updateList(adventureList);
                    //mEmptyListMessage.setText(getResources().getString(R.string.no_items));
                    //mEmptyListMessage.setVisibility(!dataSnapshot.hasChildren() ? View.VISIBLE : View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });
    }



}

