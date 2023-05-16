package com.example.bookshopcc106;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

public class OrderAdapter extends FirebaseRecyclerAdapter<orderModel, OrderAdapter.myViewHolder> {
    FirebaseAuth firebaseAuth;
    public long total;
    DatabaseReference reference;
    long id;
   int index;
    public OrderAdapter(@NonNull FirebaseRecyclerOptions<orderModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull orderModel model) {
        firebaseAuth = FirebaseAuth.getInstance();
        String currentuserEmail = firebaseAuth.getCurrentUser().getEmail();
        String rightEmail = currentuserEmail.replace(".","");

        DecimalFormat formatter = new DecimalFormat("#,###.00");

        holder.title.setText(model.getTitle());
        //holder.author.setText(model.getAuthor());
        holder.price.setText("₱ "+formatter.format(model.getPrice()));
        holder.quantity.setText("×"+String.valueOf(model.getQuantity()));
        holder.totalamount.setText("₱ "+formatter.format(model.getTotal()));
       // holder.paymentMethod.setText(model.getPaymentmethod());
        holder.status.setText(model.getStatus());
        Glide.with(holder.img.getContext())
                .load(model.getUrl())
                .placeholder(R.drawable.outline_image_24)
                .error(R.drawable.outline_image_24)
                .into(holder.img);
        ///---------------
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
        map.put("price", price);
        map.put("url", url);
        map.put("quantity",quantity);
        map.put("total",total);
        map.put("status","To Ship");
        map.put("payment method","Cash On Delivery");
        map.put("verify","true");
        FirebaseDatabase.getInstance().getReference("order").child(rightEmail).push()
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

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item,parent,false);
        return new myViewHolder(v);
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView title,price,quantity,status,paymentMethod,totalamount;


        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.bookimg);
            title = itemView.findViewById(R.id.tv_title);
             quantity = itemView.findViewById(R.id.checkout_quantity);
            price = itemView.findViewById(R.id.tv_price);
            status = itemView.findViewById(R.id.order_status);
            totalamount = itemView.findViewById(R.id.Order_total);


        }
    }
}
