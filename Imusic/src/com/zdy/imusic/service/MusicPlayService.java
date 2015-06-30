package com.zdy.imusic.service;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import com.zdy.imusic.Constant;
import com.zdy.imusic.R;
import com.zdy.imusic.api.BaiduMusicAPI;
import com.zdy.imusic.entity.Song;
import com.zdy.imusic.entity.Music.MusicInfo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.widget.RemoteViews;
import android.widget.Toast;

public class MusicPlayService extends Service implements OnPreparedListener,
		OnCompletionListener, OnErrorListener {

	private MediaPlayer mediaPlayer;

	private MyBinder myBinder = new MyBinder();

	private Song song;

	private Intent intent;

	private MusicInfo[] info;

	private int currentPostion;

	/**
	 * 发送广播的间隔，总共要发360次
	 */
	private long time = 100;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				intent.putExtra("progress", mediaPlayer.getCurrentPosition());
				sendBroadcast(intent);
				handler.sendEmptyMessageDelayed(1, time);
				break;

			case 2:
				play(song);
				break;
			}

		};
	};

	@Override
	public IBinder onBind(Intent intent) {
		return myBinder;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		intent = new Intent(Constant.MUSIC_PROGRESS);
		if (mediaPlayer == null) {
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnPreparedListener(this);
			mediaPlayer.setOnCompletionListener(this);
			mediaPlayer.setOnErrorListener(this);
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	// 播放音乐
	public void play(Song song) {
		this.song = song;
		try {
			mediaPlayer.reset();
			mediaPlayer.setDataSource(song.getBitrate().getFile_link());
			mediaPlayer.prepare();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void goOnPlay() {
		if (!mediaPlayer.isPlaying()) {
			handler.sendEmptyMessage(1);
			mediaPlayer.start();
			showNotification();
		}
	}

	// 暂停
	public void pause() {
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			handler.removeMessages(1);
			mediaPlayer.pause();
			showNotification();
		}
	}

	// 停止
	public void stop() {
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}

	public class MyBinder extends Binder {

		public void startPlay(MusicInfo[] info) {
			MusicPlayService.this.info = info;
			getSongByIdWithCallback(info[0].getSongid());
		}

		public void pause() {
			MusicPlayService.this.pause();
		}

		public void playGo() {
			goOnPlay();
		}

		public void next() {
			MusicPlayService.this.next();
		}

	}

	/**
	 * OnPreparedListener
	 */
	@Override
	public void onPrepared(MediaPlayer mp) {
		new Thread() {
			@Override
			public void run() {
				Song nextSong = getSongById(info[currentPostion + 1]
						.getSongid());
				if (nextSong != null) {
					// 发送歌曲信息回去
					Intent intent = new Intent(Constant.MUSIC_INFO_CALLBACK);
					intent.putExtra("nextSong", nextSong);
					sendBroadcast(intent);
				}
			}
		}.start();

		mediaPlayer.start();
		
		showNotification();
		Intent intent = new Intent(Constant.MAX_PROGRESS);
		intent.putExtra("max", mediaPlayer.getDuration());
		intent.putExtra("currentPos", mediaPlayer.getCurrentPosition());
		sendBroadcast(intent);
		handler.sendEmptyMessage(1);
	}

	/**
	 * 创建通知
	 */
	private void showNotification() {
			
		NotificationCompat.Builder builder =  new Builder(this);
		
		RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification);
		
		remoteViews.setImageViewResource(R.id.notify_icon, R.drawable.notification_icon);
		
		if(mediaPlayer.isPlaying()){
			remoteViews.setImageViewResource(R.id.notify_pause, R.drawable.apollo_holo_dark_pause);
			Intent pauseIntent = new Intent(Constant.NOTIFY_PAUSE);
			PendingIntent pausePendingIntent = PendingIntent.getBroadcast(this, 1, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			remoteViews.setOnClickPendingIntent(R.id.notify_pause, pausePendingIntent);
		}else{
			remoteViews.setImageViewResource(R.id.notify_pause, R.drawable.apollo_holo_dark_play);
			Intent playIntent = new Intent(Constant.NOTIFY_PLAY);
			PendingIntent playPendingIntent = PendingIntent.getBroadcast(this, 1, playIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			remoteViews.setOnClickPendingIntent(R.id.notify_pause, playPendingIntent);
		}
		
		remoteViews.setImageViewResource(R.id.notify_off, R.drawable.apollo_holo_dark_notifiation_bar_collapse);
		remoteViews.setImageViewResource(R.id.notify_next, R.drawable.apollo_holo_dark_next);
		remoteViews.setTextViewText(R.id.notify_songname, song.getSonginfo().getTitle());
		remoteViews.setTextViewText(R.id.notify_singername, song.getSonginfo().getAuthor());
		

		
		Intent nextIntent = new Intent(Constant.NOTIFY_NEXT);
		PendingIntent nextPendingIntent = PendingIntent.getBroadcast(this, 2, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		remoteViews.setOnClickPendingIntent(R.id.notify_next, nextPendingIntent);
		
		
		
		builder.setContent(remoteViews)
				.setTicker("正在播放")
				.setWhen(System.currentTimeMillis())
				.setPriority(Notification.PRIORITY_DEFAULT)
				.setOngoing(true)
				.setDefaults(Notification.DEFAULT_VIBRATE)
				.setSmallIcon(R.drawable.notification_icon);
				
		Notification notification = builder.build();
		notification.flags = Notification.FLAG_ONGOING_EVENT;  
		startForeground(1, notification);
				
	}	

	// 暂停
	public void next() {
		getSongByIdWithCallback(info[currentPostion + 1].getSongid());
	}

	/**
	 * 播放完毕的时候调用
	 */
	@Override
	public void onCompletion(MediaPlayer mp) {
		handler.removeMessages(1);
		getSongByIdWithCallback(info[currentPostion + 1].getSongid());
	}

	/*
	 * 播放出错的时候
	 */
	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		
		Toast.makeText(this, "播放出错", 0).show();
		return false;
	}

	private Song getSongById(String id) {
		RestAdapter adapter = new RestAdapter.Builder().setEndpoint(
				Constant.BASE_URL).build();

		BaiduMusicAPI musicAPI = adapter.create(BaiduMusicAPI.class);
		String from = "webapp_music";
		String method = "baidu.ting.song.play";
		String format = "json";
		String query = id;

		return musicAPI.getMusicById(from, method, format, query);

	}

	private void getSongByIdWithCallback(String id) {

		handler.removeMessages(1);
		Intent okIntent = new Intent(Constant.MUSIC_PROGRESS);
		okIntent.putExtra("ok", true);
		sendBroadcast(okIntent);

		RestAdapter adapter = new RestAdapter.Builder().setEndpoint(
				Constant.BASE_URL).build();

		BaiduMusicAPI musicAPI = adapter.create(BaiduMusicAPI.class);
		String from = "webapp_music";
		String method = "baidu.ting.song.play";
		String format = "json";
		String query = id;

		musicAPI.getMusicById(from, method, format, query,
				new Callback<Song>() {

					@Override
					public void success(Song song, Response arg1) {
						currentPostion++;
						// 发送歌曲信息回去
						Intent intent = new Intent(Constant.MUSIC_INFO_CALLBACK);
						intent.putExtra("song", song);
						sendBroadcast(intent);
						// 播放歌曲
						play(song);
						// MusicPlayService.this.song = song;
						// handler.sendEmptyMessage(2);
					}

					@Override
					public void failure(RetrofitError arg0) {

					}
				});

	}


}
