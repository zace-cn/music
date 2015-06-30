package com.zdy.imusic.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.Surface;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.animation.Animation.AnimationListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 *  屏幕相关工具类
 */
public class DisplayUtil {

	private static float density;
	private static float scaledDensity;

	private static Context context;
	private static Handler handler = new Handler();

	public static void init(Context ctx) {
		context = ctx;
	}

	/**
	 * 获取屏幕的密度
	 * 
	 * @return 屏幕的密度
	 */
	public static float getScreenDensity() {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		density = dm.density;

		return density;
	}

	/**
	 * 获取屏幕的宽度
	 * 
	 * @return 屏幕的宽度
	 */
	public static int getScreenWidth() {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		density = dm.density;
		scaledDensity = dm.scaledDensity;
		int screenWidth = dm.widthPixels;
		return screenWidth;
	}

	/**
	 * 获取屏幕的高度
	 * 
	 * @return 屏幕的高度
	 */
	public static int getScreenHeight() {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		density = dm.density;
		scaledDensity = dm.scaledDensity;
		int screenHeight = dm.heightPixels;
		return screenHeight;
	}

	/**
	 * 将dip转换为px
	 * 
	 * @param dip
	 * @return dip转换为px
	 */
	public static int dip2px(int dip) {
		if (density <= 0) {
			DisplayMetrics dm = context.getResources().getDisplayMetrics();
			density = dm.density;
			scaledDensity = dm.scaledDensity;
		}
		return (int) (dip * density + 0.5f * (dip >= 0 ? 1 : -1));
	}

	/**
	 * 将dip转换为px
	 * 
	 * @param dip
	 * @return dip转换为px
	 */
	public static float dip2px(float dip) {
		if (density <= 0) {
			DisplayMetrics dm = context.getResources().getDisplayMetrics();
			density = dm.density;
			scaledDensity = dm.scaledDensity;
		}
		return dip * density + 0.5f * (dip >= 0 ? 1 : -1);

	}

	/**
	 * 将px转换为dip
	 * 
	 * @param px
	 * @return px转换为dip
	 */
	public static int px2dip(int px) {
		if (density <= 0) {
			DisplayMetrics dm = context.getResources().getDisplayMetrics();
			density = dm.density;
			scaledDensity = dm.scaledDensity;
		}
		return (int) (px / density + 0.5f * (px >= 0 ? 1 : -1));
	}

	/**
	 * sp 转换为 px
	 * 
	 * @param sp
	 * @return sp 转换为 px
	 */
	public static int sp2px(int sp) {
		if (scaledDensity <= 0) {
			DisplayMetrics dm = context.getResources().getDisplayMetrics();
			density = dm.density;
			scaledDensity = dm.scaledDensity;
		}
		return (int) (sp * scaledDensity + 0.5f * (sp >= 0 ? 1 : -1));
	}

	/**
	 * sp 转换为 px
	 * 
	 * @param sp
	 * @return sp 转换为 px
	 */
	public static float sp2px(float sp) {
		if (scaledDensity <= 0) {
			DisplayMetrics dm = context.getResources().getDisplayMetrics();
			density = dm.density;
			scaledDensity = dm.scaledDensity;
		}
		return sp * scaledDensity + 0.5f * (sp >= 0 ? 1 : -1);
	}

	/**
	 * px 转换为 sp
	 * 
	 * @param px
	 * @return px 转换为 sp
	 */
	public static int px2sp(int px) {
		if (scaledDensity <= 0) {
			DisplayMetrics dm = context.getResources().getDisplayMetrics();
			density = dm.density;
			scaledDensity = dm.scaledDensity;
		}
		return (int) (px / scaledDensity + 0.5f * (px >= 0 ? 1 : -1));
	}

	/**
	 * 获取当前屏幕旋转角度
	 * 
	 * @param activity
	 * @return 0表示是竖屏; 90表示是左横屏; 180表示是反向竖屏; 270表示是右横屏
	 */
	public static int getScreemRotation(Activity activity) {
		int rotation = activity.getWindowManager().getDefaultDisplay()
				.getRotation();
		switch (rotation) {
		case Surface.ROTATION_0:
			return 0;
		case Surface.ROTATION_90:
			return 90;
		case Surface.ROTATION_180:
			return 180;
		case Surface.ROTATION_270:
			return 270;
		}
		return 0;
	}

	/**
	 * 锁住屏幕，让屏幕不能旋转
	 * 
	 * @param activity
	 *            需要锁定的Activity
	 */
	public static void lockActivity(Activity activity) {
		int degree = getScreemRotation(activity);
		lockActivity(activity, degree);
	}

	/**
	 * 根据传入的角度设置当前屏幕的状态，使之不能旋转
	 * 
	 * @param activity
	 *            需要锁定的Activity
	 * @param degree
	 *            旋转的角度。一般是通过{@link #getScreemRotation(Activity)}方法获得的数据
	 */
	public static void lockActivity(Activity activity, int degree) {

		int display = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
		switch (degree) {
		case 90:
			display = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
			break;

		case 180:
			display = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
			break;

		case 270:
			display = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
			break;
		}

		activity.setRequestedOrientation(display);
	}

	public static void animHide(final View v, int animTime) {
		animHideShowView(v, null, 0, false, animTime);
	}

	/**
	 * 防止动画执行过程中背景空白，屏幕跳；边执行，边控制view高度
	 * 
	 * @param v
	 *            要执行动画的View
	 * @param al
	 *            动画监听器，可为空
	 * @param measureHeight
	 *            view的实际高度，可不传，但显示时需要保证此高度不为0
	 * @param show
	 *            是显示还是隐藏
	 * @param animTime
	 *            动画时间
	 */
	public static void animHideShowView(final View v, AnimationListener al,
			int measureHeight, final boolean show, int animTime) {
		if (measureHeight <= 0) {
			measureHeight = v.getMeasuredHeight();
		}
		final int heightMeasure = measureHeight;
		Animation anim = new Animation() {

			@Override
			protected void applyTransformation(float interpolatedTime,
					Transformation t) {

				if (interpolatedTime == 1) {

					v.setVisibility(show ? View.VISIBLE : View.GONE);
				} else {
					int height;
					if (show) {
						height = (int) (heightMeasure * interpolatedTime);
					} else {
						height = heightMeasure
								- (int) (heightMeasure * interpolatedTime);
					}
					v.getLayoutParams().height = height;
					v.requestLayout();
				}
			}

			@Override
			public boolean willChangeBounds() {
				return true;
			}
		};

		if (al != null) {
			anim.setAnimationListener(al);
		}
		anim.setDuration(animTime);
		v.startAnimation(anim);
	}

	/**
	 * 判断系统设置是否打开了屏幕旋转
	 * 
	 * @param context
	 * @return 系统设置是否打开了屏幕旋转
	 */
	public static boolean isAccelerometerRotation(Context context) {
		int flag = Settings.System.getInt(context.getContentResolver(),
				Settings.System.ACCELEROMETER_ROTATION, 0);

		return flag != 0;
	}

	/**
	 * 显示输入法
	 * 
	 * @param view
	 *            推荐将EditText传入
	 */
	public static void showSoftInput(View view) {
		if (view instanceof EditText) {
			view.requestFocus();
			((EditText) view).setCursorVisible(true);
		}
		InputMethodManager imm = (InputMethodManager) view.getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInputFromInputMethod(view.getWindowToken(),
				InputMethodManager.SHOW_IMPLICIT);
	}

	/**
	 * 隐藏输入法
	 * 
	 * @param view
	 *            推荐将EditText传入
	 */
	public static void hideSoftInput(View view) {
		if (view instanceof EditText) {
			((EditText) view).setCursorVisible(false);
		}
		InputMethodManager imm = (InputMethodManager) view.getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	/**
	 * 延时显示输入法
	 * 
	 * @param view
	 *            推荐将EditText传入
	 * @param delayMs
	 *            延迟时间(毫秒)
	 */
	public static void showSoftInputDelay(final View view, final long delayMs) {

		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				showSoftInput(view);
			}
		}, delayMs);

	}

	/**
	 * 延时显示输入法
	 * 
	 * @param view
	 *            推荐将EditText传入
	 * @param delayMs
	 *            延迟时间(毫秒)
	 */
	public static void showSoftInputDelayForce(final View view,
			final long delayMs) {

		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				showSoftInputForce(view);
			}
		}, delayMs);

	}

	/**
	 * 强制显示输入法
	 * 
	 * @param view
	 *            推荐将EditText传入
	 * @param delayMs
	 *            延迟时间(毫秒)
	 */
	public static void showSoftInputForce(final View view) {
		if (view instanceof EditText) {
			view.requestFocus();
			((EditText) view).setCursorVisible(true);
		}
		InputMethodManager imm = (InputMethodManager) view.getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
	}

	/**
	 * 强制延时隐藏输入法
	 * 
	 * @param view
	 *            推荐将EditText传入
	 * @param delayMs
	 *            延迟时间(毫秒)
	 */
	public static void hideSoftInputDelay(final View view, final long delayMs) {
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				hideSoftInput(view);
			}
		}, delayMs);

	}
}
