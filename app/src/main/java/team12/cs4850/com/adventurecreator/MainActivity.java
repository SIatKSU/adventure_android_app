package team12.cs4850.com.adventurecreator;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends MyBaseActivity implements FirebaseAuth.AuthStateListener {

    private TextView tvLoggedInAs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvLoggedInAs = findViewById(R.id.tvUsername);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
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
                FirebaseAuth auth = FirebaseAuth.getInstance();
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
        FirebaseAuth.getInstance().addAuthStateListener(this);

        if (isSignedIn()) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            tvLoggedInAs.setText(getString(R.string.LoggedInAs) + user.getDisplayName());
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        FirebaseAuth.getInstance().removeAuthStateListener(this);
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth auth) {
        //mSendButton.setEnabled(isSignedIn());
        //mMessageEdit.setEnabled(isSignedIn());

        if (isSignedIn()) {
            //attachRecyclerViewAdapter();
        }
        else {
            startActivity(new Intent(getBaseContext(), StartActivity.class));
            finish();
            //Toast.makeText(this, R.string.signing_in, Toast.LENGTH_SHORT).show();
            //auth.signInAnonymously().addOnCompleteListener(new SignInResultNotifier(this));
        }
    }

    private boolean isSignedIn() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    public void signOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
    }


    public void btnClick(View view) {
        switch (view.getId()) {

        }
    }
}
