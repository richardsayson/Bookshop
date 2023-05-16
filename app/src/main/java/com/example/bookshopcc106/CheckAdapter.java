package com.example.bookshopcc106;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class CheckAdapter extends FirebaseRecyclerAdapter<checkModel, CheckAdapter.myViewHolder> {
    FirebaseAuth firebaseAuth;

    public CheckAdapter(@NonNull FirebaseRecyclerOptions<checkModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull checkModel model) {
        firebaseAuth = FirebaseAuth.getInstance();
        String currentuserEmail = firebaseAuth.getCurrentUser().getEmail();
        String rightEmail = currentuserEmail.replace(".","");
        DecimalFormat formatter = new DecimalFormat("#,###.00");
        holder.title.setText(model.getTitle());
        //holder.author.setText(model.getAuthor());
        holder.totalOrder.setText("₱ "+formatter.format(model.getTotal()));
        holder.price.setText("₱ "+formatter.format(model.getPrice()));
        holder.quantity.setText("×"+String.valueOf(model.getQuantity()));
        holder.item.setText("₱ "+ formatter.format(model.getPrice()*model.getQuantity()));
        Glide.with(holder.img.getContext())
                .load(model.getUrl())
                .placeholder(R.drawable.outline_image_24)
                .error(R.drawable.outline_image_24)
                .into(holder.img);

        paymentmethod(rightEmail,model.getTitle(),holder);
          holder.gcash.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  holder.gcash.setChecked(true);
                  holder.cod.setChecked(false);
                  holder.maya.setChecked(false);
                  updatePaymentMethod(rightEmail,model.getTitle(),model.getTotal(),"Gcash",model.getPrice(),model.getStatus(),model.getQuantity(),model.getUrl());
              }
          });
        holder.cod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.gcash.setChecked(false);
                holder.cod.setChecked(true);
                holder.maya.setChecked(false);
                holder.gcash.setChecked(true);
                holder.cod.setChecked(false);
                holder.maya.setChecked(false);
                updatePaymentMethod(rightEmail,model.getTitle(),model.getTotal()
                        ,"Cash On Delivery",model.getPrice(),model.getStatus(),model.getQuantity(),model.getUrl());
            }
        });
        holder.maya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.gcash.setChecked(false);
                holder.cod.setChecked(false);
                holder.maya.setChecked(true);
                holder.gcash.setChecked(true);
                holder.cod.setChecked(false);
                holder.maya.setChecked(false);
                updatePaymentMethod(rightEmail,model.getTitle(),model.getTotal()
                        ,"Pay Maya",model.getPrice(),model.getStatus(),model.getQuantity(),model.getUrl());
            }
        });

    }

    private void updatePaymentMethod(String rightEmail, String title, Long total, String paymentmethod, Long price, String status,Long quantity, String url) {
        Map<String, Object> map = new HashMap<>();
        map.put("title", title);
        map.put("price", Long.valueOf(price));
        map.put("url", url);
        map.put("quantity",quantity);
        map.put("total",total);
        map.put("status",status);
        map.put("paymentmethod",paymentmethod);
        FirebaseDatabase.getInstance().getReference("checkout").child(rightEmail).child(title)
                .setValue(map);




    }

    private void paymentmethod(String rightEmail, String title,myViewHolder holder) {
        FirebaseDatabase.getInstance().getReference("checkout").child(rightEmail).child(title)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            String method = String.valueOf(snapshot.child("paymentmethod").getValue());
                            switch (method){
                                case "Gcash":
                                    holder.gcash.setChecked(true);
                                    holder.cod.setChecked(false);
                                    holder.maya.setChecked(false);
                                    break;
                                case "Cash On Delivery":
                                    holder.gcash.setChecked(false);
                                    holder.cod.setChecked(true);
                                    holder.maya.setChecked(false);
                                    break;
                                case "Pay Maya":
                                    holder.gcash.setChecked(false);
                                    holder.cod.setChecked(false);
                                    holder.maya.setChecked(true);
                                    break;
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.checkout_item,parent,false);
        return new myViewHolder(v);
    }

    class myViewHolder extends RecyclerView.ViewHolder{

        ImageView img;
        TextView title,totalOrder,price,item,quantity;

        CheckBox selectBook;
        LinearLayout viewbook;
        RadioButton gcash,cod,maya;


        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.bookimg);
            title = itemView.findViewById(R.id.tv_title);
           // author = itemView.findViewById(R.id.tv_author);
            price = itemView.findViewById(R.id.tv_price);
            quantity = itemView.findViewById(R.id.checkout_quantity);
            totalOrder = itemView.findViewById(R.id.Order_total);
            gcash = itemView.findViewById(R.id.gcash_payment);
            cod = itemView.findViewById(R.id.cod_payment);
            maya = itemView.findViewById(R.id.maya_payment);
            item = itemView.findViewById(R.id.item_total);

        }
    }
}
