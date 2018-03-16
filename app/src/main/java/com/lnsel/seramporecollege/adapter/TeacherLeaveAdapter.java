package com.lnsel.seramporecollege.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lnsel.seramporecollege.R;
import com.lnsel.seramporecollege.utils.CommonMethod;

import java.util.ArrayList;
import java.util.HashMap;

public class TeacherLeaveAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> data;
    HashMap<String, String> resultp = new HashMap<String, String>();
    public TeacherLeaveAdapter(Context context, ArrayList<HashMap<String, String>> arraylist) {
        this.context = context;
        data = arraylist;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        View itemView = convertView;
        final MyViewHolder holder;
        if (itemView == null) {
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemView = inflater.inflate(R.layout.teacher_leave_list_item, null, false);
            holder = new MyViewHolder();
            holder.subject=(TextView) itemView.findViewById(R.id.subject);
            holder.description=(TextView) itemView.findViewById(R.id.description);

            holder.start_date=(TextView) itemView.findViewById(R.id.start_date);
            holder.end_date=(TextView) itemView.findViewById(R.id.end_date);

            holder.status=(TextView) itemView.findViewById(R.id.status);

            itemView.setTag(holder);
        } else {
            holder = (MyViewHolder) itemView.getTag();
        }
        resultp = data.get(position);

        //holder.start_date.setText(resultp.get("leave_frm_dt"));
        //holder.end_date.setText(resultp.get("leave_to_dt"));

        holder.start_date.setText(CommonMethod.dateConversion(resultp.get("leave_frm_dt")));
        holder.end_date.setText(CommonMethod.dateConversion(resultp.get("leave_to_dt")));


        holder.status.setText(resultp.get("approval_status_name"));

        holder.subject.setText(resultp.get("leave_type_name")+" Leave");
        holder.description.setText(resultp.get("number_of_days")+" days leave \n"+resultp.get("reason_of_leave"));

        return itemView;
    }
    private static class MyViewHolder {
        public TextView subject,description,start_date,end_date,status;
    }

    public void refresh(ArrayList<HashMap<String,String>> list){
        data = list;
       // this.arraylist = new ArrayList<HashMap<String,String>>();
       // this.arraylist.addAll(data);
        notifyDataSetChanged();
    }

}