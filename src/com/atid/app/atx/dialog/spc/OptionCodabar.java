package com.atid.app.atx.dialog.spc;

import com.atid.app.atx.R;
import com.atid.app.atx.dialog.BaseDialog;
import com.atid.app.atx.dialog.EnumListDialog;
import com.atid.app.atx.dialog.NumberDialog;
import com.atid.lib.module.barcode.spc.type.CodabarCheckCharacter;
import com.atid.lib.module.barcode.spc.type.CodabarConcatenation;
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

public class OptionCodabar extends BaseDialog implements OnClickListener, OnCheckedChangeListener{
	private static final String TAG = OptionCodabar.class.getSimpleName();
	private static final int INFO = ATLog.L2;
	
	private CheckBox chkCodabarStartStopCharacters;
	private TextView txtCodabarCheckCharacter;
	private TextView txtCodabarConcatenation;
	private TextView txtLengthMin;
	private TextView txtLengthMax;
	
	private NumberDialog dlgLengthMin;
	private NumberDialog dlgLengthMax;
	private EnumListDialog dlgCodabarCheckCharacter;
	private EnumListDialog dlgCodabarConcatenation;
	
	private boolean mIsCodabarStartStopCharacters;
	private CodabarCheckCharacter mCodabarCheckCharacter;
	private CodabarConcatenation mCodabarConcatenation;
	private int mLengthMin;
	private int mLengthMax;
	
	private Context mContext;
	
	public OptionCodabar() {
		super();
		
		mIsCodabarStartStopCharacters = false;
		mCodabarCheckCharacter = CodabarCheckCharacter.NoCheckCharacter;
		mCodabarConcatenation = CodabarConcatenation.Off;
		mLengthMin = 4;
		mLengthMax = 60;
		
		mContext = null;
	}
	
	public boolean getCodabarStartStopCharacters() {
		return mIsCodabarStartStopCharacters;
	}

	public void setCodabarStartStopCharacters(boolean value){
		mIsCodabarStartStopCharacters = value;
	}
	
	public CodabarCheckCharacter getCodabarCheckCharacter() {
		return mCodabarCheckCharacter;
	}

	public void setCodabarCheckCharacter(CodabarCheckCharacter value){
		mCodabarCheckCharacter = value;
	}
	
	public CodabarConcatenation getCodabarConcatenation() {
		return mCodabarConcatenation;
	}

	public void setCodabarConcatenation(CodabarConcatenation value){
		mCodabarConcatenation = value;
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showDialog(Context context, String title, final IValueChangedListener changedListener,
			final ICancelListener cancelListener) {

		mContext = context;
		
		LinearLayout root = (LinearLayout) LinearLayout.inflate(context, R.layout.dialog_barcode_option_spc_2d_codabar, null);
		
		chkCodabarStartStopCharacters= (CheckBox ) root.findViewById(R.id.codabar_start_stop_characters);
		chkCodabarStartStopCharacters.setOnCheckedChangeListener(this);
		txtCodabarCheckCharacter = (TextView) root.findViewById(R.id.codabar_checkcharacter);
		txtCodabarCheckCharacter.setOnClickListener(this);
		txtCodabarConcatenation = (TextView) root.findViewById(R.id.codabar_concatenation);
		txtCodabarConcatenation.setOnClickListener(this);
		txtLengthMin = (TextView) root.findViewById(R.id.min);
		txtLengthMin.setOnClickListener(this);
		txtLengthMax = (TextView) root.findViewById(R.id.max);
		txtLengthMax.setOnClickListener(this);
		
		dlgLengthMin = new NumberDialog(txtLengthMin);
		dlgLengthMin.setValue(mLengthMin);
		dlgLengthMax = new NumberDialog(txtLengthMax);
		dlgLengthMax.setValue(mLengthMax);
		dlgCodabarCheckCharacter = new EnumListDialog(txtCodabarCheckCharacter, CodabarCheckCharacter.values());
		dlgCodabarCheckCharacter.setValue(mCodabarCheckCharacter);
		dlgCodabarConcatenation = new EnumListDialog(txtCodabarConcatenation, CodabarConcatenation.values());
		dlgCodabarConcatenation.setValue(mCodabarConcatenation);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setView(root);
		builder.setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
			
				mLengthMin = dlgLengthMin.getValue();
				mLengthMax = dlgLengthMax.getValue();
				
				mCodabarCheckCharacter = (CodabarCheckCharacter) dlgCodabarCheckCharacter.getValue();
				mCodabarConcatenation = (CodabarConcatenation) dlgCodabarConcatenation.getValue();
				
				mIsCodabarStartStopCharacters = chkCodabarStartStopCharacters.isChecked();
				
				if(changedListener != null){
					changedListener.onValueChanged(OptionCodabar.this);
				}
			}
		});
		
		builder.setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {

				if(cancelListener != null){
					cancelListener.onCanceled(OptionCodabar.this);
				}
				ATLog.i(TAG, INFO, "INFO. showDialog().$NegativeButton.onClick()");
			}
		});
		
		builder.setCancelable(true);
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {

				if(cancelListener != null) {
					cancelListener.onCanceled(OptionCodabar.this);
				}
				ATLog.i(TAG, INFO, "INFO. showDialog().onCancel()");
			}
		});
		
		AlertDialog dialog = builder.create();
		dialog.setOnShowListener(new DialogInterface.OnShowListener() {
			
			@Override
			public void onShow(DialogInterface dialog) {
			
				dlgLengthMin.display();
				dlgLengthMax.display();
				
				dlgCodabarCheckCharacter.display();
				dlgCodabarConcatenation.display();
				
				chkCodabarStartStopCharacters.setChecked(mIsCodabarStartStopCharacters);
				
				ATLog.i(TAG, INFO, "INFO. showDialog().onShow()");
			}
		});
		dialog.show();
		
		ATLog.i(TAG, INFO, "INFO. showDialog()");
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.min:
			dlgLengthMin.showDialog(mContext, R.string.min);
			break;
		case R.id.max:
			dlgLengthMax.showDialog(mContext, R.string.max);
			break;		
		case R.id.codabar_checkcharacter:
			dlgCodabarCheckCharacter.showDialog(mContext, R.string.codabar_checkcharacter);
			break;		
		case R.id.codabar_concatenation:
			dlgCodabarConcatenation.showDialog(mContext, R.string.codabar_checkcharacter);
			break;		
		}
		
	}


	@Override
	public void onCheckedChanged(CompoundButton view, boolean isChecked) {
		switch(view.getId()){
		case R.id.codabar_start_stop_characters:
			break;
		}
		
	}

}
