package com.atid.app.atx.dialog;

import com.atid.app.atx.R;
import com.atid.lib.reader.params.NotifyMethod;
import com.atid.lib.util.diagnotics.ATLog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.SparseBooleanArray;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class NotifyMethodDialog extends BaseDialog {

	private static final String TAG = NotifyMethodDialog.class.getSimpleName();
	private static final int INFO = ATLog.L2;

	private NotifyMethod mMethod;

	public NotifyMethodDialog() {
		super();
		mMethod = new NotifyMethod();
	}

	public NotifyMethodDialog(TextView view) {
		super(view);
		mMethod = new NotifyMethod();
	}

	public NotifyMethod getMethod() {
		return mMethod;
	}

	public void setMethod(NotifyMethod method) {
		mMethod = method;
	}

	@Override
	public void display() {
		if (txtValue == null)
			return;
		txtValue.setText(mMethod.toString());
	}

	@Override
	public void showDialog(Context context, String title, final IValueChangedListener changedListener,
			final ICancelListener cancelListener) {

		if (txtValue != null) {
			if (!txtValue.isEnabled())
				return;
		}

		LinearLayout root = (LinearLayout) LinearLayout.inflate(context, R.layout.dialog_list_view, null);
		final ListView list = (ListView) root.findViewById(R.id.list);
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
				android.R.layout.simple_list_item_multiple_choice,
				context.getResources().getStringArray(R.array.notify_method));
		list.setAdapter(adapter);
		list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setView(root);
		builder.setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				SparseBooleanArray checkedItems = list.getCheckedItemPositions();
				for (int i = 0; i < checkedItems.size(); i++) {
					mMethod.setMethods(i, checkedItems.get(i));
				}
				display();
				if (changedListener != null) {
					changedListener.onValueChanged(NotifyMethodDialog.this);
				}
				ATLog.i(TAG, INFO, "INFO. showDialog().$PositiveButton.onClick()");
			}
		});
		builder.setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (cancelListener != null) {
					cancelListener.onCanceled(NotifyMethodDialog.this);
				}
				ATLog.i(TAG, INFO, "INFO. showDialog().$NegativeButton.onClick()");
			}
		});
		builder.setCancelable(true);
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				if (cancelListener != null) {
					cancelListener.onCanceled(NotifyMethodDialog.this);
				}
				ATLog.i(TAG, INFO, "INFO. showDialog().onCancel()");
			}
		});
		AlertDialog dialog = builder.create();
		dialog.setOnShowListener(new DialogInterface.OnShowListener() {

			@Override
			public void onShow(DialogInterface dialog) {
				for (int i = 0; i < NotifyMethod.MAX_METHOD; i++) {
					list.setItemChecked(i, mMethod.getMethod(i));
				}
				ATLog.i(TAG, INFO, "INFO. showDialog().onShow()");
			}
		});
		dialog.show();

		ATLog.i(TAG, INFO, "INFO. showDialog()");
	}
}
