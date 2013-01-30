package com.mobien.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtility {

	public Context mContext;

	public NetworkUtility(Context context) {
		// TODO Auto-generated constructor stub
		mContext = context;
	}

	public boolean checkNetConnectivity() {
		boolean b = false;
		ConnectivityManager connec = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED
				|| connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED) {
			b = true;
		}

		return b;
	}

	public boolean isOnline() {
		
		if (isInternetOn()) {
			return true;
		}else{
			return false;
		}
	}

	public final boolean isInternetOn() {
		ConnectivityManager connec = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		// ARE WE CONNECTED TO THE NET
		if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED
				|| connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTING
				|| connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING
				|| connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED) {
			// MESSAGE TO SCREEN FOR TESTING (IF REQ)
			// Toast.makeText(this, connectionType + ” connected”,
			// Toast.LENGTH_SHORT).show();
			return true;
		} else if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED
				|| connec.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED) {
			// System.out.println(“Not Connected”);
			return false;
		}
		return false;
	}

}
