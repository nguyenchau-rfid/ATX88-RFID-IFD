package com.atid.app.atx.ReadJson;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atid.app.atx.R;

import java.util.ArrayList;
import java.util.Locale;

public class ListViewAdapter extends BaseAdapter {
    Context c;
    ArrayList<ChiTietSP> inv_SP;

    public ListViewAdapter(Context c, ArrayList<ChiTietSP> inv_SP) {

        this.c = c;
        this.inv_SP = inv_SP;

    }

    @Override
    public int getCount() {
        return inv_SP.size();
    }

    @Override
    public ChiTietSP getItem(int position) {
        return inv_SP.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public String getTenSP(int i) {
        String name = this.inv_SP.get(i).getName();
        return name;

    }

    public Integer getMaSP(int i) {
        Integer Masp = this.inv_SP.get(i).getId();
        return Masp;
    }

    public String getsoluongRFID(int i) {
        String slRfid = this.inv_SP.get(i).getDemsoluong() + "";
        return slRfid;
    }

    public Integer getsoluongKiot(int i) {
        Integer slKiot = this.inv_SP.get(i).getInv().get(i).getOnHand();
        return slKiot;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChiTietSP datamodel = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(c).inflate(R.layout.item_data_list, parent, false);
        }
        TextView txtData = (TextView) convertView.findViewById(R.id.data);
        TextView txtCountKiotViet = (TextView) convertView.findViewById(R.id.countKiotviet);

        TextView txtCountRFID = (TextView) convertView.findViewById(R.id.countRFID);
        LinearLayout layoutTagItem = (LinearLayout) convertView.findViewById(R.id.tag_item);

        String tensp = String.format(Locale.US, "%s", datamodel.getId() + "\n" + datamodel.getName());

        String soluongKiotViet = String.format(Locale.US, "%s", datamodel.getOnHand() + "");

        String soluongRFID = String.format(Locale.US, "%s", datamodel.getDemsoluong());

        layoutTagItem.setVisibility(View.GONE);
        txtCountRFID.setText(soluongRFID);
        if (datamodel.getName() == null || datamodel.getOnHand() == null || soluongKiotViet.equals("")) {
            txtData.setText(datamodel.getCodeRFID() + "--");
            txtCountKiotViet.setText("0");
        } else {
            txtData.setText(tensp);
            txtCountKiotViet.setText(soluongKiotViet);
        }

        return convertView;
    }
}
