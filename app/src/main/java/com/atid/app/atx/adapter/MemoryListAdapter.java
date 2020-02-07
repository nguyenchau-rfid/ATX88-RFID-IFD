package com.atid.app.atx.adapter;

import java.util.ArrayList;
import java.util.Locale;

import com.atid.app.atx.R;
import com.atid.lib.util.StringUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MemoryListAdapter extends BaseAdapter {

	private static final int MAX_COL = 4;
	private static final int MAX_DISPLAY_LENGTH = 16;
	private static final int MAX_ROW_BIT_LENGTH = 4;

	private LayoutInflater mInflater;
	private ArrayList<MemoryListItem> mList;

	public MemoryListAdapter(Context context) {
		super();

		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mList = new ArrayList<MemoryListItem>();
		clear();
	}

	public synchronized void clear() {
		mList.clear();
		mList.add(new MemoryListItem());
		notifyDataSetChanged();
	}

	public synchronized void setValue(int offset, String data) {
		int i = 0;
		int row = (int) Math.ceil((double) data.length() / (double) MAX_DISPLAY_LENGTH);
		int address = 0;
		String value = "";

		mList.clear();

		if (data.length() < row * MAX_DISPLAY_LENGTH)
			data = StringUtil.padRight(data, row * MAX_DISPLAY_LENGTH, '0');
		for (i = 0; i < row; i++) {
			address = (i * MAX_ROW_BIT_LENGTH) + offset;
			if (data.length() > (i * MAX_DISPLAY_LENGTH) * MAX_DISPLAY_LENGTH) {
				value = data.substring(i * MAX_DISPLAY_LENGTH, (i * MAX_DISPLAY_LENGTH) + MAX_DISPLAY_LENGTH);
			} else {
				value = data.substring(i * MAX_DISPLAY_LENGTH);
			}
			mList.add(new MemoryListItem(address, value));
		}
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		int size = 0;
		synchronized(this){
			size = mList.size();
		}
		return size;
	}

	@Override
	public Object getItem(int position) {
		Object item = null;
		synchronized(this){
			item = mList.get(position);
		}
		return item;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		MemoryListViewHolder holder = null;
		MemoryListItem item = null;
		
		if (null == convertView) {
			convertView = mInflater.inflate(R.layout.item_memory_list, parent, false);
			holder = new MemoryListViewHolder(convertView);
		} else {
			holder = (MemoryListViewHolder)convertView.getTag();
		}
		
		synchronized(this){
			item = mList.get(position);
		}
		holder.setItem(item);	
		
		return convertView;
	}

	// ------------------------------------------------------------------------
	// Declare Class MemoryListItem
	// ------------------------------------------------------------------------

	private class MemoryListItem {

		private static final int DISPLAY_VALUE_LENGTH = 4;

		//private String[] mAddress;
		private String[] mValue;

		public MemoryListItem() {
			//mAddress = new String[] { "0 WORD", "1 WORD", "2 WORD", "3 WORD" };
			mValue = new String[] { "0000", "0000", "0000", "0000" };
		}

		public MemoryListItem(int offset, String tag) {
			String data = StringUtil.padRight(tag, MAX_DISPLAY_LENGTH, '0');
			//mAddress = new String[MAX_COL];
			mValue = new String[MAX_COL];
			for (int i = 0; i < MAX_COL; i++) {
				//mAddress[i] = String.format(Locale.US, "%d WORD", offset + i);
				mValue[i] = data.substring(i * MAX_COL, (i * MAX_COL) + DISPLAY_VALUE_LENGTH);
			}
		}

//		public String getAddress(int index) {
//			return mAddress[index];
//		}

		public String getValue(int index) {
			return mValue[index];
		}

//		@Override
//		public String toString() {
//			return String.format(Locale.US, "%s:%s, %s:%s, %s:%s, %s:%s", mAddress[0], mValue[0], mAddress[1],
//					mValue[1], mAddress[2], mValue[2], mAddress[3], mValue[3]);
//		}
		@Override
		public String toString() {
			return String.format(Locale.US, "%s, %s, %s, %s", mValue[0], 
					mValue[1], mValue[2], mValue[3]);
		}

	}

	// ------------------------------------------------------------------------
	// Declare Class MemoryListViewHolder
	// ------------------------------------------------------------------------

	private class MemoryListViewHolder {

		//private TextView[] mAddress;
		private TextView[] mValue;

		public MemoryListViewHolder(View parent) {
//			mAddress = new TextView[] { (TextView) parent.findViewById(R.id.address1),
//					(TextView) parent.findViewById(R.id.address2), (TextView) parent.findViewById(R.id.address3),
//					(TextView) parent.findViewById(R.id.address4), };
			mValue = new TextView[] { (TextView) parent.findViewById(R.id.value1),
					(TextView) parent.findViewById(R.id.value2), (TextView) parent.findViewById(R.id.value3),
					(TextView) parent.findViewById(R.id.value4), };
			parent.setTag(this);
		}

		public void setItem(MemoryListItem item) {
			if(item != null){
				for (int i = 0; i < MAX_COL; i++) {
					//mAddress[i].setText(item.getAddress(i));
					mValue[i].setText(item.getValue(i));
				}
			}
		}
	}
}
