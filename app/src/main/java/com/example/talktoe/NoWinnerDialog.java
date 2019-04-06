package com.example.talktoe;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class NoWinnerDialog extends DialogFragment {


    //If there is no winner at the end of the game a dialog will be displayed to the user
    //And they will be brought to the scoreboard screen
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Neither player has won this game!")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(getContext(), DisplayScoreBoard.class);
                        startActivity(intent);
                    }
                });


        return builder.create();
    }


}
