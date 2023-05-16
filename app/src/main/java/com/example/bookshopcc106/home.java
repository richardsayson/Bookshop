package com.example.bookshopcc106;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationBarMenu;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    NavigationBarMenu barMenu;
    Toolbar toolbar;
    RecyclerView recyclerView;
    HomeAdapter homeAdapter;
    RecyclerView.LayoutManager layoutManager;
    EditText search;
    FirebaseAuth firebaseAuth;
    TextView searchbtn;
    private AlertDialog loadingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
         search = findViewById(R.id.et_search_book);
        searchbtn = findViewById(R.id.tv_search_btn);

       // _______ binding the recycle view
        recyclerView = findViewById(R.id.rv);
        layoutManager = new GridLayoutManager(home.this,3);
        recyclerView.setLayoutManager(layoutManager);



     ///-----Firebase--- getting the data from firebase
        FirebaseRecyclerOptions<HomeModel> options =
                new FirebaseRecyclerOptions.Builder<HomeModel>()
                        .setQuery(FirebaseDatabase.getInstance()
                                .getReference().child("books"), HomeModel.class)
                        .build();
        homeAdapter = new HomeAdapter(options);
        recyclerView.setAdapter(homeAdapter);
        //
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
        navigationView.setCheckedItem(R.id.nav_home);

        //// -----Events
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                showSearch(search.getText().toString());
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              showSearch(search.getText().toString());
            }
        });
    }
    private void showLoadingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_loading, null);
        builder.setView(view);
        loadingDialog = builder.create();
        loadingDialog.setCancelable(false);
        loadingDialog.show();
    }

    private void hideLoadingDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }
    private void showSearch(String toString) {
        if (!toString.isEmpty()){
            if(!toString.startsWith(toString.toLowerCase())){
                FirebaseRecyclerOptions<HomeModel> options =
                        new FirebaseRecyclerOptions.Builder<HomeModel>()
                                .setQuery(FirebaseDatabase.getInstance()
                                        .getReference().child("books")
                                        .orderByChild("title")
                                        .startAt( toString)
                                        .endAt(toString+"\uf8ff"), HomeModel.class)
                                .build();

                homeAdapter = new HomeAdapter(options);
                recyclerView.setAdapter(homeAdapter);
                homeAdapter.startListening();
            }
         else{
                FirebaseRecyclerOptions<HomeModel> options =
                        new FirebaseRecyclerOptions.Builder<HomeModel>()
                                .setQuery(FirebaseDatabase.getInstance()
                                        .getReference().child("books")
                                        .orderByChild("title").startAt(toString.substring(0, 1)
                                                .toUpperCase()+toString.substring(1))
                                        .endAt(toString.substring(0, 1)
                                                .toUpperCase()+toString.substring(1)+"\uf8ff"),
                                        HomeModel.class)
                                .build();

                homeAdapter = new HomeAdapter(options);
                recyclerView.setAdapter(homeAdapter);
                homeAdapter.startListening();
            }


        }else {
            all_book();
        }
    }

    private void all_book() {
        FirebaseRecyclerOptions<HomeModel> options =
                new FirebaseRecyclerOptions.Builder<HomeModel>()
                        .setQuery(FirebaseDatabase.getInstance()
                                .getReference().child("books"), HomeModel.class)
                        .build();

        homeAdapter = new HomeAdapter(options);
        homeAdapter.startListening();
        recyclerView.setAdapter(homeAdapter);


    }

    @Override
    protected void onStart() {
        super.onStart();
        homeAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();


        if(id ==R.id.nav_cart){
            Intent i = new Intent(home.this,cart.class);
            startActivity(i);
        }
        if(id ==R.id.nav_order){
            Intent i = new Intent(home.this,order.class);
            startActivity(i);
        }  if(id ==R.id.nav_logout) {
                firebaseAuth.signOut();
                Intent i = new Intent(getApplicationContext(), login.class);
                startActivity(i);
        } if(id ==R.id.nav_profile){
            Intent i = new Intent(home.this,profile.class);
            startActivity(i);
        }
        return true;
    }
}