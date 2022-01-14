package ca.mohawk.gelfand.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class SavedDetail extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final String TAG = "==SAVED DETAIL==";

    private DrawerLayout myDrawer;

    private NavigationView myNavView;

    private MyDbHelper mydbhelp = new MyDbHelper(this);

    private String code;
    private String title;
    private String program;
    private String hours;
    private String courseDescription;
    private long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_detail);

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
            String details = extras.getString("savedDetails");

            String lines[] = details.split("\\r?\\n");

            try {
                id = Long.parseLong(lines[0]);
            } catch (NumberFormatException e) {
                Log.d(TAG, "NumberFormatException: " + e.getMessage());
            }

            code = lines[1];
            title = lines[2];
            program = lines[3];
            hours = lines[4];
            courseDescription = lines[5];

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

    public void deleteFromDb(View view) {
        deleteEntry(id);
        Intent saved = new Intent(this, Saved.class);
        startActivity(saved);
        Log.d(TAG, "Back to saved");
        Toast.makeText(this, "Course: " + id + " deleted.",Toast.LENGTH_SHORT).show();
    }

    public void deleteEntry(long _id) {
        SQLiteDatabase db = mydbhelp.getReadableDatabase();
        db.delete(MyDbHelper.MYTABLE, MyDbHelper.ID + "=" + _id, null);
        Log.d(TAG, "Deleted entry");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Find out the current state of the drawer (open or closed)
        boolean isOpen = myDrawer.isDrawerOpen(GravityCompat.START);
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                // Home button - open or close the drawer
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
        // Show visual for selection
        myNavView.setCheckedItem(item);
        // Close the Drawer
        myDrawer.closeDrawers();

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
        Log.d(TAG, "== Item Selected: " + item + " ==");
        return false;
    }

    public void back(View view) {
        Intent main = new Intent(this, MainActivity.class);
        startActivity(main);
        Log.d(TAG, "Back to Main");
    }
}