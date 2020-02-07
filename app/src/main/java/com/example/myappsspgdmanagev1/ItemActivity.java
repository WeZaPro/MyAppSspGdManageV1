package com.example.myappsspgdmanagev1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

    EditText editText;
    ImageView imagViewItem;
    Button btnUpdate;
    Artist myModel;
    String clubkey;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference mDatabaseRef = firebaseDatabase.getReference("uploads");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        imagViewItem = findViewById(R.id.imagViewItem);
        editText = findViewById(R.id.editText);
        btnUpdate = findViewById(R.id.btnUpdate);

        myModel = getIntent().getExtras().getParcelable("name");
        editText.setText(myModel.getArtistName());


        Picasso.with(this)
                .load(myModel.getImgUrl())
                .placeholder(R.drawable.imagepreview)
                .fit()
                .centerCrop()
                .into(imagViewItem);


        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Artist myModel = postSnapshot.getValue(Artist.class);
                    clubkey = postSnapshot.getKey();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void onclickUpdate(View view) {

        updateArtist(editText.getText().toString(),clubkey);

        //Toast.makeText(ItemActivity.this, "key : " + clubkey, Toast.LENGTH_SHORT).show();

    }

    private boolean updateArtist(String name, String idName) {
        //getting the specified artist reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("uploads").child(idName);

        //updating artist
        Artist artist = new Artist(name,idName);
        dR.setValue(artist);
        Toast.makeText(getApplicationContext(), "Artist Updated", Toast.LENGTH_LONG).show();
        return true;
    }
}
