package com.atid.app.atx.activity.view.fragment;

import java.util.Locale;

import com.atid.app.atx.R;
import com.atid.app.atx.dialog.BaseDialog;
import com.atid.app.atx.dialog.MessageBox;
import com.atid.app.atx.dialog.PasswordDialog;
import com.atid.lib.diagnostics.ATException;
import com.atid.lib.module.barcode.types.BarcodeType;
import com.atid.lib.module.rfid.uhf.params.Lock6cParam;
import com.atid.lib.module.rfid.uhf.params.PermaLock6cParam;
import com.atid.lib.module.rfid.uhf.params.TagExtParam;
import com.atid.lib.reader.ATEAReader;
import com.atid.lib.reader.types.KeyState;
import com.atid.lib.reader.types.KeyType;
import com.atid.lib.reader.types.OperationMode;
import com.atid.lib.types.ActionState;
import com.atid.lib.types.ResultCode;
import com.atid.lib.util.diagnotics.ATLog;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

public class AccessLockMemory extends Fragment implements OnClickListener ,
		OnCheckedChangeListener{
	private static final String TAG = AccessLockMemory.class.getSimpleName();
	private static final int INFO = ATLog.L2;
	
	private static final int LOCK_TYPE_UNKNOWN = 0;
	private static final int LOCK_TYPE_LOCK = 1;
	private static final int LOCK_TYPE_UNLOCK = 2;
	private static final int LOCK_TYPE_PERMA_LOCK = 3;
	
	private TextView txtResult;
	private TextView txtData;
	private TextView txtRssi;
	private TextView txtPhase;
	
	private ToggleButton tbKillPassword;
	private ToggleButton tbAccessPassword;
	private ToggleButton tbEpc;
	private ToggleButton tbTid;
	private ToggleButton tbUser;
	
	private TextView txtPassword;
	
	private RadioGroup rgLockType;
	private RadioButton rdLock;
	private RadioButton rdUnlock;
	private RadioButton rdPermaLock;
	
	private PasswordDialog dlgPassword;
	
	private View mView;
	
	private ATEAReader mReader;
	
	private int mLockType;
	private volatile boolean mIsAction;
	private boolean mIsInitialize;
	
	public AccessLockMemory(ATEAReader reader , boolean isInitialize) {
		if(reader != null)
			mReader = reader;
		
		dlgPassword = null;
		
		mLockType = LOCK_TYPE_UNKNOWN;
		mIsAction = false;
		mIsInitialize = isInitialize;
		
		ATLog.i(TAG, INFO, "INFO. AccessLockMemory()");			
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_access_lock_memory, container, false);
		
		initialize();
		
		loadedProperties();

		ATLog.i(TAG, INFO, "INFO. onCreateView()");	
		return mView;
	}
	
	private void initialize() {
		
		initWidget();
		
		dlgPassword = new PasswordDialog(txtPassword);
		
		clear();
		
		ATLog.i(TAG, INFO, "INFO. initialize()");
	}
	
	private void initWidget() {
		txtResult = (TextView) mView.findViewById(R.id.result);
		txtData = (TextView) mView.findViewById(R.id.data);
		txtRssi = (TextView) mView.findViewById(R.id.rssi);
		txtPhase = (TextView) mView.findViewById(R.id.phase);
		
		tbKillPassword = (ToggleButton) mView.findViewById(R.id.tb_kill_password);
		tbAccessPassword = (ToggleButton) mView.findViewById(R.id.tb_access_password);
		tbEpc = (ToggleButton) mView.findViewById(R.id.tb_epc);
		tbTid = (ToggleButton) mView.findViewById(R.id.tb_tid);
		tbUser = (ToggleButton) mView.findViewById(R.id.tb_user);
		
		txtPassword = (TextView) mView.findViewById(R.id.password);
		txtPassword.setOnClickListener(this);
		
		rgLockType = (RadioGroup) mView.findViewById(R.id.lock_select_type);
		rgLockType.setOnCheckedChangeListener(this);
		
		rdLock = (RadioButton) mView.findViewById(R.id.type_lock);
		rdUnlock = (RadioButton) mView.findViewById(R.id.type_unlock);
		rdPermaLock = (RadioButton) mView.findViewById(R.id.type_permalock);
		
		ATLog.i(TAG, INFO, "INFO. initWidget()");
	}

	
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.password:
			dlgPassword.showDialog(getActivity(), R.string.password, new BaseDialog.IValueChangedListener() {
				
				@Override
				public void onValueChanged(BaseDialog dialog) {
					try {
						mReader.getRfidUhf().setAccessPassword(((PasswordDialog)dialog).getValue());
					} catch (ATException e) {
						ATLog.e(TAG, e, "ERROR. onClick(R.id.password).$onValueChanged() - Failed to save access password");
					}
				}
			});
			ATLog.i(TAG, INFO, "INFO. onClick(R.id.password)");
			break;
		}
	}
	
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		String id="";
		
		switch(checkedId){
		case R.id.type_lock:
			mLockType = LOCK_TYPE_LOCK;
			id = "TYPE_LOCK";
			break;
		case R.id.type_unlock:
			mLockType = LOCK_TYPE_UNLOCK;
			id = "TYPE_UNLOCK";
			break;
		case R.id.type_permalock:
			mLockType = LOCK_TYPE_PERMA_LOCK;
			id = "TYPE_PERMA_LOCK";
			break;
		}
		
		ATLog.i(TAG, INFO, "INFO. onCheckedChanged() - Type[%s]", id);
	}

	public void exitView() {
		ATLog.i(TAG, INFO, "INFO. exitView()");
	}

	private void enableWidgets(boolean enabled) {
		txtResult.setEnabled(enabled);
		txtData.setEnabled(enabled);
		txtRssi.setEnabled(enabled);
		txtPhase.setEnabled(enabled);
		
		tbKillPassword.setEnabled(enabled);
		tbAccessPassword.setEnabled(enabled);
		tbEpc.setEnabled(enabled);
		tbTid.setEnabled(enabled);
		tbUser.setEnabled(enabled);
		
		txtPassword.setEnabled(enabled);
		
		rgLockType.setEnabled(enabled);
		rdLock.setEnabled(enabled);
		rdUnlock.setEnabled(enabled);
		rdPermaLock.setEnabled(enabled);
		
		ATLog.i(TAG, INFO, "INFO. enableWidgets(%s)", enabled);
	}
	
	public void completeSetting(int type, Intent data) {
		
		ATLog.i(TAG, INFO, "INFO. completeSetting()");
	}

	private boolean loadingProperties() {
		String password="";
		
		// Load Password
		try{
			password = mReader.getRfidUhf().getAccessPassword();
		}catch(ATException e){
			ATLog.e(TAG, e, "ERROR. loadingProperties() - Failed to load access password");
			return false;
		}

		// Set Password
		dlgPassword.setValue(password);

		ATLog.i(TAG, INFO, "INFO. loadingProperties()");
		return true;
	}
	
	private void loadedProperties() {

		boolean isInitialize = loadingProperties();
		if(isInitialize && mIsInitialize) {

			dlgPassword.display();
			
			enableWidgets(true);

		} else {
			enableWidgets(false);
		}
		
		rgLockType.check(R.id.type_lock);
		
		ATLog.i(TAG, INFO, "INFO. loadedProperties() - [%s]" , isInitialize);
	}
	
	public void onReaderActionChanged(ATEAReader reader, ResultCode code, ActionState action, Object params){
		
		//enableWidgets(true);
		
		if(code != ResultCode.NoError) {
			ATLog.i(TAG, INFO, "ERROR. onReaderActionChanged([%s], %s, %s) - Failed to action changed [%s]",
					reader, code, action, code);
			enableWidgets(true);
			return;
		}
		
		if( action == ActionState.Stop ) {
			if(mIsAction) {
				mIsAction = false;
				outputMessage(getString(R.string.msg_fail_read_tag));
				txtRssi.setText(String.format(Locale.US, "%.2f dB", 0.0));
				txtPhase.setText(String.format(Locale.US, "%.2f \u00B0", 0.0));
			}
			enableWidgets(true);
		}
		
		ATLog.i(TAG, INFO, "EVENT. onReaderActionChanged([%s], %s, %s)", reader, code, action);
	}

	public void onReaderReadTag(ATEAReader reader, String tag, Object params){
		ATLog.i(TAG, INFO, "EVENT. onReaderReadTag([%s], [%s])", reader, tag);
	}

	public void onReaderAccessResult(ATEAReader reader, ResultCode code, ActionState action, String epc,
			String data, Object params){
		
		if( epc != null )
			txtData.setText(epc);
		
		if( code == ResultCode.NoError ) {
			outputMessage(getString(R.string.msg_success));
			if(params != null) {
				TagExtParam param = (TagExtParam) params;
				txtRssi.setText(String.format(Locale.US, "%.2f dB", param.getRssi()));
				txtPhase.setText(String.format(Locale.US, "%.2f \u00B0", param.getPhase()));
			}
		} else {
			outputMessage(code.getMessage());
			txtRssi.setText(String.format(Locale.US, "%.2f dB", 0.0));
			txtPhase.setText(String.format(Locale.US, "%.2f \u00B0", 0.0));
		}
		
		mIsAction = false;
		ATLog.i(TAG, INFO, "EVENT. onReaderAccessResult([%s], [%s], %s, [%s], [%s])", reader, code, action, epc, data);
	}

	public void onReaderReadBarcode(ATEAReader reader, BarcodeType type, String codeId, String barcode,
			Object params){
		ATLog.i(TAG, INFO, "EVENT. onReaderReadBarcode([%s], %s, [%s], [%s])", reader, type, codeId, barcode);
	}

	public void onReaderOperationModeChanged(ATEAReader reader, OperationMode mode, Object params){
		ATLog.i(TAG, INFO, "EVENT. onReaderOperationModeChanged([%s], %s)", reader, mode);
	}

	public void onReaderPowerGainChanged(ATEAReader reader, int power, Object params){
		ATLog.i(TAG, INFO, "EVENT. onReaderPowerGainChanged([%s], %d)", reader, power);
	}

	public void onReaderBatteryState(ATEAReader reader, int batteryState, Object params){
		ATLog.i(TAG, INFO, "EVENT. onReaderPowerGainChanged([%s], %d)", reader, batteryState);
	}
	
	public void onReaderKeyChanged(ATEAReader reader, KeyType type, KeyState state, 
			Object params) {
		ATLog.i(TAG, INFO, "EVENT. onReaderKeyChanged([%s], %s, %s)", reader, type, state);
	}
	
	// ------------------------------------------------------------------------
	// Action Methods
	// ------------------------------------------------------------------------
	public void action() {

		switch(mLockType) {
		case LOCK_TYPE_LOCK:
			actionLock();
			break;
		case LOCK_TYPE_UNLOCK:
			actionUnlock();
			break;
		case LOCK_TYPE_PERMA_LOCK:
			actionPermaLock();
			break;
		}
	}
	
	private void actionLock() {
		ResultCode res = ResultCode.NoError;
		enableWidgets(false);
		
		ATLog.i(TAG, INFO, "INFO. actionLock() - Kill Password [%s]", tbKillPassword.isChecked());
		ATLog.i(TAG, INFO, "INFO. actionLock() - Access Password [%s]", tbAccessPassword.isChecked());
		ATLog.i(TAG, INFO, "INFO. actionLock() - EPC [%s]", tbEpc.isChecked());
		ATLog.i(TAG, INFO, "INFO. actionLock() - TID [%s]", tbTid.isChecked());
		ATLog.i(TAG, INFO, "INFO. actionLock() - USER [%s]", tbUser.isChecked());
		
		if(mReader.getAction() == ActionState.Stop) {
			Lock6cParam param = new Lock6cParam();
			
			param.setKillPassword(tbKillPassword.isChecked() ? Lock6cParam.LockState.Lock : Lock6cParam.LockState.NoChanged);
			param.setAccessPassword(tbAccessPassword.isChecked() ? Lock6cParam.LockState.Lock : Lock6cParam.LockState.NoChanged);
			param.setEpc(tbEpc.isChecked() ? Lock6cParam.LockState.Lock : Lock6cParam.LockState.NoChanged);
			param.setTid(tbTid.isChecked() ? Lock6cParam.LockState.Lock : Lock6cParam.LockState.NoChanged);
			param.setUser(tbUser.isChecked() ? Lock6cParam.LockState.Lock : Lock6cParam.LockState.NoChanged);
			
			if( (res = mReader.getRfidUhf().lock6c(param, dlgPassword.getValue())) != ResultCode.NoError ) {
				ATLog.e(TAG, "ERROR. actionLock() - Failed to lock memory [%s]", res);
				MessageBox.show(getActivity(), String.format(Locale.US, "%s. [%s]", getString(R.string.msg_fail_start_lock),
						res.getMessage()), getString(R.string.title_error) );
				mReader.getRfidUhf().stop();
				return;
			}
			outputMessage(getString(R.string.msg_lock_memory));
		} else {
			if( (res = mReader.getRfidUhf().stop()) != ResultCode.NoError ) {
				ATLog.e(TAG, "ERROR. actionLock() - Failed to stop operation [%s]",res);
				MessageBox.show(getActivity(), String.format(Locale.US, "%s. [%s]", getString(R.string.msg_fail_stop_action),
						res.getMessage()), getString(R.string.title_error));
				enableWidgets(true);
				return;
			}
		}
		
		mIsAction = true;
		ATLog.i(TAG, INFO, "INFO. actionLock()");
	}
	
	private void actionUnlock() {
		ResultCode res = ResultCode.NoError;
		enableWidgets(false);
		
		ATLog.i(TAG, INFO, "INFO. actionUnlock() - Kill Password [%s]", tbKillPassword.isChecked());
		ATLog.i(TAG, INFO, "INFO. actionUnlock() - Access Password [%s]", tbAccessPassword.isChecked());
		ATLog.i(TAG, INFO, "INFO. actionUnlock() - EPC [%s]", tbEpc.isChecked());
		ATLog.i(TAG, INFO, "INFO. actionUnlock() - TID [%s]", tbTid.isChecked());
		ATLog.i(TAG, INFO, "INFO. actionUnlock() - USER [%s]", tbUser.isChecked());
		
		if(mReader.getAction() == ActionState.Stop) {
			Lock6cParam param = new Lock6cParam();
			
			param.setKillPassword(tbKillPassword.isChecked() ? Lock6cParam.LockState.Unlock : Lock6cParam.LockState.NoChanged);
			param.setAccessPassword(tbAccessPassword.isChecked() ? Lock6cParam.LockState.Unlock : Lock6cParam.LockState.NoChanged);
			param.setEpc(tbEpc.isChecked() ? Lock6cParam.LockState.Unlock : Lock6cParam.LockState.NoChanged);
			param.setTid(tbTid.isChecked() ? Lock6cParam.LockState.Unlock : Lock6cParam.LockState.NoChanged);
			param.setUser(tbUser.isChecked() ? Lock6cParam.LockState.Unlock : Lock6cParam.LockState.NoChanged);
			
			if( (res = mReader.getRfidUhf().lock6c(param, dlgPassword.getValue())) != ResultCode.NoError ) {
				ATLog.e(TAG, "ERROR. actionUnlock() - Failed to unlock memory [%s]", res);
				MessageBox.show(getActivity(), String.format(Locale.US, "%s. [%s]", getString(R.string.msg_fail_start_unlock),
						res.getMessage()), getString(R.string.title_error) );
				mReader.getRfidUhf().stop();
				return;
			}
			outputMessage(getString(R.string.msg_unlock_memory));
		} else {
			if( (res = mReader.getRfidUhf().stop()) != ResultCode.NoError ) {
				ATLog.e(TAG, "ERROR. actionUnlock() - Failed to stop operation [%s]",res);
				MessageBox.show(getActivity(), String.format(Locale.US, "%s. [%s]", getString(R.string.msg_fail_stop_action),
						res.getMessage()), getString(R.string.title_error));
				enableWidgets(true);
				return;
			}
		}
		
		mIsAction = true;
		ATLog.i(TAG, INFO, "INFO. actionUnlock()");
	}
	
	private void actionPermaLock() {
		ResultCode res = ResultCode.NoError;
		enableWidgets(false);
		
		ATLog.i(TAG, INFO, "INFO. actionPermaLock() - Kill Password [%s]", tbKillPassword.isChecked());
		ATLog.i(TAG, INFO, "INFO. actionPermaLock() - Access Password [%s]", tbAccessPassword.isChecked());
		ATLog.i(TAG, INFO, "INFO. actionPermaLock() - EPC [%s]", tbEpc.isChecked());
		ATLog.i(TAG, INFO, "INFO. actionPermaLock() - TID [%s]", tbTid.isChecked());
		ATLog.i(TAG, INFO, "INFO. actionPermaLock() - USER [%s]", tbUser.isChecked());
		
		if(mReader.getAction() == ActionState.Stop){
			PermaLock6cParam param = new PermaLock6cParam();
			
			param.setKillPassword(tbKillPassword.isChecked() ? PermaLock6cParam.LockState.PermalLock :
				PermaLock6cParam.LockState.NoChanged);
			param.setAccessPassword(tbAccessPassword.isChecked() ? PermaLock6cParam.LockState.PermalLock : 
				PermaLock6cParam.LockState.NoChanged);
			param.setEpc(tbEpc.isChecked() ? PermaLock6cParam.LockState.PermalLock : 
				PermaLock6cParam.LockState.NoChanged);
			param.setTid(tbTid.isChecked() ? PermaLock6cParam.LockState.PermalLock : 
				PermaLock6cParam.LockState.NoChanged);
			
			if((res = mReader.getRfidUhf().permaLock6c(param, dlgPassword.getValue())) != ResultCode.NoError){
				ATLog.e(TAG, "ERROR. actionPermaLock() - Failed to permalock memory [%s]", res);
				MessageBox.show(getActivity(), String.format(Locale.US, "%s. [%s]", getString(R.string.msg_fail_start_permalock),
						res.getMessage()), getString(R.string.title_error));
				mReader.getRfidUhf().stop();
				return;
			}
			outputMessage(getString(R.string.msg_permalock_memory));
		} else {
			if((res = mReader.getRfidUhf().stop()) != ResultCode.NoError) {
				ATLog.e(TAG, "ERROR. actionPermalock() - Failed to stop operation [%s]", res);
				MessageBox.show(getActivity(), String.format(Locale.US, "%s. [%s]", getString(R.string.msg_fail_stop_action),
						res.getMessage()), getString(R.string.title_error));
				enableWidgets(true);
				return;
			}
		}
		
		mIsAction = true;
		ATLog.i(TAG, INFO, "INFO. actionPermaLock()");
	}
	
	public void clear() {
		
		txtRssi.setText(String.format(Locale.US, "%.2f dB", 0.0));
		txtPhase.setText(String.format(Locale.US, "%.2f \u00B0", 0.0));
		
		txtResult.setText(getString(R.string.access_result));
		txtData.setText(getString(R.string.access_data_noti));
		
		ATLog.i(TAG, INFO, "INFO. clear()");
	}

	private void outputMessage(String msg) {
		txtResult.setText(msg);
	}

}
