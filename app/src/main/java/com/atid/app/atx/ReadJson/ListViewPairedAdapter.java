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

public class ListViewPairedAdapter  extends BaseAdapter {
    Context c;
    ArrayList<RfidToProductCode> RfidtoPrd;

    public ListViewPairedAdapter(Context c, ArrayList<RfidToProductCode> RfidtoPrd) {

        this.c = c;
        this.RfidtoPrd = RfidtoPrd;

    }
    @Override
    public int getCount() {
        return RfidtoPrd.size();
    }
    @Override
    public RfidToProductCode getItem(int position) {
        return RfidtoPrd.get(position);
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RfidToProductCode datamodel = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(c).inflate(R.layout.item_data_list, parent, false);
        }
        TextView txtData = (TextView) convertView.findViewById(R.id.data);

        TextView txtRightFoot = (TextView) convertView.findViewById(R.id.countRFID);
        TextView txtLeftFoot = (TextView) convertView.findViewById(R.id.countKiotviet);

        LinearLayout layoutTagItem = (LinearLayout) convertView.findViewById(R.id.tag_item);

        String tensp = String.format(Locale.US, "%s", datamodel.getProductcodeKiot() );

        String  LeftFoot= String.format(Locale.US, "%s",datamodel.getShoes_IsLeftFoot()+"");
        String  RightFoot= String.format(Locale.US, "%s", datamodel.getShoes_IsRightFoot() + "");


        String PairedWithTagID = String.format(Locale.US, "%s",datamodel.getShoes_PairedWithTagID()+"");
        String TagID = String.format(Locale.US, "%s",datamodel.getRFIDTagCode()+"");

        layoutTagItem.setVisibility(View.GONE);

        txtData.setText(tensp + "--"+TagID +"+"+ PairedWithTagID  );

        if(LeftFoot.equals("true"))
            txtLeftFoot.setText("Trái");
        else if(LeftFoot.equals("false"))
            txtLeftFoot.setText("Phải");
        else
            txtLeftFoot.setText("---");

        if(RightFoot.equals("true"))
            txtRightFoot.setText("Trái");
        else if(RightFoot.equals("false"))
                txtRightFoot.setText("Phải");
        else
            txtRightFoot.setText("---");



        return convertView;
    }
}
