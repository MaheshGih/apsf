package com.v2soft.apsf.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
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
import com.v2soft.apsf.shared.Constants;
import com.v2soft.apsf.shared.FontCache;
import com.v2soft.apsf.shared.NetWorkAvailable;
import com.v2soft.apsf.shared.ServiceTask;

import org.json.JSONObject;

import java.util.Locale;

import static com.v2soft.apsf.shared.Utility.customToast;
import static com.v2soft.apsf.shared.Utility.isValidEmail;

/**
 * Created by srikanth.m on 3/1/2018.
 */

public class ChangePassword extends AppCompatActivity {

    TextView tv_footer;

    Typeface mTypaface;

    EditText etUser;
    EditText etPass;
    EditText etConfirmPass;

    private String mstrEmailId = "";
    private String mstrPass = "";
    private String mstrConfirmPass = "";

    private boolean isFromLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.change_password);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        TextView title_text = (TextView) findViewById(R.id.title_text);

        // toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        tv_footer = (TextView) findViewById(R.id.tv_footer);

        mTypaface = FontCache.getTypeface("ui-text-regular.ttf", this);

        etUser = (EditText) findViewById(R.id.user);
        etUser.setTypeface(mTypaface);
        etUser.clearFocus();

        etPass = (EditText) findViewById(R.id.pass);
        etPass.setTypeface(mTypaface);

        etConfirmPass = (EditText) findViewById(R.id.confirm_password);
        etConfirmPass.setTypeface(mTypaface);

        TextView tv_footer = (TextView) findViewById(R.id.tv_footer);
        tv_footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.text_site)));
                startActivity(browserIntent);
            }
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            isFromLogin = true;
            mstrEmailId = bundle.getString("EMAIL");
            etUser.setText(mstrEmailId);
            title_text.setText("Create New Password");
            etPass.requestFocus();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        DrawerMenu.nSliderPos = R.id.tv_change;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    public void forgotAction(View v) {

        mstrEmailId = etUser.getText().toString();
        if (mstrEmailId.isEmpty()) {
            customToast("Registered Id or Phone Number can't be empty", ChangePassword.this);
            return;
        }

        /*if (!isValidEmail(mstrEmailId)) {
            customToast("Please enter valid Email Id", ChangePassword.this);
            return;
        }*/

        mstrPass = etPass.getText().toString();
        if (mstrPass.isEmpty()) {
            customToast("Password can't be empty", ChangePassword.this);
            return;
        }

        mstrConfirmPass = etConfirmPass.getText().toString();
        if (mstrConfirmPass.isEmpty()) {
            customToast("Confirm Password can't be empty", ChangePassword.this);
            return;
        }

        if (!mstrPass.equals(mstrConfirmPass)) {
            customToast("Password and Confirm Password must be same", ChangePassword.this);
            return;
        }

        if (NetWorkAvailable.getNetWorkStatus()) {

            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject();
                jsonObject.put("phno", mstrEmailId);
                jsonObject.put("password", mstrPass);
            } catch (Exception e) {}

            new ServiceTask(ChangePassword.this, Constants.CHANGE_PASSWORD, jsonObject.toString()).execute();
        } else
            customToast(getString(R.string.no_network), this);
    }

    public void callBackDataFromAsyncTask(String result) {
        Log.d("CHANGE", result);

        if (result != null && !result.isEmpty()) {

            try {
                if (result.toLowerCase(Locale.ENGLISH).contains("success")) {
                    customToast("Password changed successfully", this);
                    finish();

                    /*if (isFromLogin) {
                        Intent intent = new Intent(ChangePassword.this, ProfileUpdate.class);
                        startActivity(intent);
                        return;
                    }*/
                    Intent intent = new Intent(ChangePassword.this, Login.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else
                    customToast("Password change failed", this);

            } catch (Exception e) {
                customToast("failed " + e.getLocalizedMessage(), this);
            }
        }
    }
}
