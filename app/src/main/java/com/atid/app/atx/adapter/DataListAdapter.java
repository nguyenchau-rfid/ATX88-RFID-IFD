package com.atid.app.atx.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import com.atid.app.atx.R;
import com.atid.lib.module.barcode.types.BarcodeType;
import com.atid.lib.util.diagnotics.ATLog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DataListAdapter extends BaseAdapter {

	private static final String TAG = DataListAdapter.class.getSimpleName();
	private static final int INFO = ATLog.INTER;

	public static final int DATATYPE_RFID = 0;
	public static final int DATATYPE_BARCODE = 1;

	@SuppressWarnings("unused")
	private static final int MAX_BUFFER_COUNT = 10;
	
	private LayoutInflater mInflater;
	private ArrayList<DataListItem> mList;
	private HashMap<String, DataListItem> mMap;

	private volatile boolean mIsDisplayPc;
	private volatile boolean mIsReportRssi;
	private volatile boolean mIsReportTid;

	public DataListAdapter(Context context) {
		super();

		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mList = new ArrayList<DataListItem>();
		mMap = new HashMap<String, DataListItem>();

		mIsDisplayPc = true;

		mIsReportRssi = false;
		mIsReportTid = false;
		
	}

	public synchronized void clear() {
		mList.clear();
		mMap.clear();
		notifyDataSetChanged();
		
		ATLog.i(TAG, INFO, "INFO. clear()");
	}

	public synchronized void add(String tag, String tid, float rssi, float phase) {
		DataListItem item = null;
		
		String key = String.format(Locale.US, "%s%s", tag, tid);
		if ((item = mMap.get(key)) == null) {
			item = new DataListItem(tag, tid, rssi, phase);
			mMap.put(key,  item);
			mList.add(item);

		} else {
			item.update(rssi, phase);
		}
		notifyDataSetChanged();

	}
	public synchronized void updateRssi(String tag, String tid, float rssi, float phase)
    {
		DataListItem item = null;

		String key = String.format(Locale.US, "%s%s", tag, tid);
		if ((item = mMap.get(key)) == null) {
			item = new DataListItem(tag, tid, rssi, phase);
			mMap.put(key,  item);
			//mList.add(item);

		} else {
			item.update(rssi, phase);
		}
		notifyDataSetChanged();
    }

	public synchronized void add(BarcodeType type, String codeId, String barcode) {
		DataListItem item = null;
		
		String key = String.format(Locale.US, "%s%s%s", type, codeId, barcode);
		if ((item = mMap.get(key)) == null) {
			item = new DataListItem(type, codeId, barcode);
			mMap.put(key,  item);
			mList.add(item);

		} else {
			item.update();
		}
		notifyDataSetChanged();
	}	
	
	public void setDisplayPC(boolean enabled) {
		mIsDisplayPc = enabled;
		notifyDataSetChanged();
	}

	public void setReportRssi(boolean enabled) {
		mIsReportRssi = enabled;
		notifyDataSetChanged();
	}

	public void setReportTid(boolean enabled) {
		mIsReportTid = enabled;
		notifyDataSetChanged();
	}

	public synchronized String getData(int position) {
		return mList.get(position).getData();
	}
	public synchronized Integer getDataCount(int position) {
		return mList.get(position).getCount();
	}
	public Float getmRSSI(int position)
	{
		return mList.get(position).getRssi();
	}
	public synchronized int getDataType(int position) {
		return mList.get(position).getDataType();
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
		DataListViewHolder holder = null;
		DataListItem item = null;
		
		if (null == convertView) {
			convertView = mInflater.inflate(R.layout.item_data_list, parent, false);
			holder = new DataListViewHolder(convertView);
		} else {
			holder = (DataListViewHolder) convertView.getTag();
		}
		
		synchronized(this){
			item = mList.get(position);
		}
		holder.display(item);
		
		return convertView;
	}

	// ------------------------------------------------------------------------
	// Declare Class DataListViewHolder
	// ------------------------------------------------------------------------

	private class DataListViewHolder {

		private ImageView imgType;
		private LinearLayout layoutBarcodeItem;
		private TextView txtCodeType;
		private TextView txtCodeId;
		private TextView txtData;
		private TextView txtTid;
		private LinearLayout layoutTagItem;
		private TextView txtRssi;
		private TextView txtPhase;
		private TextView txtCount;
		private TextView txtcoutKiot;
		private DataListViewHolder(View parent) {

			imgType = (ImageView) parent.findViewById(R.id.data_type);
			layoutBarcodeItem = (LinearLayout) parent.findViewById(R.id.barcode_item);
			txtCodeType = (TextView) parent.findViewById(R.id.code_type);
			txtCodeId = (TextView) parent.findViewById(R.id.code_id);
			txtData = (TextView) parent.findViewById(R.id.data);
			txtTid = (TextView) parent.findViewById(R.id.tid_value);
			layoutTagItem = (LinearLayout) parent.findViewById(R.id.tag_item);
			txtRssi = (TextView) parent.findViewById(R.id.rssi);
			txtPhase = (TextView) parent.findViewById(R.id.phase);
			txtCount = (TextView) parent.findViewById(R.id.countRFID);
			txtcoutKiot = (TextView) parent.findViewById(R.id.countKiotviet);
			parent.setTag(this);
		}

		public void display(DataListItem item) {

			if(item != null) {
				if (item.getDataType() == DATATYPE_RFID) {
					imgType.setImageResource(R.drawable.ic_method_rfid);
					layoutBarcodeItem.setVisibility(View.GONE);
					txtData.setText(mIsDisplayPc ? item.getData()
							: item.getData().length() > 4 ? item.getData().substring(4) : item.getData());
					if (mIsReportTid) {
						txtTid.setVisibility(View.VISIBLE);
						txtTid.setText(item.getExt());
					} else {
						txtTid.setVisibility(View.GONE);
					}
					if (mIsReportRssi) {
						layoutTagItem.setVisibility(View.VISIBLE);
						txtRssi.setText(String.format(Locale.US, "%.2f dB", item.getRssi()));
						txtPhase.setText(String.format(Locale.US, "%.2f \u00B0", item.getPhase()));
					} else {
						layoutTagItem.setVisibility(View.GONE);
					}
				} else {
					imgType.setImageResource(R.drawable.ic_method_barcode);
					layoutBarcodeItem.setVisibility(View.VISIBLE);
					txtTid.setVisibility(View.GONE);
					layoutTagItem.setVisibility(View.GONE);

					txtData.setText(item.getData());
					txtCodeType.setText(item.getBarcodeType().toString());
					txtCodeId.setText(item.getExt());
				}
				txtcoutKiot.setText(item.getRssi()+"-");
				txtCount.setText(String.format(Locale.US, "%d", item.getCount()));
			}
		}
		
	}

	// ------------------------------------------------------------------------
	// Declare Class DataListItem
	// ------------------------------------------------------------------------

	private class DataListItem {

		private int mDataType;
		private String mData;
		private String mExt;
		private BarcodeType mType;
		private float mRssi;
		private float mPhase;
		private int mCount;

		private DataListItem(String tag, String tid, float rssi, float phase) {
			mDataType = DATATYPE_RFID;
			mData = tag;
			mExt = tid;
			mRssi = rssi;
			mPhase = phase;
			mType = BarcodeType.Unknown;
			mCount = 1;
		}

		private DataListItem(BarcodeType type, String codeId, String barcode) {
			mDataType = DATATYPE_BARCODE;
			mData = barcode;
			mExt = codeId;
			mRssi = 0;
			mPhase = 0;
			mType = type;
			mCount = 1;
		}

		public int getDataType() {
			return mDataType;
		}

		public String getData() {
			return mData;
		}

		public String getExt() {
			return mExt;
		}

		public float getRssi() {
			return mRssi;
		}

		public float getPhase() {
			return mPhase;
		}
		
		public BarcodeType getBarcodeType() {
			return mType;
		}
		
		public int getCount() {
			return mCount;
		}
		
		public void update() {
			mCount++;
		}
		
		public void update(float rssi, float phase) {
			mRssi = rssi;
			mPhase = phase;
			mCount++;
		}

		@Override
		public String toString() {
			if (mDataType == DATATYPE_RFID)
				return String.format(Locale.US, "[%s], [%s], %.2f, %.2f", mData, mExt, mRssi, mPhase);
			else
				return String.format(Locale.US, "%s, [%s], [%s]", mType, mExt, mData);
		}
	}
}
