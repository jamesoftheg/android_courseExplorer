package ca.mohawk.gelfand.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

/**
 * ANDROID FINAL - APRIL 6TH
 * I, James Gelfand,000275852 certify that this material is my original work.
 * No other person's work has been used without due acknowledgement.
 *
 * YouTube link: https://youtu.be/7DW6pZvxkbo
 */

public class Saved extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final String TAG = "==SAVED ACTIVITY==";

    private DrawerLayout myDrawer;

    private NavigationView myNavView;

    private MyDbHelper mydbhelp = new MyDbHelper(this);

    private ArrayList<String> dbItems = new ArrayList<String>();

    private ArrayAdapter<String> adapter;

    private String savedMessage = "Should you wish to delete a class from your list, click on one of the courses below to do so.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved);

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

        ListView lv = findViewById(R.id.dbList);

        // SQL Database
        SQLiteDatabase db = mydbhelp.getReadableDatabase();

        String[] projection = {
                MyDbHelper.ID,
                MyDbHelper.COURSECODE,
                MyDbHelper.COURSENAME,
                MyDbHelper.COURSEPROGRAM,
                MyDbHelper.COURSEHOURS,
                MyDbHelper.COURSEDESCRIPTION
        };

        // Retrieve the data
        Cursor mycursor = db.query(
                MyDbHelper.MYTABLE, projection, null, null, null, null, null);
        String results = "";

        if (mycursor != null) {
            while (mycursor.moveToNext()) {
                Log.d(TAG, "found DB item");
                String code = mycursor.getString(
                        mycursor.getColumnIndex(MyDbHelper.COURSECODE));
                String name = mycursor.getString(
                        mycursor.getColumnIndex(MyDbHelper.COURSENAME));
                String program = mycursor.getString(
                        mycursor.getColumnIndex(MyDbHelper.COURSEPROGRAM));
                String hours = mycursor.getString(
                        mycursor.getColumnIndex(MyDbHelper.COURSEHOURS));
                String description = mycursor.getString(
                        mycursor.getColumnIndex(MyDbHelper.COURSEDESCRIPTION));
                long itemID = mycursor.getLong(
                        mycursor.getColumnIndex(MyDbHelper.ID));
                results = itemID +
                        "\n" + code +
                        "\n" + name +
                        "\n" + program +
                        "\n" + hours +
                        "\n" + description +
                        "\n";
                dbItems.add(results);
            }
            adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1,
                    dbItems);
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    String text = lv.getItemAtPosition(position).toString().trim();
                    Intent savedDetail = new Intent(view.getContext(), SavedDetail.class);
                    savedDetail.putExtra("savedDetails", text);
                    startActivity(savedDetail);
                    Log.d(TAG, "Saved item selected.");
                }});
            mycursor.close();
        }
        if (results == "") {
            savedMessage = "You currently have no saved classes.";
        }

        TextView hint = findViewById(R.id.savedHint);
        hint.setText(savedMessage);
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
        myNavView.setCheckedItem(item);
        myDrawer.closeDrawers();

        // Start activity onClick
        switch (item.getItemId()) {
            case R.id.mainPage:
                Intent main = new Intent(this, MainActivity.class);
                startActivity(main);
                break;
            case R.id.savedPage:
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