package com.lnsel.seramporecollege.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by db on 1/18/2018.
 */

public class CommonMethod {
    public static String getMonth(int month) {
        try {
            return new DateFormatSymbols().getMonths()[month-1];
        }catch (Exception e){
            return "NA";
        }

    }

    public static String getMonthName(String date) throws ParseException {
        Date d = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        int month = cal.get(Calendar.MONTH);
        System.out.println(date+" getMonthName Date : "+getMonth(month + 1));
        return getMonth(month + 1);
    }

    public static String getCurrentDay(){
        String dayOfTheWeek="";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
            Date d = new Date();
            dayOfTheWeek = sdf.format(d);
        }catch (Exception e){
        }
        System.out.print(" dayOfTheWeek====> "+dayOfTheWeek);
        return dayOfTheWeek;
    }

    public static String dateConversion(String inputDate){

        String outputDate="";
        SimpleDateFormat inputSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat outputSimpleDateFormat = new SimpleDateFormat("EEEE, MMM d, yyyy");
        Date date = null;
        try {
            date = inputSimpleDateFormat.parse(inputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        outputDate=outputSimpleDateFormat.format(date);
        return outputDate;
    }


    public static void setListViewHeightBasedOnChildren(Activity mActivity,
                                                        ListView list) {

        ListAdapter listAdapter = list.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(list.getWidth(),
                View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, list);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth,
                        LinearLayout.LayoutParams.WRAP_CONTENT));

            DisplayMetrics displayMetrics = new DisplayMetrics();
            mActivity.getWindowManager().getDefaultDisplay()
                    .getMetrics(displayMetrics);
            int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(
                    displayMetrics.widthPixels, View.MeasureSpec.EXACTLY);
            int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(
                    displayMetrics.heightPixels, View.MeasureSpec.AT_MOST);
            view.measure(widthMeasureSpec, heightMeasureSpec);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = list.getLayoutParams();
        params.height = totalHeight
                + (list.getDividerHeight() * (listAdapter.getCount() - 1));
        list.setLayoutParams(params);
        list.requestLayout();
    }


    public static File getOutputMediaFileInPublicDirectory(String type, Context mContext, String fileNameWithExtnsn)
            throws Exception {

        File mediaFile = null;
        File mediaStorageDir = null;
        String dirPath = "";

        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            dirPath = Environment.getExternalStorageDirectory().getPath();

        }else {
            // if SD card is not accessible, then use internal storage.
            dirPath = mContext.getFilesDir().getPath();
            //mContext.getExternalFilesDir()
        }


        // Create a media file name
        if (type.equalsIgnoreCase(".pdf")) {
            mediaStorageDir = new File(dirPath + "/SeramporeCollege");

            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    throw new FileNotFoundException("failed to create directory");
                }
            }
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + fileNameWithExtnsn);
        } else if (type.equalsIgnoreCase(".jpg")) {
            mediaStorageDir = new File(dirPath + "/SeramporeCollege");

            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    throw new FileNotFoundException("failed to create directory");
                }
            }
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + fileNameWithExtnsn);
        }else {
            throw new UnsupportedOperationException("Unsupported format");
        }
        mediaStorageDir.setReadable( true, false );
        return mediaFile;
    }
}
