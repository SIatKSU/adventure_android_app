package team12.cs4850.com.adventurecreator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class EditEventActivity extends MyBaseActivity {

    private static final String TAG = "NewEventActivity";
    //private int eventId;

    private EditText etEventTitle, etDescription;

    private RecyclerView childEventRecycler;
    private ZChildEventAdapter zChildEventAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        etEventTitle = findViewById(R.id.etEventTitle);
        etDescription = findViewById(R.id.etDescription);
        childEventRecycler = findViewById(R.id.mChildNodeRecycler);

        childEventRecycler.setHasFixedSize(true);
        LinearLayoutManager myLayoutMgr = new LinearLayoutManager(this);
        childEventRecycler.setLayoutManager(myLayoutMgr);

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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isSignedIn()) {

//            eventId  = getIntent().getIntExtra("eventId", 0);
//            for (ZEvent zEvent: currAdventure.events) {
//                if (zEvent.eventId == eventId) {
//                    currEvent = zEvent;
//                }
//            }

            setTitle(getString(R.string.EditEvent) + " " + Integer.toString(currEvent.eventId));
            etEventTitle.setText(currEvent.title);
            etDescription.setText(currEvent.description);

            attachRecyclerViewAdapter();
            //FirebaseUser user = auth.getCurrentUser();
            //tvLoggedInAs.setText(getString(R.string.LoggedInAs) + user.getDisplayName());
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void btnClick(View view) {
        switch (view.getId()) {
//            case R.id.btnAddChildEvent:
//
//                break;
//                startActivity(new Intent(AdventureListActivity.this, CreateNewAdventureActivity.class));
//                finish();
//                break;
//            case R.id.btnCancel:
//                finish();
//                break;
        }
    }

    private boolean okToSave() {
        if (TextUtils.isEmpty(etEventTitle.getText().toString().trim())) {
            etDescription.setError("Required.");
            etDescription.clearFocus();
            etDescription.requestFocus();
            return false;
        }
        else {
            etDescription.setError(null);
            return true;
        }
    }

    private void attachRecyclerViewAdapter() {

        zChildEventAdapter = new ZChildEventAdapter(currEvent);
        childEventRecycler.setAdapter(zChildEventAdapter);
    }

}
