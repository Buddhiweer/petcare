package com.bStudio.petcare;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ViewHolder>{

    ArrayList<ProjectModel> list;
    Context context;


    public ProjectAdapter(ArrayList<ProjectModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ProjectModel model = list.get(position);


//        Picasso.get().load(model.getImage1()).placeholder(R.drawable.add_image).into(holder.petImage1);
        Glide.with(holder.petImage1.getContext())
                .load(model.getImage1())
                .placeholder(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark)
//                .circleCrop()
                .error(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.petImage1);


        holder.name.setText(model.getName());
        holder.gender.setText(model.getGender());
        holder.breed.setText(model.getBreed());
        holder.age.setText(model.getAge());
        holder.district.setText(model.getDistrict());
        holder.pettype.setText(model.getPettype());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SinglePostActivity.class);
                intent.putExtra("singleImage", model.getImage1());
                intent.putExtra("singleBreed", model.getBreed());
                intent.putExtra("singleAage", model.getAge());
                intent.putExtra("singleSize", model.getSize());
                intent.putExtra("singlePublishDate", model.getPublishDate());
                intent.putExtra("singleAddress", model.getAddress());
                intent.putExtra("singleAbout", model.getAbout());
                intent.putExtra("singleContactNumber", model.getContactNumber());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView name, gender, breed, age,district,pettype;
        ImageView petImage1;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.disname);
            gender = itemView.findViewById(R.id.disgender);
            breed = itemView.findViewById(R.id.disbreed);
            age = itemView.findViewById(R.id.disage);
            district = itemView.findViewById(R.id.disdistrict);
            pettype = itemView.findViewById(R.id.distype);


            petImage1 = itemView.findViewById(R.id.disimage1);

        }
    }
}
