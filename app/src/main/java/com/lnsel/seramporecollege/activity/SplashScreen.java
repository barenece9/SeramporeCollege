package com.lnsel.seramporecollege.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;

import com.lnsel.seramporecollege.R;
import com.lnsel.seramporecollege.utils.ActivityUtil;
import com.lnsel.seramporecollege.utils.SharedManagerUtil;

public class SplashScreen extends Activity {

    private static int SPLASH_TIME_OUT = 2000;
    SharedManagerUtil session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        session=new SharedManagerUtil(this);


        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity

                if(session.isLoggedIn()){
                    if(session.getKeyUserType().equalsIgnoreCase("3")){
                        new ActivityUtil(SplashScreen.this).startStudentActivity();
                    }else if(session.getKeyUserType().equalsIgnoreCase("2")){
                        new ActivityUtil(SplashScreen.this).startTeacherActivity();
                    }else {
                        new ActivityUtil(SplashScreen.this).startLoginActivity();
                    }

                }else {
                    new ActivityUtil(SplashScreen.this).startLoginActivity();
                }

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
