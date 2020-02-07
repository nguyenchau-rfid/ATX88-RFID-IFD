package com.atid.app.atx.dialog.spc;

import com.atid.app.atx.R;
import com.atid.app.atx.dialog.BaseDialog;
import com.atid.app.atx.dialog.EnumListDialog;
import com.atid.app.atx.dialog.NumberDialog;
import com.atid.lib.module.barcode.spc.type.Code39CheckCharacter;
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

public class OptionCode39 extends BaseDialog implements OnClickListener, OnCheckedChangeListener{
	private static final String TAG = OptionCode39.class.getSimpleName();
	private static final int INFO = ATLog.L2;
	
	private CheckBox chkCode39StartStopCharacters;
	private TextView txtCode39CheckCharacter;
	private TextView txtLengthMin;
	private TextView txtLengthMax;
	private CheckBox chkCode39Append;
	private CheckBox chkCode39FullASCII;
	private TextView txtCode39CodePage;
	
	private EnumListDialog dlgCode39CheckCharacter;
	private NumberDialog dlgLengthMin;
	private NumberDialog dlgLengthMax;
	private EnumListDialog dlgCode39CodePage;
	
	private boolean mIsCode39StartStopCharacters;
	private Code39CheckCharacter mCode39CheckCharacter;
	private int mLengthMin;
	private int mLengthMax;
	private boolean mIsCode39Append;
	private boolean mIsCode39FullASCII;
	private CodePages mCode39CodePage;
	
	private Context mContext;
	
	public OptionCode39() {
		super();
		
		mIsCode39StartStopCharacters = false;
		mCode39CheckCharacter = Code39CheckCharacter.NoCheckCharacter;
		mLengthMin = 0;
		mLengthMax = 48;
		mIsCode39Append = false;
		mIsCode39FullASCII = false;
		mCode39CodePage = CodePages.ISOIEC_2022;
		
		mContext = null;
	}

	public boolean getCode39StartStopCharacters() {
		return mIsCode39StartStopCharacters;
	}

	public void setCode39StartStopCharacters(boolean value){
		mIsCode39StartStopCharacters = value;
	}
	
	public Code39CheckCharacter getCode39CheckCharacter() {
		return mCode39CheckCharacter;
	}

	public void setCode39CheckCharacter(Code39CheckCharacter value){
		mCode39CheckCharacter = value;
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
	
	public boolean getCode39Append() {
		return mIsCode39Append;
	}

	public void setCode39Append(boolean value){
		mIsCode39Append = value;
	}
	
	public boolean getCode39FullASCII() {
		return mIsCode39FullASCII;
	}

	public void setCode39FullASCII(boolean value){
		mIsCode39FullASCII = value;
	}
	
	public CodePages getCode39CodePage() {
		return mCode39CodePage;
	}

	public void setCode39CodePage(CodePages value){
		mCode39CodePage = value;
	}
	
	@Override
	public void display() {
		
	}

	@Override
	public void showDialog(Context context, String title, final IValueChangedListener changedListener,
			final ICancelListener cancelListener) {

		mContext = context;
		
		LinearLayout root = (LinearLayout) LinearLayout.inflate(context, R.layout.dialog_barcode_option_spc_2d_code39, null);
		
		chkCode39StartStopCharacters = (CheckBox) root.findViewById(R.id.code39_start_stop_characters);
		chkCode39StartStopCharacters.setOnCheckedChangeListener(this);
		txtCode39CheckCharacter = (TextView) root.findViewById(R.id.code39_check_character);
		txtCode39CheckCharacter.setOnClickListener(this);
		txtLengthMin = (TextView) root.findViewById(R.id.min);
		txtLengthMin.setOnClickListener(this);
		txtLengthMax = (TextView) root.findViewById(R.id.max);
		txtLengthMax.setOnClickListener(this);
		chkCode39Append = (CheckBox) root.findViewById(R.id.code39_append);
		chkCode39Append.setOnCheckedChangeListener(this);
		chkCode39FullASCII = (CheckBox) root.findViewById(R.id.code39_full_ascii);
		chkCode39FullASCII.setOnCheckedChangeListener(this);
		txtCode39CodePage = (TextView) root.findViewById(R.id.code39_code_page);
		txtCode39CodePage.setOnClickListener(this);
		
		dlgCode39CheckCharacter = new EnumListDialog(txtCode39CheckCharacter, Code39CheckCharacter.values());
		dlgCode39CheckCharacter.setValue(mCode39CheckCharacter);
		dlgLengthMin = new NumberDialog(txtLengthMin);
		dlgLengthMin.setValue(mLengthMin);
		dlgLengthMax = new NumberDialog(txtLengthMax);
		dlgLengthMax.setValue(mLengthMax);
		dlgCode39CodePage = new EnumListDialog(txtCode39CodePage, CodePages.values());
		dlgCode39CodePage.setValue(mCode39CodePage);
				
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setView(root);
		builder.setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				mCode39CheckCharacter = (Code39CheckCharacter)dlgCode39CheckCharacter.getValue();
				mLengthMin = dlgLengthMin.getValue();
				mLengthMax = dlgLengthMax.getValue();
				mCode39CodePage = (CodePages)dlgCode39CodePage.getValue();
						
				mIsCode39StartStopCharacters = chkCode39StartStopCharacters.isChecked();
				mIsCode39Append = chkCode39Append.isChecked();
				mIsCode39FullASCII = chkCode39FullASCII.isChecked();
				
				if(changedListener != null){
					changedListener.onValueChanged(OptionCode39.this);
				}
			}
		});
		
		builder.setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {

				if(cancelListener != null){
					cancelListener.onCanceled(OptionCode39.this);
				}
				ATLog.i(TAG, INFO, "INFO. showDialog().$NegativeButton.onClick()");
			}
		});
		
		builder.setCancelable(true);
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {

				if(cancelListener != null) {
					cancelListener.onCanceled(OptionCode39.this);
				}
				ATLog.i(TAG, INFO, "INFO. showDialog().onCancel()");
			}
		});
		
		AlertDialog dialog = builder.create();
		dialog.setOnShowListener(new DialogInterface.OnShowListener() {
			
			@Override
			public void onShow(DialogInterface dialog) {
				
				dlgCode39CheckCharacter.display();
				dlgLengthMin.display();
				dlgLengthMax.display();
				dlgCode39CodePage.display();
						
				chkCode39StartStopCharacters.setChecked(mIsCode39StartStopCharacters);
				chkCode39Append.setChecked(mIsCode39Append);
				chkCode39FullASCII.setChecked(mIsCode39FullASCII);
				
				ATLog.i(TAG, INFO, "INFO. showDialog().onShow()");
			}
		});
		dialog.show();

		ATLog.i(TAG, INFO, "INFO. showDialog()");
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.code39_check_character:
			dlgCode39CheckCharacter.showDialog(mContext, R.string.code39_check_character);
			break;
		case R.id.min:
			dlgLengthMin.showDialog(mContext, R.string.min);
			break;
		case R.id.max:
			dlgLengthMax.showDialog(mContext, R.string.max);
			break;		
		case R.id.code39_code_page:
			dlgCode39CodePage.showDialog(mContext, R.string.code39_code_page);
			break;		
		}
		
	}

	@Override
	public void onCheckedChanged(CompoundButton view, boolean isChecked) {
		switch(view.getId()){
		case R.id.code39_start_stop_characters:
			break;
		case R.id.code39_append:
			break;
		case R.id.code39_full_ascii:
			break;
		}
		
	}

}
