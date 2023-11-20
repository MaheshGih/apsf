package com.v2soft.apsf.shared;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by srikanth.m on 10/3/2017.
 */

public class Regular extends TextView {

	public Regular(Context context) {
		super(context);

		applyCustomFont(context);
	}

	public Regular(Context context, AttributeSet attrs) {
		super(context, attrs);

		applyCustomFont(context);
	}

	public Regular(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		applyCustomFont(context);
	}

	private void applyCustomFont(Context context) {
		Typeface customFont = FontCache.getTypeface("ui-text-regular.ttf", context);
		setTypeface(customFont);
	}
}