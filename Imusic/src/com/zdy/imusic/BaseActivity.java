package com.zdy.imusic;

import com.zdy.imusic.viewcontrol.ViewInflate;

import android.app.Activity;
import android.os.Bundle;

public abstract class BaseActivity<V extends ViewInflate> extends Activity {
	
	protected V fitView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		try {
			
			fitView = getFitClass().newInstance();
			fitView.init(getLayoutInflater(), null);
			setContentView(fitView.getView());
			onBindView();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	protected void onDestroy() {
		onDestroyView();
		fitView = null;
		super.onDestroy();
	}
	
	
	protected abstract Class<V> getFitClass();
	
	protected void onBindView(){};
	
	protected void onDestroyView(){};

}
