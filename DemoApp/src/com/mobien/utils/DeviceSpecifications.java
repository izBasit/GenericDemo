package com.mobien.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.BatteryManager;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

public class DeviceSpecifications {
	Activity mactivity;
	public static int level, per_value;

	public DeviceSpecifications(Activity activity) {
		mactivity = activity;
		level = -1;
	}

	public String osVersion() {
		String version = System.getProperty("os.version");
		return version;
	}

	public String platformVersion() {
		String PVersion = android.os.Build.VERSION.RELEASE;
		return PVersion;
	}

	public String modelNumber() {
		String modelNo = android.os.Build.MODEL;
		return modelNo;
	}

	public int batteryLevel() {

		BroadcastReceiver batteryReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);

			}

		};
		
		
			IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
			mactivity.registerReceiver(batteryReceiver, filter);


		return level;
	}

	public int simPresent() {

		int state = TelephonyManager.SIM_STATE_ABSENT;

		return state;
	}

	public String imeiNo() {

		TelephonyManager tManager = (TelephonyManager) mactivity
				.getSystemService(Context.TELEPHONY_SERVICE);
		String uid = tManager.getDeviceId();

		return uid;
	}

	public Boolean hasCamera() {

		PackageManager pm = mactivity.getApplicationContext()
				.getPackageManager();
		Boolean camera = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);

		return camera;
	}

	public String imsiNo() {

		TelephonyManager tManager = (TelephonyManager) mactivity
				.getSystemService(Context.TELEPHONY_SERVICE);

		String imsi = tManager.getSubscriberId();

		return imsi;
	}

	public int osVersionNo() {

		int SDK_INT = android.os.Build.VERSION.SDK_INT;

		return SDK_INT;

	}

	public String dateNTime() {
		// date and time
		Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("a");

		String amOrPm = df.format(c.getTime());

		Calendar ci = Calendar.getInstance();

		String CiDateTime = "" + ci.get(Calendar.DAY_OF_MONTH) + "/"
				+ (ci.get(Calendar.MONTH) + 1) + "/" + ci.get(Calendar.YEAR)
				+ " " + "  TIME-" + ci.get(Calendar.HOUR) + ":"
				+ ci.get(Calendar.MINUTE) + " " + amOrPm;

		return CiDateTime;
	}

	public String getTimeZone() {
		SimpleDateFormat df = new SimpleDateFormat("a");
		String timeZone = df.getTimeZone().getDisplayName();
		return timeZone;
	}
	
	/*
	 * Returns Width & Height
	 */
	public String[] getResolution() {
		// Resolution
		DisplayMetrics dm = new DisplayMetrics();
		mactivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		String str_ScreenSize[] = new String[2];
		str_ScreenSize[0] = ""+dm.widthPixels;
		str_ScreenSize[1] = ""+dm.heightPixels;

		return str_ScreenSize;
	}

	public String availableConnectivity() {
		ConnectivityManager conMan = (ConnectivityManager) mactivity
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		String nwrType = "";

		// mobile
		State mobile = conMan.getNetworkInfo(0).getState();

		// wifi
		State wifi = conMan.getNetworkInfo(1).getState();

		if (mobile == NetworkInfo.State.CONNECTED
				|| mobile == NetworkInfo.State.CONNECTING) {
			nwrType = "GSM";

		} else if (wifi == NetworkInfo.State.CONNECTED
				|| wifi == NetworkInfo.State.CONNECTING) {
			nwrType = "Wi-Fi";
		}
		return nwrType;
	}

	public Boolean inAirplaneMode() {

		boolean isEnabled = Settings.System.getInt(
				mactivity.getContentResolver(),
				Settings.System.AIRPLANE_MODE_ON, 0) == 1;

		if (isEnabled) {
			return true;
		} else {
			return false;
		}

	}

	public Boolean chkGPS() {
		LocationManager locManager = (LocationManager) mactivity
				.getSystemService(Context.LOCATION_SERVICE);

		Boolean gps = locManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);

		return gps;
	}

	public String[] memCardDetails() {

		String[] memDetails = new String[2];

		StatFs stat = new StatFs(Environment.getExternalStorageDirectory()
				.getPath());
		double sdAvailSize = (double) stat.getAvailableBlocks()
				* (double) stat.getBlockSize();
		double sdtotalSize = (double) stat.getBlockCount()
				* (double) stat.getBlockSize();
		// One binary gigabyte equals 1,073,741,824 bytes.
		float gigaTotal = (float) (sdtotalSize / 1073741824);
		float gigaAvailable = (float) (sdAvailSize / 1073741824);

		memDetails[0] = String.valueOf(gigaTotal);
		memDetails[1] = String.valueOf(gigaAvailable);

		return memDetails;
	}


}
