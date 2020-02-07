package com.atid.app.atx.dialog;

import com.atid.app.atx.R;
import com.atid.app.atx.dialog.BaseDialog.ICancelListener;
import com.atid.app.atx.dialog.ssi.OptionCodabarDialog;
import com.atid.app.atx.dialog.ssi.OptionCode11Dialog;
import com.atid.app.atx.dialog.ssi.OptionSSI1DCode128Dialog;
import com.atid.app.atx.dialog.ssi.OptionSSI1DCode39Dialog;
import com.atid.app.atx.dialog.ssi.OptionSSI1DI2of5Dialog;
import com.atid.app.atx.dialog.ssi.OptionCode93Dialog;
import com.atid.app.atx.dialog.ssi.OptionD2of5Dialog;
import com.atid.app.atx.dialog.ssi.OptionGs1DatabarDialog;
import com.atid.app.atx.dialog.ssi.OptionMatrix2of5Dialog;
import com.atid.app.atx.dialog.ssi.OptionMsiDialog;
import com.atid.app.atx.dialog.ssi.OptionSSI1DUpcEanDialog;
import com.atid.lib.module.barcode.ssi.type.BooklandISBNFormat;
import com.atid.lib.module.barcode.ssi.type.CodabarStartStopCharactersDetection;
import com.atid.lib.module.barcode.ssi.type.Code11CheckDigitVerification;
import com.atid.lib.module.barcode.ssi.type.CodeLength;
import com.atid.lib.module.barcode.ssi.type.CouponReport;
import com.atid.lib.module.barcode.ssi.type.DecodeUpcEanJanSupplementals;
import com.atid.lib.module.barcode.ssi.type.GS1DataBarLimitedSecurityLevel;
import com.atid.lib.module.barcode.ssi.type.I2of5CheckDigitVerification;
import com.atid.lib.module.barcode.ssi.type.ISBTConcatenation;
import com.atid.lib.module.barcode.ssi.type.MSICheckDigitAlgorithm;
import com.atid.lib.module.barcode.ssi.type.MSICheckDigits;
import com.atid.lib.module.barcode.ssi.type.Preamble;
import com.atid.lib.module.barcode.ssi.type.LinearSecurityLevel;
import com.atid.lib.module.barcode.ssi.type.UpcEanSecurityLevel;
import com.atid.lib.util.diagnotics.ATLog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SSI1DBarcodeOptionDialog implements OnClickListener {
	private static final String TAG = SSI1DBarcodeOptionDialog.class.getSimpleName();
	private static final int INFO = ATLog.L2;
	
	private LinearLayout linearOptionUpcEan;
	private LinearLayout linearOptionCode128;
	private LinearLayout linearOptionCode39;
	private LinearLayout linearOptionCode93;
	private LinearLayout linearOptionCode11;
	private LinearLayout linearOptionI2of5;
	private LinearLayout linearOptionD2of5;
	private LinearLayout linearOptionMatrix2of5;
	private LinearLayout linearOptionCodabar;
	private LinearLayout linearOptionMsi;
	private LinearLayout linearOptionGs1Databar;
	private LinearLayout linearOptionSecurityLevel;
	private LinearLayout linearBiDirectionalOptionRedundancy;
	
	private CheckBox chkBiDirectionalRedundancy;
	private TextView txtSecurityLevel;
	
	private OptionSSI1DUpcEanDialog dlgUpcEan;
	private OptionSSI1DCode128Dialog dlgCode128;
	private OptionSSI1DCode39Dialog dlgCode39;
	private OptionCode93Dialog dlgCode93;
	private OptionCode11Dialog dlgCode11;
	private OptionSSI1DI2of5Dialog dlgI2of5;
	private OptionD2of5Dialog dlgD2of5;
	private OptionMatrix2of5Dialog dlgMatrix2of5;
	private OptionCodabarDialog dlgCodabar;
	private OptionMsiDialog dlgMsi;
	private OptionGs1DatabarDialog dlgGs1Databar;
	
	private EnumListDialog dlgSecurityLevel;
	
	// ------------------------------------------------------
	// Loading value
	// ------------------------------------------------------
	
	// UPC / EAN
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
	private UpcEanSecurityLevel mUpcEanSecurityLevel;
	private boolean mIsEan8Jan8Extend;
	private CouponReport mCouponReport;
	
	// Code 128
	private CodeLength mCode128Length;
	private ISBTConcatenation mIsbtConcatenation;
	private boolean mIsCheckIsbtTable;
	private int mIsbtConcatenationRedundancy;
	
	// Code39
	private boolean mIsCode32Prefix;
	private CodeLength mCode39Length;
	private boolean mIsCode39CheckDigitVerification;
	private boolean mIsTransmitCode39CheckDigit;
	private boolean mIsCode39FullAsciiConversion;
	
	// Code93
	private CodeLength mCode93Length;
	
	// Code11
	private CodeLength mCode11Length;
	private Code11CheckDigitVerification mCode11CheckDigitVerification;
	private boolean mIsTransmitCode11CheckDigit;
	
	//I2of5
	private CodeLength mI2of5Length;
	private I2of5CheckDigitVerification mI2of5CheckDigitVerification;
	private boolean mIsTransmitI2of5CheckDigit;
	private boolean mIsConvertI2of5ToEan13;
	
	//D2of5
	private CodeLength mD2of5Length;
	
	//Codabar
	private CodeLength mCodabarLength;
	private boolean mIsClsiEditing;
	private boolean mIsNotisEditing;
	private CodabarStartStopCharactersDetection mCodarbarStartStopChar;
	
	//MSI
	private CodeLength mMsiLength;
	private MSICheckDigits mMsiCheckDigit;
	private boolean mIsTransmitMsiCheckDigit;
	private MSICheckDigitAlgorithm mMsiCheckDigitAlgorithm;
	
	//Matrix 2of5
	private CodeLength mMatrix2of5Length;
	private boolean mIsMatrix2of5CheckDigit;
	private boolean mIsTransmitMatrix2of5CheckDigit;
	
	
	// GS1 Databar
	private GS1DataBarLimitedSecurityLevel mGs1DatabarLimitedSecurityLevel;
	private boolean mIsConvertGs1DatabarToUpcEan;
	
	// ETC
	private boolean mBiDirectionalRedundancy;
	private LinearSecurityLevel mSecurityLevel;

	
	private Context mContext;
	
	public SSI1DBarcodeOptionDialog() {
		dlgUpcEan = new OptionSSI1DUpcEanDialog();
		dlgCode128 = new OptionSSI1DCode128Dialog();
		dlgCode39 = new OptionSSI1DCode39Dialog();
		dlgCode93 = new OptionCode93Dialog();
		dlgCode11 = new OptionCode11Dialog();
		dlgI2of5 = new OptionSSI1DI2of5Dialog();
		dlgD2of5 = new OptionD2of5Dialog();
		dlgCodabar = new OptionCodabarDialog();
		dlgMsi = new OptionMsiDialog();
		dlgMatrix2of5 = new OptionMatrix2of5Dialog();
		dlgGs1Databar = new OptionGs1DatabarDialog();
		
		mSecurityLevel = LinearSecurityLevel.Level1;
		mBiDirectionalRedundancy = false;
		
		mCode128Length = new CodeLength(dlgCode128.getLength().getLength1(), dlgCode128.getLength().getLength2());
		mCode39Length = new CodeLength(dlgCode39.getLength().getLength1(), dlgCode39.getLength().getLength2());
		mCode93Length = new CodeLength(dlgCode93.getLength().getLength1(), dlgCode93.getLength().getLength2());
		mCode11Length = new CodeLength(dlgCode11.getLength().getLength1(), dlgCode11.getLength().getLength2());
		mI2of5Length = new CodeLength(dlgI2of5.getLength().getLength1(), dlgI2of5.getLength().getLength2());
		mD2of5Length = new CodeLength(dlgD2of5.getLength().getLength1(), dlgD2of5.getLength().getLength2());
		mCodabarLength = new CodeLength(dlgCodabar.getLength().getLength1(), dlgCodabar.getLength().getLength2());
		mMsiLength = new CodeLength(dlgMsi.getLength().getLength1(), dlgMsi.getLength().getLength2());
		mMatrix2of5Length = new CodeLength(dlgMatrix2of5.getLength().getLength1(), dlgMatrix2of5.getLength().getLength2());
	}
	
	// ---------------------------------
	// UPC / EAN
	// ---------------------------------
	public BooklandISBNFormat getBooklandISBNFormat(){
		return dlgUpcEan.getBooklandISBNFormat();
	}
	
	public void setBooklandISBNFormat(BooklandISBNFormat value){
		dlgUpcEan.setBooklandISBNFormat(value);
		mIsbnFormat = value;
	}
	
	public DecodeUpcEanJanSupplementals getDecodeUpcEanJanSupplementals(){
		return dlgUpcEan.getDecodeUpcEanJanSupplementals();
	}
	
	public void setDecodeUpcEanJanSupplementals(DecodeUpcEanJanSupplementals value){
		dlgUpcEan.setDecodeUpcEanJanSupplementals(value);
		mDecodeUpcEanJanSupplementals = value;
	}
	
	public int getUpcEanJanSupplementalRedundancy(){
		return dlgUpcEan.getUpcEanJanSupplementalRedundancy();
	}
	
	public void setUpcEanJanSupplementalRedundancy(int value){
		dlgUpcEan.setUpcEanJanSupplementalRedundancy(value);
		mUpcEanJanSupplementalRedundancy = value;
	}
	
	public boolean getTransmitUpcACheckDigit(){
		return dlgUpcEan.getTransmitUpcACheckDigit();
	}
	
	public void setTransmitUpcACheckDigit(boolean value){
		dlgUpcEan.setTransmitUpcACheckDigit(value);
		mIsTransmitUpcACheckDigit = value;
	}

	public boolean getTransmitUpcECheckDigit(){
		return dlgUpcEan.getTransmitUpcECheckDigit();
	}
	public void setTransmitUpcECheckDigit(boolean value){
		dlgUpcEan.setTransmitUpcECheckDigit(value);
		mIsTransmitUpcECheckDigit = value;
	}

	public boolean getTransmitUpcE1CheckDigit(){
		return dlgUpcEan.getTransmitUpcE1CheckDigit();
	}
	
	public void setTransmitUpcE1CheckDigit(boolean value){
		dlgUpcEan.setTransmitUpcE1CheckDigit(value);
		mIsTransmitUpcE1CheckDigit = value;
	}

	public Preamble getUpcAPreamble() {
		return dlgUpcEan.getUpcAPreamble();
	}
	
	public void setUpcAPreamble(Preamble value){
		dlgUpcEan.setUpcAPreamble(value);
		mUpcAPreamble = value;
	}

	public Preamble getUpcEPreamble() {
		return dlgUpcEan.getUpcEPreamble();
	}

	public void setUpcEPreamble(Preamble value){
		dlgUpcEan.setUpcEPreamble(value);
		mUpcEPreamble = value;
	}

	public Preamble getUpcE1Preamble() {
		return dlgUpcEan.getUpcE1Preamble();
	}

	public void setUpcE1Preamble(Preamble value){
		dlgUpcEan.setUpcE1Preamble(value);
		mUpcE1Preamble = value;
	}

	public boolean getConvertUpcEToUpcA(){
		return dlgUpcEan.getConvertUpcEToUpcA();
	}

	public void setConvertUpcEToUpcA(boolean value){
		dlgUpcEan.setConvertUpcEToUpcA(value);
		mIsConvertUpcEToUpcA = value;
	}
	
	public boolean getConvertUpcE1ToUpcA(){
		return dlgUpcEan.getConvertUpcE1ToUpcA();
	}

	public void setConvertUpcE1ToUpcA(boolean value){
		dlgUpcEan.setConvertUpcE1ToUpcA(value);
		mIsConvertUpcE1ToUpcA = value;
	}
	
	public boolean getEan8Jan8Extend(){
		return dlgUpcEan.getEan8Jan8Extend();
	}

	public void setEan8Jan8Extend(boolean value){
		dlgUpcEan.setEan8Jan8Extend(value);
		mIsEan8Jan8Extend = value;
	}
	
	public UpcEanSecurityLevel getUpcEanSecurityLevel(){
		return dlgUpcEan.getUpcEanSecurityLevel();
	}
	
	public void setUpcEanSecurityLevel(UpcEanSecurityLevel value){
		dlgUpcEan.setUpcEanSecurityLevel(value);
		mUpcEanSecurityLevel = value;
	}
	
	public CouponReport getCouponReport(){
		return dlgUpcEan.getCouponReport();
	}

	public void setCouponReport(CouponReport value){
		dlgUpcEan.setCouponReport(value);
		mCouponReport = value;
	}
	
	// ---------------------------------
	// Code 128
	// ---------------------------------
	public CodeLength getCode128Length(){
		return dlgCode128.getLength();
	}
	
	public void setCode128Length(CodeLength value){
		dlgCode128.setLength(value);
		mCode128Length.setLength(value.getLength1(), value.getLength2());
	}
	
	public ISBTConcatenation getIsbtConcatenation(){
		return dlgCode128.getIsbtConcatenation();
	}
	
	public void setIsbtConcatenation(ISBTConcatenation value){
		dlgCode128.setIsbtConcatenation(value);
		mIsbtConcatenation = value;
	}
	
	public boolean getCheckIsbtTable(){
		return dlgCode128.getCheckIsbtTable();
	}
	
	public void setCheckIsbtTable(boolean value){
		dlgCode128.setCheckIsbtTable(value);
		mIsCheckIsbtTable = value;
	}
	
	public int getIsbtConcatenationRedundancy() {
		return dlgCode128.getIsbtConcatenationRedundancy();
	}
	
	public void setIsbtConcatenationRedundancy(int value){
		dlgCode128.setIsbtConcatenationRedundancy(value);
		mIsbtConcatenationRedundancy = value;
	}
	
	// ---------------------------------
	// Code 39
	// ---------------------------------
	public boolean getCode32Prefix(){
		return dlgCode39.getCode32Prefix();
	}
	
	public void setCode32Prefix(boolean value){
		dlgCode39.setCode32Prefix(value);
		mIsCode32Prefix = value;
	}
	
	public CodeLength getCode39Length(){
		return dlgCode39.getLength();
	}
	
	public void setCode39Length(CodeLength value){
		dlgCode39.setLength(value);
		mCode39Length.setLength(value.getLength1(), value.getLength2());
	}

	public boolean getCode39CheckDigitVerification(){
		return dlgCode39.getCode39CheckDigitVerification();
	}
	
	public void setCode39CheckDigitVerification(boolean value){
		dlgCode39.setCode39CheckDigitVerification(value);
		mIsCode39CheckDigitVerification = value;
	}

	public boolean getTransmitCode39CheckDigit(){
		return dlgCode39.getTransmitCode39CheckDigit();
	}
	
	public void setTransmitCode39CheckDigit(boolean value){
		dlgCode39.setTransmitCode39CheckDigit(value);
		mIsTransmitCode39CheckDigit = value;
	}

	public boolean getCode39FullAsciiConversion(){
		return dlgCode39.getCode39FullAsciiConversion();
	}
	
	public void setCode39FullAsciiConversion(boolean value){
		dlgCode39.setCode39FullAsciiConversion(value);
		mIsCode39FullAsciiConversion = value;
	}

	// ---------------------------------
	// Code 93
	// ---------------------------------
	public CodeLength getCode93Length(){
		return dlgCode93.getLength();
	}
	
	public void setCode93Length(CodeLength value){
		dlgCode93.setLength(value);
		mCode93Length.setLength(value.getLength1(), value.getLength2());
	}
	
	// ---------------------------------
	// Code 11
	// ---------------------------------
	public CodeLength getCode11Length(){
		return dlgCode11.getLength();
	}
	
	public void setCode11Length(CodeLength value) {
		dlgCode11.setLength(value);
		mCode11Length.setLength(value.getLength1(), value.getLength2());
	}
	
	public Code11CheckDigitVerification getCode11CheckDigitVerification(){
		return dlgCode11.getCode11CheckDigitVerification();
	}
	
	public void setCode11CheckDigitVerification(Code11CheckDigitVerification value){
		dlgCode11.setCode11CheckDigitVerification(value);
		mCode11CheckDigitVerification = value;
	}
	
	public boolean getTransmitCode11CheckDigit(){
		return dlgCode11.getTransmitCode11CheckDigit();
	}
	
	public void setTransmitCode11CheckDigit(boolean value){
		dlgCode11.setTransmitCode11CheckDigit(value);
		mIsTransmitCode11CheckDigit = value;
	}
	
	// ---------------------------------
	// I2of5
	// ---------------------------------
	public CodeLength getI2of5Length() {
		return dlgI2of5.getLength();
	}
	
	public void setI2of5Length(CodeLength value){
		dlgI2of5.setLength(value);
		mI2of5Length.setLength(value.getLength1(), value.getLength2());

	}
	
	public I2of5CheckDigitVerification getI2of5CheckDigitVerification(){
		return dlgI2of5.getI2of5CheckDigitVerification();
	}
	
	public void setI2of5CheckDigitVerification(I2of5CheckDigitVerification value){
		dlgI2of5.setI2of5CheckDigitVerification(value);
		mI2of5CheckDigitVerification = value;
	}
	
	public boolean getTransmitI2of5CheckDigit(){
		return dlgI2of5.getTransmitI2of5CheckDigit();
	}
	
	public void setTransmitI2of5CheckDigit(boolean value){
		dlgI2of5.setTransmitI2of5CheckDigit(value);
		mIsTransmitI2of5CheckDigit = value;
	}
	
	public boolean getConvertI2of5ToEan13(){
		return dlgI2of5.getConvertI2of5ToEan13();
	}
	
	public void setConvertI2of5ToEan13(boolean value){
		dlgI2of5.setConvertI2of5ToEan13(value);
		mIsConvertI2of5ToEan13 = value;
	}
	
	// ---------------------------------
	// D2of5
	// ---------------------------------
	public CodeLength getD2of5Length(){
		return dlgD2of5.getLength();
	}
	
	public void setD2of5Length(CodeLength value){
		dlgD2of5.setLength(value);
		mD2of5Length.setLength(value.getLength1(), value.getLength2());
	}
	
	// ---------------------------------
	// Codabar
	// ---------------------------------	
	
	public CodeLength getCodabarLength(){
		return dlgCodabar.getLength();
	}
	
	public void setCodabarLength(CodeLength value) {
		dlgCodabar.setLength(value);
		mCodabarLength.setLength(value.getLength1(), value.getLength2());
	}
	
	public boolean getClsiEditing(){
		return dlgCodabar.getClsiEditing();
	}
	
	public void setClsiEditing(boolean value){
		dlgCodabar.setClsiEditing(value);
		mIsClsiEditing = value;
	}
	
	public boolean getNotisEditing(){
		return dlgCodabar.getNotisEditing();
	}
	
	public void setNotisEditing(boolean value){
		dlgCodabar.setNotisEditing(value);
		mIsNotisEditing = value;
	}
	
	public CodabarStartStopCharactersDetection getCodabarStartStopCharactersDetection() {
		return dlgCodabar.getCodabarStartStopCharactersDetection();
	}
	
	public void setCodabarStartStopCharactersDetection(CodabarStartStopCharactersDetection value){
		dlgCodabar.setCodabarStartStopCharactersDetection(value);
		mCodarbarStartStopChar = value;
	}
	
	// ---------------------------------
	// Msi
	// ---------------------------------
	public CodeLength getMsiLength(){
		return dlgMsi.getLength();
	}
	
	public void setMsiLength(CodeLength value){
		dlgMsi.setLength(value);
		mMsiLength.setLength(value.getLength1(), value.getLength2());
	}
	
	public MSICheckDigits getMSICheckDigit(){
		return dlgMsi.getMSICheckDigit();
	}
	
	public void setMSICheckDigits(MSICheckDigits value){
		dlgMsi.setMSICheckDigits(value);
		mMsiCheckDigit = value;
	}
	
	public boolean getTransmitMsiCheckDigit(){
		return dlgMsi.getTransmitMsiCheckDigit();
	}
	
	public void setTransmitMsiCheckDigit(boolean value){
		dlgMsi.setTransmitMsiCheckDigit(value);
		mIsTransmitMsiCheckDigit = value;
	}
	
	public MSICheckDigitAlgorithm getMSICheckDigitAlgorithm(){
		return dlgMsi.getMSICheckDigitAlgorithm();
	}
	
	public void setMSICheckDigitAlgorithm(MSICheckDigitAlgorithm value){
		dlgMsi.setMSICheckDigitAlgorithm(value);
		mMsiCheckDigitAlgorithm = value;
	}
	
	// ---------------------------------
	// Matrix2of5
	// ---------------------------------
	public CodeLength getMatrix2of5Length(){
		return dlgMatrix2of5.getLength();
	}
	
	public void setMatrix2of5Length(CodeLength value){
		dlgMatrix2of5.setLength(value);
		mMatrix2of5Length.setLength(value.getLength1(), value.getLength2());
	}
	
	public boolean getMatrix2of5CheckDigit(){
		return dlgMatrix2of5.getMatrix2of5CheckDigit();
	}
	
	public void setMatrix2of5CheckDigit(boolean value){
		dlgMatrix2of5.setMatrix2of5CheckDigit(value);
		mIsMatrix2of5CheckDigit = value;
	}
	
	public boolean getTransmitMatrix2of5CheckDigit(){
		return dlgMatrix2of5.getTransmitMatrix2of5CheckDigit();
	}
	
	public void setTransmitMatrix2of5CheckDigit(boolean value){
		dlgMatrix2of5.setTransmitMatrix2of5CheckDigit(value);
		mIsTransmitMatrix2of5CheckDigit = value;
	}	

	// ---------------------------------
	// Gs1 Databar
	// ---------------------------------
	public GS1DataBarLimitedSecurityLevel getGs1DatabarLimitedSecurityLevel(){
		return dlgGs1Databar.getGs1DatabarLimitedSecurityLevel();
	}
	
	public void setGs1DatabarLimitedSecurityLevel(GS1DataBarLimitedSecurityLevel value){
		dlgGs1Databar.setGs1DatabarLimitedSecurityLevel(value);
		mGs1DatabarLimitedSecurityLevel = value;
	}
	
	public boolean getConvertGs1DatabarToUpcEan(){
		return dlgGs1Databar.getConvertGs1DatabarToUpcEan();
	}
	
	public void setConvertGs1DatabarToUpcEan(boolean value){
		dlgGs1Databar.setConvertGs1DatabarToUpcEan(value);
		mIsConvertGs1DatabarToUpcEan = value;
	}
	
	
	// ---------------------------------
	// ETC
	// ---------------------------------
	public boolean getBiDirectionalRedundancy(){
		return mBiDirectionalRedundancy;
	}
	
	public void setBiDirectionalRedundancy(boolean value){
		mBiDirectionalRedundancy = value;
	}
	 
	public LinearSecurityLevel getSecurityLevel(){
		return mSecurityLevel;
	}
	
	public void setSecurityLevel(LinearSecurityLevel value){
		mSecurityLevel = value;
	}

	// ---------------------------------
	// restore parameter ( cancel )
	// ---------------------------------
	public void restoreParameterValue() {
		// UPC / EAN
		dlgUpcEan.setBooklandISBNFormat(mIsbnFormat);
		dlgUpcEan.setDecodeUpcEanJanSupplementals(mDecodeUpcEanJanSupplementals);
		dlgUpcEan.setUpcEanJanSupplementalRedundancy(mUpcEanJanSupplementalRedundancy);
		dlgUpcEan.setTransmitUpcACheckDigit(mIsTransmitUpcACheckDigit);
		dlgUpcEan.setTransmitUpcECheckDigit(mIsTransmitUpcECheckDigit);
		dlgUpcEan.setTransmitUpcE1CheckDigit(mIsTransmitUpcE1CheckDigit);
		dlgUpcEan.setUpcAPreamble(mUpcAPreamble);
		dlgUpcEan.setUpcEPreamble(mUpcEPreamble);
		dlgUpcEan.setUpcE1Preamble(mUpcE1Preamble);
		dlgUpcEan.setConvertUpcEToUpcA(mIsConvertUpcEToUpcA);
		dlgUpcEan.setConvertUpcE1ToUpcA(mIsConvertUpcE1ToUpcA);
		dlgUpcEan.setEan8Jan8Extend(mIsEan8Jan8Extend);
		dlgUpcEan.setUpcEanSecurityLevel(mUpcEanSecurityLevel);
		dlgUpcEan.setCouponReport(mCouponReport);
		
		// Code 128
		dlgCode128.setLength(mCode128Length);
		dlgCode128.setIsbtConcatenation(mIsbtConcatenation);
		dlgCode128.setCheckIsbtTable(mIsCheckIsbtTable);
		dlgCode128.setIsbtConcatenationRedundancy(mIsbtConcatenationRedundancy);
		
		// Code 39
		dlgCode39.setCode32Prefix(mIsCode32Prefix);
		dlgCode39.setLength(mCode39Length);
		dlgCode39.setCode39CheckDigitVerification(mIsCode39CheckDigitVerification);
		dlgCode39.setTransmitCode39CheckDigit(mIsTransmitCode39CheckDigit);
		dlgCode39.setCode39FullAsciiConversion(mIsCode39FullAsciiConversion);
		
		// Code 93
		dlgCode93.setLength(mCode93Length);
		
		// Code 11
		dlgCode11.setLength(mCode11Length);
		dlgCode11.setCode11CheckDigitVerification(mCode11CheckDigitVerification);
		dlgCode11.setTransmitCode11CheckDigit(mIsTransmitCode11CheckDigit);
		
		// I2of5
		dlgI2of5.setLength(mI2of5Length);
		dlgI2of5.setI2of5CheckDigitVerification(mI2of5CheckDigitVerification);
		dlgI2of5.setTransmitI2of5CheckDigit(mIsTransmitI2of5CheckDigit);
		dlgI2of5.setConvertI2of5ToEan13(mIsConvertI2of5ToEan13);
	
		// D2of5
		dlgD2of5.setLength(mD2of5Length);
		
		// Codabar
		dlgCodabar.setLength(mCodabarLength);
		dlgCodabar.setClsiEditing(mIsClsiEditing);
		dlgCodabar.setNotisEditing(mIsNotisEditing);
		dlgCodabar.setCodabarStartStopCharactersDetection(mCodarbarStartStopChar);
		
		// Msi
		dlgMsi.setLength(mMsiLength);
		dlgMsi.setMSICheckDigits(mMsiCheckDigit);
		dlgMsi.setTransmitMsiCheckDigit(mIsTransmitMsiCheckDigit);
		dlgMsi.setMSICheckDigitAlgorithm(mMsiCheckDigitAlgorithm);
		
		// Matrix2of5
		dlgMatrix2of5.setLength(mMatrix2of5Length);
		dlgMatrix2of5.setMatrix2of5CheckDigit(mIsMatrix2of5CheckDigit);
		dlgMatrix2of5.setTransmitMatrix2of5CheckDigit(mIsTransmitMatrix2of5CheckDigit);
		
		// Gs1 Databar
		dlgGs1Databar.setGs1DatabarLimitedSecurityLevel(mGs1DatabarLimitedSecurityLevel);
		dlgGs1Databar.setConvertGs1DatabarToUpcEan(mIsConvertGs1DatabarToUpcEan);
		
		ATLog.i(TAG, INFO, "INFO. restoreParameterValue()");
	}
	
	public void showDialog(Context context, final IValueChangedListener changedListener,
			final ICancelListener cancelListener) {
	
		mContext = context;
		
		LinearLayout root = (LinearLayout) LinearLayout.inflate(context, R.layout.dialog_barcode_option_ssi_1d, null);
		
		linearOptionUpcEan = (LinearLayout) root.findViewById(R.id.linear_barcode_option_upc_ean);
		linearOptionUpcEan.setOnClickListener(this);
		linearOptionCode128 = (LinearLayout) root.findViewById(R.id.linear_barcode_option_code128);
		linearOptionCode128.setOnClickListener(this);
		linearOptionCode39 = (LinearLayout) root.findViewById(R.id.linear_barcode_option_code39);
		linearOptionCode39.setOnClickListener(this);
		linearOptionCode93 = (LinearLayout) root.findViewById(R.id.linear_barcode_option_code93);
		linearOptionCode93.setOnClickListener(this);
		linearOptionCode11 = (LinearLayout) root.findViewById(R.id.linear_barcode_option_code11);
		linearOptionCode11.setOnClickListener(this);
		linearOptionI2of5 = (LinearLayout) root.findViewById(R.id.linear_barcode_option_i2of5);
		linearOptionI2of5.setOnClickListener(this);
		linearOptionD2of5 = (LinearLayout) root.findViewById(R.id.linear_barcode_option_d2of5);
		linearOptionD2of5.setOnClickListener(this);
		linearOptionCodabar = (LinearLayout) root.findViewById(R.id.linear_barcode_option_codabar);
		linearOptionCodabar.setOnClickListener(this);
		linearOptionMsi = (LinearLayout) root.findViewById(R.id.linear_barcode_option_msi);
		linearOptionMsi.setOnClickListener(this);
		linearOptionMatrix2of5 = (LinearLayout) root.findViewById(R.id.linear_barcode_option_matrix2of5);
		linearOptionMatrix2of5.setOnClickListener(this);
		linearOptionGs1Databar = (LinearLayout) root.findViewById(R.id.linear_barcode_option_gs1_databar);
		linearOptionGs1Databar.setOnClickListener(this);
		linearBiDirectionalOptionRedundancy = (LinearLayout) root.findViewById(R.id.linear_barcode_option_bi_directional_redundancy);
		linearBiDirectionalOptionRedundancy.setOnClickListener(this);
		linearOptionSecurityLevel = (LinearLayout) root.findViewById(R.id.linear_barcode_option_security_level);
		linearOptionSecurityLevel.setOnClickListener(this);
		
		chkBiDirectionalRedundancy = (CheckBox) root.findViewById(R.id.bi_directional_redundancy);
		txtSecurityLevel = (TextView) root.findViewById(R.id.security_level);
	
		dlgSecurityLevel = new EnumListDialog(txtSecurityLevel, LinearSecurityLevel.values());
		dlgSecurityLevel.setValue(mSecurityLevel);

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(R.string.symbol_barcode_option);
		builder.setView(root);
		builder.setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
			
				mBiDirectionalRedundancy = chkBiDirectionalRedundancy.isChecked();
				mSecurityLevel = (LinearSecurityLevel) dlgSecurityLevel.getValue();
				
				if(changedListener != null){
					changedListener.onValueChanged(SSI1DBarcodeOptionDialog.this);
				}
				ATLog.i(TAG, INFO, "INFO. showDialog().$PositiveButton.onClick()");
			}
		});
		
		builder.setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				if(cancelListener != null){
					cancelListener.onCanceled(null);
				}
				ATLog.i(TAG, INFO, "INFO. showDialog().$NegativeButton.onClick()");
			}
		});
		
		builder.setCancelable(true);
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				
				if(cancelListener != null){
					cancelListener.onCanceled(null);
				}
				ATLog.i(TAG, INFO, "INFO. showDialog().onCancel()");
			}
		});
		
		AlertDialog dialog = builder.create();
		dialog.setOnShowListener(new DialogInterface.OnShowListener() {
			
			@Override
			public void onShow(DialogInterface dialog) {
				
				dlgSecurityLevel.display();
				chkBiDirectionalRedundancy.setChecked(mBiDirectionalRedundancy); 
						
				ATLog.i(TAG, INFO, "INFO. showDialog().onShow()");
			}
		});
		dialog.show();
		
		ATLog.i(TAG, INFO, "INFO. showDialog()");
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.linear_barcode_option_upc_ean:
				dlgUpcEan.showDialog(mContext, R.string.upc_ean);
			break;
			case R.id.linear_barcode_option_code128:
				dlgCode128.showDialog(mContext, R.string.code128);
			break;
			case R.id.linear_barcode_option_code39:
				dlgCode39.showDialog(mContext, R.string.code39);
			break;
			case R.id.linear_barcode_option_code93:
				dlgCode93.showDialog(mContext, R.string.code93);
			break;
			case R.id.linear_barcode_option_code11:
				dlgCode11.showDialog(mContext, R.string.code11);
			break;
			case R.id.linear_barcode_option_i2of5:
				dlgI2of5.showDialog(mContext, R.string.i2of5);
			break;
			case R.id.linear_barcode_option_d2of5:
				dlgD2of5.showDialog(mContext, R.string.d2of5);
			break;
			case R.id.linear_barcode_option_codabar:
				dlgCodabar.showDialog(mContext, R.string.codabar);
			break;
			case R.id.linear_barcode_option_msi:
				dlgMsi.showDialog(mContext, R.string.msi);
			break;
			case R.id.linear_barcode_option_matrix2of5:
				dlgMatrix2of5.showDialog(mContext, R.string.matrix2of5);
			break;
			case R.id.linear_barcode_option_gs1_databar:
				dlgGs1Databar.showDialog(mContext, R.string.gs1_databar);
			break;
			case R.id.linear_barcode_option_bi_directional_redundancy:
				
			break;
			case R.id.linear_barcode_option_security_level:
				dlgSecurityLevel.showDialog(mContext, R.string.security_level);
			break;
		}
	
	}

	// ------------------------------------------------------------------------
	// Declare Interface IValueChangedListener
	// ------------------------------------------------------------------------
	public interface IValueChangedListener {
		void onValueChanged(SSI1DBarcodeOptionDialog dialog);
	}
	
}
