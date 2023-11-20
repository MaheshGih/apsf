package com.v2soft.apsf.shared;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.v2soft.apsf.R;
import com.v2soft.apsf.logger.Logger;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by srikanth.m on 3/1/2018.
 */

public class Utility {
    private static Pattern pattern;
    private static Matcher matcher;

    /**
     * Validate Email with regular expression
     *
     * @param email
     * @return true for Valid Email and false for Invalid Email
     */
    public static boolean isValidEmail(CharSequence email) {
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    /**
     * Checks for Null String object
     *
     * @param txt
     * @return true for not null and false for null String object
     */
    public static boolean isNotNull(String txt){
        return txt!=null && txt.trim().length()>0 ? true: false;
    }

    public static boolean isNumberValid(String number) {
        boolean isValid = false;

        String expression = "^[6-9][0-9]{9}$";
        CharSequence inputStr = number;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public static void customToast(String strMsg, Context context) {
        if (strMsg == null)
            strMsg = "null";
        if (!strMsg.isEmpty()) {
            Regular textview = new Regular(context);
            textview.setText(strMsg);
            textview.setGravity(Gravity.CENTER);
            textview.setTextColor(context.getResources().getColor(R.color.white));
            textview.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.textsize_16));
            textview.setPadding(10, 10, 10, 10);
            Toast toast = new Toast(context);
            toast.setView(textview);
            toast.getView().setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setGravity(87, 0, 0);
            toast.show();
        }
    }

    public static void showMessageDialog(final Context context, final int nCase, String strTitle, String strMsg) {
        try {

            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.custom_dialog);
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

                    if (nCase == 0)
                        ((Activity) context).finish();
                    else {

                        AppPreferences.getInstance(context).setValue("REMEMBER", "false");

                        ((Activity) context).finishAffinity();
                    }
                }
            });

            dialog.show();
        } catch (Exception e) {
        }
    }

    public static void CaughtException(Context mContext, Exception e) {
        if (mContext != null) {
            StringWriter stackTrace = new StringWriter();
            e.printStackTrace(new PrintWriter(stackTrace));
            Logger.getInstance(mContext).AuditLogWriter(mContext.getString(R.string.app_name), " - ", "Exception", "Reason : " + stackTrace);
        }
    }
}