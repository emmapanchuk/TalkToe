package com.example.talktoe;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class WinnerDialog extends DialogFragment {

    //Once a win condition has been met the screen will display the name of the winner and update the scoreboard.
    //The user will be then shown the Scoreboard activity with the updated scoreboard.
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(Board.winnerResults)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(getContext(), DisplayScoreBoard.class);
                        startActivity(intent);
                    }
                });



        return builder.create();
    }


}
