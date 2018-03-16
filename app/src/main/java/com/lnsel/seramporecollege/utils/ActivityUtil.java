package com.lnsel.seramporecollege.utils;

import android.content.Context;
import android.content.Intent;

import com.lnsel.seramporecollege.MainActivity;
import com.lnsel.seramporecollege.activity.AboutActivity;
import com.lnsel.seramporecollege.activity.ChangePasswordActivity;
import com.lnsel.seramporecollege.activity.student.ReceiptPdfViewActivity;
import com.lnsel.seramporecollege.activity.student.ReceiptWebViewActivity;
import com.lnsel.seramporecollege.activity.teacher.PayslipWebViewActivity;
import com.lnsel.seramporecollege.activity.teacher.ApplyLeaveActivity;
import com.lnsel.seramporecollege.activity.LoginActivity;

import com.lnsel.seramporecollege.activity.teacher.EditStudentsAttendance;
import com.lnsel.seramporecollege.activity.teacher.PayslipPdfViewActivity;
import com.lnsel.seramporecollege.activity.student.StudentActivity;
import com.lnsel.seramporecollege.activity.teacher.TeacherActivity;

/**
 * Created by db on 1/17/2018.
 */

public class ActivityUtil {

    private Context context;

    public ActivityUtil(Context context) {
        this.context = context;
    }

    public void startLoginActivity() {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    public void startMainActivity() {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    public void startStudentActivity() {
        Intent intent = new Intent(context, StudentActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    public void startTeacherActivity() {
        Intent intent = new Intent(context, TeacherActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    public void startPayslipViewActivity() {
        Intent intent = new Intent(context, PayslipPdfViewActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    public void startApplyLeaveActivity() {
        Intent intent = new Intent(context, ApplyLeaveActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    public void startTakeStudentsAttendance() {
        Intent intent = new Intent(context, EditStudentsAttendance.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }


    public void startPdfViewActivity() {
        Intent intent = new Intent(context, PayslipWebViewActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    public void startReceiptPdfViewActivity() {
        Intent intent = new Intent(context, ReceiptPdfViewActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    public void startAboutActivity() {
        Intent intent = new Intent(context, AboutActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    public void startChangePasswordActivity() {
        Intent intent = new Intent(context, ChangePasswordActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }
}
