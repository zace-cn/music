package com.zdy.imusic.activity;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Toast;

import com.baidu.speech.VoiceRecognitionService;
import com.zdy.imusic.BaseActivity;
import com.zdy.imusic.Constant;
import com.zdy.imusic.api.BaiduMusicAPI;
import com.zdy.imusic.entity.Music;
import com.zdy.imusic.viewcontrol.impl.ListenLayoutImpl;

public class ListenActivity extends BaseActivity<ListenLayoutImpl> implements
		OnTouchListener, RecognitionListener {

	private long speechStartTime = -1;
	private BroadcastReceiver receiver;
	private boolean isFromPlay;
	
	/**
	 * 语音监听的对象
	 */
	private SpeechRecognizer speechRecognizer;

	@Override
	protected Class<ListenLayoutImpl> getFitClass() {
		return ListenLayoutImpl.class;
	}

	@Override
	protected void onBindView() {
		fitView.setTitleMsg("告诉我想听什么");
		fitView.dismissLoadView();
		fitView.setTouchListen(this);
		receiver = new StopBroadcastReceiver();
		initSpeechRecognizer();
	}
	
	
	@Override
	protected void onStart() {
		super.onStart();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constant.STOP_LISTENER);
		registerReceiver(receiver, filter);
		
		
		isFromPlay = getIntent().getBooleanExtra("isFromPlay", false);
		if (isFromPlay) {
			Intent intent = new Intent();
			bindParams(intent);
			speechStartTime = System.currentTimeMillis();
			speechRecognizer.startListening(intent);

		}
	}

	/**
	 * 初始化语音监听
	 */
	private void initSpeechRecognizer() {
		if(speechRecognizer==null){
			speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this,
					new ComponentName(this, VoiceRecognitionService.class));
			speechRecognizer.setRecognitionListener(this);
		}

	}
	
	


	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int action = event.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			Intent intent = new Intent();
			bindParams(intent);
			speechStartTime = System.currentTimeMillis();

			speechRecognizer.startListening(intent);
			break;

		case MotionEvent.ACTION_MOVE:

			break;
		case MotionEvent.ACTION_UP:
			fitView.showLoadView();
			long time = System.currentTimeMillis() - speechStartTime;
			if (time < 300) {
				if (speechRecognizer != null) {
					speechRecognizer.stopListening();
					speechRecognizer.cancel();
					fitView.dismissLoadView();
				}
				break;
			}
			speechRecognizer.stopListening();
			fitView.setVoiceDismiss();
			break;
		}

		return true;
	}

	@Override
	protected void onDestroyView() {
		unregisterReceiver(receiver);
		speechRecognizer.destroy();
		super.onDestroyView();
	}

	/**
	 * @param intent
	 *            设置监听语音的一些参数
	 */
	private void bindParams(Intent intent) {
		intent.putExtra(Constant.EXTRA_LANGUAGE, "cmn-Hans-CN");
		intent.putExtra(Constant.EXTRA_SAMPLE, 16000);
	}

	/*
	 * 准备好，可以说话
	 */
	@Override
	public void onReadyForSpeech(Bundle params) {
		fitView.setVoiceAppear();
	}

	@Override
	public void onBeginningOfSpeech() {

	}

	@Override
	public void onRmsChanged(float rmsdB) {

	}

	@Override
	public void onBufferReceived(byte[] buffer) {

	}

	@Override
	public void onEndOfSpeech() {
		speechStartTime = System.currentTimeMillis();

	}

	@Override
	public void onError(int error) {
		fitView.dismissLoadView();
		fitView.setVoiceDismiss();
		StringBuilder sb = new StringBuilder();
		switch (error) {
		case SpeechRecognizer.ERROR_AUDIO:
			sb.append("音频问题");
			break;
		case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
			sb.append("没有语音输入");
			JumpToPlay();
			break;
		case SpeechRecognizer.ERROR_CLIENT:
			sb.append("其它客户端错误");
			break;
		case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
			sb.append("权限不足");
			break;
		case SpeechRecognizer.ERROR_NETWORK:
			sb.append("网络问题");
			break;
		case SpeechRecognizer.ERROR_NO_MATCH:
			sb.append("没有匹配的识别结果");
			JumpToPlay();
			break;
		case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
			sb.append("引擎忙");
			break;
		case SpeechRecognizer.ERROR_SERVER:
			sb.append("服务端错误");
			break;
		case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
			sb.append("连接超时");
			break;
		}
		Toast.makeText(this, sb.toString(), 0).show();
	}
	
	@Override
	public void onResults(Bundle results) {
		fitView.dismissLoadView();
		ArrayList<String> nbest = results
				.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
		// String json_res = results.getString("origin_result");

		String name = nbest.get(0);
		if (!TextUtils.isEmpty(name)) {
			try {
				RestAdapter adapter = new RestAdapter.Builder().setEndpoint(
						Constant.BASE_URL).build();

				BaiduMusicAPI musicAPI = adapter.create(BaiduMusicAPI.class);
				String from = "webapp_music";
				String method = "baidu.ting.search.catalogSug";
				String format = "json";
				String query = name;

				musicAPI.getMusicInfo(from, method, format, query,
						new Callback<Music>() {

							@Override
							public void success(Music result, Response arg1) {
								if (result.getSong().length > 0) {

//									Toast.makeText(
//											ListenActivity.this,
//											result.getSong()[0].getArtistname(),
//											0).show();
									Intent intent = new Intent(
											ListenActivity.this,
											PlayActivity.class);
									intent.putExtra("music", result.getSong());
									startActivity(intent);
									overridePendingTransition(
											android.R.anim.fade_in,
											android.R.anim.fade_out);
								} else {
									Toast.makeText(ListenActivity.this,
											"没有找到相关歌曲", 0).show();
									
									JumpToPlay();
									
									
								}

							}

							@Override
							public void failure(RetrofitError arg0) {
							}
						});
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		Toast.makeText(this, nbest.get(0), 0).show();
	}

	@Override
	public void onPartialResults(Bundle partialResults) {

	}

	@Override
	public void onEvent(int eventType, Bundle params) {

	}

	private void JumpToPlay(){
		if(isFromPlay){
			Intent intent = new Intent(ListenActivity.this,PlayActivity.class);
			intent.putExtra("isContinue", true);
			startActivity(intent);
		}
	}

	class StopBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			switch (intent.getAction()) {
			case Constant.STOP_LISTENER:
				
				if(speechRecognizer!=null){
					fitView.showLoadView();
					long time = System.currentTimeMillis() - speechStartTime;
					if (time < 300) {
						if (speechRecognizer != null) {
							speechRecognizer.cancel();
							fitView.dismissLoadView();
						}
						return;
					}
					speechRecognizer.stopListening();
					fitView.setVoiceDismiss();
				}
				
				break;
			}
		}

	}

}
