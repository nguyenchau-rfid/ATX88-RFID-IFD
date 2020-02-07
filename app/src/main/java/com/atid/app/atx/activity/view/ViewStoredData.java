package com.atid.app.atx.activity.view;

import java.util.Locale;

import com.atid.app.atx.R;
import com.atid.app.atx.adapter.DataListAdapter;
import com.atid.app.atx.dialog.WaitDialog;
import com.atid.lib.diagnostics.ATException;
import com.atid.lib.module.barcode.types.BarcodeType;
import com.atid.lib.module.rfid.uhf.params.TagExtParam;
import com.atid.lib.reader.ATEAReader;
import com.atid.lib.reader.types.KeyState;
import com.atid.lib.reader.types.KeyType;
import com.atid.lib.reader.types.OperationMode;
import com.atid.lib.types.ActionState;
import com.atid.lib.types.ResultCode;
import com.atid.lib.util.diagnotics.ATLog;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class ViewStoredData extends BaseView implements OnClickListener {

	// ------------------------------------------------------------------------
	// Member Variable
	// ------------------------------------------------------------------------

	private ListView lstData;
	private TextView txtStoredCount;
	private TextView txtCount;
	private TextView txtTotalCount;
	private Button btnAction;
	private Button btnClear;
	private Button btnRemove;

	private DataListAdapter adpData;

	private int mStoredCount;
	private int mTotalCount;

	// ------------------------------------------------------------------------
	// Constructor
	// ------------------------------------------------------------------------

	public ViewStoredData() {
		super();
		TAG = ViewStoredData.class.getSimpleName();
		mId = VIEW_STORED_DATA;

		mStoredCount = 0;
		mTotalCount = 0;
	}

	// ------------------------------------------------------------------------
	// Override Event Methods
	// ------------------------------------------------------------------------

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.action:
			action();
			break;
		case R.id.clear:
			clear();
			break;
		case R.id.remove:
			remove();
			break;
		}
	}

	// ------------------------------------------------------------------------
	// Action Methods
	// ------------------------------------------------------------------------

	private void action() {

		ResultCode res = ResultCode.NoError;
		enableWidgets(false);

		WaitDialog.showProgess(getActivity(), R.string.msg_load_stored_data);
		WaitDialog.setMax(mStoredCount);
		
		clear();
		
		if (getReader().getAction() == ActionState.Stop) {
			if ((res = getReader().loadStoredData()) != ResultCode.NoError) {
				ATLog.e(TAG, "ERROR. action() - Failed to load storead data [%s]", res.getCode());
				WaitDialog.hide();
				return;
			}
		}

		ATLog.i(TAG, INFO, "INFO. action()");
	}

	private void clear() {

		adpData.clear();
		mTotalCount = 0;

		txtCount.setText(String.format(Locale.US, "%d", adpData.getCount()));
		txtTotalCount.setText(String.format(Locale.US, "%d", mTotalCount));

		ATLog.i(TAG, INFO, "INFO. clear()");
	}

	private void remove() {

		ResultCode res = ResultCode.NoError;
		enableWidgets(false);

		WaitDialog.show(getActivity(), R.string.msg_remove_stored_data);
		
		if (getReader().getAction() == ActionState.Stop) {
			if ((res = getReader().removeAllStoreadData()) != ResultCode.NoError) {
				ATLog.e(TAG, "ERROR. remove() - Failed to remove all storead data [%s]", res.getCode());
				WaitDialog.hide();
				return;
			}
		}

		ATLog.i(TAG, INFO, "INFO. remove()");
	}

	// ------------------------------------------------------------------------
	// Override Reader Event Methods
	// ------------------------------------------------------------------------

	@Override
	public void onReaderActionChanged(ATEAReader reader, ResultCode code, ActionState action, Object params) {

		//enableWidgets(true);

		if (code != ResultCode.NoError) {
			enableWidgets(true);
			ATLog.e(TAG, "ERROR. onReaderActionChanged([%s], %s, %s) - Failed to action changed [%s]", reader, code,
					action, code);
			return;
		}

		switch (action) {
		case LoadStoredData:
			btnAction.setText(R.string.action_stop);
			break;
		case RemoveAllStoredData:
			btnRemove.setText(R.string.action_stop);
			break;
		case Stop:
			adpData.notifyDataSetChanged();
			btnAction.setText(R.string.action_load);
			btnRemove.setText(R.string.action_remove);
			try {
				mStoredCount = getReader().getStoredTagCount();
			} catch (ATException e) {
				ATLog.e(TAG, "ERROR. onReaderActionChanged([%s], %s, %s) - Failed to get stored tag count", reader, code, action);
				return;
			}
			txtStoredCount.setText(String.format(Locale.US, "%d", mStoredCount));
			txtCount.setText(String.format(Locale.US, "%d", adpData.getCount()));
			txtTotalCount.setText(String.format(Locale.US, "%d", mTotalCount));
			WaitDialog.hide();
			break;
		default:
			return;
		}
		
		enableWidgets(true);
		ATLog.i(TAG, INFO, "EVENT. onReaderActionChanged([%s], %s, %s)", reader, code, action);
	}

	@Override
	public void onReaderReadTag(ATEAReader reader, String tag, Object params) {
		float rssi = 0;
		float phase = 0;

		if (params != null) {
			TagExtParam param = (TagExtParam) params;
			rssi = param.getRssi();
			phase = param.getPhase();
		}
		adpData.add(tag, "", rssi, phase);
		mTotalCount++;
		WaitDialog.setProgress(mTotalCount);

		txtCount.setText(String.format(Locale.US, "%d", adpData.getCount()));
		txtTotalCount.setText(String.format(Locale.US, "%d", mTotalCount));

		ATLog.i(TAG, INFO, "EVENT. onReaderReadTag([%s], [%s])", reader, tag);
	}

	@Override
	public void onReaderAccessResult(ATEAReader reader, ResultCode code, ActionState action, String epc, String data,
			Object params) {

		ATLog.i(TAG, INFO, "EVENT. onReaderAccessResult([%s], [%s], %s, [%s], [%s])", reader, code, action, epc, data);
	}

	@Override
	public void onReaderReadBarcode(ATEAReader reader, BarcodeType type, String codeId, String barcode, Object params) {

		adpData.add(type, codeId, barcode);
		mTotalCount++;
		WaitDialog.setProgress(mTotalCount);

		txtCount.setText(String.format(Locale.US, "%d", adpData.getCount()));
		txtTotalCount.setText(String.format(Locale.US, "%d", mTotalCount));

		ATLog.i(TAG, INFO, "EVENT. onReaderReadBarcode([%s], %s, [%s], [%s])", reader, type, codeId, barcode);
	}

	@Override
	public void onReaderOperationModeChanged(ATEAReader reader, OperationMode mode, Object params) {

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
		ATLog.i(TAG, INFO, "EVENT. onReaderKeyChanged([%s], %s, %s)", reader, type, state);
	}
	
	// ------------------------------------------------------------------------
	// Abstractable Methods
	// ------------------------------------------------------------------------

	// Get Inflate Resource Id
	@Override
	protected int getInflateResId() {
		return R.layout.view_stored_data;
	}

	// Initialize View
	@Override
	protected void initView() {

		lstData = (ListView) mView.findViewById(R.id.data_list);
		adpData = new DataListAdapter(getActivity());
		lstData.setAdapter(adpData);

		txtStoredCount = (TextView) mView.findViewById(R.id.stored_count);
		txtCount = (TextView) mView.findViewById(R.id.count);
		txtTotalCount = (TextView) mView.findViewById(R.id.total_count);

		btnAction = (Button) mView.findViewById(R.id.action);
		btnAction.setOnClickListener(this);

		btnClear = (Button) mView.findViewById(R.id.clear);
		btnClear.setOnClickListener(this);

		btnRemove = (Button) mView.findViewById(R.id.remove);
		btnRemove.setOnClickListener(this);
		
		clear();

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
		
		lstData.setEnabled(mIsEnabled);
		btnAction.setEnabled(mIsEnabled && mStoredCount > 0);
		btnClear.setEnabled(mIsEnabled);
		btnRemove.setEnabled(mIsEnabled && mStoredCount > 0);

		ATLog.i(TAG, INFO, "INFO. enableWidgets(%s)", enabled);
	}

	// Loading Reader Properties
	@Override
	protected boolean loadingProperties() {

		// Load Stored Count
		try {
			mStoredCount = getReader().getStoredTagCount();
		} catch (ATException e) {
			ATLog.e(TAG, "ERROR. loadingProperties() - Failed to load stored count");
			return false;
		}

		// Disabled Use Key Action
		try {
			getReader().setUseActionKey(false);
		} catch (ATException e) {
			ATLog.e(TAG, "ERROR. loadingProperties() - Failed to disabled key action");
			return false;
		}

		ATLog.i(TAG, INFO, "INFO. loadingProperties()");
		return true;
	}

	// Loaded Reader Properteis
	@Override
	protected void loadedProperties(boolean isInitialize) {

		if (isInitialize) {
			// Display Stored Count
			txtStoredCount.setText(String.format(Locale.US, "%d", mStoredCount));
			txtCount.setText(String.format(Locale.US, "%d", adpData.getCount()));
			txtTotalCount.setText(String.format(Locale.US, "%d", mTotalCount));

			enableWidgets(true);
		} else {
			enableWidgets(false);
		}

		ATLog.i(TAG, INFO, "INFO. loadedProperties()");
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

	}

	@Override
	protected void completeSetting(int type,Intent data) {
		
		ATLog.i(TAG, INFO, "INFO. completeSetting()");
	}

}
