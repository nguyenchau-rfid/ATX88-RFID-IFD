package com.atid.app.atx.dialog.ssi;

import com.atid.app.atx.dialog.BaseDialog;
import com.atid.app.atx.dialog.EnumListDialog;
import com.atid.app.atx.dialog.NumberDialog;
import com.atid.app.atx.dialog.NumberUnitDialog;
import com.atid.app.atx.R;
import com.atid.lib.module.barcode.ssi.type.CodeLength;
import com.atid.lib.module.barcode.ssi.type.CodeLengthType;
import com.atid.lib.module.barcode.ssi.type.ISBTConcatenation;
import com.atid.lib.util.diagnotics.ATLog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OptionSSI1DCode128Dialog extends BaseDialog implements OnClickListener, OnCheckedChangeListener{
	private static final String TAG = OptionSSI1DCode128Dialog.class.getSimpleName();
	private static final int INFO = ATLog.L2;
	
	private LinearLayout linearLenght1;
	private LinearLayout linearLenght2;
	
	private TextView txtLengthType;
	private TextView txtLength1;
	private TextView txtLength2;
	private TextView txtIsbtConcatenation;
	private CheckBox chkCheckIsbtTable;
	private TextView txtIsbtConcatenationRedundancy;
	
	private EnumListDialog dlgLengthType;
	private NumberDialog dlgLength1;
	private NumberDialog dlgLength2;
	private EnumListDialog dlgIsbtConcatenation;
	private NumberUnitDialog dlgIsbtConcatenationRedundancy;
	
	private CodeLength mLength;
	private ISBTConcatenation mIsbtConcatenation;
	private boolean mIsCheckIsbtTable;
	private int mIsbtConcatenationRedundancy;
	
	private Context mContext;
	
	public OptionSSI1DCode128Dialog(){
		super();
		
		mLength = new CodeLength(0, 0);
		mIsbtConcatenation = ISBTConcatenation.Disable;
		mIsCheckIsbtTable = true;
		mIsbtConcatenationRedundancy = 10;
		
		mContext = null;
	}
	
	public CodeLength getLength(){
		return mLength;
	}
	
	public void setLength(final CodeLength value){

		mLength.setLength(value.getLength1(), value.getLength2());

		if(mLength.getLength1() ==0 && mLength.getLength2() == 0){	
			mLength.setLengthType(CodeLengthType.AnyLength);
		} else if( mLength.getLength1() !=0 && mLength.getLength2() == 0 ) { 
			mLength.setLengthType(CodeLengthType.OneLength);
		} else if( mLength.getLength1() > mLength.getLength2() ) {
			mLength.setLengthType(CodeLengthType.TwoLength);
		} else { 
			mLength.setLengthType(CodeLengthType.Range);
		}		
	}
	
	public ISBTConcatenation getIsbtConcatenation(){
		return mIsbtConcatenation;
	}
	
	public void setIsbtConcatenation(ISBTConcatenation value){
		mIsbtConcatenation = value;
	}
	
	public boolean getCheckIsbtTable(){
		return mIsCheckIsbtTable;
	}
	
	public void setCheckIsbtTable(boolean value){
		mIsCheckIsbtTable = value;
	}
	
	public int getIsbtConcatenationRedundancy() {
		return mIsbtConcatenationRedundancy;
	}
	
	public void setIsbtConcatenationRedundancy(int value){
		mIsbtConcatenationRedundancy = value;
	}
	
	@Override
	public void display() {
		
	}

	@Override
	public void showDialog(Context context, String title, final IValueChangedListener changedListener,
			final ICancelListener cancelListener) {
		
		mContext = context;
		
		LinearLayout root = (LinearLayout) LinearLayout.inflate(context, R.layout.dialog_barcode_option_ssi_1d_code128, null);
		
		linearLenght1 = (LinearLayout) root.findViewById(R.id.linear_length1);
		linearLenght2 = (LinearLayout) root.findViewById(R.id.linear_length2);
		
		txtLengthType = (TextView) root.findViewById(R.id.length_type);
		txtLengthType.setOnClickListener(this);
		txtLength1 = (TextView) root.findViewById(R.id.length1);
		txtLength1.setOnClickListener(this);
		txtLength2 = (TextView) root.findViewById(R.id.length2);
		txtLength2.setOnClickListener(this);
		txtIsbtConcatenation = (TextView) root.findViewById(R.id.isbt_concatenation);
		txtIsbtConcatenation.setOnClickListener(this);
		chkCheckIsbtTable = (CheckBox) root.findViewById(R.id.check_isbt_table);
		chkCheckIsbtTable.setOnCheckedChangeListener(this);
		txtIsbtConcatenationRedundancy = (TextView) root.findViewById(R.id.isbt_concatenation_redundancy);
		txtIsbtConcatenationRedundancy.setOnClickListener(this);
		
		dlgLengthType = new EnumListDialog(txtLengthType, CodeLengthType.values());
		dlgLengthType.setValue(mLength.getLengthType());
		dlgLength1 = new NumberDialog(txtLength1);
		dlgLength1.setValue(mLength.getLength1());
		dlgLength2 = new NumberDialog(txtLength2);
		dlgLength2.setValue(mLength.getLength2());
		dlgIsbtConcatenation = new EnumListDialog(txtIsbtConcatenation, ISBTConcatenation.values());
		dlgIsbtConcatenation.setValue(mIsbtConcatenation);
		dlgIsbtConcatenationRedundancy = new NumberUnitDialog(txtIsbtConcatenationRedundancy , 
				mContext.getResources().getString(R.string.unit_times));
		dlgIsbtConcatenationRedundancy.setValue(mIsbtConcatenationRedundancy);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setView(root);
		builder.setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				mLength.setLengthType((CodeLengthType)dlgLengthType.getValue());
				if(dlgLengthType.getValue() == CodeLengthType.AnyLength) {
					mLength.setLength(0, 0);
				} else if(dlgLengthType.getValue() == CodeLengthType.OneLength) {
					mLength.setLength(dlgLength1.getValue(), 0);
				} else {
					mLength.setLength(dlgLength1.getValue(), dlgLength2.getValue());	
				}

				mIsbtConcatenation = (ISBTConcatenation) dlgIsbtConcatenation.getValue();
				mIsCheckIsbtTable = chkCheckIsbtTable.isChecked();
				mIsbtConcatenationRedundancy = dlgIsbtConcatenationRedundancy.getValue();
				
				if(changedListener != null)
					changedListener.onValueChanged(OptionSSI1DCode128Dialog.this);
				ATLog.i(TAG, INFO, "INFO. showDialog().$PositiveButton.onClick()");
			}
		});
		
		builder.setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				if(cancelListener != null)
					cancelListener.onCanceled(OptionSSI1DCode128Dialog.this);
				
				ATLog.i(TAG, INFO, "INFO. showDialog().$NegativeButton.onClick()");
			}
		});
		
		builder.setCancelable(true);
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				
				if(cancelListener != null)
					cancelListener.onCanceled(OptionSSI1DCode128Dialog.this);
				
				ATLog.i(TAG, INFO, "INFO. showDialog().onCancel()");
			}
		});
		
		AlertDialog dialog = builder.create();
		dialog.setOnShowListener(new DialogInterface.OnShowListener() {
			
			@Override
			public void onShow(DialogInterface dialog) {
				dlgLengthType.display();
				dlgLength1.display();
				dlgLength2.display();
				displayLength();
				
				dlgIsbtConcatenation.display();
				dlgIsbtConcatenationRedundancy.display();
				
				chkCheckIsbtTable.setChecked(mIsCheckIsbtTable);
				
				ATLog.i(TAG, INFO, "INFO. showDialog().onShow()");
			}
		});
		dialog.show();
		
		ATLog.i(TAG, INFO, "INFO. showDialog()");
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.length_type:
			dlgLengthType.showDialog(mContext, R.string.length_type, new BaseDialog.IValueChangedListener() {
				
				@Override
				public void onValueChanged(BaseDialog dialog) {
					displayLength();
				}
			});
			break;
		case R.id.length1:
			dlgLength1.showDialog(mContext, R.string.length1);
			break;
		case R.id.length2:
			dlgLength2.showDialog(mContext, R.string.length2);
			break;
		case R.id.isbt_concatenation:
			dlgIsbtConcatenation.showDialog(mContext, R.string.isbt_concatenation);
			break;
		case R.id.isbt_concatenation_redundancy:
			dlgIsbtConcatenationRedundancy.showDialog(mContext, R.string.isbt_concatenation_redundancy);
			break;
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton View, boolean isChecked) {
		switch(View.getId()){
		case R.id.check_isbt_table:
			break;
		case R.id.code128_reduced_quiet_zone:
			break;
		case R.id.ignore_code128_fnc4:
			break;

		}
	}
	
	private void displayLength(){
		
		ATLog.i(TAG, INFO, "INFO. displayLength() - Len1[%d] Len2[%d]",
				mLength.getLength1(), mLength.getLength2());

		switch((CodeLengthType) dlgLengthType.getValue()){
		case AnyLength:
			linearLenght1.setVisibility(View.GONE);
			linearLenght2.setVisibility(View.GONE);
			break;
		case OneLength:
			linearLenght1.setVisibility(View.VISIBLE);
			linearLenght2.setVisibility(View.GONE);
			break;
		case TwoLength:
			linearLenght1.setVisibility(View.VISIBLE);
			linearLenght2.setVisibility(View.VISIBLE);
			break;
		case Range:
			linearLenght1.setVisibility(View.VISIBLE);
			linearLenght2.setVisibility(View.VISIBLE);
			break;
		}
	}
}
