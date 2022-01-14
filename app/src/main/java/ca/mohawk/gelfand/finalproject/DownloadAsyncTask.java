package ca.mohawk.gelfand.finalproject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadAsyncTask extends AsyncTask<String, Void, String> {
    public static final String TAG = "==MainActivity==";

    @Override
    protected String doInBackground(String... params) {
        Log.d(TAG, "Starting Background Task");
        StringBuilder results = new StringBuilder();

        try {
            URL url = new URL(params[0]);
            String line = null;
            // Open the Connection
            HttpURLConnection conn = (HttpURLConnection)
                    url.openConnection();
            // Read in response
            int statusCode = conn.getResponseCode();
            if (statusCode == 200) {
                InputStream inputStream = new BufferedInputStream(
                        conn.getInputStream());
                BufferedReader bufferedReader =
                        new BufferedReader(new InputStreamReader(inputStream,
                                "UTF-8"));
                while ((line = bufferedReader.readLine()) != null) {
                    results.append(line);
                }
            }
            Log.d(TAG, "Data received = " + results.length());
            Log.d(TAG, "Response Code: " + statusCode);
        } catch (IOException ex) {
            Log.d(TAG, "Caught Exception: " + ex);
        }
        return results.toString();
    }

    protected void onPostExecute(String result) {
        CourseList courselist = null;

        if (result == null) {
            Log.d(TAG, "No courses found.");
        }
        else {
            Gson gson = new Gson();
            courselist = gson.fromJson(result, CourseList.class);
            Log.d(TAG, "Course list populated.");
        }

        Activity currentActivity = MainActivity.getCurrentActivity();
        ListView lv = currentActivity.findViewById(R.id.listView);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String text = lv.getItemAtPosition(position).toString().trim();
                Intent detailView = new Intent(view.getContext(), CourseDetail.class);
                detailView.putExtra("courseDetails", text);
                currentActivity.startActivity(detailView);
                Log.d(TAG, "Course selected.");
            }});

        if (courselist != null) {
            ArrayAdapter<Courses> adapter =
                    new ArrayAdapter<Courses>(currentActivity,
                            android.R.layout.simple_list_item_1, courselist);
            lv.setAdapter(adapter);
        } else {
            lv.setAdapter(null);
        }
    }
}