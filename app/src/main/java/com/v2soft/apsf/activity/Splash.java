package com.v2soft.apsf.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.v2soft.apsf.R;
import com.v2soft.apsf.logger.Logger;
import com.v2soft.apsf.model.ResultItem;
import com.v2soft.apsf.shared.AppController;
import com.v2soft.apsf.shared.AppPreferences;
import com.v2soft.apsf.shared.Constants;
import com.v2soft.apsf.shared.NetWorkAvailable;
import com.v2soft.apsf.shared.WebServiceCall;

import java.io.File;
import java.lang.reflect.Type;
import java.security.MessageDigest;
import java.util.ArrayList;

import static com.v2soft.apsf.shared.AppController.mHashDistrictsList;
import static com.v2soft.apsf.shared.AppController.mHashMandalsList;
import static com.v2soft.apsf.shared.Constants.CONSTITUENCIES;
import static com.v2soft.apsf.shared.Constants.COUNTRIES;
import static com.v2soft.apsf.shared.Constants.DEPARTMENTS;
import static com.v2soft.apsf.shared.Constants.DISTRICTS;
import static com.v2soft.apsf.shared.Constants.MANDALS;
import static com.v2soft.apsf.shared.Constants.PROFESSIONS;
import static com.v2soft.apsf.shared.Constants.QUALIFICATIONS;
import static com.v2soft.apsf.shared.Constants.STATES;
import static com.v2soft.apsf.shared.Constants.SUBCASTE;
import static com.v2soft.apsf.shared.Constants.VILLAGES;
import static com.v2soft.apsf.shared.Utility.CaughtException;
import static com.v2soft.apsf.shared.Utility.customToast;

/**
 * Created by srikanth.m on 4/2/2018.
 */

public class Splash extends Activity {

    private static final int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        NetWorkAvailable netWorkAvailable = NetWorkAvailable.getInstance(this);

        // HASH-KEY sp6FwHji3nVHTo2zdBGTLzfk3f8=
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.vsoft.mithra", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String strKeyHash = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Logger.getInstance(this).AuditLogWriter("", "", "KEYHASH", strKeyHash);
                Log.d("KeyHash:", strKeyHash);
            }
        } catch (Exception e) {
            Log.d("KeyHash:", "");
        }

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        } else
            waitAct();
            //new LoadData().execute();
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_ALL: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, do your work....
                    //new LoadData().execute();
                    waitAct();
                } else {
                    // permission denied
                    // Disable the functionality that depends on this permission.
                    customToast("Can't proceed  without Permission", this);
                    finish();
                }//else
                return;
            }
        }//switch()
    }//onRequestPermissionsResult()

    private void waitAct() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity

                String strIsRemember = AppPreferences.getInstance(Splash.this).getValue("REMEMBER");
                if (strIsRemember.equalsIgnoreCase("true"))
                    AppController.user = AppPreferences.getInstance(Splash.this).getValue("EMAIL");

                next();
            }
        }, SPLASH_TIME_OUT);
    }

    private void next() {

        try {
            File filepath = new File(Environment.getExternalStorageDirectory(), getString(R.string.app_name) + "/");
            File file = new File(filepath, "Log.txt");
            if (file.exists()) {
                // Get length of file in bytes
                long fileSize = file.length();
                // Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
                fileSize = fileSize / 1024;
                //  Convert the KB to MegaBytes (1 MB = 1024 KBytes)
                fileSize = fileSize / 1024;
                if (fileSize >= 3) {
                    file.delete();
                }
            }
        } catch (Exception e) {
        }

        String strIsLogin = AppPreferences.getInstance(this).getValue("REMEMBER");
        if (strIsLogin.equalsIgnoreCase("true")) {

            AppController.user = AppPreferences.getInstance(this).getUserName();

            finish();
            Intent intent = new Intent(Splash.this, Home.class);
            startActivity(intent);
            return;
        }

        finish();
        Intent i = new Intent(Splash.this, Login.class);
        startActivity(i);
    }
}
