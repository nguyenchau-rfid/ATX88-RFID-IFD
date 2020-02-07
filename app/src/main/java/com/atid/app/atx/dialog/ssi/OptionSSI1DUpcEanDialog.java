package com.atid.app.atx.dialog.ssi;

import com.atid.app.atx.dialog.BaseDialog;
import com.atid.app.atx.dialog.EnumListDialog;
import com.atid.app.atx.dialog.NumberUnitDialog;
import com.atid.app.atx.R;
import com.atid.lib.module.barcode.ssi.type.BooklandISBNFormat;
import com.atid.lib.module.barcode.ssi.type.CouponReport;
import com.atid.lib.module.barcode.ssi.type.DecodeUpcEanJanSupplementals;
import com.atid.lib.module.barcode.ssi.type.Preamble;
import com.atid.lib.module.barcode.ssi.type.UpcEanSecurityLevel;
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

public class OptionSSI1DUpcEanDialog extends BaseDialog implements OnClickListener, OnCheckedChangeListener{
	private static final String TAG = OptionSSI1DUpcEanDialog.class.getSimpleName();
	private static final int INFO = ATLog.L2;
	
	private TextView txtIsbnFormat;
	private TextView txtDecodeUpcEanJanSupplementals;
	private TextView txtUpcEanJanSupplementalRedundancy;
	private CheckBox chkTransmitUpcACheckDigit;
	private CheckBox chkTransmitUpcECheckDigit;
	private CheckBox chkTransmitUpcE1CheckDigit;
	private TextView txtUpcAPreamble;
	private TextView txtUpcEPreamble;
	private TextView txtUpcE1Preamble;
	private CheckBox chkConvertUpcEToUpcA;
	private CheckBox chkConvertUpcE1ToUpcA;
	private CheckBox chkEan8Jan8Extend;
	private TextView txtUpcEanSecurityLevel;
	private TextView txtCouponReport;
	 
	private EnumListDialog dlgIsbnFormat;
	private EnumListDialog dlgDecodeUpcEanJanSupplementals;
	private NumberUnitDialog dlgUpcEanJanSupplementalRedundancy;
	private EnumListDialog dlgUpcAPreamble;
	private EnumListDialog dlgUpcEPreamble;
	private EnumListDialog dlgUpcE1Preamble;
	private EnumListDialog dlgUpcEanSecurityLevel;
	private EnumListDialog dlgCouponReport;
	
	private BooklandISBNFormat mIsbnFormat;
	private DecodeUpcEanJanSupplementals mDecodeUpcEanJanSupplementals;
	private int mUpcEanJanSupplementalRedundancy;
	private boolean mIsTransmitUpcACheckDigit;
	private boolean mIsTransmitUpcECheckDigit;
	private boolean mIsTransmitUpcE1CheckDigit;
	private Preamble mUpcAPreamble;
	private Preamble mUpcEPreamble;
	private Preamble mUpcE1Preamble;
	private boolean mIsConvertUpcEToUpcA;
	private boolean mIsConvertUpcE1ToUpcA;
	private boolean mIsEan8Jan8Extend;
	private UpcEanSecurityLevel mUpcEanSecurityLevel;
	private CouponReport mCouponReport;
	
	private Context mContext;
	
	public OptionSSI1DUpcEanDialog(){
		super();

		mIsbnFormat = BooklandISBNFormat.ISBN10;
		mDecodeUpcEanJanSupplementals = DecodeUpcEanJanSupplementals.Ignore;
		mUpcEanJanSupplementalRedundancy = 10;
		mIsTransmitUpcACheckDigit = true;
		mIsTransmitUpcECheckDigit = true;
		mIsTransmitUpcE1CheckDigit = true;
		mUpcAPreamble = Preamble.SystemCharacter;
		mUpcEPreamble = Preamble.SystemCharacter;
		mUpcE1Preamble = Preamble.SystemCharacter;
		mIsConvertUpcEToUpcA = false;
		mIsConvertUpcE1ToUpcA = false;
		mIsEan8Jan8Extend = false;
		mUpcEanSecurityLevel = UpcEanSecurityLevel.Level1;
		mCouponReport = CouponReport.NewCoupon;
		
		mContext = null;
	}
	
	public BooklandISBNFormat getBooklandISBNFormat(){
		return mIsbnFormat;
	}
	
	public void setBooklandISBNFormat(BooklandISBNFormat value){
		mIsbnFormat = value;
	}
	
	public DecodeUpcEanJanSupplementals getDecodeUpcEanJanSupplementals(){
		return mDecodeUpcEanJanSupplementals;
	}
	
	public void setDecodeUpcEanJanSupplementals(DecodeUpcEanJanSupplementals value){
		mDecodeUpcEanJanSupplementals = value;
	}
	
	public int getUpcEanJanSupplementalRedundancy(){
		return mUpcEanJanSupplementalRedundancy;
	}
	
	public void setUpcEanJanSupplementalRedundancy(int value){
		mUpcEanJanSupplementalRedundancy = value;
	}
	
	public boolean getTransmitUpcACheckDigit(){
		return mIsTransmitUpcACheckDigit;
	}
	
	public void setTransmitUpcACheckDigit(boolean value){
		mIsTransmitUpcACheckDigit = value;
	}

	public boolean getTransmitUpcECheckDigit(){
		return mIsTransmitUpcECheckDigit;
	}
	public void setTransmitUpcECheckDigit(boolean value){
		mIsTransmitUpcECheckDigit = value;
	}

	public boolean getTransmitUpcE1CheckDigit(){
		return mIsTransmitUpcE1CheckDigit;
	}
	public void setTransmitUpcE1CheckDigit(boolean value){
		mIsTransmitUpcE1CheckDigit = value;
	}

	public Preamble getUpcAPreamble() {
		return mUpcAPreamble;
	}
	
	public void setUpcAPreamble(Preamble value){
		mUpcAPreamble = value;
	}

	public Preamble getUpcEPreamble() {
		return mUpcEPreamble;
	}

	public void setUpcEPreamble(Preamble value){
		mUpcEPreamble = value;
	}

	public Preamble getUpcE1Preamble() {
		return mUpcE1Preamble;
	}

	public void setUpcE1Preamble(Preamble value){
		mUpcE1Preamble = value;
	}

	public boolean getConvertUpcEToUpcA(){
		return mIsConvertUpcEToUpcA;
	}

	public void setConvertUpcEToUpcA(boolean value){
		mIsConvertUpcEToUpcA = value;
	}
	
	public boolean getConvertUpcE1ToUpcA(){
		return mIsConvertUpcE1ToUpcA;
	}

	public void setConvertUpcE1ToUpcA(boolean value){
		mIsConvertUpcE1ToUpcA = value;
	}
	
	public boolean getEan8Jan8Extend(){
		return mIsEan8Jan8Extend;
	}

	public void setEan8Jan8Extend(boolean value){
		mIsEan8Jan8Extend = value;
	}
	
	public UpcEanSecurityLevel getUpcEanSecurityLevel(){
		return mUpcEanSecurityLevel;
	}
	
	public void setUpcEanSecurityLevel(UpcEanSecurityLevel value){
		mUpcEanSecurityLevel = value;
	}
		
	public CouponReport getCouponReport(){
		return mCouponReport;
	}

	public void setCouponReport(CouponReport value){
		mCouponReport = value;
	}
	
	@Override
	public void display() {
		
	}

	@Override
	public void showDialog(Context context, String title, final IValueChangedListener changedListener,
			final ICancelListener cancelListener) {
		
		mContext = context;
		
		LinearLayout root = (LinearLayout) LinearLayout.inflate(context, R.layout.dialog_barcode_option_ssi_1d_upc_ean, null);
	
		txtIsbnFormat = (TextView) root.findViewById(R.id.bookland_isbn_format); 
		txtIsbnFormat.setOnClickListener(this);
		txtDecodeUpcEanJanSupplementals = (TextView) root.findViewById(R.id.decode_upc_ean_jan_supplementals);  
		txtDecodeUpcEanJanSupplementals.setOnClickListener(this);
		txtUpcEanJanSupplementalRedundancy = (TextView) root.findViewById(R.id.upc_ean_jan_supplemental_redundancy);  
		txtUpcEanJanSupplementalRedundancy.setOnClickListener(this);
		chkTransmitUpcACheckDigit = (CheckBox) root.findViewById(R.id.transmit_upc_a_check_digit);    
		chkTransmitUpcACheckDigit.setOnCheckedChangeListener(this);
		chkTransmitUpcECheckDigit = (CheckBox) root.findViewById(R.id.transmit_upc_e_check_digit);   
		chkTransmitUpcECheckDigit.setOnCheckedChangeListener(this);
		chkTransmitUpcE1CheckDigit = (CheckBox) root.findViewById(R.id.transmit_upc_e1_check_digit);   
		chkTransmitUpcE1CheckDigit.setOnCheckedChangeListener(this);
		txtUpcAPreamble = (TextView) root.findViewById(R.id.upc_a_preamble); 
		txtUpcAPreamble.setOnClickListener(this);
		txtUpcEPreamble = (TextView) root.findViewById(R.id.upc_e_preamble);      
		txtUpcEPreamble.setOnClickListener(this);
		txtUpcE1Preamble = (TextView) root.findViewById(R.id.upc_e1_preamble);   
		txtUpcE1Preamble.setOnClickListener(this);
		chkConvertUpcEToUpcA = (CheckBox) root.findViewById(R.id.convert_upc_e_to_upc_a);      
		chkConvertUpcEToUpcA.setOnCheckedChangeListener(this);
		chkConvertUpcE1ToUpcA = (CheckBox) root.findViewById(R.id.convert_upcd_e1_to_upc_a); 
		chkConvertUpcE1ToUpcA.setOnCheckedChangeListener(this);
		chkEan8Jan8Extend = (CheckBox) root.findViewById(R.id.ean8_jan8_extend);      
		chkEan8Jan8Extend.setOnCheckedChangeListener(this);
		txtUpcEanSecurityLevel = (TextView) root.findViewById(R.id.upc_ean_security_level); 
		txtUpcEanSecurityLevel.setOnClickListener(this);
		txtCouponReport = (TextView) root.findViewById(R.id.coupon_report);                     
		txtCouponReport.setOnClickListener(this);

		
		
		dlgIsbnFormat = new EnumListDialog(txtIsbnFormat , BooklandISBNFormat.values());
		dlgIsbnFormat.setValue(mIsbnFormat);
		dlgDecodeUpcEanJanSupplementals = new EnumListDialog(txtDecodeUpcEanJanSupplementals, DecodeUpcEanJanSupplementals.values());
		dlgDecodeUpcEanJanSupplementals.setValue(mDecodeUpcEanJanSupplementals);
		dlgUpcEanJanSupplementalRedundancy = new NumberUnitDialog(txtUpcEanJanSupplementalRedundancy, 
				mContext.getResources().getString(R.string.unit_times));
		dlgUpcEanJanSupplementalRedundancy.setValue(mUpcEanJanSupplementalRedundancy);
		dlgUpcAPreamble = new EnumListDialog(txtUpcAPreamble, Preamble.values());
		dlgUpcAPreamble.setValue(mUpcAPreamble);
		dlgUpcEPreamble = new EnumListDialog(txtUpcEPreamble, Preamble.values());
		dlgUpcEPreamble.setValue(mUpcEPreamble);
		dlgUpcE1Preamble = new EnumListDialog(txtUpcE1Preamble, Preamble.values());
		dlgUpcE1Preamble.setValue(mUpcE1Preamble);
		dlgUpcEanSecurityLevel = new EnumListDialog(txtUpcEanSecurityLevel, UpcEanSecurityLevel.values());
		dlgUpcEanSecurityLevel.setValue(mUpcEanSecurityLevel);
		dlgCouponReport = new EnumListDialog(txtCouponReport, CouponReport.values());
		dlgCouponReport.setValue(mCouponReport);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setView(root);
		builder.setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {

				mIsbnFormat = (BooklandISBNFormat)dlgIsbnFormat.getValue();
				mDecodeUpcEanJanSupplementals = (DecodeUpcEanJanSupplementals) dlgDecodeUpcEanJanSupplementals.getValue();
				mUpcEanJanSupplementalRedundancy = dlgUpcEanJanSupplementalRedundancy.getValue();
				mUpcEanSecurityLevel = (UpcEanSecurityLevel) dlgUpcEanSecurityLevel.getValue();
				mUpcAPreamble = (Preamble) dlgUpcAPreamble.getValue();
				mUpcEPreamble = (Preamble) dlgUpcEPreamble.getValue();
				mUpcE1Preamble = (Preamble) dlgUpcE1Preamble.getValue();
				mCouponReport = (CouponReport) dlgCouponReport.getValue();
				
				mIsTransmitUpcACheckDigit = chkTransmitUpcACheckDigit.isChecked();
				mIsTransmitUpcECheckDigit = chkTransmitUpcECheckDigit.isChecked();
				mIsTransmitUpcE1CheckDigit = chkTransmitUpcE1CheckDigit.isChecked();
				
				mIsConvertUpcEToUpcA = chkConvertUpcEToUpcA.isChecked();
				mIsConvertUpcE1ToUpcA = chkConvertUpcE1ToUpcA.isChecked();
				mIsEan8Jan8Extend = chkEan8Jan8Extend.isChecked();
				
				if(changedListener != null)
					changedListener.onValueChanged(OptionSSI1DUpcEanDialog.this);
				
				ATLog.i(TAG, INFO, "INFO. showDialog().$PositiveButton.onClick()");
			}
		});
		
		builder.setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {

				
				if(cancelListener != null)
					cancelListener.onCanceled(OptionSSI1DUpcEanDialog.this); 
				
				ATLog.i(TAG, INFO, "INFO. showDialog().$NegativeButton.onClick()");
			}
		});
		
		builder.setCancelable(true);
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				
				if(cancelListener != null)
					cancelListener.onCanceled(OptionSSI1DUpcEanDialog.this); 
				
				ATLog.i(TAG, INFO, "INFO. showDialog().onCancel()");
			}
		});
		
		AlertDialog dialog = builder.create();
		dialog.setOnShowListener(new DialogInterface.OnShowListener() {
			
			@Override
			public void onShow(DialogInterface dialog) {
				
				dlgIsbnFormat.display();
				dlgDecodeUpcEanJanSupplementals.display();
				dlgUpcEanJanSupplementalRedundancy.display();
				dlgUpcAPreamble.display();
				dlgUpcEPreamble.display();
				dlgUpcE1Preamble.display();
				dlgUpcEanSecurityLevel.display();
				dlgCouponReport.display();
				
				chkTransmitUpcACheckDigit.setChecked(mIsTransmitUpcACheckDigit);
				chkTransmitUpcECheckDigit.setChecked(mIsTransmitUpcECheckDigit);
				chkTransmitUpcE1CheckDigit.setChecked(mIsTransmitUpcE1CheckDigit);
				
				chkConvertUpcEToUpcA.setChecked(mIsConvertUpcEToUpcA);
				chkConvertUpcE1ToUpcA.setChecked(mIsConvertUpcE1ToUpcA);
				chkEan8Jan8Extend.setChecked(mIsEan8Jan8Extend);
				
				ATLog.i(TAG, INFO, "INFO. showDialog().onShow()");
			}
		});
		dialog.show();
		
		ATLog.i(TAG, INFO, "INFO. showDialog()");
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.bookland_isbn_format:
			dlgIsbnFormat.showDialog(mContext, R.string.bookland_isbn_format);
			break;
		case R.id.decode_upc_ean_jan_supplementals:
			dlgDecodeUpcEanJanSupplementals.showDialog(mContext, R.string.decode_upc_ean_jan_supplematals);
			break;
		case R.id.upc_ean_jan_supplemental_redundancy:
			dlgUpcEanJanSupplementalRedundancy.showDialog(mContext, R.string.upc_ean_jan_supplemental_redundancy);
			break;
		case R.id.upc_a_preamble:
			dlgUpcAPreamble.showDialog(mContext, R.string.upc_a_preamble);
			break;
		case R.id.upc_e_preamble:
			dlgUpcEPreamble.showDialog(mContext, R.string.upc_e_preamble);
			break;
		case R.id.upc_e1_preamble:
			dlgUpcE1Preamble.showDialog(mContext, R.string.upc_e1_preamble);
			break;
		case R.id.upc_ean_security_level:
			dlgUpcEanSecurityLevel.showDialog(mContext, R.string.security_level);
			break;
		case R.id.coupon_report:
			dlgCouponReport.showDialog(mContext, R.string.coupon_report);
			break;
		
		}
		
	}

	@Override
	public void onCheckedChanged(CompoundButton view, boolean isChecked) {
		switch(view.getId()){
		case R.id.upc_reduced_quiet_zone:
			break;
		case R.id.transmit_upc_a_check_digit:
			break;
		case R.id.transmit_upc_e_check_digit:
			break;
		case R.id.transmit_upc_e1_check_digit:
			break;
		case R.id.convert_upc_e_to_upc_a:
			break;
		case R.id.convert_upcd_e1_to_upc_a:
			break;
		case R.id.ean8_jan8_extend:
			break;
		}
		
	}
	 
}
