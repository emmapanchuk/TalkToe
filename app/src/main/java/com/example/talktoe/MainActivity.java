//https://medium.com/wiselteach/tic-tac-toe-tablelayout-android-app-androidmonk-a56b9e1c6a15

package com.example.talktoe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    Button playButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playButton = findViewById(R.id.playButton);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.v("Testing:", "start game method called");
                startGame();
            }
        });

    }

    public void startGame() {
        Log.v("Testing:", "start game activity called");
        Intent intent = new Intent(this, Board.class);
        startActivity(intent);
    }
}
