package com.example.myappsspgdmanagev1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class ItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);


        ImageView imagViewItem = findViewById(R.id.imagViewItem);

        Bundle bundle = getIntent().getExtras();
        String url = bundle.getString("nameKey");
        Toast.makeText(getApplicationContext(),"name is "+url,Toast.LENGTH_SHORT).show();


        Picasso.with(this)
                .load(url)
                .placeholder(R.drawable.imagepreview)
                .fit()
                .centerCrop()
                .into(imagViewItem);
    }
}
