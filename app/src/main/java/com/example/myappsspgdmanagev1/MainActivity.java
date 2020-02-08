package com.example.myappsspgdmanagev1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
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
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //we will use these constants later to pass the artist name and id to another activity
    public static final String ARTIST_NAME = "net.simplifiedcoding.firebasedatabaseexample.artistname";
    public static final String ARTIST_ID = "net.simplifiedcoding.firebasedatabaseexample.artistid";
    private static final int CHOOSE_IMAGE = 1;
    private static final int CAMERA_REQUEST = 1001;


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

        if (requestCode == CAMERA_REQUEST) {

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imgUrl);
                imgPreview.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
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

                                    String uploadID = mDatabaseRef.push().getKey();

                                    Artist upload = new Artist(uploadID, name, job, genre, uri.toString());

                                    mDatabaseRef.child(uploadID).setValue(upload);

                                    // set id to database
                                    Toast.makeText(MainActivity.this, "Upload successfully / key : " + uploadID, Toast.LENGTH_LONG).show();
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
        Intent i = new Intent(MainActivity.this, ShowImageActivity.class);
        startActivity(i);
    }

    public void btnChooseCamera(View view) {

        openCamera();
    }

    private void openCamera() {

        Dexter.withActivity(this).withPermissions(Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(new MultiplePermissionsListener() {

            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (report.areAllPermissionsGranted()) {
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.TITLE, "New Picture");
                    values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera");
                    imgUrl = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imgUrl);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                } else {
                    Toast.makeText(MainActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).check();
    }

    public void btnSearchData(View view) {
        Intent i = new Intent(MainActivity.this,SearchDataActivity.class);
        startActivity(i);
    }
}
