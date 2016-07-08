package com.cosmicsubspace.simplewordflash;

//Licensed under the MIT License.
//Include the license text thingy if you're gonna use this.
//Copyright (c) 2016 Chansol Yang

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;


//Logging Convenience Class
public class Log2 {
    private static final int PERMANENT_LOG_LEVEL = 3;
    public static final String LOG_TAG = "CS_Words";
    public static final boolean active=true;

    private static StringBuilder importantLogs = new StringBuilder();

    public static String getImportantLogs(boolean clear) {
        String res = importantLogs.toString();
        if (clear) importantLogs = new StringBuilder();
        return res;
    }



    public static void log(int level, Object callingClass, Object... arguments) {
        if (callingClass != null) log(level, callingClass.getClass(), arguments);
        else log(level, null, arguments);
    }

    public static void log(int level, Class callingClass, Object... arguments) {
        if (!active) return;

        StringBuilder log = new StringBuilder();
        log.append(level + " ");
        log.append("[From ");
        if (callingClass != null) log.append(callingClass.getSimpleName());
        else log.append("NULL");
        log.append("]");
        for (Object arg : arguments) {
            log.append(" | ");
            try {
                log.append(arg.toString());
            } catch (NullPointerException e) {
                log.append("NULL");
            }
        }

        if (level >= PERMANENT_LOG_LEVEL) {
            importantLogs.append(log.toString());
            importantLogs.append("\n");
        }

        switch (level) {
            case 0:
                Log.v(LOG_TAG, log.toString());
                break;
            case 1:
                Log.d(LOG_TAG, log.toString());
                break;
            case 2:
                Log.i(LOG_TAG, log.toString());
                break;
            case 3:
                Log.w(LOG_TAG, log.toString());
                break;
            case 4:
                Log.e(LOG_TAG, log.toString());
                break;
            case 5:
                Log.wtf(LOG_TAG, log.toString());
        }
    }
}
