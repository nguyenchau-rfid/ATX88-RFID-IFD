package com.atid.app.atx.dialog;

import com.atid.app.atx.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;

@SuppressLint("NewApi")
public class YesNoMessageBox {

	public static void show(Context context, int msgId, int titleId, int iconId, int okTextId,
			DialogInterface.OnClickListener okListener, int cancelTextId,
			final DialogInterface.OnCancelListener cancelListener) {
		MessageBox.show(context, msgId, titleId, iconId, okTextId, okListener, cancelTextId, cancelListener);
	}

	public static void show(Context context, int msgId, int titleId, int iconId, int okTextId,
			DialogInterface.OnClickListener okListener, DialogInterface.OnCancelListener cancelListener) {
		show(context, msgId, titleId, iconId, okTextId, okListener, R.string.action_no, cancelListener);
	}

	public static void show(Context context, int msgId, int titleId, int iconId, int okTextId,
			DialogInterface.OnClickListener okListener, int cancelTextId) {
		show(context, msgId, titleId, iconId, okTextId, okListener, cancelTextId, null);
	}

	public static void show(Context context, int msgId, int titleId, int iconId,
			DialogInterface.OnClickListener okListener, DialogInterface.OnCancelListener cancelListener) {
		show(context, msgId, titleId, iconId, R.string.action_yes, okListener, R.string.action_no, cancelListener);
	}

	public static void show(Context context, int msgId, int titleId, int iconId, int okTextId,
			DialogInterface.OnClickListener okListener) {
		show(context, msgId, titleId, iconId, okTextId, okListener, R.string.action_no, null);
	}

	public static void show(Context context, int msgId, int titleId, int iconId,
			DialogInterface.OnClickListener okListener, int cancelTextId) {
		show(context, msgId, titleId, iconId, R.string.action_yes, okListener, cancelTextId, null);
	}

	public static void show(Context context, int msgId, int titleId, DialogInterface.OnClickListener okListener,
			int cancelTextId) {
		show(context, msgId, titleId, android.R.drawable.ic_menu_help, R.string.action_yes, okListener, cancelTextId,
				null);
	}

	public static void show(Context context, int msgId, int titleId, DialogInterface.OnClickListener okListener,
			DialogInterface.OnCancelListener cancelListener) {
		show(context, msgId, titleId, android.R.drawable.ic_menu_help, R.string.action_yes, okListener,
				R.string.action_no, cancelListener);
	}

	public static void show(Context context, int msgId, int titleId, DialogInterface.OnClickListener okListener) {
		show(context, msgId, titleId, android.R.drawable.ic_menu_help, R.string.action_yes, okListener,
				R.string.action_no, null);
	}

	public static void show(Context context, int msgId, int titleId, int iconId,
			DialogInterface.OnClickListener okListener) {
		show(context, msgId, titleId, iconId, R.string.action_yes, okListener, R.string.action_no, null);
	}

	public static void show(Context context, int msgId, int titleId, int iconId) {
		show(context, msgId, titleId, iconId, R.string.action_yes, null, R.string.action_no, null);
	}

	public static void show(Context context, int msgId, int titleId) {
		show(context, msgId, titleId, android.R.drawable.ic_menu_help, R.string.action_yes, null, R.string.action_no,
				null);
	}

	public static void show(Context context, int msgId) {
		show(context, msgId, 0, 0, R.string.action_yes, null, R.string.action_no, null);
	}

	public static void show(Context context, String msg, String title, Drawable icon, String okText,
			DialogInterface.OnClickListener okListener, String cancelText,
			final DialogInterface.OnCancelListener cancelListener) {
		MessageBox.show(context, msg, title, icon, okText, okListener, cancelText, cancelListener);
	}

	public static void show(Context context, String msg, String title, Drawable icon, String okText,
			DialogInterface.OnClickListener okListener, final DialogInterface.OnCancelListener cancelListener) {
		show(context, msg, title, icon, okText, okListener, context.getResources().getString(R.string.action_no),
				cancelListener);
	}

	public static void show(Context context, String msg, String title, Drawable icon, String okText,
			DialogInterface.OnClickListener okListener, String cancelText) {
		show(context, msg, title, icon, okText, okListener, cancelText, null);
	}

	public static void show(Context context, String msg, String title, Drawable icon,
			DialogInterface.OnClickListener okListener, DialogInterface.OnCancelListener cancelListener) {
		show(context, msg, title, icon, context.getResources().getString(R.string.action_yes), okListener,
				context.getResources().getString(R.string.action_no), cancelListener);
	}

	public static void show(Context context, String msg, String title, Drawable icon, String okText,
			DialogInterface.OnClickListener okListener) {
		show(context, msg, title, icon, okText, okListener, context.getResources().getString(R.string.action_no), null);
	}

	public static void show(Context context, String msg, String title, Drawable icon,
			DialogInterface.OnClickListener okListener, String cancelTextId) {
		show(context, msg, title, icon, context.getResources().getString(R.string.action_yes), okListener, cancelTextId,
				null);
	}

	public static void show(Context context, String msg, String title, Drawable icon,
			DialogInterface.OnClickListener okListener) {
		show(context, msg, title, icon, context.getResources().getString(R.string.action_yes), okListener,
				context.getResources().getString(R.string.action_no), null);
	}

	public static void show(Context context, String msg, String title, DialogInterface.OnClickListener okListener) {
		show(context, msg, title,
				context.getResources().getDrawable(android.R.drawable.ic_menu_help, context.getTheme()),
				context.getResources().getString(R.string.action_yes), okListener,
				context.getResources().getString(R.string.action_no), null);
	}

	public static void show(Context context, String msg, String title, Drawable icon) {
		show(context, msg, title, icon, context.getResources().getString(R.string.action_yes), null,
				context.getResources().getString(R.string.action_no), null);
	}

	public static void show(Context context, String msg, String title) {
		show(context, msg, title,
				context.getResources().getDrawable(android.R.drawable.ic_menu_help, context.getTheme()),
				context.getResources().getString(R.string.action_yes), null,
				context.getResources().getString(R.string.action_no), null);
	}

	public static void show(Context context, String msg) {
		show(context, msg, "", null, context.getResources().getString(R.string.action_yes), null,
				context.getResources().getString(R.string.action_no), null);
	}
}
