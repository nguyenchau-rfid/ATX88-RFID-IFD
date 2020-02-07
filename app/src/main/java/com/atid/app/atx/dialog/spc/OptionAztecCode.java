package com.atid.app.atx.dialog.spc;

import com.atid.app.atx.R;
import com.atid.app.atx.dialog.BaseDialog;
import com.atid.app.atx.dialog.EnumListDialog;
import com.atid.app.atx.dialog.NumberDialog;
import com.atid.lib.module.barcode.spc.type.CodePages;
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

public class OptionAztecCode extends BaseDialog implements OnClickListener, OnCheckedChangeListener{
	private static final String TAG = OptionAztecCode.class.getSimpleName();
	private static final int INFO = ATLog.L2;
	
	private TextView txtLengthMin;
	private TextView txtLengthMax;
	private CheckBox chkAztecAppend;
	private TextView txtAztecCodePage;
	
	private NumberDialog dlgLengthMin;
	private NumberDialog dlgLengthMax;
	private EnumListDialog dlgAztecCodePage;
	
	private int mLengthMin;
	private int mLengthMax;
	private boolean mIsAztecAppend;
	private CodePages mAztecCodePage;
	
	private Context mContext;
	
	public OptionAztecCode() {
		super();
		
		mLengthMin = 1;
		mLengthMax = 3832;
		mIsAztecAppend = true;
		mAztecCodePage = CodePages.ISOIEC_2022;
		
		mContext = null;
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
	
	public boolean getAztecAppend() {
		return mIsAztecAppend;
	}

	public void setAztecAppend(boolean value){
		mIsAztecAppend = value;
	}
	
	public CodePages getAztecCodePage() {
		return mAztecCodePage;
	}

	public void setAztecCodePage(CodePages value){
		mAztecCodePage = value;
	}
	
	@Override
	public void display() {
		
	}

	@Override
	public void showDialog(Context context, String title, final IValueChangedListener changedListener,
			final ICancelListener cancelListener) {

		mContext = context;
		
		LinearLayout root = (LinearLayout) LinearLayout.inflate(context, R.layout.dialog_barcode_option_spc_2d_aztec_code, null);
		
		txtLengthMin = (TextView) root.findViewById(R.id.min);
		txtLengthMin.setOnClickListener(this);
		txtLengthMax = (TextView) root.findViewById(R.id.max);
		txtLengthMax.setOnClickListener(this);
		chkAztecAppend = (CheckBox ) root.findViewById(R.id.aztec_append);
		chkAztecAppend.setOnCheckedChangeListener(this);
		txtAztecCodePage = (TextView) root.findViewById(R.id.aztec_code_page);
		txtAztecCodePage.setOnClickListener(this);
		
		dlgLengthMin = new NumberDialog(txtLengthMin);
		dlgLengthMin.setValue(mLengthMin);
		dlgLengthMax = new NumberDialog(txtLengthMax);
		dlgLengthMax.setValue(mLengthMax);
		dlgAztecCodePage = new EnumListDialog(txtAztecCodePage, CodePages.values());
		dlgAztecCodePage.setValue(mAztecCodePage);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setView(root);
		builder.setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
			
				mLengthMin = dlgLengthMin.getValue();
				mLengthMax = dlgLengthMax.getValue();
				
				mAztecCodePage = (CodePages) dlgAztecCodePage.getValue();
				mIsAztecAppend = chkAztecAppend.isChecked();
				
				if(changedListener != null){
					changedListener.onValueChanged(OptionAztecCode.this);
				}
			}
		});
		
		builder.setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {

				if(cancelListener != null){
					cancelListener.onCanceled(OptionAztecCode.this);
				}
				ATLog.i(TAG, INFO, "INFO. showDialog().$NegativeButton.onClick()");
			}
		});
		
		builder.setCancelable(true);
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {

				if(cancelListener != null) {
					cancelListener.onCanceled(OptionAztecCode.this);
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
				dlgAztecCodePage.display();
				
				chkAztecAppend.setChecked(mIsAztecAppend);
				
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
		case R.id.aztec_code_page:
			dlgAztecCodePage.showDialog(mContext, R.string.aztec_code_page);
			break;
		}
		
	}

	@Override
	public void onCheckedChanged(CompoundButton view, boolean isChecked) {
		switch(view.getId()){
		case R.id.aztec_append:
			break;
		}
		
	}

}
