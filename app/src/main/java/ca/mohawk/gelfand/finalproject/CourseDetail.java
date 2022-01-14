package ca.mohawk.gelfand.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

public class CourseDetail extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final String TAG = "==DETAIL ACTIVITY==";

    private DrawerLayout myDrawer;
    private NavigationView myNavView;

    // Create a global instance of our SQL Helper class
    private MyDbHelper mydbhelp = new MyDbHelper(this);

    private String code;
    private String title;
    private String program;
    private String hours;
    private String courseDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

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

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String details = extras.getString("courseDetails");

            String lines[] = details.split("\\r?\\n");

            code = lines[0];
            title = lines[1];
            program = lines[2];
            hours = lines[3];
            courseDescription = lines[4];

            TextView setCode = (TextView)findViewById(R.id.courseCode);
            TextView setTitle = (TextView)findViewById(R.id.courseName);
            TextView setProgram = (TextView)findViewById(R.id.programOfStudies);
            TextView setHours = (TextView)findViewById(R.id.courseHours);
            TextView setDescription = (TextView)findViewById(R.id.courseDescription);

            setCode.setText(code);
            setTitle.setText(title);
            setProgram.setText(program);
            setHours.setText(hours);
            setDescription.setText(courseDescription);
            Log.d(TAG, "Course details set.");
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

        // Start activity onClick
        switch (item.getItemId()) {
            case R.id.mainPage:
                Intent main = new Intent(this, MainActivity.class);
                startActivity(main);
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

    public void addData(View view) {
        // Get an instance of the database using our helper class
        SQLiteDatabase db = mydbhelp.getWritableDatabase();

        // A ContentValues class provides an easy helper function to add our values
        ContentValues values = new ContentValues();

        // Similar to using a bundle - put values using column name and value
        values.put(MyDbHelper.COURSECODE, code);
        values.put(MyDbHelper.COURSENAME, title);
        values.put(MyDbHelper.COURSEPROGRAM, program);
        values.put(MyDbHelper.COURSEHOURS, hours);
        values.put(MyDbHelper.COURSEDESCRIPTION, courseDescription);

        // The db.insert command will do a SQL insert on our table, return the new row ID
        long newrowID = db.insert(MyDbHelper.MYTABLE, null, values);
        Intent savedCourses = new Intent(this, Saved.class);
        startActivity(savedCourses);
        Log.d(TAG, "New ID " + newrowID);
    }

    public void callList(View view) {
        Intent savedCourses = new Intent(this, Saved.class);
        startActivity(savedCourses);
        Log.d(TAG, "Saved Course DB");
    }

    public void back(View view) {
        Intent main = new Intent(this, MainActivity.class);
        startActivity(main);
        Log.d(TAG, "Back to Main");
    }
}