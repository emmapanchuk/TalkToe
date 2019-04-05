package com.example.talktoe;


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



        //myRef = FirebaseDatabase.getInstance().getReference();
        myRef.orderByKey().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Users newUsers = dataSnapshot.getValue(Users.class);
                Log.v("RETRIEVE", "Users name = " + newUsers.getName() + "\n Score value = "  );
                usersList.add(newUsers);

                Log.d("LENGTH","" + usersList.size());




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





    public void createLayout(){

        adapter = new UsersAdapter(this, usersList);
        recyclerView.setAdapter(adapter);


    };

    public void playAgain(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }





}

