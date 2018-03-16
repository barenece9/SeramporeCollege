package com.lnsel.seramporecollege.activity.teacher;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import com.lnsel.seramporecollege.R;
import com.lnsel.seramporecollege.adapter.TeacherTakeAttendanceAdapter;
import com.lnsel.seramporecollege.data.Student;
import com.lnsel.seramporecollege.utils.ActivityUtil;
import com.lnsel.seramporecollege.utils.SharedManagerUtil;

import java.util.ArrayList;

public class EditStudentsAttendance extends AppCompatActivity {

    SharedManagerUtil session;

    ListView ll_student;
    TeacherTakeAttendanceAdapter attendanceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_students_attendance);
        session=new SharedManagerUtil(this);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Take Attendance");


        ArrayList<Student> barcodeList=new ArrayList<>();
        for(int i=0;i<20;i++){
            Student barcode=new Student();
            barcode.setName("Student"+i);
            barcode.setStatus(false);
            barcodeList.add(barcode);
        }
        //ll_student=(ListView)findViewById(R.id.ll_student);
        attendanceAdapter = new TeacherTakeAttendanceAdapter(EditStudentsAttendance.this,barcodeList);
        ll_student.setAdapter(attendanceAdapter);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here

                customDialog();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onBackPressed() {
        customDialog();
    }


    public void customDialog(){
        final Dialog dialog = new Dialog(EditStudentsAttendance.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_custom);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView dialog_title=(TextView)findViewById(R.id.dialog_title);
        TextView dialog_common_header = (TextView) dialog.findViewById(R.id.dialog_common_header);
        //dialog_common_header.setText("");

        TextView dialog_confirmation_cancel = (TextView) dialog.findViewById(R.id.dialog_confirmation_cancel);
        dialog_confirmation_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        final TextView dialog_confirmation_ok = (TextView) dialog.findViewById(R.id.dialog_confirmation_ok);
        dialog_confirmation_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ActivityUtil(EditStudentsAttendance.this).startTeacherActivity();
                dialog.dismiss();

            }
        });
        dialog.show();
    }
}
