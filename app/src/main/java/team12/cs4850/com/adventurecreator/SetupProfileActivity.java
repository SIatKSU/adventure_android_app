package team12.cs4850.com.adventurecreator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SetupProfileActivity extends MyBaseActivity {

    private static final String TAG = "SetupProfileActivity";

    private TextView tvLoggedInAs;
    private EditText etName, etAmazonEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvLoggedInAs = findViewById(R.id.tvUsername);
        etName = findViewById(R.id.etName);
        etAmazonEmail = findViewById(R.id.etAmazonEmailAccount);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_setup_profile;
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
            FirebaseUser user = auth.getCurrentUser();
            tvLoggedInAs.setText(getString(R.string.LoggedInAs) + user.getDisplayName());
            etName.setText(user.getDisplayName());
            etAmazonEmail.setText(MyApplication.savedInfo.getString(Constants.AMAZON_EMAIL, ""));
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
                final String amazonEmailStr = etAmazonEmail.getText().toString().trim();
                if (!isValidEmail(amazonEmailStr)) {
                    etAmazonEmail.setError(getString(R.string.InvalidEmailMessage));
                    etAmazonEmail.clearFocus();
                    etAmazonEmail.requestFocus();
                } else {
                    SharedPreferences.Editor myEditor = MyApplication.savedInfo.edit();
                    myEditor.putString(Constants.AMAZON_EMAIL, amazonEmailStr);
                    myEditor.apply();
                    mDatabase.child("users").child(auth.getUid()).child("amazonEmail").setValue(amazonEmailStr);

                    Query query = mDatabase.child("adventures").orderByChild("userid").equalTo(auth.getUid());
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //ArrayList adventureList = new ArrayList<>();
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                ZAdventure zAdventure = postSnapshot.getValue(ZAdventure.class);
                                //adventureList.add(zAdventure);
                                mDatabase.child("adventures").child(zAdventure.adventureKey).child("amazonEmail").setValue(amazonEmailStr);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Getting Post failed, log a message
                            Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                            // ...
                        }
                    });




                    finish();
                }
                break;
        }
    }

    public static boolean isValidEmail(CharSequence target) {
        if (target == null)
            return false;
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

}
