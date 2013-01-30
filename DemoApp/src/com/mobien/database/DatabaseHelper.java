package com.mobien.database;

import android.content.Context;
import android.database.sqlite.*;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

import java.io.File;
import java.util.Vector;

import com.mobien.demoapp.LoginActivity;
import com.mobien.demoapp.R;
import com.mobien.utils.CommonFunctions;


public class DatabaseHelper extends SQLiteOpenHelper {

	
	public static final int DatabaseVersion = 1;
	String sql = "";
	public static SQLiteDatabase sqLiteDb = null;
	static Context appContext = null;
	public static Cursor cursor = null;
	public static String DB_OPEN_MODE = "C"; // C For Close, W for Write, R For
												// Read

	public static String TAG = "dbhelper";
	
	public DatabaseHelper(Context context) {
		super(context, context.getString(R.string.database_name), null, DatabaseVersion);
		appContext = context;

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			sqLiteDb = this.getWritableDatabase();
			sqLiteDb = db;
		} catch (Exception ex) {
			ex.printStackTrace();

		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		String query = "ALTER TABLE names ADD COLUMN hidden integer default 0";
		db.rawQuery(query, null);

	}

	public SQLiteDatabase openDatabaseInReadableMode() throws SQLException {
		if (DB_OPEN_MODE.equals("C")) {
			sqLiteDb = this.getReadableDatabase();
		} else if (DB_OPEN_MODE.equals("W")) {
			closeDB();
			sqLiteDb = this.getReadableDatabase();
		}
		DB_OPEN_MODE = "R";
		return sqLiteDb;
	}

	public SQLiteDatabase openDatabaseInWritableMode() throws SQLException {
		if (DB_OPEN_MODE.equals("C")) {
			sqLiteDb = this.getWritableDatabase();
		} else if (DB_OPEN_MODE.equals("R")) {
			closeDB();
			sqLiteDb = this.getWritableDatabase();
		}
		DB_OPEN_MODE = "W";
		return sqLiteDb;
	}

	public void closeDB() throws SQLException {
		if (cursor != null) {
			cursor.close();
		}
		if (sqLiteDb != null) {
			sqLiteDb.close();
		}
		DB_OPEN_MODE = "C";
	}

	public boolean batchCreate(Context _mContext, String[] _recordList) {

		sqLiteDb = openDatabaseInReadableMode();

		for (int i = 0; i < _recordList.length; i++) {
			System.out.println("_recordList[i] ====> " + _recordList[i]);
			// db.ExecuteSql(_recordList[i]+";");
			sqLiteDb.execSQL(_recordList[i] + ";");

		}
		/*
		 * for (int p = 0; p < Config.PERSONAL_DETAILS.size(); p++) { String
		 * profileCode = Config.PERSONAL_DETAILS.get(p); String updateQuery =
		 * "UPDATE CFGENTITYPROFILEDTL SET ATTRIBUTECREATESEQ = '0' " +
		 * "WHERE PROFILECODE = '"+profileCode+"' AND ATTRIBUTETYPE = 'H';"; int
		 * i = executeUpdateQueryForResult(updateQuery);
		 * 
		 * System.out.println(
		 * "GeoLocation made visible for device in profile code: ---"
		 * +profileCode ); }
		 */

		closeDB();
		return true;
	}

	public boolean dbexists() {
		
		File dbFile = new File(LoginActivity.DATABASE_PATH
				+ LoginActivity.DATABASE_NAME);
		// System.out.print("DataBase Exist " + dbFile.exists());
		return dbFile.exists();

	}
	
	public boolean hasElements(String tableName) {
		
		Boolean status = false;
		sqLiteDb = openDatabaseInReadableMode();
		Cursor cursor = null;
		
		try {
			
			String query = "SELECT * FROM "+tableName ;
			cursor = sqLiteDb.rawQuery(query, null);
			
			int count = cursor.getCount();
			if(count > 0)
				status = true;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			closeDB();
		}
		
		return status;
	}

	// for getting the device setting contents
	public String getUserData_atPosition(int position) {
		// TODO Auto-generated method stub
		sqLiteDb = openDatabaseInReadableMode();
		Cursor curOrder = null;
		String values = "";

		try {

			// 0 - LOGINID
			// 1 - DEVICEPASSWORD
			// 2 - COMMUNICATIONMODE
			// 3 - PUSHDATALOCALURL
			// 4 - PULLDATALOCALURL
			// 5 - ROLE_ID
			// 6 - EMPLOYEE_ID
			// 7 - APPDOWNLOADURL
			// 8 - SERVERCELLNO
			// 9 - APPLICATIONVERSION
			// 10 - IMEINO
			// 11 - TPIN
			// 12 - DEVICEMAKE
			// 13 - DEVICEMODEL
			// 14 - SENTBOXID
			// 15 - ORGID
			// 16 - CELLNO
			// 17 - CLEANUPFACTOR
			// 18 - IS_ACTIV
			// 19 - INT_FLAG
			// 20 - SYNC_DATE

/*			String query = " select LOGIN_ID, PASSWORD, COMMUNICATION_MODE,"
					+ " PUSH_DATA_URL,PULL_DATA_URL,ROLE_ID,EMPLOYEE_ID,APP_DOWNLOAD_URL, SERVER_NUMBER,"
					+ "  APP_VERSION,DEVICE_ID,TPIN,DEVICE_MAKE, DEVICE_MODEL,SENT_BOX_ID, ORG_ID, "
					+ "   CELL_NO, CLEANUP_FACTOR,IS_ACTIVE,INT_FLAG,SYNC_DATE from TM_DEVICE_SETTINGS WHERE USER_LEVEL = '1' ";*/
			
//			String query = "select USERNAME, PASSWORD from TM_DEVICE_SETTINGS WHERE USER_LEVEL = '1'";
			String query = "select USERNAME, PASSWORD from TM_DEVICE_SETTINGS";
			curOrder = sqLiteDb.rawQuery(query, null);

			if (!curOrder.isFirst()) {
				curOrder.moveToFirst();
			}
			int count = curOrder.getCount();

			if (count != 0) {
				values = curOrder.getString(position);// record at position
				System.out.println("Values are:" + values + "::");
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			closeDB();

		}
		return values;
	}

	public String getPrefix(String shortCode, Context mContext) {
		String prefix = "";
		if (cursor != null) {
			cursor.close();
		}
		if (sqLiteDb != null) {
			sqLiteDb.close();
		}
		sqLiteDb = this.getReadableDatabase();
		// sqLiteDb = openDatabaseInReadableMode();
		try {
			String query = "SELECT PREFIX FROM TBL_SHORT_CODES  WHERE SHORT_CODE =  '"
					+ shortCode + "'";
			// String query =
			// "select name from sqlite_master where name = 'SHORTCODESMASTER'";
			Cursor c = sqLiteDb.rawQuery(query, null);
			if (c.getCount() > 0) {
				c.moveToFirst();
				do {
					prefix = c.getString(0);
				} while (c.moveToNext());
			}
			c.close();
		} catch (Exception ex) {

			ex.printStackTrace();
		} finally {
			closeDB();
		}
		return prefix;
	}

	public String getSuffix(String shortCode, Context mContext) {
		String prefix = "";
		if (cursor != null) {
			cursor.close();
		}
		if (sqLiteDb != null) {
			sqLiteDb.close();
		}
		sqLiteDb = this.getReadableDatabase();
		sqLiteDb = openDatabaseInReadableMode();
		try {

			String query = "SELECT SUFFIX FROM TBL_SHORT_CODES WHERE SHORT_CODE =  '"
					+ shortCode + "'";
			Cursor c = sqLiteDb.rawQuery(query, null);
			if (c.getCount() > 0) {
				c.moveToFirst();
				do {
					prefix = c.getString(0);
				} while (c.moveToNext());
			}
			c.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			closeDB();
		}
		return prefix;
	}

	public synchronized boolean ExecuteSql(String s) {// Update Query
		try {
			sqLiteDb = openDatabaseInReadableMode();
			sqLiteDb.execSQL(s);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		} finally {
			closeDB();
		}

	}

	public void executeXmlQuery(String[] createQueryXml, Context mContext) {
		// TODO Auto-generated method stub
		try {
			sqLiteDb = openDatabaseInReadableMode();

			if (createQueryXml != null) {
				for (int recIndex = 0; recIndex < createQueryXml.length; recIndex++) {
					String[] _dataValues = CommonFunctions.split(
							createQueryXml[recIndex], '#');
					for (int i = 0; i < _dataValues.length; i++) {
						// System.out.println("::::#" +
						// _dataValues[i].toString());
						ExecuteSql(_dataValues[i].toString());

						// System.out.println(_dataValues[i].toString());
					}

				}

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			closeDB();
		}
	}

	public void executeXmlQueryUsingShortCodes(String[] queryXml,
			Context mContext) {
		try {
			sqLiteDb = openDatabaseInReadableMode();
			if (queryXml != null) {
				for (int recIndex = 0; recIndex < queryXml.length; recIndex++) {
					String[] _dataValues = CommonFunctions.split(
							queryXml[recIndex], '#');
					String query = getPrefix(_dataValues[0], mContext)
							+ _dataValues[1]
							+ getSuffix(_dataValues[0], mContext);
					 System.out.println("INSERT QUERY : "+query);
					ExecuteSql(query);
					_dataValues = null;
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			closeDB();
		}
	}

	public void executeXmlQueryUsingShortCodesNew(String[] queryXml,
			Context mContext) {
		try {
			sqLiteDb = openDatabaseInReadableMode();
			if (queryXml != null) {
				for (int recIndex = 0; recIndex < queryXml.length; recIndex++) {
					String[] _dataValues = CommonFunctions.split(
							queryXml[recIndex], '^');
					String query = getPrefix(_dataValues[0], mContext)
							+ _dataValues[1]
							+ getSuffix(_dataValues[0], mContext);
					// System.out.println("grrrrrrrrrr"+query);
					ExecuteSql(query);
					_dataValues = null;
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			closeDB();
		}
	}
		
}
	
