package com.zdy.imusic.utils;

import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.view.View;

public class AnimUtil {

	public static void ofFloat(View v,String name,float now,float will,AnimatorListenerAdapter adapter) {
		ObjectAnimator animator = ObjectAnimator.ofFloat(
				v, name, now, will);
		animator.setDuration(500);
		animator.start();
		animator.addListener(adapter);
	}

	public static void ofInt(View v,String name,int now,int will,AnimatorListenerAdapter adapter) {
		ObjectAnimator animator = ObjectAnimator.ofInt(
				v, name, now, will);
		animator.setDuration(500);
		animator.start();
		animator.addListener(adapter);
	}
	
	
	
	
	
}
