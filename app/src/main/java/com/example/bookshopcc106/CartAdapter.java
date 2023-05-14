package com.example.bookshopcc106;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class CartAdapter extends FirebaseRecyclerAdapter<cartModel, CartAdapter.myViewHolder> {
    FirebaseAuth firebaseAuth;
    public long total;
    DatabaseReference reference;

    public CartAdapter(@NonNull FirebaseRecyclerOptions<cartModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull cartModel model) {
        firebaseAuth = FirebaseAuth.getInstance();
        String currentuserEmail = firebaseAuth.getCurrentUser().getEmail();
        String rightEmail = currentuserEmail.replace(".","");
        holder.title.setText(model.getTitle());
        //holder.author.setText(model.getAuthor());
        holder.price.setText("₱ "+model.getPrice()+".00");
        holder.quantity.setText(String.valueOf(model.getQuantity()));
        Glide.with(holder.img.getContext())
                .load(model.getUrl())
                .placeholder(R.drawable.outline_image_24)
                .error(R.drawable.outline_image_24)
                .into(holder.img);

        ////______________checking if the book is on the checkout list
        reference = FirebaseDatabase.getInstance().getReference("checkout");
        reference.child(rightEmail).child(model.getTitle()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                        holder.selectBook.setChecked(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ///---------------
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String,Object> map = new HashMap<>();
                map.put("title",model.getTitle());
                map.put("price",Long.valueOf(model.getPrice()));
                map.put("url",model.getUrl());
                map.put("quantity",model.quantity+1);
                FirebaseDatabase.getInstance().getReference("cart").child(rightEmail).child(model.getTitle())
                        .setValue(map);
                if (holder.selectBook.isChecked()){
                    map.put("totalAmount",model.getQuantity()*model.getPrice());
                    FirebaseDatabase.getInstance().getReference("checkout").child(rightEmail).child(model.getTitle())
                            .setValue(map);
                    checkOutTotalAdd(model.getTitle(), rightEmail,model.getPrice(),model.getQuantity());
                }
            }
        });
        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                   ////--------if book quantity in cart is less than 1 delete it
                if (model.quantity>1) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("title", model.getTitle());
                    map.put("price", Long.valueOf(model.getPrice()));
                    map.put("url", model.getUrl());
                    map.put("quantity", model.quantity - 1);
                    FirebaseDatabase.getInstance().getReference("cart").child(rightEmail).child(model.getTitle())
                            .setValue(map);
                    if (holder.selectBook.isChecked()){
                        FirebaseDatabase.getInstance().getReference("checkout").child(rightEmail).child(model.getTitle())
                                .setValue(map);
                        checkOutTotalMinus(rightEmail,model.getPrice(),1);
                    }

                }else {
                    FirebaseDatabase.getInstance().getReference("cart").child(rightEmail).child(model.getTitle())
                            .removeValue();
                }
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference("cart").child(rightEmail).child(model.getTitle())
                        .removeValue();
                FirebaseDatabase.getInstance().getReference("checkout").child(rightEmail).child(model.getTitle()).removeValue();
                checkOutTotalMinus(rightEmail,model.getPrice(), model.getQuantity());
            }
        });
       holder.selectBook.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if (holder.selectBook.isChecked()){
                   Map<String, Object> map = new HashMap<>();
                   map.put("title", model.getTitle());
                   map.put("price", Long.valueOf(model.getPrice()));
                   map.put("url", model.getUrl());
                   map.put("quantity", model.quantity);
                   map.put("totalAmount",model.getQuantity()*model.getPrice());
                   FirebaseDatabase.getInstance().getReference("checkout").child(rightEmail).child(model.getTitle())
                           .setValue(map);
                   //checkOutTotalAdd(model.getTitle(), rightEmail,model.getPrice(),model.getQuantity());
               }else{
                   FirebaseDatabase.getInstance().getReference("checkout").child(rightEmail).child(model.getTitle()).removeValue();
                   checkOutTotalMinus(rightEmail,model.getPrice(), model.getQuantity());

               }
           }
       });

    }
    public void BookOnCheckout(String Title) {
        reference = FirebaseDatabase.getInstance().getReference("checkout");
        reference.child(Title).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().exists()){
                        DataSnapshot dataSnapshot = task.getResult();
                    }else {

                    }
                }

            }
        });
    }
    public void checkOutTotalAdd(String title,String email,long price, long quantity) {

        reference = FirebaseDatabase.getInstance().getReference("checkout").child(email);
        reference.child(title).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().exists()){
                        DataSnapshot dataSnapshot = task.getResult();
                        String checkoutTotal = String.valueOf(dataSnapshot.child("total").getValue());
                        Map<String, Object> total = new HashMap<>();
                        total.put("total",Long.valueOf(Long.valueOf(checkoutTotal)+(price * quantity)));
                        FirebaseDatabase.getInstance().getReference("checkout").child(email).child("total")
                                .setValue(total);
                    }else {
                        Map<String, Object> total = new HashMap<>();
                        total.put("total",Long.valueOf(price * quantity));
                        FirebaseDatabase.getInstance().getReference("checkout").child(email).child("total")
                                .setValue(total);
                    }
                }

            }
        });
    }public void checkOutTotalMinus(String email,long price, long quantity) {

        reference = FirebaseDatabase.getInstance().getReference("checkout").child(email);
        reference.child("total").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().exists()){
                        DataSnapshot dataSnapshot = task.getResult();
                        String checkoutTotal = String.valueOf(dataSnapshot.child("total").getValue());
                        Map<String, Object> total = new HashMap<>();
                        total.put("total",Long.valueOf(Long.valueOf(checkoutTotal)-(price * quantity)));
                        FirebaseDatabase.getInstance().getReference("checkout").child(email).child("total")
                                .setValue(total);
                    }
                }

            }
        });
    }
    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item,parent,false);
        return new myViewHolder(v);
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView title,author,price,add,minus,delete,quantity;

        CheckBox selectBook;
        LinearLayout viewbook;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.bookimg);
            title = itemView.findViewById(R.id.tv_title);
            author = itemView.findViewById(R.id.checkout_total);
            price = itemView.findViewById(R.id.tv_price);
            viewbook = itemView.findViewById(R.id.l_view_book);
            add = itemView.findViewById(R.id.add_quantity_cart);
            minus = itemView.findViewById(R.id.minus_quantity_cart);
            delete = itemView.findViewById(R.id.delete_cart_btn);
            quantity = itemView.findViewById(R.id.quantity_cart);
            selectBook = itemView.findViewById(R.id.cart_check_book);


        }
    }
}
