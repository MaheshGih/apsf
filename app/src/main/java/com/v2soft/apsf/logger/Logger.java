package com.v2soft.apsf.logger;

import android.content.Context;
import android.os.Environment;

import com.v2soft.apsf.R;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

    private static Logger instance = null;

    private static File auditLogFile;
    private static FileWriter fileWriter;

    public static Logger getInstance(Context context) {
        if (instance == null) {
            if (context == null) {
                return null;
            }
            instance = new Logger(context.getApplicationContext());
        }
        return instance;
    }

    private Logger(Context context) {
        try {
            File filepath = new File(Environment.getExternalStorageDirectory(), context.getString(R.string.app_name) + "/");
            if (!filepath.exists())
                filepath.mkdir();

            File file = new File(filepath, "Log.txt");
            
            if (!file.exists())
                file.createNewFile();

            auditLogFile = new File(file.getAbsolutePath());
            fileWriter = new FileWriter(auditLogFile, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void AuditLogWriter(String struser, String strCaf, String strEvent, String strDescription) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss", java.util.Locale.ENGLISH);
        String formattedDate = sdf.format(date);
        BufferedWriter writer = null;
        try {
            fileWriter = new FileWriter(auditLogFile, true);
            writer = new BufferedWriter(fileWriter);
            writer.write(formattedDate + "~" + struser + "~" + strCaf + "~" + strEvent + "~" + strDescription);
            writer.newLine();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null)
                    writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}