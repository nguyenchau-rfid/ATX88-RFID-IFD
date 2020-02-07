package com.atid.app.atx.dialog.spc;

import com.atid.app.atx.R;
import com.atid.app.atx.dialog.BaseDialog;
import com.atid.app.atx.dialog.NumberDialog;
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

public class OptionPostalCodesLinear extends BaseDialog implements OnClickListener, OnCheckedChangeListener{
	private static final String TAG = OptionPostalCodesLinear.class.getSimpleName();
	private static final int INFO = ATLog.L2;
	
	private TextView txtChinaPostLengthMin;
	private TextView txtChinaPostLengthMax;
	private TextView txtKoreaPostLengthMin;
	private TextView txtKoreaPostLengthMax;
	private CheckBox chkKoreaPostCheckDigit;
	
	private NumberDialog dlgChinaPostLengthMin;
	private NumberDialog dlgChinaPostLengthMax;
	private NumberDialog dlgKoreaPostLengthMin;
	private NumberDialog dlgKoreaPostLengthMax;
	
	private int mChinaPostLengthMin;
	private int mChinaPostLengthMax;
	private int mKoreaPostLengthMin;
	private int mKoreaPostLengthMax;
	private boolean mIsKoreaPostCheckDigit;
	
	private Context mContext;
	
	public OptionPostalCodesLinear() {
		super();
		
		mChinaPostLengthMin = 4;
		mChinaPostLengthMax = 80;
		
		mKoreaPostLengthMin = 4;
		mKoreaPostLengthMax = 80;
		
		mIsKoreaPostCheckDigit = false;
		
		mContext = null;
	}
	
	public int getChinaPostLengthMin() {
		return mChinaPostLengthMin;
	}

	public void setChinaPostLengthMin(int value){
		mChinaPostLengthMin = value;
	}
	
	public int getChinaPostLengthMax() {
		return mChinaPostLengthMax;
	}

	public void setChinaPostLengthMax(int value){
		mChinaPostLengthMax = value;
	}
	
	public int getKoreaPostLengthMin() {
		return mKoreaPostLengthMin;
	}

	public void setKoreaPostLengthMin(int value){
		mKoreaPostLengthMin = value;
	}
	
	public int getKoreaPostLengthMax() {
		return mKoreaPostLengthMax;
	}

	public void setKoreaPostLengthMax(int value){
		mKoreaPostLengthMax = value;
	}
	
	public boolean getKoreaPostCheckDigit() {
		return mIsKoreaPostCheckDigit;
	}

	public void setKoreaPostCheckDigit(boolean value){
		mIsKoreaPostCheckDigit = value;
	}
	
	@Override
	public void display() {
		
	}

	@Override
	public void showDialog(Context context, String title, final IValueChangedListener changedListener,
			final ICancelListener cancelListener) {

		mContext = context;
		
		LinearLayout root = (LinearLayout) LinearLayout.inflate(context, 
				R.layout.dialog_barcode_option_spc_2d_postal_codes_linear, null);
		
		txtChinaPostLengthMin = (TextView) root.findViewById(R.id.chaina_post_min);
		txtChinaPostLengthMin.setOnClickListener(this);
		txtChinaPostLengthMax = (TextView) root.findViewById(R.id.chaina_post_max);
		txtChinaPostLengthMax.setOnClickListener(this);
		txtKoreaPostLengthMin = (TextView) root.findViewById(R.id.korea_post_min);
		txtKoreaPostLengthMin.setOnClickListener(this);
		txtKoreaPostLengthMax = (TextView) root.findViewById(R.id.korea_post_max);
		txtKoreaPostLengthMax.setOnClickListener(this);
		chkKoreaPostCheckDigit = (CheckBox) root.findViewById(R.id.korea_post_check_digit);
		chkKoreaPostCheckDigit.setOnCheckedChangeListener(this);
		
		dlgChinaPostLengthMin = new NumberDialog(txtChinaPostLengthMin);
		dlgChinaPostLengthMin.setValue(mChinaPostLengthMin);
		dlgChinaPostLengthMax = new NumberDialog(txtChinaPostLengthMax);
		dlgChinaPostLengthMax.setValue(mChinaPostLengthMax);
		
		dlgKoreaPostLengthMin = new NumberDialog(txtKoreaPostLengthMin);
		dlgKoreaPostLengthMin.setValue(mKoreaPostLengthMin);
		dlgKoreaPostLengthMax = new NumberDialog(txtKoreaPostLengthMax);
		dlgKoreaPostLengthMax.setValue(mKoreaPostLengthMax);
		
		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setView(root);
		builder.setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				mChinaPostLengthMin = dlgChinaPostLengthMin.getValue();
				mChinaPostLengthMax = dlgChinaPostLengthMax.getValue();
				
				mKoreaPostLengthMin = dlgKoreaPostLengthMin.getValue();
				mKoreaPostLengthMax = dlgKoreaPostLengthMax.getValue();
				
				mIsKoreaPostCheckDigit = chkKoreaPostCheckDigit.isChecked();
				
				if(changedListener != null){
					changedListener.onValueChanged(OptionPostalCodesLinear.this);
				}
			}
		});
		
		builder.setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {

				if(cancelListener != null){
					cancelListener.onCanceled(OptionPostalCodesLinear.this);
				}
				ATLog.i(TAG, INFO, "INFO. showDialog().$NegativeButton.onClick()");
			}
		});
		
		builder.setCancelable(true);
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {

				if(cancelListener != null) {
					cancelListener.onCanceled(OptionPostalCodesLinear.this);
				}
				ATLog.i(TAG, INFO, "INFO. showDialog().onCancel()");
			}
		});
		
		AlertDialog dialog = builder.create();
		dialog.setOnShowListener(new DialogInterface.OnShowListener() {
			
			@Override
			public void onShow(DialogInterface dialog) {

				dlgChinaPostLengthMin.display();
				dlgChinaPostLengthMax.display();
				
				dlgKoreaPostLengthMin.display();
				dlgKoreaPostLengthMax.display();
				
				chkKoreaPostCheckDigit.setChecked(mIsKoreaPostCheckDigit);
				ATLog.i(TAG, INFO, "INFO. showDialog().onShow()");
			}
		});
		dialog.show();
		
		ATLog.i(TAG, INFO, "INFO. showDialog()");
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.chaina_post_min:
			dlgChinaPostLengthMin.showDialog(mContext, R.string.min);
			break;
		case R.id.chaina_post_max:
			dlgChinaPostLengthMax.showDialog(mContext, R.string.max);
			break;
		case R.id.korea_post_min:
			dlgKoreaPostLengthMin.showDialog(mContext, R.string.min);
			break;
		case R.id.korea_post_max:
			dlgKoreaPostLengthMax.showDialog(mContext, R.string.max);
			break;			
		}
		
	}

	@Override
	public void onCheckedChanged(CompoundButton view, boolean isChecked) {
		switch(view.getId()){
		case R.id.korea_post_check_digit:
			break;
		}
				
	}

}
