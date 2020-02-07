package com.atid.app.atx.dialog;

import java.util.Calendar;
import java.util.Date;

import com.atid.app.atx.R;
import com.atid.lib.util.diagnotics.ATLog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.icu.text.SimpleDateFormat;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

@SuppressLint("NewApi")
public class DateTimeDialog extends BaseDialog {

	private static final String TAG = DateTimeDialog.class.getSimpleName();
	private static final int INFO = ATLog.L2;

	private Date mDateTime;

	public DateTimeDialog() {
		super();

		mDateTime = new Date();
	}

	public DateTimeDialog(TextView view) {
		super(view);

		mDateTime = new Date();
	}

	public Date getDateTime() {
		return mDateTime;
	}

	public void setDateTime(Date time) {
		mDateTime = time;
	}

	@Override
	public void display() {

		if (txtValue == null)
			return;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		txtValue.setText(sdf.format(mDateTime));
	}

	@Override
	public void showDialog(Context context, String title, final IValueChangedListener changedListener,
			final ICancelListener cancelListener) {

		if (txtValue != null) {
			if (!txtValue.isEnabled())
				return;
		}

		LinearLayout root = (LinearLayout) LinearLayout.inflate(context, R.layout.dialog_date_time, null);
		final DatePicker date = (DatePicker) root.findViewById(R.id.date);
		final TimePicker time = (TimePicker) root.findViewById(R.id.time);

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setView(root);
		builder.setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Calendar cal = Calendar.getInstance();
				cal.set(date.getYear(), date.getMonth(), date.getDayOfMonth(), time.getCurrentHour(),
						time.getCurrentMinute());
				mDateTime = cal.getTime();
				display();
				if (changedListener != null) {
					changedListener.onValueChanged(DateTimeDialog.this);
				}
				ATLog.i(TAG, INFO, "INFO. showDialog().$PositiveButton.onClick()");
			}
		});
		builder.setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (cancelListener != null) {
					cancelListener.onCanceled(DateTimeDialog.this);
				}
				ATLog.i(TAG, INFO, "INFO. showDialog().$NegativeButton.onClick()");
			}

		});
		builder.setCancelable(true);
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				if (cancelListener != null) {
					cancelListener.onCanceled(DateTimeDialog.this);
				}
				ATLog.i(TAG, INFO, "INFO. showDialog().onCancel()");
			}
		});
		AlertDialog dialog = builder.create();
		dialog.setOnShowListener(new DialogInterface.OnShowListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onShow(DialogInterface dialog) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(mDateTime);
				date.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), null);
				time.setCurrentHour(cal.get(Calendar.HOUR));
				time.setCurrentMinute(cal.get(Calendar.MINUTE));
			}
		});
		dialog.show();

		ATLog.i(TAG, INFO, "INFO. showDialog()");
	}

}
