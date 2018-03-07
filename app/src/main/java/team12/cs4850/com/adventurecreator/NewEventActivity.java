package team12.cs4850.com.adventurecreator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseUser;

public class NewEventActivity extends MyBaseActivity {

    private static final String TAG = "NewEventActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_new_event);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_new_event;
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
            //FirebaseUser user = auth.getCurrentUser();
            //tvLoggedInAs.setText(getString(R.string.LoggedInAs) + user.getDisplayName());
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void btnClick(View view) {
//        switch (view.getId()) {
//            case R.id.btnNew:
//                startActivity(new Intent(AdventureListActivity.this, CreateNewAdventureActivity.class));
//                break;
//            case R.id.btnEdit:
//                break;
//            case R.id.btnDelete:
//                break;
//        }
    }

}
