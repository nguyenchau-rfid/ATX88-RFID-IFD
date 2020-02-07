package com.atid.app.atx.dialog;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.atid.app.atx.R;
import com.atid.app.atx.adapter.SelectMask6cAdapter;
import com.atid.app.atx.dialog.BaseDialog.ICancelListener;
import com.atid.lib.module.rfid.uhf.params.SelectMask6cParam;
import com.atid.lib.util.diagnotics.ATLog;

public class SelectionMaskDialog implements OnItemClickListener{
	private static final String TAG = SelectionMaskDialog.class.getSimpleName();
	private static final int INFO = ATLog.L2;
	
	private Context mContext;
	
	private SelectMask6cDialog dlgMask;
	
	private ListView lstMask;
	private SelectMask6cAdapter adpMask;
	
	private static final int MAX_MASK = 8;
	private ArrayList<SelectMask6cParam> mListParam;
	
	public SelectionMaskDialog() {
		
		mListParam = new ArrayList<SelectMask6cParam>();
		for(int i=0 ; i<MAX_MASK ; i++) {
			mListParam.add(new SelectMask6cParam());
		}
		ATLog.i(TAG, INFO, "INFO. SelectionMaskDialog()");
	}
	
	public int getMaxCount() {
		return MAX_MASK;
	}
	
	public SelectMask6cParam getSelectionMask(final int position) throws Exception {
		if(position < 0 || position >= MAX_MASK) {
			throw new Exception("Out of Range");
		}
		
		return mListParam.get(position);
	}
	
	public void setSelectionMask(final int position, SelectMask6cParam param ) throws Exception {
		if(position < 0 || position >= MAX_MASK) {
			throw new Exception("Out of Range");
		}

		mListParam.get(position).copy(param);
	}
	
	public void showDialog(Context context, final IValueChangedListener listener, final ICancelListener cancelListener) {
		
		mContext = context;
		LinearLayout root = (LinearLayout) LinearLayout.inflate(context, R.layout.dialog_selection_mask, null);

		dlgMask = new SelectMask6cDialog();
		
		lstMask = (ListView) root.findViewById(R.id.list_mask);
		adpMask = new SelectMask6cAdapter(context);
		lstMask.setAdapter(adpMask);
		lstMask.setOnItemClickListener(this);

		for(int i = 0 ; i < MAX_MASK ; i++) {
			adpMask.update(i, mListParam.get(i));
		}
		adpMask.notifyDataSetChanged();
		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(R.string.selection_mask);
		builder.setView(root);
		builder.setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				for(int i=0 ; i < MAX_MASK ; i++) {
					mListParam.get(i).copy(adpMask.getItem(i));
				}
				
				if(listener != null) {
					listener.onValueChanged(SelectionMaskDialog.this);
				}
			}
		});
		
		builder.setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(cancelListener != null) {
					cancelListener.onCanceled(null);
				}
				ATLog.i(TAG, INFO, "INFO. showDialog().$NegativeButton.onClick()");
			}
		});
		
		builder.setCancelable(true);
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				if(cancelListener != null) {
					cancelListener.onCanceled(null);
				}
				ATLog.i(TAG, INFO, "INFO. showDialog().$NegativeButton.onClick()");
			}
		});
		
		AlertDialog dialog = builder.create();
		dialog.setOnShowListener(new DialogInterface.OnShowListener() {
			
			@Override
			public void onShow(DialogInterface dialog) {
				
			}
		});
		dialog.show();
		ATLog.i(TAG, INFO, "INFO. showDialog()");
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		
		ATLog.i(TAG, INFO, "INFO. onItemClick() - position[%d]", position);
		
		SelectMask6cParam item = adpMask.getItem(position);
		final int index = position;
		dlgMask.setItem(item);
		dlgMask.showDialog(mContext, new SelectMask6cDialog.IValueChangedListener() {
			
			@Override
			public void onValueChanged(SelectMask6cDialog dialog) {
				adpMask.update(index, dlgMask.getItem());
				adpMask.notifyDataSetChanged();
				if(!dlgMask.getItem().getPattern().equals("")){
					lstMask.setItemChecked(index, true);
				}
			}
		});
	}

	// ------------------------------------------------------------------------
	// Declare Interface IValueChangedListener
	// ------------------------------------------------------------------------
	
	public interface IValueChangedListener {
		void onValueChanged(SelectionMaskDialog dialog);
	}

}
