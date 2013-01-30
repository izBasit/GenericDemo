package com.mobien.utils;

import java.text.DecimalFormat;
import java.util.Vector;

public class StringUtility {

	public static String[] split(String original, char splitchar) {
		Vector<String> nodes = new Vector<String>();

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
				split_string_array[loop] = nodes.elementAt(loop);
		}

		return split_string_array;
	}

	public static String[] split(String original, String splitStr) {
		Vector<String> nodes = new Vector<String>();

		int index = original.indexOf(splitStr); // Parse nodes into vector
		while (index >= 0) {
			nodes.addElement(original.substring(0, index));
			original = original.substring(index + 1);
			index = original.indexOf(splitStr);
		}
		// Get the last node
		nodes.addElement(original);

		String[] split_string_array = new String[nodes.size()];
		if (nodes.size() > 0) {
			for (int loop = 0; loop < nodes.size(); loop++)
				split_string_array[loop] = nodes.elementAt(loop);
		}

		return split_string_array;
	}

	public static Vector<String> splitInVec(String original, char splitchar) {
		Vector<String> nodes = new Vector<String>();

		int index = original.indexOf(splitchar); // Parse nodes into vector
		while (index >= 0) {
			nodes.addElement(original.substring(0, index));
			original = original.substring(index + 1);
			index = original.indexOf(splitchar);
		}
		// Get the last node
		nodes.addElement(original);

		return sort(nodes);
	}

	public static String padZero(String val, int padLength) {

		String temp = "";
		int inLen = val.length();
		for (int i = 0; i < padLength - inLen; i++) {
			temp = temp + "0";
		}
		return (temp + val);

	}

	public static Vector<String> sort(Vector<String> sort) {
		Vector<String> v = new Vector<String>();
		for (int count = 0; count < sort.size(); count++) {
			String s = sort.elementAt(count).toString();
			int i = 0;
			for (i = 0; i < v.size(); i++) {
				int c = s.compareTo(v.elementAt(i));
				if (c < 0) {
					v.insertElementAt(s, i);
					break;
				} else if (c == 0) {
					break;
				}
			}
			if (i >= v.size()) {
				v.addElement(s);
			}
		}
		return v;
	}

	public static char encodeBase64Char(int seqNo) {
		char c = ' ';
		if (seqNo <= 26) {
			int enchSeqNo = seqNo + 64;
			c = (char) enchSeqNo;
		} else {
			int tempNo = seqNo - 25;
			int enchSeqNo = tempNo + 96;
			c = (char) enchSeqNo;
		}
		return c;
	}

	public static int decodeBase64ToInt(char c) {
		int seqNo = c;
		int finalNo = 0;
		if (seqNo > 96) {
			int temp = seqNo - 96;
			finalNo = temp + 25;
		} else {
			finalNo = seqNo - 64;
		}
		return finalNo;
	}

	public static String generateStringFromVector(
			Vector<String> attCodeAndValue, String appendWhat) {

		String ICString = "";

		for (int i = 0; i < attCodeAndValue.size(); i++) {
			ICString += attCodeAndValue.elementAt(i) + appendWhat;
		}

		return ICString.substring(0, ICString.length() - 1);
	}

	public static String generateInsertQuery(String tableName,
			String[] fieldNameArray, String[] fieldValuesArray,
			String whereClause) {

		String query = "";

		try {

			if (fieldNameArray.length == fieldValuesArray.length) {

				query = "INSERT INTO " + tableName + " (";
				for (int i = 0; i < fieldNameArray.length; i++) {
					query += fieldNameArray[i] + ", ";
				}
				query = query.substring(0, query.length() - 2);
				query += " ) VALUES(";
				for (int i = 0; i < fieldValuesArray.length; i++) {
					query += "'" + fieldValuesArray[i] + "', ";
				}
				query = query.substring(0, query.length() - 2);
				query += " )";

			} else {
				System.out
						.println("Field names doesn't match field values in generateInsertQuery in StringUtility");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return query;

	}

	public static String generateUpdateQuery(String tableName,
			String[] fieldNameArray, String[] fieldValuesArray,
			String whereClause) {

		String query = "";

		try {

			if (fieldNameArray.length == fieldValuesArray.length) {

				query = "UPDATE " + tableName + " SET ";
				for (int i = 0; i < fieldNameArray.length; i++) {
					query += fieldNameArray[i] + " = '" + fieldValuesArray[i]
							+ "' ,";
				}
				query = query.substring(0, query.length() - 2);
				if (whereClause != null || whereClause != "") {
					query += "  WHERE " + whereClause;
				}
			} else {
				System.out
						.println("field names doesn't match field values in generateInsertQuery in StringUtility");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return query;
	}

	// Method to generate delete query for row
	public static String generateDeleteQueryForRow(String tableName,
			String whereClause) {
		String query = "DELETE FROM " + tableName + " WHERE " + whereClause;

		return query;
	}

	/*
	 * Convert the number to currency format using this method. Currency format
	 * = 1,000.00
	 */
	public static String formatNumberToCurrencyFormat(String whichNoToFormat) {

		double amount = Double.parseDouble(whichNoToFormat);
		DecimalFormat formatter = new DecimalFormat("#,###.00");
		String formattedString = formatter.format(amount);
		System.out.println(formattedString);
		return formattedString;
	}

}
