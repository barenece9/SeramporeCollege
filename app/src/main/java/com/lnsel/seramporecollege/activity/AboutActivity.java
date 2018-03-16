package com.lnsel.seramporecollege.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.lnsel.seramporecollege.R;
import com.lnsel.seramporecollege.utils.ActivityUtil;
import com.lnsel.seramporecollege.utils.ConstantUtil;
import com.lnsel.seramporecollege.utils.SharedManagerUtil;

public class AboutActivity extends AppCompatActivity {


    SharedManagerUtil session;
    ImageView logo_center;
    TextView copyright_tv_version_number,copyright_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        session=new SharedManagerUtil(this);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("About Us");

        copyright_tv=(TextView)findViewById(R.id.copyright_tv);
        copyright_tv_version_number=(TextView)findViewById(R.id.copyright_tv_version_number);
        logo_center=(ImageView)findViewById(R.id.logo_center);
        getVersionInfo();

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here

                if(session.getKeyUserType().equalsIgnoreCase("2")){
                    new ActivityUtil(AboutActivity.this).startTeacherActivity();
                }else if(session.getKeyUserType().equalsIgnoreCase("3")){
                    new ActivityUtil(AboutActivity.this).startStudentActivity();
                }else {
                    new ActivityUtil(AboutActivity.this).startLoginActivity();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getVersionInfo() {
        String versionName = "";
        int versionCode = -1;
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = packageInfo.versionName;
            versionCode = packageInfo.versionCode;
            ConstantUtil.APP_VERSION=String.valueOf(packageInfo.versionCode);
            copyright_tv_version_number.setText("v "+versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        ConstantUtil.DEVICE_ID= Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d("Version name : ",versionName);
        Log.d("Version code : ",String.valueOf(versionCode));
    }

    @Override
    public void onBackPressed() {
        if(session.getKeyUserType().equalsIgnoreCase("2")){
            new ActivityUtil(AboutActivity.this).startTeacherActivity();
        }else if(session.getKeyUserType().equalsIgnoreCase("3")){
            new ActivityUtil(AboutActivity.this).startStudentActivity();
        }else {
            new ActivityUtil(AboutActivity.this).startLoginActivity();
        }
        super.onBackPressed();
    }

}
