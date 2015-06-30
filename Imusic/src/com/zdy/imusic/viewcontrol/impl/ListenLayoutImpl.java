package com.zdy.imusic.viewcontrol.impl;

import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;

import com.zdy.imusic.R;
import com.zdy.imusic.view.ListenLayout;
import com.zdy.imusic.view.LoadingView;
import com.zdy.imusic.view.MyTextView;
import com.zdy.imusic.viewcontrol.ViewInflate;

public class ListenLayoutImpl implements ViewInflate {

	View view;
	MyTextView title,tip1,tip2;
	ListenLayout listenLayout;
	LoadingView loadingView;
	@Override
	public void init(LayoutInflater inflater, ViewGroup container) {
		view = inflater.inflate(R.layout.activity_main_listen, null);
		title = (MyTextView) view.findViewById(R.id.tv_main_title);
		tip1 = (MyTextView) view.findViewById(R.id.tv_main_tip1);
		tip2 = (MyTextView) view.findViewById(R.id.tv_main_tip2);
		listenLayout = (ListenLayout) view.findViewById(R.id.listen_layout);
		loadingView = (LoadingView) view.findViewById(R.id.loading);
	}

	
	public void setTouchListen(OnTouchListener listener){
		listenLayout.setOnTouchListener(listener);
	}
	
	
	public void showLoadView(){
		loadingView.setVisibility(View.VISIBLE);
	}
	
	public void dismissLoadView(){
		loadingView.setVisibility(View.GONE);
	}
	

	@Override
	public View getView() {
		return view;
	}
	
	public void setTitleMsg(String msg){
//		tip1.setTypeface(typeFace);
//		tip2.setTypeface(typeFace);
//		title.setTypeface(typeFace);
		title.setText(msg);
	}
	
	
	public void setVoiceDismiss(){
		listenLayout.setShowVoice(false);
		listenLayout.invalidate();
	}
	
	public void setVoiceAppear(){
		listenLayout.setShowVoice(true);
		listenLayout.invalidate();
	}


}
