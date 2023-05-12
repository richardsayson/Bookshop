package com.example.bookshopcc106;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;

public class HomeAdapter extends FirebaseRecyclerAdapter<HomeModel,HomeAdapter.myViewHolder> {
    FirebaseAuth firebaseAuth;

    public HomeAdapter(@NonNull FirebaseRecyclerOptions<HomeModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull HomeModel model) {
        holder.title.setText(model.getTitle());
        //holder.author.setText(model.getAuthor());
        holder.price.setText("₱ "+model.getPrice()+".00");
        Glide.with(holder.img.getContext())
                .load(model.getUrl())
                .placeholder(R.drawable.outline_image_24)
                .error(R.drawable.outline_image_24)
                .into(holder.img);
       holder.viewbook.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent i = new Intent(view.getContext(),viewbook.class);
               i.putExtra("title",model.getTitle());
               view.getContext().startActivity(i);
           }
       });

    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_item,parent,false);
        return new myViewHolder(v);
    }

    class myViewHolder extends RecyclerView.ViewHolder{

        ImageView img;
        TextView title,author,price;

        LinearLayout viewbook;





        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.book1);
            title = itemView.findViewById(R.id.tv_title);
           // author = itemView.findViewById(R.id.tv_author);
            price = itemView.findViewById(R.id.tv_price);
            viewbook = itemView.findViewById(R.id.l_view_book);
        }
    }
}
