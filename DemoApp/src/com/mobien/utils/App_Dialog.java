package com.mobien.utils;


import com.mobien.demoapp.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TextView;

public class App_Dialog extends Dialog implements OnTouchListener {

	public static boolean DIALOG_RESPONCE;
	Context mContext;

	String _HeadMessage = "", _DetailMessage = "";
	int _DIALOG_TYPE = 0;

	public App_Dialog(Context context, String HeadMessage,
			String DetailMessage, int DIALOG_TYPE) {
		super(context);
		// TODO Auto-generated constructor stub
		mContext = context;
		_HeadMessage = HeadMessage;
		_DetailMessage = DetailMessage;
		_DIALOG_TYPE = DIALOG_TYPE;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		try {
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.app_dialog);

			TextView tvDialogHeader = (TextView) findViewById(R.id.tvMessageHeader);
			TextView tvDialogDetail = (TextView) findViewById(R.id.tvMessageDetail);
			Button YESOK = (Button) findViewById(R.id.btnYesOk);
			Button NO = (Button) findViewById(R.id.btnNo);

			if (_HeadMessage.equals("")) {
				tvDialogHeader.setVisibility(View.GONE);
			}
			if (_DetailMessage.equals("")) {
				tvDialogDetail.setVisibility(View.GONE);
			}
			tvDialogHeader.setText(_HeadMessage.toString());
			tvDialogDetail.setText(_DetailMessage.toString());

			if (_DIALOG_TYPE == 0) {
				YESOK.setVisibility(View.VISIBLE);
				NO.setVisibility(View.GONE);
				YESOK.setText("OK");
				YESOK.setOnClickListener(new YESOKListener());

			} else if (_DIALOG_TYPE == 1) {
				NO.setVisibility(View.VISIBLE);
				YESOK.setVisibility(View.VISIBLE);
				YESOK.setText("Yes");
				YESOK.setOnClickListener(new YESOKListener());
				NO.setOnClickListener(new NOListener());

			}
			YESOK.requestFocus();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private class YESOKListener implements android.view.View.OnClickListener {
		@Override
		public void onClick(View v) {

			DIALOG_RESPONCE = true;

			App_Dialog.this.dismiss();
		}
	}

	private class NOListener implements android.view.View.OnClickListener {
		@Override
		public void onClick(View v) {
			DIALOG_RESPONCE = false;

			App_Dialog.this.dismiss();
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			DIALOG_RESPONCE = true;
			App_Dialog.this.dismiss();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
