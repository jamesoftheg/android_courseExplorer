package ca.mohawk.gelfand.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

/**
 * ANDROID FINAL - APRIL 6TH
 * I, James Gelfand,000275852 certify that this material is my original work.
 * No other person's work has been used without due acknowledgement.
 *
 * YouTube link: https://youtu.be/7DW6pZvxkbo
 */


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        NavigationView.OnNavigationItemSelectedListener {

    public static final String TAG = "==MAIN ACTIVITY==";

    private static Activity currentActivity = null;

    private String searchFilter;

    private String currentFilter = "startUp";

    private String filterVal;

    private int filterNum;

    private DrawerLayout myDrawer;

    NavigationView myNavView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Filter Spinner
        Spinner activity_spinner = findViewById(R.id.search_spinner);
        activity_spinner.setSelection(0, false);
        activity_spinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.search_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activity_spinner.setAdapter(adapter);

        // Nav Drawer
        myDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBar myActionBar = getSupportActionBar();
        myActionBar.setDisplayHomeAsUpEnabled(true);
        ActionBarDrawerToggle myactionbartoggle = new ActionBarDrawerToggle
                (this, myDrawer, (R.string.open), (R.string.close));
        myDrawer.addDrawerListener(myactionbartoggle);
        myactionbartoggle.syncState();
        myNavView = (NavigationView)
                findViewById(R.id.nav_view);
        myNavView.setNavigationItemSelectedListener(this);

        currentActivity = this;

        Log.d(TAG, "onCreate");
    }

    @Override
    public void onResume(){
        super.onResume();
        startDownload(null);
    }


    /**
     * Provides access to the current activity
     * @return foreground Activity
     */
    public static Activity getCurrentActivity() {
        return currentActivity;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        EditText courseSearch = (EditText) findViewById(R.id.codeSearch);
        // The various cases representing different activities.
        // Entry type will change depending on search value.
        switch(position) {
                // Code
            case 0:
                searchFilter = "?filter=[{\"type\":\"string\",\"column\":\"courseCode\",\"value\":\"";
                Log.d(TAG, "Filter: " + searchFilter);
                currentFilter = "string";
                courseSearch.getText().clear();
                courseSearch.setInputType(InputType.TYPE_CLASS_TEXT);
                break;
                // Name
            case 1:
                searchFilter = "?filter=[{\"type\":\"string\",\"column\":\"courseTitle\",\"value\":\"";
                Log.d(TAG, "Filter: " + searchFilter);
                currentFilter = "string";
                courseSearch.getText().clear();
                courseSearch.setInputType(InputType.TYPE_CLASS_TEXT);
                break;
                // Program
            case 2:
                searchFilter = "?filter=[{\"type\":\"number\",\"column\":\"program\",\"value\":\"";
                Log.d(TAG, "Filter: " + searchFilter);
                currentFilter = "int";
                courseSearch.getText().clear();
                courseSearch.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;
                // Semester
            case 3:
                searchFilter = "?filter=[{\"type\":\"number\",\"column\":\"semesterNum\",\"value\":\"";
                Log.d(TAG, "Filter: " + searchFilter);
                currentFilter = "int";
                courseSearch.getText().clear();
                courseSearch.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;
                // Elective
            case 4:
                searchFilter = "?filter=[{\"type\":\"number\",\"column\":\"optional\",\"value\":\"";
                Log.d(TAG, "Filter: " + searchFilter);
                currentFilter = "int";
                courseSearch.getText().clear();
                courseSearch.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;
        }
    }

    // Nothing selected
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        Log.d(TAG,"Nothing selected.");
    }


    /**
     * onClick handler composes URI and initiates download in background
     * @param view - from button
     */
    public void startDownload(View view) {
        DownloadAsyncTask dl = new DownloadAsyncTask();

        filterVal = "";
        filterNum = -1;

        // URL of API
        String uri = "https://csunix.mohawkcollege.ca/~geczy/mohawkprograms.php";

        // Get user search entry
        EditText courseSearch = (EditText) findViewById(R.id.codeSearch);

        if (!currentFilter.equals("startUp")) {
            if (currentFilter.equals("string")) {
                filterVal = courseSearch.getText().toString();
                // Return all data if value is empty
                if (!filterVal.equals("")) {
                    uri += searchFilter + filterVal + "\"}]";
                    Log.d(TAG, "Filter result:  " + uri);
                }
                Log.d(TAG, "startDownload " + uri);
                dl.execute(uri);
                Toast.makeText(this, "Search Loaded",Toast.LENGTH_SHORT).show();
            }
            else if (currentFilter.equals("int")) {
                try {
                    filterNum = Integer.parseInt(courseSearch.getText().toString());
                    // Return all data if value is empty
                    if (filterNum != -1) {
                        uri += searchFilter + filterNum + "\"}]";
                        Log.d(TAG, "Filter result:  " + uri);
                    }
                    Log.d(TAG, "startDownload " + uri);
                    dl.execute(uri);
                    Toast.makeText(this, "Search Loaded",Toast.LENGTH_SHORT).show();
                } catch(NumberFormatException e) {
                    System.out.println("Could not parse " + e);
                }
            }
        }
        if (currentFilter.equals("startUp")) {
            Log.d(TAG, "startDownload " + uri);
            dl.execute(uri);
            Toast.makeText(this, "Courses Loaded",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean isOpen = myDrawer.isDrawerOpen(GravityCompat.START);
        switch (item.getItemId()) {
            case android.R.id.home:
                if (isOpen == true) {
                    myDrawer.closeDrawer(GravityCompat.START);
                } else {
                    myDrawer.openDrawer(GravityCompat.START);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Respond to Navigation Drawer item selected
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        myNavView.setCheckedItem(item);
        myDrawer.closeDrawers();

        switch (item.getItemId()) {
            case R.id.mainPage:
                break;
            case R.id.savedPage:
                Intent saved = new Intent(this, Saved.class);
                startActivity(saved);
                break;
            case R.id.aboutPage:
                Intent about = new Intent(this, About.class);
                startActivity(about);
                break;
        }
        Log.d(TAG,"== Item Selected: " + item + " ==");
        return false;
    }
}