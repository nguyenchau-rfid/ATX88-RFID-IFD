package com.atid.app.atx.data;

import java.util.ArrayList;

import com.atid.lib.transport.types.ConnectType;
import com.atid.lib.types.DeviceType;
import com.atid.lib.util.diagnotics.ATLog;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class DataManager {

	private static final String TAG = DataManager.class.getSimpleName();
	private static final int INFO = ATLog.L2;

	private static final String DB_NAME = "ATX88Demo.db";
	private static final int DB_VERSION = 1;

	// ------------------------------------------------------------------------
	// Member Variable
	// ------------------------------------------------------------------------

	private DataOpenHelper mHelper;
	private SQLiteDatabase mDb;

	// ------------------------------------------------------------------------
	// Constructor
	// ------------------------------------------------------------------------

	public DataManager(Context context) {

		mHelper = new DataOpenHelper(context, DB_NAME, null, DB_VERSION);
		try {
			mDb = mHelper.getWritableDatabase();
		} catch (SQLiteException e) {
			ATLog.e(TAG, e, "ERROR. $DataManager() - Failed to create database");
			throw e;
		}
	}

	// Get Device Item List
	public ArrayList<DeviceItem> getDeviceItemList() {

		String query = "SELECT NAME, MAC, ADDRESS, DEVTP, CONNTP FROM ATDEVICE;";
		ArrayList<DeviceItem> list = new ArrayList<DeviceItem>();
		String name = "";
		String mac = "";
		String address = "";
		DeviceType devType = DeviceType.Unknown;
		ConnectType connType = ConnectType.Unknown;
		DeviceItem item = null;

		try {
			Cursor cursor = mDb.rawQuery(query, null);
			while (cursor.moveToNext()) {
				name = cursor.getString(cursor.getColumnIndex("NAME"));
				mac = cursor.getString(cursor.getColumnIndex("MAC"));
				address = cursor.getString(cursor.getColumnIndex("ADDRESS"));
				devType = DeviceType.valueOf(cursor.getInt(cursor.getColumnIndex("DEVTP")));
				connType = ConnectType.valueOf(cursor.getInt(cursor.getColumnIndex("CONNTP")));
				item = new DeviceItem(devType, connType, name, mac, address);
				list.add(item);
			}
		} catch (SQLiteException e) {
			ATLog.e(TAG, e, "ERROR. getDeviceItemList() - Failed to get device item list");
			list.clear();
			return list;
		}
		ATLog.i(TAG, INFO, "INFO. getDeviceItemList() - [%d]", list.size());
		return list;
	}

	// Insert Device Item
	public boolean insertDevice(DeviceItem item) {

		ContentValues values = new ContentValues();

		values.put("NAME", item.getName());
		values.put("MAC", item.getMac());
		values.put("ADDRESS", item.getAddress());
		values.put("DEVTP", item.getType().getCode());
		values.put("CONNTP", item.getConnectType().getCode());

		if (mDb.insert("ATDEVICE", null, values) <= 0) {
			ATLog.e(TAG, "ERROR. insertDevice([%s]) - Failed to insert device item", item.toString());
			return false;
		}
		ATLog.i(TAG, INFO, "INFO. insertDevice([%s])", item.toString());
		return true;
	}

	// Update Device Item
	public boolean updateDevice(DeviceItem item) {

		ContentValues values = new ContentValues();
		values.put("ADDRESS", item.getAddress());
		values.put("CONNTP", item.getConnectType().getCode());
		if (mDb.update("ATDEVICE", values, "NAME = ? AND MAC = ?",
				new String[] { item.getName(), item.getMac() }) <= 0) {
			ATLog.e(TAG, "ERROR. updateDevice([%s]) - Failed to update device item", item.toString());
			return false;
		}
		ATLog.i(TAG, INFO, "INFO. updateDevice([%s])", item.toString());
		return true;
	}

	// Delete Device Item
	public boolean deleteDevice(String name, String mac) {
		if (mDb.delete("ATDEVICE", "NAME = ? AND MAC = ?", new String[] { name, mac }) <= 0) {
			ATLog.e(TAG, "ERROR. deleteDevice([%s], [%s]) - Failed to delete device item", name, mac);
			return false;
		}
		ATLog.i(TAG, INFO, "INFO. deleteDevice([%s], [%s])", name, mac);
		return true;
	}

	// ------------------------------------------------------------------------
	// Class SQLiteOpenHelper
	// ------------------------------------------------------------------------

	private class DataOpenHelper extends SQLiteOpenHelper {

		public DataOpenHelper(Context context, String name, CursorFactory factory, int version) {
			super(context, name, factory, version);

		}

		@Override
		public void onCreate(SQLiteDatabase db) {

			createTable(db);

			ATLog.i(TAG, INFO, "INFO. onCreate()");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

			dropTable(db);
			createTable(db);

			ATLog.i(TAG, INFO, "INFO. onUpgrade()");
		}

		private void createTable(SQLiteDatabase db) {

			String query = "CREATE TABLE 'ATDEVICE' ('NAME' TEXT, 'MAC' TEXT, "
					+ "'ADDRESS' TEXT, 'DEVTP' INTEGER, 'CONNTP' INTEGER, PRIMARY KEY('NAME', 'MAC'))";
			try {
				db.execSQL(query);
			} catch (SQLiteException e) {
				ATLog.e(TAG, e, "ERROR. createTable() - Failed to create 'ATDEVICE' table");
				return;
			}
			ATLog.i(TAG, INFO, "INFO. createTable()");
		}

		private void dropTable(SQLiteDatabase db) {

			String query = "DROP TABLE IF EXISTS 'ATDEVICE'";
			try {
				db.execSQL(query);
			} catch (SQLiteException e) {
				ATLog.e(TAG, e, "ERROR. dropTable() - Failed to drop 'ATDEVICE' table");
				return;
			}
			ATLog.i(TAG, INFO, "INFO. dropTable()");
		}
	}
}
