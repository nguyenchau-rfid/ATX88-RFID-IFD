package com.atid.app.atx.dialog;

import com.atid.lib.util.diagnotics.ATLog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;

@SuppressWarnings("deprecation")
public class WaitDialog {
	
	private static final String TAG = WaitDialog.class.getSimpleName();
	private static final int INFO = ATLog.L2;
	
	private static ProgressDialog dlg = null;

	// Show wait dialog
	public static void show(Context context, int style, String title, String message,
			OnCancelListener listener) {
		hide();
		dlg = new ProgressDialog(context);
		dlg.setProgressStyle(style);
		if (null != title) {
			dlg.setTitle(title);

		}
		dlg.setMessage(message);
		if (null != listener) {
			dlg.setCancelable(true);
			dlg.setOnCancelListener(listener);
		} else {
			dlg.setCancelable(false);
		}
		dlg.show();

		ATLog.i(TAG, INFO, "show(%d, [%s], [%s])", style, title, message);
	}

	public static void show(Context context, int title, int message,
			OnCancelListener listener) {
		show(context, ProgressDialog.STYLE_SPINNER, context.getResources().getString(title), context
				.getResources().getString(message), listener);
	}

	public static void show(Context context, int title, int message) {
		show(context, ProgressDialog.STYLE_SPINNER, context.getResources().getString(title), context
				.getResources().getString(message), null);
	}

	public static void show(Context context, int message,
			OnCancelListener listener) {
		show(context, ProgressDialog.STYLE_SPINNER, null, context.getResources().getString(message), listener);
	}

	public static void show(Context context, int message) {
		show(context, ProgressDialog.STYLE_SPINNER, null, context.getResources().getString(message), null);
	}

	public static void show(Context context, String title, String message) {
		show(context, ProgressDialog.STYLE_SPINNER, title, message, null);
	}

	public static void show(Context context, String message,
			OnCancelListener listener) {
		show(context, ProgressDialog.STYLE_SPINNER, null, message, listener);
	}

	public static void show(Context context, String message) {
		show(context, ProgressDialog.STYLE_SPINNER, null, message, null);
	}

	public static void showProgess(Context context, int title, int message,
			OnCancelListener listener) {
		show(context, ProgressDialog.STYLE_HORIZONTAL, context.getResources().getString(title), context
				.getResources().getString(message), listener);
	}

	public static void showProgess(Context context, int title, int message) {
		show(context, ProgressDialog.STYLE_HORIZONTAL, context.getResources().getString(title), context
				.getResources().getString(message), null);
	}

	public static void showProgess(Context context, int message,
			OnCancelListener listener) {
		show(context, ProgressDialog.STYLE_HORIZONTAL, null, context.getResources().getString(message), listener);
	}

	public static void showProgess(Context context, int message) {
		show(context, ProgressDialog.STYLE_HORIZONTAL, null, context.getResources().getString(message), null);
	}

	public static void showProgess(Context context, String title, String message) {
		show(context, ProgressDialog.STYLE_HORIZONTAL, title, message, null);
	}

	public static void showProgess(Context context, String message,
			OnCancelListener listener) {
		show(context, ProgressDialog.STYLE_HORIZONTAL, null, message, listener);
	}

	public static void showProgess(Context context, String message) {
		show(context, ProgressDialog.STYLE_HORIZONTAL, null, message, null);
	}
	
	public static void setMax(int max) {
		if (null == dlg)
			return;
		dlg.setMax(max);
	}
	
	public static void setProgress(int value) {
		if (null == dlg)
			return;
		
		dlg.setProgress(value);
	}

	// Hide wait dialog
	public static void hide() {
		if (null == dlg)
			return;
		dlg.dismiss();
		dlg = null;
		ATLog.i(TAG, INFO, "hide()");
	}
}
