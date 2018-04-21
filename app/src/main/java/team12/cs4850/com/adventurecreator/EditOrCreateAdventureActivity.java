package team12.cs4850.com.adventurecreator;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class EditOrCreateAdventureActivity extends MyBaseActivity {

    private EditText mAdventureName, mAdventureDescription;
    private RadioGroup rgAdventureType;

    private RadioGroup.OnCheckedChangeListener myRGListener;

    private TextView labelPlayerHealth, labelWeaponName, labelMinDamage, labelMaxDamage;
    private EditText mPlayerHealth, mWeaponName, mMinDamage, mMaxDamage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdventureName = findViewById(R.id.etAdventureName);
        mAdventureDescription = findViewById(R.id.etWeapon);
        rgAdventureType = findViewById(R.id.rgPronoun);

        labelPlayerHealth = findViewById(R.id.labelMonsterHP);
        labelWeaponName = findViewById(R.id.labelWeaponName);
        labelMinDamage = findViewById(R.id.labelMinDamage);
        labelMaxDamage = findViewById(R.id.labelMaxDamage);

        mPlayerHealth = findViewById(R.id.mPlayerHealth);
        mWeaponName = findViewById(R.id.mWeaponName);
        mMinDamage = findViewById(R.id.mMinDamage);
        mMaxDamage = findViewById(R.id.mMaxDamage);

        myRGListener = new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbFighty:
                        setAdventureType(Constants.FIGHTY_ADVENTURE);
                        break;
                    case R.id.rbSimple:
                    default:
                        setAdventureType(Constants.SIMPLE_ADVENTURE);
                        break;
                }
            }
        };

        rgAdventureType.setOnCheckedChangeListener(myRGListener);

        if (!isNewAdventure) {
            setTitle(getString(R.string.EditAdventure));
            mAdventureName.setText(currAdventure.adventureName);
            mAdventureDescription.setText(currAdventure.adventureDescription);
            setAdventureType(currAdventure.adventureType);

            if (currAdventure.adventureType == Constants.FIGHTY_ADVENTURE) {
                mPlayerHealth.setText(Integer.toString(currAdventure.playerHealth));
                mMinDamage.setText(Integer.toString(currAdventure.minDamage));
                mMaxDamage.setText(Integer.toString(currAdventure.maxDamage));
                mWeaponName.setText(currAdventure.weaponName);
            }
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

    private int getAdventureType() {
        switch (rgAdventureType.getCheckedRadioButtonId()) {
            case R.id.rbFighty:
                return Constants.FIGHTY_ADVENTURE;
            case R.id.rbSimple:
            default:
                return Constants.SIMPLE_ADVENTURE;
        }
    }

    private void setAdventureType(int adventureType) {
        switch (adventureType) {
            case Constants.FIGHTY_ADVENTURE:
                rgAdventureType.check(R.id.rbFighty);
                labelPlayerHealth.setVisibility(View.VISIBLE);
                labelWeaponName.setVisibility(View.VISIBLE);
                labelMinDamage.setVisibility(View.VISIBLE);
                labelMaxDamage.setVisibility(View.VISIBLE);
                mPlayerHealth.setVisibility(View.VISIBLE);
                mWeaponName.setVisibility(View.VISIBLE);
                mMinDamage.setVisibility(View.VISIBLE);
                mMaxDamage.setVisibility(View.VISIBLE);
                break;
            case Constants.SIMPLE_ADVENTURE:
            default:
                rgAdventureType.check(R.id.rbSimple);
                labelPlayerHealth.setVisibility(View.INVISIBLE);
                labelWeaponName.setVisibility(View.INVISIBLE);
                labelMinDamage.setVisibility(View.INVISIBLE);
                labelMaxDamage.setVisibility(View.INVISIBLE);
                mPlayerHealth.setVisibility(View.INVISIBLE);
                mWeaponName.setVisibility(View.INVISIBLE);
                mMinDamage.setVisibility(View.INVISIBLE);
                mMaxDamage.setVisibility(View.INVISIBLE);
                break;
        }
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
                    return;
                }

                mAdventureName.setError(null);

                String adventureDescription = mAdventureDescription.getText().toString().trim();
                if (TextUtils.isEmpty(adventureDescription)) {
                    mAdventureDescription.setError("Required.");
                    mAdventureDescription.clearFocus();
                    mAdventureDescription.requestFocus();
                    return;
                }

                mAdventureDescription.setError(null);

                int adventureType = getAdventureType();

                String playerHealth = null;
                String weaponName = null;
                String minDamage = null;
                String maxDamage = null;
                int minDamageVal = 0;
                int maxDamageVal = 0;

                if (adventureType == Constants.FIGHTY_ADVENTURE) {
                    playerHealth = mPlayerHealth.getText().toString().trim();
                    if (TextUtils.isEmpty(playerHealth)) {
                        mPlayerHealth.setError("Required.");
                        mPlayerHealth.clearFocus();
                        mPlayerHealth.requestFocus();
                        return;
                    }

                    weaponName = mWeaponName.getText().toString().trim();
                    if (TextUtils.isEmpty(weaponName)) {
                        mWeaponName.setError("Required.");
                        mWeaponName.clearFocus();
                        mWeaponName.requestFocus();
                        return;
                    }

                    minDamage = mMinDamage.getText().toString().trim();
                    if (TextUtils.isEmpty(minDamage)) {
                        mMinDamage.setError("Required.");
                        mMinDamage.clearFocus();
                        mMinDamage.requestFocus();
                        return;
                    }

                    maxDamage = mMaxDamage.getText().toString().trim();
                    if (TextUtils.isEmpty(maxDamage)) {
                        mMaxDamage.setError("Required.");
                        mMaxDamage.clearFocus();
                        mMaxDamage.requestFocus();
                        return;
                    }

                    minDamageVal = Integer.parseInt(minDamage);
                    maxDamageVal = Integer.parseInt(maxDamage);
                    if (maxDamageVal < minDamageVal) {
                        mMaxDamage.setError("Max Damage cannot be less than Min Damage.");
                        mMaxDamage.clearFocus();
                        mMaxDamage.requestFocus();
                        return;
                    }
                }

                String adventureKey;
                if (isNewAdventure) {
                    adventureKey = mDatabase.child("adventures").push().getKey();
                    currAdventure = new ZAdventure(auth.getUid(), adventureName, adventureDescription, adventureKey, adventureType);
                    mDatabase.child("users").child(auth.getUid()).child("myAdventures").child(adventureKey).setValue(true);
                } else {
                    adventureKey = currAdventure.adventureKey;
                    currAdventure.adventureName = adventureName;
                    currAdventure.adventureDescription = adventureDescription;
                    currAdventure.adventureType = adventureType;
                }

                if (adventureType == Constants.FIGHTY_ADVENTURE) {
                    currAdventure.playerHealth = Integer.parseInt(playerHealth);
                    currAdventure.minDamage = minDamageVal;
                    currAdventure.maxDamage = maxDamageVal;
                    currAdventure.weaponName = weaponName;
                }

                //if currAdventure has no starting event, add one
                if ((currAdventure.events == null) || (currAdventure.events.size() == 0)) {
                    currAdventure.events = new ArrayList<>();
                    ZEvent startNode = currAdventure.AddNewEvent("Starting event", "Replace with your description");
                }
                mDatabase.child("adventures").child(adventureKey).setValue(currAdventure);
                isNewAdventure = false;

                startActivity(new Intent(EditOrCreateAdventureActivity.this, EventListActivity.class));
                break;
        }
    }
}


