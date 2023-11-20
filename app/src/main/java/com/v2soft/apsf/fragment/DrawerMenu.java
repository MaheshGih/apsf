package com.v2soft.apsf.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.v2soft.apsf.R;
import com.v2soft.apsf.activity.AboutUs;
import com.v2soft.apsf.activity.BaseSlider;
import com.v2soft.apsf.activity.ChangePassword;
import com.v2soft.apsf.activity.ContactUs;
import com.v2soft.apsf.activity.Forgot;
import com.v2soft.apsf.activity.Home;
import com.v2soft.apsf.activity.Profile;
import com.v2soft.apsf.activity.Register;
import com.v2soft.apsf.shared.AppController;
import com.v2soft.apsf.shared.AppPreferences;
import com.v2soft.apsf.shared.CircleImageView;

import java.io.File;
import java.lang.reflect.Method;
import java.util.List;

import static com.v2soft.apsf.shared.Utility.CaughtException;
import static com.v2soft.apsf.shared.Utility.showMessageDialog;

public class DrawerMenu extends Fragment implements View.OnClickListener {

    public static int nSliderPos = 0;

    Activity activity;
    View view;

    TextView tv_profile;
    TextView tv_forgot;
    TextView tv_change;
    TextView tv_reference;
    TextView tv_invite;
    TextView tv_about;
    TextView tv_contact;
    TextView tv_report;
    TextView tv_logout;
    TextView tv_name;

    CircleImageView profile_image;

    String mstrEmailId = "";

    private BaseSlider baseSlider;

    public DrawerMenu() {

    }

    public void setFragValues(BaseSlider baseSlider) {
        // Required empty public constructor
        this.baseSlider = baseSlider;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity = getActivity();
        view = inflater.inflate(R.layout.drawer_menu, container, false);
        tv_profile = (TextView) view.findViewById(R.id.tv_profile);
        tv_change = (TextView) view.findViewById(R.id.tv_change);
        tv_forgot = (TextView) view.findViewById(R.id.tv_forgot);
        tv_reference = (TextView) view.findViewById(R.id.tv_reference);
        tv_invite = (TextView) view.findViewById(R.id.tv_invite);
        tv_about = (TextView) view.findViewById(R.id.tv_about);
        tv_contact = (TextView) view.findViewById(R.id.tv_contact);
        tv_logout = (TextView) view.findViewById(R.id.tv_logout);
        tv_report = (TextView) view.findViewById(R.id.tv_report);

        tv_name = (TextView) view.findViewById(R.id.tv_name);
        tv_name.setText(AppPreferences.getInstance(activity).getValue("NAME"));
        tv_name.setVisibility(View.GONE);

        TextView tv_profession = (TextView) view.findViewById(R.id.tv_profession);
        tv_profession.setVisibility(View.GONE);
        if (!AppController.user.isEmpty()) {
            mstrEmailId = AppPreferences.getInstance(activity).getValue("EMAIL");
            tv_profession.setText(mstrEmailId);
            tv_profession.setVisibility(View.VISIBLE);
        }

        tv_profile.setOnClickListener(this);
        tv_forgot.setOnClickListener(this);
        tv_reference.setOnClickListener(this);
        tv_change.setOnClickListener(this);
        tv_invite.setOnClickListener(this);
        tv_about.setOnClickListener(this);
        tv_contact.setOnClickListener(this);
        tv_logout.setOnClickListener(this);
        tv_report.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {

        if (baseSlider != null)
            baseSlider.getDrawer().closeMenu();

        if (view.getId() == nSliderPos)
            return;

        Intent intent = null;

        switch (view.getId()) {
            case R.id.tv_profile:
                finishCurrent();
                intent = new Intent(activity, Profile.class);
                break;
            case R.id.tv_reference:
                finishCurrent();
                intent = new Intent(activity, Register.class);
                break;
            case R.id.tv_forgot:
                finishCurrent();
                intent = new Intent(activity, Forgot.class);
                break;
            case R.id.tv_change:
                finishCurrent();
                    intent = new Intent(activity, ChangePassword.class);
                break;
            case R.id.tv_about:
                finishCurrent();
                intent = new Intent(activity, AboutUs.class);
                break;
            case R.id.tv_contact:
                finishCurrent();
                intent = new Intent(activity, ContactUs.class);
                break;
            case R.id.tv_invite:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Dear User, RPS(Reservation Parirakshana Samithi) app is available in Google PlayStore...\nClick & download RPS App from here https://play.google.com/store/apps/details?id=com.v2soft.apsf");
                sendIntent.setType("text/plain");
                activity.startActivity(sendIntent);
                break;
            case R.id.tv_report:
                shareToGMail();
                break;
            case R.id.tv_logout:
                showMessageDialog(activity, 1, "Logout", "Do you want to logout from " + getString(R.string.app_name) + "?");
                break;
            case R.id.profile_image:
                break;
        }

        if (intent != null)
            startActivity(intent);

        nSliderPos = 1000000;
    }

    private void finishCurrent() {
        if (activity != null) {
            if (!(activity instanceof Home)) {
                activity.finish();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (AppController.user.isEmpty()) {
            tv_logout.setText("Login");
        } else {
            tv_logout.setText("Logout");
        }

        if (!AppController.user.isEmpty()) {
            tv_name.setText(AppPreferences.getInstance(activity).getValue("NAME"));
            tv_name.setVisibility(View.VISIBLE);
        }

        profile_image = (CircleImageView) view.findViewById(R.id.profile_image);
        /*String mstrPhotoUrl = AppPreferences.getInstance(activity).getValue(mstrEmailId + "URL");
        if (mstrPhotoUrl != null && !mstrPhotoUrl.equalsIgnoreCase("")) {
            Glide.with(this).load(mstrPhotoUrl)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(profile_image);
        }*/
        profile_image.setOnClickListener(this);
    }

    public void shareToGMail() {

        if (Build.VERSION.SDK_INT >= 24) {
            try {
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            File filepath = new File(Environment.getExternalStorageDirectory(), getString(R.string.app_name) + "/");
            File fileToSend = new File(filepath, "Log.txt");
            if (fileToSend != null && fileToSend.exists()) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"batthulaveera@gmail.com"});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Report a Problem - " + getString(R.string.app_name));
                //emailIntent.setType("text/plain");
                emailIntent.setType("message/rfc822");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Hi, I am facing some issue while using " + getContext().getString(R.string.app_name) + " app in regular manner.\nI am attaching the log file for your reference");
                emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + fileToSend.getAbsoluteFile()));
                final PackageManager pm = getContext().getPackageManager();
                final List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent, 0);
                ResolveInfo best = null;
                for (final ResolveInfo info : matches)
                    if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail"))
                        best = info;
                if (best != null)
                    emailIntent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
                startActivity(emailIntent);
            }
        } catch (Exception e) {
            CaughtException(getContext(), e);
        }
    }
}
