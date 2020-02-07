package com.atid.app.atx.dialog.spc;

import com.atid.app.atx.R;
import com.atid.app.atx.dialog.BaseDialog;
import com.atid.app.atx.dialog.EnumListDialog;
import com.atid.lib.module.barcode.spc.type.AustralianPostInterpretation;
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

public class OptionPostalCodes2D extends BaseDialog implements OnClickListener, OnCheckedChangeListener{
	private static final String TAG = OptionPostalCodes2D.class.getSimpleName();
	private static final int INFO = ATLog.L2;
	
	private CheckBox chkPlanetCodeCheckDigit;
	private CheckBox chkPostnetCheckDigit;
	private TextView txtAustralianPostInterpretation;
	
	private EnumListDialog dlgAustralianPostInterpretation;
	
	private boolean mIsPlanetCodeCheckDigit;
	private boolean mIsPostnetCheckDigit;
	private AustralianPostInterpretation mAustralianPostInterpretation;
	
	private Context mContext;
	
	public OptionPostalCodes2D() {
		super();
		
		mIsPlanetCodeCheckDigit = false;
		mIsPostnetCheckDigit = false;
		mAustralianPostInterpretation = AustralianPostInterpretation.BarOutput;
		
		mContext = null;
	}
	
	public boolean getPlanetCodeCheckDigit(){
		return mIsPlanetCodeCheckDigit;
	}
	
	public void setPlanetCodeCheckDigit(boolean value) {
		mIsPlanetCodeCheckDigit = value;
	}
	
	public boolean getPostnetCheckDigit(){
		return mIsPostnetCheckDigit;
	}
	
	public void setPostnetCheckDigit(boolean value) {
		mIsPostnetCheckDigit = value;
	}
	
	public AustralianPostInterpretation getAustralianPostInterpretation(){
		return mAustralianPostInterpretation;
	}
	
	public void setAustralianPostInterpretation(AustralianPostInterpretation value) {
		mAustralianPostInterpretation = value;
	}
	
	@Override
	public void display() {
		
	}

	@Override
	public void showDialog(Context context, String title, final IValueChangedListener changedListener,
			final ICancelListener cancelListener) {

		mContext = context;
		
		LinearLayout root = (LinearLayout) LinearLayout.inflate(context, 
				R.layout.dialog_barcode_option_spc_2d_postal_codes_2d, null);
		
		chkPlanetCodeCheckDigit = (CheckBox) root.findViewById(R.id.planet_code_check_digit);
		chkPlanetCodeCheckDigit.setOnCheckedChangeListener(this);
		chkPostnetCheckDigit = (CheckBox) root.findViewById(R.id.postnet_check_digit);
		chkPostnetCheckDigit.setOnCheckedChangeListener(this);
		txtAustralianPostInterpretation = (TextView) root.findViewById(R.id.australian_post_interpretation);
		txtAustralianPostInterpretation.setOnClickListener(this);
		
		dlgAustralianPostInterpretation = new EnumListDialog(txtAustralianPostInterpretation, AustralianPostInterpretation.values());
		dlgAustralianPostInterpretation.setValue(mAustralianPostInterpretation);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setView(root);
		builder.setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				mAustralianPostInterpretation = (AustralianPostInterpretation)dlgAustralianPostInterpretation.getValue();
				
				mIsPlanetCodeCheckDigit = chkPlanetCodeCheckDigit.isChecked();
				mIsPostnetCheckDigit = chkPostnetCheckDigit.isChecked();
				
				if(changedListener != null){
					changedListener.onValueChanged(OptionPostalCodes2D.this);
				}
			}
		});
		
		builder.setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {

				if(cancelListener != null){
					cancelListener.onCanceled(OptionPostalCodes2D.this);
				}
				ATLog.i(TAG, INFO, "INFO. showDialog().$NegativeButton.onClick()");
			}
		});
		
		builder.setCancelable(true);
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {

				if(cancelListener != null) {
					cancelListener.onCanceled(OptionPostalCodes2D.this);
				}
				ATLog.i(TAG, INFO, "INFO. showDialog().onCancel()");
			}
		});
		
		AlertDialog dialog = builder.create();
		dialog.setOnShowListener(new DialogInterface.OnShowListener() {
			
			@Override
			public void onShow(DialogInterface dialog) {
			
				dlgAustralianPostInterpretation.display();
				
				chkPlanetCodeCheckDigit.setChecked(mIsPlanetCodeCheckDigit);
				chkPostnetCheckDigit.setChecked(mIsPostnetCheckDigit);
				
				ATLog.i(TAG, INFO, "INFO. showDialog().onShow()");
			}
		});
		dialog.show();
		
		ATLog.i(TAG, INFO, "INFO. showDialog()");
	}

	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.australian_post_interpretation:
			dlgAustralianPostInterpretation.showDialog(mContext, R.string.australian_post_interpretation);
			break;
		}
		
	}

	@Override
	public void onCheckedChanged(CompoundButton view, boolean isChecked) {
		switch(view.getId()){
		case R.id.planet_code_check_digit:
			break;
		case R.id.postnet_check_digit:
			break;
		}
	}

}
