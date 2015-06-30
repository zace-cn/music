package com.zdy.imusic;

import android.content.Context;
import android.graphics.Typeface;

public class MyContext {
	Typeface typeface;
	private static MyContext myContext;

	private MyContext(Context context) {
		super();
		typeface = Typeface.createFromAsset(context.getAssets(),
				"fangzheng.ttf");
	}

	public static MyContext getInstance() {
		return myContext;
	}

	public static void init(Context context) {
		myContext = new MyContext(context);
	}

	public Typeface getTypeface() {
		return typeface;
	}

}
