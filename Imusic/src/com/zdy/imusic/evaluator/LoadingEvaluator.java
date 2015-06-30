package com.zdy.imusic.evaluator;

import android.animation.TypeEvaluator;

import com.zdy.imusic.entity.LoadingParam;

public class LoadingEvaluator implements TypeEvaluator<LoadingParam> {

	@Override
	public LoadingParam evaluate(float fraction, LoadingParam startValue,
			LoadingParam endValue) {

		int alpha = (int) (startValue.getAlpha() + fraction
				* (endValue.getAlpha() - startValue.getAlpha()));
		LoadingParam loadingParam = new LoadingParam(alpha);
		return loadingParam;
	}

	// @Override
	// public VoiceAnimParam evaluate(float fraction, VoiceAnimParam startValue,
	// VoiceAnimParam endValue) {
	// float radius = startValue.getRadius() + fraction
	// * (endValue.getRadius() - startValue.getRadius());
	//
	// int alpha = (int) (startValue.getAlpha() + fraction
	// * (endValue.getAlpha() - startValue.getAlpha()));
	//
	// VoiceAnimParam point = new VoiceAnimParam(radius, alpha);
	// return point;
	// }

}
