package com.example.myappsspgdmanagev1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private Context mContext;
    private List<Artist> mUploads;

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

        final Artist itemMyModel=mUploads.get(position);

        imageViewHolder.textViewName.setText(itemMyModel.getArtistName());
        imageViewHolder.textViewJob.setText(itemMyModel.getArtistJob());
        imageViewHolder.textViewGenre.setText(itemMyModel.getArtistGenre());
        Picasso.with(mContext)
                .load(itemMyModel.getImgUrl())
                .placeholder(R.drawable.imagepreview)
                .fit()
                .centerCrop()
                .into(imageViewHolder.imageView);

        imageViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext,"item is : "+itemMyModel.getArtistName(),Toast.LENGTH_SHORT).show();
            }
        });

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


        }
    }
}
