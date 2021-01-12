package com.e.top10iphonesapps;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private ListView listapp;
    private String feedURL = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=%d/xml";
    private int feedLimit = 10;
    private String feedCarchedURL = "INVALIDATED";
    public static final String state_URl = "feedURL";
    public static final String state_LIMIT = "feedLIMIT";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listapp = (ListView)findViewById(R.id.xmllistview);
        if(savedInstanceState != null){
            feedURL = savedInstanceState.getString(state_URl);
            feedLimit = savedInstanceState.getInt(state_LIMIT);
        }

        dowlowdURL(String.format(feedURL, feedLimit));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.feeds_menu, menu);
        if(feedLimit == 10){
            menu.findItem(R.id.mnu10).setChecked(true);
        }else{
            menu.findItem(R.id.mnu25).setChecked(true);
        }

        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(state_URl, feedURL);
        outState.putInt(state_LIMIT, feedLimit);

        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id= item.getItemId();

        switch (id){
            case R.id.mnuFeed:
                feedURL = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=%d/xml";
                break;

            case R.id.mnuPaid:
                feedURL = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/toppaidapplications/limit=%d/xml";
                break;

            case R.id.mnuSongs:
                feedURL = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topsongs/limit=%d/xml";
                break;
            case R.id.mnu10:
            case R.id.mnu25:
                if(!item.isChecked()){
                    item.setChecked(true);
                    feedLimit = 35 - feedLimit;
                    Log.d(TAG, "onOptionsItemSelected: " + item.getTitle() + " set feeding limit to " + feedLimit);
                }else {
                    Log.d(TAG, "onOptionsItemSelected: " + item.getTitle() + " Feed limit unchange");
                }
                break;
            case R.id.mnurefrech:
                feedCarchedURL = "INVALIDATE";
                break;

            default:
                return super.onOptionsItemSelected(item);
        }



        dowlowdURL(String.format(feedURL, feedLimit));
        return true;
    }

    private void dowlowdURL(String feedURL){
        if(!feedURL.equalsIgnoreCase(feedCarchedURL)){
            Log.d(TAG, "dowlowdURL: starting AsynTask");
            Dowloaddata dowloaddata = new Dowloaddata();
            dowloaddata.execute(feedURL);
            feedCarchedURL = feedURL;
            Log.d(TAG, "dowlowdURL: done");  
        }else {
            Log.d(TAG, "dowlowdURL: URL not change");
        }


    }


    private class Dowloaddata extends AsyncTask<String, Void, String>{
        private static final String TAG = "Dowloaddata";

        @Override
        protected void onPostExecute(String s) {
            Log.d(TAG, "onPostExecute: parameter is " + s);
            PassAplications passAplications = new PassAplications();
            passAplications.parse(s);
            // this is an only text arrayAdapter
           // ArrayAdapter<FeedEntry> arrayAdapter = new ArrayAdapter<>(MainActivity.this, R.layout.list_item, passAplications.getApplications());
           // listapp.setAdapter(arrayAdapter);
            FeedAdapter feedAdapter = new FeedAdapter(MainActivity.this, R.layout.list_record, passAplications.getApplications());
            listapp.setAdapter(feedAdapter);
        }

        @Override
        protected String doInBackground(String... strings) {
            Log.d(TAG, "doInBackground: starts with " + strings[0]);
            String rssFeeds = dowloadXML(strings[0]);
            if(rssFeeds == null){
                Log.e(TAG, "doInBackground: Error downloading");
            }

            return rssFeeds;

        }

        public String dowloadXML(String urlpath){

            StringBuilder xmlResoould = new StringBuilder();

            try{
                URL url = new URL(urlpath);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                int response = connection.getResponseCode();
                Log.d(TAG, "dowloadXML: the response code was " + response);
                // this three lines can be subsitutde by the next comment line
                InputStream inputStream = connection.getInputStream(); // still confuse about this InputStream
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(inputStreamReader);
                // BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                int charRead;
                char[] inputBuff = new char[500];
                while (true){
                    charRead = reader.read(inputBuff);
                    if(charRead<0){
                        break;
                    }
                    if(charRead>0){
                        xmlResoould.append(String.copyValueOf(inputBuff, 0, charRead));
                    }
                }
                reader.close();

                return xmlResoould.toString();
            }catch (MalformedURLException e){
                Log.e(TAG, "dowloadXML: Invalid URl " + e.getMessage());
            }catch (IOException e){
                Log.e(TAG, "dowloadXML: IO exception readinf data: " + e.getMessage());
            }catch (SecurityException e){
                Log.e(TAG, "dowloadXML: Security Exception. Needs Permition " + e.getMessage());
                e.printStackTrace();
            }

            return null;

        }

    }

}

