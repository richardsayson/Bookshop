package com.example.bookshopcc106;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;

public class cart extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    CartAdapter cartadapter;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    Button check;
    TextView totalcheck;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
         check = findViewById(R.id.btn_checkout);
         totalcheck = findViewById(R.id.checkout_total);
        // _______ binding the recycle view
        recyclerView = findViewById(R.id.rv);
        layoutManager = new LinearLayoutManager(cart.this);
        recyclerView.setLayoutManager(layoutManager);


        ///-----Firebase--- getting the data from firebase
        ////-----getting current user
        firebaseAuth = FirebaseAuth.getInstance();
        String currentuserEmail = firebaseAuth.getCurrentUser().getEmail();
        String rightEmail = currentuserEmail.replace(".","");

        //------------- getting the data from firebase
        FirebaseRecyclerOptions<cartModel> options =
                new FirebaseRecyclerOptions.Builder<cartModel>()
                        .setQuery(FirebaseDatabase.getInstance()
                                .getReference().child("cart").child(rightEmail), cartModel.class)
                        .build();

        cartadapter = new CartAdapter(options);
        recyclerView.setAdapter(cartadapter);
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

        ////---------Events
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference = FirebaseDatabase.getInstance().getReference("checkouttotal").child(rightEmail).child("total");
                reference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        long total=0;
                        if (task.isSuccessful()) {
                            DataSnapshot snapshot = task.getResult();
                            total = Long.valueOf(String.valueOf(snapshot.child("total").getValue()));
                            if (total!=0){
                                Intent i = new Intent(cart.this,checkout.class);
                                startActivity(i);
                            }else{
                                Toast.makeText(cart.this,"No Selected Item", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
            }
        });

        ///-----------------checking the total amount of select items
        reference = FirebaseDatabase.getInstance().getReference("checkouttotal").child(rightEmail).child("total");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long total=0 ,totalcost=0;
                ////----- getting all totalamount value of checkout_currentUse
                    DecimalFormat formatter = new DecimalFormat("#,###.00");
//                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//
//                        DecimalFormat formatter = new DecimalFormat("#,###.00");
////                        long a = snapshot.getChildrenCount();
////                        if (snapshot.getChildrenCount()>0) {
////                            for (long i = 0; i <= a; i++) {
////                                Object value = dataSnapshot.getValue();
////                                total += Long.valueOf(value.toString());
////                            }
//                            totalcheck.setText("₱ " + formatter.format(Long.valueOf(total)));
//
                if (snapshot.exists()) {
                    total = Long.valueOf(String.valueOf(snapshot.child("total").getValue()));
                    totalcheck.setText("₱ " + formatter.format(Long.valueOf(total)));
                }else{

                }
             //Toast.makeText(cart.this, jsonString, Toast.LENGTH_LONG).show();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    /////----------Methods
    public void B(String email) {
        reference = FirebaseDatabase.getInstance().getReference("checkout").child(email);
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String a = snapshot.getKey();
                Toast.makeText(cart.this,a, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        cartadapter.startListening();
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
            firebaseAuth.signOut();
            Intent i = new Intent(getApplicationContext(), login.class);
            startActivity(i);
        }

        return true;
    }
}