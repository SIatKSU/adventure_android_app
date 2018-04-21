package team12.cs4850.com.adventurecreator;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdventureListActivity extends MyBaseActivity {

    private TextView tvLoggedInAs, tvNoAdventuresFound;

    private RecyclerView adventureRecyclerView;
    private ZAdventureAdapter zAdventureAdapter;
    private ArrayList<ZAdventure> adventureList = new ArrayList<>();

    private static final String TAG = "AdventureListPage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvLoggedInAs = findViewById(R.id.tvUsername);
        tvNoAdventuresFound = findViewById(R.id.tvNoAdventuresFound);
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
    public void onStart() {
        super.onStart();
        if (isSignedIn()) {
            FirebaseUser user = auth.getCurrentUser();
            tvLoggedInAs.setText(getString(R.string.LoggedInAs) + user.getDisplayName());
            attachRecyclerViewAdapter();
        }
    }

    public void btnClick(View view) {
        Intent editAdventureIntent;
        switch (view.getId()) {
            case R.id.btnNew:
                currAdventure = null;
                editAdventureIntent = new Intent(AdventureListActivity.this, EditOrCreateAdventureActivity.class);
                isNewAdventure = true;
                startActivity(editAdventureIntent);
                break;
            case R.id.btnEdit:

                if (zAdventureAdapter.selected_position != RecyclerView.NO_POSITION) {
                    currAdventure = adventureList.get(zAdventureAdapter.selected_position);
                    editAdventureIntent = new Intent(AdventureListActivity.this, EditOrCreateAdventureActivity.class);
                    isNewAdventure = false;
                    startActivity(editAdventureIntent);
                }
                else {
                    //Snackbar.make(view, getResources().getString(R.string.NoAdventureSelected), Snackbar.LENGTH_SHORT)
                    //        .setAction("Action", null).show();
                    Toast.makeText(this, getResources().getString(R.string.NoAdventureSelected), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnDelete:
                if (zAdventureAdapter.selected_position != RecyclerView.NO_POSITION) {
                    currAdventure = adventureList.get(zAdventureAdapter.selected_position);

                    android.app.AlertDialog.Builder adb = new android.app.AlertDialog.Builder(this)
                            .setTitle(getString(R.string.DeleteAdventure))
                            .setMessage(getString(R.string.AreYouSure))
                            .setCancelable(true)
                            .setNegativeButton(getString(android.R.string.cancel), null)
                            .setPositiveButton(getString(android.R.string.yes), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String adventureKey = currAdventure.adventureKey;
                                    mDatabase.child("adventures").child(adventureKey).removeValue();
                                    mDatabase.child("users").child(auth.getUid()).child("myAdventures").child(adventureKey).removeValue();

                                }
                    });
                    android.app.AlertDialog confirmDialog = adb.create();
                    confirmDialog.show();
                }
                else {
                    //Snackbar.make(view, getResources().getString(R.string.NoAdventureSelected), Snackbar.LENGTH_SHORT)
                    //        .setAction("Action", null).show();
                    Toast.makeText(this, getResources().getString(R.string.NoAdventureSelected), Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    private void attachRecyclerViewAdapter() {

        Query query = mDatabase.child("adventures").orderByChild("userid").equalTo(auth.getUid());

        zAdventureAdapter = new ZAdventureAdapter(adventureList);
        adventureRecyclerView.setAdapter(zAdventureAdapter);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                adventureList = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    ZAdventure zAdventure = postSnapshot.getValue(ZAdventure.class);
                    adventureList.add(zAdventure);
                    zAdventureAdapter.updateList(adventureList);
                }
                tvNoAdventuresFound.setVisibility(adventureList.isEmpty() ? View.VISIBLE : View.GONE);
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

