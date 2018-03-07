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
import android.widget.Toast;

public class EditOrCreateAdventureActivity extends MyBaseActivity {

    private TextView adventureTypeLabel;
    private EditText mAdventureName, mAdventureDescription;

    private Spinner spinnerAdventureType;

    private boolean isNew;

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


        isNew  = getIntent().getBooleanExtra("isNew", false);
        //Toast.makeText(this, Boolean.toString(isNew), Toast.LENGTH_SHORT).show();
        if (!isNew) {
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
        return R.layout.activity_create_new_adventure;
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
            //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void btnClick(View view) {
        switch (view.getId()) {
            case R.id.btnCancel:
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
                        if (isNew) {
                            adventureKey = mDatabase.child("adventures").push().getKey();
                            currAdventure = new ZAdventure(auth.getUid(), adventureName, adventureDescription, adventureKey, spinnerAdventureType.getSelectedItemPosition());
                            mDatabase.child("users").child(auth.getUid()).child("myAdventures").child(adventureKey).setValue(true);
                        }
                        else {
                            adventureKey = currAdventure.adventureKey;
                            currAdventure.adventureName = adventureName;
                            currAdventure.adventureDescription = adventureDescription;
                        }

                        mDatabase.child("adventures").child(adventureKey).setValue(currAdventure);


                        startActivity(new Intent(EditOrCreateAdventureActivity.this, NewEventActivity.class));
                        finish();
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

