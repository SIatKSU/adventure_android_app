package team12.cs4850.com.adventurecreator;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class StartActivity extends MyBaseActivity {

    private static final int RC_SIGN_IN = 123;

    // Choose authentication providers
    List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build());
    //new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build(),
    //new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
    //new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build(),
    //new AuthUI.IdpConfig.Builder(AuthUI.TWITTER_PROVIDER).build());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        hasBackNavigationArrow = false;
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_start);

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_start;
    }

/*    @Override
    protected int getMenuResource() {
        return -1;
    }*/

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth auth) {
        //do NOT respond to authstate changes.
    }


    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            startActivity(new Intent(getBaseContext(), PostLoginActivity.class));
            finish();

        }
    }

    public void btnClick(View view) {
        Intent returnIntent;
        switch (view.getId()) {
            case R.id.btnSignIn:
                // Create and launch sign-in intent
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(providers)
                                .setLogo(R.drawable.dice20)      // Set logo drawable
                                //.setTheme(R.style.MySuperAppTheme)      // Set theme
                                .build(),
                        RC_SIGN_IN);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                //start event page if successfully signed in
                startActivity(new Intent(getBaseContext(), PostLoginActivity.class));
                finish();
            } else {
                // Sign in failed, check response for error code
                // ...
            }
        }
    }

}
