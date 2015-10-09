package seniordesign.com.dancewithme.utils;

import android.util.Log;

public class Logger {
    private static Boolean DEBUG = true;

    public static void d(String TAG, String string) {
        if(DEBUG) {
            Log.d(TAG, string);
        }
    }

    public static void v(String TAG, String string) {
        if(DEBUG) {
            Log.v(TAG, string);
        }
    }

    public static void i(String TAG, String string) {
        if(DEBUG) {
            Log.i(TAG, string);
        }
    }

    public static void e(String TAG, String string) {
        if(DEBUG) {
            Log.e(TAG, string);
        }
    }

    public static void w(String TAG, String string) {
        if (DEBUG) {
            Log.w(TAG, string);
        }
    }
}
