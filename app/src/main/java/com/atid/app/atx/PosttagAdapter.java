package com.atid.app.atx;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atid.app.atx.ReadJson.ChiTietSP;
import com.atid.app.atx.ReadJson.PostTag;

import java.util.ArrayList;
import java.util.Locale;

public class PosttagAdapter extends BaseAdapter {

    Context c;
    ArrayList<PostTag> inv_SP;

    public PosttagAdapter(Context c, ArrayList<PostTag> inv_SP) {

        this.c = c;
        this.inv_SP = inv_SP;

    }

    @Override
    public int getCount() {
        return inv_SP.size();
    }

    @Override
    public PostTag getItem(int position) {
        return inv_SP.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public String getTenSP(int i) {
        String name = this.inv_SP.get(i).getEpc();
        return name;

    }

    public Double getMaSP(int i) {
        Double Masp = this.inv_SP.get(i).getRfidTaskID();
        return Masp;
    }

    public String getsoluongRFID(int i) {
        String slRfid = this.inv_SP.get(i).getTrackingGPS_Latitude() + "";
        return slRfid;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PostTag datamodel = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(c).inflate(R.layout.item_data_list, parent, false);
        }
        TextView txtData = (TextView) convertView.findViewById(R.id.data);
        TextView txtCountKiotViet = (TextView) convertView.findViewById(R.id.countKiotviet);

        TextView txtCountRFID = (TextView) convertView.findViewById(R.id.countRFID);
        LinearLayout layoutTagItem = (LinearLayout) convertView.findViewById(R.id.tag_item);

        String tensp = String.format(Locale.US, "%s", datamodel.getEpc());

        String soluongKiotViet = String.format(Locale.US, "%s", datamodel.getDidError() + "");

        String soluongRFID = String.format(Locale.US, "%s", datamodel.getMessage());

        layoutTagItem.setVisibility(View.GONE);
        txtCountRFID.setText(soluongRFID);
        txtData.setText(tensp);
        txtCountKiotViet.setText(soluongKiotViet);

        return convertView;
    }
}
