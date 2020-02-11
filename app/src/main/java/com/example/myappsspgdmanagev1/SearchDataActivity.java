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

        final String searchName = editTextSearch.getText().toString().trim();

        DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");

        // Search ข้อมูลที่ Child artistName โดยรับชื่อที่เช็คมาจาก Keyboard
        Query query = mDatabaseRef.orderByChild("artistName").equalTo(searchName);

        // Query data
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // เช็คข้อมูลว่าตรงกันหรือไม่ ****
                if (dataSnapshot.getChildrenCount() == 0) {
                    Toast.makeText(SearchDataActivity.this, " No Data from you want ", Toast.LENGTH_SHORT).show();

                    textViewShowDataName.setText("");
                    textViewShowDataJob.setText("");
                    imageView.setImageResource(R.drawable.imagepreview);

                } else {

                    for (DataSnapshot data : dataSnapshot.getChildren()) {

                        // Get data ไปที่ Model Artist
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
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
