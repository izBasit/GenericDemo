package com.mobien.utils;

import java.util.Vector;

import android.content.Context;

public class CommonFunctions {
	
	public static Context mContext;
	
	public CommonFunctions(Context context) {
		CommonFunctions.mContext = context;
	}

	public static String[] split(String original, char splitchar) {
		Vector nodes = new Vector();

		int index = original.indexOf(splitchar); // Parse nodes into vector
		
		while (index >= 0) {
			nodes.addElement(original.substring(0, index));
			original = original.substring(index + 1);
			index = original.indexOf(splitchar);
		}
		// Get the last node
		nodes.addElement(original);

		String[] split_string_array = new String[nodes.size()];
		if (nodes.size() > 0) {
			for (int loop = 0; loop < nodes.size(); loop++)
				split_string_array[loop] = (String) nodes.elementAt(loop);
		}

		return split_string_array;
	}
	
}
