package com.atid.app.atx.dialog;

import java.util.ArrayList;

import com.atid.app.atx.R;
import com.atid.app.atx.dialog.BaseDialog.ICancelListener;
import com.atid.lib.module.rfid.uhf.params.FreqTableList;
import com.atid.lib.util.diagnotics.ATLog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class FreqTableDialog {

	private static final String TAG = FreqTableDialog.class.getSimpleName();
	private static final int INFO = ATLog.L2;

	private FreqTableList mTable;

	public FreqTableDialog() {
		super();
		mTable = new FreqTableList(0, 0);
	}

	public FreqTableList getTable() {
		return mTable;
	}

	public void setTable(FreqTableList table) {
		mTable = table;
	}

	public void showDialog(Context context, final ICancelListener cancelListener) {

		LinearLayout root = (LinearLayout) LinearLayout.inflate(context, R.layout.dialog_list_view, null);
		final ListView list = (ListView) root.findViewById(R.id.list);
		final FreqTableAdapter adapter = new FreqTableAdapter(context, mTable.getFreqNames());
		list.setAdapter(adapter);
		list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(R.string.frequency);
		builder.setView(root);
		builder.setNegativeButton(R.string.action_ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (cancelListener != null) {
					cancelListener.onCanceled(null);
				}
				ATLog.i(TAG, INFO, "INFO. showDialog().$NegativeButton.onClick()");
			}
		});
		builder.setCancelable(true);
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				if (cancelListener != null) {
					cancelListener.onCanceled(null);
				}
				ATLog.i(TAG, INFO, "INFO. showDialog().onCancel");
			}
		});
		AlertDialog dialog = builder.create();
		dialog.setOnShowListener(new DialogInterface.OnShowListener() {

			@Override
			public void onShow(DialogInterface dialog) {
				for (int i = 0; i < mTable.getCount(); i++) {
					adapter.setChecked(i, mTable.isUsed(i));
				}
				adapter.notifyDataSetChanged();
				ATLog.i(TAG, INFO, "INFO. showDialog().onShow()");
			}
		});
		dialog.show();

		ATLog.i(TAG, INFO, "INFO. showDialog()");
	}

	private class FreqTableAdapter extends BaseAdapter {

		private LayoutInflater mInflater;
		private ArrayList<FreqItem> mItems;

		private FreqTableAdapter(Context context, String[] names) {
			mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			mItems = new ArrayList<FreqItem>();
			for (int i = 0; i < names.length; i++)
				mItems.add(new FreqItem(names[i]));
		}

		public void setChecked(int position, boolean enabled) {
			mItems.get(position).IsUsed = enabled;
		}

		@Override
		public int getCount() {
			return mItems.size();
		}

		@Override
		public String getItem(int position) {
			return mItems.get(position).Name;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			FreqViewHolder holder = null;
			
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.item_frequency_list, parent, false);
				holder = new FreqViewHolder(convertView);
			} else {
				holder = (FreqViewHolder)convertView.getTag();
			}
			holder.display(mItems.get(position));
			return convertView;
		}

		private class FreqViewHolder {

			private CheckBox chkUsed;
			private TextView txtFreqName;

			private FreqViewHolder(View parent) {
				chkUsed = (CheckBox) parent.findViewById(R.id.used);
				txtFreqName = (TextView) parent.findViewById(R.id.freq_name);
				parent.setTag(this);
			}
			
			private void display(FreqItem item) {
				chkUsed.setChecked(item.IsUsed);
				txtFreqName.setText(item.Name);
			}
		}

		private class FreqItem {
			public String Name;
			public boolean IsUsed;

			private FreqItem(String name) {
				Name = name;
			}
		}
	}
}
