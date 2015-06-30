package com.zdy.imusic.evaluator;

import com.zdy.imusic.entity.VoiceAnimParam;

import android.animation.TypeEvaluator;

public class PointEvaluator implements TypeEvaluator<VoiceAnimParam> {

	@Override
	public VoiceAnimParam evaluate(float fraction, VoiceAnimParam startValue, VoiceAnimParam endValue) {
		float radius = startValue.getRadius() + fraction
				* (endValue.getRadius() - startValue.getRadius());

		int alpha = (int) (startValue.getAlpha() + fraction
				* (endValue.getAlpha() - startValue.getAlpha()));

		VoiceAnimParam point = new VoiceAnimParam(radius, alpha);
		return point;
	}

}
