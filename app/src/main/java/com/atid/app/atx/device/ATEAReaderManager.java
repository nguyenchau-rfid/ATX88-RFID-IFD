package com.atid.app.atx.device;

import java.util.ArrayList;

import com.atid.app.atx.data.DeviceItem;
import com.atid.lib.atx88.AT188Reader;
import com.atid.lib.atx88.AT288Reader;
import com.atid.lib.atx88.AT388Reader;
import com.atid.lib.atx88.ATRFPrismaReader;
import com.atid.lib.reader.ATEAReader;
import com.atid.lib.reader.event.IATEAReaderEventListener;
import com.atid.lib.transport.ATransport;
import com.atid.lib.transport.ATransportBluetooth;
import com.atid.lib.transport.ATransportNetwork;
import com.atid.lib.transport.types.ConnectState;
import com.atid.lib.util.diagnotics.ATLog;

public class ATEAReaderManager {

	private static final String TAG = ATEAReaderManager.class.getSimpleName();
	private static final int INFO = ATLog.L1;

	private static final boolean USE_KEEPALIVE = true;
	
	// ------------------------------------------------------------------------
	// Member Varialbe
	// ------------------------------------------------------------------------

	private ArrayList<ATEAReader> mReaders;

	// ------------------------------------------------------------------------
	// Constructor
	// ------------------------------------------------------------------------

	public ATEAReaderManager() {
		mReaders = new ArrayList<ATEAReader>();
	}

	// ------------------------------------------------------------------------
	// ATRReader Management Methods
	// ------------------------------------------------------------------------

	public int count() {
		return mReaders.size();
	}
	
	public ATEAReader get(int position) {
		return mReaders.get(position);
	}
	
	public int indexOf(ATEAReader reader) {
		return mReaders.indexOf(reader);
	}
	
	public void addAll(ArrayList<DeviceItem> items) {
		
		if (items == null) {
			ATLog.e(TAG, "ERROR. addAll() - Failed to invalid item list");
			return;
		}
		
		for (DeviceItem item : items) {
			add(item);
		}
		
		ATLog.i(TAG, INFO, "INFO. addAll(%d)", items.size());
	}

	public ATEAReader add(DeviceItem item) {
		ATransport transport = null;
		ATEAReader reader = null;

		if ((transport = getTransport(item)) == null) {
			ATLog.e(TAG, "ERROR. add([%s]) - Failed to not support connection type device", item.toString());
			return null;
		}

		switch (transport.getDeviceType()) {
		case AT188:
		case AT188N:
			reader = new AT188Reader(transport);
			break;
		case AT288:
		case AT288N:
			reader = new AT288Reader(transport);
			break;
		case AT388:
		case ATS100:
			reader = new AT388Reader(transport);
			break;
		case RFBlaster:
		case RFPrisma:
			reader = new ATRFPrismaReader(transport);
			break;
		default:
			ATLog.e(TAG, "ERROR. add([%s]) - Failed to not support device", item.toString());
			return null;
		}

		if (!USE_KEEPALIVE)
			reader.setCheckInterval(0);

		mReaders.add(reader);
		ATLog.i(TAG, INFO, "INFO. add([%s])", item.toString());
		return reader;
	}

	public void clear() {
		mReaders.clear();
		ATLog.i(TAG, INFO, "INFO. clear()");
	}
	
	public boolean remove(ATEAReader reader) {
		
		if (!mReaders.remove(reader)) {
			ATLog.e(TAG, "ERROR. remove([%s]) - Failed to invalid reader", reader.toString());
			return false;
		}
		ATLog.i(TAG, INFO, "INFO. remove([%s])", reader.toString());
		return true;
	}

	public ArrayList<ATEAReader> getReaders() {
		return mReaders;
	}
	
	public void setListener(IATEAReaderEventListener listener) {
		for (ATEAReader reader : mReaders) {
			reader.addListener(listener);
		}
	}
	
	public void removeListener(IATEAReaderEventListener listener) {
		for (ATEAReader reader : mReaders) {
			reader.removeListener(listener);
		}
	}

	// ------------------------------------------------------------------------
	// Operation Methods
	// ------------------------------------------------------------------------
	
	public void destroy() {
		
		for (ATEAReader reader : mReaders) {
			if (reader.getState() != ConnectState.Disconnected)
				reader.destroy();
		}
		mReaders.clear();
		ATLog.i(TAG, INFO, "INFO. destroy()");
	}

	// ------------------------------------------------------------------------
	// Internal Helper Methods
	// ------------------------------------------------------------------------

	private ATransport getTransport(DeviceItem item) {

		ATransport transport = null;

		switch (item.getConnectType()) {
		case Bluetooth:
			transport = new ATransportBluetooth(item.getType(), item.getName(), item.getAddress());
			break;
		case Wifi:
			transport = new ATransportNetwork(item.getType(), item.getName(), item.getMac(), item.getAddress());
			break;
		default:
			ATLog.e(TAG, "ERROR. getDevice([%s]) - Failed to not support connect type", item.toString());
			return null;
		}
		ATLog.i(TAG, INFO, "INFO. getDevice([%s])", item.toString());
		return transport;
	}
}
