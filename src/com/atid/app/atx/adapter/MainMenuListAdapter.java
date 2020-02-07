package com.atid.app.atx.adapter;

import com.atid.app.atx.R;
import com.atid.app.atx.activity.view.BaseView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MainMenuListAdapter extends BaseAdapter {

	public static final int POS_INVENTORY = 0;
	public static final int POS_STORED_DATA = 1;
	public static final int POS_ACCESS_MEMORY = 2;
	public static final int POS_OPTION = 3;
	

	// ------------------------------------------------------------------------
	// Member Variable
	// ------------------------------------------------------------------------

	private LayoutInflater mInflater;
	private String[] mList;
	private int[] mIds;
	private int mSelectedItem;

	// ------------------------------------------------------------------------
	// Constructor
	// ------------------------------------------------------------------------

	public MainMenuListAdapter(Context context) {
		super();
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mList = context.getResources().getStringArray(R.array.view_name);
		mIds = new int[] { BaseView.VIEW_INVENTORY, BaseView.VIEW_STORED_DATA, BaseView.VIEW_ACCESS_MEMORY, 
				BaseView.VIEW_OPTION };
		mSelectedItem = POS_INVENTORY;
	}

	public int getSelectedItem() {
		return mSelectedItem;
	}
	
	public int getSelectedItemId() {
		return mIds[mSelectedItem];
	}

	public void setSelection(int position) {
		mSelectedItem = position;
		notifyDataSetChanged();
	}
	
	public int indexOf(int id) {
		for (int i = 0; i < mIds.length; i++) {
			if (mIds[i] == id)
				return i;
		}
		return -1;
	}

	@Override
	public int getCount() {
		return mList.length;
	}

	@Override
	public Object getItem(int position) {
		return mList[position];
	}

	@Override
	public long getItemId(int position) {
		return mIds[position];
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		MainMenuListViewHolder holder;

		if (null == convertView) {
			convertView = mInflater.inflate(R.layout.item_main_menu, parent, false);
			holder = new MainMenuListViewHolder(convertView);
		} else {
			holder = (MainMenuListViewHolder) convertView.getTag();
		}
		holder.displayItem(position, mList[position]);
		return convertView;
	}

	// ------------------------------------------------------------------------
	// Declare Class MainMenuListViewHolder
	// ------------------------------------------------------------------------

	private class MainMenuListViewHolder {

		private View mParent;
		private ImageView imgIcon;
		private TextView txtName;

		private MainMenuListViewHolder(View parent) {

			mParent = parent;
			imgIcon = (ImageView) parent.findViewById(R.id.menu_icon);
			txtName = (TextView) parent.findViewById(R.id.menu_name);
			parent.setTag(this);
		}

		private void displayItem(int position, String name) {
			switch (position) {
			case POS_INVENTORY:
				imgIcon.setImageResource(R.drawable.menu_inventory);
				break;
			case POS_STORED_DATA:
				imgIcon.setImageResource(R.drawable.menu_stored_data);
				break;
			case POS_ACCESS_MEMORY :
				imgIcon.setImageResource(R.drawable.menu_access_memory);
				break;
			case POS_OPTION:
				imgIcon.setImageResource(R.drawable.menu_option);
				break;
			}
			txtName.setText(name);

			if (position == mSelectedItem)
				mParent.setBackgroundResource(R.color.selecte_item_backgound);
			else
				mParent.setBackgroundResource(R.color.white);
		}
	}
}
