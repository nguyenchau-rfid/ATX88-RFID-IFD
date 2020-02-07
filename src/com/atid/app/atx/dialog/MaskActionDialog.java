package com.atid.app.atx.dialog;

import com.atid.app.atx.R;
import com.atid.lib.module.rfid.uhf.types.Mask6cAction;
import com.atid.lib.module.rfid.uhf.types.Mask6cTarget;
import com.atid.lib.util.diagnotics.ATLog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class MaskActionDialog extends BaseDialog {

	private static final String TAG = MaskActionDialog.class.getSimpleName();
	private static final int INFO = ATLog.L2;

	private Mask6cAction mAction;
	private Mask6cTarget mTarget;

	public MaskActionDialog() {
		super();
		mAction = Mask6cAction.AB;
	}

	public MaskActionDialog(TextView view) {
		super(view);
		mAction = Mask6cAction.AB;
	}

	public Mask6cAction getAction() {
		return mAction;
	}

	public void setAction(Mask6cAction action) {
		mAction = action;
	}

	public Mask6cTarget getTarget() {
		return mTarget;
	}

	public void setTarget(Mask6cTarget target) {
		mTarget = target;
	}

	@Override
	public void display() {

		if (txtValue == null)
			return;

		String[] actions = mTarget == Mask6cTarget.SL
				? txtValue.getResources().getStringArray(R.array.mask_6c_select_action)
				: txtValue.getResources().getStringArray(R.array.mask_6c_session_action);
		if (mTarget != null)
			txtValue.setText(actions[mTarget.getCode()]);
	}

	@Override
	public void showDialog(Context context, String title, final IValueChangedListener changedListener,
			final ICancelListener cancelListener) {

		if (txtValue != null) {
			if (!txtValue.isEnabled())
				return;
		}

		LinearLayout root = (LinearLayout) LinearLayout.inflate(context, R.layout.dialog_list_view, null);
		final ListView list = (ListView) root.findViewById(R.id.list);
		final MaskActionAdapter adapter = new MaskActionAdapter(context, mTarget);
		list.setAdapter(adapter);
		list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setView(root);
		builder.setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				int position = list.getCheckedItemPosition();
				mAction = adapter.getItem(position);
				display();
				if (changedListener != null) {
					changedListener.onValueChanged(MaskActionDialog.this);
				}
				ATLog.i(TAG, INFO, "INFO. showDialog().$PositiveButton.onClick()");
			}
		});
		builder.setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (cancelListener != null) {
					cancelListener.onCanceled(MaskActionDialog.this);
				}
				ATLog.i(TAG, INFO, "INFO. showDialog().$NegativeButton.onClick()");
			}
		});
		builder.setCancelable(true);
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				if (cancelListener != null) {
					cancelListener.onCanceled(MaskActionDialog.this);
				}
				ATLog.i(TAG, INFO, "INFO. showDialog().onCancel()");
			}
		});
		AlertDialog dialog = builder.create();
		dialog.setOnShowListener(new DialogInterface.OnShowListener() {

			@Override
			public void onShow(DialogInterface dialog) {
				int position = mAction.getCode();
				list.setItemChecked(position, true);
				list.setSelectionFromTop(position, 0);
				ATLog.i(TAG, INFO, "INFO. showDialog().onShow()");
			}
		});
		dialog.show();

		ATLog.i(TAG, INFO, "INFO. showDialog()");
	}

	// ------------------------------------------------------------------------
	// Declare Class Mask Action Adapter
	// ------------------------------------------------------------------------

	private class MaskActionAdapter extends BaseAdapter {

		private LayoutInflater mInflater;
		private Mask6cTarget mTarget;
		private String[] mSelectAction;
		private String[] mSessionAction;

		private MaskActionAdapter(Context context, Mask6cTarget target) {
			super();

			mTarget = target;
			mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			mSelectAction = context.getResources().getStringArray(R.array.mask_6c_select_action);
			mSessionAction = context.getResources().getStringArray(R.array.mask_6c_session_action);
		}

		@Override
		public int getCount() {
			if (mTarget == Mask6cTarget.SL)
				return mSelectAction.length;
			return mSessionAction.length;
		}

		@Override
		public Mask6cAction getItem(int position) {
			return Mask6cAction.valueOf(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			MaskActionViewHolder holder = null;

			if (null == convertView) {
				convertView = mInflater.inflate(R.layout.item_mask_6c_action, parent, false);
				holder = new MaskActionViewHolder(convertView);
			} else {
				holder = (MaskActionViewHolder) convertView.getTag();
			}
			if (mTarget == Mask6cTarget.SL)
				holder.setItem(mSelectAction[position]);
			else
				holder.setItem(mSessionAction[position]);
			return convertView;
		}

		// --------------------------------------------------------------------
		// Internal MaskActionViewHodler
		// --------------------------------------------------------------------

		private class MaskActionViewHolder {

			private TextView txtValue;

			private MaskActionViewHolder(View parent) {
				txtValue = (TextView) parent.findViewById(android.R.id.text1);
				parent.setTag(this);
			}

			public void setItem(String action) {
				txtValue.setText(action);
			}
		}

	}
}
