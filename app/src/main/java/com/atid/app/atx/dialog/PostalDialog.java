package com.atid.app.atx.dialog;

import java.util.ArrayList;

import com.atid.app.atx.R;
import com.atid.lib.module.barcode.types.BarcodePostType;
import com.atid.lib.util.diagnotics.ATLog;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.SparseArray;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

@SuppressLint("UseSparseArrays")
public class PostalDialog extends BaseDialog{
	private static final String TAG = PostalDialog.class.getSimpleName();
	private static final int INFO = ATLog.L2;
	
	private BarcodePostType mPostalCodes;

	@SuppressWarnings("unused")
	private Context mContext;
	
	public PostalDialog() {
		super();
		
		mPostalCodes = BarcodePostType.Off;
	}

	public BarcodePostType getPostalCodes() {
		return mPostalCodes;
	}
	
	public void setPostalCodes(BarcodePostType value){
		mPostalCodes = value;
	}
	

	@Override
	public void display() {
		
	}

	@SuppressLint("UseSparseArrays")
	@Override
	public void showDialog(Context context, String title, final IValueChangedListener changedListener,
			final ICancelListener cancelListener) {
		mContext = context;
		
		LinearLayout root = (LinearLayout) LinearLayout.inflate(context, R.layout.dialog_list_view, null);

		ArrayList<String> list = new ArrayList<String>();
		final SparseArray<Integer> mArrayPosition = new SparseArray<Integer>();
		//final SparseArray<Integer> mArrayValue = new SparseArray<Integer>();
		
		int i = 0;
		for(BarcodePostType item : BarcodePostType.values()){
			mArrayPosition.append(i, item.getCode());
			//mArrayValue.append(i, item.getCode());

			list.add(item.toString());
			i++;
		}
		String[] names = new String[list.size()];
		list.toArray(names);
		
		final ListView listView = (ListView) root.findViewById(R.id.list);
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
				android.R.layout.simple_list_item_single_choice, names);
		listView.setAdapter(adapter);
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setView(root);
		builder.setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
			
				//int position = mArrayPosition.keyAt(listView.getCheckedItemPosition());
				int position = mArrayPosition.indexOfKey(listView.getCheckedItemPosition());
				mPostalCodes = BarcodePostType.valueOf(mArrayPosition.get(position) );
				
				if(changedListener != null){
					changedListener.onValueChanged(PostalDialog.this);
				}
			}
		});
		
		builder.setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
	
				if(cancelListener != null){
					cancelListener.onCanceled(PostalDialog.this);
				}
				ATLog.i(TAG, INFO, "INFO. showDialog().$NegativeButton.onClick()");
			}
		});
		
		builder.setCancelable(true);
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
	
				if(cancelListener != null) {
					cancelListener.onCanceled(PostalDialog.this);
				}
				ATLog.i(TAG, INFO, "INFO. showDialog().onCancel()");
			}
		});
		
		AlertDialog dialog = builder.create();
		dialog.setOnShowListener(new DialogInterface.OnShowListener() {
			
			@Override
			public void onShow(DialogInterface dialog) {
				//int position = mArrayValue.keyAt(mPostalCodes.getCode());
				int position = mArrayPosition.indexOfValue(mPostalCodes.getCode());
				listView.setItemChecked(position, true);
				listView.setSelectionFromTop(position, 0);
				ATLog.i(TAG, INFO, "INFO. showDialog().onShow()");
			}
		});
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(dialog.getWindow().getAttributes());
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.MATCH_PARENT;
		dialog.show();
		dialog.getWindow().setAttributes(lp);
		
		ATLog.i(TAG, INFO, "INFO. showDialog()");
	}
}
