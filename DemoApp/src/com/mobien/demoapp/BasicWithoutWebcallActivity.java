package com.mobien.demoapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;

public class BasicWithoutWebcallActivity extends Activity {
	
	private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_basic_without_webcall);
        mContext = this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_basic_without_webcall, menu);
        return true;
    }
    
    public boolean onKeyDown(int keyCode, KeyEvent event) {
		
    	finish();
		return super.onKeyDown(keyCode, event);
	}

	/*
	 * This method is invoked on the press of back button in placed in the
	 * header.
	 */
	public void onBackClick(View view) {

		finish();
	}
    
}
