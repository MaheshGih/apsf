package com.v2soft.apsf.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.v2soft.apsf.R;
import com.v2soft.apsf.fragment.DrawerMenu;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.Position;

public class BaseSlider extends FragmentActivity {

    public MenuDrawer mDrawer;
    public DrawerMenu menuFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDrawer = MenuDrawer.attach(BaseSlider.this, MenuDrawer.Type.OVERLAY, Position.RIGHT, MenuDrawer.MENU_DRAG_WINDOW);
        mDrawer.setContentView(R.layout.slider);
        mDrawer.setMenuView(R.layout.left_panel);
        mDrawer.setDropShadowEnabled(false);

        FragmentManager fManager = getSupportFragmentManager();
        FragmentTransaction fTransaction = fManager.beginTransaction();
        menuFrag = new DrawerMenu();
        menuFrag.setFragValues(this);
        fTransaction.replace(R.id.menu_frame, menuFrag);
        fTransaction.commit();
    }

    public MenuDrawer getDrawer() {
        return mDrawer;
    }
}
