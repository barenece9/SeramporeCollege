package com.lnsel.seramporecollege.activity.student;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lnsel.seramporecollege.R;
import com.lnsel.seramporecollege.fragment.student.AcadamicQualificationFragment;
import com.lnsel.seramporecollege.fragment.student.CourseDetailFragment;
import com.lnsel.seramporecollege.fragment.student.CurricularFragment;
import com.lnsel.seramporecollege.fragment.student.DocumentFragment;
import com.lnsel.seramporecollege.fragment.student.PersonalDetailsFragment;
import com.lnsel.seramporecollege.fragment.student.ResultFragment;

public class ProfileActivity extends AppCompatActivity {

    private TabLayout mTabLayout;

    private int[] mTabsIcons = {
            R.drawable.ic_person_black_24dp,
            R.drawable.ic_person_black_24dp,
            R.drawable.ic_person_black_24dp,
            R.drawable.ic_person_black_24dp,
            R.drawable.ic_person_black_24dp,
            R.drawable.ic_person_black_24dp};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Setup the viewPager
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setOffscreenPageLimit(5);
        MyPagerAdapter pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        if (viewPager != null)
            viewPager.setAdapter(pagerAdapter);

        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        if (mTabLayout != null) {
            mTabLayout.setupWithViewPager(viewPager);

            for (int i = 0; i < mTabLayout.getTabCount(); i++) {
                TabLayout.Tab tab = mTabLayout.getTabAt(i);
                if (tab != null)
                    tab.setCustomView(pagerAdapter.getTabView(i));
            }

            mTabLayout.getTabAt(0).getCustomView().setSelected(true);
        }
    }


    private class MyPagerAdapter extends FragmentPagerAdapter {

        public final int PAGE_COUNT = 6;

        private final String[] mTabsTitle = {"Personal\nDetails", "Academic\nQualification", "Curricular\nActivity","Document\nMaster","Course\nDetails","Result\nDetails"};

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public View getTabView(int position) {
            // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
            View view = LayoutInflater.from(ProfileActivity.this).inflate(R.layout.custom_tab, null);
            TextView title = (TextView) view.findViewById(R.id.title);
            title.setText(mTabsTitle[position]);
            ImageView icon = (ImageView) view.findViewById(R.id.icon);
            icon.setImageResource(mTabsIcons[position]);
            return view;
        }

        @Override
        public Fragment getItem(int pos) {
            switch (pos) {

                case 0:
                    PersonalDetailsFragment tab1 = new PersonalDetailsFragment();
                    return tab1;
                case 1:
                    AcadamicQualificationFragment tab2 = new AcadamicQualificationFragment();
                    return tab2;
                case 2:
                    CurricularFragment tab3 = new CurricularFragment();
                    return tab3;
                case 3:
                    DocumentFragment tab4 = new DocumentFragment();
                    return tab4;
                case 4:
                    CourseDetailFragment tab5 = new CourseDetailFragment();
                    return tab5;
                case 5:
                    ResultFragment tab6 = new ResultFragment();
                    return tab6;

            }
            return null;
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabsTitle[position];
        }
    }
    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Do you want to exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
        return true;
    }*/
}
