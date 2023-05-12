package com.example.bookshopcc106;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class viewbook extends AppCompatActivity {
TextView title,price,currentuser;
ImageView img;
DatabaseReference reference,ref;
Toolbar toolbar;
Button buy,addCart;
FirebaseAuth firebaseAuth;
String bookTitle,bookUrl,bookPrice;
cartModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewbook);
        title = findViewById(R.id.title_nmo);
        price = findViewById(R.id.price_nmo);
        img = findViewById(R.id.img);


        firebaseAuth = FirebaseAuth.getInstance();
        currentuser.setText(firebaseAuth.getCurrentUser().getEmail());


        ///------------------buy and add cart------------

        buy =findViewById(R.id.btn_buy);
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        addCart = findViewById(R.id.btn_add_cart);
        ////-----getting current user

        currentuser = findViewById(R.id.tv_current_user);

        addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  DatabaseReference ref = FirebaseDatabase.getInstance().getReference("cart");
                model.setTitle("dsds".trim());
                model.setPrice(Long.valueOf("43434"));
                model.setUrl("dsdawd".trim());
                ref.child("rede").setValue(model);
                Toast.makeText(viewbook.this, "Data inserted", Toast.LENGTH_SHORT).show();
               */

                AddCart("richard");
            }
        });

        //-------------Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);

        ///----getting the book title from the home activity
        Intent i =getIntent();
        String getTitle = i.getStringExtra("title");
        book(getTitle);
        if(getTitle.equals("")){
            Toast.makeText( viewbook.this, "No Book", Toast.LENGTH_SHORT).show();
        }
    }

    private void AddCart(String u) {

        Map<String,Object> map = new HashMap<>();
        map.put("title",bookTitle);
        map.put("price",bookPrice);
        map.put("url",bookUrl);
        FirebaseDatabase.getInstance().getReference("cart").child(u)
                .setValue(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(viewbook.this, "Data inserted", Toast.LENGTH_SHORT).show();
                                    }
                                });
    }



    private void book(String getTitle) {

    reference = FirebaseDatabase.getInstance().getReference("books");
    reference.child(getTitle).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
        @Override
        public void onComplete(@NonNull Task<DataSnapshot> task) {

            if(task.isSuccessful()){}

               if(task.getResult().exists()){

                   DataSnapshot dataSnapshot = task.getResult();

                   bookTitle = String.valueOf(dataSnapshot.child("title").getValue());
                   bookPrice = String.valueOf(dataSnapshot.child("price").getValue());
                   bookUrl = String.valueOf(dataSnapshot.child("url").getValue());

                   Glide.with(viewbook.this)
                                   .load(bookUrl)
                           .placeholder(R.drawable.outline_image_24)
                           .error(R.drawable.outline_image_24)
                           .into(img);

                   title.setText(bookTitle);
                   price.setText("₱ "+bookPrice+".00");


               }
        }
    });
    }


}