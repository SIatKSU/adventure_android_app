package team12.cs4850.com.adventurecreator;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.view.View;


public class EventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Navigate to New Event form", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent newEventForm = new Intent(EventActivity.this, EditEventActivity.class);
                startActivity(newEventForm);
            }
        });
    }
}
