package team12.cs4850.com.adventurecreator;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditEventActivity extends MyBaseActivity {

    private static final String TAG = "NewEventActivity";
    //private int eventId;

    private EditText etEventTitle, etDescription;
    private Button btnMonsterInfo;

    private RecyclerView childEventRecycler;
    private ZChildEventAdapter zChildEventAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        etEventTitle = findViewById(R.id.etEventTitle);
        etDescription = findViewById(R.id.etDescription);
        childEventRecycler = findViewById(R.id.mChildNodeRecycler);

        btnMonsterInfo = findViewById(R.id.btnMonsterInfo);

        childEventRecycler.setHasFixedSize(true);
        LinearLayoutManager myLayoutMgr = new LinearLayoutManager(this);
        childEventRecycler.setLayoutManager(myLayoutMgr);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isSignedIn()) {
            setTitle(getString(R.string.EditEvent) + " " + Integer.toString(currEvent.eventId));
            etEventTitle.setText(currEvent.title);
            etDescription.setText(currEvent.description);
            attachRecyclerViewAdapter();

            if (currEvent.eventType == Constants.MONSTER_EVENT) {
                btnMonsterInfo.setVisibility(View.VISIBLE);
            }
            else {
                btnMonsterInfo.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_edit_event;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editevent, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {

            case R.id.action_add_child:
                if (okToSave()) {
                    currEvent.title = etEventTitle.getText().toString().trim();
                    currEvent.description = etDescription.getText().toString().trim();
                    mDatabase.child("adventures").child(currAdventure.adventureKey).child("events").setValue(currAdventure.events);
                    MyBaseActivity.isNewChildEvent = true;
                    Intent i = new Intent(EditEventActivity.this, EditLinkToNextEventActivity.class);
                    //i.putExtra("eventId", eventId);
                    startActivity(i);
                }
                return true;
            case R.id.action_save:
                if (okToSave()) {
                    currEvent.title = etEventTitle.getText().toString().trim();
                    currEvent.description = etDescription.getText().toString().trim();
                    mDatabase.child("adventures").child(currAdventure.adventureKey).child("events").setValue(currAdventure.events);
                    //saving could be done more efficiently, to only save current record, like so:
                    //but is this safe?
                    //mDatabase.child("adventures").child(currAdventure.adventureKey).child("events").child(Integer.toString(listPosition)).setValue(currEvent);
                }
                return true;
            case R.id.action_delete:
                if (currEvent.eventId == 1){
                    Toast.makeText(this, getResources().getString(R.string.CantDeleteStartNode), Toast.LENGTH_SHORT).show();
                }
                else if ((currEvent.nextEventIds != null) && (currEvent.nextEventIds.size() != 0)) {
                    Toast.makeText(this, getResources().getString(R.string.CantDeleteEventMessage), Toast.LENGTH_SHORT).show();
                }
                else {
                    android.app.AlertDialog.Builder adb = new android.app.AlertDialog.Builder(this)
                            .setTitle(getString(R.string.DeleteEvent))
                            .setMessage(getString(R.string.AreYouSure))
                            .setCancelable(true)
                            .setNegativeButton(getString(android.R.string.cancel), null)
                            .setPositiveButton(getString(android.R.string.yes), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    //remove references to currEvent
                                    int index;
                                    ZEvent parentEventToUpdate;
                                    if ((currEvent.prevEventIds != null) && (currEvent.prevEventIds.size() != 0)) {
                                        for (int zPrevEventId: currEvent.prevEventIds) {
                                            parentEventToUpdate = currAdventure.getEventFromEventListUsingEventId(zPrevEventId);
                                            index = parentEventToUpdate.nextEventIds.indexOf(currEvent.eventId);
                                            parentEventToUpdate.nextEventIds.remove(index);
                                            parentEventToUpdate.nextActions.remove(index);
                                        }
                                    }
                                    currAdventure.events.remove(currEvent);
                                    mDatabase.child("adventures").child(currAdventure.adventureKey).setValue(currAdventure);
                                    currEvent = null;
                                    finish();
//                                    String adventureKey = currAdventure.adventureKey;
//                                    mDatabase.child("adventures").child(adventureKey).removeValue();
//                                    mDatabase.child("users").child(auth.getUid()).child("myAdventures").child(adventureKey).removeValue();

                                }
                            });
                    android.app.AlertDialog confirmDialog = adb.create();
                    confirmDialog.show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean okToSave() {
        if (TextUtils.isEmpty(etEventTitle.getText().toString().trim())) {
            etEventTitle.setError("Required.");
            etEventTitle.clearFocus();
            etEventTitle.requestFocus();
            return false;
        }
        etDescription.setError(null);

        if (TextUtils.isEmpty(etDescription.getText().toString().trim())) {
            etDescription.setError("Required.");
            etDescription.clearFocus();
            etDescription.requestFocus();
            return false;
        }

        etDescription.setError(null);
        return true;
    }

    private void attachRecyclerViewAdapter() {
        zChildEventAdapter = new ZChildEventAdapter(currEvent);
        childEventRecycler.setAdapter(zChildEventAdapter);
    }

    public void btnClick(View view) {
        switch (view.getId()) {
            case R.id.btnMonsterInfo:
                if (okToSave()) {
                    currEvent.title = etEventTitle.getText().toString().trim();
                    currEvent.description = etDescription.getText().toString().trim();
                    mDatabase.child("adventures").child(currAdventure.adventureKey).child("events").setValue(currAdventure.events);
                    startActivity(new Intent(EditEventActivity.this, EditMonsterInfoActivity.class));
                }
                break;
        }
    }
}
