package com.atid.app.atx.dialog.ssi;

import com.atid.app.atx.dialog.BaseDialog;
import com.atid.app.atx.dialog.EnumListDialog;
import com.atid.app.atx.R;
import com.atid.lib.module.barcode.ssi.type.AztecInverse;
import com.atid.lib.module.barcode.ssi.type.DataMatrixInverse;
import com.atid.lib.module.barcode.ssi.type.DecodeMirrorImages;
import com.atid.lib.module.barcode.ssi.type.HanXinInverse;
import com.atid.lib.module.barcode.ssi.type.QRInverse;
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

public class Option2DSymbolDialog extends BaseDialog implements OnClickListener, OnCheckedChangeListener{
	private static final String TAG = Option2DSymbolDialog.class.getSimpleName();
	private static final int INFO = ATLog.L2;
	
	private CheckBox chkCode128Emulation;
	private TextView txtDataMatrixInverse;
	private TextView txtDecodeMirrorImages;
	private TextView txtQRInverse;
	private TextView txtAztecInverse;
	private TextView txtHanXinInverse;
	
	private EnumListDialog dlgDataMatrixInverse;
	private EnumListDialog dlgDecodeMirrorImages;
	private EnumListDialog dlgQRInverse;
	private EnumListDialog dlgAztecInverse;
	private EnumListDialog dlgHanXinInverse;
	
	private boolean mIsCode128Emulation;
	private DataMatrixInverse mDataMatrixInverse;
	private DecodeMirrorImages mDecodeMirrorImages;
	private QRInverse mQRInverse;
	private AztecInverse mAztecInverse;
	private HanXinInverse mHanXinInverse;
	
	private Context mContext;
	
	public Option2DSymbolDialog(){
		super();
		
		mDataMatrixInverse = DataMatrixInverse.Regular;
		mDecodeMirrorImages = DecodeMirrorImages.Auto;
		mQRInverse = QRInverse.Regular;
		mAztecInverse = AztecInverse.InverseAutodetect;
		mHanXinInverse = HanXinInverse.Regular;
		
		mContext = null;
	}
	
	public boolean getCode128Emulation() {
		return mIsCode128Emulation;
	}
	
	public void setCode128Emulation(boolean value){
		mIsCode128Emulation = value;
	}
	
	public DataMatrixInverse getDataMatrixInverse(){
		return mDataMatrixInverse;
	}
	
	public void setDataMatrixInverse(DataMatrixInverse value){
		mDataMatrixInverse = value;
	}
	
	public DecodeMirrorImages getDecodeMirrorImages(){
		return mDecodeMirrorImages;
	}
	
	public void setDecodeMirrorImages(DecodeMirrorImages value){
		mDecodeMirrorImages = value;
	}
	
	public QRInverse getQRInverse(){
		return mQRInverse;
	}
	
	public void setQRInverse(QRInverse value){
		mQRInverse = value;
	}
	
	public AztecInverse getAztecInverse() {
		return mAztecInverse;
	}
	
	public void setAztecInverse(AztecInverse value){
		mAztecInverse = value;
	}
	
	public HanXinInverse getHanXinInverse(){
		return mHanXinInverse;
	}
	
	public void setHanXinInverse(HanXinInverse value){
		mHanXinInverse = value;
	}
	
	@Override
	public void display() {
		
	}

	@Override
	public void showDialog(Context context, String title, final IValueChangedListener changedListener,
			final ICancelListener cancelListener) {
		mContext = context;
		
		LinearLayout root = (LinearLayout) LinearLayout.inflate(context, R.layout.dialog_barcode_option_ssi_2d_2d_symbol, null);
		
		chkCode128Emulation = (CheckBox) root.findViewById(R.id.code128_emulation);
		chkCode128Emulation.setOnCheckedChangeListener(this);
		txtDataMatrixInverse = (TextView) root.findViewById(R.id.data_matrix_inverse);
		txtDataMatrixInverse.setOnClickListener(this);
		txtDecodeMirrorImages = (TextView) root.findViewById(R.id.decode_mirror_images);
		txtDecodeMirrorImages.setOnClickListener(this);
		txtQRInverse = (TextView) root.findViewById(R.id.qr_inverse);
		txtQRInverse.setOnClickListener(this);
		txtAztecInverse = (TextView) root.findViewById(R.id.aztec_inverse);
		txtAztecInverse.setOnClickListener(this);
		txtHanXinInverse = (TextView) root.findViewById(R.id.han_xin_inverse);
		txtHanXinInverse.setOnClickListener(this);
		
		dlgDataMatrixInverse = new EnumListDialog(txtDataMatrixInverse, DataMatrixInverse.values());
		dlgDataMatrixInverse.setValue(mDataMatrixInverse);
		dlgDecodeMirrorImages = new EnumListDialog(txtDecodeMirrorImages, DecodeMirrorImages.values());
		dlgDecodeMirrorImages.setValue(mDecodeMirrorImages);
		dlgQRInverse = new EnumListDialog(txtQRInverse, QRInverse.values());
		dlgQRInverse.setValue(mQRInverse);
		dlgAztecInverse = new EnumListDialog(txtAztecInverse, AztecInverse.values());
		dlgAztecInverse.setValue(mAztecInverse);
		dlgHanXinInverse = new EnumListDialog(txtHanXinInverse, HanXinInverse.values());
		dlgHanXinInverse.setValue(mHanXinInverse);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setView(root);
		builder.setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mIsCode128Emulation = chkCode128Emulation.isChecked();
				mDataMatrixInverse = (DataMatrixInverse) dlgDataMatrixInverse.getValue();
				mDecodeMirrorImages = (DecodeMirrorImages) dlgDecodeMirrorImages.getValue();
				mQRInverse = (QRInverse) dlgQRInverse.getValue();	
				mAztecInverse = (AztecInverse) dlgAztecInverse.getValue();
				mHanXinInverse = (HanXinInverse) dlgHanXinInverse.getValue();
				
				if(changedListener != null)
					changedListener.onValueChanged(Option2DSymbolDialog.this);
				ATLog.i(TAG, INFO, "INFO. showDialog().$PositiveButton.onClick()");
			}
		});
		
		builder.setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				if(cancelListener != null)
					cancelListener.onCanceled(Option2DSymbolDialog.this); 
				ATLog.i(TAG, INFO, "INFO. showDialog().$NegativeButton.onClick()");
			}
		});
		
		builder.setCancelable(true);
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				
				if(cancelListener != null)
					cancelListener.onCanceled(Option2DSymbolDialog.this);
				ATLog.i(TAG, INFO, "INFO. showDialog().onCancel()");
			}
		});
		
		AlertDialog dialog = builder.create();
		dialog.setOnShowListener(new DialogInterface.OnShowListener() {
			
			@Override
			public void onShow(DialogInterface dialog) {
				chkCode128Emulation.setChecked(mIsCode128Emulation);
				dlgDataMatrixInverse.display(); 
				dlgDecodeMirrorImages.display();
				dlgQRInverse.display();
				dlgAztecInverse.display();
				dlgHanXinInverse.display();
				
				ATLog.i(TAG, INFO, "INFO. showDialog().onShow()");
			}
		});
		dialog.show();
		
		ATLog.i(TAG, INFO, "INFO. showDialog()");
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.data_matrix_inverse:
			dlgDataMatrixInverse.showDialog(mContext, R.string.data_matrix_inverse);
			break;
		case R.id.decode_mirror_images:
			dlgDecodeMirrorImages.showDialog(mContext, R.string.decode_mirror_images);
			break;
		case R.id.qr_inverse:
			dlgQRInverse.showDialog(mContext, R.string.qr_inverse);
			break;
		case R.id.aztec_inverse:
			dlgAztecInverse.showDialog(mContext, R.string.aztec_inverse);
			break;
		case R.id.han_xin_inverse:
			dlgHanXinInverse.showDialog(mContext, R.string.han_xin_inverse);
			break;
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton view, boolean isChecked) {
		switch(view.getId()){
		case R.id.code128_emulation:
			//mIsCode128Emulation = chkCode128Emulation.isChecked();
			break;
		}
	}

}
