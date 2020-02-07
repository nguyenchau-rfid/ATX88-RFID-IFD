package com.atid.app.atx.dialog;

import java.util.ArrayList;
import java.util.Collections;

import com.atid.app.atx.R;
import com.atid.lib.types.IEnumType;
import com.atid.lib.util.diagnotics.ATLog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class EnumListDialog extends BaseDialog {

	private static final String TAG = EnumListDialog.class.getSimpleName();
	private static final int INFO = ATLog.L2;
	
	private ArrayList<IEnumType> mArray;
	private IEnumType mValue;
	
	public EnumListDialog(IEnumType[] array) {
		super();
		mArray = new ArrayList<IEnumType>();
		Collections.addAll(mArray, array);
		mValue = mArray.get(0);
	}

	public EnumListDialog(TextView view, IEnumType[] array) {
		super(view);
		mArray = new ArrayList<IEnumType>();
		Collections.addAll(mArray, array);
		mValue = mArray.get(0);
	}

	public IEnumType getValue() {
		return mValue;
	}
	
	public void setValue(IEnumType value) {
		mValue = value;
	}
	
	@Override
	public void display() {
		if (txtValue == null)
			return;
		txtValue.setText(mValue.toString());
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
		final ArrayAdapter<IEnumType> adapter = new ArrayAdapter<IEnumType>(context,
				android.R.layout.simple_list_item_single_choice, mArray);
		list.setAdapter(adapter);
		list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setView(root);
		builder.setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				int position = list.getCheckedItemPosition();
				mValue = mArray.get(position);
				display();
				if (changedListener != null) {
					changedListener.onValueChanged(EnumListDialog.this);
				}
				ATLog.i(TAG, INFO, "INFO. showDialog().$PositiveButton.onClick()");
			}
		});
		builder.setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (cancelListener != null) {
					cancelListener.onCanceled(EnumListDialog.this);
				}
				ATLog.i(TAG, INFO, "INFO. showDialog().$NegativeButton.onClick()");
			}
		});
		builder.setCancelable(true);
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				if (cancelListener != null) {
					cancelListener.onCanceled(EnumListDialog.this);
				}
				ATLog.i(TAG, INFO, "INFO. showDialog().onCancel()");
			}
		});
		AlertDialog dialog = builder.create();
		dialog.setOnShowListener(new DialogInterface.OnShowListener() {

			@Override
			public void onShow(DialogInterface dialog) {
				int position = mArray.indexOf(mValue);
				list.setItemChecked(position, true);
				list.setSelectionFromTop(position, 0);
				ATLog.i(TAG, INFO, "INFO. showDialog().onShow()");
			}
		});
		dialog.show();

		ATLog.i(TAG, INFO, "INFO. showDialog()");
	}

}
