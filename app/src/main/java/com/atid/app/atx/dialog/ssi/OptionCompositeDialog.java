package com.atid.app.atx.dialog.ssi;

import com.atid.app.atx.dialog.BaseDialog;
import com.atid.app.atx.dialog.EnumListDialog;
import com.atid.app.atx.R;
import com.atid.lib.module.barcode.ssi.type.CompositeBeepMode;
import com.atid.lib.module.barcode.ssi.type.UpcCompositeMode;
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

public class OptionCompositeDialog extends BaseDialog implements OnClickListener, OnCheckedChangeListener{
	private static final String TAG = OptionCompositeDialog.class.getSimpleName();
	private static final int INFO = ATLog.L2;
	
	private TextView txtUpcCompositeMode;
	private TextView txtCompositeBeepMode;
	private CheckBox chkGs1128EmulationMode;
	
	private EnumListDialog dlgUpcCompositeMode;
	private EnumListDialog dlgCompositeBeepMode;
	
	private UpcCompositeMode mUpcCompositeMode;
	private CompositeBeepMode mCompositeBeepMode;
	private boolean mIsGs1128EmulationMode;
	
	private Context mContext;
	
	public OptionCompositeDialog(){
		super();
		
		mUpcCompositeMode = UpcCompositeMode.UPCAlwaysLinked;
		mCompositeBeepMode = CompositeBeepMode.BeepAsEach;
		mIsGs1128EmulationMode = false;
		
		mContext = null;
	}
	
	public UpcCompositeMode getUpcCompositeMode() {
		return mUpcCompositeMode;
	}
	
	public void setUpcCompositeMode(UpcCompositeMode value){
		mUpcCompositeMode = value;
	}
	
	public CompositeBeepMode getCompositeBeepMode(){
		return mCompositeBeepMode;
	}
	
	public void setCompositeBeepMode(CompositeBeepMode value){
		mCompositeBeepMode = value;
	}
	
	public boolean getGs1128Emulation(){
		return mIsGs1128EmulationMode;
	}
	
	public void setGs1128Emulation(boolean value){
		mIsGs1128EmulationMode = value;
	}
	
	@Override
	public void display() {
		
	}

	@Override
	public void showDialog(Context context, String title, final IValueChangedListener changedListener,
			final ICancelListener cancelListener) {
		
		mContext = context;
		
		LinearLayout root = (LinearLayout) LinearLayout.inflate(context, R.layout.dialog_barcode_option_ssi_2d_composite, null);
		
		txtUpcCompositeMode = (TextView) root.findViewById(R.id.upc_composite_mode);
		txtUpcCompositeMode.setOnClickListener(this);
		txtCompositeBeepMode = (TextView) root.findViewById(R.id.composite_beep_mode);
		txtCompositeBeepMode.setOnClickListener(this);
		chkGs1128EmulationMode = (CheckBox) root.findViewById(R.id.gs1_128_emulation_mode);
		chkGs1128EmulationMode.setOnCheckedChangeListener(this);
		
		dlgUpcCompositeMode = new EnumListDialog(txtUpcCompositeMode, UpcCompositeMode.values());
		dlgUpcCompositeMode.setValue(mUpcCompositeMode);
		dlgCompositeBeepMode = new EnumListDialog(txtCompositeBeepMode, CompositeBeepMode.values());
		dlgCompositeBeepMode.setValue(mCompositeBeepMode);				
		
		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(R.string.composite);
		builder.setView(root);
		builder.setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				mUpcCompositeMode = (UpcCompositeMode) dlgUpcCompositeMode.getValue();
				mCompositeBeepMode = (CompositeBeepMode) dlgCompositeBeepMode.getValue(); 
				mIsGs1128EmulationMode = chkGs1128EmulationMode.isChecked();
				
				if(changedListener != null)
					changedListener.onValueChanged(OptionCompositeDialog.this);
				
				ATLog.i(TAG, INFO, "INFO. showDialog().$PositiveButton.onClick()");
			}
		});
		
		builder.setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				if(cancelListener != null)
					cancelListener.onCanceled(OptionCompositeDialog.this); 
				
				ATLog.i(TAG, INFO, "INFO. showDialog().$NegativeButton.onClick()");
			}
		});
		
		builder.setCancelable(true);
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				
				if(cancelListener != null)
					cancelListener.onCanceled(OptionCompositeDialog.this); 
				
				ATLog.i(TAG, INFO, "INFO. showDialog().onCancel()");
			}
		});
		
		AlertDialog dialog = builder.create();
		dialog.setOnShowListener(new DialogInterface.OnShowListener() {
			
			@Override
			public void onShow(DialogInterface dialog) {
				
				dlgUpcCompositeMode.display();
				dlgCompositeBeepMode.display();				
				chkGs1128EmulationMode.setChecked(mIsGs1128EmulationMode);

				
				ATLog.i(TAG, INFO, "INFO. showDialog().onShow()");
			}
		});
		dialog.show();
		
		ATLog.i(TAG, INFO, "INFO. showDialog()");
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.upc_composite_mode:
			dlgUpcCompositeMode.showDialog(mContext, R.string.upc_composite_mode);
			break;
		case R.id.composite_beep_mode:
			dlgCompositeBeepMode.showDialog(mContext, R.string.composite_beep_mode);
			break;
		
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton view, boolean isChecked) {
		switch(view.getId()){
		case R.id.gs1_128_emulation_mode:
			break;
		}
	}

}
