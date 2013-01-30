package com.mobien.demoapp;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Vector;

import org.kxml2.io.KXmlParser;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mobien.database.DatabaseHelper;
import com.mobien.utils.AppBaseActivity;
import com.mobien.utils.App_Dialog;
import com.mobien.utils.NetworkUtility;
import com.mobien.webservices.Generic_XmlParser;
import com.mobien.webservices.HttpEngine;
import com.mobien.webservices.XmlNode;

public class LoginActivity extends AppBaseActivity {

	private String mUsername;
	private String mPassword;
	/*
	 * @param DIALOG_YES_NO, DIALOG_OK
	 * These variables are used in case of dialogs When you give 1 as Dialog
	 * type you get 2 buttons; namely Yes & No. When you give 0 you get a single
	 * button named Ok
	 */
	private int DIALOG_YES_NO = 1, DIALOG_OK = 0;

	PowerManager.WakeLock wl;
	// UI references.
	private EditText mUserView;
	private EditText mPasswordView;
	private TextView tvVersionName;
	private App_Dialog dialog;

	public static DatabaseHelper dbUtilObj;
	public static String DATABASE_NAME = "";
	public static String DATABASE_PATH = "";
	public Context mContext;

	/*
	 * @param exceptionFlag This flag is used as a check for the status of web
	 * service. If a web service call fails an exception is throw, and upon
	 * exception this flag is set to true.
	 */
	private boolean exceptionFlag = false;
	private String versionName = "";
	private NetworkUtility netUtil;

	private String PACKAGE;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		mContext = this;

		// tvVersionName = (TextView) findViewById(R.login.tvVersionName);

		// Version Name of the application
		try {
			versionName = getPackageManager().getPackageInfo(getPackageName(),
					0).versionName;
			// tvVersionName.setText(versionName);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		PACKAGE = getPackageName();

		// Give Database Name..
		// TODO Give a name to your database
		DATABASE_NAME = "yourDatabaseName.db";
		
		DATABASE_PATH = "/data/data/" + PACKAGE + "/databases/";

		netUtil = new NetworkUtility(mContext);
		dbUtilObj = new DatabaseHelper(mContext);

		mUserView = (EditText) findViewById(R.login.et_loginId);
		mPasswordView = (EditText) findViewById(R.login.et_password);
		//
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_login, menu);
		return true;
	}

	public void onClick(View view) {
		LoginAuthentication();
	}

	public void LoginAuthentication() {

		mUsername = mUserView.getText().toString().trim();
		mPassword = mPasswordView.getText().toString().trim();

		try {

			if (mUsername.equals("") && mPassword.equals("")) {
				mUserView.requestFocus();

				dialog = new App_Dialog(mContext,
						"Please Enter Login Id and Password.", "", DIALOG_OK);
				dialog.show();
			} else if (mUsername.equals("")) {
				mUserView.requestFocus();
				mUserView.setText("");
				dialog = new App_Dialog(mContext, "Please Enter Login Id.", "",
						DIALOG_OK);
				dialog.show();

			} else if (mPassword.equals("")) {
				mPasswordView.requestFocus();
				mPasswordView.setText("");
				dialog = new App_Dialog(mContext, "Please Enter Password.", "",
						DIALOG_OK);
				dialog.show();

			} else if (!dbUtilObj.dbexists()) {

				if (netUtil.isOnline()) {
					// Calling web service.
					new Authenticate_User().execute();
				} else {
					dialog = new App_Dialog(mContext,
							"No Internet Connectivity", "", DIALOG_OK);
					dialog.show();
				}

			} else {

				System.out.println(mUsername);
				System.out.println(dbUtilObj.getUserData_atPosition(0));

				if (!mUsername.equals(dbUtilObj.getUserData_atPosition(0))) {
					mUserView.requestFocus();
					mUserView.setText("");
					mPasswordView.setText("");
					dialog = new App_Dialog(mContext, "Invalid Login Id.", "",
							DIALOG_OK);
					dialog.show();

				} else if (!mPassword.equals(dbUtilObj
						.getUserData_atPosition(1))) {
					mPasswordView.requestFocus();
					mPasswordView.setText("");
					dialog = new App_Dialog(mContext, "Invalid Password.", "",
							DIALOG_OK);
					dialog.show();

				} else {

					// Will use this code to go to next activity from 2nd time
					// login.
					Intent intent = new Intent();
					// TODO Put the name of your next activity
//					intent.setClass(mContext, yourNextActivity.class);
					startActivity(intent);
					overridePendingTransition(R.anim.gd_grow_from_top,
							R.anim.gd_shrink_from_bottom);
					finish();
				}
			}

		} catch (Exception e) {
			// e.printStackTrace();
			Toast.makeText(mContext,
					"Error Msg : " + e.getMessage().toString(),
					Toast.LENGTH_LONG).show();
		}
	}

	/*
	 * @params Boolean flag = true only when authentication fails
	 */

	public class Authenticate_User extends AsyncTask<Void, Void, Void> {

		ProgressDialog pd;
		Boolean flag = false;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pd = ProgressDialog
					.show(mContext, "",
							"Initialising the application and data with iNotify Server.");
			pd.setCancelable(false);

		}

		@Override
		protected Void doInBackground(Void... params) {

			// TODO Change Input parameters according to your web service.
			// Input parameters for web service
			String[][] inparams = new String[2][2];
			inparams[0][0] = "UserCode";
			inparams[0][1] = mUsername;
			inparams[1][0] = "Password";
			inparams[1][1] = mPassword;
			
			// TODO Change Input parameters according to your web service.
			// Output parameter Keys for web service
			String[] outParams = { "AuthenticateResult", "sXmlCreateScripts",
					"sXMLShortCode", "sXmlInsertScripts" };
			try {
				
				// TODO Change yourWebMethod to whatever web method is to be called.
				
				System.out.println("Attempting web call!!");
				//TODO response of your web service. Use accordingly.
				String[][] response = new HttpEngine().webRequest("yourWebMethod",
						mContext, inparams, outParams);

				// response
				String tempResponse[] = new String[3];

				/*
				 * The first element in response is a boolean flag which gives
				 * true - if authentication is successful OR 
				 * false - if authentication fails. 
				 * So before doing anything else we will
				 * check what the flag is and move forward accordingly.
				 */
				Boolean authentication = Boolean.parseBoolean(response[0][1]);
				tempResponse[0] = response[1][1];
				tempResponse[1] = response[2][1];
				tempResponse[2] = response[3][1];

				if (authentication) {
					
					String[] createQueryXml = parseXML(tempResponse[0]);
					System.out.println("createQueryXml qyuery length:"
							+ createQueryXml.length);
					System.out.println("CREATE SCRIPT :");
					dbUtilObj.executeXmlQuery(createQueryXml, mContext);

					// Insert scripts for Short codes
					System.out.println("INSERT SHORTCODE :");
					String[] insertQueryXml = parseXML(tempResponse[1]);
					dbUtilObj.executeXmlQuery(insertQueryXml, mContext);
					System.out.println("insertQueryXml qyuery length:"
							+ insertQueryXml.length);

					// Insert Queries
					System.out.println("INSERT SCRIPT :");
					String[] insertScripts = parseXML(tempResponse[2]);
					dbUtilObj.executeXmlQueryUsingShortCodes(insertScripts,
							mContext);

				} else {
					flag = true;
				}
			} catch (Exception e) {
				e.printStackTrace();
				exceptionFlag = true;
			}

			return null;

		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			System.out.println("IN ON POST!!=============");
			pd.dismiss();

			if (exceptionFlag) {

				App_Dialog dialog = new App_Dialog(mContext,
						"iNotify Server not responding.",
						"Please Try again in some time", 0);
				dialog.show();
			} else {

				System.out.println("FLAG : -------------" + flag);
				if (flag) {

					App_Dialog dialog = new App_Dialog(mContext, "",
							"Login Authentication Failed!!", 0);
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
								overridePendingTransition(
										R.anim.gd_grow_from_top,
										R.anim.gd_shrink_from_bottom);
							}
						}
					});
				}

				else {

					// Will use this block of code to go to next activity after
					// 1st login.
					Intent intent = new Intent();
					// TODO Put the name of your next activity
//					intent.setClass(mContext, yourNextActivity.class);
					startActivity(intent);
					overridePendingTransition(R.anim.gd_grow_from_top,
							R.anim.gd_shrink_from_bottom);
					finish();
				}

			}

		}

	}

	// Parsing the response
	public static String[] parseXML(String reply) {
		int ind = reply.indexOf("~");

		while (ind != -1) {
			String first = reply.substring(0, ind);
			String two = reply.substring(ind + 1);
			reply = first + ";" + two;
			ind = reply.indexOf("~");
		}

		String[] messages = { "" };
		try {
			KXmlParser parser = new KXmlParser();
			Generic_XmlParser gParser = new Generic_XmlParser();
			ByteArrayInputStream bais = new ByteArrayInputStream(
					reply.getBytes());
			InputStreamReader reader = new InputStreamReader(bais);
			parser.setInput(reader);

			XmlNode xml = gParser.parseXML(parser, true);
			Vector data = gParser.elements;
			messages = new String[data.size()];
			int index = 0;
			for (int i = 0; i < data.size(); i++) {
				if (!data.isEmpty()) {
					messages[index] = data.elementAt(i).toString();
					index++;
				}
			}
		} catch (Exception e) {
			// com.mobien.pmi.screen.Login.UTILITY.writeToSDFile(e.getMessage()+"     message*************    ");
			e.printStackTrace();
			System.out.println("....." + e.toString());
		}
		return messages;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
			final List<ActivityManager.RunningServiceInfo> services = activityManager
					.getRunningServices(Integer.MAX_VALUE);
			for (int i = 0; i < services.size(); i++) {
				String packageClassName = services.get(i).service
						.getClassName();
				if (packageClassName.equals(PACKAGE)) {

					android.os.Process.killProcess(services.get(i).pid);
				}
			}

			try {
				if (wl.isHeld()) {
					wl.release();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			Intent i = new Intent();
			i.setAction(Intent.ACTION_MAIN);
			i.addCategory(Intent.CATEGORY_HOME);
			PackageManager pm = LoginActivity.this.getPackageManager();
			ResolveInfo ri = pm.resolveActivity(i, 0);
			ActivityInfo ai = ri.activityInfo;
			String pkgHome = ai.packageName.toString();
			String actHome = ai.name;

			closeAllActivities();
			Intent startMain = new Intent(Intent.ACTION_MAIN);
			startMain.addCategory(Intent.CATEGORY_HOME);
			startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(startMain);

			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	protected void closeAllActivities() {
		sendBroadcast(new Intent(FINISH_ALL_ACTIVITIES_ACTIVITY_ACTION));
	}
}
