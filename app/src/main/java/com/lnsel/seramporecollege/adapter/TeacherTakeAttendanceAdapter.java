package com.lnsel.seramporecollege.adapter;

import android.app.Activity;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;


import com.lnsel.seramporecollege.R;
import com.lnsel.seramporecollege.activity.teacher.EditStudentsAttendance;
import com.lnsel.seramporecollege.data.Student;

import java.util.List;

public class TeacherTakeAttendanceAdapter extends ArrayAdapter<Student> {

    private  List<Student> list;
    private  Activity context;
    Boolean activate=false;
    int count=0;

    public TeacherTakeAttendanceAdapter(Activity context, List<Student> list) {
        super(context, R.layout.inflate_take_attendance_list_item, list);
        this.context = context;
        this.list = list;
    }

    static class ViewHolder {
        protected TextView tv_name,tv_roll;
        protected CheckBox checkBox_status;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            convertView = inflator.inflate(R.layout.inflate_take_attendance_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.tv_roll = (TextView) convertView.findViewById(R.id.tv_roll);
            viewHolder.checkBox_status = (CheckBox) convertView.findViewById(R.id.checkBox_status);
            int id = Resources.getSystem().getIdentifier("btn_check_holo_light", "drawable", "android");
            viewHolder.checkBox_status.setButtonDrawable(id);
            viewHolder.checkBox_status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int getPosition = (Integer) buttonView.getTag();  // Here we get the position that we have set for the checkbox using setTag.
                    list.get(getPosition).setStatus(buttonView.isChecked()); // Set the value of checkbox to maintain its state.


                    if(buttonView.isPressed()) {

                        if (context instanceof EditStudentsAttendance) {
                            int count = 0;
                            for (int i = 0; i < list.size(); i++) {
                                if (list.get(i).getStatus()) {
                                    count = count + 1;
                                }
                            }
                            //((EditStudentsAttendance) context).selectCount(count);
                        }
                    }


                }
            });
            convertView.setTag(viewHolder);
            convertView.setTag(R.id.tv_name, viewHolder.tv_name);
            convertView.setTag(R.id.tv_roll, viewHolder.tv_roll);
            convertView.setTag(R.id.checkBox_status, viewHolder.checkBox_status);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.checkBox_status.setTag(position); // This line is important.

        viewHolder.tv_name.setText(list.get(position).getName());
        viewHolder.tv_roll.setText(list.get(position).getRoll()+".");
        viewHolder.checkBox_status.setChecked(list.get(position).getStatus());
        if (activate) {
            viewHolder.checkBox_status.setVisibility(View.VISIBLE);
        } else {
            viewHolder.checkBox_status.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    public void CheckBoxVisibility(Boolean activate){
        this.activate = activate;
        notifyDataSetChanged();
    }

    public void refreshList( List<Student> list){
        this.list=list;
        notifyDataSetChanged();
    }

}