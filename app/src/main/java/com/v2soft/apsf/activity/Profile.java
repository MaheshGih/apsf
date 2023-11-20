package com.v2soft.apsf.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.v2soft.apsf.R;
import com.v2soft.apsf.shared.AppPreferences;
import com.v2soft.apsf.shared.Utility;

import org.json.JSONObject;

public class Profile extends BaseSlider {

    TextView nameTV, emailTV, phnoTV, refTV, regTV;
    EditText nameET, surnameET, dobET, phnoET;
    RadioButton maleRB, femaleRB;
    AutoCompleteTextView casteACT, qualificationACT, professionACT, departmentACT, bloodgroupACT, countryACT, stateACT, districtACT, mandalACT, villageACT;

    private JSONObject userJson = null;
    private JSONObject jsonObject = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        String strUserJson = AppPreferences.getInstance(this).getValue("USER_DETAILS");
        if (!strUserJson.isEmpty()) {
            try {
                userJson = new JSONObject(strUserJson);
            } catch (Exception e) {
                Utility.CaughtException(this, e);
            }
        }

        nameTV = (TextView) findViewById(R.id.name);
        emailTV = (TextView) findViewById(R.id.email);
        phnoTV = (TextView) findViewById(R.id.mobile);
        refTV = (TextView) findViewById(R.id.refid);
        regTV = (TextView) findViewById(R.id.regid);

        nameET = (EditText) findViewById(R.id.et_name);
        surnameET = (EditText) findViewById(R.id.surname);
        dobET = (EditText) findViewById(R.id.dob);
        phnoET = (EditText) findViewById(R.id.et_mobile);

        maleRB = (RadioButton) findViewById(R.id.male);
        femaleRB = (RadioButton) findViewById(R.id.female);

        casteACT = (AutoCompleteTextView) findViewById(R.id.subcaste);
        qualificationACT = (AutoCompleteTextView) findViewById(R.id.qualification);
        professionACT = (AutoCompleteTextView) findViewById(R.id.profession);
        departmentACT = (AutoCompleteTextView) findViewById(R.id.department);
        bloodgroupACT = (AutoCompleteTextView) findViewById(R.id.bloodgroup);
        countryACT = (AutoCompleteTextView) findViewById(R.id.country);
        stateACT = (AutoCompleteTextView) findViewById(R.id.state);
        districtACT = (AutoCompleteTextView) findViewById(R.id.district);
        mandalACT = (AutoCompleteTextView) findViewById(R.id.mandal);
        villageACT = (AutoCompleteTextView) findViewById(R.id.village);

        if (userJson != null && userJson.length() > 0) {

            try {
                nameTV.setText("Name " + userJson.getString("name"));
                emailTV.setText("Email " + userJson.getString("mailid"));
                phnoTV.setText("Phone No " + userJson.getString("phno"));
                refTV.setText("Ref Id : " + userJson.getString("referedRegid"));
                regTV.setText("Reg Id : " + userJson.getString("regid"));

                nameET.setText(userJson.getString("name"));
                nameET.setEnabled(false);
                surnameET.setText(userJson.getString("surname"));
                surnameET.setEnabled(false);
                dobET.setText(userJson.getString("dob"));
                dobET.setEnabled(false);
                phnoET.setText(userJson.getString("phno"));
                phnoET.setEnabled(false);

                if (!userJson.getString("gender").equalsIgnoreCase("M")) {
                    maleRB.setChecked(false);
                    femaleRB.setChecked(true);
                }

                femaleRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        try {
                            if (userJson.getString("gender").equalsIgnoreCase("M")) {
                                maleRB.setChecked(true);
                                //femaleRB.setChecked(false);
                            } else {
                                //maleRB.setChecked(false);
                                femaleRB.setChecked(true);
                            }
                        } catch (Exception e) {}
                    }
                });

                casteACT.setText(userJson.getString("subCaste"));
                casteACT.setEnabled(false);
                String strValue = userJson.getString("qualification");
                if (!strValue.isEmpty()) {

                    jsonObject = new JSONObject(strValue);
                    if (jsonObject != null && jsonObject.has("name"))
                        qualificationACT.setText(jsonObject.getString("name"));
                }
                qualificationACT.setEnabled(false);

                strValue = userJson.getString("profession");
                if (!strValue.isEmpty()) {

                    jsonObject = new JSONObject(strValue);
                    if (jsonObject != null && jsonObject.has("name"))
                        professionACT.setText(jsonObject.getString("name"));
                }
                professionACT.setEnabled(false);

                strValue = userJson.getString("department");
                if (!strValue.isEmpty()) {

                    jsonObject = new JSONObject(strValue);
                    if (jsonObject != null && jsonObject.has("name"))
                        departmentACT.setText(jsonObject.getString("name"));
                }
                departmentACT.setEnabled(false);

                bloodgroupACT.setText(userJson.getString("bloodGroup"));
                bloodgroupACT.setEnabled(false);


                String strCountry = userJson.getString("country");
                if (strCountry != null && !strCountry.isEmpty()) {
                    try {
                        jsonObject = new JSONObject(strCountry);
                        if (jsonObject != null && jsonObject.has("name"))
                            countryACT.setText(jsonObject.getString("name"));
                    } catch (Exception e) {}
                }
                countryACT.setEnabled(false);

                strCountry = userJson.getString("state");
                if (strCountry != null && !strCountry.isEmpty()) {

                    try {
                        jsonObject = new JSONObject(strCountry);
                        if (jsonObject != null && jsonObject.has("name"))
                            stateACT.setText(jsonObject.getString("name"));
                    } catch (Exception e) {}
                }
                stateACT.setEnabled(false);

                strCountry = userJson.getString("district");
                if (strCountry != null && !strCountry.isEmpty()) {

                    try {
                        jsonObject = new JSONObject(strCountry);
                        if (jsonObject != null && jsonObject.has("name"))
                            districtACT.setText(jsonObject.getString("name"));
                    } catch (Exception e) {}
                }
                districtACT.setEnabled(false);

                strCountry = userJson.getString("mandal");
                if (strCountry != null && !strCountry.isEmpty()) {

                    try {
                        jsonObject = new JSONObject(strCountry);
                        if (jsonObject != null && jsonObject.has("name"))
                            mandalACT.setText(jsonObject.getString("name"));
                    } catch (Exception e) {}
                }
                mandalACT.setEnabled(false);

                strCountry = userJson.getString("village");
                if (strCountry != null && !strCountry.isEmpty()) {

                    try {
                        jsonObject = new JSONObject(strCountry);
                        if (jsonObject != null && jsonObject.has("name"))
                            villageACT.setText(jsonObject.getString("name"));
                    } catch (Exception e) {}
                }
                villageACT.setEnabled(false);

            } catch (Exception e) {
                Utility.CaughtException(this, e);
            }
        }
    }

    public void openSlider(View v) {
        getDrawer().openMenu();
    }

    public void closeScreen(View v) {
        finish();
    }

    public void onSave(View v) {
    }

    public void callBackDataFromAsyncTask(String result) {
        Log.d("RESPONSE", result);

        try {
            if (result != null && !result.isEmpty()) {

            }
        } catch (Exception e) {
            Utility.CaughtException(this, e);
        }
    }
}