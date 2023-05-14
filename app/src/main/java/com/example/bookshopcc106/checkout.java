package com.example.bookshopcc106;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class checkout extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    CheckAdapter checkadapter;
    DatabaseReference reference;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    Button order;
    TextView total;
    CartAdapter cart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

      total = findViewById(R.id.Total_amount);
      order = findViewById(R.id.btn_placeOrder);

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



        ////-----------
        reference = FirebaseDatabase.getInstance().getReference("checkout").child(rightEmail);
        reference.child("total").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    String bookTitle = String.valueOf(snapshot.child("total").getValue());
                    total.setText("₱ "+bookTitle+".00");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void order(String email) {

        reference = FirebaseDatabase.getInstance().getReference("checkout").child(email);
        reference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().exists()){
                        DataSnapshot dataSnapshot = task.getResult();
                         int a=Integer.valueOf(String.valueOf(dataSnapshot.getChildrenCount()));
                        for (int i = 0;a>i;) {
                            String bookTitle, bookPrice, bookUrl, bookQuantity, totalAmount;

                            Map<String, Object> order = new HashMap<>();
                            bookTitle = String.valueOf(dataSnapshot.child("title").getValue());
                            bookPrice = String.valueOf(dataSnapshot.child("price").getValue());
                            bookUrl = String.valueOf(dataSnapshot.child("url").getValue());
                          //  order.put();
                        }
                    }
                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkadapter.startListening();
    }

    @Override
    public void onBackPressed() {
            super.onBackPressed();
            Intent intent = new Intent(this, cart.class);
            startActivity(intent);
    }
}