package com.v2soft.apsf.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.v2soft.apsf.R;
import com.v2soft.apsf.fragment.DrawerMenu;
import com.v2soft.apsf.shared.FontCache;
import com.v2soft.apsf.shared.NetWorkAvailable;
import com.v2soft.apsf.shared.ServiceTask;
import com.v2soft.apsf.shared.Utility;

import org.json.JSONObject;

import java.util.Locale;

import static com.v2soft.apsf.shared.Constants.SENDOTP;
import static com.v2soft.apsf.shared.Constants.VERIFYOTP;
import static com.v2soft.apsf.shared.Utility.customToast;

/**
 * Created by srikanth.m on 3/17/2019.
 */

public class Forgot extends AppCompatActivity {

    private EditText editText_User;
    private EditText editText_OTP;

    private TextView tv_submit;

    private int nCase = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.forgot);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        editText_User = (EditText) findViewById(R.id.user);
        editText_OTP = (EditText) findViewById(R.id.otp);

        tv_submit = (TextView) findViewById(R.id.tv_submit);
        tv_submit.setVisibility(View.GONE);

        editText_User.setTypeface(FontCache.getRegularFont());
        editText_OTP.setTypeface(FontCache.getRegularFont());
    }

    @Override
    public void onResume() {
        super.onResume();

        DrawerMenu.nSliderPos = R.id.tv_forgot;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    public void requestOTP(View view) {
        try {
            if (editText_User.getText().toString().length() > 0) {

                editText_OTP.setText("");
                if (NetWorkAvailable.getNetWorkStatus()) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject();
                        jsonObject.put("phno", editText_User.getText().toString());
                    } catch (Exception e) {
                    }

                    nCase = 0;
                    new ServiceTask(this, SENDOTP, jsonObject.toString()).execute();
                } else
                    customToast(getString(R.string.no_network), this);
            } else
                Utility.customToast("Please enter Registered Id or Phone Number", this);
        } catch (Exception e) {
            Utility.CaughtException(this, e);
        }
    }

    public void submitAction(View view) {
        try {
            if (editText_OTP.getText().toString().length() > 0) {

                if (NetWorkAvailable.getNetWorkStatus()) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject();
                        jsonObject.put("phno", editText_User.getText().toString());
                        jsonObject.put("otp", editText_OTP.getText().toString());
                    } catch (Exception e) {
                    }

                    nCase = 1;
                    new ServiceTask(this, VERIFYOTP, jsonObject.toString()).execute();
                } else
                    customToast(getString(R.string.no_network), this);
            } else
                Utility.customToast("Please enter OTP", this);
        } catch (Exception e) {
            Utility.CaughtException(this, e);
        }
    }

    public void callBackDataFromAsyncTask(String result) {
        Log.d("RESPONSE", result);

        try {
            if (result != null && !result.isEmpty()) {

                if (nCase == 0) { // sendotp

                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject != null && jsonObject.length() > 0) {

                        if (jsonObject.getString("responseStatus").toLowerCase(Locale.ENGLISH).equalsIgnoreCase("ok")) {
                            Utility.customToast(jsonObject.getString("message"), this);

                            editText_OTP.setVisibility(View.VISIBLE);
                            editText_OTP.requestFocus();
                            tv_submit.setVisibility(View.VISIBLE);
                        } else
                            Utility.customToast(jsonObject.getString("message"), this);
                    }
                } else { // verifyotp

                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject != null && jsonObject.length() > 0) {

                        if (jsonObject.getString("responseStatus").toLowerCase(Locale.ENGLISH).equalsIgnoreCase("ok")) {
                            Utility.customToast(jsonObject.getString("message"), this);
                            finish();

                            Intent intent = new Intent(this, ChangePassword.class);
                            startActivity(intent);
                        } else
                            Utility.customToast(jsonObject.getString("message"), this);
                    }
                }
            }
        } catch (Exception e) {
            Utility.CaughtException(this, e);
        }
    }
}