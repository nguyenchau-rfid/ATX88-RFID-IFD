package com.atid.app.atx.activity.view;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.atid.app.atx.R;
import com.atid.app.atx.activity.view.fragment.AccessLockMemory;
import com.atid.app.atx.activity.view.fragment.AccessReadMemory;
import com.atid.app.atx.activity.view.fragment.AccessWriteMemory;
import com.atid.lib.diagnostics.ATException;
import com.atid.lib.module.barcode.types.BarcodeType;
import com.atid.lib.reader.ATEAReader;
import com.atid.lib.reader.types.KeyState;
import com.atid.lib.reader.types.KeyType;
import com.atid.lib.reader.types.OperationMode;
import com.atid.lib.types.ActionState;
import com.atid.lib.types.ResultCode;
import com.atid.lib.util.diagnotics.ATLog;



public class ViewAccessMemory extends BaseView implements RadioGroup.OnCheckedChangeListener, OnClickListener{

	private static final int METHOD_UNKNOWN = 0;
	private static final int METHOD_READ_MEMORY = 1;
	private static final int METHOD_WRITE_MEMORY = 2;
	private static final int METHOD_LOCK_MEMORY = 3;
	
	private RadioGroup rdMethod;
	private RadioButton rdMethodReadMemory;
	private RadioButton rdMethodWriteMemory;
	private RadioButton rdMethodLockMemory;
	
	private Button btnAction;
	private Button btnSetting;
	private Button btnClear;
	
	private int mMethod;
	private boolean mIsInitialize;
	
	private Fragment mFragmentView;
	
	private String mWriteData;
	
	public ViewAccessMemory() {
		super();
		TAG = ViewAccessMemory.class.getCanonicalName();
		mId = VIEW_ACCESS_MEMORY;
		mMethod = METHOD_UNKNOWN;
	
		mWriteData = "";
		mIsInitialize = false;
	}
	
	@Override
	protected int getInflateResId() {
		return R.layout.view_access_memory;
	}

	@Override
	protected void initView() {
		
		rdMethod = (RadioGroup) mView.findViewById(R.id.method_type_access);
		rdMethod.setOnCheckedChangeListener(this);
		
		rdMethodReadMemory = (RadioButton) mView.findViewById(R.id.method_read_memory);
		rdMethodWriteMemory = (RadioButton) mView.findViewById(R.id.method_write_memory);
		rdMethodLockMemory = (RadioButton) mView.findViewById(R.id.method_lock_memory);
		
		btnAction = (Button) mView.findViewById(R.id.action);
		btnAction.setOnClickListener(this);
		btnSetting = (Button) mView.findViewById(R.id.setting);
		btnSetting.setOnClickListener(this);
		btnClear = (Button) mView.findViewById(R.id.clear);
		btnClear.setOnClickListener(this);

		ATLog.i(TAG, INFO, "INFO. initView()");

	}

	@Override
	protected void exitView() {
		mMethod = METHOD_UNKNOWN;
		ATLog.i(TAG, INFO, "INFO. exitView()");
	}

	@Override
	protected void enableWidgets(boolean enabled) {
		super.enableWidgets(enabled);
		
		rdMethod.setEnabled(mIsEnabled);

		if(getReader() != null) {
			rdMethodReadMemory.setEnabled(mIsEnabled && getReader().getRfidUhf() != null);
			rdMethodWriteMemory.setEnabled(mIsEnabled && getReader().getRfidUhf() != null);
			rdMethodLockMemory.setEnabled(mIsEnabled && getReader().getRfidUhf() != null);
		}

		btnAction.setEnabled(enabled);
		btnSetting.setEnabled(mIsEnabled);
		btnClear.setEnabled(mIsEnabled);

		ATLog.i(TAG, INFO, "INFO. enableWidgets(%s)", enabled);
	}
	
	@Override
	protected void completeSetting(int type,Intent data) {
		
		if(mFragmentView != null) {
			switch(mMethod){
			case METHOD_READ_MEMORY:
				((AccessReadMemory)mFragmentView).completeSetting(type, data);
				break;
			case METHOD_WRITE_MEMORY:
				((AccessWriteMemory)mFragmentView).completeSetting(type, data);
				break;
			case METHOD_LOCK_MEMORY:
				((AccessLockMemory)mFragmentView).completeSetting(type, data);
				break;
			}
		}
		ATLog.i(TAG, INFO, "INFO. completeSetting()");
	}

	@Override
	protected boolean loadingProperties() {
		
		// Disable Use Key Action
		try {
			getReader().setUseActionKey(false);
		} catch (ATException e) {
			ATLog.e(TAG, "ERROR. loadingProperties() - Failed to disabled key action");
			return false;
		}
		
		ATLog.i(TAG, INFO, "INFO. loadingProperties()");
		return true;
	}

	@Override
	protected void loadedProperties(boolean isInitialize) {
		boolean enabled = false;
		
		if(isInitialize) {
			enabled = true;
		}
		mIsInitialize = isInitialize;
		
		enableWidgets(enabled);
		
		if(mWriteData.equals("")) {
			rdMethod.check(R.id.method_read_memory);
		} else {
			rdMethod.check(R.id.method_write_memory);
		}

		//selectFragmentView(mMethod);
				
		ATLog.i(TAG, INFO, "INFO. loadedProperties() - [%s]", isInitialize);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

	}

	@Override
	public void onReaderActionChanged(ATEAReader reader, ResultCode code,
			ActionState action, Object params) {
		
		enableWidgets(true);
		if(mFragmentView != null) {
			switch(mMethod){
			case METHOD_READ_MEMORY:
				((AccessReadMemory)mFragmentView).onReaderActionChanged(reader, code, action, params);
				break;
			case METHOD_WRITE_MEMORY:
				((AccessWriteMemory)mFragmentView).onReaderActionChanged(reader, code, action, params);
				break;
			case METHOD_LOCK_MEMORY:
				((AccessLockMemory)mFragmentView).onReaderActionChanged(reader, code, action, params);
				break;
			}
		}
		
		if(action == ActionState.Stop) {
			btnAction.setText(R.string.action_start);
		} else {
			btnAction.setText(R.string.action_stop);
		}
		
		ATLog.i(TAG, INFO, "EVENT. onReaderActionChanged([%s], %s, %s)", reader, code, action);
	}

	@Override
	public void onReaderReadTag(ATEAReader reader, String tag, Object params) {
		
		if(mFragmentView != null) {
			switch(mMethod){
			case METHOD_READ_MEMORY:
				((AccessReadMemory)mFragmentView).onReaderReadTag(reader, tag, params);
				break;
			case METHOD_WRITE_MEMORY:
				((AccessWriteMemory)mFragmentView).onReaderReadTag(reader, tag, params);
				break;
			case METHOD_LOCK_MEMORY:
				((AccessLockMemory)mFragmentView).onReaderReadTag(reader, tag, params);
				break;
			}
		}

		
		ATLog.i(TAG, INFO, "EVENT. onReaderReadTag([%s], [%s])", reader, tag);
	}

	@Override
	public void onReaderAccessResult(ATEAReader reader, ResultCode code,
			ActionState action, String epc, String data, Object params) {
		if(mFragmentView != null) {
			switch(mMethod){
			case METHOD_READ_MEMORY:
				((AccessReadMemory)mFragmentView).onReaderAccessResult(reader, code, action, epc, data, params);
				break;
			case METHOD_WRITE_MEMORY:
				((AccessWriteMemory)mFragmentView).onReaderAccessResult(reader, code, action, epc, data, params);
				break;
			case METHOD_LOCK_MEMORY:
				((AccessLockMemory)mFragmentView).onReaderAccessResult(reader, code, action, epc, data, params);
				break;
			}
		}
		
		ATLog.i(TAG, INFO, "EVENT. onReaderAccessResult([%s], [%s], %s, [%s], [%s])", reader, code, action, epc, data);
	}

	@Override
	public void onReaderReadBarcode(ATEAReader reader, BarcodeType type,
			String codeId, String barcode, Object params) {
		if(mFragmentView != null) {
			switch(mMethod){
			case METHOD_READ_MEMORY:
				((AccessReadMemory)mFragmentView).onReaderReadBarcode(reader, type, codeId, barcode, params);
				break;
			case METHOD_WRITE_MEMORY:
				((AccessWriteMemory)mFragmentView).onReaderReadBarcode(reader, type, codeId, barcode, params);
				break;
			case METHOD_LOCK_MEMORY:
				((AccessLockMemory)mFragmentView).onReaderReadBarcode(reader, type, codeId, barcode, params);
				break;
			}
		}
		
		ATLog.i(TAG, INFO, "EVENT. onReaderReadBarcode([%s], %s, [%s], [%s])", reader, type, codeId, barcode);
	}

	@Override
	public void onReaderOperationModeChanged(ATEAReader reader,
			OperationMode mode, Object params) {
		if(mFragmentView != null) {
			switch(mMethod){
			case METHOD_READ_MEMORY:
				((AccessReadMemory)mFragmentView).onReaderOperationModeChanged(reader, mode, params);
				break;
			case METHOD_WRITE_MEMORY:
				((AccessWriteMemory)mFragmentView).onReaderOperationModeChanged(reader, mode, params);
				break;
			case METHOD_LOCK_MEMORY:
				((AccessLockMemory)mFragmentView).onReaderOperationModeChanged(reader, mode, params);
				break;
			}
		}
		
		ATLog.i(TAG, INFO, "EVENT. onReaderOperationModeChanged([%s], %s)", reader, mode);
	}

	@Override
	public void onReaderPowerGainChanged(ATEAReader reader, int power,
			Object params) {
		if(mFragmentView != null) {
			switch(mMethod){
			case METHOD_READ_MEMORY:
				((AccessReadMemory)mFragmentView).onReaderPowerGainChanged(reader, power, params);
				break;
			case METHOD_WRITE_MEMORY:
				((AccessWriteMemory)mFragmentView).onReaderPowerGainChanged(reader, power, params);
				break;
			case METHOD_LOCK_MEMORY:
				((AccessLockMemory)mFragmentView).onReaderPowerGainChanged(reader, power, params);
				break;
			}
		}
		
		ATLog.i(TAG, INFO, "EVENT. onReaderPowerGainChanged([%s], %d)", reader, power);
	}

	@Override
	public void onReaderBatteryState(ATEAReader reader, int batteryState,
			Object params) {
		if(mFragmentView != null) {
			switch(mMethod){
			case METHOD_READ_MEMORY:
				((AccessReadMemory)mFragmentView).onReaderBatteryState(reader, batteryState, params);
				break;
			case METHOD_WRITE_MEMORY:
				((AccessWriteMemory)mFragmentView).onReaderBatteryState(reader, batteryState, params);
				break;
			case METHOD_LOCK_MEMORY:
				((AccessLockMemory)mFragmentView).onReaderBatteryState(reader, batteryState, params);
				break;
			}
		}
		
		ATLog.i(TAG, INFO, "EVENT. onReaderBatteryState([%s], %d)", reader, batteryState);
	}
	
	@Override
	public void onReaderKeyChanged(ATEAReader reader, KeyType type, KeyState state, 
			Object params) {
		if(mFragmentView != null) {
			switch(mMethod){
			case METHOD_READ_MEMORY:
				((AccessReadMemory)mFragmentView).onReaderKeyChanged(reader, type, state, params);
				break;
			case METHOD_WRITE_MEMORY:
				((AccessWriteMemory)mFragmentView).onReaderKeyChanged(reader, type, state, params);
				break;
			case METHOD_LOCK_MEMORY:
				((AccessLockMemory)mFragmentView).onReaderKeyChanged(reader, type, state, params);
				break;
			}
		}
		ATLog.i(TAG, INFO, "EVENT. onReaderKeyChanged([%s], %s, %s)", reader, type, state);
	}
	
	@Override
	public void onClick(View v) {

		switch(v.getId()){
		case R.id.action:
			if(mFragmentView != null) {
				switch(mMethod){
				case METHOD_READ_MEMORY:
					((AccessReadMemory)mFragmentView).action();
					break;
				case METHOD_WRITE_MEMORY:
					((AccessWriteMemory)mFragmentView).action();
					break;
				case METHOD_LOCK_MEMORY:
					((AccessLockMemory)mFragmentView).action();
					break;
				}
			}
			ATLog.i(TAG, INFO, "INFO. onClick() - action");
			break;
			
		case R.id.setting:
			showRfidSetting();
			ATLog.i(TAG, INFO, "INFO. onClick() - setting");
			break;
			
		case R.id.clear:
			if(mFragmentView != null) {
				switch(mMethod){
				case METHOD_READ_MEMORY:
					((AccessReadMemory)mFragmentView).clear();
					break;
				case METHOD_WRITE_MEMORY:
					((AccessWriteMemory)mFragmentView).clear();
					break;
				case METHOD_LOCK_MEMORY:
					((AccessLockMemory)mFragmentView).clear();
					break;
				}
			}
			ATLog.i(TAG, INFO, "INFO. onClick() - clear");
			break;
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
	
		String id="";
		switch(checkedId){
		case R.id.method_read_memory:
//			if(!rdMethodReadMemory.isChecked())
//				return;
			if(mMethod == METHOD_READ_MEMORY)
				return;
			
			mMethod = METHOD_READ_MEMORY;
			id="method_read_memory";

			break;
			
		case R.id.method_write_memory:
//			if(!rdMethodWriteMemory.isChecked())
//				return;
			if(mMethod == METHOD_WRITE_MEMORY)
				return;
			
			mMethod = METHOD_WRITE_MEMORY;
			id="method_write_memory";

			break;
			
		case R.id.method_lock_memory:
//			if(!rdMethodLockMemory.isChecked())
//				return;
			if(mMethod == METHOD_LOCK_MEMORY)
				return;
			
			mMethod = METHOD_LOCK_MEMORY;
			id="method_lock_memory";

			break;
		default:
			ATLog.e(TAG, "ERROR. onCheckedChanged(%d) - Failed to unknown check id", checkedId);
			return;
		}
		
		selectFragmentView(mMethod);
		
		ATLog.i(TAG, INFO, "INFO. RadioGroup onCheckedChanged(%s)", id);
	}
	
//	private void removeFragmentView() {
//		FragmentManager fm = getFragmentManager();
//		FragmentTransaction ft = fm.beginTransaction();
//		ft.replace(R.id.container_access_memory, mFragmentView);
//		ft.commit();
//		
//		ATLog.i(TAG, INFO, "INFO. removeFragmentView()");
//	}
	
	private void replaceFragmentView() {
		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.container_access_memory, mFragmentView);
		ft.commit();
		
		ATLog.i(TAG, INFO, "INFO. replaceFragmentView()");
	}
	
	private void selectFragmentView(int view) {
		
		switch(view){
		case METHOD_READ_MEMORY :
			mFragmentView = new AccessReadMemory(getReader() ,mIsInitialize );
			break;

		case METHOD_WRITE_MEMORY :
			mFragmentView = new AccessWriteMemory(getReader(),mIsInitialize );
			if(!mWriteData.equals("")){
				((AccessWriteMemory)mFragmentView).setWriteData(mWriteData);	
				mWriteData = "";
			}
			break;
			
		case METHOD_LOCK_MEMORY :
			mFragmentView = new AccessLockMemory(getReader() ,mIsInitialize );
			break;
			
		}
		
		replaceFragmentView();
		ATLog.i(TAG, INFO, "INFO. selectFragmentView(%d)", view);
	}

	public void setWriteData(String data) {
		mWriteData = data;
		ATLog.i(TAG, INFO, "INFO. setWriteData(%s)", data);
	}
}