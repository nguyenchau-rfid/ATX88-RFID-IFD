package com.atid.app.atx.data;

import java.util.Locale;

import com.atid.lib.transport.types.ConnectType;
import com.atid.lib.types.DeviceType;
import com.atid.lib.util.StringUtil;

import android.os.Parcel;
import android.os.Parcelable;

public class DeviceItem implements Parcelable {

	private static final String DEVTYPE_AT188 = "AT188";
	private static final String DEVTYPE_AT288 = "AT288";
	private static final String DEVTYPE_AT388 = "AT388";
	private static final String DEVTYPE_RFBLASTER = "RFBLASTER";
	private static final String DEVTYPE_RFPRISMA = "RFPRISMA";
	private static final String DEVTYPE_ATS100 = "ATS100";

	private ConnectType mConnType;
	private DeviceType mType;
	private String mName;
	private String mMac;
	private String mAddress;

	public DeviceItem(ConnectType connType, String name, String address) {
		mConnType = connType;
		mType = parseType(name);
		mName = name;
		mMac = "";
		mAddress = address;
	}

	public DeviceItem(ConnectType connType, String name, String mac, String address) {
		mConnType = connType;
		mType = parseType(name);
		mName = name;
		mMac = mac;
		mAddress = address;
	}

	public DeviceItem(DeviceType devType, ConnectType connType, String name, String mac, String address) {
		mConnType = connType;
		mType = devType;
		mName = name;
		mMac = mac;
		mAddress = address;
	}
	
	public DeviceItem(Parcel source) {
		mConnType = ConnectType.valueOf(source.readInt());
		mType = DeviceType.valueOf(source.readInt());
		mName = source.readString();
		mMac = source.readString();
		mAddress = source.readString();
	}

	public ConnectType getConnectType() {
		return mConnType;
	}
	
	public DeviceType getType() {
		return mType;
	}

	public String getName() {
		return mName;
	}

	public String getMac() {
		return mMac;
	}

	public String getAddress() {
		return mAddress;
	}

	@Override
	public String toString() {
		return String.format(Locale.US, "%s, %s, [%s], [%s], [%s]", mConnType, mType, mName, mMac, mAddress);
	}

	@Override
	public boolean equals(Object obj) {
		DeviceItem item = (DeviceItem) obj;
		if (StringUtil.isNullOrEmpty(mMac)) {
			return mType == item.getType() && mName.equals(item.getName()) && mAddress.equals(item.getAddress());
		} else {
			return mType == item.getType() && mName.equals(item.getName()) && mMac.equals(item.getMac())
					&& mAddress.equals(item.getAddress());
		}
	}

	private static DeviceType parseType(String name) {
		if (StringUtil.isNullOrEmpty(name))
			return DeviceType.Unknown;
		
		name = name.toUpperCase(Locale.US);

		if (name.contains(DEVTYPE_AT188))
			return DeviceType.AT188N;
		else if (name.contains(DEVTYPE_AT288))
			return DeviceType.AT288N;
		else if (name.contains(DEVTYPE_AT388))
			return DeviceType.AT388;
		else if (name.contains(DEVTYPE_RFBLASTER))
			return DeviceType.RFBlaster;
		else if (name.contains(DEVTYPE_RFPRISMA))
			return DeviceType.RFPrisma;
		else if (name.contains(DEVTYPE_ATS100))
			return DeviceType.ATS100;
		return DeviceType.Unknown;
	}
	
	public static boolean contains(String name) {
		return parseType(name) != DeviceType.Unknown;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(mConnType.getCode());
		dest.writeInt(mType.getCode());
		dest.writeString(mName);
		dest.writeString(mMac);
		dest.writeString(mAddress);
	}
	
	public static final Parcelable.Creator<DeviceItem> CREATOR = new Parcelable.Creator<DeviceItem>() {

		@Override
		public DeviceItem createFromParcel(Parcel source) {
			return new DeviceItem(source);
		}

		@Override
		public DeviceItem[] newArray(int size) {
			return new DeviceItem[size];
		}
	};
}
