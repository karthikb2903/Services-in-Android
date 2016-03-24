package karthik.sjsu.com.servicesapp;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class PDFActivity extends AppCompatActivity {

    IntentFilter intentFilter;
    private MyService serviceBinder;
    Intent i;
    private EditText ed1,ed2,ed3,ed4,ed5;
    String url1,url2,url3,url4,url5;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);

        ed1 = (EditText)findViewById(R.id.pdf1Txt);
        ed2 = (EditText)findViewById(R.id.pdf2Txt);
        ed3 = (EditText)findViewById(R.id.pdf3Txt);
        ed4 = (EditText)findViewById(R.id.pdf4Txt);
        ed5 = (EditText)findViewById(R.id.pdf5Txt);

        url1 = ed1.getText().toString();
        url2 = ed2.getText().toString();
        url3 = ed3.getText().toString();
        url4 = ed4.getText().toString();
        url5 = ed5.getText().toString();

   //---intent to filter for file downloaded intent---
    intentFilter = new IntentFilter();
    intentFilter.addAction("FILE_DOWNLOADED_ACTION");


    //---register the receiver---
    registerReceiver(intentReceiver, intentFilter);
}



    public void startDownload(View v){

        Intent intent = new Intent(getBaseContext(), MyService.class);
        try {
            URL[] urls = new URL[] {
                    new URL(url1),
                    new URL(url2),
                    new URL(url3),
                    new URL(url4),
                    new URL(url5)
            };
            intent.putExtra("URLs", urls);



        } catch (MalformedURLException e) {

            e.printStackTrace();
        }finally {

            new DownloadFileFromURL().execute(url1);
            new DownloadFileFromURL().execute(url2);
            new DownloadFileFromURL().execute(url3);
            new DownloadFileFromURL().execute(url4);
            new DownloadFileFromURL().execute(url5);

            startService(intent);








        }


    }


private ServiceConnection connection = new ServiceConnection() {
    public void onServiceConnected(ComponentName className, IBinder service) {
        //---called when the connection is made---
        serviceBinder = ((MyService.MyBinder)service).getService();

        try {
            URL[] urls = new URL[] {
                    new URL(url1),
                    new URL(url2),
                    new URL(url3),
                    new URL(url4),
                    new URL(url5)
            };
            serviceBinder.urls = urls;

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        startService(i);
    }
    public void onServiceDisconnected(ComponentName className) {
        //---called when the service disconnects---
        serviceBinder = null;
    }
};


private BroadcastReceiver intentReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(getBaseContext(), "File downloaded!",
                Toast.LENGTH_LONG).show();
    }
};

    class DownloadFileFromURL  extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... f_url) {
            int count;
            OutputStream output=null;
            InputStream input = null;
            String fileNames[]={"cisco1.pdf","cisco2.pdf","cisco3.pdf","cisco4.pdf","cisco5.pdf"};
            try {

                    URL url = new URL(f_url[0]);
                    URLConnection connection = url.openConnection();
                    connection.connect();
                    int total=0;

                   input = new BufferedInputStream(url.openStream());
                for(int i=0;i<5;i++) {
                    output = new FileOutputStream("/sdcard/Download/" + fileNames[i]);
                }
                    byte data[] = new byte[1024];
                    while ((count = input.read(data)) != -1) {
                        total += count;

                        output.write(data, 0, count);
                    }

            } catch (Exception e) { }
            return null;

        }


        @Override
        protected void onProgressUpdate(String... progress) {

            //   mProgressDialog.setProgress(Integer.parseInt(progress[0]));

            // Update progress
            super.onProgressUpdate(progress);



        }


        protected void onPostExecute(String result) {
            super.onPostExecute(result);

        }
    }



}