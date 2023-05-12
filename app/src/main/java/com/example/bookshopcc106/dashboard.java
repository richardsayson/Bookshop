package com.example.bookshopcc106;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {



    LinearLayout cart,order,home,search;
    //Variables
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // --------------Event
        cart = findViewById(R.id.btn_cart);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(dashboard.this,cart.class);
                startActivity(i);
            }
        });

        order = findViewById(R.id.btn_order);
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(dashboard.this,order.class);
                startActivity(i);
            }
        });
        home = findViewById(R.id.btn_home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(dashboard.this,home.class);
                startActivity(i);
            }
        });
        search = findViewById(R.id.btn_searchbook);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(dashboard.this,searchBook.class);
                startActivity(i);
            }
        });




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
       // navigationView.setCheckedItem(R.id.nav_dashboard);


    }

    @Override
    public void onBackPressed() {
          if (drawerLayout.isDrawerOpen(GravityCompat.START))
          {
              drawerLayout.closeDrawer(GravityCompat.START);
          }
          else {
              super.onBackPressed();
              Intent a = new Intent(Intent.ACTION_MAIN);
              a.addCategory(Intent.CATEGORY_HOME);
              a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
              startActivity(a);
          }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
         int id = menuItem.getItemId();
      /*   if(id ==R.id.nav_dashboard){
         }
        if(id ==R.id.nav_home){
            Intent i = new Intent(dashboard.this,home.class);
            startActivity(i);
        }
        if(id ==R.id.nav_searchbook){
            Intent i = new Intent(dashboard.this,searchBook.class);
            startActivity(i);
        }

       */
        if(id ==R.id.nav_cart){
            Intent i = new Intent(dashboard.this,cart.class);
            startActivity(i);
        }
        if(id ==R.id.nav_order){
            Intent i = new Intent(dashboard.this,order.class);
            startActivity(i);
        }
        if(id ==R.id.nav_logout){
            Intent i = new Intent(getApplicationContext(), login.class);
            startActivity(i);
        }


        return true;
    }
}