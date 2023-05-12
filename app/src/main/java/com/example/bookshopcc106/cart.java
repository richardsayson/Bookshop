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

public class cart extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

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
        navigationView.setCheckedItem(R.id.nav_cart);


    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
            Intent intent = new Intent(this, home.class);
            startActivity(intent);
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
      /*  if(id ==R.id.nav_dashboard){
            Intent i = new Intent(cart.this,dashboard.class);
            startActivity(i);
        }
       if(id ==R.id.nav_searchbook){
            Intent i = new Intent(cart.this,searchBook.class);
            startActivity(i);
        }
       */
        if(id ==R.id.nav_home){
            Intent i = new Intent(cart.this,home.class);
            startActivity(i);
        }
        if(id ==R.id.nav_cart){
        }
        if(id ==R.id.nav_order){
            Intent i = new Intent(cart.this,order.class);
            startActivity(i);
        }  if(id ==R.id.nav_logout){
            Intent i = new Intent(getApplicationContext(), login.class);
            startActivity(i);
        }

        return true;
    }
}