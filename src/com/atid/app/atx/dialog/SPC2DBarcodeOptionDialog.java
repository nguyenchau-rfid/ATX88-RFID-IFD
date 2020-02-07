package com.atid.app.atx.dialog;

import com.atid.app.atx.R;
import com.atid.app.atx.dialog.BaseDialog.ICancelListener;
import com.atid.app.atx.dialog.spc.OptionAztecCode;
import com.atid.app.atx.dialog.spc.OptionCodabar;
import com.atid.app.atx.dialog.spc.OptionCodablockA;
import com.atid.app.atx.dialog.spc.OptionCodablockF;
import com.atid.app.atx.dialog.spc.OptionCode11;
import com.atid.app.atx.dialog.spc.OptionCode128;
import com.atid.app.atx.dialog.spc.OptionCode39;
import com.atid.app.atx.dialog.spc.OptionCode93;
import com.atid.app.atx.dialog.spc.OptionDataMatrix;
import com.atid.app.atx.dialog.spc.OptionEanJan13;
import com.atid.app.atx.dialog.spc.OptionEanJan8;
import com.atid.app.atx.dialog.spc.OptionGS1CompositeCodes;
import com.atid.app.atx.dialog.spc.OptionGs1128;
import com.atid.app.atx.dialog.spc.OptionGs1Databar;
import com.atid.app.atx.dialog.spc.OptionHanXinCode;
import com.atid.app.atx.dialog.spc.OptionI2of5;
import com.atid.app.atx.dialog.spc.OptionMatrix2of5;
import com.atid.app.atx.dialog.spc.OptionMaxiCode;
import com.atid.app.atx.dialog.spc.OptionMicroPDF417;
import com.atid.app.atx.dialog.spc.OptionMsi;
import com.atid.app.atx.dialog.spc.OptionNEC2of5;
import com.atid.app.atx.dialog.spc.OptionPDF417;
import com.atid.app.atx.dialog.spc.OptionPostalCodes2D;
import com.atid.app.atx.dialog.spc.OptionPostalCodesLinear;
import com.atid.app.atx.dialog.spc.OptionQRCode;
import com.atid.app.atx.dialog.spc.OptionStraight2of5IATA;
import com.atid.app.atx.dialog.spc.OptionStraight2of5Industrial;
import com.atid.app.atx.dialog.spc.OptionUpcA;
import com.atid.app.atx.dialog.spc.OptionUpcE0;
import com.atid.lib.module.barcode.spc.type.AustralianPostInterpretation;
import com.atid.lib.module.barcode.spc.type.CheckDigit;
import com.atid.lib.module.barcode.spc.type.CodabarCheckCharacter;
import com.atid.lib.module.barcode.spc.type.CodabarConcatenation;
import com.atid.lib.module.barcode.spc.type.Code11CheckDigitsRequired;
import com.atid.lib.module.barcode.spc.type.Code39CheckCharacter;
import com.atid.lib.module.barcode.spc.type.CodePages;
import com.atid.lib.module.barcode.spc.type.GS1Emulation;
import com.atid.lib.module.barcode.spc.type.MSICheckCharacter;
import com.atid.lib.module.barcode.spc.type.UPCAEAN13ExtendedCouponCode;
import com.atid.lib.module.barcode.spc.type.VideoReverse;

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

public class SPC2DBarcodeOptionDialog implements OnClickListener, OnCheckedChangeListener {
	private static final String TAG = SPC2DBarcodeOptionDialog.class.getSimpleName();
	private static final int INFO = ATLog.L2;
	
	private LinearLayout linearCodabar;
	private LinearLayout linearCode39;
	private LinearLayout linearI2of5;
	private LinearLayout linearCode93;
	private LinearLayout linearNEC2of5;
	private LinearLayout linearStraight2of5Industrial;
	private LinearLayout linearStraight2of5IATA;
	private LinearLayout linearMatrix2of5;
	private LinearLayout linearCode11 ;
	private LinearLayout linearCode128;
	private LinearLayout linearGS1128;
	private LinearLayout linearUpcA;
	private LinearLayout linearUpcE0;
	private LinearLayout linearEanJan13;
	private LinearLayout linearEanJan8 ;
	private LinearLayout linearMsi;
	private LinearLayout linearGS1DataBar;
	private LinearLayout linearCodablockA;
	private LinearLayout linearCodablockF;
	private LinearLayout linearPDF417;
	private LinearLayout linearMicroPDF417;
	private LinearLayout linearGS1CompositeCodes;
	private LinearLayout linearQRCode ;
	private LinearLayout linearDataMatrix;
	private LinearLayout linearMaxiCode;
	private LinearLayout linearAztecCode ;
	private LinearLayout linearHanXinCode;
	private LinearLayout linearPostalCodes2D;
	private LinearLayout linearPostalCodesLinear;
	
	private LinearLayout linearUPCAEAN13ExtendedCouponCode;
	private LinearLayout linearCouponGS1DataBarOutput;
	private LinearLayout linearLabelCode;
	private LinearLayout linearGS1Emulation;	
	private LinearLayout linearVideoReverse;
	
	private TextView txtUpcAEan13ExtendedCouponCode;
	private CheckBox chkCouponGS1DataBarOutput;
//	private CheckBox chkLabelCode;
	private TextView txtGS1Emulation;	
	private TextView txtVideoReverse;
	
	private OptionCodabar dlgCodabar;
	private OptionCode39 dlgCode39;
	private OptionI2of5 dlgI2of5;
	private OptionCode93 dlgCode93;
	private OptionNEC2of5 dlgNEC2of5;
	private OptionStraight2of5Industrial dlgStraight2of5Industrial;
	private OptionStraight2of5IATA dlgStraight2of5IATA;
	private OptionMatrix2of5 dlgMatrix2of5;
	private OptionCode11 dlgCode11 ;
	private OptionCode128 dlgCode128;
	private OptionGs1128 dlgGS1128;
	private OptionUpcA dlgUpcA;
	private OptionUpcE0 dlgUpcE0;
	private OptionEanJan13 dlgEanJan13;
	private OptionEanJan8 dlgEanJan8 ;
	private OptionMsi dlgMsi;
	private OptionGs1Databar dlgGS1DataBar;
	private OptionCodablockA dlgCodablockA;
	private OptionCodablockF dlgCodablockF;
	private OptionPDF417 dlgPDF417;
	private OptionMicroPDF417 dlgMicroPDF417;
	private OptionGS1CompositeCodes dlgGS1CompositeCodes;
	private OptionQRCode dlgQRCode ;
	private OptionDataMatrix dlgDataMatrix;
	private OptionMaxiCode dlgMaxiCode;
	private OptionAztecCode dlgAztecCode ;
	private OptionHanXinCode dlgHanXinCode;
	private OptionPostalCodes2D dlgPostalCodes2D;
	private OptionPostalCodesLinear dlgPostalCodesLinear;
	
	private EnumListDialog dlgUpcAEan13ExtendedCouponCode;  
	private EnumListDialog dlgGS1Emulation;                 
	private EnumListDialog dlgVideoReverse;
	
	// ------------------------------------------------------
	// Loading value
	// ------------------------------------------------------
	
	// Codabar
	private boolean mIsCodabarStartStopCharacters;
	private CodabarCheckCharacter mCodabarCheckCharacter;
	private CodabarConcatenation mCodabarConcatenation;
	private int mCodabarLengthMin;
	private int mCodabarLengthMax;
	
	// Code39
	private boolean mIsCode39StartStopCharacters;
	private Code39CheckCharacter mCode39CheckCharacter;
	private int mCode39LengthMin;
	private int mCode39LengthMax;
	private boolean mIsCode39Append;
	private boolean mIsCode39FullASCII;
	private CodePages mCode39CodePage;
	
	// I2of5
	private CheckDigit mI2of5CheckDigit;
	private int mI2of5LengthMin;
	private int mI2of5LengthMax;
	
	// Code93
	private int mCode93LengthMin;
	private int mCode93LengthMax;
	private boolean mIsCode93Append;
	private CodePages mCode93CodePage;
	
	// NEC2of5
	private CheckDigit mNEC2of5CheckDigit;
	private int mNEC2of5LengthMin;
	private int mNEC2of5LengthMax;
	
	// Straight2of5Industrial
	private int mStraight2of5IndustrialLengthMin;
	private int mStraight2of5IndustrialLengthMax;
	
	// Straight2of5IATA
	private int mStraight2of5IATALengthMin;
	private int mStraight2of5IATALengthMax;
	
	// Matrix2of5
	private int mMatrix2of5LengthMin;
	private int mMatrix2of5LengthMax;
	
	// Code11
	private Code11CheckDigitsRequired mCode11CheckDigitsRequired;
	private int mCode11LengthMin;
	private int mCode11LengthMax;
	
	// Code128
	private boolean mIsISBT128Concatenation;
	private int mCode128LengthMin;
	private int mCode128LengthMax;
	private boolean mIsCode128Append;
	private CodePages mCode128CodePage;
	
	// GS1128
	private int mGS1128LengthMin;
	private int mGS1128LengthMax;
	
	// UpcA
	private boolean mIsUPCACheckDigit;
	private boolean mIsUPCANumberSystem;
	private boolean mIsUPCAAddenda2Digit;
	private boolean mIsUPCAAddenda5Digit;
	private boolean mIsUPCAAddendaRequired;
//	private int mUpcAAddendaTimeout;
	private boolean mIsUPCAAddendaSeparator;
	
	// UpcE0
	private boolean mIsUPCE0Expand;
	private boolean mIsUPCE0AddendaRequired;
	private boolean mIsUPCE0AddendaSeparator;
	private boolean mIsUPCE0CheckDigit;
	private boolean mIsUPCE0LeadingZero;
	private boolean mIsUPCE0Addenda2Digit;
	private boolean mIsUPCE0Addenda5Digit;
	
	// EanJan13
	private boolean mIsConvertUPCAtoEAN13;
	private boolean mIsEANJAN13CheckDigit;
	private boolean mIsEANJAN13Addenda2Digit;
	private boolean mIsEANJAN13Addenda5Digit;
	private boolean mIsEANJAN13AddendaRequired;
	private boolean mIsEANJAN13AddendaSeparator;
	private boolean mIsISBNTranslate;
	
	// EanJan8
	private boolean mIsEANJAN8CheckDigit;
	private boolean mIsEANJAN8Addenda2Digit;
	private boolean mIsEANJAN8Addenda5Digit;
	private boolean mIsEANJAN8AddendaRequired;
	private boolean mIsEANJAN8AddendaSeparator;
	
	// MSI
	private MSICheckCharacter mMSICheckCharacter;
	private int mMSILengthMin;
	private int mMSILengthMax;
	
	// GS1DataBar
	private int mGS1DataBarLengthMin;
	private int mGS1DataBarLengthMax;
	
	// CodablockA
	private int mCodablockALengthMin;
	private int mCodablockALengthMax;
	
	// CodablockF
	private int mCodablockFLengthMin;
	private int mCodablockFLengthMax;
	
	// PDF417
	private int mPDF417LengthMin;
	private int mPDF417LengthMax;
	
	// MicroPDF417
	private int mMicroPDF417LengthMin;
	private int mMicroPDF417LengthMax;
	
	// GS1CompositeCodes
	private boolean mIsGS1CompositeCodes;
	private boolean mIsUPCEANVersion;
	private int mGS1CompositeCodesLengthMin;
	private int mGS1CompositeCodesLengthMax;
	
	// QRCode
	private int mQRCodeLengthMin;
	private int mQRCodeLengthMax;
	private boolean mIsQRCodeAppend;
	private CodePages mQRCodePage;
	
	// DataMatrix
	private int mDataMatrixLengthMin;
	private int mDataMatrixLengthMax;
//	private boolean mIsDataMatrixAppend;
	private CodePages mDataMatrixCodePage;
	
	// MaxiCode
	private int mMaxiCodeLengthMin;
	private int mMaxiCodeLengthMax;
	
	// AztecCode
	private int mAztecCodeLengthMin;
	private int mAztecCodeLengthMax;
	private boolean mIsAztecAppend;
	private CodePages mAztecCodePage;
	
	// HanXinCode
	private int mHanXinCodeLengthMin;
	private int mHanXinCodeLengthMax;
	
	// PostalCodes2D
	private boolean mIsPlanetCodeCheckDigit;
	private boolean mIsPostnetCheckDigit;
	private AustralianPostInterpretation mAustralianPostInterpretation;
	
	// PostalCodesLinear
	private int mChinaPostLengthMin;
	private int mChinaPostLengthMax;
	private int mKoreaPostLengthMin;
	private int mKoreaPostLengthMax;
	private boolean mIsKoreaPostCheckDigit;
	
	// ETC
	private UPCAEAN13ExtendedCouponCode mUPCAEAN13ExtendedCouponCode;
	private boolean mIsCouponGS1DataBarOutput;
//	private boolean mIsLabelCode;           
	private GS1Emulation mGS1Emulation;
	private VideoReverse mVideoReverse;
	
	private Context mContext;
	
	public SPC2DBarcodeOptionDialog() {
		
		dlgCodabar = new OptionCodabar() ;
		dlgCode39 = new OptionCode39() ;
		dlgI2of5 = new OptionI2of5() ;
		dlgCode93 = new OptionCode93() ;
		dlgNEC2of5 = new OptionNEC2of5() ;
		dlgStraight2of5Industrial = new OptionStraight2of5Industrial() ;
		dlgStraight2of5IATA = new OptionStraight2of5IATA() ;
		dlgMatrix2of5 = new OptionMatrix2of5() ;
		dlgCode11 =  new OptionCode11() ;
		dlgCode128 = new OptionCode128() ;
		dlgGS1128 = new OptionGs1128() ;
		dlgUpcA = new OptionUpcA() ;
		dlgUpcE0 = new OptionUpcE0() ;
		dlgEanJan13 = new OptionEanJan13() ;
		dlgEanJan8 =  new OptionEanJan8() ;
		dlgMsi = new OptionMsi() ;
		dlgGS1DataBar = new OptionGs1Databar() ;
		dlgCodablockA = new OptionCodablockA() ;
		dlgCodablockF = new OptionCodablockF() ;
		dlgPDF417 = new OptionPDF417() ;
		dlgMicroPDF417 = new OptionMicroPDF417() ;
		dlgGS1CompositeCodes = new OptionGS1CompositeCodes() ;
		dlgQRCode =  new OptionQRCode() ;
		dlgDataMatrix = new OptionDataMatrix() ;
		dlgMaxiCode = new OptionMaxiCode() ;
		dlgAztecCode =  new OptionAztecCode() ;
		dlgHanXinCode = new OptionHanXinCode() ;
		dlgPostalCodes2D = new OptionPostalCodes2D() ;
		dlgPostalCodesLinear = new OptionPostalCodesLinear() ;
		
		mUPCAEAN13ExtendedCouponCode = UPCAEAN13ExtendedCouponCode.Off;
		mIsCouponGS1DataBarOutput = false;
//		mIsLabelCode = false;
		mGS1Emulation = GS1Emulation.GS1EmulationOff;
		mVideoReverse = VideoReverse.ReverseOff;
		
		mContext = null;
	}
	
	// ---------------------------------
	// Codabar
	// ---------------------------------
	public boolean getCodabarStartStopCharacters() {
		return dlgCodabar.getCodabarStartStopCharacters();
	}

	public void setCodabarStartStopCharacters(boolean value){
		dlgCodabar.setCodabarStartStopCharacters(value);
		mIsCodabarStartStopCharacters = value;
	}
	
	public CodabarCheckCharacter getCodabarCheckCharacter() {
		return dlgCodabar.getCodabarCheckCharacter();
	}

	public void setCodabarCheckCharacter(CodabarCheckCharacter value){
		dlgCodabar.setCodabarCheckCharacter(value);
		mCodabarCheckCharacter = value;
	}
	
	public CodabarConcatenation getCodabarConcatenation() {
		return dlgCodabar.getCodabarConcatenation();
	}

	public void setCodabarConcatenation(CodabarConcatenation value){
		dlgCodabar.setCodabarConcatenation(value);
		mCodabarConcatenation = value;
	}
	
	public int getCodabarLengthMin() {
		return dlgCodabar.getLengthMin();
	}

	public void setCodabarLengthMin(int value){
		dlgCodabar.setLengthMin(value);
		mCodabarLengthMin = value;
	}
	
	public int getCodabarLengthMax() {
		return dlgCodabar.getLengthMax();
	}

	public void setCodabarLengthMax(int value){
		dlgCodabar.setLengthMax(value);
		mCodabarLengthMax = value;
	}
	// ---------------------------------
	// Code39
	// ---------------------------------
	public boolean getCode39StartStopCharacters() {
		return dlgCode39.getCode39StartStopCharacters();
	}

	public void setCode39StartStopCharacters(boolean value){
		dlgCode39.setCode39StartStopCharacters(value);
		mIsCode39StartStopCharacters = value;
	}
	
	public Code39CheckCharacter getCode39CheckCharacter() {
		return dlgCode39.getCode39CheckCharacter();
	}

	public void setCode39CheckCharacter(Code39CheckCharacter value){
		dlgCode39.setCode39CheckCharacter(value);
		mCode39CheckCharacter = value;
	}
	
	public int getCode39LengthMin() {
		return dlgCode39.getLengthMin();
	}

	public void setCode39LengthMin(int value){
		dlgCode39.setLengthMin(value);
		mCode39LengthMin = value;
	}
	
	public int getCode39LengthMax() {
		return dlgCode39.getLengthMax();
	}

	public void setCode39LengthMax(int value){
		dlgCode39.setLengthMax(value);
		mCode39LengthMax = value;
	}
	
	public boolean getCode39Append() {
		return dlgCode39.getCode39Append();
	}

	public void setCode39Append(boolean value){
		dlgCode39.setCode39Append(value);
		mIsCode39Append = value;
	}
	
	public boolean getCode39FullASCII() {
		return dlgCode39.getCode39FullASCII();
	}

	public void setCode39FullASCII(boolean value){
		dlgCode39.setCode39FullASCII(value);
		mIsCode39FullASCII = value;
	}
	
	public CodePages getCode39CodePage() {
		return dlgCode39.getCode39CodePage();
	}

	public void setCode39CodePage(CodePages value){
		dlgCode39.setCode39CodePage(value);
		mCode39CodePage = value;
	}
	
	// ---------------------------------
	// I2of5
	// ---------------------------------
	
	public CheckDigit getI2of5CheckDigit() {
		return dlgI2of5.getI2of5CheckDigit();
	}

	public void setI2of5CheckDigit(CheckDigit value){
		dlgI2of5.setI2of5CheckDigit(value);
		mI2of5CheckDigit = value;
	}
	
	public int getI2of5LengthMin() {
		return dlgI2of5.getLengthMin();
	}

	public void setI2of5LengthMin(int value){
		dlgI2of5.setLengthMin(value);
		mI2of5LengthMin = value;
	}
	
	public int getI2of5LengthMax() {
		return dlgI2of5.getLengthMax();
	}

	public void setI2of5LengthMax(int value){
		dlgI2of5.setLengthMax(value);
		mI2of5LengthMax = value;
	}
	
	// ---------------------------------
	// Code93
	// ---------------------------------

	public int getCode93LengthMin() {
		return dlgCode93.getLengthMin();
	}

	public void setCode93LengthMin(int value){
		dlgCode93.setLengthMin(value);
		mCode93LengthMin = value;
	}
	
	public int getCode93LengthMax() {
		return dlgCode93.getLengthMax();
	}

	public void setCode93LengthMax(int value){
		dlgCode93.setLengthMax(value);
		mCode93LengthMax = value;
	}
	
	public boolean getCode93Append() {
		return dlgCode93.getCode93Append();
	}

	public void setCode93Append(boolean value){
		dlgCode93.setCode93Append(value);
		mIsCode93Append = value;
	}
	
	public CodePages getCode93CodePage() {
		return dlgCode93.getCode93CodePage();
	}

	public void setCode93CodePage(CodePages value){
		dlgCode93.setCode93CodePage(value);
		mCode93CodePage = value;
	}

	// ---------------------------------
	// NEC2of5
	// ---------------------------------
	
	public CheckDigit getNEC2of5CheckDigit() {
		return dlgNEC2of5.getNEC2of5CheckDigit();
	}

	public void setNEC2of5CheckDigit(CheckDigit value){
		dlgNEC2of5.setNEC2of5CheckDigit(value);
		mNEC2of5CheckDigit = value;
	}
	
	public int getNEC2of5LengthMin() {
		return dlgNEC2of5.getLengthMin();
	}

	public void seNEC2of5tLengthMin(int value){
		dlgNEC2of5.setLengthMin(value);
		mNEC2of5LengthMin = value;
	}
	
	public int getNEC2of5LengthMax() {
		return dlgNEC2of5.getLengthMax();
	}

	public void setNEC2of5LengthMax(int value){
		dlgNEC2of5.setLengthMax(value);
		mNEC2of5LengthMax = value;
	}
	
	// ---------------------------------
	// Straight2of5Industrial
	// ---------------------------------
	
	public int getStraight2of5IndustrialLengthMin() {
		return dlgStraight2of5Industrial.getLengthMin();
	}

	public void setStraight2of5IndustrialLengthMin(int value){
		dlgStraight2of5Industrial.setLengthMin(value);
		mStraight2of5IndustrialLengthMin = value;
	}
	
	public int getStraight2of5IndustrialLengthMax() {
		return dlgStraight2of5Industrial.getLengthMax();
	}

	public void setStraight2of5IndustrialLengthMax(int value){
		dlgStraight2of5Industrial.setLengthMax(value);
		mStraight2of5IndustrialLengthMax = value;
	}
	
	// ---------------------------------
	// Straight2of5IATA
	// ---------------------------------
	
	public int getStraight2of5IATALengthMin() {
		return dlgStraight2of5IATA.getLengthMin();
	}

	public void setStraight2of5IATALengthMin(int value){
		dlgStraight2of5IATA.setLengthMin(value);
		mStraight2of5IATALengthMin = value;
	}
	
	public int getStraight2of5IATALengthMax() {
		return dlgStraight2of5IATA.getLengthMax();
	}

	public void setStraight2of5IATALengthMax(int value){
		dlgStraight2of5IATA.setLengthMax(value);
		mStraight2of5IATALengthMax = value;
	}
	
	// ---------------------------------
	// Matrix2of5
	// ---------------------------------
	
	public int getMatrix2of5LengthMin() {
		return dlgMatrix2of5.getLengthMin();
	}

	public void setMatrix2of5LengthMin(int value){
		dlgMatrix2of5.setLengthMin(value);
		mMatrix2of5LengthMin = value;
	}
	
	public int getMatrix2of5LengthMax() {
		return dlgMatrix2of5.getLengthMax();
	}

	public void setMatrix2of5LengthMax(int value){
		dlgMatrix2of5.setLengthMax(value);
		mMatrix2of5LengthMax = value;
	}
	
	// ---------------------------------
	// Code11
	// ---------------------------------
	
	public Code11CheckDigitsRequired getCode11CheckDigitsRequired() {
		return dlgCode11.getCode11CheckDigitsRequired();
	}

	public void setCode11CheckDigitsRequired(Code11CheckDigitsRequired value){
		dlgCode11.setCode11CheckDigitsRequired(value);
		mCode11CheckDigitsRequired = value;
	}
	
	public int getCode11LengthMin() {
		return dlgCode11.getLengthMin();
	}

	public void setCode11LengthMin(int value){
		dlgCode11.setLengthMin(value);
		mCode11LengthMin = value;
	}
	
	public int getCode11LengthMax() {
		return dlgCode11.getLengthMax();
	}

	public void setCode11LengthMax(int value){
		dlgCode11.setLengthMax(value);
		mCode11LengthMax = value;
	}
		
	// ---------------------------------
	// Code128
	// ---------------------------------
	
	public boolean getISBT128Concatenation() {
		return dlgCode128.getISBT128Concatenation();
	}

	public void setISBT128Concatenation(boolean value){
		dlgCode128.setISBT128Concatenation(value);
		mIsISBT128Concatenation = value;
	}
	
	public int getCode128LengthMin() {
		return dlgCode128.getLengthMin();
	}

	public void setCode128LengthMin(int value){
		dlgCode128.setLengthMin(value);
		mCode128LengthMin = value;
	}
	
	public int getCode128LengthMax() {
		return dlgCode128.getLengthMax();
	}

	public void setCode128LengthMax(int value){
		dlgCode128.setLengthMax(value);
		mCode128LengthMax = value;
	}
	
	public boolean getCode128Append() {
		return dlgCode128.getCode128Append();
	}

	public void setCode128Append(boolean value){
		dlgCode128.setCode128Append(value);
		mIsCode128Append = value;
	}
	
	public CodePages getCode128CodePage() {
		return dlgCode128.getCode128CodePage();
	}

	public void setCode128CodePage(CodePages value){
		dlgCode128.setCode128CodePage(value);
		mCode128CodePage = value;
	}
	
	// ---------------------------------
	// GS1128
	// ---------------------------------
	
	public int getGS1128LengthMin() {
		return dlgGS1128.getLengthMin();
	}

	public void setGS1128LengthMin(int value){
		dlgGS1128.setLengthMin(value);
		mGS1128LengthMin = value;
	}
	
	public int getGS1128LengthMax() {
		return dlgGS1128.getLengthMax();
	}

	public void setGS1128LengthMax(int value){
		dlgGS1128.setLengthMax(value);
		mGS1128LengthMax = value;
	}
	
	// ---------------------------------
	// UpcA
	// ---------------------------------
	
	public boolean getUPCACheckDigit() {
		return dlgUpcA.getUPCACheckDigit();
	}
	
	public void setUPCACheckDigit(boolean value){
		dlgUpcA.setUPCACheckDigit(value);
		mIsUPCACheckDigit = value;
	}
	
	public boolean getUPCANumberSystem() {
		return dlgUpcA.getUPCANumberSystem();
	}
	
	public void setUPCANumberSystem(boolean value){
		dlgUpcA.setUPCANumberSystem(value);
		mIsUPCANumberSystem = value;
	}
	
	public boolean getUPCAAddenda2Digit() {
		return dlgUpcA.getUPCAAddenda2Digit();
	}
	
	public void setUPCAAddenda2Digit(boolean value){
		dlgUpcA.setUPCAAddenda2Digit(value);
		mIsUPCAAddenda2Digit = value;
	}
	public boolean getUPCAAddenda5Digit() {
		return dlgUpcA.getUPCAAddenda5Digit();
	}
	
	public void setUPCAAddenda5Digit(boolean value){
		dlgUpcA.setUPCAAddenda5Digit(value);
		mIsUPCAAddenda5Digit = value;
	}
	public boolean getUPCAAddendaRequired() {
		return dlgUpcA.getUPCAAddendaRequired();
	}
	
	public void setUPCAAddendaRequired(boolean value){
		dlgUpcA.setUPCAAddendaRequired(value);
		mIsUPCAAddendaRequired = value;
	}
	
//	public int getUpcAAddendaTimeout() {
//		return mUpcAAddendaTimeout;
//	}
//	
//	public void setUpcAAddendaTimeout(int value){
//		dlgUpcA.setAddendaTimeout(value);
//		mUpcAAddendaTimeout = value;
//	}
	
	public boolean getUPCAAddendaSeparator() {
		return dlgUpcA.getUPCAAddendaSeparator();
	}
	
	public void setUPCAAddendaSeparator(boolean value){
		dlgUpcA.setUPCAAddendaSeparator(value);
		mIsUPCAAddendaSeparator = value;
	}
	
	// ---------------------------------
	// UpcE0
	// ---------------------------------
	
	public boolean getUPCE0Expand() {
		return dlgUpcE0.getUPCE0Expand();
	}
	
	public void setUPCE0Expand(boolean value) {
		dlgUpcE0.setUPCE0Expand(value);
		mIsUPCE0Expand = value;
	}
	
	public boolean getUPCE0AddendaRequired() {
		return dlgUpcE0.getUPCE0AddendaRequired();
	}
	
	public void setUPCE0AddendaRequired(boolean value) {
		dlgUpcE0.setUPCE0AddendaRequired(value);
		mIsUPCE0AddendaRequired = value;
	}
	
	public boolean getUPCE0AddendaSeparator() {
		return dlgUpcE0.getUPCE0AddendaSeparator();
	}
	
	public void setUPCE0AddendaSeparator(boolean value) {
		dlgUpcE0.setUPCE0AddendaSeparator(value);
		mIsUPCE0AddendaSeparator = value;
	}
	
	public boolean getUPCE0CheckDigit() {
		return dlgUpcE0.getUPCE0CheckDigit();
	}
	
	public void setUPCE0CheckDigit(boolean value) {
		dlgUpcE0.setUPCE0CheckDigit(value);
		mIsUPCE0CheckDigit = value;
	}
	
	public boolean getUPCE0LeadingZero() {
		return dlgUpcE0.getUPCE0LeadingZero();
	}
	
	public void setUPCE0LeadingZero(boolean value) {
		dlgUpcE0.setUPCE0LeadingZero(value);
		mIsUPCE0LeadingZero = value;
	}
	
	public boolean getUPCE0Addenda2Digit() {
		return dlgUpcE0.getUPCE0Addenda2Digit();
	}
	
	public void setUPCE0Addenda2Digit(boolean value) {
		dlgUpcE0.setUPCE0Addenda2Digit(value);
		mIsUPCE0Addenda2Digit = value;
	}
	
	public boolean getUPCE0Addenda5Digit() {
		return dlgUpcE0.getUPCE0Addenda5Digit();
	}
	
	public void setUPCE0Addenda5Digit(boolean value) {
		dlgUpcE0.setUPCE0Addenda5Digit(value);
		mIsUPCE0Addenda5Digit = value;
	}
	
	
	// ---------------------------------
	// EanJan13
	// ---------------------------------
	
	public boolean getConvertUPCAtoEAN13() {
		return dlgEanJan13.getConvertUPCAtoEAN13();
	}
	
	public void setConvertUPCAtoEAN13(boolean value) {
		dlgEanJan13.setConvertUPCAtoEAN13(value);
		mIsConvertUPCAtoEAN13 = value;
	}
	
	public boolean getEANJAN13CheckDigit() {
		return dlgEanJan13.getEANJAN13CheckDigit();
	}
	
	public void setEANJAN13CheckDigit(boolean value) {
		dlgEanJan13.setEANJAN13CheckDigit(value);
		mIsEANJAN13CheckDigit = value;
	}
	
	public boolean getEANJAN13Addenda2Digit() {
		return dlgEanJan13.getEANJAN13Addenda2Digit();
	}
	
	public void setEANJAN13Addenda2Digit(boolean value) {
		dlgEanJan13.setEANJAN13Addenda2Digit(value);
		mIsEANJAN13Addenda2Digit = value;
	}
	
	public boolean getEANJAN13Addenda5Digit() {
		return dlgEanJan13.getEANJAN13Addenda5Digit();
	}
	
	public void setEANJAN13Addenda5Digit(boolean value) {
		dlgEanJan13.setEANJAN13Addenda5Digit(value);
		mIsEANJAN13Addenda5Digit = value;
	}
	
	public boolean getEANJAN13AddendaRequired() {
		return dlgEanJan13.getEANJAN13AddendaRequired();
	}
	
	public void setEANJAN13AddendaRequired(boolean value) {
		dlgEanJan13.setEANJAN13AddendaRequired(value);
		mIsEANJAN13AddendaRequired = value;
	}
	
	public boolean getEANJAN13AddendaSeparator() {
		return dlgEanJan13.getEANJAN13AddendaSeparator();
	}
	
	public void setEANJAN13AddendaSeparator(boolean value) {
		dlgEanJan13.setEANJAN13AddendaSeparator(value);
		mIsEANJAN13AddendaSeparator = value;
	}
	
	public boolean getISBNTranslate() {
		return dlgEanJan13.getISBNTranslate();
	}
	
	public void setISBNTranslate(boolean value) {
		dlgEanJan13.setISBNTranslate(value);
		mIsISBNTranslate = value;
	}	
	
	// ---------------------------------
	// EanJan8
	// ---------------------------------
	
	public boolean getEANJAN8CheckDigit(){
		return dlgEanJan8.getEANJAN8CheckDigit();
	}
	
	public void setEANJAN8CheckDigit(boolean value){
		dlgEanJan8.setEANJAN8CheckDigit(value);
		mIsEANJAN8CheckDigit = value;
	}

	public boolean getEANJAN8Addenda2Digit(){
		return dlgEanJan8.getEANJAN8Addenda2Digit();
	}
	
	public void setEANJAN8Addenda2Digit(boolean value){
		dlgEanJan8.setEANJAN8Addenda2Digit(value);
		mIsEANJAN8Addenda2Digit = value;
	}

	public boolean getEANJAN8Addenda5Digit(){
		return dlgEanJan8.getEANJAN8Addenda5Digit();
	}
	
	public void setEANJAN8Addenda5Digit(boolean value){
		dlgEanJan8.setEANJAN8Addenda5Digit(value);
		mIsEANJAN8Addenda5Digit = value;
	}

	public boolean getEANJAN8AddendaRequired(){
		return dlgEanJan8.getEANJAN8AddendaRequired();
	}
	
	public void setEANJAN8AddendaRequired(boolean value){
		dlgEanJan8.setEANJAN8AddendaRequired(value);
		mIsEANJAN8AddendaRequired = value;
	}

	public boolean getEANJAN8AddendaSeparator(){
		return dlgEanJan8.getEANJAN8AddendaSeparator();
	}
	
	public void setEANJAN8AddendaSeparator(boolean value){
		dlgEanJan8.setEANJAN8AddendaSeparator(value);
		mIsEANJAN8AddendaSeparator = value;
	}

	// ---------------------------------
	// MSI
	// ---------------------------------
	
	public MSICheckCharacter getMSICheckCharacter() {
		return dlgMsi.getMSICheckCharacter();
	}

	public void setMSICheckCharacter(MSICheckCharacter value){
		dlgMsi.setMSICheckCharacter(value);
		mMSICheckCharacter = value;
	}
	
	public int getMSILengthMin() {
		return dlgMsi.getLengthMin();
	}

	public void setMSILengthMin(int value){
		dlgMsi.setLengthMin(value);
		mMSILengthMin = value;
	}
	
	public int getMSILengthMax() {
		return dlgMsi.getLengthMax();
	}

	public void setMSILengthMax(int value){
		dlgMsi.setLengthMax(value);
		mMSILengthMax = value;
	}
	
	// ---------------------------------
	// GS1DataBar
	// ---------------------------------
	
	public int getGS1DataBarLengthMin() {
		return dlgGS1DataBar.getLengthMin();
	}

	public void setGS1DataBarLengthMin(int value){
		dlgGS1DataBar.setLengthMin(value);
		mGS1DataBarLengthMin = value;
	}
	
	public int getGS1DataBarLengthMax() {
		return dlgGS1DataBar.getLengthMax();
	}

	public void setGS1DataBarLengthMax(int value){
		dlgGS1DataBar.setLengthMax(value);
		mGS1DataBarLengthMax = value;
	}
	
	// ---------------------------------
	// CodablockA
	// ---------------------------------
	
	public int getCodablockALengthMin() {
		return dlgCodablockA.getLengthMin();
	}

	public void setCodablockALengthMin(int value){
		dlgCodablockA.setLengthMin(value);
		mCodablockALengthMin = value;
	}
	
	public int getCodablockALengthMax() {
		return dlgCodablockA.getLengthMax();
	}

	public void setCodablockALengthMax(int value){
		dlgCodablockA.setLengthMax(value);
		mCodablockALengthMax = value;
	}
		
	// ---------------------------------
	// CodablockF
	// ---------------------------------
	
	public int getCodablockFLengthMin() {
		return dlgCodablockF.getLengthMin();
	}

	public void setCodablockFLengthMin(int value){
		dlgCodablockF.setLengthMin(value);
		mCodablockFLengthMin = value;
	}
	
	public int getCodablockFLengthMax() {
		return dlgCodablockF.getLengthMax();
	}

	public void setCodablockFLengthMax(int value){
		dlgCodablockF.setLengthMax(value);
		mCodablockFLengthMax = value;
	}
		
	// ---------------------------------
	// PDF417
	// ---------------------------------
	
	public int getPDF417LengthMin() {
		return dlgPDF417.getLengthMin();
	}

	public void setPDF417LengthMin(int value){
		dlgPDF417.setLengthMin(value);
		mPDF417LengthMin = value;
	}
	
	public int getPDF417LengthMax() {
		return dlgPDF417.getLengthMax();
	}

	public void setPDF417LengthMax(int value){
		dlgPDF417.setLengthMax(value);
		mPDF417LengthMax = value;
	}
	
	// ---------------------------------
	// MicroPDF417
	// ---------------------------------
	
	public int getMicroPDF417LengthMin() {
		return dlgMicroPDF417.getLengthMin();
	}

	public void setMicroPDF417LengthMin(int value){
		dlgMicroPDF417.setLengthMin(value);
		mMicroPDF417LengthMin = value;
	}
	
	public int getMicroPDF417LengthMax() {
		return dlgMicroPDF417.getLengthMax();
	}

	public void setMicroPDF417LengthMax(int value){
		dlgMicroPDF417.setLengthMax(value);
		mMicroPDF417LengthMax = value;
	}
	
	// ---------------------------------
	// GS1CompositeCodes
	// ---------------------------------
	
	public boolean getGS1CompositeCodes() {
		return dlgGS1CompositeCodes.getGS1CompositeCodes();
	}

	public void setGS1CompositeCodes(boolean value){
		dlgGS1CompositeCodes.setGS1CompositeCodes(value);
		mIsGS1CompositeCodes = value;
	}
	
	public boolean getUPCEANVersion() {
		return dlgGS1CompositeCodes.getUPCEANVersion();
	}

	public void setUPCEANVersion(boolean value){
		dlgGS1CompositeCodes.setUPCEANVersion(value);
		mIsUPCEANVersion = value;
	}
	
	public int getGS1CompositeCodesLengthMin() {
		return dlgGS1CompositeCodes.getLengthMin();
	}

	public void setGS1CompositeCodesLengthMin(int value){
		dlgGS1CompositeCodes.setLengthMin(value);
		mGS1CompositeCodesLengthMin = value;
	}
	
	public int getGS1CompositeCodesLengthMax() {
		return dlgGS1CompositeCodes.getLengthMax();
	}

	public void setGS1CompositeCodesLengthMax(int value){
		dlgGS1CompositeCodes.setLengthMax(value);
		mGS1CompositeCodesLengthMax = value;
	}
	
	// ---------------------------------
	// QRCode
	// ---------------------------------
	
	public int getQRCodeLengthMin() {
		return dlgQRCode.getLengthMin();
	}

	public void setQRCodeLengthMin(int value){
		dlgQRCode.setLengthMin(value);
		mQRCodeLengthMin = value;
	}
	
	public int getQRCodeLengthMax() {
		return dlgQRCode.getLengthMax();
	}

	public void setQRCodeLengthMax(int value){
		dlgQRCode.setLengthMax(value);
		mQRCodeLengthMax = value;
	}

	public boolean getQRCodeAppend() {
		return dlgQRCode.getQRCodeAppend();
	}

	public void setQRCodeAppend(boolean value){
		dlgQRCode.setQRCodeAppend(value);
		mIsQRCodeAppend = value;
	}

	public CodePages getQRCodePage() {
		return dlgQRCode.getQRCodePage();
	}

	public void setQRCodePage(CodePages value){
		dlgQRCode.setQRCodePage(value);
		mQRCodePage = value;
	}

	// ---------------------------------
	// DataMatrix
	// ---------------------------------
	
	public int getDataMatrixLengthMin() {
		return dlgDataMatrix.getLengthMin();
	}

	public void setDataMatrixLengthMin(int value){
		dlgDataMatrix.setLengthMin(value);
		mDataMatrixLengthMin = value;
	}
	
	public int getDataMatrixLengthMax() {
		return dlgDataMatrix.getLengthMax();
	}

	public void setDataMatrixLengthMax(int value){
		dlgDataMatrix.setLengthMax(value);
		mDataMatrixLengthMax = value;
	}
	
//	public boolean getDataMatrixAppend() {
//		return mIsDataMatrixAppend;
//	}
//
//	public void setDataMatrixAppend(boolean value){
//		dlgDataMatrix.setDataMatrixAppend(value);
//		mIsDataMatrixAppend = value;
//	}
	
	public CodePages getDataMatrixCodePage() {
		return dlgDataMatrix.getDataMatrixCodePage();
	}

	public void setDataMatrixCodePage(CodePages value){
		dlgDataMatrix.setDataMatrixCodePage(value);
		mDataMatrixCodePage = value;
	}
	
	// ---------------------------------
	// MaxiCode
	// ---------------------------------
	
	public int getMaxiCodeLengthMin() {
		return dlgMaxiCode.getLengthMin();
	}

	public void setMaxiCodeLengthMin(int value){
		dlgMaxiCode.setLengthMin(value);
		mMaxiCodeLengthMin = value;
	}
	
	public int getMaxiCodeLengthMax() {
		return dlgMaxiCode.getLengthMax();
	}

	public void setMaxiCodeLengthMax(int value){
		dlgMaxiCode.setLengthMax(value);
		mMaxiCodeLengthMax = value;
	}
	
	// ---------------------------------
	// AztecCode
	// ---------------------------------
	
	public int getAztecCodeLengthMin() {
		return dlgAztecCode.getLengthMin();
	}

	public void setAztecCodeLengthMin(int value){
		dlgAztecCode.setLengthMin(value);
		mAztecCodeLengthMin = value;
	}
	
	public int getAztecCodeLengthMax() {
		return dlgAztecCode.getLengthMax();
	}

	public void setAztecCodeLengthMax(int value){
		dlgAztecCode.setLengthMax(value);
		mAztecCodeLengthMax = value;
	}
	
	public boolean getAztecAppend() {
		return dlgAztecCode.getAztecAppend();
	}

	public void setAztecAppend(boolean value){
		dlgAztecCode.setAztecAppend(value);
		mIsAztecAppend = value;
	}
	
	public CodePages getAztecCodePage() {
		return dlgAztecCode.getAztecCodePage();
	}

	public void setAztecCodePage(CodePages value){
		dlgAztecCode.setAztecCodePage(value);
		mAztecCodePage = value;
	}
	
	// ---------------------------------
	// HanXinCode
	// ---------------------------------
	
	public int getHanXinCodeLengthMin() {
		return dlgHanXinCode.getLengthMin();
	}

	public void setHanXinCodeLengthMin(int value){
		dlgHanXinCode.setLengthMin(value);
		mHanXinCodeLengthMin = value;
	}
	
	public int getHanXinCodeLengthMax() {
		return dlgHanXinCode.getLengthMax();
	}

	public void setHanXinCodeLengthMax(int value){
		dlgHanXinCode.setLengthMax(value);
		mHanXinCodeLengthMax = value;
	}
	
	// ---------------------------------
	// PostalCodes2D
	// ---------------------------------
	
	public boolean getPlanetCodeCheckDigit(){
		return dlgPostalCodes2D.getPlanetCodeCheckDigit();
	}
	
	public void setPlanetCodeCheckDigit(boolean value) {
		dlgPostalCodes2D.setPlanetCodeCheckDigit(value);
		mIsPlanetCodeCheckDigit = value;
	}
	
	public boolean getPostnetCheckDigit(){
		return dlgPostalCodes2D.getPostnetCheckDigit();
	}
	
	public void setPostnetCheckDigit(boolean value) {
		dlgPostalCodes2D.setPostnetCheckDigit(value);
		mIsPostnetCheckDigit = value;
	}
	
	public AustralianPostInterpretation getAustralianPostInterpretation(){
		return dlgPostalCodes2D.getAustralianPostInterpretation();
	}
	
	public void setAustralianPostInterpretation(AustralianPostInterpretation value) {
		dlgPostalCodes2D.setAustralianPostInterpretation(value);
		mAustralianPostInterpretation = value;
	}
	
	// ---------------------------------
	// PostalCodesLinear
	// ---------------------------------
	
	public int getChinaPostLengthMin() {
		return dlgPostalCodesLinear.getChinaPostLengthMin();
	}

	public void setChinaPostLengthMin(int value){
		dlgPostalCodesLinear.setChinaPostLengthMin(value);
		mChinaPostLengthMin = value;
	}
	
	public int getChinaPostLengthMax() {
		return dlgPostalCodesLinear.getChinaPostLengthMax();
	}

	public void setChinaPostLengthMax(int value){
		dlgPostalCodesLinear.setChinaPostLengthMax(value);
		mChinaPostLengthMax = value;
	}
	
	public int getKoreaPostLengthMin() {
		return dlgPostalCodesLinear.getKoreaPostLengthMin();
	}

	public void setKoreaPostLengthMin(int value){
		dlgPostalCodesLinear.setKoreaPostLengthMin(value);
		mKoreaPostLengthMin = value;
	}
	
	public int getKoreaPostLengthMax() {
		return dlgPostalCodesLinear.getKoreaPostLengthMax();
	}

	public void setKoreaPostLengthMax(int value){
		dlgPostalCodesLinear.setKoreaPostLengthMax(value);
		mKoreaPostLengthMax = value;
	}
	
	public boolean getKoreaPostCheckDigit() {
		return dlgPostalCodesLinear.getKoreaPostCheckDigit();
	}

	public void setKoreaPostCheckDigit(boolean value){
		dlgPostalCodesLinear.setKoreaPostCheckDigit(value);
		mIsKoreaPostCheckDigit = value;
	}
	
	// ETC
	public UPCAEAN13ExtendedCouponCode getUPCAEAN13ExtendedCouponCode(){
		return mUPCAEAN13ExtendedCouponCode;
	}
	
	public void setUPCAEAN13ExtendedCouponCode(UPCAEAN13ExtendedCouponCode value){
		mUPCAEAN13ExtendedCouponCode = value;
	}
	
	public boolean getCouponGS1DataBarOutput(){
		return mIsCouponGS1DataBarOutput;
	}
	
	public void setCouponGS1DataBarOutput(boolean value){
		mIsCouponGS1DataBarOutput = value;
	}
	
//	public boolean getLabelCode(){
//		return mIsLabelCode;
//	}
//	
//	public void setLabelCode(boolean value) {
//		mIsLabelCode = value;
//	}
	
	public GS1Emulation getGS1Emulation() {
		return mGS1Emulation;
	}
	
	public void setGS1Emulation(GS1Emulation value){
		mGS1Emulation = value;
	}

	public VideoReverse getVideoReverse() {
		return mVideoReverse;
	}
	
	public void setVideoReverse(VideoReverse value){
		mVideoReverse = value;
	}

	
	
	// ---------------------------------
	// restore parameter ( cancel )
	// ---------------------------------
	public void restoreParameterValue() {
		// Codabar
		dlgCodabar.setCodabarStartStopCharacters(mIsCodabarStartStopCharacters);
		dlgCodabar.setCodabarCheckCharacter(mCodabarCheckCharacter);
		dlgCodabar.setCodabarConcatenation(mCodabarConcatenation);
		dlgCodabar.setLengthMin(mCodabarLengthMin);
		dlgCodabar.setLengthMax(mCodabarLengthMax);
		
		// Code39
		dlgCode39.setCode39StartStopCharacters(mIsCode39StartStopCharacters);
		dlgCode39.setCode39CheckCharacter(mCode39CheckCharacter);
		dlgCode39.setLengthMin(mCode39LengthMin);
		dlgCode39.setLengthMax(mCode39LengthMax);
		dlgCode39.setCode39Append(mIsCode39Append);
		dlgCode39.setCode39FullASCII(mIsCode39FullASCII);
		dlgCode39.setCode39CodePage(mCode39CodePage);
		
		// I2of5
		dlgI2of5.setI2of5CheckDigit(mI2of5CheckDigit);
		dlgI2of5.setLengthMin(mI2of5LengthMin);
		dlgI2of5.setLengthMax(mI2of5LengthMax);

		// Code93
		dlgCode93.setLengthMin(mCode93LengthMin);
		dlgCode93.setLengthMax(mCode93LengthMax);
		dlgCode93.setCode93Append(mIsCode93Append);
		dlgCode93.setCode93CodePage(mCode93CodePage);

		// NEC2of5
		dlgNEC2of5.setNEC2of5CheckDigit(mNEC2of5CheckDigit);
		dlgNEC2of5.setLengthMin(mNEC2of5LengthMin);
		dlgNEC2of5.setLengthMax(mNEC2of5LengthMax);
		
		// Straight2of5Industrial
		dlgStraight2of5Industrial.setLengthMin(mStraight2of5IndustrialLengthMin);
		dlgStraight2of5Industrial.setLengthMax(mStraight2of5IndustrialLengthMax);
		
		// Straight2of5IATA
		dlgStraight2of5IATA.setLengthMin(mStraight2of5IATALengthMin);
		dlgStraight2of5IATA.setLengthMax(mStraight2of5IATALengthMax);

		// Matrix2of5
		dlgMatrix2of5.setLengthMin(mMatrix2of5LengthMin);
		dlgMatrix2of5.setLengthMax(mMatrix2of5LengthMax);
		
		// Code11
		dlgCode11.setCode11CheckDigitsRequired(mCode11CheckDigitsRequired);
		dlgCode11.setLengthMin(mCode11LengthMin);
		dlgCode11.setLengthMax(mCode11LengthMax);

		// Code128
		dlgCode128.setISBT128Concatenation(mIsISBT128Concatenation);
		dlgCode128.setLengthMin(mCode128LengthMin);
		dlgCode128.setLengthMax(mCode128LengthMax);
		dlgCode128.setCode128Append(mIsCode128Append);
		dlgCode128.setCode128CodePage(mCode128CodePage);

		// GS1128
		dlgGS1128.setLengthMin(mGS1128LengthMin);
		dlgGS1128.setLengthMax(mGS1128LengthMax);
		
		// UpcA
		dlgUpcA.setUPCACheckDigit(mIsUPCACheckDigit);
		dlgUpcA.setUPCANumberSystem(mIsUPCANumberSystem);
		dlgUpcA.setUPCAAddenda2Digit(mIsUPCAAddenda2Digit);
		dlgUpcA.setUPCAAddenda5Digit(mIsUPCAAddenda5Digit);
		dlgUpcA.setUPCAAddendaRequired(mIsUPCAAddendaRequired);
//		dlgUpcA.setAddendaTimeout(mUpcAAddendaTimeout);
		dlgUpcA.setUPCAAddendaSeparator(mIsUPCAAddendaSeparator);

		// UpcE0
		dlgUpcE0.setUPCE0Expand(mIsUPCE0Expand);
		dlgUpcE0.setUPCE0AddendaRequired(mIsUPCE0AddendaRequired);
		dlgUpcE0.setUPCE0AddendaSeparator(mIsUPCE0AddendaSeparator);
		dlgUpcE0.setUPCE0CheckDigit(mIsUPCE0CheckDigit);
		dlgUpcE0.setUPCE0LeadingZero(mIsUPCE0LeadingZero);
		dlgUpcE0.setUPCE0Addenda2Digit(mIsUPCE0Addenda2Digit);
		dlgUpcE0.setUPCE0Addenda5Digit(mIsUPCE0Addenda5Digit);

		// EanJan13
		dlgEanJan13.setConvertUPCAtoEAN13(mIsConvertUPCAtoEAN13);
		dlgEanJan13.setEANJAN13CheckDigit(mIsEANJAN13CheckDigit);
		dlgEanJan13.setEANJAN13Addenda2Digit(mIsEANJAN13Addenda2Digit);
		dlgEanJan13.setEANJAN13Addenda5Digit(mIsEANJAN13Addenda5Digit);
		dlgEanJan13.setEANJAN13AddendaRequired(mIsEANJAN13AddendaRequired);
		dlgEanJan13.setEANJAN13AddendaSeparator(mIsEANJAN13AddendaSeparator);
		dlgEanJan13.setISBNTranslate(mIsISBNTranslate);
		
		// EanJan8
		dlgEanJan8.setEANJAN8CheckDigit(mIsEANJAN8CheckDigit);
		dlgEanJan8.setEANJAN8Addenda2Digit(mIsEANJAN8Addenda2Digit);
		dlgEanJan8.setEANJAN8Addenda5Digit(mIsEANJAN8Addenda5Digit);
		dlgEanJan8.setEANJAN8AddendaRequired(mIsEANJAN8AddendaRequired);
		dlgEanJan8.setEANJAN8AddendaSeparator(mIsEANJAN8AddendaSeparator);
		
		// MSI
		dlgMsi.setMSICheckCharacter(mMSICheckCharacter);
		dlgMsi.setLengthMin(mMSILengthMin);
		dlgMsi.setLengthMax(mMSILengthMax);
		
		// GS1DataBar
		dlgGS1DataBar.setLengthMin(mGS1DataBarLengthMin);
		dlgGS1DataBar.setLengthMax(mGS1DataBarLengthMax);
		
		// CodablockA
		dlgCodablockA.setLengthMin(mCodablockALengthMin);
		dlgCodablockA.setLengthMax(mCodablockALengthMax);

		// CodablockF
		dlgCodablockF.setLengthMin(mCodablockFLengthMin);
		dlgCodablockF.setLengthMax(mCodablockFLengthMax);
		
		// PDF417
		dlgPDF417.setLengthMin(mPDF417LengthMin);
		dlgPDF417.setLengthMax(mPDF417LengthMax);
		
		// MicroPDF417
		dlgMicroPDF417.setLengthMin(mMicroPDF417LengthMin);
		dlgMicroPDF417.setLengthMax(mMicroPDF417LengthMax);

		// GS1CompositeCodes
		dlgGS1CompositeCodes.setGS1CompositeCodes(mIsGS1CompositeCodes);
		dlgGS1CompositeCodes.setUPCEANVersion(mIsUPCEANVersion);
		dlgGS1CompositeCodes.setLengthMin(mGS1CompositeCodesLengthMin);
		dlgGS1CompositeCodes.setLengthMax(mGS1CompositeCodesLengthMax);
		
		// QRCode
		dlgQRCode.setLengthMin(mQRCodeLengthMin);
		dlgQRCode.setLengthMax(mQRCodeLengthMax);
		dlgQRCode.setQRCodeAppend(mIsQRCodeAppend);
		dlgQRCode.setQRCodePage(mQRCodePage);

		// DataMatrix
		dlgDataMatrix.setLengthMin(mDataMatrixLengthMin);
		dlgDataMatrix.setLengthMax(mDataMatrixLengthMax);
//		dlgDataMatrix.setDataMatrixAppend(mIsDataMatrixAppend);
		dlgDataMatrix.setDataMatrixCodePage(mDataMatrixCodePage);

		// MaxiCode
		dlgMaxiCode.setLengthMin(mMaxiCodeLengthMin);
		dlgMaxiCode.setLengthMax(mMaxiCodeLengthMax);

		// AztecCode
		dlgAztecCode.setLengthMin(mAztecCodeLengthMin);
		dlgAztecCode.setLengthMax(mAztecCodeLengthMax);
		dlgAztecCode.setAztecAppend(mIsAztecAppend);
		dlgAztecCode.setAztecCodePage(mAztecCodePage);

		// HanXinCode
		dlgHanXinCode.setLengthMin(mHanXinCodeLengthMin);
		dlgHanXinCode.setLengthMax(mHanXinCodeLengthMax);

		// PostalCodes2D
		dlgPostalCodes2D.setPlanetCodeCheckDigit(mIsPlanetCodeCheckDigit);
		dlgPostalCodes2D.setPostnetCheckDigit(mIsPostnetCheckDigit);
		dlgPostalCodes2D.setAustralianPostInterpretation(mAustralianPostInterpretation);
		
		// PostalCodesLinear
		dlgPostalCodesLinear.setChinaPostLengthMin(mChinaPostLengthMin);
		dlgPostalCodesLinear.setChinaPostLengthMax(mChinaPostLengthMax);
		dlgPostalCodesLinear.setKoreaPostLengthMin(mKoreaPostLengthMin);
		dlgPostalCodesLinear.setKoreaPostLengthMax(mKoreaPostLengthMax);
		dlgPostalCodesLinear.setKoreaPostCheckDigit(mIsKoreaPostCheckDigit);

	}
	
	public void showDialog(Context context, final IValueChangedListener changedListener,
			final ICancelListener cancelListener) {
		
		mContext = context;
		
		LinearLayout root = (LinearLayout) LinearLayout.inflate(context, R.layout.dialog_barcode_option_spc_2d, null);
		
		linearCodabar = (LinearLayout) root.findViewById(R.id.linear_codabar);
		linearCodabar.setOnClickListener(this);
		linearCode39 = (LinearLayout) root.findViewById(R.id.linear_code39);
		linearCode39.setOnClickListener(this);
		linearI2of5 = (LinearLayout) root.findViewById(R.id.linear_i2of5);
		linearI2of5.setOnClickListener(this);
		linearCode93 = (LinearLayout) root.findViewById(R.id.linear_code93);
		linearCode93.setOnClickListener(this);
		linearNEC2of5 = (LinearLayout) root.findViewById(R.id.linear_nec2of5);
		linearNEC2of5.setOnClickListener(this);
		linearStraight2of5Industrial = (LinearLayout) root.findViewById(R.id.linear_straight2of5_industrial);
		linearStraight2of5Industrial.setOnClickListener(this);
		linearStraight2of5IATA = (LinearLayout) root.findViewById(R.id.linear_straight2of5_iata);
		linearStraight2of5IATA.setOnClickListener(this);
		linearMatrix2of5 = (LinearLayout) root.findViewById(R.id.linear_matrix2of5);
		linearMatrix2of5.setOnClickListener(this);
		linearCode11 = (LinearLayout) root.findViewById(R.id.linear_code11) ;
		linearCode11.setOnClickListener(this);
		
		linearCode128 = (LinearLayout) root.findViewById(R.id.linear_code128);
		linearCode128.setOnClickListener(this);
		linearGS1128 = (LinearLayout) root.findViewById(R.id.linear_gs1_128);
		linearGS1128.setOnClickListener(this);
		linearUpcA = (LinearLayout) root.findViewById(R.id.linear_upca);
		linearUpcA.setOnClickListener(this);
		linearUpcE0 = (LinearLayout) root.findViewById(R.id.linear_upc_e0);
		linearUpcE0.setOnClickListener(this);
		linearEanJan13 = (LinearLayout) root.findViewById(R.id.linear_ean_jan13);
		linearEanJan13.setOnClickListener(this);
		linearEanJan8 = (LinearLayout) root.findViewById(R.id.linear_ean_jan8) ;
		linearEanJan8.setOnClickListener(this);
		linearMsi = (LinearLayout) root.findViewById(R.id.linear_msi);
		linearMsi.setOnClickListener(this);
		linearGS1DataBar = (LinearLayout) root.findViewById(R.id.linear_gs1_databar);
		linearGS1DataBar.setOnClickListener(this);
		linearCodablockA = (LinearLayout) root.findViewById(R.id.linear_codablock_a);
		linearCodablockA.setOnClickListener(this);
		linearCodablockF = (LinearLayout) root.findViewById(R.id.linear_codablock_f);
		linearCodablockF.setOnClickListener(this);
		linearPDF417 = (LinearLayout) root.findViewById(R.id.linear_pdf417);
		linearPDF417.setOnClickListener(this);
		linearMicroPDF417 = (LinearLayout) root.findViewById(R.id.linear_micro_pdf417);
		linearMicroPDF417.setOnClickListener(this);
		linearGS1CompositeCodes = (LinearLayout) root.findViewById(R.id.linear_gs1_composite_codes);
		linearGS1CompositeCodes.setOnClickListener(this);
		linearQRCode = (LinearLayout) root.findViewById(R.id.linear_qr_code) ;
		linearQRCode.setOnClickListener(this);
		linearDataMatrix = (LinearLayout) root.findViewById(R.id.linear_data_matrix);
		linearDataMatrix.setOnClickListener(this);
		linearMaxiCode = (LinearLayout) root.findViewById(R.id.linear_maxicode);
		linearMaxiCode.setOnClickListener(this);
		linearAztecCode = (LinearLayout) root.findViewById(R.id.linear_aztec_code) ;
		linearAztecCode.setOnClickListener(this);
		linearHanXinCode = (LinearLayout) root.findViewById(R.id.linear_han_xin_code);
		linearHanXinCode.setOnClickListener(this);
		linearPostalCodes2D = (LinearLayout) root.findViewById(R.id.linear_postal_codes_2d);
		linearPostalCodes2D.setOnClickListener(this);
		linearPostalCodesLinear = (LinearLayout) root.findViewById(R.id.linear_postal_codes_linear);
		linearPostalCodesLinear.setOnClickListener(this);
		
		linearUPCAEAN13ExtendedCouponCode = (LinearLayout) root.findViewById(R.id.linear_upca_ean13_extended_coupon_code);
		linearUPCAEAN13ExtendedCouponCode.setOnClickListener(this);
		linearCouponGS1DataBarOutput = (LinearLayout) root.findViewById(R.id.linear_coupon_gs1_databar_output);
		linearCouponGS1DataBarOutput.setOnClickListener(this);
		linearLabelCode = (LinearLayout) root.findViewById(R.id.linear_label_code);
		linearLabelCode.setOnClickListener(this);
		linearGS1Emulation = (LinearLayout) root.findViewById(R.id.linear_gs1_emulation);	
		linearGS1Emulation.setOnClickListener(this);
		linearVideoReverse = (LinearLayout) root.findViewById(R.id.linear_video_reverse);	
		linearVideoReverse.setOnClickListener(this);
		
		txtUpcAEan13ExtendedCouponCode = (TextView) root.findViewById(R.id.upca_ean13_extended_coupon_code);
		chkCouponGS1DataBarOutput = (CheckBox) root.findViewById(R.id.coupon_gs1_databar_output);
		chkCouponGS1DataBarOutput.setOnCheckedChangeListener(this);
//		chkLabelCode = (CheckBox) root.findViewById(R.id.label_code);
//		chkLabelCode.setOnCheckedChangeListener(this);
		txtGS1Emulation = (TextView) root.findViewById(R.id.gs1_emulation);	
		txtVideoReverse = (TextView) root.findViewById(R.id.video_reverse);
		
		dlgUpcAEan13ExtendedCouponCode = new EnumListDialog(txtUpcAEan13ExtendedCouponCode, UPCAEAN13ExtendedCouponCode.values());
		dlgUpcAEan13ExtendedCouponCode.setValue(mUPCAEAN13ExtendedCouponCode);
		dlgGS1Emulation =  new EnumListDialog(txtGS1Emulation, GS1Emulation.values() );                 
		dlgGS1Emulation.setValue(mGS1Emulation);

		dlgVideoReverse =  new EnumListDialog(txtVideoReverse, VideoReverse.values() );                 
		dlgVideoReverse.setValue(mVideoReverse);
		
		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(R.string.symbol_barcode_option);
		builder.setView(root);
		builder.setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				mUPCAEAN13ExtendedCouponCode = (UPCAEAN13ExtendedCouponCode) dlgUpcAEan13ExtendedCouponCode.getValue();
				mGS1Emulation = (GS1Emulation) dlgGS1Emulation.getValue();
				mVideoReverse = (VideoReverse) dlgVideoReverse.getValue();
				
				mIsCouponGS1DataBarOutput = chkCouponGS1DataBarOutput.isChecked();
//				mIsLabelCode = chkLabelCode.isChecked();
				
				if(changedListener != null){
					changedListener.onValueChanged(SPC2DBarcodeOptionDialog.this);
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

				dlgUpcAEan13ExtendedCouponCode.display();
				dlgGS1Emulation.display();
				dlgVideoReverse.display();
				
				chkCouponGS1DataBarOutput.setChecked(mIsCouponGS1DataBarOutput);
//				chkLabelCode.setChecked(mIsLabelCode);
				
				ATLog.i(TAG, INFO, "INFO. showDialog().onShow()");				
			}
		});
		dialog.show();
		
		ATLog.i(TAG, INFO, "INFO. showDialog()");
	}
	
	// ------------------------------------------------------------------------
	// Declare Interface IValueChangedListener
	// ------------------------------------------------------------------------
	public interface IValueChangedListener {
		void onValueChanged(SPC2DBarcodeOptionDialog dialog);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.linear_codabar:
			dlgCodabar.showDialog(mContext, R.string.codabar) ;
			break;
		case R.id.linear_code39 :
			dlgCode39.showDialog(mContext, R.string.code39) ;
			break;
		case R.id.linear_i2of5 :
			dlgI2of5.showDialog(mContext, R.string.i2of5) ;
			break;
		case R.id.linear_code93 :
			dlgCode93.showDialog(mContext, R.string.code93) ;
			break;
		case R.id.linear_nec2of5 :
			dlgNEC2of5.showDialog(mContext, R.string.nec2of5) ;
			break;
		case R.id.linear_straight2of5_industrial :
			dlgStraight2of5Industrial.showDialog(mContext, R.string.straight2of5_industrial) ;
			break;
		case R.id.linear_straight2of5_iata :
			dlgStraight2of5IATA.showDialog(mContext, R.string.straight2of5_iata) ;
			break;
		case R.id.linear_matrix2of5 :
			dlgMatrix2of5.showDialog(mContext, R.string.matrix2of5) ;
			break;
		case R.id.linear_code11 :
			dlgCode11.showDialog(mContext, R.string.code11) ;
			break;
		case R.id.linear_code128 :
			dlgCode128.showDialog(mContext, R.string.code128) ;
			break;
		case R.id.linear_gs1_128 :
			dlgGS1128.showDialog(mContext, R.string.gs1_128) ;
			break;
		case R.id.linear_upca :
			dlgUpcA.showDialog(mContext, R.string.upca) ;
			break;
		case R.id.linear_upc_e0 :
			dlgUpcE0.showDialog(mContext, R.string.upc_e0) ;
			break;
		case R.id.linear_ean_jan13 :
			dlgEanJan13.showDialog(mContext, R.string.ean_jan13) ;
			break;
		case R.id.linear_ean_jan8 :
			dlgEanJan8.showDialog(mContext, R.string.ean_jan8) ;
			break;
		case R.id.linear_msi :
			dlgMsi.showDialog(mContext, R.string.msi) ;
			break;
		case R.id.linear_gs1_databar :
			dlgGS1DataBar.showDialog(mContext, R.string.gs1_databar) ;
			break;
		case R.id.linear_codablock_a :
			dlgCodablockA.showDialog(mContext, R.string.codablock_a) ;
			break;
		case R.id.linear_codablock_f :
			dlgCodablockF.showDialog(mContext, R.string.codablock_f) ;
			break;
		case R.id.linear_pdf417 :
			dlgPDF417.showDialog(mContext, R.string.pdf417) ;
			break;
		case R.id.linear_micro_pdf417 :
			dlgMicroPDF417.showDialog(mContext, R.string.micro_pdf417) ;
			break;
		case R.id.linear_gs1_composite_codes :
			dlgGS1CompositeCodes.showDialog(mContext, R.string.gs1_composite_codes) ;
			break;
		case R.id.linear_qr_code :
			dlgQRCode.showDialog(mContext, R.string.qr_code) ;
			break;
		case R.id.linear_data_matrix :
			dlgDataMatrix.showDialog(mContext, R.string.data_matrix) ;
			break;
		case R.id.linear_maxicode :
			dlgMaxiCode.showDialog(mContext, R.string.maxicode) ;
			break;
		case R.id.linear_aztec_code :
			dlgAztecCode.showDialog(mContext, R.string.aztec_code) ;
			break;
		case R.id.linear_han_xin_code :
			dlgHanXinCode.showDialog(mContext, R.string.han_xin_code) ;
			break;
		case R.id.linear_postal_codes_2d :
			dlgPostalCodes2D.showDialog(mContext, R.string.postal_codes_2d) ;
			break;
		case R.id.linear_postal_codes_linear :
			dlgPostalCodesLinear.showDialog(mContext, R.string.postal_codes_linear) ;
			break;
		case R.id.linear_upca_ean13_extended_coupon_code :
			dlgUpcAEan13ExtendedCouponCode.showDialog(mContext, R.string.upca_ean13_extended_coupon_code);
			break;

		case R.id.linear_gs1_emulation :
			dlgGS1Emulation.showDialog(mContext, R.string.gs1_emulation);
			break;

		case R.id.linear_video_reverse :
			dlgVideoReverse.showDialog(mContext, R.string.video_reverse);
			break;
		}
	}
	
	@Override
	public void onCheckedChanged(CompoundButton view, boolean isChecked) {
		switch(view.getId()){
		case R.id.linear_coupon_gs1_databar_output :

			break;
		case R.id.linear_label_code :

			break;
		}
	}
}
