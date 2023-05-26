package com.bStudio.petcare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;



public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {

    Context context;
    ArrayList<PostView> postViews;

    public Adapter(Context c,ArrayList<PostView> p)
    {
        context = c;
        postViews = p;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.post_item_,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.Pet_name.setText(postViews.get(position).getPet_name());
        Picasso.get().load(postViews.get(position).getPet_image()).into(holder.Pet_image);

    }

    @Override
    public int getItemCount() {
        return postViews.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {

        TextView Pet_name;
        ImageView Pet_image;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            Pet_name = (TextView) itemView.findViewById(R.id.pet_view_name);
            Pet_image =(ImageView) itemView.findViewById(R.id.pet_image);
        }
    }
}
