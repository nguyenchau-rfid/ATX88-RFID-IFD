package com.atid.app.atx.dialog;

import java.util.ArrayList;

import com.atid.app.atx.R;
import com.atid.app.atx.dialog.BaseDialog.ICancelListener;
import com.atid.lib.module.barcode.params.SymbolStateList;
import com.atid.lib.util.diagnotics.ATLog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.SparseBooleanArray;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

public class SymbolStateDialog {

	private static final String TAG = SymbolStateDialog.class.getSimpleName();
	private static final int INFO = ATLog.L2;
	
	private SymbolStateList mList;
	
	public SymbolStateDialog() {
		mList = new SymbolStateList();
	}
	
	public SymbolStateList getList() {
		return mList;
	}
	
	public void setList(SymbolStateList list) {
		mList = list;
	}
	
	public void showDialog(Context context, final IValueChangedListener listener, final ICancelListener cancelListener) {
		
		if (mList.getCount() <= 0)
			return;
		
		ArrayList<String> nameList = new ArrayList<String>();
		String[] names = null;
		for (int i= 0; i < mList.getCount(); i++)
			nameList.add(mList.getType(i).toString());
		names = new String[nameList.size()];
		nameList.toArray(names);
		
		LinearLayout root = (LinearLayout) LinearLayout.inflate(context, R.layout.dialog_list_view, null);
		final ListView list = (ListView) root.findViewById(R.id.list);
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
				android.R.layout.simple_list_item_multiple_choice, names);
		list.setAdapter(adapter);
		list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(R.string.title_symbol_state);
		builder.setView(root);
		builder.setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				SparseBooleanArray checkedItems = list.getCheckedItemPositions();
				for (int i = 0; i < adapter.getCount(); i++) {
					mList.setUsed(i, checkedItems.get(i));
				}
				
				if (listener != null)
					listener.onValueChanged(SymbolStateDialog.this);
				ATLog.i(TAG, INFO, "INFO. showDialog().$PositiveButton.onClick()");
			}
		});
		builder.setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (cancelListener != null) {
					cancelListener.onCanceled(null);
				}
				ATLog.i(TAG, INFO, "INFO. showDialog().$NegativeButton.onClick()()");
			}
		});
		builder.setCancelable(true);
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				if (cancelListener != null) {
					cancelListener.onCanceled(null);
				}
				ATLog.i(TAG, INFO, "INFO. showDialog().onCancel()");
			}
		});
		AlertDialog dialog = builder.create();
		dialog.setOnShowListener(new DialogInterface.OnShowListener() {

			@Override
			public void onShow(DialogInterface dialog) {
				for (int i = 0; i < mList.getCount(); i++) {
					list.setItemChecked(i, mList.getUsed(i));
				}
				
				ATLog.i(TAG, INFO, "INFO. showDialog().onShow()");
			}
		});
		dialog.show();

		ATLog.i(TAG, INFO, "INFO. showDialog()");
	}

	// ------------------------------------------------------------------------
	// Declare Interface IValueChangedListener
	// ------------------------------------------------------------------------
	
	public interface IValueChangedListener {
		void onValueChanged(SymbolStateDialog dialog);
	}
}
