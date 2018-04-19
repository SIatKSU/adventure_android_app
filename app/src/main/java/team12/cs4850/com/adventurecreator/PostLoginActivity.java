package team12.cs4850.com.adventurecreator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class PostLoginActivity extends MyBaseActivity {

    private TextView tvLoggedInAs;
    private String amazonEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        hasBackNavigationArrow = false;
        super.onCreate(savedInstanceState);
        tvLoggedInAs = findViewById(R.id.tvUsername);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_postlogin;
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
                FirebaseUser user = auth.getCurrentUser();
                if (user != null) {
                    auth.signOut();
                    //signOut();
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
            FirebaseUser user = auth.getCurrentUser();
            tvLoggedInAs.setText(getString(R.string.LoggedInAs) + user.getDisplayName());

            if (eventTypes == null) {
                eventTypes = loadEventTypes();
            }

            //amazonEmail = MyApplication.savedInfo.getString(Constants.AMAZON_EMAIL, "");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void btnClick(View view) {
        switch (view.getId()) {
            case R.id.btnAmazonLink:
                boolean isAccountLinked = MyApplication.savedInfo.getBoolean(Constants.ACCOUNT_LINKED, false);
                if (isAccountLinked) {
                    Toast.makeText(PostLoginActivity.this, "Your account is already linked.", Toast.LENGTH_LONG).show();
                }
                else {
                    boolean checkedAccountLink = MyApplication.savedInfo.getBoolean(Constants.ACCOUNT_LINK_CHECKED, false);
                    if (checkedAccountLink) {
                        startActivity(new Intent(PostLoginActivity.this, AmazonLinkActivity.class));
                    }
                    else {
                        //if its a reinstall, Shared Pref won't be set.  and we need to check the account link.
                        Query query = mDatabase.child("linkedUserAccounts").orderByValue().equalTo(auth.getUid());
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                SharedPreferences.Editor editor = MyApplication.savedInfo.edit();
                                editor.putBoolean(Constants.ACCOUNT_LINK_CHECKED, true);
                                editor.apply();
                                if (dataSnapshot.exists()) {
                                    editor = MyApplication.savedInfo.edit();
                                    editor.putBoolean(Constants.ACCOUNT_LINKED, true);
                                    editor.apply();
                                    Toast.makeText(PostLoginActivity.this, "Your account is already linked.", Toast.LENGTH_LONG).show();
                                }
                                else {
                                    //Toast.makeText(PostLoginActivity.this, "didn't find link", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(PostLoginActivity.this, AmazonLinkActivity.class));
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                // Getting Post failed, log a message
                                System.out.println("something went wrong...database error");
                            }
                        });
                    }
                }
                break;
            case R.id.btnAdventureCreator:
                startActivity(new Intent(PostLoginActivity.this, AdventureListActivity.class));
                break;
        }
    }
}
