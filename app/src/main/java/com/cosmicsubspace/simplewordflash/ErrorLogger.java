package com.cosmicsubspace.simplewordflash;

import android.util.Log;


public class ErrorLogger {
    public static final int e = -1;

    public static void log(Exception e) {

        Log2.log(4, ErrorLogger.class, "Error Handled:\n" + logToString(e));

    }

    public static String logToString(Throwable e) {
        return Log.getStackTraceString(e);
        /*
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        return e.toString();*/
    }


}
