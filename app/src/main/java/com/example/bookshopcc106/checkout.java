package com.example.bookshopcc106;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
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
    TextView totalcost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

      totalcost = findViewById(R.id.Total_amount);
      order = findViewById(R.id.btn_placeOrder);
        // Replace `this` with your Context

        // _______ binding the recycle view
        recyclerView = findViewById(R.id.rv);
        layoutManager = new LinearLayoutManager(checkout.this);
        recyclerView.setLayoutManager(layoutManager);
        ///-----Firebase--- getting the data from firebase
        ////-----getting current user
        firebaseAuth = FirebaseAuth.getInstance();
        String currentuserEmail = firebaseAuth.getCurrentUser().getEmail();
        String rightEmail = currentuserEmail.replace(".","");

        Intent i =getIntent();
        String getTitle = i.getStringExtra("title");
        //Toast.makeText(checkout.this, getTitle, Toast.LENGTH_SHORT).show();

        FirebaseRecyclerOptions<checkModel> options =
                new FirebaseRecyclerOptions.Builder<checkModel>()
                        .setQuery(FirebaseDatabase.getInstance()
                                .getReference().child("checkout").child(rightEmail), checkModel.class)
                        .build();
        checkadapter = new CheckAdapter(options);
        recyclerView.setAdapter(checkadapter);

        ////-----------Events---------------
        FirebaseApp.initializeApp(this);
        totalchecker(rightEmail);

// Retrieve the books data
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                order(rightEmail);
                clearCheckouts(rightEmail);
            }
        });
    }

    private void clearCheckouts(String rightEmail) {
        FirebaseDatabase.getInstance().getReference("checkout").child(rightEmail).removeValue();
        FirebaseDatabase.getInstance().getReference("checkouttotal").child(rightEmail).removeValue();
        FirebaseDatabase.getInstance().getReference("checkouttotal").child(rightEmail).getKey();
    }

    private void totalchecker(String rightEmail) {
        reference = FirebaseDatabase.getInstance().getReference("checkouttotal").child(rightEmail).child("total");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long total;
                ////----- getting all totalamount value of checkout_currentUse
                DecimalFormat formatter = new DecimalFormat("#,###.00");

                 /*   for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        DecimalFormat formatter = new DecimalFormat("#,###.00");
                        long a = snapshot.getChildrenCount();
                        if (snapshot.getChildrenCount()>0) {
                            for (long i = 0; i <= a; i++) {
                                Object value = dataSnapshot.getValue();
                                total += Long.valueOf(value.toString());
                           }
                            totalcheck.setText("₱ " + formatter.format(Long.valueOf(total)));
                  */
                if (snapshot.exists()) {
                    long shipping = 84;
                    total = Long.valueOf(String.valueOf(snapshot.child("total").getValue()));

                    FirebaseDatabase.getInstance().getReference("checkout").child(rightEmail)
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    long shipping = snapshot.getChildrenCount()*84;
                                    long newTotal = total+shipping;
                                    totalcost.setText("₱ " + formatter.format(Long.valueOf(newTotal)));
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });
                }else{

                }
                //Toast.makeText(cart.this, jsonString, Toast.LENGTH_LONG).show();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void order(String email) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference("checkout").child(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Get all book data as a Map
                Map<String, Object> bookData = (Map<String, Object>) snapshot.getValue();

                // Log the book data
                for(String bookName : bookData.keySet()) {
                    Map<String, Object> bookInfo = (Map<String, Object>) bookData.get(bookName);
                    String title = (String) bookInfo.get("title");
                    Long price = (Long) bookInfo.get("price");
                    String url = (String) bookInfo.get("url");
                    String status = "To ship";
                    Log.d("FirebaseTest", "Book: " + title + ", Price: " + price + ", URL: " + url +",STATUS: "+status);

                }
                DatabaseReference newBooksRef = database.getReference("orders").child(email);
                newBooksRef.setValue(bookData).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("FirebaseTest", "Data saved successfully");

                            Intent intent = new Intent(checkout.this, order.class);
                            startActivity(intent);
                        } else {
                            Log.e("FirebaseTest", "Failed to save data.", task.getException());
                        }
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseTest", "Failed to read value.", error.toException());
            }
        });
    }
    public void placeOrder(String email) {
        reference = FirebaseDatabase.getInstance().getReference("checkout").child(email);
        reference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().exists()){
                        DataSnapshot dataSnapshot = task.getResult();
                        String[] title = new String[100];
                        for (int i = 0; dataSnapshot.getChildrenCount()>i;i++) {
                         //   title[i] = dataSnapshot.getChildren().;
                        }

                        FirebaseDatabase.getInstance().getReference("checkouttotal").child(email)
                                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                        DataSnapshot dataSnapshot = task.getResult();
                                        if (task.isSuccessful()) {
                                            if (task.getResult().exists()) {
                                             Long amount = Long.valueOf(dataSnapshot.child("total").getValue().toString());
                                            }
                                        }
                                    }
                                });

                        Toast.makeText(checkout.this, title[0], Toast.LENGTH_SHORT).show();

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