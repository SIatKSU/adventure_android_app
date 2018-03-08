package team12.cs4850.com.adventurecreator;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PostLoginActivity extends MyBaseActivity {

    private TextView tvLoggedInAs;

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
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void btnClick(View view) {
        switch (view.getId()) {

            case R.id.btnSetupProfile:
                startActivity(new Intent(PostLoginActivity.this, SetupProfileActivity.class));
                break;
            case R.id.btnAdventureCreator:
                startActivity(new Intent(PostLoginActivity.this, AdventureListActivity.class));
                break;
        }
    }
}
