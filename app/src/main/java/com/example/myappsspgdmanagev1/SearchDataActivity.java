package com.example.myappsspgdmanagev1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class SearchDataActivity extends AppCompatActivity {

    EditText editTextSearch;
    TextView textViewShowDataName, textViewShowDataJob;
    Button btnSearch;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_data);

        editTextSearch = findViewById(R.id.editTextSearch);
        textViewShowDataName = findViewById(R.id.textViewShowDataName);
        textViewShowDataJob = findViewById(R.id.textViewShowDataJob);
        imageView = findViewById(R.id.imageView);
        btnSearch = findViewById(R.id.btnSearch);

    }

    public void conClickSearch(View view) {

        /*mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        query = mFirebaseDatabaseReference.child("uploads").orderByChild("artistName").equalTo("Taweesak");
        query.addValueEventListener(valueEventListener);

        ValueEventListener valueEventListener = new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren())
                {
                    //TODO get the data here

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        };*/

        final String searchName = editTextSearch.getText().toString().trim();

        DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");

        Query query = mDatabaseRef.orderByChild("artistName").equalTo(searchName);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot data : dataSnapshot.getChildren()) {

                    Artist models = data.getValue(Artist.class);
                    String name = models.getArtistName();
                    String job = models.getArtistJob();
                    String imageUrl = models.getImgUrl();

                    textViewShowDataName.setText(name);
                    textViewShowDataJob.setText(job);

                    Picasso.with(SearchDataActivity.this)
                            .load(imageUrl)
                            .placeholder(R.drawable.imagepreview)
                            .fit()
                            .centerCrop()
                            .into(imageView);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(SearchDataActivity.this,"Nodata ",Toast.LENGTH_SHORT).show();
            }
        });


    }
}
