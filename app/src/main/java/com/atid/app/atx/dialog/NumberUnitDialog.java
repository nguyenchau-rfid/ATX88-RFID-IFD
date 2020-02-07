package com.atid.app.atx.dialog;

import java.util.Locale;

import com.atid.app.atx.R;
import com.atid.lib.util.StringUtil;
import com.atid.lib.util.diagnotics.ATLog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NumberUnitDialog extends IntegerDialog {

	private static final String TAG = NumberUnitDialog.class.getSimpleName();
	private static final int INFO = ATLog.L2;

	private String mUnit;

	public NumberUnitDialog(String unit) {
		super();
		mUnit = unit;
	}

	public NumberUnitDialog(TextView view, String unit) {
		super(view);
		mUnit = unit;
	}

	public void setUnit(String unit) {
		mUnit = unit;
	}

	@Override
	public void display() {

		if (txtValue == null)
			return;

		txtValue.setText(String.format(Locale.US, "%d %s", mValue, mUnit));
	}

	@Override
	public void showDialog(Context context, String title, final IValueChangedListener changedListener,
			final ICancelListener cancelListener) {

		if (txtValue != null) {
			if (!txtValue.isEnabled())
				return;
		}

		final InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		LinearLayout root = (LinearLayout) LinearLayout.inflate(context, R.layout.dialog_num_unit, null);
		final EditText edtVal = (EditText) root.findViewById(R.id.value);
		TextView txtUnit = (TextView) root.findViewById(R.id.unit);
		txtUnit.setText(mUnit);

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setView(root);
		builder.setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				imm.hideSoftInputFromWindow(edtVal.getWindowToken(), 0);
				mValue = StringUtil.toInteger(edtVal.getText().toString());
				display();
				if (changedListener != null) {
					changedListener.onValueChanged(NumberUnitDialog.this);
				}
				ATLog.i(TAG, INFO, "INFO. showDialog().$PositiveButton.onClick()");
			}
		});
		builder.setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				imm.hideSoftInputFromWindow(edtVal.getWindowToken(), 0);
				if (cancelListener != null) {
					cancelListener.onCanceled(NumberUnitDialog.this);
				}
				ATLog.i(TAG, INFO, "INFO. showDialog().$NegativeButton.onClick()");
			}
		});
		builder.setCancelable(true);
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				imm.hideSoftInputFromWindow(edtVal.getWindowToken(), 0);
				if (cancelListener != null) {
					cancelListener.onCanceled(NumberUnitDialog.this);
				}
				ATLog.i(TAG, INFO, "INFO. showDialog().onCancel()");
			}
		});
		AlertDialog dialog = builder.create();
		dialog.setOnShowListener(new DialogInterface.OnShowListener() {

			@Override
			public void onShow(DialogInterface dialog) {
				edtVal.setText(String.format(Locale.US, "%d", mValue));
				edtVal.selectAll();
				edtVal.requestFocus();
				imm.showSoftInput(edtVal, InputMethodManager.SHOW_FORCED);
				ATLog.i(TAG, INFO, "INFO. showDialog().onShow()");
			}
		});
		dialog.show();

		ATLog.i(TAG, INFO, "INFO. showDialog()");
	}
}
