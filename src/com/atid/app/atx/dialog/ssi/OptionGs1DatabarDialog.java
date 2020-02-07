package com.atid.app.atx.dialog.ssi;

import com.atid.app.atx.dialog.BaseDialog;
import com.atid.app.atx.dialog.EnumListDialog;
import com.atid.app.atx.R;
import com.atid.lib.module.barcode.ssi.type.GS1DataBarLimitedSecurityLevel;
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

public class OptionGs1DatabarDialog extends BaseDialog implements OnClickListener, OnCheckedChangeListener{
	private static final String TAG = OptionGs1DatabarDialog.class.getSimpleName();
	private static final int INFO = ATLog.L2;
	
	private TextView txtGs1DatabarLimitedSecurityLevel;
	private CheckBox chkConvertGs1DatabarToUpcEan;
	
	private EnumListDialog dlgGs1DatabarLimitedSecurityLevel;
	
	private GS1DataBarLimitedSecurityLevel mGs1DatabarLimitedSecurityLevel;
	private boolean mIsConvertGs1DatabarToUpcEan;
	
	private Context mContext;
	
	public OptionGs1DatabarDialog(){
		super();
		
		mGs1DatabarLimitedSecurityLevel = GS1DataBarLimitedSecurityLevel.Level3;
		mIsConvertGs1DatabarToUpcEan = false;
		
		mContext = null;
	}
	
	public GS1DataBarLimitedSecurityLevel getGs1DatabarLimitedSecurityLevel(){
		return mGs1DatabarLimitedSecurityLevel;
	}
	
	public void setGs1DatabarLimitedSecurityLevel(GS1DataBarLimitedSecurityLevel value){
		mGs1DatabarLimitedSecurityLevel = value;
	}
	
	public boolean getConvertGs1DatabarToUpcEan(){
		return mIsConvertGs1DatabarToUpcEan;
	}
	
	public void setConvertGs1DatabarToUpcEan(boolean value){
		mIsConvertGs1DatabarToUpcEan = value;
	}
	
	@Override
	public void display() {
		
	}

	@Override
	public void showDialog(Context context, String title, final IValueChangedListener changedListener,
			final ICancelListener cancelListener) {
		
		mContext = context;
		
		LinearLayout root = (LinearLayout) LinearLayout.inflate(context, R.layout.dialog_barcode_option_ssi_2d_gs1_databar, null);
		
		txtGs1DatabarLimitedSecurityLevel = (TextView) root.findViewById(R.id.gs1_databar_limited_security_level);
		txtGs1DatabarLimitedSecurityLevel.setOnClickListener(this);
		chkConvertGs1DatabarToUpcEan = (CheckBox) root.findViewById(R.id.convert_gs1_databar_to_upc_ean);
		chkConvertGs1DatabarToUpcEan.setOnCheckedChangeListener(this);
		
		dlgGs1DatabarLimitedSecurityLevel = new EnumListDialog(txtGs1DatabarLimitedSecurityLevel, GS1DataBarLimitedSecurityLevel.values());
		dlgGs1DatabarLimitedSecurityLevel.setValue(mGs1DatabarLimitedSecurityLevel);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setView(root);
		builder.setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				mGs1DatabarLimitedSecurityLevel = (GS1DataBarLimitedSecurityLevel)dlgGs1DatabarLimitedSecurityLevel.getValue();
				mIsConvertGs1DatabarToUpcEan = chkConvertGs1DatabarToUpcEan.isChecked();
				
				if(changedListener != null)
					changedListener.onValueChanged(OptionGs1DatabarDialog.this);
				
				ATLog.i(TAG, INFO, "INFO. showDialog().$PositiveButton.onClick()");
			}
		});
		
		builder.setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				if(cancelListener != null)
					cancelListener.onCanceled(OptionGs1DatabarDialog.this); 
				
				ATLog.i(TAG, INFO, "INFO. showDialog().$NegativeButton.onClick()");
			}
		});
		
		builder.setCancelable(true);
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				
				if(cancelListener != null)
					cancelListener.onCanceled(OptionGs1DatabarDialog.this); 
				
				ATLog.i(TAG, INFO, "INFO. showDialog().onCancel()");
			}
		});
		
		AlertDialog dialog = builder.create();
		dialog.setOnShowListener(new DialogInterface.OnShowListener() {
			
			@Override
			public void onShow(DialogInterface dialog) {
				
				dlgGs1DatabarLimitedSecurityLevel.display();
				chkConvertGs1DatabarToUpcEan.setChecked(mIsConvertGs1DatabarToUpcEan);
				
				ATLog.i(TAG, INFO, "INFO. showDialog().onShow()");
			}
		});
		dialog.show();
		
		ATLog.i(TAG, INFO, "INFO. showDialog()");
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.gs1_databar_limited_security_level:
			dlgGs1DatabarLimitedSecurityLevel.showDialog(mContext, R.string.gs1_databar_limited_security_level);
			break;
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton view, boolean isChecked) {
		switch(view.getId()){
		case R.id.convert_gs1_databar_to_upc_ean:
			break;
		}
		
	}

}
