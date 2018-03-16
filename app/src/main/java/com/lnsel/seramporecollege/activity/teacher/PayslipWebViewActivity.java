package com.lnsel.seramporecollege.activity.teacher;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.lnsel.seramporecollege.R;
import com.lnsel.seramporecollege.utils.ActivityUtil;
import com.lnsel.seramporecollege.utils.ConstantUtil;
import com.lnsel.seramporecollege.utils.SharedManagerUtil;
import com.lnsel.seramporecollege.utils.UrlUtil;

public class PayslipWebViewActivity extends AppCompatActivity {


    private static final String TAG = PayslipWebViewActivity.class.getSimpleName();
    SharedManagerUtil session;
    WebView webview;
    ProgressBar progressbar;
    String payslipName="Payslip";

    private final static int REQUEST_CODE = 42;
    public static final int PERMISSION_CODE = 42042;
    // public static final String READ_EXTERNAL_STORAGE = "android.permission.READ_EXTERNAL_STORAGE";
    public static final String WRITE_EXTERNAL_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payslip_web_view);
        session=new SharedManagerUtil(this);
        payslipName="Payslip-"+ ConstantUtil.payslipMonth+"-"+ConstantUtil.payslipYear;
        Log.e("payslipName==> ",payslipName);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().set
        getSupportActionBar().setTitle(payslipName);


        String file_url= UrlUtil.BASE_DOWNLOAD_URL+ConstantUtil.payslipUrl;
        Log.e("url==> ",file_url);

        webview = (WebView)findViewById(R.id.webview);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        webview.getSettings().setJavaScriptEnabled(true);
        String filename ="http://61.16.131.206/erp_srcc/employee/downloads/payslip/payslip_1516258041.pdf";
        //String filename ="http://www3.nd.edu/~cpoellab/teaching/cse40816/android_tutorial.pdf";
        //String filename ="http://www.seramporecollege.org/theology/wp-content/uploads/2012/08/academic_calendar1718.pdf";
        //webview.loadUrl(filename);
        webview.loadUrl("http://docs.google.com/gview?embedded=true&url=" + file_url);
        webview.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {
                // do your stuff here
                progressbar.setVisibility(View.GONE);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.download_menu, menu);
        return true;
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {


            case android.R.id.home:
                // todo: goto back activity from here

                new ActivityUtil(PayslipWebViewActivity.this).startTeacherActivity();
                return true;

            case R.id.action_download:

                permissionTest();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    void permissionTest() {
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                WRITE_EXTERNAL_STORAGE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE}, PERMISSION_CODE);

            return;
        }

        lunchDownloadActivity();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                lunchDownloadActivity();
            }else {
                Toast.makeText(getApplicationContext(),"Payslip can not download without storage permission",Toast.LENGTH_SHORT).show();
               // new ActivityUtil(PayslipWebViewActivity.this).startTeacherActivity();
            }
        }
    }

    public void lunchDownloadActivity(){
        new ActivityUtil(PayslipWebViewActivity.this).startPayslipViewActivity();
    }

    @Override
    public void onBackPressed() {
        new ActivityUtil(PayslipWebViewActivity.this).startTeacherActivity();
        super.onBackPressed();
    }

}
