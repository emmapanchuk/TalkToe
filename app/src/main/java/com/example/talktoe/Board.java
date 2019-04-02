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
    String playerOne, playerTwo;
    String winnerResults;

    String[][] boardStatus = new String[3][3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board);
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
                result = results.get(i);
                Log.i("SpeechDemo", "## INFO 05: Result: " + result );
                speechOutput.add(result);
                speechOutputTextView.setText(result);




            }
        if(result.contains("top") && result.contains("left")){
                topLeft.setText(playerToggle);
                boardStatus[0][0] = playerToggle;
        }

        else if(result.contains("top") && result.contains("middle")){
                topMiddle.setText(playerToggle);
                boardStatus[0][1] = playerToggle;
        }

        else if(result.contains("top") && result.contains("right")){
            topRight.setText(playerToggle);
            boardStatus[0][2] = playerToggle;
        }

        else if(result.contains("center") && result.contains("left")){
            centerLeft.setText(playerToggle);
            boardStatus[1][0] = playerToggle;
        }

        else if(result.contains("center") && result.contains("middle")){
            centerMiddle.setText(playerToggle);
            boardStatus[1][1] = playerToggle;
        }

        else if(result.contains("center") && result.contains("right")){
            centerRight.setText(playerToggle);
            boardStatus[1][2] = playerToggle;
        }

        else if(result.contains("bottom") && result.contains("left")){
            bottomLeft.setText(playerToggle);
            boardStatus[2][0] = playerToggle;
        }

        else if(result.contains("bottom") && result.contains("middle")){
            bottomMiddle.setText(playerToggle);
            boardStatus[2][1] = playerToggle;
        }

        else if(result.contains("bottom") && result.contains("right")){
            bottomRight.setText(playerToggle);
            boardStatus[2][2] = playerToggle;
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

        //check if the newly placed piece allowed a player to win
        checkBoard();





        }
    }

    public void checkBoard(){


        for(int i = 0; i < 3; i++){
            if(boardStatus[i][0].equals(boardStatus[i][1])  && boardStatus[i][1].equals(boardStatus[i][2])){
                if(boardStatus[i][0].equals("X")){
                    winnerResults = playerOne + " has won the game!";
                }
                else{
                    winnerResults = playerTwo + " has won the game!";
                }
            }


        //check horizontally for a winner


        }
    }
}
