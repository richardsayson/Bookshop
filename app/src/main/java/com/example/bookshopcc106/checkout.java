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

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

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
    CartAdapter cart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

      totalcost = findViewById(R.id.Total_amount);
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
        reference = FirebaseDatabase.getInstance().getReference("checkouttotal").child(rightEmail).child("total");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long total=0;
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
                    total = Long.valueOf(String.valueOf(snapshot.child("total").getValue()));
                    totalcost.setText("₱ " + formatter.format(Long.valueOf(total)));
                }else{

                }
                //Toast.makeText(cart.this, jsonString, Toast.LENGTH_LONG).show();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ////
        reference = FirebaseDatabase.getInstance().getReference("checkout").child(rightEmail);
        reference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().exists()){
                        DataSnapshot dataSnapshot = task.getResult();
                        JSONObject jsonObject = new JSONObject();
                     /*
                        Map<Integer, String> title= new HashMap<>();
                        for (int i = 0 ;dataSnapshot.getChildrenCount()>i;i++){
                            String name = dataSnapshot.child("title").getValue().toString();
                            title.put(i, name);
                        }
                        Toast.makeText(checkout.this,
                                String.valueOf(title),
                                Toast.LENGTH_LONG).show();
                       for (int i = 0;i<dataSnapshot.getChildrenCount();){

                       }

                      */
                         /*   for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Object value = snapshot.getValue();
                                try {
                                    jsonObject.put(value);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                        }*/

                       // n.put(jsonObject);

                       // String jsonString = jsonObject.toString();
                     //   Toast.makeText(checkout.this, jsonString, Toast.LENGTH_LONG).show();
                    }
                }

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
                        JSONObject jsonObject = new JSONObject();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String key = snapshot.getKey();
                            Object value = snapshot.getValue();
                            try {
                                jsonObject.put(key, value);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        String jsonString = jsonObject.toString();
                        Toast.makeText(checkout.this, jsonString, Toast.LENGTH_LONG).show();
                    }
                }

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