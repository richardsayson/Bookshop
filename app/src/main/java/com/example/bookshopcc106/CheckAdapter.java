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
import com.google.firebase.auth.FirebaseAuth;

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
        holder.title.setText(model.getTitle());
        //holder.author.setText(model.getAuthor());
        holder.price.setText("₱ "+model.getPrice()+".00");
        holder.quantity.setText("×"+String.valueOf(model.getQuantity()));
        Glide.with(holder.img.getContext())
                .load(model.getUrl())
                .placeholder(R.drawable.outline_image_24)
                .error(R.drawable.outline_image_24)
                .into(holder.img);
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.checkout_item,parent,false);
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
           // author = itemView.findViewById(R.id.tv_author);
            price = itemView.findViewById(R.id.tv_price);
            quantity = itemView.findViewById(R.id.checkout_quantity);



        }
    }
}
