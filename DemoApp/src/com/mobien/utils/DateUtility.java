package com.mobien.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtility {
	public static char DATE_SEPRATOR = '/';
	public static char TIME_SEPRATOR = ':';
	public static String DATE_FORMAT = "MM/dd/yyyy";
	public static String TIME_FORMAT = "HH:mm";

	public static String getDate(String format) {

		final Calendar c1 = Calendar.getInstance();
		int month = c1.get(Calendar.MONTH) + 1;
		int day = c1.get(Calendar.DAY_OF_MONTH);

		String paddedMonth = StringUtility.padZero(month + "", 2);

		String paddedDay = StringUtility.padZero(day + "", 2);

		String date = "";// +padded_day+padded_month+c1.get(Calendar.YEAR);
		if (format.equalsIgnoreCase("dd" + DATE_SEPRATOR + "mm" + DATE_SEPRATOR
				+ "yyyy")) {
			date = paddedDay + DATE_SEPRATOR + paddedMonth + DATE_SEPRATOR
					+ c1.get(Calendar.YEAR);
		} else if (format.equalsIgnoreCase("mm" + DATE_SEPRATOR + "dd"
				+ DATE_SEPRATOR + "yyyy")) {
			date = paddedMonth + DATE_SEPRATOR + paddedDay + DATE_SEPRATOR
					+ c1.get(Calendar.YEAR);
		} else if (format.equalsIgnoreCase("yyyy" + DATE_SEPRATOR + "mm"
				+ DATE_SEPRATOR + "dd")) {
			date = c1.get(Calendar.YEAR) + DATE_SEPRATOR + paddedMonth
					+ DATE_SEPRATOR + paddedDay;
		} else if (format.equalsIgnoreCase("ddmmyyyy")) {
			date = paddedDay + paddedMonth + c1.get(Calendar.YEAR);
		}
		return date;
	}

	public static int getTimeDifference(String time1, String time2) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
		Date date1 = null;
		Date date2 = null;
		try {
			date1 = format.parse(time1);
			date2 = format.parse(time2);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		int diff = 0;
		long hourdifference = date1.getHours() - date2.getHours();
		if (hourdifference == 0) {
			long difference = date1.getTime() - date2.getTime();
			long time = difference / 1000;
			int minutes = (int) ((time % 3600) / 60);
			diff = minutes;
		} else {
			long difference = date1.getTime() - date2.getTime();
			long time = difference / 1000;
			int hour = (int) ((time % 60));
			int minutes = (int) ((time % 3600) / 60);
			diff = (int) (hourdifference * 60) + minutes;
		}
		return diff;
	}

	public static String getDateTime(String format) {
		final Calendar c1 = Calendar.getInstance();
		int month = c1.get(Calendar.MONTH) + 1;
		int day = c1.get(Calendar.DAY_OF_MONTH);
		String paddedMonth;
		String paddedDay;
		if (month < 10) {
			paddedMonth = "0" + month;
		} else {
			paddedMonth = "" + month;
		}
		if (day < 10) {
			paddedDay = "0" + day;
		} else {
			paddedDay = "" + day;
		}
		String date = "";// +padded_day+padded_month+c1.get(Calendar.YEAR);
		if (format.equalsIgnoreCase("dd" + DATE_SEPRATOR + "mm" + DATE_SEPRATOR
				+ "yyyy")) {
			date = paddedDay + DATE_SEPRATOR + paddedMonth + DATE_SEPRATOR
					+ c1.get(Calendar.YEAR);
		} else if (format.equalsIgnoreCase("mm" + DATE_SEPRATOR + "dd"
				+ DATE_SEPRATOR + "yyyy")) {
			date = paddedMonth + DATE_SEPRATOR + paddedDay + DATE_SEPRATOR
					+ c1.get(Calendar.YEAR);
		} else if (format.equalsIgnoreCase("yyyy" + DATE_SEPRATOR + "mm"
				+ DATE_SEPRATOR + "dd")) {
			date = c1.get(Calendar.YEAR) + DATE_SEPRATOR + paddedMonth
					+ DATE_SEPRATOR + paddedDay;
		} else if (format.equalsIgnoreCase("ddmmyyy")) {
			date = paddedDay + paddedMonth + c1.get(Calendar.YEAR);
		}
		return date;
	}

	public static String getTimeIn12Hour(String time) {
		String[] parsed_time = StringUtility.split(time, ':');
		int min = Integer.parseInt(parsed_time[1]);
		if (min < 10) {
			parsed_time[1] = "0" + min;
		} else {
			parsed_time[1] = "" + min;
		}
		int hour = Integer.parseInt(parsed_time[0]);
		String time_new = "";
		if (hour > 12) {
			hour = hour - 12;
			if (hour < 10) {
				parsed_time[0] = "0" + hour;
			} else {
				parsed_time[0] = "" + hour;
			}
			time_new = "" + parsed_time[0] + ":" + parsed_time[1] + "PM";
		} else if (hour == 12) {
			time_new = "" + hour + ":" + parsed_time[1] + "PM";
		} else {
			if (hour < 10) {
				parsed_time[0] = "0" + hour;
			} else {
				parsed_time[0] = "" + hour;
			}
			time_new = "" + parsed_time[0] + ":" + parsed_time[1] + "AM";
		}
		return time_new;
	}

	public static String getCurrentTime() {
		String time = "";
		int curr_hour, curr_min, curr_sec;
		final Calendar c2 = Calendar.getInstance();
		curr_hour = c2.get(Calendar.HOUR_OF_DAY);
		curr_min = c2.get(Calendar.MINUTE);
		curr_sec = c2.get(Calendar.SECOND);
		time = "" + curr_hour + TIME_SEPRATOR + curr_min + TIME_SEPRATOR
				+ curr_sec;
		return time;
	}

	public static String getCurrentTimeInMilliSecond() {
		String time = "";
		int currHour, currMin, currSec, currMiliSec;
		final Calendar c2 = Calendar.getInstance();
		currHour = c2.get(Calendar.HOUR_OF_DAY);
		currMin = c2.get(Calendar.MINUTE);
		currSec = c2.get(Calendar.SECOND);
		currMiliSec = c2.get(Calendar.MILLISECOND);
		time = "" + StringUtility.padZero("" + currHour, 2)
				+ StringUtility.padZero("" + currMin, 2)
				+ StringUtility.padZero("" + currSec, 2)
				+ StringUtility.padZero("" + currMiliSec, 4);
		return time;
	}

	public static long daysBetween(Calendar startDate, Calendar endDate) {
		Calendar date = (Calendar) startDate.clone();
		long daysBetween = 0;
		if (date.before(endDate)) {
			while (date.before(endDate)) {
				date.add(Calendar.DAY_OF_MONTH, 1);
				daysBetween++;
			}
		} else if (date.after(endDate)) {
			while (date.after(endDate)) {
				date.add(Calendar.DAY_OF_MONTH, -1);
				daysBetween++;
			}
		}
		return daysBetween;
	}

	public boolean isDateMin(String date, int minDate, int maxDate) {
		SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date d1 = null;
		try {
			d1 = outputFormat.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(d1);

		int diff = (int) daysBetween(cal1, cal2);
		if (cal2.after(cal1)) {
			if (diff > maxDate) {
				return false;
			} else {
				return true;
			}
		} else if (cal2.before(cal1)) {
			if (diff > minDate + 1) {
				return false;
			} else {
				return true;
			}
		} else {
			return true;
		}
	}

	// written by Amey
	public static String get_Month(String month) {
		String mon = "";
		if (month.equalsIgnoreCase("jan")) {
			mon = "01";
		} else if (month.equalsIgnoreCase("feb")) {
			mon = "02";
		} else if (month.equalsIgnoreCase("mar")) {
			mon = "03";
		} else if (month.equalsIgnoreCase("apr")) {
			mon = "04";
		} else if (month.equalsIgnoreCase("may")) {
			mon = "05";
		} else if (month.equalsIgnoreCase("jun")) {
			mon = "06";
		} else if (month.equalsIgnoreCase("jul")) {
			mon = "07";
		} else if (month.equalsIgnoreCase("aug")) {
			mon = "08";
		} else if (month.equalsIgnoreCase("sep")) {
			mon = "09";
		} else if (month.equalsIgnoreCase("oct")) {
			mon = "10";
		} else if (month.equalsIgnoreCase("nov")) {
			mon = "11";
		} else if (month.equalsIgnoreCase("dec")) {
			mon = "12";
		}

		return mon;
	}

	// Function added by Shreya for converting time from string type (hh:mm:ss)
	// to time type (HH:mm)

	public static Date convertStringToTime(String strTime) {
		System.out.println("Time to be converted:" + strTime + ".");
		Date dateTobeReturned = null;

		DateFormat sdf = new SimpleDateFormat(DateUtility.TIME_FORMAT);

		try {
			dateTobeReturned = sdf.parse(strTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		System.out.println("Time: " + sdf.format(dateTobeReturned));

		return dateTobeReturned;
	}

	public static String getRandomNoFromDate() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat formatter = new SimpleDateFormat("yyMMddHHmmss");
		String s = formatter.format(date);
		return s;
	}

	public static String getddMMyyyy(String date, String currentFormat,
			String inWhatFormat) {

		SimpleDateFormat fromUser = new SimpleDateFormat(currentFormat);
		SimpleDateFormat myFormat = new SimpleDateFormat(inWhatFormat);
		String reformattedStr = "";
		try {
			reformattedStr = myFormat.format(fromUser.parse(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return reformattedStr;
	}
}
