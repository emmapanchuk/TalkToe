//resources:
//
//https://stacktips.com/tutorials/android/speech-to-text-in-android
//https://medium.com/wiselteach/tic-tac-toe-tablelayout-android-app-androidmonk-a56b9e1c6a15

package com.example.talktoe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    Button playButton;
    String playerOne, playerTwo;
    EditText playerOneInput, playerTwoInput;
    ImageView animImage;
    Animation animations;
    AnimationSet animationSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        playButton = findViewById(R.id.playButton);


        animationSet = new AnimationSet(true);

        animImage = findViewById(R.id.animImage);
        animations = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.movein);
        animationSet.addAnimation(animations);
        animations = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotateclockwise);
        animationSet.addAnimation(animations);
        animations = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotatecounterclockwise);
        animationSet.addAnimation(animations);
        animations = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomin);
        animationSet.addAnimation(animations);
        animations = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomout);
        animationSet.addAnimation(animations);
        animations = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoombackout);
        animationSet.addAnimation(animations);
        animImage.startAnimation(animationSet);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.v("Testing:", "start game method called");
                startGame();
            }
        });

    }

    public void startGame() {
        Log.v("BOARD:", "start game activity called");
        playerOneInput = findViewById(R.id.playerOneInput);
        playerTwoInput = findViewById(R.id.playerTwoInput);
        playerOne = playerOneInput.getText().toString();
        playerTwo = playerTwoInput.getText().toString();


        Intent intent = new Intent(this, Board.class);
        intent.putExtra("PlayerOne", playerOne);
        intent.putExtra("PlayerTwo", playerTwo);
        startActivity(intent);
    }


}
