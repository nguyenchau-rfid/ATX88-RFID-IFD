package com.atid.app.atx.activity.view;

import java.util.Locale;

import com.atid.app.atx.R;
import com.atid.app.atx.adapter.OptionListAdapter;
import com.atid.app.atx.dialog.BaseDialog;
import com.atid.app.atx.dialog.DateTimeDialog;
import com.atid.app.atx.dialog.EnumListDialog;
import com.atid.app.atx.dialog.NotifyMethodDialog;
import com.atid.app.atx.dialog.NumberUnitDialog;
import com.atid.lib.atx88.ATx88Reader;
import com.atid.lib.atx88.ATn88Reader;
import com.atid.lib.diagnostics.ATException;
import com.atid.lib.module.barcode.types.BarcodeType;
import com.atid.lib.reader.ATEAReader;
import com.atid.lib.reader.params.NotifyMethod;
import com.atid.lib.reader.types.KeyState;
import com.atid.lib.reader.types.KeyType;
import com.atid.lib.reader.types.NotifyTimeType;
import com.atid.lib.reader.types.OperationMode;
import com.atid.lib.types.ActionState;
import com.atid.lib.types.ResultCode;
import com.atid.lib.util.diagnotics.ATLog;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

@SuppressLint({ "NewApi", "SimpleDateFormat" })
public class ViewOption extends BaseView implements OnChildClickListener, OnClickListener {

	// ------------------------------------------------------------------------
	// Member Variable
	// ------------------------------------------------------------------------

	private ExpandableListView lstOption;
	private Button btnDefault;

	private OptionListAdapter adpOption;

	private String mVersion;
	private String mSerialNo;

	private DateTimeDialog dlgTime;
	private NumberUnitDialog dlgDisplayOffTime;
	private NumberUnitDialog dlgAutoOffTime;
	private EnumListDialog dlgButtonMode;
	private NotifyMethodDialog dlgButtonNotify;
	private NotifyMethodDialog dlgAlertNotify;

	private ActionState mAction;

	// ------------------------------------------------------------------------
	// Constructor
	// ------------------------------------------------------------------------

	public ViewOption() {
		super();
		TAG = ViewOption.class.getSimpleName();
		mId = VIEW_OPTION;

		mVersion = "";
		mSerialNo = "";

		dlgTime = null;
		dlgDisplayOffTime = null;
		dlgAutoOffTime = null;
		dlgButtonMode = null;
		dlgButtonNotify = null;
		dlgAlertNotify = null;

		mAction = ActionState.Stop;
	}

	// ------------------------------------------------------------------------
	// Override Event Methods
	// ------------------------------------------------------------------------

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.default_option:
			getReader().defaultParameter();
			ATLog.i(TAG, INFO, "INFO. onClick(default_option)");
			break;
		}
	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
		
		
		switch ((int) id) {
		case OptionListAdapter.ITEM_SYSTEM_TIME:
			onReaderSystemTime();
			break;
		case OptionListAdapter.ITEM_DISPLAY_OFF_TIME:
			onReaderDisplayOffTime();
			break;
		case OptionListAdapter.ITEM_AUTO_OFF_TIME:
			onReaderAutoOffTime();
			break;
		case OptionListAdapter.ITEM_BUTTON_MODE:
			onReaderButtonMode();
			break;
		case OptionListAdapter.ITEM_BUTTON_NOTFIY:
			onReaderButtonNotify();
			break;
		case OptionListAdapter.ITEM_ALERT_NOTIFY:
			onReaderAlertNotify();
			break;
		}
		
		ATLog.i(TAG, INFO, "INFO. onChildClick(%d, %d, %d)", groupPosition, childPosition, id);
		return true;
	}

	// ------------------------------------------------------------------------
	// Event Process Internal Methods
	// ------------------------------------------------------------------------

	private void enableLstOption(boolean enable) {
		if(enable) {
			getActivity().runOnUiThread(new Runnable() {
	
				@Override
				public void run() {
					lstOption.setEnabled(true);
				}
			});
		} else {
			lstOption.setEnabled(false);
		}
	}
	
	private void onReaderSystemTime() {
		enableLstOption(false);
		dlgTime.showDialog(getActivity(), R.string.system_time, new BaseDialog.IValueChangedListener() {

			@Override
			public void onValueChanged(BaseDialog dialog) {
				try {
					getReader().setTime(dlgTime.getDateTime());
				} catch (ATException e) {
					ATLog.e(TAG, e, "ERROR. onReaderSystemTime() - Failed to set time [%s]",
							dlgTime.getDateTime().toString());
				}
				enableLstOption(true);
			}
		}, new BaseDialog.ICancelListener() {
			
			@Override
			public void onCanceled(BaseDialog dialog) {
				enableLstOption(true);
			}
		});
		ATLog.i(TAG, INFO, "INFO. onReaderSystemTime()");
	}

	private void onReaderDisplayOffTime() {
		enableLstOption(false);
		dlgDisplayOffTime.showDialog(getActivity(), R.string.display_off_time, new BaseDialog.IValueChangedListener() {

			@Override
			public void onValueChanged(BaseDialog dialog) {
				adpOption.display(OptionListAdapter.ITEM_DISPLAY_OFF_TIME, String.format(Locale.US, "%d %s",
						dlgDisplayOffTime.getValue(), getResources().getString(R.string.unit_sec)));
				adpOption.notifyDataSetChanged();
				try {
					((ATx88Reader) getReader()).setDisplayOffTime(dlgDisplayOffTime.getValue());
				} catch (ATException e) {
					ATLog.e(TAG, e, "ERROR. onReaderDisplayOffTime() - Failed to set display off time [%d]",
							dlgDisplayOffTime.getValue());
				}
				enableLstOption(true);
			}
		}, new BaseDialog.ICancelListener() {
			
			@Override
			public void onCanceled(BaseDialog dialog) {
				enableLstOption(true);
			}
		});
		ATLog.i(TAG, INFO, "INFO. onReaderDisplayOffTime()");
	}

	private void onReaderAutoOffTime() {
		enableLstOption(false);
		dlgAutoOffTime.showDialog(getActivity(), R.string.auto_off_time, new BaseDialog.IValueChangedListener() {

			@Override
			public void onValueChanged(BaseDialog dialog) {
				adpOption.display(OptionListAdapter.ITEM_AUTO_OFF_TIME, String.format(Locale.US, "%d %s",
						dlgAutoOffTime.getValue(), getResources().getString(R.string.unit_sec)));
				adpOption.notifyDataSetChanged();
				try {
					getReader().setAutoOffTime(dlgAutoOffTime.getValue());
				} catch (ATException e) {
					ATLog.e(TAG, e, "ERROR. onReaderAutoOffTime() - Failed to set auto off time [%d]",
							dlgAutoOffTime.getValue());
				}
				enableLstOption(true);
			}
		}, new BaseDialog.ICancelListener() {
			
			@Override
			public void onCanceled(BaseDialog dialog) {
				enableLstOption(true);
			}
		});
		ATLog.i(TAG, INFO, "INFO. onReaderAutoOffTime()");
	}

	private void onReaderButtonMode() {
		enableLstOption(false);
		dlgButtonMode.showDialog(getActivity(), R.string.button_mode, new BaseDialog.IValueChangedListener() {

			@Override
			public void onValueChanged(BaseDialog dialog) {
				adpOption.display(OptionListAdapter.ITEM_BUTTON_MODE, dlgButtonMode.getValue().toString());
				adpOption.notifyDataSetChanged();
				try {
					((ATn88Reader) getReader()).setButtonNotifyTime((NotifyTimeType) dlgButtonMode.getValue());
				} catch (ATException e) {
					ATLog.e(TAG, e, "ERROR. onReaderButtonMode() - Failed to set notify term [%s]",
							dlgButtonMode.getValue());
				}
				enableLstOption(true);
			}
		}, new BaseDialog.ICancelListener() {
			
			@Override
			public void onCanceled(BaseDialog dialog) {
				enableLstOption(true);
			}
		});
		ATLog.i(TAG, INFO, "INFO. onReaderButtonMode()");
	}

	private void onReaderButtonNotify() {
		enableLstOption(false);
		dlgButtonNotify.showDialog(getActivity(), R.string.button_notify, new BaseDialog.IValueChangedListener() {

			@Override
			public void onValueChanged(BaseDialog dialog) {
				adpOption.display(OptionListAdapter.ITEM_BUTTON_NOTFIY, dlgButtonNotify.getMethod().toString());
				adpOption.notifyDataSetChanged();
				try {
					((ATn88Reader) getReader()).setButtonNotify((NotifyMethod) dlgButtonNotify.getMethod());
				} catch (ATException e) {
					ATLog.e(TAG, e, "ERROR. onReaderButtonNotify() - Failed to set button notify [%s]",
							dlgButtonNotify.getMethod());
				}
				enableLstOption(true);
			}
		}, new BaseDialog.ICancelListener() {
			
			@Override
			public void onCanceled(BaseDialog dialog) {
				enableLstOption(true);
			}
		});
		ATLog.i(TAG, INFO, "INFO. onReaderButtonNotify()");
	}

	private void onReaderAlertNotify() {
		enableLstOption(false);
		dlgAlertNotify.showDialog(getActivity(), R.string.alert_notify, new BaseDialog.IValueChangedListener() {

			@Override
			public void onValueChanged(BaseDialog dialog) {
				adpOption.display(OptionListAdapter.ITEM_ALERT_NOTIFY, dlgAlertNotify.getMethod().toString());
				adpOption.notifyDataSetChanged();
				try {
					((ATn88Reader) getReader()).setAlertNotify((NotifyMethod) dlgAlertNotify.getMethod());
				} catch (ATException e) {
					ATLog.e(TAG, e, "ERROR. onReaderAlertNotify() - Failed to set alert notify [%s]",
							dlgAlertNotify.getMethod());
				}
				enableLstOption(true);
			}
		}, new BaseDialog.ICancelListener() {
			
			@Override
			public void onCanceled(BaseDialog dialog) {
				enableLstOption(true);
			}
		});
		ATLog.i(TAG, INFO, "INFO. onReaderAlertNotify()");
	}

	// ------------------------------------------------------------------------
	// Override Reader Event Methods
	// ------------------------------------------------------------------------

	@Override
	public void onReaderActionChanged(ATEAReader reader, ResultCode code, ActionState action, Object params) {

		if (mAction == ActionState.DefaultParameter && action == ActionState.Stop) {
			loadProperties();
		}
		mAction = action;
		ATLog.i(TAG, INFO, "EVENT. onReaderActionChanged([%s], %s, %s)", reader, code, action);
	}

	@Override
	public void onReaderReadTag(ATEAReader reader, String tag, Object params) {

		ATLog.i(TAG, INFO, "EVENT. onReaderReadTag([%s], [%s])", reader, tag);
	}

	@Override
	public void onReaderAccessResult(ATEAReader reader, ResultCode code, ActionState action, String epc, String data,
			Object params) {

		ATLog.i(TAG, INFO, "EVENT. onReaderAccessResult([%s], [%s], %s, [%s], [%s])", reader, code, action, epc, data);
	}

	@Override
	public void onReaderReadBarcode(ATEAReader reader, BarcodeType type, String codeId, String barcode, Object params) {

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
		return R.layout.view_option;
	}

	// Initialize View
	@Override
	protected void initView() {

		lstOption = (ExpandableListView) mView.findViewById(R.id.option_list);
		adpOption = new OptionListAdapter(getActivity());
		lstOption.setAdapter(adpOption);
		lstOption.setOnChildClickListener(this);
		for (int i = 0; i < adpOption.getGroupCount(); i++)
			lstOption.expandGroup(i);

		btnDefault = (Button) mView.findViewById(R.id.default_option);
		btnDefault.setOnClickListener(this);

		Resources res = getResources();
		String unitSec = res.getString(R.string.unit_sec);

		dlgTime = new DateTimeDialog();
		dlgDisplayOffTime = new NumberUnitDialog(unitSec);
		dlgAutoOffTime = new NumberUnitDialog(unitSec);
		dlgButtonMode = new EnumListDialog(NotifyTimeType.values());
		dlgButtonNotify = new NotifyMethodDialog();
		dlgAlertNotify = new NotifyMethodDialog();

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

		lstOption.setEnabled(enabled);
		btnDefault.setEnabled(enabled);

		ATLog.i(TAG, INFO, "INFO. enableWidgets(%s)", enabled);
	}

	// Loading Reader Properties
	@Override
	protected boolean loadingProperties() {

		// Disabled Use Key Action
		try {
			getReader().setUseActionKey(false);
		} catch (ATException e) {
			ATLog.e(TAG, "ERROR. loadingProperties() - Failed to disabled key action");
			return false;
		}

		// Load Firmware Version
		mVersion = getReader().getVersion();
		// Load Serial No
		try {
			mSerialNo = getReader().getSerialNo();
		} catch (ATException e) {
			ATLog.e(TAG, e, "ERROR. loadingProperties() - Failed to load serial no");
			return false;
		}
		// Load Time
		try {
			dlgTime.setDateTime(getReader().getTime());
		} catch (ATException e) {
			ATLog.e(TAG, e, "ERROR. loadingProperties() - Failed to load time");
			return false;
		}
		// Load Display Off Time
		try {
			dlgDisplayOffTime.setValue(((ATx88Reader) getReader()).getDisplayOffTime());
		} catch (ATException e) {
			ATLog.e(TAG, e, "ERROR. loadingProperties() - Failed to load display off time");
			return false;
		}
		// Load Auto Off Time
		try {
			dlgAutoOffTime.setValue(getReader().getAutoOffTime());
		} catch (ATException e) {
			ATLog.e(TAG, e, "ERROR. loadingProperties() - Failed to load auto off time");
			return false;
		}
		// Button Mode
		try {
			dlgButtonMode.setValue(((ATn88Reader) getReader()).getButtonNotifyTime());
		} catch (ATException e) {
			ATLog.e(TAG, e, "ERROR. loadingProperties() - Failed to load notify term");
			return false;
		}
		// Button Notify
		try {
			dlgButtonNotify.setMethod(((ATn88Reader) getReader()).getButtonNotify());
		} catch (ATException e) {
			ATLog.e(TAG, e, "ERROR. loadingProperties() - Failed to load button notify");
			return false;
		}
		// Alert Notify
		try {
			dlgAlertNotify.setMethod(((ATn88Reader) getReader()).getAlertNotify());

		} catch (ATException e) {
			ATLog.e(TAG, e, "ERROR. loadingProperties() - Failed to load alert notify");
			return false;
		}
		
		ATLog.i(TAG, INFO, "INFO. loadingProperties()");
		return true;
	}

	// Loaded Reader Properteis
	@Override
	protected void loadedProperties(boolean isInitialize) {

		if (isInitialize) {
			Resources res = getResources();
			String unitSec = res.getString(R.string.unit_sec);

			adpOption.display(OptionListAdapter.ITEM_FIRMWARE_VERSION, mVersion);
			adpOption.display(OptionListAdapter.ITEM_SERIAL_NO, mSerialNo);
			adpOption.display(OptionListAdapter.ITEM_DISPLAY_OFF_TIME,
					String.format(Locale.US, "%d %s", dlgDisplayOffTime.getValue(), unitSec));
			adpOption.display(OptionListAdapter.ITEM_AUTO_OFF_TIME,
					String.format(Locale.US, "%d %s", dlgAutoOffTime.getValue(), unitSec));
			adpOption.display(OptionListAdapter.ITEM_BUTTON_MODE, dlgButtonMode.getValue().toString());
			adpOption.display(OptionListAdapter.ITEM_BUTTON_NOTFIY, dlgButtonNotify.getMethod().toString());
			adpOption.display(OptionListAdapter.ITEM_ALERT_NOTIFY, dlgAlertNotify.getMethod().toString());

			adpOption.notifyDataSetChanged();
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
