package com.example.myappsspgdmanagev1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //we will use these constants later to pass the artist name and id to another activity
    public static final String ARTIST_NAME = "net.simplifiedcoding.firebasedatabaseexample.artistname";
    public static final String ARTIST_ID = "net.simplifiedcoding.firebasedatabaseexample.artistid";
    private static final int CHOOSE_IMAGE = 1;


    //view objects
    EditText editTextName, editTextJob;
    Spinner spinnerGenre;
    Button buttonAddArtist;
    Button chooseImage;
    ImageView imgPreview;
    Uri imgUrl;

    ArtistList artistAdapter;

    //a list to store all the artist from firebase database
    List<Artist> artists = new ArrayList<>();


    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;

    private StorageTask mUploadTask;

    Artist artist = new Artist();

    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findView();

        //getting the reference of artists node
        // data
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");
        //Picture
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");

        //list to store artists
        /*artists = new ArrayList<>();*/

    }

    private void showFileChoose() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, CHOOSE_IMAGE);

    }

    // get data image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imgUrl = data.getData();

            Picasso.with(this).load(imgUrl).into(imgPreview);
        }

    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void addArtist() {
        //getting the values to save
        final String name = editTextName.getText().toString().trim();
        final String job = editTextJob.getText().toString().trim();
        final String genre = spinnerGenre.getSelectedItem().toString();

        /*if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(job)) {*/

            if (imgUrl != null) {
                final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                        + "." + getFileExtension(imgUrl));

                mUploadTask = fileReference.putFile(imgUrl)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                Artist upload = new Artist(name, job, genre, uri.toString());

                                // set id to database
                                String uploadID = mDatabaseRef.push().getKey();
                                mDatabaseRef.child(uploadID).setValue(upload);
                                Toast.makeText(MainActivity.this, "Upload successfully / key : "+uploadID, Toast.LENGTH_LONG).show();
                                imgPreview.setImageResource(R.drawable.imagepreview);
                                editTextName.setText("");
                                editTextJob.setText("");
                            }
                        });


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                Toast.makeText(MainActivity.this, "No file selected", Toast.LENGTH_SHORT).show();
            }

        /*} else {
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show();
        }*/

    }

    private void findView() {
        //getting views
        editTextName = findViewById(R.id.editTextName);
        editTextJob = findViewById(R.id.editTextJob);
        spinnerGenre = findViewById(R.id.spinnerGenres);
        //listViewArtists = findViewById(R.id.listViewArtists);
        buttonAddArtist = findViewById(R.id.buttonAddArtist);
        chooseImage = findViewById(R.id.chooseImage);
        imgPreview = findViewById(R.id.imgPreview);

    }

    // choose Image
    public void btnChooseImage(View view) {
        showFileChoose();
    }

    // add Artist
    public void btnAddArtist(View view) {

        if (mUploadTask != null && mUploadTask.isInProgress()) {
            //Toast.makeText(MainActivity.this, "Upload in progress", Toast.LENGTH_LONG).show();
        } else {
            addArtist();
        }

    }

    public void btnShowData(View view) {
        Intent i = new Intent(MainActivity.this,ShowImageActivity.class);
        startActivity(i);
    }

    // Show data list
    /*@Override
    protected void onStart() {
        super.onStart();
        //attaching value event listener
        databaseArtists.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clearing the previous artist list
                artists.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting artist
                    Artist artist = postSnapshot.getValue(Artist.class);
                    //adding artist to the list
                    artists.add(artist);
                }

                //creating adapter
                ArtistList artistAdapter = new ArtistList(MainActivity.this, artists);
                //attaching adapter to the listview
                listViewArtists.setAdapter(artistAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }*/


    /*@Override
    protected void onStart() {
        super.onStart();

        mRecyclerView=findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // ชี้ไปที่ Folder
        databaseArtists=FirebaseDatabase.getInstance().getReference("uploads");
        databaseArtists.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot:dataSnapshot.getChildren())
                {
                    Artist myModel=postSnapshot.getValue(Artist.class);
                    artists.add(myModel);
                }
                mAdapter=new ImageAdapter(getApplicationContext(), artists);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }*/
}
