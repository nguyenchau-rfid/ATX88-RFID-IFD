package com.atid.app.atx.dialog.spc;

import com.atid.app.atx.R;
import com.atid.app.atx.dialog.BaseDialog;
import com.atid.app.atx.dialog.EnumListDialog;
import com.atid.app.atx.dialog.NumberDialog;
import com.atid.lib.module.barcode.spc.type.MSICheckCharacter;
import com.atid.lib.util.diagnotics.ATLog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OptionMsi extends BaseDialog implements OnClickListener{
	private static final String TAG = OptionMsi.class.getSimpleName();
	private static final int INFO = ATLog.L2;
	
	private TextView txtMSICheckCharacter;
	private TextView txtLengthMin;
	private TextView txtLengthMax;
	
	private EnumListDialog dlgMSICheckCharacter;
	private NumberDialog dlgLengthMin;
	private NumberDialog dlgLengthMax;
	
	private MSICheckCharacter mMSICheckCharacter;
	private int mLengthMin;
	private int mLengthMax;
	
	private Context mContext;
	
	public OptionMsi() {
		super();
		
		mMSICheckCharacter = MSICheckCharacter.ValidateTyp10butDonotTransmit;
		mLengthMin = 4;
		mLengthMax = 48;
		
		mContext = null;
	}
	
	public MSICheckCharacter getMSICheckCharacter() {
		return mMSICheckCharacter;
	}

	public void setMSICheckCharacter(MSICheckCharacter value){
		mMSICheckCharacter = value;
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
		
		LinearLayout root = (LinearLayout) LinearLayout.inflate(context, R.layout.dialog_barcode_option_spc_2d_msi, null);

		txtMSICheckCharacter = (TextView) root.findViewById(R.id.msi_check_character);
		txtMSICheckCharacter.setOnClickListener(this);
		txtLengthMin = (TextView) root.findViewById(R.id.min);
		txtLengthMin.setOnClickListener(this);
		txtLengthMax = (TextView) root.findViewById(R.id.max);
		txtLengthMax.setOnClickListener(this);
		
		dlgMSICheckCharacter = new EnumListDialog(txtMSICheckCharacter, MSICheckCharacter.values());
		dlgMSICheckCharacter.setValue(mMSICheckCharacter);
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
				
				mMSICheckCharacter = (MSICheckCharacter) dlgMSICheckCharacter.getValue();
				mLengthMin = dlgLengthMin.getValue();
				mLengthMax = dlgLengthMax.getValue();
				
				if(changedListener != null){
					changedListener.onValueChanged(OptionMsi.this);
				}
			}
		});
		
		builder.setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {

				if(cancelListener != null){
					cancelListener.onCanceled(OptionMsi.this);
				}
				ATLog.i(TAG, INFO, "INFO. showDialog().$NegativeButton.onClick()");
			}
		});
		
		builder.setCancelable(true);
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {

				if(cancelListener != null) {
					cancelListener.onCanceled(OptionMsi.this);
				}
				ATLog.i(TAG, INFO, "INFO. showDialog().onCancel()");
			}
		});
		
		AlertDialog dialog = builder.create();
		dialog.setOnShowListener(new DialogInterface.OnShowListener() {
			
			@Override
			public void onShow(DialogInterface dialog) {
				
				dlgMSICheckCharacter.display();
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
		case R.id.msi_check_character:
			dlgMSICheckCharacter.showDialog(mContext, R.string.msi_check_character);
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