package com.atid.app.atx.dialog.ssi;

import com.atid.app.atx.dialog.BaseDialog;
import com.atid.app.atx.dialog.EnumListDialog;
import com.atid.app.atx.R;
import com.atid.lib.module.barcode.ssi.type.IntercharacterGapSize;
import com.atid.lib.module.barcode.ssi.type.QuietZoneLevel1D;
import com.atid.lib.util.diagnotics.ATLog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Option1DQuietZoneDialog extends BaseDialog implements OnClickListener{
	private static final String TAG = Option1DQuietZoneDialog.class.getSimpleName();
	private static final int INFO = ATLog.L2;
	
	private TextView txt1DQuietZoneLevel;
	private TextView txtIntercharacterGapSize;
	
	private EnumListDialog dlg1DQuietZoneLevel;
	private EnumListDialog dlgIntercharacterGapSize;
	
	private QuietZoneLevel1D m1DQuietZoneLevel;
	private IntercharacterGapSize mIntercharacterGapSize;
	
	private Context mContext;
	
	public Option1DQuietZoneDialog(){
		super();
		
		m1DQuietZoneLevel = QuietZoneLevel1D.Level1;
		mIntercharacterGapSize = IntercharacterGapSize.Normal;
		
		mContext = null;
	}
	
	public QuietZoneLevel1D get1DQuietZoneLevel(){
		return m1DQuietZoneLevel;
	}
	
	public void set1DQuietZoneLevel(QuietZoneLevel1D value){
		m1DQuietZoneLevel = value;
	}
	
	public IntercharacterGapSize getIntercharacterGapSize(){
		return mIntercharacterGapSize;
	}
	
	public void setIntercharacterGapSize(IntercharacterGapSize value){
		mIntercharacterGapSize = value;
	}
	
	@Override
	public void display() {
		
	}

	@Override
	public void showDialog(Context context, String title, final IValueChangedListener changedListener,
			final ICancelListener cancelListener) {
	
		mContext = context;
		
		LinearLayout root = (LinearLayout) LinearLayout.inflate(context, R.layout.dialog_barcode_option_ssi_2d_1d_quiet_zone, null);
		
		txt1DQuietZoneLevel = (TextView) root.findViewById(R.id.quiet_zone_level_1d);
		txt1DQuietZoneLevel.setOnClickListener(this);
		txtIntercharacterGapSize = (TextView) root.findViewById(R.id.intercharacter_gap_size);
		txtIntercharacterGapSize.setOnClickListener(this);
		
		dlg1DQuietZoneLevel = new EnumListDialog(txt1DQuietZoneLevel, QuietZoneLevel1D.values());
		dlg1DQuietZoneLevel.setValue(m1DQuietZoneLevel);
		dlgIntercharacterGapSize = new EnumListDialog(txtIntercharacterGapSize, IntercharacterGapSize.values());
		dlgIntercharacterGapSize.setValue(mIntercharacterGapSize);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setView(root);
		builder.setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				m1DQuietZoneLevel = (QuietZoneLevel1D) dlg1DQuietZoneLevel.getValue();
				mIntercharacterGapSize = (IntercharacterGapSize)dlgIntercharacterGapSize.getValue();
				
				if(changedListener != null)
					changedListener.onValueChanged(Option1DQuietZoneDialog.this);
				
				ATLog.i(TAG, INFO, "INFO. showDialog().$PositiveButton.onClick()");
			}
		});
		
		builder.setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				if(cancelListener != null)
					cancelListener.onCanceled(Option1DQuietZoneDialog.this);
				
				ATLog.i(TAG, INFO, "INFO. showDialog().$NegativeButton.onClick()");
			}
		});
		
		builder.setCancelable(true);
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				
				if(cancelListener != null)
					cancelListener.onCanceled(Option1DQuietZoneDialog.this);
				ATLog.i(TAG, INFO, "INFO. showDialog().onCancel()");
			}
		});
		
		AlertDialog dialog = builder.create();
		dialog.setOnShowListener(new DialogInterface.OnShowListener() {
			
			@Override
			public void onShow(DialogInterface dialog) {
				
				dlg1DQuietZoneLevel.display();
				dlgIntercharacterGapSize.display();
				
				ATLog.i(TAG, INFO, "INFO. showDialog().onShow()");
			}
		});
		dialog.show();
		
		ATLog.i(TAG, INFO, "INFO. showDialog()");
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.quiet_zone_level_1d:
			dlg1DQuietZoneLevel.showDialog(mContext, R.string.quiet_zone_level_1d);
			break;
		case R.id.intercharacter_gap_size:
			dlgIntercharacterGapSize.showDialog(mContext, R.string.intercharacter_gap_size);
			break;
		}
	}

}
