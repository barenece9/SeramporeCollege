package com.lnsel.seramporecollege.activity.student;

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
import com.lnsel.seramporecollege.activity.teacher.TeacherActivity;
import com.lnsel.seramporecollege.fragment.student.StudentAttandanceFragment;
import com.lnsel.seramporecollege.fragment.student.StudentFacultyContactFragment;
import com.lnsel.seramporecollege.fragment.student.FragmentDrawer;
import com.lnsel.seramporecollege.fragment.student.StudentInternalMarksFragment;
import com.lnsel.seramporecollege.fragment.student.StudentNoticeBordFragment;
import com.lnsel.seramporecollege.fragment.student.StudentPaymentDueFragment;
import com.lnsel.seramporecollege.fragment.student.StudentPaymentDetailsFragment;
import com.lnsel.seramporecollege.fragment.student.StudentProfileFragment;
import com.lnsel.seramporecollege.fragment.student.StudentTimeTableFragment;
import com.lnsel.seramporecollege.utils.ActivityUtil;
import com.lnsel.seramporecollege.utils.ConstantUtil;
import com.lnsel.seramporecollege.utils.SharedManagerUtil;

public class StudentActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    private static String TAG = StudentActivity.class.getSimpleName();

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    private String title="Profile";
    SharedManagerUtil session;
    boolean doubleBackToExitPressedOnce = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        session=new SharedManagerUtil(this);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
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
            new ActivityUtil(StudentActivity.this).startLoginActivity();
            return true;
        }

        if(id == R.id.action_about){
            new ActivityUtil(StudentActivity.this).startAboutActivity();
            return true;
        }

        if(id == R.id.action_change_password){
            new ActivityUtil(StudentActivity.this).startChangePasswordActivity();
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
                fragment = new StudentProfileFragment();
                title = getString(R.string.title_profile);
                ConstantUtil.fag_index=0;
                break;
            case 1:
                fragment = new StudentAttandanceFragment();
                title = getString(R.string.title_attendance_status);
                ConstantUtil.fag_index=1;
                break;
            case 2:
                fragment = new StudentPaymentDetailsFragment();
                title = getString(R.string.title_Fees_payment_status);
                ConstantUtil.fag_index=2;
                break;
            case 3:
                fragment = new StudentPaymentDueFragment();
                title = getString(R.string.title_payment_of_fees);
                ConstantUtil.fag_index=3;
                break;
            case 4:
                fragment = new StudentNoticeBordFragment();
                title = getString(R.string.title_notice_board);
                ConstantUtil.fag_index=4;
                break;
            case 5:
                fragment = new StudentFacultyContactFragment();
                title = getString(R.string.title_faculty_contact_details);
                ConstantUtil.fag_index=5;
                break;

            case 6:
                fragment = new StudentInternalMarksFragment();
                title = getString(R.string.title_internal_marks);
                ConstantUtil.fag_index=6;
                break;

            case 7:
                fragment = new StudentTimeTableFragment();
                title = getString(R.string.title_time_table);
                ConstantUtil.fag_index=7;
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

            if(title.equalsIgnoreCase("Profile")){
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
                StudentProfileFragment fragment = new StudentProfileFragment();
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