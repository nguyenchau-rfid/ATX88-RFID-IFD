package com.atid.app.atx.activity.view.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import java.util.Locale;

import com.atid.app.atx.R;
import com.atid.app.atx.adapter.MemoryListAdapter;
import com.atid.app.atx.dialog.BaseDialog;
import com.atid.app.atx.dialog.EnumListDialog;
import com.atid.app.atx.dialog.MessageBox;
import com.atid.app.atx.dialog.NumberUnitDialog;
import com.atid.app.atx.dialog.PasswordDialog;
import com.atid.lib.diagnostics.ATException;
import com.atid.lib.module.barcode.types.BarcodeType;
import com.atid.lib.module.rfid.uhf.params.TagExtParam;
import com.atid.lib.module.rfid.uhf.types.BankType;
import com.atid.lib.reader.ATEAReader;
import com.atid.lib.reader.types.KeyState;
import com.atid.lib.reader.types.KeyType;
import com.atid.lib.reader.types.OperationMode;
import com.atid.lib.types.ActionState;
import com.atid.lib.types.ResultCode;
import com.atid.lib.util.diagnotics.ATLog;

import android.widget.ListView;
import android.widget.TextView;

public class AccessReadMemory extends Fragment implements OnClickListener {
	private static final String TAG = AccessReadMemory.class.getSimpleName();
	protected static final int INFO = ATLog.L2;
	
	private TextView txtResult;
	private TextView txtData;
	private TextView txtRssi;
	private TextView txtPhase;
	
	private ListView lstData;
	private MemoryListAdapter mAdpData;
	
	private TextView txtBank;
	private TextView txtOffset;
	private TextView txtLength;
	private TextView txtPassword;
	
	private EnumListDialog dlgBank;
	private NumberUnitDialog dlgOffset;
	private NumberUnitDialog dlgLength;
	private PasswordDialog dlgPassword;
	
	private View mView;
	
	private ATEAReader mReader;
	
	private volatile boolean mIsAction;
	private boolean mIsInitialize;
	
	public AccessReadMemory(ATEAReader reader , boolean isInitialize) {
		if(reader != null)
			mReader = reader;
		
		dlgBank = null;
		dlgOffset = null;
		dlgLength = null;
		dlgPassword = null;
		
		mIsAction = false;
		
		mIsInitialize = isInitialize;
		
		ATLog.i(TAG, INFO, "INFO. AccessReadMemory()");	
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_access_read_memory, container, false);
		
		initialize();
		
		loadedProperties();
		
		ATLog.i(TAG, INFO, "INFO. onCreateView()");	
		return mView;
	}
	
	private void initialize() {
		
		initWidget();
		
		dlgBank = new EnumListDialog(txtBank, BankType.values());
		dlgOffset = new NumberUnitDialog(txtOffset, getResources().getString(R.string.unit_word));
		dlgLength = new NumberUnitDialog(txtLength, getResources().getString(R.string.unit_word));
		dlgPassword = new PasswordDialog(txtPassword);
		
		clear();
		ATLog.i(TAG, INFO, "INFO. initialize()");
	}
	
	private void initWidget() {
		txtResult = (TextView) mView.findViewById(R.id.result);
		txtData = (TextView) mView.findViewById(R.id.data);
		txtRssi = (TextView) mView.findViewById(R.id.rssi);
		txtPhase = (TextView) mView.findViewById(R.id.phase);
		
		lstData = (ListView) mView.findViewById(R.id.value);
		mAdpData = new MemoryListAdapter(getActivity());
		lstData.setAdapter(mAdpData);
		
		txtBank = (TextView) mView.findViewById(R.id.bank);
		txtBank.setOnClickListener(this);
		txtOffset = (TextView) mView.findViewById(R.id.offset);
		txtOffset.setOnClickListener(this);
		txtLength = (TextView) mView.findViewById(R.id.length);
		txtLength.setOnClickListener(this);
		txtPassword = (TextView) mView.findViewById(R.id.password);
		txtPassword.setOnClickListener(this);
		

		ATLog.i(TAG, INFO, "INFO. initWidget()");
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.bank:
			dlgBank.showDialog(getActivity(), R.string.bank);
			ATLog.i(TAG, INFO, "INFO. onClick() - bank");
			break;
		case R.id.offset:
			dlgOffset.showDialog(getActivity(), R.string.offset);
			ATLog.i(TAG, INFO, "INFO. onClick() - offset");
			break;
		case R.id.length:
			dlgLength.showDialog(getActivity(), R.string.length);
			ATLog.i(TAG, INFO, "INFO. onClick() - length");
			break;
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
			ATLog.i(TAG, INFO, "INFO. onClick() - password");
			break;
			
		}
	}
	
	public void exitView() {
		ATLog.i(TAG, INFO, "INFO. exitView()");
	}

	private void enableWidgets(boolean enabled) {
		txtResult.setEnabled(enabled);
		txtData.setEnabled(enabled);
		txtPhase.setEnabled(enabled);
		txtRssi.setEnabled(enabled);
		
		lstData.setEnabled(enabled);
		
		txtBank.setEnabled(enabled);
		txtOffset.setEnabled(enabled);
		txtLength.setEnabled(enabled);
		txtPassword.setEnabled(enabled);
		
		ATLog.i(TAG, INFO, "INFO. enableWidgets(%s)", enabled);
	}
	
	public void completeSetting(int type, Intent data) {
		
		ATLog.i(TAG, INFO, "INFO. completeSetting()");
	}

	private boolean loadingProperties() {
		String password="";
		
		// Load Bank
		dlgBank.setValue(BankType.EPC);		
		
		// Load Offset
		dlgOffset.setValue(2);
		
		// Load Length
		dlgLength.setValue(2);

		// Load Password
		try{
			password = mReader.getRfidUhf().getAccessPassword();
		}catch(ATException e){
			ATLog.e(TAG, e, "ERROR. loadingProperties() - Failed to load access password");
			return false;
		}

		// set Password
		dlgPassword.setValue(password);

		ATLog.i(TAG, INFO, "INFO. loadingProperties()");
		return true;
	}
	
	private void loadedProperties() {

		boolean isInitialize = loadingProperties();
		if(isInitialize && mIsInitialize) {

			dlgBank.display();
			
			dlgOffset.display();
			
			dlgLength.display();
			
			dlgPassword.display();
			
			enableWidgets(true);

		} else {
			enableWidgets(false);
		}
		ATLog.i(TAG, INFO, "INFO. loadedProperties() - [%s]" , isInitialize);
	}
	
	public void onReaderActionChanged(ATEAReader reader, ResultCode code, ActionState action, Object params){
		
		//enableWidgets(true);
		
		if(code != ResultCode.NoError ){
			ATLog.i(TAG, INFO, "ERROR. onReaderActionChanged([%s], %s, %s) - Failed to action changed [%s]",
					reader, code , action, code);
			enableWidgets(true);
			return;
		}
		
		if( action == ActionState.Stop) {
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
		
		if(epc != null)
			txtData.setText(epc);
		
		if(data != null)
			mAdpData.setValue(dlgOffset.getValue(), data);
		
		if(code == ResultCode.NoError) {
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
		ResultCode res = ResultCode.NoError;
		enableWidgets(false);
		
		ATLog.i(TAG, INFO, "INFO. action() - Access PW [%s]" , dlgPassword.getValue());
		
		if(mReader.getAction() == ActionState.Stop) {
			if( (res = mReader.getRfidUhf().readMemory6c((BankType) dlgBank.getValue(), dlgOffset.getValue(), 
					dlgLength.getValue(), dlgPassword.getValue())) != ResultCode.NoError) {
				ATLog.e(TAG, "ERROR. action() - Failed to read memory[%s]",res);
				MessageBox.show(getActivity(), String.format(Locale.US, "%s. [%s]",
						getString(R.string.msg_fail_start_read_memory), res.getMessage()), getString(R.string.title_error));
				
				mReader.getRfidUhf().stop();
				return;
			}
			
			outputMessage(getString(R.string.msg_read_memory));
		} else {
			if( (res = mReader.getRfidUhf().stop()) != ResultCode.NoError) {
				ATLog.e(TAG, "ERROR. action() - Failed to stop operation [%s]", res);
				MessageBox.show(getActivity(), String.format(Locale.US, "%s. [%s]", 
						getString(R.string.msg_fail_stop_action), res.getMessage()), getString(R.string.title_error));
				enableWidgets(true);
				return;
			}
		}
		
		mIsAction = true;
		ATLog.i(TAG, INFO, "INFO. action()");
	}
	
	public void clear() {
		
		txtRssi.setText(String.format(Locale.US, "%.2f dB", 0.0));
		txtPhase.setText(String.format(Locale.US, "%.2f \u00B0", 0.0));
		
		txtResult.setText(getString(R.string.access_result));
		txtData.setText(getString(R.string.access_data_noti));
		
		mAdpData.clear();
		
		ATLog.i(TAG, INFO, "INFO. clear()");
	}
	
	private void outputMessage(String msg) {
		txtResult.setText(msg);
	}
	

}
