package com.v2soft.apsf.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.v2soft.apsf.R;
import com.v2soft.apsf.logger.Logger;
import com.v2soft.apsf.shared.AppController;
import com.v2soft.apsf.shared.AppPreferences;
import com.v2soft.apsf.shared.Constants;
import com.v2soft.apsf.shared.FontCache;
import com.v2soft.apsf.shared.MyProgressDialog;
import com.v2soft.apsf.shared.NetWorkAvailable;
import com.v2soft.apsf.shared.ServiceTask;

import org.json.JSONObject;

import java.util.Iterator;
import java.util.Locale;

import static com.v2soft.apsf.shared.Utility.CaughtException;
import static com.v2soft.apsf.shared.Utility.customToast;

/**
 * Created by srikanth.m on 2/28/2018.
 */

public class Login extends AppCompatActivity {

    private static final int RC_SIGN_IN = 007;

    EditText etUser;
    EditText etPass;

    Typeface mTypaface;

    TextView textTerms;
    TextView tv_footer;

    CheckBox checkBox;

    // Progress Dialog Object
    MyProgressDialog mProgressDialog;

    boolean mbIsConnect = false;

    String mstrUserId = "";
    String mstrEmailId = "";
    String mstrPassword = "";
    String mstrFName = "";
    String mstrPhotoUrl = "";
    String mstrLName = "";
    String mstrMobile = "";
    String mstrSignUpType = "";

    private int m_nCase = 0;

    //private AdView mAdView;
    private int nPos = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        mProgressDialog = new MyProgressDialog(Login.this); // ProgressDialog.THEME_HOLO_LIGHT);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setInverseBackgroundForced(true);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mTypaface = FontCache.getTypeface("ui-text-regular.ttf", this);

        tv_footer = (TextView) findViewById(R.id.tv_footer);

        etUser = (EditText) findViewById(R.id.user);
        etUser.setTypeface(mTypaface);
        etUser.clearFocus();

        etPass = (EditText) findViewById(R.id.pass);
        etPass.setTypeface(mTypaface);
        etPass.clearFocus();

        /*etUser.setText("9494678351");
        etPass.setText("9494678351");*/

        SpannableString spannableString = new SpannableString("By logging in, you agree to the\nTerms & Conditions & Privacy Policy of " + getString(R.string.app_name));
        spannableString.setSpan(new ForegroundColorSpan(Color.BLUE), 32, 67, 0);

        textTerms = (TextView) findViewById(R.id.text_terms);
        textTerms.setText(spannableString);

        checkBox = (CheckBox) findViewById(R.id.check_remember);
        checkBox.setTypeface(mTypaface);

        ImageView image_gp = (ImageView) findViewById(R.id.image_gp);
        image_gp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    mstrSignUpType = "GMAIL";
                    Logger.getInstance(Login.this).AuditLogWriter(getString(R.string.app_name), "Login", "Logging through ", mstrSignUpType);

                    mbIsConnect = false;
                } catch (Exception e) {
                    CaughtException(Login.this, e);
                }
            }
        });

        ImageView image_fb = (ImageView) findViewById(R.id.image_fb);
        image_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetWorkAvailable.getNetWorkStatus()) {
                    mstrSignUpType = "FACEBOOK";

                } else {
                    customToast("Network connection problem", Login.this);
                }
            }
        });

        TextView tv_footer = (TextView) findViewById(R.id.tv_footer);
        tv_footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.v2softwaresolution.com/"));
                startActivity(browserIntent);
            }
        });

        Bundle b = getIntent().getExtras();
        if (b != null) {
            nPos = b.getInt("POS");
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    public void loginAction(View view) {

        /*if (true) {
            Intent intent = new Intent(Login.this, Home.class);
            startActivity(intent);
            finish();
            return;
        }*/

        mstrEmailId = etUser.getText().toString();
        if (mstrEmailId.isEmpty()) {
            customToast("Registered Id or Phone Number can't be empty", Login.this);
            return;
        }

        /*if (!isValidEmail(mstrEmailId)) {
            customToast("Please enter valid Email Id", Login.this);
            return;
        }*/

        mstrPassword = etPass.getText().toString();
        if (mstrPassword.isEmpty()) {
            customToast("Password can't be empty", Login.this);
            return;
        }

        if (NetWorkAvailable.getNetWorkStatus()) {

            m_nCase = 1;

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("username", mstrEmailId);
                jsonObject.put("password", mstrPassword);
                //jsonObject.put("mobileNo", "");

            } catch (Exception e) {
            }

            new ServiceTask(Login.this, Constants.LOGIN, jsonObject.toString()).execute();

        } else
            customToast(getString(R.string.no_network), this);
    }

    public void signUpAction(View view) {

        Intent intent = new Intent(Login.this, SignUp.class);
        startActivity(intent);
    }

    public void forgotPassword(View view) {

        Intent intent = new Intent(Login.this, Forgot.class);
        startActivity(intent);
    }

    public void SignUp() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("MobileNo", mstrMobile);
            jsonObject.put("Name", mstrFName);
            jsonObject.put("Surname", mstrLName);
            jsonObject.put("PWD", "123");
            jsonObject.put("EmailID", mstrEmailId);
            jsonObject.put("ImageURL", mstrPhotoUrl);
            jsonObject.put("Photo", mstrPhotoUrl);
            jsonObject.put("media_auth", "true");
            jsonObject.put("auth_type", mstrSignUpType);
            jsonObject.put("LoginType", mstrSignUpType);

        } catch (Exception e) {
        }

        Logger.getInstance(Login.this).AuditLogWriter(getString(R.string.app_name), "Login", "SigningUp", "after social media data fetching");

        Log.d("SINGUP", jsonObject.toString());

        m_nCase = 50;

        new ServiceTask(Login.this, Constants.SIGNUP, jsonObject.toString()).execute();

        /*AppPreferences.getInstance(this).setValue("REMEMBER", "true");

        customToast(mstrEmailId, Login.this);

        finish();

        Intent intent = new Intent(Login.this, Home.class);
        startActivity(intent);*/
    }

    public void callBackDataFromAsyncTask(String result) {
        Log.d("LOGIN", result);

        if (result != null && !result.isEmpty()) {

            if (result.contains("\"null\""))
                result = result.replaceAll("\"null\"", "\"\"");

            if (result.contains("null"))
                result = result.replaceAll("null", "\"\"");

            try {

                if (m_nCase != 50) {
                /*{"responseStatus":"OK","statusCode":null,"message":null,"data":{"userDetails":{"createdBy":null,"createdDate":null,"modifiedBy":null,"modifiedDate":null,"id":1,"name":"mahesh","surname":"t","fatherName":null,"phno":"9703111903","alternatePhno":null,"mailid":null,"photUrl":null,"dob":null,"gender":"M","bloodGroup":"A+ve","regid":"726262","password":null,"role":"ROLE_EMP","referedUser":null,"referedRegid":"726262","status":"VERIFIED","isActive":"Y","address1":null,"address2":null,"pincode":null,"subCaste":null,"caste":{"id":2,"name":"BC","code":null,"isActive":null},"qualification":{"id":3,"name":"GRADUATE","code":null,"isActive":"Y"},"profession":{"id":1,"name":"PRIVATE","code":null,"isActive":"Y"},"department":{"id":1,"name":"SOFTWARE","code":null,"isActive":"Y"},"subCasteId":2,"casteId":2,"qualificationId":3,"professionId":1,"departmentId":1,"countryId":null,"stateId":2,"districtId":null,"constituencyId":null,"mandalId":null,"villageId":null,"village":null,"mandal":null,"constituency":null,"district":null,"state":{"id":2,"name":"ANDHRA PRADESH","code":"AP","countryId":1,"isActive":"Y"},"country":null,"otp":"7765"}},"fullData":null,"referUrl":null,"redirectUrl":null,"requestUrl":null}*/

                    String strKey = "";
                    JSONObject dataJson = null;
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject != null && jsonObject.length() > 0) {

                        try {
                            Iterator<String> itr = jsonObject.keys();
                            while (itr.hasNext()) {
                                strKey = itr.next();

                                AppPreferences.getInstance(this).setValue(strKey, jsonObject.getString(strKey));
                            }
                        } catch (Exception e) {}

                        if (jsonObject.getString("responseStatus").toString().toLowerCase(Locale.ENGLISH).equalsIgnoreCase("ok")) {

                            if (jsonObject.has("data")) {

                                dataJson = jsonObject.getJSONObject("data");
                                if (dataJson != null && dataJson.length() > 0) {
                                    if (dataJson.has("userDetails")) {

                                        dataJson = dataJson.getJSONObject("userDetails");
                                        if (dataJson != null && dataJson.length() > 0) {

                                            AppPreferences.getInstance(this).setValue("USER_DETAILS", dataJson.toString());
                                            try {
                                                Iterator<String> itr = dataJson.keys();
                                                while (itr.hasNext()) {
                                                    strKey = itr.next();

                                                    AppPreferences.getInstance(this).setValue(strKey, dataJson.getString(strKey));
                                                }
                                            } catch (Exception e) {}

                                            AppController.user = mstrEmailId;

                                            AppPreferences.getInstance(Login.this).setValue("EMAIL", dataJson.getString("mailid"));
                                            AppPreferences.getInstance(Login.this).setValue("NAME", dataJson.getString("name"));
                                            if (jsonObject.has("surname"))
                                                AppPreferences.getInstance(Login.this).setValue("NAME", dataJson.getString("name") + " " + jsonObject.getString("surname"));
                                            AppPreferences.getInstance(Login.this).setValue("MOBILE", dataJson.getString("phno"));
                                            AppPreferences.getInstance(Login.this).setValue(mstrEmailId + "URL", dataJson.getString("photUrl"));

                                            AppPreferences.getInstance(Login.this).setUserName(mstrEmailId);
                                            AppPreferences.getInstance(Login.this).setPassWord(mstrPassword);

                                            AppPreferences.getInstance(this).setValue("REMEMBER", "true");

                                            finish();
                                            Intent intent = new Intent(Login.this, Home.class);
                                            startActivity(intent);
                                            return;
                                        }
                                    }
                                }
                            }
                        }

                        customToast("Login failed\nCheck your Registered Id or Phone Number & Password", this);
                    }
                } else {

                    if (result != null) {

                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject != null && jsonObject.has("Message")) {

                            if (jsonObject.getString("Message").equalsIgnoreCase("Success")) {

                                try {
                                    JSONObject dataJson = jsonObject.getJSONObject("Details");
                                    if (dataJson != null) {
                                        String strKey = "";
                                        Iterator<String> itr = dataJson.keys();
                                        while (itr.hasNext()) {
                                            strKey = itr.next();

                                            AppPreferences.getInstance(this).setValue(strKey, dataJson.getString(strKey));
                                        }
                                    }

                                    AppPreferences.getInstance(this).setValue("VID", dataJson.getString("VillageID"));
                                    AppPreferences.getInstance(this).setValue("MID", dataJson.getString("MandalID"));
                                    AppPreferences.getInstance(this).setValue("DID", dataJson.getString("DistrictID"));
                                    AppPreferences.getInstance(this).setValue("SID", dataJson.getString("StateID"));

                                    AppController.user = mstrEmailId;
                                    AppPreferences.getInstance(this).setValue("EMAIL", mstrEmailId);
                                    AppPreferences.getInstance(this).setValue("NAME", dataJson.getString("Name"));
                                    if (jsonObject.has("SurName"))
                                        AppPreferences.getInstance(this).setValue("NAME", dataJson.getString("Name") + " " + dataJson.getString("SurName"));
                                    AppPreferences.getInstance(this).setValue("MOBILE", dataJson.getString("MobileNo"));
                                    AppPreferences.getInstance(this).setValue(mstrEmailId + "URL", dataJson.getString("Photo"));

                                    AppPreferences.getInstance(this).setUserName(mstrEmailId);
                                    AppPreferences.getInstance(this).setPassWord(mstrPassword);

                                    AppPreferences.getInstance(this).setValue("REMEMBER", "true");
                                } catch (Exception e) {
                                }

                                customToast("Registered Successfully", Login.this);
                                finish();
                                //Intent intent = new Intent(Login.this, ProfileUpdate.class);
                                /*Intent intent = new Intent(Login.this, ChangePassword.class);
                                intent.putExtra("EMAIL", mstrEmailId);
                                startActivity(intent);*/
                            }
                        }
                    } else
                        customToast(result, Login.this);
                }
            } catch (Exception e) {
                CaughtException(this, e);
                customToast("Login failed\nCheck your Registered Id or Phone Number & Password", this);
            }
        }
    }
}
