package com.lnsel.seramporecollege.activity.teacher;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.lnsel.seramporecollege.R;
import com.lnsel.seramporecollege.activity.student.ReceiptPdfViewActivity;
import com.lnsel.seramporecollege.utils.ActivityUtil;
import com.lnsel.seramporecollege.utils.CommonMethod;
import com.lnsel.seramporecollege.utils.ConstantUtil;
import com.lnsel.seramporecollege.utils.SharedManagerUtil;
import com.lnsel.seramporecollege.utils.UrlUtil;
import com.shockwave.pdfium.PdfDocument;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class PayslipPdfViewActivity extends AppCompatActivity implements OnPageChangeListener, OnLoadCompleteListener,
        OnPageErrorListener {

    private static final String TAG = PayslipPdfViewActivity.class.getSimpleName();

    private final static int REQUEST_CODE = 42;
    public static final int PERMISSION_CODE = 42042;

    public static final String SAMPLE_FILE = "sample1.pdf";//from asset
   // public static final String READ_EXTERNAL_STORAGE = "android.permission.READ_EXTERNAL_STORAGE";
    public static final String WRITE_EXTERNAL_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE";


    PDFView pdfView;
    Uri uri;
    Integer pageNumber = 0;
    String pdfFileName;
    String FileName="payslip";
    // Progress Dialog
    public ProgressDialog pDialog;
    // Progress dialog type (0 - for Horizontal progress bar)
    public static final int progress_bar_type = 0;
    SharedManagerUtil session;
    String targetPath="";
    String payslipName="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payslip_pdf_view);

        payslipName="Payslip-"+ConstantUtil.payslipMonth+"-"+ConstantUtil.payslipYear;
        Log.e("payslipName==> ",payslipName);
        String url= UrlUtil.BASE_DOWNLOAD_URL+ConstantUtil.payslipUrl;
        Log.e("url==> ",url);

        session=new SharedManagerUtil(PayslipPdfViewActivity.this);
        pdfView=(PDFView) findViewById(R.id.pdfView);
        Button pick=(Button)findViewById(R.id.pick);
        pick.setVisibility(View.GONE);
        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickFile();
            }
        });

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(payslipName);

        permissionTest();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here

                new ActivityUtil(PayslipPdfViewActivity.this).startTeacherActivity();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void startDownload(){
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Downloading file. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setMax(100);
        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pDialog.setCancelable(true);
        pDialog.show();

        String url= UrlUtil.BASE_DOWNLOAD_URL+ConstantUtil.payslipUrl;
        Log.e("url",url);
        Log.e("payslipName",payslipName);

        // String url = http://...;

        String fileExtenstion = MimeTypeMap.getFileExtensionFromUrl(url);
        FileName = URLUtil.guessFileName(url, null, fileExtenstion);

        System.out.println(FileName+"  ========  "+fileExtenstion);
        //FileName = "jan_2017";


        new DownloadFileFromURL().execute(url);
        // String url= UrlUtil.MAIN_URL+ConstantUtil.payslipUrl;
        /*uri =  Uri.parse("http://61.16.131.206/erp_srcc/employee/downloads/payslip/payslip_1516258041.pdf");
        System.out.println("uri=========== : "+uri.toString());
        Log.d("uri==",uri.toString());
        afterViews();*/
    }

    void pickFile() {
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                WRITE_EXTERNAL_STORAGE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE}, PERMISSION_CODE);

            return;
        }

        launchPicker();
    }

    void permissionTest() {
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                WRITE_EXTERNAL_STORAGE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE}, PERMISSION_CODE);

            return;
        }

        startDownload();
    }

    void launchPicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        try {
            startActivityForResult(intent, REQUEST_CODE);
        } catch (ActivityNotFoundException e) {
            //alert user that file manager not working
            Toast.makeText(this,"Failed to view", Toast.LENGTH_SHORT).show();
        }
    }


    void afterViews() {
        pdfView.setBackgroundColor(Color.LTGRAY);
        if (uri != null) {
            displayFromUri(uri);
        } else {
            displayFromAsset(SAMPLE_FILE);
        }
        setTitle(pdfFileName);
    }

    private void displayFromAsset(String assetFileName) {
        System.out.println("displayFromAsset ");
        pdfFileName = assetFileName;

        pdfView.fromAsset(SAMPLE_FILE)
                .defaultPage(pageNumber)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(this))
                .spacing(10) // in dp
                .onPageError(this)
                //  .pageFitPolicy(FitPolicy.BOTH)
                .load();


    }

    private void displayFromUri(Uri uri) {

        System.out.println("displayFromUri===========");
        pdfFileName = getFileName(uri);

        pdfView.fromUri(uri)
                .defaultPage(pageNumber)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(this))
                .spacing(10) // in dp
                .onPageError(this)
                .load();
    }


    public void onResult(int resultCode, Intent intent) {
        if (resultCode == RESULT_OK) {
            uri = intent.getData();
            displayFromUri(uri);
        }
    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
        setTitle(String.format("%s %s / %s", pdfFileName, page + 1, pageCount));
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;
    }

    @Override
    public void loadComplete(int nbPages) {
        PdfDocument.Meta meta = pdfView.getDocumentMeta();
        Log.e(TAG, "title = " + meta.getTitle());
        Log.e(TAG, "author = " + meta.getAuthor());
        Log.e(TAG, "subject = " + meta.getSubject());
        Log.e(TAG, "keywords = " + meta.getKeywords());
        Log.e(TAG, "creator = " + meta.getCreator());
        Log.e(TAG, "producer = " + meta.getProducer());
        Log.e(TAG, "creationDate = " + meta.getCreationDate());
        Log.e(TAG, "modDate = " + meta.getModDate());

        printBookmarksTree(pdfView.getTableOfContents(), "-");

    }

    public void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {
        for (PdfDocument.Bookmark b : tree) {

            Log.e(TAG, String.format("%s %s, p %d", sep, b.getTitle(), b.getPageIdx()));

            if (b.hasChildren()) {
                printBookmarksTree(b.getChildren(), sep + "-");
            }
        }
    }

    /**
     * Listener for response to user permission request
     *
     * @param requestCode  Check that permission request code matches
     * @param permissions  Permissions that requested
     * @param grantResults Whether permissions granted
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startDownload();
            }else {
                //Toast.makeText(getApplicationContext(),"Payslip can not download without storage permission",Toast.LENGTH_SHORT).show();
                new ActivityUtil(PayslipPdfViewActivity.this).startTeacherActivity();
            }
        }
    }

    @Override
    public void onPageError(int page, Throwable t) {
        Log.e(TAG, "Cannot load page " + page);
    }


    /**
     * Background Async Task to download file
     * */
    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(progress_bar_type);
        }

        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();
                // this will be useful so that you can show a tipical 0-100% progress bar
                int lenghtOfFile = conection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

               // File f = new File(CommonMethod.getOutputMediaFileInPublicDirectory(".pdf",PayslipPdfViewActivity.this,"april.pdf").getPath());




                // Output stream
                //OutputStream output = new FileOutputStream("/sdcard/downloadedfile.pdf");
                //OutputStream output = new FileOutputStream("/sdcard/"+FileName+".pdf");
                targetPath=CommonMethod.getOutputMediaFileInPublicDirectory(".pdf",PayslipPdfViewActivity.this,payslipName+".pdf").getPath();
                OutputStream output = new FileOutputStream(targetPath);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress(""+(int)((total*100)/lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        /**
         * Updating progress bar
         * */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        /**
         * After completing background task
         * Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
            // dismissDialog(progress_bar_type);
            pDialog.dismiss();
            Toast.makeText(PayslipPdfViewActivity.this,"Download completed", Toast.LENGTH_SHORT).show();
            // Displaying downloaded image into image view
            // Reading image path from sdcard
            // String imagePath = Environment.getExternalStorageDirectory().toString() + "/"+FileName+".pdf";
          //  String fname="Payslip-"+ConstantUtil.payslipMonth+"-"+ConstantUtil.payslipYear;
          //  String payslipPath=CommonMethod.getOutputMediaFileInPublicDirectory(".pdf",PayslipPdfViewActivity.this,fname+".pdf").getPath();
            // setting downloaded into image view
            // my_image.setImageDrawable(Drawable.createFromPath(imagePath));

            //String url= UrlUtil.MAIN_URL+ConstantUtil.payslipUrl;
            uri =  Uri.fromFile(new File(targetPath));
            //uri =  Uri.parse(imagePath);
            System.out.println("imagePath=========== : "+targetPath);
            Log.d("imagePath==",uri.toString());
            afterViews();
        }

    }

    @Override
    public void onBackPressed() {
        new ActivityUtil(PayslipPdfViewActivity.this).startTeacherActivity();
        super.onBackPressed();
    }
}
