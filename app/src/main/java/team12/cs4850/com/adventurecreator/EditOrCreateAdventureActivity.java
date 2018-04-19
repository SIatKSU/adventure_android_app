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

public class EditOrCreateAdventureActivity extends MyBaseActivity {

    private TextView adventureTypeLabel;
    private EditText mAdventureName, mAdventureDescription;

    private Spinner spinnerAdventureType;

    //private boolean isNewAdventure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdventureName = findViewById(R.id.etAdventureName);
        mAdventureDescription = findViewById(R.id.etAdventureDescription);
        spinnerAdventureType = findViewById(R.id.spinnerAdventureType);
        adventureTypeLabel = findViewById(R.id.adventureTypeLabel);

        ArrayAdapter<String> adventureTypeAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.adventureTypeArray));
        adventureTypeAdapter.setDropDownViewResource(R.layout.my_spinner_layout);
        spinnerAdventureType.setAdapter(adventureTypeAdapter);

        //SpinnerInteractionListener spinnerListener = new SpinnerInteractionListener();
        //spinnerAdventureType.setOnTouchListener(spinnerListener);
        //spinnerAdventureType.setOnItemSelectedListener(spinnerListener);


        //isNewAdventure  = getIntent().getBooleanExtra("isNewAdventure", false);
        //Toast.makeText(this, Boolean.toString(isNewAdventure), Toast.LENGTH_SHORT).show();
        if (!isNewAdventure) {
            setTitle(getString(R.string.EditAdventure));

            spinnerAdventureType.setSelection(currAdventure.adventureType);
            spinnerAdventureType.setVisibility(View.INVISIBLE);
            adventureTypeLabel.setVisibility(View.INVISIBLE);

            mAdventureName.setText(currAdventure.adventureName);
            mAdventureDescription.setText(currAdventure.adventureDescription);

        }

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_edit_or_create_adventure;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isSignedIn()) {
            //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void btnClick(View view) {
        switch (view.getId()) {
            case R.id.btnExit:
                finish();
                break;
            case R.id.btnSave:
                String adventureName = mAdventureName.getText().toString().trim();
                if (TextUtils.isEmpty(adventureName)) {
                    mAdventureName.setError("Required.");
                    mAdventureName.clearFocus();
                    mAdventureName.requestFocus();
                }
                else {
                    mAdventureName.setError(null);

                    String adventureDescription = mAdventureDescription.getText().toString().trim();
                    if (TextUtils.isEmpty(adventureDescription)) {
                        mAdventureDescription.setError("Required.");
                        mAdventureDescription.clearFocus();
                        mAdventureDescription.requestFocus();
                    }

                    else {

                        mAdventureDescription.setError(null);

                        //we could check here if adventureName is duplicate

                        String adventureKey;
                        if (isNewAdventure) {
                            adventureKey = mDatabase.child("adventures").push().getKey();
                            currAdventure = new ZAdventure(auth.getUid(), adventureName, adventureDescription, adventureKey, spinnerAdventureType.getSelectedItemPosition());
                            mDatabase.child("users").child(auth.getUid()).child("myAdventures").child(adventureKey).setValue(true);
                        }
                        else {
                            adventureKey = currAdventure.adventureKey;
                            currAdventure.adventureName = adventureName;
                            currAdventure.adventureDescription = adventureDescription;
                        }

                        //if currAdventure has no starting event, add one
                        if ((currAdventure.events == null) || (currAdventure.events.size() == 0)) {
                            currAdventure.events = new ArrayList<ZEvent>();
                            ZEvent startNode = currAdventure.AddNewEvent("Starting event", "Replace with your description");
                        }
                        mDatabase.child("adventures").child(adventureKey).setValue(currAdventure);
                        isNewAdventure = false;

                        startActivity(new Intent(EditOrCreateAdventureActivity.this, EventListActivity.class));
                        //finish();
                    }
                }

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
//                    case R.id.spinnerPackRule:
//                        //warn user about not having DeviceID
//                        if ((pos == Constants.PackRuleDateSequence) || (pos == Constants.PackRuleSequence)) {
//                            Toast.makeText(getBaseContext(), "Use of a Pack Rule without DeviceID is not recommended.", Toast.LENGTH_SHORT).show();
//                        }
//                        hideAndShowElements();
//                        setupAndgenPackNumber();
//                        break;
                }
            }
        }

        public void onNothingSelected(AdapterView<?> parent) {
        }
    }


}

