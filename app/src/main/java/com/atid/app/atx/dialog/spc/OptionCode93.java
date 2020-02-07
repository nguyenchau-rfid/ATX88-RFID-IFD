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

public class OptionCode93 extends BaseDialog implements OnClickListener, OnCheckedChangeListener{
	private static final String TAG = OptionCode93.class.getSimpleName();
	private static final int INFO = ATLog.L2;
	
	private TextView txtLengthMin;
	private TextView txtLengthMax;
	private CheckBox chkCode93Append;
	private TextView txtCode93CodePage;
	
	private NumberDialog dlgLengthMin;
	private NumberDialog dlgLengthMax;
	private EnumListDialog dlgCode93CodePage;
	
	private int mLengthMin;
	private int mLengthMax;
	private boolean mIsCode93Append;
	private CodePages mCode93CodePage;
	
	private Context mContext;
	
	public OptionCode93() {
		super();
		
		mLengthMin = 0;
		mLengthMax = 80;
		mIsCode93Append = false;
		mCode93CodePage = CodePages.ISOIEC_2022;
		
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
	
	public boolean getCode93Append() {
		return mIsCode93Append;
	}

	public void setCode93Append(boolean value){
		mIsCode93Append = value;
	}
	
	public CodePages getCode93CodePage() {
		return mCode93CodePage;
	}

	public void setCode93CodePage(CodePages value){
		mCode93CodePage = value;
	}

	@Override
	public void display() {
		
	}

	@Override
	public void showDialog(Context context, String title, final IValueChangedListener changedListener,
			final ICancelListener cancelListener) {

		mContext = context;
		
		LinearLayout root = (LinearLayout) LinearLayout.inflate(context, R.layout.dialog_barcode_option_spc_2d_code93, null);
		
		txtLengthMin = (TextView) root.findViewById(R.id.min);
		txtLengthMin.setOnClickListener(this);
		txtLengthMax = (TextView) root.findViewById(R.id.max);
		txtLengthMax.setOnClickListener(this);
		chkCode93Append = (CheckBox) root.findViewById(R.id.code93_append);
		chkCode93Append.setOnCheckedChangeListener(this);
		txtCode93CodePage = (TextView) root.findViewById(R.id.code93_code_page);
		txtCode93CodePage.setOnClickListener(this);
		
		dlgLengthMin = new NumberDialog(txtLengthMin);
		dlgLengthMin.setValue(mLengthMin);
		dlgLengthMax = new NumberDialog(txtLengthMax);
		dlgLengthMax.setValue(mLengthMax);
		dlgCode93CodePage = new EnumListDialog(txtCode93CodePage, CodePages.values());
		dlgCode93CodePage.setValue(mCode93CodePage);
		
		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setView(root);
		builder.setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				mLengthMin = dlgLengthMin.getValue();
				mLengthMax = dlgLengthMax.getValue();
				mCode93CodePage = (CodePages)dlgCode93CodePage.getValue();
				
				mIsCode93Append = chkCode93Append.isChecked();
				
				if(changedListener != null){
					changedListener.onValueChanged(OptionCode93.this);
				}
			}
		});
		
		builder.setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {

				if(cancelListener != null){
					cancelListener.onCanceled(OptionCode93.this);
				}
				ATLog.i(TAG, INFO, "INFO. showDialog().$NegativeButton.onClick()");
			}
		});
		
		builder.setCancelable(true);
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {

				if(cancelListener != null) {
					cancelListener.onCanceled(OptionCode93.this);
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
				dlgCode93CodePage.display();
				
				chkCode93Append.setChecked(mIsCode93Append);
				
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
		case R.id.code93_code_page:
			dlgCode93CodePage.showDialog(mContext, R.string.code93_code_page);
			break;
		}
		
	}

	@Override
	public void onCheckedChanged(CompoundButton view, boolean isChecked) {
		switch(view.getId()){
		case R.id.code93_append:
			break;
		}
		
	}

}
