package com.atid.app.atx.activity;

import java.util.Locale;

import com.atid.app.atx.R;
import com.atid.app.atx.data.Constants;
import com.atid.app.atx.data.GlobalData;
import com.atid.app.atx.dialog.BaseDialog;
import com.atid.app.atx.dialog.EnumListDialog;
import com.atid.app.atx.dialog.FreqTableDialog;
import com.atid.app.atx.dialog.InventoryAlgorithmDialog;
import com.atid.app.atx.dialog.InventorySetDialog;
import com.atid.app.atx.dialog.MessageBox;
import com.atid.app.atx.dialog.NumberUnitDialog;
import com.atid.app.atx.dialog.PowerGainDialog;
import com.atid.app.atx.dialog.SelectionMaskDialog;
import com.atid.app.atx.dialog.WaitDialog;
import com.atid.lib.atx88.ATx88Reader;
import com.atid.lib.diagnostics.ATException;
import com.atid.lib.module.rfid.uhf.params.PowerRange;
import com.atid.lib.module.rfid.uhf.params.SelectMask6cParam;
import com.atid.lib.module.rfid.uhf.types.GlobalBandType;
import com.atid.lib.module.rfid.uhf.types.LinkProfileType;
import com.atid.lib.reader.ATEAReader;
import com.atid.lib.reader.event.IATEAReaderEventListener;
import com.atid.lib.reader.types.KeyState;
import com.atid.lib.reader.types.KeyType;
import com.atid.lib.reader.types.OperationMode;
import com.atid.lib.transport.types.ConnectState;
import com.atid.lib.types.ActionState;
import com.atid.lib.types.DeviceType;
import com.atid.lib.types.ModuleRfidUhfType;
import com.atid.lib.types.ResultCode;
import com.atid.lib.util.diagnotics.ATLog;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;


public class RfidOptionActivity extends Activity 
		implements IATEAReaderEventListener , OnClickListener , OnCheckedChangeListener {
	
	private static final String TAG = RfidOptionActivity.class.getSimpleName();
	private static final int INFO = ATLog.L1;

	public static final int ID = 0x32101000;
	
	private static final int DEFAULT_VALUE = 0;
	
	private static final int LOADING_STATE_READER = 1;
	private static final int LOADING_STATE_RFID_READER = 2;
	private static final int LOADING_STATE_RFID_DISABLE_ACTION_KEY = 3;
	private static final int LOADING_STATE_RFID_GLOBAL_BAND = 4;
	private static final int LOADING_STATE_RFID_POWER_GAIN = 5;
	private static final int LOADING_STATE_RFID_INVENTORY_TIME = 6;
	private static final int LOADING_STATE_RFID_IDLE_TIME = 7;
	private static final int LOADING_STATE_RFID_OPERATION_TIME = 8;
	private static final int LOADING_STATE_RFID_AUTO_SAVE_MODE = 9;
	private static final int LOADING_STATE_RFID_REPORT_RSSI = 10;
	private static final int LOADING_STATE_RFID_SELECT_MASK = 11;
	private static final int LOADING_STATE_RFID_SELECT_FLAG = 12;
	private static final int LOADING_STATE_RFID_SESSION_TARGET = 13;
	private static final int LOADING_STATE_RFID_SESSION_FLAG = 14;
	private static final int LOADING_STATE_RFID_ALGORITHM = 15;
	private static final int LOADING_STATE_RFID_START_Q = 16;
	private static final int LOADING_STATE_RFID_MIN_Q = 17;
	private static final int LOADING_STATE_RFID_MAX_Q = 18;
	private static final int LOADING_STATE_RFID_FREQUENCY_TABLE = 19;
	private static final int LOADING_STATE_RFID_CURRENT_LINK_PROFILE = 20;
	private static final int LOADING_STATE_RFID_DEFAULT_LINK_PROFILE = 21;
	
	// ------------------------------------------------------------------------
	// Member Variable
	// ------------------------------------------------------------------------
	private LinearLayout linearGlobalBand;
	private LinearLayout linearPowerGain;
	private LinearLayout linearInventoryTime;
	private LinearLayout linearIdleTime;
	private LinearLayout linearOperationTime;
	private LinearLayout linearAutoSave;
	private LinearLayout linearReportRssi;
	private LinearLayout linearCurrentLinkProfile;
	private LinearLayout linearDefaultLinkProfile;
	private LinearLayout linearSelectionMask;
	private LinearLayout linearInventorySet;
	private LinearLayout linearInventoryAlgorithm;
	private LinearLayout linearFrequency;
	
	private TextView txtValueGlobalBand;
	private TextView txtValuePowerGain;
	private TextView txtValueInventoryTime;
	private TextView txtValueIdleTime;
	private TextView txtValueOperationTime;
	private Switch swtAutoSaveMode;
	private Switch swtReportRssi;
	private TextView txtValueCurrentLinkProfile;
	private TextView txtValueDefaultLinkProfile;
	
	private ATEAReader mReader;
	
	private GlobalBandType mGlobalBand;
	private PowerGainDialog dlgPowerGain;
	private NumberUnitDialog dlgInventoryTime;
	private NumberUnitDialog dlgIdleTime;
	private NumberUnitDialog dlgOperationTime;
	private boolean mIsAutoSaveMode;
	private boolean mIsReportRssi;
	private EnumListDialog dlgCurrentLinkProfile;
	private EnumListDialog dlgDefaultLinkProfile;
	private SelectionMaskDialog dlgSelectionMask;
	private InventorySetDialog dlgInventorySet;
	private InventoryAlgorithmDialog dlgAlgorithm;
	private FreqTableDialog dlgFreqTable;
	
	ModuleRfidUhfType mModuleRfidUhfType;
	private DeviceType mDeviceType;
	
	private Thread mThread;
	private volatile boolean mIsThreadAlive;
	
	private int mLoadingState;
	private ATException mLoadingError;
	
	private LinearLayout linearTagSpeed;
	private Switch swtTagSpeed;
	private boolean mIsTagSpeed;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rfid_option);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		int position = getIntent().getIntExtra(Constants.SELECTED_READER, DEFAULT_VALUE);
		
		mReader = GlobalData.ReaderManager.get(position);
		mReader.addListener(this);

		mGlobalBand = GlobalBandType.Unknown;
		dlgPowerGain = null;
		dlgInventoryTime = null;
		dlgOperationTime = null;
		dlgIdleTime = null;
		dlgSelectionMask = null;
		dlgInventorySet = null;
		dlgAlgorithm = null;
		dlgFreqTable = null;
		mIsAutoSaveMode = false;
		mIsReportRssi = false;
		mIsTagSpeed = getIntent().getBooleanExtra(Constants.RFID_TAG_SPEED, false);
		mDeviceType = mReader.getDeviceType();
		
		mModuleRfidUhfType = ModuleRfidUhfType.ATR2000S;
		
		initialize();
		
		mIsThreadAlive = false;
		mThread = new Thread(mLoadingProc);
		mThread.start();
		
		WaitDialog.show(this, R.string.msg_initialize_view, new DialogInterface.OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
			
				mIsThreadAlive = false;
				if(mThread.isAlive()){
					try{
						mThread.join();
					}catch(InterruptedException e){
						ATLog.e(TAG, "ERROR. onCreate().onCancel() - Failed to join thread");
					}
				}
				WaitDialog.hide();
				setResult(Activity.RESULT_CANCELED);
				finish();
				ATLog.i(TAG, INFO, "INFO. onCreate().onCancel()");
				return;
			}
		});
		
		ATLog.i(TAG, INFO, "INFO. onCreate()");
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:
			mReader.removeListener(this);
			
			Intent intent = new Intent();
			intent.putExtra(Constants.RFID_TAG_SPEED, mIsTagSpeed);
			setResult(Activity.RESULT_CANCELED , intent);
			finish();
			return true;
		}

		ATLog.i(TAG, INFO, "INFO. onOptionsItemSelected()");
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		
		mReader.removeListener(this);
		WaitDialog.hide();
		
		Intent intent = new Intent();
		intent.putExtra(Constants.RFID_TAG_SPEED, mIsTagSpeed);
		setResult(Activity.RESULT_CANCELED , intent);

		ATLog.i(TAG, INFO, "INFO. onBackPressed()");
		super.onBackPressed();
	}
	
	@Override
	public void onClick(View v) {

		switch(v.getId()){
	    case R.id.linear_rfid_globalband:
	    	break;
	    case R.id.linear_rfid_power_gain :
	    	onRfidUhfPowerGain();
	    	break;
	    case R.id.linear_rfid_inventory_time :
	    	onRfidUhfInventoryTime();
	    	break;
	    case R.id.linear_rfid_idle_time :
	    	onRfidUhfIdleTime();
	    	break;
	    case R.id.linear_rfid_operation_time :
	    	onRfidUhfOperationTime();
	    	break;
	    case R.id.linear_rfid_auto_save :
	    	break;
	    case R.id.linear_rfid_report_rssi :
	    	break;
	    case R.id.linear_rfid_current_link_profile :
	    	onRfidUhfCurrentLinkProfile();
	    	break;
	    case R.id.linear_rfid_default_link_profile :
	    	onRfidUhfDefaultLinkProfile();
	    	break;
	    case R.id.linear_rfid_selection_mask :
	    	onRfidUhfSelectionMask();
	    	break;
	    case R.id.linear_rfid_inventory_set :
	    	onRfidUhfInventorySet();
	    	break;
	    case R.id.linear_rfid_inventory_algorithm :
	    	onRfidUhfAlgorithm();
	    	break;
       	case R.id.linear_rfid_frequency :
       		onRfidUhfFrequency();
       		break;
       	case R.id.linear_rfid_tag_speed :
       		break;
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton view, boolean isChecked) {
		switch(view.getId()) {
		case R.id.swt_auto_save_mode :
			onRfidUhfAutoSaveMode(isChecked);
			break;
			
		case R.id.swt_report_rssi :
			onRfidUhfReportRssi(true);
			break;
		case R.id.swt_tag_speed :
			mIsTagSpeed = isChecked;
			break;
		}
		
	}
	
	@Override
	public void onReaderStateChanged(ATEAReader reader, ConnectState state,
			Object params) {

		ATLog.i(TAG, INFO, "EVENT. onReaderStateChanged([%s], %s)", reader, state);
		
		if(state == ConnectState.Disconnected) {
			mReader.removeListener(this);
			setResult(Activity.RESULT_FIRST_USER);
			finish();
		}
	}

	@Override
	public void onReaderActionChanged(ATEAReader reader, ResultCode code,
			ActionState action, Object params) {
		ATLog.i(TAG, INFO, "EVENT. onReaderActionChanged([%s], %s, %s)", reader, code, action);
	}

	@Override
	public void onReaderOperationModeChanged(ATEAReader reader,
			OperationMode mode, Object params) {
		ATLog.i(TAG, INFO, "EVENT. onReaderOperationModeChanged([%s], %s)", reader, mode);
	}

	@Override
	public void onReaderBatteryState(ATEAReader reader, int batteryState,
			Object params) {
		ATLog.i(TAG, INFO, "EVENT. onReaderBatteryState([%s], %d)", reader, batteryState);
	}

	@Override
	public void onReaderKeyChanged(ATEAReader reader, KeyType type, KeyState state, 
			Object params) {
		ATLog.i(TAG, INFO, "EVENT. onReaderKeyChanged([%s], %s, %s)", reader, type, state);
	}

	// ------------------------------------------------------------------------
	// Internal Widgets Control Methods
	// ------------------------------------------------------------------------
	private void initialize() {
		
		initActivity();
		
		dlgPowerGain = new PowerGainDialog();
		dlgPowerGain.setUnit(getResources().getString(R.string.unit_dbm));
		dlgInventoryTime = new NumberUnitDialog(getResources().getString(R.string.unit_ms));
		dlgOperationTime = new NumberUnitDialog(getResources().getString(R.string.unit_ms));
		dlgIdleTime = new NumberUnitDialog(getResources().getString(R.string.unit_ms));
		dlgSelectionMask = new SelectionMaskDialog();
		dlgInventorySet = new InventorySetDialog();
		dlgAlgorithm = new InventoryAlgorithmDialog();
		dlgFreqTable = new FreqTableDialog();
		dlgCurrentLinkProfile = new EnumListDialog(txtValueCurrentLinkProfile, LinkProfileType.values());
		dlgDefaultLinkProfile = new EnumListDialog(txtValueDefaultLinkProfile, LinkProfileType.values());
		
		ATLog.i(TAG, INFO, "INFO. initActivity()");
	}
	
	private void initActivity() {
		linearGlobalBand = (LinearLayout) findViewById(R.id.linear_rfid_globalband);
		linearGlobalBand.setOnClickListener(this);
		linearPowerGain = (LinearLayout) findViewById(R.id.linear_rfid_power_gain);
		linearPowerGain.setOnClickListener(this);
		linearInventoryTime = (LinearLayout) findViewById(R.id.linear_rfid_inventory_time);
		linearInventoryTime.setOnClickListener(this);
		linearIdleTime = (LinearLayout) findViewById(R.id.linear_rfid_idle_time);
		linearIdleTime.setOnClickListener(this);
		
		linearOperationTime = (LinearLayout) findViewById(R.id.linear_rfid_operation_time);
		linearOperationTime.setOnClickListener(this);
		linearAutoSave = (LinearLayout) findViewById(R.id.linear_rfid_auto_save);
		linearAutoSave.setOnClickListener(this);
		linearReportRssi = (LinearLayout) findViewById(R.id.linear_rfid_report_rssi);
		linearReportRssi.setOnClickListener(this);
		linearCurrentLinkProfile = (LinearLayout) findViewById(R.id.linear_rfid_current_link_profile);
		linearCurrentLinkProfile.setOnClickListener(this);
		linearDefaultLinkProfile = (LinearLayout) findViewById(R.id.linear_rfid_default_link_profile);
		linearDefaultLinkProfile.setOnClickListener(this);
		linearSelectionMask = (LinearLayout) findViewById(R.id.linear_rfid_selection_mask);
		linearSelectionMask.setOnClickListener(this);
		linearInventorySet = (LinearLayout) findViewById(R.id.linear_rfid_inventory_set);
		linearInventorySet.setOnClickListener(this);
		
		linearInventoryAlgorithm = (LinearLayout) findViewById(R.id.linear_rfid_inventory_algorithm);
		linearInventoryAlgorithm.setOnClickListener(this);
		
		linearFrequency = (LinearLayout) findViewById(R.id.linear_rfid_frequency);
		linearFrequency.setOnClickListener(this);
		
		txtValueGlobalBand = (TextView) findViewById(R.id.value_rfid_globalband);
		txtValuePowerGain = (TextView) findViewById(R.id.value_rfid_power_gain);
		txtValueInventoryTime = (TextView) findViewById(R.id.value_rfid_inventory_time);
		txtValueIdleTime = (TextView) findViewById(R.id.value_rfid_idle_time);
		txtValueOperationTime = (TextView) findViewById(R.id.value_rfid_operation_time);
		txtValueCurrentLinkProfile = (TextView) findViewById(R.id.value_rfid_current_link_profile);
		txtValueDefaultLinkProfile = (TextView) findViewById(R.id.value_rfid_default_link_profile);
		
		swtAutoSaveMode = (Switch) findViewById(R.id.swt_auto_save_mode);
		swtAutoSaveMode.setOnCheckedChangeListener(this);
		swtReportRssi = (Switch) findViewById(R.id.swt_report_rssi);
		swtReportRssi.setOnCheckedChangeListener(this);
		
		linearTagSpeed = (LinearLayout) findViewById(R.id.linear_rfid_tag_speed);
		linearTagSpeed.setOnClickListener(this);
		
		swtTagSpeed = (Switch) findViewById(R.id.swt_tag_speed);
		swtTagSpeed.setOnCheckedChangeListener(this);
		
		enableWidget(false);	
		ATLog.i(TAG, INFO, "INFO. initActivity()");
	}

	private void enableWidget(boolean enabled) {
		
		linearGlobalBand.setEnabled(enabled);
		linearPowerGain.setEnabled(enabled);
		linearInventoryTime.setEnabled(enabled);
		linearIdleTime.setEnabled(enabled);
		
		linearOperationTime.setEnabled(enabled);
		linearAutoSave.setEnabled(enabled);
		linearReportRssi.setEnabled(enabled);
		if(mModuleRfidUhfType != ModuleRfidUhfType.ATR500S && mDeviceType != DeviceType.AT188N) {
			linearCurrentLinkProfile.setEnabled(enabled);
			linearDefaultLinkProfile.setEnabled(enabled);
		} else {
			linearCurrentLinkProfile.setEnabled(false);
			linearDefaultLinkProfile.setEnabled(false);
		}
		
		linearSelectionMask.setEnabled(enabled);
		linearInventorySet.setEnabled(enabled);
		
		linearInventoryAlgorithm.setEnabled(enabled);
		linearFrequency.setEnabled(enabled);
		
		linearTagSpeed.setEnabled(enabled);
		
		swtAutoSaveMode.setEnabled(enabled);
		swtReportRssi.setEnabled(enabled);
		swtTagSpeed.setEnabled(enabled);
		
		ATLog.i(TAG, INFO, "INFO. enableWidget(%s)", enabled);
	}
	
	// ------------------------------------------------------------------------
	// Set properties
	// ------------------------------------------------------------------------
	private void onRfidUhfPowerGain() {
		if(mReader == null){
			ATLog.e(TAG, "ERROR. onRfidUhfPowerGain() - Failed to get reader");
			return;
		}
		
		enableWidget(false);
		dlgPowerGain.showDialog(this, R.string.power_gain, new BaseDialog.IValueChangedListener() {
			
			@Override
			public void onValueChanged(BaseDialog dialog) {
				
				try{
					mReader.getRfidUhf().setPower(dlgPowerGain.getValue());
					
					txtValuePowerGain.setText(String.format(Locale.US, "%.1f %s",
						 ((double)dlgPowerGain.getValue()/10.0) , getResources().getString(R.string.unit_dbm)));
					
				} catch (ATException e) {
					ATLog.e(TAG, e, "ERROR. onRfidUhfPowerGain() - Failed to set power gain [%d]",
							dlgPowerGain.getValue());
					
					showMessage(R.string.msg_fail_save_rfid_power_gain);
				} finally {
					enableWidget(true);
				}
			}
		}, new BaseDialog.ICancelListener() {
			
			@Override
			public void onCanceled(BaseDialog dialog) {
				enableWidget(true);
			}
		});
		
		ATLog.i(TAG, INFO, "INFO. onRfidUhfPowerGain()");
	}
	
	private void onRfidUhfInventoryTime() {
		if(mReader == null) {
			ATLog.e(TAG, "ERROR. onRfidUhfInventoryTime() - Failed to get reader");
			return;
		}
		
		enableWidget(false);
		dlgInventoryTime.showDialog(this, R.string.inventory_time, new BaseDialog.IValueChangedListener() {
			
			@Override
			public void onValueChanged(BaseDialog dialog) {
				try{
					mReader.getRfidUhf().setInventoryTime(dlgInventoryTime.getValue());
					
					txtValueInventoryTime.setText(String.format(Locale.US, "%d %s",
							dlgInventoryTime.getValue(), getResources().getString(R.string.unit_ms)));
					
				} catch (ATException e){
					ATLog.e(TAG, e, "ERROR. onRfidUhfInventoryTime() - Failed to set inventory time [%d]",
							dlgInventoryTime.getValue());
					showMessage(R.string.msg_fail_save_rfid_inventory_time);

				} finally {
					enableWidget(true);
				}
			}
		}, new BaseDialog.ICancelListener() {
			
			@Override
			public void onCanceled(BaseDialog dialog) {
				enableWidget(true);
			}
		});
		
		ATLog.i(TAG, INFO, "INFO. onRfidUhfInventoryTime()");
	}
	
	private void onRfidUhfIdleTime() {
		if(mReader == null) {
			ATLog.e(TAG, "ERROR. onRfidUhfIdleTime() - Failed to get reader");
			return;
		}
		
		enableWidget(false);
		dlgIdleTime.showDialog(this, R.string.idle_time, new BaseDialog.IValueChangedListener() {
			
			@Override
			public void onValueChanged(BaseDialog dialog) {
				try{
					mReader.getRfidUhf().setIdleTime(dlgIdleTime.getValue());
					
					txtValueIdleTime.setText(String.format(Locale.US, "%d %s", 
							dlgIdleTime.getValue(), getResources().getString(R.string.unit_ms)));
					
				}catch (ATException e){
					ATLog.e(TAG, e, "ERROR. onRfidUhfIdleTime() - Failed to set idle time [%d]",
							dlgIdleTime.getValue());
					showMessage(R.string.msg_fail_save_rfid_idle_time);

				} finally {
					enableWidget(true);
				}
			}
		}, new BaseDialog.ICancelListener() {
			
			@Override
			public void onCanceled(BaseDialog dialog) {
				enableWidget(true);
			}
		});
		
		ATLog.i(TAG, INFO, "INFO. onRfidUhfIdleTime()");
	}
	
	private void onRfidUhfOperationTime() {
		if(mReader == null) {
			ATLog.e(TAG, "ERROR. onRfidUhfIdleTime() - Failed to get reader");
			return;
		}
		enableWidget(false);
		dlgOperationTime.showDialog(this, R.string.operation_time, new BaseDialog.IValueChangedListener() {
			
			@Override
			public void onValueChanged(BaseDialog dialog) {
				try{
					mReader.getRfidUhf().setOperationTime(dlgOperationTime.getValue());
					txtValueOperationTime.setText(String.format(Locale.US, "%d %s", 
							dlgOperationTime.getValue(), getResources().getString(R.string.unit_ms)));
					
				} catch (ATException e) {
					ATLog.e(TAG, e, "ERROR. onRfidUhfIdleTime() - Failed to set operation time [%d]",
							dlgOperationTime.getValue());
					showMessage(R.string.msg_fail_save_rfid_operation_time);

				} finally {
					enableWidget(true);
				}
			}
		}, new BaseDialog.ICancelListener() {
			
			@Override
			public void onCanceled(BaseDialog dialog) {
				enableWidget(true);
			}
		});
		
	}
	
	private void onRfidUhfCurrentLinkProfile() {
		if(mReader == null ){
			ATLog.e(TAG, "ERROR. onRfidUhfCurrentLinkProfile() - Failed to get reader");
			return;

		}
		enableWidget(false);
		dlgCurrentLinkProfile.showDialog(this, R.string.current_link_profile, new BaseDialog.IValueChangedListener() {
			
			@Override
			public void onValueChanged(BaseDialog dialog) {
				try{
					mReader.getRfidUhf().setCurrentLinkProfile(dlgCurrentLinkProfile.getValue().getCode());
					
				} catch (ATException e) {
					ATLog.e(TAG, e, "ERROR. onRfidUhfCurrentLinkProfile() - Failed to set link profile [%d]",
							dlgCurrentLinkProfile.getValue().getCode());
					showMessage(R.string.msg_fail_save_rfid_current_link_profile);

				} finally {
					enableWidget(true);
				}
			}
		}, new BaseDialog.ICancelListener() {
			
			@Override
			public void onCanceled(BaseDialog dialog) {
				enableWidget(true);
			}
		});

		ATLog.i(TAG, INFO, "INFO. onRfidUhfCurrentLinkProfile()");
	}
	
	private void onRfidUhfDefaultLinkProfile() {
		if(mReader == null ){
			ATLog.e(TAG, "ERROR. onRfidUhfDefaultLinkProfile() - Failed to get reader");
			return;

		}
		enableWidget(false);
		dlgDefaultLinkProfile.showDialog(this, R.string.default_link_profile, new BaseDialog.IValueChangedListener() {
			
			@Override
			public void onValueChanged(BaseDialog dialog) {
				try{
					mReader.getRfidUhf().setDefaultLinkProfile(dlgDefaultLinkProfile.getValue().getCode());
					showMessage(R.string.require_default_link_profile);
					
				} catch (ATException e) {
					ATLog.e(TAG, e, "ERROR. onRfidUhfDefaultLinkProfile() - Failed to set link profile [%d]",
							dlgDefaultLinkProfile.getValue().getCode());
					showMessage(R.string.msg_fail_save_rfid_default_link_profile);

				} finally {
					enableWidget(true);
				}
			}
		}, new BaseDialog.ICancelListener() {
			
			@Override
			public void onCanceled(BaseDialog dialog) {
				enableWidget(true);
			}
		});

		ATLog.i(TAG, INFO, "INFO. onRfidUhfDefaultLinkProfile()");
	}
	
	private void onRfidUhfAutoSaveMode(boolean enabled) {
		if(mReader == null) {
			ATLog.e(TAG, "ERROR. onRfidUhfAutoSaveMode(%s) - Failed to get reader", enabled);
			return;
		}

		enableWidget(false);
		mIsAutoSaveMode = enabled;
		try {
			 ((ATx88Reader) mReader).setAutoSaveMode(mIsAutoSaveMode);
			
		} catch (ATException e) {
			ATLog.e(TAG, e, "ERROR. onRfidUhfAutoSaveMode() - Failed to set auto save mode [%s]",
					enabled);
			showMessage(R.string.msg_fail_save_rfid_auto_save_mode);
		} finally {
			enableWidget(true);
		}
		ATLog.i(TAG, INFO, "INFO. onRfidUhfAutoSaveMode(%s)", enabled);
	}
	
	private void onRfidUhfReportRssi(boolean enabled) {
		if(mReader == null) {
			ATLog.e(TAG, "ERROR. onRfidUhfAlgorithm(%s) - Failed to get reader", enabled);
			return;
		}
		
		enableWidget(false);
		mIsReportRssi = enabled;
		try {
			mReader.getRfidUhf().setReportRssi(mIsReportRssi);
			//mReader.getRfidUhf().setReportRssi(true);
		} catch (ATException e) {
			ATLog.e(TAG, e, "ERROR. onRfidUhfAutoSaveMode() - Failed to set report rssi [%s]",
					enabled);
			showMessage(R.string.msg_fail_save_rfid_report_rssi);
		} finally {
			enableWidget(true);
		}
		ATLog.i(TAG, INFO, "INFO. onRfidUhfReportRssi(%s)", enabled);
	}
	
	private void onRfidUhfSelectionMask() {
		if(mReader == null) {
			ATLog.e(TAG, "ERROR. onRfidUhfSelectionMask() - Failed to get reader");
			return;
		}
		enableWidget(false);
		dlgSelectionMask.showDialog(this, new SelectionMaskDialog.IValueChangedListener() {
			
			@Override
			public void onValueChanged(SelectionMaskDialog dialog) {

				//  Select Mask
				mIsThreadAlive = false;
				enableWidget(false);
				mThread = new Thread(mSaveSelectMaskProc);
				mThread.start();
				
				WaitDialog.show(RfidOptionActivity.this, R.string.msg_saving_select_mask, new DialogInterface.OnCancelListener() {
					
					@Override
					public void onCancel(DialogInterface dialog) {
						mIsThreadAlive = false;
						if(mThread.isAlive()) {
							try{
								mThread.join();
							}catch(InterruptedException e){
								ATLog.e(TAG, "ERROR. onRfidUhfSelectionMask().onCancel() - Failed to join thread");
							}
							WaitDialog.hide();
							setResult(Activity.RESULT_CANCELED);
							finish();
							ATLog.i(TAG, INFO, "INFO. onRfidUhfSelectionMask().onCancel()");
						}
					}
				});
			}
		}, new BaseDialog.ICancelListener() {
			
			@Override
			public void onCanceled(BaseDialog dialog) {
				enableWidget(true);
				
			}
		});
		
		ATLog.i(TAG, INFO, "INFO. onRfidUhfSelectionMask()");
	}
	
	private Runnable mSaveSelectMaskProc = new Runnable() {
		private boolean isResult = true;
		@Override
		public void run() {
			mIsThreadAlive = true;
			
			//  Select Mask
			int max = dlgSelectionMask.getMaxCount();
			for (int i = 0; i < max; i++) {
				if (!mIsThreadAlive) {
					ATLog.i(TAG, INFO, "INFO. $mSaveSelectMaskProc.run() - Canceled");
					break;
				}
				
				ATLog.i(TAG, INFO, "INFO. $mSaveSelectMaskProc.run() - index[%d]", i);
				
				try {
					mReader.getRfidUhf().setSelectMask6c(i, dlgSelectionMask.getSelectionMask(i));
				} catch (Exception e) {
					ATLog.e(TAG, e, "ERROR. $mSaveSelectMaskProc.run() - Failed to set select mask [%d]", i);
					isResult &= false;
				}
			}
			
			WaitDialog.hide();
			if(!isResult)
				showMessage(R.string.msg_fail_save_rfid_select_mask);
			
			runOnUiThread( new Runnable () {
				@Override
				public void run() {
					enableWidget(true);		
				}
			});
			
			ATLog.i(TAG, INFO, "INFO. $mSaveSelectMaskProc.run()");
		}
	};
	
	private void onRfidUhfInventorySet() {
		if(mReader == null) {
			ATLog.e(TAG, "ERROR. onRfidUhfAlgorithm() - Failed to get reader");
			return;
		}
		enableWidget(false);
		dlgInventorySet.showDialog(this, new InventorySetDialog.IValueChangedListener() {
			
			@Override
			public void onValueChanged(InventorySetDialog dialog) {

				// Select Flag
				try{
					mReader.getRfidUhf().setSelectFlag(dlgInventorySet.getSelectFlag());
					
				}catch (ATException e){
					ATLog.e(TAG, e, "ERROR. onRfidUhfInventorySet() - Failed to set select flag [%s]",
							dlgInventorySet.getSelectFlag().toString());
					showMessage(R.string.msg_fail_save_rfid_select_flag);
				}

				// Session Target
				try{
					mReader.getRfidUhf().setSessionTarget(dlgInventorySet.getSessionTarget());
					
				}catch (ATException e){
					ATLog.e(TAG, e, "ERROR. onRfidUhfInventorySet() - Failed to set session target [%s]",
							dlgInventorySet.getSessionTarget().toString());
					showMessage(R.string.msg_fail_save_rfid_session_target);
				}

				// Session Flag
				try{
					mReader.getRfidUhf().setSessionFlag(dlgInventorySet.getSessionFlag());
					
				}catch (ATException e){
					ATLog.e(TAG, e, "ERROR. onRfidUhfInventorySet() - Failed to set session flag [%s]",
							dlgInventorySet.getSessionFlag().toString());
					showMessage(R.string.msg_fail_save_rfid_session_flag);
				}

				enableWidget(true);
			}
		}, new BaseDialog.ICancelListener() {
			
			@Override
			public void onCanceled(BaseDialog dialog) {
				enableWidget(true);
				
			}
		});
		
		ATLog.i(TAG, INFO, "INFO. onRfidUhfInventorySet()");
	}
	
	private void onRfidUhfAlgorithm() {
		if(mReader == null) {
			ATLog.e(TAG, "ERROR. onRfidUhfAlgorithm() - Failed to get reader");
			return;
		}
		
		enableWidget(false);
		dlgAlgorithm.showDialog(this, new InventoryAlgorithmDialog.IValueChangedListener() {
			@Override
			public void onValueChanged(InventoryAlgorithmDialog dialog) {
				
				try{
					mReader.getRfidUhf().setAlgorithmType(dlgAlgorithm.getAlgorithm());
					
				}catch (ATException e){
					ATLog.e(TAG, e, "ERROR. onRfidUhfAlgorithm() - Failed to set algorithm type [%s]",
							dlgAlgorithm.getAlgorithm());
					showMessage(R.string.msg_fail_save_rfid_algorithm);
				}

				
				try{
					mReader.getRfidUhf().setStartQ(dlgAlgorithm.getStartQ());
					
				}catch (ATException e){
					ATLog.e(TAG, e, "ERROR. onRfidUhfAlgorithm() - Failed to set start Q [%d]",
							dlgAlgorithm.getStartQ());
					showMessage(R.string.msg_fail_save_rfid_start_q);
				}

				try{
					mReader.getRfidUhf().setMinQ(dlgAlgorithm.getMinQ());
					
				}catch (ATException e){
					ATLog.e(TAG, e, "ERROR. onRfidUhfAlgorithm() - Failed to set min Q [%d]",
							dlgAlgorithm.getMinQ());
					showMessage(R.string.msg_fail_save_rfid_min_q);
				}

				try{
					mReader.getRfidUhf().setMaxQ(dlgAlgorithm.getMaxQ());
					
				}catch (ATException e){
					ATLog.e(TAG, e, "ERROR. onRfidUhfAlgorithm() - Failed to set max Q [%d]",
							dlgAlgorithm.getMaxQ());
					showMessage(R.string.msg_fail_save_rfid_max_q);
				}

				enableWidget(true);
			}
		}, new BaseDialog.ICancelListener() {
			
			@Override
			public void onCanceled(BaseDialog dialog) {
				enableWidget(true);
			}
		});
		
		ATLog.i(TAG, INFO, "INFO. onRfidUhfAlgorithm()");
	}
	
	private void onRfidUhfFrequency() {
		if(mReader == null) {
			ATLog.e(TAG, "ERROR. onRfidUhfFrequency() - Failed to get reader");
			return;
		}
		enableWidget(true);
		dlgFreqTable.showDialog(this, new BaseDialog.ICancelListener() {
			
			@Override
			public void onCanceled(BaseDialog dialog) {
				enableWidget(true);
			}
		});
		
		ATLog.i(TAG, INFO, "INFO. onRfidUhfFrequency()");
	}
	
	private void showMessage(final int message) {
		runOnUiThread( new Runnable() {
			@Override
			public void run() {
				MessageBox.show(RfidOptionActivity.this, message);
			}
		});
		
	}
	// ------------------------------------------------------------------------
	// Load properties
	// ------------------------------------------------------------------------

	private Runnable mLoadingProc = new Runnable() {
		@Override
		public void run() {
			
			mIsThreadAlive = true;
			
			mLoadingState = LOADING_STATE_READER;
			if(mReader == null){
				ATLog.e(TAG, "ERROR. $mLoadingProc.run() - Failed to get reader");
				mIsThreadAlive = false;
				RfidOptionActivity.this.runOnUiThread(mFailedLoadProc);
				return ;
			}

			mLoadingState = LOADING_STATE_RFID_READER;
			if(mReader.getRfidUhf() == null){
				ATLog.e(TAG, "ERROR. $mLoadingProc.run() - Failed to get RFID UHF reader");
				mIsThreadAlive = false;
				RfidOptionActivity.this.runOnUiThread(mFailedLoadProc);
				return ;
			}

			mLoadingState = LOADING_STATE_RFID_DISABLE_ACTION_KEY;
			if(mIsThreadAlive){
				// Disable Action Key
				try {
					mReader.setUseActionKey(false);
				} catch (ATException e) {
					mLoadingError = new ATException(e.getCode());
					ATLog.e(TAG, e,"ERROR. $mLoadingProc.run() - Failed to disabled key action");
					mIsThreadAlive = false;
					RfidOptionActivity.this.runOnUiThread(mFailedLoadProc);
					return ;
				}
			} else {
				runOnUiThread(mLoadedProc);
				ATLog.i(TAG, INFO, "INFO. $mLoadingProc.run() - Canceled");
				return;
			}

			if(mIsThreadAlive){
				mModuleRfidUhfType = mReader.getRfidUhf().getType();
			} else {
				runOnUiThread(mLoadedProc);
				ATLog.i(TAG, INFO, "INFO. $mLoadingProc.run() - Canceled");
				return;
			}
			
			ATLog.i(TAG, INFO, "INFO. $mLoadingProc.run() - mModuleRfidUhfType [%s]", mModuleRfidUhfType);
			
			mLoadingState = LOADING_STATE_RFID_GLOBAL_BAND;
			if(mIsThreadAlive){
				// Global Band
				try {
					mGlobalBand = mReader.getRfidUhf().getGlobalBand();
				} catch (ATException e) {
					mLoadingError = new ATException(e.getCode());
					ATLog.e(TAG, e,"ERROR. $mLoadingProc.run() - Failed to load global band");
					mIsThreadAlive = false;
					RfidOptionActivity.this.runOnUiThread(mFailedLoadProc);
					return ;
				}
			} else {
				runOnUiThread(mLoadedProc);
				ATLog.i(TAG, INFO, "INFO. $mLoadingProc.run() - Canceled");
				return;
			}

			mLoadingState = LOADING_STATE_RFID_POWER_GAIN;
			if(mIsThreadAlive){
				// power gain
				try {
					PowerRange powerRange = mReader.getRfidUhf().getPowerRange();
					int power = mReader.getRfidUhf().getPower();
					dlgPowerGain.setPowerGainRange(powerRange);
					if(power > powerRange.getMax() ) 
						dlgPowerGain.setValue(powerRange.getMax());
					else
						dlgPowerGain.setValue(power);
					
				} catch (ATException e) {
					mLoadingError = new ATException(e.getCode());
					ATLog.e(TAG, e, "ERROR. $mLoadingProc.run() - Failed to load power");
					mIsThreadAlive = false;
					RfidOptionActivity.this.runOnUiThread(mFailedLoadProc);
					return ;
				}
			} else {
				runOnUiThread(mLoadedProc);
				ATLog.i(TAG, INFO, "INFO. $mLoadingProc.run() - Canceled");
				return;
			}

			mLoadingState = LOADING_STATE_RFID_INVENTORY_TIME;
			if(mIsThreadAlive){
				// Inventory Time
				try {
					dlgInventoryTime.setValue(mReader.getRfidUhf().getInventoryTime());
				} catch (ATException e) {
					mLoadingError = new ATException(e.getCode());
					ATLog.e(TAG, e, "ERROR. $mLoadingProc.run() - Failed to load inventory time");
					mIsThreadAlive = false;
					RfidOptionActivity.this.runOnUiThread(mFailedLoadProc);
					return ;
				}
			} else {
				runOnUiThread(mLoadedProc);
				ATLog.i(TAG, INFO, "INFO. $mLoadingProc.run() - Canceled");
				return;
			}

			mLoadingState = LOADING_STATE_RFID_IDLE_TIME;
			if(mIsThreadAlive){
				// Idle Time
				try {
					dlgIdleTime.setValue(mReader.getRfidUhf().getIdleTime());
				} catch (ATException e) {
					mLoadingError = new ATException(e.getCode());
					ATLog.e(TAG, e, "ERROR. $mLoadingProc.run() - Failed to load idle time");
					mIsThreadAlive = false;
					RfidOptionActivity.this.runOnUiThread(mFailedLoadProc);
					return ;
				}
			} else {
				runOnUiThread(mLoadedProc);
				ATLog.i(TAG, INFO, "INFO. $mLoadingProc.run() - Canceled");
				return;
			}
			
			mLoadingState = LOADING_STATE_RFID_OPERATION_TIME;
			if(mIsThreadAlive){
				// Load Operation Time
				try {
					int value = mReader.getRfidUhf().getOperationTime();
					dlgOperationTime.setValue(value);
				} catch (ATException e) {
					mLoadingError = new ATException(e.getCode());
					ATLog.e(TAG, e, "ERROR. $mLoadingProc.run() - Failed to load operation time");
					mIsThreadAlive = false;
					RfidOptionActivity.this.runOnUiThread(mFailedLoadProc);
					return ;

				}
			} else {
				runOnUiThread(mLoadedProc);
				ATLog.i(TAG, INFO, "INFO. $mLoadingProc.run() - Canceled");
				return;
			}

			mLoadingState = LOADING_STATE_RFID_AUTO_SAVE_MODE;
			if(mIsThreadAlive) {
				// Load Auto Save Mode
				try {
					mIsAutoSaveMode = ((ATx88Reader) mReader).getAutoSaveMode();	
				} catch (ATException e) {
					mLoadingError = new ATException(e.getCode());
					ATLog.e(TAG, e, "ERROR. $mLoadingProc.run() - Failed to load auto save mode");
					mIsThreadAlive = false;
					RfidOptionActivity.this.runOnUiThread(mFailedLoadProc);
					return ;
				}
				
			} else {
				runOnUiThread(mLoadedProc);
				ATLog.i(TAG, INFO, "INFO. $mLoadingProc.run() - Canceled");
				return;
			}
			
			mLoadingState = LOADING_STATE_RFID_REPORT_RSSI;
			if(mIsThreadAlive) {
				// Load Report Rssi

				try {
					mIsReportRssi = mReader.getRfidUhf().getReportRssi();


				} catch (ATException e) {
					mLoadingError = new ATException(e.getCode());
					ATLog.e(TAG, e, "ERROR. $mLoadingProc.run() - Failed to load report rssi");
					mIsThreadAlive = false;
					RfidOptionActivity.this.runOnUiThread(mFailedLoadProc);
					return ;
				}

			} else {
				runOnUiThread(mLoadedProc);
				ATLog.i(TAG, INFO, "INFO. $mLoadingProc.run() - Canceled");
				return;
			}			
			
			mLoadingState = LOADING_STATE_RFID_SELECT_MASK;
			if(mIsThreadAlive){
				//  Select Mask
				int max = dlgSelectionMask.getMaxCount();
				for (int i = 0; i < max; i++) {
					
					try {
						SelectMask6cParam param = mReader.getRfidUhf().getSelectMask6c(i);
						try {
							dlgSelectionMask.setSelectionMask(i, param);
						} catch (Exception e) {
							ATLog.e(TAG, e, "ERROR. $mLoadingProc.run() - Failed to set dialog select mask [%d]", i);
						}
					
					} catch (ATException e) {
						mLoadingError = new ATException(e.getCode());
						ATLog.e(TAG, e, "ERROR. $mLoadingProc.run() - Failed to load select mask [%d]", i);
						mIsThreadAlive = false;
						RfidOptionActivity.this.runOnUiThread(mFailedLoadProc);
						return ;
					}
				}
			} else {
				runOnUiThread(mLoadedProc);
				ATLog.i(TAG, INFO, "INFO. $mLoadingProc.run() - Canceled");
				return;
			}

			mLoadingState = LOADING_STATE_RFID_SELECT_FLAG;
			if(mIsThreadAlive){
				//  Select Flag
				try {
					dlgInventorySet.setSelectFlag(mReader.getRfidUhf().getSelectFlag());
				} catch (ATException e) {
					mLoadingError = new ATException(e.getCode());
					ATLog.e(TAG, e, "ERROR. $mLoadingProc.run() - Failed to load select flag");
					mIsThreadAlive = false;
					RfidOptionActivity.this.runOnUiThread(mFailedLoadProc);
					return ;
				}
			} else {
				runOnUiThread(mLoadedProc);
				ATLog.i(TAG, INFO, "INFO. $mLoadingProc.run() - Canceled");
				return;
			}
			
			mLoadingState = LOADING_STATE_RFID_SESSION_TARGET;
			if(mIsThreadAlive){
				//  Session Target
				try {
					dlgInventorySet.setSessionTarget(mReader.getRfidUhf().getSessionTarget());
				} catch (ATException e) {
					mLoadingError = new ATException(e.getCode());
					ATLog.e(TAG, e, "ERROR. $mLoadingProc.run() - Failed to load session target");
					mIsThreadAlive = false;
					RfidOptionActivity.this.runOnUiThread(mFailedLoadProc);
					return ;
				}
			} else {
				runOnUiThread(mLoadedProc);
				ATLog.i(TAG, INFO, "INFO. $mLoadingProc.run() - Canceled");
				return;
			}
			
			mLoadingState = LOADING_STATE_RFID_SESSION_FLAG;
			if(mIsThreadAlive){
				//  Session Flag
				try {
					dlgInventorySet.setSessionFlag(mReader.getRfidUhf().getSessionFlag());
				} catch (ATException e) {
					mLoadingError = new ATException(e.getCode());
					ATLog.e(TAG, e, "ERROR. $mLoadingProc.run() - Failed to load session flag");
					mIsThreadAlive = false;
					RfidOptionActivity.this.runOnUiThread(mFailedLoadProc);
					return ;
				}
			} else {
				runOnUiThread(mLoadedProc);
				ATLog.i(TAG, INFO, "INFO. $mLoadingProc.run() - Canceled");
				return;
			}
			
			mLoadingState = LOADING_STATE_RFID_ALGORITHM;
			if(mIsThreadAlive){
				// Algorithm
				try {
					dlgAlgorithm.setAlgorithm(mReader.getRfidUhf().getAlgorithmType());
				} catch (ATException e) {
					mLoadingError = new ATException(e.getCode());
					ATLog.e(TAG, e, "ERROR. $mLoadingProc.run() - Failed to load algorithm");
					mIsThreadAlive = false;
					RfidOptionActivity.this.runOnUiThread(mFailedLoadProc);
					return ;
				}
			} else {
				runOnUiThread(mLoadedProc);
				ATLog.i(TAG, INFO, "INFO. $mLoadingProc.run() - Canceled");
				return;
			}

			mLoadingState = LOADING_STATE_RFID_START_Q;
			if(mIsThreadAlive){
				// Start Q
				try {
					dlgAlgorithm.setStartQ(mReader.getRfidUhf().getStartQ());
				} catch (ATException e) {
					mLoadingError = new ATException(e.getCode());
					ATLog.e(TAG, e, "ERROR. $mLoadingProc.run() - Failed to load start Q");
					mIsThreadAlive = false;
					RfidOptionActivity.this.runOnUiThread(mFailedLoadProc);
					return ;
				}
			} else {
				runOnUiThread(mLoadedProc);
				ATLog.i(TAG, INFO, "INFO. $mLoadingProc.run() - Canceled");
				return;
			}

			mLoadingState = LOADING_STATE_RFID_MIN_Q;
			if(mIsThreadAlive){
				// Min Q
				try {
					dlgAlgorithm.setMinQ(mReader.getRfidUhf().getMinQ());
				} catch (ATException e) {
					mLoadingError = new ATException(e.getCode());
					ATLog.e(TAG, e, "ERROR. $mLoadingProc.run() - Failed to load min Q");
					mIsThreadAlive = false;
					RfidOptionActivity.this.runOnUiThread(mFailedLoadProc);
					return ;
				}
			} else {
				runOnUiThread(mLoadedProc);
				ATLog.i(TAG, INFO, "INFO. $mLoadingProc.run() - Canceled");
				return;
			}

			mLoadingState = LOADING_STATE_RFID_MAX_Q;
			if(mIsThreadAlive){
				// Max Q
				try {
					dlgAlgorithm.setMaxQ(mReader.getRfidUhf().getMaxQ());
				} catch (ATException e) {
					mLoadingError = new ATException(e.getCode());
					ATLog.e(TAG, e, "ERROR. $mLoadingProc.run() - Failed to load max Q");
					mIsThreadAlive = false;
					RfidOptionActivity.this.runOnUiThread(mFailedLoadProc);
					return ;
				}
				
			} else {
				runOnUiThread(mLoadedProc);
				ATLog.i(TAG, INFO, "INFO. $mLoadingProc.run() - Canceled");
				return;
			}

			mLoadingState = LOADING_STATE_RFID_FREQUENCY_TABLE;
			if(mIsThreadAlive){
				// Frequency Table
				try {
					dlgFreqTable.setTable(mReader.getRfidUhf().getFreqTable());
				} catch (ATException e) {
					mLoadingError = new ATException(e.getCode());
					ATLog.e(TAG, e, "ERROR. $mLoadingProc.run() - Failed to load frequency table");
					mIsThreadAlive = false;
					RfidOptionActivity.this.runOnUiThread(mFailedLoadProc);
					return ;
				}
			} else {
				runOnUiThread(mLoadedProc);
				ATLog.i(TAG, INFO, "INFO. $mLoadingProc.run() - Canceled");
				return;
			}

			mLoadingState = LOADING_STATE_RFID_CURRENT_LINK_PROFILE;
			if(mIsThreadAlive){
				// Temporarily Link Profile 
				try {
					dlgCurrentLinkProfile.setValue(LinkProfileType.valueOf(mReader.getRfidUhf().getCurrentLinkProfile()));
				} catch (ATException e) {
					mLoadingError = new ATException(e.getCode());
					ATLog.e(TAG, e, "ERROR. $mLoadingProc.run() - Failed to load current link profile");
					mIsThreadAlive = false;
					RfidOptionActivity.this.runOnUiThread(mFailedLoadProc);
					return ;
				}
			} else {
				runOnUiThread(mLoadedProc);
				ATLog.i(TAG, INFO, "INFO. $mLoadingProc.run() - Canceled");
				return;
			}
			
			mLoadingState = LOADING_STATE_RFID_DEFAULT_LINK_PROFILE;
			if(mIsThreadAlive){
				// Permanently Link Profile 
				try {
					dlgDefaultLinkProfile.setValue(LinkProfileType.valueOf(mReader.getRfidUhf().getDefaultLinkProfile()));
				} catch (ATException e) {
					mLoadingError = new ATException(e.getCode());
					ATLog.e(TAG, e, "ERROR. $mLoadingProc.run() - Failed to load default link profile");
					mIsThreadAlive = false;
					RfidOptionActivity.this.runOnUiThread(mFailedLoadProc);
					return ;
				}
			} else {
				runOnUiThread(mLoadedProc);
				ATLog.i(TAG, INFO, "INFO. $mLoadingProc.run() - Canceled");
				return;
			}

			runOnUiThread(mLoadedProc);				
			ATLog.i(TAG, INFO, "INFO. $mLoadingProc.run()");
		}
	};
	
	private Runnable mLoadedProc = new Runnable() {
		@Override
		public void run() {
			
			if(mIsThreadAlive){
				txtValueGlobalBand.setText(mGlobalBand.toString());
				
				txtValuePowerGain.setText(String.format(Locale.US, "%.1f %s",
					 ((double)dlgPowerGain.getValue()/10.0) , getResources().getString(R.string.unit_dbm)));
				
				txtValueInventoryTime.setText(String.format(Locale.US, "%d %s",
						dlgInventoryTime.getValue(), getResources().getString(R.string.unit_ms)));
				
				txtValueIdleTime.setText(String.format(Locale.US, "%d %s", 
						dlgIdleTime.getValue(), getResources().getString(R.string.unit_ms)));
				
				txtValueOperationTime.setText(String.format(Locale.US, "%d %s",
						dlgOperationTime.getValue(), getResources().getString(R.string.unit_ms)));
				
				swtAutoSaveMode.setChecked(mIsAutoSaveMode);
				//swtReportRssi.setChecked(true);
				swtReportRssi.setChecked(mIsReportRssi);
				
				txtValueCurrentLinkProfile.setText(
						String.format(Locale.US, "%s", dlgCurrentLinkProfile.getValue().toString()));

				txtValueDefaultLinkProfile.setText(
						String.format(Locale.US, "%s", dlgDefaultLinkProfile.getValue().toString()));

				swtTagSpeed.setChecked(mIsTagSpeed);
				
				mIsThreadAlive = false;
			}
		
			enableWidget(true);
			WaitDialog.hide();
			ATLog.i(TAG, INFO, "INFO. $mLoadedProc.run()");
		}
	};

	private Runnable mFailedLoadProc = new Runnable() {
		int failMessage;
		String message;
		
		@Override
		public void run() {
			WaitDialog.hide();
			enableWidget(false);
			
			switch(mLoadingState){
			case LOADING_STATE_READER :
				failMessage = R.string.msg_fail_load_reader_instance;
				break;
			case LOADING_STATE_RFID_READER :
				failMessage = R.string.msg_fail_load_rfid_reader_instance;
				break;
			case LOADING_STATE_RFID_DISABLE_ACTION_KEY :
				failMessage = R.string.msg_fail_load_disabled_action_key;
				break;
			case LOADING_STATE_RFID_GLOBAL_BAND :
				failMessage = R.string.msg_fail_load_rfid_global_band;
				break;
			case LOADING_STATE_RFID_POWER_GAIN :
				failMessage = R.string.msg_fail_load_rfid_power_gain;
				break;
			case LOADING_STATE_RFID_INVENTORY_TIME :
				failMessage = R.string.msg_fail_load_rfid_inventory_time;
				break;
			case LOADING_STATE_RFID_IDLE_TIME :
				failMessage = R.string.msg_fail_load_rfid_idle_time;
				break;
			case LOADING_STATE_RFID_OPERATION_TIME :
				failMessage = R.string.msg_fail_load_rfid_operation_time;
				break;
			case LOADING_STATE_RFID_AUTO_SAVE_MODE :
				failMessage = R.string.msg_fail_load_rfid_auto_save_mode;
				break;
			case LOADING_STATE_RFID_REPORT_RSSI :
				failMessage = R.string.msg_fail_load_rfid_report_rssi;
				break;
			case LOADING_STATE_RFID_SELECT_MASK :
				failMessage = R.string.msg_fail_load_rfid_select_mask;
				break;
			case LOADING_STATE_RFID_SELECT_FLAG :
				failMessage = R.string.msg_fail_load_rfid_select_flag;
				break;
			case LOADING_STATE_RFID_SESSION_TARGET :
				failMessage = R.string.msg_fail_load_rfid_session_target;
				break;
			case LOADING_STATE_RFID_SESSION_FLAG :
				failMessage = R.string.msg_fail_load_rfid_session_flag;
				break;
			case LOADING_STATE_RFID_ALGORITHM :
				failMessage = R.string.msg_fail_load_rfid_algorithm;
				break;
			case LOADING_STATE_RFID_START_Q :
				failMessage = R.string.msg_fail_load_rfid_start_q;
				break;
			case LOADING_STATE_RFID_MIN_Q :
				failMessage = R.string.msg_fail_load_rfid_min_q;
				break;
			case LOADING_STATE_RFID_MAX_Q :
				failMessage = R.string.msg_fail_load_rfid_max_q;
				break;
			case LOADING_STATE_RFID_FREQUENCY_TABLE :
				failMessage = R.string.msg_fail_load_rfid_frequency_table;
				break;
			case LOADING_STATE_RFID_CURRENT_LINK_PROFILE :
				failMessage = R.string.msg_fail_load_rfid_current_link_profile;
				break;
			case LOADING_STATE_RFID_DEFAULT_LINK_PROFILE :
				failMessage = R.string.msg_fail_load_rfid_default_link_profile;
				break;
			}
			
			if( mLoadingState == LOADING_STATE_READER || 
					mLoadingState == LOADING_STATE_RFID_READER ) {
				message = String.format(Locale.US, "%s\r\n",
						getResources().getString(failMessage));
			} else {
				message = String.format(Locale.US, "%s\r\nError[%s]",
						getResources().getString(failMessage), mLoadingError.getCode());
			}
			
			ATLog.i(TAG, INFO, "INFO. $mFailedLoadProc.run()");
			MessageBox.show(RfidOptionActivity.this, message, R.string.title_error,
					android.R.drawable.ic_menu_info_details, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							mReader.removeListener(RfidOptionActivity.this);
							setResult(Activity.RESULT_CANCELED);
							RfidOptionActivity.this.finish();
						}
					});
		}
	};

}