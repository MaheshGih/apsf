package com.v2soft.apsf.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.v2soft.apsf.R;
import com.v2soft.apsf.fragment.DrawerMenu;
import com.v2soft.apsf.shared.AppPreferences;
import com.v2soft.apsf.shared.CircleImageView;
import com.v2soft.apsf.shared.Constants;
import com.v2soft.apsf.shared.FontCache;
import com.v2soft.apsf.shared.NetWorkAvailable;
import com.v2soft.apsf.shared.ServiceTask;
import com.v2soft.apsf.shared.Utility;

/**
 * Created by srikanth.m on 3/1/2018.
 */

public class ContactUs extends BaseSlider {

    Typeface mTypeface;

    private String mstrName = "";
    private String mstrEmail = "";
    private String mstrSubject = "";
    private String mstrMessage = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mTypeface = FontCache.getTypeface("ui-text-regular.ttf", this);

        mstrName = AppPreferences.getInstance(this).getValue("NAME");
        mstrEmail = AppPreferences.getInstance(this).getUserName();
        String mstrPhotoUrl = AppPreferences.getInstance(this).getValue(mstrEmail + "URL");
        if (mstrPhotoUrl != null && !mstrPhotoUrl.equalsIgnoreCase("")) {
            /*Glide.with(this).load(mstrPhotoUrl)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(profile_image);*/
        }

        final EditText etName = (EditText) findViewById(R.id.name);
        etName.setTypeface(mTypeface);
        if (mstrName != null && !mstrName.isEmpty()) {
            etName.setText(mstrName);
            etName.setEnabled(false);
        }

        final EditText etEmail = (EditText) findViewById(R.id.email);
        etEmail.setTypeface(mTypeface);
        if (mstrEmail != null && !mstrEmail.isEmpty()) {
            etEmail.setText(mstrEmail);
            etEmail.setEnabled(false);
        }

        final EditText etSubject = (EditText) findViewById(R.id.subject);
        etSubject.setTypeface(mTypeface);

        final EditText etMessage = (EditText) findViewById(R.id.message);
        etMessage.setTypeface(mTypeface);

        TextView tv_signup = (TextView) findViewById(R.id.tv_signup);
        tv_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mstrName = etName.getText().toString();
                mstrEmail = etEmail.getText().toString();
                mstrMessage = etMessage.getText().toString();

                if (!mstrName.isEmpty()) {

                    if (!mstrEmail.isEmpty()) {

                        if (!mstrMessage.isEmpty()) {

                            if (NetWorkAvailable.getNetWorkStatus()) {

                                new ServiceTask(ContactUs.this, Constants.CONTACT_US, "?P_Name=" + mstrName + "&P_EmailID=" + mstrEmail + "&P_Message=" + mstrMessage).execute();
                            } else
                                Utility.customToast(getString(R.string.no_network), ContactUs.this);

                        } else
                            Utility.customToast("Please enter message", ContactUs.this);
                    } else
                        Utility.customToast("Please enter email", ContactUs.this);
                } else
                    Utility.customToast("Please enter name", ContactUs.this);
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
    }

    @Override
    public void onResume() {
        super.onResume();

        DrawerMenu.nSliderPos = R.id.tv_contact;
    }

    @Override
    public void onBackPressed() {
        if (getDrawer().isMenuVisible()) {
            getDrawer().closeMenu();
            return;
        }
        super.onBackPressed();
    }

    public void openSlider(View v) {
        getDrawer().openMenu();
    }

    public void openLocation(View view) {

        try {
            String urlAddress = getString(R.string.text_address_link);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlAddress));
            startActivity(intent);
        } catch (Exception e) {

        }
    }

    public void callBackDataFromAsyncTask(String result) {
        Log.d("RESPONSE", result);

        if (result != null && !result.isEmpty()) {

            if (result.contains("SUCCESS")) {
                Utility.customToast("Request submitted successfully", ContactUs.this);
                finish();
            } else
                Utility.customToast(result, ContactUs.this);
        } else
            Utility.customToast("Unable to contact", ContactUs.this);
    }
}