package com.atid.app.atx.activity;

import com.atid.app.atx.R;
import com.atid.app.atx.activity.fragment.MainMenuFragment;
import com.atid.app.atx.activity.view.BaseView;
import com.atid.app.atx.activity.view.BaseView.IATEAReader;
import com.atid.app.atx.activity.view.ViewAccessMemory;
import com.atid.app.atx.activity.view.ViewInventory;
import com.atid.app.atx.activity.view.ViewOption;
import com.atid.app.atx.activity.view.ViewStoredData;
import com.atid.app.atx.adapter.MainMenuListAdapter;
import com.atid.app.atx.data.Constants;
import com.atid.app.atx.data.GlobalData;
import com.atid.app.atx.dialog.MessageBox;
import com.atid.lib.diagnostics.ATException;
import com.atid.lib.module.barcode.ATBarcode;
import com.atid.lib.module.barcode.event.IATBarcodeEventListener;
import com.atid.lib.module.barcode.types.BarcodeType;
import com.atid.lib.module.rfid.uhf.ATRfidUhf;
import com.atid.lib.module.rfid.uhf.event.IATRfidUhfEventListener;
import com.atid.lib.reader.ATEAReader;
import com.atid.lib.reader.event.IATEAReaderEventListener;
import com.atid.lib.reader.types.KeyState;
import com.atid.lib.reader.types.KeyType;
import com.atid.lib.reader.types.OperationMode;
import com.atid.lib.transport.types.ConnectState;
import com.atid.lib.types.ActionState;
import com.atid.lib.types.ResultCode;
import com.atid.lib.util.diagnotics.ATLog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class MainActivity extends Activity
		implements MainMenuFragment.IMainMenuListener, IATEAReader, IATEAReaderEventListener,
		ViewInventory.ISetWriteMemoryListener, IATRfidUhfEventListener, IATBarcodeEventListener {

	private static final String TAG = MainActivity.class.getSimpleName();
	private static final int INFO = ATLog.L1;
	public static final int ID = 0x02101000;

	// ------------------------------------------------------------------------
	// Member Variable
	// ------------------------------------------------------------------------

	private MainMenuFragment mMainMenu;
	private BaseView[] mViews;
	private String[] mTitles;
	private ATEAReader mReader;
	private TextView txtFindProduct;
	private TextView txtPin;
	// ------------------------------------------------------------------------
	// Overridable Event Methods
	// ------------------------------------------------------------------------

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.activity_main);
		txtFindProduct=(TextView)findViewById(R.id.find_product);
		txtPin=findViewById(R.id.device_battery);

		getActionBar().setHomeButtonEnabled(true);

		int position = getIntent().getIntExtra(Constants.SELECTED_READER, 0);
		mReader = GlobalData.ReaderManager.get(position);
		mReader.addListener(this);
		if (mReader.getRfidUhf() != null) {
			mReader.getRfidUhf().addListener(this);
		}
		if (mReader.getBarcode() != null) {
			mReader.getBarcode().addListener(this);
		}

		mTitles = getResources().getStringArray(R.array.view_name);

		mViews = new BaseView[] { new ViewInventory(), new ViewStoredData(), new ViewAccessMemory(), 
				new ViewOption(),new ViewInventory() };
		((ViewInventory) getView(BaseView.VIEW_INVENTORY)).setSetWriteMemoryListener(this);
		mMainMenu = (MainMenuFragment) getFragmentManager().findFragmentById(R.id.main_menu_fragement);
		mMainMenu.initMenu(mReader.getVersion(), mReader.getDeviceName(), mReader.getAddress(),
							mReader.getBarcode() != null, mReader.getRfidUhf() != null);

		mMainMenu.setSelectMenu(BaseView.VIEW_INVENTORY);
		try {
			if(mReader.getBatteryState()<30)
				Toast.makeText(getApplicationContext(), "Dung lượng pin của máy quét còn ít..vui lòng sạc thêm pin cho thiết bị...", Toast.LENGTH_LONG).show();
			txtPin.setText(mReader.getBatteryState()+"%");
		} catch (ATException e) {
			e.printStackTrace();
		}
		txtFindProduct.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//closeView();
				mMainMenu.toggleMenu();
				((ViewInventory) getView(BaseView.VIEW_INVENTORY)).FindProduct();

			}
		});
		ATLog.i(TAG, INFO, "INFO. onCreate()");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
		super.onPostCreate(savedInstanceState, persistentState);
		mMainMenu.onTogglePostCreate(savedInstanceState);
	}

	@Override
	protected void onDestroy() {

		ATLog.i(TAG, INFO, "INFO. onDestroy()");
		super.onDestroy();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mMainMenu.onToggleConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case android.R.id.home:
			closeView();
			this.finish();
			return true;
		case R.id.menu_item:
			mMainMenu.toggleMenu();
			return true;

		}

		ATLog.i(TAG, INFO, "INFO. onOptionsItemSelected()");
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {

		closeView();

		ATLog.i(TAG, INFO, "INFO. onBackPressed()");
		super.onBackPressed();
	}

	@Override
	public void onMainMenuSelected(int id) {
		
		replaceView(id);

		ATLog.i(TAG, INFO, "INFO. onMainMenuSelected(%d)", id);
	}

	@Override
	public void onSetWriteMemory(String data) {

		((ViewAccessMemory) getView(BaseView.VIEW_ACCESS_MEMORY)).setWriteData(data);
		mMainMenu.setSelectMenu(BaseView.VIEW_ACCESS_MEMORY);
		
		ATLog.i(TAG, INFO, "INFO. onSetWriteMemory([%s])", data);
	}

	@Override
	public ATEAReader getReader() {
		return mReader;
	}

	// ------------------------------------------------------------------------
	// Reader Event Methods
	// ------------------------------------------------------------------------

	@Override
	public void onReaderStateChanged(ATEAReader reader, ConnectState state, Object params) {

		if (state == ConnectState.Disconnected) {
			mReader.removeListener(this);
			setResult(Activity.RESULT_FIRST_USER);
			finish();
		}
		ATLog.i(TAG, INFO, "EVENT. onReaderStateChanged([%s], %s)", reader, state);
	}

	@Override
	public void onReaderActionChanged(ATEAReader reader, ResultCode code, ActionState action, Object params) {

		int id = mMainMenu.getSelectMenu();
		BaseView view = getView(id);
		if (view != null)
			view.onReaderActionChanged(reader, code, action, params);

		ATLog.i(TAG, INFO, "EVENT. onReaderActionChanged([%s], %s, %s)", reader, code, action);
	}

	@Override
	public void onReaderOperationModeChanged(ATEAReader reader, OperationMode mode, Object params) {
		
		int id = mMainMenu.getSelectMenu();
		BaseView view = getView(id);
		if (view != null)
			view.onReaderOperationModeChanged(reader, mode, params);

		ATLog.i(TAG, INFO, "EVENT. onReaderOperationModeChanged([%s], %s)", reader, mode);
	}

	@Override
	public void onReaderBatteryState(ATEAReader reader, int batteryState, Object params) {

		ATLog.i(TAG, INFO, "EVENT. onReaderBatteryState([%s], %d)", reader, batteryState);
	}

	@Override
	public void onReaderKeyChanged(ATEAReader reader, KeyType type, KeyState state, 
			Object params) {
		
		int id = mMainMenu.getSelectMenu();
		BaseView view = getView(id);
		if (view != null)
			view.onReaderKeyChanged(reader, type, state, params);
		
		ATLog.i(TAG, INFO, "EVENT. onReaderKeyChanged([%s], %s, %s)", reader, type, state);
	}

	@Override
	public void onBarcodeReadData(ATBarcode barcode, BarcodeType type, String codeId, String data, Object params) {

		int id = mMainMenu.getSelectMenu();
		BaseView view = getView(id);
		if (view != null)
			view.onReaderReadBarcode(mReader, type, codeId, data, params);

		ATLog.i(TAG, INFO, "EVENT. onReaderReadBarcode([%s], %s, [%s], [%s])", mReader, type, codeId, barcode);
	}

	@Override
	public void onRfidUhfReadTag(ATRfidUhf uhf, String tag, Object params) {
		int id = mMainMenu.getSelectMenu();
		BaseView view = getView(id);
		if (view != null)
			view.onReaderReadTag(mReader, tag, params);

		ATLog.i(TAG, INFO, "EVENT. onRfidUhfReadTag([%s], [%s])", mReader, tag);
	}

	@Override
	public void onRfidUhfAccessResult(ATRfidUhf uhf, ResultCode code, ActionState action, String epc, String data,
			Object params) {

		int id = mMainMenu.getSelectMenu();
		BaseView view = getView(id);
		if (view != null)
			view.onReaderAccessResult(mReader, code, action, epc, data, params);

		ATLog.i(TAG, INFO, "EVENT. onRfidUhfAccessResult([%s], [%s], %s, [%s], [%s])", mReader, code, action, epc,
				data);
	}

	@Override
	public void onRfidUhfPowerGainChanged(ATRfidUhf uhf, int power, Object params) {

		int id = mMainMenu.getSelectMenu();
		BaseView view = getView(id);
		if (view != null)
			view.onReaderPowerGainChanged(mReader, power, params);

		ATLog.i(TAG, INFO, "EVENT. onRfidUhfPowerGainChanged([%s], %d)", mReader, power);
	}

	// ------------------------------------------------------------------------
	// Internal Helper Methods
	// ------------------------------------------------------------------------

	private void closeView() {

		if (getReader() == null)
			return;

		if (getReader().getAction() != ActionState.Stop) {
			// getReader().stop();
		}
		getReader().removeListener(this);

		ATLog.i(TAG, INFO, "INFO. closeView()");
	}

	private void replaceView(int id) {

		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.container, getView(id));
		ft.commit();

		getActionBar().setIcon(getIcon(id));
		setTitle(getTitle(id));

		ATLog.i(TAG, INFO, "INFO. replaceView(%d)", id);
	}

	private BaseView getView(int id) {
		switch (id) {
		case BaseView.VIEW_INVENTORY:
			return mViews[MainMenuListAdapter.POS_INVENTORY];
		case BaseView.VIEW_STORED_DATA:
			return mViews[MainMenuListAdapter.POS_STORED_DATA];
		case BaseView.VIEW_ACCESS_MEMORY:
			return mViews[MainMenuListAdapter.POS_ACCESS_MEMORY];
		case BaseView.VIEW_OPTION:
			return mViews[MainMenuListAdapter.POS_OPTION];


		}
		return null;
	}

	private String getTitle(int id) {
		switch (id) {
		case BaseView.VIEW_INVENTORY:
			return mTitles[MainMenuListAdapter.POS_INVENTORY];
		case BaseView.VIEW_STORED_DATA:
			return mTitles[MainMenuListAdapter.POS_STORED_DATA];
		case BaseView.VIEW_ACCESS_MEMORY:
			return mTitles[MainMenuListAdapter.POS_ACCESS_MEMORY];
		case BaseView.VIEW_OPTION:
			return mTitles[MainMenuListAdapter.POS_OPTION];

		}
		return "";
	}

	private int getIcon(int id) {
		switch (id) {
		case BaseView.VIEW_INVENTORY:
			return R.drawable.menu_inventory;
		case BaseView.VIEW_STORED_DATA:
			return R.drawable.menu_stored_data;
		case BaseView.VIEW_ACCESS_MEMORY:
			return R.drawable.menu_access_memory;
		case BaseView.VIEW_OPTION:
			return R.drawable.menu_option;

		}
		return 0;
	}
}
