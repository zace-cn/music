package com.zdy.imusic.viewcontrol.impl;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.zdy.imusic.App;
import com.zdy.imusic.Constant;
import com.zdy.imusic.R;
import com.zdy.imusic.activity.ListenActivity;
import com.zdy.imusic.utils.AnimUtil;
import com.zdy.imusic.utils.DisplayUtil;
import com.zdy.imusic.view.BlurringView;
import com.zdy.imusic.view.CircleImageView;
import com.zdy.imusic.view.CircleImageView.OnSeekChangeListener;
import com.zdy.imusic.view.LyricView;
import com.zdy.imusic.view.MyTextView;
import com.zdy.imusic.viewcontrol.ViewInflate;

public class PlayLayoutImpl implements ViewInflate {
	View view;
	CircleImageView imageView, nextCircleImgView;
	View play_bg;
	BlurringView play_blur;
	ImageView play_img_bg, play, pause, next, share;
	Bitmap bitmap, nextBitmap;
	MyTextView tv_play_songname, tv_play_singername;

	MyTextView lyc_tv_play_songname, lyc_tv_play_singername;
	FrameLayout rootLayout;
	LyricView lyricView;

	private int screenWidth, screenHeight;
	private FrameLayout.LayoutParams circleParams;
	private FrameLayout.LayoutParams nextCircleParams;

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case 1:
				imageView.setImageBitmap(bitmap);
				play_img_bg.setImageBitmap(bitmap);
				play_blur.invalidate();
				break;

			case 2:
				nextCircleImgView.setImageBitmap(nextBitmap);
				break;
			case 3:

				Toast.makeText(App.app, "哈哈哈", 0).show();
				break;
			}

		};
	};

	public void setCircleImgOnTouch(OnCtrlMediaListener listener,
			Context context) {
		this.ctrlMediaListener = listener;
		imageView.setOnTouchListener(new CircleOntouch());
		rootLayout.setOnTouchListener(new RootOnTouch(context));
		imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				lyc_tv_play_songname.setVisibility(View.VISIBLE);
				lyc_tv_play_singername.setVisibility(View.VISIBLE);
				tv_play_songname.setVisibility(View.GONE);
				tv_play_singername.setVisibility(View.GONE);
				imageView.setVisibility(View.GONE);
				lyricView.setVisibility(View.VISIBLE);
			}
		});

		lyricView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				lyc_tv_play_songname.setVisibility(View.GONE);
				lyc_tv_play_singername.setVisibility(View.GONE);
				tv_play_songname.setVisibility(View.VISIBLE);
				tv_play_singername.setVisibility(View.VISIBLE);
				imageView.setVisibility(View.VISIBLE);
				lyricView.setVisibility(View.GONE);
			}
		});
	}

	@Override
	public void init(LayoutInflater inflater, ViewGroup container) {
		view = inflater.inflate(R.layout.activity_main_play, null);

		rootLayout = (FrameLayout) view.findViewById(R.id.fl_layout_root_play);
		lyricView = (LyricView) view.findViewById(R.id.lyricView);
		play = (ImageView) view.findViewById(R.id.img_play_play);
		pause = (ImageView) view.findViewById(R.id.img_play_pause);
		next = (ImageView) view.findViewById(R.id.img_play_next);
		share = (ImageView) view.findViewById(R.id.img_play_share);

		nextCircleImgView = (CircleImageView) view
				.findViewById(R.id.next_circleview);
		imageView = (CircleImageView) view.findViewById(R.id.play_circleview);
		play_img_bg = (ImageView) view.findViewById(R.id.play_img_bg);

		lyc_tv_play_songname = (MyTextView) view
				.findViewById(R.id.lyc_tv_play_songname);
		lyc_tv_play_singername = (MyTextView) view
				.findViewById(R.id.lyc_tv_play_singername);
		tv_play_songname = (MyTextView) view
				.findViewById(R.id.tv_play_songname);
		tv_play_singername = (MyTextView) view
				.findViewById(R.id.tv_play_singername);

		screenWidth = DisplayUtil.getScreenWidth();
		screenHeight = DisplayUtil.getScreenHeight();

		/*
		 * 动态设置暂停按钮的位置
		 */

		FrameLayout.LayoutParams params2 = (android.widget.FrameLayout.LayoutParams) pause
				.getLayoutParams();
		params2.leftMargin = screenWidth / 4 * 3 + params2.width / 2;
		pause.requestLayout();

		/*
		 * 动态设置下一曲按钮的位置
		 */

		FrameLayout.LayoutParams params3 = (android.widget.FrameLayout.LayoutParams) next
				.getLayoutParams();
		params3.topMargin = screenHeight / 2 - screenWidth / 8;
		next.setLayoutParams(params3);

		/*
		 * 动态设置中间圆形imageview
		 */

		circleParams = (android.widget.FrameLayout.LayoutParams) imageView
				.getLayoutParams();
		circleParams.height = screenWidth / 2;
		circleParams.width = screenWidth / 2;
		circleParams.gravity = Gravity.CENTER;

		imageView.requestLayout();

		/*
		 * 动态下一曲圆形imageview
		 */

		nextCircleParams = (android.widget.FrameLayout.LayoutParams) nextCircleImgView
				.getLayoutParams();
		nextCircleParams.height = screenWidth / 2;
		nextCircleParams.width = screenWidth / 2;
		nextCircleParams.leftMargin = screenWidth / 4;
		nextCircleParams.topMargin = screenHeight + 10;
		nextCircleImgView.requestLayout();

		/*
		 * 动态设置imageview的大小超过屏幕，解决边缘不能模糊
		 */
		LayoutParams params = play_img_bg.getLayoutParams();
		params.height = screenHeight + 100;
		params.width = screenWidth + 100;
		play_img_bg.setLayoutParams(params);

		LayoutParams params4 = lyricView.getLayoutParams();
		params4.height = screenHeight;
		params4.width = screenWidth;
		lyricView.setLayoutParams(params4);
		rootLayout.bringChildToFront(lyricView);

		play_blur = (BlurringView) view.findViewById(R.id.play_blur);
		play_bg = view.findViewById(R.id.play_bg);
		play_blur.setBlurredView(play_img_bg);
	}

	@Override
	public View getView() {
		return view;
	}

	public void setText(String song, String name) {
		tv_play_songname.setText(song);
		tv_play_singername.setText(name);
		lyc_tv_play_songname.setText(song);
		lyc_tv_play_singername.setText(name);
	}

	public void setImageViewOnSeekListener(OnSeekChangeListener mListener) {
		imageView.setOnSeekChangeListener(mListener);
	}

	public void setImg(final Context context, final String url) {

		new Thread() {
			@Override
			public void run() {
				try {
					bitmap = Picasso.with(context).load(url).get();
					handler.sendEmptyMessage(1);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();

	}

	public void setNextmg(final Context context, final String url) {

		new Thread() {
			@Override
			public void run() {
				try {
					nextBitmap = Picasso.with(context).load(url).get();
					handler.sendEmptyMessage(2);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();

	}

	public void setDefultImg(final Context context, final int id) {

		new Thread() {
			@Override
			public void run() {
				try {
					bitmap = Picasso.with(context).load(id).get();

					handler.sendEmptyMessage(1);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();

	}

	public void resetProgress() {
		imageView.resetCount();
	}

	public float getProgress() {
		return imageView.getProgress();
	}

	public void setDefult() {

	}

	private int INTERVAL = 45;// 歌词每行的间隔

	public void setProgress(int i) {
		imageView.setProgress(i);

		lyricView.setOffsetY(lyricView.getOffsetY() - lyricView.SpeedLrc());
		lyricView.SelectIndex(i);

	}

	public void setLycSizeAndLyrics(String str, int curPos) {
		lyricView.read(str);
		lyricView.setOffsetY(900 - lyricView.SelectIndex(curPos + 2)
				* (lyricView.getSIZEWORD() + INTERVAL - 1));
		// lyricView.setOffsetY(500);
	}

	public void setMaxProgress(int i) {
		imageView.setMaxProgress(i);
	}

	enum States {
		NORMAL, LESTORRIGHT, TOPORBOTTOM
	}

	private boolean isMove;

	class CircleOntouch implements OnTouchListener {

		States states = States.NORMAL;

		int startX;
		int startY;

		int currentX;
		int currentY;

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			int action = event.getAction();
			switch (action) {
			case MotionEvent.ACTION_DOWN:
				isMove = false;
				startX = (int) event.getRawX();
				startY = (int) event.getRawY();
				break;

			case MotionEvent.ACTION_MOVE:
				isMove = true;
				currentX = (int) event.getRawX();
				currentY = (int) event.getRawY();

				if (states != States.NORMAL) {
					if (states == States.LESTORRIGHT) {// 左右
						smoothLeftOrRight(event);
					} else {// 上下
						smoothTopOrBottom(event);
					}
				} else {
					if (Math.abs(startX - currentX) > 10
							|| Math.abs(startY - currentY) > 10) {
						if (Math.abs(startX - currentX) > Math.abs(startY
								- currentY)) {// 左右滑动
							pause.setVisibility(View.VISIBLE);
							states = States.LESTORRIGHT;
							smoothLeftOrRight(event);

						} else {// 上下滑动
							states = States.TOPORBOTTOM;
							next.setVisibility(View.VISIBLE);
							smoothTopOrBottom(event);
						}

					}
				}
				break;
			case MotionEvent.ACTION_UP:

				if (!isMove) {
					imageView.performClick();
				}

				switch (states) {
				case NORMAL:
					states = States.NORMAL;
					break;
				case LESTORRIGHT:

					int l = (int) imageView.getX();

					if (l > screenWidth / 8) {// 回到原点

						ctrlMediaListener.play();
						states = States.NORMAL;
						// float curX = imageView.getX();
						ViewToCenter(true);
						// AnimUtil.ofFloat(imageView, "X", curX, screenWidth /
						// 4,
						// new AnimatorListenerAdapter() {
						//
						// @Override
						// public void onAnimationEnd(
						// Animator animation) {
						// play.setVisibility(View.GONE);
						// pause.setVisibility(View.GONE);
						// }
						// });

					} else {// 可以暂停
						ctrlMediaListener.pause();

						float curX = imageView.getX();
						AnimUtil.ofFloat(imageView, "X", curX,
								0 - screenWidth / 3,
								new AnimatorListenerAdapter() {
									@Override
									public void onAnimationEnd(
											Animator animation) {
										play.setVisibility(View.VISIBLE);
										pause.setVisibility(View.GONE);
									}
								});

					}

					break;
				case TOPORBOTTOM:
					int Y = (int) imageView.getY();

					if (Y > screenHeight / 2) {// 超过播放下一曲界限
						// 播放下一曲
						float curY = imageView.getY();
						float curY2 = nextCircleImgView.getY();

						AnimUtil.ofFloat(imageView, "Y", curY,
								screenHeight + 10,
								new AnimatorListenerAdapter() {

									@Override
									public void onAnimationEnd(
											Animator animation) {

										imageView.setImageBitmap(nextBitmap);
										imageView.setY(screenHeight / 2
												- screenWidth / 4);

										play_img_bg.setImageBitmap(nextBitmap);
										play_blur.invalidate();
										ctrlMediaListener.next();
										next.setVisibility(View.GONE);
									}

								});

						AnimUtil.ofFloat(nextCircleImgView, "Y", curY2,
								screenHeight / 2 - screenWidth / 4,
								new AnimatorListenerAdapter() {

									@Override
									public void onAnimationEnd(
											Animator animation) {

										nextCircleImgView
												.setY(screenHeight + 10);

									}
								});

						break;
					} else if (Y < screenHeight / 2
							&& Y >= screenHeight / 2 - screenWidth / 4) {// 回到中心位置
						Y = screenHeight / 2 - screenWidth / 4;
						next.setVisibility(View.GONE);
						int nextY = screenHeight + 10;
						nextCircleImgView.setY(nextY);
						imageView.setY(Y);
						;
						states = States.NORMAL;
					} else if (Y < screenHeight / 2 - screenWidth / 2) {// 达到分享界限
						// 分享

					} else if (Y >= screenHeight / 2 - screenWidth / 2
							&& Y <= screenHeight / 2) {
						// 没达到分享界线回到原位
						Y = screenHeight / 2 - screenWidth / 4;
						next.setVisibility(View.GONE);
						imageView.setY(Y);
						states = States.NORMAL;
					}

					break;
				}

				break;
			}

			return true;
		}

		/**
		 * 左右滑动的方法
		 */
		void smoothLeftOrRight(MotionEvent event) {

			int moveX = currentX - startX;

			int x = (int) (imageView.getX() + moveX);
			imageView.setX(x);

			startX = (int) event.getRawX();
			startY = (int) event.getRawY();

		}

		/**
		 * 上下滑动的方法
		 */
		void smoothTopOrBottom(MotionEvent event) {

			if (currentY <= screenHeight / 2) {// 上滑

			} else {
				nextCircleImgView.setVisibility(View.VISIBLE);
				int moveY = currentY - startY;

				int y = (int) (imageView.getY() + moveY);

				int nextY = (int) (nextCircleImgView.getY() - moveY * 2);

				if (nextY <= screenHeight / 2 - screenWidth / 4) {
					nextY = screenHeight / 2 - screenWidth / 4;
				}

				imageView.setY(y);
				nextCircleImgView.setY(nextY);

			}

			startX = (int) event.getRawX();
			startY = (int) event.getRawY();
		}

	}

	public void ViewToCenter(boolean leftRight) {
		if (leftRight) {
			float curX = imageView.getX();
			AnimUtil.ofFloat(imageView, "X", curX, screenWidth / 4,
					new AnimatorListenerAdapter() {

						@Override
						public void onAnimationEnd(Animator animation) {
							play.setVisibility(View.GONE);
							pause.setVisibility(View.GONE);
						}
					});
		}

	}

	class RootOnTouch implements OnTouchListener {

		Context context;
		boolean isTop;
		public RootOnTouch(Context context) {
			this.context = context;
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			int action = event.getAction();
			switch (action) {
			case MotionEvent.ACTION_DOWN:
				float touchY = event.getRawY();
				if (touchY < screenHeight / 10) {
					isTop = true;
					break;
				} else {
					float curX = imageView.getX();
					AnimUtil.ofFloat(imageView, "X", curX, 0 - screenWidth / 3,
							new AnimatorListenerAdapter() {
								@Override
								public void onAnimationEnd(Animator animation) {
									ctrlMediaListener.pause();
									play.setVisibility(View.VISIBLE);
									pause.setVisibility(View.GONE);

									Intent intent = new Intent(context,
											ListenActivity.class);
									intent.putExtra("isFromPlay", true);
									context.startActivity(intent);
								}
							});
				}

				break;

			case MotionEvent.ACTION_MOVE:
				break;
			case MotionEvent.ACTION_UP:
				if(isTop){
					isTop = false;
				}else{
					Intent intent2 = new Intent(Constant.STOP_LISTENER);
					context.sendBroadcast(intent2);
				}
				break;
			}

			return true;
		}

	}

	private OnCtrlMediaListener ctrlMediaListener;

	public interface OnCtrlMediaListener {
		void pause();

		void play();

		void next();
	}

}
