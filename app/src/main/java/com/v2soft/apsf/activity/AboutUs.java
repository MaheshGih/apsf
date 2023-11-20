package com.v2soft.apsf.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.v2soft.apsf.R;
import com.v2soft.apsf.fragment.DrawerMenu;
import com.v2soft.apsf.shared.AppPreferences;
import com.v2soft.apsf.shared.CircleImageView;

/**
 * Created by srikanth.m on 3/1/2018.
 */

public class AboutUs extends BaseSlider {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);

        AppPreferences.getInstance(this).getValue("NAME");
        String mstrEmailId = AppPreferences.getInstance(this).getValue("EMAIL");
        String mstrPhotoUrl = AppPreferences.getInstance(this).getValue(mstrEmailId + "URL");
        if (mstrPhotoUrl != null && !mstrPhotoUrl.equalsIgnoreCase("")) {
            /*Glide.with(this).load(mstrPhotoUrl)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(profile_image);*/
        }

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
    public void onBackPressed() {
        if (getDrawer().isMenuVisible()) {
            getDrawer().toggleMenu();
            return;
        }
        super.onBackPressed();
    }

    public void openSlider(View v) {
        getDrawer().openMenu();
    }

    @Override
    public void onResume() {
        super.onResume();

        DrawerMenu.nSliderPos = R.id.tv_about;
    }
}
