package com.atid.app.atx.dialog.spc;

import com.atid.app.atx.R;
import com.atid.app.atx.dialog.BaseDialog;
import com.atid.app.atx.dialog.EnumListDialog;
import com.atid.app.atx.dialog.NumberDialog;
import com.atid.lib.module.barcode.spc.type.Code11CheckDigitsRequired;
import com.atid.lib.util.diagnotics.ATLog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OptionCode11 extends BaseDialog implements OnClickListener{
	private static final String TAG = OptionCode11.class.getSimpleName();
	private static final int INFO = ATLog.L2;
	
	private TextView txtCode11CheckDigitsRequired; 
	private TextView txtLengthMin;
	private TextView txtLengthMax;
	
	private EnumListDialog dlgCode11CheckDigitsRequired;
	private NumberDialog dlgLengthMin;
	private NumberDialog dlgLengthMax;
	
	private Code11CheckDigitsRequired mCode11CheckDigitsRequired;
	private int mLengthMin;
	private int mLengthMax;
	
	private Context mContext;
	
	public OptionCode11() {
		super();
		
		mCode11CheckDigitsRequired = Code11CheckDigitsRequired.CheckDigit2;
		mLengthMin = 4;
		mLengthMax = 80;
		
		mContext = null;
	}
	
	public Code11CheckDigitsRequired getCode11CheckDigitsRequired() {
		return mCode11CheckDigitsRequired;
	}

	public void setCode11CheckDigitsRequired(Code11CheckDigitsRequired value){
		mCode11CheckDigitsRequired = value;
	}
	
	public int getLengthMin() {
		return mLengthMin;
	}

	public void setLengthMin(int value){
		mLengthMin = value;
	}
	
	public int getLengthMax() {
		return mLengthMax;
	}

	public void setLengthMax(int value){
		mLengthMax = value;
	}
		
	@Override
	public void display() {
		
	}

	@Override
	public void showDialog(Context context, String title, final IValueChangedListener changedListener,
			final ICancelListener cancelListener) {

		mContext = context;
		
		LinearLayout root = (LinearLayout) LinearLayout.inflate(context, R.layout.dialog_barcode_option_spc_2d_code11, null);
		
		txtCode11CheckDigitsRequired  = (TextView) root.findViewById(R.id.code11_check_digits_required);
		txtCode11CheckDigitsRequired.setOnClickListener(this);
		txtLengthMin = (TextView) root.findViewById(R.id.min);
		txtLengthMin.setOnClickListener(this);
		txtLengthMax = (TextView) root.findViewById(R.id.max);
		txtLengthMax.setOnClickListener(this);
		
		dlgCode11CheckDigitsRequired = new EnumListDialog(txtCode11CheckDigitsRequired, Code11CheckDigitsRequired.values());
		dlgCode11CheckDigitsRequired.setValue(mCode11CheckDigitsRequired);
		dlgLengthMin = new NumberDialog(txtLengthMin);
		dlgLengthMin.setValue(mLengthMin);
		dlgLengthMax = new NumberDialog(txtLengthMax);
		dlgLengthMax.setValue(mLengthMax);
				
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setView(root);
		builder.setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				mCode11CheckDigitsRequired = (Code11CheckDigitsRequired) dlgCode11CheckDigitsRequired.getValue();
				
				mLengthMin = dlgLengthMin.getValue();
				mLengthMax = dlgLengthMax.getValue();
				
				if(changedListener != null){
					changedListener.onValueChanged(OptionCode11.this);
				}
			}
		});
		
		builder.setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {

				if(cancelListener != null){
					cancelListener.onCanceled(OptionCode11.this);
				}
				ATLog.i(TAG, INFO, "INFO. showDialog().$NegativeButton.onClick()");
			}
		});
		
		builder.setCancelable(true);
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {

				if(cancelListener != null) {
					cancelListener.onCanceled(OptionCode11.this);
				}
				ATLog.i(TAG, INFO, "INFO. showDialog().onCancel()");
			}
		});
		
		AlertDialog dialog = builder.create();
		dialog.setOnShowListener(new DialogInterface.OnShowListener() {
			
			@Override
			public void onShow(DialogInterface dialog) {
				
				dlgCode11CheckDigitsRequired.display();
				dlgLengthMin.display();
				dlgLengthMax.display();
							
				ATLog.i(TAG, INFO, "INFO. showDialog().onShow()");
			}
		});
		dialog.show();
		
		ATLog.i(TAG, INFO, "INFO. showDialog()");
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.code11_check_digits_required:
			dlgCode11CheckDigitsRequired.showDialog(mContext, R.string.code11_check_digits_required);
			break;
		case R.id.min:
			dlgLengthMin.showDialog(mContext, R.string.min);
			break;
		case R.id.max:
			dlgLengthMax.showDialog(mContext, R.string.max);
			break;		
		}
		
	}

}
