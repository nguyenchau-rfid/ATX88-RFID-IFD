package com.atid.app.atx.ReadJson;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.atid.app.atx.R;

import java.text.DecimalFormat;
import java.util.List;

public class ViewTaskAdapter extends ArrayAdapter<ModelRFIDTasksAdapter> {

    private final Context context;
    private final List<ModelRFIDTasksAdapter> taskList;
    TokenManager doingay=new TokenManager();
    public ViewTaskAdapter(@NonNull Context context, int resource, @NonNull List<ModelRFIDTasksAdapter> objects) {
        super(context, resource, objects);
        taskList = objects;
        this.context = context;
    }

    @Override
    public ModelRFIDTasksAdapter getItem(int position) {
        return taskList.get(position);
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.customshow, parent, false);

        TextView idTask = rowView.findViewById(R.id.tvIdTask);
        TextView tvDescription = (TextView) rowView.findViewById(R.id.tvDescription);
        TextView tvDate = (TextView) rowView.findViewById(R.id.tvDate);

        ModelRFIDTasksAdapter listTask = taskList.get(position);
        //first convert the Double to String
        DecimalFormat formatter = new DecimalFormat("#0");
        double D= listTask.getRfidTaskID();
      //  System.out.println(formatter.format(D));
//        double D =listTask.getRfidTaskID();
//        String s = String.valueOf(D);
//        String str = s.replace(".0", "");
//        int i = Integer.parseInt(str);

        idTask.setText(formatter.format(D)+"");
        tvDescription.setText(listTask.getDescription());
        String ngaygio=doingay.setDateHour(listTask.getVoucherDate());
        tvDate.setText(ngaygio);
        return rowView;
    }
}
