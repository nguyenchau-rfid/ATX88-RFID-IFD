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

public class OptionUpcE0 extends BaseDialog implements OnCheckedChangeListener {
	private static final String TAG = OptionUpcE0.class.getSimpleName();
	private static final int INFO = ATLog.L2;
	
	private CheckBox chkUPCE0Expand;
	private CheckBox chkUPCE0AddendaRequired;
	private CheckBox chkUPCE0AddendaSeparator;
	private CheckBox chkUPCE0CheckDigit;
	private CheckBox chkUPCE0LeadingZero;
	private CheckBox chkUPCE0Addenda2Digit;
	private CheckBox chkUPCE0Addenda5Digit;
	
	private boolean mIsUPCE0Expand;
	private boolean mIsUPCE0AddendaRequired;
	private boolean mIsUPCE0AddendaSeparator;
	private boolean mIsUPCE0CheckDigit;
	private boolean mIsUPCE0LeadingZero;
	private boolean mIsUPCE0Addenda2Digit;
	private boolean mIsUPCE0Addenda5Digit;
	
	@SuppressWarnings("unused")
	private Context mContext;
	
	public OptionUpcE0() {
		super();
		
		mIsUPCE0Expand = false;          
		mIsUPCE0AddendaRequired = false; 
		mIsUPCE0AddendaSeparator = true;
		mIsUPCE0CheckDigit = true;      
		mIsUPCE0LeadingZero = true;     
		mIsUPCE0Addenda2Digit = false;   
		mIsUPCE0Addenda5Digit = false;   
		
		mContext = null;
	}
	
	public boolean getUPCE0Expand() {
		return mIsUPCE0Expand;
	}
	
	public void setUPCE0Expand(boolean value) {
		mIsUPCE0Expand = value;
	}
	
	public boolean getUPCE0AddendaRequired() {
		return mIsUPCE0AddendaRequired;
	}
	
	public void setUPCE0AddendaRequired(boolean value) {
		mIsUPCE0AddendaRequired = value;
	}
	
	
	public boolean getUPCE0AddendaSeparator() {
		return mIsUPCE0AddendaSeparator;
	}
	
	public void setUPCE0AddendaSeparator(boolean value) {
		mIsUPCE0AddendaSeparator = value;
	}
	
	public boolean getUPCE0CheckDigit() {
		return mIsUPCE0CheckDigit;
	}
	
	public void setUPCE0CheckDigit(boolean value) {
		mIsUPCE0CheckDigit = value;
	}
	
	public boolean getUPCE0LeadingZero() {
		return mIsUPCE0LeadingZero;
	}
	
	public void setUPCE0LeadingZero(boolean value) {
		mIsUPCE0LeadingZero = value;
	}
	
	public boolean getUPCE0Addenda2Digit() {
		return mIsUPCE0Addenda2Digit;
	}
	
	public void setUPCE0Addenda2Digit(boolean value) {
		mIsUPCE0Addenda2Digit = value;
	}
	
	public boolean getUPCE0Addenda5Digit() {
		return mIsUPCE0Addenda5Digit;
	}
	
	public void setUPCE0Addenda5Digit(boolean value) {
		mIsUPCE0Addenda5Digit = value;
	}
	
	
	@Override
	public void display() {
		
	}

	@Override
	public void showDialog(Context context, String title, final IValueChangedListener changedListener,
			final ICancelListener cancelListener) {

		mContext = context;
		
		LinearLayout root = (LinearLayout) LinearLayout.inflate(context, R.layout.dialog_barcode_option_spc_2d_upc_e0, null);
		
		chkUPCE0Expand = (CheckBox) root.findViewById(R.id.upc_e0_expand);          
		chkUPCE0Expand.setOnCheckedChangeListener(this);
		chkUPCE0AddendaRequired = (CheckBox) root.findViewById(R.id.upc_e0_addenda_required);
		chkUPCE0AddendaRequired.setOnCheckedChangeListener(this);
		chkUPCE0AddendaSeparator = (CheckBox) root.findViewById(R.id.upc_e0_addenda_separator);
		chkUPCE0AddendaSeparator.setOnCheckedChangeListener(this);
		chkUPCE0CheckDigit = (CheckBox) root.findViewById(R.id.upc_e0_check_digit);
		chkUPCE0CheckDigit.setOnCheckedChangeListener(this);
		chkUPCE0LeadingZero = (CheckBox) root.findViewById(R.id.upc_e0_leading_zero);
		chkUPCE0LeadingZero.setOnCheckedChangeListener(this);
		chkUPCE0Addenda2Digit = (CheckBox) root.findViewById(R.id.upc_e0_addenda_2_digit);
		chkUPCE0Addenda2Digit.setOnCheckedChangeListener(this);
		chkUPCE0Addenda5Digit = (CheckBox) root.findViewById(R.id.upc_e0_addenda_5_digit);
		chkUPCE0Addenda5Digit.setOnCheckedChangeListener(this);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setView(root);
		builder.setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
			
				mIsUPCE0Expand = chkUPCE0Expand.isChecked();           
				mIsUPCE0AddendaRequired = chkUPCE0AddendaRequired.isChecked();
				mIsUPCE0AddendaSeparator = chkUPCE0AddendaSeparator.isChecked(); 
				mIsUPCE0CheckDigit = chkUPCE0CheckDigit.isChecked();
				mIsUPCE0LeadingZero = chkUPCE0LeadingZero.isChecked();
				mIsUPCE0Addenda2Digit = chkUPCE0Addenda2Digit.isChecked();
				mIsUPCE0Addenda5Digit = chkUPCE0Addenda5Digit.isChecked();
				
				if(changedListener != null){
					changedListener.onValueChanged(OptionUpcE0.this);
				}
			}
		});
		
		builder.setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {

				if(cancelListener != null){
					cancelListener.onCanceled(OptionUpcE0.this);
				}
				ATLog.i(TAG, INFO, "INFO. showDialog().$NegativeButton.onClick()");
			}
		});
		
		builder.setCancelable(true);
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {

				if(cancelListener != null) {
					cancelListener.onCanceled(OptionUpcE0.this);
				}
				ATLog.i(TAG, INFO, "INFO. showDialog().onCancel()");
			}
		});
		
		AlertDialog dialog = builder.create();
		dialog.setOnShowListener(new DialogInterface.OnShowListener() {
			
			@Override
			public void onShow(DialogInterface dialog) {
			
				chkUPCE0Expand.setChecked(mIsUPCE0Expand);           
				chkUPCE0AddendaRequired.setChecked(mIsUPCE0AddendaRequired);
				chkUPCE0AddendaSeparator.setChecked(mIsUPCE0AddendaSeparator); 
				chkUPCE0CheckDigit.setChecked(mIsUPCE0CheckDigit);
				chkUPCE0LeadingZero.setChecked(mIsUPCE0LeadingZero);
				chkUPCE0Addenda2Digit.setChecked(mIsUPCE0Addenda2Digit);
				chkUPCE0Addenda5Digit.setChecked(mIsUPCE0Addenda5Digit);
				
				ATLog.i(TAG, INFO, "INFO. showDialog().onShow()");
			}
		});
		dialog.show();
		
		ATLog.i(TAG, INFO, "INFO. showDialog()");
	}

	@Override
	public void onCheckedChanged(CompoundButton view, boolean isChecked) {
		switch(view.getId()){
		case R.id.upc_e0_expand:
			break;
		case R.id.upc_e0_addenda_required:
			break;
		case R.id.upc_e0_addenda_separator:
			break;
		case R.id.upc_e0_check_digit:
			break;
		case R.id.upc_e0_leading_zero:
			break;
		case R.id.upc_e0_addenda_2_digit:
			break;
		case R.id.upc_e0_addenda_5_digit:
			break;
		}
		
	}

}
