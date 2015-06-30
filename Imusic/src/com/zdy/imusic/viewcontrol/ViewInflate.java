package com.zdy.imusic.viewcontrol;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public interface ViewInflate {
	
	void init(LayoutInflater inflater,ViewGroup container);
	View getView();
}
