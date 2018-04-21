package team12.cs4850.com.adventurecreator;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;

public class EditMonsterInfoActivity extends MyBaseActivity {

    private static final String TAG = "editMonsterInfoActivity";

    private EditText mMonsterName, mMonsterHealth, mWeapon, mMinDamage, mMaxDamage;
    private RadioGroup rgPronoun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMonsterName = findViewById(R.id.etMonsterName);
        mMonsterHealth = findViewById(R.id.mMonsterHealth);
        mWeapon = findViewById(R.id.mWeaponName);
        mMinDamage = findViewById(R.id.mMinDamage);
        mMaxDamage = findViewById(R.id.mMaxDamage);

        rgPronoun = findViewById(R.id.rgPronoun);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isSignedIn()) {
            mMonsterName.setText(currEvent.monsterName);
            mMonsterHealth.setText(Integer.toString(currEvent.monsterHealth));
            mWeapon.setText(currEvent.weaponName);
            mMinDamage.setText(Integer.toString(currEvent.minDamage));
            mMaxDamage.setText(Integer.toString(currEvent.maxDamage));

            switch (currEvent.monsterPronoun) {

                case "her":
                    rgPronoun.check(R.id.rbHer);
                    break;
                case "its":
                    rgPronoun.check(R.id.rbIts);
                    break;
                case "their":
                    rgPronoun.check(R.id.rbTheir);
                    break;
                case "his":
                default:
                    rgPronoun.check(R.id.rbHis);
                    break;
            }
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_edit_monsterinfo;
    }


    private boolean okToSave() {
        if (TextUtils.isEmpty(mMonsterName.getText().toString().trim())) {
            mMonsterName.setError("Required.");
            mMonsterName.clearFocus();
            mMonsterName.requestFocus();
            return false;
        }
        mMonsterName.setError(null);

        if (TextUtils.isEmpty(mMonsterHealth.getText().toString().trim())) {
            mMonsterHealth.setError("Required.");
            mMonsterHealth.clearFocus();
            mMonsterHealth.requestFocus();
            return false;
        }
        mMonsterHealth.setError(null);

        int health = Integer.parseInt(mMonsterHealth.getText().toString().trim());
        if (health < 1) {
            mMonsterHealth.setError("Must be greater than 0.");
            mMonsterHealth.clearFocus();
            mMonsterHealth.requestFocus();
            return false;
        }
        mMonsterHealth.setError(null);

        if (TextUtils.isEmpty(mWeapon.getText().toString().trim())) {
            mWeapon.setError("Required.");
            mWeapon.clearFocus();
            mWeapon.requestFocus();
            return false;
        }
        mWeapon.setError(null);

        if (TextUtils.isEmpty(mMinDamage.getText().toString().trim())) {
            mMinDamage.setError("Required.");
            mMinDamage.clearFocus();
            mMinDamage.requestFocus();
            return false;
        }
        mMinDamage.setError(null);

        if (TextUtils.isEmpty(mMaxDamage.getText().toString().trim())) {
            mMaxDamage.setError("Required.");
            mMaxDamage.clearFocus();
            mMaxDamage.requestFocus();
            return false;
        }
        mMaxDamage.setError(null);

        int minDamageVal = Integer.parseInt(mMinDamage.getText().toString().trim());
        int maxDamageVal = Integer.parseInt(mMaxDamage.getText().toString().trim());
        if (maxDamageVal < minDamageVal) {
            mMaxDamage.setError("Max Damage cannot be less than Min Damage.");
            mMaxDamage.clearFocus();
            mMaxDamage.requestFocus();
            return false;
        }
        mMaxDamage.setError(null);

        return true;
    }

    public void btnClick(View view) {
        switch (view.getId()) {
            case R.id.btnSave:
                if (okToSave()) {

                    currEvent.monsterName = mMonsterName.getText().toString().trim();
                    currEvent.monsterHealth = Integer.parseInt(mMonsterHealth.getText().toString().trim());
                    currEvent.weaponName = mWeapon.getText().toString().trim();
                    currEvent.minDamage = Integer.parseInt(mMinDamage.getText().toString().trim());
                    currEvent.maxDamage = Integer.parseInt(mMaxDamage.getText().toString().trim());

                    switch (rgPronoun.getCheckedRadioButtonId()) {

                        case R.id.rbHer:
                            currEvent.monsterPronoun = "her";
                            break;
                        case R.id.rbIts:
                            currEvent.monsterPronoun = "its";
                            break;
                        case R.id.rbTheir:
                            currEvent.monsterPronoun = "their";
                            break;
                        case R.id.rbHis:
                        default:
                            currEvent.monsterPronoun = "his";
                            break;
                    }

                    mDatabase.child("adventures").child(currAdventure.adventureKey).child("events").setValue(currAdventure.events);
                }
                finish();
                break;
        }
    }

}
