package com.atid.app.atx.activity;

import java.util.ArrayList;

import com.atid.lib.util.diagnotics.ATLog;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

@SuppressLint("NewApi")
public abstract class PermissionActivity extends Activity {

	protected static final int INFO = ATLog.L1;

	private static final int REQUEST_PERMISSION_CONTACTS = 1000;

	protected String TAG;

	protected final String[] mPermissions;

	public PermissionActivity() {
		super();

		TAG = PermissionActivity.class.getSimpleName();

		mPermissions = new String[] { Manifest.permission.WAKE_LOCK, Manifest.permission.WRITE_EXTERNAL_STORAGE,
				Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION,
				Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.BLUETOOTH_ADMIN,
				Manifest.permission.BLUETOOTH, };
	}

	protected boolean checkPermission() {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
			onPermissionsResult(true);
			return false;
		}

		ArrayList<String> permissions = new ArrayList<String>();
		int grantResult = PackageManager.PERMISSION_GRANTED;

		for (int i = 0; i < mPermissions.length; i++) {
			grantResult = PackageManager.PERMISSION_GRANTED;
			grantResult = ContextCompat.checkSelfPermission(this, mPermissions[i]);
			if (grantResult == PackageManager.PERMISSION_DENIED) {
				permissions.add(mPermissions[i]);
			}
		}
		if (permissions.size() <= 0) {
			onPermissionsResult(true);
			return false;
		}

		String[] requestPermissions = new String[permissions.size()];
		permissions.toArray(requestPermissions);
		ActivityCompat.requestPermissions(this, permissions.toArray(requestPermissions), REQUEST_PERMISSION_CONTACTS);

		ATLog.i(TAG, INFO, "INFO. checkPermission()");
		return true;
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		if (requestCode != REQUEST_PERMISSION_CONTACTS)
			return;
		
		boolean result = true;
		for (int i = 0; i < grantResults.length; i++) {
			result &= grantResults[i] != PackageManager.PERMISSION_DENIED;
		}
		
		onPermissionsResult(result);
	}
	
	protected abstract void onPermissionsResult(boolean result);
}
