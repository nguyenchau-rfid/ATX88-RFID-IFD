package com.atid.app.atx.activity;

import java.util.Set;

import com.atid.app.atx.R;
import com.atid.app.atx.adapter.DeviceListBluetoothAdapter;
import com.atid.app.atx.data.DeviceItem;
import com.atid.app.atx.data.GlobalData;
import com.atid.lib.util.diagnotics.ATLog;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

public class RegistDeviceActivity extends Activity implements OnClickListener, OnItemClickListener {

	private static final String TAG = RegistDeviceActivity.class.getSimpleName();
	private static final int INFO = ATLog.L2;

	public static final int ID = 0x12101000;
	public static final String ITEM = "item";

	// ------------------------------------------------------------------------
	// Member Variable
	// ------------------------------------------------------------------------

	private ProgressBar progBluetooth;
	private ListView lstBtPairedDevices;
	private ListView lstBtNewDevices;
	private Button btnScan;

	private DeviceListBluetoothAdapter adpBtPairedDevices;
	private DeviceListBluetoothAdapter adpBtNewDevices;

	private BluetoothAdapter mBluetooth;

	// ------------------------------------------------------------------------
	// Override Event Methods
	// ------------------------------------------------------------------------

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regist_device);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		// Initialize Widgets
		initActivity();

		// Initialize Bluetooth
		if (GlobalData.isSupportBluetooth) {
			mBluetooth = BluetoothAdapter.getDefaultAdapter();

			// Register Bluetooth Discover Event Receiver
			IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
			registerReceiver(mReceiver, filter);
			filter = new IntentFilter(BluetoothDevice.ACTION_NAME_CHANGED);
			registerReceiver(mReceiver, filter);
			filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
			registerReceiver(mReceiver, filter);
			filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
			registerReceiver(mReceiver, filter);

			if (GlobalData.isEnableBluetooth) {
				// Fill Paired Bluetooth Device List
				fillPairedBluetoothDevices();
			}
		}

		ATLog.i(TAG, INFO, "INFO. onCreate()");
	}

	@Override
	protected void onDestroy() {

		unregisterReceiver(mReceiver);

		ATLog.i(TAG, INFO, "INFO. onDestroy()");
		super.onDestroy();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:
			exitActivity();
			ATLog.i(TAG, INFO, "INFO. onOptionsItemSelected(home)");
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {

		exitActivity();
		ATLog.i(TAG, INFO, "INFO. onBackPressed()");

		super.onBackPressed();
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.scan_device:
			if (isDiscover())
				stopDiscover();
			else
				startDiscover();
			ATLog.i(TAG, INFO, "INFO. onClick(scan_device)");
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parentView, View view, int position, long id) {

		Intent intent = null;

		switch (parentView.getId()) {
		case R.id.paired_device:
			intent = new Intent();
			intent.putExtra(ITEM, adpBtPairedDevices.getItem(position));
			exitActivity(intent);
			ATLog.i(TAG, INFO, "INFO. onItemClick(paired_device, %d)", position);
			return;
		case R.id.new_bluetooth_device:
			intent = new Intent();
			intent.putExtra(ITEM, adpBtNewDevices.getItem(position));
			exitActivity(intent);
			ATLog.i(TAG, INFO, "INFO. onItemClick(new_bluetooth_device, %d)", position);
			return;
		}
	}

	// ------------------------------------------------------------------------
	// Internal Widgets Control Methods
	// ------------------------------------------------------------------------

	private void initActivity() {

		progBluetooth = (ProgressBar) findViewById(R.id.prog_bluetooth);

		lstBtPairedDevices = (ListView) findViewById(R.id.paired_device);
		adpBtPairedDevices = new DeviceListBluetoothAdapter(this);
		lstBtPairedDevices.setAdapter(adpBtPairedDevices);
		lstBtPairedDevices.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		lstBtPairedDevices.setOnItemClickListener(this);

		lstBtNewDevices = (ListView) findViewById(R.id.new_bluetooth_device);
		adpBtNewDevices = new DeviceListBluetoothAdapter(this);
		lstBtNewDevices.setAdapter(adpBtNewDevices);
		lstBtNewDevices.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		lstBtNewDevices.setOnItemClickListener(this);

		btnScan = (Button) findViewById(R.id.scan_device);
		btnScan.setOnClickListener(this);

		ATLog.i(TAG, INFO, "INFO. initActivity()");
	}

	private void exitActivity() {
		exitActivity(null);
	}

	private void exitActivity(Intent intent) {

		if (mBluetooth.isDiscovering()) {
			mBluetooth.cancelDiscovery();
		}

		if (intent == null)
			setResult(Activity.RESULT_CANCELED);
		else
			setResult(Activity.RESULT_OK, intent);
		finish();

		ATLog.i(TAG, INFO, "INFO. exitActivity()");
	}

	// ------------------------------------------------------------------------
	// Device Discovering Methods
	// ------------------------------------------------------------------------

	private boolean isDiscover() {
		return mBluetooth.isDiscovering();
	}

	private void startDiscover() {

		btnScan.setEnabled(false);

		progBluetooth.setVisibility(View.VISIBLE);
		mBluetooth.startDiscovery();

		ATLog.i(TAG, INFO, "INFO. startDiscover()");
	}

	private void stopDiscover() {

		btnScan.setEnabled(false);

		mBluetooth.cancelDiscovery();
		
		ATLog.i(TAG, INFO, "INFO. stopDiscover()");
	}

	// ------------------------------------------------------------------------
	// Bluetooth Device Discovering Methods
	// ------------------------------------------------------------------------

	private void fillPairedBluetoothDevices() {

		Set<BluetoothDevice> devices = mBluetooth.getBondedDevices();
		String name = "";

		for (BluetoothDevice device : devices) {
			name = device.getName();
			if (DeviceItem.contains(name))
				adpBtPairedDevices.add(name, device.getAddress());
		}
		adpBtPairedDevices.notifyDataSetChanged();

		ATLog.i(TAG, INFO, "INFO. fillPairedBluetoothDevices()");
	}

	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			String action = intent.getAction();
			BluetoothDevice device = null;

			if (BluetoothDevice.ACTION_FOUND.equals(action)) {

				device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				AddNewDevice(device);

				ATLog.i(TAG, INFO, "EVENT. ACTION_FOUND");

			} else if (BluetoothDevice.ACTION_NAME_CHANGED.equals(action)) {

				device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				AddNewDevice(device);

				ATLog.i(TAG, INFO, "EVENT. ACTION_NAME_CHANGED");

			} else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {

				btnScan.setText(R.string.action_stop);
				btnScan.setEnabled(true);

				ATLog.i(TAG, INFO, "EVENT. ACTION_DISCOVERY_STARTED");

			} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {

				btnScan.setText(R.string.action_scan_device);
				btnScan.setEnabled(true);

				progBluetooth.setVisibility(View.GONE);
				
				ATLog.i(TAG, INFO, "EVENT. ACTION_DISCOVERY_FINISHED");

			}
		}

	};

	private void AddNewDevice(BluetoothDevice device) {
		if (device == null)
			return;
		if (device.getBondState() == BluetoothDevice.BOND_BONDED)
			return;
		String name = device.getName();
		String address = device.getAddress();

		if (DeviceItem.contains(name)) {
			adpBtNewDevices.add(name, address);
		}

		ATLog.i(TAG, INFO, "INFO. AddNewDevice([%s]:[%s])", name, address);
	}

}
