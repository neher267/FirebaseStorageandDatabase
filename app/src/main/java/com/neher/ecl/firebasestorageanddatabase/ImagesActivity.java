package com.neher.ecl.firebasestorageanddatabase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ImagesActivity extends AppCompatActivity implements ImageAdapter.OnItemClickListener{

    private static final String TAG = "ImagesActivity";

    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBarCircle;

    private DatabaseReference mDatabaseRefer;
    private FirebaseStorage mFirebaseStorage;
    private List<Upload> uploads;
    private ImageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);

        mProgressBarCircle = findViewById(R.id.progress_bar_circle);
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));



        uploads = new ArrayList<>();

        adapter = new ImageAdapter(ImagesActivity.this, uploads);
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(ImagesActivity.this);

        mDatabaseRefer = FirebaseDatabase.getInstance().getReference("uploads");
        mFirebaseStorage = FirebaseStorage.getInstance();

        mDatabaseRefer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                uploads.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    upload.setKey(postSnapshot.getKey());
                    uploads.add(upload);
                }

                adapter.notifyDataSetChanged();
                mProgressBarCircle.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ImagesActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
                mProgressBarCircle.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(this, "onItemClick at position: "+position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onWhatEverClick(int position) {
        Toast.makeText(this, "onWhatEverClick at position: "+position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteClick(int position) {
        Upload upload = uploads.get(position);

        String dbKey = upload.getKey();
        StorageReference imageRef = mFirebaseStorage.getReferenceFromUrl(upload.getImageUrl());
        imageRef.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ImagesActivity.this, "Delete success!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
