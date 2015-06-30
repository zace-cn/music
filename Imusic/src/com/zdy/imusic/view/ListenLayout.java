package com.zdy.imusic.view;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.zdy.imusic.R;
import com.zdy.imusic.entity.VoiceAnimParam;
import com.zdy.imusic.evaluator.PointEvaluator;
import com.zdy.imusic.utils.DisplayUtil;

/**
 * @author zdy app首页采集声音的界面
 * 
 */
public class ListenLayout extends RelativeLayout {

	private Paint paint = new Paint();
	private Paint circlePaint = new Paint();
	private int screenWidth;
	private int screenHeight;
	public static final float RADIUS = 100f;
	private VoiceAnimParam currentPoint;
	private Bitmap bitmap;

	private boolean isShowVoice;

	public void setShowVoice(boolean isShowVoice) {
		this.isShowVoice = isShowVoice;
	}


	public ListenLayout(Context context) {
		this(context, null);
	}

	public ListenLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ListenLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		screenWidth = DisplayUtil.getScreenWidth();
		screenHeight = DisplayUtil.getScreenHeight();
		bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.voice_recording);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		paint.setColor(Color.BLACK);
		paint.setAntiAlias(true);
		paint.setDither(true);
		paint.setStyle(Paint.Style.FILL);
		paint.setStrokeWidth(2);

		circlePaint.setColor(Color.BLACK);
		circlePaint.setAntiAlias(true);
		circlePaint.setDither(true);
		circlePaint.setStyle(Paint.Style.STROKE);
		circlePaint.setStrokeWidth(5);
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		canvas.drawCircle(screenWidth / 2 / 3, screenHeight / 2, 20, paint);
		canvas.drawCircle(screenWidth - screenWidth / 2 / 3, screenHeight / 2,
				20, paint);

		if (isShowVoice) {
			drawVoice(canvas);
			if (currentPoint == null) {
				currentPoint = new VoiceAnimParam(RADIUS, 255);
				drawCircle(canvas);
				startAnimation();
			} else {
				drawCircle(canvas);
			}
		}

	}

	/**
	 * 中间声音图标绘制
	 */
	private void drawVoice(Canvas canvas) {
		canvas.drawCircle(screenWidth / 2, screenHeight / 2, RADIUS, paint);

		canvas.drawBitmap(bitmap, screenWidth / 2 - bitmap.getWidth() / 2,
				screenHeight / 2 - bitmap.getHeight() / 2, paint);

	}

	private void startAnimation() {
		VoiceAnimParam startPoint = new VoiceAnimParam(RADIUS, 255);
		VoiceAnimParam endPoint = new VoiceAnimParam(RADIUS * 2, 0);

		ValueAnimator animator = ValueAnimator.ofObject(new PointEvaluator(),
				startPoint, endPoint);

		animator.addUpdateListener(new AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				currentPoint = (VoiceAnimParam) animation.getAnimatedValue();
				invalidate();
			}
		});
		animator.setRepeatCount(ValueAnimator.INFINITE);
		animator.setRepeatMode(ValueAnimator.RESTART);
		animator.setDuration(2000);
		animator.start();

	}

	private void drawCircle(Canvas canvas) {
		circlePaint.setAlpha((int) currentPoint.getAlpha());
		canvas.drawCircle(screenWidth / 2, screenHeight / 2,
				currentPoint.getRadius(), circlePaint);
	}

}
