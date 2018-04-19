package team12.cs4850.com.adventurecreator;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AmazonLinkActivity extends MyBaseActivity {

    private static final String TAG = "AmazonLinkActivity";

    private TextView tvLoggedInAs;
    private EditText etAmazonLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvLoggedInAs = findViewById(R.id.tvUsername);
        etAmazonLink = findViewById(R.id.etAmazonLink);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_amazon_link;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isSignedIn()) {
            FirebaseUser user = auth.getCurrentUser();
            tvLoggedInAs.setText(getString(R.string.LoggedInAs) + user.getDisplayName());
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void btnClick(View view) {
        switch (view.getId()) {
            case R.id.btnLink:
                final String amazonCodeStr = etAmazonLink.getText().toString().trim();

                if (TextUtils.isEmpty(amazonCodeStr))
                {
                    etAmazonLink.setError("Please enter the code from the Alexa app.");
                    etAmazonLink.clearFocus();
                    etAmazonLink.requestFocus();
                } else {
                    Query query = mDatabase.child("linkRequest").orderByChild("pin").equalTo(amazonCodeStr);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            ZLinkRequest zLinkRequest = null;
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                zLinkRequest = postSnapshot.getValue(ZLinkRequest.class);
                                break;
                            }

                            if (zLinkRequest != null) {
                                String amazonUserID = zLinkRequest.amazonUserID;
                                String amazonUserIDKey = amazonUserID.replaceAll("[^a-zA-Z0-9 ]", "");	//remove punctuation - not allowed in a firebase key
                                mDatabase.child("linkedUserAccounts").child(amazonUserIDKey).setValue(auth.getUid());
                                mDatabase.child("linkRequest").child(amazonUserIDKey).removeValue();
                                SharedPreferences.Editor editor = MyApplication.savedInfo.edit();
                                editor.putBoolean(Constants.ACCOUNT_LINKED, true);
                                editor.apply();

                                Toast.makeText(AmazonLinkActivity.this, "Congrats!  Your Amazon account is now linked.", Toast.LENGTH_LONG).show();
                                finish();
                            }

                            //not a match
                            else {
                                etAmazonLink.setError("Invalid code: unable to link Amazon account.");
                                etAmazonLink.clearFocus();
                                etAmazonLink.requestFocus();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Getting Post failed, log a message
                            Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                            // ...
                        }
                    });
                }
                break;
        }
    }

//    public static boolean isValidEmail(CharSequence target) {
//        if (target == null)
//            return false;
//        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
//    }

}
