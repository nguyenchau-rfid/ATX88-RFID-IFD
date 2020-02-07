package com.atid.app.atx.activity.view;

import com.atid.app.atx.R;
import com.atid.app.atx.activity.BarcodeOptionActivity;
import com.atid.app.atx.activity.RfidOptionActivity;
import com.atid.app.atx.data.Constants;
import com.atid.app.atx.data.GlobalData;
import com.atid.app.atx.dialog.WaitDialog;
import com.atid.lib.diagnostics.ATException;
import com.atid.lib.module.barcode.types.BarcodeType;
//import com.atid.lib.module.rfid.uhf.types.SelectFlag;
import com.atid.lib.reader.ATEAReader;
import com.atid.lib.reader.event.IATEAReaderEventListener;
import com.atid.lib.reader.types.KeyState;
import com.atid.lib.reader.types.KeyType;
import com.atid.lib.reader.types.OperationMode;
import com.atid.lib.types.ActionState;
import com.atid.lib.types.DeviceType;
import com.atid.lib.types.ResultCode;
import com.atid.lib.util.diagnotics.ATLog;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseView extends Fragment {

	protected static final int INFO = ATLog.L2;
	protected String TAG;

	public static final int VIEW_INVENTORY = 1000;
	public static final int VIEW_STORED_DATA = 1001;
	public static final int VIEW_ACCESS_MEMORY = 1002;
	public static final int VIEW_OPTION = 1003;
	
	protected static final int MAX_MASK = 8;

	protected static final int OPTION_BARCODE = 1;
	protected static final int OPTION_RFID_UHF = 2;
	
	// ------------------------------------------------------------------------
	// Member Variable
	// ------------------------------------------------------------------------

	protected View mView;
	protected long mId;
	protected boolean mIsEnabled;
	protected boolean mIsUseMask;
	private boolean mIsInitialize;

	// ------------------------------------------------------------------------
	// Constructor
	// ------------------------------------------------------------------------

	public BaseView() {
		super();
		mIsEnabled = false;
		mIsInitialize = false;
		mIsUseMask = false;
	}

	// ------------------------------------------------------------------------
	// Overiable Event Methods
	// ------------------------------------------------------------------------

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		mView = inflater.inflate(getInflateResId(), container, false);

		// Initialize Widget for Layout
		initView();

		// Enable Widgets...
		enableWidgets(false);

		loadProperties();

		ATLog.i(TAG, INFO, "INFO. onCreateView()");
		return mView;
	}

	@Override
	public void onDestroyView() {

		if(getReader().getAction() != ActionState.Stop) {
			if(getReader().getRfidUhf() != null) {
				getReader().getRfidUhf().stop();
				while(getReader().getAction() != ActionState.Stop) {
					try {
						ATLog.e(TAG, "wait for stop.");
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			if(getReader().getBarcode() != null)
				getReader().getBarcode().stop();
		}
		
		// De-initialize Widget for Layout
		exitView();
		//ATLog.i(TAG, INFO, "INFO. onDestroyView()");
		ATLog.e(TAG, "INFO. onDestroyView()");
		super.onDestroyView();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == RfidOptionActivity.ID) {
			switch (resultCode) {
			case Activity.RESULT_FIRST_USER:
				finishView();
				break;
			case Activity.RESULT_OK:
			case Activity.RESULT_CANCELED:
				checkMask();
				completeSetting(OPTION_RFID_UHF, data);
				try {
					getReader().setUseActionKey(true);
				} catch (ATException e) {
					ATLog.e(TAG, "ERROR. onActivityResult() - Failed to enable key action");
				}
				enableWidgets(true);
				break;
			}
			ATLog.i(TAG, INFO, "INFO. onActivityResult() - RfidOptionActivity [%d]" , resultCode);
		} else if (requestCode == BarcodeOptionActivity.ID) {
			switch (resultCode) {
			case Activity.RESULT_FIRST_USER:
				finishView();
				break;
			case Activity.RESULT_OK:
			case Activity.RESULT_CANCELED:
				completeSetting(OPTION_BARCODE, data);
				try {
					getReader().setUseActionKey(true);
				} catch (ATException e) {
					ATLog.e(TAG, "ERROR. onActivityResult() - Failed to enable key action");
				}
				enableWidgets(true);
				break;
			}
			ATLog.i(TAG, INFO, "INFO. onActivityResult() - BarcodeOptionActivity [%d]" , resultCode);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	// ------------------------------------------------------------------------
	// Abstract Methods
	// ------------------------------------------------------------------------

	// Get Inflate Resource Id
	protected abstract int getInflateResId();

	// Initialize View
	protected abstract void initView();

	// Exit View
	protected abstract void exitView();

	// Enabled Widgets
	protected void enableWidgets(boolean enabled) {
		mIsEnabled = enabled && getReader().getAction() == ActionState.Stop;
	}

	// Exit View
	protected abstract void completeSetting(int type, Intent data);
		
	// Loading Reader Properties
	protected abstract boolean loadingProperties();

	// Loaded Reader Properteis
	protected abstract void loadedProperties(boolean isInitialize);

	// ------------------------------------------------------------------------
	// Abstract Event Methods
	// ------------------------------------------------------------------------

	public abstract void onReaderActionChanged(ATEAReader reader, ResultCode code, ActionState action, Object params);

	public abstract void onReaderReadTag(ATEAReader reader, String tag, Object params);

	public abstract void onReaderAccessResult(ATEAReader reader, ResultCode code, ActionState action, String epc,
			String data, Object params);

	public abstract void onReaderReadBarcode(ATEAReader reader, BarcodeType type, String codeId, String barcode,
			Object params);

	public abstract void onReaderOperationModeChanged(ATEAReader reader, OperationMode mode, Object params);

	public abstract void onReaderPowerGainChanged(ATEAReader reader, int power, Object params);

	public abstract void onReaderBatteryState(ATEAReader reader, int batteryState, Object params);
	
	public abstract void onReaderKeyChanged(ATEAReader reader, KeyType type, KeyState state, Object params); 
			
	
	// ------------------------------------------------------------------------
	// Heritable Methods
	// ------------------------------------------------------------------------

	protected ATEAReader getReader() {
		return ((IATEAReader) getActivity()).getReader();
	}

	public long getViewId() {
		return mId;
	}

	protected boolean isRFPrisma() {
		return getReader().getDeviceType() == DeviceType.RFBlaster
				|| getReader().getDeviceType() == DeviceType.RFPrisma;
	}
	
	protected boolean isATS100() {
		return getReader().getDeviceType() == DeviceType.ATS100;
	}
	
	protected void finishView() {
		getReader().removeListener((IATEAReaderEventListener) getActivity());
		getActivity().setResult(Activity.RESULT_FIRST_USER);
		getActivity().finish();
		
		ATLog.i(TAG, INFO, "INFO. finishView()");
	}

	// ------------------------------------------------------------------------
	// Load Reader Properties
	// ------------------------------------------------------------------------

	protected void loadProperties() {
		WaitDialog.show(getActivity(), R.string.msg_initialize_view);

		Thread thread = new Thread(mLoadingProperties);
		thread.start();
	}

	private Runnable mLoadingProperties = new Runnable() {

		@Override
		public void run() {
			mIsInitialize = loadingProperties();

			getActivity().runOnUiThread(mLoadedProperties);
		}

	};

	private Runnable mLoadedProperties = new Runnable() {

		@Override
		public void run() {

			loadedProperties(mIsInitialize);
			WaitDialog.hide();
		}

	};

	// ------------------------------------------------------------------------
	// Setting
	// ------------------------------------------------------------------------
	protected void showRfidSetting() {
		int position = GlobalData.ReaderManager.indexOf(getReader());
		Intent intent = new Intent(getActivity(), RfidOptionActivity.class);
		
		intent.putExtra(Constants.SELECTED_READER, position);
		try {
			intent.putExtra(Constants.RFID_TAG_SPEED, (Boolean)GlobalData.getConfig(getActivity().getApplicationContext(),
					getReader().getDeviceType(), getReader().getAddress(), GlobalData.KEY_TAG_SPEED));
		} catch (Exception e) {
			ATLog.e(TAG, "ERROR. showRfidSetting() - Failed to get global data(%s)", 
					GlobalData.KEY_TAG_SPEED);
		}
		
		startActivityForResult(intent, RfidOptionActivity.ID);
	}

	protected void showBarcodeSetting() {
		int position = GlobalData.ReaderManager.indexOf(getReader());
		Intent intent = new Intent(getActivity(), BarcodeOptionActivity.class);
		
		intent.putExtra(Constants.SELECTED_READER, position);
		try {
			intent.putExtra(Constants.BARCODE_RESTART_TIME, (Integer)GlobalData.getConfig(getActivity().getApplicationContext(),
					getReader().getDeviceType(), getReader().getAddress(), GlobalData.KEY_RESTART_TIME));
		} catch (Exception e) {
			ATLog.e(TAG, "ERROR. showBarcodeSetting() - Failed to get global data(%s)", 
					GlobalData.KEY_RESTART_TIME);

		}
		
		startActivityForResult(intent, BarcodeOptionActivity.ID);
	}
	
	// ------------------------------------------------------------------------
	// Selection Mask Methods
	// ------------------------------------------------------------------------

	protected boolean checkMask() {
//		SelectFlag selectFlag = SelectFlag.All;
		mIsUseMask = false;
//		try {
//			selectFlag = getReader().getRfidUhf().getSelectFlag();
//		} catch (ATException e) {
//			ATLog.e(TAG, e, "ERROR. checkMask() - Failed to load select flag");
//			return false;
//		}
		//if (selectFlag != SelectFlag.NotUsed) {
			for (int i = 0; i < MAX_MASK; i++) {
				try {
					mIsUseMask |= getReader().getRfidUhf().getSelectMask6cEnabled(i);
				} catch (ATException e) {
					ATLog.e(TAG, e, "ERROR. checkMask() - Failed to load select mask enabled [i]", i);
					return false;
				}
			}
		//}
		ATLog.i(TAG, INFO, "INFO. checkMask()");
		return true;
	}

	// ------------------------------------------------------------------------
	// Declare Interface ATEAReader
	// ------------------------------------------------------------------------

	public interface IATEAReader {

		ATEAReader getReader();
	}
}
