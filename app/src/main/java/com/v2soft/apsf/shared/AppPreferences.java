package com.v2soft.apsf.shared;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AppPreferences {

    private static AppPreferences instance = null;

    /**
     * Must be called once on app startup
     *
     * @param context - application context
     * @return this
     */
    public static AppPreferences getInstance(Context context) {
        if (instance == null) {
            if (context == null) {
                throw new IllegalStateException(AppPreferences.class.getSimpleName() +
                        " is not initialized, call getInstance(Context) with a VALID Context first.");
            }
            instance = new AppPreferences(context.getApplicationContext());
        }
        return instance;
    }


    private SharedPreferences preferences;

    private AppPreferences(Context context) {
        preferences = context.getSharedPreferences(context.getPackageName() + "_preferences", Context.MODE_PRIVATE); // From Android sources for getDefaultSharedPreferences
    }

    /**
     * Is Recording Enabled
     *
     * @return true/false
     */
    public boolean isRecordingEnabled() {
        return preferences.getBoolean("RecordingEnabled", true);
    }

    /**
     * Set Recording is Enabled
     *
     * @param enabled true/false
     */
    public void setRecordingEnabled(boolean enabled) {
        preferences.edit().putBoolean("RecordingEnabled", enabled).commit();
    }

    /**
     * Is Login Enabled
     *
     * @return true/false
     */
    public boolean isLoginEnabled() {
        return preferences.getBoolean("LOGIN", false);
    }

    /**
     * Set Login is Enabled
     *
     * @param enabled true/false
     */
    public void setLoginEnabled(boolean enabled) {
        preferences.edit().putBoolean("LOGIN", enabled).commit();
    }

    public void setUserName(String strUser) {
        preferences.edit().putString("username", strUser).commit();
    }

    public String getUserName() {
        return preferences.getString("username", "");
    }

    public void setPassWord(String strPass) {
        preferences.edit().putString("password", strPass).commit();
    }

    public String getPassWord() {
        return preferences.getString("password", "");
    }

    /**
     * Record incoming calls if {@link #isRecordingEnabled() isRecordingEnabled}
     *
     * @return true/false
     */
    public boolean isRecordingIncomingEnabled() {
        return preferences.getBoolean("RecordingIncomingEnabled", true);
    }

    /**
     * Set Recording is Enabled for incoming calls
     *
     * @param enabled true/false
     */
    public void setRecordingIncomingEnabled(boolean enabled) {
        preferences.edit().putBoolean("RecordingIncomingEnabled", enabled).commit();
    }

    /**
     * Record outgoing calls if {@link #isRecordingEnabled() isRecordingEnabled}
     *
     * @return true/false
     */
    public boolean isRecordingOutgoingEnabled() {
        return preferences.getBoolean("RecordingOutgoingEnabled", true);
    }

    /**
     * Set Recording is Enabled for outgoing calls
     *
     * @param enabled true/false
     */
    public void setRecordingOutgoingEnabled(boolean enabled) {
        preferences.edit().putBoolean("RecordingOutgoingEnabled", enabled).commit();
    }

    /**
     * Get the location to store the recordings too...
     * TODO: make configurable
     *
     * @return directory location
     */
    public File getFilesDirectory() {
        // might make this configurable....
        String filesDir = (new StringBuilder()).append(Environment.getExternalStorageDirectory().getAbsolutePath()).append("/").append("calls").append("/").toString();
        filesDir = preferences.getString("FilesDirectory", filesDir);
        File myDir = new File(filesDir);
        if (!myDir.exists()) {
            myDir.mkdirs();
        }
        return myDir;
    }

    public void setFilesDirectory(File file) {
        setFilesDirectory(file.getAbsoluteFile());
    }

    public void setFilesDirectory(String path) {
        if (!path.endsWith("/calls/")) {
            path += "/calls/";
        }
        preferences.edit().putString("FilesDirectory", path).commit();
    }

    public enum OlderThan {
        NEVER,
        DAILY,
        THREE_DAYS,
        WEEKLY,
        MONTHLY,
        QUARTERLY,
        YEARLY
    }


    /**
     * get the Older Than Days for recording cleanup
     *
     * @return 0 if not set
     */

    public OlderThan getOlderThan() {
        String name = preferences.getString("OlderThan", OlderThan.NEVER.name());
        return OlderThan.valueOf(name);
    }

    /**
     * Set the Older Than date for recording cleanup
     *
     * @param olderThan NEVER default
     */

    public void setOlderThan(OlderThan olderThan) {
        preferences.edit().putString("OlderThan", olderThan.name()).commit();
    }

    public void setScheduleNo(String strMobileNo) {
        preferences.edit().putString("schedule_no", strMobileNo).commit();
    }

    public String getScheduleNo() {
        return preferences.getString("schedule_no", "");
    }

    /**
     * Set
     *
     * @param strKey string
     * @param strValue string
     */
    public void setValue(String strKey, String strValue) {
        preferences.edit().putString(strKey, strValue).commit();
    }

    /**
     * Get
     *
     * @return string
     */
    public String getValue(String strKey) {
        return preferences.getString(strKey, "");
    }

    public boolean isExpire() {
        if (preferences != null) {
            try {
                String strDatetime = preferences.getString("DATETIME", "");
                String strSessionTime = preferences.getString("SESSIONTIME", "20");
                int nSessionTime = Integer.parseInt(strSessionTime);

                if (!strDatetime.isEmpty()) {
                    //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm aa");

                    Date strSystemTime = Calendar.getInstance().getTime();
                    Date strServerTime = sdf.parse(strDatetime);

                    //in milliseconds
                    long diff = strSystemTime.getTime() - strServerTime.getTime();
                    long diffMinutes = diff / (60 * 1000) % 60;

                    if (diffMinutes > nSessionTime)
                        return true;
                }
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }
}