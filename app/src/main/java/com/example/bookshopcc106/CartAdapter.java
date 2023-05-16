package com.example.bookshopcc106;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class CartAdapter extends FirebaseRecyclerAdapter<cartModel, CartAdapter.myViewHolder> {
    FirebaseAuth firebaseAuth;
    public long total;
    DatabaseReference reference;
    long id;
   int index;
    public CartAdapter(@NonNull FirebaseRecyclerOptions<cartModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull cartModel model) {
        firebaseAuth = FirebaseAuth.getInstance();
        String currentuserEmail = firebaseAuth.getCurrentUser().getEmail();
        String rightEmail = currentuserEmail.replace(".","");
        DecimalFormat formatter = new DecimalFormat("#,###.00");

        holder.title.setText(model.getTitle());
        //holder.author.setText(model.getAuthor());
        holder.price.setText("₱ "+formatter.format(model.getPrice()));
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
                if(snapshot.exists()) {
                        holder.selectBook.setChecked(true);
                }
                else{
                    holder.selectBook.setChecked(false);
                }
                }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
      //////----------Eventss
        ////------
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
                    checkOutTotalAdd(model.getTitle(), rightEmail, model.getUrl(), model.getPrice(),model.getQuantity());
                    checkOutAllAmountAdd(rightEmail, model.getPrice(), model.getQuantity());
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
                        checkOutTotalMinus(model.getTitle(), rightEmail, model.getUrl(), model.getPrice(),model.getQuantity());
                        checkOutAllAmountMinus(rightEmail, model.getPrice(), model.getQuantity());
                    }

                }else {
                    FirebaseDatabase.getInstance().getReference("cart").child(rightEmail).child(model.getTitle())
                            .removeValue();
                    FirebaseDatabase.getInstance().getReference("checkout").child(rightEmail).child(model.getTitle())
                            .removeValue();
                    checkOutAllAmountMinus(rightEmail, model.getPrice(), model.getQuantity());
                }
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference("cart").child(rightEmail).child(model.getTitle())
                        .removeValue();
                FirebaseDatabase.getInstance().getReference("checkout").child(rightEmail).child(model.getTitle()).removeValue();
               // checkOutTotalMinus(rightEmail,model.getPrice(), model.getQuantity());
                checkOutAllAmountMinus(rightEmail, model.getPrice(), model.getQuantity());
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
                   map.put("total",model.getQuantity()*model.getPrice()+84);
                   map.put("status","To Pay");
                   map.put("paymentmethod","Cash On Delivery");
                   FirebaseDatabase.getInstance().getReference("checkout").child(rightEmail).child(model.getTitle())
                           .setValue(map);
                   checkOutAllAmount(rightEmail, model.getPrice(), model.getQuantity());
                   //checkOutTotalAdd(model.getTitle(), rightEmail,model.getPrice(),model.getQuantity());
//                   FirebaseDatabase.getInstance().getReference("checkout").child(rightEmail).child(model.getTitle())
//                           .setValue(map);
                 //  placeOrder(rightEmail,model.getTitle(),model.getPrice(),model.getUrl(),model.getQuantity(), model.getQuantity()* model.getPrice());
               }else{
                   FirebaseDatabase.getInstance().getReference("checkout").child(rightEmail).child(model.getTitle()).removeValue();
                   checkOutAllAmount_unselect(rightEmail, model.getPrice(), model.getQuantity());
                //   orderCancel(rightEmail);
               }
           }
       });

    }
/////--------------------Methods
    private void orderCancel(String rightEmail) {
        FirebaseDatabase.getInstance().getReference().child("order").child(rightEmail)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            if (snapshot.getChildrenCount()!=0) {
                                id = (snapshot.getChildrenCount());
                            }
                        }for ( index = 0; id > index;index++){
                            reference = FirebaseDatabase.getInstance().getReference("order").child(rightEmail);
                            reference.child(String.valueOf(index)).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if(task.isSuccessful()){
                                        if(task.getResult().exists()) {
                                            DataSnapshot dataSnapshot = task.getResult();
                                            String verify = dataSnapshot.child("verify").getValue().toString();
                                            if (verify.equals("false")) {
                                                FirebaseDatabase.getInstance().getReference("order").child(rightEmail).child(String.valueOf(index))
                                                        .removeValue();
                                            }
                                        }
                                    }

                                }
                            });
                        }

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    private void placeOrder(String rightEmail, String title, Long price, String url, Long quantity, Long total) {
        FirebaseDatabase.getInstance().getReference().child("order").child(rightEmail).get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(task.isSuccessful()) {
                            DataSnapshot dataSnapshot = task.getResult();
                                id = (dataSnapshot.getChildrenCount());
                            }
                        }
                });
        Map<String, Object> map = new HashMap<>();
        map.put("title", title);
        map.put("price", Long.valueOf(price));
        map.put("url", url);
        map.put("quantity",Long.valueOf(quantity));
        map.put("total",Long.valueOf(total));
        map.put("status","To Ship");
        map.put("paymentmethod","Cash On Delivery");
        map.put("verify","true");
        FirebaseDatabase.getInstance().getReference("order").child(rightEmail).child(title)
                .setValue(map);
    }

    public void BookOnCheckout(String Title) {
        reference = FirebaseDatabase.getInstance().getReference("checkout");
        reference.child(Title).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
             snapshot.getKey();
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
    public void checkOutAllAmountAdd(String email,long price, long quantity) {

        reference = FirebaseDatabase.getInstance().getReference("checkouttotal").child(email);
        reference.child("total").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().exists()) {
                        DataSnapshot dataSnapshot = task.getResult();
                        Long checkoutTotal = Long.valueOf(dataSnapshot.child("total").getValue().toString());
                        Map<String, Object> map = new HashMap<>();
                        map.put("total", checkoutTotal + price);
                        FirebaseDatabase.getInstance().getReference("checkouttotal").child(email).child("total")
                                .setValue(map);

                    }
                }

            }
        });
    }
    public void checkOutAllAmountMinus(String email,long price, long quantity) {

        reference = FirebaseDatabase.getInstance().getReference("checkouttotal").child(email);
        reference.child("total").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().exists()){
                        DataSnapshot dataSnapshot = task.getResult();
                        Long checkoutTotal = Long.valueOf(dataSnapshot.child("total").getValue().toString());
                        Map<String, Object> map = new HashMap<>();
                        map.put("total",checkoutTotal-price);
                        FirebaseDatabase.getInstance().getReference("checkouttotal").child(email).child("total")
                                .setValue(map);
                    }
                }

            }
        });
    }
    public void checkOutAllAmount_unselect(String email,long price, long quantity) {

        reference = FirebaseDatabase.getInstance().getReference("checkouttotal").child(email);
        reference.child("total").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().exists()){
                        DataSnapshot dataSnapshot = task.getResult();
                        Long checkoutTotal = Long.valueOf(dataSnapshot.child("total").getValue().toString());
                        Map<String, Object> map = new HashMap<>();
                        map.put("total",checkoutTotal-(price*quantity));
                        FirebaseDatabase.getInstance().getReference("checkouttotal").child(email).child("total")
                                .setValue(map);
                    }
                }

            }
        });
    }
    public void checkOutAllAmount(String email,long price, long quantity) {

        reference = FirebaseDatabase.getInstance().getReference("checkouttotal").child(email);
        reference.child("total").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().exists()){
                        DataSnapshot dataSnapshot = task.getResult();
                        Long checkoutTotal = Long.valueOf(dataSnapshot.child("total").getValue().toString());
                        Map<String, Object> map = new HashMap<>();
                        map.put("total",checkoutTotal+(price*quantity));
                        FirebaseDatabase.getInstance().getReference("checkouttotal").child(email).child("total")
                                .setValue(map);
                    }else {
                        Map<String, Object> map = new HashMap<>();
                        map.put("total",price*quantity);
                        FirebaseDatabase.getInstance().getReference("checkouttotal").child(email).child("total")
                                .setValue(map);
                    }
                }

            }
        });
    }

    public void checkOutTotalAdd(String title,String email,String url,long price, long quantity) {

        reference = FirebaseDatabase.getInstance().getReference("checkout").child(email);
        reference.child(title).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().exists()){
                        DataSnapshot dataSnapshot = task.getResult();
                        Long checkoutTotal = Long.valueOf(dataSnapshot.child("total").getValue().toString());
                        Map<String, Object> map = new HashMap<>();
                        map.put("title",title);
                        map.put("price",price);
                        map.put("quantity",quantity+1);
                        map.put("url",url);
                        map.put("total",checkoutTotal+price);
                        FirebaseDatabase.getInstance().getReference("checkout").child(email).child(title)
                                .setValue(map);
                    }else {
                        Map<String, Object> total = new HashMap<>();
                        total.put("total",Long.valueOf(price * quantity));
                        FirebaseDatabase.getInstance().getReference("checkout").child(email).child("total")
                                .setValue(total);
                    }
                }

            }
        });
    }

    public void checkOutTotalMinus(String title,String email,String url,long price, long quantity) {

        reference = FirebaseDatabase.getInstance().getReference("checkout").child(email);
        reference.child(title).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().exists()){
                        DataSnapshot dataSnapshot = task.getResult();
                        Long checkoutTotal = Long.valueOf(dataSnapshot.child("total").getValue().toString());
                        Map<String, Object> map = new HashMap<>();
                        map.put("title",title);
                        map.put("price",price);
                        map.put("quantity",quantity-1);
                        map.put("url",url);
                        map.put("total",checkoutTotal-price);
                        FirebaseDatabase.getInstance().getReference("checkout").child(email).child(title)
                                .setValue(map);
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
