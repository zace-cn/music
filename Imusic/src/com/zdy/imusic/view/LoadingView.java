package com.zdy.imusic.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import com.zdy.imusic.utils.BaseUtil;
import com.zdy.imusic.utils.DisplayUtil;

public class LoadingView extends View {

	private Paint paint = new Paint();
	private int screenWidth;
	private int screenHeight;
	public static final float RADIUS = 14f;

	private  Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			invalidate();
		}
	};

	public LoadingView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public LoadingView(Context context) {
		this(context, null);
	}

	public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		screenWidth = DisplayUtil.getScreenWidth();
		screenHeight = DisplayUtil.getScreenHeight();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		paint.setColor(Color.BLACK);
		paint.setAntiAlias(true);
		paint.setDither(true);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(2);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawLoad(canvas);
	}

	private void drawLoad(Canvas canvas) {
		if(isShown()){
			paint.setStyle(Paint.Style.STROKE);
			canvas.drawCircle(screenWidth / 13 * 5, screenHeight / 2, RADIUS, paint);
			canvas.drawCircle(screenWidth / 13 * 6, screenHeight / 2, RADIUS, paint);
			canvas.drawCircle(screenWidth / 13 * 7, screenHeight / 2, RADIUS, paint);
			canvas.drawCircle(screenWidth / 13 * 8, screenHeight / 2, RADIUS, paint);
			paint.setStyle(Paint.Style.FILL);

			int[] randomNum = BaseUtil.randomCommon(5, 9, 2);

			canvas.drawCircle(screenWidth / 13 * randomNum[0], screenHeight / 2,
					RADIUS, paint);
			canvas.drawCircle(screenWidth / 13 * randomNum[1], screenHeight / 2,
					RADIUS, paint);
			handler.sendMessageDelayed(Message.obtain(), 300);
		}
		
	}
	
	

}
