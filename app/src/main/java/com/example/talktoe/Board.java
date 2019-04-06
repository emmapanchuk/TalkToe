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

    String speechResult, playerToggle,winnerName;
    String[][] boardStatus = {{"a", "b", "c"}, {"d", "e","f"}, {"g","h","i"}};
    ArrayList<String> speechOutput;
    TextView playerOneName, playerTwoName;;
    String result;
    Button speechButton, topLeft, topMiddle, topRight, centerLeft,
            centerMiddle, centerRight, bottomLeft, bottomMiddle, bottomRight, scoreboardButton, instructionsButton;
    static String playerOne, playerTwo, nextPlayer, winnerResults;

    private DatabaseReference mDatabase, userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board);

        //Retrieved stored user names from MainActivity and display them on the screen.
        //If users have not provided names assign them default names of Player One and Player Two
        Intent intent = getIntent();
        playerOne = intent.getStringExtra("PlayerOne");
        playerTwo = intent.getStringExtra("PlayerTwo");

        playerOneName = findViewById(R.id.playerOneName);
        playerOneName.setText(playerOne);
        if(playerOne.length() == 0){
            playerOneName.setText("Player One");
            playerOne = "Player One";
        }

        playerTwoName = findViewById(R.id.playerTwoName);
        playerTwoName.setText(playerTwo);
        if(playerTwo.length() == 0){
            playerTwoName.setText("Player Two");
            playerTwo = "Player Two";
        }

        //Use nextPlayer to track who's turn it is.
        //By default, it will be player One's turn first
        nextPlayer = playerTwo;

        //Player toggle is used to determine if an X or an O should be placed
        //By default, an X will be placed first
        playerToggle = "X";


        speechButton = findViewById(R.id.speechButton);
        instructionsButton = findViewById(R.id.instructionsButton);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        winnerName = "";




        topLeft = findViewById(R.id.topLeft);
        topMiddle = findViewById(R.id.topMiddle);
        topRight = findViewById(R.id.topRight);
        centerLeft = findViewById(R.id.centerLeft);
        centerMiddle = findViewById(R.id.centerMiddle);
        centerRight = findViewById(R.id.centerRight);
        bottomLeft = findViewById(R.id.bottomLeft);
        bottomMiddle = findViewById(R.id.bottomMiddle);
        bottomRight = findViewById(R.id.bottomRight);





        speechOutput = new ArrayList<String>();

        //If the user clicks the instructions button they will be shown a dialog
        //with instructions on how to play the game
        instructionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InstructionsDialog dialog = new InstructionsDialog();
                dialog.show(getSupportFragmentManager(), "Instructions");

            }
        });

        //If the user clicks the scoreboard button they will be
        //brought to the DisplayScoreBoard activity
        scoreboardButton = findViewById(R.id.scoreboardButton);
        scoreboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewScoreboard();
            }
        });

        //If the user clicks the speak button they will be prompted to speak to the device
        //on which position they are selecting
        speechButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak();
            }
        });
    }

    //If the user clicks different buttons they will play their character at that button.
    //Switch cases are used for each of the buttons.
    //After playing a position the board will be checked for any win conditions.
    //If there are no win conditions the next user will be prompted to play.
    public void onClick(View v){
        switch(v.getId())
        {
            case R.id.topLeft:
                if(boardStatus[0][0].equals("a")){
                    topLeft.setText(playerToggle);
                    boardStatus[0][0] = playerToggle;
                    checkBoard();
                    switchPlayer();
                }
                break;
            case R.id.topMiddle:
                if(boardStatus[0][1].equals("b")){
                    topMiddle.setText(playerToggle);
                    boardStatus[0][1] = playerToggle;
                    checkBoard();
                    switchPlayer();
                }
                break;
            case R.id.topRight:
                if(boardStatus[0][2].equals("c")){
                    topRight.setText(playerToggle);
                    boardStatus[0][2] = playerToggle;
                    checkBoard();
                    switchPlayer();
                }
                break;
            case R.id.centerLeft:
                if(boardStatus[1][0].equals("d")){
                    centerLeft.setText(playerToggle);
                    boardStatus[1][0] = playerToggle;
                    checkBoard();
                    switchPlayer();
                }
                break;
            case R.id.centerMiddle:
                if(boardStatus[1][1].equals("e")){
                    centerMiddle.setText(playerToggle);
                    boardStatus[1][1] = playerToggle;
                    checkBoard();
                    switchPlayer();
                }
                break;
            case R.id.centerRight:
                if(boardStatus[1][2].equals("f")){
                    centerRight.setText(playerToggle);
                    boardStatus[1][2] = playerToggle;
                    checkBoard();
                    switchPlayer();
                }
                break;
            case R.id.bottomLeft:
                if(boardStatus[2][0].equals("g")) {
                    bottomLeft.setText(playerToggle);
                    boardStatus[2][0] = playerToggle;
                    checkBoard();
                    switchPlayer();
                }
                break;
            case R.id.bottomMiddle:
                if(boardStatus[2][1].equals("h")){
                    bottomMiddle.setText(playerToggle);
                    boardStatus[2][1] = playerToggle;
                    checkBoard();
                    switchPlayer();
                }
                break;
            case R.id.bottomRight:

                if(boardStatus[2][2].equals("i")){
                    bottomRight.setText(playerToggle);
                    boardStatus[2][2] = playerToggle;
                    checkBoard();
                    switchPlayer();
                }
                break;
        }


    }

    //Speak function will be called by the SPEAK button listener.
    //This will allow the user to speak their instructions to the device.
    //Results are passed to onActivityResult.
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

    //After the speak function has ended the results will be returned to onActivityResult.
    //The results will be matched to a position on the board.
    //If the results were unclear or if the position is already taken the user will be prompted
    //To repeat the speaking step.
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            for (int i = 0; i < results.size(); i++) {
                result = results.get(i).toLowerCase();
                Log.d("SpeechResults", result);
                speechOutput.add(result);






            }
        if(result.contains("top") && result.contains("left") && boardStatus[0][0].equals("a")){
                topLeft.setText(playerToggle);
                boardStatus[0][0] = playerToggle;
                checkBoard();
                switchPlayer();
        }

        else if(((result.contains("top") && result.contains("mid")) || (result.contains("top") && result.contains("mod")) ||
                (result.contains("talk") && ((result.contains("mid") || result.contains("mod")))) ||
                result.contains("tau") &&((result.contains("mid") || result.contains("mod"))) )
                && boardStatus[0][1].equals("b")){
                topMiddle.setText(playerToggle);
                boardStatus[0][1] = playerToggle;
                checkBoard();
                switchPlayer();
        }

        else if(result.contains("top") && (result.contains("right") || result.contains("write") || result.contains("rat") || result.contains("red"))
                && boardStatus[0][2].equals("c")){
            topRight.setText(playerToggle);
            boardStatus[0][2] = playerToggle;
            checkBoard();
            switchPlayer();
        }

        else if((result.contains("cent") && result.contains("left")) ||
                (result.contains("sen") && result.contains("left"))
                && boardStatus[1][0].equals("d")){
            centerLeft.setText(playerToggle);
            boardStatus[1][0] = playerToggle;
            checkBoard();
            switchPlayer();
        }

        else if((result.contains("cent") && (result.contains("mid") || result.contains("mod"))) ||
                (result.contains("sen") && (result.contains("mid") || result.contains("mod"))
                        && boardStatus[1][1].equals("e"))){
            centerMiddle.setText(playerToggle);
            boardStatus[1][1] = playerToggle;
            checkBoard();
            switchPlayer();
        }

        else if((result.contains("cent") && (result.contains("right") || result.contains("write") || result.contains("rat") || result.contains("red")) ||
                (result.contains("sen") && (result.contains("right") || result.contains("write") || result.contains("rat") || result.contains("red"))))
                && boardStatus[1][2].equals("f")){
            centerRight.setText(playerToggle);
            boardStatus[1][2] = playerToggle;
            checkBoard();
            switchPlayer();
        }

        else if(((result.contains("bot") && result.contains("left") ) || (result.contains("bat") && result.contains("left"))) && boardStatus[2][0].equals("g")){
            bottomLeft.setText(playerToggle);
            boardStatus[2][0] = playerToggle;
            checkBoard();
            switchPlayer();
        }

        else if(result.contains("bot") && (result.contains("mid") || result.contains("mod"))
                && boardStatus[2][1].equals("h")){
            bottomMiddle.setText(playerToggle);
            boardStatus[2][1] = playerToggle;
            checkBoard();
            switchPlayer();
        }

        else if(result.contains("bot") && (result.contains("right") || result.contains("write") || result.contains("rat") || result.contains("red")) && boardStatus[2][2].equals("i")){
            bottomRight.setText(playerToggle);
            boardStatus[2][2] = playerToggle;
            checkBoard();
            switchPlayer();
        }

        //If the command was not understandable the user will be asked to repeat their command.
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

    //After each move checkBoard will be run to check if any win conditions have been met.
    //The following resource was referenced for this function:
    //https://medium.com/wiselteach/tic-tac-toe-tablelayout-android-app-androidmonk-a56b9e1c6a15
    public void checkBoard(){

        //check horizontally for a winner
        for(int i = 0; i < 3; i++) {
            if (boardStatus[i][0].equals(boardStatus[i][1]) && boardStatus[i][1].equals(boardStatus[i][2])) {
                if (boardStatus[i][0].equals("X")) {
                    winnerResults = playerOne + " has won the game!";
                    winnerName = playerOne;
                    userExists();

                    break;
                } else {
                    winnerResults = playerTwo + " has won the game!";
                    winnerName = playerTwo;
                    userExists();

                    break;

                }
            }


        }

        //check vertically for a winner
        for(int i = 0; i < 3; i++) {
            if (boardStatus[0][i].equals(boardStatus[1][i]) && boardStatus[1][i].equals(boardStatus[2][i])) {
                if (boardStatus[0][i].equals("X")) {
                    winnerResults = playerOne + " has won the game!";
                    winnerName = playerOne;
                    userExists();

                    break;
                } else {
                    winnerResults = playerTwo + " has won the game!";
                    winnerName = playerTwo;
                    userExists();

                    break;
                }
            }
        }

        //diagonally

        if((boardStatus[0][0].equals(boardStatus[1][1])) && (boardStatus[0][0].equals(boardStatus[2][2]))){

            if (boardStatus[0][0].equals("X")) {
                winnerResults = playerOne + " has won the game!";
                winnerName = playerOne;
                userExists();


            }
            else {
                winnerResults = playerTwo + " has won the game!";
                winnerName = playerTwo;
                userExists();


            }

        }

         //diagonally
        if (boardStatus[0][2].equals(boardStatus[1][1]) && (boardStatus[0][2].equals(boardStatus[2][0]))) {

            if (boardStatus[0][2].equals("X")) {
                winnerResults = playerOne + " has won the game!";
                winnerName = playerOne;
                userExists();

            } else {
                winnerResults = playerTwo + " has won the game!";
                winnerName = playerTwo;
                userExists();

            }
        }

        //If all the locations on the board have been played but no win conditions have been met
        //the game will end without a winner
        if(boardStatus[0][0] != "a" && boardStatus[0][1] != "b" && boardStatus[0][2] != "c" &&
                boardStatus[1][0] != "d" && boardStatus[1][1] != "e" && boardStatus[1][2] != "f" &&
                boardStatus[2][0] != "g" && boardStatus[2][1] != "h" && boardStatus[2][2] != "i"){
            NoWinnerDialog dialog = new NoWinnerDialog();
            dialog.show(getSupportFragmentManager(), "NoWinner");
        }




    }

    //After each turn has finished and no win condition has been met
    //switchPlayer will be used to toggle between the two players.
    //A dialog will be shown to direct the user to hand the device to the other user.
    public void switchPlayer(){

        if(playerToggle.equals("X")){
            playerToggle = "O";
        }

        else{
            playerToggle = "X";
        }

        SwitchDialog dialog = new SwitchDialog();
        dialog.show(getSupportFragmentManager(), "nextUser");
    }

    //viewScoreboard is called to start the DisplayScoreBoard activity.
    //It can be called by clicking the SCOREBOARD button or by completing a game,
    //whether there is a winner or not.
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

    //If a user wins the game and they do not yet exist in the database
    //They will be saved to the Firebase database with a score of 1.
    public void createNewUser(){
        Users user = new Users(winnerName);
        mDatabase.child("users").child(winnerName).setValue(user);
        Log.d("DB", "Writing new user " + winnerName + " to the database");

        Toast.makeText(this, "Scoreboard has been updated!", Toast.LENGTH_SHORT).show();

        endGame();



    }

    //If a user wins the game and already exists in the database
    //their score will be fetched from the Firebase database and incremented by 1
    //before being stored back to the database
    public void updateUser(){

        Log.d("ACCESS", "In update");

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

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

        endGame();

    }

    //If a win condition is met a dialog will be shown to announce the winner.
    //Afterwards, the scoreboard will be displayed to the user.
    public void endGame(){

        WinnerDialog dialog = new WinnerDialog();
        dialog.show(getSupportFragmentManager(), "winner");




    }





}
