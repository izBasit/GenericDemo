package com.mobien.demoapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;

import com.mobien.utils.App_Dialog;
import com.mobien.webservices.HttpEngine;

public class AfterLoginActivity extends Activity {

	private Context mContext;

	/*
	 * @param DIALOG_YES_NO, DIALOG_OK These variables are used in case of
	 * dialogs When you give 1 as Dialog type you get 2 buttons; namely Yes &
	 * No. When you give 0 you get a single button named Ok
	 */
	private int DIALOG_YES_NO = 1, DIALOG_OK = 0;

	/*
	 * @param exceptionFlag This flag is used as a check for the status of web
	 * service. If a web service call fails an exception is throw, and upon
	 * exception this flag is set to true.
	 */
	private boolean exceptionFlag = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_after_login);
		mContext = this;

		// TODO Just giving a web call. Give the call as per your convenience
		new AsyncWebRequest().execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_temporary_activity, menu);
		return true;
	}

	public class AsyncWebRequest extends AsyncTask<String, Void, Void> {

		/*
		 * Modify data flag according to your will for checking whether the data
		 * you are getting is correct.
		 */
		Boolean dataFlag = false;
		String errorMessage;
		ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pd = ProgressDialog
					.show(mContext, "",
							"Please wait while transaction is being synchronised with iNotify Server.");
			pd.setCancelable(false);

		}

		@Override
		protected Void doInBackground(String... params) {

			// TODO Change Input parameters according to your web service.
			String[][] inparams = new String[1][2];
			inparams[0][0] = "message";
			inparams[0][1] = "value";
			// TODO Change Input parameters according to your web service.
			String[] outParams = { "LeaveRequestResult", "errormessage" };
			try {
				System.out.println("Attempting web call!!");

				// TODO Change yourWebMethod to whatever web method is to be
				// called.
				String[][] reponse = new HttpEngine().webRequest(
						"yourWebMethod", mContext, inparams, outParams);
				// TODO response of your web service. Use accordingly.
				dataFlag = Boolean.parseBoolean(reponse[0][1]);

			} catch (Exception e) {
				e.printStackTrace();
				exceptionFlag = true;
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			System.out.println("in onPost");
			pd.dismiss();

			/*
			 * When there is an exception in your web service call.
			 */
			if (exceptionFlag) {
				App_Dialog dialog = new App_Dialog(mContext,
						"iNotify Server not responding.",
						"Please Try again in some time", DIALOG_OK);
				dialog.show();
			} else {

				if (!dataFlag) {

					App_Dialog dialog = new App_Dialog(mContext, "",
							"Synchronisation with iNotify failed.", DIALOG_OK);
					dialog.show();
					dialog.setCancelable(false);
					dialog.setOnDismissListener(new OnDismissListener() {
						@Override
						public void onDismiss(DialogInterface dialog) {
							if (App_Dialog.DIALOG_RESPONCE) {
								Intent intent = new Intent();
								intent.setClass(getApplicationContext(),
										LoginActivity.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								intent.putExtra("EXIT", true);
								startActivity(intent);
								finish();
							}
						}
					});
				}

				else {

					// TODO Everything has worked according to your plan. Write
					// your logic below this line. :D
				}

			}

		}

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			App_Dialog dialog = new App_Dialog(mContext,
					"Do you want to Log out?", "", DIALOG_YES_NO);
			dialog.show();
			dialog.setCancelable(false);
			dialog.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					if (App_Dialog.DIALOG_RESPONCE) {
						Intent intent = new Intent();
						intent.setClass(getApplicationContext(),
								LoginActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						intent.putExtra("EXIT", true);
						startActivity(intent);
						finish();
					}
				}
			});
		}

		return super.onKeyDown(keyCode, event);
	}

	/*
	 * This method is invoked on the press of back button in placed in the
	 * header.
	 */
	public void onBackClick(View view) {

		App_Dialog dialog = new App_Dialog(mContext, "Do you want to Log out?",
				"", DIALOG_YES_NO);
		dialog.show();
		dialog.setCancelable(false);
		dialog.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				if (App_Dialog.DIALOG_RESPONCE) {
					Intent intent = new Intent();
					intent.setClass(getApplicationContext(),
							LoginActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.putExtra("EXIT", true);
					startActivity(intent);
					finish();
				}
			}
		});

	}
}
