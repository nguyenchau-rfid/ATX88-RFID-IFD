package com.atid.app.atx.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import com.atid.app.atx.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class KeyListAdapter extends BaseAdapter {
//	private static final String TAG = KeyListAdapter.class.getSimpleName();
//	private static final int INFO = ATLog.INTER;
	
	private LayoutInflater mInflater;
	private ArrayList<TriggerListItem> mList;
	private HashMap<String, TriggerListItem> mMap;
	
	public KeyListAdapter(Context context){
		super();
	
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mList = new ArrayList<TriggerListItem>();
		mMap = new HashMap<String, TriggerListItem>();
	}

	public synchronized void clear() {
		mList.clear();
		notifyDataSetChanged();
	}
	
	public synchronized void add(String type, String state){
		TriggerListItem item = null;
		String key = String.format(Locale.US, "%s%s",type , state);
		
		if((item = mMap.get(key)) == null) {
			item = new TriggerListItem(type, state);
			mMap.put(key, item);
			mList.add(item);
		} else {
			item.update();
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
		TriggerListViewHolder holder = null;
		TriggerListItem item = null;
		
		if( null == convertView){
			convertView = mInflater.inflate(R.layout.item_key_list, parent, false);
			holder = new TriggerListViewHolder(convertView);
		} else {
			holder = (TriggerListViewHolder) convertView.getTag();
		}
		
		synchronized(this){
			item = mList.get(position);	
		}
		holder.display(item);	
		
		return convertView;
	}
	
	// ------------------------------------------------------------------------
	// Declare Class TriggerListViewHolder
	// ------------------------------------------------------------------------
	private class TriggerListViewHolder {
		private TextView txtType;
		private TextView txtState;
		private TextView txtCount;
		
		private TriggerListViewHolder(View parent){
			
			txtType = (TextView) parent.findViewById(R.id.type);
			txtState = (TextView) parent.findViewById(R.id.state);
			txtCount = (TextView) parent.findViewById(R.id.count);
			parent.setTag(this);
		}
		
		public void display(TriggerListItem item){
			txtType.setText(item.getType());
			txtState.setText(item.getState());
			txtCount.setText(String.format(Locale.US, "%d", item.getCount()));
		}
	}
	
	// ------------------------------------------------------------------------
	// Declare Class TriggerListItem
	// ------------------------------------------------------------------------
	
	private class TriggerListItem {
		private String mType;
		private String mState;
		private int mCount;
		
		private TriggerListItem(String type, String state){
			mType = type;
			mState = state;
			mCount = 1;
		}
		
		public String getType(){
			return mType;
		}
		
		public String getState(){
			return mState;
		}
		
		public int getCount(){
			return mCount;
		}
		
		public void update(){
			mCount++;
		}
		
		@Override
		public String toString() {
			return String.format(Locale.US, "[%s]", mState);
		}
	}

}
