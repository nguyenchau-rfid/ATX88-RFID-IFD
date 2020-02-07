package com.atid.app.atx.dialog.ssi;

import com.atid.app.atx.dialog.BaseDialog;
import com.atid.app.atx.dialog.EnumListDialog;
import com.atid.app.atx.R;
import com.atid.lib.module.barcode.ssi.type.AustraliaPostFormat;
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

public class OptionPostalCodesDialog extends BaseDialog implements OnClickListener, OnCheckedChangeListener{
	private static final String TAG = OptionPostalCodesDialog.class.getSimpleName();
	private static final int INFO = ATLog.L2;
	
	private CheckBox chkTransmitUsPostalCheckDigit;
	private CheckBox chkTransmitUkPostalCheckDigit;
	private TextView txtAustraliaPostFormat;
	
	private EnumListDialog dlgAustraliaPostFormat;
	
	private boolean mIsTransmitUsPostalCheckDigit;
	private boolean mIsTransmitUkPostalCheckDigit;
	private AustraliaPostFormat mAustraliaPostFormat;
	
	private Context mContext;
	
	public OptionPostalCodesDialog(){
		super();
		
		mIsTransmitUsPostalCheckDigit = true;
		mIsTransmitUkPostalCheckDigit = true;
		mAustraliaPostFormat = AustraliaPostFormat.Autodiscriminate;
		
		mContext = null;
	}
	
	public boolean getTransmitUsPostalCheckDigit(){
		return mIsTransmitUsPostalCheckDigit;
	}
	
	public void setTransmitUsPostalCheckDigit(boolean value){
		mIsTransmitUsPostalCheckDigit = value;
	}
	
	public boolean getTransmitUkPostalCheckDigit(){
		return mIsTransmitUkPostalCheckDigit;
	}
	
	public void setTransmitUkPostalCheckDigit(boolean value){
		mIsTransmitUkPostalCheckDigit = value;
	}
	
	public AustraliaPostFormat getAustraliaPostFormat(){
		return mAustraliaPostFormat;
	}
	
	public void setAustraliaPostFormat(AustraliaPostFormat value){
		mAustraliaPostFormat = value;
	}
	
	@Override
	public void display() {
		
	}

	@Override
	public void showDialog(Context context, String title, final IValueChangedListener changedListener,
			final ICancelListener cancelListener) {
		
		mContext = context;
		
		LinearLayout root = (LinearLayout) LinearLayout.inflate(context, R.layout.dialog_barcode_option_ssi_2d_postal_codes, null);
		
		chkTransmitUsPostalCheckDigit = (CheckBox) root.findViewById(R.id.transmit_us_postal_check_digit);
		chkTransmitUsPostalCheckDigit.setOnCheckedChangeListener(this);
		chkTransmitUkPostalCheckDigit = (CheckBox) root.findViewById(R.id.transmit_uk_postal_check_digit);
		chkTransmitUkPostalCheckDigit.setOnCheckedChangeListener(this);
		txtAustraliaPostFormat = (TextView) root.findViewById(R.id.australia_post_format);
		txtAustraliaPostFormat.setOnClickListener(this);
		
		dlgAustraliaPostFormat = new EnumListDialog(txtAustraliaPostFormat, AustraliaPostFormat.values());
		dlgAustraliaPostFormat.setValue(mAustraliaPostFormat);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setView(root);
		builder.setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				mAustraliaPostFormat = (AustraliaPostFormat)dlgAustraliaPostFormat.getValue();
				
				mIsTransmitUsPostalCheckDigit = chkTransmitUsPostalCheckDigit.isChecked();
				mIsTransmitUkPostalCheckDigit = chkTransmitUkPostalCheckDigit.isChecked();
				
				if(changedListener != null)
					changedListener.onValueChanged(OptionPostalCodesDialog.this);
				
				ATLog.i(TAG, INFO, "INFO. showDialog().$PositiveButton.onClick()");
			}
		});
		
		builder.setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				if(cancelListener != null)
					cancelListener.onCanceled(OptionPostalCodesDialog.this); 
				
				ATLog.i(TAG, INFO, "INFO. showDialog().$NegativeButton.onClick()");
			}
		});
		
		builder.setCancelable(true);
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				
				if(cancelListener != null)
					cancelListener.onCanceled(OptionPostalCodesDialog.this); 
				
				ATLog.i(TAG, INFO, "INFO. showDialog().onCancel()");
			}
		});
		
		AlertDialog dialog = builder.create();
		dialog.setOnShowListener(new DialogInterface.OnShowListener() {
			
			@Override
			public void onShow(DialogInterface dialog) {
				
				dlgAustraliaPostFormat.display();
				
				chkTransmitUsPostalCheckDigit.setChecked(mIsTransmitUsPostalCheckDigit);
				chkTransmitUkPostalCheckDigit.setChecked(mIsTransmitUkPostalCheckDigit);
				
				ATLog.i(TAG, INFO, "INFO. showDialog().onShow()");
			}
		});
		dialog.show();
		
		ATLog.i(TAG, INFO, "INFO. showDialog()");
	}
	

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.australia_post_format:
			dlgAustraliaPostFormat.showDialog(mContext, R.string.australia_post_format);
			break;

		}
		
	}

	@Override
	public void onCheckedChanged(CompoundButton view, boolean arg1) {
		switch(view.getId()){
		case R.id.transmit_us_postal_check_digit:
			break;
		case R.id.transmit_uk_postal_check_digit:
			break;
		}
		
	}

}
