package com.atid.app.atx.activity;

import java.nio.charset.Charset;
import java.util.Locale;

import com.atid.app.atx.R;
import com.atid.app.atx.data.Constants;
import com.atid.app.atx.data.GlobalData;
import com.atid.app.atx.dialog.BaseDialog;
import com.atid.app.atx.dialog.CharsetDialog;
import com.atid.app.atx.dialog.MessageBox;
import com.atid.app.atx.dialog.NumberUnitDialog;
import com.atid.app.atx.dialog.PostalDialog;
import com.atid.app.atx.dialog.SPC2DBarcodeOptionDialog;
import com.atid.app.atx.dialog.SSI1DBarcodeOptionDialog;
import com.atid.app.atx.dialog.SSI2DBarcodeOptionDialog;
import com.atid.app.atx.dialog.SymbolStateDialog;
import com.atid.app.atx.dialog.WaitDialog;
import com.atid.lib.diagnostics.ATException;
import com.atid.lib.module.barcode.spc.param.SPCParamName;
import com.atid.lib.module.barcode.spc.param.SPCParamNameList;
import com.atid.lib.module.barcode.spc.param.SPCParamValue;
import com.atid.lib.module.barcode.spc.param.SPCParamValueList;
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
import com.atid.lib.module.barcode.ssi.param.SSI1DParamName;
import com.atid.lib.module.barcode.ssi.param.SSI1DParamNameList;
import com.atid.lib.module.barcode.ssi.param.SSI1DParamValue;
import com.atid.lib.module.barcode.ssi.param.SSI1DParamValueList;
import com.atid.lib.module.barcode.ssi.param.SSI2DParamName;
import com.atid.lib.module.barcode.ssi.param.SSI2DParamNameList;
import com.atid.lib.module.barcode.ssi.param.SSI2DParamValue;
import com.atid.lib.module.barcode.ssi.param.SSI2DParamValueList;
import com.atid.lib.module.barcode.ssi.type.AustraliaPostFormat;
import com.atid.lib.module.barcode.ssi.type.AztecInverse;
import com.atid.lib.module.barcode.ssi.type.BooklandISBNFormat;
import com.atid.lib.module.barcode.ssi.type.CodabarStartStopCharactersDetection;
import com.atid.lib.module.barcode.ssi.type.Code11CheckDigitVerification;
import com.atid.lib.module.barcode.ssi.type.CodeLength;
import com.atid.lib.module.barcode.ssi.type.CompositeBeepMode;
import com.atid.lib.module.barcode.ssi.type.CouponReport;
import com.atid.lib.module.barcode.ssi.type.DataMatrixInverse;
import com.atid.lib.module.barcode.ssi.type.DecodeMirrorImages;
import com.atid.lib.module.barcode.ssi.type.DecodeUpcEanJanSupplementals;
import com.atid.lib.module.barcode.ssi.type.GS1DataBarLimitedSecurityLevel;
import com.atid.lib.module.barcode.ssi.type.HanXinInverse;
import com.atid.lib.module.barcode.ssi.type.I2of5CheckDigitVerification;
import com.atid.lib.module.barcode.ssi.type.ISBTConcatenation;
import com.atid.lib.module.barcode.ssi.type.IntercharacterGapSize;
import com.atid.lib.module.barcode.ssi.type.Inverse1D;
import com.atid.lib.module.barcode.ssi.type.LinearSecurityLevel;
import com.atid.lib.module.barcode.ssi.type.MSICheckDigitAlgorithm;
import com.atid.lib.module.barcode.ssi.type.MSICheckDigits;
import com.atid.lib.module.barcode.ssi.type.Preamble;
import com.atid.lib.module.barcode.ssi.type.QRInverse;
import com.atid.lib.module.barcode.ssi.type.QuietZoneLevel1D;
import com.atid.lib.module.barcode.ssi.type.RedundancyLevel;
import com.atid.lib.module.barcode.ssi.type.SecurityLevel;
import com.atid.lib.module.barcode.ssi.type.UpcCompositeMode;
import com.atid.lib.module.barcode.ssi.type.UpcEanJanSupplementalAIMIDFormat;
import com.atid.lib.module.barcode.ssi.type.UpcEanSecurityLevel;
import com.atid.lib.module.barcode.types.BarcodePostType;
import com.atid.lib.reader.ATEAReader;
import com.atid.lib.reader.event.IATEAReaderEventListener;
import com.atid.lib.reader.types.KeyState;
import com.atid.lib.reader.types.KeyType;
import com.atid.lib.reader.types.OperationMode;
import com.atid.lib.transport.types.ConnectState;
import com.atid.lib.types.ActionState;
import com.atid.lib.types.ModuleBarcodeType;
import com.atid.lib.types.ResultCode;
import com.atid.lib.util.diagnotics.ATLog;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;




public class BarcodeOptionActivity extends Activity 
		implements IATEAReaderEventListener, OnClickListener{
	private static final String TAG = BarcodeOptionActivity.class.getSimpleName();
	private static final int INFO = ATLog.L1;

	public static final int ID = 0x42101000;
	
	private static final int DEFAULT_VALUE = 0;
	
	private static final int LOADING_STATE_READER = 1;
	private static final int LOADING_STATE_BARCODE_READER = 2;
	private static final int LOADING_STATE_BARCODE_DISABLE_ACTION_KEY = 3;
	private static final int LOADING_STATE_BARCODE_OPTION = 4;
	private static final int LOADING_STATE_BARCODE_SYMBOL = 5;
	private static final int LOADING_STATE_BARCODE_CHARSET = 6;

	// ------------------------------------------------------------------------
	// Member Variable
	// ------------------------------------------------------------------------
	private LinearLayout linearNameSymbolState;
	private LinearLayout linearPostalCodes;
	private LinearLayout linearNameSymbolBarcodeOption;
	private LinearLayout linearNameSymbolAllEnable;
	private LinearLayout linearNameSymbolAllDisable;
	private LinearLayout linearNameSymbolDefault;
	private LinearLayout linearNameSymbolCharacterSet;
	private LinearLayout linearNameBarcodeRestartTime;
	
	private TextView txtPostalCodes;
	private TextView txtValueBarcodeChracterSet;
	private TextView txtValueBarcodeRestartTime;
	
	private ATEAReader mReader;
	
	private SymbolStateDialog dlgSymbolState;
	private PostalDialog dlgPostalCodes;
	private SSI1DBarcodeOptionDialog dlgSSI1DBarcodeOption;
	private SSI2DBarcodeOptionDialog dlgSSI2DBarcodeOption;
	private SPC2DBarcodeOptionDialog dlgSPC2DBarcodeOption;
	
	private CharsetDialog dlgBarcodeChracterSet;
	private NumberUnitDialog dlgBarcodeRestartTime;
	
	private boolean mIsPostSelect;
	private BarcodePostType mPost;
	private int mBarcodeRestartTime;
	
	private Thread mThread;
	private volatile boolean mIsThreadAlive;
	
	private ModuleBarcodeType mModuleBarcodeType;
	
	private int mLoadingState;
	private ATException mLoadingError;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_barcode_option);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		int position = getIntent().getIntExtra(Constants.SELECTED_READER, DEFAULT_VALUE);
		
		mReader = GlobalData.ReaderManager.get(position);
		mReader.addListener(this);
		
		dlgSymbolState = null;
		dlgPostalCodes = null;
		dlgSSI2DBarcodeOption = null;
		dlgSPC2DBarcodeOption = null;
		dlgBarcodeChracterSet = null;
		dlgBarcodeRestartTime = null;
		
		mIsPostSelect = false;
		mPost = BarcodePostType.Off;
		
		mBarcodeRestartTime = getIntent().getIntExtra(Constants.BARCODE_RESTART_TIME, DEFAULT_VALUE);
		
		mModuleBarcodeType = ModuleBarcodeType.AT2DSE4710;
		
		initialize();
		
		mIsThreadAlive = false;
		mThread = new Thread(mLoadingProc);
		mThread.start();
		
		WaitDialog.show(this, R.string.msg_initialize_view, new DialogInterface.OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				
				mIsThreadAlive = false;
				if(mThread.isAlive()){
					try{
						mThread.join();
					}catch(InterruptedException e){
						ATLog.e(TAG, "ERROR. onCreate().onCancel() - Failed to join thread");
					}
				}
				WaitDialog.hide();
				setResult(Activity.RESULT_CANCELED);
				BarcodeOptionActivity.this.finish();
				ATLog.i(TAG, INFO, "INFO. onCreate().onCancel()");
				return;
			}
		});
		
		ATLog.i(TAG, INFO, "INFO. onCreate()");
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:
			mReader.removeListener(this);
			
			Intent intent = new Intent();
			intent.putExtra(Constants.BARCODE_RESTART_TIME, mBarcodeRestartTime);
			
			ATLog.i(TAG, INFO, "INFO. onOptionsItemSelected() - Barcode Restart time [%d]", mBarcodeRestartTime);
			
			setResult(Activity.RESULT_CANCELED);
			finish();
			return true;
		}

		ATLog.i(TAG, INFO, "INFO. onOptionsItemSelected()");
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onBackPressed() {
		
		mReader.removeListener(this);
		WaitDialog.hide();

		Intent intent = new Intent();
		intent.putExtra(Constants.BARCODE_RESTART_TIME, mBarcodeRestartTime);
		setResult(Activity.RESULT_CANCELED , intent);
		
		ATLog.i(TAG, INFO, "INFO. onBackPressed() - Barcode Restart time [%d]", mBarcodeRestartTime);
		super.onBackPressed();
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.linear_barcode_symbol_state :
			onBarcodeSymbolState();
			break;
		case R.id.linear_postal_codes :
			onBarcodePostalCodes();
			break;
		case R.id.linear_barcode_option :
			onBarcodeOption();
			break;
       	case R.id.linear_barcode_symbol_all_enable :
       		onBarcodeEnableAllSymbol();
       		break;
       	case R.id.linear_barcode_symbol_all_disable :
       		onBarcodeDisableAllSymbol();
       		break;
       	case R.id.linear_barcode_symbol_default :
       		onBarcodeDefaultSymbol();
       		break;
       	case R.id.linear_barcode_symbol_character_set :
       		onBarcodeCharacterSet();
       		break;
       	case R.id.linear_barcode_symbol_restart_time :
       		onBarcodeRestartTime();
       		break;
		}
		
	}
	
	// ------------------------------------------------------------------------
	// Override Reader Event Methods
	// ------------------------------------------------------------------------
	@Override
	public void onReaderStateChanged(ATEAReader reader, ConnectState state,
			Object params) {
		ATLog.i(TAG, INFO, "EVENT. onReaderStateChanged([%s], %s)", reader, state);
		if( state == ConnectState.Disconnected){
			mReader.removeListener(this);
			setResult(Activity.RESULT_FIRST_USER);
			finish();
		}
	}

	@Override
	public void onReaderActionChanged(ATEAReader reader, ResultCode code,
			ActionState action, Object params) {
		ATLog.i(TAG, INFO, "EVENT. onReaderActionChanged([%s], %s, %s)", reader, code, action);
	}

	@Override
	public void onReaderOperationModeChanged(ATEAReader reader,
			OperationMode mode, Object params) {
		ATLog.i(TAG, INFO, "EVENT. onReaderOperationModeChanged([%s], %s)", reader, mode);
	}

	@Override
	public void onReaderBatteryState(ATEAReader reader, int batteryState,
			Object params) {
		ATLog.i(TAG, INFO, "EVENT. onReaderBatteryState([%s], %d)", reader, batteryState);
	}


	@Override
	public void onReaderKeyChanged(ATEAReader reader, KeyType type, KeyState state, 
			Object params) {
		ATLog.i(TAG, INFO, "EVENT. onReaderKeyChanged([%s], %s, %s)", reader, type, state);
	}
	
	// ------------------------------------------------------------------------
	// Internal Widgets Control Methods
	// ------------------------------------------------------------------------
	private void initialize() {
		initActivity();
		
		dlgSymbolState = new SymbolStateDialog();
		dlgPostalCodes = new PostalDialog();
		
		dlgSSI1DBarcodeOption  = new SSI1DBarcodeOptionDialog();
		dlgSSI2DBarcodeOption = new SSI2DBarcodeOptionDialog();
		dlgSPC2DBarcodeOption = new SPC2DBarcodeOptionDialog();
		dlgBarcodeChracterSet = new CharsetDialog();
		dlgBarcodeRestartTime = new NumberUnitDialog(txtValueBarcodeRestartTime, getResources().getString(R.string.unit_ms));
		
		ATLog.i(TAG, INFO, "INFO. initialize()");
	}
	
	private void initActivity() {
		linearNameSymbolState = (LinearLayout) findViewById(R.id.linear_barcode_symbol_state);
		linearNameSymbolState.setOnClickListener(this);
		linearPostalCodes = (LinearLayout) findViewById(R.id.linear_postal_codes);
		linearPostalCodes.setOnClickListener(this);
		mIsPostSelect = mReader.getBarcode().isSuportSelPost();
		if(mIsPostSelect)
			linearPostalCodes.setVisibility(View.VISIBLE);
		else 
			linearPostalCodes.setVisibility(View.GONE);
		
		linearNameSymbolBarcodeOption = (LinearLayout) findViewById(R.id.linear_barcode_option);
		linearNameSymbolBarcodeOption.setOnClickListener(this);
		linearNameSymbolAllEnable = (LinearLayout) findViewById(R.id.linear_barcode_symbol_all_enable);
		linearNameSymbolAllEnable.setOnClickListener(this);
		linearNameSymbolAllDisable = (LinearLayout) findViewById(R.id.linear_barcode_symbol_all_disable);
		linearNameSymbolAllDisable.setOnClickListener(this);
		linearNameSymbolDefault = (LinearLayout) findViewById(R.id.linear_barcode_symbol_default);
		linearNameSymbolDefault.setOnClickListener(this);
		linearNameSymbolCharacterSet = (LinearLayout) findViewById(R.id.linear_barcode_symbol_character_set);
		linearNameSymbolCharacterSet.setOnClickListener(this);
		linearNameBarcodeRestartTime = (LinearLayout) findViewById(R.id.linear_barcode_symbol_restart_time);
		linearNameBarcodeRestartTime.setOnClickListener(this);
		
		txtPostalCodes = (TextView) findViewById(R.id.postal_codes);
		txtValueBarcodeChracterSet = (TextView) findViewById(R.id.value_barcode_symbol_character_set);
		txtValueBarcodeRestartTime = (TextView) findViewById(R.id.value_barcode_symbol_restart_time);
		
		ATLog.i(TAG, INFO, "INFO. initActivity()");
	}
	
	private void enableWidget(final boolean enabled) {
		
		runOnUiThread(new Runnable(){
			@Override
			public void run(){
				linearNameSymbolState.setEnabled(enabled);
				
				linearPostalCodes.setEnabled(enabled);
				
				linearNameSymbolBarcodeOption.setEnabled(enabled);
				
				linearNameSymbolAllEnable.setEnabled(enabled);
				
				linearNameSymbolAllDisable.setEnabled(enabled);
				
				linearNameSymbolDefault.setEnabled(enabled);
				
				linearNameSymbolCharacterSet.setEnabled(enabled);
				
				linearNameBarcodeRestartTime.setEnabled(enabled);
			}
		});
		
		ATLog.i(TAG, INFO, "INFO. enableWidget(%s)", enabled);
	}
	
	// ------------------------------------------------------------------------
	// Set properties
	// ------------------------------------------------------------------------
	private void onBarcodeSymbolState() {
		ATLog.i(TAG, INFO, "INFO. onBarcodeSymbolState()");
		
		if(mReader == null){
			ATLog.e(TAG, "ERROR. onBarcodeSymbolState() - Failed to get reader");
			return;
		}
		
		enableWidget(false);
		
		dlgSymbolState.showDialog(this, new SymbolStateDialog.IValueChangedListener() {
			
			@Override
			public void onValueChanged(SymbolStateDialog dialog) {
				
				asyncWork(R.string.msg_save_symbol, R.string.msg_fail_save_symbol, new ActionWork(){
					@Override
					public boolean onWork() {
					
						ATLog.i(TAG, INFO, "INFO. onBarcodeSymbolState().onValueChanged().onWork()");
						try {
							mReader.getBarcode().setSymbolState(dlgSymbolState.getList());
						} catch (ATException e) {
							ATLog.e(TAG, e,
									"ERROR. onBarcodeSymbolState().onValueChanged().onWork() - Failed to set symbologies state list");
							enableWidget(true);
							return false;
						}
						
//						mIsPostSelect = mReader.getBarcode().isSuportSelPost();
						if(mIsPostSelect) {
							try{
								mReader.getBarcode().setSymbolPostState(dlgPostalCodes.getPostalCodes());
							} catch (ATException e) {
								ATLog.e(TAG, e,
									"ERROR. onBarcodeSymbolState().onValueChanged().onWork() - Failed to set postal symbologies state");
								enableWidget(true);
								return false;
							}
						}
						
						enableWidget(true);
						ATLog.i(TAG, INFO, "INFO. onBarcodeSymbolState().onValueChanged().onWork() - success");
						return true;
					}
				
				});
			};
		}, new BaseDialog.ICancelListener() {
			
			@Override
			public void onCanceled(BaseDialog dialog) {
				enableWidget(true);
				ATLog.i(TAG, INFO, "INFO. onBarcodeSymbolState().onCanceled()");
			}
		});
	}
	
	private void onBarcodePostalCodes() {
		ATLog.i(TAG, INFO, "INFO. onBarcodePostalCodes()");
		
		if(mReader == null){
			ATLog.e(TAG, "ERROR. onBarcodePostalCodes() - Failed to get reader");
			return;
		}
		
		enableWidget(false);
		
		dlgPostalCodes.showDialog(this, R.string.postal_codes, new BaseDialog.IValueChangedListener() {
			
			@Override
			public void onValueChanged(BaseDialog dialog) {
			    
				asyncWork(R.string.msg_save_post_code, R.string.msg_fail_save_post_code, new ActionWork(){
					@Override
					public boolean onWork() {
					
						ATLog.i(TAG, INFO, "INFO. onBarcodePostalCodes().onValueChanged().onWork()");
						
//						mIsPostSelect = mReader.getBarcode().isSuportSelPost();
						if(mIsPostSelect) {
							try{
								mReader.getBarcode().setSymbolPostState(dlgPostalCodes.getPostalCodes());
							} catch (ATException e) {
								ATLog.e(TAG, e,
									"ERROR. onBarcodePostalCodes().onValueChanged().onWork() - Failed to set postal symbologies state");
								enableWidget(true);
								return false;
							}
						}
						runOnUiThread( new Runnable () {
							@Override
							public void run() {
								txtPostalCodes.setText(
										String.format(Locale.US, "%s", dlgPostalCodes.getPostalCodes().toString()));
							}
						});


						enableWidget(true);
						ATLog.i(TAG, INFO, "INFO. onBarcodePostalCodes().onValueChanged().onWork() - success");
						return true;
					}
				
				});
			};
		}, new BaseDialog.ICancelListener() {
			
			@Override
			public void onCanceled(BaseDialog dialog) {
				enableWidget(true);
				ATLog.i(TAG, INFO, "INFO. onBarcodePostalCodes().onCanceled()");
			}
		});
	}
	
	private void onBarcodeOption() {
		ATLog.i(TAG, INFO, "INFO. onBarcodeOption()");
		
		if(mReader == null) {
			ATLog.e(TAG, "ERROR. onBarcodeOption() - Failed to get reader");
			return;
		}
		
		enableWidget(false);
		
		if( mModuleBarcodeType == ModuleBarcodeType.AT1DSE955
				|| mModuleBarcodeType == ModuleBarcodeType.AT1DSE965 ) {
			
			dlgSSI1DBarcodeOption.showDialog(this, new SSI1DBarcodeOptionDialog.IValueChangedListener() {
				
				@Override
				public void onValueChanged(SSI1DBarcodeOptionDialog dialog) {
					
					asyncWork(R.string.msg_save_barcode_option, R.string.msg_fail_save_barcode_option, new ActionWork(){
						@Override
						public boolean onWork(){
							ATLog.i(TAG, INFO, "INFO. onBarcodeOption(SSI1DBarcodeOptionDialog).onValueChanged().onWork()");
							
							if(!saveSSI1DBarcodeParameter()){
								ATLog.e(TAG,
										"ERROR. onBarcodeSymbolState(SSI1DBarcodeOptionDialog).onValueChanged().onWork() - Failed to set barcode option");
								enableWidget(true);
								return false;
							}
							
							enableWidget(true);
							ATLog.i(TAG, INFO, "INFO. onBarcodeOption(SSI1DBarcodeOptionDialog).onValueChanged().onWork() - success");
							return true;
						}
					});
					
				}
			}, new BaseDialog.ICancelListener() {
				
				@Override
				public void onCanceled(BaseDialog dialog) {
					
					dlgSSI1DBarcodeOption.restoreParameterValue();
					
					enableWidget(true);
					ATLog.i(TAG, INFO, "INFO. onBarcodeOption().onCanceled()");
				}
			});
			
		} else if(mModuleBarcodeType == ModuleBarcodeType.AT2DSE4710 ) {
			
			dlgSSI2DBarcodeOption.showDialog(this, new SSI2DBarcodeOptionDialog.IValueChangedListener() {
				
				@Override
				public void onValueChanged(SSI2DBarcodeOptionDialog dialog) {
					
					asyncWork(R.string.msg_save_barcode_option, R.string.msg_fail_save_barcode_option, new ActionWork(){
						@Override
						public boolean onWork(){
							ATLog.i(TAG, INFO, "INFO. onBarcodeOption(SSI2DBarcodeOptionDialog).onValueChanged().onWork()");
							
							if(!saveSSI2DBarcodeParameter()){
								ATLog.e(TAG,
										"ERROR. onBarcodeSymbolState(SSI2DBarcodeOptionDialog).onValueChanged().onWork() - Failed to set barcode option");
								enableWidget(true);
								return false;
							}
							
							enableWidget(true);
							ATLog.i(TAG, INFO, "INFO. onBarcodeOption(SSI2DBarcodeOptionDialog).onValueChanged().onWork() - success");
							return true;
						}
					});
					
				}
			}, new BaseDialog.ICancelListener() {
				
				@Override
				public void onCanceled(BaseDialog dialog) {
					
					dlgSSI2DBarcodeOption.restoreParameterValue();
					
					enableWidget(true);
					ATLog.i(TAG, INFO, "INFO. onBarcodeOption().onCanceled()");
				}
			});
			
		} else if(mModuleBarcodeType == ModuleBarcodeType.AT2DN368X) {
			
			dlgSPC2DBarcodeOption.showDialog(this, new SPC2DBarcodeOptionDialog.IValueChangedListener() {
				
				@Override
				public void onValueChanged(SPC2DBarcodeOptionDialog dialog) {

					asyncWork(R.string.msg_save_barcode_option, R.string.msg_fail_save_barcode_option, new ActionWork(){

						@Override
						public boolean onWork() {

							if(!saveSPC2DBarcodeParameter()){
								ATLog.e(TAG,
										"ERROR. onBarcodeSymbolState(SPC2DBarcodeOptionDialog).onValueChanged().onWork() - Failed to set barcode option");
								enableWidget(true);
								return false;
							}
							enableWidget(true);
							ATLog.i(TAG, INFO, "INFO. onBarcodeOption(SPC2DBarcodeOptionDialog).onValueChanged().onWork() - success");
							return true;
							
						}
					});
				}
			}, new BaseDialog.ICancelListener() {
				
				@Override
				public void onCanceled(BaseDialog dialog) {
					
					dlgSPC2DBarcodeOption.restoreParameterValue();
					
					enableWidget(true);
					ATLog.i(TAG, INFO, "INFO. onBarcodeOption(SPC2DBarcodeOptionDialog).onCanceled()");
				}
			});
			
		} else {
			enableWidget(true);
		}
		
	}
	
	private void onBarcodeEnableAllSymbol() {
		ATLog.i(TAG, INFO, "INFO. onBarcodeEnableAllSymbol()");
		
		if(mReader == null){
			ATLog.e(TAG, "ERROR. onBarcodeEnableAllSymbol() - Failed to get reader");
			return;
		}
		
		enableWidget(false);
		
		asyncWork(R.string.msg_enable_all_symbol, R.string.msg_fail_enable_all_symbol, new ActionWork(){
			@Override
			public boolean onWork() {
				
				ATLog.i(TAG, INFO, "INFO. onBarcodeEnableAllSymbol().onWork()");
				
				try {
					mReader.getBarcode().enableAllSymbol(true);
				} catch (ATException e) {
					ATLog.e(TAG, e, "ERROR. onBarcodeEnableAllSymbol().onWork() - Failed to enabled all symbologies");
					enableWidget(true);
					return false;
				}
				
				// Get Barcode Symbol List
				try {
					dlgSymbolState.setList(mReader.getBarcode().getSymbolState());
				} catch (ATException e) {
					ATLog.e(TAG, e, "ERROR. onBarcodeEnableAllSymbol().onWork() - Failed to get symbologies state list");
					enableWidget(true);
					return false;
				}
				ATLog.i(TAG, INFO, "INFO. onBarcodeEnableAllSymbol().onWork() - success");
				enableWidget(true);
				return true;
			}
		});
		
		
	}
	
	private void onBarcodeDisableAllSymbol() {
		ATLog.i(TAG, INFO, "INFO. onBarcodeDisableAllSymbol()");
		
		if(mReader == null){
			ATLog.e(TAG, "ERROR. onBarcodeDisableAllSymbol() - Failed to get reader");
			return;
		}
		enableWidget(false);
		asyncWork(R.string.msg_disable_all_symbol, R.string.msg_fail_disable_all_symbol, new ActionWork() {
			@Override
			public boolean onWork() {
				ATLog.i(TAG, INFO, "INFO. onBarcodeDisableAllSymbol().onWork()");
				
				try {
					mReader.getBarcode().enableAllSymbol(false);
				} catch (ATException e) {
					ATLog.e(TAG, e, "ERROR. onBarcodeDisableAllSymbol().onWork() - Failed to disabled all symbologies");
					enableWidget(true);
					return false;
				}
				// Get Barcode Symbol List
				try {
					dlgSymbolState.setList(mReader.getBarcode().getSymbolState());
				} catch (ATException e) {
					ATLog.e(TAG, e, "ERROR. onBarcodeDisableAllSymbol().onWork() - Failed to get symbologies state list");
					enableWidget(true);
					return false;
				}
				
				ATLog.i(TAG, INFO, "INFO. onBarcodeDisableAllSymbol().onWork() - success");
				enableWidget(true);
				return true;
			}
		});
	}
	
	private void onBarcodeDefaultSymbol() {
		ATLog.i(TAG, INFO, "INFO. onBarcodeDefaultSymbol()");
		
		if(mReader == null){
			ATLog.e(TAG, "ERROR. onBarcodeDefaultSymbol() - Failed to get reader");
			return;
		}
		enableWidget(false);
		asyncWork(R.string.msg_default_symbol, R.string.msg_fail_default_symbol, new ActionWork() {
			@Override
			public boolean onWork() {
				
				ATLog.i(TAG, INFO, "INFO. onBarcodeDisableAllSymbol().onWork()");
				
				try {
					mReader.getBarcode().defaultSymbol();
				} catch (ATException e) {
					ATLog.e(TAG, e, "ERROR. onBarcodeDefaultSymbol().onWork() - Failed to default all symbologies");
					enableWidget(true);
					return false;
				}
				// Get Barcode Symbol List
				try {
					dlgSymbolState.setList(mReader.getBarcode().getSymbolState());
				} catch (ATException e) {
					ATLog.e(TAG, e, "ERROR. onBarcodeDefaultSymbol().onWork() - Failed to get symbologies state list");
					enableWidget(true);
					return false;
				}
				
				mIsThreadAlive = true;

				if(mModuleBarcodeType == ModuleBarcodeType.AT2DSE4710 ) {
					try {
						if(!loadSSI2DBarcodeParameter()) {
							ATLog.e(TAG, "ERROR. onBarcodeDefaultSymbol().onWork() - cancel to get barcode option");
							enableWidget(true);
							mIsThreadAlive = false;
							return false;
						}
					} catch (ATException e) {
						ATLog.e(TAG, "ERROR. onBarcodeDefaultSymbol().onWork() - Failed to get barcode option");
						BarcodeOptionActivity.this.runOnUiThread(mFailedLoadProc);
						enableWidget(true);
						mIsThreadAlive = false;
						return false;

					}

				} else if(mModuleBarcodeType == ModuleBarcodeType.AT2DN368X) {
					try {
						if(!loadSPC2DBarcodeParameter()) {
							ATLog.e(TAG, "ERROR. onBarcodeDefaultSymbol().onWork() - cancel to get barcode option");
							enableWidget(true);
							mIsThreadAlive = false;
							return false;
						}
					} catch (ATException e) {
						ATLog.e(TAG, "ERROR. onBarcodeDefaultSymbol().onWork() - Failed to get barcode option");
						BarcodeOptionActivity.this.runOnUiThread(mFailedLoadProc);
						enableWidget(true);
						mIsThreadAlive = false;
						return false;

					}
				}
				
				mIsThreadAlive = false;
				
				try {
					//GlobalData.BarcodeCharset = mReader.getBarcode().getCharset();
					
					Charset charset = mReader.getBarcode().getCharset();
					dlgBarcodeChracterSet.setValue(charset.name());
					GlobalData.putConfig(mReader.getDeviceType(), mReader.getAddress(), GlobalData.KEY_BARCODE_CHARSET, charset);
					
					runOnUiThread(new Runnable(){
						@Override
						public void run() {
							txtValueBarcodeChracterSet.setText(
									String.format(Locale.US, "%s", dlgBarcodeChracterSet.getValue()));
						}
					});
				} catch (ATException e) {
					ATLog.e(TAG, e, "ERROR. onCharset() - Failed to get Barcode Character Set [%s]",
							dlgBarcodeChracterSet.getValue());
					
					enableWidget(true);
					return false;
				}
				
				enableWidget(true);
				
				ATLog.i(TAG, INFO, "INFO. onBarcodeDefaultSymbol().onWork() - success");
				return true;
			}
			
		});
	}

	private void onBarcodeCharacterSet() {
		if (mReader.getBarcode() == null)
			return;
		
		enableWidget(false);
		dlgBarcodeChracterSet.showDialog(this, R.string.symbol_character_set, new BaseDialog.IValueChangedListener() {

			@Override
			public void onValueChanged(BaseDialog dialog) {

				ATLog.i(TAG, INFO, "INFO. onBarcodeCharacterSet().onValueChanged()");
				
				try {
					mReader.getBarcode().setCharset(Charset.forName(dlgBarcodeChracterSet.getValue()));
				} catch (ATException e) {
					ATLog.e(TAG, e, "ERROR. onBarcodeCharacterSet() - Failed to set Barcode Character Set [%s]",
							dlgBarcodeChracterSet.getValue());
					showMessage(R.string.msg_fail_save_charset);
					enableWidget(true);
					return;
				}
				
				try {
					//GlobalData.BarcodeCharset = mReader.getBarcode().getCharset();
					Charset charset = mReader.getBarcode().getCharset();
					GlobalData.putConfig(mReader.getDeviceType(), mReader.getAddress(), GlobalData.KEY_BARCODE_CHARSET, charset);

				} catch (ATException e) {
					ATLog.e(TAG, e, "ERROR. onBarcodeCharacterSet() - Failed to get Barcode Character Set [%s]",
							dlgBarcodeChracterSet.getValue());
					showMessage(R.string.msg_fail_load_charset);
					enableWidget(true);
					return;
				}

				runOnUiThread( new Runnable () {
					@Override
					public void run() {
						txtValueBarcodeChracterSet.setText(
								String.format(Locale.US, "%s", dlgBarcodeChracterSet.getValue()));
					}
				});

				
				enableWidget(true);
			}
		}, new BaseDialog.ICancelListener() {
			
			@Override
			public void onCanceled(BaseDialog dialog) {
				enableWidget(true);
			}
		});
	}
	
	
	private void onBarcodeRestartTime() {
		if (mReader.getBarcode() == null)
			return;
		
		enableWidget(false);
		dlgBarcodeRestartTime.showDialog(this, R.string.restart_time, new BaseDialog.IValueChangedListener() {

			@Override
			public void onValueChanged(BaseDialog dialog) {

				mBarcodeRestartTime = dlgBarcodeRestartTime.getValue();
				
				ATLog.i(TAG, INFO, "INFO. onBarcodeRestartTime().onValueChanged() - Restart Time[%d]", mBarcodeRestartTime);
				
				enableWidget(true);
			}
		}, new BaseDialog.ICancelListener() {
			
			@Override
			public void onCanceled(BaseDialog dialog) {
				enableWidget(true);
			}
		});
	}
	
	private void asyncWork(final int message , final int messageFail , final ActionWork work) {

		WaitDialog.show(this, message);
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				final boolean result = work.onWork();
				runOnUiThread( new Runnable() {
					@Override
					public void run() {
						WaitDialog.hide();
						if(!result) {
							MessageBox.show(BarcodeOptionActivity.this, messageFail);
							
						}
					}
				});
			}
		}).start();
	}
	
	private interface ActionWork {
		boolean onWork();
	}
	
	private void showMessage(final int message) {
		runOnUiThread( new Runnable() {
			@Override
			public void run() {
				MessageBox.show(BarcodeOptionActivity.this, message);
			}
		});
		
	}
	
	// ------------------------------------------------------------------------
	// Load properties
	// ------------------------------------------------------------------------
	private boolean loadSSI1DBarcodeParameter() throws ATException {
		byte[] data = null;
		SSI1DParamNameList paramNameList = null;
		SSI1DParamValueList paramValueList = null;

		// 
		// UPC/EAN  
		// 
		if(mIsThreadAlive){

			paramNameList = new SSI1DParamNameList ( new SSI1DParamName[] {
					SSI1DParamName.Bookland_ISBN_Format,
					SSI1DParamName.Decode_UPC_EAN_JAN_Supplementals,
					SSI1DParamName.Decode_UPC_EAN_JAN_Supplemental_Redundancy,
					SSI1DParamName.Transmit_UPC_A_Check_Digit,
					SSI1DParamName.Transmit_UPC_E_Check_Digit,
					SSI1DParamName.Transmit_UPC_E1_Check_Digit,
					SSI1DParamName.UPC_A_Preamble,
					SSI1DParamName.UPC_E_Preamble,
					SSI1DParamName.UPC_E1_Preamble,
					SSI1DParamName.Convert_UPC_E_to_UPC_A,
					SSI1DParamName.Convert_UPC_E1_to_UPC_A,
					SSI1DParamName.EAN_8_JAN_8_Extend,
					SSI1DParamName.UPC_EAN_Security_Level,
					SSI1DParamName.Coupon_Report,
					});
			
			try {
				data = mReader.getBarcode().getBarcodeParam( paramNameList.getBytes(mModuleBarcodeType.getCode()));	
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSSI1DBarcodeParameter() - Failed to load Barcode Option (UPC/EAN) , Pamram List");
				throw e;
			}
						
			try {
				paramValueList = SSI1DParamValueList.parseParams(data, mModuleBarcodeType.getCode());
				
				dlgSSI1DBarcodeOption.setBooklandISBNFormat(
						(BooklandISBNFormat)paramValueList.getValueAt(SSI1DParamName.Bookland_ISBN_Format));
				dlgSSI1DBarcodeOption.setDecodeUpcEanJanSupplementals(
						(DecodeUpcEanJanSupplementals)paramValueList.getValueAt(SSI1DParamName.Decode_UPC_EAN_JAN_Supplementals));
				dlgSSI1DBarcodeOption.setUpcEanJanSupplementalRedundancy(
						(Integer)paramValueList.getValueAt(SSI1DParamName.Decode_UPC_EAN_JAN_Supplemental_Redundancy));
				dlgSSI1DBarcodeOption.setTransmitUpcACheckDigit(
						(Boolean)paramValueList.getValueAt(SSI1DParamName.Transmit_UPC_A_Check_Digit));
				dlgSSI1DBarcodeOption.setTransmitUpcECheckDigit(
						(Boolean)paramValueList.getValueAt(SSI1DParamName.Transmit_UPC_E_Check_Digit));
				dlgSSI1DBarcodeOption.setTransmitUpcE1CheckDigit(
						(Boolean)paramValueList.getValueAt(SSI1DParamName.Transmit_UPC_E1_Check_Digit));
				dlgSSI1DBarcodeOption.setUpcAPreamble(
						(Preamble)paramValueList.getValueAt(SSI1DParamName.UPC_A_Preamble));
				dlgSSI1DBarcodeOption.setUpcEPreamble(
						(Preamble)paramValueList.getValueAt(SSI1DParamName.UPC_E_Preamble));
				dlgSSI1DBarcodeOption.setUpcE1Preamble(
						(Preamble)paramValueList.getValueAt(SSI1DParamName.UPC_E1_Preamble));
				dlgSSI1DBarcodeOption.setConvertUpcEToUpcA(
						(Boolean)paramValueList.getValueAt(SSI1DParamName.Convert_UPC_E_to_UPC_A));
				dlgSSI1DBarcodeOption.setConvertUpcE1ToUpcA(
						(Boolean)paramValueList.getValueAt(SSI1DParamName.Convert_UPC_E1_to_UPC_A));
				dlgSSI1DBarcodeOption.setEan8Jan8Extend(
						(Boolean)paramValueList.getValueAt(SSI1DParamName.EAN_8_JAN_8_Extend));
				dlgSSI1DBarcodeOption.setUpcEanSecurityLevel(
						(UpcEanSecurityLevel)paramValueList.getValueAt(SSI1DParamName.UPC_EAN_Security_Level));
				dlgSSI1DBarcodeOption.setCouponReport(
						(CouponReport)paramValueList.getValueAt(SSI1DParamName.Coupon_Report));
				
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSSI1DBarcodeParameter() - Failed to load Barcode Option (UPC/EAN) , Parsing Error");
				throw e;
			}

		} else {
			return false;
		}
		
		// 
		// Code128 
		// 
		if(mIsThreadAlive){
			
			paramNameList = new SSI1DParamNameList( new SSI1DParamName[]{
					SSI1DParamName.Set_Length_1_Code128,
					SSI1DParamName.Set_Length_2_Code128,
					SSI1DParamName.ISBT_Concatenation,
					SSI1DParamName.Check_ISBT_Table,
					SSI1DParamName.ISBT_Concatenation_Redundancy,
			});
			
			try {
				data = mReader.getBarcode().getBarcodeParam( paramNameList.getBytes(mModuleBarcodeType.getCode()));
				
			} catch (ATException e) {
				ATLog.e(TAG, e, "ERROR. loadSSI1DBarcodeParameter() - Failed to load Barcode Option (Code128) , Pamram List");
				throw e;
			}
			
			try {
				paramValueList = SSI1DParamValueList.parseParams(data, mModuleBarcodeType.getCode());
				int length1 = (Integer) paramValueList.getValueAt(SSI1DParamName.Set_Length_1_Code128);
				int length2 = (Integer) paramValueList.getValueAt(SSI1DParamName.Set_Length_2_Code128);
				dlgSSI1DBarcodeOption.setCode128Length(new CodeLength(length1, length2));
				dlgSSI1DBarcodeOption.setIsbtConcatenation(
						(ISBTConcatenation) paramValueList.getValueAt(SSI1DParamName.ISBT_Concatenation));
				dlgSSI1DBarcodeOption.setCheckIsbtTable(
						(Boolean) paramValueList.getValueAt(SSI1DParamName.Check_ISBT_Table));
				dlgSSI1DBarcodeOption.setIsbtConcatenationRedundancy(
						(Integer) paramValueList.getValueAt(SSI1DParamName.ISBT_Concatenation_Redundancy));

			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSSI1DBarcodeParameter() - Failed to load Barcode Option (Code128) , Parsing Error");
				throw e;
			}
			
		} else {
			return false;
		}
		
		// 
		// code39
		// 
		if(mIsThreadAlive){
			
			paramNameList = new SSI1DParamNameList( new SSI1DParamName[] {
					SSI1DParamName.Code32_Prefix,
					SSI1DParamName.Set_Length_1_Code39,
					SSI1DParamName.Set_Length_2_Code39,
					SSI1DParamName.Code39_Check_Digit_Verification,
					SSI1DParamName.Transmit_Code39_Check_Digit,
					SSI1DParamName.Code39_Full_Ascii_Conversion,
			});
			
			try {
				data = mReader.getBarcode().getBarcodeParam( paramNameList.getBytes(mModuleBarcodeType.getCode()));
				
			} catch (ATException e){
				ATLog.e(TAG, e,"ERROR. loadSSI1DBarcodeParameter() - Failed to load Barcode Option (Code39) , Pamram List");
				throw e;
			}
			
			try {
				paramValueList = SSI1DParamValueList.parseParams(data, mModuleBarcodeType.getCode());
				dlgSSI1DBarcodeOption.setCode32Prefix(
						(Boolean) paramValueList.getValueAt(SSI1DParamName.Code32_Prefix));
				int length1 = (Integer) paramValueList.getValueAt(SSI1DParamName.Set_Length_1_Code39);
				int length2 = (Integer) paramValueList.getValueAt(SSI1DParamName.Set_Length_2_Code39);
				dlgSSI1DBarcodeOption.setCode39Length(new CodeLength(length1, length2));
				dlgSSI1DBarcodeOption.setCode39CheckDigitVerification(
						(Boolean) paramValueList.getValueAt(SSI1DParamName.Code39_Check_Digit_Verification));
				dlgSSI1DBarcodeOption.setTransmitCode39CheckDigit(
						(Boolean) paramValueList.getValueAt(SSI1DParamName.Transmit_Code39_Check_Digit));
				dlgSSI1DBarcodeOption.setCode39FullAsciiConversion(
						(Boolean) paramValueList.getValueAt(SSI1DParamName.Code39_Full_Ascii_Conversion));

			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSSI1DBarcodeParameter() - Failed to load Barcode Option (Code39) , Parsing Error");
				throw e;
			}
			
		} else {
			return false;
		}
		
		// 
		// code93
		// 
		if(mIsThreadAlive){
			
			paramNameList = new SSI1DParamNameList( new SSI1DParamName[] {
				SSI1DParamName.Set_Length_1_Code93,
				SSI1DParamName.Set_Lenght_2_Code93
			});
			
			try {
				data = mReader.getBarcode().getBarcodeParam( paramNameList.getBytes(mModuleBarcodeType.getCode()));
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSSI1DBarcodeParameter() - Failed to load Barcode Option (Code93) , Pamram List");
				throw e;
			}
			
			try {
				paramValueList = SSI1DParamValueList.parseParams(data, mModuleBarcodeType.getCode());
				int length1 = (Integer) paramValueList.getValueAt(SSI1DParamName.Set_Length_1_Code93);
				int length2 = (Integer) paramValueList.getValueAt(SSI1DParamName.Set_Lenght_2_Code93);
				dlgSSI1DBarcodeOption.setCode93Length( new CodeLength(length1, length2));
				
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSSI1DBarcodeParameter() - Failed to load Barcode Option (Code93) , Parsing Error");
				throw e;
			} 
		} else {
			return false;
		}
		
		// 
		// Code11
		// 
		if(mIsThreadAlive){
			
			paramNameList = new SSI1DParamNameList( new SSI1DParamName[]{
					SSI1DParamName.Set_Length_1_Code11,
					SSI1DParamName.Set_Length_2_Code11,
					SSI1DParamName.Code11_Check_Digit_Verification,
					SSI1DParamName.Transmit_Code_11_Check_Digits
			});
			
			try {
				data = mReader.getBarcode().getBarcodeParam( paramNameList.getBytes(mModuleBarcodeType.getCode()));
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSSI1DBarcodeParameter() - Failed to load Barcode Option (Code11) , Pamram List");
				throw e;
			}
			
			try {
				paramValueList = SSI1DParamValueList.parseParams(data, mModuleBarcodeType.getCode());
				int length1 = (Integer) paramValueList.getValueAt(SSI1DParamName.Set_Length_1_Code11);
				int length2 = (Integer) paramValueList.getValueAt(SSI1DParamName.Set_Length_2_Code11);
				dlgSSI1DBarcodeOption.setCode11Length( new CodeLength(length1, length2));
				dlgSSI1DBarcodeOption.setCode11CheckDigitVerification(
						(Code11CheckDigitVerification) paramValueList.getValueAt(SSI1DParamName.Code11_Check_Digit_Verification));
				dlgSSI1DBarcodeOption.setTransmitCode11CheckDigit(
						(Boolean) paramValueList.getValueAt(SSI1DParamName.Transmit_Code_11_Check_Digits));
				
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSSI1DBarcodeParameter() - Failed to load Barcode Option (Code11) , Parsing Error");
				throw e;
			}
		} else {
			return false;
		}
		// 
		// I2of5
		// 
		if(mIsThreadAlive){
			
			paramNameList = new SSI1DParamNameList( new SSI1DParamName[]{
				SSI1DParamName.Set_Length_1_I2of5,
				SSI1DParamName.Set_Length_2_I2of5,
				SSI1DParamName.I2of5_Check_Digit_Verification,
				SSI1DParamName.Transmit_I2of5_Check_Digit,
				SSI1DParamName.Convert_I2of5_to_EAN_13,
			});
			
			try {
				data = mReader.getBarcode().getBarcodeParam( paramNameList.getBytes(mModuleBarcodeType.getCode()));
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSSI1DBarcodeParameter() - Failed to load Barcode Option (I2of5) , Pamram List");
				throw e;
			}
			
			try {
				paramValueList = SSI1DParamValueList.parseParams(data, mModuleBarcodeType.getCode());
				int length1 = (Integer) paramValueList.getValueAt(SSI1DParamName.Set_Length_1_I2of5);
				int length2 = (Integer) paramValueList.getValueAt(SSI1DParamName.Set_Length_2_I2of5);
				dlgSSI1DBarcodeOption.setI2of5Length( new CodeLength(length1, length2));
				dlgSSI1DBarcodeOption.setI2of5CheckDigitVerification(
						(I2of5CheckDigitVerification) paramValueList.getValueAt(SSI1DParamName.I2of5_Check_Digit_Verification));
				dlgSSI1DBarcodeOption.setTransmitI2of5CheckDigit(
						(Boolean) paramValueList.getValueAt(SSI1DParamName.Transmit_I2of5_Check_Digit));
				dlgSSI1DBarcodeOption.setConvertI2of5ToEan13(
						(Boolean) paramValueList.getValueAt(SSI1DParamName.Convert_I2of5_to_EAN_13));
				
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSSI1DBarcodeParameter() - Failed to load Barcode Option (I2of5) , Parsing Error");
				throw e;
			}
			
		} else {
			return false;
		}
		// 
		// D2of5
		// 
		if(mIsThreadAlive){
			
			paramNameList = new SSI1DParamNameList( new SSI1DParamName[]{
					SSI1DParamName.Set_Length_1_D2of5,
					SSI1DParamName.Set_Length_2_D2of5
				});
			
			try {
				data = mReader.getBarcode().getBarcodeParam( paramNameList.getBytes(mModuleBarcodeType.getCode()));
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSSI1DBarcodeParameter() - Failed to load Barcode Option (D2of5) , Pamram List");
				throw e;
			}
			
			try {
				paramValueList = SSI1DParamValueList.parseParams(data, mModuleBarcodeType.getCode());
				int length1 = (Integer) paramValueList.getValueAt(SSI1DParamName.Set_Length_1_D2of5);
				int length2 = (Integer) paramValueList.getValueAt(SSI1DParamName.Set_Length_2_D2of5);
				dlgSSI1DBarcodeOption.setD2of5Length( new CodeLength(length1, length2));

			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSSI1DBarcodeParameter() - Failed to load Barcode Option (D2of5) , Parsing Error");
				throw e;
			}
			
		} else {
			return false;
		}
		// 
		// Codabar
		// 
		if(mIsThreadAlive) {
			
			paramNameList = new SSI1DParamNameList( new SSI1DParamName[]{
				SSI1DParamName.Set_Length_1_Codabar,
				SSI1DParamName.Set_Length_2_Codabar,
				SSI1DParamName.CLSI_Editing,
				SSI1DParamName.NOTIS_Editing,
				SSI1DParamName.Codabar_Upper_Lower_Start_Stop_Characters_Dectection
			});
			
			try {
				data = mReader.getBarcode().getBarcodeParam( paramNameList.getBytes(mModuleBarcodeType.getCode()));
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSSI1DBarcodeParameter() - Failed to load Barcode Option (Codabar) , Pamram List");
				throw e;
			}
			
			try {
				paramValueList = SSI1DParamValueList.parseParams(data, mModuleBarcodeType.getCode());
				int length1 = (Integer) paramValueList.getValueAt(SSI1DParamName.Set_Length_1_Codabar);
				int length2 = (Integer) paramValueList.getValueAt(SSI1DParamName.Set_Length_2_Codabar);
				dlgSSI1DBarcodeOption.setCodabarLength( new CodeLength(length1, length2));
				dlgSSI1DBarcodeOption.setClsiEditing(
						(Boolean) paramValueList.getValueAt(SSI1DParamName.CLSI_Editing));
				dlgSSI1DBarcodeOption.setNotisEditing(
						(Boolean) paramValueList.getValueAt(SSI1DParamName.NOTIS_Editing));
				dlgSSI1DBarcodeOption.setCodabarStartStopCharactersDetection((CodabarStartStopCharactersDetection)
						paramValueList.getValueAt(SSI1DParamName.Codabar_Upper_Lower_Start_Stop_Characters_Dectection));
				
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSSI1DBarcodeParameter() - Failed to load Barcode Option (Codabar) , Parsing Error");
				throw e;
			}
			
		} else {
			return false;
		}
		// 
		// MSI
		// 
		if(mIsThreadAlive){
			
			paramNameList = new SSI1DParamNameList( new SSI1DParamName[]{
				SSI1DParamName.Set_Length_1_MSI,
				SSI1DParamName.Set_Length_2_MSI,
				SSI1DParamName.MSI_Check_Digits,
				SSI1DParamName.Transmit_MSI_Check_Digits,
				SSI1DParamName.MSI_Check_Digit_Algorithm,
			});
			
			try {
				data = mReader.getBarcode().getBarcodeParam( paramNameList.getBytes(mModuleBarcodeType.getCode()));
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSSI1DBarcodeParameter() - Failed to load Barcode Option (MSI) , Pamram List");
				throw e;
			}
			
			try {
				paramValueList = SSI1DParamValueList.parseParams(data, mModuleBarcodeType.getCode());
				int length1 = (Integer) paramValueList.getValueAt(SSI1DParamName.Set_Length_1_MSI);
				int length2 = (Integer) paramValueList.getValueAt(SSI1DParamName.Set_Length_2_MSI);
				dlgSSI1DBarcodeOption.setMsiLength( new CodeLength(length1, length2));
				dlgSSI1DBarcodeOption.setMSICheckDigits(
						(MSICheckDigits) paramValueList.getValueAt(SSI1DParamName.MSI_Check_Digits));
				dlgSSI1DBarcodeOption.setTransmitMsiCheckDigit(
						(Boolean) paramValueList.getValueAt(SSI1DParamName.Transmit_MSI_Check_Digits));
				dlgSSI1DBarcodeOption.setMSICheckDigitAlgorithm(
						(MSICheckDigitAlgorithm) paramValueList.getValueAt(SSI1DParamName.MSI_Check_Digit_Algorithm));
				
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSSI1DBarcodeParameter() - Failed to load Barcode Option (MSI) , Parsing Error");
				throw e;
			}
		} else {
			return false;
		}
		// 
		// Matrix 2of5
		// 
		if(mIsThreadAlive){
			
			paramNameList = new SSI1DParamNameList( new SSI1DParamName[]{
					SSI1DParamName.Set_Length_1_Matrix2of5,
					SSI1DParamName.Set_Length_2_Matrix2of5,
					SSI1DParamName.Matrix2of5_Check_Digit,
					SSI1DParamName.Transmit_Matrix2of5_Check_Digit,
				});
			
			try {
				data = mReader.getBarcode().getBarcodeParam( paramNameList.getBytes(mModuleBarcodeType.getCode()));
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSSI1DBarcodeParameter() - Failed to load Barcode Option (Matrix 2of5) , Pamram List");
				throw e;
			}
			
			try {
				paramValueList = SSI1DParamValueList.parseParams(data, mModuleBarcodeType.getCode());
				int length1 = (Integer) paramValueList.getValueAt(SSI1DParamName.Set_Length_1_Matrix2of5);
				int length2 = (Integer) paramValueList.getValueAt(SSI1DParamName.Set_Length_2_Matrix2of5);
				dlgSSI1DBarcodeOption.setMatrix2of5Length( new CodeLength(length1, length2));
				dlgSSI1DBarcodeOption.setMatrix2of5CheckDigit(
						(Boolean) paramValueList.getValueAt(SSI1DParamName.Matrix2of5_Check_Digit));
				dlgSSI1DBarcodeOption.setTransmitMatrix2of5CheckDigit(
						(Boolean) paramValueList.getValueAt(SSI1DParamName.Transmit_Matrix2of5_Check_Digit));
				
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSSI1DBarcodeParameter() - Failed to load Barcode Option (Matrix 2of5) , Parsing Error");
				throw e;
			}
		} else {
			return false;
		}
		
		// 
		// GS1 Databar
		// 

		if(mIsThreadAlive){
			
			paramNameList = new SSI1DParamNameList( new SSI1DParamName[]{
					SSI1DParamName.GS1_DataBar_Limited_Security_Level,
					SSI1DParamName.Convert_GS1_DataBar_to_UPC_EAN
				});
			
			try {
				data = mReader.getBarcode().getBarcodeParam(paramNameList.getBytes(mModuleBarcodeType.getCode()));
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSSI1DBarcodeParameter() - Failed to load Barcode Option (GS1 Databar) , Pamram List");
				throw e;
			}
			
			try {
				paramValueList = SSI1DParamValueList.parseParams(data, mModuleBarcodeType.getCode());
				dlgSSI1DBarcodeOption.setGs1DatabarLimitedSecurityLevel((GS1DataBarLimitedSecurityLevel)
						 paramValueList.getValueAt(SSI1DParamName.GS1_DataBar_Limited_Security_Level));
				dlgSSI1DBarcodeOption.setConvertGs1DatabarToUpcEan(
						(Boolean) paramValueList.getValueAt(SSI1DParamName.Convert_GS1_DataBar_to_UPC_EAN));
				
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSSI1DBarcodeParameter() - Failed to load Barcode Option (GS1 Databar) , Parsing Error");
				throw e;
			}
		} else {
			return false;
		}


		// 
		// Inverse 1D
		// Redundancy Level
		// Security Level
		// 
		if(mIsThreadAlive){
			
			paramNameList = new SSI1DParamNameList( new SSI1DParamName[]{
					SSI1DParamName.Bi_Directional_Redundancy,
					SSI1DParamName.Linear_Security_Level
				});
			
			try {
				data = mReader.getBarcode().getBarcodeParam(paramNameList.getBytes(mModuleBarcodeType.getCode()));
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSSI1DBarcodeParameter() - Failed to load Barcode Option (ETC) , Pamram List");
				throw e;
			}
			
			try {
				paramValueList = SSI1DParamValueList.parseParams(data, mModuleBarcodeType.getCode());
				dlgSSI1DBarcodeOption.setBiDirectionalRedundancy(
						(Boolean) paramValueList.getValueAt(SSI1DParamName.Bi_Directional_Redundancy));
				dlgSSI1DBarcodeOption.setSecurityLevel(
						(LinearSecurityLevel) paramValueList.getValueAt(SSI1DParamName.Linear_Security_Level));
				
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSSI1DBarcodeParameter() - Failed to load Barcode Option (ETC) , Parsing Error");
				throw e;
			}
		} else {
			return false;
		}

		ATLog.i(TAG, INFO, "INFO. loadSSI1DBarcodeParameter()");
		return true;
	}
	
	private boolean saveSSI1DBarcodeParameter() {
		
		SSI1DParamValueList paramValueList = null;
		SSI1DParamValue[] paramValue = null;
		CodeLength length = null;
		
		//
		// UPC/EAN
		//
		paramValue = new SSI1DParamValue[]{
			new SSI1DParamValue(SSI1DParamName.Bookland_ISBN_Format , 
					dlgSSI1DBarcodeOption.getBooklandISBNFormat()),
			new SSI1DParamValue(SSI1DParamName.Decode_UPC_EAN_JAN_Supplementals , 
					dlgSSI1DBarcodeOption.getDecodeUpcEanJanSupplementals()),
			new SSI1DParamValue(SSI1DParamName.Decode_UPC_EAN_JAN_Supplemental_Redundancy , 
					dlgSSI1DBarcodeOption.getUpcEanJanSupplementalRedundancy()),
			new SSI1DParamValue(SSI1DParamName.Transmit_UPC_A_Check_Digit , 
					dlgSSI1DBarcodeOption.getTransmitUpcACheckDigit()),
			new SSI1DParamValue(SSI1DParamName.Transmit_UPC_E_Check_Digit , 
					dlgSSI1DBarcodeOption.getTransmitUpcECheckDigit()),
			new SSI1DParamValue(SSI1DParamName.Transmit_UPC_E1_Check_Digit , 
					dlgSSI1DBarcodeOption.getTransmitUpcE1CheckDigit()),
			new SSI1DParamValue(SSI1DParamName.UPC_A_Preamble , 
					dlgSSI1DBarcodeOption.getUpcAPreamble()),
			new SSI1DParamValue(SSI1DParamName.UPC_E_Preamble , 
					dlgSSI1DBarcodeOption.getUpcEPreamble()),
			new SSI1DParamValue(SSI1DParamName.UPC_E1_Preamble , 
					dlgSSI1DBarcodeOption.getUpcE1Preamble()),
			new SSI1DParamValue(SSI1DParamName.Convert_UPC_E_to_UPC_A , 
					dlgSSI1DBarcodeOption.getConvertUpcEToUpcA()),
			new SSI1DParamValue(SSI1DParamName.Convert_UPC_E1_to_UPC_A , 
					dlgSSI1DBarcodeOption.getConvertUpcE1ToUpcA()),
			new SSI1DParamValue(SSI1DParamName.EAN_8_JAN_8_Extend , 
					dlgSSI1DBarcodeOption.getEan8Jan8Extend()),
			new SSI1DParamValue(SSI1DParamName.UPC_EAN_Security_Level , 
					dlgSSI1DBarcodeOption.getUpcEanSecurityLevel()),
			new SSI1DParamValue(SSI1DParamName.Coupon_Report , 
					dlgSSI1DBarcodeOption.getCouponReport()),
		};
		
		paramValueList = new SSI1DParamValueList(paramValue);
		try {
			mReader.getBarcode().setBarcodeParam(paramValueList.getBytes(mModuleBarcodeType.getCode()));
		} catch (ATException e) {
			ATLog.e(TAG, e, "ERROR. saveSSI1DBarcodeParameter() - Failed to set Barcode Option(UPC/EAN)");
			return false;
		}
		
		//
		// Code128
		//
		length = dlgSSI1DBarcodeOption.getCode128Length();
		paramValue = new SSI1DParamValue[]{
			new SSI1DParamValue(SSI1DParamName.Set_Length_1_Code128, length.getLength1()),
			new SSI1DParamValue(SSI1DParamName.Set_Length_2_Code128, length.getLength2()),
			new SSI1DParamValue(SSI1DParamName.ISBT_Concatenation, 
					dlgSSI1DBarcodeOption.getIsbtConcatenation()),
			new SSI1DParamValue(SSI1DParamName.Check_ISBT_Table, 
					dlgSSI1DBarcodeOption.getCheckIsbtTable()),
			new SSI1DParamValue(SSI1DParamName.ISBT_Concatenation_Redundancy, 
					dlgSSI1DBarcodeOption.getIsbtConcatenationRedundancy()),
		};
		
		paramValueList = new SSI1DParamValueList(paramValue);
		
		try {
			mReader.getBarcode().setBarcodeParam(paramValueList.getBytes(mModuleBarcodeType.getCode()));
		} catch (ATException e) {
			ATLog.e(TAG, e, "ERROR. saveSSI2DBarcodeParameter() - Failed to set Barcode Option(Code128)");
			return false;
		}
		
		//
		// code39
		//
		length = dlgSSI1DBarcodeOption.getCode39Length();
		paramValue = new SSI1DParamValue[]{
			new SSI1DParamValue(SSI1DParamName.Code32_Prefix, dlgSSI1DBarcodeOption.getCode32Prefix()),
			new SSI1DParamValue(SSI1DParamName.Set_Length_1_Code39, length.getLength1()),
			new SSI1DParamValue(SSI1DParamName.Set_Length_2_Code39, length.getLength2()),
			new SSI1DParamValue(SSI1DParamName.Code39_Check_Digit_Verification, 
					dlgSSI1DBarcodeOption.getCode39CheckDigitVerification()),
			new SSI1DParamValue(SSI1DParamName.Transmit_Code39_Check_Digit, 
					dlgSSI1DBarcodeOption.getTransmitCode39CheckDigit()),
			new SSI1DParamValue(SSI1DParamName.Code39_Full_Ascii_Conversion, 
					dlgSSI1DBarcodeOption.getCode39FullAsciiConversion()),
		};
		
		paramValueList = new SSI1DParamValueList(paramValue);
		
		try {
			mReader.getBarcode().setBarcodeParam(paramValueList.getBytes(mModuleBarcodeType.getCode()));
		} catch (ATException e) {
			ATLog.e(TAG, e, "ERROR. saveSSI1DBarcodeParameter() - Failed to set Barcode Option(code39)");
			return false;
		}
		
		//
		// code93
		//
		length = dlgSSI1DBarcodeOption.getCode93Length();
		paramValue = new SSI1DParamValue[]{
			new SSI1DParamValue(SSI1DParamName.Set_Length_1_Code93, length.getLength1()),
			new SSI1DParamValue(SSI1DParamName.Set_Lenght_2_Code93, length.getLength2()),
		};
		
		paramValueList = new SSI1DParamValueList(paramValue);
		
		try {
			mReader.getBarcode().setBarcodeParam(paramValueList.getBytes(mModuleBarcodeType.getCode()));
		} catch (ATException e) {
			ATLog.e(TAG, e, "ERROR. saveSSI1DBarcodeParameter() - Failed to set Barcode Option(code93)");
			return false;
		}
		
		//
		// code11
		//
		length = dlgSSI1DBarcodeOption.getCode11Length();
		paramValue = new SSI1DParamValue[]{
			new SSI1DParamValue(SSI1DParamName.Set_Length_1_Code11, length.getLength1()),
			new SSI1DParamValue(SSI1DParamName.Set_Length_2_Code11, length.getLength2()),
			new SSI1DParamValue(SSI1DParamName.Code11_Check_Digit_Verification, 
					dlgSSI1DBarcodeOption.getCode11CheckDigitVerification()),
			new SSI1DParamValue(SSI1DParamName.Transmit_Code_11_Check_Digits, 
					dlgSSI1DBarcodeOption.getTransmitCode11CheckDigit()),
		};
		
		paramValueList = new SSI1DParamValueList(paramValue);
		
		try {
			mReader.getBarcode().setBarcodeParam(paramValueList.getBytes(mModuleBarcodeType.getCode()));
		} catch (ATException e) {
			ATLog.e(TAG, e, "ERROR. saveSSI1DBarcodeParameter() - Failed to set Barcode Option(code11)");
			return false;
		}
		
		//
		// I2of5
		//
		length = dlgSSI1DBarcodeOption.getI2of5Length();
		paramValue = new SSI1DParamValue[]{
			new SSI1DParamValue(SSI1DParamName.Set_Length_1_I2of5, length.getLength1()),
			new SSI1DParamValue(SSI1DParamName.Set_Length_2_I2of5, length.getLength2()),
			new SSI1DParamValue(SSI1DParamName.I2of5_Check_Digit_Verification, 
					dlgSSI1DBarcodeOption.getI2of5CheckDigitVerification()),
			new SSI1DParamValue(SSI1DParamName.Transmit_I2of5_Check_Digit, 
					dlgSSI1DBarcodeOption.getTransmitI2of5CheckDigit()),
			new SSI1DParamValue(SSI1DParamName.Convert_I2of5_to_EAN_13, 
					dlgSSI1DBarcodeOption.getConvertI2of5ToEan13()),
		};
		
		paramValueList = new SSI1DParamValueList(paramValue);
		
		try {
			mReader.getBarcode().setBarcodeParam(paramValueList.getBytes(mModuleBarcodeType.getCode()));
		} catch (ATException e) {
			ATLog.e(TAG, e, "ERROR. saveSSI1DBarcodeParameter() - Failed to set Barcode Option(I2of5)");
			return false;
		}
		
		//
		// D2of5
		//
		length = dlgSSI1DBarcodeOption.getD2of5Length();
		paramValue = new SSI1DParamValue[]{
			new SSI1DParamValue(SSI1DParamName.Set_Length_1_D2of5, length.getLength1()),
			new SSI1DParamValue(SSI1DParamName.Set_Length_2_D2of5, length.getLength2()),
		};
		
		paramValueList = new SSI1DParamValueList(paramValue);
		
		try {
			mReader.getBarcode().setBarcodeParam(paramValueList.getBytes(mModuleBarcodeType.getCode()));
		} catch (ATException e) {
			ATLog.e(TAG, e, "ERROR. saveSSI1DBarcodeParameter() - Failed to set Barcode Option(D2of5)");
			return false;
		}
		
		//
		// Codabar
		//
		length = dlgSSI1DBarcodeOption.getCodabarLength();
		paramValue = new SSI1DParamValue[]{
			new SSI1DParamValue(SSI1DParamName.Set_Length_1_Codabar, length.getLength1()),
			new SSI1DParamValue(SSI1DParamName.Set_Length_2_Codabar, length.getLength2()),
			new SSI1DParamValue(SSI1DParamName.CLSI_Editing, 
					dlgSSI1DBarcodeOption.getClsiEditing()),
			new SSI1DParamValue(SSI1DParamName.NOTIS_Editing, 
					dlgSSI1DBarcodeOption.getNotisEditing()),
			new SSI1DParamValue(SSI1DParamName.Codabar_Upper_Lower_Start_Stop_Characters_Dectection, 
					dlgSSI1DBarcodeOption.getCodabarStartStopCharactersDetection()),
		};
		
		paramValueList = new SSI1DParamValueList(paramValue);
		
		try {
			mReader.getBarcode().setBarcodeParam(paramValueList.getBytes(mModuleBarcodeType.getCode()));
		} catch (ATException e) {
			ATLog.e(TAG, e, "ERROR. saveSSI1DBarcodeParameter() - Failed to set Barcode Option(Codabar)");
			return false;
		}
		
		//
		// MSI
		//
		length = dlgSSI1DBarcodeOption.getMsiLength();
		paramValue = new SSI1DParamValue[]{
			new SSI1DParamValue(SSI1DParamName.Set_Length_1_MSI, length.getLength1()),
			new SSI1DParamValue(SSI1DParamName.Set_Length_2_MSI, length.getLength2()),
			new SSI1DParamValue(SSI1DParamName.MSI_Check_Digits, 
					dlgSSI1DBarcodeOption.getMSICheckDigit()),
			new SSI1DParamValue(SSI1DParamName.Transmit_MSI_Check_Digits, 
					dlgSSI1DBarcodeOption.getTransmitMsiCheckDigit()),
			new SSI1DParamValue(SSI1DParamName.MSI_Check_Digit_Algorithm, 
					dlgSSI1DBarcodeOption.getMSICheckDigitAlgorithm()),
		};
		
		paramValueList = new SSI1DParamValueList(paramValue);
		
		try {
			mReader.getBarcode().setBarcodeParam(paramValueList.getBytes(mModuleBarcodeType.getCode()));
		} catch (ATException e) {
			ATLog.e(TAG, e, "ERROR. saveSSI1DBarcodeParameter() - Failed to set Barcode Option(MSI)");
			return false;
		}
		
		//
		// Matrix 2of5
		//
		length = dlgSSI1DBarcodeOption.getMatrix2of5Length();
		paramValue = new SSI1DParamValue[]{
			new SSI1DParamValue(SSI1DParamName.Set_Length_1_Matrix2of5, length.getLength1()),
			new SSI1DParamValue(SSI1DParamName.Set_Length_2_Matrix2of5, length.getLength2()),
			new SSI1DParamValue(SSI1DParamName.Matrix2of5_Check_Digit, 
					dlgSSI1DBarcodeOption.getMatrix2of5CheckDigit()),
			new SSI1DParamValue(SSI1DParamName.Transmit_Matrix2of5_Check_Digit, 
					dlgSSI1DBarcodeOption.getTransmitMatrix2of5CheckDigit()),
		};
		
		paramValueList = new SSI1DParamValueList(paramValue);
		
		try {
			mReader.getBarcode().setBarcodeParam(paramValueList.getBytes(mModuleBarcodeType.getCode()));
		} catch (ATException e) {
			ATLog.e(TAG, e, "ERROR. saveSSI1DBarcodeParameter() - Failed to set Barcode Option(Matrix 2of5)");
			return false;
		}
		
		//
		// GS1 Databar
		//

		paramValue = new SSI1DParamValue[]{
			new SSI1DParamValue(SSI1DParamName.GS1_DataBar_Limited_Security_Level, 
					dlgSSI1DBarcodeOption.getGs1DatabarLimitedSecurityLevel()),
			new SSI1DParamValue(SSI1DParamName.Convert_GS1_DataBar_to_UPC_EAN, 
					dlgSSI1DBarcodeOption.getConvertGs1DatabarToUpcEan()),
		};
		
		paramValueList = new SSI1DParamValueList(paramValue);
		
		try {
			mReader.getBarcode().setBarcodeParam(paramValueList.getBytes(mModuleBarcodeType.getCode()));
		} catch (ATException e) {
			ATLog.e(TAG, e, "ERROR. saveSSI1DBarcodeParameter() - Failed to set Barcode Option(GS1 Databar)");
			return false;
		}
		
		// Inverse 1D
		// Redundancy Level
		// Security Level

		paramValue = new SSI1DParamValue[]{
				new SSI1DParamValue(SSI1DParamName.Bi_Directional_Redundancy, 
						dlgSSI1DBarcodeOption.getBiDirectionalRedundancy()),
				new SSI1DParamValue(SSI1DParamName.Linear_Security_Level, 
						dlgSSI1DBarcodeOption.getSecurityLevel()),
			};
			
			paramValueList = new SSI1DParamValueList(paramValue);
			
			try {
				mReader.getBarcode().setBarcodeParam(paramValueList.getBytes(mModuleBarcodeType.getCode()));
			} catch (ATException e) {
				ATLog.e(TAG, e, "ERROR. saveSSI1DBarcodeParameter() - Failed to set Barcode Option(ETC)");
				return false;
			}

		
		ATLog.i(TAG, INFO, "INFO. saveSSI1DBarcodeParameter()");
		return true;
	}
	
	
	private boolean loadSSI2DBarcodeParameter() throws ATException {
		byte[] data = null;
		SSI2DParamNameList paramNameList = null;
		SSI2DParamValueList paramValueList = null;

		// 
		// UPC/EAN  
		// 
		if(mIsThreadAlive){

			paramNameList = new SSI2DParamNameList ( new SSI2DParamName[] {
					SSI2DParamName.Bookland_ISBN_Format,
					SSI2DParamName.Decode_UPC_EAN_JAN_Supplementals,
					SSI2DParamName.Decode_UPC_EAN_JAN_Supplemental_Redundancy,
					SSI2DParamName.Decode_UPC_EAN_JAN_Supplemental_AIM_ID,
					SSI2DParamName.UPC_Reduced_Quiet_Zone,
					SSI2DParamName.Transmit_UPC_A_Check_Digit,
					SSI2DParamName.Transmit_UPC_E_Check_Digit,
					SSI2DParamName.Transmit_UPC_E1_Check_Digit,
					SSI2DParamName.UPC_A_Preamble,
					SSI2DParamName.UPC_E_Preamble,
					SSI2DParamName.UPC_E1_Preamble,
					SSI2DParamName.Convert_UPC_E_to_UPC_A,
					SSI2DParamName.Convert_UPC_E1_to_UPC_A,
					SSI2DParamName.EAN_8_JAN_8_Extend,
					SSI2DParamName.Coupon_Report
					});
			
			try {
				data = mReader.getBarcode().getBarcodeParam( paramNameList.getBytes(mModuleBarcodeType.getCode()));	
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSSI2DBarcodeParameter() - Failed to load Barcode Option (UPC/EAN) , Pamram List");
				throw e;
			}
						
			try {
				paramValueList = SSI2DParamValueList.parseParams(data, mModuleBarcodeType.getCode());
				dlgSSI2DBarcodeOption.setBooklandISBNFormat(
						(BooklandISBNFormat)paramValueList.getValueAt(SSI2DParamName.Bookland_ISBN_Format));
				dlgSSI2DBarcodeOption.setDecodeUpcEanJanSupplementals(
						(DecodeUpcEanJanSupplementals)paramValueList.getValueAt(SSI2DParamName.Decode_UPC_EAN_JAN_Supplementals));
				dlgSSI2DBarcodeOption.setUpcEanJanSupplementalRedundancy(
						(Integer)paramValueList.getValueAt(SSI2DParamName.Decode_UPC_EAN_JAN_Supplemental_Redundancy));
				dlgSSI2DBarcodeOption.setUpcEanJanSupplementalAIMIDFormat(
						(UpcEanJanSupplementalAIMIDFormat)paramValueList.getValueAt(SSI2DParamName.Decode_UPC_EAN_JAN_Supplemental_AIM_ID));
				dlgSSI2DBarcodeOption.setUpcReducedQuietZone(
						(Boolean)paramValueList.getValueAt(SSI2DParamName.UPC_Reduced_Quiet_Zone));
				dlgSSI2DBarcodeOption.setTransmitUpcACheckDigit(
						(Boolean)paramValueList.getValueAt(SSI2DParamName.Transmit_UPC_A_Check_Digit));
				dlgSSI2DBarcodeOption.setTransmitUpcECheckDigit(
						(Boolean)paramValueList.getValueAt(SSI2DParamName.Transmit_UPC_E_Check_Digit));
				dlgSSI2DBarcodeOption.setTransmitUpcE1CheckDigit(
						(Boolean)paramValueList.getValueAt(SSI2DParamName.Transmit_UPC_E1_Check_Digit));
				dlgSSI2DBarcodeOption.setUpcAPreamble(
						(Preamble)paramValueList.getValueAt(SSI2DParamName.UPC_A_Preamble));
				dlgSSI2DBarcodeOption.setUpcEPreamble(
						(Preamble)paramValueList.getValueAt(SSI2DParamName.UPC_E_Preamble));
				dlgSSI2DBarcodeOption.setUpcE1Preamble(
						(Preamble)paramValueList.getValueAt(SSI2DParamName.UPC_E1_Preamble));
				dlgSSI2DBarcodeOption.setConvertUpcEToUpcA(
						(Boolean)paramValueList.getValueAt(SSI2DParamName.Convert_UPC_E_to_UPC_A));
				dlgSSI2DBarcodeOption.setConvertUpcE1ToUpcA(
						(Boolean)paramValueList.getValueAt(SSI2DParamName.Convert_UPC_E1_to_UPC_A));
				dlgSSI2DBarcodeOption.setEan8Jan8Extend(
						(Boolean)paramValueList.getValueAt(SSI2DParamName.EAN_8_JAN_8_Extend));
				dlgSSI2DBarcodeOption.setCouponReport(
						(CouponReport)paramValueList.getValueAt(SSI2DParamName.Coupon_Report));
				
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSSI2DBarcodeParameter() - Failed to load Barcode Option (UPC/EAN) , Parsing Error");
				throw e;
			}

		} else {
			return false;
		}
		
		// 
		// Code128 
		// 
		if(mIsThreadAlive){
			
			paramNameList = new SSI2DParamNameList( new SSI2DParamName[]{
					SSI2DParamName.Set_Length_1_Code128,
					SSI2DParamName.Set_Length_2_Code128,
					SSI2DParamName.ISBT_Concatenation,
					SSI2DParamName.Check_ISBT_Table,
					SSI2DParamName.ISBT_Concatenation_Redundancy,
					SSI2DParamName.Code128_Reduced_Quiet_Zone,
					SSI2DParamName.Ignore_Code128_FNC4
			});
			
			try {
				data = mReader.getBarcode().getBarcodeParam( paramNameList.getBytes(mModuleBarcodeType.getCode()));
				
			} catch (ATException e) {
				ATLog.e(TAG, e, "ERROR. loadSSI2DBarcodeParameter() - Failed to load Barcode Option (Code128) , Pamram List");
				throw e;
			}
			
			try {
				paramValueList = SSI2DParamValueList.parseParams(data, mModuleBarcodeType.getCode());
				int length1 = (Integer) paramValueList.getValueAt(SSI2DParamName.Set_Length_1_Code128);
				int length2 = (Integer) paramValueList.getValueAt(SSI2DParamName.Set_Length_2_Code128);
				dlgSSI2DBarcodeOption.setCode128Length(new CodeLength(length1, length2));
				dlgSSI2DBarcodeOption.setIsbtConcatenation(
						(ISBTConcatenation) paramValueList.getValueAt(SSI2DParamName.ISBT_Concatenation));
				dlgSSI2DBarcodeOption.setCheckIsbtTable(
						(Boolean) paramValueList.getValueAt(SSI2DParamName.Check_ISBT_Table));
				dlgSSI2DBarcodeOption.setIsbtConcatenationRedundancy(
						(Integer) paramValueList.getValueAt(SSI2DParamName.ISBT_Concatenation_Redundancy));
				dlgSSI2DBarcodeOption.setCode128ReducedQuietZone(
						(Boolean) paramValueList.getValueAt(SSI2DParamName.Code128_Reduced_Quiet_Zone));
				dlgSSI2DBarcodeOption.setIgnoreCode128Fnc4(
						(Boolean) paramValueList.getValueAt(SSI2DParamName.Ignore_Code128_FNC4));

			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSSI2DBarcodeParameter() - Failed to load Barcode Option (Code128) , Parsing Error");
				throw e;
			}
			
		} else {
			return false;
		}
		
		// 
		// code39
		// 
		if(mIsThreadAlive){
			
			paramNameList = new SSI2DParamNameList( new SSI2DParamName[] {
					SSI2DParamName.Code32_Prefix,
					SSI2DParamName.Set_Length_1_Code39,
					SSI2DParamName.Set_Length_2_Code39,
					SSI2DParamName.Code39_Check_Digit_Verification,
					SSI2DParamName.Transmit_Code39_Check_Digit,
					SSI2DParamName.Code39_Full_Ascii_Conversion,
					SSI2DParamName.Code39_Buffering_Scan_Store,
					SSI2DParamName.Code39_Reduced_Quiet_Zone
			});
			
			try {
				data = mReader.getBarcode().getBarcodeParam( paramNameList.getBytes(mModuleBarcodeType.getCode()));
				
			} catch (ATException e){
				ATLog.e(TAG, e,"ERROR. loadSSI2DBarcodeParameter() - Failed to load Barcode Option (Code39) , Pamram List");
				throw e;
			}
			
			try {
				paramValueList = SSI2DParamValueList.parseParams(data, mModuleBarcodeType.getCode());
				dlgSSI2DBarcodeOption.setCode32Prefix(
						(Boolean) paramValueList.getValueAt(SSI2DParamName.Code32_Prefix));
				int length1 = (Integer) paramValueList.getValueAt(SSI2DParamName.Set_Length_1_Code39);
				int length2 = (Integer) paramValueList.getValueAt(SSI2DParamName.Set_Length_2_Code39);
				dlgSSI2DBarcodeOption.setCode39Length(new CodeLength(length1, length2));
				dlgSSI2DBarcodeOption.setCode39CheckDigitVerification(
						(Boolean) paramValueList.getValueAt(SSI2DParamName.Code39_Check_Digit_Verification));
				dlgSSI2DBarcodeOption.setTransmitCode39CheckDigit(
						(Boolean) paramValueList.getValueAt(SSI2DParamName.Transmit_Code39_Check_Digit));
				dlgSSI2DBarcodeOption.setCode39FullAsciiConversion(
						(Boolean) paramValueList.getValueAt(SSI2DParamName.Code39_Full_Ascii_Conversion));
				dlgSSI2DBarcodeOption.setCode39BufferingScanStore(
						(Boolean) paramValueList.getValueAt(SSI2DParamName.Code39_Buffering_Scan_Store));
				dlgSSI2DBarcodeOption.setCode39ReducedQuietZone(
						(Boolean) paramValueList.getValueAt(SSI2DParamName.Code39_Reduced_Quiet_Zone));

			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSSI2DBarcodeParameter() - Failed to load Barcode Option (Code39) , Parsing Error");
				throw e;
			}
			
		} else {
			return false;
		}
		
		// 
		// code93
		// 
		if(mIsThreadAlive){
			
			paramNameList = new SSI2DParamNameList( new SSI2DParamName[] {
				SSI2DParamName.Set_Length_1_Code93,
				SSI2DParamName.Set_Lenght_2_Code93
			});
			
			try {
				data = mReader.getBarcode().getBarcodeParam( paramNameList.getBytes(mModuleBarcodeType.getCode()));
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSSI2DBarcodeParameter() - Failed to load Barcode Option (Code93) , Pamram List");
				throw e;
			}
			
			try {
				paramValueList = SSI2DParamValueList.parseParams(data, mModuleBarcodeType.getCode());
				int length1 = (Integer) paramValueList.getValueAt(SSI2DParamName.Set_Length_1_Code93);
				int length2 = (Integer) paramValueList.getValueAt(SSI2DParamName.Set_Lenght_2_Code93);
				dlgSSI2DBarcodeOption.setCode93Length( new CodeLength(length1, length2));
				
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSSI2DBarcodeParameter() - Failed to load Barcode Option (Code93) , Parsing Error");
				throw e;
			} 
		} else {
			return false;
		}
		
		
		// 
		// Code11
		// 
		if(mIsThreadAlive){
			
			paramNameList = new SSI2DParamNameList( new SSI2DParamName[]{
					SSI2DParamName.Set_Length_1_Code11,
					SSI2DParamName.Set_Length_2_Code11,
					SSI2DParamName.Code11_Check_Digit_Verification,
					SSI2DParamName.Transmit_Code_11_Check_Digits
			});
			
			try {
				data = mReader.getBarcode().getBarcodeParam( paramNameList.getBytes(mModuleBarcodeType.getCode()));
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSSI2DBarcodeParameter() - Failed to load Barcode Option (Code11) , Pamram List");
				throw e;
			}
			
			try {
				paramValueList = SSI2DParamValueList.parseParams(data, mModuleBarcodeType.getCode());
				int length1 = (Integer) paramValueList.getValueAt(SSI2DParamName.Set_Length_1_Code11);
				int length2 = (Integer) paramValueList.getValueAt(SSI2DParamName.Set_Length_2_Code11);
				dlgSSI2DBarcodeOption.setCode11Length( new CodeLength(length1, length2));
				dlgSSI2DBarcodeOption.setCode11CheckDigitVerification(
						(Code11CheckDigitVerification) paramValueList.getValueAt(SSI2DParamName.Code11_Check_Digit_Verification));
				dlgSSI2DBarcodeOption.setTransmitCode11CheckDigit(
						(Boolean) paramValueList.getValueAt(SSI2DParamName.Transmit_Code_11_Check_Digits));
				
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSSI2DBarcodeParameter() - Failed to load Barcode Option (Code11) , Parsing Error");
				throw e;
			}
		} else {
			return false;
		}
		// 
		// I2of5
		// 
		if(mIsThreadAlive){
			
			paramNameList = new SSI2DParamNameList( new SSI2DParamName[]{
				SSI2DParamName.Set_Length_1_I2of5,
				SSI2DParamName.Set_Length_2_I2of5,
				SSI2DParamName.I2of5_Check_Digit_Verification,
				SSI2DParamName.Transmit_I2of5_Check_Digit,
				SSI2DParamName.Convert_I2of5_to_EAN_13,
				SSI2DParamName.I2of5_Reduced_Quiet_Zone
			});
			
			try {
				data = mReader.getBarcode().getBarcodeParam( paramNameList.getBytes(mModuleBarcodeType.getCode()));
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSSI2DBarcodeParameter() - Failed to load Barcode Option (I2of5) , Pamram List");
				throw e;
			}
			
			try {
				paramValueList = SSI2DParamValueList.parseParams(data, mModuleBarcodeType.getCode());
				int length1 = (Integer) paramValueList.getValueAt(SSI2DParamName.Set_Length_1_I2of5);
				int length2 = (Integer) paramValueList.getValueAt(SSI2DParamName.Set_Length_2_I2of5);
				dlgSSI2DBarcodeOption.setI2of5Length( new CodeLength(length1, length2));
				dlgSSI2DBarcodeOption.setI2of5CheckDigitVerification(
						(I2of5CheckDigitVerification) paramValueList.getValueAt(SSI2DParamName.I2of5_Check_Digit_Verification));
				dlgSSI2DBarcodeOption.setTransmitI2of5CheckDigit(
						(Boolean) paramValueList.getValueAt(SSI2DParamName.Transmit_I2of5_Check_Digit));
				dlgSSI2DBarcodeOption.setConvertI2of5ToEan13(
						(Boolean) paramValueList.getValueAt(SSI2DParamName.Convert_I2of5_to_EAN_13));
				dlgSSI2DBarcodeOption.setI2of5ReducedQuietZone(
						(Boolean) paramValueList.getValueAt(SSI2DParamName.I2of5_Reduced_Quiet_Zone));
				
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSSI2DBarcodeParameter() - Failed to load Barcode Option (I2of5) , Parsing Error");
				throw e;
			}
			
		} else {
			return false;
		}
		// 
		// D2of5
		// 
		if(mIsThreadAlive){
			
			paramNameList = new SSI2DParamNameList( new SSI2DParamName[]{
					SSI2DParamName.Set_Length_1_D2of5,
					SSI2DParamName.Set_Length_2_D2of5
				});
			
			try {
				data = mReader.getBarcode().getBarcodeParam( paramNameList.getBytes(mModuleBarcodeType.getCode()));
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSSI2DBarcodeParameter() - Failed to load Barcode Option (D2of5) , Pamram List");
				throw e;
			}
			
			try {
				paramValueList = SSI2DParamValueList.parseParams(data, mModuleBarcodeType.getCode());
				int length1 = (Integer) paramValueList.getValueAt(SSI2DParamName.Set_Length_1_D2of5);
				int length2 = (Integer) paramValueList.getValueAt(SSI2DParamName.Set_Length_2_D2of5);
				dlgSSI2DBarcodeOption.setD2of5Length( new CodeLength(length1, length2));

			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSSI2DBarcodeParameter() - Failed to load Barcode Option (D2of5) , Parsing Error");
				throw e;
			}
			
		} else {
			return false;
		}
		// 
		// Codabar
		// 
		if(mIsThreadAlive) {
			
			paramNameList = new SSI2DParamNameList( new SSI2DParamName[]{
				SSI2DParamName.Set_Length_1_Codabar,
				SSI2DParamName.Set_Length_2_Codabar,
				SSI2DParamName.CLSI_Editing,
				SSI2DParamName.NOTIS_Editing,
				SSI2DParamName.Codabar_Upper_Lower_Start_Stop_Characters_Dectection
			});
			
			try {
				data = mReader.getBarcode().getBarcodeParam( paramNameList.getBytes(mModuleBarcodeType.getCode()));
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSSI2DBarcodeParameter() - Failed to load Barcode Option (Codabar) , Pamram List");
				throw e;
			}
			
			try {
				paramValueList = SSI2DParamValueList.parseParams(data, mModuleBarcodeType.getCode());
				int length1 = (Integer) paramValueList.getValueAt(SSI2DParamName.Set_Length_1_Codabar);
				int length2 = (Integer) paramValueList.getValueAt(SSI2DParamName.Set_Length_2_Codabar);
				dlgSSI2DBarcodeOption.setCodabarLength( new CodeLength(length1, length2));
				dlgSSI2DBarcodeOption.setClsiEditing(
						(Boolean) paramValueList.getValueAt(SSI2DParamName.CLSI_Editing));
				dlgSSI2DBarcodeOption.setNotisEditing(
						(Boolean) paramValueList.getValueAt(SSI2DParamName.NOTIS_Editing));
				dlgSSI2DBarcodeOption.setCodabarStartStopCharactersDetection((CodabarStartStopCharactersDetection)
						paramValueList.getValueAt(SSI2DParamName.Codabar_Upper_Lower_Start_Stop_Characters_Dectection));
				
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSSI2DBarcodeParameter() - Failed to load Barcode Option (Codabar) , Parsing Error");
				throw e;
			}
			
		} else {
			return false;
		}
		// 
		// MSI
		// 
		if(mIsThreadAlive){
			
			paramNameList = new SSI2DParamNameList( new SSI2DParamName[]{
				SSI2DParamName.Set_Length_1_MSI,
				SSI2DParamName.Set_Length_2_MSI,
				SSI2DParamName.MSI_Check_Digits,
				SSI2DParamName.Transmit_MSI_Check_Digits,
				SSI2DParamName.MSI_Check_Digit_Algorithm,
			});
			
			try {
				data = mReader.getBarcode().getBarcodeParam( paramNameList.getBytes(mModuleBarcodeType.getCode()));
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSSI2DBarcodeParameter() - Failed to load Barcode Option (MSI) , Pamram List");
				throw e;
			}
			
			try {
				paramValueList = SSI2DParamValueList.parseParams(data, mModuleBarcodeType.getCode());
				int length1 = (Integer) paramValueList.getValueAt(SSI2DParamName.Set_Length_1_MSI);
				int length2 = (Integer) paramValueList.getValueAt(SSI2DParamName.Set_Length_2_MSI);
				dlgSSI2DBarcodeOption.setMsiLength( new CodeLength(length1, length2));
				dlgSSI2DBarcodeOption.setMSICheckDigits(
						(MSICheckDigits) paramValueList.getValueAt(SSI2DParamName.MSI_Check_Digits));
				dlgSSI2DBarcodeOption.setTransmitMsiCheckDigit(
						(Boolean) paramValueList.getValueAt(SSI2DParamName.Transmit_MSI_Check_Digits));
				dlgSSI2DBarcodeOption.setMSICheckDigitAlgorithm(
						(MSICheckDigitAlgorithm) paramValueList.getValueAt(SSI2DParamName.MSI_Check_Digit_Algorithm));
				
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSSI2DBarcodeParameter() - Failed to load Barcode Option (MSI) , Parsing Error");
				throw e;
			}
		} else {
			return false;
		}
		// 
		// Matrix 2of5
		// 
		if(mIsThreadAlive){
			
			paramNameList = new SSI2DParamNameList( new SSI2DParamName[]{
					SSI2DParamName.Set_Length_1_Matrix2of5,
					SSI2DParamName.Set_Length_2_Matrix2of5,
					SSI2DParamName.Matrix2of5_Check_Digit,
					SSI2DParamName.Transmit_Matrix2of5_Check_Digit,
				});
			
			try {
				data = mReader.getBarcode().getBarcodeParam( paramNameList.getBytes(mModuleBarcodeType.getCode()));
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSSI2DBarcodeParameter() - Failed to load Barcode Option (Matrix 2of5) , Pamram List");
				throw e;
			}
			
			try {
				paramValueList = SSI2DParamValueList.parseParams(data, mModuleBarcodeType.getCode());
				int length1 = (Integer) paramValueList.getValueAt(SSI2DParamName.Set_Length_1_Matrix2of5);
				int length2 = (Integer) paramValueList.getValueAt(SSI2DParamName.Set_Length_2_Matrix2of5);
				dlgSSI2DBarcodeOption.setMatrix2of5Length( new CodeLength(length1, length2));
				dlgSSI2DBarcodeOption.setMatrix2of5CheckDigit(
						(Boolean) paramValueList.getValueAt(SSI2DParamName.Matrix2of5_Check_Digit));
				dlgSSI2DBarcodeOption.setTransmitMatrix2of5CheckDigit(
						(Boolean) paramValueList.getValueAt(SSI2DParamName.Transmit_Matrix2of5_Check_Digit));
				
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSSI2DBarcodeParameter() - Failed to load Barcode Option (Matrix 2of5) , Parsing Error");
				throw e;
			}
		} else {
			return false;
		}
		
		// 
		// Postal Codes
		// 
		if(mIsThreadAlive){
			
			paramNameList = new SSI2DParamNameList( new SSI2DParamName[]{
					SSI2DParamName.Transmit_US_Postal_Check_Digit,
					SSI2DParamName.Transmit_UK_Postal_Check_Digit,
					SSI2DParamName.Australia_Post_Format,
				});
			
			try {
				data = mReader.getBarcode().getBarcodeParam(paramNameList.getBytes(mModuleBarcodeType.getCode()));
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSSI2DBarcodeParameter() - Failed to load Barcode Option (Postal Codes) , Pamram List");
				throw e;
			}
			
			try {
				paramValueList = SSI2DParamValueList.parseParams(data, mModuleBarcodeType.getCode());
				dlgSSI2DBarcodeOption.setTransmitUsPostalCheckDigit(
						(Boolean) paramValueList.getValueAt(SSI2DParamName.Transmit_US_Postal_Check_Digit));
				dlgSSI2DBarcodeOption.setTransmitUkPostalCheckDigit(
						(Boolean) paramValueList.getValueAt(SSI2DParamName.Transmit_UK_Postal_Check_Digit));
				dlgSSI2DBarcodeOption.setAustraliaPostFormat(
						(AustraliaPostFormat) paramValueList.getValueAt(SSI2DParamName.Australia_Post_Format));
				
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSSI2DBarcodeParameter() - Failed to load Barcode Option (Postal Codes) , Parsing Error");
				throw e;
			}
		} else {
			return false;
		}

		// 
		// GS1 Databar
		// 

		if(mIsThreadAlive){
			
			paramNameList = new SSI2DParamNameList( new SSI2DParamName[]{
					SSI2DParamName.GS1_DataBar_Limited_Security_Level,
					SSI2DParamName.Convert_GS1_DataBar_to_UPC_EAN
				});
			
			try {
				data = mReader.getBarcode().getBarcodeParam(paramNameList.getBytes(mModuleBarcodeType.getCode()));
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSSI2DBarcodeParameter() - Failed to load Barcode Option (GS1 Databar) , Pamram List");
				throw e;
			}
			
			try {
				paramValueList = SSI2DParamValueList.parseParams(data, mModuleBarcodeType.getCode());
				dlgSSI2DBarcodeOption.setGs1DatabarLimitedSecurityLevel((GS1DataBarLimitedSecurityLevel)
						 paramValueList.getValueAt(SSI2DParamName.GS1_DataBar_Limited_Security_Level));
				dlgSSI2DBarcodeOption.setConvertGs1DatabarToUpcEan(
						(Boolean) paramValueList.getValueAt(SSI2DParamName.Convert_GS1_DataBar_to_UPC_EAN));
				
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSSI2DBarcodeParameter() - Failed to load Barcode Option (GS1 Databar) , Parsing Error");
				throw e;
			}
		} else {
			return false;
		}


		// 
		// Composite
		// 

		if(mIsThreadAlive){
			
			paramNameList = new SSI2DParamNameList( new SSI2DParamName[]{
					SSI2DParamName.UPC_Composite_Mode,
					SSI2DParamName.Composite_Beep_Mode,
					SSI2DParamName.GS1_128_Emulation_Mode_for_UCC_EAN_Composite_Codes
				});
			
			try {
				data = mReader.getBarcode().getBarcodeParam(paramNameList.getBytes(mModuleBarcodeType.getCode()));
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSSI2DBarcodeParameter() - Failed to load Barcode Option (Composite) , Pamram List");
				throw e;
			}
			
			try {
				paramValueList = SSI2DParamValueList.parseParams(data, mModuleBarcodeType.getCode());
				dlgSSI2DBarcodeOption.setUpcCompositeMode(
						(UpcCompositeMode) paramValueList.getValueAt(SSI2DParamName.UPC_Composite_Mode));
				dlgSSI2DBarcodeOption.setCompositeBeepMode(
						(CompositeBeepMode) paramValueList.getValueAt(SSI2DParamName.Composite_Beep_Mode));
				dlgSSI2DBarcodeOption.setGs1128Emulation(
						(Boolean) paramValueList.getValueAt(SSI2DParamName.GS1_128_Emulation_Mode_for_UCC_EAN_Composite_Codes));
				
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSSI2DBarcodeParameter() - Failed to load Barcode Option (Composite) , Parsing Error");
				throw e;
			}
		} else {
			return false;
		}


		// 
		// 2D symbol
		// 

		if(mIsThreadAlive){
			
			paramNameList = new SSI2DParamNameList( new SSI2DParamName[]{
					SSI2DParamName.Code128_Emulation,
					SSI2DParamName.Data_Matrix_Inverse,
					SSI2DParamName.Decode_Mirror_Images,
					SSI2DParamName.QR_Inverse,
					SSI2DParamName.Aztec_Inverse,
					SSI2DParamName.HanXin_Inverse
				});
			
			try {
				data = mReader.getBarcode().getBarcodeParam(paramNameList.getBytes(mModuleBarcodeType.getCode()));
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSSI2DBarcodeParameter() - Failed to load Barcode Option (2D symbol) , Pamram List");
				throw e;
			}
			
			try {
				paramValueList = SSI2DParamValueList.parseParams(data, mModuleBarcodeType.getCode());
				dlgSSI2DBarcodeOption.setCode128Emulation(
						(Boolean) paramValueList.getValueAt(SSI2DParamName.Code128_Emulation));
				dlgSSI2DBarcodeOption.setDataMatrixInverse(
						(DataMatrixInverse) paramValueList.getValueAt(SSI2DParamName.Data_Matrix_Inverse));
				dlgSSI2DBarcodeOption.setDecodeMirrorImages(
						(DecodeMirrorImages) paramValueList.getValueAt(SSI2DParamName.Decode_Mirror_Images));
				dlgSSI2DBarcodeOption.setQRInverse(
						(QRInverse) paramValueList.getValueAt(SSI2DParamName.QR_Inverse));
				dlgSSI2DBarcodeOption.setAztecInverse(
						(AztecInverse) paramValueList.getValueAt(SSI2DParamName.Aztec_Inverse));
				dlgSSI2DBarcodeOption.setHanXinInverse(
						(HanXinInverse) paramValueList.getValueAt(SSI2DParamName.HanXin_Inverse));

			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSSI2DBarcodeParameter() - Failed to load Barcode Option (2D symbol) , Parsing Error");
				throw e;
			}
		} else {
			return false;
		}


		// 
		// 1D Quiet Zone Level
		// 

		if(mIsThreadAlive){
			
			paramNameList = new SSI2DParamNameList( new SSI2DParamName[]{
					SSI2DParamName.Quiet_Zone_Level_1D,
					SSI2DParamName.Intercharacter_Gap_Size
				});
			
			try {
				data = mReader.getBarcode().getBarcodeParam(paramNameList.getBytes(mModuleBarcodeType.getCode()));
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSSI2DBarcodeParameter() - Failed to load Barcode Option (1D Quiet Zone Level) , Pamram List");
				throw e;
			}
			
			try {
				paramValueList = SSI2DParamValueList.parseParams(data, mModuleBarcodeType.getCode());
				dlgSSI2DBarcodeOption.set1DQuietZoneLevel(
						(QuietZoneLevel1D) paramValueList.getValueAt(SSI2DParamName.Quiet_Zone_Level_1D));
				dlgSSI2DBarcodeOption.setIntercharacterGapSize(
						(IntercharacterGapSize) paramValueList.getValueAt(SSI2DParamName.Intercharacter_Gap_Size));
				
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSSI2DBarcodeParameter() - Failed to load Barcode Option (1D Quiet Zone Level) , Parsing Error");
				throw e;
			}
		} else {
			return false;
		}

		// 
		// Inverse 1D
		// Redundancy Level
		// Security Level
		// 
		if(mIsThreadAlive){
			
			paramNameList = new SSI2DParamNameList( new SSI2DParamName[]{
					SSI2DParamName.Inverse_1D,
					SSI2DParamName.Redundancy_Level,
					SSI2DParamName.Security_Level
				});
			
			try {
				data = mReader.getBarcode().getBarcodeParam(paramNameList.getBytes(mModuleBarcodeType.getCode()));
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSSI2DBarcodeParameter() - Failed to load Barcode Option (ETC) , Pamram List");
				throw e;
			}
			
			try {
				paramValueList = SSI2DParamValueList.parseParams(data, mModuleBarcodeType.getCode());
				dlgSSI2DBarcodeOption.setInverse1D(
						(Inverse1D) paramValueList.getValueAt(SSI2DParamName.Inverse_1D));
				dlgSSI2DBarcodeOption.setRedundancyLevel(
						(RedundancyLevel) paramValueList.getValueAt(SSI2DParamName.Redundancy_Level));
				dlgSSI2DBarcodeOption.setSecurityLevel(
						(SecurityLevel) paramValueList.getValueAt(SSI2DParamName.Security_Level));
				
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSSI2DBarcodeParameter() - Failed to load Barcode Option (ETC) , Parsing Error");
				throw e;
			}
		} else {
			return false;
		}

		ATLog.i(TAG, INFO, "INFO. loadSSI2DBarcodeParameter()");
		return true;
	}
	
	private boolean saveSSI2DBarcodeParameter() {
		
		SSI2DParamValueList paramValueList = null;
		SSI2DParamValue[] paramValue = null;
		CodeLength length = null;
		
		//
		// UPC/EAN
		//
		paramValue = new SSI2DParamValue[]{
			new SSI2DParamValue(SSI2DParamName.Bookland_ISBN_Format , 
					dlgSSI2DBarcodeOption.getBooklandISBNFormat()),
			new SSI2DParamValue(SSI2DParamName.Decode_UPC_EAN_JAN_Supplementals , 
					dlgSSI2DBarcodeOption.getDecodeUpcEanJanSupplementals()),
			new SSI2DParamValue(SSI2DParamName.Decode_UPC_EAN_JAN_Supplemental_Redundancy , 
					dlgSSI2DBarcodeOption.getUpcEanJanSupplementalRedundancy()),
			new SSI2DParamValue(SSI2DParamName.Decode_UPC_EAN_JAN_Supplemental_AIM_ID , 
					dlgSSI2DBarcodeOption.getUpcEanJanSupplementalAIMIDFormat()),
			new SSI2DParamValue(SSI2DParamName.UPC_Reduced_Quiet_Zone , 
					dlgSSI2DBarcodeOption.getUpcReducedQuietZone()),
			new SSI2DParamValue(SSI2DParamName.Transmit_UPC_A_Check_Digit , 
					dlgSSI2DBarcodeOption.getTransmitUpcACheckDigit()),
			new SSI2DParamValue(SSI2DParamName.Transmit_UPC_E_Check_Digit , 
					dlgSSI2DBarcodeOption.getTransmitUpcECheckDigit()),
			new SSI2DParamValue(SSI2DParamName.Transmit_UPC_E1_Check_Digit , 
					dlgSSI2DBarcodeOption.getTransmitUpcE1CheckDigit()),
			new SSI2DParamValue(SSI2DParamName.UPC_A_Preamble , 
					dlgSSI2DBarcodeOption.getUpcAPreamble()),
			new SSI2DParamValue(SSI2DParamName.UPC_E_Preamble , 
					dlgSSI2DBarcodeOption.getUpcEPreamble()),
			new SSI2DParamValue(SSI2DParamName.UPC_E1_Preamble , 
					dlgSSI2DBarcodeOption.getUpcE1Preamble()),
			new SSI2DParamValue(SSI2DParamName.Convert_UPC_E_to_UPC_A , 
					dlgSSI2DBarcodeOption.getConvertUpcEToUpcA()),
			new SSI2DParamValue(SSI2DParamName.Convert_UPC_E1_to_UPC_A , 
					dlgSSI2DBarcodeOption.getConvertUpcE1ToUpcA()),
			new SSI2DParamValue(SSI2DParamName.EAN_8_JAN_8_Extend , 
					dlgSSI2DBarcodeOption.getEan8Jan8Extend()),
			new SSI2DParamValue(SSI2DParamName.Coupon_Report , 
					dlgSSI2DBarcodeOption.getCouponReport()),
		};
		
		paramValueList = new SSI2DParamValueList(paramValue);
		try {
			mReader.getBarcode().setBarcodeParam(paramValueList.getBytes(mModuleBarcodeType.getCode()));
		} catch (ATException e) {
			ATLog.e(TAG, e, "ERROR. saveSSI2DBarcodeParameter() - Failed to set Barcode Option(UPC/EAN)");
			return false;
		}
		
		//
		// Code128
		//
		length = dlgSSI2DBarcodeOption.getCode128Length();
		paramValue = new SSI2DParamValue[]{
			new SSI2DParamValue(SSI2DParamName.Set_Length_1_Code128, length.getLength1()),
			new SSI2DParamValue(SSI2DParamName.Set_Length_2_Code128, length.getLength2()),
			new SSI2DParamValue(SSI2DParamName.ISBT_Concatenation, 
					dlgSSI2DBarcodeOption.getIsbtConcatenation()),
			new SSI2DParamValue(SSI2DParamName.Check_ISBT_Table, 
					dlgSSI2DBarcodeOption.getCheckIsbtTable()),
			new SSI2DParamValue(SSI2DParamName.ISBT_Concatenation_Redundancy, 
					dlgSSI2DBarcodeOption.getIsbtConcatenationRedundancy()),
			new SSI2DParamValue(SSI2DParamName.Code128_Reduced_Quiet_Zone, 
					dlgSSI2DBarcodeOption.getCode128ReducedQuietZone()),
			new SSI2DParamValue(SSI2DParamName.Ignore_Code128_FNC4, 
					dlgSSI2DBarcodeOption.getIgnoreCode128Fnc4()),
		};
		
		paramValueList = new SSI2DParamValueList(paramValue);
		
		try {
			mReader.getBarcode().setBarcodeParam(paramValueList.getBytes(mModuleBarcodeType.getCode()));
		} catch (ATException e) {
			ATLog.e(TAG, e, "ERROR. saveSSI2DBarcodeParameter() - Failed to set Barcode Option(Code128)");
			return false;
		}
		
		//
		// code39
		//
		length = dlgSSI2DBarcodeOption.getCode39Length();
		paramValue = new SSI2DParamValue[]{
			new SSI2DParamValue(SSI2DParamName.Code32_Prefix, dlgSSI2DBarcodeOption.getCode32Prefix()),
			new SSI2DParamValue(SSI2DParamName.Set_Length_1_Code39, length.getLength1()),
			new SSI2DParamValue(SSI2DParamName.Set_Length_2_Code39, length.getLength2()),
			new SSI2DParamValue(SSI2DParamName.Code39_Check_Digit_Verification, 
					dlgSSI2DBarcodeOption.getCode39CheckDigitVerification()),
			new SSI2DParamValue(SSI2DParamName.Transmit_Code39_Check_Digit, 
					dlgSSI2DBarcodeOption.getTransmitCode39CheckDigit()),
			new SSI2DParamValue(SSI2DParamName.Code39_Full_Ascii_Conversion, 
					dlgSSI2DBarcodeOption.getCode39FullAsciiConversion()),
			new SSI2DParamValue(SSI2DParamName.Code39_Buffering_Scan_Store, 
					dlgSSI2DBarcodeOption.getCode39BufferingScanStore()),
			new SSI2DParamValue(SSI2DParamName.Code39_Reduced_Quiet_Zone, 
					dlgSSI2DBarcodeOption.getCode39ReducedQuietZone()),
		};
		
		paramValueList = new SSI2DParamValueList(paramValue);
		
		try {
			mReader.getBarcode().setBarcodeParam(paramValueList.getBytes(mModuleBarcodeType.getCode()));
		} catch (ATException e) {
			ATLog.e(TAG, e, "ERROR. saveSSI2DBarcodeParameter() - Failed to set Barcode Option(code39)");
			return false;
		}
		
		//
		// code93
		//
		length = dlgSSI2DBarcodeOption.getCode93Length();
		paramValue = new SSI2DParamValue[]{
			new SSI2DParamValue(SSI2DParamName.Set_Length_1_Code93, length.getLength1()),
			new SSI2DParamValue(SSI2DParamName.Set_Lenght_2_Code93, length.getLength2()),
		};
		
		paramValueList = new SSI2DParamValueList(paramValue);
		
		try {
			mReader.getBarcode().setBarcodeParam(paramValueList.getBytes(mModuleBarcodeType.getCode()));
		} catch (ATException e) {
			ATLog.e(TAG, e, "ERROR. saveSSI2DBarcodeParameter() - Failed to set Barcode Option(code93)");
			return false;
		}
		
		//
		// code11
		//
		length = dlgSSI2DBarcodeOption.getCode11Length();
		paramValue = new SSI2DParamValue[]{
			new SSI2DParamValue(SSI2DParamName.Set_Length_1_Code11, length.getLength1()),
			new SSI2DParamValue(SSI2DParamName.Set_Length_2_Code11, length.getLength2()),
			new SSI2DParamValue(SSI2DParamName.Code11_Check_Digit_Verification, 
					dlgSSI2DBarcodeOption.getCode11CheckDigitVerification()),
			new SSI2DParamValue(SSI2DParamName.Transmit_Code_11_Check_Digits, 
					dlgSSI2DBarcodeOption.getTransmitCode11CheckDigit()),
		};
		
		paramValueList = new SSI2DParamValueList(paramValue);
		
		try {
			mReader.getBarcode().setBarcodeParam(paramValueList.getBytes(mModuleBarcodeType.getCode()));
		} catch (ATException e) {
			ATLog.e(TAG, e, "ERROR. saveSSI2DBarcodeParameter() - Failed to set Barcode Option(code11)");
			return false;
		}
		
		//
		// I2of5
		//
		length = dlgSSI2DBarcodeOption.getI2of5Length();
		paramValue = new SSI2DParamValue[]{
			new SSI2DParamValue(SSI2DParamName.Set_Length_1_I2of5, length.getLength1()),
			new SSI2DParamValue(SSI2DParamName.Set_Length_2_I2of5, length.getLength2()),
			new SSI2DParamValue(SSI2DParamName.I2of5_Check_Digit_Verification, 
					dlgSSI2DBarcodeOption.getI2of5CheckDigitVerification()),
			new SSI2DParamValue(SSI2DParamName.Transmit_I2of5_Check_Digit, 
					dlgSSI2DBarcodeOption.getTransmitI2of5CheckDigit()),
			new SSI2DParamValue(SSI2DParamName.Convert_I2of5_to_EAN_13, 
					dlgSSI2DBarcodeOption.getConvertI2of5ToEan13()),
			new SSI2DParamValue(SSI2DParamName.I2of5_Reduced_Quiet_Zone, 
					dlgSSI2DBarcodeOption.getI2of5ReducedQuietZone()),
		};
		
		paramValueList = new SSI2DParamValueList(paramValue);
		
		try {
			mReader.getBarcode().setBarcodeParam(paramValueList.getBytes(mModuleBarcodeType.getCode()));
		} catch (ATException e) {
			ATLog.e(TAG, e, "ERROR. saveSSI2DBarcodeParameter() - Failed to set Barcode Option(I2of5)");
			return false;
		}
		
		//
		// D2of5
		//
		length = dlgSSI2DBarcodeOption.getD2of5Length();
		paramValue = new SSI2DParamValue[]{
			new SSI2DParamValue(SSI2DParamName.Set_Length_1_D2of5, length.getLength1()),
			new SSI2DParamValue(SSI2DParamName.Set_Length_2_D2of5, length.getLength2()),
		};
		
		paramValueList = new SSI2DParamValueList(paramValue);
		
		try {
			mReader.getBarcode().setBarcodeParam(paramValueList.getBytes(mModuleBarcodeType.getCode()));
		} catch (ATException e) {
			ATLog.e(TAG, e, "ERROR. saveSSI2DBarcodeParameter() - Failed to set Barcode Option(D2of5)");
			return false;
		}
		
		//
		// Codabar
		//
		length = dlgSSI2DBarcodeOption.getCodabarLength();
		paramValue = new SSI2DParamValue[]{
			new SSI2DParamValue(SSI2DParamName.Set_Length_1_Codabar, length.getLength1()),
			new SSI2DParamValue(SSI2DParamName.Set_Length_2_Codabar, length.getLength2()),
			new SSI2DParamValue(SSI2DParamName.CLSI_Editing, 
					dlgSSI2DBarcodeOption.getClsiEditing()),
			new SSI2DParamValue(SSI2DParamName.NOTIS_Editing, 
					dlgSSI2DBarcodeOption.getNotisEditing()),
			new SSI2DParamValue(SSI2DParamName.Codabar_Upper_Lower_Start_Stop_Characters_Dectection, 
					dlgSSI2DBarcodeOption.getCodabarStartStopCharactersDetection()),
		};
		
		paramValueList = new SSI2DParamValueList(paramValue);
		
		try {
			mReader.getBarcode().setBarcodeParam(paramValueList.getBytes(mModuleBarcodeType.getCode()));
		} catch (ATException e) {
			ATLog.e(TAG, e, "ERROR. saveSSI2DBarcodeParameter() - Failed to set Barcode Option(Codabar)");
			return false;
		}
		
		//
		// MSI
		//
		length = dlgSSI2DBarcodeOption.getMsiLength();
		paramValue = new SSI2DParamValue[]{
			new SSI2DParamValue(SSI2DParamName.Set_Length_1_MSI, length.getLength1()),
			new SSI2DParamValue(SSI2DParamName.Set_Length_2_MSI, length.getLength2()),
			new SSI2DParamValue(SSI2DParamName.MSI_Check_Digits, 
					dlgSSI2DBarcodeOption.getMSICheckDigit()),
			new SSI2DParamValue(SSI2DParamName.Transmit_MSI_Check_Digits, 
					dlgSSI2DBarcodeOption.getTransmitMsiCheckDigit()),
			new SSI2DParamValue(SSI2DParamName.MSI_Check_Digit_Algorithm, 
					dlgSSI2DBarcodeOption.getMSICheckDigitAlgorithm()),
		};
		
		paramValueList = new SSI2DParamValueList(paramValue);
		
		try {
			mReader.getBarcode().setBarcodeParam(paramValueList.getBytes(mModuleBarcodeType.getCode()));
		} catch (ATException e) {
			ATLog.e(TAG, e, "ERROR. saveSSI2DBarcodeParameter() - Failed to set Barcode Option(MSI)");
			return false;
		}
		
		//
		// Matrix 2of5
		//
		length = dlgSSI2DBarcodeOption.getMatrix2of5Length();
		paramValue = new SSI2DParamValue[]{
			new SSI2DParamValue(SSI2DParamName.Set_Length_1_Matrix2of5, length.getLength1()),
			new SSI2DParamValue(SSI2DParamName.Set_Length_2_Matrix2of5, length.getLength2()),
			new SSI2DParamValue(SSI2DParamName.Matrix2of5_Check_Digit, 
					dlgSSI2DBarcodeOption.getMatrix2of5CheckDigit()),
			new SSI2DParamValue(SSI2DParamName.Transmit_Matrix2of5_Check_Digit, 
					dlgSSI2DBarcodeOption.getTransmitMatrix2of5CheckDigit()),
		};
		
		paramValueList = new SSI2DParamValueList(paramValue);
		
		try {
			mReader.getBarcode().setBarcodeParam(paramValueList.getBytes(mModuleBarcodeType.getCode()));
		} catch (ATException e) {
			ATLog.e(TAG, e, "ERROR. saveSSI2DBarcodeParameter() - Failed to set Barcode Option(Matrix 2of5)");
			return false;
		}
		
		//
		// Postal Codes
		//

		paramValue = new SSI2DParamValue[]{
			new SSI2DParamValue(SSI2DParamName.Transmit_US_Postal_Check_Digit, 
					dlgSSI2DBarcodeOption.getTransmitUsPostalCheckDigit()),
			new SSI2DParamValue(SSI2DParamName.Transmit_UK_Postal_Check_Digit, 
					dlgSSI2DBarcodeOption.getTransmitUkPostalCheckDigit()),
			new SSI2DParamValue(SSI2DParamName.Australia_Post_Format, 
					dlgSSI2DBarcodeOption.getAustraliaPostFormat()),
		};
		
		paramValueList = new SSI2DParamValueList(paramValue);
		
		try {
			mReader.getBarcode().setBarcodeParam(paramValueList.getBytes(mModuleBarcodeType.getCode()));
		} catch (ATException e) {
			ATLog.e(TAG, e, "ERROR. saveSSI2DBarcodeParameter() - Failed to set Barcode Option(Postal Codes)");
			return false;
		}
		
		//
		// GS1 Databar
		//

		paramValue = new SSI2DParamValue[]{
			new SSI2DParamValue(SSI2DParamName.GS1_DataBar_Limited_Security_Level, 
					dlgSSI2DBarcodeOption.getGs1DatabarLimitedSecurityLevel()),
			new SSI2DParamValue(SSI2DParamName.Convert_GS1_DataBar_to_UPC_EAN, 
					dlgSSI2DBarcodeOption.getConvertGs1DatabarToUpcEan()),
		};
		
		paramValueList = new SSI2DParamValueList(paramValue);
		
		try {
			mReader.getBarcode().setBarcodeParam(paramValueList.getBytes(mModuleBarcodeType.getCode()));
		} catch (ATException e) {
			ATLog.e(TAG, e, "ERROR. saveSSI2DBarcodeParameter() - Failed to set Barcode Option(GS1 Databar)");
			return false;
		}
		
		//
		// Composite
		//
		
		paramValue = new SSI2DParamValue[]{
			new SSI2DParamValue(SSI2DParamName.UPC_Composite_Mode, 
					dlgSSI2DBarcodeOption.getUpcCompositeMode()),
			new SSI2DParamValue(SSI2DParamName.Composite_Beep_Mode, 
					dlgSSI2DBarcodeOption.getCompositeBeepMode()),
			new SSI2DParamValue(SSI2DParamName.GS1_128_Emulation_Mode_for_UCC_EAN_Composite_Codes, 
					dlgSSI2DBarcodeOption.getGs1128Emulation()),
		};
		
		paramValueList = new SSI2DParamValueList(paramValue);
		
		try {
			mReader.getBarcode().setBarcodeParam(paramValueList.getBytes(mModuleBarcodeType.getCode()));
		} catch (ATException e) {
			ATLog.e(TAG, e, "ERROR. saveSSI2DBarcodeParameter() - Failed to set Barcode Option(Composite)");
			return false;
		}
		
		//
		// 2D symbol
		//
		
		paramValue = new SSI2DParamValue[]{
			new SSI2DParamValue(SSI2DParamName.Code128_Emulation, 
					dlgSSI2DBarcodeOption.getCode128Emulation()),
			new SSI2DParamValue(SSI2DParamName.Data_Matrix_Inverse, 
					dlgSSI2DBarcodeOption.getDataMatrixInverse()),
			new SSI2DParamValue(SSI2DParamName.Decode_Mirror_Images, 
					dlgSSI2DBarcodeOption.getDecodeMirrorImages()),
			new SSI2DParamValue(SSI2DParamName.QR_Inverse, 
					dlgSSI2DBarcodeOption.getQRInverse()),
			new SSI2DParamValue(SSI2DParamName.Aztec_Inverse, 
					dlgSSI2DBarcodeOption.getAztecInverse()),
			new SSI2DParamValue(SSI2DParamName.HanXin_Inverse, 
					dlgSSI2DBarcodeOption.getHanXinInverse()),
		};
		
		paramValueList = new SSI2DParamValueList(paramValue);
		
		try {
			mReader.getBarcode().setBarcodeParam(paramValueList.getBytes(mModuleBarcodeType.getCode()));
		} catch (ATException e) {
			ATLog.e(TAG, e, "ERROR. saveSSI2DBarcodeParameter() - Failed to set Barcode Option(2D symbol)");
			return false;
		}
		
		//
		// 1D Quiet Zone Level
		//

		paramValue = new SSI2DParamValue[]{
			new SSI2DParamValue(SSI2DParamName.Quiet_Zone_Level_1D, 
					dlgSSI2DBarcodeOption.get1DQuietZoneLevel()),
			new SSI2DParamValue(SSI2DParamName.Intercharacter_Gap_Size, 
					dlgSSI2DBarcodeOption.getIntercharacterGapSize()),
		};
		
		paramValueList = new SSI2DParamValueList(paramValue);
		
		try {
			mReader.getBarcode().setBarcodeParam(paramValueList.getBytes(mModuleBarcodeType.getCode()));
		} catch (ATException e) {
			ATLog.e(TAG, e, "ERROR. saveSSI2DBarcodeParameter() - Failed to set Barcode Option(1D Quiet Zone Level)");
			return false;
		}
		
		// Inverse 1D
		// Redundancy Level
		// Security Level

		paramValue = new SSI2DParamValue[]{
				new SSI2DParamValue(SSI2DParamName.Inverse_1D, 
						dlgSSI2DBarcodeOption.getInverse1D()),
				new SSI2DParamValue(SSI2DParamName.Redundancy_Level, 
						dlgSSI2DBarcodeOption.getRedundancyLevel()),
				new SSI2DParamValue(SSI2DParamName.Security_Level, 
						dlgSSI2DBarcodeOption.getSecurityLevel()),
			};
			
			paramValueList = new SSI2DParamValueList(paramValue);
			
			try {
				mReader.getBarcode().setBarcodeParam(paramValueList.getBytes(mModuleBarcodeType.getCode()));
			} catch (ATException e) {
				ATLog.e(TAG, e, "ERROR. saveSSI2DBarcodeParameter() - Failed to set Barcode Option(ETC)");
				return false;
			}

		
		ATLog.i(TAG, INFO, "INFO. saveSSI2DBarcodeParameter()");
		return true;
	}
	
	private boolean loadSPC2DBarcodeParameter() throws ATException {
		String data = null;
		SPCParamNameList paramNameList = null;
		SPCParamValueList paramValueList = null;
		
		// ---------------------------------
		// Codabar
		// ---------------------------------
		if(mIsThreadAlive){
			
			paramNameList = new SPCParamNameList( new SPCParamName[] {
				SPCParamName.CodabarStartStopCharacters,
				SPCParamName.CodabarCheckCharacter,
				SPCParamName.CodabarConcatenation,
				SPCParamName.CodabarMessageLengthMin,
				SPCParamName.CodabarMessageLengthMax
			});
			
			try {
				data = mReader.getBarcode().getBarcodeParamString(paramNameList.getQueryCommand(mModuleBarcodeType.getCode()));
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSPC2DBarcodeParameter() - Failed to load Barcode Option (Codabar) , Pamram List");
				throw e;
			}
			
			try {
				paramValueList = SPCParamValueList.parseParams(data, mModuleBarcodeType.getCode());
				dlgSPC2DBarcodeOption.setCodabarStartStopCharacters(
						(Boolean) paramValueList.getValueAt(SPCParamName.CodabarStartStopCharacters));
				dlgSPC2DBarcodeOption.setCodabarCheckCharacter(
						(CodabarCheckCharacter) paramValueList.getValueAt(SPCParamName.CodabarCheckCharacter));
				dlgSPC2DBarcodeOption.setCodabarConcatenation(
						(CodabarConcatenation) paramValueList.getValueAt(SPCParamName.CodabarConcatenation));
				dlgSPC2DBarcodeOption.setCodabarLengthMin(
						(Integer) paramValueList.getValueAt(SPCParamName.CodabarMessageLengthMin));
				dlgSPC2DBarcodeOption.setCodabarLengthMax(
						(Integer) paramValueList.getValueAt(SPCParamName.CodabarMessageLengthMax));
				
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSPC2DBarcodeParameter() - Failed to load Barcode Option (Codabar) , Parsing Error");
				throw e;
			}
			
		} else {
			return false;
		}
		// ---------------------------------
		// Code39
		// ---------------------------------
		if(mIsThreadAlive){
			paramNameList = new SPCParamNameList( new SPCParamName[] {
				SPCParamName.Code39StartStopCharacters,	
				SPCParamName.Code39CheckCharacter,	
				SPCParamName.Code39MessageLengthMin,	
				SPCParamName.Code39MessageLengthMax,	
				SPCParamName.Code39Append,	
				SPCParamName.Code39FullASCII,	
				SPCParamName.Code39CodePage	
			});
			
			try {
				data = mReader.getBarcode().getBarcodeParamString(paramNameList.getQueryCommand(mModuleBarcodeType.getCode()));
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSPC2DBarcodeParameter() - Failed to load Barcode Option (Code39) , Pamram List");
				throw e;
			}
			
			try {
				paramValueList = SPCParamValueList.parseParams(data, mModuleBarcodeType.getCode());
				dlgSPC2DBarcodeOption.setCode39StartStopCharacters(
						(Boolean) paramValueList.getValueAt(SPCParamName.Code39StartStopCharacters));
				dlgSPC2DBarcodeOption.setCode39CheckCharacter(
						(Code39CheckCharacter) paramValueList.getValueAt(SPCParamName.Code39CheckCharacter));
				dlgSPC2DBarcodeOption.setCode39LengthMin(
						(Integer) paramValueList.getValueAt(SPCParamName.Code39MessageLengthMin));
				dlgSPC2DBarcodeOption.setCode39LengthMax(
						(Integer) paramValueList.getValueAt(SPCParamName.Code39MessageLengthMax));
				dlgSPC2DBarcodeOption.setCode39Append(
						(Boolean) paramValueList.getValueAt(SPCParamName.Code39Append));
				dlgSPC2DBarcodeOption.setCode39FullASCII(
						(Boolean) paramValueList.getValueAt(SPCParamName.Code39FullASCII));
				dlgSPC2DBarcodeOption.setCode39CodePage(
						(CodePages) paramValueList.getValueAt(SPCParamName.Code39CodePage));
				
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSPC2DBarcodeParameter() - Failed to load Barcode Option (Code39) , Parsing Error");
				throw e;
			}
			
		} else {
			return false;
		}
		// ---------------------------------
		// I2of5
		// ---------------------------------
		if(mIsThreadAlive){
			paramNameList = new SPCParamNameList( new SPCParamName[] {
				SPCParamName.I2of5CheckDigit,	
				SPCParamName.I2of5MessageLengthMin,	
				SPCParamName.I2of5MessageLengthMax,	
					
			});
			
			try {
				data = mReader.getBarcode().getBarcodeParamString(paramNameList.getQueryCommand(mModuleBarcodeType.getCode()));
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSPC2DBarcodeParameter() - Failed to load Barcode Option (I2of5) , Pamram List");
				throw e;
			}
			
			try {
				paramValueList = SPCParamValueList.parseParams(data, mModuleBarcodeType.getCode());
				dlgSPC2DBarcodeOption.setI2of5CheckDigit(
						(CheckDigit) paramValueList.getValueAt(SPCParamName.I2of5CheckDigit));
				dlgSPC2DBarcodeOption.setI2of5LengthMin(
						(Integer) paramValueList.getValueAt(SPCParamName.I2of5MessageLengthMin));
				dlgSPC2DBarcodeOption.setI2of5LengthMax(
						(Integer) paramValueList.getValueAt(SPCParamName.I2of5MessageLengthMax));
				
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSPC2DBarcodeParameter() - Failed to load Barcode Option (I2of5) , Parsing Error");
				throw e;
			}
		} else {
			return false;
		}
		// ---------------------------------
		// NEC2of5
		// ---------------------------------
		if(mIsThreadAlive){
			paramNameList = new SPCParamNameList( new SPCParamName[] {
				SPCParamName.NEC2of5CheckDigit,	
				SPCParamName.NEC2of5MessageLengthMin,	
				SPCParamName.NEC2of5MessageLengthMax,	
			});
			
			try {
				data = mReader.getBarcode().getBarcodeParamString(paramNameList.getQueryCommand(mModuleBarcodeType.getCode()));
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSPC2DBarcodeParameter() - Failed to load Barcode Option (NEC2of5) , Pamram List");
				throw e;
			}
			
			try {
				paramValueList = SPCParamValueList.parseParams(data, mModuleBarcodeType.getCode());
				dlgSPC2DBarcodeOption.setNEC2of5CheckDigit(
						(CheckDigit) paramValueList.getValueAt(SPCParamName.NEC2of5CheckDigit));
				dlgSPC2DBarcodeOption.seNEC2of5tLengthMin(
						(Integer) paramValueList.getValueAt(SPCParamName.NEC2of5MessageLengthMin));
				dlgSPC2DBarcodeOption.setNEC2of5LengthMax(
						(Integer) paramValueList.getValueAt(SPCParamName.NEC2of5MessageLengthMax));
				
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSPC2DBarcodeParameter() - Failed to load Barcode Option (NEC2of5) , Parsing Error");
				throw e;
			}
		} else {
			return false;
		}
		// ---------------------------------
		// Code93
		// ---------------------------------
		if(mIsThreadAlive){
			paramNameList = new SPCParamNameList( new SPCParamName[] {
				SPCParamName.Code93MessageLengthMin,	
				SPCParamName.Code93MessageLengthMax,	
				SPCParamName.Code93Append,	
				SPCParamName.Code93CodePage,	
			});
			
			try {
				data = mReader.getBarcode().getBarcodeParamString(paramNameList.getQueryCommand(mModuleBarcodeType.getCode()));
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSPC2DBarcodeParameter() - Failed to load Barcode Option (Code93) , Pamram List");
				throw e;
			}
			
			try {
				paramValueList = SPCParamValueList.parseParams(data, mModuleBarcodeType.getCode());
				dlgSPC2DBarcodeOption.setCode93LengthMin(
						(Integer) paramValueList.getValueAt(SPCParamName.Code93MessageLengthMin));
				dlgSPC2DBarcodeOption.setCode93LengthMax(
						(Integer) paramValueList.getValueAt(SPCParamName.Code93MessageLengthMax));
				dlgSPC2DBarcodeOption.setCode93Append(
						(Boolean) paramValueList.getValueAt(SPCParamName.Code93Append));
				dlgSPC2DBarcodeOption.setCode93CodePage(
						(CodePages) paramValueList.getValueAt(SPCParamName.Code93CodePage));
				
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSPC2DBarcodeParameter() - Failed to load Barcode Option (Code93) , Parsing Error");
				throw e;
			}
		} else {
			return false;
		}

		// ---------------------------------
		// Straight2of5Industrial
		// ---------------------------------
		if(mIsThreadAlive){
			paramNameList = new SPCParamNameList( new SPCParamName[] {
				SPCParamName.Straight2of5IndustrialMessageLengthMin,	
				SPCParamName.Straight2of5IndustrialMessageLengthMax,	

			});
			
			try {
				data = mReader.getBarcode().getBarcodeParamString(paramNameList.getQueryCommand(mModuleBarcodeType.getCode()));
			} catch (ATException e) {
				ATLog.e(TAG, e,
						"ERROR. loadSPC2DBarcodeParameter() - Failed to load Barcode Option (Straight2of5Industrial) , Pamram List");
				throw e;
			}
			
			try {
				paramValueList = SPCParamValueList.parseParams(data, mModuleBarcodeType.getCode());
				dlgSPC2DBarcodeOption.setStraight2of5IndustrialLengthMin(
						(Integer) paramValueList.getValueAt(SPCParamName.Straight2of5IndustrialMessageLengthMin));
				dlgSPC2DBarcodeOption.setStraight2of5IndustrialLengthMax(
						(Integer) paramValueList.getValueAt(SPCParamName.Straight2of5IndustrialMessageLengthMax));
				
			} catch (ATException e) {
				ATLog.e(TAG, e,
						"ERROR. loadSPC2DBarcodeParameter() - Failed to load Barcode Option (Straight2of5Industrial) , Parsing Error");
				throw e;
			}
		} else {
			return false;
		}
		// ---------------------------------
		// Straight2of5IATA
		// ---------------------------------
		if(mIsThreadAlive){
			paramNameList = new SPCParamNameList( new SPCParamName[] {
				SPCParamName.Straight2of5IATAMessageLengthMin,	
				SPCParamName.Straight2of5IATAMessageLengthMax	
			});
			
			try {
				data = mReader.getBarcode().getBarcodeParamString(paramNameList.getQueryCommand(mModuleBarcodeType.getCode()));
			} catch (ATException e) {
				ATLog.e(TAG, e,
						"ERROR. loadSPC2DBarcodeParameter() - Failed to load Barcode Option (Straight2of5IATA) , Pamram List");
						
				throw e;
			}
			
			try {
				paramValueList = SPCParamValueList.parseParams(data, mModuleBarcodeType.getCode());
				dlgSPC2DBarcodeOption.setStraight2of5IATALengthMin(
						(Integer) paramValueList.getValueAt(SPCParamName.Straight2of5IATAMessageLengthMin));
				dlgSPC2DBarcodeOption.setStraight2of5IATALengthMax(
						(Integer) paramValueList.getValueAt(SPCParamName.Straight2of5IATAMessageLengthMax));
				
			} catch (ATException e) {
				ATLog.e(TAG, e,
						"ERROR. loadSPC2DBarcodeParameter() - Failed to load Barcode Option (Straight2of5IATA) , Parsing Error");
				throw e;
			}
		} else {
			return false;
		}
		// ---------------------------------
		// Matrix2of5
		// ---------------------------------
		if(mIsThreadAlive){
			paramNameList = new SPCParamNameList( new SPCParamName[] {
				SPCParamName.Matrix2of5MessageLengthMin,	
				SPCParamName.Matrix2of5MessageLengthMax,	
			});
			
			try {
				data = mReader.getBarcode().getBarcodeParamString(paramNameList.getQueryCommand(mModuleBarcodeType.getCode()));
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSPC2DBarcodeParameter() - Failed to load Barcode Option (Matrix2of5) , Pamram List");
				throw e;
			}
			
			try {
				paramValueList = SPCParamValueList.parseParams(data, mModuleBarcodeType.getCode());
				dlgSPC2DBarcodeOption.setMatrix2of5LengthMin(
						(Integer) paramValueList.getValueAt(SPCParamName.Matrix2of5MessageLengthMin));
				dlgSPC2DBarcodeOption.setMatrix2of5LengthMax(
						(Integer) paramValueList.getValueAt(SPCParamName.Matrix2of5MessageLengthMax));
				
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSPC2DBarcodeParameter() - Failed to load Barcode Option (Matrix2of5) , Parsing Error");
				throw e;
			}
		} else {
			return false;
		}
		// ---------------------------------
		// Code11
		// ---------------------------------
		if(mIsThreadAlive){
			paramNameList = new SPCParamNameList( new SPCParamName[] {
				SPCParamName.Code11CheckDigitsRequired,	
				SPCParamName.Code11MessageLengthMin,	
				SPCParamName.Code11MessageLengthMax,	
					
			});
			
			try {
				data = mReader.getBarcode().getBarcodeParamString(paramNameList.getQueryCommand(mModuleBarcodeType.getCode()));
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSPC2DBarcodeParameter() - Failed to load Barcode Option (Code11) , Pamram List");
				throw e;
			}
			
			try {
				paramValueList = SPCParamValueList.parseParams(data, mModuleBarcodeType.getCode());
				dlgSPC2DBarcodeOption.setCode11CheckDigitsRequired(
						(Code11CheckDigitsRequired) paramValueList.getValueAt(SPCParamName.Code11CheckDigitsRequired));
				dlgSPC2DBarcodeOption.setCode11LengthMin(
						(Integer) paramValueList.getValueAt(SPCParamName.Code11MessageLengthMin));
				dlgSPC2DBarcodeOption.setCode11LengthMax(
						(Integer) paramValueList.getValueAt(SPCParamName.Code11MessageLengthMax));				
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSPC2DBarcodeParameter() - Failed to load Barcode Option (Code11) , Parsing Error");
				throw e;
			}
		} else {
			return false;
		}
		// ---------------------------------
		// Code128
		// ---------------------------------
		if(mIsThreadAlive){
			paramNameList = new SPCParamNameList( new SPCParamName[] {
				SPCParamName.ISBT128Concatenation,	
				SPCParamName.Code128MessageLengthMin,	
				SPCParamName.Code128MessageLengthMax,	
				SPCParamName.Code128Append,	
				SPCParamName.Code128CodePage,	

			});
			
			try {
				data = mReader.getBarcode().getBarcodeParamString(paramNameList.getQueryCommand(mModuleBarcodeType.getCode()));
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSPC2DBarcodeParameter() - Failed to load Barcode Option (Code128) , Pamram List");
				throw e;
			}
			
			try {
				paramValueList = SPCParamValueList.parseParams(data, mModuleBarcodeType.getCode());
				dlgSPC2DBarcodeOption.setISBT128Concatenation(
						(Boolean) paramValueList.getValueAt(SPCParamName.ISBT128Concatenation));
				dlgSPC2DBarcodeOption.setCode128LengthMin(
						(Integer) paramValueList.getValueAt(SPCParamName.Code128MessageLengthMin));
				dlgSPC2DBarcodeOption.setCode128LengthMax(
						(Integer) paramValueList.getValueAt(SPCParamName.Code128MessageLengthMax));
				dlgSPC2DBarcodeOption.setCode128Append(
						(Boolean) paramValueList.getValueAt(SPCParamName.Code128Append));
				dlgSPC2DBarcodeOption.setCode128CodePage(
						(CodePages) paramValueList.getValueAt(SPCParamName.Code128CodePage));
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSPC2DBarcodeParameter() - Failed to load Barcode Option (Code128) , Parsing Error");
				throw e;
			}
		} else {
			return false;
		}
		// ---------------------------------
		// GS1-128
		// ---------------------------------
		if(mIsThreadAlive){
			paramNameList = new SPCParamNameList( new SPCParamName[] {
				SPCParamName.GS1128MessageLengthMin,	
				SPCParamName.GS1128MessageLengthMax,	
					
			});
			
			try {
				data = mReader.getBarcode().getBarcodeParamString(paramNameList.getQueryCommand(mModuleBarcodeType.getCode()));
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSPC2DBarcodeParameter() - Failed to load Barcode Option (GS1-128) , Pamram List");
				throw e;
			}
			
			try {
				paramValueList = SPCParamValueList.parseParams(data, mModuleBarcodeType.getCode());
				dlgSPC2DBarcodeOption.setGS1128LengthMin(
						(Integer) paramValueList.getValueAt(SPCParamName.GS1128MessageLengthMin));
				dlgSPC2DBarcodeOption.setGS1128LengthMax(
						(Integer) paramValueList.getValueAt(SPCParamName.GS1128MessageLengthMax));
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSPC2DBarcodeParameter() - Failed to load Barcode Option (GS1-128) , Parsing Error");
				throw e;
			}
		} else {
			return false;
		}
		// ---------------------------------
		// UpcA
		// ---------------------------------
		if(mIsThreadAlive){
			paramNameList = new SPCParamNameList( new SPCParamName[] {
				SPCParamName.UPCACheckDigit,	
				SPCParamName.UPCANumberSystem,	
				SPCParamName.UPCAAddenda2Digit,	
				SPCParamName.UPCAAddenda5Digit,	
				SPCParamName.UPCAAddendaRequired,	
				//SPCParamName.AddendaTimeout,	
				SPCParamName.UPCAAddendaSeparator,
			});
			
			try {
				data = mReader.getBarcode().getBarcodeParamString(paramNameList.getQueryCommand(mModuleBarcodeType.getCode()));
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSPC2DBarcodeParameter() - Failed to load Barcode Option (UpcA) , Pamram List");
				throw e;
			}
			
			try {
				paramValueList = SPCParamValueList.parseParams(data, mModuleBarcodeType.getCode());
				dlgSPC2DBarcodeOption.setUPCACheckDigit(
						(Boolean) paramValueList.getValueAt(SPCParamName.UPCACheckDigit));
				dlgSPC2DBarcodeOption.setUPCANumberSystem(
						(Boolean) paramValueList.getValueAt(SPCParamName.UPCANumberSystem));
				dlgSPC2DBarcodeOption.setUPCAAddenda2Digit(
						(Boolean) paramValueList.getValueAt(SPCParamName.UPCAAddenda2Digit));
				dlgSPC2DBarcodeOption.setUPCAAddenda5Digit(
						(Boolean) paramValueList.getValueAt(SPCParamName.UPCAAddenda5Digit));
				dlgSPC2DBarcodeOption.setUPCAAddendaRequired(
						(Boolean) paramValueList.getValueAt(SPCParamName.UPCAAddendaRequired));
//				dlgSPC2DBarcodeOption.setUpcAAddendaTimeout(
//						(Integer) paramValueList.getValueAt(SPCParamName.AddendaTimeout));
				dlgSPC2DBarcodeOption.setUPCAAddendaSeparator(
						(Boolean) paramValueList.getValueAt(SPCParamName.UPCAAddendaSeparator));
				
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSPC2DBarcodeParameter() - Failed to load Barcode Option (UpcA) , Parsing Error");
				throw e;
			}
			

		} else {
			return false;
		}
		// ---------------------------------
		// UpcE0
		// ---------------------------------
		if(mIsThreadAlive){
			paramNameList = new SPCParamNameList( new SPCParamName[] {
				SPCParamName.UPCE0Expand,
				SPCParamName.UPCE0AddendaRequired,
				SPCParamName.UPCE0AddendaSeparator,
				SPCParamName.UPCE0CheckDigit,
				SPCParamName.UPCE0LeadingZero,
				SPCParamName.UPCE0Addenda2Digit,
				SPCParamName.UPCE0Addenda5Digit,
								
			});
			
			try {
				data = mReader.getBarcode().getBarcodeParamString(paramNameList.getQueryCommand(mModuleBarcodeType.getCode()));
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSPC2DBarcodeParameter() - Failed to load Barcode Option (UpcE0) , Pamram List");
				throw e;
			}
			
			try {
				paramValueList = SPCParamValueList.parseParams(data, mModuleBarcodeType.getCode());
				dlgSPC2DBarcodeOption.setUPCE0Expand(
						(Boolean) paramValueList.getValueAt(SPCParamName.UPCE0Expand));
				dlgSPC2DBarcodeOption.setUPCE0AddendaRequired(
						(Boolean) paramValueList.getValueAt(SPCParamName.UPCE0AddendaRequired));
				dlgSPC2DBarcodeOption.setUPCE0AddendaSeparator(
						(Boolean) paramValueList.getValueAt(SPCParamName.UPCE0AddendaSeparator));
				dlgSPC2DBarcodeOption.setUPCE0CheckDigit(
						(Boolean) paramValueList.getValueAt(SPCParamName.UPCE0CheckDigit));
				dlgSPC2DBarcodeOption.setUPCE0LeadingZero(
						(Boolean) paramValueList.getValueAt(SPCParamName.UPCE0LeadingZero));
				dlgSPC2DBarcodeOption.setUPCE0Addenda2Digit(
						(Boolean) paramValueList.getValueAt(SPCParamName.UPCE0Addenda2Digit));
				dlgSPC2DBarcodeOption.setUPCE0Addenda5Digit(
						(Boolean) paramValueList.getValueAt(SPCParamName.UPCE0Addenda5Digit));

			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSPC2DBarcodeParameter() - Failed to load Barcode Option (UpcE0) , Parsing Error");
				throw e;
			}
		} else {
			return false;
		}
		// ---------------------------------
		// EanJan13
		// ---------------------------------
		if(mIsThreadAlive){
			paramNameList = new SPCParamNameList( new SPCParamName[] {
				SPCParamName.ConvertUPCAtoEAN13,
				SPCParamName.EANJAN13CheckDigit,
				SPCParamName.EANJAN13Addenda2Digit,
				SPCParamName.EANJAN13Addenda5Digit,
				SPCParamName.EANJAN13AddendaRequired,
				SPCParamName.EANJAN13AddendaSeparator,
				SPCParamName.ISBNTranslate,

			});
			
			try {
				data = mReader.getBarcode().getBarcodeParamString(paramNameList.getQueryCommand(mModuleBarcodeType.getCode()));
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSPC2DBarcodeParameter() - Failed to load Barcode Option (EanJan13) , Pamram List");
				throw e;
			}
			
			try {
				paramValueList = SPCParamValueList.parseParams(data, mModuleBarcodeType.getCode());
				dlgSPC2DBarcodeOption.setConvertUPCAtoEAN13(
						(Boolean) paramValueList.getValueAt(SPCParamName.ConvertUPCAtoEAN13));
				dlgSPC2DBarcodeOption.setEANJAN13CheckDigit(
						(Boolean) paramValueList.getValueAt(SPCParamName.EANJAN13CheckDigit));
				dlgSPC2DBarcodeOption.setEANJAN13Addenda2Digit(
						(Boolean) paramValueList.getValueAt(SPCParamName.EANJAN13Addenda2Digit));
				dlgSPC2DBarcodeOption.setEANJAN13Addenda5Digit(
						(Boolean) paramValueList.getValueAt(SPCParamName.EANJAN13Addenda5Digit));
				dlgSPC2DBarcodeOption.setEANJAN13AddendaRequired(
						(Boolean) paramValueList.getValueAt(SPCParamName.EANJAN13AddendaRequired));
				dlgSPC2DBarcodeOption.setEANJAN13AddendaSeparator(
						(Boolean) paramValueList.getValueAt(SPCParamName.EANJAN13AddendaSeparator));
				dlgSPC2DBarcodeOption.setISBNTranslate(
						(Boolean) paramValueList.getValueAt(SPCParamName.ISBNTranslate));

			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSPC2DBarcodeParameter() - Failed to load Barcode Option (EanJan13) , Parsing Error");
				throw e;
			}
		} else {
			return false;
		}
		// ---------------------------------
		// EanJan8
		// ---------------------------------
		if(mIsThreadAlive){
			paramNameList = new SPCParamNameList( new SPCParamName[] {
				SPCParamName.EANJAN8CheckDigit,
				SPCParamName.EANJAN8Addenda2Digit,
				SPCParamName.EANJAN8Addenda5Digit,
				SPCParamName.EANJAN8AddendaRequired,
				SPCParamName.EANJAN8AddendaSeparator,
			});
			
			try {
				data = mReader.getBarcode().getBarcodeParamString(paramNameList.getQueryCommand(mModuleBarcodeType.getCode()));
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSPC2DBarcodeParameter() - Failed to load Barcode Option (EanJan8) , Pamram List");
				throw e;
			}
			
			try {
				paramValueList = SPCParamValueList.parseParams(data, mModuleBarcodeType.getCode());
				dlgSPC2DBarcodeOption.setEANJAN8CheckDigit(
						(Boolean) paramValueList.getValueAt(SPCParamName.EANJAN8CheckDigit));
				dlgSPC2DBarcodeOption.setEANJAN8Addenda2Digit(
						(Boolean) paramValueList.getValueAt(SPCParamName.EANJAN8Addenda2Digit));
				dlgSPC2DBarcodeOption.setEANJAN8Addenda5Digit(
						(Boolean) paramValueList.getValueAt(SPCParamName.EANJAN8Addenda5Digit));
				dlgSPC2DBarcodeOption.setEANJAN8AddendaRequired(
						(Boolean) paramValueList.getValueAt(SPCParamName.EANJAN8AddendaRequired));
				dlgSPC2DBarcodeOption.setEANJAN8AddendaSeparator(
						(Boolean) paramValueList.getValueAt(SPCParamName.EANJAN8AddendaSeparator));

				
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSPC2DBarcodeParameter() - Failed to load Barcode Option (Codabar) , Parsing Error");
				throw e;
			}
		} else {
			return false;
		}
		// ---------------------------------
		// MSI
		// ---------------------------------
		if(mIsThreadAlive){
			paramNameList = new SPCParamNameList( new SPCParamName[] {
				SPCParamName.MSICheckCharacter,
				SPCParamName.MSIMessageLengthMin,
				SPCParamName.MSIMessageLengthMax,

			});
			
			try {
				data = mReader.getBarcode().getBarcodeParamString(paramNameList.getQueryCommand(mModuleBarcodeType.getCode()));
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSPC2DBarcodeParameter() - Failed to load Barcode Option (Codabar) , Pamram List");
				throw e;
			}
			
			try {
				paramValueList = SPCParamValueList.parseParams(data, mModuleBarcodeType.getCode());
				dlgSPC2DBarcodeOption.setMSICheckCharacter(
						(MSICheckCharacter) paramValueList.getValueAt(SPCParamName.MSICheckCharacter));
				dlgSPC2DBarcodeOption.setMSILengthMin(
						(Integer) paramValueList.getValueAt(SPCParamName.MSIMessageLengthMin));
				dlgSPC2DBarcodeOption.setMSILengthMax(
						(Integer) paramValueList.getValueAt(SPCParamName.MSIMessageLengthMax));

				
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSPC2DBarcodeParameter() - Failed to load Barcode Option (EanJan8) , Parsing Error");
				throw e;
			}
		} else {
			return false;
		}
		// ---------------------------------
		// GS1DataBar
		// ---------------------------------
		if(mIsThreadAlive){
			paramNameList = new SPCParamNameList( new SPCParamName[] {
				SPCParamName.GS1DataBarExpandedMessageLengthMin,
				SPCParamName.GS1DataBarExpandedMessageLengthMax,

			});
			
			try {
				data = mReader.getBarcode().getBarcodeParamString(paramNameList.getQueryCommand(mModuleBarcodeType.getCode()));
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSPC2DBarcodeParameter() - Failed to load Barcode Option (GS1DataBar) , Pamram List");
				throw e;
			}
			
			try {
				paramValueList = SPCParamValueList.parseParams(data, mModuleBarcodeType.getCode());
				dlgSPC2DBarcodeOption.setGS1DataBarLengthMin(
						(Integer) paramValueList.getValueAt(SPCParamName.GS1DataBarExpandedMessageLengthMin));
				dlgSPC2DBarcodeOption.setGS1DataBarLengthMax(
						(Integer) paramValueList.getValueAt(SPCParamName.GS1DataBarExpandedMessageLengthMax));
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSPC2DBarcodeParameter() - Failed to load Barcode Option (GS1DataBar) , Parsing Error");
				throw e;
			}
		} else {
			return false;
		}
		// ---------------------------------
		// CodablockA
		// ---------------------------------
		if(mIsThreadAlive){
			paramNameList = new SPCParamNameList( new SPCParamName[] {
				SPCParamName.CodablockAMessageLengthMin,
				SPCParamName.CodablockAMessageLengthMax,

			});
			
			try {
				data = mReader.getBarcode().getBarcodeParamString(paramNameList.getQueryCommand(mModuleBarcodeType.getCode()));
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSPC2DBarcodeParameter() - Failed to load Barcode Option (CodablockA) , Pamram List");
				throw e;
			}
			
			try {
				paramValueList = SPCParamValueList.parseParams(data, mModuleBarcodeType.getCode());
				dlgSPC2DBarcodeOption.setCodablockALengthMin(
						(Integer) paramValueList.getValueAt(SPCParamName.CodablockAMessageLengthMin));
				dlgSPC2DBarcodeOption.setCodablockALengthMax(
						(Integer) paramValueList.getValueAt(SPCParamName.CodablockAMessageLengthMax));
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSPC2DBarcodeParameter() - Failed to load Barcode Option (CodablockA) , Parsing Error");
				throw e;
			}
		} else {
			return false;
		}
		
		// ---------------------------------
		// CodablockF
		// ---------------------------------
		if(mIsThreadAlive){
			paramNameList = new SPCParamNameList( new SPCParamName[] {
				SPCParamName.CodablockFMessageLengthMin,
				SPCParamName.CodablockFMessageLengthMax,
					
			});
			
			try {
				data = mReader.getBarcode().getBarcodeParamString(paramNameList.getQueryCommand(mModuleBarcodeType.getCode()));
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSPC2DBarcodeParameter() - Failed to load Barcode Option (CodablockF) , Pamram List");
				throw e;
			}
			
			try {
				paramValueList = SPCParamValueList.parseParams(data, mModuleBarcodeType.getCode());
				dlgSPC2DBarcodeOption.setCodablockFLengthMin(
						(Integer) paramValueList.getValueAt(SPCParamName.CodablockFMessageLengthMin));
				dlgSPC2DBarcodeOption.setCodablockFLengthMax(
						(Integer) paramValueList.getValueAt(SPCParamName.CodablockFMessageLengthMax));				
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSPC2DBarcodeParameter() - Failed to load Barcode Option (CodablockF) , Parsing Error");
				throw e;
			}
		} else {
			return false;
		}
		
		// ---------------------------------
		// PDF417
		// ---------------------------------
		if(mIsThreadAlive){
			paramNameList = new SPCParamNameList( new SPCParamName[] {
				SPCParamName.PDF417MessageLengthMin,
				SPCParamName.PDF417MessageLengthMax,
					
			});
			
			try {
				data = mReader.getBarcode().getBarcodeParamString(paramNameList.getQueryCommand(mModuleBarcodeType.getCode()));
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSPC2DBarcodeParameter() - Failed to load Barcode Option (PDF417) , Pamram List");
				throw e;
			}
			
			try {
				paramValueList = SPCParamValueList.parseParams(data, mModuleBarcodeType.getCode());
				dlgSPC2DBarcodeOption.setPDF417LengthMin(
						(Integer) paramValueList.getValueAt(SPCParamName.PDF417MessageLengthMin));
				dlgSPC2DBarcodeOption.setPDF417LengthMax(
						(Integer) paramValueList.getValueAt(SPCParamName.PDF417MessageLengthMax));
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSPC2DBarcodeParameter() - Failed to load Barcode Option (PDF417) , Parsing Error");
				throw e;
			}
		} else {
			return false;
		}
		
		// ---------------------------------
		// MicroPDF417
		// ---------------------------------
		if(mIsThreadAlive){
			paramNameList = new SPCParamNameList( new SPCParamName[] {
				SPCParamName.MicroPDF417MessageLengthMin,
				SPCParamName.MicroPDF417MessageLengthMax,

			});
			
			try {
				data = mReader.getBarcode().getBarcodeParamString(paramNameList.getQueryCommand(mModuleBarcodeType.getCode()));
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSPC2DBarcodeParameter() - Failed to load Barcode Option (MicroPDF417) , Pamram List");
				throw e;
			}
			
			try {
				paramValueList = SPCParamValueList.parseParams(data, mModuleBarcodeType.getCode());
				dlgSPC2DBarcodeOption.setMicroPDF417LengthMin(
						(Integer) paramValueList.getValueAt(SPCParamName.MicroPDF417MessageLengthMin));
				dlgSPC2DBarcodeOption.setMicroPDF417LengthMax(
						(Integer) paramValueList.getValueAt(SPCParamName.MicroPDF417MessageLengthMax));
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSPC2DBarcodeParameter() - Failed to load Barcode Option (MicroPDF417) , Parsing Error");
				throw e;
			}
		} else {
			return false;
		}
		
		// ---------------------------------
		// GS1CompositeCodes
		// ---------------------------------
		if(mIsThreadAlive){
			paramNameList = new SPCParamNameList( new SPCParamName[] {
				SPCParamName.GS1CompositeCodes,
				SPCParamName.UPCEANVersion,
				SPCParamName.GS1CompositeCodeMessageLengthMin,
				SPCParamName.GS1CompositeCodeMessageLengthMax,
					
			});
			
			try {
				data = mReader.getBarcode().getBarcodeParamString(paramNameList.getQueryCommand(mModuleBarcodeType.getCode()));
			} catch (ATException e) {
				ATLog.e(TAG, e,
						"ERROR. loadSPC2DBarcodeParameter() - Failed to load Barcode Option (GS1CompositeCodes) , Pamram List");
				throw e;
			}
			
			try {
				paramValueList = SPCParamValueList.parseParams(data, mModuleBarcodeType.getCode());
				dlgSPC2DBarcodeOption.setGS1CompositeCodes(
						(Boolean) paramValueList.getValueAt(SPCParamName.GS1CompositeCodes));
				dlgSPC2DBarcodeOption.setUPCEANVersion(
						(Boolean) paramValueList.getValueAt(SPCParamName.UPCEANVersion));
				dlgSPC2DBarcodeOption.setGS1CompositeCodesLengthMin(
						(Integer) paramValueList.getValueAt(SPCParamName.GS1CompositeCodeMessageLengthMin));
				dlgSPC2DBarcodeOption.setGS1CompositeCodesLengthMax(
						(Integer) paramValueList.getValueAt(SPCParamName.GS1CompositeCodeMessageLengthMax));
			} catch (ATException e) {
				ATLog.e(TAG, e,
						"ERROR. loadSPC2DBarcodeParameter() - Failed to load Barcode Option (GS1CompositeCodes) , Parsing Error");
				throw e;
			}
		} else {
			return false;
		}
		
		// ---------------------------------
		// QRCode
		// ---------------------------------
		if(mIsThreadAlive){
			paramNameList = new SPCParamNameList( new SPCParamName[] {
					SPCParamName.QRCodeMessageLengthMin,
					SPCParamName.QRCodeMessageLengthMax,
					SPCParamName.QRCodeAppend,
					SPCParamName.QRCodePage,

			});
			
			try {
				data = mReader.getBarcode().getBarcodeParamString(paramNameList.getQueryCommand(mModuleBarcodeType.getCode()));
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSPC2DBarcodeParameter() - Failed to load Barcode Option (QRCode) , Pamram List");
				throw e;
			}
			
			try {
				paramValueList = SPCParamValueList.parseParams(data, mModuleBarcodeType.getCode());
				dlgSPC2DBarcodeOption.setQRCodeLengthMin(
						(Integer) paramValueList.getValueAt(SPCParamName.QRCodeMessageLengthMin));
				dlgSPC2DBarcodeOption.setQRCodeLengthMax(
						(Integer) paramValueList.getValueAt(SPCParamName.QRCodeMessageLengthMax));
				dlgSPC2DBarcodeOption.setQRCodeAppend(
						(Boolean) paramValueList.getValueAt(SPCParamName.QRCodeAppend));
				dlgSPC2DBarcodeOption.setQRCodePage(
						(CodePages) paramValueList.getValueAt(SPCParamName.QRCodePage));
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSPC2DBarcodeParameter() - Failed to load Barcode Option (QRCode) , Parsing Error");
				throw e;
			}
		} else {
			return false;
		}
		
		// ---------------------------------
		// DataMatrix
		// ---------------------------------
		if(mIsThreadAlive){
			paramNameList = new SPCParamNameList( new SPCParamName[] {
					SPCParamName.DataMatrixMessageLengthMin,
					SPCParamName.DataMatrixMessageLengthMax,
//					SPCParamName.DataMatrixAppend,
					SPCParamName.DataMatrixCodePage,

			});
			
			try {
				data = mReader.getBarcode().getBarcodeParamString(paramNameList.getQueryCommand(mModuleBarcodeType.getCode()));
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSPC2DBarcodeParameter() - Failed to load Barcode Option (DataMatrix) , Pamram List");
				throw e;
			}
			
			try {
				paramValueList = SPCParamValueList.parseParams(data, mModuleBarcodeType.getCode());
				dlgSPC2DBarcodeOption.setDataMatrixLengthMin(
						(Integer) paramValueList.getValueAt(SPCParamName.DataMatrixMessageLengthMin));
				dlgSPC2DBarcodeOption.setDataMatrixLengthMax(
						(Integer) paramValueList.getValueAt(SPCParamName.DataMatrixMessageLengthMax));
//				dlgSPC2DBarcodeOption.setDataMatrixAppend(
//						(Boolean) paramValueList.getValueAt(SPCParamName.DataMatrixAppend));
				dlgSPC2DBarcodeOption.setDataMatrixCodePage(
						(CodePages) paramValueList.getValueAt(SPCParamName.DataMatrixCodePage));				
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSPC2DBarcodeParameter() - Failed to load Barcode Option (DataMatrix) , Parsing Error");
				throw e;
			}
		} else {
			return false;
		}
		
		// ---------------------------------
		// MaxiCode
		// ---------------------------------
		if(mIsThreadAlive){
			paramNameList = new SPCParamNameList( new SPCParamName[] {
					SPCParamName.MaxiCodeMessageLengthMin,
					SPCParamName.MaxiCodeMessageLengthMax,

			});
			
			try {
				data = mReader.getBarcode().getBarcodeParamString(paramNameList.getQueryCommand(mModuleBarcodeType.getCode()));
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSPC2DBarcodeParameter() - Failed to load Barcode Option (MaxiCode) , Pamram List");
				throw e;
			}
			
			try {
				paramValueList = SPCParamValueList.parseParams(data, mModuleBarcodeType.getCode());
				dlgSPC2DBarcodeOption.setMaxiCodeLengthMin(
						(Integer) paramValueList.getValueAt(SPCParamName.MaxiCodeMessageLengthMin));
				dlgSPC2DBarcodeOption.setMaxiCodeLengthMax(
						(Integer) paramValueList.getValueAt(SPCParamName.MaxiCodeMessageLengthMax));
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSPC2DBarcodeParameter() - Failed to load Barcode Option (MaxiCode) , Parsing Error");
				throw e;
			}
		} else {
			return false;
		}
		
		// ---------------------------------
		// AztecCode
		// ---------------------------------
		if(mIsThreadAlive){
			paramNameList = new SPCParamNameList( new SPCParamName[] {
					SPCParamName.AztecCodeMessageLengthMin,
					SPCParamName.AztecCodeMessageLengthMax,
					SPCParamName.AztecAppend,
					SPCParamName.AztecCodePage,

			});
			
			try {
				data = mReader.getBarcode().getBarcodeParamString(paramNameList.getQueryCommand(mModuleBarcodeType.getCode()));
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSPC2DBarcodeParameter() - Failed to load Barcode Option (AztecCode) , Pamram List");
				throw e;
			}
			
			try {
				paramValueList = SPCParamValueList.parseParams(data, mModuleBarcodeType.getCode());
				dlgSPC2DBarcodeOption.setAztecCodeLengthMin(
						(Integer) paramValueList.getValueAt(SPCParamName.AztecCodeMessageLengthMin));
				dlgSPC2DBarcodeOption.setAztecCodeLengthMax(
						(Integer) paramValueList.getValueAt(SPCParamName.AztecCodeMessageLengthMax));
				dlgSPC2DBarcodeOption.setAztecAppend(
						(Boolean) paramValueList.getValueAt(SPCParamName.AztecAppend));
				dlgSPC2DBarcodeOption.setAztecCodePage(
						(CodePages) paramValueList.getValueAt(SPCParamName.AztecCodePage));
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSPC2DBarcodeParameter() - Failed to load Barcode Option (AztecCode) , Parsing Error");
				throw e;
			}
		} else {
			return false;
		}
		
		// ---------------------------------
		// HanXinCode
		// ---------------------------------
		if(mIsThreadAlive){
			paramNameList = new SPCParamNameList( new SPCParamName[] {
					SPCParamName.HanXinCodeMessageLengthMin,
					SPCParamName.HanXinCodeMessageLengthMax,

			});
			
			try {
				data = mReader.getBarcode().getBarcodeParamString(paramNameList.getQueryCommand(mModuleBarcodeType.getCode()));
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSPC2DBarcodeParameter() - Failed to load Barcode Option (HanXinCode) , Pamram List");
				throw e;
			}
			
			try {
				paramValueList = SPCParamValueList.parseParams(data, mModuleBarcodeType.getCode());
				dlgSPC2DBarcodeOption.setHanXinCodeLengthMin(
						(Integer) paramValueList.getValueAt(SPCParamName.HanXinCodeMessageLengthMin));
				dlgSPC2DBarcodeOption.setHanXinCodeLengthMax(
						(Integer) paramValueList.getValueAt(SPCParamName.HanXinCodeMessageLengthMax));
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSPC2DBarcodeParameter() - Failed to load Barcode Option (HanXinCode) , Parsing Error");
				throw e;
			}
		} else {
			return false;
		}
		
		// ---------------------------------
		// PostalCodes2D
		// ---------------------------------
		if(mIsThreadAlive){
			paramNameList = new SPCParamNameList( new SPCParamName[] {
					SPCParamName.PlanetCodeCheckDigit,
					SPCParamName.PostnetCheckDigit,
					SPCParamName.AustralianPostInterpretation,

			});
			
			try {
				data = mReader.getBarcode().getBarcodeParamString(paramNameList.getQueryCommand(mModuleBarcodeType.getCode()));
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSPC2DBarcodeParameter() - Failed to load Barcode Option (PostalCodes2D) , Pamram List");
				throw e;
			}
			
			try {
				paramValueList = SPCParamValueList.parseParams(data, mModuleBarcodeType.getCode());
				dlgSPC2DBarcodeOption.setPlanetCodeCheckDigit(
						(Boolean) paramValueList.getValueAt(SPCParamName.PlanetCodeCheckDigit));
				dlgSPC2DBarcodeOption.setPostnetCheckDigit(
						(Boolean) paramValueList.getValueAt(SPCParamName.PostnetCheckDigit));
				dlgSPC2DBarcodeOption.setAustralianPostInterpretation(
						(AustralianPostInterpretation) paramValueList.getValueAt(SPCParamName.AustralianPostInterpretation));
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSPC2DBarcodeParameter() - Failed to load Barcode Option (PostalCodes2D) , Parsing Error");
				throw e;
			}
		} else {
			return false;
		}
		
		
		// ---------------------------------
		// PostalCodesLinear
		// ---------------------------------
		if(mIsThreadAlive){
			paramNameList = new SPCParamNameList( new SPCParamName[] {
					SPCParamName.ChinaPostMessageLengthMin,
					SPCParamName.ChinaPostMessageLengthMax,
					SPCParamName.KoreaPostMessageLengthMin,
					SPCParamName.KoreaPostMessageLengthMax,
					SPCParamName.KoreaPostCheckDigit,

			});
			
			try {
				data = mReader.getBarcode().getBarcodeParamString(paramNameList.getQueryCommand(mModuleBarcodeType.getCode()));
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSPC2DBarcodeParameter() - Failed to load Barcode Option (PostalCodesLinear) , Pamram List");
				throw e;
			}
			
			try {
				paramValueList = SPCParamValueList.parseParams(data, mModuleBarcodeType.getCode());
				dlgSPC2DBarcodeOption.setChinaPostLengthMin(
						(Integer) paramValueList.getValueAt(SPCParamName.ChinaPostMessageLengthMin));
				dlgSPC2DBarcodeOption.setChinaPostLengthMax(
						(Integer) paramValueList.getValueAt(SPCParamName.ChinaPostMessageLengthMax));
				dlgSPC2DBarcodeOption.setKoreaPostLengthMin(
						(Integer) paramValueList.getValueAt(SPCParamName.KoreaPostMessageLengthMin));
				dlgSPC2DBarcodeOption.setKoreaPostLengthMax(
						(Integer) paramValueList.getValueAt(SPCParamName.KoreaPostMessageLengthMax));
				dlgSPC2DBarcodeOption.setKoreaPostCheckDigit(
						(Boolean) paramValueList.getValueAt(SPCParamName.KoreaPostCheckDigit));
				
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSPC2DBarcodeParameter() - Failed to load Barcode Option (PostalCodesLinear) , Parsing Error");
				throw e;
			}
		} else {
			return false;
		}
		
		
		// ---------------------------------
		// ETC
		// ---------------------------------
		// UPCA EAN13 Extended Coupon Code
		// Coupon GS1 DataBar Output
		// GS1 Emulation
		// ---------------------------------
		if(mIsThreadAlive){
			paramNameList = new SPCParamNameList( new SPCParamName[] {
					SPCParamName.UPCAEAN13ExtendedCouponCode,
					SPCParamName.CouponGS1DataBarOutput,
//					SPCParamName.LabelCode,
					SPCParamName.GS1Emulation,
					SPCParamName.VideoReverse,
			});
			
			try {
				data = mReader.getBarcode().getBarcodeParamString(paramNameList.getQueryCommand(mModuleBarcodeType.getCode()));
			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSPC2DBarcodeParameter() - Failed to load Barcode Option (ETC) , Pamram List");
				throw e;
			}
			
			try {
				paramValueList = SPCParamValueList.parseParams(data, mModuleBarcodeType.getCode());
				dlgSPC2DBarcodeOption.setUPCAEAN13ExtendedCouponCode(
						(UPCAEAN13ExtendedCouponCode) paramValueList.getValueAt(SPCParamName.UPCAEAN13ExtendedCouponCode));
				dlgSPC2DBarcodeOption.setCouponGS1DataBarOutput(
						(Boolean) paramValueList.getValueAt(SPCParamName.CouponGS1DataBarOutput));
//				dlgSPC2DBarcodeOption.setLabelCode(
//						(Boolean) paramValueList.getValueAt(SPCParamName.LabelCode));
				dlgSPC2DBarcodeOption.setGS1Emulation(
						(GS1Emulation) paramValueList.getValueAt(SPCParamName.GS1Emulation));
				dlgSPC2DBarcodeOption.setVideoReverse(
						(VideoReverse) paramValueList.getValueAt(SPCParamName.VideoReverse));

			} catch (ATException e) {
				ATLog.e(TAG, e,"ERROR. loadSPC2DBarcodeParameter() - Failed to load Barcode Option (ETC) , Parsing Error");
				throw e;
			}
		} else {
			return false;
		}
		
		ATLog.i(TAG, INFO, "INFO. loadSPC2DBarcodeParameter()");
		return true;
	}
	
	private boolean saveSPC2DBarcodeParameter() {
		SPCParamValueList paramValueList = null;
		SPCParamValue[] paramValue = null;
		
		// ---------------------------------
		// Codabar
		// ---------------------------------
		paramValue = new SPCParamValue[]{
				new SPCParamValue(SPCParamName.CodabarStartStopCharacters,
						dlgSPC2DBarcodeOption.getCodabarStartStopCharacters()),
				new SPCParamValue(SPCParamName.CodabarCheckCharacter,
						dlgSPC2DBarcodeOption.getCodabarCheckCharacter()),
				new SPCParamValue(SPCParamName.CodabarConcatenation,
						dlgSPC2DBarcodeOption.getCodabarConcatenation()),
				new SPCParamValue(SPCParamName.CodabarMessageLengthMin,
						dlgSPC2DBarcodeOption.getCodabarLengthMin()),
				new SPCParamValue(SPCParamName.CodabarMessageLengthMax,
						dlgSPC2DBarcodeOption.getCodabarLengthMax()),
		};
		
		paramValueList = new SPCParamValueList(paramValue);
		
		try {
			mReader.getBarcode().setBarcodeParamString(paramValueList.getCommand(mModuleBarcodeType.getCode()));
		} catch (ATException e) {
			ATLog.e(TAG, e, "ERROR. saveSPC2DBarcodeParameter() - Failed to set Barcode Option(Codabar)");
			return false;
		}
		
		// ---------------------------------
		// Code39
		// ---------------------------------
		paramValue = new SPCParamValue[]{
				new SPCParamValue(SPCParamName.Code39StartStopCharacters,
						dlgSPC2DBarcodeOption.getCode39StartStopCharacters()),
				new SPCParamValue(SPCParamName.Code39CheckCharacter,
						dlgSPC2DBarcodeOption.getCode39CheckCharacter()),
				new SPCParamValue(SPCParamName.Code39MessageLengthMin,
						dlgSPC2DBarcodeOption.getCode39LengthMin()),
				new SPCParamValue(SPCParamName.Code39MessageLengthMax,
						dlgSPC2DBarcodeOption.getCode39LengthMax()),
				new SPCParamValue(SPCParamName.Code39Append,
						dlgSPC2DBarcodeOption.getCode39Append()),
				new SPCParamValue(SPCParamName.Code39FullASCII,
						dlgSPC2DBarcodeOption.getCode39FullASCII()),
				new SPCParamValue(SPCParamName.Code39CodePage,
						dlgSPC2DBarcodeOption.getCode39CodePage()),
		};
		
		paramValueList = new SPCParamValueList(paramValue);
		
		try {
			mReader.getBarcode().setBarcodeParamString(paramValueList.getCommand(mModuleBarcodeType.getCode()));
		} catch (ATException e) {
			ATLog.e(TAG, e, "ERROR. saveSPC2DBarcodeParameter() - Failed to set Barcode Option(Code39)");
			return false;
		}
		
		// ---------------------------------
		// I2of5
		// ---------------------------------
		paramValue = new SPCParamValue[]{
				new SPCParamValue(SPCParamName.I2of5CheckDigit,
						dlgSPC2DBarcodeOption.getI2of5CheckDigit()),
				new SPCParamValue(SPCParamName.I2of5MessageLengthMin,
						dlgSPC2DBarcodeOption.getI2of5LengthMin()),
				new SPCParamValue(SPCParamName.I2of5MessageLengthMax,
						dlgSPC2DBarcodeOption.getI2of5LengthMax()),
		};
		
		paramValueList = new SPCParamValueList(paramValue);
		
		try {
			mReader.getBarcode().setBarcodeParamString(paramValueList.getCommand(mModuleBarcodeType.getCode()));
		} catch (ATException e) {
			ATLog.e(TAG, e, "ERROR. saveSPC2DBarcodeParameter() - Failed to set Barcode Option(I2of5)");
			return false;
		}
		// ---------------------------------
		// Code93
		// ---------------------------------
		paramValue = new SPCParamValue[]{
				new SPCParamValue(SPCParamName.Code93MessageLengthMin,
						dlgSPC2DBarcodeOption.getCode93LengthMin()),
				new SPCParamValue(SPCParamName.Code93MessageLengthMax,
						dlgSPC2DBarcodeOption.getCode93LengthMax()),
				new SPCParamValue(SPCParamName.Code93Append,
						dlgSPC2DBarcodeOption.getCode93Append()),
				new SPCParamValue(SPCParamName.Code93CodePage,
						dlgSPC2DBarcodeOption.getCode93CodePage()),

		};
		
		paramValueList = new SPCParamValueList(paramValue);
		
		try {
			mReader.getBarcode().setBarcodeParamString(paramValueList.getCommand(mModuleBarcodeType.getCode()));
		} catch (ATException e) {
			ATLog.e(TAG, e, "ERROR. saveSPC2DBarcodeParameter() - Failed to set Barcode Option(Code93)");
			return false;
		}
		// ---------------------------------
		// NEC2of5
		// ---------------------------------
		paramValue = new SPCParamValue[]{
				new SPCParamValue(SPCParamName.NEC2of5CheckDigit,
						dlgSPC2DBarcodeOption.getNEC2of5CheckDigit()),
				new SPCParamValue(SPCParamName.NEC2of5MessageLengthMin,
						dlgSPC2DBarcodeOption.getNEC2of5LengthMin()),
				new SPCParamValue(SPCParamName.NEC2of5MessageLengthMax,
						dlgSPC2DBarcodeOption.getNEC2of5LengthMax()),
		};
		
		paramValueList = new SPCParamValueList(paramValue);
		
		try {
			mReader.getBarcode().setBarcodeParamString(paramValueList.getCommand(mModuleBarcodeType.getCode()));
		} catch (ATException e) {
			ATLog.e(TAG, e, "ERROR. saveSPC2DBarcodeParameter() - Failed to set Barcode Option(NEC2of5)");
			return false;
		}
		// ---------------------------------
		// Straight2of5Industrial
		// ---------------------------------
		paramValue = new SPCParamValue[]{
				new SPCParamValue(SPCParamName.Straight2of5IndustrialMessageLengthMin,
						dlgSPC2DBarcodeOption.getStraight2of5IndustrialLengthMin()),
				new SPCParamValue(SPCParamName.Straight2of5IndustrialMessageLengthMax,
						dlgSPC2DBarcodeOption.getStraight2of5IndustrialLengthMax()),
		};
		
		paramValueList = new SPCParamValueList(paramValue);
		
		try {
			mReader.getBarcode().setBarcodeParamString(paramValueList.getCommand(mModuleBarcodeType.getCode()));
		} catch (ATException e) {
			ATLog.e(TAG, e, "ERROR. saveSPC2DBarcodeParameter() - Failed to set Barcode Option(Straight2of5Industrial)");
			return false;
		}
		// ---------------------------------
		// Straight2of5IATA
		// ---------------------------------
		paramValue = new SPCParamValue[]{
				new SPCParamValue(SPCParamName.Straight2of5IATAMessageLengthMin,
						dlgSPC2DBarcodeOption.getStraight2of5IATALengthMin()),
				new SPCParamValue(SPCParamName.Straight2of5IATAMessageLengthMax,
						dlgSPC2DBarcodeOption.getStraight2of5IATALengthMax()),
		};
		
		paramValueList = new SPCParamValueList(paramValue);
		
		try {
			mReader.getBarcode().setBarcodeParamString(paramValueList.getCommand(mModuleBarcodeType.getCode()));
		} catch (ATException e) {
			ATLog.e(TAG, e, "ERROR. saveSPC2DBarcodeParameter() - Failed to set Barcode Option(Straight2of5IATA)");
			return false;
		}
		// ---------------------------------
		// Matrix2of5
		// ---------------------------------
		paramValue = new SPCParamValue[]{
				new SPCParamValue(SPCParamName.Matrix2of5MessageLengthMin,
						dlgSPC2DBarcodeOption.getMatrix2of5LengthMin()),
				new SPCParamValue(SPCParamName.Matrix2of5MessageLengthMax,
						dlgSPC2DBarcodeOption.getMatrix2of5LengthMax()),
		};
		
		paramValueList = new SPCParamValueList(paramValue);
		
		try {
			mReader.getBarcode().setBarcodeParamString(paramValueList.getCommand(mModuleBarcodeType.getCode()));
		} catch (ATException e) {
			ATLog.e(TAG, e, "ERROR. saveSPC2DBarcodeParameter() - Failed to set Barcode Option(Matrix2of5)");
			return false;
		}
		
		// ---------------------------------
		// Code11
		// ---------------------------------
		paramValue = new SPCParamValue[]{
				new SPCParamValue(SPCParamName.Code11CheckDigitsRequired,
						dlgSPC2DBarcodeOption.getCode11CheckDigitsRequired()),
				new SPCParamValue(SPCParamName.Code11MessageLengthMin,
						dlgSPC2DBarcodeOption.getCode11LengthMin()),
				new SPCParamValue(SPCParamName.Code11MessageLengthMax,
						dlgSPC2DBarcodeOption.getCode11LengthMax()),
		};
		
		paramValueList = new SPCParamValueList(paramValue);
		
		try {
			mReader.getBarcode().setBarcodeParamString(paramValueList.getCommand(mModuleBarcodeType.getCode()));
		} catch (ATException e) {
			ATLog.e(TAG, e, "ERROR. saveSPC2DBarcodeParameter() - Failed to set Barcode Option(Code11)");
			return false;
		}
		
		// ---------------------------------
		// Code128
		// ---------------------------------
		paramValue = new SPCParamValue[]{
				new SPCParamValue(SPCParamName.ISBT128Concatenation,
						dlgSPC2DBarcodeOption.getISBT128Concatenation()),
				new SPCParamValue(SPCParamName.Code128MessageLengthMin,
						dlgSPC2DBarcodeOption.getCode128LengthMin()),
				new SPCParamValue(SPCParamName.Code128MessageLengthMax,
						dlgSPC2DBarcodeOption.getCode128LengthMax()),
				new SPCParamValue(SPCParamName.Code128Append,
						dlgSPC2DBarcodeOption.getCode128Append()),
				new SPCParamValue(SPCParamName.Code128CodePage,
						dlgSPC2DBarcodeOption.getCode128CodePage()),
		};
		
		paramValueList = new SPCParamValueList(paramValue);
		
		try {
			mReader.getBarcode().setBarcodeParamString(paramValueList.getCommand(mModuleBarcodeType.getCode()));
		} catch (ATException e) {
			ATLog.e(TAG, e, "ERROR. saveSPC2DBarcodeParameter() - Failed to set Barcode Option(Code128)");
			return false;
		}
		
		// ---------------------------------
		// GS1128
		// ---------------------------------
		paramValue = new SPCParamValue[]{
				new SPCParamValue(SPCParamName.GS1128MessageLengthMin,
						dlgSPC2DBarcodeOption.getGS1128LengthMin()),
				new SPCParamValue(SPCParamName.GS1128MessageLengthMax,
						dlgSPC2DBarcodeOption.getGS1128LengthMax()),
		};
		
		paramValueList = new SPCParamValueList(paramValue);
		
		try {
			mReader.getBarcode().setBarcodeParamString(paramValueList.getCommand(mModuleBarcodeType.getCode()));
		} catch (ATException e) {
			ATLog.e(TAG, e, "ERROR. saveSPC2DBarcodeParameter() - Failed to set Barcode Option(GS1128)");
			return false;
		}
		
		// ---------------------------------
		// UpcA
		// ---------------------------------
		paramValue = new SPCParamValue[]{
				new SPCParamValue(SPCParamName.UPCACheckDigit,
						dlgSPC2DBarcodeOption.getUPCACheckDigit()),
				new SPCParamValue(SPCParamName.UPCANumberSystem,
						dlgSPC2DBarcodeOption.getUPCANumberSystem()),
				new SPCParamValue(SPCParamName.UPCAAddenda2Digit,
						dlgSPC2DBarcodeOption.getUPCAAddenda2Digit()),
				new SPCParamValue(SPCParamName.UPCAAddenda5Digit,
						dlgSPC2DBarcodeOption.getUPCAAddenda5Digit()),
				new SPCParamValue(SPCParamName.UPCAAddendaRequired,
						dlgSPC2DBarcodeOption.getUPCAAddendaRequired()),
				new SPCParamValue(SPCParamName.UPCAAddendaSeparator,
						dlgSPC2DBarcodeOption.getUPCAAddendaSeparator()),
		};
		
		paramValueList = new SPCParamValueList(paramValue);
		
		try {
			mReader.getBarcode().setBarcodeParamString(paramValueList.getCommand(mModuleBarcodeType.getCode()));
		} catch (ATException e) {
			ATLog.e(TAG, e, "ERROR. saveSPC2DBarcodeParameter() - Failed to set Barcode Option(UpcA)");
			return false;
		}
		
		// ---------------------------------
		// UpcE0
		// ---------------------------------
		paramValue = new SPCParamValue[]{
				new SPCParamValue(SPCParamName.UPCE0Expand,
						dlgSPC2DBarcodeOption.getUPCE0Expand()),
				new SPCParamValue(SPCParamName.UPCE0AddendaRequired,
						dlgSPC2DBarcodeOption.getUPCE0AddendaRequired()),
				new SPCParamValue(SPCParamName.UPCE0AddendaSeparator,
						dlgSPC2DBarcodeOption.getUPCE0AddendaSeparator()),
				new SPCParamValue(SPCParamName.UPCE0CheckDigit,
						dlgSPC2DBarcodeOption.getUPCE0CheckDigit()),
				new SPCParamValue(SPCParamName.UPCE0LeadingZero,
						dlgSPC2DBarcodeOption.getUPCE0LeadingZero()),
				new SPCParamValue(SPCParamName.UPCE0Addenda2Digit,
						dlgSPC2DBarcodeOption.getUPCE0Addenda2Digit()),
				new SPCParamValue(SPCParamName.UPCE0Addenda5Digit,
						dlgSPC2DBarcodeOption.getUPCE0Addenda5Digit()),
		};
		
		paramValueList = new SPCParamValueList(paramValue);
		
		try {
			mReader.getBarcode().setBarcodeParamString(paramValueList.getCommand(mModuleBarcodeType.getCode()));
		} catch (ATException e) {
			ATLog.e(TAG, e, "ERROR. saveSPC2DBarcodeParameter() - Failed to set Barcode Option(UpcE0)");
			return false;
		}
		
		// ---------------------------------
		// EanJan13
		// ---------------------------------
		paramValue = new SPCParamValue[]{
				new SPCParamValue(SPCParamName.ConvertUPCAtoEAN13,
						dlgSPC2DBarcodeOption.getConvertUPCAtoEAN13()),
				new SPCParamValue(SPCParamName.EANJAN13CheckDigit,
						dlgSPC2DBarcodeOption.getEANJAN13CheckDigit()),
				new SPCParamValue(SPCParamName.EANJAN13Addenda2Digit,
						dlgSPC2DBarcodeOption.getEANJAN13Addenda2Digit()),
				new SPCParamValue(SPCParamName.EANJAN13Addenda5Digit,
						dlgSPC2DBarcodeOption.getEANJAN13Addenda5Digit()),
				new SPCParamValue(SPCParamName.EANJAN13AddendaRequired,
						dlgSPC2DBarcodeOption.getEANJAN13AddendaRequired()),
				new SPCParamValue(SPCParamName.EANJAN13AddendaSeparator,
						dlgSPC2DBarcodeOption.getEANJAN13AddendaSeparator()),
				new SPCParamValue(SPCParamName.ISBNTranslate,
						dlgSPC2DBarcodeOption.getISBNTranslate()),
		};
		
		paramValueList = new SPCParamValueList(paramValue);
		
		try {
			mReader.getBarcode().setBarcodeParamString(paramValueList.getCommand(mModuleBarcodeType.getCode()));
		} catch (ATException e) {
			ATLog.e(TAG, e, "ERROR. saveSPC2DBarcodeParameter() - Failed to set Barcode Option(EanJan13)");
			return false;
		}
		
		// ---------------------------------
		// EanJan8
		// ---------------------------------
		paramValue = new SPCParamValue[]{
				new SPCParamValue(SPCParamName.EANJAN8CheckDigit,
						dlgSPC2DBarcodeOption.getEANJAN8CheckDigit()),
				new SPCParamValue(SPCParamName.EANJAN8Addenda2Digit,
						dlgSPC2DBarcodeOption.getEANJAN8Addenda2Digit()),
				new SPCParamValue(SPCParamName.EANJAN8Addenda5Digit,
						dlgSPC2DBarcodeOption.getEANJAN8Addenda5Digit()),
				new SPCParamValue(SPCParamName.EANJAN8AddendaRequired,
						dlgSPC2DBarcodeOption.getEANJAN8AddendaRequired()),
				new SPCParamValue(SPCParamName.EANJAN8AddendaSeparator,
						dlgSPC2DBarcodeOption.getEANJAN8AddendaSeparator()),
		};
		
		paramValueList = new SPCParamValueList(paramValue);
		
		try {
			mReader.getBarcode().setBarcodeParamString(paramValueList.getCommand(mModuleBarcodeType.getCode()));
		} catch (ATException e) {
			ATLog.e(TAG, e, "ERROR. saveSPC2DBarcodeParameter() - Failed to set Barcode Option(EanJan8)");
			return false;
		}
		
		// ---------------------------------
		// MSI
		// ---------------------------------
		paramValue = new SPCParamValue[]{
				new SPCParamValue(SPCParamName.MSICheckCharacter,
						dlgSPC2DBarcodeOption.getMSICheckCharacter()),
				new SPCParamValue(SPCParamName.MSIMessageLengthMin,
						dlgSPC2DBarcodeOption.getMSILengthMin()),
				new SPCParamValue(SPCParamName.MSIMessageLengthMax,
						dlgSPC2DBarcodeOption.getMSILengthMax()),
		};
		
		paramValueList = new SPCParamValueList(paramValue);
		
		try {
			mReader.getBarcode().setBarcodeParamString(paramValueList.getCommand(mModuleBarcodeType.getCode()));
		} catch (ATException e) {
			ATLog.e(TAG, e, "ERROR. saveSPC2DBarcodeParameter() - Failed to set Barcode Option(MSI)");
			return false;
		}
		
		// ---------------------------------
		// GS1DataBar
		// ---------------------------------
		paramValue = new SPCParamValue[]{
				new SPCParamValue(SPCParamName.GS1DataBarExpandedMessageLengthMin,
						dlgSPC2DBarcodeOption.getGS1DataBarLengthMin()),
				new SPCParamValue(SPCParamName.GS1DataBarExpandedMessageLengthMax,
						dlgSPC2DBarcodeOption.getGS1DataBarLengthMax()),
		};
		
		paramValueList = new SPCParamValueList(paramValue);
		
		try {
			mReader.getBarcode().setBarcodeParamString(paramValueList.getCommand(mModuleBarcodeType.getCode()));
		} catch (ATException e) {
			ATLog.e(TAG, e, "ERROR. saveSPC2DBarcodeParameter() - Failed to set Barcode Option(GS1DataBar)");
			return false;
		}
		
		// ---------------------------------
		// CodablockA
		// ---------------------------------
		paramValue = new SPCParamValue[]{
				new SPCParamValue(SPCParamName.CodablockAMessageLengthMin,
						dlgSPC2DBarcodeOption.getCodablockALengthMin()),
				new SPCParamValue(SPCParamName.CodablockAMessageLengthMax,
						dlgSPC2DBarcodeOption.getCodablockALengthMax()),
		};
		
		paramValueList = new SPCParamValueList(paramValue);
		
		try {
			mReader.getBarcode().setBarcodeParamString(paramValueList.getCommand(mModuleBarcodeType.getCode()));
		} catch (ATException e) {
			ATLog.e(TAG, e, "ERROR. saveSPC2DBarcodeParameter() - Failed to set Barcode Option(CodablockA)");
			return false;
		}
		
		// ---------------------------------
		// CodablockF
		// ---------------------------------
		paramValue = new SPCParamValue[]{
				new SPCParamValue(SPCParamName.CodablockFMessageLengthMin,
						dlgSPC2DBarcodeOption.getCodablockFLengthMin()),
				new SPCParamValue(SPCParamName.CodablockFMessageLengthMax,
						dlgSPC2DBarcodeOption.getCodablockFLengthMax()),
		};
		
		paramValueList = new SPCParamValueList(paramValue);
		
		try {
			mReader.getBarcode().setBarcodeParamString(paramValueList.getCommand(mModuleBarcodeType.getCode()));
		} catch (ATException e) {
			ATLog.e(TAG, e, "ERROR. saveSPC2DBarcodeParameter() - Failed to set Barcode Option(CodablockF)");
			return false;
		}
		
		// ---------------------------------
		// PDF417
		// ---------------------------------
		paramValue = new SPCParamValue[]{
				new SPCParamValue(SPCParamName.PDF417MessageLengthMin,
						dlgSPC2DBarcodeOption.getPDF417LengthMin()),
				new SPCParamValue(SPCParamName.PDF417MessageLengthMax,
						dlgSPC2DBarcodeOption.getPDF417LengthMax()),
		};
		
		paramValueList = new SPCParamValueList(paramValue);
		
		try {
			mReader.getBarcode().setBarcodeParamString(paramValueList.getCommand(mModuleBarcodeType.getCode()));
		} catch (ATException e) {
			ATLog.e(TAG, e, "ERROR. saveSPC2DBarcodeParameter() - Failed to set Barcode Option(PDF417)");
			return false;
		}
		
		// ---------------------------------
		// MicroPDF417
		// ---------------------------------
		paramValue = new SPCParamValue[]{
				new SPCParamValue(SPCParamName.MicroPDF417MessageLengthMin,
						dlgSPC2DBarcodeOption.getMicroPDF417LengthMin()),
				new SPCParamValue(SPCParamName.MicroPDF417MessageLengthMax,
						dlgSPC2DBarcodeOption.getMicroPDF417LengthMax()),
		};
		
		paramValueList = new SPCParamValueList(paramValue);
		
		try {
			mReader.getBarcode().setBarcodeParamString(paramValueList.getCommand(mModuleBarcodeType.getCode()));
		} catch (ATException e) {
			ATLog.e(TAG, e, "ERROR. saveSPC2DBarcodeParameter() - Failed to set Barcode Option(MicroPDF417)");
			return false;
		}
		
		// ---------------------------------
		// GS1CompositeCodes
		// ---------------------------------
		paramValue = new SPCParamValue[]{
				new SPCParamValue(SPCParamName.GS1CompositeCodes,
						dlgSPC2DBarcodeOption.getGS1CompositeCodes()),
				new SPCParamValue(SPCParamName.UPCEANVersion,
						dlgSPC2DBarcodeOption.getUPCEANVersion()),
				new SPCParamValue(SPCParamName.GS1CompositeCodeMessageLengthMin,
						dlgSPC2DBarcodeOption.getGS1CompositeCodesLengthMin()),
				new SPCParamValue(SPCParamName.GS1CompositeCodeMessageLengthMax,
						dlgSPC2DBarcodeOption.getGS1CompositeCodesLengthMax()),
		};
		
		paramValueList = new SPCParamValueList(paramValue);
		
		try {
			mReader.getBarcode().setBarcodeParamString(paramValueList.getCommand(mModuleBarcodeType.getCode()));
		} catch (ATException e) {
			ATLog.e(TAG, e, "ERROR. saveSPC2DBarcodeParameter() - Failed to set Barcode Option(GS1CompositeCodes)");
			return false;
		}
		
		// ---------------------------------
		// QRCode
		// ---------------------------------
		paramValue = new SPCParamValue[]{
				new SPCParamValue(SPCParamName.QRCodeMessageLengthMin,
						dlgSPC2DBarcodeOption.getQRCodeLengthMin()),
				new SPCParamValue(SPCParamName.QRCodeMessageLengthMax,
						dlgSPC2DBarcodeOption.getQRCodeLengthMax()),
				new SPCParamValue(SPCParamName.QRCodeAppend,
						dlgSPC2DBarcodeOption.getQRCodeAppend()),
				new SPCParamValue(SPCParamName.QRCodePage,
						dlgSPC2DBarcodeOption.getQRCodePage()),
		};
		
		paramValueList = new SPCParamValueList(paramValue);
		
		try {
			mReader.getBarcode().setBarcodeParamString(paramValueList.getCommand(mModuleBarcodeType.getCode()));
		} catch (ATException e) {
			ATLog.e(TAG, e, "ERROR. saveSPC2DBarcodeParameter() - Failed to set Barcode Option(QRCode)");
			return false;
		}
		
		// ---------------------------------
		// DataMatrix
		// ---------------------------------
		paramValue = new SPCParamValue[]{
				new SPCParamValue(SPCParamName.DataMatrixMessageLengthMin,
						dlgSPC2DBarcodeOption.getDataMatrixLengthMin()),
				new SPCParamValue(SPCParamName.DataMatrixMessageLengthMax,
						dlgSPC2DBarcodeOption.getDataMatrixLengthMax()),
				new SPCParamValue(SPCParamName.DataMatrixCodePage,
						dlgSPC2DBarcodeOption.getDataMatrixCodePage()),
		};
		
		paramValueList = new SPCParamValueList(paramValue);
		
		try {
			mReader.getBarcode().setBarcodeParamString(paramValueList.getCommand(mModuleBarcodeType.getCode()));
		} catch (ATException e) {
			ATLog.e(TAG, e, "ERROR. saveSPC2DBarcodeParameter() - Failed to set Barcode Option(DataMatrix)");
			return false;
		}
		
		// ---------------------------------
		// MaxiCode
		// ---------------------------------
		paramValue = new SPCParamValue[]{
				new SPCParamValue(SPCParamName.MaxiCodeMessageLengthMin,
						dlgSPC2DBarcodeOption.getMaxiCodeLengthMin()),
				new SPCParamValue(SPCParamName.MaxiCodeMessageLengthMax,
						dlgSPC2DBarcodeOption.getMaxiCodeLengthMax()),
		};
		
		paramValueList = new SPCParamValueList(paramValue);
		
		try {
			mReader.getBarcode().setBarcodeParamString(paramValueList.getCommand(mModuleBarcodeType.getCode()));
		} catch (ATException e) {
			ATLog.e(TAG, e, "ERROR. saveSPC2DBarcodeParameter() - Failed to set Barcode Option(MaxiCode)");
			return false;
		}
		
		// ---------------------------------
		// AztecCode
		// ---------------------------------
		paramValue = new SPCParamValue[]{
				new SPCParamValue(SPCParamName.AztecCodeMessageLengthMin,
						dlgSPC2DBarcodeOption.getAztecCodeLengthMin()),
				new SPCParamValue(SPCParamName.AztecCodeMessageLengthMax,
						dlgSPC2DBarcodeOption.getAztecCodeLengthMax()),
				new SPCParamValue(SPCParamName.AztecAppend,
						dlgSPC2DBarcodeOption.getAztecAppend()),
				new SPCParamValue(SPCParamName.AztecCodePage,
						dlgSPC2DBarcodeOption.getAztecCodePage()),
		};
		
		paramValueList = new SPCParamValueList(paramValue);
		
		try {
			mReader.getBarcode().setBarcodeParamString(paramValueList.getCommand(mModuleBarcodeType.getCode()));
		} catch (ATException e) {
			ATLog.e(TAG, e, "ERROR. saveSPC2DBarcodeParameter() - Failed to set Barcode Option(AztecCode)");
			return false;
		}
		
		// ---------------------------------
		// HanXinCode
		// ---------------------------------
		paramValue = new SPCParamValue[]{
				new SPCParamValue(SPCParamName.HanXinCodeMessageLengthMin,
						dlgSPC2DBarcodeOption.getHanXinCodeLengthMin()),
				new SPCParamValue(SPCParamName.HanXinCodeMessageLengthMax,
						dlgSPC2DBarcodeOption.getHanXinCodeLengthMax()),
		};
		
		paramValueList = new SPCParamValueList(paramValue);
		
		try {
			mReader.getBarcode().setBarcodeParamString(paramValueList.getCommand(mModuleBarcodeType.getCode()));
		} catch (ATException e) {
			ATLog.e(TAG, e, "ERROR. saveSPC2DBarcodeParameter() - Failed to set Barcode Option(HanXinCode)");
			return false;
		}
		
		// ---------------------------------
		// PostalCodes2D
		// ---------------------------------
		paramValue = new SPCParamValue[]{
				new SPCParamValue(SPCParamName.PlanetCodeCheckDigit,
						dlgSPC2DBarcodeOption.getPlanetCodeCheckDigit()),
				new SPCParamValue(SPCParamName.PostnetCheckDigit,
						dlgSPC2DBarcodeOption.getPostnetCheckDigit()),
				new SPCParamValue(SPCParamName.AustralianPostInterpretation,
						dlgSPC2DBarcodeOption.getAustralianPostInterpretation()),
		};
		
		paramValueList = new SPCParamValueList(paramValue);
		
		try {
			mReader.getBarcode().setBarcodeParamString(paramValueList.getCommand(mModuleBarcodeType.getCode()));
		} catch (ATException e) {
			ATLog.e(TAG, e, "ERROR. saveSPC2DBarcodeParameter() - Failed to set Barcode Option(PostalCodes2D)");
			return false;
		}
		
		// ---------------------------------
		// PostalCodesLinear
		// ---------------------------------
		paramValue = new SPCParamValue[]{
				new SPCParamValue(SPCParamName.ChinaPostMessageLengthMin,
						dlgSPC2DBarcodeOption.getChinaPostLengthMin()),
				new SPCParamValue(SPCParamName.ChinaPostMessageLengthMax,
						dlgSPC2DBarcodeOption.getChinaPostLengthMax()),
				new SPCParamValue(SPCParamName.KoreaPostMessageLengthMin,
						dlgSPC2DBarcodeOption.getKoreaPostLengthMin()),
				new SPCParamValue(SPCParamName.KoreaPostMessageLengthMax,
						dlgSPC2DBarcodeOption.getKoreaPostLengthMax()),
				new SPCParamValue(SPCParamName.KoreaPostCheckDigit,
						dlgSPC2DBarcodeOption.getKoreaPostCheckDigit()),

		};
		
		paramValueList = new SPCParamValueList(paramValue);
		
		try {
			mReader.getBarcode().setBarcodeParamString(paramValueList.getCommand(mModuleBarcodeType.getCode()));
		} catch (ATException e) {
			ATLog.e(TAG, e, "ERROR. saveSPC2DBarcodeParameter() - Failed to set Barcode Option(PostalCodesLinear)");
			return false;
		}
		
		// ---------------------------------
		// ETC
		// ---------------------------------
		// UPCA EAN13 Extended Coupon Code
		// Coupon GS1 DataBar Output
		// GS1 Emulation
		// ---------------------------------
		paramValue = new SPCParamValue[]{
				new SPCParamValue(SPCParamName.UPCAEAN13ExtendedCouponCode,
						dlgSPC2DBarcodeOption.getUPCAEAN13ExtendedCouponCode()),
				new SPCParamValue(SPCParamName.CouponGS1DataBarOutput,
						dlgSPC2DBarcodeOption.getCouponGS1DataBarOutput()),
				new SPCParamValue(SPCParamName.GS1Emulation,
						dlgSPC2DBarcodeOption.getGS1Emulation()),
				new SPCParamValue(SPCParamName.VideoReverse,
						dlgSPC2DBarcodeOption.getVideoReverse()),
				
		};

		paramValueList = new SPCParamValueList(paramValue);
		
		try {
			mReader.getBarcode().setBarcodeParamString(paramValueList.getCommand(mModuleBarcodeType.getCode()));
		} catch (ATException e) {
			ATLog.e(TAG, e, "ERROR. saveSPC2DBarcodeParameter() - Failed to set Barcode Option(ETC)");
			return false;
		}
		
		
		ATLog.i(TAG, INFO, "INFO. saveSPC2DBarcodeParameter()");
		return true;
	}

	
	private Runnable mLoadingProc = new Runnable() {
		@Override
		public void run() {
			
			mIsThreadAlive = true;
			
			mLoadingState = LOADING_STATE_READER;
			if(mReader == null){
				ATLog.e(TAG, "ERROR. $mLoadingProc.run() - Failed to get reader");
				BarcodeOptionActivity.this.runOnUiThread(mFailedLoadProc);
				return;
			}
			
			mLoadingState = LOADING_STATE_BARCODE_READER;
			if(mReader.getBarcode() == null){
				ATLog.e(TAG, "ERROR. $mLoadingProc.run() - Failed to get Barcode reader");
				mIsThreadAlive = false;

				BarcodeOptionActivity.this.runOnUiThread(mFailedLoadProc);
				return ;
			}
			
			mLoadingState = LOADING_STATE_BARCODE_DISABLE_ACTION_KEY;
			if(mIsThreadAlive){
				// Disable Action Key
				try {
					mReader.setUseActionKey(false);
				} catch (ATException e) {
					mLoadingError = new ATException(e.getCode());
					ATLog.e(TAG, e, "ERROR. $mLoadingProc.run() - Failed to disabled key action");
					mIsThreadAlive = false;
					BarcodeOptionActivity.this.runOnUiThread(mFailedLoadProc);
					return ;
				}
			} else {
				runOnUiThread(mLoadedProc);
				ATLog.i(TAG, INFO, "INFO. $mLoadingProc.run() - Canceled");
				return;
			}
			
			if(mIsThreadAlive){
				mModuleBarcodeType = mReader.getBarcode().getType();
			} else {
				runOnUiThread(mLoadedProc);
				ATLog.i(TAG, INFO, "INFO. $mLoadingProc.run() - Canceled");
				return;
			}
			
			mLoadingState = LOADING_STATE_BARCODE_OPTION;
			
			// Barcode Detail Option
			if(mIsThreadAlive){
				if(mModuleBarcodeType == ModuleBarcodeType.AT1DSE955 
						|| mModuleBarcodeType == ModuleBarcodeType.AT1DSE965 ) {
					try {
						if(!loadSSI1DBarcodeParameter()) {
							runOnUiThread(mLoadedProc);
							ATLog.i(TAG, INFO, "INFO. $mLoadingProc.run() - Canceled");
							return;
							
						}
					} catch (ATException e) {
						mLoadingError = new ATException(e.getCode());
						ATLog.e(TAG, e, "ERROR. $mLoadingProc.run() - Failed to get barcode option");
						BarcodeOptionActivity.this.runOnUiThread(mFailedLoadProc);
						return;
					} 
					
				} else if(mModuleBarcodeType == ModuleBarcodeType.AT2DSE4710 ) {
				
					try {
						if(!loadSSI2DBarcodeParameter()) {
							runOnUiThread(mLoadedProc);
							ATLog.i(TAG, INFO, "INFO. $mLoadingProc.run() - Canceled");
							return;
							
						}
					} catch (ATException e) {
						mLoadingError = new ATException(e.getCode());
						ATLog.e(TAG, e, "ERROR. $mLoadingProc.run() - Failed to get barcode option");
						BarcodeOptionActivity.this.runOnUiThread(mFailedLoadProc);
						return;
					}
				} else if(mModuleBarcodeType == ModuleBarcodeType.AT2DN368X) {
					try {
						if(!loadSPC2DBarcodeParameter()) {
							runOnUiThread(mLoadedProc);
							ATLog.i(TAG, INFO, "INFO. $mLoadingProc.run() - Canceled");
							return;
						}
					} catch (ATException e) {
						mLoadingError = new ATException(e.getCode());
						ATLog.e(TAG, e, "ERROR. $mLoadingProc.run() - Failed to get barcode option");
						BarcodeOptionActivity.this.runOnUiThread(mFailedLoadProc);
						return;
					}
				}
			} else {
				runOnUiThread(mLoadedProc);
				ATLog.i(TAG, INFO, "INFO. $mLoadingProc.run() - Canceled");
				return;
			}
			
			mLoadingState = LOADING_STATE_BARCODE_SYMBOL;

			if(mIsThreadAlive){
				// Get Barcode Symbol List
				try {
					dlgSymbolState.setList(mReader.getBarcode().getSymbolState());
				} catch (ATException e) {
					mLoadingError = new ATException(e.getCode());
					ATLog.e(TAG, e,"ERROR. $mLoadingProc.run() - Failed to load symbologies state list");
					BarcodeOptionActivity.this.runOnUiThread(mFailedLoadProc);
					return;
				}
				
//				mIsPostSelect = mReader.getBarcode().isSuportSelPost();
				if(mIsPostSelect) {
					try{
						mPost = mReader.getBarcode().getSymbolPostState();
					} catch (ATException e) {
						mLoadingError = new ATException(e.getCode());
						ATLog.e(TAG, e,"ERROR. $mLoadingProc.run() - Failed to load postal symbologies state");
						BarcodeOptionActivity.this.runOnUiThread(mFailedLoadProc);
						return;
					}
					
					dlgPostalCodes.setPostalCodes(mPost);
				}
			} else {
				runOnUiThread(mLoadedProc);
				ATLog.i(TAG, INFO, "INFO. $mLoadingProc.run() - Canceled");
				return;
			}

			mLoadingState = LOADING_STATE_BARCODE_CHARSET;

			if(mIsThreadAlive){
				try {
					Charset charset = mReader.getBarcode().getCharset();	
					dlgBarcodeChracterSet.setValue(charset.name());
				} catch (ATException e) {
					mLoadingError = new ATException(e.getCode());
					ATLog.e(TAG, e,"ERROR. $mLoadingProc.run() - Failed to load barcode CharacterSet");
					BarcodeOptionActivity.this.runOnUiThread(mFailedLoadProc);
					return;
				}
			} else {
				runOnUiThread(mLoadedProc);
				ATLog.i(TAG, INFO, "INFO. $mLoadingProc.run() - Canceled");
				return;
			}
			
			dlgBarcodeRestartTime.setValue(mBarcodeRestartTime);
			
			runOnUiThread(mLoadedProc);
			ATLog.i(TAG, INFO, "INFO. $$mLoadingProc.run()");
		}
	};
	
	private Runnable mLoadedProc = new Runnable() {
		@Override
		public void run() {
			if(mIsThreadAlive){
				
				if(mIsPostSelect) 
					txtPostalCodes.setText(
							String.format(Locale.US, "%s", dlgPostalCodes.getPostalCodes().toString()));
				
				txtValueBarcodeChracterSet.setText(
						String.format(Locale.US, "%s", dlgBarcodeChracterSet.getValue())); 
				
				txtValueBarcodeRestartTime.setText(
						String.format(Locale.US, "%s %s", 
								dlgBarcodeRestartTime.getValue(), getResources().getString(R.string.unit_ms)));
				mIsThreadAlive = false;
			}
			enableWidget(true);
			WaitDialog.hide();
			ATLog.i(TAG, INFO, "INFO. $$mLoadedProc.run()");
		}
	};
	
	private Runnable mFailedLoadProc = new Runnable() {
		int failMessage;
		String message;
		
		@Override
		public void run() {
			WaitDialog.hide();
			enableWidget(false);
			
			switch(mLoadingState){
			case LOADING_STATE_READER :
				failMessage = R.string.msg_fail_load_reader_instance;
				break;
			case LOADING_STATE_BARCODE_READER :
				failMessage = R.string.msg_fail_load_barcode_reader_instance;
				break;
			case LOADING_STATE_BARCODE_DISABLE_ACTION_KEY :
				failMessage = R.string.msg_fail_load_disabled_action_key;
				break;
			case LOADING_STATE_BARCODE_OPTION :
				failMessage = R.string.msg_fail_load_barcode_option;
				break;
			case LOADING_STATE_BARCODE_SYMBOL :
				failMessage = R.string.msg_fail_load_symbol;
				break;
			case LOADING_STATE_BARCODE_CHARSET :
				failMessage = R.string.msg_fail_load_charset;
				break;
			}
			
			if( mLoadingState == LOADING_STATE_READER || 
					mLoadingState == LOADING_STATE_BARCODE_READER ) {
				message = String.format(Locale.US, "%s",
						getResources().getString(failMessage) );
			} else {
				message = String.format(Locale.US, "%s\r\nError[%s]",
						getResources().getString(failMessage), mLoadingError.getCode());

			}
			
			MessageBox.show(BarcodeOptionActivity.this, message, R.string.title_error,
					android.R.drawable.ic_menu_info_details, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							mReader.removeListener(BarcodeOptionActivity.this);
							setResult(Activity.RESULT_CANCELED);
							BarcodeOptionActivity.this.finish();
						}
					});
		}
	};

}
