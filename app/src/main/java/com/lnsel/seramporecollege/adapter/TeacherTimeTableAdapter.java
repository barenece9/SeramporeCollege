package com.lnsel.seramporecollege.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lnsel.seramporecollege.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class TeacherTimeTableAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> data;
    HashMap<String, String> resultp = new HashMap<String, String>();
    private ArrayList<HashMap<String,String>> arraylist;

    public TeacherTimeTableAdapter(Context context, ArrayList<HashMap<String, String>> arraylist) {
        this.context = context;
        data = arraylist;
        this.arraylist = new ArrayList<HashMap<String,String>>();
        this.arraylist.addAll(data);
    }

    public TeacherTimeTableAdapter(Context context) {
        // TODO Auto-generated constructor stub
        this.context=context;
        /*inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);*/
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
            itemView = inflater.inflate(R.layout.teacher_time_table_list_item, null, false);
            holder = new MyViewHolder();
            holder.subject=(TextView) itemView.findViewById(R.id.subject);
            holder.course_id=(TextView) itemView.findViewById(R.id.course_id);
            holder.paper_type=(TextView) itemView.findViewById(R.id.paper_type);

            holder.paper_code=(TextView) itemView.findViewById(R.id.paper_code);
            holder.year=(TextView) itemView.findViewById(R.id.year);

            holder.day_slot=(TextView) itemView.findViewById(R.id.day_slot);
            holder.time_slot=(TextView) itemView.findViewById(R.id.time_slot);
            holder.section=(TextView)itemView.findViewById(R.id.section);

            itemView.setTag(holder);
        } else {
            holder = (MyViewHolder) itemView.getTag();
        }
        resultp = data.get(position);

        holder.day_slot.setText(resultp.get("day_slot"));
        holder.time_slot.setText(resultp.get("time_slot"));

        holder.subject.setText(resultp.get("subject_name")+" ("+resultp.get("paper_type")+")");
        holder.course_id.setText("Course : "+resultp.get("course_id"));
        holder.course_id.setVisibility(View.GONE);
        holder.paper_code.setText("Paper Code : "+resultp.get("paper_code"));
       // holder.paper_type.setText("Paper Type : "+resultp.get("paper_type"));
        holder.paper_type.setVisibility(View.GONE);
        holder.year.setText("Year : "+resultp.get("year"));
        holder.section.setText("Section : "+resultp.get("section_name"));

        return itemView;
    }
    private static class MyViewHolder {
        public TextView subject,course_id,paper_code,paper_type,year,day_slot,time_slot,section;
    }


    // Filter Class
    public void filterByDate(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        data.clear();
        if (charText.length() == 0||charText.equalsIgnoreCase("")) {
            data.addAll(arraylist);
           // btn_clear.setVisibility(View.GONE);
        }
        else
        {
            for (HashMap<String,String> wp : arraylist)
            {
                if (wp.get("day_slot").toLowerCase(Locale.getDefault()).contains(charText)||
                        wp.get("time_slot").toLowerCase(Locale.getDefault()).contains(charText))
                {
                    data.add(wp);
                }
            }

          //  btn_clear.setVisibility(View.VISIBLE);
        }
        notifyDataSetChanged();
    }

    // Filter Class
    public void filterByTime(String charText) {
        ArrayList<HashMap<String,String>> dateFilterList=new ArrayList<>();
        //dateFilterList.addAll(data);
        charText = charText.toLowerCase(Locale.getDefault());
        dateFilterList.clear();
        if (charText.length() == 0||charText.equalsIgnoreCase("")) {
            dateFilterList.addAll(data);
        }
        else
        {
            for (HashMap<String,String> wp : data)
            {
                if (wp.get("day_slot").toLowerCase(Locale.getDefault()).contains(charText)||
                        wp.get("time_slot").toLowerCase(Locale.getDefault()).contains(charText))
                {
                    dateFilterList.add(wp);
                }
            }

        }
        refresh(dateFilterList);
       // notifyDataSetChanged();
    }


    public void refresh(ArrayList<HashMap<String,String>> list){
        data = list;
        this.arraylist = new ArrayList<HashMap<String,String>>();
        this.arraylist.addAll(data);
        notifyDataSetChanged();
    }


}