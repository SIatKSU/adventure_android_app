package team12.cs4850.com.adventurecreator;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by siatk on 2/27/2018.
 */

public class MyApplication extends Application {

    private static Context mContext;
    public static Context getContext(){
        return mContext;
    }

    public static SharedPreferences savedInfo;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

        savedInfo = getSharedPreferences(Constants.SAVED_PREFERENCES, MODE_PRIVATE);

        //registerActivityLifecycleCallbacks(new MyActivityLifecycleCallbacks());

        // initialize the shared preferences from 'disk'

        //playDefault = MyApplication.savedInfo.getBoolean(Constants.PLAY_BEEP_DEFAULT, true);
        //playSound = MyApplication.savedInfo.getBoolean(Constants.PLAY_BEEP_MULTI, false);
        //vibrateOn = MyApplication.savedInfo.getBoolean(Constants.VIBRATE, false);
        //if (playDefault) {
        //mDefaultBeep = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
        //}
        //if (playSound) {
        //mBeepMulti = MediaPlayer.create(this, R.raw.beepmulti);
        //}
        //mBeepWarning = MediaPlayer.create(this, R.raw.notgood);

    }

    @Override
    public void onTerminate (){
        //mCurrentActivity = null;
        super.onTerminate();
    }

/*    private static final class MyActivityLifecycleCallbacks implements ActivityLifecycleCallbacks {

        public void onActivityCreated(Activity activity, Bundle bundle) {
            mCurrentActivity = activity;
        }
        public void onActivityStarted(Activity activity) {
            mCurrentActivity = activity;
        }
        public void onActivityResumed(Activity activity) {
            mCurrentActivity = activity;
        }
        public void onActivityDestroyed(Activity activity) {
            if (mCurrentActivity == activity) {
                mCurrentActivity = null;
            }
        }
        public void onActivityPaused(Activity activity) {
        }
        public void onActivitySaveInstanceState(Activity activity,
                                                Bundle outState) {
        }
        public void onActivityStopped(Activity activity) {
        }
    }

    *//**
     * The Handler that gets information back from the BluetoothChatService
     *//*
    public final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if ((mCurrentActivity != null) && (mCurrentActivity instanceof MyBaseActivity)) {
                ((MyBaseActivity) mCurrentActivity).messageResponder(msg);
            }
        }
    };*/


}
