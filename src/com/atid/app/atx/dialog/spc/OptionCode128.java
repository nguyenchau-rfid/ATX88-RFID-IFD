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

public class OptionCode128 extends BaseDialog implements OnClickListener, OnCheckedChangeListener{
	private static final String TAG = OptionCode128.class.getSimpleName();
	private static final int INFO = ATLog.L2;
	
	private CheckBox chkISBT128Concatenation;
	private TextView txtLengthMin;
	private TextView txtLengthMax;
	private CheckBox chkCode128Append;
	private TextView txtCode128CodePage;
	
	private NumberDialog dlgLengthMin;
	private NumberDialog dlgLengthMax;
	private EnumListDialog dlgCode128CodePage;
	
	private boolean mIsISBT128Concatenation;
	private int mLengthMin;
	private int mLengthMax;
	private boolean mIsCode128Append;
	private CodePages mCode128CodePage;
	
	private Context mContext;
	
	public OptionCode128() {
		super();
		
		mIsISBT128Concatenation = false;
		mLengthMin = 0;
		mLengthMax = 80;
		mIsCode128Append = true;
		mCode128CodePage = CodePages.ISOIEC_2022;
		
		mContext = null;
	}
	
	public boolean getISBT128Concatenation() {
		return mIsISBT128Concatenation;
	}

	public void setISBT128Concatenation(boolean value){
		mIsISBT128Concatenation = value;
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
	
	public boolean getCode128Append() {
		return mIsCode128Append;
	}

	public void setCode128Append(boolean value){
		mIsCode128Append = value;
	}
	
	public CodePages getCode128CodePage() {
		return mCode128CodePage;
	}

	public void setCode128CodePage(CodePages value){
		mCode128CodePage = value;
	}
	
	@Override
	public void display() {
		
	}

	@Override
	public void showDialog(Context context, String title, final IValueChangedListener changedListener,
			final ICancelListener cancelListener) {

		mContext = context;
		
		LinearLayout root = (LinearLayout) LinearLayout.inflate(context, R.layout.dialog_barcode_option_spc_2d_code128, null);
		chkISBT128Concatenation = (CheckBox) root.findViewById(R.id.isbt128_concatenation);
		chkISBT128Concatenation.setOnCheckedChangeListener(this);
		txtLengthMin = (TextView) root.findViewById(R.id.min);
		txtLengthMin.setOnClickListener(this);
		txtLengthMax = (TextView) root.findViewById(R.id.max);
		txtLengthMax.setOnClickListener(this);
		chkCode128Append = (CheckBox) root.findViewById(R.id.code128_append);
		chkCode128Append.setOnCheckedChangeListener(this);
		txtCode128CodePage = (TextView) root.findViewById(R.id.code128_code_page);
		txtCode128CodePage.setOnClickListener(this);
		
		dlgLengthMin = new NumberDialog(txtLengthMin);
		dlgLengthMin.setValue(mLengthMin);
		dlgLengthMax = new NumberDialog(txtLengthMax);
		dlgLengthMax.setValue(mLengthMax);
		dlgCode128CodePage = new EnumListDialog(txtCode128CodePage, CodePages.values());
		dlgCode128CodePage.setValue(mCode128CodePage);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setView(root);
		builder.setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				mLengthMin = dlgLengthMin.getValue();
				mLengthMax = dlgLengthMax.getValue();
				
				mCode128CodePage = (CodePages) dlgCode128CodePage.getValue();
				
				mIsISBT128Concatenation = chkISBT128Concatenation.isChecked();
				mIsCode128Append = chkCode128Append.isChecked();
				
				if(changedListener != null){
					changedListener.onValueChanged(OptionCode128.this);
				}
			}
		});
		
		builder.setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {

				if(cancelListener != null){
					cancelListener.onCanceled(OptionCode128.this);
				}
				ATLog.i(TAG, INFO, "INFO. showDialog().$NegativeButton.onClick()");
			}
		});
		
		builder.setCancelable(true);
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {

				if(cancelListener != null) {
					cancelListener.onCanceled(OptionCode128.this);
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
				dlgCode128CodePage.display();;
				
				chkISBT128Concatenation.setChecked(mIsISBT128Concatenation);
				chkCode128Append.setChecked(mIsCode128Append);
				
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
		case R.id.code128_code_page:
			dlgCode128CodePage.showDialog(mContext, R.string.code128_code_page);
			break;		
		}
		
	}

	@Override
	public void onCheckedChanged(CompoundButton view, boolean isChecked) {
		switch(view.getId()){
		case R.id.isbt128_concatenation:
			break;
		case R.id.code128_append:
			break;
		}
	}

}
