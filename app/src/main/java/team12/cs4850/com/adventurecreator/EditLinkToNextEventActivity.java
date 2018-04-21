package team12.cs4850.com.adventurecreator;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class EditLinkToNextEventActivity extends MyBaseActivity {

    private static final String TAG = "AddChildEventActivity";

    private EditText etTriggerWords;
    private TextView tvEventType, tvEventDescription;
    private Spinner spinnerNextNode, spinnerEventType;
    private Button btnDeleteLink;

    private List<String> nextNodeStringList;
    private List<Integer> nextNodeList;

    private ZEvent zOrigChildEvent;

    private String[] eventTypes;
    private String[] eventTypeDescriptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        etTriggerWords = findViewById(R.id.etTriggerWords);
        spinnerNextNode = findViewById(R.id.spinnerNextNode);
        spinnerEventType = findViewById(R.id.spinnerEventType);
        tvEventType = findViewById(R.id.tvEventType);
        tvEventDescription = findViewById(R.id.eventDescription);

        btnDeleteLink = findViewById(R.id.btnDeleteLink);

        if (currAdventure.adventureType == Constants.SIMPLE_ADVENTURE) {
            //monster events not available for simple adventure.
            eventTypes = getResources().getStringArray(R.array.simpleEventTypes);
            eventTypeDescriptions = getResources().getStringArray(R.array.simpleDescription);
        }
        else {
            eventTypes = getResources().getStringArray(R.array.eventTypes);
            eventTypeDescriptions = getResources().getStringArray(R.array.prefabDescription);
        }

        //setup eventTypes spinner
        ArrayAdapter<String> spinnerEventTypeAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, eventTypes);
        spinnerEventTypeAdapter.setDropDownViewResource(R.layout.my_spinner_layout);
        spinnerEventType.setAdapter(spinnerEventTypeAdapter);


        //setup nextNode spinner
        nextNodeStringList = new ArrayList<>();
        nextNodeList = new ArrayList<>();
        for (ZEvent zEvent : currAdventure.events) {
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
            spinnerEventType.setVisibility(View.VISIBLE);
            tvEventDescription.setText(eventTypeDescriptions[0]);
            btnDeleteLink.setVisibility(View.GONE);

            if ((currEvent.eventType == Constants.MONSTER_EVENT) &&
                ((currEvent.nextEventIds == null) || (currEvent.nextEventIds.isEmpty()))) {
                etTriggerWords.setText("attack");
            }
        } else {
            String triggerWords = currEvent.getTriggerWordsFromChildEventId(currChildEvent.eventId);
            etTriggerWords.setText(triggerWords);
            int spinnerIndex = nextNodeList.indexOf(currChildEvent.eventId);
            spinnerNextNode.setSelection(spinnerIndex);
            tvEventType.setVisibility(View.GONE);
            spinnerEventType.setVisibility(View.GONE);
            tvEventDescription.setText(currChildEvent.description);
            btnDeleteLink.setVisibility(View.VISIBLE);
        }
        zOrigChildEvent = currChildEvent;
        if (zOrigChildEvent != null) {
            if (zOrigChildEvent.prevEventIds == null) {
                zOrigChildEvent.prevEventIds = new ArrayList<>();
            }
        }

        SpinnerInteractionListener spinnerListener = new SpinnerInteractionListener();
        spinnerNextNode.setOnTouchListener(spinnerListener);
        spinnerNextNode.setOnItemSelectedListener(spinnerListener);

        spinnerEventType.setOnTouchListener(spinnerListener);
        spinnerEventType.setOnItemSelectedListener(spinnerListener);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_edit_link_to_next_event;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_addchildevent, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //we do need a way to delete links. not implemented yet though.
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
        }
    }

    public void btnClick(View view) {
        switch (view.getId()) {
            case R.id.btnSave:
                tryToSaveChildEvent();
                break;
            case R.id.btnExit:
                finish();
                break;
            case R.id.btnDeleteLink:
                android.app.AlertDialog.Builder adb = new android.app.AlertDialog.Builder(this)
                        .setTitle("Delete link to next event")
                        .setMessage(getString(R.string.AreYouSure))
                        .setCancelable(true)
                        .setNegativeButton(getString(android.R.string.cancel), null)
                        .setPositiveButton(getString(android.R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                try {
                                    int nextEventId = nextNodeList.get(spinnerNextNode.getSelectedItemPosition());
                                    ZEvent nextEvent = currAdventure.getEventFromEventListUsingEventId(nextEventId);

                                    int index = currEvent.getListIndexFromChildEventId(nextEventId);
                                    currEvent.nextActions.remove(index);
                                    currEvent.nextEventIds.remove(index);
                                    int prevIndex = nextEvent.getListIndexFromPreviousEventId(currEvent.eventId);
                                    nextEvent.prevEventIds.remove(prevIndex);

                                    //saving could be done more efficiently, more targeted - but not going to worry about this for now
                                    mDatabase.child("adventures").child(currAdventure.adventureKey).setValue(currAdventure);
                                    Toast.makeText(EditLinkToNextEventActivity.this, "Link removed.", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                                catch (Exception ex) {
                                    Toast.makeText(EditLinkToNextEventActivity.this, "There was a problem removing this link.", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }
                        });
                android.app.AlertDialog confirmDialog = adb.create();
                confirmDialog.show();
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
                            tvEventType.setVisibility(View.VISIBLE);
                            spinnerEventType.setVisibility(View.VISIBLE);
                            btnDeleteLink.setVisibility(View.GONE);

                            int prefabEventType = spinnerEventType.getSelectedItemPosition();
                            tvEventDescription.setText(eventTypeDescriptions[prefabEventType]);
                        } else {
                            tvEventType.setVisibility(View.GONE);
                            spinnerEventType.setVisibility(View.GONE);
                            btnDeleteLink.setVisibility(View.VISIBLE);

                            int nextEventId = nextNodeList.get(pos);
                            ZEvent nextEvent = currAdventure.getEventFromEventListUsingEventId(nextEventId);
                            tvEventDescription.setText(nextEvent.description);
                        }
                        break;
                    case R.id.spinnerEventType:
                        tvEventDescription.setText(eventTypeDescriptions[pos]);
                        break;
                }
            }
        }

        public void onNothingSelected(AdapterView<?> parent) {
        }
    }

    private void tryToSaveChildEvent() {
        if (okToSave()) {
            if (currEvent.nextActions == null) {
                currEvent.nextActions = new ArrayList<>();
                currEvent.nextEventIds = new ArrayList<>();
            }
            int index;
            String triggerWords = etTriggerWords.getText().toString().trim();

            if (spinnerNextNode.getSelectedItemPosition() == nextNodeStringList.size() - 1) {
                //linking to a new child

                ZEvent newChildEvent = currAdventure.AddNewEvent("", "");
                newChildEvent.eventType = spinnerEventType.getSelectedItemPosition();
                //if creating a monster, set the event default monster values
                if (newChildEvent.eventType == Constants.MONSTER_EVENT) {
                    newChildEvent.monsterName = "troll";
                    newChildEvent.weaponName = "club";
                    newChildEvent.monsterPronoun = "his"; //its, his, her, their
                    newChildEvent.monsterHealth = 15;
                    newChildEvent.minDamage = 0;
                    newChildEvent.maxDamage = 5;
                }

                if (isNewChildEvent) {
                    //creating a new child link
                    currEvent.nextActions.add(triggerWords);
                    currEvent.nextEventIds.add(newChildEvent.eventId);
                    newChildEvent.prevEventIds.add(currEvent.eventId);      //add a reference to the parent linking to this node
                } else {
                    //editing an existing link
                    index = currEvent.getListIndexFromChildEventId(zOrigChildEvent.eventId);
                    currEvent.nextActions.set(index, triggerWords);
                    currEvent.nextEventIds.set(index, newChildEvent.eventId);

                    //in the previous child, remove the reference to the parent linking to this node
                    zOrigChildEvent.prevEventIds.remove((Integer) currEvent.eventId);
                    //in the current child, add a reference to the parent linking to this node
                    newChildEvent.prevEventIds.add(currEvent.eventId);
                }
                //dont forget the adventure counter is being updated, and needs to be saved as well
                //saving could be done more efficiently, more targeted - but not going to worry about this for now
                mDatabase.child("adventures").child(currAdventure.adventureKey).setValue(currAdventure);
                currEvent = newChildEvent;
            } else {
                //linking to an existing child

                int nextEventId = nextNodeList.get(spinnerNextNode.getSelectedItemPosition());
                ZEvent nextEvent = currAdventure.getEventFromEventListUsingEventId(nextEventId);

                if (isNewChildEvent) {
                    //creating a new child link

                    currEvent.nextActions.add(triggerWords);
                    currEvent.nextEventIds.add(nextEventId);
                    if (nextEvent.prevEventIds == null) {
                        nextEvent.prevEventIds = new ArrayList<>();
                    }
                    nextEvent.prevEventIds.add(currEvent.eventId);      //add a reference to the parent linking to this node
                } else {
                    //editing an existing link

                    index = currEvent.getListIndexFromChildEventId(nextEventId);
                    currEvent.nextActions.set(index, triggerWords);
                    currEvent.nextEventIds.set(index, nextEventId);

                    if (zOrigChildEvent != nextEvent) {
                        //in the previous child, remove the reference to the parent linking to this node
                        zOrigChildEvent.prevEventIds.remove((Integer) currEvent.eventId);
                        //in the current child, add a reference to the parent linking to this node
                        nextEvent.prevEventIds.add(currEvent.eventId);
                    }

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
        } else {
            etTriggerWords.setError(null);
            return true;
        }
    }

}
