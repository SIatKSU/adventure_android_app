package team12.cs4850.com.adventurecreator;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

public class EventListActivity extends MyBaseActivity {

    private TextView adventureLabel;

    private RecyclerView eventRecyclerView;
    private ZEventAdapter zEventAdapter;

    private static final String TAG = "EventListPage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adventureLabel = findViewById(R.id.eventsLabel);
        eventRecyclerView = findViewById(R.id.mEventsRecycler);

        eventRecyclerView.setHasFixedSize(true);
        LinearLayoutManager myLayoutMgr = new LinearLayoutManager(this);
        eventRecyclerView.setLayoutManager(myLayoutMgr);

        adventureLabel.setText(currAdventure.adventureName);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_event_list;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isSignedIn()) {
            attachRecyclerViewAdapter();
        }
    }

    private void attachRecyclerViewAdapter() {
        zEventAdapter = new ZEventAdapter(currAdventure.events);
        eventRecyclerView.setAdapter(zEventAdapter);
    }



}

