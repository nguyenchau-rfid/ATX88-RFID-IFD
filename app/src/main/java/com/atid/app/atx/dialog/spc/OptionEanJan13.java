package com.atid.app.atx.dialog.spc;

import com.atid.app.atx.R;
import com.atid.app.atx.dialog.BaseDialog;
import com.atid.lib.util.diagnotics.ATLog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;

public class OptionEanJan13 extends BaseDialog implements OnCheckedChangeListener{
	private static final String TAG = OptionEanJan13.class.getSimpleName();
	private static final int INFO = ATLog.L2;
	
	private CheckBox chkConvertUPCAtoEAN13;
	private CheckBox chkEANJAN13CheckDigit;
	private CheckBox chkEANJAN13Addenda2Digit;
	private CheckBox chkEANJAN13Addenda5Digit;
	private CheckBox chkEANJAN13AddendaRequired;
	private CheckBox chkEANJAN13AddendaSeparator;
	private CheckBox chkISBNTranslate;
	
	private boolean mIsConvertUPCAtoEAN13;
	private boolean mIsEANJAN13CheckDigit;
	private boolean mIsEANJAN13Addenda2Digit;
	private boolean mIsEANJAN13Addenda5Digit;
	private boolean mIsEANJAN13AddendaRequired;
	private boolean mIsEANJAN13AddendaSeparator;
	private boolean mIsISBNTranslate;
	
	@SuppressWarnings("unused")
	private Context mContext;
	
	public OptionEanJan13() {
		super();
		
		mIsConvertUPCAtoEAN13 = false;      
		mIsEANJAN13CheckDigit = true;
		mIsEANJAN13Addenda2Digit = false;   
		mIsEANJAN13Addenda5Digit = false;
		mIsEANJAN13AddendaRequired = false; 
		mIsEANJAN13AddendaSeparator = true;
		mIsISBNTranslate = false;
		
		mContext = null;
	}
	
	public boolean getConvertUPCAtoEAN13() {
		return mIsConvertUPCAtoEAN13;
	}
	
	public void setConvertUPCAtoEAN13(boolean value) {
		mIsConvertUPCAtoEAN13 = value;
	}
	
	public boolean getEANJAN13CheckDigit() {
		return mIsEANJAN13CheckDigit;
	}
	
	public void setEANJAN13CheckDigit(boolean value) {
		mIsEANJAN13CheckDigit = value;
	}
	
	public boolean getEANJAN13Addenda2Digit() {
		return mIsEANJAN13Addenda2Digit;
	}
	
	public void setEANJAN13Addenda2Digit(boolean value) {
		mIsEANJAN13Addenda2Digit = value;
	}
	
	public boolean getEANJAN13Addenda5Digit() {
		return mIsEANJAN13Addenda5Digit;
	}
	
	public void setEANJAN13Addenda5Digit(boolean value) {
		mIsEANJAN13Addenda5Digit = value;
	}
	
	public boolean getEANJAN13AddendaRequired() {
		return mIsEANJAN13AddendaRequired;
	}
	
	public void setEANJAN13AddendaRequired(boolean value) {
		mIsEANJAN13AddendaRequired = value;
	}
	
	public boolean getEANJAN13AddendaSeparator() {
		return mIsEANJAN13AddendaSeparator;
	}
	
	public void setEANJAN13AddendaSeparator(boolean value) {
		mIsEANJAN13AddendaSeparator = value;
	}
	
	public boolean getISBNTranslate() {
		return mIsISBNTranslate;
	}
	
	public void setISBNTranslate(boolean value) {
		mIsISBNTranslate = value;
	}	
	
	@Override
	public void display() {
		
	}

	@Override
	public void showDialog(Context context, String title, final IValueChangedListener changedListener,
			final ICancelListener cancelListener) {

		mContext = context;
		
		LinearLayout root = (LinearLayout) LinearLayout.inflate(context, R.layout.dialog_barcode_option_spc_2d_ean_jan13, null);
		
		chkConvertUPCAtoEAN13 = (CheckBox) root.findViewById(R.id.convert_upca_to_ean13); 
		chkConvertUPCAtoEAN13.setOnCheckedChangeListener(this);
		chkEANJAN13CheckDigit = (CheckBox) root.findViewById(R.id.ean_jan13_check_digit);    
		chkEANJAN13CheckDigit.setOnCheckedChangeListener(this);
		chkEANJAN13Addenda2Digit = (CheckBox) root.findViewById(R.id.ean_jan13_addenda_2_digit);   
		chkEANJAN13Addenda2Digit.setOnCheckedChangeListener(this);
		chkEANJAN13Addenda5Digit = (CheckBox) root.findViewById(R.id.ean_jan13_addenda_5_digit);   
		chkEANJAN13Addenda5Digit.setOnCheckedChangeListener(this);
		chkEANJAN13AddendaRequired = (CheckBox) root.findViewById(R.id.ean_jan13_addenda_required);
		chkEANJAN13AddendaRequired.setOnCheckedChangeListener(this);
		chkEANJAN13AddendaSeparator = (CheckBox) root.findViewById(R.id.ean_jan13_addenda_separator);
		chkEANJAN13AddendaSeparator.setOnCheckedChangeListener(this);
		chkISBNTranslate = (CheckBox) root.findViewById(R.id.isbn_translate);
		chkISBNTranslate.setOnCheckedChangeListener(this);

		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setView(root);
		builder.setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
			
				mIsConvertUPCAtoEAN13 = chkConvertUPCAtoEAN13.isChecked();
				mIsEANJAN13CheckDigit = chkEANJAN13CheckDigit.isChecked();
				mIsEANJAN13Addenda2Digit = chkEANJAN13Addenda2Digit.isChecked();
				mIsEANJAN13Addenda5Digit = chkEANJAN13Addenda5Digit.isChecked();
				mIsEANJAN13AddendaRequired = chkEANJAN13AddendaRequired.isChecked();
				mIsEANJAN13AddendaSeparator = chkEANJAN13AddendaSeparator.isChecked();
				mIsISBNTranslate = chkISBNTranslate.isChecked();
				
				if(changedListener != null){
					changedListener.onValueChanged(OptionEanJan13.this);
				}
			}
		});
		
		builder.setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {

				if(cancelListener != null){
					cancelListener.onCanceled(OptionEanJan13.this);
				}
				ATLog.i(TAG, INFO, "INFO. showDialog().$NegativeButton.onClick()");
			}
		});
		
		builder.setCancelable(true);
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {

				if(cancelListener != null) {
					cancelListener.onCanceled(OptionEanJan13.this);
				}
				ATLog.i(TAG, INFO, "INFO. showDialog().onCancel()");
			}
		});
		
		AlertDialog dialog = builder.create();
		dialog.setOnShowListener(new DialogInterface.OnShowListener() {
			
			@Override
			public void onShow(DialogInterface dialog) {
			
				chkConvertUPCAtoEAN13.setChecked(mIsConvertUPCAtoEAN13);      
				chkEANJAN13CheckDigit.setChecked(mIsEANJAN13CheckDigit);      
				chkEANJAN13Addenda2Digit.setChecked(mIsEANJAN13Addenda2Digit);   
				chkEANJAN13Addenda5Digit.setChecked(mIsEANJAN13Addenda5Digit);   
				chkEANJAN13AddendaRequired.setChecked(mIsEANJAN13AddendaRequired); 
				chkEANJAN13AddendaSeparator.setChecked(mIsEANJAN13AddendaSeparator);
				chkISBNTranslate.setChecked(mIsISBNTranslate);

				ATLog.i(TAG, INFO, "INFO. showDialog().onShow()");
			}
		});
		dialog.show();
		
		ATLog.i(TAG, INFO, "INFO. showDialog()");
	}

	@Override
	public void onCheckedChanged(CompoundButton view, boolean isChecked) {
		switch(view.getId()){
		case R.id.convert_upca_to_ean13:
			break;
		case R.id.ean_jan13_check_digit:
			break;
		case R.id.ean_jan13_addenda_2_digit:
			break;
		case R.id.ean_jan13_addenda_5_digit:
			break;
		case R.id.ean_jan13_addenda_required:
			break;
		case R.id.ean_jan13_addenda_separator:
			break;
		case R.id.isbn_translate:
			break;
		}
		
	}

}
