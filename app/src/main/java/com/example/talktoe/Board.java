//resources:
//
//https://stacktips.com/tutorials/android/speech-to-text-in-android
//https://medium.com/wiselteach/tic-tac-toe-tablelayout-android-app-androidmonk-a56b9e1c6a15

package com.example.talktoe;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.media.MediaRecorder.AudioSource.VOICE_RECOGNITION;
import static java.lang.Integer.parseInt;

public class Board extends AppCompatActivity {

    Button speechButton;
    String speechResult;
    ArrayList<String> speechOutput;
    TextView speechOutputTextView;
    String result;
    Button topLeft, topMiddle, topRight, centerLeft, centerMiddle, centerRight, bottomLeft, bottomMiddle, bottomRight, scoreboardButton;
    String playerToggle;
    static String playerOne, playerTwo;
    static String nextPlayer;
    String winnerResults;
    TextView playerOneName, playerTwoName;
    String winnerName;
    private DatabaseReference mDatabase, userRef;
    boolean existsResult;
    DatabaseReference myRef;

    String[][] boardStatus = {{"a", "b", "c"}, {"d", "e","f"}, {"g","h","i"}};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board);

        Intent intent = getIntent();
        playerOne = intent.getStringExtra("PlayerOne");
        playerTwo = intent.getStringExtra("PlayerTwo");

        playerOneName = findViewById(R.id.playerOneName);
        playerOneName.setText(playerOne);

        playerTwoName = findViewById(R.id.playerTwoName);
        playerTwoName.setText(playerTwo);
        nextPlayer = playerTwo;

        speechOutputTextView = findViewById(R.id.speechOutputTextView);
        speechButton = findViewById(R.id.speechButton);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        winnerName = "PLEASEWORK";

        playerToggle = "X";


        topLeft = findViewById(R.id.topLeft);
        topMiddle = findViewById(R.id.topMiddle);
        topRight = findViewById(R.id.topRight);
        centerLeft = findViewById(R.id.centerLeft);
        centerMiddle = findViewById(R.id.centerMiddle);
        centerRight = findViewById(R.id.centerRight);
        bottomLeft = findViewById(R.id.bottomLeft);
        bottomMiddle = findViewById(R.id.bottomMiddle);
        bottomRight = findViewById(R.id.bottomRight);



        userExists();


        speechOutput = new ArrayList<String>();

        scoreboardButton = findViewById(R.id.scoreboardButton);
        scoreboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewScoreboard();
            }
        });

        speechButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak();
            }
        });
    }

    public String speak(){
        speechOutput.clear();
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Just speak normally into your phone");
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 10);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
        try {
            startActivityForResult(intent, VOICE_RECOGNITION);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }


        return speechResult;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        Log.i("SpeechDemo", "## INFO 02: RequestCode VOICE_RECOGNITION = " + requestCode);
        if (resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            for (int i = 0; i < results.size(); i++) {
                result = results.get(i).toLowerCase();
                Log.i("SpeechDemo", "## INFO 05: Result: " + result );
                speechOutput.add(result);
                speechOutputTextView.setText(result);




            }
        if(result.contains("top") && result.contains("left") && boardStatus[0][0].equals("a")){
                topLeft.setText(playerToggle);
                boardStatus[0][0] = playerToggle;
                checkBoard();
                switchPlayer();
        }

        else if(result.contains("top") && result.contains("middle")&& boardStatus[0][1].equals("b")){
                topMiddle.setText(playerToggle);
                boardStatus[0][1] = playerToggle;
                checkBoard();
                switchPlayer();
        }

        else if(result.contains("top") && result.contains("right")&& boardStatus[0][2].equals("c")){
            topRight.setText(playerToggle);
            boardStatus[0][2] = playerToggle;
            checkBoard();
            switchPlayer();
        }

        else if(result.contains("center") && result.contains("left")&& boardStatus[1][0].equals("d")){
            centerLeft.setText(playerToggle);
            boardStatus[1][0] = playerToggle;
            checkBoard();
            switchPlayer();
        }

        else if(result.contains("center") && result.contains("middle")&& boardStatus[1][1].equals("e")){
            centerMiddle.setText(playerToggle);
            boardStatus[1][1] = playerToggle;
            checkBoard();
            switchPlayer();
        }

        else if(result.contains("center") && result.contains("right")&& boardStatus[1][2].equals("f")){
            centerRight.setText(playerToggle);
            boardStatus[1][2] = playerToggle;
            checkBoard();
            switchPlayer();
        }

        else if(result.contains("bottom") && result.contains("left")&& boardStatus[2][0].equals("g")){
            bottomLeft.setText(playerToggle);
            boardStatus[2][0] = playerToggle;
            checkBoard();
            switchPlayer();
        }

        else if(result.contains("bottom") && result.contains("middle")&& boardStatus[2][1].equals("h")){
            bottomMiddle.setText(playerToggle);
            boardStatus[2][1] = playerToggle;
            checkBoard();
            switchPlayer();
        }

        else if(result.contains("bottom") && result.contains("right")&& boardStatus[2][2].equals("i")){
            bottomRight.setText(playerToggle);
            boardStatus[2][2] = playerToggle;
            checkBoard();
            switchPlayer();
        }

        else{

            Toast.makeText(this, "Please repeat your command.", Toast.LENGTH_SHORT).show();

            speechOutput.clear();
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "We couldn't understand your command. Please try again.");
            intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 10);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
            try {
                startActivityForResult(intent, VOICE_RECOGNITION);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            }
        }



        }
    }

    public void checkBoard(){

        //check horizontally for a winner
        for(int i = 0; i < 3; i++) {
            if (boardStatus[i][0].equals(boardStatus[i][1]) && boardStatus[i][1].equals(boardStatus[i][2])) {
                if (boardStatus[i][0].equals("X")) {
                    winnerResults = playerOne + " has won the game!";
                    storeWinnerResults();
                    break;
                } else {
                    winnerResults = playerTwo + " has won the game!";
                    storeWinnerResults();
                    break;

                }
            }


        }

        //check vertically for a winner
        for(int i = 0; i < 3; i++) {
            if (boardStatus[0][i].equals(boardStatus[1][i]) && boardStatus[1][i].equals(boardStatus[2][i])) {
                if (boardStatus[0][i].equals("X")) {
                    winnerResults = playerOne + " has won the game!";
                    break;
                } else {
                    winnerResults = playerTwo + " has won the game!";
                    break;
                }
            }
        }

        //diagonally

        if((boardStatus[0][0].equals(boardStatus[1][1])) && (boardStatus[0][0].equals(boardStatus[2][2]))){

            if (boardStatus[0][0].equals("X")) {
                winnerResults = playerOne + " has won the game!";

            }
            else {
                winnerResults = playerTwo + " has won the game!";

            }

        }

         //diagonally
        if (boardStatus[0][2].equals(boardStatus[1][1]) && (boardStatus[0][2].equals(boardStatus[2][0]))) {

            if (boardStatus[0][2].equals("X")) {
                winnerResults = playerOne + " has won the game!";
            } else {
                winnerResults = playerTwo + " has won the game!";
            }
        }




    }

    public void storeWinnerResults(){

      // boolean result = userExists();

        //userExists();
        //if the winner does not yet exist in the database, write them to the database
        if(true){
            Users user = new Users(winnerName);
            mDatabase.child("users").child(winnerName).setValue(user);
            Log.d("DB", "Writing new user " + winnerName + " to the database");

            Toast.makeText(this, "Scoreboard has been updated!", Toast.LENGTH_SHORT).show();


    }

        else{



            Log.d("ACCESS", "In update");
            userRef = FirebaseDatabase.getInstance().getReference("users/");

            userRef.addListenerForSingleValueEvent(new ValueEventListener(){
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    Users updateUser = dataSnapshot.getValue(Users.class);
                    Log.v("RETRIEVEinUPDATE", "Name value = " + updateUser.getName() + "Score value = " + updateUser.getScore());
                    String scoreString = updateUser.getScore();

                    int score = Integer.parseInt(scoreString);

                    score++;
                    updateUser.setScore(String.valueOf(score));
                    userRef.setValue(updateUser);

                }
                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.d("ERROR TAG", "Failed to read value.", error.toException());
                    }


                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }


                    //  @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }






            });

        }

    }
    public void switchPlayer(){

        if(playerToggle.equals("X")){
            playerToggle = "O";
        }

        else{
            playerToggle = "X";
        }

        SwitchDialog dialog = new SwitchDialog();
        dialog.show(getSupportFragmentManager(), "test");
    }

    public void viewScoreboard(){

        Intent intent = new Intent(this, DisplayScoreBoard.class);
        startActivity(intent);
    }

    // Check if the winner already exists in the database.
    //If the winner exists, return true.
    public void userExists(){

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userNameRef = rootRef.child("users").child(winnerName);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()) {
                   createNewUser();
                }
                else{
                    updateUser();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("TAG", databaseError.getMessage()); //Don't ignore errors!
            }
        };
        userNameRef.addListenerForSingleValueEvent(eventListener);


    }

    public void createNewUser(){
        Users user = new Users(winnerName);
        mDatabase.child("users").child(winnerName).setValue(user);
        Log.d("DB", "Writing new user " + winnerName + " to the database");

        Toast.makeText(this, "Scoreboard has been updated!", Toast.LENGTH_SHORT).show();



    }

    public void updateUser(){

        Log.d("ACCESS", "In update");

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        //DatabaseReference userRef = rootRef.child("users").child(winnerName);
        userRef = FirebaseDatabase.getInstance().getReference().child("users").child(winnerName);

        userRef.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Users updateUser = dataSnapshot.getValue(Users.class);
                Log.v("RETRIEVEinUPDATE", "Name value = " + updateUser.getName() + "Score value = " + updateUser.getScore());
                String scoreString = updateUser.getScore();

                int score = Integer.parseInt(scoreString);

                score++;
                updateUser.setScore(String.valueOf(score));
                userRef.setValue(updateUser);

            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.d("ERROR TAG", "Failed to read value.", error.toException());
            }


            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }


            //  @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }






        });

    }





}
