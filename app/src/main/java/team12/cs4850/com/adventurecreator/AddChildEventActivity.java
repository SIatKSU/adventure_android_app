package team12.cs4850.com.adventurecreator;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

public class AddChildEventActivity extends MyBaseActivity {

    private static final String TAG = "AddChildEventActivity";

    private int eventId;
    private ZEvent currEvent;


    private EditText etTriggerWords;
    private Spinner spinnerNextNode, spinnerEventType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_new_event);

        etTriggerWords = findViewById(R.id.etTriggerWords);
        spinnerNextNode = findViewById(R.id.spinnerNextNode);
        spinnerEventType = findViewById(R.id.spinnerEventType);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_add_child_event;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_addchildevent, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_save:
//                String eventTitle = etEventTitle.getText().toString().trim();
//                String eventDescription = etDescription.getText().toString().trim();
//                if (TextUtils.isEmpty(eventDescription)) {
//                    etDescription.setError("Required.");
//                    etDescription.clearFocus();
//                    etDescription.requestFocus();
//                }
//                else {
//                    etDescription.setError(null);
//                    currEvent.title = eventTitle;
//                    currEvent.description = eventDescription;
//                    mDatabase.child("adventures").child(currAdventure.adventureKey).child("events").setValue(currAdventure.events);
//                    //saving could be done more efficiently, to only save current record, like so:
//                    //but is this safe?
//                    //mDatabase.child("adventures").child(currAdventure.adventureKey).child("events").child(Integer.toString(listPosition)).setValue(currEvent);
//                    finish();
//                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isSignedIn()) {

            eventId  = getIntent().getIntExtra("eventId", 0);
            for (ZEvent zEvent: currAdventure.events) {
                if (zEvent.eventId == eventId) {
                    currEvent = zEvent;
                }
            }
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
//            case R.id.btnSave:
//                startActivity(new Intent(AdventureListActivity.this, CreateNewAdventureActivity.class));
//                finish();
//                break;
//            case R.id.btnCancel:
//                finish();
//                break;
        }
    }

}
