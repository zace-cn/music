package com.zdy.imusic;

import android.app.Application;
import android.content.Context;

import com.zdy.imusic.utils.DisplayUtil;

public class App extends Application {

	public static App app;
	public static Context context;
	@Override
	public void onCreate() {
		super.onCreate();
		context = this;
		app = this;
		DisplayUtil.init(this);
		MyContext.init(this);
	}
}
