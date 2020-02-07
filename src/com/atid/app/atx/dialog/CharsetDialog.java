package com.atid.app.atx.dialog;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import com.atid.app.atx.R;
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

public class CharsetDialog extends StringDialog {
	private static final String TAG = CharsetDialog.class.getSimpleName();
	private static final int INFO = ATLog.L2;
	
	public CharsetDialog() {
		super();
	}

	@Override
	public void display() {
		if (txtValue == null)
			return;

		txtValue.setText(String.format(Locale.US, "%s", this.mValue));
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
		final CharsetAdapter adapter = new CharsetAdapter(context);
		list.setAdapter(adapter);
		list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setView(root);
		builder.setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				int position = list.getCheckedItemPosition();
				mValue = adapter.getItemValue(position);
				display();
				if (changedListener != null) {
					changedListener.onValueChanged(CharsetDialog.this);
				}
				ATLog.i(TAG, INFO, "INFO. showDialog().$PositiveButton.onClick()");
			}
		});
		builder.setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (cancelListener != null) {
					cancelListener.onCanceled(CharsetDialog.this);
				}
				ATLog.i(TAG, INFO, "INFO. showDialog().$NegativeButton.onClick()");
			}
		});
		builder.setCancelable(true);
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				if (cancelListener != null) {
					cancelListener.onCanceled(CharsetDialog.this);
				}
				ATLog.i(TAG, INFO, "INFO. showDialog().onCancel()");
			}
		});
		AlertDialog dialog = builder.create();
		dialog.setOnShowListener(new DialogInterface.OnShowListener() {

			@Override
			public void onShow(DialogInterface dialog) {
				int position = adapter.getItemPosition(mValue);
				ATLog.e(TAG, String.format("mValue:%s, position:%d", mValue, position));
				list.setItemChecked(position, true);
				list.setSelectionFromTop(position, 0);
				ATLog.i(TAG, INFO, "INFO. showDialog().onShow()");
			}
		});
		dialog.show();

		ATLog.i(TAG, INFO, "INFO. showDialog()");
	}
	
	// ------------------------------------------------------------------------
	// Declare Class Charset Adapter
	// ------------------------------------------------------------------------

	private class CharsetAdapter extends BaseAdapter {

		private LayoutInflater mInflater;
		private ArrayList<String> mList;

		// --------------------------------------------------------------------
		// Constructor
		// --------------------------------------------------------------------

		@SuppressWarnings("rawtypes")
		public CharsetAdapter(Context context) {
			super();

			mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			mList = new ArrayList<String>();
			
			SortedMap<String, Charset> charSets = Charset.availableCharsets();
			Set s = charSets.entrySet();
			Iterator i = s.iterator();
			while(i.hasNext()) {
				Map.Entry m = (Map.Entry)i.next();
				Charset value = (Charset)m.getValue();
				mList.add(value.name());
			}
		}

		public String getItemValue(int position) {
			return mList.get(position);
		}

		public int getItemPosition(String value) {
			for (int i = 0; i < mList.size(); i++) {
				if(mList.get(i).toUpperCase(Locale.US).equals(value.toUpperCase(Locale.US)))
					return i;
			}
			return 0;
		}

		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			CharsetViewHolder holder = null;

			if (null == convertView) {
				convertView = mInflater.inflate(android.R.layout.simple_list_item_single_choice, parent, false);
				holder = new CharsetViewHolder(convertView);
			} else {
				holder = (CharsetViewHolder) convertView.getTag();
			}
			holder.setItem(mList.get(position));
			return convertView;
		}

		// ------------------------------------------------------------------------
		// Internal CharsetViewHolder Class
		// ------------------------------------------------------------------------
		private class CharsetViewHolder {
			private TextView txtValue;

			public CharsetViewHolder(View parent) {
				txtValue = (TextView) parent.findViewById(android.R.id.text1);
				parent.setTag(this);
			}
			
			public void setItem(String item) {
				txtValue.setText(item);
			}
		}
	}
}
