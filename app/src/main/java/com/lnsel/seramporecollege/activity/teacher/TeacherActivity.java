package com.lnsel.seramporecollege.activity.teacher;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.lnsel.seramporecollege.R;
import com.lnsel.seramporecollege.fragment.teacher.TeacherEditAttandanceFragment;
import com.lnsel.seramporecollege.fragment.teacher.TeacherTakeAttandanceFragment;
import com.lnsel.seramporecollege.fragment.teacher.TeacherDeptTimeTableFragment;
import com.lnsel.seramporecollege.fragment.teacher.TeacherFragmentDrawer;
import com.lnsel.seramporecollege.fragment.teacher.TeacherLeaveDetailsFragment;
import com.lnsel.seramporecollege.fragment.teacher.TeacherLeaveRecordFragment;
import com.lnsel.seramporecollege.fragment.teacher.TeacherNoticeBordFragment;
import com.lnsel.seramporecollege.fragment.teacher.TeacherPaySlipFragment;
import com.lnsel.seramporecollege.fragment.teacher.TeacherProfileFragment;
import com.lnsel.seramporecollege.fragment.teacher.TeacherTimeTableFragment;
import com.lnsel.seramporecollege.utils.ActivityUtil;
import com.lnsel.seramporecollege.utils.ConstantUtil;
import com.lnsel.seramporecollege.utils.SharedManagerUtil;

public class TeacherActivity extends AppCompatActivity implements TeacherFragmentDrawer.FragmentDrawerListener {

    private static String TAG = TeacherActivity.class.getSimpleName();
    private Toolbar mToolbar;
    private TeacherFragmentDrawer drawerFragment;
    private String title="Profile";
    boolean doubleBackToExitPressedOnce = false;
    SharedManagerUtil session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);

        session=new SharedManagerUtil(this);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerFragment = (TeacherFragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer_teacher);
        drawerFragment.setUp(R.id.fragment_navigation_drawer_teacher, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        // display the first navigation drawer view on app launch
        displayView(ConstantUtil.fag_index);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            session.logoutUser();
            new ActivityUtil(TeacherActivity.this).startLoginActivity();
            return true;
        }

        if(id == R.id.action_about){
            new ActivityUtil(TeacherActivity.this).startAboutActivity();
            return true;
        }

        if(id == R.id.action_change_password){
            new ActivityUtil(TeacherActivity.this).startChangePasswordActivity();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        title = getString(R.string.app_name);
        switch (position) {
            case 0:
                fragment = new TeacherProfileFragment();
                title = getString(R.string.title_teacher_profile);
                ConstantUtil.fag_index=0;

                break;
            case 1:
                fragment = new TeacherTakeAttandanceFragment();
                title = getString(R.string.title_teacher_attendance);
                ConstantUtil.fag_index=1;
                break;

            case 2:
                fragment = new TeacherEditAttandanceFragment();
                title = getString(R.string.title_teacher_edit_attendance);
                ConstantUtil.fag_index=2;

                break;

            case 3:
                fragment = new TeacherTimeTableFragment();
                title = getString(R.string.title_teacher_time_table);
                ConstantUtil.fag_index=3;

                break;

            case 4:
                fragment = new TeacherDeptTimeTableFragment();
                title = getString(R.string.title_teacher_dept_time_table);
                ConstantUtil.fag_index=4;

                break;

            case 5:
                fragment = new TeacherLeaveDetailsFragment();
                title = getString(R.string.title_teacher_leave_details);
                ConstantUtil.fag_index=5;

                break;

            case 6:
                fragment = new TeacherLeaveRecordFragment();
                title = getString(R.string.title_teacher_leave_record);
                ConstantUtil.fag_index=6;

                break;

            case 7:
                fragment = new TeacherPaySlipFragment();
                title = getString(R.string.title_teacher_payslip);
                ConstantUtil.fag_index=7;
                break;

            case 8:
                fragment = new TeacherNoticeBordFragment();
                title = getString(R.string.title_teacher_notice_board);
                ConstantUtil.fag_index=8;
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if(title.equalsIgnoreCase("Take Attendance")){
                Toast.makeText(this, "Press cancel to exit", Toast.LENGTH_SHORT).show();
            }else if(title.equalsIgnoreCase("Profile")){
                //finish();
                if (doubleBackToExitPressedOnce) {
                    finish();
                }

                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Press once again to exit", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce=false;
                    }
                }, 2000);
            }else if(!title.equalsIgnoreCase("Profile")) {
                TeacherProfileFragment fragment = new TeacherProfileFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.commit();
                // set the toolbar title
                title="Profile";
                getSupportActionBar().setTitle(title);
            }else {
                finish();
            }
        }
        return true;
    }


}