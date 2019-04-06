package com.example.talktoe;

//Resources:
//https://www.simplifiedcoding.net/android-recyclerview-cardview-tutorial/#RecyclerView-Item-Layout-using-CardView


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;


import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;



public class DisplayScoreBoard extends AppCompatActivity{

    RecyclerView recyclerView;
    UsersAdapter adapter;
    List<Users> usersList;
    DatabaseReference myRef;
    FirebaseDatabase database;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_scoreboard);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();



        usersList = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        //All values will be fetched from the Firebase database and saved into usersList.
        //usersList will be used to to create a RecyclerView to display the users and their score
        myRef = FirebaseDatabase.getInstance().getReference("users/");
        myRef.orderByKey().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Users newUsers = dataSnapshot.getValue(Users.class);
                usersList.add(newUsers);


                createLayout();



            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.d("ERROR TAG", "Failed to read value.", error.toException());
            }








           // @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

          //  @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

          //  @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }


        });




    }




    //createLayout will be called after all users have been fetched from the database and stored in the usersList.
    //This will be used to create a recycler view
    public void createLayout(){

        adapter = new UsersAdapter(this, usersList);
        recyclerView.setAdapter(adapter);


    };

    //playAgain button allows the user to return to the MainActivity to start another game
    public void playAgain(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }





}

