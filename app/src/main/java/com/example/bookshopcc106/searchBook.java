package com.example.bookshopcc106;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class searchBook extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_book);


        //-------------------Hooks____________
        drawerLayout = findViewById(R.id.draw_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        //-------------Toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //---------------nav drawer manu

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle;
        toggle = new ActionBarDrawerToggle(this,
                drawerLayout,
                toolbar,
                R.string.Nav_open,
                R.string.Nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        //navigationView.setCheckedItem(R.id.nav_searchbook);
    }
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
            Intent intent = new Intent(this, dashboard.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
      /*  if(id ==R.id.nav_dashboard){
            Intent i = new Intent(searchBook.this,dashboard.class);
            startActivity(i);
        }
        if(id ==R.id.nav_home){
            Intent i = new Intent(searchBook.this,home.class);
            startActivity(i);
        } if(id ==R.id.nav_searchbook){

        }

       */
        if(id ==R.id.nav_cart){
            Intent i = new Intent(searchBook.this,cart.class);
            startActivity(i);
        }
        if(id ==R.id.nav_order){
            Intent i = new Intent(searchBook.this,order.class);
            startActivity(i);
        }
        if(id ==R.id.nav_logout){
            Intent i = new Intent(getApplicationContext(), login.class);
            startActivity(i);
        }

        return true;
    }
}