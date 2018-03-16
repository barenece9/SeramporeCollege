package com.lnsel.seramporecollege.activity.student;

import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.lnsel.seramporecollege.R;
import com.lnsel.seramporecollege.VolleyLibrary.AppController;
import com.lnsel.seramporecollege.utils.ActivityUtil;
import com.lnsel.seramporecollege.utils.CommonMethod;
import com.lnsel.seramporecollege.utils.ConstantUtil;

import com.lnsel.seramporecollege.utils.SharedManagerUtil;
import com.lnsel.seramporecollege.utils.UrlUtil;


import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;


public class ReceiptWebViewActivity extends AppCompatActivity {


    private static final String TAG = ReceiptWebViewActivity.class.getSimpleName();
    SharedManagerUtil session;
    WebView webview;
    Button btn_save;
    ProgressBar progressbar;
    String payslipName="Receipt";
    public static final int progress_bar_type = 0;
    public static final int PERMISSION_CODE = 42042;
    public static final String WRITE_EXTERNAL_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE";

    String responseData="";



    Uri uri;
    // Progress Dialog
    public ProgressDialog pDialog;
    // Progress dialog type (0 - for Horizontal progress bar)
    String targetPath="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_web_view);
        session=new SharedManagerUtil(this);
        payslipName="Receipt-"+ ConstantUtil.student_payment_date;
        Log.e("ReceiptName==> ",payslipName);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().set
        getSupportActionBar().setTitle(payslipName);

        String file_url= UrlUtil.STUDENT_PAYMENT_RECEIPT_DOWNLOAD_URL+ConstantUtil.payment_collection_id;


        //String file_url="http://61.16.131.206/erp_srcc/api/test/download_payment_details?collection_id="+ConstantUtil.payment_collection_id;
       // String file_url= "http://61.16.131.206/erp_srcc/api/test/download_payment_details?collection_id=1649";
        Log.e("url==> ",file_url);

        webview = (WebView)findViewById(R.id.webview);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        btn_save=(Button)findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save(webview);
            }
        });
       // w = new WebView(this);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadUrl(file_url);
        webview.setInitialScale(150);
        webview.getSettings().setBuiltInZoomControls(true);
        webview.setWebViewClient(new WebViewClient()
        {
            public void onPageFinished(WebView view, String url)
            {
                /*Picture picture = view.capturePicture();
                Bitmap  b = Bitmap.createBitmap( picture.getWidth(),
                        picture.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas c = new Canvas( b );

                picture.draw( c );
                FileOutputStream fos = null;
                try {

                    String targetPath= CommonMethod.getOutputMediaFileInPublicDirectory(".jpg",ReceiptWebViewActivity.this,payslipName+".jpg").getPath();
                    // fos = new FileOutputStream( "mnt/sdcard/yahoo.jpg" );
                    fos = new FileOutputStream( targetPath );
                    if ( fos != null )
                    {
                        b.compress(Bitmap.CompressFormat.JPEG, 100, fos);

                        fos.close();
                        Toast.makeText(getApplicationContext(),"pdf save",Toast.LENGTH_SHORT).show();
                    }

                }
                catch( Exception e )
                {
                    System.out.println("Exception====");

                }*/
                progressbar.setVisibility(View.GONE);
                btn_save.setVisibility(View.GONE);

            }
        });


    }

    public void save(WebView view){
        progressbar.setVisibility(View.VISIBLE);
         Picture picture = view.capturePicture();
                Bitmap  b = Bitmap.createBitmap( picture.getWidth(),
                        picture.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas c = new Canvas( b );

                picture.draw( c );
                FileOutputStream fos = null;
                try {

                    String targetPath= CommonMethod.getOutputMediaFileInPublicDirectory(".jpg",ReceiptWebViewActivity.this,payslipName+".jpg").getPath();
                    // fos = new FileOutputStream( "mnt/sdcard/yahoo.jpg" );
                    fos = new FileOutputStream( targetPath );
                    if ( fos != null )
                    {
                        b.compress(Bitmap.CompressFormat.JPEG, 100, fos);

                        fos.close();
                        Toast.makeText(getApplicationContext(),"save as image",Toast.LENGTH_SHORT).show();
                    }

                }
                catch( Exception e )
                {
                    System.out.println("Exception====");

                }
        progressbar.setVisibility(View.GONE);
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

                new ActivityUtil(ReceiptWebViewActivity.this).startStudentActivity();
                return true;

            case R.id.action_download:
                //new convertOperation().execute("");
               // convertHtmlToPdf(responseData);
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

        startDownload();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startDownload();
            }else {
                Toast.makeText(getApplicationContext(),"Receipt can not download without storage permission",Toast.LENGTH_SHORT).show();
               // new ActivityUtil(PayslipWebViewActivity.this).startTeacherActivity();
            }
        }
    }



    public void startDownload2(){
        String file_url= "http://61.16.131.206/erp_srcc/api/student/download_payment_details";
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setMessage("loading...");
        //progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        progress.show();


        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                file_url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("TAG", response.toString());
                        //convertHtmlToPdf(response);
                      // new convertOperation().execute(response);
                        //responseData=response;
                        //convertHtmlToPdf(response);
                        progress.dismiss();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "Error: " + error.getMessage());
                progress.dismiss();
                Toast.makeText(getApplicationContext(),"Server not Responding, " +
                        "Please check your Internet Connection", Toast.LENGTH_LONG).show();
                //view.errorInfo("Server not Responding, Please check your Internet Connection");
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("collection_id", "286");//286//1649


                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }


    public void convertHtmlToPdf(String response){
        try {
            // response = "<html><body> This is my Project </body></html>";
            // response = "<html><body>"+response+"</body></html>";
            //String k=response;
            File file2 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Serampore");
            if (!file2.exists()) {
                file2.mkdirs();
            }
            OutputStream file = new FileOutputStream(file2+"/demo.pdf");
            //OutputStream file = new FileOutputStream(new File("C:\\Test.pdf"));
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, file);
            document.open();

            try {
                //document.open();
                Drawable d = getResources().getDrawable(R.drawable.head_logo);
                BitmapDrawable bitDw = ((BitmapDrawable) d);
                Bitmap bmp = bitDw.getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                Image image = Image.getInstance(stream.toByteArray());
                document.add(image);
               // document.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            InputStream is = new ByteArrayInputStream(response.getBytes());
            XMLWorkerHelper.getInstance().parseXHtml(writer, document, is);
            Toast.makeText(getApplicationContext(),"Pdf dowmload and save",Toast.LENGTH_SHORT).show();
            document.close();
            file.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onBackPressed() {
        new ActivityUtil(ReceiptWebViewActivity.this).startStudentActivity();
        super.onBackPressed();
    }





    private class convertOperation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                // response = "<html><body> This is my Project </body></html>";
               // params[0] = "<html><body>"+params[0]+"</body></html>";
                //String k=response;
                File file2 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Serampore");
                if (!file2.exists()) {
                    file2.mkdirs();
                }
                OutputStream file = new FileOutputStream(file2+"/demo.pdf");
                //OutputStream file = new FileOutputStream(new File("C:\\Test.pdf"));
                Document document = new Document();
                PdfWriter writer = PdfWriter.getInstance(document, file);
                document.open();
                InputStream is = new ByteArrayInputStream(responseData.getBytes());
                XMLWorkerHelper.getInstance().parseXHtml(writer, document, is);
                Toast.makeText(getApplicationContext(),"Pdf dowmload and save",Toast.LENGTH_SHORT).show();
                document.close();
                file.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            // txt.setText(result);
            // might want to change "executed" for the returned string passed
            // into onPostExecute() but that is upto you
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }


    public void startDownload(){
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Downloading file. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setMax(100);
        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pDialog.setCancelable(true);
        pDialog.show();
        String url= "https://61.16.131.206/erp_srcc/api/student/download_payment_details?collection_id=286";
        //String url= UrlUtil.BASE_DOWNLOAD_URL+ConstantUtil.payslipUrl;
        Log.e("url",url);
        Log.e("payslipName",payslipName);

        // String url = http://...;

        String fileExtenstion = MimeTypeMap.getFileExtensionFromUrl(url);
        //FileName = URLUtil.guessFileName(url, null, fileExtenstion);

        //System.out.println(FileName+"  ========  "+fileExtenstion);
        //FileName = "jan_2017";


        new ReceiptWebViewActivity.DownloadFileFromURL().execute(url);
        // String url= UrlUtil.MAIN_URL+ConstantUtil.payslipUrl;
        /*uri =  Uri.parse("http://61.16.131.206/erp_srcc/employee/downloads/payslip/payslip_1516258041.pdf");
        System.out.println("uri=========== : "+uri.toString());
        Log.d("uri==",uri.toString());
        afterViews();*/
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
                targetPath=CommonMethod.getOutputMediaFileInPublicDirectory(".pdf",ReceiptWebViewActivity.this,payslipName+".pdf").getPath();
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
           // afterViews();
        }

    }

}
