package com.hs.customfilelogger;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import pl.brightinventions.slf4android.FileLogHandlerConfiguration;
import pl.brightinventions.slf4android.LoggerConfiguration;

/**
 * Created by Hannan Shaik on 13/07/16.
 */

public class MyLogger {

    private static final int WRITE_EXTERNAL_STORAGE_PERMISSION = 1001;
    private String className;
    private static Logger Log;
    public static String LOG_FOLDER = Environment.getExternalStorageDirectory().getPath() + "/"+BuildConfig.APPLICATION_ID+"/logs/";

    private static int PURGE_DAYS = 7; //number of days before which the files need to be deleted/purged
    private static boolean isLogFolderCreated;

    public static void setup(Context context) {

        Date date = new Date();
        String today = new SimpleDateFormat("ddMMyyyy", Locale.ENGLISH).format(date);

        File logDirectory = new File(LOG_FOLDER);
        if(!logDirectory.exists()){
            isLogFolderCreated = logDirectory.mkdirs();
        }

        FileLogHandlerConfiguration fileHandler = LoggerConfiguration.fileLogHandler(context);
        fileHandler.setFullFilePathPattern(logDirectory.getPath() + "/"+today+".log");

        LoggerConfiguration.configuration()
                .removeRootLogcatHandler()
                .addHandlerToRootLogger(fileHandler);

        deleteOlderFiles();
    }

    private static void deleteOlderFiles(){
        final long purgeTime =
                System.currentTimeMillis() - (PURGE_DAYS * 24 * 60 * 60 * 1000);


        File logDirectory = new File(LOG_FOLDER);
        if(logDirectory.exists()){
            File[] files = logDirectory.listFiles();
            for(File file : files){
                if(file.lastModified()<purgeTime){
                    file.delete();
                }
            }
        }
    }

    public static MyLogger getInstance(String className){
        MyLogger instance = new MyLogger();
        instance.className = className;
        Log = LoggerFactory.getLogger(className);
        return instance;
    }

    public void d(String message){
        android.util.Log.d(className, message);
        if(isLogFolderCreated)
            Log.debug(message);
    }

    public void e(String message){
        android.util.Log.e(className, message);
        if(isLogFolderCreated)
            Log.error(message);
    }

    public void e(String message, Throwable t){
        android.util.Log.e(className, message, t);
        if(isLogFolderCreated)
            Log.error(message, t);
    }

    public void i(String message){
        android.util.Log.i(className, message);
        if(isLogFolderCreated)
            Log.info(message);
    }

}
