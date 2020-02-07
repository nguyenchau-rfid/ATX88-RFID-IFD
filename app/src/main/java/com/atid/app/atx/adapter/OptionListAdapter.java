package com.atid.app.atx.adapter;

import java.util.ArrayList;

import com.atid.app.atx.R;
import com.atid.lib.util.diagnotics.ATLog;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class OptionListAdapter extends BaseExpandableListAdapter {

	private static final String TAG = OptionListAdapter.class.getSimpleName();

	private static final int GROUP_DEVICE = 0;

	public static final int ITEM_FIRMWARE_VERSION = 0;
	public static final int ITEM_SERIAL_NO = 1;
	public static final int ITEM_SYSTEM_TIME = 2;
	public static final int ITEM_DISPLAY_OFF_TIME = 3;
	public static final int ITEM_AUTO_OFF_TIME = 4;
	public static final int ITEM_BUTTON_MODE = 5;
	public static final int ITEM_BUTTON_NOTFIY = 6;
	public static final int ITEM_ALERT_NOTIFY = 7;

	// ------------------------------------------------------------------------
	// Member Varable
	// ------------------------------------------------------------------------

	private LayoutInflater mInflater;
	private ArrayList<GroupItem> mGroup;
	private ArrayList<OptionItem> mItem;

	// ------------------------------------------------------------------------
	// Constructor
	// ------------------------------------------------------------------------

	public OptionListAdapter(Context context) {
		super();

		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mGroup = new ArrayList<GroupItem>();
		mItem = new ArrayList<OptionItem>();

		Resources res = context.getResources();

		final String[] group = res.getStringArray(R.array.option_group);
		final int[] deviceItems = new int[] { R.string.firmware_version, R.string.serial_no, R.string.system_time,
				R.string.display_off_time, R.string.auto_off_time, R.string.button_mode, R.string.button_notify,
				R.string.alert_notify };

		int i = 0;
		ArrayList<OptionItem> items = null;
		OptionItem item = null;

		for (i = 0; i < group.length; i++)
			mGroup.add(new GroupItem(group[i]));
		items = mGroup.get(GROUP_DEVICE).getItems();
		for (i = 0; i < deviceItems.length; i++) {
			item = new OptionItem(res.getString(deviceItems[i]));
			mItem.add(item);
			items.add(item);
		}
	}

	public void display(int id, String value) {

		OptionItem item = mItem.get(id);
		item.setValue(value);
	}

	// ------------------------------------------------------------------------
	// Overridable Methods
	// ------------------------------------------------------------------------

	@Override
	public int getGroupCount() {
		return mGroup.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return mGroup.get(groupPosition).getItems().size();
	}

	@Override
	public GroupItem getGroup(int groupPosition) {
		return mGroup.get(groupPosition);
	}

	@Override
	public OptionItem getChild(int groupPosition, int childPosition) {
		return mGroup.get(groupPosition).getItems().get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		OptionItem item = mGroup.get(groupPosition).getItems().get(childPosition);
		return mItem.indexOf(item);
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

		GroupViewHolder holder = null;

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_option_group, parent, false);
			holder = new GroupViewHolder(convertView);
		} else {
			holder = (GroupViewHolder) convertView.getTag();
		}
		holder.display(mGroup.get(groupPosition).getName());
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
			ViewGroup parent) {

		ItemViewHolder holder = null;

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_option_item, parent, false);
			holder = new ItemViewHolder(convertView);
		} else {
			holder = (ItemViewHolder) convertView.getTag();
		}
		try {
			holder.display(mGroup.get(groupPosition).getItems().get(childPosition));
		} catch (Exception ex) {
			ATLog.e(TAG, ex, "ERROR. getChildView(%d, %d, %s)", groupPosition, childPosition, isLastChild);
		}
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	// ------------------------------------------------------------------------
	// Declare View Holder Classes
	// ------------------------------------------------------------------------

	private class GroupViewHolder {

		private TextView txtName;

		private GroupViewHolder(View parent) {
			txtName = (TextView) parent.findViewById(R.id.name);
			parent.setTag(this);
		}

		public void display(String name) {
			txtName.setText(name);
		}
	}

	private class ItemViewHolder {

		private TextView txtName;
		private TextView txtValue;

		private ItemViewHolder(View parent) {
			txtName = (TextView) parent.findViewById(R.id.name);
			txtValue = (TextView) parent.findViewById(R.id.value);
			parent.setTag(this);
		}

		public void display(OptionItem item) {
			if(item != null){
				txtName.setText(item.getName());
				txtValue.setText(item.getValue());
			}
		}
	}

	// ------------------------------------------------------------------------
	// Declare Item Classes
	// ------------------------------------------------------------------------

	public class GroupItem {

		private String mName;
		private ArrayList<OptionItem> mItems;

		public GroupItem(String name) {
			mName = name;
			mItems = new ArrayList<OptionItem>();
		}

		public String getName() {
			return mName;
		}

		public ArrayList<OptionItem> getItems() {
			return mItems;
		}
	}

	public class OptionItem {

		private String mName;
		private String mValue;

		public OptionItem(String name) {
			mName = name;
			mValue = "";
		}

		public String getName() {
			return mName;
		}

		public String getValue() {
			return mValue;
		}

		public void setValue(String value) {
			mValue = value;
		}
	}
}
