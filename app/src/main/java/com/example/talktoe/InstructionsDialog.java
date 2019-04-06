package com.example.talktoe;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class InstructionsDialog extends DialogFragment {

     //Create a dialog to instruct the user how to play the game.
    //After exit on the dialog they will be returned to the game.
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("To play this game click the spot to play or click the SPEAK button and give the command for the row (top, center, bottom) and the column (left, middle, right)")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });


        return builder.create();
    }


}
