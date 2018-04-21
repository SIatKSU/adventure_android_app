package team12.cs4850.com.adventurecreator;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by siatk on 2/27/2018.
 */

public class MyApplication extends Application {

    //private static Context mContext;
    //public static Context getContext(){
    //    return mContext;
    //}

    public static SharedPreferences savedInfo;

    @Override
    public void onCreate() {
        super.onCreate();
        //mContext = this;
        savedInfo = getSharedPreferences(Constants.SAVED_PREFERENCES, MODE_PRIVATE);
    }
}
