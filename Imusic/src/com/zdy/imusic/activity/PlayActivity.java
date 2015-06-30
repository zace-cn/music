package com.zdy.imusic.activity;

import retrofit.RestAdapter;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Parcelable;
import android.text.TextUtils;

import com.zdy.imusic.BaseActivity;
import com.zdy.imusic.Constant;
import com.zdy.imusic.R;
import com.zdy.imusic.api.BaiduMusicAPI;
import com.zdy.imusic.entity.Lyrics;
import com.zdy.imusic.entity.Music.MusicInfo;
import com.zdy.imusic.entity.Song;
import com.zdy.imusic.service.MusicPlayService;
import com.zdy.imusic.view.CircleImageView;
import com.zdy.imusic.view.CircleImageView.OnSeekChangeListener;
import com.zdy.imusic.viewcontrol.impl.PlayLayoutImpl;
import com.zdy.imusic.viewcontrol.impl.PlayLayoutImpl.OnCtrlMediaListener;

public class PlayActivity extends BaseActivity<PlayLayoutImpl> implements OnCtrlMediaListener {

	private MusicPlayService.MyBinder myBinder;
	private MusicRecieve recieve;
	private MusicInfo[] info;
	private Intent service;
	private boolean isContinue;
	private ServiceConnection connection = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			System.out.println();
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			myBinder = (MusicPlayService.MyBinder) service;

			Intent intent = getIntent();
			Parcelable[] songs = intent.getParcelableArrayExtra("music");
			info = new MusicInfo[songs.length];
			for (int i = 0; i < songs.length; i++) {
				info[i] = (MusicInfo) songs[i];
			}

			myBinder.startPlay(info);

		}
	};

	@Override
	protected Class<PlayLayoutImpl> getFitClass() {
		return PlayLayoutImpl.class;
	}

	@Override
	protected void onBindView() {
		recieve = new MusicRecieve();
		fitView.setDefultImg(this, R.drawable.music_cover_default);
		fitView.setCircleImgOnTouch(this, this);
		fitView.setImageViewOnSeekListener(new OnCircleBarChange());
		service = new Intent(this, MusicPlayService.class);
		bindService(service, connection, BIND_AUTO_CREATE);
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		
		isContinue =intent.getBooleanExtra("isContinue", false);
		if(isContinue){
			fitView.ViewToCenter(true);
			myBinder.playGo();
		}
		
	}
	
	
	

	@Override
	protected void onStart() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constant.MUSIC_PROGRESS);
		filter.addAction(Constant.MUSIC_INFO_CALLBACK);
		filter.addAction(Constant.MAX_PROGRESS);
		registerReceiver(recieve, filter);
		super.onStart();
	}

	@Override
	protected void onStop() {

		super.onStop();
	}

	@Override
	protected void onDestroyView() {
		unbindService(connection);
		unregisterReceiver(recieve);
		super.onDestroyView();
	}

	// @Override
	// protected void onSaveInstanceState(Bundle outState) {
	// outState.putInt("progress", fitView.getProgress());
	// outState.putParcelable("song", song);
	// super.onSaveInstanceState(outState);
	// }
	//
	//
	// @Override
	// protected void onRestoreInstanceState(Bundle savedInstanceState) {
	// int progress = savedInstanceState.getInt("progress");
	// song = savedInstanceState.getParcelable("song");
	// fitView.setProgress(progress);
	// fitView.setImg(PlayActivity.this, song.getSonginfo()
	// .getPic_huge());
	// fitView.setText(song.getSonginfo().getTitle(), song
	// .getSonginfo().getAuthor());
	// super.onRestoreInstanceState(savedInstanceState);
	// }

	private Song song;
	private String str;
	class MusicRecieve extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			switch (intent.getAction()) {
			case Constant.MUSIC_PROGRESS:
				if (intent.getBooleanExtra("ok", false)) {
					fitView.resetProgress();
					return;
				}
				
				int progress = intent.getIntExtra("progress", 0);
				
				fitView.setProgress(progress);
				
				break;

			case Constant.MUSIC_INFO_CALLBACK:

				song = (Song) intent.getParcelableExtra("song");
				
				if (song == null) {
					song = (Song) intent.getParcelableExtra("nextSong");
					fitView.setNextmg(context, song.getSonginfo().getPic_huge());
					return;
				}
				
				fitView.setImg(PlayActivity.this, song.getSonginfo().getPic_huge());
				fitView.setText(song.getSonginfo().getTitle(), song.getSonginfo().getAuthor());

				break;
			case Constant.MAX_PROGRESS://设置最大值，也能认为开始播放了
				int max = intent.getIntExtra("max", 0);
				final int currentPos = intent.getIntExtra("currentPos", 0);
				fitView.setMaxProgress(max);
				
				new Thread(){
					public void run() {
						RestAdapter adapter = new RestAdapter.Builder().setEndpoint(
								Constant.BASE_URL).build();

						BaiduMusicAPI musicAPI = adapter.create(BaiduMusicAPI.class);
						String from = "webapp_music";
						String method = "baidu.ting.song.lry";
						String format = "json";
						String query = song.getSonginfo().getSong_id();
						Lyrics lyrics = musicAPI.getMusicLycById(from, method, format, query);
						
						str = lyrics.getLrcContent();
						
						if(!TextUtils.isEmpty(str)){
							fitView.setLycSizeAndLyrics(str,currentPos);
						}
						
					};
				}.start();
				
				
				break;
			}

		}

	}

	
	/**
	 * 进度条监听
	 * @author Administrator
	 *
	 */
	class OnCircleBarChange implements OnSeekChangeListener{

		@Override
		public void onProgressChange(CircleImageView view, int newProgress) {
			
		}
		
	}
	
	
	
	@Override
	public void pause() {
		myBinder.pause();
	}

	@Override
	public void play() {
		myBinder.playGo();
	}

	@Override
	public void next() {
		myBinder.next();

	}
	
	
	
	

}
