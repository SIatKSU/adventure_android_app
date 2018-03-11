package team12.cs4850.com.adventurecreator;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EditLinkToNextEventActivity extends MyBaseActivity {

    private static final String TAG = "AddChildEventActivity";

    //private int eventId;

    private EditText etTriggerWords;
    private TextView tvEventType;
    private Spinner spinnerNextNode, spinnerEventType;

    private List<String> nextNodeStringList;
    private List<Integer> nextNodeList;
    //private List<String> eventTypes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        etTriggerWords = findViewById(R.id.etTriggerWords);
        spinnerNextNode = findViewById(R.id.spinnerNextNode);
        spinnerEventType = findViewById(R.id.spinnerEventType);
        tvEventType = findViewById(R.id.tvEventType);

        //setup eventTypes spinner
        //eventTypes = getEventTypes();
        ArrayAdapter<String> spinnerEventTypeAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, eventTypes);
        spinnerEventTypeAdapter.setDropDownViewResource(R.layout.my_spinner_layout);
        spinnerEventType.setAdapter(spinnerEventTypeAdapter);


        //setup nextNode spinner
        nextNodeStringList = new ArrayList<>();
        nextNodeList = new ArrayList<>();
        for (ZEvent zEvent: currAdventure.events) {
            if (zEvent.eventId != currEvent.eventId) {
                nextNodeList.add(zEvent.eventId);
                nextNodeStringList.add(Integer.toString(zEvent.eventId) + " " + zEvent.title);
            }
        }
        nextNodeStringList.add(getString(R.string.CreateNewChildEvent));

        ArrayAdapter<String> spinnerNextNodeAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, nextNodeStringList);
        spinnerNextNodeAdapter.setDropDownViewResource(R.layout.my_spinner_layout);
        spinnerNextNode.setAdapter(spinnerNextNodeAdapter);


        if (isNewChildEvent) {
            spinnerNextNode.setSelection(nextNodeStringList.size() - 1);      //default to create a new node
            tvEventType.setVisibility(View.VISIBLE);
            spinnerEventType.setVisibility(View.VISIBLE);               //default to create a new node
        }
        else {
            //currChildEvent.eventId
            String triggerWords = currEvent.getTriggerWordsFromChildEventId(currChildEvent.eventId);
            etTriggerWords.setText(triggerWords);
            int spinnerIndex = nextNodeList.indexOf(currChildEvent.eventId);
            spinnerNextNode.setSelection(spinnerIndex);      //default to create a new node

            tvEventType.setVisibility(View.INVISIBLE);
            spinnerEventType.setVisibility(View.INVISIBLE);               //default to create a new node
        }


        SpinnerInteractionListener spinnerListener = new SpinnerInteractionListener();
        spinnerNextNode.setOnTouchListener(spinnerListener);
        spinnerNextNode.setOnItemSelectedListener(spinnerListener);

/*        if (nextNodeStringList.size() == 1) {//"Create New Child" Option is only option available
            spinnerEventType.setVisibility(View.VISIBLE);
        }
        else {
            spinnerEventType.setVisibility(View.INVISIBLE);
        }*/


    }

//    private List<String> getEventTypes () {
//        List<String> eventTypes = new ArrayList<>();
//        eventTypes.add("basic event");
//        return eventTypes;
//    }


    @Override
    protected int getLayoutResource() {
        return R.layout.activity_edit_link_to_next_event;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_addchildevent, menu);
        if (isNewChildEvent) {
            menu.findItem(R.id.action_delete).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
//            case R.id.action_save:
//                tryToSaveChildEvent();
//                return true;
            case R.id.action_delete:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isSignedIn()) {


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
            case R.id.btnSave:
                tryToSaveChildEvent();
                break;
            case R.id.btnCancel:
                finish();
                break;
        }
    }

    //custom spinner class that doesn't trigger the onItemSelected method if spinner value is programmatically changed (default spinner is so annoying!)
    class SpinnerInteractionListener implements AdapterView.OnItemSelectedListener, View.OnTouchListener {
        boolean userSelect = false;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            userSelect = true;
            return false;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            if (userSelect) {
                userSelect = false;
                switch (parent.getId()) {
                    case R.id.spinnerNextNode:
                        if (pos == nextNodeStringList.size() - 1) {//"Create New Child" Option was picked"
                            spinnerEventType.setVisibility(View.VISIBLE);
                        }
                        else {
                            spinnerEventType.setVisibility(View.INVISIBLE);
                        }
                        break;
                    case R.id.spinnerEventType:
                        break;
                }
            }
        }

        public void onNothingSelected(AdapterView<?> parent) {
        }
    }

    private void tryToSaveChildEvent() {
        if (okToSave()) {
            if (currEvent.actions == null) {
                currEvent.actions = new ArrayList<>();
                currEvent.nextEventIds = new ArrayList<>();
            }
            int triggerIndex;
            String triggerWords = etTriggerWords.getText().toString().trim();

            if (spinnerNextNode.getSelectedItemPosition() == nextNodeStringList.size() - 1) {
                //Toast.makeText(getBaseContext(), "New Child", Toast.LENGTH_SHORT).show();
                ZEvent newChildEvent = currAdventure.AddNewEvent(getString(R.string.YourTitleHere), getString(R.string.YourDescriptionHere));


                if (isNewChildEvent) {
                    currEvent.actions.add(triggerWords);
                    currEvent.nextEventIds.add(newChildEvent.eventId);
                }
                else {
                    triggerIndex = currEvent.getTriggerWordsIndexFromChildEventId(newChildEvent.eventId);
                    currEvent.actions.set(triggerIndex, triggerWords);
                    currEvent.nextEventIds.set(triggerIndex, newChildEvent.eventId);
                }
                //dont forget the adventure counter is being updated, and needs to be saved as well
                //saving could be done more efficiently, more targeted - but not going to worry about this for now
                mDatabase.child("adventures").child(currAdventure.adventureKey).setValue(currAdventure);
                currEvent = newChildEvent;
            }
            else {
                //linking to an existing node
                int nextEventId = nextNodeList.get(spinnerNextNode.getSelectedItemPosition());

                if (isNewChildEvent) {
                    currEvent.actions.add(triggerWords);
                    currEvent.nextEventIds.add(nextEventId);
                }
                else {
                    triggerIndex = currEvent.getTriggerWordsIndexFromChildEventId(nextEventId);
                    currEvent.actions.set(triggerIndex, triggerWords);
                    currEvent.nextEventIds.set(triggerIndex, nextEventId);
                }
                //saving could be done more efficiently, more targeted - but not going to worry about this for now
                mDatabase.child("adventures").child(currAdventure.adventureKey).setValue(currAdventure);
                currEvent = currAdventure.getEventFromEventListUsingEventId(nextEventId);
            }

            //startActivity(new Intent(EditLinkToNextEventActivity.this, EditEventActivity.class));
            finish();
        }
    }

    private boolean okToSave() {
        if (TextUtils.isEmpty(etTriggerWords.getText().toString().trim())) {
            etTriggerWords.setError("Required.");
            etTriggerWords.clearFocus();
            etTriggerWords.requestFocus();
            return false;
        }
        else {
            etTriggerWords.setError(null);
            return true;
        }
    }

}
