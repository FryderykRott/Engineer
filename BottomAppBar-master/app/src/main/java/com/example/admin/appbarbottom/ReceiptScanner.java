package com.example.admin.appbarbottom;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.cloudmersive.client.ImageOcrApi;
import com.cloudmersive.client.invoker.ApiClient;
import com.cloudmersive.client.invoker.ApiException;
import com.cloudmersive.client.invoker.Configuration;
import com.cloudmersive.client.invoker.auth.ApiKeyAuth;
import com.cloudmersive.client.model.ReceiptRecognitionResult;
import com.example.admin.appbarbottom.ReceiptCarerUtils.Utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class ReceiptScanner extends AsyncTask<Long, Integer, Long> {

        File file_to_scan;
        Activity a;
        ArrayList<Bitmap> images;
        ProgressDialog dialog;

        public ReceiptScanner(Activity a, ArrayList<Bitmap> images){
            try {
                this.file_to_scan = File.createTempFile("file", ".jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.a = a;
            this.images = images;

            dialog = new ProgressDialog(a);
            dialog.setMessage("Processing...");

        }

        @Override
        protected Long doInBackground(Long... input)
        {
//            dialog.show();
            String path = "";


//            try {
//
//                downloadFile("http://www.fiskalni.edu.pl/wp-content/uploads/2018/10/co-dadza-e-paragony-fiskalne-1200x1112.jpg", file_to_scan);
//
//                path = file_to_scan.getPath();
//
//            }
//            catch (Exception ex)
//            {
//                System.out.println(ex.toString());
//            }

            ApiClient defaultClient = Configuration.getDefaultApiClient();

//            defaultClient.setConnectTimeout(3000000);

            ApiKeyAuth api_key = (ApiKeyAuth) defaultClient.getAuthentication("Apikey");

            api_key.setApiKey("8bc8faf8-49eb-4148-aa18-31df0b66f310");

            ImageOcrApi apiInstance = new ImageOcrApi();
            apiInstance.getApiClient().setConnectTimeout(3000000);

            String recognitionMode = "Advanced";
            String language = "POL";
            String preprocessing = "Advanced";


            path = Utils.getRealPathFromURI(a, images.get(0));
            File file = new File(path);

            try {
                ReceiptRecognitionResult result = apiInstance.imageOcrPhotoRecognizeReceipt(file, recognitionMode, language, preprocessing);
                Log.i("SCAN RESULT:", result.toString());
            } catch (ApiException e) {
                Log.i("ds","Exception when calling ImageOcrApi");
                e.printStackTrace();
            }

//            dialog.dismiss();
            return 0L;
        }

        private static void downloadFile(String url, File outputFile) {
            try {
                URL u = new URL(url);
                URLConnection conn = u.openConnection();
                int contentLength = conn.getContentLength();

                DataInputStream stream = new DataInputStream(u.openStream());

                byte[] buffer = new byte[contentLength];
                stream.readFully(buffer);
                stream.close();

                DataOutputStream fos = new DataOutputStream(new FileOutputStream(outputFile));
                fos.write(buffer);
                fos.flush();
                fos.close();
            } catch(IOException e) {
                // swallow a 404
            }
        }


        @Override
        protected void onProgressUpdate(Integer... progress)
        {

        }

        @Override
        protected void onPostExecute(Long result) {

        }

}
