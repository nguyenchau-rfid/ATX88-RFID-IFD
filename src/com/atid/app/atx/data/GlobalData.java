package com.atid.app.atx.data;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Locale;

import com.atid.app.atx.device.ATEAReaderManager;
import com.atid.lib.types.DeviceType;
import com.atid.lib.util.diagnotics.ATLog;

import android.content.Context;
import android.content.SharedPreferences;

public class GlobalData {

	private static final String TAG = GlobalData.class.getSimpleName();
	private static final int INFO = ATLog.INFO;
	
	public static final String KEY_DISPLAY_PC = "display pc";
	public static final String KEY_RESTART_TIME = "restart time";
	public static final String KEY_BARCODE_CHARSET = "barcode charset";
	public static final String KEY_TAG_SPEED = "tag speed";
	
	private static final boolean DEFAULT_VALUE_DISPLAY_PC = true;
	private static final int DEFAULT_VALUE_RESTART_TIME = 0;
	private static final String DEFAULT_VALUE_BARCODE_CHARSET = Charset.defaultCharset().name();
	private static final boolean DEFAULT_VALUE_TAG_SPEED = false;
	
	// ------------------------------------------------------------------------
	// Global Data
	// ------------------------------------------------------------------------
	
	public static boolean isSupportBluetooth = false;
	public static boolean isSupportBluetoothLe = false;
	public static boolean isSupprotWifi = false;
	
	public static boolean isEnableBluetooth = false;
	public static boolean isEnableWifi = false;
	
	private static boolean isDisplayPC = DEFAULT_VALUE_DISPLAY_PC;
	private static int RestartTime = DEFAULT_VALUE_RESTART_TIME;
	private static Charset BarcodeCharset = Charset.forName(DEFAULT_VALUE_BARCODE_CHARSET);
	private static boolean isTagSpeed = DEFAULT_VALUE_TAG_SPEED;
	
	public static DataManager DataManager = null;
	public static ATEAReaderManager ReaderManager = null;
	
	private static HashMap<String, Object> mMap = new HashMap<String, Object>();
	
	// ------------------------------------------------------------------------
	// Load/Save Configuration
	// ------------------------------------------------------------------------

	private static Object getData(String key){
		Object item = null;
				
		if(key == null) {
			ATLog.e(TAG, "ERROR. getData() - Key is null !!!");
			return item;
		}
		synchronized(mMap){
			if(mMap.get(key) != null)
				item = mMap.get(key);
			else {
				ATLog.e(TAG, "ERROR. getData(%s) - Not Found data !!!", key);
			}
		}
		return item;
	}

	private static void removeData(String key){
				
		synchronized(mMap){
			if( mMap.get(key) != null ) {
				mMap.remove(key);
			} else {
				ATLog.e(TAG, "ERROR. removeData(%s) - Not Found data !!!", key);
			}

		}
	}

	private static void putData(String key, Object value){

		synchronized(mMap){
			
			if( key != null && value != null)
				mMap.put(key, value);
			else
				ATLog.e(TAG, "ERROR. putData() - Key or Value is null !!!");
		}
	}

	public static synchronized Object getConfig(Context context, DeviceType device, String macAddr, String key) throws Exception{
		
		String pakageName = context.getPackageName();
		SharedPreferences prefs = context.getSharedPreferences(pakageName, Context.MODE_PRIVATE);
		
		String name = String.format(Locale.US, "%d%s%s", device.getCode(), macAddr.toString(), key.toString());
		Object data = getData(name);
		if(data == null){
			if(key.compareToIgnoreCase(KEY_DISPLAY_PC) == 0){
				isDisplayPC = prefs.getBoolean(name, DEFAULT_VALUE_DISPLAY_PC);
				putData(key, isDisplayPC);
			} else if(key.compareToIgnoreCase(KEY_RESTART_TIME) == 0) {
				RestartTime = prefs.getInt(name, DEFAULT_VALUE_RESTART_TIME);
				putData(key, RestartTime);
			} else if(key.compareToIgnoreCase(KEY_BARCODE_CHARSET) == 0) {
				BarcodeCharset = Charset.forName(DEFAULT_VALUE_BARCODE_CHARSET);
				putData(name, BarcodeCharset);
			} else if(key.compareToIgnoreCase(KEY_TAG_SPEED) == 0) {
				isTagSpeed = prefs.getBoolean(key, DEFAULT_VALUE_TAG_SPEED);
				putData(key, isTagSpeed);
			} else {
				ATLog.e(TAG, "ERROR. getConfig(%s, %s, %s) - Unknown Key",
						device.toString(), macAddr, key);
			}
		}
		return data;
	}

	public static synchronized void putConfig(DeviceType device, String macAddr, String key , Object value){
		if(value == null) {
			ATLog.e(TAG, "ERROR. putConfig() - Value is null !!!");
			return;
		}
		String name = String.format(Locale.US, "%d%s%s", device.getCode(), macAddr, key);
		putData(name, value);
	}

	// Load Config
	public static synchronized void loadConfig(Context context, DeviceType device , String macAddr) {
		String key = null;
		String pakageName = context.getPackageName();
		SharedPreferences prefs = context.getSharedPreferences(pakageName, Context.MODE_PRIVATE);
		
		if(macAddr == null) {
			ATLog.e(TAG, "ERROR. MAC address is null !!!");
			return;
		}
		
		key = String.format(Locale.US, "%d%s%s", device.getCode(), macAddr, KEY_DISPLAY_PC);
		isDisplayPC = prefs.getBoolean(key, DEFAULT_VALUE_DISPLAY_PC);
		putData(key, isDisplayPC);
		ATLog.i(TAG, INFO, "INFO. loadConfig() - DeviceType[%s] Address [%s] Display PC : %s", 
				device.toString(), macAddr, isDisplayPC);
		
		key = String.format(Locale.US, "%d%s%s", device.getCode(), macAddr, KEY_RESTART_TIME);
		RestartTime = prefs.getInt(key, DEFAULT_VALUE_RESTART_TIME);
		putData(key, RestartTime);
		ATLog.i(TAG, INFO, "INFO. loadConfig() - DeviceType[%s] Address [%s] Restart TIme : %d", 
				device.toString(), macAddr, RestartTime);
		
		key = String.format(Locale.US, "%d%s%s", device.getCode(), macAddr, KEY_BARCODE_CHARSET);
		BarcodeCharset = Charset.forName(DEFAULT_VALUE_BARCODE_CHARSET);
		putData(key, BarcodeCharset);
		ATLog.i(TAG, INFO, "INFO. loadConfig() - DeviceType[%s] Address [%s] Barcode Character Set : %s", 
				device.toString(), macAddr, BarcodeCharset.name());
		
		key = String.format(Locale.US, "%d%s%s", device.getCode(), macAddr, KEY_TAG_SPEED);
		isTagSpeed = prefs.getBoolean(key, DEFAULT_VALUE_TAG_SPEED);
		putData(key, isTagSpeed);
		ATLog.i(TAG, INFO, "INFO. loadConfig() - DeviceType[%s] Address [%s] Tag Speed : %s", 
				device.toString(), macAddr, isTagSpeed);
		
		ATLog.i(TAG, INFO, "INFO. loadConfig()");
	}

	// Remove Config
	public static synchronized boolean removeConfig(Context context, DeviceType device , String macAddr) {
		boolean result = true;
		
		String key = null;
		String pakageName = context.getPackageName();
		SharedPreferences prefs = context.getSharedPreferences(pakageName, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		
		key = String.format(Locale.US, "%d%s%s", device.getCode(), macAddr, KEY_DISPLAY_PC);
		removeData(key);
		editor.remove(key);
		ATLog.i(TAG, INFO, "INFO. removeConfig() - DeviceType[%s] Address [%s]  %s", 
				device.toString(), macAddr, KEY_DISPLAY_PC);

		key = String.format(Locale.US, "%d%s%s", device.getCode(), macAddr, KEY_RESTART_TIME);
		removeData(key);
		editor.remove(key);
		ATLog.i(TAG, INFO, "INFO. removeConfig() - DeviceType[%s] Address [%s] %s", 
				device.toString(), macAddr, KEY_RESTART_TIME);
		
		key = String.format(Locale.US, "%d%s%s", device.getCode(), macAddr, KEY_BARCODE_CHARSET);
		removeData(key);
		editor.remove(key);
		ATLog.i(TAG, INFO, "INFO. removeConfig() - DeviceType[%s] Address [%s] %s", 
				device.toString(), macAddr, KEY_BARCODE_CHARSET);
		
		key = String.format(Locale.US, "%d%s%s", device.getCode(), macAddr, KEY_TAG_SPEED);
		removeData(key);
		editor.remove(key);
		ATLog.i(TAG, INFO, "INFO. removeConfig() - DeviceType[%s] Address [%s] %s", 
				device.toString(), macAddr, KEY_TAG_SPEED);
		
		result = editor.commit();
		ATLog.i(TAG, INFO, "INFO. removeConfig() - [%s]", result);
		return result;
	}

	
	// Save Config
	public static synchronized boolean saveConfig(Context context, DeviceType device , String macAddr) {
		boolean result = true;
		Object item = null;
		
		String key = null;
		String pakageName = context.getPackageName();
		SharedPreferences prefs = context.getSharedPreferences(pakageName, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		
		key = String.format(Locale.US, "%d%s%s", device.getCode(), macAddr, KEY_DISPLAY_PC);
		item = getData(key);
		if(item != null){
			isDisplayPC = (Boolean) item;
		} else {
			isDisplayPC = DEFAULT_VALUE_DISPLAY_PC;
		}
		editor.putBoolean(key, isDisplayPC);
		ATLog.i(TAG, INFO, "INFO. saveConfig() - DeviceType[%s] Address [%s] Display PC : %s", 
				device.toString(), macAddr, isDisplayPC);
		
		key = String.format(Locale.US, "%d%s%s", device.getCode(), macAddr, KEY_RESTART_TIME);
		item = getData(key);
		if(item != null) {
			RestartTime = (Integer)getData(key);	
		} else {
			RestartTime = DEFAULT_VALUE_RESTART_TIME;
		}
		editor.putInt(key, RestartTime);
		ATLog.i(TAG, INFO, "INFO. saveConfig() - DeviceType[%s] Address [%s] Restart TIme : %d", 
				device.toString(), macAddr, RestartTime);
		
		key = String.format(Locale.US, "%d%s%s", device.getCode(), macAddr, KEY_BARCODE_CHARSET);
		item = getData(key);
		if(item != null){
			BarcodeCharset = (Charset)getData(key);	
		} else {
			BarcodeCharset = Charset.forName(DEFAULT_VALUE_BARCODE_CHARSET);
		}
		editor.putString(key, BarcodeCharset.name());
		ATLog.i(TAG, INFO, "INFO. saveConfig() - DeviceType[%s] Address [%s] Barcode Character Set : %s", 
				device.toString(), macAddr, BarcodeCharset.name());
		
		key = String.format(Locale.US, "%d%s%s", device.getCode(), macAddr, KEY_TAG_SPEED);
		item = getData(key);
		if(item != null){
			isTagSpeed = (Boolean)getData(key);	
		} else {
			isTagSpeed = DEFAULT_VALUE_TAG_SPEED;
		}
		editor.putBoolean(key, isTagSpeed);
		ATLog.i(TAG, INFO, "INFO. saveConfig() - DeviceType[%s] Address [%s] Tag Speed : %s", 
				device.toString(), macAddr, isTagSpeed);
		
		result = editor.commit();
		ATLog.i(TAG, INFO, "INFO. saveConfig() - [%s]", result);
		return result;
	}
}
