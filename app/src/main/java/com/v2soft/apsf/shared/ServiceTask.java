package com.v2soft.apsf.shared;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;

import com.v2soft.apsf.R;
import com.v2soft.apsf.activity.ChangePassword;
import com.v2soft.apsf.activity.ContactUs;
import com.v2soft.apsf.activity.Forgot;
import com.v2soft.apsf.activity.Home;
import com.v2soft.apsf.activity.Login;
import com.v2soft.apsf.activity.Profile;
import com.v2soft.apsf.activity.Register;
import com.v2soft.apsf.activity.SignUp;
import com.v2soft.apsf.logger.Logger;

import java.security.Signature;

import static com.v2soft.apsf.shared.Constants.CHANGE_PASSWORD;
import static com.v2soft.apsf.shared.Constants.LOGIN;
import static com.v2soft.apsf.shared.Constants.SENDOTP;
import static com.v2soft.apsf.shared.Constants.SIGNUP;
import static com.v2soft.apsf.shared.Constants.VERIFYOTP;

/**
 * Created by srikanth.m on 10/11/2017.
 */

public class ServiceTask extends AsyncTask<String, Void, String> {

    private Activity activity;
    private String methodName;
    private String strInput = "";

    MyProgressDialog mProgressDialog;

    public ServiceTask(Activity activity, String methodName, String strInput) {
        this.activity = activity;
        this.methodName = methodName;
        this.strInput = strInput;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        try {
            mProgressDialog = new MyProgressDialog(activity);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setInverseBackgroundForced(true);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mProgressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String doInBackground(String... params) {
        //create a new soap request object
        try {
            //request to server and get Soap Primitive response
            Logger.getInstance(activity).AuditLogWriter(activity.getString(R.string.app_name), "ServiceTask", "URL", Constants.URL + methodName + strInput);

            if (methodName.equalsIgnoreCase(LOGIN))
                return WebServiceCall.callPOSTRequest(Constants.URL + methodName, strInput);
            else if (methodName.equalsIgnoreCase(SIGNUP) || methodName.equalsIgnoreCase(SENDOTP) || methodName.equalsIgnoreCase(VERIFYOTP) || methodName.equalsIgnoreCase(CHANGE_PASSWORD))
                return WebServiceCall.callRSSThread(Constants.URL + methodName, strInput);
            else
                return WebServiceCall.callGETRequest(Constants.URL + methodName, strInput);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        mProgressDialog.dismiss();

        Logger.getInstance(activity).AuditLogWriter(activity.getString(R.string.app_name), "ServiceTask", "Response", result);

        if (result == null)
            result = "";

        //invoke call back method of Activity
        if (activity instanceof Login)
            ((Login) activity).callBackDataFromAsyncTask(result);
        else if (activity instanceof Forgot)
            ((Forgot) activity).callBackDataFromAsyncTask(result);
        else if (activity instanceof Register)
            ((Register) activity).callBackDataFromAsyncTask(result);
        else if (activity instanceof ContactUs)
            ((ContactUs) activity).callBackDataFromAsyncTask(result);
        else if (activity instanceof Home)
            ((Home) activity).callBackDataFromAsyncTask(result);
        else if (activity instanceof Profile)
            ((Profile) activity).callBackDataFromAsyncTask(result);
        else if (activity instanceof ChangePassword)
            ((ChangePassword) activity).callBackDataFromAsyncTask(result);
        else if (activity instanceof SignUp)
            ((SignUp) activity).callBackDataFromAsyncTask(result);
    }
}
