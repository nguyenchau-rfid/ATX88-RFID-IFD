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

public class DeviceListWifiAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private ArrayList<DeviceItem> mList;

	public DeviceListWifiAdapter(Context context) {
		super();

		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mList = new ArrayList<DeviceItem>();
	}
	
	public void add(String name, String mac, String address) {
		DeviceItem item = new DeviceItem(ConnectType.Wifi, name, mac, address);
		if (mList.contains(item))
			return;
		mList.add(item);
	}

	public void clear() {
		mList.clear();
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public DeviceItem getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		DeviceListViewHolder mHolder;

		if (null == convertView) {
			convertView = mInflater.inflate(R.layout.item_device_wifi, parent, false);
			mHolder = new DeviceListViewHolder(convertView);
		} else {
			mHolder = (DeviceListViewHolder)convertView.getTag();
		}
		mHolder.displayItem(mList.get(position));
		return convertView;
	}

	// ------------------------------------------------------------------------
	// DeviceListBluetoothViewHolder
	// ------------------------------------------------------------------------

	private class DeviceListViewHolder {
		
		private ImageView imgType;
		private TextView txtName;
		private TextView txtMac;
		private TextView txtAddress;

		private DeviceListViewHolder(View parent) {
			
			imgType = (ImageView)parent.findViewById(R.id.device_type);
			txtName = (TextView)parent.findViewById(R.id.device_name);
			txtMac = (TextView)parent.findViewById(R.id.mac_address);
			txtAddress = (TextView)parent.findViewById(R.id.ip_address);
			
			parent.setTag(this);
		}

		private void displayItem(DeviceItem item) {
			imgType.setImageResource(ResUtil.getProductImage(item.getType()));
			txtName.setText(item.getName());
			txtMac.setText(item.getMac());
			txtAddress.setText(item.getAddress());
		}
	}
}
