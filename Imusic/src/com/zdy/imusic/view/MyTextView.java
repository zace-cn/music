package com.zdy.imusic.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.zdy.imusic.MyContext;

public class MyTextView extends TextView {


	public MyTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
	}

	public MyTextView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MyTextView(Context context) {
		this(context, null);
	}

	@Override
	public Typeface getTypeface() {
		return MyContext.getInstance().getTypeface();
	}

	@Override
	public void setTypeface(Typeface tf) {

		super.setTypeface(MyContext.getInstance().getTypeface());
	}
	
	

}
