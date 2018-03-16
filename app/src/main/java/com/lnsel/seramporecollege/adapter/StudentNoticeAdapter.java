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

public class StudentNoticeAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> data;
    HashMap<String, String> resultp = new HashMap<String, String>();
    public StudentNoticeAdapter(Context context,
                                ArrayList<HashMap<String, String>> arraylist) {
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
            itemView = inflater.inflate(R.layout.student_notice_list_item, null, false);
            holder = new MyViewHolder();
            holder.subject=(TextView) itemView.findViewById(R.id.subject);
            holder.description=(TextView) itemView.findViewById(R.id.description);

            holder.start_date=(TextView) itemView.findViewById(R.id.start_date);
            holder.end_date=(TextView) itemView.findViewById(R.id.end_date);
            itemView.setTag(holder);
        } else {
            holder = (MyViewHolder) itemView.getTag();
        }
        resultp = data.get(position);

        holder.start_date.setText(CommonMethod.dateConversion(resultp.get("date_start")));
        holder.end_date.setText(CommonMethod.dateConversion(resultp.get("date_end")));

        holder.subject.setText(resultp.get("notice_heading"));
        holder.description.setText(resultp.get("notice_body"));

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