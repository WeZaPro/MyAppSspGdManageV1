package com.example.myappsspgdmanagev1;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference mDatabaseRef = firebaseDatabase.getReference("uploads");

    private Context mContext;
    private List<Artist> mUploads;
    String clubkey;

    public ImageAdapter(Context context, List<Artist> myModels)
    {
        mContext=context;
        mUploads=myModels;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(mContext).inflate(R.layout.layout_artist_list, parent,false);
        return  new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder imageViewHolder, int position) {

        Artist itemMyModel=mUploads.get(position);

        imageViewHolder.textViewName.setText(itemMyModel.getArtistName());
        imageViewHolder.textViewJob.setText(itemMyModel.getArtistJob());
        imageViewHolder.textViewGenre.setText(itemMyModel.getArtistGenre());
        Picasso.with(mContext)
                .load(itemMyModel.getImgUrl())
                .placeholder(R.drawable.imagepreview)
                .fit()
                .centerCrop()
                .into(imageViewHolder.imageView);

        //imageViewHolder.itemView.setTag(position);
        imageViewHolder.itemView.setTag(itemMyModel);

    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{

        TextView textViewName,textViewJob,textViewGenre;
        ImageView imageView;


        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);


            textViewName = itemView.findViewById(R.id.textViewName);
            textViewJob = itemView.findViewById(R.id.textViewJob);
            textViewGenre = itemView.findViewById(R.id.textViewGenre);
            imageView = itemView.findViewById(R.id.imageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //int position = (int) view.getTag();
                    Artist myModels = (Artist) view.getTag();

                    Intent intent = new Intent(mContext,ItemActivity.class);
                    /*intent.putExtra("urlKey",myModels.getImgUrl());
                    intent.putExtra("nameKey",myModels.getArtistName());*/
                    intent.putExtra("name",myModels);

                    mContext.startActivity(intent);
                }
            });
        }
    }
}
