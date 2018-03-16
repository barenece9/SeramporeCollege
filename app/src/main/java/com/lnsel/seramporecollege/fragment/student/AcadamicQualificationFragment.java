package com.lnsel.seramporecollege.fragment.student;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.lnsel.seramporecollege.R;
import com.lnsel.seramporecollege.adapter.AcademicAdapter;
import java.util.ArrayList;
import java.util.HashMap;


public class AcadamicQualificationFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    private static final String TAG_COURSES = "allcourses";
    private static final String TAG_FEES = "course_fees";
    private static final String TAG_NAME = "course_name";
    private static final String TAG_DURATION = "course_duration";
    private static final String TAG_DESCRIPTION = "course_description";

    boolean _areLecturesLoaded = false;

    private static View rootView;
    public static AcadamicQualificationFragment newInstance(int pageNo) {

        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNo);
        AcadamicQualificationFragment fragment = new AcadamicQualificationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null)
                parent.removeView(rootView);
        }
        try {
            rootView = inflater.inflate(R.layout.fragment_academic_qualification, container, false);
        } catch (InflateException e) {

        }

        return rootView;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !_areLecturesLoaded ) {
            loadLectures();
            _areLecturesLoaded = false;
        }
    }
    public void loadLectures(){

        ListView list_view=(ListView)rootView.findViewById(R.id.list_view);

        ArrayList<HashMap<String,String>> contactList=new ArrayList<>();
        AcademicAdapter adapter = new AcademicAdapter(getActivity(), contactList);
        list_view.setAdapter(adapter);
    }

}
