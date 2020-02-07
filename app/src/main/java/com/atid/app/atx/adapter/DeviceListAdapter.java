package com.atid.app.atx.adapter;

import com.atid.app.atx.R;
import com.atid.app.atx.device.ATEAReaderManager;
import com.atid.app.atx.util.ResUtil;
import com.atid.lib.reader.ATEAReader;
import com.atid.lib.transport.ATransport;
import com.atid.lib.util.diagnotics.ATLog;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

@SuppressLint("NewApi")
@SuppressWarnings("unused")
public class DeviceListAdapter extends BaseAdapter {

	private final static String TAG = DeviceListAdapter.class.getSimpleName();
	private final static int INFO = ATLog.INTER;
	
	private LayoutInflater mInflater;
	private ATEAReaderManager mReaders;
	private OnListItemClickListener mListener;

	public DeviceListAdapter(Context context, ATEAReaderManager readers) {
		super();

		mReaders = readers;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mListener = null;
	}
	
	public void setOnListItemClickListener(OnListItemClickListener listener) {
		mListener = listener;
	}
	
	@Override
	public int getCount() {
		return mReaders.count();
	}

	@Override
	public ATEAReader getItem(int position) {
		return mReaders.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		DeviceListViewHolder mHolder;
		
		if (null == convertView) {
			convertView = mInflater.inflate(R.layout.item_list_device, parent, false);
			mHolder = new DeviceListViewHolder(convertView);
		} else {
			mHolder = (DeviceListViewHolder)convertView.getTag();
		}
		
		mHolder.displayItem(position, mReaders.get(position).getTransport());	
		
		return convertView;
	}

	// ------------------------------------------------------------------------
	// View Holder Class
	// ------------------------------------------------------------------------

	private class DeviceListViewHolder implements OnClickListener {

		private int mPosition;
		private ImageView imgDevType;
		private ImageView imgConnType;
		private TextView txtDevName;
		private TextView txtAddress;
		private Button btnMore;
		private ProgressBar progMore;

		public DeviceListViewHolder(View parent) {
			
			mPosition = -1;
			imgDevType = (ImageView)parent.findViewById(R.id.device_type);
			imgConnType = (ImageView)parent.findViewById(R.id.connect_type);
			txtDevName = (TextView)parent.findViewById(R.id.device_name);
			txtAddress = (TextView)parent.findViewById(R.id.device_address);
			btnMore = (Button)parent.findViewById(R.id.action_more);
			btnMore.setOnClickListener(this);
			progMore = (ProgressBar)parent.findViewById(R.id.progress_more);
			parent.setTag(this);
		}
		
		@SuppressWarnings("deprecation")
		public void displayItem(int position, ATransport transport) {
			mPosition = position;
			imgDevType.setImageResource(ResUtil.getProductImage(transport.getDeviceType()));
			imgConnType.setImageResource(ResUtil.getConnectTypeImage(transport.getConnectType()));
			txtDevName.setText(transport.getDeviceName());
			txtAddress.setText(transport.getAddress());
			switch (transport.getState()) {
			case Disconnected:
				progMore.setVisibility(View.GONE);
				btnMore.setVisibility(View.VISIBLE);
				if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
					btnMore.setBackground(btnMore.getResources().getDrawable(R.drawable.btn_more_red_selector, null));
				} else {
					btnMore.setBackground(btnMore.getResources().getDrawable(R.drawable.btn_more_red_selector));
				}
				break;
			case Listen:
				progMore.setVisibility(View.GONE);
				btnMore.setVisibility(View.VISIBLE);
				if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
					btnMore.setBackground(btnMore.getResources().getDrawable(R.drawable.btn_more_green_selector, null));
				} else {
					btnMore.setBackground(btnMore.getResources().getDrawable(R.drawable.btn_more_green_selector));
				}
				break;
			case Connected:
				progMore.setVisibility(View.GONE);
				btnMore.setVisibility(View.VISIBLE);
				if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
					btnMore.setBackground(btnMore.getResources().getDrawable(R.drawable.btn_more_blue_selector, null));
				} else {
					btnMore.setBackground(btnMore.getResources().getDrawable(R.drawable.btn_more_blue_selector));
				}
				break;
			default:
				progMore.setVisibility(View.VISIBLE);
				btnMore.setVisibility(View.GONE);
				break;
			}
		}

		@Override
		public void onClick(View v) {
			if (mListener == null)
				return;
			mListener.onItemButtonClick(mPosition, v);
		}
	}

	// ------------------------------------------------------------------------
	// Item Click Listener Interface
	// ------------------------------------------------------------------------
	
	public interface OnListItemClickListener {
		void onItemButtonClick(int position, View view);
	}
}
