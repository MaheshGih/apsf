package com.v2soft.apsf.shared;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;

import com.v2soft.apsf.R;

/**
 * Created by srikanth.m on 11/11/2016.
 */

public class MyProgressDialog extends AlertDialog {

    Context mContext;

    public MyProgressDialog(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        mContext = context;
    }

    @Override
    public void show() {
        super.show();

        View dialog = getLayoutInflater().inflate(R.layout.loading_dialog, null);
        dialog.setBackgroundDrawable(new ColorDrawable(mContext.getResources().getColor(R.color.transperant)));

        setContentView(dialog);
    }
}
