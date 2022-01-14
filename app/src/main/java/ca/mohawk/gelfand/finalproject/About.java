package ca.mohawk.gelfand.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

/**
 * ANDROID FINAL - APRIL 6TH
 * I, James Gelfand,000275852 certify that this material is my original work.
 * No other person's work has been used without due acknowledgement.
 *
 * YouTube link: https://youtu.be/7DW6pZvxkbo
 */

public class About extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final String TAG = "==ABOUT ACTIVITY==";

    private DrawerLayout myDrawer;

    NavigationView myNavView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

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
                break;
        }
        Log.d(TAG,"== Item Selected: " + item + " ==");
        return false;
    }

}