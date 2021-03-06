package com.example.user.eran;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class profilePage extends AppCompatActivity {

    private static final String TAG = "ViewDatabase";

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private ImageView imageView;

    private FirebaseUser user;
    private TextView mfull_name;
    private TextView mdisplayed_name;
    private TextView memail_field;
    private TextView maddress;

    //private View bedit_info;

    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mfull_name = (TextView) findViewById(R.id.full_name);
        mdisplayed_name = (TextView) findViewById(R.id.displayed_name);
        memail_field = (TextView) findViewById(R.id.email_field);
        maddress = (TextView) findViewById(R.id.address);


        //bedit_info = findViewById(R.id.edit_info);

        imageView = (ImageView) findViewById(R.id.user_photo);

        //get firebase auth instance
        mAuth = FirebaseAuth.getInstance();

        mFirebaseDatabase = FirebaseDatabase.getInstance();

        myRef = mFirebaseDatabase.getReference();

        //get current user
        user = mAuth.getCurrentUser();

        userID = user.getUid();

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }

    private void showData(DataSnapshot dataSnapshot) {
        eranCustomer uInfo = new eranCustomer();
        dataSnapshot = dataSnapshot.child("customer").child(userID);

       // uInfo.set
        uInfo.setFname(dataSnapshot.child("FirstName").getValue().toString());
        uInfo.setLname(dataSnapshot.child("LastName").getValue().toString());
        uInfo.setCity(dataSnapshot.child("City").getValue().toString());
        uInfo.setStreet(dataSnapshot.child("Street").getValue().toString());
        uInfo.setUname(dataSnapshot.child("UserName").getValue().toString());

        uInfo.setImagePath(dataSnapshot.child("Photo").getValue().toString());


        Picasso.with(profilePage.this).load(uInfo.getImagePath()).into(imageView);
        mfull_name.setText("Full Name: " + uInfo.getFname() + " " + uInfo.getLname());
        mdisplayed_name.setText("User Name: " +uInfo.getUname());
        memail_field.setText("Email: " +user.getEmail());
        maddress.setText("Address: " +uInfo.getStreet() + ", " + uInfo.getCity());


    }
}



