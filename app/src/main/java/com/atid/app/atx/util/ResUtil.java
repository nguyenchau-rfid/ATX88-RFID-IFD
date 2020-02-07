package com.atid.app.atx.util;

import com.atid.app.atx.R;
import com.atid.lib.transport.types.ConnectType;
import com.atid.lib.types.DeviceType;

public class ResUtil {

	// 한글
	public static int getProductImage(DeviceType type) {
		switch (type) {
		case AT188:
		case AT188N:
			return R.drawable.ic_product_at188;
		case AT288:
		case AT288N:
			return R.drawable.ic_product_at288;
		case AT388:
			return R.drawable.ic_product_at388;
		case RFBlaster:
		case RFPrisma:
			return R.drawable.ic_product_rfprisma;
		case ATS100:
			return R.drawable.ic_product_ats100;
		default:
			return R.drawable.ic_unknown;
		}
	}
	
	public static int getConnectTypeImage(ConnectType type) {
		switch (type) {
		case Bluetooth:
			return R.drawable.ic_connect_type_bluetooth;
		case Wifi:
			return R.drawable.ic_connect_type_wifi;
		case USB:
			return R.drawable.ic_connect_type_usb;
		case UART:
			return R.drawable.ic_connect_type_uart;
		default:
			return R.drawable.ic_unknown;
		}
	}
}
