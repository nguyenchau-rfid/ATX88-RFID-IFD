package com.atid.app.atx.activity.fragment;

import com.atid.app.atx.R;
import com.atid.app.atx.activity.view.BaseView;
import com.atid.app.atx.adapter.MainMenuListAdapter;
import com.atid.lib.util.diagnotics.ATLog;

import android.app.Activity;
import android.app.Fragment;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class MainMenuFragment extends Fragment implements OnItemClickListener {

	private static final String TAG = MainMenuFragment.class.getSimpleName();
	private static final int INFO = ATLog.L1;

	// ------------------------------------------------------------------------
	// Member Variable
	// ------------------------------------------------------------------------

	private DrawerLayout mDrawerLayout;
	private View mFragmentContainerView;

	private LinearLayout mMenu;
	private TextView txtVersion;
	private TextView txtDevName;
	private TextView txtAddress;
	private ListView lstMenus;

	private MainMenuListAdapter adpMenus;

	private IMainMenuListener mListener;

	private ActionBarDrawerToggle mToggle;
	
	@SuppressWarnings("unused")
	private boolean mIsAvailableBarcode;
	private boolean mIsAvailableRfidUhf;

	// ------------------------------------------------------------------------
	// Overridable Methods
	// ------------------------------------------------------------------------

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ATLog.i(TAG, INFO, "INFO. onCreate()");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		mMenu = (LinearLayout) inflater.inflate(R.layout.fragment_main_menu, container, false);

		txtVersion = (TextView) mMenu.findViewById(R.id.firmware_version);

		txtDevName = (TextView) mMenu.findViewById(R.id.device_name);

		txtAddress = (TextView) mMenu.findViewById(R.id.device_address);

		lstMenus = (ListView) mMenu.findViewById(R.id.menu_list);
		adpMenus = new MainMenuListAdapter(getActivity());
		lstMenus.setAdapter(adpMenus);
		lstMenus.setOnItemClickListener(this);
		adpMenus.setSelection(MainMenuListAdapter.POS_INVENTORY);
		
		mIsAvailableBarcode = false;
		mIsAvailableRfidUhf = false;

		ATLog.i(TAG, INFO, "INFO. onCreateView()");
		return mMenu;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mListener = (IMainMenuListener) activity;
		ATLog.i(TAG, INFO, "INFO. onAttach()");
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
		ATLog.i(TAG, INFO, "INFO. onDetach()");
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
		boolean skipEvent = (mIsAvailableRfidUhf == false) && 
				(id == BaseView.VIEW_ACCESS_MEMORY );
		
		if(!skipEvent) {
			adpMenus.setSelection(position);
			if (mListener != null) {
				mListener.onMainMenuSelected((int) adpMenus.getItemId(position));
			}
		}
		mDrawerLayout.closeDrawer(mFragmentContainerView);
		ATLog.i(TAG, INFO, "INFO. onItemClick(%d)", position);
	}

	// ------------------------------------------------------------------------
	// Operation Methods
	// ------------------------------------------------------------------------

	public void initMenu(String version, String devName, String address, boolean isAvailableBarcode, boolean isAvailableRfidUhf) {

		txtVersion.setText(version);
		txtDevName.setText(devName);
		txtAddress.setText(address);
		
		mIsAvailableBarcode = isAvailableBarcode;
		mIsAvailableRfidUhf = isAvailableRfidUhf;

		mFragmentContainerView = getActivity().findViewById(R.id.main_menu_fragement);
		mDrawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

		mToggle = new ActionBarDrawerToggle(getActivity(), mDrawerLayout, R.drawable.ic_drawer, R.string.view_inventory,
				R.string.view_stored_data) {

			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
			}

		};
		mDrawerLayout.setDrawerListener(mToggle);
		ATLog.i(TAG, INFO, "INFO. initMenu()");
	}

	public void onTogglePostCreate(Bundle savedInstanceState) {
		mToggle.syncState();
	}

	public void onToggleConfigurationChanged(Configuration newConfig) {
		mToggle.onConfigurationChanged(newConfig);
	}

	public boolean onToggleOptionsItemSelected(MenuItem item) {
		return mToggle.onOptionsItemSelected(item);
	}
	
	public void toggleMenu() {
		if (mDrawerLayout.isDrawerOpen(mFragmentContainerView)) {
			mDrawerLayout.closeDrawer(mFragmentContainerView);
		} else {
			mDrawerLayout.openDrawer(mFragmentContainerView);
		}
	}

	public void setSelectMenu(int id) {
		int position = adpMenus.indexOf(id);
		adpMenus.setSelection(position);
		if (mListener != null) {
			mListener.onMainMenuSelected(id);
		}
	}

	public int getSelectMenu() {
		return adpMenus.getSelectedItemId();
	}
	
	// ------------------------------------------------------------------------
	// Declare Interface IMainMenuListener
	// ------------------------------------------------------------------------

	public static interface IMainMenuListener {
		void onMainMenuSelected(int id);
	}
}
