package com.atid.app.atx.activity;

import java.nio.charset.Charset;
import java.util.Locale;

import com.atid.app.atx.R;
import com.atid.app.atx.adapter.DeviceListAdapter;
import com.atid.app.atx.adapter.DeviceListAdapter.OnListItemClickListener;
import com.atid.app.atx.data.Constants;
import com.atid.app.atx.data.DataManager;
import com.atid.app.atx.data.DeviceItem;
import com.atid.app.atx.data.GlobalData;
import com.atid.app.atx.device.ATEAReaderManager;
import com.atid.app.atx.dialog.BaseDialog;
import com.atid.app.atx.dialog.MessageBox;
import com.atid.app.atx.dialog.NumberDialog;
import com.atid.app.atx.dialog.YesNoMessageBox;
import com.atid.lib.diagnostics.ATException;
import com.atid.lib.reader.ATEAReader;
import com.atid.lib.reader.event.IATEAReaderEventListener;
import com.atid.lib.reader.types.KeyState;
import com.atid.lib.reader.types.KeyType;
import com.atid.lib.reader.types.OperationMode;
import com.atid.lib.transport.ATransport;
import com.atid.lib.transport.types.ConnectState;
import com.atid.lib.types.ActionState;
import com.atid.lib.types.ResultCode;
import com.atid.lib.util.SysUtil;
import com.atid.lib.util.diagnotics.ATLog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application.ActivityLifecycleCallbacks;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class DeviceManageActivity extends PermissionActivity
		implements OnClickListener, OnListItemClickListener, OnItemClickListener, IATEAReaderEventListener {

	private static final String TAG = DeviceManageActivity.class.getSimpleName();
	private static final int INFO = ATLog.L1;

	public static final int REQUEST_ENABLE_BLUETOOTH = 1;

	// ------------------------------------------------------------------------
	// Member Variable
	// ------------------------------------------------------------------------

	private TextView txtVersion;
	private ListView lstDevices;
	private Button btnNewDevice;

	private DeviceListAdapter adpDevices;

	private BluetoothAdapter mBluetooth;
	private WifiManager mWifi;

	private boolean mIsShowActivity;
	private boolean mIsCheckEnableBluetooth;
	private boolean mIsRegistReceiver;
	
	private NumberDialog dlgSdkLogLevel;
	
	private PowerManager.WakeLock mWakeLock;
	private ActivityLifecycleManager mActivityLifecycleCallbacks;

	// ------------------------------------------------------------------------
	// Override Event Methods
	// ------------------------------------------------------------------------

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		mActivityLifecycleCallbacks = new ActivityLifecycleManager();
		getApplication().registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_device_manage);

		mIsRegistReceiver = false;
		
		checkPermission();
		ATLog.i(TAG, INFO, "INFO. onCreate()");
	}

	@Override
	protected void onDestroy() {

		// Uninitialize Wifi
		if (mIsRegistReceiver) {
			unregisterReceiver(mBroadcastReceiver);
		}

		if(GlobalData.DataManager.getDeviceItemList() != null) {
			for(DeviceItem item : GlobalData.DataManager.getDeviceItemList()) {
				if( !GlobalData.saveConfig(this, item.getType(), item.getAddress()) ) {
					ATLog.e(TAG, "ERROR. onDestroy() - Failed to save config to global data[%s, %s] ",
							item.getType().toString(), item.getAddress());
				}
			}
		}
		
		// Destroy Reader Manager
		if (GlobalData.ReaderManager != null) {
			GlobalData.ReaderManager.destroy();
		}

		ATLog.i(TAG, INFO, "INFO. onDestroy()");

		ATLog.shutdown();
		super.onDestroy();
		getApplication().unregisterActivityLifecycleCallbacks(mActivityLifecycleCallbacks);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {
		case REQUEST_ENABLE_BLUETOOTH:
			if (resultCode == Activity.RESULT_OK) {

				checkSystem();

				ATLog.i(TAG, INFO, "INFO. onActivityResult(REQUEST_ENABLE_BLUETOOTH)");
				return;
			}
			GlobalData.isEnableBluetooth = false;
			return;
		case MainActivity.ID:
			if (resultCode == Activity.RESULT_FIRST_USER) {
				mIsShowActivity = false;
				adpDevices.notifyDataSetChanged();
			}
			break;
		case RegistDeviceActivity.ID:
			if (resultCode == Activity.RESULT_OK) {

				DeviceItem item = data.getParcelableExtra(RegistDeviceActivity.ITEM);
				addReader(item);
				GlobalData.loadConfig(this, item.getType(), item.getAddress());
				
				ATLog.i(TAG, INFO, "INFO. onActivityResult(RegistDeviceActivity, [%s])", item.toString());
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onPermissionsResult(boolean result) {

		if (!result) {
			MessageBox.show(this, R.string.msg_fail_permission, R.string.title_error,
					android.R.drawable.ic_dialog_alert, new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							DeviceManageActivity.this.finish();
							return;
						}
					});
			return;
		}

		dlgSdkLogLevel = new NumberDialog();
		dlgSdkLogLevel.setValue(ATLog.getLogLevel());
		
		mIsShowActivity = false;
		mIsCheckEnableBluetooth = false;

		// Allocate Database Manager
		DataManager dataManager = new DataManager(this);

		// Allocate Reader Manager
		ATEAReaderManager readerManager = new ATEAReaderManager();

		readerManager.addAll(dataManager.getDeviceItemList());

		GlobalData.DataManager = dataManager;
		GlobalData.ReaderManager = readerManager;
		
		for(DeviceItem item : dataManager.getDeviceItemList())
			GlobalData.loadConfig(this, item.getType(), item.getAddress());

		// Initialize Widgets
		initActivity();

		// Initialize Bluetooth
		mBluetooth = BluetoothAdapter.getDefaultAdapter();

		// Not supported.
//		// Initialize Wifi
//		registerReceiver(mBroadcastReceiver, new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION));
//		mIsRegistReceiver = true;
//		mWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);

		checkSystem();

		readerManager.setListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.device_manage_option, menu);
		
		
		ATLog.i(TAG, INFO, "INFO. onCreateOptionsMenu()");
		return true;
	}
	
	@Override 
	public boolean onPrepareOptionsMenu(Menu menu) {
		ATLog.i(TAG, INFO, "INFO. onCreateOptionsMenu()");
		return super.onPrepareOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if(id == R.id.menu_log_level) {
			
			dlgSdkLogLevel.setValue(ATLog.getLogLevel());
			
			String message = String.format(Locale.US, "%s (Range : 0 ~ 9)",
					getResources().getString(R.string.title_log_level));
			
			dlgSdkLogLevel.showDialog(this, message, new BaseDialog.IValueChangedListener() {
				
				@Override
				public void onValueChanged(BaseDialog dialog) {
					if(dlgSdkLogLevel.getValue() >= 0 && dlgSdkLogLevel.getValue() < 10)
						ATLog.setLogLevel(dlgSdkLogLevel.getValue());
				}
			}, new BaseDialog.ICancelListener() {

				@Override
				public void onCanceled(BaseDialog dialog) {
					
				}
				
			});
			return true;			
		}
		ATLog.i(TAG, INFO, "INFO. onCreateOptionsMenu()");
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.new_device:
			
			if(!isAvailableBluetoothState()) {
				Toast.makeText(this, "Current state of the local Bluetooth adapter is not ON", Toast.LENGTH_SHORT).show();
				return;
			}
			
			Intent intent = new Intent(this, RegistDeviceActivity.class);
			startActivityForResult(intent, RegistDeviceActivity.ID);
			ATLog.i(TAG, INFO, "INFO. onClick(new_device)");
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> view, View item, int position, long id) {

		ATEAReader reader = adpDevices.getItem(position);

		if (reader.getState() == ConnectState.Connected) {
			showMainActivity(reader);
		} else {
			if(!isAvailableBluetoothState()) {
				Toast.makeText(this, "Current state of the local Bluetooth adapter is not ON", Toast.LENGTH_SHORT).show();
				return;
			}
			
			mIsShowActivity = true;
			if (!reader.connect()) {
				ATLog.e(TAG, "ERROR. onItemClick(%d) - Failed to connect reader", position);
				return;
			}
		}
		ATLog.i(TAG, INFO, "INFO. onItemClick(%d)", position);
	}

	@Override
	public void onItemButtonClick(int position, View view) {

		final int pos = position;
		PopupMenu popup = new PopupMenu(this, view);
		getMenuInflater().inflate(R.menu.menu_device_list, popup.getMenu());
		MenuItem menuAction = popup.getMenu().findItem(R.id.menu_action);

		final ATEAReader reader = adpDevices.getItem(position);
		if (reader == null) {
			ATLog.e(TAG, "ERROR. onItemButtonClick(%d) - Failed to invalid reader object", position);
			return;
		}

		if (reader.getState() == ConnectState.Connected) {
			menuAction.setTitle(R.string.menu_disconnect);
		} else {
			menuAction.setTitle(R.string.menu_connect);
		}
		
		if(!isAvailableBluetoothState()) {
			Toast.makeText(this, "Current state of the local Bluetooth adapter is not ON", Toast.LENGTH_SHORT).show();
			menuAction.setEnabled(false);
		} else
			menuAction.setEnabled(true);
		
		popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				switch (item.getItemId()) {
				case R.id.menu_action:
					if (reader.getState() == ConnectState.Connected) {
						reader.disconnect();
					} else {
						if (!reader.connect()) {
							ATLog.e(TAG,
									"ERROR. onItemButtonClick(%d).onMenuItemClick(menu_action) - Failed to connect reader",
									pos);
							return true;
						}
					}
					ATLog.i(TAG, INFO, "INFO. onItemButtonClick(%d).onMenuItemClick(menu_action)", pos);
					break;
				case R.id.menu_delete:
					deleteReader(pos);
					ATLog.i(TAG, INFO, "INFO. onItemButtonClick(%d).onMenuItemClick(menu_delete)", pos);
					break;
				}
				return true;
			}
		});
		popup.show();
		ATLog.i(TAG, INFO, "INFO. onItemButtonClick(%d) - [%s,%s]", 
				position, reader.getDeviceName(), reader.getAddress());
	}

	// ------------------------------------------------------------------------
	// Internal Widget Control Methods
	// ------------------------------------------------------------------------

	private void initActivity() {

		txtVersion = (TextView) findViewById(R.id.app_version);
		txtVersion.setText(SysUtil.getVersion(this));

		lstDevices = (ListView) findViewById(R.id.device_list);
		adpDevices = new DeviceListAdapter(this, GlobalData.ReaderManager);
		adpDevices.setOnListItemClickListener(this);
		lstDevices.setAdapter(adpDevices);
		lstDevices.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		lstDevices.setOnItemClickListener(this);
		lstDevices.setEnabled(true);

		btnNewDevice = (Button) findViewById(R.id.new_device);
		btnNewDevice.setOnClickListener(this);

		ATLog.i(TAG, INFO, "INFO. initActivity()");
	}

	// ------------------------------------------------------------------------
	// Internal Operation Methods
	// ------------------------------------------------------------------------
	
	private boolean isAvailableBluetoothState() {
		boolean available = false;
		
		if(mBluetooth == null)
			return available;
		
		int state = BluetoothAdapter.STATE_TURNING_ON;
		
		while(state == BluetoothAdapter.STATE_TURNING_OFF || state == BluetoothAdapter.STATE_TURNING_ON) {
			state = mBluetooth.getState();
		}
		
		if(state == BluetoothAdapter.STATE_ON) {
			available = true;
		} else {
			ATLog.i(TAG, INFO, "INFO. current state of the local Bluetooth adapter : " + state);
		}
		
		return available;
	}

	// Check Bluetooth & Wifi System
	@SuppressLint("InlinedApi")
	private synchronized void checkSystem() {

		// Check Bluetooth
		if (mBluetooth == null) {
			// Not Support Bluetooth...
			GlobalData.isSupportBluetooth = false;
			ATLog.e(TAG, "INFO. checkSystem() - Not suppored bluetooth");
		} else {
			GlobalData.isSupportBluetooth = true;
			// Suppoted Bluetooth
			if (!mBluetooth.isEnabled() && !mIsCheckEnableBluetooth) {
				mIsCheckEnableBluetooth = true;
				// Disabled Bluetotoh
				GlobalData.isEnableBluetooth = false;
				Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(intent, REQUEST_ENABLE_BLUETOOTH);
				ATLog.i(TAG, INFO, "INFO. checkSystem() - Request Enable Bluetooth");
				return;
			} else {
				GlobalData.isEnableBluetooth = true;
			}
			// Check Support BluetoothLe?
			GlobalData.isSupportBluetoothLe = getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
		}

		// Check Wifi
		if (mWifi == null) {
			// Not Support Wifi...
			GlobalData.isSupprotWifi = false;
			ATLog.e(TAG, "INFO. checkSystem() - Not suppored wifi");
		} else {
			// Supported Wifi
			GlobalData.isSupprotWifi = true;
			if (!mWifi.isWifiEnabled()) {
				// Disable Wifi...
				GlobalData.isEnableWifi = false;
				YesNoMessageBox.show(this, R.string.msg_turn_on_wifi, R.string.title_wifi,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								mWifi.setWifiEnabled(true);
							}
						}, new DialogInterface.OnCancelListener() {

							@Override
							public void onCancel(DialogInterface dialog) {

								if (!GlobalData.isSupportBluetooth && !GlobalData.isSupprotWifi) {
									MessageBox.show(DeviceManageActivity.this, R.string.msg_not_support_bt_or_wifi,
											R.string.title_system);
								} else if (GlobalData.isSupportBluetooth && !GlobalData.isSupprotWifi) {
									MessageBox.show(DeviceManageActivity.this, R.string.msg_not_support_wifi,
											R.string.title_wifi);
								} else if (!GlobalData.isSupportBluetooth && GlobalData.isSupprotWifi) {
									MessageBox.show(DeviceManageActivity.this, R.string.msg_not_support_bt,
											R.string.title_bt);
								}
							}
						});
				ATLog.i(TAG, INFO, "INFO. checkSystem() - Request Enable Wifi");
				return;
			} else {
				GlobalData.isEnableWifi = true;
			}
		}

//		if (!GlobalData.isSupportBluetooth && !GlobalData.isSupprotWifi) {
//			MessageBox.show(this, R.string.msg_not_support_bt_or_wifi, R.string.title_system);
//		} else if (GlobalData.isSupportBluetooth && !GlobalData.isSupprotWifi) {
//			MessageBox.show(this, R.string.msg_not_support_wifi, R.string.title_wifi);
//		} else if (!GlobalData.isSupportBluetooth && GlobalData.isSupprotWifi) {
//			MessageBox.show(this, R.string.msg_not_support_bt, R.string.title_bt);
//		}
		
		if (!GlobalData.isSupportBluetooth) {
			MessageBox.show(this, R.string.msg_not_support_bt, R.string.title_bt);
		}

		ATLog.i(TAG, INFO, "INFO. checkSystem()");
	}

	// Add Reader to Reader Manager
	private void addReader(DeviceItem item) {

		if (!GlobalData.DataManager.insertDevice(item)) {
			ATLog.e(TAG, "ERROR. addReader([%s]) - Failed to insert device to database", item.toString());
			return;
		}

		ATEAReader reader = GlobalData.ReaderManager.add(item);
		reader.addListener(this);
		adpDevices.notifyDataSetChanged();

		ATLog.i(TAG, INFO, "INFO. addReader([%s])", item.toString());
	}

	// Delete Reader from Reader Manager
	private void deleteReader(int position) {

		ATEAReader reader = adpDevices.getItem(position);
		ATransport transport = null;

		if (reader == null) {
			ATLog.e(TAG, "ERROR. deleteReader(%d) - Failed to invalid reader", position);
			return;
		}

		transport = reader.getTransport();
		if (!GlobalData.DataManager.deleteDevice(transport.getDeviceName(), transport.getMacAddress())) {
			ATLog.e(TAG, "ERROR. deleteReader(%d) - Failed to delete device from database", position);
			return;
		}

		if (!GlobalData.ReaderManager.remove(reader)) {
			ATLog.e(TAG, "ERROR. deleteReader(%d) - Failed to delete reader from reader manager", position);
			return;
		}
		adpDevices.notifyDataSetChanged();

		reader.removeListener(this);
		if (reader.getState() != ConnectState.Disconnected) {
			reader.disconnect();
		}
		
		if( !GlobalData.removeConfig(this, reader.getDeviceType(), reader.getAddress()) ){
			ATLog.e(TAG, "ERROR. deleteReader(%d) - Failed to delete config from global data", position);
		}

		ATLog.i(TAG, INFO, "INFO. deleteReader(%d)", position);
	}

	// Show Main Activity
	private void showMainActivity(ATEAReader reader) {

		int position = GlobalData.ReaderManager.indexOf(reader);
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra(Constants.SELECTED_READER, position);
		startActivityForResult(intent, MainActivity.ID);
		ATLog.i(TAG, INFO, "INFO. showMainAcitivty([%s])", reader);
	}

	// ------------------------------------------------------------------------
	// Broadcast Receiver Methods
	// ------------------------------------------------------------------------

	private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			String action = intent.getAction();
			if (action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
				switch (mWifi.getWifiState()) {
				case WifiManager.WIFI_STATE_ENABLED:
					checkSystem();
					break;
				}
			}
		}

	};

	// ------------------------------------------------------------------------
	// Reader Event Handler Methods
	// ------------------------------------------------------------------------

	@Override
	public void onReaderStateChanged(ATEAReader reader, ConnectState state, Object params) {

		ATLog.i(TAG, INFO, "EVENT. onReaderStateChanged([%s], %s)", reader, state);
		adpDevices.notifyDataSetChanged();

		switch (state) {
		case Connected:
			if (mIsShowActivity) {
				mIsShowActivity = false;
				try {
					if(reader.getBarcode() != null)
						reader.getBarcode().setCharset((Charset)
								GlobalData.getConfig(getApplicationContext(), reader.getDeviceType(), reader.getAddress(), 
										GlobalData.KEY_BARCODE_CHARSET));
				} catch (ATException e) {
					ATLog.e(TAG, "EVENT. onReaderStateChanged([%s], %s) - Failed to set Character Set", reader, state);
				} catch (Exception e) {
					ATLog.e(TAG, "EVENT. onReaderStateChanged([%s], %s) - Failed to get global data(%s)", 
							reader, state, GlobalData.KEY_BARCODE_CHARSET);
				}
				showMainActivity(reader);
			}
			adpDevices.notifyDataSetChanged();
			break;
		case Disconnected:
			mIsShowActivity = false;
			adpDevices.notifyDataSetChanged();
			break;
		default:
			break;
		}

		//ATLog.i(TAG, INFO, "EVENT. onReaderStateChanged([%s], %s)", reader, state);
	}

	@Override
	public void onReaderActionChanged(ATEAReader reader, ResultCode code, ActionState action, Object params) {

		ATLog.i(TAG, INFO, "EVENT. onReaderActionChanged([%s], %s, %s)", reader, code, action);
	}

	@Override
	public void onReaderOperationModeChanged(ATEAReader reader, OperationMode mode, Object params) {

		ATLog.i(TAG, INFO, "EVENT. onReaderOperationModeChanged([%s], %s)", reader, mode);
	}

	@Override
	public void onReaderBatteryState(ATEAReader reader, int batteryState, Object params) {

		ATLog.i(TAG, INFO, "EVENT. onReaderBatteryState([%s], %d)", reader, batteryState);
	}

	@Override
	public void onReaderKeyChanged(ATEAReader reader, KeyType type, KeyState state, 
			Object params) {
		ATLog.i(TAG, INFO, "EVENT. onReaderKeyChanged([%s], %s, %s)", reader, type, state);
	}

	public class ActivityLifecycleManager implements ActivityLifecycleCallbacks {

		private int mRefCount = 0;
		private String _tag = ActivityLifecycleManager.class.getSimpleName();
		
		@SuppressWarnings("deprecation")
		@Override
		public void onActivityStarted(Activity activity) {
			mRefCount++;
			ATLog.i(TAG, INFO, "INFO. ActivityLifecycleManager.onActivityStarted : " + mRefCount);
			
			if(mRefCount == 1) {
				// Setup always wake up
				android.content.Context context = activity.getApplicationContext();
				//SysUtil.wakeLock(activity.getApplicationContext(), SysUtil.getAppName(context));
				
				if(mWakeLock != null) {
					ATLog.i(TAG, INFO, "INFO. Already exist wake lock");
				}
				
				PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
				mWakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, _tag);
				//mWakeLock = powerManager.newWakeLock(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, TAG);
				mWakeLock.acquire();
				ATLog.i(TAG, INFO, "INFO. Acquires the wake lock.");
			}
		}
		
		@Override
		public void onActivityStopped(Activity activity) {
			mRefCount--;
			ATLog.i(TAG, INFO, "INFO. ActivityLifecycleManager.onActivityStopped : " + mRefCount);
			
			if(mRefCount == 0) {
				// release WakeLock.
				//SysUtil.wakeUnlock();
				
				if (mWakeLock == null)
					return;

				mWakeLock.release();
				mWakeLock = null;
				ATLog.i(TAG, INFO, "INFO. Releases the wake lock.");
			}
		}

		@Override
		public void onActivityCreated(Activity activity, Bundle savedInstanceState) {}
		@Override
		public void onActivityResumed(Activity activity) {}
		@Override
		public void onActivityPaused(Activity activity) {}
		@Override
		public void onActivitySaveInstanceState(Activity activity, Bundle outState) {}
		@Override
		public void onActivityDestroyed(Activity activity) {}
		
	}
}
