package com.example.talktoe;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.media.MediaRecorder.AudioSource.VOICE_RECOGNITION;

public class Board extends AppCompatActivity {

    Button speechButton;
    String speechResult;
    ArrayList<String> speechOutput;
    TextView speechOutputTextView;
    String result;
    Button topLeft, topMiddle, topRight, centerLeft, centerMiddle, centerRight, bottomLeft, bottomMiddle, bottomRight;
    String playerToggle;
    static String playerOne, playerTwo;
    static String nextPlayer;
    String winnerResults;
    TextView playerOneName, playerTwoName;
    String winnerName;

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






        speechOutput = new ArrayList<String>();

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

        checkBoard();
        switchPlayer();





        }
    }

    public void checkBoard(){

        //check horizontally for a winner
        for(int i = 0; i < 3; i++) {
            if (boardStatus[i][0].equals(boardStatus[i][1]) && boardStatus[i][1].equals(boardStatus[i][2])) {
                if (boardStatus[i][0].equals("X")) {
                    winnerResults = playerOne + " has won the game!";
                    break;
                } else {
                    winnerResults = playerTwo + " has won the game!";
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
// Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myDatabase = database.getReference();

        myDatabase = FirebaseDatabase.getInstance().getReference();



        Users user = new Users(winnerName);





        myDatabase.child("name" + winnerName).setValue(user);

        Toast.makeText(this, "Written to database!", Toast.LENGTH_SHORT).show();
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




}
