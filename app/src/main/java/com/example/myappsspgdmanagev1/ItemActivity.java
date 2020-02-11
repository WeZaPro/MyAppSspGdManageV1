package com.example.myappsspgdmanagev1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ItemActivity extends AppCompatActivity {

    EditText editTextName, editTextJob, editTextGenre, editTextUrl, editTextID;
    ImageView imagViewItem;
    Button btnUpdate;
    Artist myModel;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        imagViewItem = findViewById(R.id.imagViewItem);
        editTextName = findViewById(R.id.editTextName);
        editTextJob = findViewById(R.id.editTextJob);
        editTextGenre = findViewById(R.id.editTextGenre);
        btnUpdate = findViewById(R.id.btnUpdate);

        myModel = getIntent().getExtras().getParcelable("name");
        editTextName.setText(myModel.getArtistName());
        editTextJob.setText(myModel.getArtistJob());
        editTextGenre.setText(myModel.getArtistGenre());

        Picasso.with(this)
                .load(myModel.getImgUrl())
                .placeholder(R.drawable.imagepreview)
                .fit()
                .centerCrop()
                .into(imagViewItem);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updateArtist(myModel.getIdname(),
                        editTextName.getText().toString(),
                        editTextJob.getText().toString(),
                        editTextGenre.getText().toString(),
                        myModel.getImgUrl());

                Intent i = new Intent(ItemActivity.this, ShowImageActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void updateArtist(String id, String name, String job, String genre, String url) {

        databaseReference= FirebaseDatabase.getInstance().getReference("uploads").child(id);

        Artist artist = new Artist(id, name, job, genre, url);
        databaseReference.setValue(artist);

        Toast.makeText(getApplicationContext(), "Artist Updated", Toast.LENGTH_LONG).show();
    }
}



