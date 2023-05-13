package com.example.bookshopcc106;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class checkout extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    CheckAdapter checkadapter;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    TextView total;
    CartAdapter cart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

      total = findViewById(R.id.Total_amount);


        ////-----getting current user
        firebaseAuth = FirebaseAuth.getInstance();

        // _______ binding the recycle view
        recyclerView = findViewById(R.id.rv);
        layoutManager = new LinearLayoutManager(checkout.this);
        recyclerView.setLayoutManager(layoutManager);


        ///-----Firebase--- getting the data from firebase
        String currentuserEmail = firebaseAuth.getCurrentUser().getEmail();
        String rightEmail = currentuserEmail.replace(".","");

        Intent i =getIntent();
        String getTitle = i.getStringExtra("title");
        Toast.makeText(checkout.this, getTitle, Toast.LENGTH_SHORT).show();

        FirebaseRecyclerOptions<checkModel> options =
                new FirebaseRecyclerOptions.Builder<checkModel>()
                        .setQuery(FirebaseDatabase.getInstance()
                                .getReference().child("checkout").child(rightEmail), checkModel.class)
                        .build();
        checkadapter = new CheckAdapter(options);
        recyclerView.setAdapter(checkadapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkadapter.startListening();
    }

    @Override
    public void onBackPressed() {
            super.onBackPressed();
        String currentuserEmail = firebaseAuth.getCurrentUser().getEmail();
        String rightEmail = currentuserEmail.replace(".","");
        FirebaseDatabase.getInstance().getReference("checkout").child(rightEmail).removeValue();

            Intent intent = new Intent(this, home.class);
            startActivity(intent);
    }
}