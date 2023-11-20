package com.v2soft.apsf.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.usage.NetworkStats;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.v2soft.apsf.R;
import com.v2soft.apsf.logger.Logger;
import com.v2soft.apsf.model.ResultItem;
import com.v2soft.apsf.shared.AppController;
import com.v2soft.apsf.shared.AppPreferences;
import com.v2soft.apsf.shared.Constants;
import com.v2soft.apsf.shared.FontCache;
import com.v2soft.apsf.shared.MyProgressDialog;
import com.v2soft.apsf.shared.NetWorkAvailable;
import com.v2soft.apsf.shared.ServiceTask;
import com.v2soft.apsf.shared.WebServiceCall;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.v2soft.apsf.shared.AppController.mHashBloodGroups;
import static com.v2soft.apsf.shared.AppController.mHashCategories;
import static com.v2soft.apsf.shared.AppController.mHashCountries;
import static com.v2soft.apsf.shared.AppController.mHashDepartments;
import static com.v2soft.apsf.shared.AppController.mHashDistricts;
import static com.v2soft.apsf.shared.AppController.mHashDistrictsList;
import static com.v2soft.apsf.shared.AppController.mHashMandals;
import static com.v2soft.apsf.shared.AppController.mHashMandalsList;
import static com.v2soft.apsf.shared.AppController.mHashProfessions;
import static com.v2soft.apsf.shared.AppController.mHashStates;
import static com.v2soft.apsf.shared.AppController.mHashSubCastes;
import static com.v2soft.apsf.shared.AppController.mHashSubCastesList;
import static com.v2soft.apsf.shared.AppController.mHashVillages;
import static com.v2soft.apsf.shared.AppController.mListBloodGroups;
import static com.v2soft.apsf.shared.AppController.mListCategories;
import static com.v2soft.apsf.shared.AppController.mListCountries;
import static com.v2soft.apsf.shared.AppController.mListDepartments;
import static com.v2soft.apsf.shared.AppController.mListDistricts;
import static com.v2soft.apsf.shared.AppController.mListMandals;
import static com.v2soft.apsf.shared.AppController.mListProfessions;
import static com.v2soft.apsf.shared.AppController.mListQualifications;
import static com.v2soft.apsf.shared.AppController.mListStates;
import static com.v2soft.apsf.shared.AppController.mListSubCastes;
import static com.v2soft.apsf.shared.AppController.mListVillages;
import static com.v2soft.apsf.shared.AppController.mVillagesList;
import static com.v2soft.apsf.shared.Constants.CASTE;
import static com.v2soft.apsf.shared.Constants.DEPARTMENTS;
import static com.v2soft.apsf.shared.Constants.PROFESSIONS;
import static com.v2soft.apsf.shared.Constants.SUBCASTE;
import static com.v2soft.apsf.shared.Utility.CaughtException;
import static com.v2soft.apsf.shared.Utility.customToast;
import static com.v2soft.apsf.shared.Utility.isNumberValid;
import static com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance;

/**
 * Created by srikanth.m on 2/28/2018.
 */

public class SignUp extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private static final int m_nTAKE_PHOTO_CODE = 1;
    private static final int BROWSE_GALLERY = 2;

    AutoCompleteTextView bloodgroup;
    AutoCompleteTextView qualification;
    AutoCompleteTextView country;
    AutoCompleteTextView state;
    AutoCompleteTextView district;
    AutoCompleteTextView mandal;
    AutoCompleteTextView village;

    private EditText referenceCode;
    private EditText nameET;
    private RadioGroup groupGender;
    private EditText phnoET;
    private AutoCompleteTextView profession;
    private AutoCompleteTextView department;
    private AutoCompleteTextView category;
    private AutoCompleteTextView subcaste;

    EditText dobET;
    EditText emailET;
    EditText addressET;
    EditText dob;

    LinearLayout ll_parent;

    Context context;
    Typeface mTypeface;

    int m_nCase = 0;

    public String mstrBloodGroup = "";
    String mstrCaste = "";
    String mstrCategory = "";
    String mstrQualification = "";
    String mstrProfession = "";
    String mstrDepartment = "";
    public String mstrCountry = "India";
    public String mstrState = "";
    public String mstrDistrict = "";
    public String mstrMandal = "";
    public String mstrVillage = "";

    String mstrBloodGroupId = "";
    String mstrCasteId = "";
    String mstrCategoryId = "";
    String mstrQualificationId = "";
    String mstrProfessionId = "";
    String mstrDepartmentId = "";
    String mstrCountryId = "1";
    String mstrStateId = "";
    String mstrDistrictId = "";
    String mstrMandalId = "";
    String mstrVillageId = "";

    DatePickerDialog m_datePickerDialog;

    private File m_outFile;
    //private CircleImageView iv_pic;

    private String userChoosenTask = "";
    private boolean mbIsPhotoTaken = false;

    private HashMap<String, String> mHshMap;

    private CheckBox checkBoxAgree;

    String strProfileUrl = "";
    JSONObject jsonCloudinaryData = null;

    private MyProgressDialog myProgressDialog;

    ArrayList<ResultItem> lstSubItems = new ArrayList<>();
    ArrayList<String> lstMandatoryFields = new ArrayList<>();

    private InputMethodManager inputMethodManager = null;

    private HashMap<String, String> hashNumbers = new HashMap<>();

    private TextView name_error;
    private TextView agree_error;
    private TextView mobile_error;
    private TextView category_error;
    private TextView reference_error;
    private TextView profession_error;
    private TextView department_error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);

        try {
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

            context = this;

            mTypeface = FontCache.getTypeface("ui-text-regular.ttf", this);

            mHshMap = new HashMap<>();

            name_error = (TextView) findViewById(R.id.name_error);
            agree_error = (TextView) findViewById(R.id.agree_error);
            mobile_error = (TextView) findViewById(R.id.mobile_error);
            category_error = (TextView) findViewById(R.id.category_error);
            reference_error = (TextView) findViewById(R.id.reference_error);
            profession_error = (TextView) findViewById(R.id.profession_error);
            department_error = (TextView) findViewById(R.id.department_error);

            name_error.setVisibility(View.GONE);
            agree_error.setVisibility(View.GONE);
            mobile_error.setVisibility(View.GONE);
            category_error.setVisibility(View.GONE);
            reference_error.setVisibility(View.GONE);
            profession_error.setVisibility(View.GONE);
            department_error.setVisibility(View.GONE);

            referenceCode = (EditText) findViewById(R.id.reference);
            referenceCode.setText(AppPreferences.getInstance(this).getValue("regid"));
            referenceCode.setTypeface(mTypeface);
            /*referenceCode.setEnabled(false);
            referenceCode.setVisibility(View.GONE);*/

            nameET = (EditText) findViewById(R.id.name);
            nameET.setTypeface(mTypeface);
            dobET = (EditText) findViewById(R.id.dob);
            phnoET = (EditText) findViewById(R.id.mobile);
            phnoET.setTypeface(mTypeface);
            emailET = (EditText) findViewById(R.id.email);
            addressET = (EditText) findViewById(R.id.village_city);

            groupGender = (RadioGroup) findViewById(R.id.gender);
            RadioButton male = (RadioButton) findViewById(R.id.male);
            male.setTypeface(mTypeface);

            RadioButton female = (RadioButton) findViewById(R.id.female);
            female.setTypeface(mTypeface);

            checkBoxAgree = (CheckBox) findViewById(R.id.agree);
            checkBoxAgree.setTypeface(mTypeface);

            ll_parent = (LinearLayout) findViewById(R.id.ll_parent);
            setTypefaces();

            bloodgroup = (AutoCompleteTextView) findViewById(R.id.bloodgroup);
            subcaste = (AutoCompleteTextView) findViewById(R.id.subcaste);
            category = (AutoCompleteTextView) findViewById(R.id.category);
            category.setTypeface(mTypeface);
            profession = (AutoCompleteTextView) findViewById(R.id.profession);
            profession.setTypeface(mTypeface);
            qualification = (AutoCompleteTextView) findViewById(R.id.qualification);
            department = (AutoCompleteTextView) findViewById(R.id.department);
            department.setTypeface(mTypeface);
            country = (AutoCompleteTextView) findViewById(R.id.country);
            country.setText("India");
            country.setEnabled(false);
            state = (AutoCompleteTextView) findViewById(R.id.state);
            //city = (AutoCompleteTextView) findViewById(R.id.city);
            district = (AutoCompleteTextView) findViewById(R.id.district);
            mandal = (AutoCompleteTextView) findViewById(R.id.mandal);
            village = (AutoCompleteTextView) findViewById(R.id.village);

            dob = (EditText) findViewById(R.id.dob);
            dob.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {

                    showDatePicker();
                    dob.setEnabled(false);
                    return false;
                }
            });

            /*iv_pic = (CircleImageView) findViewById(R.id.iv_pic);
            iv_pic.setBackgroundResource(R.drawable.ic_profile_24dp);

            iv_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mbIsPhotoTaken = false;
                    selectImage();
                    //showPhotoCaptureDialog();
                }
            });*/

            department.setInputType(InputType.TYPE_NULL);
            department.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {

                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

                        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        department.showDropDown();

                        department_error.setText("");
                        department_error.setVisibility(View.GONE);

                        /*if (!mstrProfessionId.isEmpty())
                            department.showDropDown();
                        else
                            Utility.customToast("Please select Profession", Register.this);*/
                    }

                    return false;
                }
            });

            //district.setInputType(InputType.TYPE_NULL);
            district.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {

                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        if (!mstrStateId.isEmpty())
                            district.showDropDown();
                        else
                            customToast("Please select State", SignUp.this);
                    }
                    return false;
                }
            });

            //mandal.setInputType(InputType.TYPE_NULL);
            mandal.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {

                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        if (!mstrDistrictId.isEmpty())
                            mandal.showDropDown();
                        else
                            customToast("Please select District", SignUp.this);
                    }
                    return false;
                }
            });

            //village.setInputType(InputType.TYPE_NULL);
            village.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {

                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        if (!mstrMandalId.isEmpty())
                            village.showDropDown();
                        else
                            customToast("Please select Mandal", SignUp.this);
                    }
                    return false;
                }
            });

            phnoET.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    mobile_error.setText("");
                    mobile_error.setVisibility(View.GONE);
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    phnoET.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    if (s.toString().trim().length() == 10) {
                        inputMethodManager.hideSoftInputFromWindow(phnoET.getWindowToken(), 0);

                        if (isNumberValid(s.toString())) {

                            if (hashNumbers.containsKey(s.toString())) {
                                if (hashNumbers.get(s.toString()).equalsIgnoreCase("true")) {
                                    phnoET.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_done_24dp, 0);
                                } else {
                                    phnoET.setText("");
                                    customToast("Mobile number already exists", SignUp.this);
                                }
                                return;
                            }

                            if (NetWorkAvailable.getNetWorkStatus()) {

                                m_nCase = 11;

                                new ServiceTask(SignUp.this, Constants.IS_NUM_EXISTS, "/" + s.toString()).execute();
                            } else
                                customToast(getString(R.string.no_network), SignUp.this);
                        } else {
                            phnoET.setText("");
                            customToast("Phone number must start with 6, 7, 8 or 9", SignUp.this);
                        }
                    }
                }
            });

            referenceCode.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    reference_error.setText("");
                    reference_error.setVisibility(View.GONE);
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    referenceCode.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    if (s.toString().trim().length() == 14) {
                        inputMethodManager.hideSoftInputFromWindow(referenceCode.getWindowToken(), 0);

                        if (NetWorkAvailable.getNetWorkStatus()) {

                            m_nCase = 12;

                            new ServiceTask(SignUp.this, Constants.IS_REG_ID_EXISTS, "/" + s.toString()).execute();
                        } else
                            customToast(getString(R.string.no_network), SignUp.this);
                    }
                }
            });

            nameET.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    name_error.setText("");
                    name_error.setVisibility(View.GONE);
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            TextView tv_footer = (TextView) findViewById(R.id.tv_footer);
            tv_footer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.text_site)));
                    startActivity(browserIntent);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            CaughtException(this, e);
            finish();
        }

        myProgressDialog = new MyProgressDialog(this);
        myProgressDialog.setCancelable(false);
        myProgressDialog.setInverseBackgroundForced(true);
        myProgressDialog.setCanceledOnTouchOutside(false);
        myProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myProgressDialog.show();

        mListBloodGroups.clear();
        mHashBloodGroups.clear();

        mListBloodGroups.add("A+ve");
        mListBloodGroups.add("A-ve");
        mListBloodGroups.add("B+ve");
        mListBloodGroups.add("B-ve");
        mListBloodGroups.add("AB+VE");
        mListBloodGroups.add("AB-ve");
        mListBloodGroups.add("O+ve");
        mListBloodGroups.add("O-ve");

        for (int i = 0; i < mListBloodGroups.size(); i++) {
            mHashBloodGroups.put(mListBloodGroups.get(i), "" + i);
        }

        loadBloodGroups();

        lstMandatoryFields.add("Reference Code");
        lstMandatoryFields.add("Full Name");
        lstMandatoryFields.add("Mobile Number");
        lstMandatoryFields.add("Profession");
        lstMandatoryFields.add("Department");
        lstMandatoryFields.add("Caste");

        if (NetWorkAvailable.getNetWorkStatus()) {

            new LoadData().execute();
        } else
            customToast(getString(R.string.no_network), SignUp.this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    public void signUpAction(View view) {

        //String strValid = validateInput();
        String strValid = "Success";
        if (strValid.equalsIgnoreCase("Success")) {

            if (NetWorkAvailable.getNetWorkStatus()) {

                String strNumber = phnoET.getText().toString().trim();
                if (strNumber.isEmpty() || strNumber.length() != 10) {
                    //customToast("Please enter valid Phone Number", SignUp.this);
                    mobile_error.setText("Please enter valid Phone Number");
                    mobile_error.setVisibility(View.VISIBLE);
                    return;
                }

                String strRedId = referenceCode.getText().toString().trim();
                if (!strRedId.isEmpty() && strRedId.length() != 14) {
                    referenceCode.setText("");
                    //customToast("Please enter valid Reference Code", SignUp.this);
                    reference_error.setText("Please enter valid Reference Code");
                    reference_error.setVisibility(View.VISIBLE);
                    return;
                } else {
                    reference_error.setText("");
                    reference_error.setVisibility(View.GONE);
                }

                String strName = nameET.getText().toString().trim();
                if (strName.isEmpty()) {
                    //customToast("Please enter Full Name", SignUp.this);
                    name_error.setText("Please enter Full Name");
                    name_error.setVisibility(View.VISIBLE);
                    return;
                }

                int radioId = groupGender.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) findViewById(radioId);
                String strGender = radioButton.getText().toString();
                strGender = strGender.equalsIgnoreCase("Male") ? "M" : "F";

                /*if (!Utility.isValidEmail(mHshMap.get("Email"))) {
                    customToast("Please enter valid Email", Register.this);
                    return;
                }*/

                /*if (!etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
                    customToast("Password and Confirm Password are not same", Register.this);
                    return;
                }*/

                mstrProfession = profession.getText().toString().trim();
                mstrProfessionId = mHashProfessions.get(mstrProfession);
                if (mstrProfessionId == null) {
                    //customToast("Please select valid Profession", SignUp.this);
                    profession.setText("");
                    profession_error.setText("Please select valid Profession");
                    profession_error.setVisibility(View.VISIBLE);
                    return;
                }

                mstrDepartment = department.getText().toString().trim();
                mstrDepartmentId = mHashDepartments.get(mstrDepartment);
                if (mstrDepartmentId == null) {
                    //customToast("Please select valid Department", SignUp.this);
                    department.setText("");
                    department_error.setText("Please select valid Department");
                    department_error.setVisibility(View.VISIBLE);
                    return;
                }

                mstrCategory = category.getText().toString().trim();
                mstrCategoryId = mHashCategories.get(mstrCategory);
                if (mstrCategoryId == null) {
                    //customToast("Please select valid Category", SignUp.this);
                    category.setText("");
                    category_error.setText("Please select valid Category");
                    category_error.setVisibility(View.VISIBLE);
                    return;
                }

                /*mstrCaste = subcaste.getText().toString().trim();
                mstrCasteId = mHashSubCastes.get(mstrCaste);
                if (mstrCasteId == null) {
                    customToast("Please select valid Caste", SignUp.this);
                    subcaste.setText("");
                    return;
                }*/

                if (!checkBoxAgree.isChecked()) {
                    //customToast("Please agree Terms & Conditions", SignUp.this);
                    agree_error.setText("Please agree Terms & Conditions");
                    agree_error.setVisibility(View.VISIBLE);
                    return;
                }

                /*if (!mbIsPhotoTaken) {
                    Utility.customToast("Please select profile picture", Register.this);
                    return;
                }

                new UploadFile().execute();*/

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("name", strName);
                    jsonObject.put("surname", null);
                    jsonObject.put("fatherName", null);
                    jsonObject.put("phno", strNumber);
                    jsonObject.put("alternatePhno", strNumber);
                    jsonObject.put("mailid", null);
                    jsonObject.put("photUrl", null);
                    jsonObject.put("dob", null);
                    jsonObject.put("gender", strGender);
                    jsonObject.put("bloodGroup", null);
                    jsonObject.put("password", null);
                    jsonObject.put("referedRegid", referenceCode.getText().toString().trim());
                    jsonObject.put("status", null);
                    jsonObject.put("isActive", null);
                    jsonObject.put("address1", null);
                    jsonObject.put("address2", null);
                    jsonObject.put("pincode", null);
                    jsonObject.put("subCasteId", mstrCasteId);
                    jsonObject.put("casteId", mstrCategoryId);
                    jsonObject.put("qualificationId", null);
                    jsonObject.put("professionId", mstrProfessionId);
                    jsonObject.put("departmentId", mstrDepartmentId);

                    jsonObject.put("countryId", "1");
                    jsonObject.put("stateId", null);
                    jsonObject.put("districtId", null);
                    jsonObject.put("constituencyId", null);
                    jsonObject.put("mandalId", null);
                    jsonObject.put("villageId", null);

                } catch (Exception e) {
                }

                Log.d("SINGUP", jsonObject.toString());
                Logger.getInstance(this).AuditLogWriter(AppController.user, "Reference Form", "request data", jsonObject.toString());

                m_nCase = 50;

                new ServiceTask(SignUp.this, Constants.SIGNUP, jsonObject.toString()).execute();

            } else
                customToast(getString(R.string.no_network), context);
        } else
            customToast("Please Enter " + strValid, SignUp.this);
    }

    /*public class UploadFile extends AsyncTask<Void, Void, JSONObject> {

        MyProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mProgressDialog = new MyProgressDialog(Register.this); // ProgressDialog.THEME_HOLO_LIGHT);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setInverseBackgroundForced(true);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mProgressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(Void... arg0) {
            // TODO Auto-generated method stub

            try {

                if (jsonCloudinaryData == null) {
                    if (m_outFile != null && m_outFile.exists()) {
                        Map config = new HashMap();
                        config.put("cloud_name", getString(R.string.cloud_name));
                        config.put("api_key", getString(R.string.api_key));
                        config.put("api_secret", getString(R.string.api_secret));
                        Cloudinary cloudinary = new Cloudinary(config);

                        jsonCloudinaryData = cloudinary.uploader().upload(m_outFile.getAbsolutePath(), Cloudinary.emptyMap());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return jsonCloudinaryData;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            // TODO Auto-generated method stub
            mProgressDialog.dismiss();

            if (result != null) {

                Log.d("CLOUDINARY", result.toString());

                try {

                    if (result.has("url"))
                        strProfileUrl = result.getString("url");

                } catch (Exception e) {
                }

                int radioId = groupGender.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) findViewById(radioId);
                String strGender = radioButton.getText().toString();
                strGender = strGender.equalsIgnoreCase("Male") ? "1" : "0";

                radioId = groupBlood.getCheckedRadioButtonId();
                radioButton = (RadioButton) findViewById(radioId);
                String strBlood = radioButton.getText().toString();
                strBlood = strBlood.equalsIgnoreCase("Yes") ? "1" : "0";

                radioId = groupPrivacy.getCheckedRadioButtonId();
                radioButton = (RadioButton) findViewById(radioId);
                String strPrivacy = radioButton.getText().toString();
                strPrivacy = strPrivacy.equalsIgnoreCase("Yes") ? "1" : "0";

                radioId = groupNRI.getCheckedRadioButtonId();
                radioButton = (RadioButton) findViewById(radioId);
                String strNRI = radioButton.getText().toString();
                strNRI = strNRI.equalsIgnoreCase("Yes") ? "Y" : "0";

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("MobileNo", mHshMap.get("Mobile Number").toString());
                    jsonObject.put("Name", mHshMap.get("Name").toString());
                    jsonObject.put("Surname", mHshMap.get("Surname").toString());
                    jsonObject.put("PWD", mHshMap.get("Password").toString());
                    jsonObject.put("DOB", mHshMap.get("Date of Birth").toString());
                    jsonObject.put("Gender", strGender);
                    jsonObject.put("CountryID", "1");
                    jsonObject.put("StateID", mstrStateId);
                    jsonObject.put("DistrictID", mstrDistrictId);
                    jsonObject.put("MandalID", mstrMandalId);
                    jsonObject.put("VillageID", mstrVillageId);
                    jsonObject.put("SubCasteID", mstrCasteId);
                    jsonObject.put("EmailID", mHshMap.get("Email").toString());
                    jsonObject.put("BGID", mstrBloodGroupId);
                    jsonObject.put("BloodDonar", strBlood);
                    jsonObject.put("QualificationID", mstrQualificationId);
                    jsonObject.put("ProfessionID", mstrProfessionId);
                    jsonObject.put("DeptID", mstrDepartmentId);
                    jsonObject.put("SharePermission", strPrivacy);
                    jsonObject.put("ContactPrivacy", strPrivacy);
                    jsonObject.put("AreyouNRI", strNRI);
                    jsonObject.put("ImageURL", strProfileUrl);
                    jsonObject.put("Photo", strProfileUrl);
                } catch (Exception e) {
                }

                Log.d("SINGUP", jsonObject.toString());

                m_nCase = 50;

                new ServiceTask(Register.this, Constants.SIGNUP, jsonObject.toString()).execute();
            }
        }
    }*/

    public void setTypefaces() {

        View view = null;
        int nCount = ll_parent.getChildCount();
        for (int i = 0; i < nCount; i++) {

            view = ll_parent.getChildAt(i);
            if (view != null) {
                if (view instanceof AutoCompleteTextView)
                    ((AutoCompleteTextView) view).setTypeface(mTypeface);
                else if (view instanceof EditText)
                    ((EditText) view).setTypeface(mTypeface);
                else if (view instanceof TextView)
                    ((TextView) view).setTypeface(mTypeface);
            }
        }
    }

    public String validateInput() {

        View view = null;
        String strValue = "";
        int nCount = ll_parent.getChildCount();
        for (int i = 0; i < nCount; i++) {

            view = ll_parent.getChildAt(i);
            if (view != null && view.getVisibility() == View.VISIBLE) {
                if (view instanceof EditText || view instanceof AutoCompleteTextView) {
                    if (view instanceof AutoCompleteTextView)
                        strValue = ((AutoCompleteTextView) view).getText().toString();
                    else if (view instanceof EditText)
                        strValue = ((EditText) view).getText().toString();

                    if (lstMandatoryFields.contains(view.getTag().toString()) && strValue.isEmpty())
                        return view.getTag().toString();
                    else
                        mHshMap.put(view.getTag().toString(), strValue);
                }
            }
        }
        return "Success";
    }

    private String getValue(String strField) {
        try {
            View view = ll_parent.findViewWithTag(strField);
            if (view != null) {
                if (view instanceof EditText) {
                    return ((EditText) view).getText().toString();
                } else if (view instanceof AutoCompleteTextView) {
                    return ((AutoCompleteTextView) view).getText().toString();
                }
            }
        } catch (Exception e) {

        }
        return "";
    }

    public void showDatePicker() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR); // current year
        int mMonth = c.get(Calendar.MONTH); // current month
        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

        // date picker dialog
        m_datePickerDialog = newInstance(SignUp.this, mYear, mMonth, mDay);
        m_datePickerDialog.setThemeDark(false);
        m_datePickerDialog.showYearPickerFirst(false);
        m_datePickerDialog.setTitle("Date of Birth");
        m_datePickerDialog.setAccentColor(getResources().getColor(R.color.colorPrimary));

        /*Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        m_datePickerDialog.setMinDate(cal);*/
        m_datePickerDialog.setMaxDate(Calendar.getInstance());
        m_datePickerDialog.show(getFragmentManager(), "Date of Birth");

        m_datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dob.setEnabled(true);
            }
        });
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        // set day of month , month and year value in the edit text
        dob.setEnabled(true);

        monthOfYear = monthOfYear + 1;

        String strDay = "" + dayOfMonth;
        String strMonth = "" + monthOfYear;

        if (dayOfMonth < 10)
            strDay = "0" + dayOfMonth;
        if (monthOfYear < 10)
            strMonth = "0" + monthOfYear;

        dob.setText(strDay + "/" + strMonth + "/" + year);
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Gallery",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {

                    userChoosenTask = "Take Photo";
                    cameraIntent();

                } else if (items[item].equals("Choose from Gallery")) {

                    userChoosenTask = "Choose from Gallery";
                    galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    /*public void showPhotoCaptureDialog() {
        final Dialog dialog = new Dialog(Register.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.attach);
        dialog.show();

        LinearLayout ll_takePicture = (LinearLayout) dialog.findViewById(R.id.ll_takePicture);

        LinearLayout ll_useGallary = (LinearLayout) dialog.findViewById(R.id.ll_useGallary);

        ImageView iv_close = (ImageView) dialog.findViewById(R.id.img_close);

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ll_takePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "Take a Picture", Toast.LENGTH_LONG).show();
                dialog.dismiss();

                userChoosenTask = "Photo";
                cameraIntent();
            }
        });

        ll_useGallary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "Use Gallary", Toast.LENGTH_LONG).show();
                dialog.dismiss();

                userChoosenTask = "Gallery";
                galleryIntent();
            }
        });
    }*/

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), BROWSE_GALLERY);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, m_nTAKE_PHOTO_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            try {
                switch (requestCode) {
                    case m_nTAKE_PHOTO_CODE: {
                        //onCaptureImageResult(data);
                    }
                    break;
                    case BROWSE_GALLERY: {
                        //onSelectFromGalleryResult(data);
                    }
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /*private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        if (thumbnail == null)
            return;

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        m_outFile = getTempFile(Register.this);
        File destination = new File(m_outFile.getAbsolutePath());

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (thumbnail != null) {
            mbIsPhotoTaken = true;
            try {
                //thumbnail = modifyOrientation(thumbnail, m_outFile.getAbsolutePath());
                iv_pic.setImageBitmap(roundCornerImage(thumbnail, 10));
                iv_pic.setRotation(90);
            } catch (Exception e) {
            }
        }
    }*/

    public static Bitmap modifyOrientation(Bitmap bitmap, String image_absolute_path) throws IOException {
        ExifInterface ei = new ExifInterface(image_absolute_path);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotate(bitmap, 90);

            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotate(bitmap, 180);

            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotate(bitmap, 270);

            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                return flip(bitmap, true, false);

            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                return flip(bitmap, false, true);

            default:
                return bitmap;
        }
    }

    public static Bitmap rotate(Bitmap bitmap, float degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static Bitmap flip(Bitmap bitmap, boolean horizontal, boolean vertical) {
        Matrix matrix = new Matrix();
        matrix.preScale(horizontal ? -1 : 1, vertical ? -1 : 1);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public Bitmap roundCornerImage(Bitmap raw, float round) {
        int width = raw.getWidth();
        int height = raw.getHeight();
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        canvas.drawARGB(0, 0, 0, 0);

        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.parseColor("#000000"));

        final Rect rect = new Rect(0, 0, width, height);
        final RectF rectF = new RectF(rect);

        canvas.drawRoundRect(rectF, round, round, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        canvas.drawBitmap(raw, rect, rect, paint);

        return result;
    }

    private File getTempFile(Context context) {
        try {
            final File filePath = new File(Environment.getExternalStorageDirectory(), getString(R.string.app_name) + "/images/ProfilePics/");
            if (!filePath.exists())//create directory if it does not exist
            {
                if (!filePath.mkdirs()) {
                    Log.e("MyCameraApp", "failed to create directory");
                    return null;
                }
            }

            File fileNewFile = new File(filePath, "profile_pic.jpg");

            return fileNewFile;
        } catch (Exception e) {
            Log.e("ERROR", e.toString());
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("deprecation")
    /*private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                //bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                bm = getScaledBitmap(data.getData());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (bm == null)
            return;

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        m_outFile = getTempFile(Register.this);
        File destination = new File(m_outFile.getAbsolutePath());

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mbIsPhotoTaken = true;
        iv_pic.setImageBitmap(bm);
    }*/

    private Bitmap getScaledBitmap(Uri uri) {
        Bitmap thumb = null;
        try {
            ContentResolver cr = getContentResolver();
            InputStream in = cr.openInputStream(uri);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            thumb = BitmapFactory.decodeStream(in, null, options);
        } catch (FileNotFoundException e) {
            customToast("File not found", SignUp.this);
        }
        return thumb;
    }

    public void callBackDataFromAsyncTask(String result) {
        Log.d("RESPONSE", result);

        if (result != null && !result.isEmpty()) {

            if (!result.contains("FileNotFoundException")) {
                try {

                    if (m_nCase != 50) {

                        if (m_nCase == 9) { // villages

                            JSONArray jsonArray = new JSONArray(result);

                            Gson gson = new Gson();

                            if (jsonArray != null && jsonArray.length() > 0) {
                                Type listType = new TypeToken<ArrayList<ResultItem>>() {
                                }.getType();
                                mVillagesList.clear();
                                ArrayList<ResultItem> ist = gson.fromJson(jsonArray.toString(), listType);
                                mVillagesList.addAll(ist);

                                mListVillages.clear();
                                mHashVillages.clear();

                                ResultItem c = null;
                                if (mVillagesList.size() > 0) {
                                    for (int i = 0; i < mVillagesList.size(); i++) {
                                        c = mVillagesList.get(i);
                                        if (c != null) {
                                            mListVillages.add(c.name.trim());
                                            mHashVillages.put(c.name.trim(), "" + c.id);
                                        }
                                    }

                                    loadVillages();
                                } else
                                    customToast("No data available", context);
                            }
                        } else if (m_nCase == 10) { // submit

                        } else if (m_nCase == 11) { // ismobilemomxists

                            if (result != null) {

                                JSONObject jsonObject = new JSONObject(result);

                                if (jsonObject != null && jsonObject.length() > 0) {
                                    if (jsonObject.has("responseStatus") && jsonObject.getString("responseStatus").toLowerCase(Locale.ENGLISH).equalsIgnoreCase("ok")) {

                                        hashNumbers.put(phnoET.getText().toString(), "true");
                                        phnoET.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_done_24dp, 0);
                                        referenceCode.requestFocus();
                                        return;
                                    }

                                    hashNumbers.put(phnoET.getText().toString(), "false");
                                    phnoET.setText("");
                                    //customToast(jsonObject.getString("message"), context);
                                    mobile_error.setVisibility(View.VISIBLE);
                                    mobile_error.setText(jsonObject.getString("message"));
                                }
                            } else {
                                //customToast(result, context);
                                mobile_error.setVisibility(View.VISIBLE);
                                mobile_error.setText(result);
                            }

                        } else if (m_nCase == 12) { // isexistregid

                            if (result != null) {

                                JSONObject jsonObject = new JSONObject(result);

                                if (jsonObject != null && jsonObject.length() > 0) {
                                    if (jsonObject.has("responseStatus") && jsonObject.getString("responseStatus").toLowerCase(Locale.ENGLISH).equalsIgnoreCase("ok")) {

                                        referenceCode.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_done_24dp, 0);
                                        nameET.requestFocus();
                                        return;
                                    }

                                    referenceCode.setText("");
                                    //customToast(jsonObject.getString("message"), context);
                                    mobile_error.setVisibility(View.VISIBLE);
                                    mobile_error.setText(jsonObject.getString("message"));
                                }
                            } else {
                                //customToast(result, context);
                                mobile_error.setVisibility(View.VISIBLE);
                                mobile_error.setText(result);
                            }
                        }
                    } else {

                        if (result != null) {

                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject != null && jsonObject.length() > 0) {

                                if (jsonObject.has("responseStatus") && jsonObject.getString("responseStatus").toLowerCase(Locale.ENGLISH).equalsIgnoreCase("ok")) {

                                    //customToast(jsonObject.getString("message"), context);

                                    showMessage(getString(R.string.app_name), jsonObject.getString("message"));
                                    return;
                                }

                                customToast(jsonObject.getString("message"), context);
                            }
                        } else
                            customToast(result, context);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    customToast("Unable to register user", context);
                }
            } else
                customToast("User already registered with these details", context);
        } /*else
            customToast("Unable to register user", context);*/
    }

    public void showMessage(String strTitle, String strMsg) {
        try {
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(R.layout.info_dialog);
            //dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);

            TextView tv_title = (TextView) dialog.findViewById(R.id.tv_title);
            tv_title.setText(strTitle);

            TextView tv_msg = (TextView) dialog.findViewById(R.id.tv_msg);
            tv_msg.setText(strMsg);

            Button btn_cancel = (Button) dialog.findViewById(R.id.btn_no);
            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            Button btn_ok = (Button) dialog.findViewById(R.id.btn_yes);
            btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();

                    finish();
                }
            });

            dialog.show();
        } catch (Exception e) {
        }
    }

    public void loadCountries() {
        if (mListCountries.size() > 0) {

            ArrayAdapter<String> a = new ArrayAdapter<String>(SignUp.this, R.layout.dropdown_row, R.id.textView1, mListCountries);
            a.notifyDataSetChanged();
            country.setAdapter(a);
            country.setInputType(InputType.TYPE_NULL);

            country.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    country.showDropDown();
                    return false;
                }
            });

            country.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    mstrCountry = country.getText().toString();
                    mstrCountryId = mHashCountries.get(mstrCountry);

                    if (NetWorkAvailable.getNetWorkStatus()) {
                        m_nCase = 6;

                        new ServiceTask(SignUp.this, Constants.STATES, "?CountryID=" + mstrCountryId).execute();
                    } else
                        customToast(getString(R.string.no_network), context);
                }
            });
        }
    }

    public void loadStates() {
        if (mListStates.size() > 0) {

            ArrayAdapter<String> a = new ArrayAdapter<String>(SignUp.this, R.layout.dropdown_row, R.id.textView1, mListStates);
            a.notifyDataSetChanged();
            state.setAdapter(a);
            state.setInputType(InputType.TYPE_NULL);

            state.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    state.showDropDown();
                    return false;
                }
            });

            state.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    mstrState = state.getText().toString().trim();
                    mstrStateId = mHashStates.get(mstrState);

                    lstSubItems = mHashDistrictsList.get(mstrStateId);
                    if (lstSubItems != null) {
                        for (ResultItem item : lstSubItems) {
                            mListDistricts.add(item.name);
                            mHashDistricts.put(item.name, "" + item.id);
                        }
                    }

                    loadDistricts();
                }
            });
        }
    }

    public void loadDistricts() {
        if (mListDistricts.size() > 0) {

            ArrayAdapter<String> a = new ArrayAdapter<String>(SignUp.this, R.layout.dropdown_row, R.id.textView1, mListDistricts);
            a.notifyDataSetChanged();
            district.setAdapter(a);
            district.setInputType(InputType.TYPE_NULL);

            district.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    mstrDistrict = district.getText().toString().trim();
                    mstrDistrictId = mHashDistricts.get(mstrDistrict);

                    lstSubItems = mHashMandalsList.get(mstrDistrictId);
                    if (lstSubItems != null) {
                        for (ResultItem item : lstSubItems) {
                            mListMandals.add(item.name);
                            mHashMandals.put(item.name, "" + item.id);
                        }
                    }

                    loadMandals();
                }
            });
        }
    }

    public void loadMandals() {
        if (mListMandals.size() > 0) {

            ArrayAdapter<String> a = new ArrayAdapter<String>(SignUp.this, R.layout.dropdown_row, R.id.textView1, mListMandals);
            a.notifyDataSetChanged();
            mandal.setAdapter(a);
            mandal.setInputType(InputType.TYPE_NULL);

            mandal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    mstrMandal = mandal.getText().toString().trim();
                    mstrMandalId = mHashMandals.get(mstrMandal);

                    if (NetWorkAvailable.getNetWorkStatus()) {
                        m_nCase = 9;

                        new ServiceTask(SignUp.this, Constants.VILLAGES, "/" + mstrMandalId).execute();
                    } else
                        customToast(getString(R.string.no_network), context);
                }
            });
        }
    }

    public void loadVillages() {
        if (mListVillages.size() > 0) {

            ArrayAdapter<String> a = new ArrayAdapter<String>(SignUp.this, R.layout.dropdown_row, R.id.textView1, mListVillages);
            a.notifyDataSetChanged();
            village.setAdapter(a);
            village.setInputType(InputType.TYPE_NULL);

            village.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    village.showDropDown();
                    return false;
                }
            });

            village.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    mstrVillage = village.getText().toString().trim();
                    mstrVillageId = mHashVillages.get(mstrVillage);
                }
            });
        }
    }

    public void loadBloodGroups() {
        if (mListBloodGroups.size() > 0) {

            ArrayAdapter<String> a = new ArrayAdapter<String>(SignUp.this, R.layout.dropdown_row, R.id.textView1, mListBloodGroups);
            a.notifyDataSetChanged();
            bloodgroup.setAdapter(a);
            bloodgroup.setInputType(InputType.TYPE_NULL);

            bloodgroup.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    bloodgroup.showDropDown();
                    return false;
                }
            });

            bloodgroup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    mstrBloodGroup = bloodgroup.getText().toString().trim();
                    mstrBloodGroupId = mHashBloodGroups.get(mstrBloodGroup);
                }
            });
        }
    }

    public void loadCategory() {
        if (mListCategories.size() > 0) {

            ArrayAdapter<String> a = new ArrayAdapter<String>(SignUp.this, R.layout.dropdown_row, R.id.textView1, mListCategories);
            a.notifyDataSetChanged();
            category.setAdapter(a);
            category.setInputType(InputType.TYPE_NULL);

            category.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {

                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    category.showDropDown();
                    return false;
                }
            });

            category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    category_error.setText("");
                    category_error.setVisibility(View.GONE);

                    mstrCategory = category.getText().toString().trim();
                    mstrCategoryId = mHashCategories.get(mstrCategory);

                    lstSubItems = mHashSubCastesList.get(mstrCategory);
                    if (lstSubItems != null && lstSubItems.size() > 0) {

                        mListSubCastes.clear();
                        mHashSubCastes.clear();

                        for (ResultItem item : lstSubItems) {
                            mListSubCastes.add(item.name);
                            mHashSubCastes.put(item.name, "" + item.id);
                        }

                        subcaste.setText("");
                        mstrCaste = "";
                        mstrCasteId = "";

                        //loadSubCastes();
                    }
                }
            });
        }
    }

    public void loadSubCastes() {
        if (mListSubCastes.size() > 0) {

            ArrayAdapter<String> a = new ArrayAdapter<String>(SignUp.this, R.layout.dropdown_row, R.id.textView1, mListSubCastes);
            a.notifyDataSetChanged();
            subcaste.setAdapter(a);
            subcaste.setInputType(InputType.TYPE_NULL);

            subcaste.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {

                    if (!mstrCategory.isEmpty())
                        subcaste.showDropDown();
                    else
                        customToast("Please select Category", SignUp.this);
                    return false;
                }
            });

            subcaste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    mstrCaste = subcaste.getText().toString().trim();
                    mstrCasteId = mHashSubCastes.get(mstrCaste);
                }
            });
        }
    }

    public void loadProfessions() {
        if (mListProfessions.size() > 0) {

            ArrayAdapter<String> a = new ArrayAdapter<String>(SignUp.this, R.layout.dropdown_row, R.id.textView1, mListProfessions);
            a.notifyDataSetChanged();
            profession.setAdapter(a);
            profession.setInputType(InputType.TYPE_NULL);

            profession.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {

                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    profession.showDropDown();
                    return false;
                }
            });

            profession.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    mstrProfession = profession.getText().toString().trim();
                    mstrProfessionId = mHashProfessions.get(mstrProfession);

                    profession_error.setText("");
                    profession_error.setVisibility(View.GONE);

                    loadDepartments();
                }
            });
        }
    }

    public void loadQualifications() {
        if (mListQualifications.size() > 0) {

            ArrayAdapter<String> a = new ArrayAdapter<String>(SignUp.this, R.layout.dropdown_row, R.id.textView1, mListQualifications);
            a.notifyDataSetChanged();
            qualification.setAdapter(a);
            qualification.setInputType(InputType.TYPE_NULL);

            qualification.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    qualification.showDropDown();
                    return false;
                }
            });

            qualification.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    mstrQualification = qualification.getText().toString().trim();
                    mstrQualificationId = mHashProfessions.get(mstrQualification);
                }
            });
        }
    }

    public void loadDepartments() {
        if (mListDepartments.size() > 0) {

            ArrayAdapter<String> a = new ArrayAdapter<String>(SignUp.this, R.layout.dropdown_row, R.id.textView1, mListDepartments);
            a.notifyDataSetChanged();
            department.setAdapter(a);
            //department.setInputType(InputType.TYPE_NULL);

            department.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {

                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    department.showDropDown();
                    return false;
                }
            });

            department.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    department_error.setText("");
                    department_error.setVisibility(View.GONE);

                    mstrDepartment = department.getText().toString().trim();
                    mstrDepartmentId = mHashDepartments.get(mstrDepartment);
                }
            });
        }
    }

    private class LoadData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPostExecute(Void aVoid) {
            myProgressDialog.dismiss();

            //loadQualifications();

            loadProfessions();

            loadDepartments();

            //loadStates();

            loadCategory();

            loadSubCastes();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                // ADDED CODE
                if (NetWorkAvailable.getNetWorkStatus()) {
                    String strResponse = "";
                    Gson gson = new Gson();
                    ArrayList<ResultItem> lstVals = new ArrayList<>();

                    // countries
                    /*try {
                        strResponse = WebServiceCall.callGETRequest(Constants.URL + COUNTRIES, "");
                        if (strResponse != null) {

                            Type listType = new TypeToken<ArrayList<ResultItem>>() {
                            }.getType();
                            AppController.mCountriesList.clear();
                            ArrayList<ResultItem> ist = gson.fromJson(strResponse, listType);
                            AppController.mCountriesList.addAll(ist);
                        }

                        if (AppController.mCountriesList.size() > 0) {
                            AppController.mHashCountries.clear();
                            AppController.mListCountries.clear();

                            for (ResultItem item : AppController.mCountriesList) {
                                AppController.mHashCountries.put(item.name, "" + item.id);
                                AppController.mListCountries.add(item.name);
                            }
                        }
                    } catch (Exception e) {
                        CaughtException(Register.this, e);
                    }*/

                    // states
                    /*try {
                        strResponse = WebServiceCall.callGETRequest(Constants.URL + STATES, "");
                        if (strResponse != null) {

                            Type listType = new TypeToken<ArrayList<ResultItem>>() {
                            }.getType();
                            AppController.mStatesList.clear();
                            ArrayList<ResultItem> ist = gson.fromJson(strResponse, listType);
                            AppController.mStatesList.addAll(ist);
                        }

                        if (AppController.mStatesList.size() > 0) {
                            AppController.mHashStates.clear();
                            AppController.mListStates.clear();

                            for (ResultItem item : AppController.mStatesList) {
                                AppController.mHashStates.put(item.name, "" + item.id);
                                AppController.mListStates.add(item.name);
                            }
                        }
                    } catch (Exception e) {
                        CaughtException(Register.this, e);
                    }*/

                    // districts
                    /*try {
                        strResponse = WebServiceCall.callGETRequest(Constants.URL + DISTRICTS, "");
                        if (strResponse != null) {

                            Type listType = new TypeToken<ArrayList<ResultItem>>() {
                            }.getType();
                            AppController.mDistrictList.clear();
                            ArrayList<ResultItem> ist = gson.fromJson(strResponse, listType);
                            AppController.mDistrictList.addAll(ist);
                        }

                        if (AppController.mDistrictList.size() > 0) {
                            AppController.mHashDistricts.clear();
                            AppController.mListDistricts.clear();

                            for (ResultItem item : AppController.mDistrictList) {
                                AppController.mHashDistricts.put(item.name, "" + item.id);
                                AppController.mListDistricts.add(item.name);

                                lstVals = new ArrayList<>();
                                if (mHashDistrictsList.containsKey("" + item.stateId))
                                    lstVals = mHashDistrictsList.get("" + item.stateId);

                                if (lstVals == null)
                                    lstVals = new ArrayList<>();
                                lstVals.add(item);
                                mHashDistrictsList.put("" + item.stateId, lstVals);
                            }
                        }
                    } catch (Exception e) {
                        CaughtException(Register.this, e);
                    }*/

                    // mandals
                    /*try {
                        strResponse = WebServiceCall.callGETRequest(Constants.URL + MANDALS, "");
                        if (strResponse != null) {

                            Type listType = new TypeToken<ArrayList<ResultItem>>() {
                            }.getType();
                            AppController.mMandalsList.clear();
                            ArrayList<ResultItem> ist = gson.fromJson(strResponse, listType);
                            AppController.mMandalsList.addAll(ist);
                        }

                        if (AppController.mMandalsList.size() > 0) {
                            AppController.mHashMandals.clear();
                            AppController.mListMandals.clear();

                            for (ResultItem item : AppController.mMandalsList) {
                                AppController.mHashMandals.put(item.name, "" + item.id);
                                AppController.mListMandals.add(item.name);

                                lstVals = new ArrayList<>();
                                if (mHashMandalsList.containsKey("" + item.districtId))
                                    lstVals = mHashMandalsList.get("" + item.districtId);

                                if (lstVals == null)
                                    lstVals = new ArrayList<>();
                                lstVals.add(item);
                                mHashMandalsList.put("" + item.districtId, lstVals);
                            }
                        }
                    } catch (Exception e) {
                        CaughtException(Register.this, e);
                    }*/

                    // constituencies
                    /*try {
                        strResponse = WebServiceCall.callGETRequest(Constants.URL + CONSTITUENCIES, "");
                        if (strResponse != null) {

                            Type listType = new TypeToken<ArrayList<ResultItem>>() {
                            }.getType();
                            AppController.mConstituencesList.clear();
                            ArrayList<ResultItem> ist = gson.fromJson(strResponse, listType);
                            AppController.mConstituencesList.addAll(ist);
                        }

                        if (AppController.mConstituencesList.size() > 0) {
                            AppController.mHashConstituences.clear();
                            AppController.mListConstituences.clear();

                            for (ResultItem item : AppController.mConstituencesList) {
                                AppController.mHashConstituences.put(item.name, "" + item.id);
                                AppController.mListConstituences.add(item.name);
                            }
                        }
                    } catch (Exception e) {
                        CaughtException(Register.this, e);
                    }*/

                    // QUALIFICATIONS
                    /*try {
                        strResponse = WebServiceCall.callGETRequest(Constants.URL + QUALIFICATIONS, "");
                        if (strResponse != null) {

                            Type listType = new TypeToken<ArrayList<ResultItem>>() {
                            }.getType();
                            AppController.mQualificationsList.clear();
                            ArrayList<ResultItem> ist = gson.fromJson(strResponse, listType);
                            AppController.mQualificationsList.addAll(ist);
                        }

                        if (AppController.mQualificationsList.size() > 0) {
                            AppController.mHashQualifications.clear();
                            AppController.mListQualifications.clear();

                            for (ResultItem item : AppController.mQualificationsList) {
                                AppController.mHashQualifications.put(item.name, "" + item.id);
                                AppController.mListQualifications.add(item.name);
                            }
                        }
                    } catch (Exception e) {
                        CaughtException(Register.this, e);
                    }*/

                    // PROFESSIONS
                    try {
                        strResponse = WebServiceCall.callGETRequest(Constants.URL + PROFESSIONS, "");
                        if (strResponse != null) {

                            Type listType = new TypeToken<ArrayList<ResultItem>>() {
                            }.getType();
                            AppController.mProfessionsList.clear();
                            ArrayList<ResultItem> ist = gson.fromJson(strResponse, listType);
                            AppController.mProfessionsList.addAll(ist);
                        }

                        if (AppController.mProfessionsList.size() > 0) {
                            AppController.mHashProfessions.clear();
                            AppController.mListProfessions.clear();

                            for (ResultItem item : AppController.mProfessionsList) {
                                AppController.mHashProfessions.put(item.name, "" + item.id);
                                AppController.mListProfessions.add(item.name);
                            }
                        }
                    } catch (Exception e) {
                        CaughtException(SignUp.this, e);
                    }

                    // DEPARTMENTS
                    try {
                        strResponse = WebServiceCall.callGETRequest(Constants.URL + DEPARTMENTS, "");
                        if (strResponse != null) {

                            Type listType = new TypeToken<ArrayList<ResultItem>>() {
                            }.getType();
                            AppController.mDepartmentsList.clear();
                            ArrayList<ResultItem> ist = gson.fromJson(strResponse, listType);
                            AppController.mDepartmentsList.addAll(ist);
                        }

                        if (AppController.mDepartmentsList.size() > 0) {
                            AppController.mHashDepartments.clear();
                            AppController.mListDepartments.clear();

                            for (ResultItem item : AppController.mDepartmentsList) {
                                AppController.mHashDepartments.put(item.name, "" + item.id);
                                AppController.mListDepartments.add(item.name);
                            }
                        }
                    } catch (Exception e) {
                        CaughtException(SignUp.this, e);
                    }

                    // CATEGORIES
                    try {
                        strResponse = WebServiceCall.callGETRequest(Constants.URL + CASTE, "");
                        if (strResponse != null) {

                            Type listType = new TypeToken<ArrayList<ResultItem>>() {
                            }.getType();
                            AppController.mCategoriesList.clear();
                            ArrayList<ResultItem> ist = gson.fromJson(strResponse, listType);
                            AppController.mCategoriesList.addAll(ist);
                        }

                        if (AppController.mCategoriesList.size() > 0) {
                            AppController.mHashCategories.clear();
                            AppController.mListCategories.clear();

                            for (ResultItem item : AppController.mCategoriesList) {
                                AppController.mHashCategories.put(item.name, "" + item.id);
                                AppController.mListCategories.add(item.name);
                            }
                        }
                    } catch (Exception e) {
                        CaughtException(SignUp.this, e);
                    }

                    // SUB CASTES
                    try {
                        strResponse = WebServiceCall.callGETRequest(Constants.URL + SUBCASTE, "");
                        if (strResponse != null) {

                            Type listType = new TypeToken<ArrayList<ResultItem>>() {
                            }.getType();
                            AppController.mSubCastesList.clear();
                            ArrayList<ResultItem> ist = gson.fromJson(strResponse, listType);
                            AppController.mSubCastesList.addAll(ist);
                        }

                        if (AppController.mSubCastesList.size() > 0) {
                            AppController.mHashSubCastes.clear();
                            AppController.mListSubCastes.clear();

                            for (ResultItem item : AppController.mSubCastesList) {
                                AppController.mHashSubCastes.put(item.name, "" + item.id);
                                AppController.mListSubCastes.add(item.name);

                                lstVals = new ArrayList<>();
                                if (mHashSubCastesList.containsKey(item.code))
                                    lstVals = mHashSubCastesList.get(item.code);

                                if (lstVals == null)
                                    lstVals = new ArrayList<>();
                                lstVals.add(item);
                                mHashSubCastesList.put(item.code, lstVals);
                            }
                        }
                    } catch (Exception e) {
                        CaughtException(SignUp.this, e);
                    }
                }
                // END

            } catch (Exception e) {
                CaughtException(SignUp.this, e);
            }
            return null;
        }
    }
}
