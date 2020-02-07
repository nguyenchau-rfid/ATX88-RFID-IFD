package com.atid.app.atx.activity.view;

import java.util.Locale;

import com.atid.app.atx.R;
import com.atid.app.atx.adapter.DataListAdapter;
import com.atid.app.atx.adapter.KeyListAdapter;
import com.atid.app.atx.data.Constants;
import com.atid.app.atx.data.GlobalData;
import com.atid.app.atx.dialog.MessageBox;
import com.atid.app.atx.dialog.WaitDialog;
import com.atid.app.atx.dialog.YesNoMessageBox;
import com.atid.lib.diagnostics.ATException;
import com.atid.lib.module.barcode.types.BarcodeType;
import com.atid.lib.module.rfid.uhf.params.SelectMask6cParam;
import com.atid.lib.module.rfid.uhf.params.TagExtParam;
import com.atid.lib.module.rfid.uhf.types.BankType;
import com.atid.lib.module.rfid.uhf.types.Mask6cAction;
import com.atid.lib.module.rfid.uhf.types.Mask6cTarget;
import com.atid.lib.module.rfid.uhf.types.SelectFlag;
import com.atid.lib.module.rfid.uhf.types.SessionFlag;
import com.atid.lib.reader.ATEAReader;
import com.atid.lib.reader.types.KeyState;
import com.atid.lib.reader.types.KeyType;
import com.atid.lib.reader.types.OperationMode;
import com.atid.lib.types.ActionState;
import com.atid.lib.types.ResultCode;
import com.atid.lib.util.StringUtil;
import com.atid.lib.util.diagnotics.ATLog;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class ViewInventory extends BaseView implements OnClickListener, OnCheckedChangeListener,
		RadioGroup.OnCheckedChangeListener, OnItemLongClickListener {

	private static final int METHOD_RFID = 0;
	private static final int METHOD_BARCODE = 1;
	private static final int METHOD_TRIGGER = 4;
	
	private static final int MASK_EPC_OFFSET = 16;
	private static final int NIBLE_SIZE = 4;

	private static final int NO_RESTART = 0;
	
	private static final double TPS_TIME_SECOND = 1000.0;
	
	// ------------------------------------------------------------------------
	// Member Variable
	// ------------------------------------------------------------------------

	private RadioGroup rdMethod;
	private RadioButton rdMethodRfid;
	private RadioButton rdMethodBarcode;
	private RadioButton rdMethodTrigger;
	
	private ListView lstData;
	private ListView lstTrigger;
	
	private TextView txtCount;
	private TextView txtTotalCount;
	private TextView txtKeyType;
	private TextView txtKeyState;
	private TextView txtTagSpeed;
	
	private LinearLayout linearAction;
	private LinearLayout linearCount;
	private LinearLayout linearTriggerState;
	
	private Button btnAction;
	private Button btnClear;
	private Button btnSetting;

	private DataListAdapter adpData;
	private KeyListAdapter adpTrigger;

	private volatile boolean mIsContinuousMode;
	private volatile boolean mIsReportRssi;

	private int mTotalCount;

	private int mMethod;

	private ISetWriteMemoryListener mSetWriteListener;

	private int mRfidTagCount;
	private volatile boolean mIsRfidTagSpeed;
	private long mRfidTagLastTime;
	
	private int mBarcodeRestartTime;
	private volatile boolean mIsBarcodeRestart;
	private Handler mHandler;
	
	// ------------------------------------------------------------------------
	// Constructor
	// ------------------------------------------------------------------------

	public ViewInventory() {
		super();
		TAG = ViewInventory.class.getSimpleName();
		mId = VIEW_INVENTORY;
		mTotalCount = 0;
		mMethod = METHOD_RFID;

		mIsContinuousMode = true;
		mIsReportRssi = false;

		mSetWriteListener = null;
		
		mRfidTagCount = 0;
		mIsRfidTagSpeed = false;
		mRfidTagLastTime = 0;

		mBarcodeRestartTime = 0;
		mIsBarcodeRestart = false;
		mHandler = new Handler();
		
	}

	// ------------------------------------------------------------------------
	// Public Methods
	// ------------------------------------------------------------------------

	public void setSetWriteMemoryListener(ISetWriteMemoryListener listener) {
		mSetWriteListener = listener;
	}

	// ------------------------------------------------------------------------
	// Override Event Methods
	// ------------------------------------------------------------------------

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		OperationMode mode = OperationMode.Normal;
		String checkId = "";
		switch (checkedId) {
		case R.id.method_rfid:
			if (!rdMethodRfid.isChecked())
				return;
			if (mMethod == METHOD_RFID)
				return;
			mMethod = METHOD_RFID;
			mode = OperationMode.Normal;
			checkId = "method_rfid";
			break;
		case R.id.method_barcode:
			if (!rdMethodBarcode.isChecked())
				return;
			if (mMethod == METHOD_BARCODE)
				return;
			mMethod = METHOD_BARCODE;
			mode = OperationMode.Barcode;
			checkId = "method_barcode";
			break;
		case R.id.method_trigger_event_only:
			if(!rdMethodTrigger.isChecked())
				return;
			if(mMethod == METHOD_TRIGGER)
				return;
			mMethod = METHOD_TRIGGER;
			mode = OperationMode.TriggerEventOnly;
			checkId = "method_trigger";
			break;
		default:
			ATLog.e(TAG, "ERROR. onCheckedChanged(%d) - Failed to unknown check id", checkedId);
			return;
		}
		try {
			getReader().setOperationMode(mode);
		} catch (ATException e) {
			ATLog.e(TAG, "ERROR. onCheckedChanged(%s) - Faield to set operation mode", checkId);
			return;
		}
		displayOperationMode();
		ATLog.i(TAG, INFO, "INFO. onCheckChnaged(%s)", checkId);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.action:
			if (mMethod == METHOD_RFID || mMethod == METHOD_BARCODE)
				action();
			break;
		case R.id.setting:
			if (mMethod == METHOD_RFID) {
				showRfidSetting();	
			} else if (mMethod == METHOD_BARCODE){
				showBarcodeSetting();
			}
			ATLog.i(TAG, INFO, "INFO. onClick(R.id.setting) - Method [%d]", mMethod);
			break;
		case R.id.clear:
			if (mMethod == METHOD_RFID || mMethod == METHOD_BARCODE)
				clear();
			break;
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		int type = adpData.getDataType(position);
		final String data = adpData.getData(position);

		if (type == DataListAdapter.DATATYPE_RFID) {
			
			if(getReader().getRfidUhf() != null && getReader().getAction() != ActionState.Stop) {
				getReader().getRfidUhf().stop();
			}
			
			YesNoMessageBox.show(getActivity(), R.string.msg_set_select_mask, R.string.title_select_mask,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							enableWidgets(false);
							WaitDialog.show(getActivity(), R.string.msg_saving_select_mask);
							SetSelectMaskThread thread = new SetSelectMaskThread(data);
							thread.start();
						}
					});
		} else if (type == DataListAdapter.DATATYPE_BARCODE) {
			
			if(getReader().getBarcode() != null && getReader().getAction() != ActionState.Stop) {
				getReader().getBarcode().stop();
			}
			
			YesNoMessageBox.show(getActivity(), R.string.msg_write_tag, R.string.title_write_tag,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (mSetWriteListener != null) {
								mSetWriteListener.onSetWriteMemory(StringUtil.toHex(data));
							}
						}
					});
		}
		return true;
	}

	// ------------------------------------------------------------------------
	// Action Methods
	// ------------------------------------------------------------------------

	@SuppressWarnings("incomplete-switch")
	private void action() {
		ResultCode res = ResultCode.NoError;
		enableWidgets(false);

		if (getReader().getAction() == ActionState.Stop) {

			if (mMethod == METHOD_RFID) {
				if ((res = getReader().getRfidUhf().inventory6c()) != ResultCode.NoError) {
					ATLog.e(TAG, "ERROR. action() - Failed to start inventory [%s]", res);
					MessageBox.show(
							getActivity(), String.format(Locale.US, "%s. [%s]",
									getString(R.string.msg_fail_start_inventory), res.getMessage()),
							getString(R.string.title_error));
					getReader().getRfidUhf().stop();
					return;
				}
			} else if (mMethod == METHOD_BARCODE){
				if(mIsBarcodeRestart) {
					mIsBarcodeRestart = false;
				} else {
					if ((res = getReader().getBarcode().startDecode()) != ResultCode.NoError) {
						ATLog.e(TAG, "ERROR. action() - Failed to start decode [%s]", res);
						MessageBox.show(
								getActivity(), String.format(Locale.US, "%s. [%s]",
										getString(R.string.msg_fail_start_decode), res.getMessage()),
								getString(R.string.title_error));
						getReader().getBarcode().stop();
						return;
					}
					
					if(mBarcodeRestartTime > NO_RESTART) {
						mIsBarcodeRestart = true;
					} else {
						mIsBarcodeRestart = false;
					}
				}
			}
			
		} else {
			
			if(mIsBarcodeRestart) {
				if( mMethod == METHOD_BARCODE ) {
					mHandler.removeCallbacks(mRestartBarcodeScanProc);
					if ((res = getReader().getBarcode().stop()) != ResultCode.NoError) {
						ATLog.e(TAG, "ERROR. action() - Failed to stop operation [%s]", res);
						MessageBox.show(
								getActivity(), String.format(Locale.US, "%s. [%s]",
										getString(R.string.msg_fail_stop_action), res.getMessage()),
								getString(R.string.title_error));
						enableWidgets(true);
						return;
					}
				}
				mIsBarcodeRestart = false;
			} else {
				switch (getReader().getAction()) {
				case Decoding:
					if (mMethod == METHOD_BARCODE){
						if ((res = getReader().getBarcode().stop()) != ResultCode.NoError) {
							ATLog.e(TAG, "ERROR. action() - Failed to stop operation [%s]", res);
							MessageBox.show(
									getActivity(), String.format(Locale.US, "%s. [%s]",
											getString(R.string.msg_fail_stop_action), res.getMessage()),
									getString(R.string.title_error));
							enableWidgets(true);
							return;
						}
					}
					break;
				case Inventory6c:
				case InventoryAndDecode:
					if (mMethod == METHOD_RFID) {
						if ((res = getReader().getRfidUhf().stop()) != ResultCode.NoError) {
							ATLog.e(TAG, "ERROR. action() - Failed to stop operation [%s]", res);
							MessageBox.show(
									getActivity(), String.format(Locale.US, "%s. [%s]",
											getString(R.string.msg_fail_stop_action), res.getMessage()),
									getString(R.string.title_error));
							enableWidgets(true);
							return;
						}					
					}
					break;
				}
			}
		}
		ATLog.i(TAG, INFO, "INFO. action() - repeat [%s]", mIsBarcodeRestart);
	}

	private void clear() {

		adpData.clear();
		adpTrigger.clear();
		txtKeyType.setText("");
		txtKeyState.setText("");
		mTotalCount = 0;

		if(mIsRfidTagSpeed){
			mRfidTagCount = 0;
			mRfidTagLastTime = 0;
			txtTotalCount.setText(String.format(Locale.US, "0.0 tps"));
		}

		txtCount.setText(String.format(Locale.US, "%d", adpData.getCount()));
		txtTotalCount.setText(String.format(Locale.US, "%d", mTotalCount));

		ATLog.i(TAG, INFO, "INFO. clear()");
	}

	private void displayOperationMode() {
		switch (mMethod) {
		case METHOD_RFID:
			lstTrigger.setVisibility(View.GONE);
			lstData.setVisibility(View.VISIBLE);
			linearAction.setVisibility(View.VISIBLE);
			linearCount.setVisibility(View.VISIBLE);
			linearTriggerState.setVisibility(View.GONE);
			if(mIsRfidTagSpeed)
				txtTagSpeed.setVisibility(View.VISIBLE);
			break;
		case METHOD_BARCODE:
			lstTrigger.setVisibility(View.GONE);
			lstData.setVisibility(View.VISIBLE);
			linearAction.setVisibility(View.VISIBLE);
			linearCount.setVisibility(View.VISIBLE);
			linearTriggerState.setVisibility(View.GONE);
			txtTagSpeed.setVisibility(View.GONE);
			break;
		case METHOD_TRIGGER:
			lstTrigger.setVisibility(View.VISIBLE);
			lstData.setVisibility(View.GONE);
			linearAction.setVisibility(View.INVISIBLE);
			linearCount.setVisibility(View.GONE);
			linearTriggerState.setVisibility(View.VISIBLE);
			txtTagSpeed.setVisibility(View.GONE);
			break;
		default:
			ATLog.e(TAG, "ERROR. changeOperationMode() - Failed to change display operation mode");
			return;
		}

		ATLog.i(TAG, INFO, "INFO. changeOperationMode()");
	}

	// ------------------------------------------------------------------------
	// Override Reader Event Methods
	// ------------------------------------------------------------------------

	@Override
	public void onReaderActionChanged(ATEAReader reader, ResultCode code, ActionState action, Object params) {


		if (code != ResultCode.NoError) {
			ATLog.e(TAG, "ERROR. onReaderActionChanged([%s], %s, %s) - Failed to action changed [%s]", reader, code,
					action, code);
			enableWidgets(true);
			return;
		}
		if (action == ActionState.Stop) {
			
			txtCount.setText(String.format(Locale.US, "%d", adpData.getCount()));
			txtTotalCount.setText(String.format(Locale.US, "%d", mTotalCount));
			
			if(mIsRfidTagSpeed){
				mRfidTagCount = 0;
				mRfidTagLastTime = 0;
			}

			if(mBarcodeRestartTime > NO_RESTART) {
				if( mIsBarcodeRestart) {
					mHandler.postDelayed(mRestartBarcodeScanProc, mBarcodeRestartTime);
				} 
			}

			if(!mIsBarcodeRestart) {
				btnAction.setText(R.string.action_start);
				enableWidgets(true);
			} 
			
		} else {

			btnAction.setText(R.string.action_stop);
			enableWidgets(true);
		}


		ATLog.i(TAG, INFO, "EVENT. onReaderActionChanged([%s], %s, %s)", reader, code, action);
	}

	@Override
	public void onReaderReadTag(ATEAReader reader, String tag, Object params) {
		float rssi = 0;
		float phase = 0;
		
		long time = System.currentTimeMillis();
		double interval = 0.0;
		double tagSpeed = 0.0;
		
		if (params != null) {
			TagExtParam param = (TagExtParam) params;
			rssi = param.getRssi();
			phase = param.getPhase();
		}
		adpData.add(tag, "", rssi, phase);
		mTotalCount++;
		
		txtCount.setText(String.format(Locale.US, "%d", adpData.getCount()));
		txtTotalCount.setText(String.format(Locale.US, "%d", mTotalCount));

		if(mIsRfidTagSpeed){
			mRfidTagCount++;
			if(mRfidTagLastTime == 0) {
				mRfidTagLastTime = time;
				tagSpeed = 0.0;
			} else {
				interval = (double) ( (time - mRfidTagLastTime) / TPS_TIME_SECOND );
				tagSpeed = (double) mRfidTagCount / interval;
			}
			txtTagSpeed.setText(String.format(Locale.US, "%.2f tps", tagSpeed));

			ATLog.i(TAG, INFO, "EVENT. onReaderReadTag([%s], [%s], [%.2f, %.2f]) - [%.02f tps]"
					, reader, tag, rssi, phase, tagSpeed);
		} else
			ATLog.i(TAG, INFO, "EVENT. onReaderReadTag([%s], [%s], [%.2f, %.2f])", reader, tag, rssi, phase);
	}

	@Override
	public void onReaderAccessResult(ATEAReader reader, ResultCode code, ActionState action, String epc, String data,
			Object params) {
		float rssi = 0;
		float phase = 0;

		if (params != null) {
			TagExtParam param = (TagExtParam) params;
			rssi = param.getRssi();
			phase = param.getPhase();
		}
		ATLog.i(TAG, INFO, "EVENT. onReaderAccessResult([%s], [%s], %s, [%s], [%s], [%.2f, %.2f])", reader, code,
				action, epc, data, rssi, phase);
	}

	@Override
	public void onReaderReadBarcode(ATEAReader reader, BarcodeType type, String codeId, String barcode, Object params) {
		adpData.add(type, codeId, barcode);
		mTotalCount++;

		txtCount.setText(String.format(Locale.US, "%d", adpData.getCount()));
		txtTotalCount.setText(String.format(Locale.US, "%d", mTotalCount));

		ATLog.i(TAG, INFO, "EVENT. onReaderReadBarcode([%s], %s, [%s], [%s])", reader, type, codeId, barcode);
	}

	@Override
	public void onReaderOperationModeChanged(ATEAReader reader, OperationMode mode, Object params) {

		switch (mode) {
		case Normal:
			if (mMethod != METHOD_RFID) {
				mMethod = METHOD_RFID;
				rdMethod.check(R.id.method_rfid);
			}
			break;
		case Barcode:
			if (mMethod != METHOD_BARCODE) {
				mMethod = METHOD_BARCODE;
				rdMethod.check(R.id.method_barcode);
			}
			break;
		case TriggerEventOnly:
			if (mMethod != METHOD_TRIGGER) {
				mMethod = METHOD_TRIGGER;
				rdMethod.check(R.id.method_trigger_event_only);
			}
			break;
			
		default:
			ATLog.e(TAG, "ERROR. onReaderOperationModeChanged([%s], %s) - Failed to unknown operation mode", reader,
					mode);
			break;
		}
		displayOperationMode();
		ATLog.i(TAG, INFO, "EVENT. onReaderOperationModeChanged([%s], %s)", reader, mode);
	}

	@Override
	public void onReaderPowerGainChanged(ATEAReader reader, int power, Object params) {

		ATLog.i(TAG, INFO, "EVENT. onReaderPowerGainChanged([%s], %d)", reader, power);
	}

	@Override
	public void onReaderBatteryState(ATEAReader reader, int batteryState, Object params) {

		ATLog.i(TAG, INFO, "EVENT. onReaderBatteryState([%s], %d)", reader, batteryState);
	}

	@Override
	public void onReaderKeyChanged(ATEAReader reader, KeyType type, KeyState state, 
			Object params) {
		
		if (mMethod == METHOD_TRIGGER) {
			adpTrigger.add(type.toString(), state.toString());
			txtKeyType.setText(type.toString());
			txtKeyState.setText(state.toString());
		}
		
		ATLog.i(TAG, INFO, "EVENT. onReaderKeyChanged([%s], %s, %s)", reader, type, state);
	}
	
	// ------------------------------------------------------------------------
	// Abstractable Methods
	// ------------------------------------------------------------------------

	// Get Inflate Resource Id
	@Override
	protected int getInflateResId() {
		return R.layout.view_inventory;
	}

	// Initialize View
	@Override
	protected void initView() {

		rdMethod = (RadioGroup) mView.findViewById(R.id.method_type);
		rdMethod.setOnCheckedChangeListener(this);

		rdMethodRfid = (RadioButton) mView.findViewById(R.id.method_rfid);
		rdMethodBarcode = (RadioButton) mView.findViewById(R.id.method_barcode);
		rdMethodTrigger = (RadioButton) mView.findViewById(R.id.method_trigger_event_only);
		
		lstData = (ListView) mView.findViewById(R.id.data_list);
		adpData = new DataListAdapter(getActivity());
		lstData.setAdapter(adpData);
		lstData.setOnItemLongClickListener(this);

		lstTrigger = (ListView) mView.findViewById(R.id.trigger_list);
		adpTrigger = new KeyListAdapter(getActivity());
		lstTrigger.setAdapter(adpTrigger);
		
		txtCount = (TextView) mView.findViewById(R.id.count);
		txtTotalCount = (TextView) mView.findViewById(R.id.total_count);
		txtKeyType = (TextView) mView.findViewById(R.id.key_type);
		txtKeyState = (TextView) mView.findViewById(R.id.key_state);
		txtTagSpeed = (TextView) mView.findViewById(R.id.tag_speed);
		if(mIsRfidTagSpeed)
			txtTagSpeed.setVisibility(View.VISIBLE);
		else
			txtTagSpeed.setVisibility(View.GONE);
		
		linearAction = (LinearLayout) mView.findViewById(R.id.linear_action);
		linearCount = (LinearLayout) mView.findViewById(R.id.linear_count);
		linearTriggerState = (LinearLayout) mView.findViewById(R.id.linear_key);

		if(!isATS100()) {
			rdMethodTrigger.setVisibility(View.GONE);
			lstTrigger.setVisibility(View.GONE);
			linearTriggerState.setVisibility(View.GONE);
		}

		btnAction = (Button) mView.findViewById(R.id.action);
		btnAction.setOnClickListener(this);

		btnClear = (Button) mView.findViewById(R.id.clear);
		btnClear.setOnClickListener(this);

		btnSetting = (Button) mView.findViewById(R.id.setting);
		btnSetting.setOnClickListener(this);

		ATLog.i(TAG, INFO, "INFO. initView()");
	}

	// Exit View
	@Override
	protected void exitView() {
		
		ATLog.i(TAG, INFO, "INFO. exitView()");
	}

	// Enabled Widgets
	@Override
	protected void enableWidgets(boolean enabled) {
		super.enableWidgets(enabled);
		rdMethod.setEnabled(mIsEnabled);
		if (getReader() != null) {
			rdMethodRfid.setEnabled(mIsEnabled && getReader().getRfidUhf() != null);
			rdMethodBarcode.setEnabled(mIsEnabled && getReader().getBarcode() != null);
			rdMethodTrigger.setEnabled(mIsEnabled 
					&& getReader().getRfidUhf() != null && getReader().getBarcode() != null);
		}
		lstData.setEnabled(enabled);
		lstTrigger.setEnabled(enabled);
		
		linearAction.setEnabled(mIsEnabled);
		btnAction.setEnabled(enabled);
		btnClear.setEnabled(mIsEnabled);
		btnSetting.setEnabled(mIsEnabled);
		
		ATLog.i(TAG, INFO, "INFO. enableWidgets(%s)", enabled);
	}

	// Loading Reader Properties
	@Override
	protected boolean loadingProperties() {

		mTotalCount = 0;

		// Load Operation Mode
		OperationMode mode;
		try {
			mode = getReader().getOperationMode();
		} catch (ATException e) {
			ATLog.e(TAG, e, "ERROR. loadingProperties() - Failed to load operation mode");
			return false;
		}
		switch (mode) {
		case Normal:
			mMethod = METHOD_RFID;
			break;
		case Barcode:
			mMethod = METHOD_BARCODE;
			break;
		case TriggerEventOnly:
			mMethod = METHOD_TRIGGER;
			break;
		default:
			ATLog.e(TAG, "ERROR. loadingProperties() - Failed to load unknown operation mode");
			return false;
		}
		
		try {
			mIsRfidTagSpeed = (Boolean)GlobalData.getConfig(getActivity().getApplicationContext(),
					getReader().getDeviceType(), getReader().getAddress(), GlobalData.KEY_TAG_SPEED);
		} catch (Exception e1) {
			ATLog.e(TAG, "ERROR. loadingProperties() - Failed to get global data(%s)", 
					GlobalData.KEY_TAG_SPEED);
		}
		
		try {
			mBarcodeRestartTime = (Integer)GlobalData.getConfig(getActivity().getApplicationContext(),
					getReader().getDeviceType(), getReader().getAddress(), GlobalData.KEY_RESTART_TIME);
		} catch (Exception e1) {
			ATLog.e(TAG, "ERROR. loadingProperties() - Failed to get global data(%s)", 
					GlobalData.KEY_RESTART_TIME);
		}

		if(getReader().getRfidUhf() == null) {
			// Enabled Use Key Action
			try {
				getReader().setUseActionKey(true);
			} catch (ATException e) {
				ATLog.e(TAG, "ERROR. loadingProperties() - Failed to enabled key action");
				return false;
			}

			return true;
		}
		
		// Set Continuous Mode
		try {
			getReader().getRfidUhf().setContinuousMode(mIsContinuousMode);
		} catch (ATException e) {
			ATLog.e(TAG, e, "ERROR. loadingProperties() - Failed to set continuous mode [%s]",
					mIsContinuousMode);
			return false;
		}
		
		// Load Continous Mode
		try {
			mIsContinuousMode = getReader().getRfidUhf().getContinuousMode();
		} catch (ATException e) {
			ATLog.e(TAG, e, "ERROR. loadingProperties() - Failed to load continuous mode");
			return false;
		}

		// Load Report Rssi
		try {
			mIsReportRssi = getReader().getRfidUhf().getReportRssi();
		} catch (ATException e) {
			ATLog.e(TAG, e, "ERROR. loadingProperties() - Failed to load report rssi");
			return false;
		}

		// Load Selection Mask
		if (!checkMask()) {
			ATLog.e(TAG, "ERROR. loadingProperties() - Failed to check selection mask");
			return false;
		}
		
		// Enabled Use Key Action
		try {
			getReader().setUseActionKey(true);
		} catch (ATException e) {
			ATLog.e(TAG, "ERROR. loadingProperties() - Failed to enabled key action");
			return false;
		}

		ATLog.i(TAG, INFO, "INFO. loadingProperties()");
		return true;
	}

	// Loaded Reader Properteis
	@Override
	protected void loadedProperties(boolean isInitialize) {

		if (isInitialize) {
			switch (mMethod) {
			case METHOD_RFID:
				rdMethod.check(R.id.method_rfid);
				break;
			case METHOD_BARCODE:
				rdMethod.check(R.id.method_barcode);
				break;
			case METHOD_TRIGGER:
				rdMethod.check(R.id.method_trigger_event_only);
				break;
			}

			txtCount.setText(String.format(Locale.US, "%d", adpData.getCount()));
			txtTotalCount.setText(String.format(Locale.US, "%d", mTotalCount));

			adpData.setReportRssi(mIsReportRssi);

			enableWidgets(true);
		} else {
			enableWidgets(false);
		}
		displayOperationMode();

		ATLog.i(TAG, INFO, "INFO. loadedProperties()");
	}

	private void finishSetSelectMask(boolean result) {

		WaitDialog.hide();

		if (result) {
			enableWidgets(true);
		} else {
			finishView();
		}

		ATLog.i(TAG, INFO, "INFO. finishSetSelectMask(%s)", result);
	}

	@Override
	protected void completeSetting(int type, Intent data) {
		// Load Report Rssi
		try {
			mIsReportRssi = getReader().getRfidUhf().getReportRssi();
		} catch (ATException e) {
			ATLog.e(TAG, e, "ERROR. completeSetting() - Failed to load report rssi");
			return ;
		}
		adpData.setReportRssi(mIsReportRssi);
		
		switch(type){
		case OPTION_RFID_UHF :
			if(data != null) {
				mIsRfidTagSpeed = data.getBooleanExtra(Constants.RFID_TAG_SPEED, false);
				GlobalData.putConfig(
						getReader().getDeviceType(), getReader().getAddress(), GlobalData.KEY_TAG_SPEED, mIsRfidTagSpeed);
			} else {
				try {
					mIsRfidTagSpeed = (Boolean)GlobalData.getConfig(getActivity().getApplicationContext(),
							getReader().getDeviceType(), getReader().getAddress(), GlobalData.KEY_TAG_SPEED);
				} catch (Exception e) {
					ATLog.e(TAG, "ERROR. completeSetting() - Failed to get global data(%s)", 
							GlobalData.KEY_TAG_SPEED);
				}
			}

			if(mIsRfidTagSpeed)
				txtTagSpeed.setVisibility(View.VISIBLE);
			else
				txtTagSpeed.setVisibility(View.GONE);

			break;
		case OPTION_BARCODE :
			if(data != null) {
				mBarcodeRestartTime = data.getIntExtra(Constants.BARCODE_RESTART_TIME, NO_RESTART);
				GlobalData.putConfig(getReader().getDeviceType(),
						 getReader().getAddress(), GlobalData.KEY_RESTART_TIME, mBarcodeRestartTime);
			}
			else  {
				try {
					mBarcodeRestartTime = (Integer)GlobalData.getConfig(getActivity().getApplicationContext(),
							getReader().getDeviceType(), getReader().getAddress(), GlobalData.KEY_RESTART_TIME);
				} catch (Exception e) {
					ATLog.e(TAG, "ERROR. completeSetting() - Failed to get global data(%s)", 
							GlobalData.KEY_RESTART_TIME);
				}
			}
			break;
		}
		
		ATLog.i(TAG, INFO, "INFO. completeSetting()");
	}
	
	private Runnable mRestartBarcodeScanProc = new Runnable() {
		@Override
		public void run() {
			ResultCode res = ResultCode.NoError;
			if ((res = getReader().getBarcode().startDecode()) != ResultCode.NoError) {
				ATLog.e(TAG, "ERROR. $mRestartBarcodeScanProc.run() - Failed to start decode [%s]", res);
				MessageBox.show(
						getActivity(), String.format(Locale.US, "%s. [%s]",
								getString(R.string.msg_fail_start_decode), res.getMessage()),
						getString(R.string.title_error));
				getReader().getBarcode().stop();
				return;
			}
		}
	};
	// ------------------------------------------------------------------------
	// Declare Class SetSelectMaskThread
	// ------------------------------------------------------------------------

	private class SetSelectMaskThread extends Thread {

		private String mData;
		private boolean mRes;

		public SetSelectMaskThread(String data) {
			super();

			mData = data;
			mRes = true;
		}

		@Override
		public void run() {

			ATLog.i(TAG, INFO, "+++ INFO. SetSelectMaskThread.run()");

			SelectMask6cParam param = new SelectMask6cParam(true, Mask6cTarget.SL, Mask6cAction.AB, BankType.EPC,
					MASK_EPC_OFFSET, mData, mData.length() * NIBLE_SIZE);

			try {
				getReader().getRfidUhf().setSelectMask6c(0, param);
			} catch (ATException e) {
				ATLog.e(TAG, e, "ERROR. SetSelectMaskThread.run() - Failed to set selection mack 6c [%s]",
						param.toString());
				mIsUseMask = false;
				mRes = false;
				getActivity().runOnUiThread(mFinish);
				return;
			}
			for (int i = 1; i < MAX_MASK; i++) {
				try {
					getReader().getRfidUhf().setSelectMask6cEnabled(i, false);
				} catch (ATException e) {
					ATLog.e(TAG, e, "ERROR. SetSelectMaskThread.run() - Failed to set selection mack 6c disabled [%d]",
							i);
					mIsUseMask = false;
					mRes = false;
					getActivity().runOnUiThread(mFinish);
					return;
				}
			}
			try {
				getReader().getRfidUhf().setSelectFlag(SelectFlag.SL);
			} catch (ATException e) {
				ATLog.e(TAG, e, "ERROR. SetSelectMaskThread.run() - Failed to set select flag");
				mIsUseMask = false;
				mRes = false;
				getActivity().runOnUiThread(mFinish);
				return;
			}
			try {
				getReader().getRfidUhf().setSessionFlag(SessionFlag.AB);
			} catch (ATException e) {
				ATLog.e(TAG, e, "ERROR. SetSelectMaskThread.run() - Failed to set session flag");
				mIsUseMask = false;
				mRes = false;
				getActivity().runOnUiThread(mFinish);
				return;
			}
			mIsUseMask = true;
			mRes = true;
			getActivity().runOnUiThread(mFinish);

			ATLog.i(TAG, INFO, "--- INFO. SetSelectMaskThread.run()");
		}

		private Runnable mFinish = new Runnable() {

			@Override
			public void run() {
				finishSetSelectMask(mRes);
			}

		};
	}

	// ------------------------------------------------------------------------
	// Declare Interface ISetWriteMemoryListener
	// ------------------------------------------------------------------------

	public interface ISetWriteMemoryListener {
		void onSetWriteMemory(String data);
	}
}
