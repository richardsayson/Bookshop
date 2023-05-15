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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class order extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    FirebaseAuth firebaseAuth;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    OrderAdapter orderadapter;
    DatabaseReference reference;
    FirebaseDatabase firebaseDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);



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
        navigationView.setCheckedItem(R.id.nav_order);


        ///-----Firebase--- getting the data from firebase
        ///----------- getting the current user
        firebaseAuth = FirebaseAuth.getInstance();
        String currentuserEmail = firebaseAuth.getCurrentUser().getEmail();
        String rightEmail = currentuserEmail.replace(".","");

        recyclerView = findViewById(R.id.rv);
        layoutManager = new LinearLayoutManager(order.this);
        recyclerView.setLayoutManager(layoutManager);


        FirebaseRecyclerOptions<orderModel> options =
                new FirebaseRecyclerOptions.Builder<orderModel>()
                        .setQuery(FirebaseDatabase.getInstance()
                                .getReference().child("order").child(rightEmail), orderModel.class)
                        .build();
        orderadapter = new OrderAdapter(options);
        recyclerView.setAdapter(orderadapter);

    }
    @Override
    protected void onStart() {
        super.onStart();
        orderadapter.startListening();
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
       /* if(id ==R.id.nav_dashboard){
            Intent i = new Intent(order.this,dashboard.class);
            startActivity(i);
        }
       if(id ==R.id.nav_searchbook){
            Intent i = new Intent(order.this,searchBook.class);
            startActivity(i);
        }

        */
        if(id ==R.id.nav_home){
            Intent i = new Intent(order.this,home.class);
            startActivity(i);
        }
        if(id ==R.id.nav_cart){
            Intent i = new Intent(order.this,cart.class);
            startActivity(i);
        }
        if(id ==R.id.nav_order){

        }
        if(id ==R.id.nav_logout){

            firebaseAuth.signOut();
            Intent i = new Intent(getApplicationContext(), login.class);
            startActivity(i);
        }

        return true;
    }
}