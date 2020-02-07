package com.atid.app.atx.adapter;

import java.util.ArrayList;

import com.atid.app.atx.R;
import com.atid.app.atx.data.DeviceItem;
import com.atid.app.atx.util.ResUtil;
import com.atid.lib.transport.types.ConnectType;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DeviceListBluetoothAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private ArrayList<DeviceItem> mList;
	
	public DeviceListBluetoothAdapter(Context context) {
		super();

		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mList = new ArrayList<DeviceItem>();
	}
	
	public synchronized void add(String name, String address) {
		DeviceItem item = new DeviceItem(ConnectType.Bluetooth, name, address);
		if (mList.contains(item))
			return;
		mList.add(item);
		notifyDataSetChanged();
	}
	
	public synchronized void clear() {
		mList.clear();
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		int size = 0;
		synchronized(this) {
			size = mList.size(); 
		}
		return size;
	}

	@Override
	public DeviceItem getItem(int position) {
		DeviceItem item = null;
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

		DeviceListViewHolder mHolder;
		DeviceItem item = null;
		
		if (null == convertView) {
			convertView = mInflater.inflate(R.layout.item_device_bluetooth, parent, false);
			mHolder = new DeviceListViewHolder(convertView);
		} else {
			mHolder = (DeviceListViewHolder)convertView.getTag();
		}
		
		synchronized(this){
			item = mList.get(position);
		}
		mHolder.displayItem(item);	
		
		return convertView;
	}

	// ------------------------------------------------------------------------
	// DeviceListBluetoothViewHolder
	// ------------------------------------------------------------------------

	private class DeviceListViewHolder {
	
		private ImageView imgType;
		private TextView txtName;
		private TextView txtAddress;
		
		private DeviceListViewHolder(View parent) {
			
			imgType = (ImageView)parent.findViewById(R.id.device_type);
			txtName = (TextView)parent.findViewById(R.id.device_name);
			txtAddress = (TextView)parent.findViewById(R.id.device_address);

			parent.setTag(this);
		}
		
		private void displayItem(DeviceItem item) {
			if(item != null) {
				imgType.setImageResource(ResUtil.getProductImage(item.getType()));
				if(item.getName() != null)
					txtName.setText(item.getName());
				if(item.getAddress() != null)
					txtAddress.setText(item.getAddress());
			}
		}
	}
}
