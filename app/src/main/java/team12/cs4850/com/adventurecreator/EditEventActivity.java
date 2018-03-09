package team12.cs4850.com.adventurecreator;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class EditEventActivity extends MyBaseActivity {

    private static final String TAG = "NewEventActivity";
    private int listPosition;
    private ZEvent currEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_new_event);

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_edit_event;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editevent, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_save:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isSignedIn()) {

            listPosition  = getIntent().getIntExtra("eventListPosition", 0);
            currEvent = currAdventure.events.get(listPosition);

            setTitle(getString(R.string.EditEvent) + " " + Integer.toString(currEvent.eventId));


            //FirebaseUser user = auth.getCurrentUser();
            //tvLoggedInAs.setText(getString(R.string.LoggedInAs) + user.getDisplayName());
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void btnClick(View view) {
        switch (view.getId()) {
//            case R.id.btnSave:
//                startActivity(new Intent(AdventureListActivity.this, CreateNewAdventureActivity.class));
//                finish();
//                break;
//            case R.id.btnCancel:
//                finish();
//                break;
        }
    }

}
