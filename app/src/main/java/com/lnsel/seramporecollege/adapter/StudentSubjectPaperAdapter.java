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

public class StudentSubjectPaperAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> data;
    HashMap<String, String> resultp = new HashMap<String, String>();
    public StudentSubjectPaperAdapter(Context context,
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
            itemView = inflater.inflate(R.layout.student_subject_paper_list_item, null, false);
            holder = new MyViewHolder();
            holder.tv_subject=(TextView) itemView.findViewById(R.id.tv_subject);
            holder.tv_paper=(TextView) itemView.findViewById(R.id.tv_paper);
            holder.tv_class=(TextView) itemView.findViewById(R.id.tv_class);
            holder.tv_present=(TextView) itemView.findViewById(R.id.tv_present);
            itemView.setTag(holder);
        } else {
            holder = (MyViewHolder) itemView.getTag();
        }
        resultp = data.get(position);

        holder.tv_subject.setText(resultp.get("name"));
        holder.tv_paper.setText(resultp.get("paper_code"));
       // holder.tv_class.setText(resultp.get("id"));
      //  holder.tv_present.setText(resultp.get("id"));

        return itemView;
    }
    private static class MyViewHolder {
        public TextView tv_subject,tv_paper,tv_class,tv_present;
    }


    public void refresh(ArrayList<HashMap<String,String>> list){
        data = list;
        notifyDataSetChanged();
    }


}